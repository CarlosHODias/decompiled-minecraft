/*    */ package net.minecraft.client.model.monster.endermite;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class EndermiteModel
/*    */   extends EntityModel<EntityRenderState> {
/*    */   private static final int BODY_COUNT = 4;
/* 16 */   private static final int[][] BODY_SIZES = new int[][] { { 4, 3, 2 }, { 6, 4, 5 }, { 3, 3, 1 }, { 1, 2, 1 } };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   private static final int[][] BODY_TEXS = new int[][] { { 0, 0 }, { 0, 5 }, { 0, 14 }, { 0, 18 } };
/*    */ 
/*    */ 
/*    */   
/*    */   private final ModelPart[] bodyParts;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EndermiteModel(ModelPart root) {
/* 33 */     super(root);
/* 34 */     this.bodyParts = new ModelPart[4];
/* 35 */     for (int i = 0; i < 4; i++) {
/* 36 */       this.bodyParts[i] = root.getChild(createSegmentName(i));
/*    */     }
/*    */   }
/*    */   
/*    */   private static String createSegmentName(int i) {
/* 41 */     return "segment" + i;
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 45 */     MeshDefinition mesh = new MeshDefinition();
/* 46 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 48 */     float placement = -3.5F;
/* 49 */     for (int i = 0; i < 4; i++) {
/* 50 */       root.addOrReplaceChild(createSegmentName(i), 
/* 51 */           CubeListBuilder.create()
/* 52 */           .texOffs(BODY_TEXS[i][0], BODY_TEXS[i][1]).addBox(BODY_SIZES[i][0] * -0.5F, 0.0F, BODY_SIZES[i][2] * -0.5F, BODY_SIZES[i][0], BODY_SIZES[i][1], BODY_SIZES[i][2]), 
/* 53 */           PartPose.offset(0.0F, (24 - BODY_SIZES[i][1]), placement));
/*    */       
/* 55 */       if (i < 3) {
/* 56 */         placement += (BODY_SIZES[i][2] + BODY_SIZES[i + 1][2]) * 0.5F;
/*    */       }
/*    */     } 
/*    */     
/* 60 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(EntityRenderState state) {
/* 65 */     super.setupAnim(state);
/* 66 */     for (int i = 0; i < this.bodyParts.length; i++) {
/* 67 */       (this.bodyParts[i]).yRot = Mth.cos((state.ageInTicks * 0.9F + i * 0.15F * 3.1415927F)) * 3.1415927F * 0.01F * (1 + Math.abs(i - 2));
/* 68 */       (this.bodyParts[i]).x = Mth.sin((state.ageInTicks * 0.9F + i * 0.15F * 3.1415927F)) * 3.1415927F * 0.1F * Math.abs(i - 2);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/endermite/EndermiteModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */