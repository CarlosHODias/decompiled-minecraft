/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class HugeMushroomFeatureConfiguration implements FeatureConfiguration {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockStateProvider.CODEC.fieldOf("cap_provider").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("stem_provider").forGetter(()), (App)com.mojang.serialization.Codec.INT.fieldOf("foliage_radius").orElse(2).forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, HugeMushroomFeatureConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.Codec<HugeMushroomFeatureConfiguration> CODEC;
/*    */   
/*    */   public final BlockStateProvider capProvider;
/*    */   public final BlockStateProvider stemProvider;
/*    */   public final int foliageRadius;
/*    */   
/*    */   public HugeMushroomFeatureConfiguration(BlockStateProvider capProvider, BlockStateProvider stemProvider, int foliageRadius) {
/* 19 */     this.capProvider = capProvider;
/* 20 */     this.stemProvider = stemProvider;
/* 21 */     this.foliageRadius = foliageRadius;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/HugeMushroomFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */