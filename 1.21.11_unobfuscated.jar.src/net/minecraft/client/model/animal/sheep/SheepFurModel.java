/*    */ package net.minecraft.client.model.animal.sheep;
/*    */ 
/*    */ import net.minecraft.client.model.QuadrupedModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SheepRenderState;
/*    */ 
/*    */ public class SheepFurModel
/*    */   extends QuadrupedModel<SheepRenderState>
/*    */ {
/*    */   public SheepFurModel(ModelPart root) {
/* 18 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createFurLayer() {
/* 22 */     MeshDefinition mesh = new MeshDefinition();
/* 23 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 25 */     root.addOrReplaceChild("head", 
/* 26 */         CubeListBuilder.create()
/* 27 */         .texOffs(0, 0).addBox(-3.0F, -4.0F, -4.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.6F)), 
/* 28 */         PartPose.offset(0.0F, 6.0F, -8.0F));
/*    */     
/* 30 */     root.addOrReplaceChild("body", 
/* 31 */         CubeListBuilder.create()
/* 32 */         .texOffs(28, 8).addBox(-4.0F, -10.0F, -7.0F, 8.0F, 16.0F, 6.0F, new CubeDeformation(1.75F)), 
/* 33 */         PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));
/*    */ 
/*    */     
/* 36 */     CubeListBuilder leg = CubeListBuilder.create()
/* 37 */       .texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F));
/* 38 */     root.addOrReplaceChild("right_hind_leg", leg, PartPose.offset(-3.0F, 12.0F, 7.0F));
/* 39 */     root.addOrReplaceChild("left_hind_leg", leg, PartPose.offset(3.0F, 12.0F, 7.0F));
/* 40 */     root.addOrReplaceChild("right_front_leg", leg, PartPose.offset(-3.0F, 12.0F, -5.0F));
/* 41 */     root.addOrReplaceChild("left_front_leg", leg, PartPose.offset(3.0F, 12.0F, -5.0F));
/*    */     
/* 43 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(SheepRenderState state) {
/* 48 */     super.setupAnim((LivingEntityRenderState)state);
/* 49 */     this.head.y += state.headEatPositionScale * 9.0F * state.ageScale;
/* 50 */     this.head.xRot = state.headEatAngleScale;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/sheep/SheepFurModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */