/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.portal.TeleportTransition;
/*    */ 
/*    */ public interface Portal
/*    */ {
/*    */   public enum Transition {
/* 11 */     CONFUSION,
/* 12 */     NONE;
/*    */   }
/*    */   
/*    */   default int getPortalTransitionTime(ServerLevel level, Entity entity) {
/* 16 */     return 0;
/*    */   }
/*    */   
/*    */   TeleportTransition getPortalDestination(ServerLevel paramServerLevel, Entity paramEntity, BlockPos paramBlockPos);
/*    */   
/*    */   default Transition getLocalTransition() {
/* 22 */     return Transition.NONE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/Portal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */