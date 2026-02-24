/*   */ package net.minecraft.client.renderer.entity.state;
/*   */ 
/*   */ import net.minecraft.world.entity.animal.equine.Llama;
/*   */ import net.minecraft.world.item.ItemStack;
/*   */ 
/*   */ public class LlamaRenderState extends LivingEntityRenderState {
/* 7 */   public Llama.Variant variant = Llama.Variant.DEFAULT;
/*   */   public boolean hasChest;
/* 9 */   public ItemStack bodyItem = ItemStack.EMPTY;
/*   */   public boolean isTraderLlama;
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/LlamaRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */