/*     */ package net.minecraft.client.model.monster.skeleton;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import net.minecraft.client.model.AnimationUtils;
/*     */ import net.minecraft.client.model.HumanoidModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ 
/*     */ public class SkeletonModel<S extends SkeletonRenderState> extends HumanoidModel<S> {
/*     */   public SkeletonModel(ModelPart root) {
/*  20 */     super(root);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  24 */     MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
/*  25 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  27 */     createDefaultSkeletonMesh(root);
/*  28 */     return LayerDefinition.create(mesh, 64, 32);
/*     */   }
/*     */   
/*     */   protected static void createDefaultSkeletonMesh(PartDefinition root) {
/*  32 */     root.addOrReplaceChild("right_arm", 
/*  33 */         CubeListBuilder.create()
/*  34 */         .texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), 
/*  35 */         PartPose.offset(-5.0F, 2.0F, 0.0F));
/*     */     
/*  37 */     root.addOrReplaceChild("left_arm", 
/*  38 */         CubeListBuilder.create()
/*  39 */         .texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), 
/*  40 */         PartPose.offset(5.0F, 2.0F, 0.0F));
/*     */     
/*  42 */     root.addOrReplaceChild("right_leg", 
/*  43 */         CubeListBuilder.create()
/*  44 */         .texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), 
/*  45 */         PartPose.offset(-2.0F, 12.0F, 0.0F));
/*     */     
/*  47 */     root.addOrReplaceChild("left_leg", 
/*  48 */         CubeListBuilder.create()
/*  49 */         .texOffs(0, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), 
/*  50 */         PartPose.offset(2.0F, 12.0F, 0.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   public static LayerDefinition createSingleModelDualBodyLayer() {
/*  55 */     MeshDefinition meshdefinition = new MeshDefinition();
/*  56 */     PartDefinition root = meshdefinition.getRoot();
/*  57 */     root.addOrReplaceChild("body", CubeListBuilder.create()
/*  58 */         .texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F)
/*  59 */         .texOffs(28, 0).addBox(-4.0F, 10.0F, -2.0F, 8.0F, 1.0F, 4.0F)
/*  60 */         .texOffs(16, 48).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.025F)), 
/*  61 */         PartPose.offset(0.0F, 0.0F, 0.0F));
/*     */     
/*  63 */     root.addOrReplaceChild("head", CubeListBuilder.create()
/*  64 */         .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F)
/*  65 */         .texOffs(0, 32).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F)), 
/*  66 */         PartPose.offset(0.0F, 0.0F, 0.0F))
/*  67 */       .addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
/*  68 */     root.addOrReplaceChild("right_arm", CubeListBuilder.create()
/*  69 */         .texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F)
/*  70 */         .texOffs(42, 33).addBox(-1.55F, -2.025F, -1.5F, 3.0F, 12.0F, 3.0F), 
/*  71 */         PartPose.offset(-5.5F, 2.0F, 0.0F));
/*     */     
/*  73 */     root.addOrReplaceChild("left_arm", CubeListBuilder.create()
/*  74 */         .texOffs(56, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F)
/*  75 */         .texOffs(40, 48).addBox(-1.45F, -2.025F, -1.5F, 3.0F, 12.0F, 3.0F), 
/*  76 */         PartPose.offset(5.5F, 2.0F, 0.0F));
/*     */     
/*  78 */     root.addOrReplaceChild("right_leg", CubeListBuilder.create()
/*  79 */         .texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F)
/*  80 */         .texOffs(0, 49).addBox(-1.5F, -0.0F, -1.5F, 3.0F, 12.0F, 3.0F), 
/*  81 */         PartPose.offset(-2.0F, 12.0F, 0.0F));
/*     */     
/*  83 */     root.addOrReplaceChild("left_leg", CubeListBuilder.create()
/*  84 */         .texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F)
/*  85 */         .texOffs(4, 49).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F), 
/*  86 */         PartPose.offset(2.0F, 12.0F, 0.0F));
/*     */     
/*  88 */     return LayerDefinition.create(meshdefinition, 64, 64);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(S state) {
/*  93 */     super.setupAnim((HumanoidRenderState)state);
/*     */     
/*  95 */     if (((SkeletonRenderState)state).isAggressive && !((SkeletonRenderState)state).isHoldingBow) {
/*  96 */       float attackTime = ((SkeletonRenderState)state).attackTime;
/*  97 */       float attack2 = Mth.sin((attackTime * 3.1415927F));
/*  98 */       float attack = Mth.sin(((1.0F - (1.0F - attackTime) * (1.0F - attackTime)) * 3.1415927F));
/*  99 */       this.rightArm.zRot = 0.0F;
/* 100 */       this.leftArm.zRot = 0.0F;
/* 101 */       this.rightArm.yRot = -(0.1F - attack2 * 0.6F);
/* 102 */       this.leftArm.yRot = 0.1F - attack2 * 0.6F;
/* 103 */       this.rightArm.xRot = -1.5707964F;
/* 104 */       this.leftArm.xRot = -1.5707964F;
/* 105 */       this.rightArm.xRot -= attack2 * 1.2F - attack * 0.4F;
/* 106 */       this.leftArm.xRot -= attack2 * 1.2F - attack * 0.4F;
/*     */       
/* 108 */       AnimationUtils.bobArms(this.rightArm, this.leftArm, ((SkeletonRenderState)state).ageInTicks);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void translateToHand(SkeletonRenderState state, HumanoidArm arm, PoseStack poseStack) {
/* 114 */     root().translateAndRotate(poseStack);
/* 115 */     float offset = (arm == HumanoidArm.RIGHT) ? 1.0F : -1.0F;
/* 116 */     ModelPart part = getArm(arm);
/* 117 */     part.x += offset;
/* 118 */     part.translateAndRotate(poseStack);
/* 119 */     part.x -= offset;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/skeleton/SkeletonModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */