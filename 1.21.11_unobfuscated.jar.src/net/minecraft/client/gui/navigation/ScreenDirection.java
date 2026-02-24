/*    */ package net.minecraft.client.gui.navigation;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntComparator;
/*    */ 
/*    */ public enum ScreenDirection {
/*  6 */   UP,
/*  7 */   DOWN,
/*  8 */   LEFT,
/*  9 */   RIGHT;
/*    */   
/*    */   ScreenDirection() {
/* 12 */     this.coordinateValueComparator = ((k1, k2) -> (k1 == k2) ? 0 : (isBefore(k1, k2) ? -1 : 1));
/*    */   } private final IntComparator coordinateValueComparator;
/*    */   public ScreenAxis getAxis() {
/* 15 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: case 3: break; }  return 
/*    */       
/* 17 */       ScreenAxis.HORIZONTAL;
/*    */   }
/*    */ 
/*    */   
/*    */   public ScreenDirection getOpposite() {
/* 22 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: case 3: break; }  return 
/*    */ 
/*    */ 
/*    */       
/* 26 */       LEFT;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPositive() {
/* 31 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 2: case 1: case 3: break; }  return true;
/*    */   }
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
/*    */   public boolean isAfter(int a, int b) {
/* 44 */     if (isPositive()) {
/* 45 */       return (a > b);
/*    */     }
/* 47 */     return (b > a);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isBefore(int a, int b) {
/* 58 */     if (isPositive()) {
/* 59 */       return (a < b);
/*    */     }
/* 61 */     return (b < a);
/*    */   }
/*    */ 
/*    */   
/*    */   public IntComparator coordinateValueComparator() {
/* 66 */     return this.coordinateValueComparator;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/navigation/ScreenDirection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */