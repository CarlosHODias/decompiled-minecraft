/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.OptionalInt;
/*    */ import net.minecraft.client.gui.ActiveTextCollector;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.TextAlignment;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.SingleKeyCache;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class MultiLineTextWidget extends AbstractStringWidget {
/* 13 */   private OptionalInt maxWidth = OptionalInt.empty();
/* 14 */   private OptionalInt maxRows = OptionalInt.empty();
/*    */   private final SingleKeyCache<CacheKey, MultiLineLabel> cache;
/*    */   private boolean centered = false;
/*    */   
/*    */   public MultiLineTextWidget(Component message, Font font) {
/* 19 */     this(0, 0, message, font);
/*    */   }
/*    */   
/*    */   public MultiLineTextWidget(int x, int y, Component message, Font font) {
/* 23 */     super(x, y, 0, 0, message, font);
/* 24 */     this.cache = Util.singleKeyCache(key -> key.maxRows.isPresent() ? MultiLineLabel.create(font, key.maxWidth, key.maxRows.getAsInt(), new Component[] { key.message }) : MultiLineLabel.create(font, key.message, key.maxWidth));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 31 */     this.active = false;
/*    */   }
/*    */   
/*    */   public MultiLineTextWidget setMaxWidth(int maxWidth) {
/* 35 */     this.maxWidth = OptionalInt.of(maxWidth);
/* 36 */     return this;
/*    */   }
/*    */   
/*    */   public MultiLineTextWidget setMaxRows(int maxRows) {
/* 40 */     this.maxRows = OptionalInt.of(maxRows);
/* 41 */     return this;
/*    */   }
/*    */   
/*    */   public MultiLineTextWidget setCentered(boolean centered) {
/* 45 */     this.centered = centered;
/* 46 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getWidth() {
/* 51 */     return ((MultiLineLabel)this.cache.getValue(getFreshCacheKey())).getWidth();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 56 */     Objects.requireNonNull(getFont()); return ((MultiLineLabel)this.cache.getValue(getFreshCacheKey())).getLineCount() * 9;
/*    */   }
/*    */ 
/*    */   
/*    */   public void visitLines(ActiveTextCollector output) {
/* 61 */     MultiLineLabel multilineLabel = (MultiLineLabel)this.cache.getValue(getFreshCacheKey());
/* 62 */     int x = getTextX();
/* 63 */     int y = getTextY();
/* 64 */     Objects.requireNonNull(getFont()); int lineHeight = 9;
/* 65 */     if (this.centered) {
/* 66 */       int midX = getX() + getWidth() / 2;
/* 67 */       multilineLabel.visitLines(TextAlignment.CENTER, midX, y, lineHeight, output);
/*    */     } else {
/* 69 */       multilineLabel.visitLines(TextAlignment.LEFT, x, y, lineHeight, output);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected int getTextX() {
/* 74 */     return getX();
/*    */   }
/*    */   
/*    */   protected int getTextY() {
/* 78 */     return getY();
/*    */   }
/*    */   
/*    */   private CacheKey getFreshCacheKey() {
/* 82 */     return new CacheKey(getMessage(), this.maxWidth.orElse(Integer.MAX_VALUE), this.maxRows);
/*    */   }
/*    */   private static final class CacheKey extends Record { private final Component message; private final int maxWidth; private final OptionalInt maxRows;
/* 85 */     private CacheKey(Component message, int maxWidth, OptionalInt maxRows) { this.message = message; this.maxWidth = maxWidth; this.maxRows = maxRows; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/MultiLineTextWidget$CacheKey;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #85	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 85 */       //   0	7	0	this	Lnet/minecraft/client/gui/components/MultiLineTextWidget$CacheKey; } public Component message() { return this.message; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/MultiLineTextWidget$CacheKey;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #85	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/gui/components/MultiLineTextWidget$CacheKey; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/MultiLineTextWidget$CacheKey;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #85	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/gui/components/MultiLineTextWidget$CacheKey;
/* 85 */       //   0	8	1	o	Ljava/lang/Object; } public int maxWidth() { return this.maxWidth; } public OptionalInt maxRows() { return this.maxRows; }
/*    */      }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/MultiLineTextWidget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */