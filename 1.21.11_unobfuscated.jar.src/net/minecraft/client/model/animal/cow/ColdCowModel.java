/*    */ package net.minecraft.client.model.animal.cow;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ 
/*    */ public class ColdCowModel
/*    */   extends CowModel
/*    */ {
/*    */   public ColdCowModel(ModelPart root) {
/* 16 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 20 */     MeshDefinition mesh = createBaseCowModel();
/* 21 */     mesh.getRoot().addOrReplaceChild("body", CubeListBuilder.create()
/* 22 */         .texOffs(20, 32).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F, new CubeDeformation(0.5F))
/* 23 */         .texOffs(18, 4).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F)
/* 24 */         .texOffs(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F), 
/* 25 */         PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));
/*    */     
/* 27 */     PartDefinition head = mesh.getRoot().addOrReplaceChild("head", CubeListBuilder.create()
/* 28 */         .texOffs(0, 0).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F)
/* 29 */         .texOffs(9, 33).addBox(-3.0F, 1.0F, -7.0F, 6.0F, 3.0F, 1.0F), 
/* 30 */         PartPose.offset(0.0F, 4.0F, -8.0F));
/*    */     
/* 32 */     head.addOrReplaceChild("right_horn", CubeListBuilder.create()
/* 33 */         .texOffs(0, 40).addBox(-1.5F, -4.5F, -0.5F, 2.0F, 6.0F, 2.0F), 
/* 34 */         PartPose.offsetAndRotation(-4.5F, -2.5F, -3.5F, 1.5708F, 0.0F, 0.0F));
/*    */     
/* 36 */     head.addOrReplaceChild("left_horn", CubeListBuilder.create()
/* 37 */         .texOffs(0, 32).addBox(-1.5F, -3.0F, -0.5F, 2.0F, 6.0F, 2.0F), 
/* 38 */         PartPose.offsetAndRotation(5.5F, -2.5F, -5.0F, 1.5708F, 0.0F, 0.0F));
/* 39 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/cow/ColdCowModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */