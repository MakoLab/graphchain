package com.makolab.rnd.mammoth.validator.model;

import java.util.List;

public class GraphChainValidationResult {

  private final boolean valid;
  private final List<BlockValidationResult> blocks;

  public GraphChainValidationResult(boolean valid, List<BlockValidationResult> blocks) {
    this.valid = valid;
    this.blocks = blocks;
  }

  public boolean isValid() {
    return valid;
  }

  public List<BlockValidationResult> getBlocks() {
    return blocks;
  }

  public void addBlock(BlockValidationResult blockValidationResult) {
    blocks.add(blockValidationResult);
  }

  @Override
  public String toString() {
    return "GraphChainValidationResult{" +
        "valid=" + valid +
        ", blocks=" + blocks +
        '}';
  }
}
