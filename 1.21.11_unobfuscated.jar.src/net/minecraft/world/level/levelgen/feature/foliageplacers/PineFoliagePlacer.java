/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class PineFoliagePlacer extends FoliagePlacer {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(i -> foliagePlacerParts(i).and((App)IntProvider.codec(0, 24).fieldOf("height").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, PineFoliagePlacer::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<PineFoliagePlacer> CODEC;
/*    */   private final IntProvider height;
/*    */   
/*    */   public PineFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider height) {
/* 18 */     super(radius, offset);
/* 19 */     this.height = height;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 24 */     return FoliagePlacerType.PINE_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(net.minecraft.world.level.LevelSimulatedReader level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 29 */     int currentRadius = 0;
/*    */     
/* 31 */     for (int yo = offset; yo >= offset - foliageHeight; yo--) {
/* 32 */       placeLeavesRow(level, foliageSetter, random, config, foliageAttachment.pos(), currentRadius, yo, foliageAttachment.doubleTrunk());
/*    */       
/* 34 */       if (currentRadius >= 1 && yo == offset - foliageHeight + 1) {
/* 35 */         currentRadius--;
/* 36 */       } else if (currentRadius < leafRadius + foliageAttachment.radiusOffset()) {
/* 37 */         currentRadius++;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageRadius(RandomSource random, int trunkHeight) {
/* 44 */     return super.foliageRadius(random, trunkHeight) + random.nextInt(Math.max(trunkHeight + 1, 1));
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) {
/* 49 */     return this.height.sample(random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
/* 54 */     return (dx == currentRadius && dz == currentRadius && currentRadius > 0);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/foliageplacers/PineFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */