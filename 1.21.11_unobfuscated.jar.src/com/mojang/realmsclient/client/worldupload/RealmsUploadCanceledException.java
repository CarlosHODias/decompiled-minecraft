/*    */ package com.mojang.realmsclient.client.worldupload;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class RealmsUploadCanceledException extends RealmsUploadException {
/*  6 */   private static final Component UPLOAD_CANCELED = (Component)Component.translatable("mco.upload.cancelled");
/*    */ 
/*    */   
/*    */   public Component getStatusMessage() {
/* 10 */     return UPLOAD_CANCELED;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/client/worldupload/RealmsUploadCanceledException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */