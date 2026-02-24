/*   */ package net.minecraft.world.level.levelgen.feature.configurations;
/*   */ 
/*   */ import com.mojang.serialization.Codec;
/*   */ import com.mojang.serialization.MapCodec;
/*   */ 
/*   */ public class NoneFeatureConfiguration implements FeatureConfiguration {
/* 7 */   public static final NoneFeatureConfiguration INSTANCE = new NoneFeatureConfiguration();
/*   */   
/* 9 */   public static final Codec<NoneFeatureConfiguration> CODEC = MapCodec.unitCodec(INSTANCE);
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/NoneFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */