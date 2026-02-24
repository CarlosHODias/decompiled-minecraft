/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.LongSet;
/*    */ import it.unimi.dsi.fastutil.longs.LongSets;
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryPosition
/*    */   implements DebugScreenEntry
/*    */ {
/* 22 */   public static final Identifier GROUP = Identifier.withDefaultNamespace("position");
/*    */ 
/*    */   
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 26 */     Minecraft minecraft = Minecraft.getInstance();
/* 27 */     Entity entity = minecraft.getCameraEntity();
/*    */     
/* 29 */     if (entity == null) {
/*    */       return;
/*    */     }
/*    */     
/* 33 */     BlockPos feetPos = minecraft.getCameraEntity().blockPosition();
/* 34 */     ChunkPos chunkPos = new ChunkPos(feetPos);
/* 35 */     Direction direction = entity.getDirection();
/* 36 */     switch (direction) { case NORTH: 
/*    */       case SOUTH: 
/*    */       case WEST: 
/*    */       case EAST: 
/*    */       default:
/* 41 */         break; }  String faceString = "Invalid";
/*    */     
/* 43 */     LongSet chunks = (serverOrClientLevel instanceof ServerLevel) ? ((ServerLevel)serverOrClientLevel).getForceLoadedChunks() : (LongSet)LongSets.EMPTY_SET;
/*    */     
/* 45 */     displayer.addToGroup(GROUP, List.of(
/* 46 */           String.format(Locale.ROOT, "XYZ: %.3f / %.5f / %.3f", new Object[] { minecraft.getCameraEntity().getX(), minecraft.getCameraEntity().getY(), minecraft.getCameraEntity().getZ()
/* 47 */             }), String.format(Locale.ROOT, "Block: %d %d %d", new Object[] { feetPos.getX(), feetPos.getY(), feetPos.getZ()
/* 48 */             }), String.format(Locale.ROOT, "Chunk: %d %d %d [%d %d in r.%d.%d.mca]", new Object[] { chunkPos.x, SectionPos.blockToSectionCoord(feetPos.getY()), chunkPos.z, chunkPos.getRegionLocalX(), chunkPos.getRegionLocalZ(), chunkPos.getRegionX(), chunkPos.getRegionZ()
/* 49 */             }), String.format(Locale.ROOT, "Facing: %s (%s) (%.1f / %.1f)", new Object[] { direction, faceString, Mth.wrapDegrees(entity.getYRot()), Mth.wrapDegrees(entity.getXRot())
/* 50 */             }), String.valueOf(minecraft.level.dimension().identifier()) + " FC: " + String.valueOf(minecraft.level.dimension().identifier())));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugEntryPosition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */