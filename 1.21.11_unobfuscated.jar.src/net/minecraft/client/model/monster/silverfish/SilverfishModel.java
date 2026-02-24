/*     */ package net.minecraft.client.model.monster.silverfish;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class SilverfishModel
/*     */   extends EntityModel<EntityRenderState>
/*     */ {
/*     */   private static final int BODY_COUNT = 7;
/*  18 */   private final ModelPart[] bodyParts = new ModelPart[7];
/*  19 */   private final ModelPart[] bodyLayers = new ModelPart[3];
/*     */   
/*  21 */   private static final int[][] BODY_SIZES = new int[][] { { 3, 2, 2 }, { 4, 3, 2 }, { 6, 4, 3 }, { 3, 3, 3 }, { 2, 2, 3 }, { 2, 1, 2 }, { 1, 1, 2 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  30 */   private static final int[][] BODY_TEXS = new int[][] { { 0, 0 }, { 0, 4 }, { 0, 9 }, { 0, 16 }, { 0, 22 }, { 11, 0 }, { 13, 4 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SilverfishModel(ModelPart root) {
/*  41 */     super(root);
/*  42 */     Arrays.setAll(this.bodyParts, i -> root.getChild(getSegmentName(i)));
/*  43 */     Arrays.setAll(this.bodyLayers, i -> root.getChild(getLayerName(i)));
/*     */   }
/*     */   
/*     */   private static String getLayerName(int i) {
/*  47 */     return "layer" + i;
/*     */   }
/*     */   
/*     */   private static String getSegmentName(int i) {
/*  51 */     return "segment" + i;
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  55 */     MeshDefinition mesh = new MeshDefinition();
/*  56 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  58 */     float[] zPlacement = new float[7];
/*  59 */     float placement = -3.5F;
/*  60 */     for (int i = 0; i < 7; i++) {
/*  61 */       root.addOrReplaceChild(getSegmentName(i), 
/*  62 */           CubeListBuilder.create()
/*  63 */           .texOffs(BODY_TEXS[i][0], BODY_TEXS[i][1]).addBox(BODY_SIZES[i][0] * -0.5F, 0.0F, BODY_SIZES[i][2] * -0.5F, BODY_SIZES[i][0], BODY_SIZES[i][1], BODY_SIZES[i][2]), 
/*  64 */           PartPose.offset(0.0F, (24 - BODY_SIZES[i][1]), placement));
/*     */       
/*  66 */       zPlacement[i] = placement;
/*  67 */       if (i < 6) {
/*  68 */         placement += (BODY_SIZES[i][2] + BODY_SIZES[i + 1][2]) * 0.5F;
/*     */       }
/*     */     } 
/*     */     
/*  72 */     root.addOrReplaceChild(getLayerName(0), 
/*  73 */         CubeListBuilder.create()
/*  74 */         .texOffs(20, 0).addBox(-5.0F, 0.0F, BODY_SIZES[2][2] * -0.5F, 10.0F, 8.0F, BODY_SIZES[2][2]), 
/*  75 */         PartPose.offset(0.0F, 16.0F, zPlacement[2]));
/*     */     
/*  77 */     root.addOrReplaceChild(getLayerName(1), 
/*  78 */         CubeListBuilder.create()
/*  79 */         .texOffs(20, 11).addBox(-3.0F, 0.0F, BODY_SIZES[4][2] * -0.5F, 6.0F, 4.0F, BODY_SIZES[4][2]), 
/*  80 */         PartPose.offset(0.0F, 20.0F, zPlacement[4]));
/*     */     
/*  82 */     root.addOrReplaceChild(getLayerName(2), 
/*  83 */         CubeListBuilder.create()
/*  84 */         .texOffs(20, 18).addBox(-3.0F, 0.0F, BODY_SIZES[4][2] * -0.5F, 6.0F, 5.0F, BODY_SIZES[1][2]), 
/*  85 */         PartPose.offset(0.0F, 19.0F, zPlacement[1]));
/*     */ 
/*     */     
/*  88 */     return LayerDefinition.create(mesh, 64, 32);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(EntityRenderState state) {
/*  93 */     super.setupAnim(state);
/*     */     
/*  95 */     for (int i = 0; i < this.bodyParts.length; i++) {
/*  96 */       (this.bodyParts[i]).yRot = Mth.cos((state.ageInTicks * 0.9F + i * 0.15F * 3.1415927F)) * 3.1415927F * 0.05F * (1 + Math.abs(i - 2));
/*  97 */       (this.bodyParts[i]).x = Mth.sin((state.ageInTicks * 0.9F + i * 0.15F * 3.1415927F)) * 3.1415927F * 0.2F * Math.abs(i - 2);
/*     */     } 
/*     */     
/* 100 */     (this.bodyLayers[0]).yRot = (this.bodyParts[2]).yRot;
/* 101 */     (this.bodyLayers[1]).yRot = (this.bodyParts[4]).yRot;
/* 102 */     (this.bodyLayers[1]).x = (this.bodyParts[4]).x;
/* 103 */     (this.bodyLayers[2]).yRot = (this.bodyParts[1]).yRot;
/* 104 */     (this.bodyLayers[2]).x = (this.bodyParts[1]).x;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/silverfish/SilverfishModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */