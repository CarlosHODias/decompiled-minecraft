/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.CarvingMask;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.chunk.ProtoChunk;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ 
/*    */ public class PlacementContext extends WorldGenerationContext {
/*    */   private final WorldGenLevel level;
/*    */   private final ChunkGenerator generator;
/*    */   private final Optional<PlacedFeature> topFeature;
/*    */   
/*    */   public PlacementContext(WorldGenLevel level, ChunkGenerator generator, Optional<PlacedFeature> topFeature) {
/* 21 */     super(generator, (LevelHeightAccessor)level);
/* 22 */     this.level = level;
/* 23 */     this.generator = generator;
/* 24 */     this.topFeature = topFeature;
/*    */   }
/*    */   
/*    */   public int getHeight(Heightmap.Types type, int x, int z) {
/* 28 */     return this.level.getHeight(type, x, z);
/*    */   }
/*    */   
/*    */   public CarvingMask getCarvingMask(ChunkPos pos) {
/* 32 */     return ((ProtoChunk)this.level.getChunk(pos.x, pos.z)).getOrCreateCarvingMask();
/*    */   }
/*    */   
/*    */   public BlockState getBlockState(BlockPos pos) {
/* 36 */     return this.level.getBlockState(pos);
/*    */   }
/*    */   
/*    */   public int getMinY() {
/* 40 */     return this.level.getMinY();
/*    */   }
/*    */   
/*    */   public WorldGenLevel getLevel() {
/* 44 */     return this.level;
/*    */   }
/*    */   
/*    */   public Optional<PlacedFeature> topFeature() {
/* 48 */     return this.topFeature;
/*    */   }
/*    */   
/*    */   public ChunkGenerator generator() {
/* 52 */     return this.generator;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/placement/PlacementContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */