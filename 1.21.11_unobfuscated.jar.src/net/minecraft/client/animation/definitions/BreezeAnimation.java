/*     */ package net.minecraft.client.animation.definitions;
/*     */ 
/*     */ import net.minecraft.client.animation.AnimationChannel;
/*     */ import net.minecraft.client.animation.AnimationDefinition;
/*     */ import net.minecraft.client.animation.Keyframe;
/*     */ import net.minecraft.client.animation.KeyframeAnimations;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class BreezeAnimation {
/*  10 */   public static final AnimationDefinition IDLE = AnimationDefinition.Builder.withLength(2.0F).looping()
/*  11 */     .addAnimation("wind_top", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  12 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  13 */             (Vector3fc)KeyframeAnimations.posVec(0.5F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  14 */             (Vector3fc)KeyframeAnimations.posVec(-0.5F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/*  15 */             (Vector3fc)KeyframeAnimations.posVec(-0.5F, 0.0F, 0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, 
/*  16 */             (Vector3fc)KeyframeAnimations.posVec(0.5F, 0.0F, 0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/*  17 */             (Vector3fc)KeyframeAnimations.posVec(0.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  19 */         })).addAnimation("wind_mid", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  20 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.5F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  21 */             (Vector3fc)KeyframeAnimations.posVec(-0.5F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/*  22 */             (Vector3fc)KeyframeAnimations.posVec(-0.5F, 0.0F, 0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/*  23 */             (Vector3fc)KeyframeAnimations.posVec(0.5F, 0.0F, 0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/*  24 */             (Vector3fc)KeyframeAnimations.posVec(0.5F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  26 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  27 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/*  28 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.0F, 
/*  29 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/*  31 */         })).addAnimation("rods", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  32 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/*  33 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 1080.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  35 */         })).addAnimation("rods", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  36 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/*  37 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/*  38 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  40 */         })).build();
/*     */   
/*  42 */   public static final AnimationDefinition SHOOT = AnimationDefinition.Builder.withLength(1.125F)
/*  43 */     .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  44 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  45 */             (Vector3fc)KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  46 */             (Vector3fc)KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9167F, 
/*  47 */             (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  48 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  50 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  51 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  52 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7917F, 
/*  53 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9583F, 
/*  54 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  55 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  57 */         })).addAnimation("wind_bottom", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  58 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  60 */         })).addAnimation("wind_mid", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  61 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  62 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  63 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9167F, 
/*  64 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  65 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  67 */         })).addAnimation("wind_mid", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  68 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  69 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  70 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 6.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9167F, 
/*  71 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  72 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  74 */         })).addAnimation("wind_top", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  75 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  76 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  77 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9167F, 
/*  78 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  79 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  81 */         })).addAnimation("wind_top", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  82 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  83 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/*  84 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9583F, 
/*  85 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  86 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  88 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  89 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  90 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  91 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9167F, 
/*  92 */             (Vector3fc)KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  93 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  95 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  96 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  97 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 3.0F, 5.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/*  98 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 3.0F, 6.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9583F, 
/*  99 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 3.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/* 100 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 102 */         })).addAnimation("rods", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 103 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 104 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 106 */         })).build();
/*     */   
/* 108 */   public static final AnimationDefinition JUMP = AnimationDefinition.Builder.withLength(0.5F)
/* 109 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 110 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/* 111 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 11.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 112 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 114 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 115 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/* 116 */             (Vector3fc)KeyframeAnimations.degreeVec(-19.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 117 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 119 */         })).addAnimation("wind_body", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] {
/* 120 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/* 121 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.2999999523162842D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 122 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 124 */         })).addAnimation("wind_bottom", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 125 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 90.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 126 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 128 */         })).addAnimation("wind_bottom", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] {
/* 129 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/* 130 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.100000023841858D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 131 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 133 */         })).addAnimation("wind_mid", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 134 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 135 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 180.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 137 */         })).addAnimation("wind_mid", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 138 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, -6.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/* 139 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 140 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 142 */         })).addAnimation("wind_top", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 143 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 144 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 90.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 146 */         })).addAnimation("wind_top", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 147 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/* 148 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 149 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 151 */         })).addAnimation("rods", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 152 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 153 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 155 */         })).build();
/*     */   
/* 157 */   public static final AnimationDefinition INHALE = AnimationDefinition.Builder.withLength(2.0F)
/* 158 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 159 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 160 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 161 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 163 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 164 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 165 */             (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 166 */             (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 168 */         })).addAnimation("wind_body", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] {
/* 169 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 170 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 171 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 173 */         })).addAnimation("wind_bottom", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 174 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 175 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 90.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 177 */         })).addAnimation("wind_bottom", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] {
/* 178 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 179 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 180 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 182 */         })).addAnimation("wind_mid", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 183 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 184 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 186 */         })).addAnimation("wind_mid", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 187 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 188 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -6.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 189 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -6.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 191 */         })).addAnimation("wind_top", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 192 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 193 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 195 */         })).addAnimation("wind_top", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 196 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 197 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 198 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 200 */         })).addAnimation("rods", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 201 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 202 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 204 */         })).build();
/*     */   
/* 206 */   public static final AnimationDefinition SLIDE = AnimationDefinition.Builder.withLength(0.2F)
/* 207 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 208 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2F, 
/* 209 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -6.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 211 */         })).addAnimation("wind_mid", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 212 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2F, 
/* 213 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 215 */         })).addAnimation("wind_top", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 216 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2F, 
/* 217 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 219 */         })).build();
/*     */   
/* 221 */   public static final AnimationDefinition SLIDE_BACK = AnimationDefinition.Builder.withLength(0.1F)
/* 222 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 223 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -6.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1F, 
/* 224 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 226 */         })).addAnimation("wind_mid", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 227 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1F, 
/* 228 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 230 */         })).addAnimation("wind_top", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 231 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1F, 
/* 232 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 234 */         })).build();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/animation/definitions/BreezeAnimation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */