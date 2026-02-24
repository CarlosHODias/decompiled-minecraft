/*    */ package net.minecraft.client.model.object.projectile;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.ShulkerBulletRenderState;
/*    */ 
/*    */ public class ShulkerBulletModel
/*    */   extends EntityModel<ShulkerBulletRenderState>
/*    */ {
/*    */   private static final String MAIN = "main";
/*    */   private final ModelPart main;
/*    */   
/*    */   public ShulkerBulletModel(ModelPart root) {
/* 19 */     super(root);
/* 20 */     this.main = root.getChild("main");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 24 */     MeshDefinition mesh = new MeshDefinition();
/* 25 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 27 */     root.addOrReplaceChild("main", 
/* 28 */         CubeListBuilder.create()
/* 29 */         .texOffs(0, 0).addBox(-4.0F, -4.0F, -1.0F, 8.0F, 8.0F, 2.0F)
/* 30 */         .texOffs(0, 10).addBox(-1.0F, -4.0F, -4.0F, 2.0F, 8.0F, 8.0F)
/* 31 */         .texOffs(20, 0).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 2.0F, 8.0F), PartPose.ZERO);
/*    */ 
/*    */ 
/*    */     
/* 35 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(ShulkerBulletRenderState state) {
/* 40 */     super.setupAnim(state);
/* 41 */     this.main.yRot = state.yRot * 0.017453292F;
/* 42 */     this.main.xRot = state.xRot * 0.017453292F;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/projectile/ShulkerBulletModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */