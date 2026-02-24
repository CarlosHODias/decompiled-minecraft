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
/*     */ 
/*     */ 
/*     */ public class ArmadilloAnimation
/*     */ {
/*  15 */   public static final AnimationDefinition ARMADILLO_ROLL_UP = AnimationDefinition.Builder.withLength(0.5F)
/*  16 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  17 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  18 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  20 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  21 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  22 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  23 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 6.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  24 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 6.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  25 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  27 */         })).addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  28 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  29 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  31 */         })).addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  32 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  33 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  34 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  35 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  37 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  38 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  39 */             (Vector3fc)KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  40 */             (Vector3fc)KeyframeAnimations.degreeVec(-72.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  42 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  43 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  44 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  45 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.0F, 1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  46 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.0F, 1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  47 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.0F, 6.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  48 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.0F, 7.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  50 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  51 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  52 */             (Vector3fc)KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  54 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  55 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  56 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 5.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1875F, 
/*  57 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 8.0F, -3.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  58 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 8.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  59 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 8.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  60 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 3.0F, -6.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  62 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  63 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  64 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  66 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  67 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  68 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 5.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1875F, 
/*  69 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 8.0F, -3.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  70 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 8.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  71 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 8.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  72 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 3.0F, -6.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  74 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  75 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  76 */             (Vector3fc)KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  77 */             (Vector3fc)KeyframeAnimations.degreeVec(-32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  78 */             (Vector3fc)KeyframeAnimations.degreeVec(-85.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  80 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  81 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  82 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1875F, 
/*  83 */             (Vector3fc)KeyframeAnimations.posVec(-0.5F, 11.5F, 0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  84 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 9.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  85 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 9.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  86 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 2.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  88 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  89 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  90 */             (Vector3fc)KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  91 */             (Vector3fc)KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  92 */             (Vector3fc)KeyframeAnimations.degreeVec(-85.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/*  94 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  95 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  96 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1875F, 
/*  97 */             (Vector3fc)KeyframeAnimations.posVec(0.5F, 11.5F, 0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  98 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 9.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  99 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 9.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/* 100 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 2.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 102 */         })).addAnimation("cube", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 103 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 104 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/* 105 */             (Vector3fc)KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, 
/* 106 */             (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 107 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 109 */         })).addAnimation("cube", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 110 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 111 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 8.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/* 112 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 7.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 113 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 7.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/* 114 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/* 115 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, 
/* 116 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 117 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 119 */         })).build();
/*     */   
/* 121 */   public static final AnimationDefinition ARMADILLO_WALK = AnimationDefinition.Builder.withLength(1.4583F).looping()
/* 122 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 123 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/* 124 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 4.6F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.2917F, 
/* 125 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 6.81F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5F, 
/* 126 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7083F, 
/* 127 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.9583F, 
/* 128 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -4.6F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/* 129 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -6.89F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.25F, 
/* 130 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.4583F, 
/* 131 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 133 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 134 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/* 135 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5F, 
/* 136 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7083F, 
/* 137 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.9583F, 
/* 138 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.25F, 
/* 139 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.4583F, 
/* 140 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 142 */         })).addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 143 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 144 */             (Vector3fc)KeyframeAnimations.degreeVec(-9.17F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 145 */             (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/* 146 */             (Vector3fc)KeyframeAnimations.degreeVec(-8.24F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4583F, 
/* 147 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 149 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 150 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 151 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 152 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 153 */             (Vector3fc)KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 154 */             (Vector3fc)KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/* 155 */             (Vector3fc)KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4583F, 
/* 156 */             (Vector3fc)KeyframeAnimations.degreeVec(-50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 158 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 159 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 160 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 161 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 162 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/* 163 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, -0.18F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4583F, 
/* 164 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 166 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 167 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 168 */             (Vector3fc)KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5417F, 
/* 169 */             (Vector3fc)KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/* 170 */             (Vector3fc)KeyframeAnimations.degreeVec(-50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9583F, 
/* 171 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/* 172 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4583F, 
/* 173 */             (Vector3fc)KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 175 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 176 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -0.25F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 177 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5417F, 
/* 178 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, -0.18F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/* 179 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9583F, 
/* 180 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/* 181 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4583F, 
/* 182 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -0.25F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 184 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 185 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/* 186 */             (Vector3fc)KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5417F, 
/* 187 */             (Vector3fc)KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/* 188 */             (Vector3fc)KeyframeAnimations.degreeVec(-50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9583F, 
/* 189 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/* 190 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4583F, 
/* 191 */             (Vector3fc)KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 193 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 194 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -0.25F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 195 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5417F, 
/* 196 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, -0.18F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/* 197 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9583F, 
/* 198 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/* 199 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4583F, 
/* 200 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -0.25F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 202 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 203 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 204 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 205 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 206 */             (Vector3fc)KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 207 */             (Vector3fc)KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/* 208 */             (Vector3fc)KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4583F, 
/* 209 */             (Vector3fc)KeyframeAnimations.degreeVec(-50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 211 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 212 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 213 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 214 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 215 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/* 216 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, -0.18F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4583F, 
/* 217 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 219 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 220 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 221 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 222 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/* 223 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 224 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 225 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4583F, 
/* 226 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 228 */         })).build();
/*     */   
/* 230 */   public static final AnimationDefinition ARMADILLO_PEEK = AnimationDefinition.Builder.withLength(2.5F)
/* 231 */     .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/* 232 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.15F, 
/* 233 */             (Vector3fc)KeyframeAnimations.degreeVec(-65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4F, 
/* 234 */             (Vector3fc)KeyframeAnimations.degreeVec(-50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 235 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7F, 
/* 236 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9F, 
/* 237 */             (Vector3fc)KeyframeAnimations.degreeVec(-7.5F, 0.0F, 45.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.15F, 
/* 238 */             (Vector3fc)KeyframeAnimations.degreeVec(-7.5F, 0.0F, 45.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3F, 
/* 239 */             (Vector3fc)KeyframeAnimations.degreeVec(-0.8639F, -1.4959F, -39.1287F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.6F, 
/* 240 */             (Vector3fc)KeyframeAnimations.degreeVec(-0.8639F, -1.4959F, -39.1287F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, 
/* 241 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8F, 
/* 242 */             (Vector3fc)KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.85F, 
/* 243 */             (Vector3fc)KeyframeAnimations.degreeVec(-70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 245 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/* 246 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 7.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.05F, 
/* 247 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 4.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.15F, 
/* 248 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 4.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 249 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 5.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.35F, 
/* 250 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4F, 
/* 251 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 252 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.1F, 1.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.6F, 
/* 253 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.1F, 0.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7F, 
/* 254 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.1F, 0.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, 
/* 255 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.1F, 0.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8F, 
/* 256 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.1F, 0.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.95F, 
/* 257 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.1F, 5.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 258 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.1F, 7.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.15F, 
/* 259 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.1F, 8.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3F, 
/* 260 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.1F, 5.2F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 262 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 263 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 3.0F, -2.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 265 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 266 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 3.0F, -2.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 268 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 269 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/* 270 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.8333F, 
/* 271 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/* 272 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.75F, 
/* 273 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8F, 
/* 274 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.95F, 
/* 275 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 277 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 278 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(-1.0F, 2.0F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/* 279 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 2.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.6667F, 
/* 280 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 2.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.8333F, 
/* 281 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 2.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/* 282 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 2.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.75F, 
/* 283 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 2.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.95F, 
/* 284 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 2.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.0F, 
/* 285 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 2.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.15F, 
/* 286 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 3.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 288 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 289 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.6667F, 
/* 290 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.8333F, 
/* 291 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/* 292 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.75F, 
/* 293 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8F, 
/* 294 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.95F, 
/* 295 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 297 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 298 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(1.0F, 2.0F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.6667F, 
/* 299 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 2.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.75F, 
/* 300 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 2.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.8333F, 
/* 301 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 2.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/* 302 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 2.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.75F, 
/* 303 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 2.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.95F, 
/* 304 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 2.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.0F, 
/* 305 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 2.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.15F, 
/* 306 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 3.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 308 */         })).addAnimation("cube", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/* 309 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 310 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.35F, 
/* 311 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 312 */             (Vector3fc)KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.6F, 
/* 313 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 314 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.05F, 
/* 315 */             (Vector3fc)KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.15F, 
/* 316 */             (Vector3fc)KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/* 317 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3F, 
/* 318 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/* 319 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 321 */         })).addAnimation("cube", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 322 */           new Keyframe(0.25F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.35F, 
/* 323 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.6F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 324 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.6F, 
/* 325 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 326 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.05F, 
/* 327 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.2F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.15F, 
/* 328 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.7F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/* 329 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3F, 
/* 330 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.3F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/* 331 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 333 */         })).build();
/*     */   
/* 335 */   public static final AnimationDefinition ARMADILLO_ROLL_OUT = AnimationDefinition.Builder.withLength(1.5F)
/* 336 */     .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/* 337 */           new Keyframe(0.1F, (Vector3fc)KeyframeAnimations.degreeVec(-50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.15F, 
/* 338 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 339 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4F, 
/* 340 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.65F, 
/* 341 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7F, 
/* 342 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.85F, 
/* 343 */             (Vector3fc)KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9F, 
/* 344 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.95F, 
/* 345 */             (Vector3fc)KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.05F, 
/* 346 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1F, 
/* 347 */             (Vector3fc)KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 349 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/* 350 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 5.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.05F, 
/* 351 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1F, 
/* 352 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.15F, 
/* 353 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.1F, 1.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 354 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.03F, 0.13F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4F, 
/* 355 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.03F, 0.13F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.65F, 
/* 356 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.03F, 0.13F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7F, 
/* 357 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.1F, 0.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 358 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.1F, 2.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.85F, 
/* 359 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 5.1F, 3.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9F, 
/* 360 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.1F, 0.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.95F, 
/* 361 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.9F, -0.8F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.05F, 
/* 362 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.9F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1F, 
/* 363 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.6F, 0.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.15F, 
/* 364 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.4F, 0.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2F, 
/* 365 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 366 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3F, 
/* 367 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.2F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 369 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 370 */           new Keyframe(1.1F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3F, 
/* 371 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 30.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4F, 
/* 372 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.45F, 
/* 373 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 374 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 376 */         })).addAnimation("right_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 377 */           new Keyframe(1.1F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 3.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2F, 
/* 378 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 8.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3F, 
/* 379 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4F, 
/* 380 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.45F, 
/* 381 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 382 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 384 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 385 */           new Keyframe(1.1F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3F, 
/* 386 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4F, 
/* 387 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.45F, 
/* 388 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 389 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 391 */         })).addAnimation("left_hind_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 392 */           new Keyframe(1.1F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 3.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2F, 
/* 393 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 8.0F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3F, 
/* 394 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.35F, 
/* 395 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4F, 
/* 396 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.45F, 
/* 397 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 398 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 400 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 401 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.05F, 
/* 402 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/* 403 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.55F, 
/* 404 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.6F, 
/* 405 */             (Vector3fc)KeyframeAnimations.degreeVec(-92.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.1F, 
/* 406 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.3F, 
/* 407 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 30.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.4F, 
/* 408 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.45F, 
/* 409 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 410 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 412 */         })).addAnimation("right_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/* 413 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(-1.0F, 2.0F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.05F, 
/* 414 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 2.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/* 415 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 2.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.55F, 
/* 416 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 2.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7F, 
/* 417 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 2.0F, 2.63F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.1F, 
/* 418 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 2.0F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2F, 
/* 419 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 7.0F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3F, 
/* 420 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4F, 
/* 421 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.45F, 
/* 422 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 423 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 425 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 426 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.05F, 
/* 427 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/* 428 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.55F, 
/* 429 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.6F, 
/* 430 */             (Vector3fc)KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.1F, 
/* 431 */             (Vector3fc)KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.3F, 
/* 432 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.4F, 
/* 433 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.45F, 
/* 434 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 435 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 437 */         })).addAnimation("left_front_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/* 438 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(1.0F, 2.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.05F, 
/* 439 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 2.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.15F, 
/* 440 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/* 441 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 2.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.55F, 
/* 442 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 2.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7F, 
/* 443 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 2.0F, 1.88F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.75F, 
/* 444 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 2.0F, 2.67F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.1F, 
/* 445 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 2.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.2F, 
/* 446 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 8.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.25F, 
/* 447 */             (Vector3fc)KeyframeAnimations.posVec(1.06F, 5.06F, 1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.3F, 
/* 448 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.4F, 
/* 449 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.45F, 
/* 450 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 451 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*     */         
/* 453 */         })).addAnimation("cube", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/* 454 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.05F, 
/* 455 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.15F, 
/* 456 */             (Vector3fc)KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 457 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7F, 
/* 458 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 459 */             (Vector3fc)KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.85F, 
/* 460 */             (Vector3fc)KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9F, 
/* 461 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.95F, 
/* 462 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.05F, 
/* 463 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1F, 
/* 464 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 466 */         })).addAnimation("cube", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/* 467 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.05F, 
/* 468 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.6F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.15F, 
/* 469 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 470 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7F, 
/* 471 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 472 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.2F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.85F, 
/* 473 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.7F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9F, 
/* 474 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.95F, 
/* 475 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.3F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.05F, 
/* 476 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2F, 
/* 477 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 478 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 8.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 479 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 481 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 482 */           new Keyframe(1.1F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2F, 
/* 483 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 484 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.3F, 
/* 485 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4F, 
/* 486 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/* 487 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*     */         
/* 489 */         })).build();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/animation/definitions/ArmadilloAnimation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */