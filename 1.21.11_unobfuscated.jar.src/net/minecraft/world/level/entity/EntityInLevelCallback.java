/*   */ package net.minecraft.world.level.entity;
/*   */ 
/*   */ import net.minecraft.world.entity.Entity;
/*   */ 
/*   */ public interface EntityInLevelCallback {
/* 6 */   public static final EntityInLevelCallback NULL = new EntityInLevelCallback() {
/*   */       public void onMove() {}
/*   */       
/*   */       public void onRemove(Entity.RemovalReason reason) {}
/*   */     };
/*   */   
/*   */   void onMove();
/*   */   
/*   */   void onRemove(Entity.RemovalReason paramRemovalReason);
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/entity/EntityInLevelCallback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */