/*     */ package net.minecraft.client.model.monster.warden;
/*     */ 
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.animation.KeyframeAnimation;
/*     */ import net.minecraft.client.animation.definitions.WardenAnimation;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.WardenRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WardenModel
/*     */   extends EntityModel<WardenRenderState>
/*     */ {
/*     */   private static final float DEFAULT_ARM_X_Y = 13.0F;
/*     */   private static final float DEFAULT_ARM_Z = 1.0F;
/*     */   protected final ModelPart bone;
/*     */   protected final ModelPart body;
/*     */   protected final ModelPart head;
/*     */   protected final ModelPart rightTendril;
/*     */   protected final ModelPart leftTendril;
/*     */   protected final ModelPart leftLeg;
/*     */   protected final ModelPart leftArm;
/*     */   protected final ModelPart leftRibcage;
/*     */   protected final ModelPart rightArm;
/*     */   protected final ModelPart rightLeg;
/*     */   protected final ModelPart rightRibcage;
/*     */   private final KeyframeAnimation attackAnimation;
/*     */   private final KeyframeAnimation sonicBoomAnimation;
/*     */   private final KeyframeAnimation diggingAnimation;
/*     */   private final KeyframeAnimation emergeAnimation;
/*     */   private final KeyframeAnimation roarAnimation;
/*     */   private final KeyframeAnimation sniffAnimation;
/*     */   
/*     */   public WardenModel(ModelPart root) {
/*  43 */     super(root, RenderTypes::entityCutoutNoCull);
/*     */     
/*  45 */     this.bone = root.getChild("bone");
/*  46 */     this.body = this.bone.getChild("body");
/*  47 */     this.head = this.body.getChild("head");
/*  48 */     this.rightLeg = this.bone.getChild("right_leg");
/*  49 */     this.leftLeg = this.bone.getChild("left_leg");
/*  50 */     this.rightArm = this.body.getChild("right_arm");
/*  51 */     this.leftArm = this.body.getChild("left_arm");
/*  52 */     this.rightTendril = this.head.getChild("right_tendril");
/*  53 */     this.leftTendril = this.head.getChild("left_tendril");
/*  54 */     this.rightRibcage = this.body.getChild("right_ribcage");
/*  55 */     this.leftRibcage = this.body.getChild("left_ribcage");
/*     */     
/*  57 */     this.attackAnimation = WardenAnimation.WARDEN_ATTACK.bake(root);
/*  58 */     this.sonicBoomAnimation = WardenAnimation.WARDEN_SONIC_BOOM.bake(root);
/*  59 */     this.diggingAnimation = WardenAnimation.WARDEN_DIG.bake(root);
/*  60 */     this.emergeAnimation = WardenAnimation.WARDEN_EMERGE.bake(root);
/*  61 */     this.roarAnimation = WardenAnimation.WARDEN_ROAR.bake(root);
/*  62 */     this.sniffAnimation = WardenAnimation.WARDEN_SNIFF.bake(root);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  66 */     MeshDefinition mesh = new MeshDefinition();
/*  67 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  69 */     PartDefinition bone = root.addOrReplaceChild("bone", 
/*  70 */         CubeListBuilder.create(), 
/*  71 */         PartPose.offset(0.0F, 24.0F, 0.0F));
/*     */     
/*  73 */     PartDefinition body = bone.addOrReplaceChild("body", 
/*  74 */         CubeListBuilder.create()
/*  75 */         .texOffs(0, 0).addBox(-9.0F, -13.0F, -4.0F, 18.0F, 21.0F, 11.0F), 
/*  76 */         PartPose.offset(0.0F, -21.0F, 0.0F));
/*     */     
/*  78 */     body.addOrReplaceChild("right_ribcage", 
/*  79 */         CubeListBuilder.create().texOffs(90, 11).addBox(-2.0F, -11.0F, -0.1F, 9.0F, 21.0F, 0.0F), 
/*  80 */         PartPose.offset(-7.0F, -2.0F, -4.0F));
/*     */ 
/*     */     
/*  83 */     body.addOrReplaceChild("left_ribcage", 
/*  84 */         CubeListBuilder.create()
/*  85 */         .texOffs(90, 11).mirror().addBox(-7.0F, -11.0F, -0.1F, 9.0F, 21.0F, 0.0F).mirror(false), 
/*  86 */         PartPose.offset(7.0F, -2.0F, -4.0F));
/*     */ 
/*     */     
/*  89 */     PartDefinition head = body.addOrReplaceChild("head", 
/*  90 */         CubeListBuilder.create()
/*  91 */         .texOffs(0, 32).addBox(-8.0F, -16.0F, -5.0F, 16.0F, 16.0F, 10.0F), 
/*  92 */         PartPose.offset(0.0F, -13.0F, 0.0F));
/*     */ 
/*     */     
/*  95 */     head.addOrReplaceChild("right_tendril", 
/*  96 */         CubeListBuilder.create()
/*  97 */         .texOffs(52, 32).addBox(-16.0F, -13.0F, 0.0F, 16.0F, 16.0F, 0.0F), 
/*  98 */         PartPose.offset(-8.0F, -12.0F, 0.0F));
/*     */ 
/*     */     
/* 101 */     head.addOrReplaceChild("left_tendril", 
/* 102 */         CubeListBuilder.create()
/* 103 */         .texOffs(58, 0).addBox(0.0F, -13.0F, 0.0F, 16.0F, 16.0F, 0.0F), 
/* 104 */         PartPose.offset(8.0F, -12.0F, 0.0F));
/*     */ 
/*     */     
/* 107 */     body.addOrReplaceChild("right_arm", 
/* 108 */         CubeListBuilder.create()
/* 109 */         .texOffs(44, 50).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 28.0F, 8.0F), 
/* 110 */         PartPose.offset(-13.0F, -13.0F, 1.0F));
/*     */ 
/*     */     
/* 113 */     body.addOrReplaceChild("left_arm", 
/* 114 */         CubeListBuilder.create()
/* 115 */         .texOffs(0, 58).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 28.0F, 8.0F), 
/* 116 */         PartPose.offset(13.0F, -13.0F, 1.0F));
/*     */ 
/*     */     
/* 119 */     bone.addOrReplaceChild("right_leg", 
/* 120 */         CubeListBuilder.create()
/* 121 */         .texOffs(76, 48).addBox(-3.1F, 0.0F, -3.0F, 6.0F, 13.0F, 6.0F), 
/* 122 */         PartPose.offset(-5.9F, -13.0F, 0.0F));
/*     */ 
/*     */     
/* 125 */     bone.addOrReplaceChild("left_leg", 
/* 126 */         CubeListBuilder.create()
/* 127 */         .texOffs(76, 76).addBox(-2.9F, 0.0F, -3.0F, 6.0F, 13.0F, 6.0F), 
/* 128 */         PartPose.offset(5.9F, -13.0F, 0.0F));
/*     */ 
/*     */     
/* 131 */     return LayerDefinition.create(mesh, 128, 128);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createTendrilsLayer() {
/* 135 */     return createBodyLayer().apply(mesh -> {
/*     */           mesh.getRoot().retainExactParts(Set.of("left_tendril", "right_tendril"));
/*     */           return mesh;
/*     */         });
/*     */   }
/*     */   
/*     */   public static LayerDefinition createHeartLayer() {
/* 142 */     return createBodyLayer().apply(mesh -> {
/*     */           mesh.getRoot().retainExactParts(Set.of("body"));
/*     */           return mesh;
/*     */         });
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBioluminescentLayer() {
/* 149 */     return createBodyLayer().apply(mesh -> {
/*     */           mesh.getRoot().retainExactParts(Set.of("head", "left_arm", "right_arm", "left_leg", "right_leg"));
/*     */           return mesh;
/*     */         });
/*     */   }
/*     */   
/*     */   public static LayerDefinition createPulsatingSpotsLayer() {
/* 156 */     return createBodyLayer().apply(mesh -> {
/*     */           mesh.getRoot().retainExactParts(Set.of("body", "head", "left_arm", "right_arm", "left_leg", "right_leg"));
/*     */           return mesh;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(WardenRenderState state) {
/* 164 */     super.setupAnim(state);
/*     */ 
/*     */     
/* 167 */     animateHeadLookTarget(state.yRot, state.xRot);
/* 168 */     animateWalk(state.walkAnimationPos, state.walkAnimationSpeed);
/* 169 */     animateIdlePose(state.ageInTicks);
/* 170 */     animateTendrils(state, state.ageInTicks);
/*     */ 
/*     */     
/* 173 */     this.attackAnimation.apply(state.attackAnimationState, state.ageInTicks);
/* 174 */     this.sonicBoomAnimation.apply(state.sonicBoomAnimationState, state.ageInTicks);
/* 175 */     this.diggingAnimation.apply(state.diggingAnimationState, state.ageInTicks);
/* 176 */     this.emergeAnimation.apply(state.emergeAnimationState, state.ageInTicks);
/* 177 */     this.roarAnimation.apply(state.roarAnimationState, state.ageInTicks);
/* 178 */     this.sniffAnimation.apply(state.sniffAnimationState, state.ageInTicks);
/*     */   }
/*     */   
/*     */   private void animateHeadLookTarget(float yRot, float xRot) {
/* 182 */     this.head.xRot = xRot * 0.017453292F;
/* 183 */     this.head.yRot = yRot * 0.017453292F;
/*     */   }
/*     */   
/*     */   private void animateIdlePose(float ageInTicks) {
/* 187 */     float scaledAge = ageInTicks * 0.1F;
/* 188 */     float wobbleCosine = Mth.cos(scaledAge);
/* 189 */     float wobbleSine = Mth.sin(scaledAge);
/*     */ 
/*     */     
/* 192 */     this.head.zRot += 0.06F * wobbleCosine;
/* 193 */     this.head.xRot += 0.06F * wobbleSine;
/*     */     
/* 195 */     this.body.zRot += 0.025F * wobbleSine;
/* 196 */     this.body.xRot += 0.025F * wobbleCosine;
/*     */   }
/*     */   
/*     */   private void animateWalk(float animationPos, float animationSpeed) {
/* 200 */     float speedModifier = Math.min(0.5F, 3.0F * animationSpeed);
/* 201 */     float adjustedPos = animationPos * 0.8662F;
/* 202 */     float adjustedPosCosine = Mth.cos(adjustedPos);
/* 203 */     float adjustedPosSine = Mth.sin(adjustedPos);
/* 204 */     float speedModifierWithMin = Math.min(0.35F, speedModifier);
/*     */     
/* 206 */     this.head.zRot += 0.3F * adjustedPosSine * speedModifier;
/* 207 */     this.head.xRot += 1.2F * Mth.cos((adjustedPos + 1.5707964F)) * speedModifierWithMin;
/*     */     
/* 209 */     this.body.zRot = 0.1F * adjustedPosSine * speedModifier;
/* 210 */     this.body.xRot = 1.0F * adjustedPosCosine * speedModifierWithMin;
/*     */     
/* 212 */     this.leftLeg.xRot = 1.0F * adjustedPosCosine * speedModifier;
/* 213 */     this.rightLeg.xRot = 1.0F * Mth.cos((adjustedPos + 3.1415927F)) * speedModifier;
/*     */     
/* 215 */     this.leftArm.xRot = -(0.8F * adjustedPosCosine * speedModifier);
/* 216 */     this.leftArm.zRot = 0.0F;
/*     */     
/* 218 */     this.rightArm.xRot = -(0.8F * adjustedPosSine * speedModifier);
/* 219 */     this.rightArm.zRot = 0.0F;
/*     */     
/* 221 */     resetArmPoses();
/*     */   }
/*     */   
/*     */   private void resetArmPoses() {
/* 225 */     this.leftArm.yRot = 0.0F;
/* 226 */     this.leftArm.z = 1.0F;
/* 227 */     this.leftArm.x = 13.0F;
/* 228 */     this.leftArm.y = -13.0F;
/*     */     
/* 230 */     this.rightArm.yRot = 0.0F;
/* 231 */     this.rightArm.z = 1.0F;
/* 232 */     this.rightArm.x = -13.0F;
/* 233 */     this.rightArm.y = -13.0F;
/*     */   }
/*     */   
/*     */   private void animateTendrils(WardenRenderState state, float ageInTicks) {
/* 237 */     float tendrilXRot = state.tendrilAnimation * (float)(Math.cos(ageInTicks * 2.25D) * Math.PI * 0.10000000149011612D);
/* 238 */     this.leftTendril.xRot = tendrilXRot;
/* 239 */     this.rightTendril.xRot = -tendrilXRot;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/warden/WardenModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */