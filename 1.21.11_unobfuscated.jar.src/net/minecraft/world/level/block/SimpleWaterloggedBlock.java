/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public interface SimpleWaterloggedBlock extends BucketPickup, LiquidBlockContainer {
/*    */   default boolean canPlaceLiquid(LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type) {
/* 22 */     return (type == Fluids.WATER);
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
/* 27 */     if (!((Boolean)state.getValue((Property)BlockStateProperties.WATERLOGGED)) && fluidState.getType() == Fluids.WATER) {
/* 28 */       if (!level.isClientSide()) {
/* 29 */         level.setBlock(pos, (BlockState)state.setValue((Property)BlockStateProperties.WATERLOGGED, true), 3);
/* 30 */         level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay((LevelReader)level));
/*    */       } 
/* 32 */       return true;
/*    */     } 
/* 34 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   default ItemStack pickupBlock(LivingEntity user, LevelAccessor level, BlockPos pos, BlockState state) {
/* 39 */     if ((Boolean)state.getValue((Property)BlockStateProperties.WATERLOGGED)) {
/* 40 */       level.setBlock(pos, (BlockState)state.setValue((Property)BlockStateProperties.WATERLOGGED, false), 3);
/* 41 */       if (!state.canSurvive((LevelReader)level, pos)) {
/* 42 */         level.destroyBlock(pos, true);
/*    */       }
/* 44 */       return new ItemStack((ItemLike)Items.WATER_BUCKET);
/*    */     } 
/* 46 */     return ItemStack.EMPTY;
/*    */   }
/*    */ 
/*    */   
/*    */   default Optional<SoundEvent> getPickupSound() {
/* 51 */     return Fluids.WATER.getPickupSound();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SimpleWaterloggedBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */