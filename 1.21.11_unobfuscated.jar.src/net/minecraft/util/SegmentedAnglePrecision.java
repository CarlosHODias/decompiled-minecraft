/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SegmentedAnglePrecision
/*    */ {
/*    */   private final int mask;
/*    */   private final int precision;
/*    */   private final float degreeToAngle;
/*    */   private final float angleToDegree;
/*    */   
/*    */   public SegmentedAnglePrecision(int bitPrecision) {
/* 24 */     if (bitPrecision < 2) {
/* 25 */       throw new IllegalArgumentException("Precision cannot be less than 2 bits");
/*    */     }
/* 27 */     if (bitPrecision > 30) {
/* 28 */       throw new IllegalArgumentException("Precision cannot be greater than 30 bits");
/*    */     }
/*    */     
/* 31 */     int twoPi = 1 << bitPrecision;
/* 32 */     this.mask = twoPi - 1;
/* 33 */     this.precision = bitPrecision;
/* 34 */     this.degreeToAngle = twoPi / 360.0F;
/* 35 */     this.angleToDegree = 360.0F / twoPi;
/*    */   }
/*    */   
/*    */   public boolean isSameAxis(int binaryAngleA, int binaryAngleB) {
/* 39 */     int semicircleMask = getMask() >> 1;
/*    */     
/* 41 */     return ((binaryAngleA & semicircleMask) == (binaryAngleB & semicircleMask));
/*    */   }
/*    */   
/*    */   public int fromDirection(Direction direction) {
/* 45 */     if (direction.getAxis().isVertical()) {
/* 46 */       return 0;
/*    */     }
/* 48 */     int segmentedAngle2bit = direction.get2DDataValue();
/* 49 */     return segmentedAngle2bit << this.precision - 2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int fromDegreesWithTurns(float degrees) {
/* 58 */     return Math.round(degrees * this.degreeToAngle);
/*    */   }
/*    */   
/*    */   public int fromDegrees(float degrees) {
/* 62 */     return normalize(fromDegreesWithTurns(degrees));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float toDegreesWithTurns(int binaryAngle) {
/* 71 */     return binaryAngle * this.angleToDegree;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float toDegrees(int binaryAngle) {
/* 80 */     float degrees = toDegreesWithTurns(normalize(binaryAngle));
/* 81 */     return (degrees >= 180.0F) ? (degrees - 360.0F) : degrees;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int normalize(int binaryAngle) {
/* 90 */     return binaryAngle & this.mask;
/*    */   }
/*    */   
/*    */   public int getMask() {
/* 94 */     return this.mask;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/SegmentedAnglePrecision.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */