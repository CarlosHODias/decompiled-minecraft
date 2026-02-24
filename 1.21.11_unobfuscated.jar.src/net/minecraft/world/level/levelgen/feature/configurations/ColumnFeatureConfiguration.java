/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ 
/*    */ public class ColumnFeatureConfiguration implements FeatureConfiguration {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)IntProvider.codec(0, 3).fieldOf("reach").forGetter(()), (App)IntProvider.codec(1, 10).fieldOf("height").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, ColumnFeatureConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.Codec<ColumnFeatureConfiguration> CODEC;
/*    */   private final IntProvider reach;
/*    */   private final IntProvider height;
/*    */   
/*    */   public ColumnFeatureConfiguration(IntProvider reach, IntProvider height) {
/* 17 */     this.reach = reach;
/* 18 */     this.height = height;
/*    */   }
/*    */   
/*    */   public IntProvider reach() {
/* 22 */     return this.reach;
/*    */   }
/*    */   
/*    */   public IntProvider height() {
/* 26 */     return this.height;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/ColumnFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */