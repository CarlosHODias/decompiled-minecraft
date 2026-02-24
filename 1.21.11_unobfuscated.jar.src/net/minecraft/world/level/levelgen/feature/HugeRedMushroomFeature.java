/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.HugeMushroomBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
/*    */ 
/*    */ public class HugeRedMushroomFeature extends AbstractHugeMushroomFeature {
/*    */   public HugeRedMushroomFeature(Codec<HugeMushroomFeatureConfiguration> codec) {
/* 13 */     super(codec);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void makeCap(LevelAccessor level, RandomSource random, BlockPos origin, int treeHeight, BlockPos.MutableBlockPos blockPos, HugeMushroomFeatureConfiguration config) {
/* 18 */     for (int dy = treeHeight - 3; dy <= treeHeight; dy++) {
/* 19 */       int radius = (dy < treeHeight) ? config.foliageRadius : (config.foliageRadius - 1);
/* 20 */       int center = config.foliageRadius - 2;
/*    */       
/* 22 */       for (int dx = -radius; dx <= radius; dx++) {
/* 23 */         for (int dz = -radius; dz <= radius; dz++) {
/* 24 */           boolean minX = (dx == -radius);
/* 25 */           boolean maxX = (dx == radius);
/* 26 */           boolean minZ = (dz == -radius);
/* 27 */           boolean maxZ = (dz == radius);
/*    */           
/* 29 */           boolean xEdge = (minX || maxX);
/* 30 */           boolean zEdge = (minZ || maxZ);
/*    */           
/* 32 */           if (dy >= treeHeight || xEdge != zEdge) {
/*    */ 
/*    */ 
/*    */             
/* 36 */             blockPos.setWithOffset((net.minecraft.core.Vec3i)origin, dx, dy, dz);
/* 37 */             BlockState state = config.capProvider.getState(random, origin);
/* 38 */             if (state.hasProperty((Property)HugeMushroomBlock.WEST) && 
/* 39 */               state.hasProperty((Property)HugeMushroomBlock.EAST) && 
/* 40 */               state.hasProperty((Property)HugeMushroomBlock.NORTH) && 
/* 41 */               state.hasProperty((Property)HugeMushroomBlock.SOUTH) && 
/* 42 */               state.hasProperty((Property)HugeMushroomBlock.UP))
/*    */             {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */               
/* 49 */               state = (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)state.setValue((Property)HugeMushroomBlock.UP, (dy >= treeHeight - 1))).setValue((Property)HugeMushroomBlock.WEST, (dx < -center))).setValue((Property)HugeMushroomBlock.EAST, (dx > center))).setValue((Property)HugeMushroomBlock.NORTH, (dz < -center))).setValue((Property)HugeMushroomBlock.SOUTH, (dz > center));
/*    */             }
/* 51 */             placeMushroomBlock(level, blockPos, state);
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected int getTreeRadiusForHeight(int trunkHeight, int treeHeight, int leafRadius, int yo) {
/* 59 */     int radius = 0;
/* 60 */     if (yo < treeHeight && yo >= treeHeight - 3) {
/* 61 */       radius = leafRadius;
/* 62 */     } else if (yo == treeHeight) {
/* 63 */       radius = leafRadius;
/*    */     } 
/* 65 */     return radius;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/HugeRedMushroomFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */