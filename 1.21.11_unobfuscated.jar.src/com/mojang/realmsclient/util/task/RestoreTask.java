/*    */ package com.mojang.realmsclient.util.task;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.client.RealmsClient;
/*    */ import com.mojang.realmsclient.dto.Backup;
/*    */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*    */ import com.mojang.realmsclient.exception.RetryCallException;
/*    */ import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
/*    */ import com.mojang.realmsclient.gui.screens.configuration.RealmsConfigureWorldScreen;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class RestoreTask extends LongRunningTask {
/* 14 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 16 */   private static final Component TITLE = (Component)Component.translatable("mco.backup.restoring");
/*    */   
/*    */   private final Backup backup;
/*    */   private final long realmId;
/*    */   private final RealmsConfigureWorldScreen lastScreen;
/*    */   
/*    */   public RestoreTask(Backup backup, long realmId, RealmsConfigureWorldScreen lastScreen) {
/* 23 */     this.backup = backup;
/* 24 */     this.realmId = realmId;
/* 25 */     this.lastScreen = lastScreen;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 30 */     RealmsClient client = RealmsClient.getOrCreate();
/* 31 */     for (int i = 0; i < 25; i++) {
/*    */       try {
/* 33 */         if (aborted()) {
/*    */           return;
/*    */         }
/* 36 */         client.restoreWorld(this.realmId, this.backup.backupId);
/* 37 */         pause(1L);
/* 38 */         if (aborted()) {
/*    */           return;
/*    */         }
/* 41 */         setScreen((Screen)this.lastScreen);
/*    */         return;
/* 43 */       } catch (RetryCallException e) {
/* 44 */         if (aborted()) {
/*    */           return;
/*    */         }
/* 47 */         pause(e.delaySeconds);
/* 48 */       } catch (RealmsServiceException e) {
/* 49 */         if (aborted()) {
/*    */           return;
/*    */         }
/* 52 */         LOGGER.error("Couldn't restore backup", (Throwable)e);
/* 53 */         setScreen((Screen)new RealmsGenericErrorScreen(e, (Screen)this.lastScreen));
/*    */         return;
/* 55 */       } catch (Exception e) {
/* 56 */         if (aborted()) {
/*    */           return;
/*    */         }
/* 59 */         LOGGER.error("Couldn't restore backup", e);
/* 60 */         error(e);
/*    */         return;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Component getTitle() {
/* 69 */     return TITLE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/util/task/RestoreTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */