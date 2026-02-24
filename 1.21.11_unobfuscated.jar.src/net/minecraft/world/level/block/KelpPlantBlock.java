/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ 
/*    */ public class KelpPlantBlock extends GrowingPlantBodyBlock implements LiquidBlockContainer {
/* 17 */   public static final MapCodec<KelpPlantBlock> CODEC = simpleCodec(KelpPlantBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<KelpPlantBlock> codec() {
/* 21 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected KelpPlantBlock(BlockBehaviour.Properties properties) {
/* 25 */     super(properties, Direction.UP, Shapes.block(), true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected GrowingPlantHeadBlock getHeadBlock() {
/* 30 */     return (GrowingPlantHeadBlock)Blocks.KELP;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FluidState getFluidState(BlockState state) {
/* 35 */     return Fluids.WATER.getSource(false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canAttachTo(BlockState state) {
/* 40 */     return getHeadBlock().canAttachTo(state);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canPlaceLiquid(LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type) {
/* 45 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
/* 50 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/KelpPlantBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */