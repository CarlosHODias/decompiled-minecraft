/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class XoroshiroRandomSource implements RandomSource {
/*     */   private static final float FLOAT_UNIT = 5.9604645E-8F;
/*     */   private static final double DOUBLE_UNIT = 1.1102230246251565E-16D;
/*     */   public static final Codec<XoroshiroRandomSource> CODEC;
/*     */   private Xoroshiro128PlusPlus randomNumberGenerator;
/*     */   
/*     */   static {
/*  15 */     CODEC = Xoroshiro128PlusPlus.CODEC.xmap(generator -> new XoroshiroRandomSource(generator), source -> source.randomNumberGenerator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  21 */   private final MarsagliaPolarGaussian gaussianSource = new MarsagliaPolarGaussian(this);
/*     */   
/*     */   public XoroshiroRandomSource(long seed) {
/*  24 */     this.randomNumberGenerator = new Xoroshiro128PlusPlus(RandomSupport.upgradeSeedTo128bit(seed));
/*     */   }
/*     */   
/*     */   public XoroshiroRandomSource(RandomSupport.Seed128bit seed) {
/*  28 */     this.randomNumberGenerator = new Xoroshiro128PlusPlus(seed);
/*     */   }
/*     */   
/*     */   public XoroshiroRandomSource(long seedLo, long seedHi) {
/*  32 */     this.randomNumberGenerator = new Xoroshiro128PlusPlus(seedLo, seedHi);
/*     */   }
/*     */   
/*     */   private XoroshiroRandomSource(Xoroshiro128PlusPlus randomNumberGenerator) {
/*  36 */     this.randomNumberGenerator = randomNumberGenerator;
/*     */   }
/*     */ 
/*     */   
/*     */   public RandomSource fork() {
/*  41 */     return new XoroshiroRandomSource(this.randomNumberGenerator.nextLong(), this.randomNumberGenerator.nextLong());
/*     */   }
/*     */ 
/*     */   
/*     */   public PositionalRandomFactory forkPositional() {
/*  46 */     return new XoroshiroPositionalRandomFactory(this.randomNumberGenerator.nextLong(), this.randomNumberGenerator.nextLong());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSeed(long seed) {
/*  51 */     this.randomNumberGenerator = new Xoroshiro128PlusPlus(RandomSupport.upgradeSeedTo128bit(seed));
/*  52 */     this.gaussianSource.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public int nextInt() {
/*  57 */     return (int)this.randomNumberGenerator.nextLong();
/*     */   }
/*     */ 
/*     */   
/*     */   public int nextInt(int bound) {
/*  62 */     if (bound <= 0) {
/*  63 */       throw new IllegalArgumentException("Bound must be positive");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  69 */     long randomBits = Integer.toUnsignedLong(nextInt());
/*     */ 
/*     */     
/*  72 */     long multipliedRandomBits = randomBits * bound;
/*     */     
/*  74 */     long fractionalPart = multipliedRandomBits & 0xFFFFFFFFL;
/*     */ 
/*     */     
/*  77 */     if (fractionalPart < bound) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  82 */       int unbiasedBucketsStartIndex = Integer.remainderUnsigned((bound ^ 0xFFFFFFFF) + 1, bound);
/*  83 */       while (fractionalPart < unbiasedBucketsStartIndex) {
/*     */         
/*  85 */         randomBits = Integer.toUnsignedLong(nextInt());
/*  86 */         multipliedRandomBits = randomBits * bound;
/*  87 */         fractionalPart = multipliedRandomBits & 0xFFFFFFFFL;
/*     */       } 
/*     */     } 
/*     */     
/*  91 */     long integerPart = multipliedRandomBits >> 32L;
/*     */     
/*  93 */     return (int)integerPart;
/*     */   }
/*     */ 
/*     */   
/*     */   public long nextLong() {
/*  98 */     return this.randomNumberGenerator.nextLong();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean nextBoolean() {
/* 103 */     return ((this.randomNumberGenerator.nextLong() & 0x1L) != 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public float nextFloat() {
/* 108 */     return (float)nextBits(24) * 5.9604645E-8F;
/*     */   }
/*     */ 
/*     */   
/*     */   public double nextDouble() {
/* 113 */     return nextBits(53) * 1.1102230246251565E-16D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double nextGaussian() {
/* 118 */     return this.gaussianSource.nextGaussian();
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumeCount(int rounds) {
/* 123 */     for (int i = 0; i < rounds; i++) {
/* 124 */       this.randomNumberGenerator.nextLong();
/*     */     }
/*     */   }
/*     */   
/*     */   private long nextBits(int bits) {
/* 129 */     return this.randomNumberGenerator.nextLong() >>> 64 - bits;
/*     */   }
/*     */   
/*     */   public static class XoroshiroPositionalRandomFactory implements PositionalRandomFactory {
/*     */     private final long seedLo;
/*     */     private final long seedHi;
/*     */     
/*     */     public XoroshiroPositionalRandomFactory(long seedLo, long seedHi) {
/* 137 */       this.seedLo = seedLo;
/* 138 */       this.seedHi = seedHi;
/*     */     }
/*     */ 
/*     */     
/*     */     public RandomSource at(int x, int y, int z) {
/* 143 */       long positionalSeed = Mth.getSeed(x, y, z);
/* 144 */       long randomSeed = positionalSeed ^ this.seedLo;
/* 145 */       return new XoroshiroRandomSource(randomSeed, this.seedHi);
/*     */     }
/*     */ 
/*     */     
/*     */     public RandomSource fromHashOf(String name) {
/* 150 */       RandomSupport.Seed128bit seed = RandomSupport.seedFromHashOf(name);
/* 151 */       return new XoroshiroRandomSource(seed.xor(this.seedLo, this.seedHi));
/*     */     }
/*     */ 
/*     */     
/*     */     public RandomSource fromSeed(long seed) {
/* 156 */       return new XoroshiroRandomSource(seed ^ this.seedLo, seed ^ this.seedHi);
/*     */     }
/*     */ 
/*     */     
/*     */     @VisibleForTesting
/*     */     public void parityConfigString(StringBuilder sb) {
/* 162 */       sb.append("seedLo: ").append(this.seedLo).append(", seedHi: ").append(this.seedHi);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/XoroshiroRandomSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */