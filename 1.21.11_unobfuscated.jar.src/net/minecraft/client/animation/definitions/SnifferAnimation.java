/*     */ package net.minecraft.client.animation.definitions;
/*     */ 
/*     */ import net.minecraft.client.animation.AnimationChannel;
/*     */ import net.minecraft.client.animation.AnimationDefinition;
/*     */ import net.minecraft.client.animation.Keyframe;
/*     */ import net.minecraft.client.animation.KeyframeAnimations;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class SnifferAnimation
/*     */ {
/*  11 */   public static final AnimationDefinition BABY_TRANSFORM = AnimationDefinition.Builder.withLength(0.0F)
/*  12 */     .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] {
/*  13 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.scaleVec(1.2000000476837158D, 1.2000000476837158D, 1.2000000476837158D), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  15 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  16 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  18 */         })).build();
/*     */   
/*  20 */   public static final AnimationDefinition SNIFFER_SNIFFSNIFF = AnimationDefinition.Builder.withLength(8.0F)
/*  21 */     .looping()
/*  22 */     .addAnimation("nose", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] {
/*  23 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5417F, 
/*  24 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  25 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 0.5D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.6667F, 
/*  26 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 2.5D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7917F, 
/*  27 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.9167F, 
/*  28 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/*  29 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 3.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.125F, 
/*  30 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/*  31 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  33 */         })).build();
/*     */   
/*  35 */   public static final AnimationDefinition SNIFFER_LONGSNIFF = AnimationDefinition.Builder.withLength(1.0F)
/*  36 */     .addAnimation("nose", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] {
/*  37 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.0833F, 
/*  38 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 0.699999988079071D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.125F, 
/*  39 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 3.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/*  40 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 3.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7083F, 
/*  41 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 4.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.8333F, 
/*  42 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/*  43 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/*  45 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  46 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  47 */             (Vector3fc)KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.875F, 
/*  48 */             (Vector3fc)KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/*  49 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  51 */         })).build();
/*     */   
/*  53 */   public static final AnimationDefinition SNIFFER_WALK = AnimationDefinition.Builder.withLength(2.0F)
/*  54 */     .looping()
/*  55 */     .addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  56 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  57 */             (Vector3fc)KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/*  58 */             (Vector3fc)KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/*  59 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/*  60 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  62 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  63 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  64 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/*  65 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/*  66 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/*  67 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  69 */         })).addAnimation("right_mid_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  70 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-7.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  71 */             (Vector3fc)KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.3333F, 
/*  72 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/*  73 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/*  74 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, 
/*  75 */             (Vector3fc)KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/*  76 */             (Vector3fc)KeyframeAnimations.degreeVec(-7.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  78 */         })).addAnimation("right_mid_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  79 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.67F, -0.67F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  80 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.3333F, 
/*  81 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/*  82 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/*  83 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9167F, 
/*  84 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/*  85 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.67F, -0.67F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  87 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  88 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  89 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/*  90 */             (Vector3fc)KeyframeAnimations.degreeVec(25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/*  91 */             (Vector3fc)KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/*  92 */             (Vector3fc)KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, 
/*  93 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/*  94 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  96 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  97 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  98 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/*  99 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.22F, 0.78F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3333F, 
/* 100 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/* 101 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, 
/* 102 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 103 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 105 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 106 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 107 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 108 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/* 109 */             (Vector3fc)KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 110 */             (Vector3fc)KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 112 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 113 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 114 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 115 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, 
/* 116 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 117 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 119 */         })).addAnimation("left_mid_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 120 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 121 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 122 */             (Vector3fc)KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/* 123 */             (Vector3fc)KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3333F, 
/* 124 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 125 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 127 */         })).addAnimation("left_mid_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 128 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 129 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9167F, 
/* 130 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/* 131 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3333F, 
/* 132 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 133 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 135 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 136 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 137 */             (Vector3fc)KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/* 138 */             (Vector3fc)KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 139 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 140 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/* 141 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 142 */             (Vector3fc)KeyframeAnimations.degreeVec(25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 144 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 145 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.22F, 0.78F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.3333F, 
/* 146 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/* 147 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 148 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 149 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/* 150 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 151 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.22F, 0.78F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 153 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 154 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(1.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 155 */             (Vector3fc)KeyframeAnimations.degreeVec(-1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 156 */             (Vector3fc)KeyframeAnimations.degreeVec(1.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 157 */             (Vector3fc)KeyframeAnimations.degreeVec(-1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 158 */             (Vector3fc)KeyframeAnimations.degreeVec(1.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 160 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 161 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/* 162 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/* 163 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 164 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/* 165 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.375F, 
/* 166 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 167 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 169 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 170 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.1667F, 
/* 171 */             (Vector3fc)KeyframeAnimations.degreeVec(9.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.875F, 
/* 172 */             (Vector3fc)KeyframeAnimations.degreeVec(-1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.25F, 
/* 173 */             (Vector3fc)KeyframeAnimations.degreeVec(7.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.75F, 
/* 174 */             (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.0F, 
/* 175 */             (Vector3fc)KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 177 */         })).addAnimation("left_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 178 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5F, 
/* 179 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/* 180 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5F, 
/* 181 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.0F, 
/* 182 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 184 */         })).addAnimation("right_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 185 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5F, 
/* 186 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 7.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/* 187 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5F, 
/* 188 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 7.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.0F, 
/* 189 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 191 */         })).build();
/*     */   
/* 193 */   public static final AnimationDefinition SNIFFER_SNIFF_SEARCH = AnimationDefinition.Builder.withLength(2.0F)
/* 194 */     .looping()
/* 195 */     .addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 196 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/* 197 */             (Vector3fc)KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 198 */             (Vector3fc)KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/* 199 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 200 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 202 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 203 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 204 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 205 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/* 206 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 207 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 209 */         })).addAnimation("right_mid_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 210 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-7.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 211 */             (Vector3fc)KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.3333F, 
/* 212 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 213 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/* 214 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, 
/* 215 */             (Vector3fc)KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 216 */             (Vector3fc)KeyframeAnimations.degreeVec(-7.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 218 */         })).addAnimation("right_mid_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 219 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.67F, -0.67F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 220 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.3333F, 
/* 221 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 222 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/* 223 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9167F, 
/* 224 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 225 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.67F, -0.67F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 227 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 228 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/* 229 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 230 */             (Vector3fc)KeyframeAnimations.degreeVec(25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/* 231 */             (Vector3fc)KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/* 232 */             (Vector3fc)KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, 
/* 233 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 234 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 236 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 237 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/* 238 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 239 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.22F, 0.78F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3333F, 
/* 240 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/* 241 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, 
/* 242 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 243 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 245 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 246 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 247 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 248 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/* 249 */             (Vector3fc)KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 250 */             (Vector3fc)KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 252 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 253 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 254 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 255 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, 
/* 256 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 257 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 259 */         })).addAnimation("left_mid_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 260 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 261 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 262 */             (Vector3fc)KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/* 263 */             (Vector3fc)KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3333F, 
/* 264 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 265 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 267 */         })).addAnimation("left_mid_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 268 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 269 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9167F, 
/* 270 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/* 271 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3333F, 
/* 272 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 273 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 275 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 276 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 277 */             (Vector3fc)KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/* 278 */             (Vector3fc)KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 279 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 280 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/* 281 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 282 */             (Vector3fc)KeyframeAnimations.degreeVec(25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 284 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 285 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.22F, 0.78F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.3333F, 
/* 286 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/* 287 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 288 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 289 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/* 290 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 291 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.22F, 0.78F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 293 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 294 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 295 */             (Vector3fc)KeyframeAnimations.degreeVec(1.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 296 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 297 */             (Vector3fc)KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 299 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 300 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/* 301 */             (Vector3fc)KeyframeAnimations.degreeVec(33.61503F, 11.46526F, 9.803F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.875F, 
/* 302 */             (Vector3fc)KeyframeAnimations.degreeVec(34.71128F, 17.67415F, 14.15251F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/* 303 */             (Vector3fc)KeyframeAnimations.degreeVec(37.21128F, -17.67415F, -14.15251F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.875F, 
/* 304 */             (Vector3fc)KeyframeAnimations.degreeVec(38.30529F, -21.62827F, -17.40292F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 305 */             (Vector3fc)KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 307 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 308 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 309 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 311 */         })).addAnimation("left_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 312 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 313 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -15.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 314 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 315 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -15.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 316 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 317 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -15.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 318 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, 
/* 319 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -15.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 320 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 322 */         })).addAnimation("right_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 323 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 324 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 15.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 325 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 326 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 15.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 327 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 328 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 15.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 329 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, 
/* 330 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 15.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 331 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 333 */         })).addAnimation("nose", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] { 
/* 334 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.0833F, 
/* 335 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.5D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.2083F, 
/* 336 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.375F, 
/* 337 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4583F, 
/* 338 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 2.5D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.625F, 
/* 339 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.8333F, 
/* 340 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.9167F, 
/* 341 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 2.5D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0833F, 
/* 342 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.2917F, 
/* 343 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.3333F, 
/* 344 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 2.5D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5F, 
/* 345 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.625F, 
/* 346 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.6667F, 
/* 347 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 3.5D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8333F, 
/* 348 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.0F, 
/* 349 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 351 */         })).build();
/*     */   
/* 353 */   public static final AnimationDefinition SNIFFER_DIG = AnimationDefinition.Builder.withLength(8.0F)
/* 354 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/* 355 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 356 */             (Vector3fc)KeyframeAnimations.degreeVec(1.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3333F, 
/* 357 */             (Vector3fc)KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 358 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 359 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/* 360 */             (Vector3fc)KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/* 361 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.5F, 
/* 362 */             (Vector3fc)KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(4.0F, 
/* 363 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(4.5F, 
/* 364 */             (Vector3fc)KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(5.6667F, 
/* 365 */             (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(5.8333F, 
/* 366 */             (Vector3fc)KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(6.0F, 
/* 367 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 369 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 370 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3333F, 
/* 371 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 372 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -7.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 374 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] {
/* 375 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 376 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5417F, 
/* 377 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0399999618530273D, 0.9800000190734863D, 1.0199999809265137D), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/* 378 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 380 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/* 381 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.1667F, 
/* 382 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.4167F, 
/* 383 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5F, 
/* 384 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5833F, 
/* 385 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.875F, 
/* 386 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.0833F, 
/* 387 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.5F, 
/* 388 */             (Vector3fc)KeyframeAnimations.degreeVec(47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.6667F, 
/* 389 */             (Vector3fc)KeyframeAnimations.degreeVec(38.44F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.875F, 
/* 390 */             (Vector3fc)KeyframeAnimations.degreeVec(10.95951F, 13.57454F, -14.93501F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.2083F, 
/* 391 */             (Vector3fc)KeyframeAnimations.degreeVec(47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.5833F, 
/* 392 */             (Vector3fc)KeyframeAnimations.degreeVec(55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.7917F, 
/* 393 */             (Vector3fc)KeyframeAnimations.degreeVec(4.2932F, -16.187F, 10.90042F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.125F, 
/* 394 */             (Vector3fc)KeyframeAnimations.degreeVec(47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.4167F, 
/* 395 */             (Vector3fc)KeyframeAnimations.degreeVec(54.71135F, 7.98009F, -5.56662F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.5F, 
/* 396 */             (Vector3fc)KeyframeAnimations.degreeVec(55.72895F, -6.77684F, 4.46197F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.5833F, 
/* 397 */             (Vector3fc)KeyframeAnimations.degreeVec(54.71135F, 7.98009F, -5.56662F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.6667F, 
/* 398 */             (Vector3fc)KeyframeAnimations.degreeVec(55.72895F, -6.77684F, 4.46197F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.75F, 
/* 399 */             (Vector3fc)KeyframeAnimations.degreeVec(54.71135F, 7.98009F, -5.56662F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.8333F, 
/* 400 */             (Vector3fc)KeyframeAnimations.degreeVec(55.72895F, -6.77684F, 4.46197F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.0F, 
/* 401 */             (Vector3fc)KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.75F, 
/* 402 */             (Vector3fc)KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(5.9167F, 
/* 403 */             (Vector3fc)KeyframeAnimations.degreeVec(-32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(6.25F, 
/* 404 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 406 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/* 407 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 408 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.375F, 
/* 409 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 410 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/* 411 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.875F, 
/* 412 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0833F, 
/* 413 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/* 414 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 6.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6667F, 
/* 415 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.2083F, 
/* 416 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.5833F, 
/* 417 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(4.125F, 
/* 418 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(5.0F, 
/* 419 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(5.75F, 
/* 420 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(6.0F, 
/* 421 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(6.25F, 
/* 422 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 424 */         })).addAnimation("left_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 425 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 426 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4167F, 
/* 427 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -50.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/* 428 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(5.9167F, 
/* 429 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(6.0833F, 
/* 430 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -65.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(6.3333F, 
/* 431 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 433 */         })).addAnimation("right_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 434 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 435 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4167F, 
/* 436 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 50.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/* 437 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 30.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(5.9167F, 
/* 438 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 30.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(6.0833F, 
/* 439 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 65.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(6.3333F, 
/* 440 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 30.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 442 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 443 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/* 444 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.375F, 
/* 445 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 447 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 448 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/* 449 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/* 450 */             (Vector3fc)KeyframeAnimations.posVec(-2.0F, -0.75F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.375F, 
/* 451 */             (Vector3fc)KeyframeAnimations.posVec(-4.0F, -5.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 453 */         })).addAnimation("right_mid_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 454 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 455 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4167F, 
/* 456 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 458 */         })).addAnimation("right_mid_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 459 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 460 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3333F, 
/* 461 */             (Vector3fc)KeyframeAnimations.posVec(-2.0F, -0.75F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4167F, 
/* 462 */             (Vector3fc)KeyframeAnimations.posVec(-4.0F, -5.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 464 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 465 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3333F, 
/* 466 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 467 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 469 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 470 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3333F, 
/* 471 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4167F, 
/* 472 */             (Vector3fc)KeyframeAnimations.posVec(-2.0F, -0.75F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 473 */             (Vector3fc)KeyframeAnimations.posVec(-4.0F, -5.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 475 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 476 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/* 477 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.375F, 
/* 478 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 480 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 481 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/* 482 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/* 483 */             (Vector3fc)KeyframeAnimations.posVec(2.0F, -0.75F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.375F, 
/* 484 */             (Vector3fc)KeyframeAnimations.posVec(4.0F, -5.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 486 */         })).addAnimation("left_mid_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 487 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 488 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4167F, 
/* 489 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 491 */         })).addAnimation("left_mid_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 492 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 493 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3333F, 
/* 494 */             (Vector3fc)KeyframeAnimations.posVec(2.0F, -0.75F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4167F, 
/* 495 */             (Vector3fc)KeyframeAnimations.posVec(4.0F, -5.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 497 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 498 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3333F, 
/* 499 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 500 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 502 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 503 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3333F, 
/* 504 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4167F, 
/* 505 */             (Vector3fc)KeyframeAnimations.posVec(2.0F, -0.75F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 506 */             (Vector3fc)KeyframeAnimations.posVec(4.0F, -5.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 508 */         })).build();
/*     */   
/* 510 */   public static final AnimationDefinition SNIFFER_STAND_UP = AnimationDefinition.Builder.withLength(3.0F)
/* 511 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 512 */           new Keyframe(0.25F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 513 */             (Vector3fc)KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 514 */             (Vector3fc)KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7083F, 
/* 515 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 517 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 518 */           new Keyframe(0.25F, (Vector3fc)KeyframeAnimations.posVec(0.0F, -7.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 519 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -7.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 520 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7083F, 
/* 521 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 523 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 524 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.3333F, 
/* 525 */             (Vector3fc)KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/* 526 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 527 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.375F, 
/* 528 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 530 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 531 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.375F, 
/* 532 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 534 */         })).addAnimation("left_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 535 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9167F, 
/* 536 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/* 537 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 539 */         })).addAnimation("right_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 540 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 30.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9167F, 
/* 541 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 30.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/* 542 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 544 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 545 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4583F, 
/* 546 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 548 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 549 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(-4.0F, -5.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.2083F, 
/* 550 */             (Vector3fc)KeyframeAnimations.posVec(6.0F, -5.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4583F, 
/* 551 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 553 */         })).addAnimation("right_mid_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 554 */           new Keyframe(0.0833F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5833F, 
/* 555 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 557 */         })).addAnimation("right_mid_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 558 */           new Keyframe(0.0833F, (Vector3fc)KeyframeAnimations.posVec(-4.0F, -5.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3333F, 
/* 559 */             (Vector3fc)KeyframeAnimations.posVec(6.0F, -5.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5833F, 
/* 560 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 562 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 563 */           new Keyframe(0.1667F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.6667F, 
/* 564 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 566 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 567 */           new Keyframe(0.1667F, (Vector3fc)KeyframeAnimations.posVec(-4.0F, -5.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4167F, 
/* 568 */             (Vector3fc)KeyframeAnimations.posVec(6.0F, -5.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.6667F, 
/* 569 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 571 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 572 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4583F, 
/* 573 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 575 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 576 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(4.0F, -5.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.2083F, 
/* 577 */             (Vector3fc)KeyframeAnimations.posVec(-6.0F, -5.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4583F, 
/* 578 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 580 */         })).addAnimation("left_mid_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 581 */           new Keyframe(0.0833F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5833F, 
/* 582 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 584 */         })).addAnimation("left_mid_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 585 */           new Keyframe(0.0833F, (Vector3fc)KeyframeAnimations.posVec(4.0F, -5.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3333F, 
/* 586 */             (Vector3fc)KeyframeAnimations.posVec(-6.0F, -5.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5833F, 
/* 587 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 589 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 590 */           new Keyframe(0.1667F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.6667F, 
/* 591 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 593 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 594 */           new Keyframe(0.1667F, (Vector3fc)KeyframeAnimations.posVec(4.0F, -5.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4167F, 
/* 595 */             (Vector3fc)KeyframeAnimations.posVec(-6.0F, -5.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.6667F, 
/* 596 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 598 */         })).build();
/*     */   
/* 600 */   public static final AnimationDefinition SNIFFER_BABY_FALL = AnimationDefinition.Builder.withLength(4.0F)
/* 601 */     .addAnimation("bone", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 602 */           new Keyframe(1.0F, (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5F, 
/* 603 */             (Vector3fc)KeyframeAnimations.degreeVec(-98.91F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.9583F, 
/* 604 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.7083F, 
/* 605 */             (Vector3fc)KeyframeAnimations.degreeVec(-68.28F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.9583F, 
/* 606 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 608 */         })).addAnimation("bone", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 609 */           new Keyframe(1.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 20.0F, 17.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5F, 
/* 610 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 25.19F, 20.37F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.9583F, 
/* 611 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 20.0F, 17.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.7083F, 
/* 612 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 17.06F, 11.25F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.8333F, 
/* 613 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 9.85F, 2.2F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.9583F, 
/* 614 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 616 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] {
/* 617 */           new Keyframe(1.0F, (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9583F, 
/* 618 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9167F, 
/* 619 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/* 620 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0499999523162842D, 0.949999988079071D, 1.0499999523162842D), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0833F, 
/* 621 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 623 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 624 */           new Keyframe(1.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/* 625 */             (Vector3fc)KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9583F, 
/* 626 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.75F, 
/* 627 */             (Vector3fc)KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9167F, 
/* 628 */             (Vector3fc)KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0417F, 
/* 629 */             (Vector3fc)KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.125F, 
/* 630 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 632 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 633 */           new Keyframe(1.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 7.0F, 19.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 634 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 7.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9583F, 
/* 635 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 7.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.75F, 
/* 636 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 7.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/* 637 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 639 */         })).addAnimation("left_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 640 */           new Keyframe(1.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9583F, 
/* 641 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7083F, 
/* 642 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9167F, 
/* 643 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.125F, 
/* 644 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 646 */         })).addAnimation("right_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 647 */           new Keyframe(1.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9583F, 
/* 648 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7083F, 
/* 649 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9167F, 
/* 650 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.125F, 
/* 651 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 653 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 654 */           new Keyframe(1.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 655 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, 
/* 656 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 657 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/* 658 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/* 659 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.75F, 
/* 660 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/* 661 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 663 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 664 */           new Keyframe(1.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 665 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 667 */         })).addAnimation("right_mid_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 668 */           new Keyframe(1.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.375F, 
/* 669 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.625F, 
/* 670 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.875F, 
/* 671 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.125F, 
/* 672 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.375F, 
/* 673 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.625F, 
/* 674 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/* 675 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 677 */         })).addAnimation("right_mid_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 678 */           new Keyframe(1.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 679 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 681 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 682 */           new Keyframe(1.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 683 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 684 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, 
/* 685 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 686 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/* 687 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/* 688 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/* 689 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 691 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 692 */           new Keyframe(1.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 693 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 695 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 696 */           new Keyframe(1.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 697 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, 
/* 698 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 699 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/* 700 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/* 701 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.75F, 
/* 702 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/* 703 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 705 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 706 */           new Keyframe(1.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 707 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 709 */         })).addAnimation("left_mid_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 710 */           new Keyframe(1.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.375F, 
/* 711 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.625F, 
/* 712 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.875F, 
/* 713 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.125F, 
/* 714 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.375F, 
/* 715 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.625F, 
/* 716 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/* 717 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 719 */         })).addAnimation("left_mid_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 720 */           new Keyframe(1.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 721 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 723 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 724 */           new Keyframe(1.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 725 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 726 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, 
/* 727 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 728 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/* 729 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/* 730 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/* 731 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 733 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 734 */           new Keyframe(1.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 735 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 737 */         })).build();
/*     */   
/* 739 */   public static final AnimationDefinition SNIFFER_HAPPY = AnimationDefinition.Builder.withLength(2.0F)
/* 740 */     .looping()
/* 741 */     .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 742 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 743 */             (Vector3fc)KeyframeAnimations.degreeVec(-32.00206F, 19.3546F, -11.70092F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/* 744 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5F, 
/* 745 */             (Vector3fc)KeyframeAnimations.degreeVec(-32.00206F, -19.3546F, 11.70092F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.0F, 
/* 746 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 748 */         })).addAnimation("left_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 749 */           new Keyframe(0.5F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.75F, 
/* 750 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -67.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.9583F, 
/* 751 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.125F, 
/* 752 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -67.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.2917F, 
/* 753 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 755 */         })).addAnimation("right_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 756 */           new Keyframe(0.5F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.75F, 
/* 757 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 67.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.9583F, 
/* 758 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.125F, 
/* 759 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 67.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.2917F, 
/* 760 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 762 */         })).build();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/animation/definitions/SnifferAnimation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */