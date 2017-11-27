package com.makolab.rnd.mammoth.service.cryptography;

public interface HashCalculator {

  String calculateHash(String input);

  byte[] calculateHashAsBytes(String input);
}
