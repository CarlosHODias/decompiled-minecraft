/*    */ package net.minecraft.client.renderer.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.BlockAndTintGetter;
/*    */ import net.minecraft.world.level.ColorResolver;
/*    */ import net.minecraft.world.level.EmptyBlockAndTintGetter;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class MovingBlockRenderState
/*    */   implements BlockAndTintGetter
/*    */ {
/* 19 */   public BlockPos randomSeedPos = BlockPos.ZERO;
/* 20 */   public BlockPos blockPos = BlockPos.ZERO;
/* 21 */   public BlockState blockState = Blocks.AIR.defaultBlockState();
/*    */   
/*    */   public Holder<Biome> biome;
/* 24 */   public BlockAndTintGetter level = (BlockAndTintGetter)EmptyBlockAndTintGetter.INSTANCE;
/*    */ 
/*    */   
/*    */   public float getShade(Direction direction, boolean shade) {
/* 28 */     return this.level.getShade(direction, shade);
/*    */   }
/*    */ 
/*    */   
/*    */   public LevelLightEngine getLightEngine() {
/* 33 */     return this.level.getLightEngine();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBlockTint(BlockPos pos, ColorResolver color) {
/* 38 */     if (this.biome == null) {
/* 39 */       return -1;
/*    */     }
/* 41 */     return color.getColor((Biome)this.biome.value(), pos.getX(), pos.getZ());
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity getBlockEntity(BlockPos pos) {
/* 46 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getBlockState(BlockPos pos) {
/* 51 */     if (pos.equals(this.blockPos)) {
/* 52 */       return this.blockState;
/*    */     }
/* 54 */     return Blocks.AIR.defaultBlockState();
/*    */   }
/*    */ 
/*    */   
/*    */   public FluidState getFluidState(BlockPos pos) {
/* 59 */     return getBlockState(pos).getFluidState();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 64 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinY() {
/* 69 */     return this.blockPos.getY();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/MovingBlockRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */