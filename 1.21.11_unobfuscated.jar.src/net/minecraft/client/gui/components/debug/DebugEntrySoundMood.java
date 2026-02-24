/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DebugEntrySoundMood
/*    */   implements DebugScreenEntry
/*    */ {
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 13 */     Minecraft minecraft = Minecraft.getInstance();
/* 14 */     if (minecraft.player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 18 */     displayer.addLine(minecraft.getSoundManager().getDebugString() + minecraft.getSoundManager().getDebugString());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugEntrySoundMood.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */