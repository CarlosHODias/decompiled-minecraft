/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryEntityRenderStats
/*    */   implements DebugScreenEntry
/*    */ {
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 11 */     String stats = (Minecraft.getInstance()).levelRenderer.getEntityStatistics();
/* 12 */     if (stats != null) {
/* 13 */       displayer.addLine(stats);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAllowed(boolean reducedDebugInfo) {
/* 19 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugEntryEntityRenderStats.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */