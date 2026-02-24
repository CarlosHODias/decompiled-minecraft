/*    */ package net.minecraft.client.resources.server;public interface PackLoadFeedback {
/*    */   void reportUpdate(java.util.UUID paramUUID, Update paramUpdate);
/*    */   
/*    */   void reportFinalResult(java.util.UUID paramUUID, FinalResult paramFinalResult);
/*    */   
/*    */   public enum Update {
/*  7 */     ACCEPTED,
/*  8 */     DOWNLOADED;
/*    */   }
/*    */   
/*    */   public enum FinalResult
/*    */   {
/* 13 */     DECLINED,
/*    */ 
/*    */     
/* 16 */     APPLIED,
/*    */ 
/*    */     
/* 19 */     DISCARDED,
/* 20 */     DOWNLOAD_FAILED,
/* 21 */     ACTIVATION_FAILED;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/server/PackLoadFeedback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */