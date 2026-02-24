/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import java.util.function.LongFunction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WorldgenRandom
/*    */   extends LegacyRandomSource
/*    */ {
/*    */   private final RandomSource randomSource;
/*    */   private int count;
/*    */   
/*    */   public WorldgenRandom(RandomSource randomSource) {
/* 17 */     super(0L);
/* 18 */     this.randomSource = randomSource;
/*    */   }
/*    */   
/*    */   public int getCount() {
/* 22 */     return this.count;
/*    */   }
/*    */ 
/*    */   
/*    */   public RandomSource fork() {
/* 27 */     return this.randomSource.fork();
/*    */   }
/*    */ 
/*    */   
/*    */   public PositionalRandomFactory forkPositional() {
/* 32 */     return this.randomSource.forkPositional();
/*    */   }
/*    */ 
/*    */   
/*    */   public int next(int bits) {
/* 37 */     this.count++;
/* 38 */     RandomSource randomSource = this.randomSource; if (randomSource instanceof LegacyRandomSource) { LegacyRandomSource legacyRandomSource = (LegacyRandomSource)randomSource;
/* 39 */       return legacyRandomSource.next(bits); }
/*    */     
/* 41 */     return (int)(this.randomSource.nextLong() >>> 64 - bits);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void setSeed(long seed) {
/* 47 */     if (this.randomSource == null) {
/*    */       return;
/*    */     }
/* 50 */     this.randomSource.setSeed(seed);
/*    */   }
/*    */   
/*    */   public long setDecorationSeed(long seed, int chunkX, int chunkZ) {
/* 54 */     setSeed(seed);
/*    */     
/* 56 */     long xScale = nextLong() | 0x1L;
/* 57 */     long zScale = nextLong() | 0x1L;
/* 58 */     long result = chunkX * xScale + chunkZ * zScale ^ seed;
/* 59 */     setSeed(result);
/* 60 */     return result;
/*    */   }
/*    */   
/*    */   public void setFeatureSeed(long seed, int index, int step) {
/* 64 */     long result = seed + index + (10000 * step);
/* 65 */     setSeed(result);
/*    */   }
/*    */   
/*    */   public void setLargeFeatureSeed(long seed, int chunkX, int chunkZ) {
/* 69 */     setSeed(seed);
/* 70 */     long xScale = nextLong();
/* 71 */     long zScale = nextLong();
/* 72 */     long result = chunkX * xScale ^ chunkZ * zScale ^ seed;
/* 73 */     setSeed(result);
/*    */   }
/*    */   
/*    */   public void setLargeFeatureWithSalt(long seed, int x, int z, int blend) {
/* 77 */     long result = x * 341873128712L + z * 132897987541L + seed + blend;
/* 78 */     setSeed(result);
/*    */   }
/*    */   
/*    */   public static RandomSource seedSlimeChunk(int x, int z, long seed, long salt) {
/* 82 */     return RandomSource.create(seed + (x * x * 4987142) + (x * 5947611) + (z * z) * 4392871L + (z * 389711) ^ salt);
/*    */   }
/*    */   
/*    */   public enum Algorithm {
/* 86 */     LEGACY(LegacyRandomSource::new),
/* 87 */     XOROSHIRO(XoroshiroRandomSource::new);
/*    */     
/*    */     private final LongFunction<RandomSource> constructor;
/*    */ 
/*    */     
/*    */     Algorithm(LongFunction<RandomSource> constructor) {
/* 93 */       this.constructor = constructor;
/*    */     }
/*    */     
/*    */     public RandomSource newInstance(long seed) {
/* 97 */       return this.constructor.apply(seed);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/WorldgenRandom.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */