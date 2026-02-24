/*    */ package net.minecraft.client.model.animal.fish;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class CodModel
/*    */   extends EntityModel<LivingEntityRenderState> {
/*    */   private final ModelPart tailFin;
/*    */   
/*    */   public CodModel(ModelPart root) {
/* 18 */     super(root);
/* 19 */     this.tailFin = root.getChild("tail_fin");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 23 */     MeshDefinition mesh = new MeshDefinition();
/* 24 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 26 */     int yo = 22;
/* 27 */     root.addOrReplaceChild("body", 
/* 28 */         CubeListBuilder.create()
/* 29 */         .texOffs(0, 0).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 4.0F, 7.0F), 
/* 30 */         PartPose.offset(0.0F, 22.0F, 0.0F));
/*    */     
/* 32 */     root.addOrReplaceChild("head", 
/* 33 */         CubeListBuilder.create()
/* 34 */         .texOffs(11, 0).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 4.0F, 3.0F), 
/* 35 */         PartPose.offset(0.0F, 22.0F, 0.0F));
/*    */     
/* 37 */     root.addOrReplaceChild("nose", 
/* 38 */         CubeListBuilder.create()
/* 39 */         .texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 1.0F), 
/* 40 */         PartPose.offset(0.0F, 22.0F, -3.0F));
/*    */     
/* 42 */     root.addOrReplaceChild("right_fin", 
/* 43 */         CubeListBuilder.create()
/* 44 */         .texOffs(22, 1).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F), 
/* 45 */         PartPose.offsetAndRotation(-1.0F, 23.0F, 0.0F, 0.0F, 0.0F, -0.7853982F));
/*    */     
/* 47 */     root.addOrReplaceChild("left_fin", 
/* 48 */         CubeListBuilder.create()
/* 49 */         .texOffs(22, 4).addBox(0.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F), 
/* 50 */         PartPose.offsetAndRotation(1.0F, 23.0F, 0.0F, 0.0F, 0.0F, 0.7853982F));
/*    */     
/* 52 */     root.addOrReplaceChild("tail_fin", 
/* 53 */         CubeListBuilder.create()
/* 54 */         .texOffs(22, 3).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 4.0F), 
/* 55 */         PartPose.offset(0.0F, 22.0F, 7.0F));
/*    */     
/* 57 */     root.addOrReplaceChild("top_fin", 
/* 58 */         CubeListBuilder.create()
/* 59 */         .texOffs(20, -6).addBox(0.0F, -1.0F, -1.0F, 0.0F, 1.0F, 6.0F), 
/* 60 */         PartPose.offset(0.0F, 20.0F, 0.0F));
/*    */ 
/*    */     
/* 63 */     return LayerDefinition.create(mesh, 32, 32);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(LivingEntityRenderState state) {
/* 68 */     super.setupAnim(state);
/*    */     
/* 70 */     float amplitudeMultiplier = state.isInWater ? 1.0F : 1.5F;
/* 71 */     this.tailFin.yRot = -amplitudeMultiplier * 0.45F * Mth.sin((0.6F * state.ageInTicks));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/fish/CodModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */