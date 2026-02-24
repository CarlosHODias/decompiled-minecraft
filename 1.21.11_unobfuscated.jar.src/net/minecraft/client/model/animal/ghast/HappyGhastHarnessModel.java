/*    */ package net.minecraft.client.model.animal.ghast;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.HappyGhastRenderState;
/*    */ 
/*    */ public class HappyGhastHarnessModel
/*    */   extends EntityModel<HappyGhastRenderState> {
/*    */   private static final float GOGGLES_Y_OFFSET = 14.0F;
/*    */   private final ModelPart goggles;
/*    */   
/*    */   public HappyGhastHarnessModel(ModelPart root) {
/* 20 */     super(root);
/* 21 */     this.goggles = root.getChild("goggles");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createHarnessLayer(boolean baby) {
/* 25 */     MeshDefinition meshdefinition = new MeshDefinition();
/* 26 */     PartDefinition root = meshdefinition.getRoot();
/*    */     
/* 28 */     root.addOrReplaceChild("harness", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
/*    */     
/* 30 */     root.addOrReplaceChild("goggles", CubeListBuilder.create().texOffs(0, 32).addBox(-8.0F, -2.5F, -2.5F, 16.0F, 5.0F, 5.0F, new CubeDeformation(0.15F)), PartPose.offset(0.0F, 14.0F, -5.5F));
/*    */     
/* 32 */     return LayerDefinition.create(meshdefinition, 64, 64).apply(MeshTransformer.scaling(4.0F)).apply(baby ? HappyGhastModel.BABY_TRANSFORMER : MeshTransformer.IDENTITY);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(HappyGhastRenderState state) {
/* 37 */     super.setupAnim(state);
/* 38 */     if (state.isRidden) {
/* 39 */       this.goggles.xRot = 0.0F;
/* 40 */       this.goggles.y = 14.0F;
/*    */     } else {
/* 42 */       this.goggles.xRot = -0.7854F;
/* 43 */       this.goggles.y = 9.0F;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/ghast/HappyGhastHarnessModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */