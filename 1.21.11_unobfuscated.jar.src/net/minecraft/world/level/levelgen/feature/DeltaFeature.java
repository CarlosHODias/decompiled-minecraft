/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelWriter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
/*    */ 
/*    */ public class DeltaFeature extends Feature<DeltaFeatureConfiguration> {
/* 16 */   private static final ImmutableList<net.minecraft.world.level.block.Block> CANNOT_REPLACE = ImmutableList.of(Blocks.BEDROCK, Blocks.NETHER_BRICKS, Blocks.NETHER_BRICK_FENCE, Blocks.NETHER_BRICK_STAIRS, Blocks.NETHER_WART, Blocks.CHEST, Blocks.SPAWNER);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   private static final Direction[] DIRECTIONS = Direction.values();
/*    */   private static final double RIM_SPAWN_CHANCE = 0.9D;
/*    */   
/*    */   public DeltaFeature(Codec<DeltaFeatureConfiguration> codec) {
/* 27 */     super(codec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<DeltaFeatureConfiguration> context) {
/*    */     boolean anyPlaced = false;
/* 33 */     RandomSource random = context.random();
/* 34 */     WorldGenLevel level = context.level();
/* 35 */     DeltaFeatureConfiguration config = context.config();
/* 36 */     BlockPos origin = context.origin();
/* 37 */     boolean spawnRim = (random.nextDouble() < 0.9D);
/* 38 */     int rimX = spawnRim ? config.rimSize().sample(random) : 0;
/* 39 */     int rimZ = spawnRim ? config.rimSize().sample(random) : 0;
/* 40 */     boolean hasRim = (spawnRim && rimX != 0 && rimZ != 0);
/*    */     
/* 42 */     int radiusX = config.size().sample(random);
/* 43 */     int radiusZ = config.size().sample(random);
/* 44 */     int radiusLimit = Math.max(radiusX, radiusZ);
/* 45 */     for (BlockPos pos : (Iterable<BlockPos>)BlockPos.withinManhattan(origin, radiusX, 0, radiusZ)) {
/* 46 */       if (pos.distManhattan((Vec3i)origin) > radiusLimit) {
/*    */         break;
/*    */       }
/*    */       
/* 50 */       if (isClear((LevelAccessor)level, pos, config)) {
/* 51 */         if (hasRim) {
/* 52 */           anyPlaced = true;
/* 53 */           setBlock((LevelWriter)level, pos, config.rim());
/*    */         } 
/*    */         
/* 56 */         BlockPos posOffset = pos.offset(rimX, 0, rimZ);
/* 57 */         if (isClear((LevelAccessor)level, posOffset, config)) {
/* 58 */           anyPlaced = true;
/* 59 */           setBlock((LevelWriter)level, posOffset, config.contents());
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 64 */     return anyPlaced;
/*    */   }
/*    */   
/*    */   private static boolean isClear(LevelAccessor level, BlockPos pos, DeltaFeatureConfiguration config) {
/* 68 */     BlockState state = level.getBlockState(pos);
/* 69 */     if (state.is(config.contents().getBlock())) {
/* 70 */       return false;
/*    */     }
/*    */     
/* 73 */     if (CANNOT_REPLACE.contains(state.getBlock())) {
/* 74 */       return false;
/*    */     }
/*    */     
/* 77 */     for (Direction d : DIRECTIONS) {
/* 78 */       boolean isAir = level.getBlockState(pos.relative(d)).isAir();
/* 79 */       if ((isAir && d != Direction.UP) || (!isAir && d == Direction.UP)) {
/* 80 */         return false;
/*    */       }
/*    */     } 
/* 83 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/DeltaFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */