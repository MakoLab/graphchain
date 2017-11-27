package com.makolab.rnd.mammoth.model.chain;

public class LastBlockInfo {

  private final String blockIri;
  private final String blockHash;
  private final String blockIndex;

  public LastBlockInfo(String blockIri, String blockHash, String blockIndex) {
    this.blockIri = blockIri;
    this.blockHash = blockHash;
    this.blockIndex = blockIndex;
  }

  public String getBlockIri() {
    return blockIri;
  }

  public String getBlockHash() {
    return blockHash;
  }

  public String getBlockIndex() {
    return blockIndex;
  }

  @Override
  public String toString() {
    return "LastBlockInfo{" +
        "blockIri='" + blockIri + '\'' +
        ", blockHash='" + blockHash + '\'' +
        ", blockIndex='" + blockIndex + '\'' +
        '}';
  }
}
