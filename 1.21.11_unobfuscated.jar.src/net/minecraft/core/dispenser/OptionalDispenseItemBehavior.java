/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ public abstract class OptionalDispenseItemBehavior
/*    */   extends DefaultDispenseItemBehavior
/*    */ {
/*    */   private boolean success = true;
/*    */   
/*    */   public boolean isSuccess() {
/*  9 */     return this.success;
/*    */   }
/*    */   
/*    */   public void setSuccess(boolean success) {
/* 13 */     this.success = success;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playSound(BlockSource source) {
/* 18 */     source.level().levelEvent(isSuccess() ? 1000 : 1001, source.pos(), 0);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/dispenser/OptionalDispenseItemBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */