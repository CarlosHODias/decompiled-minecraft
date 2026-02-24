/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.block.Portal;
/*    */ import net.minecraft.world.level.portal.TeleportTransition;
/*    */ 
/*    */ public class PortalProcessor
/*    */ {
/*    */   private final Portal portal;
/*    */   private BlockPos entryPosition;
/*    */   private int portalTime;
/*    */   private boolean insidePortalThisTick;
/*    */   
/*    */   public PortalProcessor(Portal portal, BlockPos portalEntryPosition) {
/* 16 */     this.portal = portal;
/* 17 */     this.entryPosition = portalEntryPosition;
/* 18 */     this.insidePortalThisTick = true;
/*    */   }
/*    */   
/*    */   public boolean processPortalTeleportation(ServerLevel serverLevel, Entity entity, boolean allowedToTeleport) {
/* 22 */     if (this.insidePortalThisTick) {
/* 23 */       this.insidePortalThisTick = false;
/* 24 */       return (allowedToTeleport && this.portalTime++ >= this.portal.getPortalTransitionTime(serverLevel, entity));
/*    */     } 
/* 26 */     decayTick();
/* 27 */     return false;
/*    */   }
/*    */   
/*    */   public TeleportTransition getPortalDestination(ServerLevel serverLevel, Entity entity) {
/* 31 */     return this.portal.getPortalDestination(serverLevel, entity, this.entryPosition);
/*    */   }
/*    */   
/*    */   public Portal.Transition getPortalLocalTransition() {
/* 35 */     return this.portal.getLocalTransition();
/*    */   }
/*    */   
/*    */   private void decayTick() {
/* 39 */     this.portalTime = Math.max(this.portalTime - 4, 0);
/*    */   }
/*    */   
/*    */   public boolean hasExpired() {
/* 43 */     return (this.portalTime <= 0);
/*    */   }
/*    */   
/*    */   public BlockPos getEntryPosition() {
/* 47 */     return this.entryPosition;
/*    */   }
/*    */   
/*    */   public void updateEntryPosition(BlockPos entryPosition) {
/* 51 */     this.entryPosition = entryPosition;
/*    */   }
/*    */   
/*    */   public int getPortalTime() {
/* 55 */     return this.portalTime;
/*    */   }
/*    */   
/*    */   public boolean isInsidePortalThisTick() {
/* 59 */     return this.insidePortalThisTick;
/*    */   }
/*    */   
/*    */   public void setAsInsidePortalThisTick(boolean insidePortal) {
/* 63 */     this.insidePortalThisTick = insidePortal;
/*    */   }
/*    */   
/*    */   public boolean isSamePortal(Portal portal) {
/* 67 */     return (this.portal == portal);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/PortalProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */