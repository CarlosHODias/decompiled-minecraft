/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DoNothing
/*    */   implements BehaviorControl<LivingEntity>
/*    */ {
/*    */   private final int minDuration;
/*    */   private final int maxDuration;
/* 15 */   private Behavior.Status status = Behavior.Status.STOPPED;
/*    */   private long endTimestamp;
/*    */   
/*    */   public DoNothing(int minDuration, int maxDuration) {
/* 19 */     this.minDuration = minDuration;
/* 20 */     this.maxDuration = maxDuration;
/*    */   }
/*    */ 
/*    */   
/*    */   public Behavior.Status getStatus() {
/* 25 */     return this.status;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean tryStart(ServerLevel level, LivingEntity body, long timestamp) {
/* 30 */     this.status = Behavior.Status.RUNNING;
/* 31 */     int duration = this.minDuration + level.getRandom().nextInt(this.maxDuration + 1 - this.minDuration);
/* 32 */     this.endTimestamp = timestamp + duration;
/* 33 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void tickOrStop(ServerLevel level, LivingEntity body, long timestamp) {
/* 38 */     if (timestamp > this.endTimestamp) {
/* 39 */       doStop(level, body, timestamp);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public final void doStop(ServerLevel level, LivingEntity body, long timestamp) {
/* 45 */     this.status = Behavior.Status.STOPPED;
/*    */   }
/*    */ 
/*    */   
/*    */   public String debugString() {
/* 50 */     return getClass().getSimpleName();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/DoNothing.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */