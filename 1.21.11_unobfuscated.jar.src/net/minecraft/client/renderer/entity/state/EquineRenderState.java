/*   */ package net.minecraft.client.renderer.entity.state;
/*   */ 
/*   */ import net.minecraft.world.item.ItemStack;
/*   */ 
/*   */ public class EquineRenderState extends LivingEntityRenderState {
/* 6 */   public ItemStack saddle = ItemStack.EMPTY;
/* 7 */   public ItemStack bodyArmorItem = ItemStack.EMPTY;
/*   */   public boolean isRidden;
/*   */   public boolean animateTail;
/*   */   public float eatAnimation;
/*   */   public float standAnimation;
/*   */   public float feedingAnimation;
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/EquineRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */