package com.makolab.rnd.mammoth.core.exception;

/**
 *
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
public class NormalizingRdfException extends Exception {

  public NormalizingRdfException(String message) {
    super(message);
  }

  public NormalizingRdfException(String message, Throwable cause) {
    super(message, cause);
  }
}