/*    */ package net.minecraft.world.entity.ai.memory;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
/*    */ import net.minecraft.world.entity.ai.behavior.EntityTracker;
/*    */ import net.minecraft.world.entity.ai.behavior.PositionTracker;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class WalkTarget
/*    */ {
/*    */   private final PositionTracker target;
/*    */   
/*    */   public WalkTarget(BlockPos target, float speedModifier, int closeEnoughDist) {
/* 16 */     this((PositionTracker)new BlockPosTracker(target), speedModifier, closeEnoughDist);
/*    */   }
/*    */   private final float speedModifier; private final int closeEnoughDist;
/*    */   public WalkTarget(Vec3 target, float speedModifier, int closeEnoughDist) {
/* 20 */     this((PositionTracker)new BlockPosTracker(BlockPos.containing((Position)target)), speedModifier, closeEnoughDist);
/*    */   }
/*    */   
/*    */   public WalkTarget(Entity target, float speedModifier, int closeEnoughDist) {
/* 24 */     this((PositionTracker)new EntityTracker(target, false), speedModifier, closeEnoughDist);
/*    */   }
/*    */   
/*    */   public WalkTarget(PositionTracker target, float speedModifier, int closeEnoughDist) {
/* 28 */     this.target = target;
/* 29 */     this.speedModifier = speedModifier;
/* 30 */     this.closeEnoughDist = closeEnoughDist;
/*    */   }
/*    */   
/*    */   public PositionTracker getTarget() {
/* 34 */     return this.target;
/*    */   }
/*    */   
/*    */   public float getSpeedModifier() {
/* 38 */     return this.speedModifier;
/*    */   }
/*    */   
/*    */   public int getCloseEnoughDist() {
/* 42 */     return this.closeEnoughDist;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/memory/WalkTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */