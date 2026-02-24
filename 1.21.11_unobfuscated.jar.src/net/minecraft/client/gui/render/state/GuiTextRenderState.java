/*    */ package net.minecraft.client.gui.render.state;
/*    */ 
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ import org.joml.Matrix3x2fc;
/*    */ 
/*    */ 
/*    */ public final class GuiTextRenderState
/*    */   implements ScreenArea
/*    */ {
/*    */   public final Font font;
/*    */   public final FormattedCharSequence text;
/*    */   public final Matrix3x2fc pose;
/*    */   public final int x;
/*    */   public final int y;
/*    */   public final int color;
/*    */   public final int backgroundColor;
/*    */   public final boolean dropShadow;
/*    */   final boolean includeEmpty;
/*    */   public final ScreenRectangle scissor;
/*    */   private Font.PreparedText preparedText;
/*    */   private ScreenRectangle bounds;
/*    */   
/*    */   public GuiTextRenderState(Font font, FormattedCharSequence text, Matrix3x2fc pose, int x, int y, int color, int backgroundColor, boolean dropShadow, boolean includeEmpty, ScreenRectangle scissor) {
/* 26 */     this.font = font;
/* 27 */     this.text = text;
/* 28 */     this.pose = pose;
/* 29 */     this.x = x;
/* 30 */     this.y = y;
/* 31 */     this.color = color;
/* 32 */     this.backgroundColor = backgroundColor;
/* 33 */     this.dropShadow = dropShadow;
/* 34 */     this.includeEmpty = includeEmpty;
/* 35 */     this.scissor = scissor;
/*    */   }
/*    */   
/*    */   public Font.PreparedText ensurePrepared() {
/* 39 */     if (this.preparedText == null) {
/* 40 */       this.preparedText = this.font.prepareText(this.text, this.x, this.y, this.color, this.dropShadow, this.includeEmpty, this.backgroundColor);
/* 41 */       ScreenRectangle bounds = this.preparedText.bounds();
/* 42 */       if (bounds != null) {
/* 43 */         bounds = bounds.transformMaxBounds(this.pose);
/* 44 */         this.bounds = (this.scissor != null) ? this.scissor.intersection(bounds) : bounds;
/*    */       } 
/*    */     } 
/* 47 */     return this.preparedText;
/*    */   }
/*    */ 
/*    */   
/*    */   public ScreenRectangle bounds() {
/* 52 */     ensurePrepared();
/* 53 */     return this.bounds;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/state/GuiTextRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */