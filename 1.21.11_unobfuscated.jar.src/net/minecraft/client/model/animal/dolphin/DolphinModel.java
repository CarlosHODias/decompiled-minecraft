/*    */ package net.minecraft.client.model.animal.dolphin;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.DolphinRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class DolphinModel
/*    */   extends EntityModel<DolphinRenderState> {
/* 16 */   public static final MeshTransformer BABY_TRANSFORMER = MeshTransformer.scaling(0.5F);
/*    */   
/*    */   private final ModelPart body;
/*    */   private final ModelPart tail;
/*    */   private final ModelPart tailFin;
/*    */   
/*    */   public DolphinModel(ModelPart root) {
/* 23 */     super(root);
/* 24 */     this.body = root.getChild("body");
/* 25 */     this.tail = this.body.getChild("tail");
/* 26 */     this.tailFin = this.tail.getChild("tail_fin");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 30 */     MeshDefinition mesh = new MeshDefinition();
/* 31 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 33 */     float offY = 18.0F;
/* 34 */     float offZ = -8.0F;
/*    */     
/* 36 */     PartDefinition body = root.addOrReplaceChild("body", 
/* 37 */         CubeListBuilder.create()
/* 38 */         .texOffs(22, 0).addBox(-4.0F, -7.0F, 0.0F, 8.0F, 7.0F, 13.0F), 
/* 39 */         PartPose.offset(0.0F, 22.0F, -5.0F));
/*    */     
/* 41 */     body.addOrReplaceChild("back_fin", 
/* 42 */         CubeListBuilder.create()
/* 43 */         .texOffs(51, 0).addBox(-0.5F, 0.0F, 8.0F, 1.0F, 4.0F, 5.0F), 
/* 44 */         PartPose.rotation(1.0471976F, 0.0F, 0.0F));
/*    */     
/* 46 */     body.addOrReplaceChild("left_fin", 
/* 47 */         CubeListBuilder.create()
/* 48 */         .texOffs(48, 20).mirror().addBox(-0.5F, -4.0F, 0.0F, 1.0F, 4.0F, 7.0F), 
/* 49 */         PartPose.offsetAndRotation(2.0F, -2.0F, 4.0F, 1.0471976F, 0.0F, 2.0943952F));
/*    */     
/* 51 */     body.addOrReplaceChild("right_fin", 
/* 52 */         CubeListBuilder.create()
/* 53 */         .texOffs(48, 20).addBox(-0.5F, -4.0F, 0.0F, 1.0F, 4.0F, 7.0F), 
/* 54 */         PartPose.offsetAndRotation(-2.0F, -2.0F, 4.0F, 1.0471976F, 0.0F, -2.0943952F));
/*    */     
/* 56 */     PartDefinition tail = body.addOrReplaceChild("tail", 
/* 57 */         CubeListBuilder.create()
/* 58 */         .texOffs(0, 19).addBox(-2.0F, -2.5F, 0.0F, 4.0F, 5.0F, 11.0F), 
/* 59 */         PartPose.offsetAndRotation(0.0F, -2.5F, 11.0F, -0.10471976F, 0.0F, 0.0F));
/*    */     
/* 61 */     tail.addOrReplaceChild("tail_fin", 
/* 62 */         CubeListBuilder.create()
/* 63 */         .texOffs(19, 20).addBox(-5.0F, -0.5F, 0.0F, 10.0F, 1.0F, 6.0F), 
/* 64 */         PartPose.offset(0.0F, 0.0F, 9.0F));
/*    */     
/* 66 */     PartDefinition head = body.addOrReplaceChild("head", 
/* 67 */         CubeListBuilder.create()
/* 68 */         .texOffs(0, 0).addBox(-4.0F, -3.0F, -3.0F, 8.0F, 7.0F, 6.0F), 
/* 69 */         PartPose.offset(0.0F, -4.0F, -3.0F));
/*    */     
/* 71 */     head.addOrReplaceChild("nose", 
/* 72 */         CubeListBuilder.create()
/* 73 */         .texOffs(0, 13).addBox(-1.0F, 2.0F, -7.0F, 2.0F, 2.0F, 4.0F), PartPose.ZERO);
/*    */ 
/*    */ 
/*    */     
/* 77 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(DolphinRenderState state) {
/* 82 */     super.setupAnim(state);
/*    */     
/* 84 */     this.body.xRot = state.xRot * 0.017453292F;
/* 85 */     this.body.yRot = state.yRot * 0.017453292F;
/*    */     
/* 87 */     if (state.isMoving) {
/* 88 */       this.body.xRot += -0.05F - 0.05F * Mth.cos((state.ageInTicks * 0.3F));
/* 89 */       this.tail.xRot = -0.1F * Mth.cos((state.ageInTicks * 0.3F));
/* 90 */       this.tailFin.xRot = -0.2F * Mth.cos((state.ageInTicks * 0.3F));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/dolphin/DolphinModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */