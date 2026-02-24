/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class DarkOakFoliagePlacer extends FoliagePlacer {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(i -> foliagePlacerParts(i).apply((Applicative)i, DarkOakFoliagePlacer::new));
/*    */   } public static final com.mojang.serialization.MapCodec<DarkOakFoliagePlacer> CODEC;
/*    */   public DarkOakFoliagePlacer(IntProvider radius, IntProvider offset) {
/* 15 */     super(radius, offset);
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 20 */     return FoliagePlacerType.DARK_OAK_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 25 */     BlockPos pos = foliageAttachment.pos().above(offset);
/* 26 */     boolean doubleTrunk = foliageAttachment.doubleTrunk();
/*    */     
/* 28 */     if (doubleTrunk) {
/* 29 */       placeLeavesRow(level, foliageSetter, random, config, pos, leafRadius + 2, -1, doubleTrunk);
/* 30 */       placeLeavesRow(level, foliageSetter, random, config, pos, leafRadius + 3, 0, doubleTrunk);
/* 31 */       placeLeavesRow(level, foliageSetter, random, config, pos, leafRadius + 2, 1, doubleTrunk);
/* 32 */       if (random.nextBoolean()) {
/* 33 */         placeLeavesRow(level, foliageSetter, random, config, pos, leafRadius, 2, doubleTrunk);
/*    */       }
/*    */     } else {
/* 36 */       placeLeavesRow(level, foliageSetter, random, config, pos, leafRadius + 2, -1, doubleTrunk);
/* 37 */       placeLeavesRow(level, foliageSetter, random, config, pos, leafRadius + 1, 0, doubleTrunk);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) {
/* 43 */     return 4;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocationSigned(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
/* 48 */     if (y == 0 && doubleTrunk && (
/* 49 */       dx == -currentRadius || dx >= currentRadius) && (dz == -currentRadius || dz >= currentRadius)) {
/* 50 */       return true;
/*    */     }
/*    */     
/* 53 */     return super.shouldSkipLocationSigned(random, dx, y, dz, currentRadius, doubleTrunk);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
/* 58 */     if (y == -1 && !doubleTrunk) {
/* 59 */       return (dx == currentRadius && dz == currentRadius);
/*    */     }
/* 61 */     if (y == 1) {
/* 62 */       return (dx + dz > currentRadius * 2 - 2);
/*    */     }
/* 64 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/foliageplacers/DarkOakFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */