package com.locke.babelrecords.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class KeyGenerator {
  public static KeyPair generateRSAKey() {
    KeyPair keyPair;

    try {
      var keyPairGenerator = java.security.KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(2048);
      keyPair = keyPairGenerator.generateKeyPair();
    } catch ( Exception e ) {
      throw new IllegalStateException();
    }

    return keyPair;
  }
}
