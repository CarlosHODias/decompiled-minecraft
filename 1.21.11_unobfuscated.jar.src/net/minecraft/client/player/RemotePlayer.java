/*    */ package net.minecraft.client.player;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*    */ import net.minecraft.util.profiling.Profiler;
/*    */ import net.minecraft.util.profiling.Zone;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class RemotePlayer extends AbstractClientPlayer {
/* 12 */   private Vec3 lerpDeltaMovement = Vec3.ZERO;
/*    */   private int lerpDeltaMovementSteps;
/*    */   
/*    */   public RemotePlayer(ClientLevel level, GameProfile gameProfile) {
/* 16 */     super(level, gameProfile);
/*    */     
/* 18 */     this.noPhysics = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 23 */     double size = getBoundingBox().getSize() * 10.0D;
/* 24 */     if (Double.isNaN(size)) {
/* 25 */       size = 1.0D;
/*    */     }
/* 27 */     size *= 64.0D * getViewScale();
/* 28 */     return (distance < size * size);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hurtClient(DamageSource source) {
/* 33 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 38 */     super.tick();
/* 39 */     calculateEntityAnimation(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void aiStep() {
/* 44 */     if (isInterpolating()) {
/* 45 */       getInterpolation().interpolate();
/*    */     }
/* 47 */     if (this.lerpHeadSteps > 0) {
/* 48 */       lerpHeadRotationStep(this.lerpHeadSteps, this.lerpYHeadRot);
/* 49 */       this.lerpHeadSteps--;
/*    */     } 
/* 51 */     if (this.lerpDeltaMovementSteps > 0) {
/* 52 */       addDeltaMovement(new Vec3((this.lerpDeltaMovement.x - 
/* 53 */             (getDeltaMovement()).x) / this.lerpDeltaMovementSteps, (this.lerpDeltaMovement.y - 
/* 54 */             (getDeltaMovement()).y) / this.lerpDeltaMovementSteps, (this.lerpDeltaMovement.z - 
/* 55 */             (getDeltaMovement()).z) / this.lerpDeltaMovementSteps));
/*    */       
/* 57 */       this.lerpDeltaMovementSteps--;
/*    */     } 
/*    */     
/* 60 */     updateSwingTime();
/* 61 */     updateBob();
/*    */     
/* 63 */     Zone ignored = Profiler.get().zone("push"); 
/* 64 */     try { pushEntities();
/* 65 */       if (ignored != null) ignored.close();  }
/*    */     catch (Throwable throwable) { if (ignored != null)
/*    */         try { ignored.close(); }
/*    */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*    */           throw throwable; }
/* 70 */      } public void lerpMotion(Vec3 movement) { this.lerpDeltaMovement = movement;
/* 71 */     this.lerpDeltaMovementSteps = getType().updateInterval() + 1; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void updatePlayerPose() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 83 */     super.recreateFromPacket(packet);
/* 84 */     setOldPosAndRot();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/player/RemotePlayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */