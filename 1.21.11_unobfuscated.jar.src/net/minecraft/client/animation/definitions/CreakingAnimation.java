/*     */ package net.minecraft.client.animation.definitions;
/*     */ 
/*     */ import net.minecraft.client.animation.AnimationChannel;
/*     */ import net.minecraft.client.animation.AnimationDefinition;
/*     */ import net.minecraft.client.animation.Keyframe;
/*     */ import net.minecraft.client.animation.KeyframeAnimations;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class CreakingAnimation
/*     */ {
/*  11 */   public static final AnimationDefinition CREAKING_WALK = AnimationDefinition.Builder.withLength(1.125F).looping()
/*  12 */     .addAnimation("upper_body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  13 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(26.8802F, -23.399F, -9.0616F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  14 */             (Vector3fc)KeyframeAnimations.degreeVec(-2.2093F, 5.9119F, 0.0675F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5417F, 
/*  15 */             (Vector3fc)KeyframeAnimations.degreeVec(23.0778F, 14.2906F, 4.6066F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/*  16 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.875F, 
/*  17 */             (Vector3fc)KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  18 */             (Vector3fc)KeyframeAnimations.degreeVec(26.8802F, -23.399F, -9.0616F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  20 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  21 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0417F, 
/*  22 */             (Vector3fc)KeyframeAnimations.degreeVec(-17.5F, -62.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0833F, 
/*  23 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  24 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, 
/*  25 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  26 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0417F, 
/*  27 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0833F, 
/*  28 */             (Vector3fc)KeyframeAnimations.degreeVec(-37.1532F, 81.1131F, -28.3621F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  29 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  31 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  32 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  33 */             (Vector3fc)KeyframeAnimations.degreeVec(-32.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.875F, 
/*  34 */             (Vector3fc)KeyframeAnimations.degreeVec(12.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  35 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  37 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  38 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  39 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5417F, 
/*  40 */             (Vector3fc)KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  41 */             (Vector3fc)KeyframeAnimations.degreeVec(-9.0923F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7917F, 
/*  42 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.137F, -66.7758F, 13.9603F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/*  43 */             (Vector3fc)KeyframeAnimations.degreeVec(-9.0923F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/*  44 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  45 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  47 */         })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  48 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  49 */             (Vector3fc)KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  50 */             (Vector3fc)KeyframeAnimations.degreeVec(49.8924F, -3.8282F, 3.2187F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  51 */             (Vector3fc)KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/*  52 */             (Vector3fc)KeyframeAnimations.degreeVec(-56.5613F, -12.2403F, -8.7374F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9167F, 
/*  53 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  54 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  56 */         })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  57 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  58 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.1846F, 0.5979F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  59 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.0665F, -2.2177F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  60 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.3563F, -4.3474F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/*  61 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.1047F, -1.6556F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9167F, 
/*  62 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  63 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  65 */         })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  66 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(25.5305F, 11.3125F, 5.3525F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  67 */             (Vector3fc)KeyframeAnimations.degreeVec(-49.5628F, 7.3556F, 6.7933F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  68 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, 
/*  69 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9167F, 
/*  70 */             (Vector3fc)KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0417F, 
/*  71 */             (Vector3fc)KeyframeAnimations.degreeVec(55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  72 */             (Vector3fc)KeyframeAnimations.degreeVec(25.5305F, 11.3125F, 5.3525F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  74 */         })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  75 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.9674F, -3.6578F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  76 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.2979F, -0.9411F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  77 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.3F, -0.94F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, 
/*  78 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.3F, 1.06F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  79 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.9674F, -3.6578F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  81 */         })).build();
/*     */   
/*  83 */   public static final AnimationDefinition CREAKING_ATTACK = AnimationDefinition.Builder.withLength(0.7083F).looping()
/*  84 */     .addAnimation("upper_body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  85 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0833F, 
/*  86 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 45.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  87 */             (Vector3fc)KeyframeAnimations.degreeVec(-115.0F, 67.5F, -90.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  88 */             (Vector3fc)KeyframeAnimations.degreeVec(67.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5417F, 
/*  89 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 45.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/*  90 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  92 */         })).addAnimation("upper_body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  93 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0833F, 
/*  94 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  95 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -2.7716F, -1.1481F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  96 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5417F, 
/*  97 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/*  98 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 100 */         })).addAnimation("upper_body", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] {
/* 101 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/* 102 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 104 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 105 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 106 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -45.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 107 */             (Vector3fc)KeyframeAnimations.degreeVec(-11.25F, -45.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/* 108 */             (Vector3fc)KeyframeAnimations.degreeVec(-117.3939F, 76.6331F, -130.1483F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/* 109 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, -45.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 110 */             (Vector3fc)KeyframeAnimations.degreeVec(60.0F, -45.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/* 111 */             (Vector3fc)KeyframeAnimations.degreeVec(60.0F, -45.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 112 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -45.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/* 113 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 115 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 116 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 117 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/* 118 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 119 */             (Vector3fc)KeyframeAnimations.posVec(0.3827F, 0.5133F, -0.7682F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/* 120 */             (Vector3fc)KeyframeAnimations.posVec(0.3827F, 0.5133F, -0.7682F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 121 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/* 122 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 124 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] {
/* 125 */           new Keyframe(0.1667F, (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/* 126 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 127 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.2999999523162842D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 128 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 130 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 131 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 132 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 133 */             (Vector3fc)KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, 
/* 134 */             (Vector3fc)KeyframeAnimations.degreeVec(55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 135 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/* 136 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 138 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 139 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 140 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 141 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/* 142 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 144 */         })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 145 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 146 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 147 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/* 148 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 150 */         })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 151 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 152 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 153 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/* 154 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 156 */         })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 157 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 158 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 45.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 159 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 45.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/* 160 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 162 */         })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 163 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 164 */             (Vector3fc)KeyframeAnimations.posVec(0.7071F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 165 */             (Vector3fc)KeyframeAnimations.posVec(0.7071F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/* 166 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 168 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 169 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 170 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 171 */             (Vector3fc)KeyframeAnimations.degreeVec(10.3453F, 14.7669F, 2.664F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, 
/* 172 */             (Vector3fc)KeyframeAnimations.degreeVec(57.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 173 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/* 174 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 176 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 177 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/* 178 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 180 */         })).build();
/*     */   
/* 182 */   public static final AnimationDefinition CREAKING_INVULNERABLE = AnimationDefinition.Builder.withLength(0.2917F)
/* 183 */     .addAnimation("upper_body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 184 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0833F, 
/* 185 */             (Vector3fc)KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 186 */             (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 187 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 189 */         })).addAnimation("upper_body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 190 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0833F, 
/* 191 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 192 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 194 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 195 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0833F, 
/* 196 */             (Vector3fc)KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 197 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 198 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 200 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 201 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 202 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 204 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 205 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0833F, 
/* 206 */             (Vector3fc)KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 207 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 208 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 210 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 211 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 212 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 214 */         })).build();
/*     */   
/* 216 */   public static final AnimationDefinition CREAKING_DEATH = AnimationDefinition.Builder.withLength(2.25F)
/* 217 */     .addAnimation("upper_body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/* 218 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0833F, 
/* 219 */             (Vector3fc)KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 220 */             (Vector3fc)KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/* 221 */             (Vector3fc)KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/* 222 */             (Vector3fc)KeyframeAnimations.degreeVec(16.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.6667F, 
/* 223 */             (Vector3fc)KeyframeAnimations.degreeVec(29.0814F, 62.5516F, 26.5771F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 224 */             (Vector3fc)KeyframeAnimations.degreeVec(12.2115F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 225 */             (Vector3fc)KeyframeAnimations.degreeVec(10.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0417F, 
/* 226 */             (Vector3fc)KeyframeAnimations.degreeVec(-47.64F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/* 227 */             (Vector3fc)KeyframeAnimations.degreeVec(21.96F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 228 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/* 229 */             (Vector3fc)KeyframeAnimations.degreeVec(17.3266F, 7.9022F, -0.1381F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 231 */         })).addAnimation("upper_body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 232 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0833F, 
/* 233 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.557F, 1.2659F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 234 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -2.0889F, -0.3493F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/* 235 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 237 */         })).addAnimation("upper_body", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] {
/* 238 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0833F, 
/* 239 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.100000023841858D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 240 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 0.8999999761581421D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/* 241 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 243 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 244 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/* 245 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 246 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 247 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5417F, 
/* 248 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/* 249 */             (Vector3fc)KeyframeAnimations.degreeVec(-12.1479F, -34.3927F, 6.9326F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/* 250 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 252 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 253 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/* 254 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 256 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 257 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/* 258 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 259 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/* 260 */             (Vector3fc)KeyframeAnimations.degreeVec(-4.4444F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.875F, 
/* 261 */             (Vector3fc)KeyframeAnimations.degreeVec(-26.7402F, -78.831F, 26.3025F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9583F, 
/* 262 */             (Vector3fc)KeyframeAnimations.degreeVec(-5.5556F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 263 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 265 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 266 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/* 267 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 269 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/* 270 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0833F, 
/* 271 */             (Vector3fc)KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/* 272 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 273 */             (Vector3fc)KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5417F, 
/* 274 */             (Vector3fc)KeyframeAnimations.degreeVec(5.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/* 275 */             (Vector3fc)KeyframeAnimations.degreeVec(-67.4168F, -12.9552F, -8.0231F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.6667F, 
/* 276 */             (Vector3fc)KeyframeAnimations.degreeVec(8.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 277 */             (Vector3fc)KeyframeAnimations.degreeVec(10.773F, -29.5608F, -5.3627F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 278 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7917F, 
/* 279 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/* 280 */             (Vector3fc)KeyframeAnimations.degreeVec(12.9625F, 39.2735F, 8.2901F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9167F, 
/* 281 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 283 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 284 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/* 285 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 287 */         })).build();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/animation/definitions/CreakingAnimation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */