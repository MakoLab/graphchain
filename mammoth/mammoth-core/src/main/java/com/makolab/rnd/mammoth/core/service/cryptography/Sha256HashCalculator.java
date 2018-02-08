package com.makolab.rnd.mammoth.core.service.cryptography;

import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
@Service
public class Sha256HashCalculator implements HashCalculator {

  private static final String SHA256_NAME = "SHA-256";

  @Override
  public String calculateHash(String input) {
    byte[] digest = calculateHashAsBytes(input);
    return String.format("%064x", new java.math.BigInteger(1, digest));
  }

  @Override
  public byte[] calculateHashAsBytes(String input) {
    try {
      MessageDigest md = MessageDigest.getInstance(SHA256_NAME);

      md.update(input.getBytes("UTF-8"));
      return md.digest();
    } catch (NoSuchAlgorithmException ex) {
      String msg = String.format(
          "No digesting algorithm with name '%s'. " +
              "This is impossible to happen, but it happened anyway...",
          SHA256_NAME);
      throw new IllegalStateException(msg, ex);
    } catch (UnsupportedEncodingException ex) {
      throw new IllegalStateException("Exception occurred while calculating hash.", ex);
    }
  }
}
