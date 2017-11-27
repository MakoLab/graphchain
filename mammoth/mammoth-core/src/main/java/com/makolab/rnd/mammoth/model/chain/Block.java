package com.makolab.rnd.mammoth.model.chain;

import java.util.Objects;

/**
x * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
public class Block {

  private final String blockIri;
  private final BlockHeader blockHeader;
  private final BlockContent blockContent;
  private final Boolean genesisBlock;

  public Block(String blockIri, BlockHeader blockHeader, BlockContent blockContent) {
    this(blockIri, blockHeader, blockContent, false);
  }

  public Block(String blockIri, BlockHeader blockHeader, BlockContent blockContent, Boolean genesisBlock) {
    this.blockIri = blockIri;
    this.blockHeader = blockHeader;
    this.blockContent = blockContent;
    this.genesisBlock = genesisBlock;
  }

  public String getBlockIri() {
    return blockIri;
  }

  public BlockHeader getBlockHeader() {
    return blockHeader;
  }

  public BlockContent getBlockContent() {
    return blockContent;
  }

  public Boolean isGenesisBlock() {
    return genesisBlock;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Block block = (Block) o;
    return Objects.equals(getBlockIri(), block.getBlockIri()) &&
        Objects.equals(getBlockHeader(), block.getBlockHeader()) &&
        Objects.equals(getBlockContent(), block.getBlockContent()) &&
        Objects.equals(genesisBlock, block.genesisBlock);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getBlockIri(), getBlockHeader(), getBlockContent(), genesisBlock);
  }
}