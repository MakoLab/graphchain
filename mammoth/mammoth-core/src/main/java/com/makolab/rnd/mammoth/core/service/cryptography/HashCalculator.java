package com.makolab.rnd.mammoth.core.service.cryptography;

public interface HashCalculator {

  String calculateHash(String input);

  byte[] calculateHashAsBytes(String input);
}
