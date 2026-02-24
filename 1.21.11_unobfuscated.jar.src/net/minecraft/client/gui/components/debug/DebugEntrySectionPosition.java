/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DebugEntrySectionPosition
/*    */   implements DebugScreenEntry
/*    */ {
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 16 */     Minecraft minecraft = Minecraft.getInstance();
/* 17 */     Entity entity = minecraft.getCameraEntity();
/*    */     
/* 19 */     if (entity == null) {
/*    */       return;
/*    */     }
/*    */     
/* 23 */     BlockPos feetPos = minecraft.getCameraEntity().blockPosition();
/*    */     
/* 25 */     displayer.addToGroup(DebugEntryPosition.GROUP, String.format(Locale.ROOT, "Section-relative: %02d %02d %02d", new Object[] { feetPos.getX() & 0xF, feetPos.getY() & 0xF, feetPos.getZ() & 0xF }));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAllowed(boolean reducedDebugInfo) {
/* 30 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugEntrySectionPosition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */