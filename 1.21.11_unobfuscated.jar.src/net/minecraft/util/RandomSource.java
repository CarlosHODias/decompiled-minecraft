/*    */ package net.minecraft.util;
/*    */ 
/*    */ import io.netty.util.internal.ThreadLocalRandom;
/*    */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*    */ import net.minecraft.world.level.levelgen.PositionalRandomFactory;
/*    */ import net.minecraft.world.level.levelgen.RandomSupport;
/*    */ import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
/*    */ import net.minecraft.world.level.levelgen.ThreadSafeLegacyRandomSource;
/*    */ 
/*    */ public interface RandomSource {
/*    */   static RandomSource create() {
/* 12 */     return create(RandomSupport.generateUniqueSeed());
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public static final double GAUSSIAN_SPREAD_FACTOR = 2.297D;
/*    */   
/*    */   @Deprecated
/*    */   static RandomSource createThreadSafe() {
/* 20 */     return (RandomSource)new ThreadSafeLegacyRandomSource(RandomSupport.generateUniqueSeed());
/*    */   }
/*    */   
/*    */   static RandomSource create(long seed) {
/* 24 */     return (RandomSource)new LegacyRandomSource(seed);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static RandomSource createNewThreadLocalInstance() {
/* 32 */     return (RandomSource)new SingleThreadedRandomSource(ThreadLocalRandom.current().nextLong());
/*    */   }
/*    */ 
/*    */   
/*    */   RandomSource fork();
/*    */ 
/*    */   
/*    */   PositionalRandomFactory forkPositional();
/*    */ 
/*    */   
/*    */   void setSeed(long paramLong);
/*    */ 
/*    */   
/*    */   int nextInt();
/*    */ 
/*    */   
/*    */   int nextInt(int paramInt);
/*    */ 
/*    */   
/*    */   default int nextIntBetweenInclusive(int min, int maxInclusive) {
/* 52 */     return nextInt(maxInclusive - min + 1) + min;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   long nextLong();
/*    */ 
/*    */ 
/*    */   
/*    */   boolean nextBoolean();
/*    */ 
/*    */ 
/*    */   
/*    */   float nextFloat();
/*    */ 
/*    */   
/*    */   double nextDouble();
/*    */ 
/*    */   
/*    */   double nextGaussian();
/*    */ 
/*    */   
/*    */   default double triangle(double mean, double spread) {
/* 75 */     return mean + spread * (nextDouble() - nextDouble());
/*    */   }
/*    */   
/*    */   default float triangle(float mean, float spread) {
/* 79 */     return mean + spread * (nextFloat() - nextFloat());
/*    */   }
/*    */   
/*    */   default void consumeCount(int rounds) {
/* 83 */     for (int i = 0; i < rounds; i++) {
/* 84 */       nextInt();
/*    */     }
/*    */   }
/*    */   
/*    */   default int nextInt(int origin, int bound) {
/* 89 */     if (origin >= bound) {
/* 90 */       throw new IllegalArgumentException("bound - origin is non positive");
/*    */     }
/* 92 */     return origin + nextInt(bound - origin);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/RandomSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */