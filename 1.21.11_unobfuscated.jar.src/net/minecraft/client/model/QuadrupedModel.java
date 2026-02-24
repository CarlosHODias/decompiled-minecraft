/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class QuadrupedModel<T extends LivingEntityRenderState>
/*    */   extends EntityModel<T> {
/*    */   protected final ModelPart head;
/*    */   protected final ModelPart body;
/*    */   protected final ModelPart rightHindLeg;
/*    */   protected final ModelPart leftHindLeg;
/*    */   protected final ModelPart rightFrontLeg;
/*    */   protected final ModelPart leftFrontLeg;
/*    */   
/*    */   protected QuadrupedModel(ModelPart root) {
/* 22 */     super(root);
/* 23 */     this.head = root.getChild("head");
/* 24 */     this.body = root.getChild("body");
/* 25 */     this.rightHindLeg = root.getChild("right_hind_leg");
/* 26 */     this.leftHindLeg = root.getChild("left_hind_leg");
/* 27 */     this.rightFrontLeg = root.getChild("right_front_leg");
/* 28 */     this.leftFrontLeg = root.getChild("left_front_leg");
/*    */   }
/*    */   
/*    */   public static MeshDefinition createBodyMesh(int legSize, boolean mirrorLeftLeg, boolean mirrorRightLeg, CubeDeformation g) {
/* 32 */     MeshDefinition mesh = new MeshDefinition();
/* 33 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 35 */     root.addOrReplaceChild("head", 
/* 36 */         CubeListBuilder.create()
/* 37 */         .texOffs(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, g), 
/* 38 */         PartPose.offset(0.0F, (18 - legSize), -6.0F));
/*    */     
/* 40 */     root.addOrReplaceChild("body", 
/* 41 */         CubeListBuilder.create()
/* 42 */         .texOffs(28, 8).addBox(-5.0F, -10.0F, -7.0F, 10.0F, 16.0F, 8.0F, g), 
/* 43 */         PartPose.offsetAndRotation(0.0F, (17 - legSize), 2.0F, 1.5707964F, 0.0F, 0.0F));
/*    */     
/* 45 */     createLegs(root, mirrorLeftLeg, mirrorRightLeg, legSize, g);
/* 46 */     return mesh;
/*    */   }
/*    */   
/*    */   static void createLegs(PartDefinition root, boolean mirrorLeftLeg, boolean mirrorRightLeg, int legSize, CubeDeformation g) {
/* 50 */     CubeListBuilder rightLeg = CubeListBuilder.create().mirror(mirrorRightLeg).texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, legSize, 4.0F, g);
/* 51 */     CubeListBuilder leftLeg = CubeListBuilder.create().mirror(mirrorLeftLeg).texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, legSize, 4.0F, g);
/* 52 */     root.addOrReplaceChild("right_hind_leg", rightLeg, PartPose.offset(-3.0F, (24 - legSize), 7.0F));
/* 53 */     root.addOrReplaceChild("left_hind_leg", leftLeg, PartPose.offset(3.0F, (24 - legSize), 7.0F));
/* 54 */     root.addOrReplaceChild("right_front_leg", rightLeg, PartPose.offset(-3.0F, (24 - legSize), -5.0F));
/* 55 */     root.addOrReplaceChild("left_front_leg", leftLeg, PartPose.offset(3.0F, (24 - legSize), -5.0F));
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(T state) {
/* 60 */     super.setupAnim(state);
/*    */     
/* 62 */     this.head.xRot = ((LivingEntityRenderState)state).xRot * 0.017453292F;
/* 63 */     this.head.yRot = ((LivingEntityRenderState)state).yRot * 0.017453292F;
/*    */     
/* 65 */     float animationPos = ((LivingEntityRenderState)state).walkAnimationPos;
/* 66 */     float animationSpeed = ((LivingEntityRenderState)state).walkAnimationSpeed;
/*    */     
/* 68 */     this.rightHindLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/* 69 */     this.leftHindLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 70 */     this.rightFrontLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 71 */     this.leftFrontLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/QuadrupedModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */