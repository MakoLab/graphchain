package com.makolab.rnd.mammoth.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
public class TimestampCreator {

  public static String createTimestampString() {
    return "" + System.currentTimeMillis() / 1000L;
  }
}
