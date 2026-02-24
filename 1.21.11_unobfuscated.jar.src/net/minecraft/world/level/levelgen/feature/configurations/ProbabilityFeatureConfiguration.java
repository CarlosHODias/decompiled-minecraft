/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public class ProbabilityFeatureConfiguration implements FeatureConfiguration {
/*    */   static {
/*  7 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)com.mojang.serialization.Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, ProbabilityFeatureConfiguration::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.Codec<ProbabilityFeatureConfiguration> CODEC;
/*    */   public final float probability;
/*    */   
/*    */   public ProbabilityFeatureConfiguration(float probability) {
/* 14 */     this.probability = probability;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */