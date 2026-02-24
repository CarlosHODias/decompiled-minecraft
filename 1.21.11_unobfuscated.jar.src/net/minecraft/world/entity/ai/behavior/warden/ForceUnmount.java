/*    */ package net.minecraft.world.entity.ai.behavior.warden;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*    */ 
/*    */ public class ForceUnmount extends Behavior<LivingEntity> {
/*    */   public ForceUnmount() {
/* 10 */     super((Map)ImmutableMap.of());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel level, LivingEntity body) {
/* 15 */     return body.isPassenger();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel level, LivingEntity body, long timestamp) {
/* 20 */     body.unRide();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/warden/ForceUnmount.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */