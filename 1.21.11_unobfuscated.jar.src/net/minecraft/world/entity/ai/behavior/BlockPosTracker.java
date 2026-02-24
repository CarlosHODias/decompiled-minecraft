/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BlockPosTracker implements PositionTracker {
/*    */   private final BlockPos blockPos;
/*    */   
/*    */   public BlockPosTracker(BlockPos blockPos) {
/* 12 */     this.blockPos = blockPos.immutable();
/* 13 */     this.centerPosition = Vec3.atCenterOf((Vec3i)blockPos);
/*    */   }
/*    */   private final Vec3 centerPosition;
/*    */   public BlockPosTracker(Vec3 vec) {
/* 17 */     this.blockPos = BlockPos.containing((Position)vec);
/* 18 */     this.centerPosition = vec;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 currentPosition() {
/* 23 */     return this.centerPosition;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos currentBlockPosition() {
/* 28 */     return this.blockPos;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isVisibleBy(LivingEntity body) {
/* 33 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 38 */     return "BlockPosTracker{blockPos=" + String.valueOf(this.blockPos) + ", centerPosition=" + String.valueOf(this.centerPosition) + "}";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/BlockPosTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */