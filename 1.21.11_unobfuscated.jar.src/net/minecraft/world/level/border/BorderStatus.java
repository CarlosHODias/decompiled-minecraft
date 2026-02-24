/*    */ package net.minecraft.world.level.border;
/*    */ 
/*    */ public enum BorderStatus {
/*  4 */   GROWING(4259712),
/*  5 */   SHRINKING(16724016),
/*  6 */   STATIONARY(2138367);
/*    */   
/*    */   private final int color;
/*    */ 
/*    */   
/*    */   BorderStatus(int color) {
/* 12 */     this.color = color;
/*    */   }
/*    */   
/*    */   public int getColor() {
/* 16 */     return this.color;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/border/BorderStatus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */