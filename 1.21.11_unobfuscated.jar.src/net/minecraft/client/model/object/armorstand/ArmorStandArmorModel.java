/*    */ package net.minecraft.client.model.object.armorstand;
/*    */ 
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.ArmorModelSet;
/*    */ import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ 
/*    */ public class ArmorStandArmorModel
/*    */   extends HumanoidModel<ArmorStandRenderState> {
/*    */   public ArmorStandArmorModel(ModelPart root) {
/* 18 */     super(root);
/*    */   }
/*    */   
/*    */   public static ArmorModelSet<LayerDefinition> createArmorLayerSet(CubeDeformation innerDeformation, CubeDeformation outerDeformation) {
/* 22 */     return createArmorMeshSet(ArmorStandArmorModel::createBaseMesh, innerDeformation, outerDeformation)
/* 23 */       .map(mesh -> LayerDefinition.create(mesh, 64, 32));
/*    */   }
/*    */   
/*    */   private static MeshDefinition createBaseMesh(CubeDeformation g) {
/* 27 */     MeshDefinition mesh = HumanoidModel.createMesh(g, 0.0F);
/* 28 */     PartDefinition root = mesh.getRoot();
/* 29 */     PartDefinition head = root.addOrReplaceChild("head", 
/* 30 */         CubeListBuilder.create()
/* 31 */         .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, g), 
/* 32 */         PartPose.offset(0.0F, 1.0F, 0.0F));
/*    */     
/* 34 */     head.addOrReplaceChild("hat", 
/* 35 */         CubeListBuilder.create()
/* 36 */         .texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, g.extend(0.5F)), PartPose.ZERO);
/*    */ 
/*    */     
/* 39 */     root.addOrReplaceChild("right_leg", 
/* 40 */         CubeListBuilder.create()
/* 41 */         .texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, g.extend(-0.1F)), 
/* 42 */         PartPose.offset(-1.9F, 11.0F, 0.0F));
/*    */     
/* 44 */     root.addOrReplaceChild("left_leg", 
/* 45 */         CubeListBuilder.create()
/* 46 */         .texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, g.extend(-0.1F)), 
/* 47 */         PartPose.offset(1.9F, 11.0F, 0.0F));
/*    */ 
/*    */     
/* 50 */     return mesh;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(ArmorStandRenderState state) {
/* 55 */     super.setupAnim((HumanoidRenderState)state);
/*    */     
/* 57 */     this.head.xRot = 0.017453292F * state.headPose.x();
/* 58 */     this.head.yRot = 0.017453292F * state.headPose.y();
/* 59 */     this.head.zRot = 0.017453292F * state.headPose.z();
/*    */     
/* 61 */     this.body.xRot = 0.017453292F * state.bodyPose.x();
/* 62 */     this.body.yRot = 0.017453292F * state.bodyPose.y();
/* 63 */     this.body.zRot = 0.017453292F * state.bodyPose.z();
/*    */     
/* 65 */     this.leftArm.xRot = 0.017453292F * state.leftArmPose.x();
/* 66 */     this.leftArm.yRot = 0.017453292F * state.leftArmPose.y();
/* 67 */     this.leftArm.zRot = 0.017453292F * state.leftArmPose.z();
/*    */     
/* 69 */     this.rightArm.xRot = 0.017453292F * state.rightArmPose.x();
/* 70 */     this.rightArm.yRot = 0.017453292F * state.rightArmPose.y();
/* 71 */     this.rightArm.zRot = 0.017453292F * state.rightArmPose.z();
/*    */     
/* 73 */     this.leftLeg.xRot = 0.017453292F * state.leftLegPose.x();
/* 74 */     this.leftLeg.yRot = 0.017453292F * state.leftLegPose.y();
/* 75 */     this.leftLeg.zRot = 0.017453292F * state.leftLegPose.z();
/*    */     
/* 77 */     this.rightLeg.xRot = 0.017453292F * state.rightLegPose.x();
/* 78 */     this.rightLeg.yRot = 0.017453292F * state.rightLegPose.y();
/* 79 */     this.rightLeg.zRot = 0.017453292F * state.rightLegPose.z();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/armorstand/ArmorStandArmorModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */