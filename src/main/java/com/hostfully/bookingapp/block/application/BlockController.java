package com.hostfully.bookingapp.block.application;

import com.hostfully.bookingapp.block.domain.Block;
import com.hostfully.bookingapp.block.domain.BlockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/blocks")
public class BlockController {

    private final BlockService blockService;

    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @PostMapping
    public ResponseEntity<Block> createBlock(@RequestBody @Valid CreateBlockRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blockService.createBlock(request));
    }

    @PutMapping(value = "/{blockId}")
    public ResponseEntity<Block> updateBlock(@PathVariable UUID blockId, @RequestBody @Valid UpdateBlockRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(blockService.updateBlock(blockId, request));
    }

    @GetMapping(value = "/{blockId}")
    public ResponseEntity<Block> getBlock(@PathVariable UUID blockId) {
        return ResponseEntity.status(HttpStatus.OK).body(blockService.getBlock(blockId));
    }

    @DeleteMapping(value = "/{blockId}")
    public ResponseEntity<Void> deleteBlock(@PathVariable UUID blockId) {
        blockService.deleteBlock(blockId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
