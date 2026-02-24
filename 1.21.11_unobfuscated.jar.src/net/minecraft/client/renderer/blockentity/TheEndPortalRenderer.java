/*   */ package net.minecraft.client.renderer.blockentity;
/*   */ 
/*   */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*   */ import net.minecraft.client.renderer.blockentity.state.EndPortalRenderState;
/*   */ import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
/*   */ 
/*   */ public class TheEndPortalRenderer extends AbstractEndPortalRenderer<TheEndPortalBlockEntity, EndPortalRenderState> {
/*   */   public EndPortalRenderState createRenderState() {
/* 9 */     return new EndPortalRenderState();
/*   */   }
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/TheEndPortalRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */