/*    */ package com.mojang.realmsclient.util.task;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.RealmsMainScreen;
/*    */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*    */ import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.gui.screens.TitleScreen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public abstract class LongRunningTask implements Runnable {
/*    */   protected static final int NUMBER_OF_RETRIES = 25;
/* 15 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   private boolean aborted = false;
/*    */   
/*    */   protected static void pause(long seconds) {
/*    */     try {
/* 20 */       Thread.sleep(seconds * 1000L);
/* 21 */     } catch (InterruptedException e) {
/* 22 */       Thread.currentThread().interrupt();
/* 23 */       LOGGER.error("", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static void setScreen(Screen screen) {
/* 29 */     Minecraft minecraft = Minecraft.getInstance();
/* 30 */     minecraft.execute(() -> minecraft.setScreen(screen));
/*    */   }
/*    */   
/*    */   protected void error(Component errorMessage) {
/* 34 */     abortTask();
/* 35 */     Minecraft minecraft = Minecraft.getInstance();
/* 36 */     minecraft.execute(() -> minecraft.setScreen((Screen)new RealmsGenericErrorScreen(errorMessage, (Screen)new RealmsMainScreen((Screen)new TitleScreen()))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void error(Exception ex) {
/* 44 */     if (ex instanceof RealmsServiceException) { RealmsServiceException rsx = (RealmsServiceException)ex;
/* 45 */       error(rsx.realmsError.errorMessage()); }
/*    */     else
/* 47 */     { error((Component)Component.literal(ex.getMessage())); }
/*    */   
/*    */   }
/*    */   
/*    */   protected void error(RealmsServiceException ex) {
/* 52 */     error(ex.realmsError.errorMessage());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean aborted() {
/* 58 */     return this.aborted;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {}
/*    */ 
/*    */   
/*    */   public void init() {}
/*    */   
/*    */   public void abortTask() {
/* 68 */     this.aborted = true;
/*    */   }
/*    */   
/*    */   public abstract Component getTitle();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/util/task/LongRunningTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */