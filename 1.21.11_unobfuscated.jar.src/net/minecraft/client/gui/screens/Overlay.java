/*   */ package net.minecraft.client.gui.screens;
/*   */ 
/*   */ import net.minecraft.client.gui.components.Renderable;
/*   */ 
/*   */ public abstract class Overlay implements Renderable {
/*   */   public boolean isPauseScreen() {
/* 7 */     return true;
/*   */   }
/*   */   
/*   */   public void tick() {}
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/Overlay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */