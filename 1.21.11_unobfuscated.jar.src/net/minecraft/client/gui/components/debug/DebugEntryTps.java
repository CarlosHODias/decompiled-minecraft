/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*    */ import net.minecraft.client.server.IntegratedServer;
/*    */ import net.minecraft.network.Connection;
/*    */ import net.minecraft.server.ServerTickRateManager;
/*    */ import net.minecraft.world.TickRateManager;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryTps
/*    */   implements DebugScreenEntry
/*    */ {
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/*    */     String str1, tps;
/* 18 */     Minecraft minecraft = Minecraft.getInstance();
/* 19 */     IntegratedServer server = minecraft.getSingleplayerServer();
/* 20 */     ClientPacketListener connectionListener = minecraft.getConnection();
/* 21 */     if (connectionListener == null || serverOrClientLevel == null) {
/*    */       return;
/*    */     }
/*    */     
/* 25 */     Connection connection = connectionListener.getConnection();
/* 26 */     float averageSentPackets = connection.getAverageSentPackets();
/* 27 */     float averageReceivedPackets = connection.getAverageReceivedPackets();
/*    */     
/* 29 */     TickRateManager tickRateManager = serverOrClientLevel.tickRateManager();
/* 30 */     if (tickRateManager.isSteppingForward()) {
/* 31 */       str1 = " (frozen - stepping)";
/* 32 */     } else if (tickRateManager.isFrozen()) {
/* 33 */       str1 = " (frozen)";
/*    */     } else {
/* 35 */       str1 = "";
/*    */     } 
/*    */ 
/*    */     
/* 39 */     if (server != null) {
/* 40 */       ServerTickRateManager serverTickRateManager = server.tickRateManager();
/* 41 */       boolean isSpriting = serverTickRateManager.isSprinting();
/* 42 */       if (isSpriting) {
/* 43 */         str1 = " (sprinting)";
/*    */       }
/* 45 */       String tpsTarget = isSpriting ? "-" : String.format(Locale.ROOT, "%.1f", new Object[] { tickRateManager.millisecondsPerTick() });
/* 46 */       tps = String.format(Locale.ROOT, "Integrated server @ %.1f/%s ms%s, %.0f tx, %.0f rx", new Object[] { server.getCurrentSmoothedTickTime(), tpsTarget, str1, averageSentPackets, averageReceivedPackets });
/*    */     } else {
/* 48 */       tps = String.format(Locale.ROOT, "\"%s\" server%s, %.0f tx, %.0f rx", new Object[] { connectionListener.serverBrand(), str1, averageSentPackets, averageReceivedPackets });
/*    */     } 
/*    */     
/* 51 */     displayer.addLine(tps);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAllowed(boolean reducedDebugInfo) {
/* 56 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugEntryTps.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */