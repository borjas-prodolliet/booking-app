package com.hostfully.bookingapp.block.domain;

import com.hostfully.bookingapp.block.application.CreateBlockRequest;
import com.hostfully.bookingapp.block.application.UpdateBlockRequest;
import com.hostfully.bookingapp.exception.AppException;
import com.hostfully.bookingapp.exception.AppExceptionDetail;
import com.hostfully.bookingapp.property.domain.Property;
import com.hostfully.bookingapp.property.domain.PropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlockServiceTest {

    @Mock
    private BlockDao blockDao;
    @Mock
    private PropertyService propertyService;

    private BlockService blockService;

    @BeforeEach
    void setUp() {
        blockService = new BlockService(blockDao, propertyService);
    }

    @Test
    @DisplayName("Given a correct create block request, it should create a block")
    void createBlockSuccess() {
        UUID propertyId = UUID.randomUUID();
        LocalDate dateFrom = LocalDate.of(2026, 10, 21);
        LocalDate dateTo = LocalDate.of(2026, 10, 25);

        Property property = Property.builder()
                .id(propertyId)
                .name("Property Name")
                .address("Main street 123")
                .description("Property description")
                .checkInTime("15:00")
                .checkOutTime("11:00")
                .build();

        Block expected = Block.builder()
                .property(property)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build();

        ArgumentCaptor<Block> blockCaptor = ArgumentCaptor.forClass(Block.class);

        when(propertyService.getProperty(propertyId)).thenReturn(property);

        CreateBlockRequest request = new CreateBlockRequest(propertyId, dateFrom, dateTo);
        blockService.createBlock(request);
        verify(blockDao).create(blockCaptor.capture());
        assertEquals(expected, blockCaptor.getValue());
    }

    @ParameterizedTest
    @MethodSource("provideUpdateBlockFields")
    @DisplayName("Given a correct update block request, it should update a block")
    void updateBlockSuccess(LocalDate dateFrom, LocalDate dateTo) {
        UUID blockId = UUID.randomUUID();

        Property property = Property.builder()
                .id(UUID.randomUUID())
                .name("Property Name")
                .address("Main street 123")
                .description("Property description")
                .checkInTime("15:00")
                .checkOutTime("11:00")
                .build();

        Block block = Block.builder()
                .property(property)
                .dateFrom(LocalDate.of(2026, 10, 21))
                .dateTo(LocalDate.of(2026, 10, 25))
                .build();

        Block expected = Block.builder()
                .property(property)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build();

        ArgumentCaptor<Block> blockCaptor = ArgumentCaptor.forClass(Block.class);

        when(blockDao.get(blockId)).thenReturn(Optional.of(block));

        UpdateBlockRequest request = new UpdateBlockRequest(dateFrom, dateTo);
        blockService.updateBlock(blockId, request);
        verify(blockDao).update(blockCaptor.capture());
        assertEquals(expected, blockCaptor.getValue());
    }

    @Test
    @DisplayName("Given a update block request, when no changes, it should throw an exception")
    void updateBlockThrowsExceptionWhenNoChanges() {
        UUID blockId = UUID.randomUUID();
        LocalDate dateFrom = LocalDate.of(2026, 10, 21);
        LocalDate dateTo = LocalDate.of(2026, 10, 25);

        Property property = Property.builder()
                .id(UUID.randomUUID())
                .name("Property Name")
                .address("Main street 123")
                .description("Property description")
                .checkInTime("15:00")
                .checkOutTime("11:00")
                .build();

        Block block = Block.builder()
                .property(property)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build();

        when(blockDao.get(blockId)).thenReturn(Optional.of(block));

        UpdateBlockRequest request = new UpdateBlockRequest(dateFrom, dateTo);

        assertThatThrownBy(() -> blockService.updateBlock(blockId, request))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(AppExceptionDetail.NO_CHANGES.getMessage());
    }

    private static Stream<Arguments> provideUpdateBlockFields() {
        return Stream.of(
                Arguments.of(LocalDate.of(2026, 10, 21), LocalDate.of(2026, 10, 23)),
                Arguments.of(LocalDate.of(2026, 10, 22), LocalDate.of(2026, 10, 25))
        );
    }

    @Test
    @DisplayName("When deleting a block it should delete the booking")
    void deleteBookingSuccess() {
        UUID blockId = UUID.randomUUID();

        Block block = Block.builder()
                .dateFrom(LocalDate.of(2026, 10, 21))
                .dateTo(LocalDate.of(2026, 10, 25))
                .build();

        when(blockDao.get(blockId)).thenReturn(Optional.of(block));

        blockService.deleteBlock(blockId);

        verify(blockDao).delete(block);
    }
}