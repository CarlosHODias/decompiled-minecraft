/*    */ package net.minecraft.client.model.monster.slime;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ 
/*    */ public class SlimeModel
/*    */   extends EntityModel<EntityRenderState> {
/*    */   public SlimeModel(ModelPart root) {
/* 15 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createOuterBodyLayer() {
/* 19 */     MeshDefinition mesh = new MeshDefinition();
/* 20 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 22 */     root.addOrReplaceChild("cube", 
/* 23 */         CubeListBuilder.create()
/* 24 */         .texOffs(0, 0).addBox(-4.0F, 16.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
/*    */ 
/*    */ 
/*    */     
/* 28 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createInnerBodyLayer() {
/* 32 */     MeshDefinition mesh = new MeshDefinition();
/* 33 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 35 */     root.addOrReplaceChild("cube", 
/* 36 */         CubeListBuilder.create()
/* 37 */         .texOffs(0, 16).addBox(-3.0F, 17.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.ZERO);
/*    */ 
/*    */     
/* 40 */     root.addOrReplaceChild("right_eye", 
/* 41 */         CubeListBuilder.create()
/* 42 */         .texOffs(32, 0).addBox(-3.25F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
/*    */ 
/*    */     
/* 45 */     root.addOrReplaceChild("left_eye", 
/* 46 */         CubeListBuilder.create()
/* 47 */         .texOffs(32, 4).addBox(1.25F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
/*    */ 
/*    */     
/* 50 */     root.addOrReplaceChild("mouth", 
/* 51 */         CubeListBuilder.create()
/* 52 */         .texOffs(32, 8).addBox(0.0F, 21.0F, -3.5F, 1.0F, 1.0F, 1.0F), PartPose.ZERO);
/*    */ 
/*    */ 
/*    */     
/* 56 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/slime/SlimeModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */