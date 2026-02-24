/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.FullChunkStatus;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class EmptyLevelChunk
/*    */   extends LevelChunk {
/*    */   private final Holder<Biome> biome;
/*    */   
/*    */   public EmptyLevelChunk(Level level, ChunkPos pos, Holder<Biome> biome) {
/* 21 */     super(level, pos);
/* 22 */     this.biome = biome;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getBlockState(BlockPos pos) {
/* 27 */     return Blocks.VOID_AIR.defaultBlockState();
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState setBlockState(BlockPos pos, BlockState state, @net.minecraft.world.level.block.Block.UpdateFlags int flags) {
/* 32 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public FluidState getFluidState(BlockPos pos) {
/* 37 */     return Fluids.EMPTY.defaultFluidState();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLightEmission(BlockPos pos) {
/* 42 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity getBlockEntity(BlockPos pos, LevelChunk.EntityCreationType creationType) {
/* 47 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addAndRegisterBlockEntity(BlockEntity blockEntity) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBlockEntity(BlockEntity blockEntity) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void removeBlockEntity(BlockPos pos) {}
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 64 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isYSpaceEmpty(int yStartInclusive, int yEndInclusive) {
/* 69 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public FullChunkStatus getFullStatus() {
/* 74 */     return FullChunkStatus.FULL;
/*    */   }
/*    */ 
/*    */   
/*    */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ) {
/* 79 */     return this.biome;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/EmptyLevelChunk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */