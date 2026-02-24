package net.minecraft.util;

import java.security.SignatureException;

@FunctionalInterface
public interface SignatureUpdater {
  void update(Output paramOutput) throws SignatureException;
  
  @FunctionalInterface
  public static interface Output {
    void update(byte[] param1ArrayOfbyte) throws SignatureException;
  }
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/SignatureUpdater.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */