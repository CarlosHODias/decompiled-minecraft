/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.valueproviders.ConstantInt;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ 
/*    */ public class CountConfiguration implements FeatureConfiguration {
/*  8 */   public static final Codec<CountConfiguration> CODEC = IntProvider.codec(0, 256).fieldOf("count")
/*  9 */     .xmap(CountConfiguration::new, CountConfiguration::count).codec();
/*    */   
/*    */   private final IntProvider count;
/*    */   
/*    */   public CountConfiguration(int count) {
/* 14 */     this.count = (IntProvider)ConstantInt.of(count);
/*    */   }
/*    */   
/*    */   public CountConfiguration(IntProvider count) {
/* 18 */     this.count = count;
/*    */   }
/*    */   
/*    */   public IntProvider count() {
/* 22 */     return this.count;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/CountConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */