/*    */ package net.minecraft.world;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.levelgen.RandomSupport;
/*    */ import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
/*    */ 
/*    */ public class RandomSequence {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)XoroshiroRandomSource.CODEC.fieldOf("source").forGetter(())).apply((Applicative)i, RandomSequence::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.Codec<RandomSequence> CODEC;
/*    */   private final XoroshiroRandomSource source;
/*    */   
/*    */   public RandomSequence(XoroshiroRandomSource source) {
/* 20 */     this.source = source;
/*    */   }
/*    */   
/*    */   public RandomSequence(long seed, Identifier key) {
/* 24 */     this(createSequence(seed, Optional.of(key)));
/*    */   }
/*    */   
/*    */   public RandomSequence(long seed, Optional<Identifier> key) {
/* 28 */     this(createSequence(seed, key));
/*    */   }
/*    */ 
/*    */   
/*    */   private static XoroshiroRandomSource createSequence(long seed, Optional<Identifier> key) {
/* 33 */     RandomSupport.Seed128bit seed128bit = RandomSupport.upgradeSeedTo128bitUnmixed(seed);
/* 34 */     if (key.isPresent()) {
/* 35 */       seed128bit = seed128bit.xor(seedForKey(key.get()));
/*    */     }
/* 37 */     return new XoroshiroRandomSource(seed128bit.mixed());
/*    */   }
/*    */   
/*    */   public static RandomSupport.Seed128bit seedForKey(Identifier key) {
/* 41 */     return RandomSupport.seedFromHashOf(key.toString());
/*    */   }
/*    */   
/*    */   public net.minecraft.util.RandomSource random() {
/* 45 */     return (net.minecraft.util.RandomSource)this.source;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/RandomSequence.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */