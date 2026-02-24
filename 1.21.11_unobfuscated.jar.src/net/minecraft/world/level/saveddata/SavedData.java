/*    */ package net.minecraft.world.level.saveddata;
/*    */ 
/*    */ public abstract class SavedData {
/*    */   private boolean dirty;
/*    */   
/*    */   public void setDirty() {
/*  7 */     setDirty(true);
/*    */   }
/*    */   
/*    */   public void setDirty(boolean dirty) {
/* 11 */     this.dirty = dirty;
/*    */   }
/*    */   
/*    */   public boolean isDirty() {
/* 15 */     return this.dirty;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/saveddata/SavedData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */