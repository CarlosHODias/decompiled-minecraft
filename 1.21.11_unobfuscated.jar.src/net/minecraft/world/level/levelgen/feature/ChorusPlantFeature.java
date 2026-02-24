/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.ChorusFlowerBlock;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class ChorusPlantFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public ChorusPlantFeature(Codec<NoneFeatureConfiguration> codec) {
/* 13 */     super(codec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/* 18 */     WorldGenLevel level = context.level();
/* 19 */     BlockPos origin = context.origin();
/* 20 */     RandomSource random = context.random();
/* 21 */     if (level.isEmptyBlock(origin) && level.getBlockState(origin.below()).is(Blocks.END_STONE)) {
/* 22 */       ChorusFlowerBlock.generatePlant((LevelAccessor)level, origin, random, 8);
/* 23 */       return true;
/*    */     } 
/* 25 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/ChorusPlantFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */