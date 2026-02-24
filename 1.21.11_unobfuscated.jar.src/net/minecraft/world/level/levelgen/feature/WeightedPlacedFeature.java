/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class WeightedPlacedFeature {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)PlacedFeature.CODEC.fieldOf("feature").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("chance").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, WeightedPlacedFeature::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<WeightedPlacedFeature> CODEC;
/*    */   public final Holder<PlacedFeature> feature;
/*    */   public final float chance;
/*    */   
/*    */   public WeightedPlacedFeature(Holder<PlacedFeature> feature, float chance) {
/* 22 */     this.feature = feature;
/* 23 */     this.chance = chance;
/*    */   }
/*    */   
/*    */   public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, net.minecraft.util.RandomSource random, BlockPos origin) {
/* 27 */     return ((PlacedFeature)this.feature.value()).place(level, chunkGenerator, random, origin);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/WeightedPlacedFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */