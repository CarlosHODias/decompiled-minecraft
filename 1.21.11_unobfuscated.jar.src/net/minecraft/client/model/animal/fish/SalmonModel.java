/*    */ package net.minecraft.client.model.animal.fish;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.SalmonRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class SalmonModel
/*    */   extends EntityModel<SalmonRenderState> {
/* 16 */   public static final MeshTransformer SMALL_TRANSFORMER = MeshTransformer.scaling(0.5F);
/* 17 */   public static final MeshTransformer LARGE_TRANSFORMER = MeshTransformer.scaling(1.5F);
/*    */   
/*    */   private static final String BODY_FRONT = "body_front";
/*    */   
/*    */   private static final String BODY_BACK = "body_back";
/*    */   private static final float Z_OFFSET = -7.2F;
/*    */   private final ModelPart bodyBack;
/*    */   
/*    */   public SalmonModel(ModelPart root) {
/* 26 */     super(root);
/* 27 */     this.bodyBack = root.getChild("body_back");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 31 */     MeshDefinition mesh = new MeshDefinition();
/* 32 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 34 */     int yo = 20;
/* 35 */     PartDefinition bodyFront = root.addOrReplaceChild("body_front", 
/* 36 */         CubeListBuilder.create()
/* 37 */         .texOffs(0, 0).addBox(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 8.0F), 
/* 38 */         PartPose.offset(0.0F, 20.0F, -7.2F));
/*    */     
/* 40 */     PartDefinition bodyBack = root.addOrReplaceChild("body_back", 
/* 41 */         CubeListBuilder.create()
/* 42 */         .texOffs(0, 13).addBox(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 8.0F), 
/* 43 */         PartPose.offset(0.0F, 20.0F, 0.8000002F));
/*    */     
/* 45 */     root.addOrReplaceChild("head", 
/* 46 */         CubeListBuilder.create()
/* 47 */         .texOffs(22, 0).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 4.0F, 3.0F), 
/* 48 */         PartPose.offset(0.0F, 20.0F, -7.2F));
/*    */     
/* 50 */     bodyBack.addOrReplaceChild("back_fin", 
/* 51 */         CubeListBuilder.create()
/* 52 */         .texOffs(20, 10).addBox(0.0F, -2.5F, 0.0F, 0.0F, 5.0F, 6.0F), 
/* 53 */         PartPose.offset(0.0F, 0.0F, 8.0F));
/*    */     
/* 55 */     bodyFront.addOrReplaceChild("top_front_fin", 
/* 56 */         CubeListBuilder.create()
/* 57 */         .texOffs(2, 1).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 3.0F), 
/* 58 */         PartPose.offset(0.0F, -4.5F, 5.0F));
/*    */     
/* 60 */     bodyBack.addOrReplaceChild("top_back_fin", 
/* 61 */         CubeListBuilder.create()
/* 62 */         .texOffs(0, 2).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 4.0F), 
/* 63 */         PartPose.offset(0.0F, -4.5F, -1.0F));
/*    */     
/* 65 */     root.addOrReplaceChild("right_fin", 
/* 66 */         CubeListBuilder.create()
/* 67 */         .texOffs(-4, 0).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F), 
/* 68 */         PartPose.offsetAndRotation(-1.5F, 21.5F, -7.2F, 0.0F, 0.0F, -0.7853982F));
/*    */     
/* 70 */     root.addOrReplaceChild("left_fin", 
/* 71 */         CubeListBuilder.create()
/* 72 */         .texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F), 
/* 73 */         PartPose.offsetAndRotation(1.5F, 21.5F, -7.2F, 0.0F, 0.0F, 0.7853982F));
/*    */ 
/*    */     
/* 76 */     return LayerDefinition.create(mesh, 32, 32);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(SalmonRenderState state) {
/* 81 */     super.setupAnim(state);
/* 82 */     float amplitudeMultiplier = 1.0F;
/* 83 */     float angleMultiplier = 1.0F;
/* 84 */     if (!state.isInWater) {
/* 85 */       amplitudeMultiplier = 1.3F;
/* 86 */       angleMultiplier = 1.7F;
/*    */     } 
/* 88 */     this.bodyBack.yRot = -amplitudeMultiplier * 0.25F * Mth.sin((angleMultiplier * 0.6F * state.ageInTicks));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/fish/SalmonModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */