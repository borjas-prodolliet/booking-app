package com.hostfully.bookingapp.block.domain;

import com.hostfully.bookingapp.block.application.CreateBlockRequest;
import com.hostfully.bookingapp.block.application.UpdateBlockRequest;
import com.hostfully.bookingapp.exception.AppException;
import com.hostfully.bookingapp.exception.AppExceptionDetail;
import com.hostfully.bookingapp.property.domain.Property;
import com.hostfully.bookingapp.property.domain.PropertyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.hostfully.bookingapp.shared.DateValidator.validateDateRange;

@Service
@Slf4j
public class BlockService {

    private final BlockDao blockDao;
    private final PropertyService propertyService;

    public BlockService(BlockDao blockDao, PropertyService propertyService) {
        this.blockDao = blockDao;
        this.propertyService = propertyService;
    }

    public Block createBlock(CreateBlockRequest request) {
        log.info("Creating block {}", request);

        validateDateRange(request.dateFrom(), request.dateTo());

        propertyService.verifyPropertyAvailability(
                request.propertyId(), request.dateFrom(), request.dateTo(), null, null);

        Property property = propertyService.getProperty(request.propertyId());

        Block block = Block.builder()
                .property(property)
                .dateFrom(request.dateFrom())
                .dateTo(request.dateTo())
                .build();

        return blockDao.create(block);
    }

    public Block updateBlock(UUID blockId, UpdateBlockRequest request) {
        log.info("Updating block {}", request);

        validateDateRange(request.dateFrom(), request.dateTo());

        Block block = getBlock(blockId);

        propertyService.verifyPropertyAvailability(
                block.getProperty().getId(), request.dateFrom(), request.dateTo(), null, blockId);

        boolean hasChanges = false;

        if (!block.getDateFrom().equals(request.dateFrom())) {
            hasChanges = true;
            block.setDateFrom(request.dateFrom());
        }

        if (!block.getDateTo().equals(request.dateTo())) {
            hasChanges = true;
            block.setDateTo(request.dateTo());
        }

        if (!hasChanges) {
            log.error("The block has no changes");
            throw new AppException(AppExceptionDetail.NO_CHANGES);
        }

        return blockDao.update(block);
    }

    public Block getBlock(UUID blockId) {
        log.info("Getting block {}", blockId);

        return blockDao.get(blockId).orElseThrow(() -> {
            log.error("Block not found: {}", blockId);
            return new AppException(AppExceptionDetail.BLOCK_NOT_FOUND);
        });
    }

    public void deleteBlock(UUID blockId) {
        log.info("Deleting block {}", blockId);

        Block block = getBlock(blockId);
        blockDao.delete(block);
    }
}
