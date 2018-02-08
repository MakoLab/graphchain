package com.makolab.rnd.mammoth.core.service.base64;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

@Service
public class Base64Handler {

  public String toString(String base64) {
    byte[] bytes = Base64.decodeBase64(base64.getBytes());
    return new String(bytes, StandardCharsets.UTF_8);
  }

  public String toBase64(String string) {
    byte[] bytes = Base64.encodeBase64(string.getBytes());
    return new String(bytes, StandardCharsets.UTF_8);
  }
}
