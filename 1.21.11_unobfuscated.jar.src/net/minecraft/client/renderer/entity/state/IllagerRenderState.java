/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.entity.monster.illager.AbstractIllager;
/*    */ 
/*    */ public class IllagerRenderState extends UndeadRenderState {
/*    */   public boolean isRiding;
/*    */   public boolean isAggressive;
/*  9 */   public HumanoidArm mainArm = HumanoidArm.RIGHT;
/* 10 */   public AbstractIllager.IllagerArmPose armPose = AbstractIllager.IllagerArmPose.NEUTRAL;
/*    */   public int maxCrossbowChargeDuration;
/*    */   public float ticksUsingItem;
/*    */   public float attackAnim;
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/IllagerRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */