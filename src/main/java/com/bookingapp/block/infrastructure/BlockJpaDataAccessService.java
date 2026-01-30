package com.bookingapp.block.infrastructure;

import com.bookingapp.block.domain.Block;
import com.bookingapp.block.domain.BlockDao;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BlockJpaDataAccessService implements BlockDao {

    private final BlockRepository blockRepository;

    public BlockJpaDataAccessService(BlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    @Override
    public Block create(Block block) {
        return blockRepository.save(block);
    }

    @Override
    public Optional<Block> get(UUID blockId) {
        return blockRepository.findById(blockId);
    }

    @Override
    public Block update(Block block) {
        return blockRepository.save(block);
    }

    @Override
    public void delete(Block block) {
        blockRepository.delete(block);
    }
}
