/*    */ package net.minecraft.client.model.object.cart;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.MinecartRenderState;
/*    */ 
/*    */ public class MinecartModel
/*    */   extends EntityModel<MinecartRenderState> {
/*    */   public MinecartModel(ModelPart root) {
/* 15 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 19 */     MeshDefinition mesh = new MeshDefinition();
/* 20 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 22 */     int w = 20;
/* 23 */     int d = 8;
/* 24 */     int h = 16;
/* 25 */     int yOff = 4;
/*    */     
/* 27 */     root.addOrReplaceChild("bottom", 
/* 28 */         CubeListBuilder.create()
/* 29 */         .texOffs(0, 10).addBox(-10.0F, -8.0F, -1.0F, 20.0F, 16.0F, 2.0F), 
/* 30 */         PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 1.5707964F, 0.0F, 0.0F));
/*    */ 
/*    */ 
/*    */     
/* 34 */     root.addOrReplaceChild("front", 
/* 35 */         CubeListBuilder.create()
/* 36 */         .texOffs(0, 0).addBox(-8.0F, -9.0F, -1.0F, 16.0F, 8.0F, 2.0F), 
/* 37 */         PartPose.offsetAndRotation(-9.0F, 4.0F, 0.0F, 0.0F, 4.712389F, 0.0F));
/*    */     
/* 39 */     root.addOrReplaceChild("back", 
/* 40 */         CubeListBuilder.create()
/* 41 */         .texOffs(0, 0).addBox(-8.0F, -9.0F, -1.0F, 16.0F, 8.0F, 2.0F), 
/* 42 */         PartPose.offsetAndRotation(9.0F, 4.0F, 0.0F, 0.0F, 1.5707964F, 0.0F));
/*    */     
/* 44 */     root.addOrReplaceChild("left", 
/* 45 */         CubeListBuilder.create()
/* 46 */         .texOffs(0, 0).addBox(-8.0F, -9.0F, -1.0F, 16.0F, 8.0F, 2.0F), 
/* 47 */         PartPose.offsetAndRotation(0.0F, 4.0F, -7.0F, 0.0F, 3.1415927F, 0.0F));
/*    */     
/* 49 */     root.addOrReplaceChild("right", 
/* 50 */         CubeListBuilder.create()
/* 51 */         .texOffs(0, 0).addBox(-8.0F, -9.0F, -1.0F, 16.0F, 8.0F, 2.0F), 
/* 52 */         PartPose.offset(0.0F, 4.0F, 7.0F));
/*    */ 
/*    */     
/* 55 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/cart/MinecartModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */