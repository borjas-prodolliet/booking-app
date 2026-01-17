package com.hostfully.bookingapp.block.domain;

import java.util.Optional;
import java.util.UUID;

public interface BlockDao {
    Block create(Block block);

    Optional<Block> get(UUID blockId);

    Block update(Block block);

    void delete(Block block);
}
