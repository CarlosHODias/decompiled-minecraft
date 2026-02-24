/*    */ package net.minecraft.client.model.object.banner;
/*    */ 
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class BannerFlagModel extends Model<Float> {
/*    */   private final ModelPart flag;
/*    */   
/*    */   public BannerFlagModel(ModelPart root) {
/* 17 */     super(root, RenderTypes::entitySolid);
/* 18 */     this.flag = root.getChild("flag");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createFlagLayer(boolean standing) {
/* 22 */     MeshDefinition mesh = new MeshDefinition();
/* 23 */     PartDefinition root = mesh.getRoot();
/* 24 */     root.addOrReplaceChild("flag", 
/* 25 */         CubeListBuilder.create()
/* 26 */         .texOffs(0, 0).addBox(-10.0F, 0.0F, -2.0F, 20.0F, 40.0F, 1.0F), 
/* 27 */         PartPose.offset(0.0F, standing ? -44.0F : -20.5F, standing ? 0.0F : 10.5F));
/*    */     
/* 29 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(Float phase) {
/* 34 */     super.setupAnim(phase);
/* 35 */     this.flag.xRot = (-0.0125F + 0.01F * Mth.cos((6.2831855F * phase))) * 3.1415927F;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/banner/BannerFlagModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */