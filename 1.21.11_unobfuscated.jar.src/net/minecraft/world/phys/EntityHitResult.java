/*    */ package net.minecraft.world.phys;
/*    */ 
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class EntityHitResult extends HitResult {
/*    */   private final Entity entity;
/*    */   
/*    */   public EntityHitResult(Entity entity) {
/*  9 */     this(entity, entity.position());
/*    */   }
/*    */   
/*    */   public EntityHitResult(Entity entity, Vec3 location) {
/* 13 */     super(location);
/*    */     
/* 15 */     this.entity = entity;
/*    */   }
/*    */   
/*    */   public Entity getEntity() {
/* 19 */     return this.entity;
/*    */   }
/*    */ 
/*    */   
/*    */   public HitResult.Type getType() {
/* 24 */     return HitResult.Type.ENTITY;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/phys/EntityHitResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */