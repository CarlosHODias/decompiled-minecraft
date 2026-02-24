/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.TreeFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ 
/*    */ public class BendingTrunkPlacer extends TrunkPlacer {
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.mapCodec(i -> trunkPlacerParts(i).and(i.group((App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("min_height_for_leaves", 1).forGetter(()), (App)IntProvider.codec(1, 64).fieldOf("bend_length").forGetter(()))).apply((Applicative)i, BendingTrunkPlacer::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<BendingTrunkPlacer> CODEC;
/*    */   
/*    */   private final int minHeightForLeaves;
/*    */   
/*    */   private final IntProvider bendLength;
/*    */   
/*    */   public BendingTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, int minHeightForLeaves, IntProvider bendLength) {
/* 32 */     super(baseHeight, heightRandA, heightRandB);
/*    */     
/* 34 */     this.minHeightForLeaves = minHeightForLeaves;
/* 35 */     this.bendLength = bendLength;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TrunkPlacerType<?> type() {
/* 40 */     return TrunkPlacerType.BENDING_TRUNK_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config) {
/* 45 */     Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/* 46 */     int logHeight = treeHeight - 1;
/* 47 */     BlockPos.MutableBlockPos pos = origin.mutable();
/* 48 */     BlockPos belowPos = pos.below();
/*    */     
/* 50 */     setDirtAt(level, trunkSetter, random, belowPos, config);
/* 51 */     List<FoliagePlacer.FoliageAttachment> foliagePoints = com.google.common.collect.Lists.newArrayList();
/*    */     
/* 53 */     for (int i = 0; i <= logHeight; i++) {
/*    */       
/* 55 */       if (i + 1 >= logHeight + random.nextInt(2)) {
/* 56 */         pos.move(direction);
/*    */       }
/*    */       
/* 59 */       if (TreeFeature.validTreePos(level, (BlockPos)pos)) {
/* 60 */         placeLog(level, trunkSetter, random, (BlockPos)pos, config);
/*    */       }
/*    */       
/* 63 */       if (i >= this.minHeightForLeaves) {
/* 64 */         foliagePoints.add(new FoliagePlacer.FoliageAttachment(pos.immutable(), 0, false));
/*    */       }
/*    */       
/* 67 */       pos.move(Direction.UP);
/*    */     } 
/*    */ 
/*    */     
/* 71 */     int dirLength = this.bendLength.sample(random);
/* 72 */     for (int j = 0; j <= dirLength; j++) {
/* 73 */       if (TreeFeature.validTreePos(level, (BlockPos)pos)) {
/* 74 */         placeLog(level, trunkSetter, random, (BlockPos)pos, config);
/*    */       }
/*    */       
/* 77 */       foliagePoints.add(new FoliagePlacer.FoliageAttachment(pos.immutable(), 0, false));
/* 78 */       pos.move(direction);
/*    */     } 
/*    */     
/* 81 */     return foliagePoints;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/trunkplacers/BendingTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */