/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public enum EmptyBlockAndTintGetter
/*    */   implements BlockAndTintGetter
/*    */ {
/* 15 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public float getShade(Direction direction, boolean shade) {
/* 19 */     return 1.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public LevelLightEngine getLightEngine() {
/* 24 */     return LevelLightEngine.EMPTY;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBlockTint(BlockPos pos, ColorResolver color) {
/* 29 */     return -1;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity getBlockEntity(BlockPos pos) {
/* 34 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getBlockState(BlockPos pos) {
/* 39 */     return Blocks.AIR.defaultBlockState();
/*    */   }
/*    */ 
/*    */   
/*    */   public FluidState getFluidState(BlockPos pos) {
/* 44 */     return Fluids.EMPTY.defaultFluidState();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 49 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinY() {
/* 54 */     return 0;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/EmptyBlockAndTintGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */