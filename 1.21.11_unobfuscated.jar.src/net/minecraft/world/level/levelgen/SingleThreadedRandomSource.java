/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SingleThreadedRandomSource
/*    */   implements BitRandomSource
/*    */ {
/*    */   private static final int MODULUS_BITS = 48;
/*    */   private static final long MODULUS_MASK = 281474976710655L;
/*    */   private static final long MULTIPLIER = 25214903917L;
/*    */   private static final long INCREMENT = 11L;
/*    */   private long seed;
/* 15 */   private final MarsagliaPolarGaussian gaussianSource = new MarsagliaPolarGaussian(this);
/*    */   
/*    */   public SingleThreadedRandomSource(long seed) {
/* 18 */     setSeed(seed);
/*    */   }
/*    */ 
/*    */   
/*    */   public RandomSource fork() {
/* 23 */     return new SingleThreadedRandomSource(nextLong());
/*    */   }
/*    */ 
/*    */   
/*    */   public PositionalRandomFactory forkPositional() {
/* 28 */     return new LegacyRandomSource.LegacyPositionalRandomFactory(nextLong());
/*    */   }
/*    */ 
/*    */   
/*    */   public void setSeed(long seed) {
/* 33 */     this.seed = (seed ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL;
/* 34 */     this.gaussianSource.reset();
/*    */   }
/*    */ 
/*    */   
/*    */   public int next(int bits) {
/* 39 */     long newSeed = this.seed * 25214903917L + 11L & 0xFFFFFFFFFFFFL;
/* 40 */     this.seed = newSeed;
/* 41 */     return (int)(newSeed >> 48 - bits);
/*    */   }
/*    */ 
/*    */   
/*    */   public double nextGaussian() {
/* 46 */     return this.gaussianSource.nextGaussian();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/SingleThreadedRandomSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */