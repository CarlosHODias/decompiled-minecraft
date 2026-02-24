/*     */ package net.minecraft.client.model.animal.frog;
/*     */ 
/*     */ import net.minecraft.client.animation.KeyframeAnimation;
/*     */ import net.minecraft.client.animation.definitions.FrogAnimation;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.FrogRenderState;
/*     */ 
/*     */ 
/*     */ public class FrogModel
/*     */   extends EntityModel<FrogRenderState>
/*     */ {
/*     */   private static final float MAX_WALK_ANIMATION_SPEED = 1.5F;
/*     */   private static final float MAX_SWIM_ANIMATION_SPEED = 1.0F;
/*     */   private static final float WALK_ANIMATION_SCALE_FACTOR = 2.5F;
/*     */   private final ModelPart body;
/*     */   private final ModelPart head;
/*     */   private final ModelPart eyes;
/*     */   private final ModelPart tongue;
/*     */   private final ModelPart leftArm;
/*     */   private final ModelPart rightArm;
/*     */   private final ModelPart leftLeg;
/*     */   private final ModelPart rightLeg;
/*     */   private final ModelPart croakingBody;
/*     */   private final KeyframeAnimation jumpAnimation;
/*     */   private final KeyframeAnimation croakAnimation;
/*     */   private final KeyframeAnimation tongueAnimation;
/*     */   private final KeyframeAnimation swimAnimation;
/*     */   private final KeyframeAnimation walkAnimation;
/*     */   private final KeyframeAnimation idleWaterAnimation;
/*     */   
/*     */   public FrogModel(ModelPart root) {
/*  39 */     super(root.getChild("root"));
/*  40 */     this.body = this.root.getChild("body");
/*  41 */     this.head = this.body.getChild("head");
/*  42 */     this.eyes = this.head.getChild("eyes");
/*  43 */     this.tongue = this.body.getChild("tongue");
/*  44 */     this.leftArm = this.body.getChild("left_arm");
/*  45 */     this.rightArm = this.body.getChild("right_arm");
/*  46 */     this.leftLeg = this.root.getChild("left_leg");
/*  47 */     this.rightLeg = this.root.getChild("right_leg");
/*  48 */     this.croakingBody = this.body.getChild("croaking_body");
/*     */     
/*  50 */     this.jumpAnimation = FrogAnimation.FROG_JUMP.bake(root);
/*  51 */     this.croakAnimation = FrogAnimation.FROG_CROAK.bake(root);
/*  52 */     this.tongueAnimation = FrogAnimation.FROG_TONGUE.bake(root);
/*  53 */     this.swimAnimation = FrogAnimation.FROG_SWIM.bake(root);
/*  54 */     this.walkAnimation = FrogAnimation.FROG_WALK.bake(root);
/*  55 */     this.idleWaterAnimation = FrogAnimation.FROG_IDLE_WATER.bake(root);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  59 */     MeshDefinition mesh = new MeshDefinition();
/*  60 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  62 */     PartDefinition modelRoot = root.addOrReplaceChild("root", 
/*  63 */         CubeListBuilder.create(), 
/*  64 */         PartPose.offset(0.0F, 24.0F, 0.0F));
/*     */     
/*  66 */     PartDefinition body = modelRoot.addOrReplaceChild("body", 
/*  67 */         CubeListBuilder.create()
/*  68 */         .texOffs(3, 1).addBox(-3.5F, -2.0F, -8.0F, 7.0F, 3.0F, 9.0F)
/*  69 */         .texOffs(23, 22).addBox(-3.5F, -1.0F, -8.0F, 7.0F, 0.0F, 9.0F), 
/*  70 */         PartPose.offset(0.0F, -2.0F, 4.0F));
/*     */     
/*  72 */     PartDefinition head = body.addOrReplaceChild("head", 
/*  73 */         CubeListBuilder.create()
/*  74 */         .texOffs(23, 13).addBox(-3.5F, -1.0F, -7.0F, 7.0F, 0.0F, 9.0F)
/*  75 */         .texOffs(0, 13).addBox(-3.5F, -2.0F, -7.0F, 7.0F, 3.0F, 9.0F), 
/*  76 */         PartPose.offset(0.0F, -2.0F, -1.0F));
/*     */     
/*  78 */     PartDefinition eyes = head.addOrReplaceChild("eyes", 
/*  79 */         CubeListBuilder.create(), 
/*  80 */         PartPose.offset(-0.5F, 0.0F, 2.0F));
/*     */     
/*  82 */     eyes.addOrReplaceChild("right_eye", 
/*  83 */         CubeListBuilder.create()
/*  84 */         .texOffs(0, 0).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F), 
/*  85 */         PartPose.offset(-1.5F, -3.0F, -6.5F));
/*     */     
/*  87 */     eyes.addOrReplaceChild("left_eye", 
/*  88 */         CubeListBuilder.create()
/*  89 */         .texOffs(0, 5).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F), 
/*  90 */         PartPose.offset(2.5F, -3.0F, -6.5F));
/*     */ 
/*     */     
/*  93 */     body.addOrReplaceChild("croaking_body", 
/*  94 */         CubeListBuilder.create()
/*  95 */         .texOffs(26, 5).addBox(-3.5F, -0.1F, -2.9F, 7.0F, 2.0F, 3.0F, new CubeDeformation(-0.1F)), 
/*  96 */         PartPose.offset(0.0F, -1.0F, -5.0F));
/*     */ 
/*     */     
/*  99 */     PartDefinition tongue = body.addOrReplaceChild("tongue", 
/* 100 */         CubeListBuilder.create()
/* 101 */         .texOffs(17, 13).addBox(-2.0F, 0.0F, -7.1F, 4.0F, 0.0F, 7.0F), 
/* 102 */         PartPose.offset(0.0F, -1.01F, 1.0F));
/*     */ 
/*     */     
/* 105 */     PartDefinition leftArm = body.addOrReplaceChild("left_arm", 
/* 106 */         CubeListBuilder.create()
/* 107 */         .texOffs(0, 32).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 3.0F), 
/* 108 */         PartPose.offset(4.0F, -1.0F, -6.5F));
/*     */ 
/*     */     
/* 111 */     leftArm.addOrReplaceChild("left_hand", 
/* 112 */         CubeListBuilder.create()
/* 113 */         .texOffs(18, 40).addBox(-4.0F, 0.01F, -4.0F, 8.0F, 0.0F, 8.0F), 
/* 114 */         PartPose.offset(0.0F, 3.0F, -1.0F));
/*     */ 
/*     */     
/* 117 */     PartDefinition rightArm = body.addOrReplaceChild("right_arm", 
/* 118 */         CubeListBuilder.create()
/* 119 */         .texOffs(0, 38).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 3.0F), 
/* 120 */         PartPose.offset(-4.0F, -1.0F, -6.5F));
/*     */ 
/*     */     
/* 123 */     rightArm.addOrReplaceChild("right_hand", 
/* 124 */         CubeListBuilder.create()
/* 125 */         .texOffs(2, 40).addBox(-4.0F, 0.01F, -5.0F, 8.0F, 0.0F, 8.0F), 
/* 126 */         PartPose.offset(0.0F, 3.0F, 0.0F));
/*     */ 
/*     */     
/* 129 */     PartDefinition leftLeg = modelRoot.addOrReplaceChild("left_leg", 
/* 130 */         CubeListBuilder.create()
/* 131 */         .texOffs(14, 25).addBox(-1.0F, 0.0F, -2.0F, 3.0F, 3.0F, 4.0F), 
/* 132 */         PartPose.offset(3.5F, -3.0F, 4.0F));
/*     */ 
/*     */     
/* 135 */     leftLeg.addOrReplaceChild("left_foot", 
/* 136 */         CubeListBuilder.create()
/* 137 */         .texOffs(2, 32).addBox(-4.0F, 0.01F, -4.0F, 8.0F, 0.0F, 8.0F), 
/* 138 */         PartPose.offset(2.0F, 3.0F, 0.0F));
/*     */ 
/*     */     
/* 141 */     PartDefinition rightLeg = modelRoot.addOrReplaceChild("right_leg", 
/* 142 */         CubeListBuilder.create()
/* 143 */         .texOffs(0, 25).addBox(-2.0F, 0.0F, -2.0F, 3.0F, 3.0F, 4.0F), 
/* 144 */         PartPose.offset(-3.5F, -3.0F, 4.0F));
/*     */ 
/*     */     
/* 147 */     rightLeg.addOrReplaceChild("right_foot", 
/* 148 */         CubeListBuilder.create()
/* 149 */         .texOffs(18, 32).addBox(-4.0F, 0.01F, -4.0F, 8.0F, 0.0F, 8.0F), 
/* 150 */         PartPose.offset(-2.0F, 3.0F, 0.0F));
/*     */ 
/*     */     
/* 153 */     return LayerDefinition.create(mesh, 48, 48);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(FrogRenderState state) {
/* 158 */     super.setupAnim(state);
/*     */     
/* 160 */     this.jumpAnimation.apply(state.jumpAnimationState, state.ageInTicks);
/* 161 */     this.croakAnimation.apply(state.croakAnimationState, state.ageInTicks);
/* 162 */     this.tongueAnimation.apply(state.tongueAnimationState, state.ageInTicks);
/*     */     
/* 164 */     if (state.isSwimming) {
/* 165 */       this.swimAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 1.0F, 2.5F);
/*     */     } else {
/* 167 */       this.walkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 1.5F, 2.5F);
/*     */     } 
/*     */     
/* 170 */     this.idleWaterAnimation.apply(state.swimIdleAnimationState, state.ageInTicks);
/*     */     
/* 172 */     this.croakingBody.visible = state.croakAnimationState.isStarted();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/frog/FrogModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */