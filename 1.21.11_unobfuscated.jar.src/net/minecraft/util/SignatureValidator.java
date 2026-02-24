/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.authlib.yggdrasil.ServicesKeyInfo;
/*    */ import com.mojang.authlib.yggdrasil.ServicesKeySet;
/*    */ import com.mojang.authlib.yggdrasil.ServicesKeyType;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.security.PublicKey;
/*    */ import java.security.Signature;
/*    */ import java.security.SignatureException;
/*    */ import java.util.Collection;
/*    */ import java.util.Objects;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ public interface SignatureValidator
/*    */ {
/*    */   public static final SignatureValidator NO_VALIDATION = (payload, signature) -> true;
/* 18 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */ 
/*    */   
/*    */   default boolean validate(byte[] payload, byte[] signature) {
/* 23 */     return validate(output -> output.update(payload), signature);
/*    */   }
/*    */   
/*    */   private static boolean verifySignature(SignatureUpdater updater, byte[] signature, Signature verifier) throws SignatureException {
/* 27 */     Objects.requireNonNull(verifier); updater.update(verifier::update);
/* 28 */     return verifier.verify(signature);
/*    */   }
/*    */   
/*    */   static SignatureValidator from(PublicKey publicKey, String algorithm) {
/* 32 */     return (updater, signature) -> {
/*    */         try {
/*    */           Signature verifier = Signature.getInstance(algorithm);
/*    */           verifier.initVerify(publicKey);
/*    */           return verifySignature(updater, signature, verifier);
/* 37 */         } catch (Exception e) {
/*    */           LOGGER.error("Failed to verify signature", e);
/*    */           return false;
/*    */         } 
/*    */       };
/*    */   }
/*    */   
/*    */   static SignatureValidator from(ServicesKeySet keySet, ServicesKeyType type) {
/* 45 */     Collection<ServicesKeyInfo> keys = keySet.keys(type);
/* 46 */     if (keys.isEmpty()) {
/* 47 */       return null;
/*    */     }
/* 49 */     return (updater, signature) -> keys.stream().anyMatch(());
/*    */   }
/*    */   
/*    */   boolean validate(SignatureUpdater paramSignatureUpdater, byte[] paramArrayOfbyte);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/SignatureValidator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */