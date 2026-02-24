/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryNoop
/*    */   implements DebugScreenEntry {
/*    */   private final boolean isAllowedWithReducedDebugInfo;
/*    */   
/*    */   public DebugEntryNoop() {
/* 11 */     this(false);
/*    */   }
/*    */   
/*    */   public DebugEntryNoop(boolean isAllowedWithReducedDebugInfo) {
/* 15 */     this.isAllowedWithReducedDebugInfo = isAllowedWithReducedDebugInfo;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isAllowed(boolean reducedDebugInfo) {
/* 25 */     return (this.isAllowedWithReducedDebugInfo || !reducedDebugInfo);
/*    */   }
/*    */ 
/*    */   
/*    */   public DebugEntryCategory category() {
/* 30 */     return DebugEntryCategory.RENDERER;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugEntryNoop.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */