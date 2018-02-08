package com.makolab.rnd.mammoth.core.exception;

public class BlockNotFound extends ReadingBlockException {

  public BlockNotFound(String message) {
    super(message);
  }

  public BlockNotFound(String message, Throwable cause) {
    super(message, cause);
  }
}
