/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.world.entity.Display;
/*    */ 
/*    */ public class BlockDisplayEntityRenderState
/*    */   extends DisplayEntityRenderState
/*    */ {
/*    */   public Display.BlockDisplay.BlockRenderState blockRenderState;
/*    */   
/*    */   public boolean hasSubState() {
/* 11 */     return (this.blockRenderState != null);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/BlockDisplayEntityRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */