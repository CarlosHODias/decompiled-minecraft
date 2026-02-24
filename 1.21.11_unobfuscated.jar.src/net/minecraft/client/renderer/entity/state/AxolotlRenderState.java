/*   */ package net.minecraft.client.renderer.entity.state;
/*   */ 
/*   */ import net.minecraft.world.entity.animal.axolotl.Axolotl;
/*   */ 
/*   */ public class AxolotlRenderState extends LivingEntityRenderState {
/* 6 */   public Axolotl.Variant variant = Axolotl.Variant.DEFAULT;
/*   */   public float playingDeadFactor;
/*   */   public float movingFactor;
/* 9 */   public float inWaterFactor = 1.0F;
/*   */   public float onGroundFactor;
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/AxolotlRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */