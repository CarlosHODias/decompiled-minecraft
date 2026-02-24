/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class BlobFoliagePlacer extends FoliagePlacer {
/*    */   public static final com.mojang.serialization.MapCodec<BlobFoliagePlacer> CODEC;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> blobParts(i).apply((com.mojang.datafixers.kinds.Applicative)i, BlobFoliagePlacer::new));
/*    */   } protected final int height;
/*    */   protected static <P extends BlobFoliagePlacer> com.mojang.datafixers.Products.P3<RecordCodecBuilder.Mu<P>, IntProvider, IntProvider, Integer> blobParts(RecordCodecBuilder.Instance<P> instance) {
/* 16 */     return foliagePlacerParts((RecordCodecBuilder.Instance)instance).and(
/* 17 */         (App)com.mojang.serialization.Codec.intRange(0, 16).fieldOf("height").forGetter(p -> p.height));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlobFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
/* 24 */     super(radius, offset);
/* 25 */     this.height = height;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 30 */     return FoliagePlacerType.BLOB_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(net.minecraft.world.level.LevelSimulatedReader level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 35 */     for (int yo = offset; yo >= offset - foliageHeight; yo--) {
/* 36 */       int currentRadius = Math.max(leafRadius + foliageAttachment.radiusOffset() - 1 - yo / 2, 0);
/* 37 */       placeLeavesRow(level, foliageSetter, random, config, foliageAttachment.pos(), currentRadius, yo, foliageAttachment.doubleTrunk());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) {
/* 43 */     return this.height;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
/* 48 */     return (dx == currentRadius && dz == currentRadius && (random.nextInt(2) == 0 || y == 0));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/foliageplacers/BlobFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */