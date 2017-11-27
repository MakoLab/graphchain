package com.makolab.rnd.mammoth.exception;

public class CalculatingHashException extends Exception {

  public CalculatingHashException(String message) {
    super(message);
  }

  public CalculatingHashException(String message, Throwable cause) {
    super(message, cause);
  }
}