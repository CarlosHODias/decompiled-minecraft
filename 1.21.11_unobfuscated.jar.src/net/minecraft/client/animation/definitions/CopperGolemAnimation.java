/*      */ package net.minecraft.client.animation.definitions;
/*      */ 
/*      */ import net.minecraft.client.animation.AnimationChannel;
/*      */ import net.minecraft.client.animation.AnimationDefinition;
/*      */ import net.minecraft.client.animation.Keyframe;
/*      */ import net.minecraft.client.animation.KeyframeAnimations;
/*      */ import org.joml.Vector3fc;
/*      */ 
/*      */ public class CopperGolemAnimation {
/*   10 */   public static final AnimationDefinition COPPER_GOLEM_WALK = AnimationDefinition.Builder.withLength(0.8333F).looping()
/*   11 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*   12 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.2083F, 
/*   13 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, -1.87F, -10.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4167F, 
/*   14 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.625F, 
/*   15 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, -0.82F, 10.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.8333F, 
/*   16 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*      */         
/*   18 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*   19 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.2083F, 
/*   20 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 1.87F, 10.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4167F, 
/*   21 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.625F, 
/*   22 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.82F, -10.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.8333F, 
/*   23 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*      */         
/*   25 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*   26 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4167F, 
/*   27 */             (Vector3fc)KeyframeAnimations.degreeVec(-80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.8333F, 
/*   28 */             (Vector3fc)KeyframeAnimations.degreeVec(70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*      */         
/*   30 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*   31 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4167F, 
/*   32 */             (Vector3fc)KeyframeAnimations.degreeVec(70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.8333F, 
/*   33 */             (Vector3fc)KeyframeAnimations.degreeVec(-80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*      */         
/*   35 */         })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*   36 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4167F, 
/*   37 */             (Vector3fc)KeyframeAnimations.degreeVec(60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.8333F, 
/*   38 */             (Vector3fc)KeyframeAnimations.degreeVec(-60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*      */         
/*   40 */         })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*   41 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4167F, 
/*   42 */             (Vector3fc)KeyframeAnimations.degreeVec(-60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.8333F, 
/*   43 */             (Vector3fc)KeyframeAnimations.degreeVec(60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*      */         
/*   45 */         })).build();
/*      */   
/*   47 */   public static final AnimationDefinition COPPER_GOLEM_IDLE = AnimationDefinition.Builder.withLength(3.5F)
/*   48 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*   49 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*   50 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -35.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*   51 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -35.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/*   52 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 35.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/*   53 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 35.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7083F, 
/*   54 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 35.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*   55 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.5F, 
/*   56 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*   58 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*   59 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*   60 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*   61 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/*   62 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/*   63 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/*   64 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 300.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*   65 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 300.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, 
/*   66 */             (Vector3fc)KeyframeAnimations.degreeVec(-25.0F, 300.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7083F, 
/*   67 */             (Vector3fc)KeyframeAnimations.degreeVec(-25.0F, 300.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*   68 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.5F, 
/*   69 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*   71 */         })).build();
/*      */   
/*   73 */   public static final AnimationDefinition COPPER_GOLEM_WALK_ITEM = AnimationDefinition.Builder.withLength(0.8333F)
/*   74 */     .looping()
/*   75 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*   76 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.2083F, 
/*   77 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, -1.87F, -5.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4167F, 
/*   78 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.625F, 
/*   79 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, -0.82F, 5.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.8333F, 
/*   80 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*      */         
/*   82 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*   83 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.2083F, 
/*   84 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 1.87F, 10.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4167F, 
/*   85 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.625F, 
/*   86 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.82F, -10.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.8333F, 
/*   87 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*      */         
/*   89 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*   90 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-59.78638F, -6.49053F, -3.76613F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*   92 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*   93 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-59.78638F, 6.49053F, 3.76613F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*   95 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*   96 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(-0.21129F, -0.0212F, -0.07004F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*   98 */         })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*   99 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4167F, 
/*  100 */             (Vector3fc)KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.8333F, 
/*  101 */             (Vector3fc)KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*      */         
/*  103 */         })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  104 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4167F, 
/*  105 */             (Vector3fc)KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.8333F, 
/*  106 */             (Vector3fc)KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
/*      */         
/*  108 */         })).build();
/*      */   
/*  110 */   public static final AnimationDefinition COPPER_GOLEM_CHEST_INTERACTION_ITEM_DROP = AnimationDefinition.Builder.withLength(3.0F)
/*  111 */     .looping()
/*  112 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  113 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  114 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  115 */             (Vector3fc)KeyframeAnimations.degreeVec(18.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  116 */             (Vector3fc)KeyframeAnimations.degreeVec(24.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  117 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  118 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  119 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  120 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/*  121 */             (Vector3fc)KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9167F, 
/*  122 */             (Vector3fc)KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0417F, 
/*  123 */             (Vector3fc)KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  124 */             (Vector3fc)KeyframeAnimations.degreeVec(12.40525F, -4.4E-4F, 0.00829F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/*  125 */             (Vector3fc)KeyframeAnimations.degreeVec(12.40525F, -4.4E-4F, 0.00829F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/*  126 */             (Vector3fc)KeyframeAnimations.degreeVec(13.92716F, 26.80536F, 6.38918F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.625F, 
/*  127 */             (Vector3fc)KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  128 */             (Vector3fc)KeyframeAnimations.degreeVec(21.43F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7917F, 
/*  129 */             (Vector3fc)KeyframeAnimations.degreeVec(21.43F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  130 */             (Vector3fc)KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  131 */             (Vector3fc)KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.125F, 
/*  132 */             (Vector3fc)KeyframeAnimations.degreeVec(12.40725F, 0.0F, 0.00783F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  133 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/*  134 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.375F, 
/*  135 */             (Vector3fc)KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4167F, 
/*  136 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  137 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/*  138 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.625F, 
/*  139 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6667F, 
/*  140 */             (Vector3fc)KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7083F, 
/*  141 */             (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.875F, 
/*  142 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  143 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  145 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/*  146 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  147 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  148 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  149 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.625F, 
/*  150 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.4F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  151 */             (Vector3fc)KeyframeAnimations.posVec(-0.01805F, 0.88303F, -0.09783F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7917F, 
/*  152 */             (Vector3fc)KeyframeAnimations.posVec(-0.01805F, 0.88303F, -0.09783F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  153 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  154 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  155 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/*  156 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.375F, 
/*  157 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  158 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/*  159 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.875F, 
/*  160 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  161 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  163 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  164 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  165 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  166 */             (Vector3fc)KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  167 */             (Vector3fc)KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  168 */             (Vector3fc)KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  169 */             (Vector3fc)KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  170 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  171 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  172 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.6667F, 
/*  173 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  174 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/*  175 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0417F, 
/*  176 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/*  177 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/*  178 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 27.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/*  179 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4167F, 
/*  180 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4583F, 
/*  181 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/*  182 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.625F, 
/*  183 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  184 */             (Vector3fc)KeyframeAnimations.degreeVec(9.73588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7083F, 
/*  185 */             (Vector3fc)KeyframeAnimations.degreeVec(9.73588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7917F, 
/*  186 */             (Vector3fc)KeyframeAnimations.degreeVec(10.15255F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  187 */             (Vector3fc)KeyframeAnimations.degreeVec(17.86088F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.875F, 
/*  188 */             (Vector3fc)KeyframeAnimations.degreeVec(17.23588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9167F, 
/*  189 */             (Vector3fc)KeyframeAnimations.degreeVec(17.23588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  190 */             (Vector3fc)KeyframeAnimations.degreeVec(17.23588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0833F, 
/*  191 */             (Vector3fc)KeyframeAnimations.degreeVec(-0.26F, -1.93F, -3.73F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  192 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  193 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/*  194 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6667F, 
/*  195 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7083F, 
/*  196 */             (Vector3fc)KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.875F, 
/*  197 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  198 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  200 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/*  201 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  202 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  203 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.15451F, 0.47553F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  204 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  205 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0417F, 
/*  206 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/*  207 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/*  208 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4167F, 
/*  209 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4583F, 
/*  210 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/*  211 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.625F, 
/*  212 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  213 */             (Vector3fc)KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.875F, 
/*  214 */             (Vector3fc)KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9167F, 
/*  215 */             (Vector3fc)KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  216 */             (Vector3fc)KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0833F, 
/*  217 */             (Vector3fc)KeyframeAnimations.posVec(-0.39F, 0.52F, -2.21F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  218 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  219 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6667F, 
/*  220 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.875F, 
/*  221 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  222 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  224 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  225 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  226 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  227 */             (Vector3fc)KeyframeAnimations.degreeVec(-7.38733F, 1.29876F, 9.91615F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  228 */             (Vector3fc)KeyframeAnimations.degreeVec(-7.38733F, 1.29876F, 9.91615F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  229 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 32.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  230 */             (Vector3fc)KeyframeAnimations.degreeVec(-34.55418F, 11.73507F, 36.8361F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, 
/*  231 */             (Vector3fc)KeyframeAnimations.degreeVec(-117.82767F, 2.94538F, 0.22703F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5417F, 
/*  232 */             (Vector3fc)KeyframeAnimations.degreeVec(-97.7902F, 0.73403F, 1.39387F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  233 */             (Vector3fc)KeyframeAnimations.degreeVec(-92.79F, 0.73F, 1.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  234 */             (Vector3fc)KeyframeAnimations.degreeVec(-92.79F, 0.73F, 1.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/*  235 */             (Vector3fc)KeyframeAnimations.degreeVec(-95.83405F, 33.18639F, -0.40081F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/*  236 */             (Vector3fc)KeyframeAnimations.degreeVec(-95.83F, 33.19F, -0.4F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/*  237 */             (Vector3fc)KeyframeAnimations.degreeVec(-44.60123F, 10.14454F, 8.66307F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  238 */             (Vector3fc)KeyframeAnimations.degreeVec(-4.31506F, 6.54961F, 13.21388F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0833F, 
/*  239 */             (Vector3fc)KeyframeAnimations.degreeVec(-4.31506F, 6.54961F, 13.21388F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.125F, 
/*  240 */             (Vector3fc)KeyframeAnimations.degreeVec(-4.31506F, 6.54961F, 13.21388F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2083F, 
/*  241 */             (Vector3fc)KeyframeAnimations.degreeVec(-113.7629F, 21.38835F, 15.48184F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  242 */             (Vector3fc)KeyframeAnimations.degreeVec(-113.76F, 21.39F, 15.48F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/*  243 */             (Vector3fc)KeyframeAnimations.degreeVec(-39.99304F, 7.3511F, 14.05666F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.625F, 
/*  244 */             (Vector3fc)KeyframeAnimations.degreeVec(68.07913F, -3.61348F, 1.39182F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6667F, 
/*  245 */             (Vector3fc)KeyframeAnimations.degreeVec(193.16708F, -1.90441F, -0.43495F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7083F, 
/*  246 */             (Vector3fc)KeyframeAnimations.degreeVec(250.66708F, -1.90441F, -0.43495F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7917F, 
/*  247 */             (Vector3fc)KeyframeAnimations.degreeVec(264.19006F, -4.41519F, -0.66792F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.8333F, 
/*  248 */             (Vector3fc)KeyframeAnimations.degreeVec(270.53693F, 16.03493F, 0.47968F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.875F, 
/*  249 */             (Vector3fc)KeyframeAnimations.degreeVec(319.31668F, 17.97846F, 1.34328F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9167F, 
/*  250 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  251 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  253 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/*  254 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  255 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  256 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  257 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  258 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  259 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/*  260 */             (Vector3fc)KeyframeAnimations.posVec(0.25358F, -0.20153F, 2.21248F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/*  261 */             (Vector3fc)KeyframeAnimations.posVec(0.25F, -0.2F, 2.21F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5417F, 
/*  262 */             (Vector3fc)KeyframeAnimations.posVec(-0.79739F, -0.10573F, 1.70592F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/*  263 */             (Vector3fc)KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  264 */             (Vector3fc)KeyframeAnimations.posVec(-0.51052F, -0.38088F, 0.79745F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0833F, 
/*  265 */             (Vector3fc)KeyframeAnimations.posVec(-0.51052F, -0.38088F, 0.79745F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.125F, 
/*  266 */             (Vector3fc)KeyframeAnimations.posVec(-0.51052F, -0.38088F, 0.79745F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  267 */             (Vector3fc)KeyframeAnimations.posVec(-0.51F, -0.38F, 0.8F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.875F, 
/*  268 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  269 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  271 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  272 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  273 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  274 */             (Vector3fc)KeyframeAnimations.degreeVec(25.0F, 0.0F, -37.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  275 */             (Vector3fc)KeyframeAnimations.degreeVec(-21.59341F, -12.60837F, -45.69252F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  276 */             (Vector3fc)KeyframeAnimations.degreeVec(-120.7755F, -5.21988F, -2.02064F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  277 */             (Vector3fc)KeyframeAnimations.degreeVec(-98.27419F, -1.79323F, -1.15048F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  278 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.27F, -1.79F, -1.15F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5417F, 
/*  279 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.27419F, -1.79323F, -1.15048F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  280 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/*  281 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/*  282 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/*  283 */             (Vector3fc)KeyframeAnimations.degreeVec(-95.75F, -2.42F, 5.97F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/*  284 */             (Vector3fc)KeyframeAnimations.degreeVec(-98.4029F, -17.39503F, 6.85104F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/*  285 */             (Vector3fc)KeyframeAnimations.degreeVec(-101.24523F, -29.87096F, 7.69993F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/*  286 */             (Vector3fc)KeyframeAnimations.degreeVec(-101.25F, -29.87F, 7.7F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  287 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  288 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  289 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  290 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/*  291 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  292 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.58526F, -17.10045F, 11.7676F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  293 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.59F, -17.1F, 11.77F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/*  294 */             (Vector3fc)KeyframeAnimations.degreeVec(-46.59531F, -16.13694F, -3.85578F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.875F, 
/*  295 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  296 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  298 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  299 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  300 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  301 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  302 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/*  303 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/*  304 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  305 */             (Vector3fc)KeyframeAnimations.posVec(-0.00677F, -0.76064F, 3.19059F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  306 */             (Vector3fc)KeyframeAnimations.posVec(0.0512F, -0.76176F, 3.12882F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.875F, 
/*  307 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  308 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  310 */         })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  311 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  312 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  313 */             (Vector3fc)KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.125F, 
/*  314 */             (Vector3fc)KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  315 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/*  316 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.375F, 
/*  317 */             (Vector3fc)KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  318 */             (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/*  319 */             (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5833F, 
/*  320 */             (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.625F, 
/*  321 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.875F, 
/*  322 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  323 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  325 */         })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/*  326 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  327 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  328 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.125F, 
/*  329 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  330 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/*  331 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.375F, 
/*  332 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0909F, -0.10834F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  333 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.09F, -0.11F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/*  334 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.09F, -0.11F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.875F, 
/*  335 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  336 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  338 */         })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  339 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  340 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  341 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.125F, 
/*  342 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  343 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/*  344 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.375F, 
/*  345 */             (Vector3fc)KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  346 */             (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/*  347 */             (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5833F, 
/*  348 */             (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.625F, 
/*  349 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  350 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  352 */         })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  353 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  354 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  355 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.125F, 
/*  356 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  357 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/*  358 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.375F, 
/*  359 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0909F, -0.10834F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  360 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.09F, -0.11F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/*  361 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.09F, -0.11F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  362 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  364 */         })).build();
/*      */   
/*  366 */   public static final AnimationDefinition COPPER_GOLEM_CHEST_INTERACTION_ITEM_NODROP = AnimationDefinition.Builder.withLength(3.0F)
/*  367 */     .looping()
/*  368 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  369 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  370 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  371 */             (Vector3fc)KeyframeAnimations.degreeVec(18.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  372 */             (Vector3fc)KeyframeAnimations.degreeVec(24.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  373 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  374 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  375 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  376 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/*  377 */             (Vector3fc)KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9167F, 
/*  378 */             (Vector3fc)KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0417F, 
/*  379 */             (Vector3fc)KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  380 */             (Vector3fc)KeyframeAnimations.degreeVec(12.40525F, -4.4E-4F, 0.00829F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/*  381 */             (Vector3fc)KeyframeAnimations.degreeVec(12.40525F, -4.4E-4F, 0.00829F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/*  382 */             (Vector3fc)KeyframeAnimations.degreeVec(13.92716F, 26.80536F, 6.38918F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.625F, 
/*  383 */             (Vector3fc)KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  384 */             (Vector3fc)KeyframeAnimations.degreeVec(21.43F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7917F, 
/*  385 */             (Vector3fc)KeyframeAnimations.degreeVec(21.43F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  386 */             (Vector3fc)KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  387 */             (Vector3fc)KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  388 */             (Vector3fc)KeyframeAnimations.degreeVec(12.40725F, 0.0F, 0.00783F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  389 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  390 */             (Vector3fc)KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/*  391 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.875F, 
/*  392 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  393 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  395 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/*  396 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  397 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  398 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  399 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.625F, 
/*  400 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.4F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  401 */             (Vector3fc)KeyframeAnimations.posVec(-0.01805F, 0.88303F, -0.09783F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7917F, 
/*  402 */             (Vector3fc)KeyframeAnimations.posVec(-0.01805F, 0.88303F, -0.09783F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  403 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  404 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  405 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  406 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/*  407 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.875F, 
/*  408 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  409 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  411 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  412 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  413 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  414 */             (Vector3fc)KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  415 */             (Vector3fc)KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  416 */             (Vector3fc)KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  417 */             (Vector3fc)KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  418 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  419 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  420 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.6667F, 
/*  421 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  422 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/*  423 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0417F, 
/*  424 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/*  425 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/*  426 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 27.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/*  427 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4167F, 
/*  428 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4583F, 
/*  429 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/*  430 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.625F, 
/*  431 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  432 */             (Vector3fc)KeyframeAnimations.degreeVec(9.73588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7083F, 
/*  433 */             (Vector3fc)KeyframeAnimations.degreeVec(9.73588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7917F, 
/*  434 */             (Vector3fc)KeyframeAnimations.degreeVec(10.15255F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  435 */             (Vector3fc)KeyframeAnimations.degreeVec(17.86088F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.875F, 
/*  436 */             (Vector3fc)KeyframeAnimations.degreeVec(17.23588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9167F, 
/*  437 */             (Vector3fc)KeyframeAnimations.degreeVec(17.23588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  438 */             (Vector3fc)KeyframeAnimations.degreeVec(17.23588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0833F, 
/*  439 */             (Vector3fc)KeyframeAnimations.degreeVec(-0.26F, -1.93F, -3.73F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  440 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/*  441 */             (Vector3fc)KeyframeAnimations.degreeVec(1.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  442 */             (Vector3fc)KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6667F, 
/*  443 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  444 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  445 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  447 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/*  448 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  449 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  450 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.15451F, 0.47553F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  451 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  452 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0417F, 
/*  453 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/*  454 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/*  455 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4167F, 
/*  456 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4583F, 
/*  457 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/*  458 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.625F, 
/*  459 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  460 */             (Vector3fc)KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.875F, 
/*  461 */             (Vector3fc)KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9167F, 
/*  462 */             (Vector3fc)KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  463 */             (Vector3fc)KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0833F, 
/*  464 */             (Vector3fc)KeyframeAnimations.posVec(-0.39F, 0.52F, -2.21F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  465 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/*  466 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.01091F, -0.02988F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  467 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.01F, -0.03F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6667F, 
/*  468 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.01F, -0.03F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  469 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.01F, -0.03F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  470 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  472 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  473 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  474 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  475 */             (Vector3fc)KeyframeAnimations.degreeVec(-7.38733F, 1.29876F, 9.91615F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  476 */             (Vector3fc)KeyframeAnimations.degreeVec(-7.38733F, 1.29876F, 9.91615F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  477 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 32.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  478 */             (Vector3fc)KeyframeAnimations.degreeVec(-34.55418F, 11.73507F, 36.8361F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, 
/*  479 */             (Vector3fc)KeyframeAnimations.degreeVec(-117.82767F, 2.94538F, 0.22703F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5417F, 
/*  480 */             (Vector3fc)KeyframeAnimations.degreeVec(-97.7902F, 0.73403F, 1.39387F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  481 */             (Vector3fc)KeyframeAnimations.degreeVec(-92.79F, 0.73F, 1.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  482 */             (Vector3fc)KeyframeAnimations.degreeVec(-92.79F, 0.73F, 1.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/*  483 */             (Vector3fc)KeyframeAnimations.degreeVec(-95.83405F, 33.18639F, -0.40081F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/*  484 */             (Vector3fc)KeyframeAnimations.degreeVec(-95.83F, 33.19F, -0.4F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/*  485 */             (Vector3fc)KeyframeAnimations.degreeVec(-44.60123F, 10.14454F, 8.66307F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  486 */             (Vector3fc)KeyframeAnimations.degreeVec(-4.31506F, 6.54961F, 13.21388F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0833F, 
/*  487 */             (Vector3fc)KeyframeAnimations.degreeVec(-4.31506F, 6.54961F, 13.21388F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  488 */             (Vector3fc)KeyframeAnimations.degreeVec(-4.31506F, 6.54961F, 13.21388F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  489 */             (Vector3fc)KeyframeAnimations.degreeVec(-6.53898F, 13.96898F, 14.34786F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  490 */             (Vector3fc)KeyframeAnimations.degreeVec(3.50393F, -4.70737F, 8.3608F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  491 */             (Vector3fc)KeyframeAnimations.degreeVec(3.50393F, -4.70737F, 8.3608F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/*  492 */             (Vector3fc)KeyframeAnimations.degreeVec(3.90089F, -4.3843F, 3.35549F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/*  493 */             (Vector3fc)KeyframeAnimations.degreeVec(3.9F, -4.38F, 3.36F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9167F, 
/*  494 */             (Vector3fc)KeyframeAnimations.degreeVec(3.9F, -4.38F, 3.36F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  495 */             (Vector3fc)KeyframeAnimations.degreeVec(3.90089F, -4.3843F, 3.35549F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  496 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  498 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/*  499 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  500 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  501 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  502 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  503 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  504 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/*  505 */             (Vector3fc)KeyframeAnimations.posVec(0.25358F, -0.20153F, 2.21248F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/*  506 */             (Vector3fc)KeyframeAnimations.posVec(0.25F, -0.2F, 2.21F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5417F, 
/*  507 */             (Vector3fc)KeyframeAnimations.posVec(-0.79739F, -0.10573F, 1.70592F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/*  508 */             (Vector3fc)KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  509 */             (Vector3fc)KeyframeAnimations.posVec(-0.51052F, -0.38088F, 0.79745F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0833F, 
/*  510 */             (Vector3fc)KeyframeAnimations.posVec(-0.51052F, -0.38088F, 0.79745F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  511 */             (Vector3fc)KeyframeAnimations.posVec(-0.51052F, -0.38088F, 0.79745F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  512 */             (Vector3fc)KeyframeAnimations.posVec(-0.46F, -0.34F, 0.72F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  513 */             (Vector3fc)KeyframeAnimations.posVec(-0.46F, 0.1159F, -0.30086F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  514 */             (Vector3fc)KeyframeAnimations.posVec(-0.46F, 0.1159F, -0.30086F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/*  515 */             (Vector3fc)KeyframeAnimations.posVec(-0.46F, 0.1159F, -0.30086F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/*  516 */             (Vector3fc)KeyframeAnimations.posVec(-0.46F, -0.88F, -0.3F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9167F, 
/*  517 */             (Vector3fc)KeyframeAnimations.posVec(-0.46F, -0.88F, -0.3F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  518 */             (Vector3fc)KeyframeAnimations.posVec(-0.46F, 0.1159F, -0.30086F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  519 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  521 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  522 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  523 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  524 */             (Vector3fc)KeyframeAnimations.degreeVec(25.0F, 0.0F, -37.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  525 */             (Vector3fc)KeyframeAnimations.degreeVec(-21.59341F, -12.60837F, -45.69252F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  526 */             (Vector3fc)KeyframeAnimations.degreeVec(-120.7755F, -5.21988F, -2.02064F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  527 */             (Vector3fc)KeyframeAnimations.degreeVec(-98.27419F, -1.79323F, -1.15048F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  528 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.27F, -1.79F, -1.15F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5417F, 
/*  529 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.27419F, -1.79323F, -1.15048F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  530 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/*  531 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/*  532 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/*  533 */             (Vector3fc)KeyframeAnimations.degreeVec(-95.75F, -2.42F, 5.97F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/*  534 */             (Vector3fc)KeyframeAnimations.degreeVec(-98.4029F, -17.39503F, 6.85104F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/*  535 */             (Vector3fc)KeyframeAnimations.degreeVec(-101.24523F, -29.87096F, 7.69993F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/*  536 */             (Vector3fc)KeyframeAnimations.degreeVec(-101.25F, -29.87F, 7.7F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  537 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  538 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  539 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  540 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -20.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  541 */             (Vector3fc)KeyframeAnimations.degreeVec(2.47864F, -0.32621F, -12.50706F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  542 */             (Vector3fc)KeyframeAnimations.degreeVec(2.47864F, -0.32621F, -12.50706F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/*  543 */             (Vector3fc)KeyframeAnimations.degreeVec(2.41492F, -0.64686F, -5.01363F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/*  544 */             (Vector3fc)KeyframeAnimations.degreeVec(2.41F, -0.65F, -5.01F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9167F, 
/*  545 */             (Vector3fc)KeyframeAnimations.degreeVec(2.41F, -0.65F, -5.01F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  546 */             (Vector3fc)KeyframeAnimations.degreeVec(2.41492F, -0.64686F, -5.01363F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  547 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  549 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/*  550 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  551 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  552 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  553 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/*  554 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/*  555 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  556 */             (Vector3fc)KeyframeAnimations.posVec(-0.00677F, -0.76064F, 3.19059F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  557 */             (Vector3fc)KeyframeAnimations.posVec(-0.00677F, -0.76064F, 3.19059F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  558 */             (Vector3fc)KeyframeAnimations.posVec(-0.00677F, -0.76064F, 3.19059F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  559 */             (Vector3fc)KeyframeAnimations.posVec(0.03F, -0.76F, 0.45F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  560 */             (Vector3fc)KeyframeAnimations.posVec(0.03F, -0.28229F, -0.07133F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  561 */             (Vector3fc)KeyframeAnimations.posVec(0.03F, -0.28229F, -0.07133F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/*  562 */             (Vector3fc)KeyframeAnimations.posVec(0.03F, -0.28229F, -0.07133F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/*  563 */             (Vector3fc)KeyframeAnimations.posVec(0.03F, -1.28F, -0.07F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9167F, 
/*  564 */             (Vector3fc)KeyframeAnimations.posVec(0.03F, -1.28F, -0.07F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  565 */             (Vector3fc)KeyframeAnimations.posVec(0.03F, -0.28229F, -0.07133F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  566 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  568 */         })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  569 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  570 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  571 */             (Vector3fc)KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  572 */             (Vector3fc)KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  573 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  574 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/*  575 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.875F, 
/*  576 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  577 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  579 */         })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  580 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  581 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  582 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  583 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  584 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  585 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/*  586 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.875F, 
/*  587 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  588 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  590 */         })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  591 */           new Keyframe(0.125F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  592 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  593 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  594 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  596 */         })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  597 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  598 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  599 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  600 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  601 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  602 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  604 */         })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] {
/*  605 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  606 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  608 */         })).build();
/*      */   
/*  610 */   public static final AnimationDefinition COPPER_GOLEM_CHEST_INTERACTION_NOITEM_GET = AnimationDefinition.Builder.withLength(3.0F)
/*  611 */     .looping()
/*  612 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  613 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  614 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  615 */             (Vector3fc)KeyframeAnimations.degreeVec(18.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  616 */             (Vector3fc)KeyframeAnimations.degreeVec(24.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  617 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  618 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  619 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  620 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/*  621 */             (Vector3fc)KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9167F, 
/*  622 */             (Vector3fc)KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0417F, 
/*  623 */             (Vector3fc)KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  624 */             (Vector3fc)KeyframeAnimations.degreeVec(12.40525F, -4.4E-4F, 0.00829F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/*  625 */             (Vector3fc)KeyframeAnimations.degreeVec(12.40525F, -4.4E-4F, 0.00829F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/*  626 */             (Vector3fc)KeyframeAnimations.degreeVec(13.92716F, 26.80536F, 6.38918F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.625F, 
/*  627 */             (Vector3fc)KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  628 */             (Vector3fc)KeyframeAnimations.degreeVec(21.43F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7917F, 
/*  629 */             (Vector3fc)KeyframeAnimations.degreeVec(21.43F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  630 */             (Vector3fc)KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  631 */             (Vector3fc)KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.125F, 
/*  632 */             (Vector3fc)KeyframeAnimations.degreeVec(12.40725F, 0.0F, 0.00783F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  633 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  634 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/*  635 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.375F, 
/*  636 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4167F, 
/*  637 */             (Vector3fc)KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  638 */             (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.625F, 
/*  639 */             (Vector3fc)KeyframeAnimations.degreeVec(24.14867F, -20.70481F, -9.00717F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7083F, 
/*  640 */             (Vector3fc)KeyframeAnimations.degreeVec(24.14867F, -20.70481F, -9.00717F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.75F, 
/*  641 */             (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7917F, 
/*  642 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  643 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  645 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/*  646 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  647 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  648 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  649 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.625F, 
/*  650 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.4F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  651 */             (Vector3fc)KeyframeAnimations.posVec(-0.01805F, 0.88303F, -0.09783F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7917F, 
/*  652 */             (Vector3fc)KeyframeAnimations.posVec(-0.01805F, 0.88303F, -0.09783F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  653 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  654 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  655 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  656 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/*  657 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.625F, 
/*  658 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.46194F, -0.19134F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7083F, 
/*  659 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.46194F, -0.19134F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7917F, 
/*  660 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  661 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  663 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  664 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  665 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  666 */             (Vector3fc)KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  667 */             (Vector3fc)KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  668 */             (Vector3fc)KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  669 */             (Vector3fc)KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  670 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  671 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  672 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.6667F, 
/*  673 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  674 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/*  675 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0417F, 
/*  676 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/*  677 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/*  678 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 27.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/*  679 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4167F, 
/*  680 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4583F, 
/*  681 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/*  682 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/*  683 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.625F, 
/*  684 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  685 */             (Vector3fc)KeyframeAnimations.degreeVec(10.16381F, -16.71134F, -6.35306F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  686 */             (Vector3fc)KeyframeAnimations.degreeVec(10.16381F, -16.71134F, -6.35306F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9167F, 
/*  687 */             (Vector3fc)KeyframeAnimations.degreeVec(10.16381F, -16.71134F, -6.35306F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9583F, 
/*  688 */             (Vector3fc)KeyframeAnimations.degreeVec(10.16381F, -16.71134F, -6.35306F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/*  689 */             (Vector3fc)KeyframeAnimations.degreeVec(5.16381F, -16.71134F, -6.35306F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  690 */             (Vector3fc)KeyframeAnimations.degreeVec(0.16381F, -16.71134F, -6.35306F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0833F, 
/*  691 */             (Vector3fc)KeyframeAnimations.degreeVec(0.15732F, -4.21139F, -6.31751F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.125F, 
/*  692 */             (Vector3fc)KeyframeAnimations.degreeVec(0.07901F, 5.3943F, -3.15187F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  693 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/*  694 */             (Vector3fc)KeyframeAnimations.degreeVec(4.53867F, 7.47675F, 0.59181F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  695 */             (Vector3fc)KeyframeAnimations.degreeVec(-2.53852F, 9.99038F, -0.44067F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4167F, 
/*  696 */             (Vector3fc)KeyframeAnimations.degreeVec(-12.68664F, 9.76061F, -2.18558F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.625F, 
/*  697 */             (Vector3fc)KeyframeAnimations.degreeVec(-15.19938F, 22.36971F, -3.52259F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6667F, 
/*  698 */             (Vector3fc)KeyframeAnimations.degreeVec(-3.02173F, 22.37156F, -2.41802F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7083F, 
/*  699 */             (Vector3fc)KeyframeAnimations.degreeVec(-0.52173F, 22.37156F, -2.41802F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.75F, 
/*  700 */             (Vector3fc)KeyframeAnimations.degreeVec(-12.40598F, -0.4674F, -1.79838F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.8333F, 
/*  701 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  702 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  704 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/*  705 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  706 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  707 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.15451F, 0.47553F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  708 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  709 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0417F, 
/*  710 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/*  711 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/*  712 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4167F, 
/*  713 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4583F, 
/*  714 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/*  715 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/*  716 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.625F, 
/*  717 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  718 */             (Vector3fc)KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  719 */             (Vector3fc)KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9167F, 
/*  720 */             (Vector3fc)KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9583F, 
/*  721 */             (Vector3fc)KeyframeAnimations.posVec(-0.52521F, 0.96725F, -0.32978F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/*  722 */             (Vector3fc)KeyframeAnimations.posVec(-0.52521F, 0.96725F, -0.32978F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  723 */             (Vector3fc)KeyframeAnimations.posVec(-0.5345F, 1.16541F, -0.37206F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0833F, 
/*  724 */             (Vector3fc)KeyframeAnimations.posVec(-0.5345F, 1.16541F, -0.37206F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  725 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  726 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.625F, 
/*  727 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.8333F, 
/*  728 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  729 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  731 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  732 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  733 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  734 */             (Vector3fc)KeyframeAnimations.degreeVec(-7.38733F, 1.29876F, 9.91615F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  735 */             (Vector3fc)KeyframeAnimations.degreeVec(-7.38733F, 1.29876F, 9.91615F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  736 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 32.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  737 */             (Vector3fc)KeyframeAnimations.degreeVec(-34.55418F, 11.73507F, 36.8361F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, 
/*  738 */             (Vector3fc)KeyframeAnimations.degreeVec(-82.47403F, 17.82361F, 2.17224F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  739 */             (Vector3fc)KeyframeAnimations.degreeVec(-85.08388F, 14.26971F, 1.99595F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5417F, 
/*  740 */             (Vector3fc)KeyframeAnimations.degreeVec(-85.16266F, 13.19102F, 2.43976F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  741 */             (Vector3fc)KeyframeAnimations.degreeVec(-92.79F, 0.73F, 1.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  742 */             (Vector3fc)KeyframeAnimations.degreeVec(-92.79F, 0.73F, 1.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/*  743 */             (Vector3fc)KeyframeAnimations.degreeVec(-95.83405F, 33.18639F, -0.40081F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/*  744 */             (Vector3fc)KeyframeAnimations.degreeVec(-95.83F, 33.19F, -0.4F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/*  745 */             (Vector3fc)KeyframeAnimations.degreeVec(-98.33F, 33.19F, -0.4F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5417F, 
/*  746 */             (Vector3fc)KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  747 */             (Vector3fc)KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  748 */             (Vector3fc)KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/*  749 */             (Vector3fc)KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  750 */             (Vector3fc)KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  751 */             (Vector3fc)KeyframeAnimations.degreeVec(-84.12204F, 8.95753F, 14.11779F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2083F, 
/*  752 */             (Vector3fc)KeyframeAnimations.degreeVec(-84.12204F, 8.95753F, 14.11779F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  753 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.6065F, 13.90544F, 15.98524F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  754 */             (Vector3fc)KeyframeAnimations.degreeVec(-124.48661F, 66.29146F, -7.28605F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.375F, 
/*  755 */             (Vector3fc)KeyframeAnimations.degreeVec(-129.4866F, 66.29146F, -7.28605F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4167F, 
/*  756 */             (Vector3fc)KeyframeAnimations.degreeVec(-108.91607F, 1.79762F, 20.93924F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/*  757 */             (Vector3fc)KeyframeAnimations.degreeVec(-102.18303F, 4.35881F, 17.40962F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/*  758 */             (Vector3fc)KeyframeAnimations.degreeVec(-98.33642F, -0.70114F, 4.09322F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.625F, 
/*  759 */             (Vector3fc)KeyframeAnimations.degreeVec(-98.39385F, 6.71929F, 3.00137F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6667F, 
/*  760 */             (Vector3fc)KeyframeAnimations.degreeVec(-98.33981F, 1.77244F, 3.7307F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7083F, 
/*  761 */             (Vector3fc)KeyframeAnimations.degreeVec(-100.70987F, 3.48829F, 7.1138F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.75F, 
/*  762 */             (Vector3fc)KeyframeAnimations.degreeVec(-97.95F, 6.92F, 13.88F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7917F, 
/*  763 */             (Vector3fc)KeyframeAnimations.degreeVec(-87.95F, 6.92F, 13.88F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.8333F, 
/*  764 */             (Vector3fc)KeyframeAnimations.degreeVec(-97.95F, 6.92F, 13.88F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.875F, 
/*  765 */             (Vector3fc)KeyframeAnimations.degreeVec(-102.95F, 6.92F, 13.88F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9167F, 
/*  766 */             (Vector3fc)KeyframeAnimations.degreeVec(-76.475F, 3.46F, 6.94F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  767 */             (Vector3fc)KeyframeAnimations.degreeVec(-26.475F, 3.46F, 6.94F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  768 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  770 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/*  771 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  772 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  773 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  774 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  775 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  776 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/*  777 */             (Vector3fc)KeyframeAnimations.posVec(0.25358F, -0.20153F, 2.21248F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/*  778 */             (Vector3fc)KeyframeAnimations.posVec(0.25F, -0.2F, 2.21F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/*  779 */             (Vector3fc)KeyframeAnimations.posVec(0.25F, -0.2F, 2.21F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5417F, 
/*  780 */             (Vector3fc)KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  781 */             (Vector3fc)KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  782 */             (Vector3fc)KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/*  783 */             (Vector3fc)KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  784 */             (Vector3fc)KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  785 */             (Vector3fc)KeyframeAnimations.posVec(-0.51F, -0.38F, 0.8F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  786 */             (Vector3fc)KeyframeAnimations.posVec(-0.51F, -0.38F, 0.8F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.375F, 
/*  787 */             (Vector3fc)KeyframeAnimations.posVec(-0.51F, -0.38F, 0.8F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4167F, 
/*  788 */             (Vector3fc)KeyframeAnimations.posVec(-2.14094F, 0.69619F, 1.23422F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  789 */             (Vector3fc)KeyframeAnimations.posVec(-0.97932F, 0.38244F, 0.12884F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/*  790 */             (Vector3fc)KeyframeAnimations.posVec(-1.55232F, 1.79904F, 0.37956F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.625F, 
/*  791 */             (Vector3fc)KeyframeAnimations.posVec(-1.53125F, 1.64598F, 1.41168F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6667F, 
/*  792 */             (Vector3fc)KeyframeAnimations.posVec(-1.57256F, 1.05375F, 1.32469F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.75F, 
/*  793 */             (Vector3fc)KeyframeAnimations.posVec(-1.33F, 0.16F, 1.02F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7917F, 
/*  794 */             (Vector3fc)KeyframeAnimations.posVec(-1.33F, 0.16F, 1.02F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.8333F, 
/*  795 */             (Vector3fc)KeyframeAnimations.posVec(-1.33F, 0.16F, 1.02F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.875F, 
/*  796 */             (Vector3fc)KeyframeAnimations.posVec(-1.33F, 0.16F, 1.02F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9167F, 
/*  797 */             (Vector3fc)KeyframeAnimations.posVec(-0.5748F, 0.38848F, 1.45646F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  798 */             (Vector3fc)KeyframeAnimations.posVec(-0.67F, 0.08F, 0.51F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  799 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  801 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  802 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  803 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  804 */             (Vector3fc)KeyframeAnimations.degreeVec(25.0F, 0.0F, -37.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  805 */             (Vector3fc)KeyframeAnimations.degreeVec(-21.59341F, -12.60837F, -45.69252F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  806 */             (Vector3fc)KeyframeAnimations.degreeVec(-120.7755F, -5.21988F, -2.02064F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  807 */             (Vector3fc)KeyframeAnimations.degreeVec(-98.27419F, -1.79323F, -1.15048F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  808 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.27F, -1.79F, -1.15F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5417F, 
/*  809 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.27419F, -1.79323F, -1.15048F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  810 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/*  811 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/*  812 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/*  813 */             (Vector3fc)KeyframeAnimations.degreeVec(-95.75F, -2.42F, 5.97F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/*  814 */             (Vector3fc)KeyframeAnimations.degreeVec(-98.4029F, -17.39503F, 6.85104F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/*  815 */             (Vector3fc)KeyframeAnimations.degreeVec(-101.24523F, -29.87096F, 7.69993F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/*  816 */             (Vector3fc)KeyframeAnimations.degreeVec(-101.25F, -29.87F, 7.7F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  817 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/*  818 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.9583F, 
/*  819 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/*  820 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2083F, 
/*  821 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.58526F, -17.10045F, 11.7676F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  822 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.59F, -17.1F, 11.77F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4167F, 
/*  823 */             (Vector3fc)KeyframeAnimations.degreeVec(-46.59531F, -16.13694F, -3.85578F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/*  824 */             (Vector3fc)KeyframeAnimations.degreeVec(-24.5317F, -19.0214F, -13.70805F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.8333F, 
/*  825 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  826 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  828 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  829 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  830 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  831 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  832 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/*  833 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/*  834 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  835 */             (Vector3fc)KeyframeAnimations.posVec(-0.00677F, -0.76064F, 3.19059F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  836 */             (Vector3fc)KeyframeAnimations.posVec(0.0512F, -0.76176F, 3.12882F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.8333F, 
/*  837 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  838 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  840 */         })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/*  841 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  842 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  843 */             (Vector3fc)KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/*  844 */             (Vector3fc)KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7083F, 
/*  845 */             (Vector3fc)KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7917F, 
/*  846 */             (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.8333F, 
/*  847 */             (Vector3fc)KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.875F, 
/*  848 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  849 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  851 */         })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/*  852 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  853 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  854 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/*  855 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7083F, 
/*  856 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.7917F, 
/*  857 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.09F, -0.11F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.8333F, 
/*  858 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  859 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  861 */         })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  862 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  863 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  864 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  865 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  866 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  867 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4167F, 
/*  868 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  869 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/*  870 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.625F, 
/*  871 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  872 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  874 */         })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/*  875 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  876 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  877 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  878 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  879 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  880 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4167F, 
/*  881 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  882 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/*  883 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.625F, 
/*  884 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  885 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  887 */         })).build();
/*      */   
/*  889 */   public static final AnimationDefinition COPPER_GOLEM_CHEST_INTERACTION_NOITEM_NOGET = AnimationDefinition.Builder.withLength(3.0F)
/*  890 */     .looping()
/*  891 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  892 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  893 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  894 */             (Vector3fc)KeyframeAnimations.degreeVec(18.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  895 */             (Vector3fc)KeyframeAnimations.degreeVec(24.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  896 */             (Vector3fc)KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  897 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  898 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  899 */             (Vector3fc)KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/*  900 */             (Vector3fc)KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9167F, 
/*  901 */             (Vector3fc)KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0417F, 
/*  902 */             (Vector3fc)KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  903 */             (Vector3fc)KeyframeAnimations.degreeVec(12.40525F, -4.4E-4F, 0.00829F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/*  904 */             (Vector3fc)KeyframeAnimations.degreeVec(12.40525F, -4.4E-4F, 0.00829F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/*  905 */             (Vector3fc)KeyframeAnimations.degreeVec(13.92716F, 26.80536F, 6.38918F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.625F, 
/*  906 */             (Vector3fc)KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7083F, 
/*  907 */             (Vector3fc)KeyframeAnimations.degreeVec(12.40725F, 0.00444F, 0.00783F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  908 */             (Vector3fc)KeyframeAnimations.degreeVec(12.40725F, 0.00444F, 0.00783F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.125F, 
/*  909 */             (Vector3fc)KeyframeAnimations.degreeVec(12.40725F, 0.0F, 0.00783F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  910 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  911 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/*  912 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6667F, 
/*  913 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  914 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  916 */         })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/*  917 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  918 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  919 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  920 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.625F, 
/*  921 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.4F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7083F, 
/*  922 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.34F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  923 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.34F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  924 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/*  925 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/*  926 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6667F, 
/*  927 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  928 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  930 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  931 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  932 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  933 */             (Vector3fc)KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  934 */             (Vector3fc)KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  935 */             (Vector3fc)KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  936 */             (Vector3fc)KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  937 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  938 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  939 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.6667F, 
/*  940 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  941 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/*  942 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0417F, 
/*  943 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/*  944 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/*  945 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 27.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/*  946 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4167F, 
/*  947 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4583F, 
/*  948 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/*  949 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/*  950 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.625F, 
/*  951 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7083F, 
/*  952 */             (Vector3fc)KeyframeAnimations.degreeVec(0.57F, -1.25F, 0.07F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, 
/*  953 */             (Vector3fc)KeyframeAnimations.degreeVec(0.89798F, -18.12465F, -0.16276F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7917F, 
/*  954 */             (Vector3fc)KeyframeAnimations.degreeVec(1.21328F, -21.15422F, -0.2148F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.875F, 
/*  955 */             (Vector3fc)KeyframeAnimations.degreeVec(1.21328F, -21.15422F, -0.2148F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/*  956 */             (Vector3fc)KeyframeAnimations.degreeVec(1.21328F, -21.15422F, -0.2148F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/*  957 */             (Vector3fc)KeyframeAnimations.degreeVec(2.56546F, 0.76525F, 0.57246F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/*  958 */             (Vector3fc)KeyframeAnimations.degreeVec(4.53867F, 7.47675F, 0.59181F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/*  959 */             (Vector3fc)KeyframeAnimations.degreeVec(4.53867F, 7.47675F, 0.59181F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/*  960 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  961 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  962 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  964 */         })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/*  965 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  966 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  967 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.15451F, 0.47553F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  968 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  969 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0417F, 
/*  970 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/*  971 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/*  972 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4167F, 
/*  973 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4583F, 
/*  974 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, 
/*  975 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/*  976 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.625F, 
/*  977 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.7083F, 
/*  978 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/*  979 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  980 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.01F, -0.03F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  981 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/*  983 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/*  984 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  985 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  986 */             (Vector3fc)KeyframeAnimations.degreeVec(-7.38733F, 1.29876F, 9.91615F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  987 */             (Vector3fc)KeyframeAnimations.degreeVec(-7.38733F, 1.29876F, 9.91615F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  988 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 32.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  989 */             (Vector3fc)KeyframeAnimations.degreeVec(-34.55418F, 11.73507F, 36.8361F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, 
/*  990 */             (Vector3fc)KeyframeAnimations.degreeVec(-82.47403F, 17.82361F, 2.17224F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  991 */             (Vector3fc)KeyframeAnimations.degreeVec(-85.08388F, 14.26971F, 1.99595F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5417F, 
/*  992 */             (Vector3fc)KeyframeAnimations.degreeVec(-85.16266F, 13.19102F, 2.43976F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/*  993 */             (Vector3fc)KeyframeAnimations.degreeVec(-92.79F, 0.73F, 1.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/*  994 */             (Vector3fc)KeyframeAnimations.degreeVec(-92.79F, 0.73F, 1.39F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/*  995 */             (Vector3fc)KeyframeAnimations.degreeVec(-95.83405F, 33.18639F, -0.40081F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/*  996 */             (Vector3fc)KeyframeAnimations.degreeVec(-95.83F, 33.19F, -0.4F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/*  997 */             (Vector3fc)KeyframeAnimations.degreeVec(-98.33F, 33.19F, -0.4F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5417F, 
/*  998 */             (Vector3fc)KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/*  999 */             (Vector3fc)KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/* 1000 */             (Vector3fc)KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 1001 */             (Vector3fc)KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/* 1002 */             (Vector3fc)KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/* 1003 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/* 1004 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/* 1005 */             (Vector3fc)KeyframeAnimations.degreeVec(3.9F, -4.38F, 3.36F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9167F, 
/* 1006 */             (Vector3fc)KeyframeAnimations.degreeVec(3.9F, -4.38F, 3.36F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/* 1007 */             (Vector3fc)KeyframeAnimations.degreeVec(3.90089F, -4.3843F, 3.35549F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/* 1008 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/* 1010 */         })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/* 1011 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/* 1012 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/* 1013 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/* 1014 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/* 1015 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 1016 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, 
/* 1017 */             (Vector3fc)KeyframeAnimations.posVec(0.25358F, -0.20153F, 2.21248F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 1018 */             (Vector3fc)KeyframeAnimations.posVec(0.25F, -0.2F, 2.21F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/* 1019 */             (Vector3fc)KeyframeAnimations.posVec(0.25F, -0.2F, 2.21F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5417F, 
/* 1020 */             (Vector3fc)KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/* 1021 */             (Vector3fc)KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/* 1022 */             (Vector3fc)KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0F, 
/* 1023 */             (Vector3fc)KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/* 1024 */             (Vector3fc)KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/* 1025 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/* 1026 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/* 1027 */             (Vector3fc)KeyframeAnimations.posVec(-0.46F, -0.88F, -0.3F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9167F, 
/* 1028 */             (Vector3fc)KeyframeAnimations.posVec(-0.46F, -0.88F, -0.3F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/* 1029 */             (Vector3fc)KeyframeAnimations.posVec(-0.46F, 0.1159F, -0.30086F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/* 1030 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/* 1032 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { 
/* 1033 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/* 1034 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/* 1035 */             (Vector3fc)KeyframeAnimations.degreeVec(25.0F, 0.0F, -37.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/* 1036 */             (Vector3fc)KeyframeAnimations.degreeVec(-21.59341F, -12.60837F, -45.69252F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/* 1037 */             (Vector3fc)KeyframeAnimations.degreeVec(-120.7755F, -5.21988F, -2.02064F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/* 1038 */             (Vector3fc)KeyframeAnimations.degreeVec(-98.27419F, -1.79323F, -1.15048F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/* 1039 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.27F, -1.79F, -1.15F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5417F, 
/* 1040 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.27419F, -1.79323F, -1.15048F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5833F, 
/* 1041 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/* 1042 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.1667F, 
/* 1043 */             (Vector3fc)KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/* 1044 */             (Vector3fc)KeyframeAnimations.degreeVec(-95.75F, -2.42F, 5.97F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 1045 */             (Vector3fc)KeyframeAnimations.degreeVec(-98.4029F, -17.39503F, 6.85104F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2917F, 
/* 1046 */             (Vector3fc)KeyframeAnimations.degreeVec(-101.24523F, -29.87096F, 7.69993F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/* 1047 */             (Vector3fc)KeyframeAnimations.degreeVec(-101.25F, -29.87F, 7.7F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/* 1048 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.8333F, 
/* 1049 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0833F, 
/* 1050 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.1667F, 
/* 1051 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2083F, 
/* 1052 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.58526F, -17.10045F, 11.7676F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/* 1053 */             (Vector3fc)KeyframeAnimations.degreeVec(-88.59F, -17.1F, 11.77F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4167F, 
/* 1054 */             (Vector3fc)KeyframeAnimations.degreeVec(-46.59531F, -16.13694F, -3.85578F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/* 1055 */             (Vector3fc)KeyframeAnimations.degreeVec(-24.5317F, -19.0214F, -13.70805F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/* 1056 */             (Vector3fc)KeyframeAnimations.degreeVec(-24.5317F, -19.0214F, -13.70805F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/* 1057 */             (Vector3fc)KeyframeAnimations.degreeVec(2.41F, -0.65F, -5.01F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9167F, 
/* 1058 */             (Vector3fc)KeyframeAnimations.degreeVec(2.41F, -0.65F, -5.01F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/* 1059 */             (Vector3fc)KeyframeAnimations.degreeVec(2.41492F, -0.64686F, -5.01363F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/* 1060 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/* 1062 */         })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { 
/* 1063 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/* 1064 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/* 1065 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/* 1066 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, 
/* 1067 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5833F, 
/* 1068 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.6667F, 
/* 1069 */             (Vector3fc)KeyframeAnimations.posVec(-0.00677F, -0.76064F, 3.19059F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/* 1070 */             (Vector3fc)KeyframeAnimations.posVec(0.0512F, -0.76176F, 3.12882F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4583F, 
/* 1071 */             (Vector3fc)KeyframeAnimations.posVec(0.03F, -0.51F, 2.09F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5F, 
/* 1072 */             (Vector3fc)KeyframeAnimations.posVec(0.03F, -0.51F, 2.09F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5417F, 
/* 1073 */             (Vector3fc)KeyframeAnimations.posVec(0.03F, -1.28F, -0.07F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9167F, 
/* 1074 */             (Vector3fc)KeyframeAnimations.posVec(0.03F, -1.28F, -0.07F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/* 1075 */             (Vector3fc)KeyframeAnimations.posVec(0.03F, -0.28229F, -0.07133F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/* 1076 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/* 1078 */         })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 1079 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/* 1080 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/* 1081 */             (Vector3fc)KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/* 1082 */             (Vector3fc)KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/* 1083 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/* 1084 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/* 1086 */         })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 1087 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/* 1088 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/* 1089 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/* 1090 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/* 1091 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/* 1092 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/* 1094 */         })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 1095 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/* 1096 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/* 1097 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/* 1098 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/* 1099 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/* 1100 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/* 1101 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/* 1103 */         })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] {
/* 1104 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/* 1105 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/* 1106 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.0417F, 
/* 1107 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.2917F, 
/* 1108 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/* 1109 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/* 1110 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*      */         
/* 1112 */         })).build();
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/animation/definitions/CopperGolemAnimation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */