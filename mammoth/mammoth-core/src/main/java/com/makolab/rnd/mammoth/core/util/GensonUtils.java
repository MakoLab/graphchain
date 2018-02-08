package com.makolab.rnd.mammoth.core.util;

import com.makolab.rnd.mammoth.core.model.p2p.message.PeerToPeerMessage;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

/**
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

  public static Genson createGensonWithConverters() {
    return new GensonBuilder()
        .useIndentation(true)
        .setSkipNull(false)
        .useMethods(true)
        .withConverter(new PeerToPeerMessageConverter(), PeerToPeerMessage.class)
        .create();
  }

  public static Genson createGensonWithExclusions(String... exclusions) {
    GensonBuilder gensonBuilder = new GensonBuilder()
        .useIndentation(true)
        .setSkipNull(false)
        .useMethods(true);
    for (String exclusion : exclusions) {
      gensonBuilder.exclude(exclusion);
    }
    return gensonBuilder.create();
  }
}