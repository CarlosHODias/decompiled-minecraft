/*    */ package com.mojang.realmsclient.util.task;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.client.RealmsClient;
/*    */ import com.mojang.realmsclient.exception.RetryCallException;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class SwitchSlotTask extends LongRunningTask {
/* 10 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 12 */   private static final Component TITLE = (Component)Component.translatable("mco.minigame.world.slot.screen.title");
/*    */   
/*    */   private final long realmId;
/*    */   private final int slot;
/*    */   private final Runnable callback;
/*    */   
/*    */   public SwitchSlotTask(long realmId, int slot, Runnable callback) {
/* 19 */     this.realmId = realmId;
/* 20 */     this.slot = slot;
/* 21 */     this.callback = callback;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 26 */     RealmsClient client = RealmsClient.getOrCreate();
/* 27 */     for (int i = 0; i < 25; i++) {
/*    */       try {
/* 29 */         if (aborted()) {
/*    */           return;
/*    */         }
/*    */         
/* 33 */         if (client.switchSlot(this.realmId, this.slot)) {
/* 34 */           this.callback.run();
/*    */           break;
/*    */         } 
/* 37 */       } catch (RetryCallException e) {
/* 38 */         if (aborted()) {
/*    */           return;
/*    */         }
/* 41 */         pause(e.delaySeconds);
/* 42 */       } catch (Exception e) {
/* 43 */         if (aborted()) {
/*    */           return;
/*    */         }
/* 46 */         LOGGER.error("Couldn't switch world!");
/* 47 */         error(e);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getTitle() {
/* 54 */     return TITLE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/util/task/SwitchSlotTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */