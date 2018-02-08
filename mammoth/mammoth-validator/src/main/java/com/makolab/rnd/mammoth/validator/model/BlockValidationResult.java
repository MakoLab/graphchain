package com.makolab.rnd.mammoth.validator.model;

import com.makolab.rnd.mammoth.core.model.chain.BlockHeader;

/**
 *
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
public class BlockValidationResult {

  private final boolean valid;
  private final String calculatedBlockHash;
  private final String calculatedDataHash;
  private final String currentBlockHash;
  private final String currentDataHash;

  public BlockValidationResult(boolean valid,
                               String calculatedBlockHash,
                               String calculatedDataHash,
                               BlockHeader blockHeader) {
    this.valid = valid;
    this.calculatedBlockHash = calculatedBlockHash;
    this.calculatedDataHash = calculatedDataHash;
    this.currentBlockHash = blockHeader.getHash();
    this.currentDataHash = blockHeader.getDataHash();
  }

  public boolean isValid() {
    return valid;
  }

  public String getCalculatedBlockHash() {
    return calculatedBlockHash;
  }

  public String getCalculatedDataHash() {
    return calculatedDataHash;
  }

  public String getCurrentBlockHash() {
    return currentBlockHash;
  }

  public String getCurrentDataHash() {
    return currentDataHash;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("BlockValidationResult{");
    sb.append("valid = ").append(isValid()).append(", \n");
    sb.append("calculatedBlockHash = ").append(getCalculatedBlockHash()).append(", \n");
    sb.append("calculatedDataHash = ").append(getCalculatedDataHash()).append(", \n");
    sb.append("currentBlockHash = ").append(getCurrentBlockHash()).append(", \n");
    sb.append("currentDataHash = ").append(getCurrentDataHash());
    sb.append("}");

    return sb.toString();
  }
}