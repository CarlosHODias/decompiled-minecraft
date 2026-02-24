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
/*     */ public class CamelAnimation
/*     */ {
/*  13 */   public static final AnimationDefinition CAMEL_WALK = AnimationDefinition.Builder.withLength(1.5F)
/*  14 */     .looping()
/*  15 */     .addAnimation("root", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  16 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/*  17 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5F, 
/*  18 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/*  20 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  21 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.375F, 
/*  22 */             (Vector3fc)KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.75F, 
/*  23 */             (Vector3fc)KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.125F, 
/*  24 */             (Vector3fc)KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5F, 
/*  25 */             (Vector3fc)KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/*  27 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  28 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.75F, 
/*  29 */             (Vector3fc)KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5F, 
/*  30 */             (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/*  32 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  33 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4583F, 
/*  34 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.75F, 
/*  35 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5F, 
/*  36 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/*  38 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  39 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.75F, 
/*  40 */             (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5F, 
/*  41 */             (Vector3fc)KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/*  43 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  44 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.75F, 
/*  45 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.2083F, 
/*  46 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5F, 
/*  47 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/*  49 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  50 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-20.4F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.75F, 
/*  51 */             (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.375F, 
/*  52 */             (Vector3fc)KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/*  53 */             (Vector3fc)KeyframeAnimations.degreeVec(-20.4F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  55 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  56 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.21F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.75F, 
/*  57 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0833F, 
/*  58 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.375F, 
/*  59 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/*  60 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.21F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  62 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  63 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.625F, 
/*  64 */             (Vector3fc)KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5F, 
/*  65 */             (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/*  67 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  68 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.375F, 
/*  69 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.625F, 
/*  70 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5F, 
/*  71 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/*  73 */         })).addAnimation("left_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  74 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.375F, 
/*  75 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.75F, 
/*  76 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.125F, 
/*  77 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5F, 
/*  78 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/*  80 */         })).addAnimation("right_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  81 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.375F, 
/*  82 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.75F, 
/*  83 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.125F, 
/*  84 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5F, 
/*  85 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/*  87 */         })).addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  88 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(15.94102F, -8.42106F, 20.94102F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.75F, 
/*  89 */             (Vector3fc)KeyframeAnimations.degreeVec(15.94102F, 8.42106F, -20.94102F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.5F, 
/*  90 */             (Vector3fc)KeyframeAnimations.degreeVec(15.94102F, -8.42106F, 20.94102F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/*  92 */         })).build();
/*     */   
/*  94 */   public static final AnimationDefinition CAMEL_SIT = AnimationDefinition.Builder.withLength(2.0F)
/*  95 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  96 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3F, 
/*  97 */             (Vector3fc)KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8F, 
/*  98 */             (Vector3fc)KeyframeAnimations.degreeVec(24.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/*  99 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 101 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 102 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3F, 
/* 103 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8F, 
/* 104 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -6.0F, 1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 105 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -19.9F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 107 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 108 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 109 */             (Vector3fc)KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 110 */             (Vector3fc)KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 111 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 113 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 114 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 115 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -2.0F, 11.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 116 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -2.0F, 11.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7F, 
/* 117 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -8.4F, 11.4F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 118 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -20.6F, 12.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 120 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 121 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 122 */             (Vector3fc)KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 123 */             (Vector3fc)KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 124 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 126 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 127 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 128 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -2.0F, 11.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 129 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -2.0F, 11.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7F, 
/* 130 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -8.4F, 11.4F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 131 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -20.6F, 12.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 133 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 134 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 135 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 136 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7F, 
/* 137 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, -3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9F, 
/* 138 */             (Vector3fc)KeyframeAnimations.degreeVec(-65.0F, -9.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 139 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 141 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 142 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 143 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 144 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7F, 
/* 145 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, -0.62F, 0.25F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9F, 
/* 146 */             (Vector3fc)KeyframeAnimations.posVec(0.5F, -11.25F, 2.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 147 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, -20.5F, 5.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 149 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 150 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 151 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 152 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7F, 
/* 153 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9F, 
/* 154 */             (Vector3fc)KeyframeAnimations.degreeVec(-65.0F, 9.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 155 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 157 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 158 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 159 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 160 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7F, 
/* 161 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, -0.62F, 0.25F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9F, 
/* 162 */             (Vector3fc)KeyframeAnimations.posVec(-0.5F, -11.25F, 2.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 163 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, -20.5F, 5.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 165 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 166 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7F, 
/* 167 */             (Vector3fc)KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 168 */             (Vector3fc)KeyframeAnimations.degreeVec(-21.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 169 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 171 */         })).addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 172 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7F, 
/* 173 */             (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9F, 
/* 174 */             (Vector3fc)KeyframeAnimations.degreeVec(80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 175 */             (Vector3fc)KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 177 */         })).build();
/*     */   
/* 179 */   public static final AnimationDefinition CAMEL_SIT_POSE = AnimationDefinition.Builder.withLength(1.0F)
/* 180 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 181 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 182 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 184 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 185 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, -19.9F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 186 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -19.9F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 188 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 189 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 190 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 192 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 193 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, -20.6F, 12.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 194 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -20.6F, 12.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 196 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 197 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 198 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 200 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 201 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, -20.6F, 12.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 202 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -20.6F, 12.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 204 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 205 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 206 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 208 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 209 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(1.0F, -20.5F, 5.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 210 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, -20.5F, 5.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 212 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 213 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 214 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 216 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 217 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(-1.0F, -20.5F, 5.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 218 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, -20.5F, 5.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 220 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 221 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 222 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 224 */         })).addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 225 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 226 */             (Vector3fc)KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 228 */         })).build();
/*     */   
/* 230 */   public static final AnimationDefinition CAMEL_STANDUP = AnimationDefinition.Builder.withLength(2.6F)
/* 231 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 232 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7F, 
/* 233 */             (Vector3fc)KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8F, 
/* 234 */             (Vector3fc)KeyframeAnimations.degreeVec(-17.83F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.3F, 
/* 235 */             (Vector3fc)KeyframeAnimations.degreeVec(-5.83F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6F, 
/* 236 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 238 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 239 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, -19.9F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7F, 
/* 240 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -19.9F, -3.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4F, 
/* 241 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -12.76F, -4.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8F, 
/* 242 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -10.1F, -4.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.3F, 
/* 243 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -2.9F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6F, 
/* 244 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 246 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 247 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 248 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1F, 
/* 249 */             (Vector3fc)KeyframeAnimations.degreeVec(-49.06F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8F, 
/* 250 */             (Vector3fc)KeyframeAnimations.degreeVec(-22.5F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3F, 
/* 251 */             (Vector3fc)KeyframeAnimations.degreeVec(-25.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6F, 
/* 252 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 254 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 255 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, -20.6F, 12.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 256 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -20.6F, 8.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1F, 
/* 257 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -7.14F, 4.42F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8F, 
/* 258 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.27F, -1.33F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3F, 
/* 259 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.27F, -0.33F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6F, 
/* 260 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 262 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 263 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 264 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1F, 
/* 265 */             (Vector3fc)KeyframeAnimations.degreeVec(-49.06F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8F, 
/* 266 */             (Vector3fc)KeyframeAnimations.degreeVec(-22.5F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3F, 
/* 267 */             (Vector3fc)KeyframeAnimations.degreeVec(-25.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6F, 
/* 268 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 270 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 271 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, -20.6F, 12.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 272 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -20.6F, 8.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1F, 
/* 273 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -7.14F, 4.42F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8F, 
/* 274 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.27F, -1.33F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3F, 
/* 275 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.27F, -0.33F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6F, 
/* 276 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 278 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 279 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.3F, 
/* 280 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.6F, 
/* 281 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1F, 
/* 282 */             (Vector3fc)KeyframeAnimations.degreeVec(-60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9F, 
/* 283 */             (Vector3fc)KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2F, 
/* 284 */             (Vector3fc)KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6F, 
/* 285 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 287 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 288 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(1.0F, -20.5F, 5.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.3F, 
/* 289 */             (Vector3fc)KeyframeAnimations.posVec(-2.0F, -20.5F, 3.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.6F, 
/* 290 */             (Vector3fc)KeyframeAnimations.posVec(-2.0F, -20.5F, 3.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1F, 
/* 291 */             (Vector3fc)KeyframeAnimations.posVec(-2.0F, -10.5F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 292 */             (Vector3fc)KeyframeAnimations.posVec(-2.0F, -0.4F, -3.9F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9F, 
/* 293 */             (Vector3fc)KeyframeAnimations.posVec(-2.0F, -4.3F, -9.8F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2F, 
/* 294 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, -2.5F, -5.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6F, 
/* 295 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 297 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 298 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.3F, 
/* 299 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.6F, 
/* 300 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1F, 
/* 301 */             (Vector3fc)KeyframeAnimations.degreeVec(-60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9F, 
/* 302 */             (Vector3fc)KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2F, 
/* 303 */             (Vector3fc)KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6F, 
/* 304 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 306 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 307 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(-1.0F, -20.5F, 5.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.3F, 
/* 308 */             (Vector3fc)KeyframeAnimations.posVec(2.0F, -20.5F, 3.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.6F, 
/* 309 */             (Vector3fc)KeyframeAnimations.posVec(2.0F, -20.5F, 3.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1F, 
/* 310 */             (Vector3fc)KeyframeAnimations.posVec(2.0F, -10.5F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 311 */             (Vector3fc)KeyframeAnimations.posVec(2.0F, -0.4F, -3.9F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9F, 
/* 312 */             (Vector3fc)KeyframeAnimations.posVec(2.0F, -4.3F, -9.8F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2F, 
/* 313 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, -2.5F, -5.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6F, 
/* 314 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 316 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 317 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.3F, 
/* 318 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8F, 
/* 319 */             (Vector3fc)KeyframeAnimations.degreeVec(55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 320 */             (Vector3fc)KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4F, 
/* 321 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 323 */         })).addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 324 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4F, 
/* 325 */             (Vector3fc)KeyframeAnimations.degreeVec(55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9F, 
/* 326 */             (Vector3fc)KeyframeAnimations.degreeVec(55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 327 */             (Vector3fc)KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6F, 
/* 328 */             (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 330 */         })).build();
/*     */   
/* 332 */   public static final AnimationDefinition CAMEL_DASH = AnimationDefinition.Builder.withLength(0.5F)
/* 333 */     .looping()
/* 334 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 335 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 336 */             (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 338 */         })).addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 339 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(67.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.125F, 
/* 340 */             (Vector3fc)KeyframeAnimations.degreeVec(112.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/* 341 */             (Vector3fc)KeyframeAnimations.degreeVec(67.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.375F, 
/* 342 */             (Vector3fc)KeyframeAnimations.degreeVec(112.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5F, 
/* 343 */             (Vector3fc)KeyframeAnimations.degreeVec(67.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 345 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 346 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.125F, 
/* 347 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/* 348 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.375F, 
/* 349 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5F, 
/* 350 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 352 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 353 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(44.97272F, 1.76749F, -1.76833F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.125F, 
/* 354 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/* 355 */             (Vector3fc)KeyframeAnimations.degreeVec(44.97272F, 1.76749F, -1.76833F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.375F, 
/* 356 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5F, 
/* 357 */             (Vector3fc)KeyframeAnimations.degreeVec(44.97272F, 1.76749F, -1.76833F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 359 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 360 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.125F, 
/* 361 */             (Vector3fc)KeyframeAnimations.degreeVec(44.97272F, -1.76749F, 1.76833F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/* 362 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.375F, 
/* 363 */             (Vector3fc)KeyframeAnimations.degreeVec(44.97272F, -1.76749F, 1.76833F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5F, 
/* 364 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 366 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 367 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.125F, 
/* 368 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/* 369 */             (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.375F, 
/* 370 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5F, 
/* 371 */             (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 373 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 374 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.125F, 
/* 375 */             (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/* 376 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.375F, 
/* 377 */             (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5F, 
/* 378 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 380 */         })).addAnimation("left_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 381 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -67.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 382 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -67.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 384 */         })).addAnimation("right_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 385 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 67.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 386 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 67.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 388 */         })).build();
/*     */   
/* 390 */   public static final AnimationDefinition CAMEL_IDLE = AnimationDefinition.Builder.withLength(4.0F)
/* 391 */     .addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 392 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/* 393 */             (Vector3fc)KeyframeAnimations.degreeVec(4.98107F, 0.43523F, -4.98107F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.0F, 
/* 394 */             (Vector3fc)KeyframeAnimations.degreeVec(4.9872F, -0.29424F, 3.36745F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.0F, 
/* 395 */             (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 397 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 398 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.0F, 
/* 399 */             (Vector3fc)KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(4.0F, 
/* 400 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 402 */         })).addAnimation("left_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 403 */           new Keyframe(2.5F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -45.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.625F, 
/* 404 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.75F, 
/* 405 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -45.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.875F, 
/* 406 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.0F, 
/* 407 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -45.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 409 */         })).addAnimation("right_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 410 */           new Keyframe(2.5F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 45.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.625F, 
/* 411 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.75F, 
/* 412 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 45.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.875F, 
/* 413 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.0F, 
/* 414 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 45.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 416 */         })).build();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/animation/definitions/CamelAnimation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */