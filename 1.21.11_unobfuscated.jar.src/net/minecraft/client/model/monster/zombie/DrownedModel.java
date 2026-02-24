/*    */ package net.minecraft.client.model.monster.zombie;
/*    */ 
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombieRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class DrownedModel extends ZombieModel<ZombieRenderState> {
/*    */   public DrownedModel(ModelPart root) {
/* 17 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer(CubeDeformation g) {
/* 21 */     MeshDefinition mesh = HumanoidModel.createMesh(g, 0.0F);
/* 22 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 24 */     root.addOrReplaceChild("left_arm", 
/* 25 */         CubeListBuilder.create()
/* 26 */         .texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, g), 
/* 27 */         PartPose.offset(5.0F, 2.0F, 0.0F));
/*    */     
/* 29 */     root.addOrReplaceChild("left_leg", 
/* 30 */         CubeListBuilder.create()
/* 31 */         .texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, g), 
/* 32 */         PartPose.offset(1.9F, 12.0F, 0.0F));
/*    */ 
/*    */     
/* 35 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(ZombieRenderState state) {
/* 40 */     super.setupAnim(state);
/*    */     
/* 42 */     if (state.leftArmPose == HumanoidModel.ArmPose.THROW_TRIDENT) {
/* 43 */       this.leftArm.xRot = this.leftArm.xRot * 0.5F - 3.1415927F;
/* 44 */       this.leftArm.yRot = 0.0F;
/*    */     } 
/*    */     
/* 47 */     if (state.rightArmPose == HumanoidModel.ArmPose.THROW_TRIDENT) {
/* 48 */       this.rightArm.xRot = this.rightArm.xRot * 0.5F - 3.1415927F;
/* 49 */       this.rightArm.yRot = 0.0F;
/*    */     } 
/*    */     
/* 52 */     float swimAmount = state.swimAmount;
/* 53 */     if (swimAmount > 0.0F) {
/* 54 */       this.rightArm.xRot = Mth.rotLerpRad(swimAmount, this.rightArm.xRot, -2.5132742F) + swimAmount * 0.35F * Mth.sin((0.1F * state.ageInTicks));
/* 55 */       this.leftArm.xRot = Mth.rotLerpRad(swimAmount, this.leftArm.xRot, -2.5132742F) - swimAmount * 0.35F * Mth.sin((0.1F * state.ageInTicks));
/* 56 */       this.rightArm.zRot = Mth.rotLerpRad(swimAmount, this.rightArm.zRot, -0.15F);
/* 57 */       this.leftArm.zRot = Mth.rotLerpRad(swimAmount, this.leftArm.zRot, 0.15F);
/*    */       
/* 59 */       this.leftLeg.xRot -= swimAmount * 0.55F * Mth.sin((0.1F * state.ageInTicks));
/* 60 */       this.rightLeg.xRot += swimAmount * 0.55F * Mth.sin((0.1F * state.ageInTicks));
/* 61 */       this.head.xRot = 0.0F;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/zombie/DrownedModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */