/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
/*    */ 
/*    */ public class CaveCarverConfiguration extends CarverConfiguration {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)CarverConfiguration.CODEC.forGetter(()), (App)FloatProvider.CODEC.fieldOf("horizontal_radius_multiplier").forGetter(()), (App)FloatProvider.CODEC.fieldOf("vertical_radius_multiplier").forGetter(()), (App)FloatProvider.codec(-1.0F, 1.0F).fieldOf("floor_level").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, CaveCarverConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.Codec<CaveCarverConfiguration> CODEC;
/*    */   
/*    */   public final FloatProvider horizontalRadiusMultiplier;
/*    */   
/*    */   public final FloatProvider verticalRadiusMultiplier;
/*    */   
/*    */   final FloatProvider floorLevel;
/*    */ 
/*    */   
/*    */   public CaveCarverConfiguration(float probability, HeightProvider y, FloatProvider yScale, VerticalAnchor lavaLevel, CarverDebugSettings debugSettings, HolderSet<Block> replaceable, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, FloatProvider floorLevel) {
/* 26 */     super(probability, y, yScale, lavaLevel, debugSettings, replaceable);
/* 27 */     this.horizontalRadiusMultiplier = horizontalRadiusMultiplier;
/* 28 */     this.verticalRadiusMultiplier = verticalRadiusMultiplier;
/* 29 */     this.floorLevel = floorLevel;
/*    */   }
/*    */   
/*    */   public CaveCarverConfiguration(float probability, HeightProvider y, FloatProvider yScale, VerticalAnchor lavaLevel, HolderSet<Block> replaceable, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, FloatProvider floorLevel) {
/* 33 */     this(probability, y, yScale, lavaLevel, CarverDebugSettings.DEFAULT, replaceable, horizontalRadiusMultiplier, verticalRadiusMultiplier, floorLevel);
/*    */   }
/*    */   
/*    */   public CaveCarverConfiguration(CarverConfiguration carver, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, FloatProvider floorLevel) {
/* 37 */     this(carver.probability, carver.y, carver.yScale, carver.lavaLevel, carver.debugSettings, carver.replaceable, horizontalRadiusMultiplier, verticalRadiusMultiplier, floorLevel);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/carver/CaveCarverConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */