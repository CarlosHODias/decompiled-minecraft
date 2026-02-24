/*    */ package net.minecraft.client.renderer.blockentity.state;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public class CampfireRenderState
/*    */   extends BlockEntityRenderState {
/* 10 */   public List<ItemStackRenderState> items = Collections.emptyList();
/* 11 */   public Direction facing = Direction.NORTH;
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/state/CampfireRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */