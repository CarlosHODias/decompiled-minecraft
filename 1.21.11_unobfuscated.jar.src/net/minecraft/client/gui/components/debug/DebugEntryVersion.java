/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.client.ClientBrandRetriever;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ class DebugEntryVersion
/*    */   implements DebugScreenEntry
/*    */ {
/*    */   public void display(DebugScreenDisplayer displayer, Level level, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 13 */     displayer.addPriorityLine("Minecraft " + SharedConstants.getCurrentVersion().name() + " (" + Minecraft.getInstance().getLaunchedVersion() + "/" + ClientBrandRetriever.getClientModName() + ")");
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAllowed(boolean reducedDebugInfo) {
/* 18 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugEntryVersion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */