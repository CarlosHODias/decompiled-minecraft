/*     */ package net.minecraft.client.animation.definitions;
/*     */ 
/*     */ import net.minecraft.client.animation.AnimationChannel;
/*     */ import net.minecraft.client.animation.AnimationDefinition;
/*     */ import net.minecraft.client.animation.Keyframe;
/*     */ import net.minecraft.client.animation.KeyframeAnimations;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WardenAnimation
/*     */ {
/*  13 */   public static final AnimationDefinition WARDEN_EMERGE = AnimationDefinition.Builder.withLength(6.68F)
/*  14 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  15 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.52F, 
/*  16 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.2F, 
/*  17 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.68F, 
/*  18 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8F, 
/*  19 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.28F, 
/*  20 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.88F, 
/*  21 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.76F, 
/*  22 */             (Vector3fc)KeyframeAnimations.degreeVec(25.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.92F, 
/*  23 */             (Vector3fc)KeyframeAnimations.degreeVec(35.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.08F, 
/*  24 */             (Vector3fc)KeyframeAnimations.degreeVec(25.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.44F, 
/*  25 */             (Vector3fc)KeyframeAnimations.degreeVec(47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.56F, 
/*  26 */             (Vector3fc)KeyframeAnimations.degreeVec(47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.68F, 
/*  27 */             (Vector3fc)KeyframeAnimations.degreeVec(47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.0F, 
/*  28 */             (Vector3fc)KeyframeAnimations.degreeVec(70.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.8F, 
/*  29 */             (Vector3fc)KeyframeAnimations.degreeVec(70.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(6.64F, 
/*  30 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/*  32 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/*  33 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, -63.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.52F, 
/*  34 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -56.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.2F, 
/*  35 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.68F, 
/*  36 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8F, 
/*  37 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.28F, 
/*  38 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.88F, 
/*  39 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.16F, 
/*  40 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -27.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.76F, 
/*  41 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -14.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.92F, 
/*  42 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -11.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.08F, 
/*  43 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -14.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.44F, 
/*  44 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -6.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.56F, 
/*  45 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -4.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.68F, 
/*  46 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -6.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.0F, 
/*  47 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -3.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.8F, 
/*  48 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -3.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(6.64F, 
/*  49 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/*  51 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  52 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.52F, 
/*  53 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.92F, 
/*  54 */             (Vector3fc)KeyframeAnimations.degreeVec(0.74F, 0.0F, -40.38F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.16F, 
/*  55 */             (Vector3fc)KeyframeAnimations.degreeVec(-67.5F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.24F, 
/*  56 */             (Vector3fc)KeyframeAnimations.degreeVec(-67.5F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.32F, 
/*  57 */             (Vector3fc)KeyframeAnimations.degreeVec(-47.5F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.4F, 
/*  58 */             (Vector3fc)KeyframeAnimations.degreeVec(-67.5F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.68F, 
/*  59 */             (Vector3fc)KeyframeAnimations.degreeVec(-67.5F, 0.0F, 15.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.76F, 
/*  60 */             (Vector3fc)KeyframeAnimations.degreeVec(-67.5F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.84F, 
/*  61 */             (Vector3fc)KeyframeAnimations.degreeVec(-52.5F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.92F, 
/*  62 */             (Vector3fc)KeyframeAnimations.degreeVec(-67.5F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.64F, 
/*  63 */             (Vector3fc)KeyframeAnimations.degreeVec(-17.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.76F, 
/*  64 */             (Vector3fc)KeyframeAnimations.degreeVec(70.0F, 0.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.04F, 
/*  65 */             (Vector3fc)KeyframeAnimations.degreeVec(70.0F, 0.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.12F, 
/*  66 */             (Vector3fc)KeyframeAnimations.degreeVec(80.0F, 0.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.24F, 
/*  67 */             (Vector3fc)KeyframeAnimations.degreeVec(70.0F, 0.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.0F, 
/*  68 */             (Vector3fc)KeyframeAnimations.degreeVec(77.5F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(6.64F, 
/*  69 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/*  71 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/*  72 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.52F, 
/*  73 */             (Vector3fc)KeyframeAnimations.posVec(-8.0F, -11.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.92F, 
/*  74 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.24F, 
/*  75 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.47F, -0.95F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.32F, 
/*  76 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.47F, -0.95F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.4F, 
/*  77 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.47F, -0.95F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.68F, 
/*  78 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.76F, 
/*  79 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.84F, 
/*  80 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.92F, 
/*  81 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.64F, 
/*  82 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -2.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.76F, 
/*  83 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -4.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.04F, 
/*  84 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.12F, 
/*  85 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.24F, 
/*  86 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.0F, 
/*  87 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(6.64F, 
/*  88 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/*  90 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  91 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.52F, 
/*  92 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.2F, 
/*  93 */             (Vector3fc)KeyframeAnimations.degreeVec(-152.5F, 2.5F, 7.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.68F, 
/*  94 */             (Vector3fc)KeyframeAnimations.degreeVec(-180.0F, 12.5F, -10.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8F, 
/*  95 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 12.5F, -10.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.28F, 
/*  96 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 12.5F, -10.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.88F, 
/*  97 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 12.5F, -10.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.08F, 
/*  98 */             (Vector3fc)KeyframeAnimations.degreeVec(-95.0F, 12.5F, -10.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.24F, 
/*  99 */             (Vector3fc)KeyframeAnimations.degreeVec(-83.93F, 3.93F, 5.71F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.36F, 
/* 100 */             (Vector3fc)KeyframeAnimations.degreeVec(-80.0F, 7.5F, 17.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.76F, 
/* 101 */             (Vector3fc)KeyframeAnimations.degreeVec(-67.5F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.08F, 
/* 102 */             (Vector3fc)KeyframeAnimations.degreeVec(-67.5F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.44F, 
/* 103 */             (Vector3fc)KeyframeAnimations.degreeVec(-55.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.56F, 
/* 104 */             (Vector3fc)KeyframeAnimations.degreeVec(-60.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.68F, 
/* 105 */             (Vector3fc)KeyframeAnimations.degreeVec(-55.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.0F, 
/* 106 */             (Vector3fc)KeyframeAnimations.degreeVec(-67.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.56F, 
/* 107 */             (Vector3fc)KeyframeAnimations.degreeVec(-50.45F, 0.0F, 2.69F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(6.08F, 
/* 108 */             (Vector3fc)KeyframeAnimations.degreeVec(-62.72F, 0.0F, 4.3F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(6.64F, 
/* 109 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 111 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/* 112 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.52F, 
/* 113 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.2F, 
/* 114 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -21.0F, 9.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.68F, 
/* 115 */             (Vector3fc)KeyframeAnimations.posVec(2.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8F, 
/* 116 */             (Vector3fc)KeyframeAnimations.posVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.28F, 
/* 117 */             (Vector3fc)KeyframeAnimations.posVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.88F, 
/* 118 */             (Vector3fc)KeyframeAnimations.posVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.08F, 
/* 119 */             (Vector3fc)KeyframeAnimations.posVec(2.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.24F, 
/* 120 */             (Vector3fc)KeyframeAnimations.posVec(2.0F, 2.71F, 3.86F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.36F, 
/* 121 */             (Vector3fc)KeyframeAnimations.posVec(2.0F, 1.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.76F, 
/* 122 */             (Vector3fc)KeyframeAnimations.posVec(2.0F, 3.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.08F, 
/* 123 */             (Vector3fc)KeyframeAnimations.posVec(2.0F, 3.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.44F, 
/* 124 */             (Vector3fc)KeyframeAnimations.posVec(2.67F, 4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.56F, 
/* 125 */             (Vector3fc)KeyframeAnimations.posVec(2.67F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.68F, 
/* 126 */             (Vector3fc)KeyframeAnimations.posVec(2.67F, 4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.0F, 
/* 127 */             (Vector3fc)KeyframeAnimations.posVec(0.67F, 3.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(6.64F, 
/* 128 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 130 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/* 131 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.12F, 
/* 132 */             (Vector3fc)KeyframeAnimations.degreeVec(-167.5F, -17.5F, -7.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.6F, 
/* 133 */             (Vector3fc)KeyframeAnimations.degreeVec(-167.5F, -17.5F, -7.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.88F, 
/* 134 */             (Vector3fc)KeyframeAnimations.degreeVec(-175.0F, -17.5F, 15.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.16F, 
/* 135 */             (Vector3fc)KeyframeAnimations.degreeVec(-190.0F, -17.5F, 5.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.28F, 
/* 136 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, -5.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.68F, 
/* 137 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, -17.5F, -12.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8F, 
/* 138 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, -17.5F, -12.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.28F, 
/* 139 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, -17.5F, -12.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.88F, 
/* 140 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, -17.5F, -12.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.04F, 
/* 141 */             (Vector3fc)KeyframeAnimations.degreeVec(-81.29F, -10.64F, -14.21F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.16F, 
/* 142 */             (Vector3fc)KeyframeAnimations.degreeVec(-83.5F, -5.5F, -15.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.76F, 
/* 143 */             (Vector3fc)KeyframeAnimations.degreeVec(-62.5F, -7.5F, 5.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.92F, 
/* 144 */             (Vector3fc)KeyframeAnimations.degreeVec(-58.75F, -3.75F, 5.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.08F, 
/* 145 */             (Vector3fc)KeyframeAnimations.degreeVec(-55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.44F, 
/* 146 */             (Vector3fc)KeyframeAnimations.degreeVec(-52.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.56F, 
/* 147 */             (Vector3fc)KeyframeAnimations.degreeVec(-50.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.68F, 
/* 148 */             (Vector3fc)KeyframeAnimations.degreeVec(-52.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.0F, 
/* 149 */             (Vector3fc)KeyframeAnimations.degreeVec(-72.5F, -2.5F, 5.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.56F, 
/* 150 */             (Vector3fc)KeyframeAnimations.degreeVec(-57.5F, -4.54F, 2.99F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(6.08F, 
/* 151 */             (Vector3fc)KeyframeAnimations.degreeVec(-70.99F, -5.77F, 1.78F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(6.64F, 
/* 152 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 154 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/* 155 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.12F, 
/* 156 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -8.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.6F, 
/* 157 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -8.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.88F, 
/* 158 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.2F, 
/* 159 */             (Vector3fc)KeyframeAnimations.posVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.68F, 
/* 160 */             (Vector3fc)KeyframeAnimations.posVec(-4.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8F, 
/* 161 */             (Vector3fc)KeyframeAnimations.posVec(-4.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.28F, 
/* 162 */             (Vector3fc)KeyframeAnimations.posVec(-4.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.88F, 
/* 163 */             (Vector3fc)KeyframeAnimations.posVec(-4.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.04F, 
/* 164 */             (Vector3fc)KeyframeAnimations.posVec(-3.23F, 5.7F, 4.97F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.16F, 
/* 165 */             (Vector3fc)KeyframeAnimations.posVec(-1.49F, 2.22F, 5.25F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.76F, 
/* 166 */             (Vector3fc)KeyframeAnimations.posVec(-1.14F, 1.71F, 1.86F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.92F, 
/* 167 */             (Vector3fc)KeyframeAnimations.posVec(-1.14F, 1.21F, 3.86F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.08F, 
/* 168 */             (Vector3fc)KeyframeAnimations.posVec(-1.14F, 2.71F, 4.86F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.44F, 
/* 169 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 1.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.56F, 
/* 170 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.68F, 
/* 171 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.0F, 
/* 172 */             (Vector3fc)KeyframeAnimations.posVec(-2.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(6.64F, 
/* 173 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 175 */         })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/* 176 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.52F, 
/* 177 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.28F, 
/* 178 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.88F, 
/* 179 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.36F, 
/* 180 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.32F, 
/* 181 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.48F, 
/* 182 */             (Vector3fc)KeyframeAnimations.degreeVec(55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.6F, 
/* 183 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.0F, 
/* 184 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.8F, 
/* 185 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(6.64F, 
/* 186 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 188 */         })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/* 189 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, -63.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.52F, 
/* 190 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -56.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.2F, 
/* 191 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.68F, 
/* 192 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8F, 
/* 193 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.28F, 
/* 194 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.88F, 
/* 195 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.36F, 
/* 196 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -22.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.76F, 
/* 197 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -12.28F, 2.48F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.92F, 
/* 198 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -9.28F, 2.48F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.08F, 
/* 199 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -12.28F, 2.48F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.32F, 
/* 200 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -4.14F, 4.14F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.48F, 
/* 201 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.57F, -8.43F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.6F, 
/* 202 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.0F, 
/* 203 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.8F, 
/* 204 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(6.64F, 
/* 205 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 207 */         })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/* 208 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.52F, 
/* 209 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.28F, 
/* 210 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.88F, 
/* 211 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.36F, 
/* 212 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.84F, 
/* 213 */             (Vector3fc)KeyframeAnimations.degreeVec(20.0F, 0.0F, -17.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.0F, 
/* 214 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.68F, 
/* 215 */             (Vector3fc)KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.84F, 
/* 216 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.0F, 
/* 217 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.8F, 
/* 218 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(6.64F, 
/* 219 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 221 */         })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/* 222 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, -63.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.52F, 
/* 223 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -56.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.2F, 
/* 224 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.68F, 
/* 225 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8F, 
/* 226 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.28F, 
/* 227 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.88F, 
/* 228 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.36F, 
/* 229 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -22.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.84F, 
/* 230 */             (Vector3fc)KeyframeAnimations.posVec(-4.0F, 2.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.0F, 
/* 231 */             (Vector3fc)KeyframeAnimations.posVec(-4.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.68F, 
/* 232 */             (Vector3fc)KeyframeAnimations.posVec(-4.0F, 0.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.84F, 
/* 233 */             (Vector3fc)KeyframeAnimations.posVec(-2.0F, 2.0F, -3.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.0F, 
/* 234 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.8F, 
/* 235 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(6.64F, 
/* 236 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 238 */         })).build();
/*     */   
/* 240 */   public static final AnimationDefinition WARDEN_DIG = AnimationDefinition.Builder.withLength(5.0F)
/* 241 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/* 242 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/* 243 */             (Vector3fc)KeyframeAnimations.degreeVec(4.13441F, 0.94736F, 1.2694F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5F, 
/* 244 */             (Vector3fc)KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7083F, 
/* 245 */             (Vector3fc)KeyframeAnimations.degreeVec(54.45407F, -13.53935F, -18.14183F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0417F, 
/* 246 */             (Vector3fc)KeyframeAnimations.degreeVec(59.46442F, -10.8885F, 35.7954F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.3333F, 
/* 247 */             (Vector3fc)KeyframeAnimations.degreeVec(82.28261F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.625F, 
/* 248 */             (Vector3fc)KeyframeAnimations.degreeVec(53.23606F, 10.04715F, -29.72932F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.2083F, 
/* 249 */             (Vector3fc)KeyframeAnimations.degreeVec(-17.71739F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.5417F, 
/* 250 */             (Vector3fc)KeyframeAnimations.degreeVec(112.28261F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.6667F, 
/* 251 */             (Vector3fc)KeyframeAnimations.degreeVec(116.06889F, 5.11581F, -24.50117F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.8333F, 
/* 252 */             (Vector3fc)KeyframeAnimations.degreeVec(121.56244F, -4.17248F, 19.57737F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.0417F, 
/* 253 */             (Vector3fc)KeyframeAnimations.degreeVec(138.5689F, 5.11581F, -24.50117F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.25F, 
/* 254 */             (Vector3fc)KeyframeAnimations.degreeVec(144.06244F, -4.17248F, 19.57737F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.375F, 
/* 255 */             (Vector3fc)KeyframeAnimations.degreeVec(147.28261F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.625F, 
/* 256 */             (Vector3fc)KeyframeAnimations.degreeVec(147.28261F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.875F, 
/* 257 */             (Vector3fc)KeyframeAnimations.degreeVec(134.36221F, 8.81113F, -8.90172F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.0417F, 
/* 258 */             (Vector3fc)KeyframeAnimations.degreeVec(132.05966F, -8.35927F, 9.70506F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.25F, 
/* 259 */             (Vector3fc)KeyframeAnimations.degreeVec(134.36221F, 8.81113F, -8.90172F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.5F, 
/* 260 */             (Vector3fc)KeyframeAnimations.degreeVec(147.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 262 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/* 263 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5F, 
/* 264 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -16.48454F, -6.5784F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7083F, 
/* 265 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -16.48454F, -6.5784F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0417F, 
/* 266 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -16.97F, -7.11F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.625F, 
/* 267 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -13.97F, -7.11F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.2083F, 
/* 268 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -11.48454F, -0.5784F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.5417F, 
/* 269 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -16.48454F, -6.5784F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.6667F, 
/* 270 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -20.27F, -5.42F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.375F, 
/* 271 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -21.48454F, -5.5784F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.0417F, 
/* 272 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -22.48454F, -5.5784F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.5F, 
/* 273 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -40.0F, -8.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 275 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 276 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.6667F, 
/* 277 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.2083F, 
/* 278 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.75F, 
/* 279 */             (Vector3fc)KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.375F, 
/* 280 */             (Vector3fc)KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.5417F, 
/* 281 */             (Vector3fc)KeyframeAnimations.degreeVec(67.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.375F, 
/* 282 */             (Vector3fc)KeyframeAnimations.degreeVec(67.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 284 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 285 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.375F, 
/* 286 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 288 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 289 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5F, 
/* 290 */             (Vector3fc)KeyframeAnimations.degreeVec(-101.8036F, -21.29587F, 30.61478F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7083F, 
/* 291 */             (Vector3fc)KeyframeAnimations.degreeVec(-101.8036F, -21.29587F, 30.61478F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/* 292 */             (Vector3fc)KeyframeAnimations.degreeVec(48.7585F, -17.61941F, 9.9865F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.1667F, 
/* 293 */             (Vector3fc)KeyframeAnimations.degreeVec(48.7585F, -17.61941F, 9.9865F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.4583F, 
/* 294 */             (Vector3fc)KeyframeAnimations.degreeVec(-101.8036F, -21.29587F, 30.61478F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.75F, 
/* 295 */             (Vector3fc)KeyframeAnimations.degreeVec(-89.04994F, -4.19657F, -1.47845F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.2083F, 
/* 296 */             (Vector3fc)KeyframeAnimations.degreeVec(-158.30728F, 3.7152F, -1.52352F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.5417F, 
/* 297 */             (Vector3fc)KeyframeAnimations.degreeVec(-89.04994F, -4.19657F, -1.47845F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.375F, 
/* 298 */             (Vector3fc)KeyframeAnimations.degreeVec(-120.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 300 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 301 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7083F, 
/* 302 */             (Vector3fc)KeyframeAnimations.posVec(2.22F, 0.0F, 0.86F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/* 303 */             (Vector3fc)KeyframeAnimations.posVec(3.12F, 0.0F, 4.29F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.2083F, 
/* 304 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.375F, 
/* 305 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 307 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/* 308 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.2917F, 
/* 309 */             (Vector3fc)KeyframeAnimations.degreeVec(-63.89288F, -0.52011F, 2.09491F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5F, 
/* 310 */             (Vector3fc)KeyframeAnimations.degreeVec(-63.89288F, -0.52011F, 2.09491F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7083F, 
/* 311 */             (Vector3fc)KeyframeAnimations.degreeVec(-62.87857F, 15.15061F, 9.97445F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.9167F, 
/* 312 */             (Vector3fc)KeyframeAnimations.degreeVec(-86.93642F, 17.45026F, 4.05284F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.1667F, 
/* 313 */             (Vector3fc)KeyframeAnimations.degreeVec(-86.93642F, 17.45026F, 4.05284F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.4583F, 
/* 314 */             (Vector3fc)KeyframeAnimations.degreeVec(-86.93642F, 17.45026F, 4.05284F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.6667F, 
/* 315 */             (Vector3fc)KeyframeAnimations.degreeVec(63.0984F, 8.83573F, -8.71284F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8333F, 
/* 316 */             (Vector3fc)KeyframeAnimations.degreeVec(35.5984F, 8.83573F, -8.71284F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.2083F, 
/* 317 */             (Vector3fc)KeyframeAnimations.degreeVec(-153.27473F, -0.02953F, 3.5235F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.5417F, 
/* 318 */             (Vector3fc)KeyframeAnimations.degreeVec(-87.07754F, -0.02625F, 3.132F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.375F, 
/* 319 */             (Vector3fc)KeyframeAnimations.degreeVec(-120.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 321 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 322 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5F, 
/* 323 */             (Vector3fc)KeyframeAnimations.posVec(-0.28F, 5.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7083F, 
/* 324 */             (Vector3fc)KeyframeAnimations.posVec(-1.51F, 4.35F, 4.33F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.9167F, 
/* 325 */             (Vector3fc)KeyframeAnimations.posVec(-0.6F, 3.61F, 4.63F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.1667F, 
/* 326 */             (Vector3fc)KeyframeAnimations.posVec(-0.6F, 3.61F, 0.63F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.6667F, 
/* 327 */             (Vector3fc)KeyframeAnimations.posVec(-2.85F, -0.1F, 3.33F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.2083F, 
/* 328 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.375F, 
/* 329 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 331 */         })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 332 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5F, 
/* 333 */             (Vector3fc)KeyframeAnimations.degreeVec(113.27F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7083F, 
/* 334 */             (Vector3fc)KeyframeAnimations.degreeVec(113.27F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.3333F, 
/* 335 */             (Vector3fc)KeyframeAnimations.degreeVec(113.27F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.5833F, 
/* 336 */             (Vector3fc)KeyframeAnimations.degreeVec(182.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.8333F, 
/* 337 */             (Vector3fc)KeyframeAnimations.degreeVec(120.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.0833F, 
/* 338 */             (Vector3fc)KeyframeAnimations.degreeVec(182.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.2917F, 
/* 339 */             (Vector3fc)KeyframeAnimations.degreeVec(120.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.5F, 
/* 340 */             (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 342 */         })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 343 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5F, 
/* 344 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -13.98F, -2.37F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7083F, 
/* 345 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -13.98F, -2.37F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.3333F, 
/* 346 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -13.98F, -2.37F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.5833F, 
/* 347 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -7.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.8333F, 
/* 348 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -9.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.0833F, 
/* 349 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -16.71F, -3.69F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.2917F, 
/* 350 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -28.0F, -5.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 352 */         })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 353 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5F, 
/* 354 */             (Vector3fc)KeyframeAnimations.degreeVec(114.98F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7083F, 
/* 355 */             (Vector3fc)KeyframeAnimations.degreeVec(114.98F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.3333F, 
/* 356 */             (Vector3fc)KeyframeAnimations.degreeVec(114.98F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.5833F, 
/* 357 */             (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.8333F, 
/* 358 */             (Vector3fc)KeyframeAnimations.degreeVec(172.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.0833F, 
/* 359 */             (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.2917F, 
/* 360 */             (Vector3fc)KeyframeAnimations.degreeVec(197.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.5F, 
/* 361 */             (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 363 */         })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 364 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5F, 
/* 365 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -14.01F, -2.35F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7083F, 
/* 366 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -14.01F, -2.35F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.3333F, 
/* 367 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -14.01F, -2.35F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.5833F, 
/* 368 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -5.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.8333F, 
/* 369 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -7.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.0833F, 
/* 370 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -15.5F, -3.76F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.2917F, 
/* 371 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -28.0F, -5.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 373 */         })).build();
/*     */   
/* 375 */   public static final AnimationDefinition WARDEN_ROAR = AnimationDefinition.Builder.withLength(4.2F)
/* 376 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 377 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.24F, 
/* 378 */             (Vector3fc)KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.6F, 
/* 379 */             (Vector3fc)KeyframeAnimations.degreeVec(32.5F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.84F, 
/* 380 */             (Vector3fc)KeyframeAnimations.degreeVec(38.33F, 0.0F, 2.99F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.08F, 
/* 381 */             (Vector3fc)KeyframeAnimations.degreeVec(40.97F, 0.0F, -4.3F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.36F, 
/* 382 */             (Vector3fc)KeyframeAnimations.degreeVec(44.41F, 0.0F, 6.29F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.0F, 
/* 383 */             (Vector3fc)KeyframeAnimations.degreeVec(47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.2F, 
/* 384 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 386 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 387 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.24F, 
/* 388 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.6F, 
/* 389 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -3.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.0F, 
/* 390 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -3.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.2F, 
/* 391 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 393 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 394 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.24F, 
/* 395 */             (Vector3fc)KeyframeAnimations.degreeVec(-32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.6F, 
/* 396 */             (Vector3fc)KeyframeAnimations.degreeVec(-32.5F, 0.0F, -27.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8F, 
/* 397 */             (Vector3fc)KeyframeAnimations.degreeVec(-32.5F, 0.0F, 26.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.04F, 
/* 398 */             (Vector3fc)KeyframeAnimations.degreeVec(-32.5F, 0.0F, -27.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.44F, 
/* 399 */             (Vector3fc)KeyframeAnimations.degreeVec(-32.5F, 0.0F, 26.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.84F, 
/* 400 */             (Vector3fc)KeyframeAnimations.degreeVec(-5.0F, 0.0F, -12.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.2F, 
/* 401 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 403 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 404 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.24F, 
/* 405 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.6F, 
/* 406 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -2.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.2F, 
/* 407 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -2.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.48F, 
/* 408 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -2.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.2F, 
/* 409 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 411 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 412 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.72F, 
/* 413 */             (Vector3fc)KeyframeAnimations.degreeVec(-120.0F, 0.0F, -20.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.24F, 
/* 414 */             (Vector3fc)KeyframeAnimations.degreeVec(-77.5F, 3.75F, 15.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.48F, 
/* 415 */             (Vector3fc)KeyframeAnimations.degreeVec(67.5F, -32.5F, 20.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.48F, 
/* 416 */             (Vector3fc)KeyframeAnimations.degreeVec(37.5F, -32.5F, 25.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.88F, 
/* 417 */             (Vector3fc)KeyframeAnimations.degreeVec(27.6F, -17.1F, 32.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.2F, 
/* 418 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 420 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 421 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.72F, 
/* 422 */             (Vector3fc)KeyframeAnimations.posVec(3.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.48F, 
/* 423 */             (Vector3fc)KeyframeAnimations.posVec(4.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.48F, 
/* 424 */             (Vector3fc)KeyframeAnimations.posVec(4.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.2F, 
/* 425 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 427 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 428 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.72F, 
/* 429 */             (Vector3fc)KeyframeAnimations.degreeVec(-125.0F, 0.0F, 20.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.24F, 
/* 430 */             (Vector3fc)KeyframeAnimations.degreeVec(-76.25F, -17.5F, -7.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.48F, 
/* 431 */             (Vector3fc)KeyframeAnimations.degreeVec(62.5F, 42.5F, -12.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.48F, 
/* 432 */             (Vector3fc)KeyframeAnimations.degreeVec(37.5F, 27.5F, -27.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.88F, 
/* 433 */             (Vector3fc)KeyframeAnimations.degreeVec(25.0F, 18.4F, -30.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.2F, 
/* 434 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 436 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 437 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.72F, 
/* 438 */             (Vector3fc)KeyframeAnimations.posVec(-3.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.48F, 
/* 439 */             (Vector3fc)KeyframeAnimations.posVec(-4.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.48F, 
/* 440 */             (Vector3fc)KeyframeAnimations.posVec(-4.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.2F, 
/* 441 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 443 */         })).build();
/*     */   
/* 445 */   public static final AnimationDefinition WARDEN_SNIFF = AnimationDefinition.Builder.withLength(4.16F)
/* 446 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 447 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.56F, 
/* 448 */             (Vector3fc)KeyframeAnimations.degreeVec(17.5F, 32.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.96F, 
/* 449 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 32.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.2F, 
/* 450 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.8F, 
/* 451 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, -30.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.32F, 
/* 452 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 454 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 455 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.68F, 
/* 456 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 40.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.96F, 
/* 457 */             (Vector3fc)KeyframeAnimations.degreeVec(-22.5F, 40.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.24F, 
/* 458 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.52F, 
/* 459 */             (Vector3fc)KeyframeAnimations.degreeVec(-35.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.76F, 
/* 460 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.28F, 
/* 461 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.88F, 
/* 462 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.32F, 
/* 463 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 465 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 466 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.96F, 
/* 467 */             (Vector3fc)KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.2F, 
/* 468 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.76F, 
/* 469 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.32F, 
/* 470 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 472 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 473 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.96F, 
/* 474 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.2F, 
/* 475 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.76F, 
/* 476 */             (Vector3fc)KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.32F, 
/* 477 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 479 */         })).build();
/*     */   
/* 481 */   public static final AnimationDefinition WARDEN_ATTACK = AnimationDefinition.Builder.withLength(0.33333F)
/* 482 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 483 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.0417F, 
/* 484 */             (Vector3fc)KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.2083F, 
/* 485 */             (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3333F, 
/* 486 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 488 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 489 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.0417F, 
/* 490 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.2083F, 
/* 491 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3333F, 
/* 492 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 494 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 495 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.0417F, 
/* 496 */             (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/* 497 */             (Vector3fc)KeyframeAnimations.degreeVec(-30.17493F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3333F, 
/* 498 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 500 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 501 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.0417F, 
/* 502 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/* 503 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -2.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3333F, 
/* 504 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 506 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 507 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.0417F, 
/* 508 */             (Vector3fc)KeyframeAnimations.degreeVec(-120.36119F, 40.78947F, -20.94102F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.1667F, 
/* 509 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3333F, 
/* 510 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 512 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 513 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.0417F, 
/* 514 */             (Vector3fc)KeyframeAnimations.posVec(4.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.1667F, 
/* 515 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3333F, 
/* 516 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 518 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 519 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.0417F, 
/* 520 */             (Vector3fc)KeyframeAnimations.degreeVec(-120.36119F, -40.78947F, 20.94102F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.1667F, 
/* 521 */             (Vector3fc)KeyframeAnimations.degreeVec(-61.1632F, 42.85882F, 11.52421F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3333F, 
/* 522 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 524 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 525 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.0417F, 
/* 526 */             (Vector3fc)KeyframeAnimations.posVec(-4.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.1667F, 
/* 527 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3333F, 
/* 528 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 530 */         })).build();
/*     */   
/* 532 */   public static final AnimationDefinition WARDEN_SONIC_BOOM = AnimationDefinition.Builder.withLength(3.0F)
/* 533 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 534 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0833F, 
/* 535 */             (Vector3fc)KeyframeAnimations.degreeVec(47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.625F, 
/* 536 */             (Vector3fc)KeyframeAnimations.degreeVec(55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.9167F, 
/* 537 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.0F, 
/* 538 */             (Vector3fc)KeyframeAnimations.degreeVec(-32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.4583F, 
/* 539 */             (Vector3fc)KeyframeAnimations.degreeVec(-32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.7083F, 
/* 540 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.875F, 
/* 541 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 543 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 544 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0833F, 
/* 545 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.625F, 
/* 546 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -4.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.9167F, 
/* 547 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.7083F, 
/* 548 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.875F, 
/* 549 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 551 */         })).addAnimation("right_ribcage", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 552 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5417F, 
/* 553 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.7917F, 
/* 554 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.875F, 
/* 555 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 125.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.5F, 
/* 556 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 125.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.6667F, 
/* 557 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 559 */         })).addAnimation("left_ribcage", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 560 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5417F, 
/* 561 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.7917F, 
/* 562 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.875F, 
/* 563 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -125.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.5F, 
/* 564 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -125.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.6667F, 
/* 565 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 567 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 568 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/* 569 */             (Vector3fc)KeyframeAnimations.degreeVec(67.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.75F, 
/* 570 */             (Vector3fc)KeyframeAnimations.degreeVec(80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.9167F, 
/* 571 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.5F, 
/* 572 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.7083F, 
/* 573 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.875F, 
/* 574 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 576 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 577 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.9167F, 
/* 578 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.5F, 
/* 579 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.7083F, 
/* 580 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.875F, 
/* 581 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 583 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 584 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.875F, 
/* 585 */             (Vector3fc)KeyframeAnimations.degreeVec(-42.28659F, -32.69813F, -5.00825F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.1667F, 
/* 586 */             (Vector3fc)KeyframeAnimations.degreeVec(-29.83757F, -35.39626F, -45.28089F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.3333F, 
/* 587 */             (Vector3fc)KeyframeAnimations.degreeVec(-29.83757F, -35.39626F, -45.28089F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.6667F, 
/* 588 */             (Vector3fc)KeyframeAnimations.degreeVec(-72.28659F, -32.69813F, -5.00825F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8333F, 
/* 589 */             (Vector3fc)KeyframeAnimations.degreeVec(35.26439F, -30.0F, 35.26439F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.9167F, 
/* 590 */             (Vector3fc)KeyframeAnimations.degreeVec(73.75484F, -13.0931F, 19.20518F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.5F, 
/* 591 */             (Vector3fc)KeyframeAnimations.degreeVec(73.75484F, -13.0931F, 19.20518F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.75F, 
/* 592 */             (Vector3fc)KeyframeAnimations.degreeVec(58.20713F, -21.1064F, 28.7261F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.0F, 
/* 593 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 595 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 596 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8333F, 
/* 597 */             (Vector3fc)KeyframeAnimations.posVec(3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.75F, 
/* 598 */             (Vector3fc)KeyframeAnimations.posVec(3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.0F, 
/* 599 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 601 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 602 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.875F, 
/* 603 */             (Vector3fc)KeyframeAnimations.degreeVec(-33.80694F, 32.31058F, 6.87997F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.1667F, 
/* 604 */             (Vector3fc)KeyframeAnimations.degreeVec(-17.87827F, 34.62115F, 49.02433F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.3333F, 
/* 605 */             (Vector3fc)KeyframeAnimations.degreeVec(-17.87827F, 34.62115F, 49.02433F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.6667F, 
/* 606 */             (Vector3fc)KeyframeAnimations.degreeVec(-51.30694F, 32.31058F, 6.87997F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8333F, 
/* 607 */             (Vector3fc)KeyframeAnimations.degreeVec(35.26439F, 30.0F, -35.26439F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.9167F, 
/* 608 */             (Vector3fc)KeyframeAnimations.degreeVec(73.75484F, 13.0931F, -19.20518F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.5F, 
/* 609 */             (Vector3fc)KeyframeAnimations.degreeVec(73.75484F, 13.0931F, -19.20518F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.75F, 
/* 610 */             (Vector3fc)KeyframeAnimations.degreeVec(58.20713F, 21.1064F, -28.7261F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.0F, 
/* 611 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 613 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 614 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8333F, 
/* 615 */             (Vector3fc)KeyframeAnimations.posVec(-3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.75F, 
/* 616 */             (Vector3fc)KeyframeAnimations.posVec(-3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.0F, 
/* 617 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 619 */         })).build();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/animation/definitions/WardenAnimation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */