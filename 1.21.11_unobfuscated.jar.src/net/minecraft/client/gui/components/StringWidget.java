/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.gui.ActiveTextCollector;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.locale.Language;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ 
/*    */ public class StringWidget extends AbstractStringWidget {
/*    */   private static final int TEXT_MARGIN = 2;
/* 14 */   private int maxWidth = 0;
/* 15 */   private int cachedWidth = 0;
/*    */   
/*    */   private boolean cachedWidthDirty = true;
/* 18 */   private TextOverflow textOverflow = TextOverflow.CLAMPED;
/*    */   
/*    */   public StringWidget(Component message, Font font) {
/* 21 */     this(0, 0, font.width(message.getVisualOrderText()), 9, message, font);
/*    */   }
/*    */   
/*    */   public StringWidget(int width, int height, Component message, Font font) {
/* 25 */     this(0, 0, width, height, message, font);
/*    */   }
/*    */   
/*    */   public StringWidget(int x, int y, int width, int height, Component message, Font font) {
/* 29 */     super(x, y, width, height, message, font);
/* 30 */     this.active = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setMessage(Component message) {
/* 35 */     super.setMessage(message);
/* 36 */     this.cachedWidthDirty = true;
/*    */   }
/*    */   
/*    */   public StringWidget setMaxWidth(int maxWidth) {
/* 40 */     return setMaxWidth(maxWidth, TextOverflow.CLAMPED);
/*    */   }
/*    */   
/*    */   public StringWidget setMaxWidth(int maxWidth, TextOverflow textOverflow) {
/* 44 */     this.maxWidth = maxWidth;
/* 45 */     this.textOverflow = textOverflow;
/* 46 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getWidth() {
/* 51 */     if (this.maxWidth > 0) {
/* 52 */       if (this.cachedWidthDirty) {
/* 53 */         this.cachedWidth = Math.min(this.maxWidth, getFont().width(getMessage().getVisualOrderText()));
/* 54 */         this.cachedWidthDirty = false;
/*    */       } 
/* 56 */       return this.cachedWidth;
/*    */     } 
/* 58 */     return super.getWidth();
/*    */   }
/*    */ 
/*    */   
/*    */   public void visitLines(ActiveTextCollector output) {
/* 63 */     Component message = getMessage();
/* 64 */     Font font = getFont();
/* 65 */     int maxWidth = (this.maxWidth > 0) ? this.maxWidth : getWidth();
/* 66 */     int textWidth = font.width((FormattedText)message);
/* 67 */     int x = getX();
/* 68 */     Objects.requireNonNull(font); int y = getY() + (getHeight() - 9) / 2;
/*    */     
/* 70 */     boolean textOverflow = (textWidth > maxWidth);
/* 71 */     if (textOverflow) {
/* 72 */       switch (this.textOverflow.ordinal()) { case 0:
/* 73 */           output.accept(x, y, clipText(message, font, maxWidth)); break;
/* 74 */         case 1: renderScrollingStringOverContents(output, message, 2); break; }
/*    */     
/*    */     } else {
/* 77 */       output.accept(x, y, message.getVisualOrderText());
/*    */     } 
/*    */   }
/*    */   
/*    */   public static FormattedCharSequence clipText(Component text, Font font, int width) {
/* 82 */     FormattedText clippedText = font.substrByWidth((FormattedText)text, width - font.width((FormattedText)CommonComponents.ELLIPSIS));
/* 83 */     return Language.getInstance().getVisualOrder(FormattedText.composite(new FormattedText[] { clippedText, (FormattedText)CommonComponents.ELLIPSIS }));
/*    */   }
/*    */   
/*    */   public enum TextOverflow {
/* 87 */     CLAMPED,
/* 88 */     SCROLLING;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/StringWidget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */