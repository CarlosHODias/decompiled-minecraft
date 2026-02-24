/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.world.entity.AnimationState;
/*    */ 
/*    */ public class SnifferRenderState extends LivingEntityRenderState {
/*    */   public boolean isSearching;
/*  7 */   public final AnimationState diggingAnimationState = new AnimationState();
/*  8 */   public final AnimationState sniffingAnimationState = new AnimationState();
/*  9 */   public final AnimationState risingAnimationState = new AnimationState();
/* 10 */   public final AnimationState feelingHappyAnimationState = new AnimationState();
/* 11 */   public final AnimationState scentingAnimationState = new AnimationState();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/SnifferRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */