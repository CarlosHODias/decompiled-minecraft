/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.MultifaceGrowthConfiguration;
/*    */ 
/*    */ public class MultifaceGrowthFeature extends Feature<MultifaceGrowthConfiguration> {
/*    */   public MultifaceGrowthFeature(Codec<MultifaceGrowthConfiguration> codec) {
/* 17 */     super(codec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<MultifaceGrowthConfiguration> context) {
/* 22 */     WorldGenLevel level = context.level();
/* 23 */     BlockPos origin = context.origin();
/* 24 */     RandomSource random = context.random();
/* 25 */     MultifaceGrowthConfiguration config = context.config();
/* 26 */     if (!isAirOrWater(level.getBlockState(origin))) {
/* 27 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 31 */     List<Direction> searchDirections = config.getShuffledDirections(random);
/* 32 */     if (placeGrowthIfPossible(level, origin, level.getBlockState(origin), config, random, searchDirections)) {
/* 33 */       return true;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 38 */     BlockPos.MutableBlockPos pos = origin.mutable();
/* 39 */     for (Direction searchDirection : searchDirections) {
/* 40 */       pos.set((Vec3i)origin);
/* 41 */       List<Direction> placementDirections = config.getShuffledDirectionsExcept(random, searchDirection.getOpposite());
/* 42 */       for (int i = 0; i < config.searchRange; i++) {
/* 43 */         pos.setWithOffset((Vec3i)origin, searchDirection);
/* 44 */         BlockState state = level.getBlockState((BlockPos)pos);
/* 45 */         if (!isAirOrWater(state) && !state.is((Block)config.placeBlock)) {
/*    */           break;
/*    */         }
/*    */         
/* 49 */         if (placeGrowthIfPossible(level, (BlockPos)pos, state, config, random, placementDirections)) {
/* 50 */           return true;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 55 */     return false;
/*    */   }
/*    */   
/*    */   public static boolean placeGrowthIfPossible(WorldGenLevel level, BlockPos pos, BlockState oldState, MultifaceGrowthConfiguration config, RandomSource random, List<Direction> placementDirections) {
/* 59 */     BlockPos.MutableBlockPos mutable = pos.mutable();
/* 60 */     for (Direction placementDirection : placementDirections) {
/* 61 */       BlockState neighbourState = level.getBlockState((BlockPos)mutable.setWithOffset((Vec3i)pos, placementDirection));
/* 62 */       if (neighbourState.is(config.canBePlacedOn)) {
/* 63 */         BlockState newState = config.placeBlock.getStateForPlacement(oldState, (BlockGetter)level, pos, placementDirection);
/* 64 */         if (newState == null) {
/* 65 */           return false;
/*    */         }
/* 67 */         level.setBlock(pos, newState, 3);
/* 68 */         level.getChunk(pos).markPosForPostprocessing(pos);
/* 69 */         if (random.nextFloat() < config.chanceOfSpreading) {
/* 70 */           config.placeBlock.getSpreader().spreadFromFaceTowardRandomDirection(newState, (net.minecraft.world.level.LevelAccessor)level, pos, placementDirection, random, true);
/*    */         }
/* 72 */         return true;
/*    */       } 
/*    */     } 
/* 75 */     return false;
/*    */   }
/*    */   
/*    */   private static boolean isAirOrWater(BlockState state) {
/* 79 */     return (state.isAir() || state.is(Blocks.WATER));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/MultifaceGrowthFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */