/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.GrowingPlantHeadBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TwistingVinesConfig;
/*    */ 
/*    */ public class TwistingVinesFeature extends Feature<TwistingVinesConfig> {
/*    */   public TwistingVinesFeature(Codec<TwistingVinesConfig> codec) {
/* 18 */     super(codec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<TwistingVinesConfig> context) {
/* 23 */     WorldGenLevel level = context.level();
/* 24 */     BlockPos origin = context.origin();
/* 25 */     if (isInvalidPlacementLocation((LevelAccessor)level, origin)) {
/* 26 */       return false;
/*    */     }
/*    */     
/* 29 */     RandomSource random = context.random();
/* 30 */     TwistingVinesConfig config = context.config();
/* 31 */     int spreadWidth = config.spreadWidth();
/* 32 */     int spreadHeight = config.spreadHeight();
/* 33 */     int maxHeight = config.maxHeight();
/*    */     
/* 35 */     BlockPos.MutableBlockPos placePos = new BlockPos.MutableBlockPos();
/*    */     
/* 37 */     for (int i = 0; i < spreadWidth * spreadWidth; i++) {
/* 38 */       placePos.set((Vec3i)origin).move(
/* 39 */           Mth.nextInt(random, -spreadWidth, spreadWidth), 
/* 40 */           Mth.nextInt(random, -spreadHeight, spreadHeight), 
/* 41 */           Mth.nextInt(random, -spreadWidth, spreadWidth));
/*    */ 
/*    */       
/* 44 */       if (findFirstAirBlockAboveGround((LevelAccessor)level, placePos))
/*    */       {
/*    */ 
/*    */         
/* 48 */         if (!isInvalidPlacementLocation((LevelAccessor)level, (BlockPos)placePos)) {
/*    */ 
/*    */ 
/*    */           
/* 52 */           int vineHeight = Mth.nextInt(random, 1, maxHeight);
/* 53 */           if (random.nextInt(6) == 0) {
/* 54 */             vineHeight *= 2;
/*    */           }
/* 56 */           if (random.nextInt(5) == 0) {
/* 57 */             vineHeight = 1;
/*    */           }
/*    */           
/* 60 */           int minAge = 17;
/* 61 */           int maxAge = 25;
/* 62 */           placeWeepingVinesColumn((LevelAccessor)level, random, placePos, vineHeight, 17, 25);
/*    */         }  } 
/* 64 */     }  return true;
/*    */   }
/*    */   
/*    */   private static boolean findFirstAirBlockAboveGround(LevelAccessor level, BlockPos.MutableBlockPos placePos) {
/*    */     while (true) {
/* 69 */       placePos.move(0, -1, 0);
/* 70 */       if (level.isOutsideBuildHeight((BlockPos)placePos)) {
/* 71 */         return false;
/*    */       }
/* 73 */       if (!level.getBlockState((BlockPos)placePos).isAir()) {
/* 74 */         placePos.move(0, 1, 0);
/* 75 */         return true;
/*    */       } 
/*    */     } 
/*    */   } public static void placeWeepingVinesColumn(LevelAccessor level, RandomSource random, BlockPos.MutableBlockPos placePos, int totalHeight, int minAge, int naxAge) {
/* 79 */     for (int height = 1; height <= totalHeight; height++) {
/* 80 */       if (level.isEmptyBlock((BlockPos)placePos)) {
/* 81 */         if (height == totalHeight || !level.isEmptyBlock(placePos.above())) {
/* 82 */           level.setBlock((BlockPos)placePos, (BlockState)Blocks.TWISTING_VINES.defaultBlockState().setValue((Property)GrowingPlantHeadBlock.AGE, Mth.nextInt(random, minAge, naxAge)), 2);
/*    */           break;
/*    */         } 
/* 85 */         level.setBlock((BlockPos)placePos, Blocks.TWISTING_VINES_PLANT.defaultBlockState(), 2);
/*    */       } 
/*    */ 
/*    */       
/* 89 */       placePos.move(Direction.UP);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static boolean isInvalidPlacementLocation(LevelAccessor level, BlockPos pos) {
/* 94 */     if (!level.isEmptyBlock(pos)) {
/* 95 */       return true;
/*    */     }
/*    */     
/* 98 */     BlockState stateBelow = level.getBlockState(pos.below());
/* 99 */     return (!stateBelow.is(Blocks.NETHERRACK) && !stateBelow.is(Blocks.WARPED_NYLIUM) && !stateBelow.is(Blocks.WARPED_WART_BLOCK));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/TwistingVinesFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */