/*     */ package net.minecraft.client.model.animal.armadillo;
/*     */ 
/*     */ import net.minecraft.client.animation.KeyframeAnimation;
/*     */ import net.minecraft.client.animation.definitions.ArmadilloAnimation;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.ArmadilloRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class ArmadilloModel
/*     */   extends EntityModel<ArmadilloRenderState>
/*     */ {
/*  20 */   public static final MeshTransformer BABY_TRANSFORMER = MeshTransformer.scaling(0.6F);
/*     */   
/*     */   private static final float MAX_DOWN_HEAD_ROTATION_EXTENT = 25.0F;
/*     */   
/*     */   private static final float MAX_UP_HEAD_ROTATION_EXTENT = 22.5F;
/*     */   
/*     */   private static final float MAX_WALK_ANIMATION_SPEED = 16.5F;
/*     */   private static final float WALK_ANIMATION_SCALE_FACTOR = 2.5F;
/*     */   private static final String HEAD_CUBE = "head_cube";
/*     */   private static final String RIGHT_EAR_CUBE = "right_ear_cube";
/*     */   private static final String LEFT_EAR_CUBE = "left_ear_cube";
/*     */   private final ModelPart body;
/*     */   private final ModelPart rightHindLeg;
/*     */   private final ModelPart leftHindLeg;
/*     */   private final ModelPart cube;
/*     */   private final ModelPart head;
/*     */   private final ModelPart tail;
/*     */   private final KeyframeAnimation walkAnimation;
/*     */   private final KeyframeAnimation rollOutAnimation;
/*     */   private final KeyframeAnimation rollUpAnimation;
/*     */   private final KeyframeAnimation peekAnimation;
/*     */   
/*     */   public ArmadilloModel(ModelPart root) {
/*  43 */     super(root);
/*  44 */     this.body = root.getChild("body");
/*  45 */     this.rightHindLeg = root.getChild("right_hind_leg");
/*  46 */     this.leftHindLeg = root.getChild("left_hind_leg");
/*  47 */     this.head = this.body.getChild("head");
/*  48 */     this.tail = this.body.getChild("tail");
/*  49 */     this.cube = root.getChild("cube");
/*     */     
/*  51 */     this.walkAnimation = ArmadilloAnimation.ARMADILLO_WALK.bake(root);
/*  52 */     this.rollOutAnimation = ArmadilloAnimation.ARMADILLO_ROLL_OUT.bake(root);
/*  53 */     this.rollUpAnimation = ArmadilloAnimation.ARMADILLO_ROLL_UP.bake(root);
/*  54 */     this.peekAnimation = ArmadilloAnimation.ARMADILLO_PEEK.bake(root);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  60 */     MeshDefinition meshdefinition = new MeshDefinition();
/*  61 */     PartDefinition partdefinition = meshdefinition.getRoot();
/*     */     
/*  63 */     PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 20).addBox(-4.0F, -7.0F, -10.0F, 8.0F, 8.0F, 12.0F, new CubeDeformation(0.3F))
/*  64 */         .texOffs(0, 40).addBox(-4.0F, -7.0F, -10.0F, 8.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, 4.0F));
/*     */     
/*  66 */     body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(44, 53).addBox(-0.5F, -0.0865F, 0.0933F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 1.0F, 0.5061F, 0.0F, 0.0F));
/*     */     
/*  68 */     PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, -11.0F));
/*     */     
/*  70 */     head.addOrReplaceChild("head_cube", CubeListBuilder.create().texOffs(43, 15).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));
/*     */     
/*  72 */     PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create(), PartPose.offset(-1.0F, -1.0F, 0.0F));
/*     */     
/*  74 */     right_ear.addOrReplaceChild("right_ear_cube", CubeListBuilder.create().texOffs(43, 10).addBox(-2.0F, -3.0F, 0.0F, 2.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, -0.6F, 0.1886F, -0.3864F, -0.0718F));
/*     */     
/*  76 */     PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create(), PartPose.offset(1.0F, -2.0F, 0.0F));
/*     */     
/*  78 */     left_ear.addOrReplaceChild("left_ear_cube", CubeListBuilder.create().texOffs(47, 10).addBox(0.0F, -3.0F, 0.0F, 2.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.0F, -0.6F, 0.1886F, 0.3864F, 0.0718F));
/*     */     
/*  80 */     partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(51, 31).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 21.0F, 4.0F));
/*     */     
/*  82 */     partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(42, 31).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 21.0F, 4.0F));
/*     */     
/*  84 */     partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(51, 43).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 21.0F, -4.0F));
/*     */     
/*  86 */     partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(42, 43).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 21.0F, -4.0F));
/*     */     
/*  88 */     partdefinition.addOrReplaceChild("cube", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -10.0F, -6.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
/*     */     
/*  90 */     return LayerDefinition.create(meshdefinition, 64, 64);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(ArmadilloRenderState state) {
/*  95 */     super.setupAnim(state);
/*  96 */     if (state.isHidingInShell) {
/*  97 */       this.body.skipDraw = true;
/*  98 */       this.leftHindLeg.visible = false;
/*  99 */       this.rightHindLeg.visible = false;
/* 100 */       this.tail.visible = false;
/* 101 */       this.cube.visible = true;
/*     */     } else {
/* 103 */       this.body.skipDraw = false;
/* 104 */       this.leftHindLeg.visible = true;
/* 105 */       this.rightHindLeg.visible = true;
/* 106 */       this.tail.visible = true;
/* 107 */       this.cube.visible = false;
/*     */       
/* 109 */       this.head.xRot = Mth.clamp(state.xRot, -22.5F, 25.0F) * 0.017453292F;
/* 110 */       this.head.yRot = Mth.clamp(state.yRot, -32.5F, 32.5F) * 0.017453292F;
/*     */     } 
/* 112 */     this.walkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 16.5F, 2.5F);
/*     */     
/* 114 */     this.rollOutAnimation.apply(state.rollOutAnimationState, state.ageInTicks);
/* 115 */     this.rollUpAnimation.apply(state.rollUpAnimationState, state.ageInTicks);
/* 116 */     this.peekAnimation.apply(state.peekAnimationState, state.ageInTicks);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/armadillo/ArmadilloModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */