/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
/*    */ 
/*    */ public class CarverConfiguration extends net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(()), (App)HeightProvider.CODEC.fieldOf("y").forGetter(()), (App)FloatProvider.CODEC.fieldOf("yScale").forGetter(()), (App)VerticalAnchor.CODEC.fieldOf("lava_level").forGetter(()), (App)CarverDebugSettings.CODEC.optionalFieldOf("debug_settings", CarverDebugSettings.DEFAULT).forGetter(()), (App)net.minecraft.core.RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("replaceable").forGetter(())).apply((Applicative)i, CarverConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<CarverConfiguration> CODEC;
/*    */   
/*    */   public final HeightProvider y;
/*    */   
/*    */   public final FloatProvider yScale;
/*    */   
/*    */   public final VerticalAnchor lavaLevel;
/*    */   
/*    */   public final CarverDebugSettings debugSettings;
/*    */   
/*    */   public final HolderSet<Block> replaceable;
/*    */   
/*    */   public CarverConfiguration(float probability, HeightProvider y, FloatProvider yScale, VerticalAnchor lavaLevel, CarverDebugSettings debugSettings, HolderSet<Block> replaceable) {
/* 33 */     super(probability);
/* 34 */     this.y = y;
/* 35 */     this.yScale = yScale;
/* 36 */     this.lavaLevel = lavaLevel;
/* 37 */     this.debugSettings = debugSettings;
/* 38 */     this.replaceable = replaceable;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/carver/CarverConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */