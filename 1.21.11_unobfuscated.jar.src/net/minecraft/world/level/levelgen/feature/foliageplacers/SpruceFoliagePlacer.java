/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class SpruceFoliagePlacer extends FoliagePlacer {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(i -> foliagePlacerParts(i).and((App)IntProvider.codec(0, 24).fieldOf("trunk_height").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, SpruceFoliagePlacer::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<SpruceFoliagePlacer> CODEC;
/*    */   private final IntProvider trunkHeight;
/*    */   
/*    */   public SpruceFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider trunkHeight) {
/* 19 */     super(radius, offset);
/* 20 */     this.trunkHeight = trunkHeight;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 25 */     return FoliagePlacerType.SPRUCE_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(net.minecraft.world.level.LevelSimulatedReader level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 30 */     BlockPos foliagePos = foliageAttachment.pos();
/*    */     
/* 32 */     int currentRadius = random.nextInt(2);
/* 33 */     int maxRadius = 1;
/* 34 */     int minRadius = 0;
/*    */     
/* 36 */     for (int yo = offset; yo >= -foliageHeight; yo--) {
/* 37 */       placeLeavesRow(level, foliageSetter, random, config, foliagePos, currentRadius, yo, foliageAttachment.doubleTrunk());
/*    */       
/* 39 */       if (currentRadius >= maxRadius) {
/* 40 */         currentRadius = minRadius;
/* 41 */         minRadius = 1;
/* 42 */         maxRadius = Math.min(maxRadius + 1, leafRadius + foliageAttachment.radiusOffset());
/*    */       } else {
/* 44 */         currentRadius++;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) {
/* 52 */     return Math.max(4, treeHeight - this.trunkHeight.sample(random));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
/* 57 */     return (dx == currentRadius && dz == currentRadius && currentRadius > 0);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/foliageplacers/SpruceFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */