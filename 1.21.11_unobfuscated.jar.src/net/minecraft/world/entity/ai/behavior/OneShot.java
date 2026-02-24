/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ 
/*    */ 
/*    */ public abstract class OneShot<E extends LivingEntity>
/*    */   implements BehaviorControl<E>, Trigger<E>
/*    */ {
/* 11 */   private Behavior.Status status = Behavior.Status.STOPPED;
/*    */ 
/*    */   
/*    */   public final Behavior.Status getStatus() {
/* 15 */     return this.status;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean tryStart(ServerLevel level, E body, long timestamp) {
/* 20 */     if (trigger(level, (LivingEntity)body, timestamp)) {
/* 21 */       this.status = Behavior.Status.RUNNING;
/* 22 */       return true;
/*    */     } 
/* 24 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void tickOrStop(ServerLevel level, E body, long timestamp) {
/* 29 */     doStop(level, body, timestamp);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void doStop(ServerLevel level, E body, long timestamp) {
/* 34 */     this.status = Behavior.Status.STOPPED;
/*    */   }
/*    */ 
/*    */   
/*    */   public String debugString() {
/* 39 */     return getClass().getSimpleName();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/OneShot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */