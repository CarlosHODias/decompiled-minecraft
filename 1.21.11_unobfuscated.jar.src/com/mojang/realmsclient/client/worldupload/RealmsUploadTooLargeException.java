/*    */ package com.mojang.realmsclient.client.worldupload;
/*    */ 
/*    */ import com.mojang.realmsclient.Unit;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class RealmsUploadTooLargeException extends RealmsUploadException {
/*    */   final long sizeLimit;
/*    */   
/*    */   public RealmsUploadTooLargeException(long sizeLimit) {
/* 10 */     this.sizeLimit = sizeLimit;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component[] getErrorMessages() {
/* 15 */     return new Component[] {
/* 16 */         (Component)Component.translatable("mco.upload.failed.too_big.title"), 
/* 17 */         (Component)Component.translatable("mco.upload.failed.too_big.description", new Object[] { Unit.humanReadable(this.sizeLimit, Unit.getLargest(this.sizeLimit)) })
/*    */       };
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/client/worldupload/RealmsUploadTooLargeException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */