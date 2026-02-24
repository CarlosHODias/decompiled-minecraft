/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ 
/*    */ public class PointedDripstoneConfiguration implements FeatureConfiguration {
/*    */   static {
/*  7 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((App)Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_taller_dripstone").orElse(0.2F).forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_directional_spread").orElse(0.7F).forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spread_radius2").orElse(0.5F).forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spread_radius3").orElse(0.5F).forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, PointedDripstoneConfiguration::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Codec<PointedDripstoneConfiguration> CODEC;
/*    */   
/*    */   public final float chanceOfTallerDripstone;
/*    */   
/*    */   public final float chanceOfDirectionalSpread;
/*    */   
/*    */   public final float chanceOfSpreadRadius2;
/*    */   
/*    */   public final float chanceOfSpreadRadius3;
/*    */ 
/*    */   
/*    */   public PointedDripstoneConfiguration(float chanceOfTallerDripstone, float chanceOfDirectionalSpread, float chanceOfSpreadRadius2, float chanceOfSpreadRadius3) {
/* 24 */     this.chanceOfTallerDripstone = chanceOfTallerDripstone;
/* 25 */     this.chanceOfDirectionalSpread = chanceOfDirectionalSpread;
/* 26 */     this.chanceOfSpreadRadius2 = chanceOfSpreadRadius2;
/* 27 */     this.chanceOfSpreadRadius3 = chanceOfSpreadRadius3;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/PointedDripstoneConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */