/*     */ package net.minecraft.client.animation.definitions;
/*     */ 
/*     */ import net.minecraft.client.animation.AnimationChannel;
/*     */ import net.minecraft.client.animation.AnimationDefinition;
/*     */ import net.minecraft.client.animation.Keyframe;
/*     */ import net.minecraft.client.animation.KeyframeAnimations;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class BatAnimation
/*     */ {
/*  11 */   public static final AnimationDefinition BAT_RESTING = AnimationDefinition.Builder.withLength(0.5F)
/*  12 */     .looping()
/*  13 */     .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  14 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(180.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  16 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  17 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  19 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  20 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(180.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  22 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  23 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  25 */         })).addAnimation("feet", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  26 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  28 */         })).addAnimation("right_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  29 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  31 */         })).addAnimation("right_wing", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  32 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  34 */         })).addAnimation("right_wing_tip", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  35 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -120.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  37 */         })).addAnimation("left_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  38 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  40 */         })).addAnimation("left_wing", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  41 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  43 */         })).addAnimation("left_wing_tip", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  44 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 120.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  46 */         })).build();
/*     */   
/*  48 */   public static final AnimationDefinition BAT_FLYING = AnimationDefinition.Builder.withLength(0.5F)
/*  49 */     .looping()
/*  50 */     .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  51 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  52 */             (Vector3fc)KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  53 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  55 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  56 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  57 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  58 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  59 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, 
/*  60 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  61 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  63 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  64 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  65 */             (Vector3fc)KeyframeAnimations.degreeVec(52.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  66 */             (Vector3fc)KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  68 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  69 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  70 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  71 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  72 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, 
/*  73 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  74 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  76 */         })).addAnimation("feet", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  77 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  78 */             (Vector3fc)KeyframeAnimations.degreeVec(-21.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  79 */             (Vector3fc)KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  80 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  82 */         })).addAnimation("right_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  83 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 85.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  84 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -55.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  85 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 50.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  86 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 70.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  87 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 85.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  89 */         })).addAnimation("right_wing_tip", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  90 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 10.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0417F, 
/*  91 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 65.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  92 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -135.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  93 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 10.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  95 */         })).addAnimation("left_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  96 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -85.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  97 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 55.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  98 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -50.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  99 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -70.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 100 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -85.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 102 */         })).addAnimation("left_wing_tip", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 103 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -10.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0417F, 
/* 104 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -65.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/* 105 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 135.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 106 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -10.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 108 */         })).build();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/animation/definitions/BatAnimation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */