/*    */ package com.mojang.realmsclient.client.worldupload;
/*    */ 
/*    */ import com.mojang.realmsclient.client.UploadStatus;
/*    */ 
/*    */ public interface RealmsWorldUploadStatusTracker {
/*    */   UploadStatus getUploadStatus();
/*    */   
/*    */   void setUploading();
/*    */   
/*    */   static RealmsWorldUploadStatusTracker noOp() {
/* 11 */     return new RealmsWorldUploadStatusTracker() {
/* 12 */         private final UploadStatus uploadStatus = new UploadStatus();
/*    */ 
/*    */         
/*    */         public UploadStatus getUploadStatus() {
/* 16 */           return this.uploadStatus;
/*    */         }
/*    */         
/*    */         public void setUploading() {}
/*    */       };
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/client/worldupload/RealmsWorldUploadStatusTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */