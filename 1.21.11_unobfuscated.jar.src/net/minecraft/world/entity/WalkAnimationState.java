/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class WalkAnimationState {
/*    */   private float speedOld;
/*    */   private float speed;
/*    */   private float position;
/*  9 */   private float positionScale = 1.0F;
/*    */   
/*    */   public void setSpeed(float speed) {
/* 12 */     this.speed = speed;
/*    */   }
/*    */   
/*    */   public void update(float targetSpeed, float factor, float positionScale) {
/* 16 */     this.speedOld = this.speed;
/* 17 */     this.speed += (targetSpeed - this.speed) * factor;
/* 18 */     this.position += this.speed;
/* 19 */     this.positionScale = positionScale;
/*    */   }
/*    */   
/*    */   public void stop() {
/* 23 */     this.speedOld = 0.0F;
/* 24 */     this.speed = 0.0F;
/* 25 */     this.position = 0.0F;
/*    */   }
/*    */   
/*    */   public float speed() {
/* 29 */     return this.speed;
/*    */   }
/*    */   
/*    */   public float speed(float partialTicks) {
/* 33 */     return Math.min(Mth.lerp(partialTicks, this.speedOld, this.speed), 1.0F);
/*    */   }
/*    */   
/*    */   public float position() {
/* 37 */     return this.position * this.positionScale;
/*    */   }
/*    */   
/*    */   public float position(float partialTicks) {
/* 41 */     return (this.position - this.speed * (1.0F - partialTicks)) * this.positionScale;
/*    */   }
/*    */   
/*    */   public boolean isMoving() {
/* 45 */     return (this.speed > 1.0E-5F);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/WalkAnimationState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */