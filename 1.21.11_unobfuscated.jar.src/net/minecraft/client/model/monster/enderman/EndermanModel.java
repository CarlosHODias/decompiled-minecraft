/*    */ package net.minecraft.client.model.monster.enderman;
/*    */ 
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.EndermanRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class EndermanModel<T extends EndermanRenderState> extends HumanoidModel<T> {
/*    */   public EndermanModel(ModelPart root) {
/* 17 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 21 */     float yOffset = -14.0F;
/* 22 */     MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, -14.0F);
/* 23 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 25 */     PartDefinition head = root.addOrReplaceChild("head", 
/* 26 */         CubeListBuilder.create()
/* 27 */         .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), 
/* 28 */         PartPose.offset(0.0F, -13.0F, 0.0F));
/*    */     
/* 30 */     head.addOrReplaceChild("hat", 
/* 31 */         CubeListBuilder.create()
/* 32 */         .texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)), PartPose.ZERO);
/*    */ 
/*    */     
/* 35 */     root.addOrReplaceChild("body", 
/* 36 */         CubeListBuilder.create()
/* 37 */         .texOffs(32, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F), 
/* 38 */         PartPose.offset(0.0F, -14.0F, 0.0F));
/*    */     
/* 40 */     root.addOrReplaceChild("right_arm", 
/* 41 */         CubeListBuilder.create()
/* 42 */         .texOffs(56, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F), 
/* 43 */         PartPose.offset(-5.0F, -12.0F, 0.0F));
/*    */     
/* 45 */     root.addOrReplaceChild("left_arm", 
/* 46 */         CubeListBuilder.create()
/* 47 */         .texOffs(56, 0).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F), 
/* 48 */         PartPose.offset(5.0F, -12.0F, 0.0F));
/*    */     
/* 50 */     root.addOrReplaceChild("right_leg", 
/* 51 */         CubeListBuilder.create()
/* 52 */         .texOffs(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 30.0F, 2.0F), 
/* 53 */         PartPose.offset(-2.0F, -5.0F, 0.0F));
/*    */     
/* 55 */     root.addOrReplaceChild("left_leg", 
/* 56 */         CubeListBuilder.create()
/* 57 */         .texOffs(56, 0).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 30.0F, 2.0F), 
/* 58 */         PartPose.offset(2.0F, -5.0F, 0.0F));
/*    */ 
/*    */     
/* 61 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(T state) {
/* 66 */     super.setupAnim((HumanoidRenderState)state);
/*    */     
/* 68 */     this.head.visible = true;
/*    */     
/* 70 */     this.rightArm.xRot *= 0.5F;
/* 71 */     this.leftArm.xRot *= 0.5F;
/* 72 */     this.rightLeg.xRot *= 0.5F;
/* 73 */     this.leftLeg.xRot *= 0.5F;
/*    */     
/* 75 */     float max = 0.4F;
/* 76 */     this.rightArm.xRot = Mth.clamp(this.rightArm.xRot, -0.4F, 0.4F);
/* 77 */     this.leftArm.xRot = Mth.clamp(this.leftArm.xRot, -0.4F, 0.4F);
/* 78 */     this.rightLeg.xRot = Mth.clamp(this.rightLeg.xRot, -0.4F, 0.4F);
/* 79 */     this.leftLeg.xRot = Mth.clamp(this.leftLeg.xRot, -0.4F, 0.4F);
/*    */ 
/*    */     
/* 82 */     if (((EndermanRenderState)state).carriedBlock != null) {
/* 83 */       this.rightArm.xRot = -0.5F;
/* 84 */       this.leftArm.xRot = -0.5F;
/* 85 */       this.rightArm.zRot = 0.05F;
/* 86 */       this.leftArm.zRot = -0.05F;
/*    */     } 
/*    */     
/* 89 */     if (((EndermanRenderState)state).isCreepy) {
/* 90 */       float amt = 5.0F;
/* 91 */       this.head.y -= 5.0F;
/* 92 */       this.hat.y += 5.0F;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/enderman/EndermanModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */