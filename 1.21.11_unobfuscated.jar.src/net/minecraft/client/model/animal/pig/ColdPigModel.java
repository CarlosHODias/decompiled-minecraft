/*    */ package net.minecraft.client.model.animal.pig;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class ColdPigModel
/*    */   extends PigModel
/*    */ {
/*    */   public ColdPigModel(ModelPart root) {
/* 15 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer(CubeDeformation g) {
/* 19 */     MeshDefinition basePigModel = createBasePigModel(g);
/* 20 */     PartDefinition root = basePigModel.getRoot();
/*    */     
/* 22 */     root.addOrReplaceChild("body", CubeListBuilder.create()
/* 23 */         .texOffs(28, 8).addBox(-5.0F, -10.0F, -7.0F, 10.0F, 16.0F, 8.0F)
/* 24 */         .texOffs(28, 32).addBox(-5.0F, -10.0F, -7.0F, 10.0F, 16.0F, 8.0F, new CubeDeformation(0.5F)), 
/* 25 */         PartPose.offsetAndRotation(0.0F, 11.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));
/*    */     
/* 27 */     return LayerDefinition.create(basePigModel, 64, 64);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/pig/ColdPigModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */