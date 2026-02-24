/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.core.Rotations;
/*    */ import net.minecraft.world.entity.decoration.ArmorStand;
/*    */ 
/*    */ public class ArmorStandRenderState extends HumanoidRenderState {
/*    */   public float yRot;
/*    */   public float wiggle;
/*    */   public boolean isMarker;
/*    */   public boolean isSmall;
/*    */   public boolean showArms;
/*    */   public boolean showBasePlate = true;
/* 13 */   public Rotations headPose = ArmorStand.DEFAULT_HEAD_POSE;
/* 14 */   public Rotations bodyPose = ArmorStand.DEFAULT_BODY_POSE;
/* 15 */   public Rotations leftArmPose = ArmorStand.DEFAULT_LEFT_ARM_POSE;
/* 16 */   public Rotations rightArmPose = ArmorStand.DEFAULT_RIGHT_ARM_POSE;
/* 17 */   public Rotations leftLegPose = ArmorStand.DEFAULT_LEFT_LEG_POSE;
/* 18 */   public Rotations rightLegPose = ArmorStand.DEFAULT_RIGHT_LEG_POSE;
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/ArmorStandRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */