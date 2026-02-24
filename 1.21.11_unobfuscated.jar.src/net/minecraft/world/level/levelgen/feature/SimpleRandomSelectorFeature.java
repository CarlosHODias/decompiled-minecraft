/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class SimpleRandomSelectorFeature extends Feature<SimpleRandomFeatureConfiguration> {
/*    */   public SimpleRandomSelectorFeature(Codec<SimpleRandomFeatureConfiguration> codec) {
/* 13 */     super(codec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<SimpleRandomFeatureConfiguration> context) {
/* 18 */     RandomSource random = context.random();
/* 19 */     SimpleRandomFeatureConfiguration config = context.config();
/* 20 */     WorldGenLevel level = context.level();
/* 21 */     BlockPos origin = context.origin();
/* 22 */     ChunkGenerator chunkGenerator = context.chunkGenerator();
/* 23 */     int index = random.nextInt(config.features.size());
/* 24 */     PlacedFeature feature = (PlacedFeature)config.features.get(index).value();
/* 25 */     return feature.place(level, chunkGenerator, random, origin);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/SimpleRandomSelectorFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */