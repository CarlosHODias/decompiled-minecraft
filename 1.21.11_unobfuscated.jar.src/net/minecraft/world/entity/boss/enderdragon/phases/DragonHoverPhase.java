/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DragonHoverPhase
/*    */   extends AbstractDragonPhaseInstance {
/*    */   private Vec3 targetLocation;
/*    */   
/*    */   public DragonHoverPhase(EnderDragon dragon) {
/* 12 */     super(dragon);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doServerTick(ServerLevel level) {
/* 17 */     if (this.targetLocation == null) {
/* 18 */       this.targetLocation = this.dragon.position();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSitting() {
/* 24 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 29 */     this.targetLocation = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getFlySpeed() {
/* 34 */     return 1.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 getFlyTargetLocation() {
/* 39 */     return this.targetLocation;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnderDragonPhase<DragonHoverPhase> getPhase() {
/* 44 */     return EnderDragonPhase.HOVERING;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/boss/enderdragon/phases/DragonHoverPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */