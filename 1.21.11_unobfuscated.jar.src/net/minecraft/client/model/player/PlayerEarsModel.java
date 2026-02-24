/*    */ package net.minecraft.client.model.player;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class PlayerEarsModel
/*    */   extends PlayerModel {
/*    */   public PlayerEarsModel(ModelPart root) {
/* 14 */     super(root, false);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createEarsLayer() {
/* 18 */     MeshDefinition mesh = PlayerModel.createMesh(CubeDeformation.NONE, false);
/*    */     
/* 20 */     PartDefinition root = mesh.getRoot().clearRecursively();
/* 21 */     PartDefinition head = root.getChild("head");
/*    */     
/* 23 */     CubeListBuilder earCube = CubeListBuilder.create()
/* 24 */       .texOffs(24, 0)
/* 25 */       .addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(1.0F));
/* 26 */     head.addOrReplaceChild("left_ear", earCube, 
/* 27 */         PartPose.offset(-6.0F, -6.0F, 0.0F));
/*    */     
/* 29 */     head.addOrReplaceChild("right_ear", earCube, 
/* 30 */         PartPose.offset(6.0F, -6.0F, 0.0F));
/*    */ 
/*    */     
/* 33 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/player/PlayerEarsModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */