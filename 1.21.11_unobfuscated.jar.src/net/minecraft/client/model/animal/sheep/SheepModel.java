/*    */ package net.minecraft.client.model.animal.sheep;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.client.model.BabyModelTransform;
/*    */ import net.minecraft.client.model.QuadrupedModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SheepRenderState;
/*    */ 
/*    */ public class SheepModel
/*    */   extends QuadrupedModel<SheepRenderState>
/*    */ {
/* 20 */   public static final MeshTransformer BABY_TRANSFORMER = (MeshTransformer)new BabyModelTransform(false, 8.0F, 4.0F, 2.0F, 2.0F, 24.0F, Set.of("head"));
/*    */   
/*    */   public SheepModel(ModelPart root) {
/* 23 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 27 */     MeshDefinition mesh = QuadrupedModel.createBodyMesh(12, false, true, CubeDeformation.NONE);
/* 28 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 30 */     root.addOrReplaceChild("head", 
/* 31 */         CubeListBuilder.create()
/* 32 */         .texOffs(0, 0).addBox(-3.0F, -4.0F, -6.0F, 6.0F, 6.0F, 8.0F), 
/* 33 */         PartPose.offset(0.0F, 6.0F, -8.0F));
/*    */     
/* 35 */     root.addOrReplaceChild("body", 
/* 36 */         CubeListBuilder.create()
/* 37 */         .texOffs(28, 8).addBox(-4.0F, -10.0F, -7.0F, 8.0F, 16.0F, 6.0F), 
/* 38 */         PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));
/*    */     
/* 40 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(SheepRenderState state) {
/* 45 */     super.setupAnim((LivingEntityRenderState)state);
/* 46 */     this.head.y += state.headEatPositionScale * 9.0F * state.ageScale;
/* 47 */     this.head.xRot = state.headEatAngleScale;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/sheep/SheepModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */