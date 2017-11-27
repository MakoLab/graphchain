package com.makolab.rnd.mammoth.exception;

/**
 *
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
public class ExtractingRdfDatasetException extends Exception {

  public ExtractingRdfDatasetException(String message) {
    super(message);
  }

  public ExtractingRdfDatasetException(String message, Throwable cause) {
    super(message, cause);
  }
}
