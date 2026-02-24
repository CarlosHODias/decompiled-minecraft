/*    */ package net.minecraft.client.model.monster.skeleton;
/*    */ 
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.BoggedRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
/*    */ 
/*    */ public class BoggedModel extends SkeletonModel<BoggedRenderState> {
/*    */   private final ModelPart mushrooms;
/*    */   
/*    */   public BoggedModel(ModelPart root) {
/* 19 */     super(root);
/* 20 */     this.mushrooms = root.getChild("head").getChild("mushrooms");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 24 */     MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
/* 25 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 27 */     SkeletonModel.createDefaultSkeletonMesh(root);
/*    */     
/* 29 */     PartDefinition mushrooms = root.getChild("head").addOrReplaceChild("mushrooms", CubeListBuilder.create(), PartPose.ZERO);
/* 30 */     mushrooms.addOrReplaceChild("red_mushroom_1", 
/* 31 */         CubeListBuilder.create()
/* 32 */         .texOffs(50, 16).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 4.0F, 0.0F), 
/* 33 */         PartPose.offsetAndRotation(3.0F, -8.0F, 3.0F, 0.0F, 0.7853982F, 0.0F));
/*    */     
/* 35 */     mushrooms.addOrReplaceChild("red_mushroom_2", 
/* 36 */         CubeListBuilder.create()
/* 37 */         .texOffs(50, 16).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 4.0F, 0.0F), 
/* 38 */         PartPose.offsetAndRotation(3.0F, -8.0F, 3.0F, 0.0F, 2.3561945F, 0.0F));
/*    */     
/* 40 */     mushrooms.addOrReplaceChild("brown_mushroom_1", 
/* 41 */         CubeListBuilder.create()
/* 42 */         .texOffs(50, 22).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 4.0F, 0.0F), 
/* 43 */         PartPose.offsetAndRotation(-3.0F, -8.0F, -3.0F, 0.0F, 0.7853982F, 0.0F));
/*    */     
/* 45 */     mushrooms.addOrReplaceChild("brown_mushroom_2", 
/* 46 */         CubeListBuilder.create()
/* 47 */         .texOffs(50, 22).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 4.0F, 0.0F), 
/* 48 */         PartPose.offsetAndRotation(-3.0F, -8.0F, -3.0F, 0.0F, 2.3561945F, 0.0F));
/*    */     
/* 50 */     mushrooms.addOrReplaceChild("brown_mushroom_3", 
/* 51 */         CubeListBuilder.create()
/* 52 */         .texOffs(50, 28).addBox(-3.0F, -4.0F, 0.0F, 6.0F, 4.0F, 0.0F), 
/* 53 */         PartPose.offsetAndRotation(-2.0F, -1.0F, 4.0F, -1.5707964F, 0.0F, 0.7853982F));
/*    */     
/* 55 */     mushrooms.addOrReplaceChild("brown_mushroom_4", 
/* 56 */         CubeListBuilder.create()
/* 57 */         .texOffs(50, 28).addBox(-3.0F, -4.0F, 0.0F, 6.0F, 4.0F, 0.0F), 
/* 58 */         PartPose.offsetAndRotation(-2.0F, -1.0F, 4.0F, -1.5707964F, 0.0F, 2.3561945F));
/*    */ 
/*    */     
/* 61 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(BoggedRenderState state) {
/* 66 */     super.setupAnim(state);
/* 67 */     this.mushrooms.visible = !state.isSheared;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/skeleton/BoggedModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */