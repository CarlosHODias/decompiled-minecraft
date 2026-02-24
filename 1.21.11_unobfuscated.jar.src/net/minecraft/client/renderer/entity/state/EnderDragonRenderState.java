/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.world.entity.boss.enderdragon.DragonFlightHistory;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EnderDragonRenderState
/*    */   extends EntityRenderState
/*    */ {
/*    */   public float flapTime;
/*    */   public float deathTime;
/*    */   public boolean hasRedOverlay;
/*    */   public Vec3 beamOffset;
/*    */   public boolean isLandingOrTakingOff;
/*    */   public boolean isSitting;
/*    */   public double distanceToEgg;
/*    */   public float partialTicks;
/* 19 */   public final DragonFlightHistory flightHistory = new DragonFlightHistory();
/*    */   
/*    */   public DragonFlightHistory.Sample getHistoricalPos(int delay) {
/* 22 */     return this.flightHistory.get(delay, this.partialTicks);
/*    */   }
/*    */   
/*    */   public float getHeadPartYOffset(int part, DragonFlightHistory.Sample bodyPos, DragonFlightHistory.Sample partPos) {
/*    */     double result;
/* 27 */     if (this.isLandingOrTakingOff) {
/* 28 */       result = part / Math.max(this.distanceToEgg / 4.0D, 1.0D);
/* 29 */     } else if (this.isSitting) {
/* 30 */       result = part;
/*    */     }
/* 32 */     else if (part == 6) {
/* 33 */       result = 0.0D;
/*    */     } else {
/* 35 */       result = partPos.y() - bodyPos.y();
/*    */     } 
/*    */     
/* 38 */     return (float)result;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/EnderDragonRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */