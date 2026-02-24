/*    */ package net.minecraft.client.model.animal.equine;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.EquineRenderState;
/*    */ 
/*    */ 
/*    */ public class EquineSaddleModel
/*    */   extends AbstractEquineModel<EquineRenderState>
/*    */ {
/*    */   private static final String SADDLE = "saddle";
/*    */   private static final String LEFT_SADDLE_MOUTH = "left_saddle_mouth";
/*    */   private static final String LEFT_SADDLE_LINE = "left_saddle_line";
/*    */   private static final String RIGHT_SADDLE_MOUTH = "right_saddle_mouth";
/*    */   private static final String RIGHT_SADDLE_LINE = "right_saddle_line";
/*    */   private static final String HEAD_SADDLE = "head_saddle";
/*    */   private static final String MOUTH_SADDLE_WRAP = "mouth_saddle_wrap";
/*    */   private final ModelPart[] ridingParts;
/*    */   
/*    */   public EquineSaddleModel(ModelPart root) {
/* 27 */     super(root);
/* 28 */     ModelPart leftSaddleLine = this.headParts.getChild("left_saddle_line");
/* 29 */     ModelPart rightSaddleLine = this.headParts.getChild("right_saddle_line");
/* 30 */     this.ridingParts = new ModelPart[] { leftSaddleLine, rightSaddleLine };
/*    */   }
/*    */   
/*    */   public static LayerDefinition createSaddleLayer(boolean baby) {
/* 34 */     return createFullScaleSaddleLayer(baby).apply(baby ? BABY_TRANSFORMER : MeshTransformer.IDENTITY);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createFullScaleSaddleLayer(boolean baby) {
/* 38 */     MeshDefinition mesh = baby ? createFullScaleBabyMesh(CubeDeformation.NONE) : createBodyMesh(CubeDeformation.NONE);
/* 39 */     PartDefinition root = mesh.getRoot();
/* 40 */     PartDefinition body = root.getChild("body");
/*    */     
/* 42 */     PartDefinition headParts = root.getChild("head_parts");
/*    */     
/* 44 */     body.addOrReplaceChild("saddle", 
/* 45 */         CubeListBuilder.create()
/* 46 */         .texOffs(26, 0).addBox(-5.0F, -8.0F, -9.0F, 10.0F, 9.0F, 9.0F, new CubeDeformation(0.5F)), PartPose.ZERO);
/*    */ 
/*    */ 
/*    */     
/* 50 */     headParts.addOrReplaceChild("left_saddle_mouth", 
/* 51 */         CubeListBuilder.create()
/* 52 */         .texOffs(29, 5).addBox(2.0F, -9.0F, -6.0F, 1.0F, 2.0F, 2.0F), PartPose.ZERO);
/*    */ 
/*    */     
/* 55 */     headParts.addOrReplaceChild("right_saddle_mouth", 
/* 56 */         CubeListBuilder.create()
/* 57 */         .texOffs(29, 5).addBox(-3.0F, -9.0F, -6.0F, 1.0F, 2.0F, 2.0F), PartPose.ZERO);
/*    */ 
/*    */     
/* 60 */     headParts.addOrReplaceChild("left_saddle_line", 
/* 61 */         CubeListBuilder.create()
/* 62 */         .texOffs(32, 2).addBox(3.1F, -6.0F, -8.0F, 0.0F, 3.0F, 16.0F), 
/* 63 */         PartPose.rotation(-0.5235988F, 0.0F, 0.0F));
/*    */     
/* 65 */     headParts.addOrReplaceChild("right_saddle_line", 
/* 66 */         CubeListBuilder.create()
/* 67 */         .texOffs(32, 2).addBox(-3.1F, -6.0F, -8.0F, 0.0F, 3.0F, 16.0F), 
/* 68 */         PartPose.rotation(-0.5235988F, 0.0F, 0.0F));
/*    */     
/* 70 */     headParts.addOrReplaceChild("head_saddle", 
/* 71 */         CubeListBuilder.create()
/* 72 */         .texOffs(1, 1).addBox(-3.0F, -11.0F, -1.9F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.22F)), PartPose.ZERO);
/*    */ 
/*    */     
/* 75 */     headParts.addOrReplaceChild("mouth_saddle_wrap", 
/* 76 */         CubeListBuilder.create()
/* 77 */         .texOffs(19, 0).addBox(-2.0F, -11.0F, -4.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.ZERO);
/*    */ 
/*    */ 
/*    */     
/* 81 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(EquineRenderState state) {
/* 86 */     super.setupAnim(state);
/* 87 */     for (ModelPart part : this.ridingParts)
/* 88 */       part.visible = state.isRidden; 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/equine/EquineSaddleModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */