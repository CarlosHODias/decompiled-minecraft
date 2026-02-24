/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class CoralTreeFeature extends CoralFeature {
/*    */   public CoralTreeFeature(Codec<NoneFeatureConfiguration> codec) {
/* 15 */     super(codec);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean placeFeature(LevelAccessor level, RandomSource random, BlockPos origin, BlockState state) {
/* 20 */     BlockPos.MutableBlockPos mutPos = origin.mutable();
/*    */     
/* 22 */     int trunckHeight = random.nextInt(3) + 1;
/* 23 */     for (int i = 0; i < trunckHeight; i++) {
/* 24 */       if (!placeCoralBlock(level, random, (BlockPos)mutPos, state)) {
/* 25 */         return true;
/*    */       }
/* 27 */       mutPos.move(Direction.UP);
/*    */     } 
/* 29 */     BlockPos trunckTopPos = mutPos.immutable();
/*    */     
/* 31 */     int nBranches = random.nextInt(3) + 2;
/* 32 */     List<Direction> directions = Direction.Plane.HORIZONTAL.shuffledCopy(random);
/* 33 */     List<Direction> branchDirections = directions.subList(0, nBranches);
/*    */     
/* 35 */     for (Direction branchDirection : branchDirections) {
/* 36 */       mutPos.set((Vec3i)trunckTopPos);
/* 37 */       mutPos.move(branchDirection);
/*    */       
/* 39 */       int branchHeight = random.nextInt(5) + 2;
/* 40 */       int segmentLength = 0;
/* 41 */       for (int j = 0; j < branchHeight && 
/* 42 */         placeCoralBlock(level, random, (BlockPos)mutPos, state); j++) {
/*    */ 
/*    */         
/* 45 */         segmentLength++;
/* 46 */         mutPos.move(Direction.UP);
/*    */         
/* 48 */         if (j == 0 || (segmentLength >= 2 && random.nextFloat() < 0.25F)) {
/* 49 */           mutPos.move(branchDirection);
/* 50 */           segmentLength = 0;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 55 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/CoralTreeFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */