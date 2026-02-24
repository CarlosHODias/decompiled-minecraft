/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.longs.LongSet;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class UniformHeight extends HeightProvider {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)VerticalAnchor.CODEC.fieldOf("min_inclusive").forGetter(()), (App)VerticalAnchor.CODEC.fieldOf("max_inclusive").forGetter(())).apply((Applicative)i, UniformHeight::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<UniformHeight> CODEC;
/* 20 */   private static final Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*    */   
/*    */   private final VerticalAnchor minInclusive;
/*    */   
/*    */   private final VerticalAnchor maxInclusive;
/* 25 */   private final LongSet warnedFor = (LongSet)new it.unimi.dsi.fastutil.longs.LongOpenHashSet();
/*    */   
/*    */   private UniformHeight(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) {
/* 28 */     this.minInclusive = minInclusive;
/* 29 */     this.maxInclusive = maxInclusive;
/*    */   }
/*    */   
/*    */   public static UniformHeight of(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) {
/* 33 */     return new UniformHeight(minInclusive, maxInclusive);
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource random, WorldGenerationContext context) {
/* 38 */     int min = this.minInclusive.resolveY(context);
/* 39 */     int max = this.maxInclusive.resolveY(context);
/* 40 */     if (min > max) {
/* 41 */       if (this.warnedFor.add(min << 32L | max)) {
/* 42 */         LOGGER.warn("Empty height range: {}", this);
/*    */       }
/* 44 */       return min;
/*    */     } 
/*    */     
/* 47 */     return Mth.randomBetweenInclusive(random, min, max);
/*    */   }
/*    */ 
/*    */   
/*    */   public HeightProviderType<?> getType() {
/* 52 */     return HeightProviderType.UNIFORM;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "[" + String.valueOf(this.minInclusive) + "-" + String.valueOf(this.maxInclusive) + "]";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/heightproviders/UniformHeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */