package com.makolab.rnd.mammoth.model.chain;

import java.util.Objects;

/**
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
public class BlockHeader {

  private final String dataGraphIri;
  private final String dataHash;
  private final String hash;
  private final String index;
  private final String previousBlock;
  private final String previousHash;
  private final String timestamp;

  public BlockHeader(String dataGraphIri, String dataHash, String hash, String index, String previousBlock,
                     String previousHash, String timestamp) {
    this.dataGraphIri = dataGraphIri;
    this.dataHash = dataHash;
    this.hash = hash;
    this.index = index;
    this.previousBlock = previousBlock;
    this.previousHash = previousHash;
    this.timestamp = timestamp;
  }

  public String getDataGraphIri() {
    return dataGraphIri;
  }

  public String getDataHash() {
    return dataHash;
  }

  public String getHash() {
    return hash;
  }

  public String getIndex() {
    return index;
  }

  public String getPreviousBlock() {
    return previousBlock;
  }

  public String getPreviousHash() {
    return previousHash;
  }

  public String getTimestamp() {
    return timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlockHeader that = (BlockHeader) o;
    return Objects.equals(getDataGraphIri(), that.getDataGraphIri()) &&
        Objects.equals(getDataHash(), that.getDataHash()) &&
        Objects.equals(getHash(), that.getHash()) &&
        Objects.equals(getIndex(), that.getIndex()) &&
        Objects.equals(getPreviousBlock(), that.getPreviousBlock()) &&
        Objects.equals(getPreviousHash(), that.getPreviousHash()) &&
        Objects.equals(getTimestamp(), that.getTimestamp());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getDataGraphIri(), getDataHash(), getHash(), getIndex(), getPreviousBlock(), getPreviousHash(), getTimestamp());
  }
}