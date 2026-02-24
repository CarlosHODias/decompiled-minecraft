/*   */ package net.minecraft.client.renderer.entity.state;
/*   */ 
/*   */ import net.minecraft.world.entity.HumanoidArm;
/*   */ import net.minecraft.world.item.ItemStack;
/*   */ 
/*   */ public class UndeadRenderState
/*   */   extends HumanoidRenderState {
/*   */   public ItemStack getUseItemStackForArm(HumanoidArm arm) {
/* 9 */     return getMainHandItemStack();
/*   */   }
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/UndeadRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */