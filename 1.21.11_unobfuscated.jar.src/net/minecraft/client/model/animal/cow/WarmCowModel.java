/*    */ package net.minecraft.client.model.animal.cow;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ 
/*    */ public class WarmCowModel
/*    */   extends CowModel
/*    */ {
/*    */   public WarmCowModel(ModelPart root) {
/* 13 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 17 */     MeshDefinition mesh = createBaseCowModel();
/* 18 */     mesh.getRoot().addOrReplaceChild("head", CubeListBuilder.create()
/* 19 */         .texOffs(0, 0).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F)
/* 20 */         .texOffs(1, 33).addBox(-3.0F, 1.0F, -7.0F, 6.0F, 3.0F, 1.0F)
/* 21 */         .texOffs(27, 0).addBox(-8.0F, -3.0F, -5.0F, 4.0F, 2.0F, 2.0F)
/* 22 */         .texOffs(39, 0).addBox(-8.0F, -5.0F, -5.0F, 2.0F, 2.0F, 2.0F)
/* 23 */         .texOffs(27, 0).mirror().addBox(4.0F, -3.0F, -5.0F, 4.0F, 2.0F, 2.0F).mirror(false)
/* 24 */         .texOffs(39, 0).mirror().addBox(6.0F, -5.0F, -5.0F, 2.0F, 2.0F, 2.0F).mirror(false), 
/* 25 */         PartPose.offset(0.0F, 4.0F, -8.0F));
/* 26 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/cow/WarmCowModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */