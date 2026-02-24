/*    */ package com.mojang.realmsclient.util.task;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.client.RealmsClient;
/*    */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*    */ import com.mojang.realmsclient.exception.RetryCallException;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public abstract class ResettingWorldTask extends LongRunningTask {
/* 11 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final long serverId;
/*    */   
/*    */   private final Component title;
/*    */   private final Runnable callback;
/*    */   
/*    */   public ResettingWorldTask(long serverId, Component title, Runnable callback) {
/* 19 */     this.serverId = serverId;
/* 20 */     this.title = title;
/* 21 */     this.callback = callback;
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract void sendResetRequest(RealmsClient paramRealmsClient, long paramLong) throws RealmsServiceException;
/*    */   
/*    */   public void run() {
/* 28 */     RealmsClient client = RealmsClient.getOrCreate();
/* 29 */     for (int i = 0; i < 25; i++) {
/*    */       try {
/* 31 */         if (aborted()) {
/*    */           return;
/*    */         }
/*    */         
/* 35 */         sendResetRequest(client, this.serverId);
/*    */         
/* 37 */         if (aborted()) {
/*    */           return;
/*    */         }
/*    */         
/* 41 */         this.callback.run();
/*    */         
/*    */         return;
/* 44 */       } catch (RetryCallException e) {
/* 45 */         if (aborted()) {
/*    */           return;
/*    */         }
/* 48 */         pause(e.delaySeconds);
/* 49 */       } catch (Exception e) {
/* 50 */         if (aborted()) {
/*    */           return;
/*    */         }
/* 53 */         LOGGER.error("Couldn't reset world");
/* 54 */         error(e);
/*    */         return;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getTitle() {
/* 62 */     return this.title;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/util/task/ResettingWorldTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */