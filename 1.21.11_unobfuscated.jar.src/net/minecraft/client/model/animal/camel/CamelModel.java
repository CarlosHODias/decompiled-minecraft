/*     */ package net.minecraft.client.model.animal.camel;
/*     */ 
/*     */ import net.minecraft.client.animation.KeyframeAnimation;
/*     */ import net.minecraft.client.animation.definitions.CamelAnimation;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.CamelRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ 
/*     */ public class CamelModel
/*     */   extends EntityModel<CamelRenderState>
/*     */ {
/*     */   private static final float MAX_WALK_ANIMATION_SPEED = 2.0F;
/*     */   private static final float WALK_ANIMATION_SCALE_FACTOR = 2.5F;
/*  22 */   public static final MeshTransformer BABY_TRANSFORMER = MeshTransformer.scaling(0.45F);
/*     */   
/*     */   protected final ModelPart head;
/*     */   
/*     */   private final KeyframeAnimation walkAnimation;
/*     */   private final KeyframeAnimation sitAnimation;
/*     */   private final KeyframeAnimation sitPoseAnimation;
/*     */   private final KeyframeAnimation standupAnimation;
/*     */   private final KeyframeAnimation idleAnimation;
/*     */   private final KeyframeAnimation dashAnimation;
/*     */   
/*     */   public CamelModel(ModelPart root) {
/*  34 */     super(root);
/*  35 */     ModelPart body = root.getChild("body");
/*  36 */     this.head = body.getChild("head");
/*     */     
/*  38 */     this.walkAnimation = CamelAnimation.CAMEL_WALK.bake(root);
/*  39 */     this.sitAnimation = CamelAnimation.CAMEL_SIT.bake(root);
/*  40 */     this.sitPoseAnimation = CamelAnimation.CAMEL_SIT_POSE.bake(root);
/*  41 */     this.standupAnimation = CamelAnimation.CAMEL_STANDUP.bake(root);
/*  42 */     this.idleAnimation = CamelAnimation.CAMEL_IDLE.bake(root);
/*  43 */     this.dashAnimation = CamelAnimation.CAMEL_DASH.bake(root);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  47 */     return LayerDefinition.create(createBodyMesh(), 128, 128);
/*     */   }
/*     */   
/*     */   protected static MeshDefinition createBodyMesh() {
/*  51 */     MeshDefinition mesh = new MeshDefinition();
/*  52 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  54 */     PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
/*  55 */         .texOffs(0, 25).addBox(-7.5F, -12.0F, -23.5F, 15.0F, 12.0F, 27.0F), 
/*  56 */         PartPose.offset(0.0F, 4.0F, 9.5F));
/*     */     
/*  58 */     body.addOrReplaceChild("hump", CubeListBuilder.create()
/*  59 */         .texOffs(74, 0).addBox(-4.5F, -5.0F, -5.5F, 9.0F, 5.0F, 11.0F), 
/*  60 */         PartPose.offset(0.0F, -12.0F, -10.0F));
/*     */     
/*  62 */     body.addOrReplaceChild("tail", CubeListBuilder.create()
/*  63 */         .texOffs(122, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 14.0F, 0.0F), 
/*  64 */         PartPose.offset(0.0F, -9.0F, 3.5F));
/*     */     
/*  66 */     PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create()
/*  67 */         .texOffs(60, 24).addBox(-3.5F, -7.0F, -15.0F, 7.0F, 8.0F, 19.0F)
/*  68 */         .texOffs(21, 0).addBox(-3.5F, -21.0F, -15.0F, 7.0F, 14.0F, 7.0F)
/*  69 */         .texOffs(50, 0).addBox(-2.5F, -21.0F, -21.0F, 5.0F, 5.0F, 6.0F), 
/*  70 */         PartPose.offset(0.0F, -3.0F, -19.5F));
/*     */     
/*  72 */     head.addOrReplaceChild("left_ear", CubeListBuilder.create()
/*  73 */         .texOffs(45, 0).addBox(-0.5F, 0.5F, -1.0F, 3.0F, 1.0F, 2.0F), 
/*  74 */         PartPose.offset(2.5F, -21.0F, -9.5F));
/*     */     
/*  76 */     head.addOrReplaceChild("right_ear", CubeListBuilder.create()
/*  77 */         .texOffs(67, 0).addBox(-2.5F, 0.5F, -1.0F, 3.0F, 1.0F, 2.0F), 
/*  78 */         PartPose.offset(-2.5F, -21.0F, -9.5F));
/*     */     
/*  80 */     root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create()
/*  81 */         .texOffs(58, 16).addBox(-2.5F, 2.0F, -2.5F, 5.0F, 21.0F, 5.0F), 
/*  82 */         PartPose.offset(4.9F, 1.0F, 9.5F));
/*     */     
/*  84 */     root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create()
/*  85 */         .texOffs(94, 16).addBox(-2.5F, 2.0F, -2.5F, 5.0F, 21.0F, 5.0F), 
/*  86 */         PartPose.offset(-4.9F, 1.0F, 9.5F));
/*     */     
/*  88 */     root.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
/*  89 */         .texOffs(0, 0).addBox(-2.5F, 2.0F, -2.5F, 5.0F, 21.0F, 5.0F), 
/*  90 */         PartPose.offset(4.9F, 1.0F, -10.5F));
/*     */     
/*  92 */     root.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
/*  93 */         .texOffs(0, 26).addBox(-2.5F, 2.0F, -2.5F, 5.0F, 21.0F, 5.0F), 
/*  94 */         PartPose.offset(-4.9F, 1.0F, -10.5F));
/*     */     
/*  96 */     return mesh;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(CamelRenderState state) {
/* 101 */     super.setupAnim(state);
/*     */ 
/*     */     
/* 104 */     applyHeadRotation(state, state.yRot, state.xRot);
/*     */     
/* 106 */     this.walkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 2.0F, 2.5F);
/*     */     
/* 108 */     this.sitAnimation.apply(state.sitAnimationState, state.ageInTicks);
/* 109 */     this.sitPoseAnimation.apply(state.sitPoseAnimationState, state.ageInTicks);
/* 110 */     this.standupAnimation.apply(state.sitUpAnimationState, state.ageInTicks);
/* 111 */     this.idleAnimation.apply(state.idleAnimationState, state.ageInTicks);
/* 112 */     this.dashAnimation.apply(state.dashAnimationState, state.ageInTicks);
/*     */   }
/*     */   
/*     */   private void applyHeadRotation(CamelRenderState state, float yRot, float xRot) {
/* 116 */     yRot = Mth.clamp(yRot, -30.0F, 30.0F);
/* 117 */     xRot = Mth.clamp(xRot, -25.0F, 45.0F);
/*     */     
/* 119 */     if (state.jumpCooldown > 0.0F) {
/* 120 */       float headRotation = 45.0F * state.jumpCooldown / 55.0F;
/* 121 */       xRot = Mth.clamp(xRot + headRotation, -25.0F, 70.0F);
/*     */     } 
/*     */     
/* 124 */     this.head.yRot = yRot * 0.017453292F;
/* 125 */     this.head.xRot = xRot * 0.017453292F;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/camel/CamelModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */