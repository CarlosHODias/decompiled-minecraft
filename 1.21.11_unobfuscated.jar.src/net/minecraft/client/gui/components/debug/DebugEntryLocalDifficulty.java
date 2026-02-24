/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.DifficultyInstance;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryLocalDifficulty
/*    */   implements DebugScreenEntry
/*    */ {
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/*    */     ServerLevel serverLevel;
/* 17 */     Minecraft minecraft = Minecraft.getInstance();
/* 18 */     Entity entity = minecraft.getCameraEntity();
/*    */     
/* 20 */     if (entity != null && serverChunk != null && serverOrClientLevel instanceof ServerLevel) { serverLevel = (ServerLevel)serverOrClientLevel; }
/*    */     else
/*    */     { return; }
/*    */     
/* 24 */     BlockPos feetPos = entity.blockPosition();
/*    */     
/* 26 */     if (serverLevel.isInsideBuildHeight(feetPos.getY())) {
/* 27 */       float moonBrightness = serverLevel.getMoonBrightness(feetPos);
/* 28 */       long localTime = serverChunk.getInhabitedTime();
/* 29 */       DifficultyInstance localDifficulty = new DifficultyInstance(serverLevel.getDifficulty(), serverLevel.getDayTime(), localTime, moonBrightness);
/* 30 */       displayer.addLine(String.format(Locale.ROOT, "Local Difficulty: %.2f // %.2f (Day %d)", new Object[] { localDifficulty.getEffectiveDifficulty(), localDifficulty.getSpecialMultiplier(), serverLevel.getDayCount() }));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugEntryLocalDifficulty.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */