package com.makolab.rnd.mammoth.core.exception;

/**
 *
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
public class CreatingBlockException extends Exception {

  public CreatingBlockException(String message) {
    super(message);
  }

  public CreatingBlockException(String message, Throwable cause) {
    super(message, cause);
  }
}
