/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class MarsagliaPolarGaussian
/*    */ {
/*    */   public final RandomSource randomSource;
/*    */   private double nextNextGaussian;
/*    */   private boolean haveNextNextGaussian;
/*    */   
/*    */   public MarsagliaPolarGaussian(RandomSource randomSource) {
/* 13 */     this.randomSource = randomSource;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 17 */     this.haveNextNextGaussian = false;
/*    */   }
/*    */   
/*    */   public double nextGaussian() {
/*    */     double x, y, radiusSquared;
/* 22 */     if (this.haveNextNextGaussian) {
/* 23 */       this.haveNextNextGaussian = false;
/* 24 */       return this.nextNextGaussian;
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     do {
/* 33 */       x = 2.0D * this.randomSource.nextDouble() - 1.0D;
/* 34 */       y = 2.0D * this.randomSource.nextDouble() - 1.0D;
/* 35 */       radiusSquared = Mth.square(x) + Mth.square(y);
/* 36 */     } while (radiusSquared >= 1.0D || radiusSquared == 0.0D);
/*    */     
/* 38 */     double multiplier = Math.sqrt(-2.0D * Math.log(radiusSquared) / radiusSquared);
/*    */     
/* 40 */     this.nextNextGaussian = y * multiplier;
/* 41 */     this.haveNextNextGaussian = true;
/* 42 */     return x * multiplier;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/MarsagliaPolarGaussian.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */