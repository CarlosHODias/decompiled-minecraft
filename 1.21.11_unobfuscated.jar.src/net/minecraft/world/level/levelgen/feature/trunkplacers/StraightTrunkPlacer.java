/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ 
/*    */ public class StraightTrunkPlacer extends TrunkPlacer {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(i -> trunkPlacerParts(i).apply((Applicative)i, StraightTrunkPlacer::new));
/*    */   } public static final com.mojang.serialization.MapCodec<StraightTrunkPlacer> CODEC;
/*    */   public StraightTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
/* 20 */     super(baseHeight, heightRandA, heightRandB);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TrunkPlacerType<?> type() {
/* 25 */     return TrunkPlacerType.STRAIGHT_TRUNK_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config) {
/* 30 */     setDirtAt(level, trunkSetter, random, origin.below(), config);
/*    */     
/* 32 */     for (int y = 0; y < treeHeight; y++) {
/* 33 */       placeLog(level, trunkSetter, random, origin.above(y), config);
/*    */     }
/* 35 */     return (List<FoliagePlacer.FoliageAttachment>)com.google.common.collect.ImmutableList.of(new FoliagePlacer.FoliageAttachment(origin.above(treeHeight), 0, false));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/trunkplacers/StraightTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */