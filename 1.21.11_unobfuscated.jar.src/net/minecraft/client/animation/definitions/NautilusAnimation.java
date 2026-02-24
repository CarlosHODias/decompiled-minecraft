/*    */ package net.minecraft.client.animation.definitions;
/*    */ 
/*    */ import net.minecraft.client.animation.AnimationChannel;
/*    */ import net.minecraft.client.animation.AnimationDefinition;
/*    */ import net.minecraft.client.animation.Keyframe;
/*    */ import net.minecraft.client.animation.KeyframeAnimations;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class NautilusAnimation
/*    */ {
/* 11 */   public static final AnimationDefinition SWIMMING = AnimationDefinition.Builder.withLength(1.0F).looping()
/* 12 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] {
/* 13 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 14 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.2000000476837158D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 15 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 0.8999999761581421D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.875F, 
/* 16 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 17 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR)
/*    */         
/* 19 */         })).addAnimation("upper_mouth", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 20 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 21 */             (Vector3fc)KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 22 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.875F, 
/* 23 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 24 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*    */         
/* 26 */         })).addAnimation("upper_mouth", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] {
/* 27 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 28 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.399999976158142D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 29 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 0.8999999761581421D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.875F, 
/* 30 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 31 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR)
/*    */         
/* 33 */         })).addAnimation("inner_mouth", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] {
/* 34 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 35 */             (Vector3fc)KeyframeAnimations.scaleVec(0.800000011920929D, 0.800000011920929D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 36 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 0.8999999761581421D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.875F, 
/* 37 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 38 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR)
/*    */         
/* 40 */         })).addAnimation("lower_mouth", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] {
/* 41 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 42 */             (Vector3fc)KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 43 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.875F, 
/* 44 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 45 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
/*    */         
/* 47 */         })).addAnimation("lower_mouth", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] {
/* 48 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 49 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.399999976158142D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.75F, 
/* 50 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 0.8999999761581421D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.875F, 
/* 51 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, 
/* 52 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR)
/*    */         
/* 54 */         })).build();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/animation/definitions/NautilusAnimation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */