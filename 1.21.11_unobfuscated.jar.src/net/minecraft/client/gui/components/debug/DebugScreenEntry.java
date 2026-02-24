/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public interface DebugScreenEntry
/*    */ {
/*    */   void display(DebugScreenDisplayer paramDebugScreenDisplayer, Level paramLevel, LevelChunk paramLevelChunk1, LevelChunk paramLevelChunk2);
/*    */   
/*    */   default boolean isAllowed(boolean reducedDebugInfo) {
/* 11 */     return !reducedDebugInfo;
/*    */   }
/*    */   
/*    */   default DebugEntryCategory category() {
/* 15 */     return DebugEntryCategory.SCREEN_TEXT;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugScreenEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */