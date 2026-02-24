/*    */ package net.minecraft.world.level.gameevent;public interface GameEventListener {
/*    */   PositionSource getListenerSource();
/*    */   
/*    */   int getListenerRadius();
/*    */   
/*    */   boolean handleGameEvent(net.minecraft.server.level.ServerLevel paramServerLevel, net.minecraft.core.Holder<GameEvent> paramHolder, GameEvent.Context paramContext, net.minecraft.world.phys.Vec3 paramVec3);
/*    */   
/*    */   public enum DeliveryMode {
/*  9 */     UNSPECIFIED,
/* 10 */     BY_DISTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default DeliveryMode getDeliveryMode() {
/* 19 */     return DeliveryMode.UNSPECIFIED;
/*    */   }
/*    */   
/*    */   public static interface Provider<T extends GameEventListener> {
/*    */     T getListener();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/gameevent/GameEventListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */