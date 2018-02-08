package com.makolab.rnd.mammoth.core.exception;

public class RepositoryOperationException extends Exception {

  public RepositoryOperationException(String message) {
    super(message);
  }

  public RepositoryOperationException(String message, Throwable cause) {
    super(message, cause);
  }
}