package com.makolab.rnd.mammoth.validator.service;

import com.makolab.rnd.mammoth.core.model.chain.Block;
import com.makolab.rnd.mammoth.core.model.chain.BlockHeader;
import com.makolab.rnd.mammoth.core.service.cryptography.Sha256HashCalculator;
import com.makolab.rnd.mammoth.validator.model.BlockValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BlockHashValidator {

  private static final Logger LOGGER = LoggerFactory.getLogger(BlockHashValidator.class);

  private final Sha256HashCalculator hashCalculator;

  public BlockHashValidator() {
    this.hashCalculator = new Sha256HashCalculator();
  }

  public BlockValidationResult validateBlock(Block blockToValidate, String calculatedDataHash) {
    BlockHeader blockHeader = blockToValidate.getBlockHeader();

    StringBuilder sb = new StringBuilder();
    sb.append(blockHeader.getIndex());
    sb.append(blockHeader.getPreviousBlock());
    sb.append(blockHeader.getPreviousHash());
    sb.append(blockHeader.getTimestamp());
    sb.append(blockHeader.getDataGraphIri());
    sb.append(calculatedDataHash);

    String currentBlockHash = blockHeader.getHash();
    String calculatedBlockHash = hashCalculator.calculateHash(sb.toString());

    if (calculatedBlockHash.equals(currentBlockHash)) {
      return new BlockValidationResult(true, calculatedBlockHash, calculatedDataHash, blockHeader);
    } else {
      return new BlockValidationResult(false, calculatedBlockHash, calculatedDataHash, blockHeader);
    }
  }
}