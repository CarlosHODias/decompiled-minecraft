/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.world.entity.Display;
/*    */ 
/*    */ 
/*    */ public class TextDisplayEntityRenderState
/*    */   extends DisplayEntityRenderState
/*    */ {
/*    */   public Display.TextDisplay.TextRenderState textRenderState;
/*    */   public Display.TextDisplay.CachedInfo cachedInfo;
/*    */   
/*    */   public boolean hasSubState() {
/* 13 */     return (this.textRenderState != null);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/TextDisplayEntityRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */