/*    */ package net.minecraft.realms;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.components.Renderable;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class RealmsLabel implements Renderable {
/*    */   private final Component text;
/*    */   private final int x;
/*    */   private final int y;
/*    */   private final int color;
/*    */   
/*    */   public RealmsLabel(Component text, int x, int y, int color) {
/* 15 */     this.text = text;
/* 16 */     this.x = x;
/* 17 */     this.y = y;
/* 18 */     this.color = color;
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 23 */     graphics.drawCenteredString((Minecraft.getInstance()).font, this.text, this.x, this.y, this.color);
/*    */   }
/*    */   
/*    */   public Component getText() {
/* 27 */     return this.text;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/realms/RealmsLabel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */