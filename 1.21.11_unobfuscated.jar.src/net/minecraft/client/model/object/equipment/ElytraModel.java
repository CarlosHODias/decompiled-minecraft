/*    */ package net.minecraft.client.model.object.equipment;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ 
/*    */ public class ElytraModel
/*    */   extends EntityModel<HumanoidRenderState>
/*    */ {
/* 17 */   public static final MeshTransformer BABY_TRANSFORMER = MeshTransformer.scaling(0.5F);
/*    */   
/*    */   private final ModelPart rightWing;
/*    */   private final ModelPart leftWing;
/*    */   
/*    */   public ElytraModel(ModelPart root) {
/* 23 */     super(root);
/* 24 */     this.leftWing = root.getChild("left_wing");
/* 25 */     this.rightWing = root.getChild("right_wing");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createLayer() {
/* 29 */     MeshDefinition mesh = new MeshDefinition();
/* 30 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 32 */     CubeDeformation windDeformation = new CubeDeformation(1.0F);
/* 33 */     root.addOrReplaceChild("left_wing", 
/* 34 */         CubeListBuilder.create()
/* 35 */         .texOffs(22, 0).addBox(-10.0F, 0.0F, 0.0F, 10.0F, 20.0F, 2.0F, windDeformation), 
/* 36 */         PartPose.offsetAndRotation(5.0F, 0.0F, 0.0F, 0.2617994F, 0.0F, -0.2617994F));
/*    */     
/* 38 */     root.addOrReplaceChild("right_wing", 
/* 39 */         CubeListBuilder.create()
/* 40 */         .texOffs(22, 0).mirror().addBox(0.0F, 0.0F, 0.0F, 10.0F, 20.0F, 2.0F, windDeformation), 
/* 41 */         PartPose.offsetAndRotation(-5.0F, 0.0F, 0.0F, 0.2617994F, 0.0F, 0.2617994F));
/*    */ 
/*    */     
/* 44 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(HumanoidRenderState state) {
/* 49 */     super.setupAnim(state);
/*    */     
/* 51 */     this.leftWing.y = state.isCrouching ? 3.0F : 0.0F;
/* 52 */     this.leftWing.xRot = state.elytraRotX;
/* 53 */     this.leftWing.zRot = state.elytraRotZ;
/* 54 */     this.leftWing.yRot = state.elytraRotY;
/*    */     
/* 56 */     this.rightWing.yRot = -this.leftWing.yRot;
/* 57 */     this.rightWing.y = this.leftWing.y;
/* 58 */     this.rightWing.xRot = this.leftWing.xRot;
/* 59 */     this.rightWing.zRot = -this.leftWing.zRot;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/equipment/ElytraModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */