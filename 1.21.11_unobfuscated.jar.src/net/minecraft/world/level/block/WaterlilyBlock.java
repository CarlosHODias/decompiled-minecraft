/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WaterlilyBlock extends VegetationBlock {
/* 18 */   public static final MapCodec<WaterlilyBlock> CODEC = simpleCodec(WaterlilyBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<WaterlilyBlock> codec() {
/* 22 */     return CODEC;
/*    */   }
/*    */   
/* 25 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 1.5D);
/*    */   
/*    */   protected WaterlilyBlock(BlockBehaviour.Properties properties) {
/* 28 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 33 */     super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
/*    */     
/* 35 */     if (level instanceof net.minecraft.server.level.ServerLevel && entity instanceof net.minecraft.world.entity.vehicle.boat.AbstractBoat) {
/* 36 */       level.destroyBlock(new BlockPos((Vec3i)pos), true, entity);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 42 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/* 47 */     FluidState fluidState = level.getFluidState(pos);
/* 48 */     FluidState fluidAbove = level.getFluidState(pos.above());
/* 49 */     return ((fluidState.getType() == Fluids.WATER || state.getBlock() instanceof IceBlock) && fluidAbove.getType() == Fluids.EMPTY);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/WaterlilyBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */