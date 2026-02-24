/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class BuddingAmethystBlock extends AmethystBlock {
/* 13 */   public static final MapCodec<BuddingAmethystBlock> CODEC = simpleCodec(BuddingAmethystBlock::new);
/*    */   public static final int GROWTH_CHANCE = 5;
/*    */   
/*    */   public MapCodec<BuddingAmethystBlock> codec() {
/* 17 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 22 */   private static final Direction[] DIRECTIONS = Direction.values();
/*    */   
/*    */   public BuddingAmethystBlock(BlockBehaviour.Properties properties) {
/* 25 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 30 */     if (random.nextInt(5) != 0) {
/*    */       return;
/*    */     }
/*    */     
/* 34 */     Direction growDirection = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
/* 35 */     BlockPos growPos = pos.relative(growDirection);
/* 36 */     BlockState relativeState = level.getBlockState(growPos);
/* 37 */     Block nextStage = null;
/* 38 */     if (canClusterGrowAtState(relativeState)) {
/* 39 */       nextStage = Blocks.SMALL_AMETHYST_BUD;
/* 40 */     } else if (relativeState.is(Blocks.SMALL_AMETHYST_BUD) && relativeState.getValue((Property)AmethystClusterBlock.FACING) == growDirection) {
/* 41 */       nextStage = Blocks.MEDIUM_AMETHYST_BUD;
/* 42 */     } else if (relativeState.is(Blocks.MEDIUM_AMETHYST_BUD) && relativeState.getValue((Property)AmethystClusterBlock.FACING) == growDirection) {
/* 43 */       nextStage = Blocks.LARGE_AMETHYST_BUD;
/* 44 */     } else if (relativeState.is(Blocks.LARGE_AMETHYST_BUD) && relativeState.getValue((Property)AmethystClusterBlock.FACING) == growDirection) {
/* 45 */       nextStage = Blocks.AMETHYST_CLUSTER;
/*    */     } 
/*    */     
/* 48 */     if (nextStage != null) {
/* 49 */       BlockState targetState = (BlockState)((BlockState)nextStage.defaultBlockState()
/* 50 */         .setValue((Property)AmethystClusterBlock.FACING, (Comparable)growDirection))
/* 51 */         .setValue((Property)AmethystClusterBlock.WATERLOGGED, (relativeState.getFluidState().getType() == Fluids.WATER));
/* 52 */       level.setBlockAndUpdate(growPos, targetState);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static boolean canClusterGrowAtState(BlockState state) {
/* 57 */     return (state.isAir() || (state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/BuddingAmethystBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */