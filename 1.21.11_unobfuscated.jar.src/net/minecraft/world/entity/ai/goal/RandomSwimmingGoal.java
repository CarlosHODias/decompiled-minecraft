/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class RandomSwimmingGoal
/*    */   extends RandomStrollGoal {
/*    */   public RandomSwimmingGoal(PathfinderMob mob, double speedModifier, int interval) {
/* 10 */     super(mob, speedModifier, interval);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Vec3 getPosition() {
/* 15 */     return BehaviorUtils.getRandomSwimmablePos(this.mob, 10, 7);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/goal/RandomSwimmingGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */