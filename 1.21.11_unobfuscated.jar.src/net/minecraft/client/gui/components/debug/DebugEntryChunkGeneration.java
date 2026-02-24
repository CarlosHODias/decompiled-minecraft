/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.level.ServerChunkCache;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.biome.BiomeSource;
/*    */ import net.minecraft.world.level.biome.Climate;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ import net.minecraft.world.level.levelgen.RandomState;
/*    */ 
/*    */ public class DebugEntryChunkGeneration
/*    */   implements DebugScreenEntry
/*    */ {
/* 21 */   private static final Identifier GROUP = Identifier.withDefaultNamespace("chunk_generation");
/*    */ 
/*    */   
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 25 */     Minecraft minecraft = Minecraft.getInstance();
/* 26 */     Entity entity = minecraft.getCameraEntity();
/* 27 */     ServerLevel serverLevel = (serverOrClientLevel instanceof ServerLevel) ? (ServerLevel)serverOrClientLevel : null;
/*    */     
/* 29 */     if (entity == null || serverLevel == null) {
/*    */       return;
/*    */     }
/*    */     
/* 33 */     BlockPos feetPos = entity.blockPosition();
/*    */     
/* 35 */     ServerChunkCache chunkSource = serverLevel.getChunkSource();
/*    */     
/* 37 */     List<String> result = new ArrayList<>();
/* 38 */     ChunkGenerator generator = chunkSource.getGenerator();
/* 39 */     RandomState randomState = chunkSource.randomState();
/* 40 */     generator.addDebugScreenInfo(result, randomState, feetPos);
/*    */     
/* 42 */     Climate.Sampler sampler = randomState.sampler();
/* 43 */     BiomeSource biomeSource = generator.getBiomeSource();
/* 44 */     biomeSource.addDebugInfo(result, feetPos, sampler);
/*    */     
/* 46 */     if (serverChunk != null && serverChunk.isOldNoiseGeneration()) {
/* 47 */       result.add("Blending: Old");
/*    */     }
/*    */     
/* 50 */     displayer.addToGroup(GROUP, result);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugEntryChunkGeneration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */