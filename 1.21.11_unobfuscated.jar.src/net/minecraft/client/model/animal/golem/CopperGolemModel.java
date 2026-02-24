/*     */ package net.minecraft.client.model.animal.golem;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.animation.KeyframeAnimation;
/*     */ import net.minecraft.client.animation.definitions.CopperGolemAnimation;
/*     */ import net.minecraft.client.model.ArmedModel;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.HeadedModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.CopperGolemRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.entity.animal.golem.CopperGolemState;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class CopperGolemModel
/*     */   extends EntityModel<CopperGolemRenderState>
/*     */   implements ArmedModel<CopperGolemRenderState>, HeadedModel {
/*     */   private static final float MAX_WALK_ANIMATION_SPEED = 2.0F;
/*     */   private static final float WALK_ANIMATION_SCALE_FACTOR = 2.5F;
/*     */   private static final float Z_FIGHT_MITIGATION = 0.015F;
/*     */   private final ModelPart head;
/*     */   private final ModelPart body;
/*     */   private final ModelPart rightArm;
/*     */   private final ModelPart leftArm;
/*     */   private final KeyframeAnimation walkAnimation;
/*     */   private final KeyframeAnimation walkWithItemAnimation;
/*     */   private final KeyframeAnimation idleAnimation;
/*     */   private final KeyframeAnimation interactionGetItem;
/*     */   private final KeyframeAnimation interactionGetNoItem;
/*     */   private final KeyframeAnimation interactionDropItem;
/*     */   private final KeyframeAnimation interactionDropNoItem;
/*     */   
/*     */   public CopperGolemModel(ModelPart root) {
/*  43 */     super(root);
/*  44 */     this.body = root.getChild("body");
/*  45 */     this.head = this.body.getChild("head");
/*  46 */     this.rightArm = this.body.getChild("right_arm");
/*  47 */     this.leftArm = this.body.getChild("left_arm");
/*     */     
/*  49 */     this.walkAnimation = CopperGolemAnimation.COPPER_GOLEM_WALK.bake(root);
/*  50 */     this.walkWithItemAnimation = CopperGolemAnimation.COPPER_GOLEM_WALK_ITEM.bake(root);
/*  51 */     this.idleAnimation = CopperGolemAnimation.COPPER_GOLEM_IDLE.bake(root);
/*  52 */     this.interactionGetItem = CopperGolemAnimation.COPPER_GOLEM_CHEST_INTERACTION_NOITEM_GET.bake(root);
/*  53 */     this.interactionGetNoItem = CopperGolemAnimation.COPPER_GOLEM_CHEST_INTERACTION_NOITEM_NOGET.bake(root);
/*  54 */     this.interactionDropItem = CopperGolemAnimation.COPPER_GOLEM_CHEST_INTERACTION_ITEM_DROP.bake(root);
/*  55 */     this.interactionDropNoItem = CopperGolemAnimation.COPPER_GOLEM_CHEST_INTERACTION_ITEM_NODROP.bake(root);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  59 */     MeshDefinition meshDefinition = new MeshDefinition().transformed(p -> p.translated(0.0F, 24.0F, 0.0F));
/*  60 */     PartDefinition root = meshDefinition.getRoot();
/*     */     
/*  62 */     PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 15).addBox(-4.0F, -6.0F, -3.0F, 8.0F, 6.0F, 6.0F, CubeDeformation.NONE), PartPose.offset(0.0F, -5.0F, 0.0F));
/*  63 */     body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.0F, -5.0F, 8.0F, 5.0F, 10.0F, new CubeDeformation(0.015F))
/*  64 */         .texOffs(56, 0).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 3.0F, 2.0F, CubeDeformation.NONE)
/*  65 */         .texOffs(37, 8).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.015F))
/*  66 */         .texOffs(37, 0).addBox(-2.0F, -13.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.015F)), PartPose.offset(0.0F, -6.0F, 0.0F));
/*  67 */     body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(36, 16).addBox(-3.0F, -1.0F, -2.0F, 3.0F, 10.0F, 4.0F, CubeDeformation.NONE), PartPose.offset(-4.0F, -6.0F, 0.0F));
/*  68 */     body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(50, 16).addBox(0.0F, -1.0F, -2.0F, 3.0F, 10.0F, 4.0F, CubeDeformation.NONE), PartPose.offset(4.0F, -6.0F, 0.0F));
/*  69 */     root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 27).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, CubeDeformation.NONE), PartPose.offset(0.0F, -5.0F, 0.0F));
/*  70 */     root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 27).addBox(0.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, CubeDeformation.NONE), PartPose.offset(0.0F, -5.0F, 0.0F));
/*  71 */     return LayerDefinition.create(meshDefinition, 64, 64);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createRunningPoseBodyLayer() {
/*  75 */     MeshDefinition meshDefinition = new MeshDefinition().transformed(p -> p.translated(0.0F, 0.0F, 0.0F));
/*  76 */     PartDefinition root = meshDefinition.getRoot();
/*     */     
/*  78 */     PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(-1.064F, -5.0F, 0.0F));
/*  79 */     body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 15).addBox(-4.02F, -6.116F, -3.5F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1F, 0.1F, 0.7F, 0.1204F, -0.0064F, -0.0779F));
/*  80 */     body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.1F, -5.0F, 8.0F, 5.0F, 10.0F, new CubeDeformation(0.0F))
/*  81 */         .texOffs(56, 0).addBox(-1.02F, -2.1F, -6.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
/*  82 */         .texOffs(37, 8).addBox(-1.02F, -9.1F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.015F))
/*  83 */         .texOffs(37, 0).addBox(-2.0F, -13.1F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.015F)), PartPose.offset(0.7F, -5.6F, -1.8F));
/*  84 */     PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -6.0F, 0.0F));
/*  85 */     right_arm.addOrReplaceChild("right_arm_r1", CubeListBuilder.create().texOffs(36, 16).addBox(-3.052F, -1.11F, -2.036F, 3.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7F, -0.248F, -1.62F, 1.0036F, 0.0F, 0.0F));
/*  86 */     PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -6.0F, 0.0F));
/*  87 */     left_arm.addOrReplaceChild("left_arm_r1", CubeListBuilder.create().texOffs(50, 16).addBox(0.032F, -1.1F, -2.0F, 3.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.732F, 0.0F, 0.0F, -0.8715F, -0.0535F, -0.0449F));
/*  88 */     PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-3.064F, -5.0F, 0.0F));
/*  89 */     right_leg.addOrReplaceChild("right_leg_r1", CubeListBuilder.create().texOffs(0, 27).addBox(-1.856F, -0.1F, -1.09F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.048F, 0.0F, -0.9F, -0.8727F, 0.0F, 0.0F));
/*  90 */     PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(0.936F, -5.0F, 0.0F));
/*  91 */     left_leg.addOrReplaceChild("left_leg_r1", CubeListBuilder.create().texOffs(16, 27).addBox(-2.088F, -0.1F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));
/*  92 */     return LayerDefinition.create(meshDefinition, 64, 64);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createSittingPoseBodyLayer() {
/*  96 */     MeshDefinition meshDefinition = new MeshDefinition().transformed(p -> p.translated(0.0F, 0.0F, 0.0F));
/*  97 */     PartDefinition root = meshDefinition.getRoot();
/*     */     
/*  99 */     PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
/* 100 */         .texOffs(3, 19).addBox(-3.0F, -4.0F, -4.525F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
/* 101 */         .texOffs(0, 15).addBox(-4.0F, -3.0F, -3.525F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 2.325F));
/* 102 */     body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(3, 18).addBox(-4.0F, -3.0F, -2.2F, 8.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -4.325F, 0.0F, 0.0F, -3.1416F));
/* 103 */     PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create()
/* 104 */         .texOffs(37, 8).addBox(-1.0F, -7.0F, -3.3F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.015F))
/* 105 */         .texOffs(37, 0).addBox(-2.0F, -11.0F, -4.3F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.015F))
/* 106 */         .texOffs(0, 0).addBox(-4.0F, -3.0F, -7.325F, 8.0F, 5.0F, 10.0F, new CubeDeformation(0.0F))
/* 107 */         .texOffs(56, 0).addBox(-1.0F, 0.0F, -8.325F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, -0.2F));
/* 108 */     PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, -5.6F, -1.8F, 0.4363F, 0.0F, 0.0F));
/* 109 */     right_arm.addOrReplaceChild("right_arm_r1", CubeListBuilder.create().texOffs(36, 16).addBox(-3.075F, -0.9733F, -1.9966F, 3.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0893F, 0.1198F, -1.0472F, 0.0F, 0.0F));
/* 110 */     PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, -5.6F, -1.7F, 0.4363F, 0.0F, 0.0F));
/* 111 */     left_arm.addOrReplaceChild("left_arm_r1", CubeListBuilder.create().texOffs(50, 16).addBox(0.075F, -1.0443F, -1.8997F, 3.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.0015F, -0.0808F, -1.0472F, 0.0F, 0.0F));
/* 112 */     PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.1F, -2.1F, -2.075F));
/* 113 */     right_leg.addOrReplaceChild("right_leg_r1", CubeListBuilder.create().texOffs(0, 27).addBox(-2.0F, 0.975F, 0.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.05F, -1.9F, 1.075F, -1.5708F, 0.0F, 0.0F));
/* 114 */     PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -2.0F, -2.075F));
/* 115 */     left_leg.addOrReplaceChild("left_leg_r1", CubeListBuilder.create().texOffs(16, 27).addBox(-2.0F, 0.975F, 0.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.05F, -2.0F, 1.075F, -1.5708F, 0.0F, 0.0F));
/* 116 */     return LayerDefinition.create(meshDefinition, 64, 64);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createStarPoseBodyLayer() {
/* 120 */     MeshDefinition meshDefinition = new MeshDefinition().transformed(p -> p.translated(0.0F, 0.0F, 0.0F));
/* 121 */     PartDefinition root = meshDefinition.getRoot();
/*     */     
/* 123 */     PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 15).addBox(-4.0F, -6.0F, -3.0F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));
/* 124 */     body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.0F, -5.0F, 8.0F, 5.0F, 10.0F, new CubeDeformation(0.0F))
/* 125 */         .texOffs(56, 0).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
/* 126 */         .texOffs(37, 8).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.015F))
/* 127 */         .texOffs(37, 0).addBox(-2.0F, -13.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.015F)), PartPose.offset(0.0F, -6.0F, 0.0F));
/* 128 */     PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -6.0F, 0.0F));
/* 129 */     right_arm.addOrReplaceChild("right_arm_r1", CubeListBuilder.create().texOffs(36, 16).addBox(-1.5F, -5.0F, -2.0F, 3.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.9199F));
/* 130 */     right_arm.addOrReplaceChild("rightItem", CubeListBuilder.create(), PartPose.offset(-1.0F, 7.4F, -1.0F));
/* 131 */     PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -6.0F, 0.0F));
/* 132 */     left_arm.addOrReplaceChild("left_arm_r1", CubeListBuilder.create().texOffs(50, 16).addBox(-1.5F, -5.0F, -2.0F, 3.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.0F, 0.0F, 0.0F, 0.0F, -1.9199F));
/* 133 */     PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-3.0F, -5.0F, 0.0F));
/* 134 */     right_leg.addOrReplaceChild("right_leg_r1", CubeListBuilder.create().texOffs(0, 27).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.35F, 2.0F, 0.01F, 0.0F, 0.0F, 0.2618F));
/* 135 */     PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(1.0F, -5.0F, 0.0F));
/* 136 */     left_leg.addOrReplaceChild("left_leg_r1", CubeListBuilder.create().texOffs(16, 27).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.65F, 2.0F, 0.0F, 0.0F, 0.0F, -0.2618F));
/* 137 */     return LayerDefinition.create(meshDefinition, 64, 64);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createEyesLayer() {
/* 141 */     return createBodyLayer().apply(mesh -> {
/*     */           mesh.getRoot().retainPartsAndChildren(Set.of("eyes"));
/*     */           return mesh;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(CopperGolemRenderState state) {
/* 149 */     super.setupAnim(state);
/* 150 */     this.head.xRot = state.xRot * 0.017453292F;
/* 151 */     this.head.yRot = state.yRot * 0.017453292F;
/* 152 */     if (state.rightHandItemState.isEmpty() && state.leftHandItemState.isEmpty()) {
/* 153 */       this.walkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 2.0F, 2.5F);
/*     */     } else {
/* 155 */       this.walkWithItemAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 2.0F, 2.5F);
/* 156 */       poseHeldItemArmsIfStill();
/*     */     } 
/* 158 */     this.idleAnimation.apply(state.idleAnimationState, state.ageInTicks);
/* 159 */     this.interactionGetItem.apply(state.interactionGetItem, state.ageInTicks);
/* 160 */     this.interactionGetNoItem.apply(state.interactionGetNoItem, state.ageInTicks);
/* 161 */     this.interactionDropItem.apply(state.interactionDropItem, state.ageInTicks);
/* 162 */     this.interactionDropNoItem.apply(state.interactionDropNoItem, state.ageInTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   public void translateToHand(CopperGolemRenderState state, HumanoidArm arm, PoseStack poseStack) {
/* 167 */     this.root.translateAndRotate(poseStack);
/* 168 */     this.body.translateAndRotate(poseStack);
/* 169 */     ModelPart activeArm = (arm == HumanoidArm.RIGHT) ? this.rightArm : this.leftArm;
/* 170 */     activeArm.translateAndRotate(poseStack);
/* 171 */     if (state.copperGolemState.equals(CopperGolemState.IDLE)) {
/* 172 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees((arm == HumanoidArm.RIGHT) ? -90.0F : 90.0F));
/* 173 */       poseStack.translate(0.0F, 0.0F, 0.125F);
/*     */     } else {
/* 175 */       poseStack.scale(0.55F, 0.55F, 0.55F);
/* 176 */       poseStack.translate(-0.125F, 0.3125F, -0.1875F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelPart getHead() {
/* 182 */     return this.head;
/*     */   }
/*     */ 
/*     */   
/*     */   public void translateToHead(PoseStack poseStack) {
/* 187 */     this.body.translateAndRotate(poseStack);
/* 188 */     this.head.translateAndRotate(poseStack);
/* 189 */     poseStack.translate(0.0F, 0.125F, 0.0F);
/* 190 */     poseStack.scale(1.0625F, 1.0625F, 1.0625F);
/*     */   }
/*     */   
/*     */   public void applyBlockOnAntennaTransform(PoseStack poseStack) {
/* 194 */     this.root.translateAndRotate(poseStack);
/* 195 */     this.body.translateAndRotate(poseStack);
/* 196 */     this.head.translateAndRotate(poseStack);
/* 197 */     poseStack.translate(0.0D, -2.25D, 0.0D);
/*     */   }
/*     */   
/*     */   private void poseHeldItemArmsIfStill() {
/* 201 */     this.rightArm.xRot = Math.min(this.rightArm.xRot, -0.87266463F);
/* 202 */     this.leftArm.xRot = Math.min(this.leftArm.xRot, -0.87266463F);
/* 203 */     this.rightArm.yRot = Math.min(this.rightArm.yRot, -0.1134464F);
/* 204 */     this.leftArm.yRot = Math.max(this.leftArm.yRot, 0.1134464F);
/* 205 */     this.rightArm.zRot = Math.min(this.rightArm.zRot, -0.064577185F);
/* 206 */     this.leftArm.zRot = Math.max(this.leftArm.zRot, 0.064577185F);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/golem/CopperGolemModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */