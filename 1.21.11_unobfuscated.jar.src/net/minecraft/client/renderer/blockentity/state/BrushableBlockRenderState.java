/*   */ package net.minecraft.client.renderer.blockentity.state;
/*   */ 
/*   */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*   */ import net.minecraft.core.Direction;
/*   */ 
/*   */ public class BrushableBlockRenderState
/*   */   extends BlockEntityRenderState {
/* 8 */   public ItemStackRenderState itemState = new ItemStackRenderState();
/*   */   public int dustProgress;
/*   */   public Direction hitDirection;
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/state/BrushableBlockRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */