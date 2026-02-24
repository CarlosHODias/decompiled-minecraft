/*    */ package com.mojang.realmsclient.util.task;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.RealmsMainScreen;
/*    */ import com.mojang.realmsclient.client.RealmsClient;
/*    */ import com.mojang.realmsclient.dto.RealmsServer;
/*    */ import com.mojang.realmsclient.exception.RetryCallException;
/*    */ import com.mojang.realmsclient.gui.screens.configuration.RealmsConfigureWorldScreen;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class OpenServerTask extends LongRunningTask {
/* 15 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 17 */   private static final Component TITLE = (Component)Component.translatable("mco.configure.world.opening");
/*    */   
/*    */   private final RealmsServer serverData;
/*    */   private final Screen returnScreen;
/*    */   private final boolean join;
/*    */   private final Minecraft minecraft;
/*    */   
/*    */   public OpenServerTask(RealmsServer realmsServer, Screen returnScreen, boolean join, Minecraft minecraft) {
/* 25 */     this.serverData = realmsServer;
/* 26 */     this.returnScreen = returnScreen;
/* 27 */     this.join = join;
/* 28 */     this.minecraft = minecraft;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 33 */     RealmsClient client = RealmsClient.getOrCreate();
/* 34 */     for (int i = 0; i < 25; i++) {
/* 35 */       if (aborted()) {
/*    */         return;
/*    */       }
/*    */       
/*    */       try {
/* 40 */         boolean openResult = client.open(this.serverData.id);
/* 41 */         if (openResult) {
/* 42 */           this.minecraft.execute(() -> {
/*    */                 Screen patt0$temp = this.returnScreen;
/*    */                 if (patt0$temp instanceof RealmsConfigureWorldScreen) {
/*    */                   RealmsConfigureWorldScreen screen = (RealmsConfigureWorldScreen)patt0$temp;
/*    */                   screen.stateChanged();
/*    */                 } 
/*    */                 this.serverData.state = RealmsServer.State.OPEN;
/*    */                 if (this.join) {
/*    */                   RealmsMainScreen.play(this.serverData, this.returnScreen);
/*    */                 } else {
/*    */                   this.minecraft.setScreen(this.returnScreen);
/*    */                 } 
/*    */               });
/*    */           break;
/*    */         } 
/* 57 */       } catch (RetryCallException e) {
/* 58 */         if (aborted()) {
/*    */           return;
/*    */         }
/* 61 */         pause(e.delaySeconds);
/* 62 */       } catch (Exception e) {
/* 63 */         if (aborted()) {
/*    */           return;
/*    */         }
/* 66 */         LOGGER.error("Failed to open server", e);
/* 67 */         error(e);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getTitle() {
/* 74 */     return TITLE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/util/task/OpenServerTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */