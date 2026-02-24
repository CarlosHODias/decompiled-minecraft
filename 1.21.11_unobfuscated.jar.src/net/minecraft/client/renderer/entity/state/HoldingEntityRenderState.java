/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ 
/*    */ 
/*    */ public class HoldingEntityRenderState
/*    */   extends LivingEntityRenderState
/*    */ {
/* 12 */   public final ItemStackRenderState heldItem = new ItemStackRenderState();
/*    */   
/*    */   public static void extractHoldingEntityRenderState(LivingEntity entity, HoldingEntityRenderState state, ItemModelResolver itemModelResolver) {
/* 15 */     itemModelResolver.updateForLiving(state.heldItem, entity.getMainHandItem(), ItemDisplayContext.GROUND, entity);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/HoldingEntityRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */