/*    */ package net.minecraft.client.model.animal.equine;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.DonkeyRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EquineRenderState;
/*    */ 
/*    */ public class DonkeyModel extends AbstractEquineModel<DonkeyRenderState> {
/*    */   public static final float DONKEY_SCALE = 0.87F;
/*    */   public static final float MULE_SCALE = 0.92F;
/*    */   
/*    */   static {
/* 18 */     DONKEY_TRANSFORMER = (mesh -> {
/*    */         modifyMesh(mesh.getRoot());
/*    */         return mesh;
/*    */       });
/*    */   }
/*    */   
/*    */   private static final MeshTransformer DONKEY_TRANSFORMER;
/*    */   
/*    */   public DonkeyModel(ModelPart root) {
/* 27 */     super(root);
/* 28 */     this.leftChest = this.body.getChild("left_chest");
/* 29 */     this.rightChest = this.body.getChild("right_chest");
/*    */   }
/*    */   private final ModelPart leftChest; private final ModelPart rightChest;
/*    */   public static LayerDefinition createBodyLayer(float scale) {
/* 33 */     return LayerDefinition.create(AbstractEquineModel.createBodyMesh(CubeDeformation.NONE), 64, 64)
/* 34 */       .apply(DONKEY_TRANSFORMER)
/* 35 */       .apply(MeshTransformer.scaling(scale));
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBabyLayer(float scale) {
/* 39 */     return LayerDefinition.create(AbstractEquineModel.createFullScaleBabyMesh(CubeDeformation.NONE), 64, 64)
/* 40 */       .apply(DONKEY_TRANSFORMER)
/* 41 */       .apply(BABY_TRANSFORMER)
/* 42 */       .apply(MeshTransformer.scaling(scale));
/*    */   }
/*    */   
/*    */   public static LayerDefinition createSaddleLayer(float scale, boolean baby) {
/* 46 */     return EquineSaddleModel.createFullScaleSaddleLayer(baby)
/* 47 */       .apply(DONKEY_TRANSFORMER)
/* 48 */       .apply(baby ? AbstractEquineModel.BABY_TRANSFORMER : MeshTransformer.IDENTITY)
/* 49 */       .apply(MeshTransformer.scaling(scale));
/*    */   }
/*    */   
/*    */   private static void modifyMesh(PartDefinition root) {
/* 53 */     PartDefinition body = root.getChild("body");
/* 54 */     CubeListBuilder chest = CubeListBuilder.create()
/* 55 */       .texOffs(26, 21).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 3.0F);
/* 56 */     body.addOrReplaceChild("left_chest", chest, PartPose.offsetAndRotation(6.0F, -8.0F, 0.0F, 0.0F, -1.5707964F, 0.0F));
/* 57 */     body.addOrReplaceChild("right_chest", chest, PartPose.offsetAndRotation(-6.0F, -8.0F, 0.0F, 0.0F, 1.5707964F, 0.0F));
/*    */     
/* 59 */     PartDefinition head = root.getChild("head_parts").getChild("head");
/* 60 */     CubeListBuilder ear = CubeListBuilder.create()
/* 61 */       .texOffs(0, 12).addBox(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 1.0F);
/* 62 */     head.addOrReplaceChild("left_ear", ear, PartPose.offsetAndRotation(1.25F, -10.0F, 4.0F, 0.2617994F, 0.0F, 0.2617994F));
/* 63 */     head.addOrReplaceChild("right_ear", ear, PartPose.offsetAndRotation(-1.25F, -10.0F, 4.0F, 0.2617994F, 0.0F, -0.2617994F));
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(DonkeyRenderState state) {
/* 68 */     super.setupAnim(state);
/* 69 */     this.leftChest.visible = state.hasChest;
/* 70 */     this.rightChest.visible = state.hasChest;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/equine/DonkeyModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */