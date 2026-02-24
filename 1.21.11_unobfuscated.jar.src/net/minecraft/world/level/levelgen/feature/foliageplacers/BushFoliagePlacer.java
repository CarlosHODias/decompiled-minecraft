/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class BushFoliagePlacer extends BlobFoliagePlacer {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(i -> blobParts(i).apply((com.mojang.datafixers.kinds.Applicative)i, BushFoliagePlacer::new));
/*    */   } public static final com.mojang.serialization.MapCodec<BushFoliagePlacer> CODEC;
/*    */   public BushFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
/* 14 */     super(radius, offset, height);
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 19 */     return FoliagePlacerType.BUSH_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 24 */     for (int yo = offset; yo >= offset - foliageHeight; yo--) {
/* 25 */       int currentRadius = leafRadius + foliageAttachment.radiusOffset() - 1 - yo;
/* 26 */       placeLeavesRow(level, foliageSetter, random, config, foliageAttachment.pos(), currentRadius, yo, foliageAttachment.doubleTrunk());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
/* 32 */     return (dx == currentRadius && dz == currentRadius && random.nextInt(2) == 0);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/foliageplacers/BushFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */