/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ 
/*    */ public class MegaJungleTrunkPlacer extends GiantTrunkPlacer {
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(i -> trunkPlacerParts(i).apply((Applicative)i, MegaJungleTrunkPlacer::new));
/*    */   } public static final com.mojang.serialization.MapCodec<MegaJungleTrunkPlacer> CODEC;
/*    */   public MegaJungleTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
/* 21 */     super(baseHeight, heightRandA, heightRandB);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TrunkPlacerType<?> type() {
/* 26 */     return TrunkPlacerType.MEGA_JUNGLE_TRUNK_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config) {
/* 31 */     List<FoliagePlacer.FoliageAttachment> attachments = Lists.newArrayList();
/* 32 */     attachments.addAll(super.placeTrunk(level, trunkSetter, random, treeHeight, origin, config));
/*    */ 
/*    */     
/* 35 */     for (int branchHeight = treeHeight - 2 - random.nextInt(4); branchHeight > treeHeight / 2; branchHeight -= 2 + random.nextInt(4)) {
/* 36 */       float angle = random.nextFloat() * 6.2831855F;
/* 37 */       int bx = 0;
/* 38 */       int bz = 0;
/*    */       
/* 40 */       for (int b = 0; b < 5; b++) {
/* 41 */         bx = (int)(1.5F + Mth.cos(angle) * b);
/* 42 */         bz = (int)(1.5F + Mth.sin(angle) * b);
/* 43 */         BlockPos pos = origin.offset(bx, branchHeight - 3 + b / 2, bz);
/* 44 */         placeLog(level, trunkSetter, random, pos, config);
/*    */       } 
/*    */       
/* 47 */       attachments.add(new FoliagePlacer.FoliageAttachment(origin.offset(bx, branchHeight, bz), -2, false));
/*    */     } 
/*    */     
/* 50 */     return attachments;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/trunkplacers/MegaJungleTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */