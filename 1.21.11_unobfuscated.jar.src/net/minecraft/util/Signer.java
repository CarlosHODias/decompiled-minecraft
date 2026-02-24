/*    */ package net.minecraft.util;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.security.PrivateKey;
/*    */ import java.security.Signature;
/*    */ import java.security.SignatureException;
/*    */ import java.util.Objects;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public interface Signer {
/* 10 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */ 
/*    */   
/*    */   default byte[] sign(byte[] payload) {
/* 15 */     return sign(output -> output.update(payload));
/*    */   }
/*    */   
/*    */   static Signer from(PrivateKey privateKey, String algorithm) {
/* 19 */     return updater -> {
/*    */         try {
/*    */           Signature signer = Signature.getInstance(algorithm); signer.initSign(privateKey);
/*    */           Objects.requireNonNull(signer);
/*    */           updater.update(signer::update);
/*    */           return signer.sign();
/* 25 */         } catch (Exception e) {
/*    */           throw new IllegalStateException("Failed to sign message", e);
/*    */         } 
/*    */       };
/*    */   }
/*    */   
/*    */   byte[] sign(SignatureUpdater paramSignatureUpdater);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/Signer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */