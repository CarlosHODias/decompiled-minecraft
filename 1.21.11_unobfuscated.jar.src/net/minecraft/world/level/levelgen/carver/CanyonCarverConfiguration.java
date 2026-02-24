/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ 
/*    */ public class CanyonCarverConfiguration extends CarverConfiguration {
/*    */   public static final Codec<CanyonCarverConfiguration> CODEC;
/*    */   public final FloatProvider verticalRotation;
/*    */   public final CanyonShapeConfiguration shape;
/*    */   
/*    */   public static class CanyonShapeConfiguration {
/*    */     static {
/* 14 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)FloatProvider.CODEC.fieldOf("distance_factor").forGetter(()), (App)FloatProvider.CODEC.fieldOf("thickness").forGetter(()), (App)net.minecraft.util.ExtraCodecs.POSITIVE_INT.fieldOf("width_smoothness").forGetter(()), (App)FloatProvider.CODEC.fieldOf("horizontal_radius_factor").forGetter(()), (App)Codec.FLOAT.fieldOf("vertical_radius_default_factor").forGetter(()), (App)Codec.FLOAT.fieldOf("vertical_radius_center_factor").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, CanyonShapeConfiguration::new));
/*    */     }
/*    */ 
/*    */     
/*    */     public static final Codec<CanyonShapeConfiguration> CODEC;
/*    */     
/*    */     public final FloatProvider distanceFactor;
/*    */     
/*    */     public final FloatProvider thickness;
/*    */     
/*    */     public final int widthSmoothness;
/*    */     
/*    */     public final FloatProvider horizontalRadiusFactor;
/*    */     public final float verticalRadiusDefaultFactor;
/*    */     public final float verticalRadiusCenterFactor;
/*    */     
/*    */     public CanyonShapeConfiguration(FloatProvider distanceFactor, FloatProvider thickness, int widthSmoothness, FloatProvider horizontalRadiusFactor, float verticalRadiusDefaultFactor, float verticalRadiusCenterFactor) {
/* 31 */       this.widthSmoothness = widthSmoothness;
/* 32 */       this.horizontalRadiusFactor = horizontalRadiusFactor;
/* 33 */       this.verticalRadiusDefaultFactor = verticalRadiusDefaultFactor;
/* 34 */       this.verticalRadiusCenterFactor = verticalRadiusCenterFactor;
/* 35 */       this.distanceFactor = distanceFactor;
/* 36 */       this.thickness = thickness;
/*    */     } }
/*    */   
/*    */   static {
/* 40 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)CarverConfiguration.CODEC.forGetter(()), (App)FloatProvider.CODEC.fieldOf("vertical_rotation").forGetter(()), (App)CanyonShapeConfiguration.CODEC.fieldOf("shape").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, CanyonCarverConfiguration::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CanyonCarverConfiguration(float probability, net.minecraft.world.level.levelgen.heightproviders.HeightProvider y, FloatProvider yScale, net.minecraft.world.level.levelgen.VerticalAnchor lavaLevel, CarverDebugSettings debugSettings, net.minecraft.core.HolderSet<net.minecraft.world.level.block.Block> replaceable, FloatProvider verticalRotation, CanyonShapeConfiguration shape) {
/* 50 */     super(probability, y, yScale, lavaLevel, debugSettings, replaceable);
/* 51 */     this.verticalRotation = verticalRotation;
/* 52 */     this.shape = shape;
/*    */   }
/*    */   
/*    */   public CanyonCarverConfiguration(CarverConfiguration carver, FloatProvider distanceFactor, CanyonShapeConfiguration shape) {
/* 56 */     this(carver.probability, carver.y, carver.yScale, carver.lavaLevel, carver.debugSettings, carver.replaceable, distanceFactor, shape);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/carver/CanyonCarverConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */