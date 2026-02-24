/*    */ package net.minecraft.client.model.animal.bee;
/*    */ 
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.util.Unit;
/*    */ 
/*    */ public class BeeStingerModel
/*    */   extends Model<Unit> {
/*    */   public BeeStingerModel(ModelPart root) {
/* 16 */     super(root, RenderTypes::entityCutout);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 20 */     MeshDefinition mesh = new MeshDefinition();
/* 21 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 23 */     CubeListBuilder cross = CubeListBuilder.create().texOffs(0, 0)
/* 24 */       .addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F);
/* 25 */     root.addOrReplaceChild("cross_1", cross, PartPose.rotation(0.7853982F, 0.0F, 0.0F));
/* 26 */     root.addOrReplaceChild("cross_2", cross, PartPose.rotation(2.3561945F, 0.0F, 0.0F));
/*    */     
/* 28 */     return LayerDefinition.create(mesh, 16, 16);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/bee/BeeStingerModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */