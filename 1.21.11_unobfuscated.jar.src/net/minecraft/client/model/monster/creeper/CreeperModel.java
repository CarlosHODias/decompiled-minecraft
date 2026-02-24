/*    */ package net.minecraft.client.model.monster.creeper;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.CreeperRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class CreeperModel
/*    */   extends EntityModel<CreeperRenderState> {
/*    */   private final ModelPart head;
/*    */   private final ModelPart rightHindLeg;
/*    */   private final ModelPart leftHindLeg;
/*    */   private final ModelPart rightFrontLeg;
/*    */   private final ModelPart leftFrontLeg;
/*    */   private static final int Y_OFFSET = 6;
/*    */   
/*    */   public CreeperModel(ModelPart root) {
/* 24 */     super(root);
/* 25 */     this.head = root.getChild("head");
/* 26 */     this.leftHindLeg = root.getChild("right_hind_leg");
/* 27 */     this.rightHindLeg = root.getChild("left_hind_leg");
/* 28 */     this.leftFrontLeg = root.getChild("right_front_leg");
/* 29 */     this.rightFrontLeg = root.getChild("left_front_leg");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer(CubeDeformation g) {
/* 33 */     MeshDefinition mesh = new MeshDefinition();
/* 34 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 36 */     root.addOrReplaceChild("head", 
/* 37 */         CubeListBuilder.create()
/* 38 */         .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, g), 
/* 39 */         PartPose.offset(0.0F, 6.0F, 0.0F));
/*    */ 
/*    */ 
/*    */     
/* 43 */     root.addOrReplaceChild("body", 
/* 44 */         CubeListBuilder.create()
/* 45 */         .texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, g), 
/* 46 */         PartPose.offset(0.0F, 6.0F, 0.0F));
/*    */ 
/*    */     
/* 49 */     CubeListBuilder leg = CubeListBuilder.create()
/* 50 */       .texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, g);
/* 51 */     root.addOrReplaceChild("right_hind_leg", leg, PartPose.offset(-2.0F, 18.0F, 4.0F));
/* 52 */     root.addOrReplaceChild("left_hind_leg", leg, PartPose.offset(2.0F, 18.0F, 4.0F));
/* 53 */     root.addOrReplaceChild("right_front_leg", leg, PartPose.offset(-2.0F, 18.0F, -4.0F));
/* 54 */     root.addOrReplaceChild("left_front_leg", leg, PartPose.offset(2.0F, 18.0F, -4.0F));
/*    */     
/* 56 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(CreeperRenderState state) {
/* 61 */     super.setupAnim(state);
/*    */     
/* 63 */     this.head.yRot = state.yRot * 0.017453292F;
/* 64 */     this.head.xRot = state.xRot * 0.017453292F;
/*    */     
/* 66 */     float animationSpeed = state.walkAnimationSpeed;
/* 67 */     float animationPos = state.walkAnimationPos;
/* 68 */     this.rightHindLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/* 69 */     this.leftHindLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 70 */     this.rightFrontLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 71 */     this.leftFrontLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/creeper/CreeperModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */