/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ 
/*    */ public class WeightedListHeight extends HeightProvider {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeightedList.nonEmptyCodec(HeightProvider.CODEC).fieldOf("distribution").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, WeightedListHeight::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<WeightedListHeight> CODEC;
/*    */   private final WeightedList<HeightProvider> distribution;
/*    */   
/*    */   public WeightedListHeight(WeightedList<HeightProvider> distribution) {
/* 17 */     this.distribution = distribution;
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource random, net.minecraft.world.level.levelgen.WorldGenerationContext heightAccessor) {
/* 22 */     return ((HeightProvider)this.distribution.getRandomOrThrow(random)).sample(random, heightAccessor);
/*    */   }
/*    */ 
/*    */   
/*    */   public HeightProviderType<?> getType() {
/* 27 */     return HeightProviderType.WEIGHTED_LIST;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/heightproviders/WeightedListHeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */