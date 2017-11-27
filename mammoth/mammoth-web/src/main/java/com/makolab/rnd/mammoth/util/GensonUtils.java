package com.makolab.rnd.mammoth.util;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

/**
 *
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
public class GensonUtils {

  public static Genson createGenson() {
    return new GensonBuilder()
        .useIndentation(true)
        .setSkipNull(false)
        .useMethods(true)
        .create();
  }
}