/*    */ package com.mojang.realmsclient.util.task;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.client.RealmsClient;
/*    */ import com.mojang.realmsclient.dto.WorldTemplate;
/*    */ import com.mojang.realmsclient.exception.RetryCallException;
/*    */ import com.mojang.realmsclient.gui.screens.configuration.RealmsConfigureWorldScreen;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class SwitchMinigameTask extends LongRunningTask {
/* 12 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 14 */   private static final Component TITLE = (Component)Component.translatable("mco.minigame.world.starting.screen.title");
/*    */   
/*    */   private final long realmId;
/*    */   private final WorldTemplate worldTemplate;
/*    */   private final RealmsConfigureWorldScreen nextScreen;
/*    */   
/*    */   public SwitchMinigameTask(long realmId, WorldTemplate worldTemplate, RealmsConfigureWorldScreen nextScreen) {
/* 21 */     this.realmId = realmId;
/* 22 */     this.worldTemplate = worldTemplate;
/* 23 */     this.nextScreen = nextScreen;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 28 */     RealmsClient client = RealmsClient.getOrCreate();
/* 29 */     for (int i = 0; i < 25; i++) {
/*    */       try {
/* 31 */         if (aborted()) {
/*    */           return;
/*    */         }
/*    */         
/* 35 */         if (client.putIntoMinigameMode(this.realmId, this.worldTemplate.id())) {
/* 36 */           setScreen((Screen)this.nextScreen);
/*    */           break;
/*    */         } 
/* 39 */       } catch (RetryCallException e) {
/* 40 */         if (aborted()) {
/*    */           return;
/*    */         }
/* 43 */         pause(e.delaySeconds);
/* 44 */       } catch (Exception e) {
/* 45 */         if (aborted()) {
/*    */           return;
/*    */         }
/* 48 */         LOGGER.error("Couldn't start mini game!");
/* 49 */         error(e);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getTitle() {
/* 56 */     return TITLE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/util/task/SwitchMinigameTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */