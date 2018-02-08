package com.makolab.rnd.mammoth.core.exception;

public class CalculatingHashException extends Exception {

  public CalculatingHashException(String message) {
    super(message);
  }

  public CalculatingHashException(String message, Throwable cause) {
    super(message, cause);
  }
}