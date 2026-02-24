/*    */ package net.minecraft.client.model.animal.ghast;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartNames;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.model.monster.ghast.GhastModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HappyGhastRenderState;
/*    */ 
/*    */ public class HappyGhastModel extends EntityModel<HappyGhastRenderState> {
/* 18 */   public static final MeshTransformer BABY_TRANSFORMER = MeshTransformer.scaling(0.2375F);
/*    */   
/*    */   private static final float BODY_SQUEEZE = 0.9375F;
/*    */   
/* 22 */   private final ModelPart[] tentacles = new ModelPart[9];
/*    */   
/*    */   private final ModelPart body;
/*    */   
/*    */   public HappyGhastModel(ModelPart root) {
/* 27 */     super(root);
/* 28 */     this.body = root.getChild("body");
/* 29 */     for (int i = 0; i < this.tentacles.length; i++) {
/* 30 */       this.tentacles[i] = this.body.getChild(PartNames.tentacle(i));
/*    */     }
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer(boolean isBaby, CubeDeformation deformation) {
/* 35 */     MeshDefinition mesh = new MeshDefinition();
/* 36 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 38 */     PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, deformation), PartPose.offset(0.0F, 16.0F, 0.0F));
/* 39 */     if (isBaby) {
/* 40 */       body.addOrReplaceChild("inner_body", CubeListBuilder.create().texOffs(0, 32).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, deformation.extend(-0.5F)), PartPose.offset(0.0F, 8.0F, 0.0F));
/*    */     }
/*    */     
/* 43 */     body.addOrReplaceChild(PartNames.tentacle(0), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, deformation), PartPose.offset(-3.75F, 7.0F, -5.0F));
/* 44 */     body.addOrReplaceChild(PartNames.tentacle(1), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, deformation), PartPose.offset(1.25F, 7.0F, -5.0F));
/* 45 */     body.addOrReplaceChild(PartNames.tentacle(2), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, deformation), PartPose.offset(6.25F, 7.0F, -5.0F));
/* 46 */     body.addOrReplaceChild(PartNames.tentacle(3), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, deformation), PartPose.offset(-6.25F, 7.0F, 0.0F));
/* 47 */     body.addOrReplaceChild(PartNames.tentacle(4), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, deformation), PartPose.offset(-1.25F, 7.0F, 0.0F));
/* 48 */     body.addOrReplaceChild(PartNames.tentacle(5), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, deformation), PartPose.offset(3.75F, 7.0F, 0.0F));
/* 49 */     body.addOrReplaceChild(PartNames.tentacle(6), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, deformation), PartPose.offset(-3.75F, 7.0F, 5.0F));
/* 50 */     body.addOrReplaceChild(PartNames.tentacle(7), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, deformation), PartPose.offset(1.25F, 7.0F, 5.0F));
/* 51 */     body.addOrReplaceChild(PartNames.tentacle(8), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, deformation), PartPose.offset(6.25F, 7.0F, 5.0F));
/*    */     
/* 53 */     return LayerDefinition.create(mesh, 64, 64).apply(MeshTransformer.scaling(4.0F));
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(HappyGhastRenderState state) {
/* 58 */     super.setupAnim(state);
/* 59 */     if (!state.bodyItem.isEmpty()) {
/* 60 */       this.body.xScale = 0.9375F;
/* 61 */       this.body.yScale = 0.9375F;
/* 62 */       this.body.zScale = 0.9375F;
/*    */     } 
/* 64 */     GhastModel.animateTentacles((EntityRenderState)state, this.tentacles);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/ghast/HappyGhastModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */