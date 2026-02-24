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
/*    */ import net.minecraft.util.Unit;
/*    */ 
/*    */ public class BannerModel
/*    */   extends Model<Unit> {
/*    */   public static final int BANNER_WIDTH = 20;
/*    */   public static final int BANNER_HEIGHT = 40;
/*    */   public static final String FLAG = "flag";
/*    */   private static final String POLE = "pole";
/*    */   private static final String BAR = "bar";
/*    */   
/*    */   public BannerModel(ModelPart root) {
/* 22 */     super(root, RenderTypes::entitySolid);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer(boolean standing) {
/* 26 */     MeshDefinition mesh = new MeshDefinition();
/* 27 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 29 */     if (standing) {
/* 30 */       root.addOrReplaceChild("pole", 
/* 31 */           CubeListBuilder.create()
/* 32 */           .texOffs(44, 0).addBox(-1.0F, -42.0F, -1.0F, 2.0F, 42.0F, 2.0F), PartPose.ZERO);
/*    */     }
/*    */ 
/*    */     
/* 36 */     root.addOrReplaceChild("bar", 
/* 37 */         CubeListBuilder.create()
/* 38 */         .texOffs(0, 42).addBox(-10.0F, standing ? -44.0F : -20.5F, standing ? -1.0F : 9.5F, 20.0F, 2.0F, 2.0F), PartPose.ZERO);
/*    */ 
/*    */     
/* 41 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/banner/BannerModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */