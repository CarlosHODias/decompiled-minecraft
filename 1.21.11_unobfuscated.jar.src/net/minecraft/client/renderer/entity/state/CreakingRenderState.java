/*   */ package net.minecraft.client.renderer.entity.state;
/*   */ 
/*   */ import net.minecraft.world.entity.AnimationState;
/*   */ 
/*   */ public class CreakingRenderState extends LivingEntityRenderState {
/* 6 */   public final AnimationState invulnerabilityAnimationState = new AnimationState();
/* 7 */   public final AnimationState attackAnimationState = new AnimationState();
/* 8 */   public final AnimationState deathAnimationState = new AnimationState();
/*   */   public boolean eyesGlowing;
/*   */   public boolean canMove;
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/CreakingRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */