/*    */ package net.minecraft.client.model.animal.frog;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class TadpoleModel
/*    */   extends EntityModel<LivingEntityRenderState> {
/*    */   private final ModelPart tail;
/*    */   
/*    */   public TadpoleModel(ModelPart root) {
/* 19 */     super(root, RenderTypes::entityCutoutNoCull);
/* 20 */     this.tail = root.getChild("tail");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 24 */     MeshDefinition mesh = new MeshDefinition();
/* 25 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 27 */     float xo = 0.0F;
/* 28 */     float yo = 22.0F;
/* 29 */     float zo = -3.0F;
/* 30 */     root.addOrReplaceChild("body", 
/* 31 */         CubeListBuilder.create()
/* 32 */         .texOffs(0, 0).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 3.0F), 
/* 33 */         PartPose.offset(0.0F, 22.0F, -3.0F));
/*    */     
/* 35 */     root.addOrReplaceChild("tail", 
/* 36 */         CubeListBuilder.create()
/* 37 */         .texOffs(0, 0).addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 7.0F), 
/* 38 */         PartPose.offset(0.0F, 22.0F, 0.0F));
/*    */ 
/*    */     
/* 41 */     return LayerDefinition.create(mesh, 16, 16);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(LivingEntityRenderState state) {
/* 46 */     super.setupAnim(state);
/* 47 */     float amplitudeMultiplier = state.isInWater ? 1.0F : 1.5F;
/* 48 */     this.tail.yRot = -amplitudeMultiplier * 0.25F * Mth.sin((0.3F * state.ageInTicks));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/frog/TadpoleModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */