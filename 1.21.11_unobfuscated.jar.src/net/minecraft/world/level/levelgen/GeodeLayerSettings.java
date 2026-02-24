/*    */ package net.minecraft.world.level.levelgen;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public class GeodeLayerSettings {
/*  7 */   private static final Codec<Double> LAYER_RANGE = Codec.doubleRange(0.01D, 50.0D); public static final Codec<GeodeLayerSettings> CODEC; static {
/*  8 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)LAYER_RANGE.fieldOf("filling").orElse(1.7D).forGetter(()), (App)LAYER_RANGE.fieldOf("inner_layer").orElse(2.2D).forGetter(()), (App)LAYER_RANGE.fieldOf("middle_layer").orElse(3.2D).forGetter(()), (App)LAYER_RANGE.fieldOf("outer_layer").orElse(4.2D).forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, GeodeLayerSettings::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public final double filling;
/*    */   
/*    */   public final double innerLayer;
/*    */   
/*    */   public final double middleLayer;
/*    */   
/*    */   public final double outerLayer;
/*    */   
/*    */   public GeodeLayerSettings(double filling, double innerLayer, double middleLayer, double outerLayer) {
/* 21 */     this.filling = filling;
/* 22 */     this.innerLayer = innerLayer;
/* 23 */     this.middleLayer = middleLayer;
/* 24 */     this.outerLayer = outerLayer;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/GeodeLayerSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */