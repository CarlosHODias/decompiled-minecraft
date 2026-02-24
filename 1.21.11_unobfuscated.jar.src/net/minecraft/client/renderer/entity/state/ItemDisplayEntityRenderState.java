/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ 
/*    */ public class ItemDisplayEntityRenderState extends DisplayEntityRenderState {
/*  6 */   public final ItemStackRenderState item = new ItemStackRenderState();
/*    */ 
/*    */   
/*    */   public boolean hasSubState() {
/* 10 */     return !this.item.isEmpty();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/ItemDisplayEntityRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */