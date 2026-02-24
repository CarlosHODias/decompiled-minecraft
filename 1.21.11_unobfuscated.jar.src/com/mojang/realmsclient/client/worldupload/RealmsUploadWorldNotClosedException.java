/*   */ package com.mojang.realmsclient.client.worldupload;
/*   */ 
/*   */ import net.minecraft.network.chat.Component;
/*   */ 
/*   */ public class RealmsUploadWorldNotClosedException
/*   */   extends RealmsUploadException {
/*   */   public Component getStatusMessage() {
/* 8 */     return (Component)Component.translatable("mco.upload.close.failure");
/*   */   }
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/client/worldupload/RealmsUploadWorldNotClosedException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */