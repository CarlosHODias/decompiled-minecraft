/*    */ package net.minecraft.client.model.effects;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.EvokerFangsRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class EvokerFangsModel
/*    */   extends EntityModel<EvokerFangsRenderState> {
/*    */   private static final String BASE = "base";
/*    */   private static final String UPPER_JAW = "upper_jaw";
/*    */   private static final String LOWER_JAW = "lower_jaw";
/*    */   private final ModelPart base;
/*    */   private final ModelPart upperJaw;
/*    */   private final ModelPart lowerJaw;
/*    */   
/*    */   public EvokerFangsModel(ModelPart root) {
/* 23 */     super(root);
/* 24 */     this.base = root.getChild("base");
/* 25 */     this.upperJaw = this.base.getChild("upper_jaw");
/* 26 */     this.lowerJaw = this.base.getChild("lower_jaw");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 30 */     MeshDefinition mesh = new MeshDefinition();
/* 31 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 33 */     PartDefinition base = root.addOrReplaceChild("base", 
/* 34 */         CubeListBuilder.create()
/* 35 */         .texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 10.0F, 12.0F, 10.0F), 
/* 36 */         PartPose.offset(-5.0F, 24.0F, -5.0F));
/*    */     
/* 38 */     CubeListBuilder jaw = CubeListBuilder.create()
/* 39 */       .texOffs(40, 0).addBox(0.0F, 0.0F, 0.0F, 4.0F, 14.0F, 8.0F);
/* 40 */     base.addOrReplaceChild("upper_jaw", jaw, PartPose.offsetAndRotation(6.5F, 0.0F, 1.0F, 0.0F, 0.0F, 2.042035F));
/* 41 */     base.addOrReplaceChild("lower_jaw", jaw, PartPose.offsetAndRotation(3.5F, 0.0F, 9.0F, 0.0F, 3.1415927F, 4.2411504F));
/*    */     
/* 43 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(EvokerFangsRenderState state) {
/* 48 */     super.setupAnim(state);
/*    */     
/* 50 */     float biteProgress = state.biteProgress;
/* 51 */     float biteAmount = Math.min(biteProgress * 2.0F, 1.0F);
/* 52 */     biteAmount = 1.0F - biteAmount * biteAmount * biteAmount;
/* 53 */     this.upperJaw.zRot = 3.1415927F - biteAmount * 0.35F * 3.1415927F;
/* 54 */     this.lowerJaw.zRot = 3.1415927F + biteAmount * 0.35F * 3.1415927F;
/*    */     
/* 56 */     this.base.y -= (biteProgress + Mth.sin((biteProgress * 2.7F))) * 7.2F;
/*    */     
/* 58 */     float preScale = 1.0F;
/* 59 */     if (biteProgress > 0.9F) {
/* 60 */       preScale *= (1.0F - biteProgress) / 0.1F;
/*    */     }
/* 62 */     this.root.y = 24.0F - 20.0F * preScale;
/* 63 */     this.root.xScale = preScale;
/* 64 */     this.root.yScale = preScale;
/* 65 */     this.root.zScale = preScale;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/effects/EvokerFangsModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */