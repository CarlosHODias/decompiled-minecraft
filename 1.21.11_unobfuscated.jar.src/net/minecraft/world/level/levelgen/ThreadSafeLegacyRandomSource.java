/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class ThreadSafeLegacyRandomSource
/*    */   implements BitRandomSource
/*    */ {
/*    */   private static final int MODULUS_BITS = 48;
/*    */   private static final long MODULUS_MASK = 281474976710655L;
/*    */   private static final long MULTIPLIER = 25214903917L;
/*    */   private static final long INCREMENT = 11L;
/* 19 */   private final AtomicLong seed = new AtomicLong();
/* 20 */   private final MarsagliaPolarGaussian gaussianSource = new MarsagliaPolarGaussian(this);
/*    */   
/*    */   public ThreadSafeLegacyRandomSource(long seed) {
/* 23 */     setSeed(seed);
/*    */   }
/*    */ 
/*    */   
/*    */   public RandomSource fork() {
/* 28 */     return new ThreadSafeLegacyRandomSource(nextLong());
/*    */   }
/*    */ 
/*    */   
/*    */   public PositionalRandomFactory forkPositional() {
/* 33 */     return new LegacyRandomSource.LegacyPositionalRandomFactory(nextLong());
/*    */   }
/*    */ 
/*    */   
/*    */   public void setSeed(long seed) {
/* 38 */     this.seed.set((seed ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int next(int bits) {
/*    */     while (true) {
/* 46 */       long oldSeed = this.seed.get();
/* 47 */       long nextSeed = oldSeed * 25214903917L + 11L & 0xFFFFFFFFFFFFL;
/* 48 */       if (this.seed.compareAndSet(oldSeed, nextSeed))
/* 49 */         return (int)(nextSeed >>> 48 - bits); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public double nextGaussian() {
/* 54 */     return this.gaussianSource.nextGaussian();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/ThreadSafeLegacyRandomSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */