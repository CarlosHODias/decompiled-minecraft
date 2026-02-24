/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class BasaltPillarFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public BasaltPillarFeature(Codec<NoneFeatureConfiguration> codec) {
/* 16 */     super(codec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/* 21 */     BlockPos origin = context.origin();
/* 22 */     WorldGenLevel level = context.level();
/* 23 */     RandomSource random = context.random();
/* 24 */     if (!level.isEmptyBlock(origin) || level.isEmptyBlock(origin.above())) {
/* 25 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 29 */     BlockPos.MutableBlockPos pos = origin.mutable();
/* 30 */     BlockPos.MutableBlockPos tmpPos = origin.mutable();
/*    */     
/*    */     boolean placeNorthHangoff = true;
/*    */     boolean placeSouthHangoff = true;
/*    */     boolean placeWestHangoff = true;
/*    */     boolean placeEastHangoff = true;
/* 36 */     while (level.isEmptyBlock((BlockPos)pos)) {
/* 37 */       if (level.isOutsideBuildHeight((BlockPos)pos)) {
/* 38 */         return true;
/*    */       }
/*    */       
/* 41 */       level.setBlock((BlockPos)pos, Blocks.BASALT.defaultBlockState(), 2);
/*    */       
/* 43 */       placeNorthHangoff = (placeNorthHangoff && placeHangOff((LevelAccessor)level, random, (BlockPos)tmpPos.setWithOffset((Vec3i)pos, Direction.NORTH)));
/* 44 */       placeSouthHangoff = (placeSouthHangoff && placeHangOff((LevelAccessor)level, random, (BlockPos)tmpPos.setWithOffset((Vec3i)pos, Direction.SOUTH)));
/* 45 */       placeWestHangoff = (placeWestHangoff && placeHangOff((LevelAccessor)level, random, (BlockPos)tmpPos.setWithOffset((Vec3i)pos, Direction.WEST)));
/* 46 */       placeEastHangoff = (placeEastHangoff && placeHangOff((LevelAccessor)level, random, (BlockPos)tmpPos.setWithOffset((Vec3i)pos, Direction.EAST)));
/*    */       
/* 48 */       pos.move(Direction.DOWN);
/*    */     } 
/*    */ 
/*    */     
/* 52 */     pos.move(Direction.UP);
/* 53 */     placeBaseHangOff((LevelAccessor)level, random, (BlockPos)tmpPos.setWithOffset((Vec3i)pos, Direction.NORTH));
/* 54 */     placeBaseHangOff((LevelAccessor)level, random, (BlockPos)tmpPos.setWithOffset((Vec3i)pos, Direction.SOUTH));
/* 55 */     placeBaseHangOff((LevelAccessor)level, random, (BlockPos)tmpPos.setWithOffset((Vec3i)pos, Direction.WEST));
/* 56 */     placeBaseHangOff((LevelAccessor)level, random, (BlockPos)tmpPos.setWithOffset((Vec3i)pos, Direction.EAST));
/* 57 */     pos.move(Direction.DOWN);
/*    */     
/* 59 */     BlockPos.MutableBlockPos basePos = new BlockPos.MutableBlockPos();
/* 60 */     for (int dx = -3; dx < 4; dx++) {
/* 61 */       for (int dz = -3; dz < 4; dz++) {
/* 62 */         int probability = Mth.abs(dx) * Mth.abs(dz);
/* 63 */         if (random.nextInt(10) < 10 - probability) {
/*    */ 
/*    */ 
/*    */           
/* 67 */           basePos.set((Vec3i)pos.offset(dx, 0, dz));
/* 68 */           int maxDrop = 3;
/* 69 */           while (level.isEmptyBlock((BlockPos)tmpPos.setWithOffset((Vec3i)basePos, Direction.DOWN))) {
/* 70 */             basePos.move(Direction.DOWN);
/* 71 */             maxDrop--;
/* 72 */             if (maxDrop <= 0) {
/*    */               break;
/*    */             }
/*    */           } 
/*    */           
/* 77 */           if (!level.isEmptyBlock((BlockPos)tmpPos.setWithOffset((Vec3i)basePos, Direction.DOWN))) {
/* 78 */             level.setBlock((BlockPos)basePos, Blocks.BASALT.defaultBlockState(), 2);
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/* 83 */     return true;
/*    */   }
/*    */   
/*    */   private void placeBaseHangOff(LevelAccessor level, RandomSource random, BlockPos pos) {
/* 87 */     if (random.nextBoolean()) {
/* 88 */       level.setBlock(pos, Blocks.BASALT.defaultBlockState(), 2);
/*    */     }
/*    */   }
/*    */   
/*    */   private boolean placeHangOff(LevelAccessor level, RandomSource random, BlockPos hangOffPos) {
/* 93 */     if (random.nextInt(10) != 0) {
/* 94 */       level.setBlock(hangOffPos, Blocks.BASALT.defaultBlockState(), 2);
/* 95 */       return true;
/*    */     } 
/*    */     
/* 98 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/BasaltPillarFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */