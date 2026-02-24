/*    */ package com.mojang.realmsclient.util.task;
/*    */ 
/*    */ import com.mojang.realmsclient.dto.RealmsJoinInformation;
/*    */ import com.mojang.realmsclient.dto.RealmsServer;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.multiplayer.resolver.ServerAddress;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.realms.RealmsConnect;
/*    */ 
/*    */ public class ConnectTask extends LongRunningTask {
/* 12 */   private static final Component TITLE = (Component)Component.translatable("mco.connect.connecting");
/*    */   
/*    */   private final RealmsConnect realmsConnect;
/*    */   private final RealmsServer server;
/*    */   private final RealmsJoinInformation address;
/*    */   
/*    */   public ConnectTask(Screen lastScreen, RealmsServer server, RealmsJoinInformation address) {
/* 19 */     this.server = server;
/* 20 */     this.address = address;
/* 21 */     this.realmsConnect = new RealmsConnect(lastScreen);
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 26 */     if (this.address.address() != null) {
/* 27 */       this.realmsConnect.connect(this.server, ServerAddress.parseString(this.address.address()));
/*    */     } else {
/* 29 */       abortTask();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void abortTask() {
/* 35 */     super.abortTask();
/* 36 */     this.realmsConnect.abort();
/* 37 */     Minecraft.getInstance().getDownloadedPackSource().cleanupAfterDisconnect();
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 42 */     this.realmsConnect.tick();
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getTitle() {
/* 47 */     return TITLE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/util/task/ConnectTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */