/*    */ package com.mojang.realmsclient.client.worldupload;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public abstract class RealmsUploadException
/*    */   extends RuntimeException {
/*    */   public Component getStatusMessage() {
/*  8 */     return null;
/*    */   }
/*    */   
/*    */   public Component[] getErrorMessages() {
/* 12 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/client/worldupload/RealmsUploadException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */