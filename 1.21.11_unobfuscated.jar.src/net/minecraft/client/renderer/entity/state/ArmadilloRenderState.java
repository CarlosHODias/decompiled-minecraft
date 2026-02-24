/*   */ package net.minecraft.client.renderer.entity.state;
/*   */ 
/*   */ import net.minecraft.world.entity.AnimationState;
/*   */ 
/*   */ public class ArmadilloRenderState extends LivingEntityRenderState {
/*   */   public boolean isHidingInShell;
/* 7 */   public final AnimationState rollOutAnimationState = new AnimationState();
/* 8 */   public final AnimationState rollUpAnimationState = new AnimationState();
/* 9 */   public final AnimationState peekAnimationState = new AnimationState();
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/ArmadilloRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */