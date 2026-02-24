/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ItemPickupParticle
/*    */   extends Particle
/*    */ {
/*    */   protected static final int LIFE_TIME = 3;
/*    */   private final Entity target;
/*    */   protected int life;
/*    */   protected final EntityRenderState itemRenderState;
/*    */   protected double targetX;
/*    */   protected double targetY;
/*    */   protected double targetZ;
/*    */   protected double targetXOld;
/*    */   protected double targetYOld;
/*    */   protected double targetZOld;
/*    */   
/*    */   public ItemPickupParticle(ClientLevel level, EntityRenderState itemEntity, Entity target, Vec3 movement) {
/* 23 */     super(level, itemEntity.x, itemEntity.y, itemEntity.z, movement.x, movement.y, movement.z);
/* 24 */     this.target = target;
/* 25 */     this.itemRenderState = itemEntity;
/* 26 */     this.itemRenderState.outlineColor = 0;
/* 27 */     updatePosition();
/* 28 */     saveOldPosition();
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 33 */     this.life++;
/* 34 */     if (this.life == 3) {
/* 35 */       remove();
/*    */     }
/* 37 */     saveOldPosition();
/* 38 */     updatePosition();
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleRenderType getGroup() {
/* 43 */     return ParticleRenderType.ITEM_PICKUP;
/*    */   }
/*    */   
/*    */   private void updatePosition() {
/* 47 */     this.targetX = this.target.getX();
/* 48 */     this.targetY = (this.target.getY() + this.target.getEyeY()) / 2.0D;
/* 49 */     this.targetZ = this.target.getZ();
/*    */   }
/*    */   
/*    */   private void saveOldPosition() {
/* 53 */     this.targetXOld = this.targetX;
/* 54 */     this.targetYOld = this.targetY;
/* 55 */     this.targetZOld = this.targetZ;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/ItemPickupParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */