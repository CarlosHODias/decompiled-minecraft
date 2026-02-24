/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public interface SculkBehaviour
/*    */ {
/*    */   default byte getSculkSpreadDelay() {
/* 15 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   default void onDischarged(LevelAccessor level, BlockState state, BlockPos pos, RandomSource random) {}
/*    */   
/*    */   default boolean depositCharge(LevelAccessor level, BlockPos pos, RandomSource random) {
/* 22 */     return false;
/*    */   }
/*    */   
/*    */   default boolean attemptSpreadVein(LevelAccessor level, BlockPos pos, BlockState state, Collection<Direction> facings, boolean postProcess) {
/* 26 */     return (((MultifaceSpreadeableBlock)Blocks.SCULK_VEIN).getSpreader().spreadAll(state, level, pos, postProcess) > 0L);
/*    */   }
/*    */   
/*    */   default boolean canChangeBlockStateOnSpread() {
/* 30 */     return true;
/*    */   }
/*    */   
/*    */   default int updateDecayDelay(int age) {
/* 34 */     return 1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public static final SculkBehaviour DEFAULT = new SculkBehaviour()
/*    */     {
/*    */       public boolean attemptSpreadVein(LevelAccessor level, BlockPos pos, BlockState state, Collection<Direction> facings, boolean postProcess) {
/* 42 */         if (facings == null) {
/* 43 */           return (((SculkVeinBlock)Blocks.SCULK_VEIN).getSameSpaceSpreader().spreadAll(level.getBlockState(pos), level, pos, postProcess) > 0L);
/*    */         }
/* 45 */         if (!facings.isEmpty()) {
/* 46 */           if (state.isAir() || state.getFluidState().is((Fluid)Fluids.WATER)) {
/* 47 */             return SculkVeinBlock.regrow(level, pos, state, facings);
/*    */           }
/* 49 */           return false;
/*    */         } 
/* 51 */         return super.attemptSpreadVein(level, pos, state, facings, postProcess);
/*    */       }
/*    */ 
/*    */ 
/*    */       
/*    */       public int attemptUseCharge(SculkSpreader.ChargeCursor cursor, LevelAccessor level, BlockPos originPos, RandomSource random, SculkSpreader spreader, boolean spreadVeins) {
/* 57 */         return (cursor.getDecayDelay() > 0) ? cursor.getCharge() : 0;
/*    */       }
/*    */ 
/*    */       
/*    */       public int updateDecayDelay(int age) {
/* 62 */         return Math.max(age - 1, 0);
/*    */       }
/*    */     };
/*    */   
/*    */   int attemptUseCharge(SculkSpreader.ChargeCursor paramChargeCursor, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, RandomSource paramRandomSource, SculkSpreader paramSculkSpreader, boolean paramBoolean);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SculkBehaviour.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */