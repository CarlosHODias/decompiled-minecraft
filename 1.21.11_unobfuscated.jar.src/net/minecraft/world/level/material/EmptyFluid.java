/*    */ package net.minecraft.world.level.material;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class EmptyFluid
/*    */   extends Fluid {
/*    */   public Item getBucket() {
/* 18 */     return Items.AIR;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid other, Direction direction) {
/* 23 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 getFlow(BlockGetter level, BlockPos pos, FluidState fluidState) {
/* 28 */     return Vec3.ZERO;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getTickDelay(LevelReader level) {
/* 33 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isEmpty() {
/* 38 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getExplosionResistance() {
/* 43 */     return 0.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getHeight(FluidState fluidState, BlockGetter level, BlockPos pos) {
/* 48 */     return 0.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getOwnHeight(FluidState fluidState) {
/* 53 */     return 0.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState createLegacyBlock(FluidState fluidState) {
/* 58 */     return Blocks.AIR.defaultBlockState();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSource(FluidState fluidState) {
/* 63 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getAmount(FluidState fluidState) {
/* 68 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(FluidState state, BlockGetter level, BlockPos pos) {
/* 73 */     return Shapes.empty();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/material/EmptyFluid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */