/*   */ package net.minecraft.client.renderer.entity.state;
/*   */ 
/*   */ import net.minecraft.world.phys.Vec3;
/*   */ 
/*   */ public class GuardianRenderState
/*   */   extends LivingEntityRenderState {
/*   */   public float spikesAnimation;
/*   */   public float tailAnimation;
/* 9 */   public Vec3 eyePosition = Vec3.ZERO;
/*   */   public Vec3 lookDirection;
/*   */   public Vec3 lookAtPosition;
/*   */   public Vec3 attackTargetPosition;
/*   */   public float attackTime;
/*   */   public float attackScale;
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/GuardianRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */