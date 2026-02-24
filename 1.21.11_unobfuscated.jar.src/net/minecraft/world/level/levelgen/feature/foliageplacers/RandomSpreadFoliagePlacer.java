/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class RandomSpreadFoliagePlacer extends FoliagePlacer {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> foliagePlacerParts(i).and(i.group((App)IntProvider.codec(1, 512).fieldOf("foliage_height").forGetter(()), (App)com.mojang.serialization.Codec.intRange(0, 256).fieldOf("leaf_placement_attempts").forGetter(()))).apply((com.mojang.datafixers.kinds.Applicative)i, RandomSpreadFoliagePlacer::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<RandomSpreadFoliagePlacer> CODEC;
/*    */   
/*    */   private final IntProvider foliageHeight;
/*    */   
/*    */   private final int leafPlacementAttempts;
/*    */   
/*    */   public RandomSpreadFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider foliageHeight, int leafPlacementAttempts) {
/* 24 */     super(radius, offset);
/*    */     
/* 26 */     this.foliageHeight = foliageHeight;
/* 27 */     this.leafPlacementAttempts = leafPlacementAttempts;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 32 */     return FoliagePlacerType.RANDOM_SPREAD_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 37 */     BlockPos origin = foliageAttachment.pos();
/* 38 */     BlockPos.MutableBlockPos pos = origin.mutable();
/*    */     
/* 40 */     for (int i = 0; i < this.leafPlacementAttempts; i++) {
/* 41 */       pos.setWithOffset((net.minecraft.core.Vec3i)origin, random.nextInt(leafRadius) - random.nextInt(leafRadius), random.nextInt(foliageHeight) - random.nextInt(foliageHeight), random.nextInt(leafRadius) - random.nextInt(leafRadius));
/* 42 */       tryPlaceLeaf(level, foliageSetter, random, config, (BlockPos)pos);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) {
/* 48 */     return this.foliageHeight.sample(random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
/* 53 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/foliageplacers/RandomSpreadFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */