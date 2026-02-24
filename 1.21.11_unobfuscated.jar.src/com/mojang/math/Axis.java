/*    */ package com.mojang.math;
/*    */ @FunctionalInterface
/*    */ public interface Axis {
/*    */   public static final Axis XN;
/*    */   public static final Axis XP;
/*    */   public static final Axis YN;
/*    */   
/*    */   static {
/*  9 */     XN = (angle -> new org.joml.Quaternionf().rotationX(-angle));
/* 10 */     XP = (angle -> new org.joml.Quaternionf().rotationX(angle));
/* 11 */     YN = (angle -> new org.joml.Quaternionf().rotationY(-angle));
/* 12 */     YP = (angle -> new org.joml.Quaternionf().rotationY(angle));
/* 13 */     ZN = (angle -> new org.joml.Quaternionf().rotationZ(-angle));
/* 14 */     ZP = (angle -> new org.joml.Quaternionf().rotationZ(angle));
/*    */   } public static final Axis YP; public static final Axis ZN; public static final Axis ZP;
/*    */   static Axis of(org.joml.Vector3f vector) {
/* 17 */     return angle -> new org.joml.Quaternionf().rotationAxis(angle, (org.joml.Vector3fc)vector);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   default org.joml.Quaternionf rotationDegrees(float angle) {
/* 23 */     return rotation(angle * 0.017453292F);
/*    */   }
/*    */   
/*    */   org.joml.Quaternionf rotation(float paramFloat);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/math/Axis.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */