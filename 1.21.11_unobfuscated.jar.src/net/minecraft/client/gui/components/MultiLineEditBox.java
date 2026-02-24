/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.input.CharacterEvent;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class MultiLineEditBox
/*     */   extends AbstractTextAreaWidget {
/*     */   private static final int CURSOR_INSERT_WIDTH = 1;
/*  23 */   private static final int PLACEHOLDER_TEXT_COLOR = ARGB.color(204, -2039584);
/*     */   
/*     */   private static final int CURSOR_COLOR = -3092272;
/*     */   private static final String CURSOR_APPEND_CHARACTER = "_";
/*     */   private static final int CURSOR_BLINK_INTERVAL_MS = 300;
/*     */   private final Font font;
/*     */   private final Component placeholder;
/*     */   private final MultilineTextField textField;
/*     */   private final int textColor;
/*     */   private final boolean textShadow;
/*     */   private final int cursorColor;
/*  34 */   private long focusedTime = Util.getMillis();
/*     */   
/*     */   private MultiLineEditBox(Font font, int x, int y, int width, int height, Component placeholder, Component narration, int textColor, boolean textShadow, int cursorColor, boolean showBackground, boolean showDecorations) {
/*  37 */     super(x, y, width, height, narration, showBackground, showDecorations);
/*  38 */     this.font = font;
/*  39 */     this.textShadow = textShadow;
/*  40 */     this.textColor = textColor;
/*  41 */     this.cursorColor = cursorColor;
/*  42 */     this.placeholder = placeholder;
/*  43 */     this.textField = new MultilineTextField(font, width - totalInnerPadding());
/*  44 */     this.textField.setCursorListener(this::scrollToCursor);
/*     */   }
/*     */   
/*     */   public void setCharacterLimit(int characterLimit) {
/*  48 */     this.textField.setCharacterLimit(characterLimit);
/*     */   }
/*     */   
/*     */   public void setLineLimit(int lineLimit) {
/*  52 */     this.textField.setLineLimit(lineLimit);
/*     */   }
/*     */   
/*     */   public void setValueListener(Consumer<String> valueListener) {
/*  56 */     this.textField.setValueListener(valueListener);
/*     */   }
/*     */   
/*     */   public void setValue(String value) {
/*  60 */     setValue(value, false);
/*     */   }
/*     */   
/*     */   public void setValue(String value, boolean allowOverflowLineLimit) {
/*  64 */     this.textField.setValue(value, allowOverflowLineLimit);
/*     */   }
/*     */   
/*     */   public String getValue() {
/*  68 */     return this.textField.value();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateWidgetNarration(NarrationElementOutput output) {
/*  73 */     output.add(NarratedElementType.TITLE, (Component)Component.translatable("gui.narrate.editBox", new Object[] { getMessage(), getValue() }));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClick(MouseButtonEvent event, boolean doubleClick) {
/*  78 */     if (doubleClick) {
/*  79 */       this.textField.selectWordAtCursor();
/*     */     } else {
/*  81 */       this.textField.setSelecting(event.hasShiftDown());
/*  82 */       seekCursorScreen(event.x(), event.y());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onDrag(MouseButtonEvent event, double dx, double dy) {
/*  88 */     this.textField.setSelecting(true);
/*  89 */     seekCursorScreen(event.x(), event.y());
/*  90 */     this.textField.setSelecting(event.hasShiftDown());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/*  95 */     return this.textField.keyPressed(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean charTyped(CharacterEvent event) {
/* 100 */     if (!this.visible || !isFocused() || !event.isAllowedChatCharacter()) {
/* 101 */       return false;
/*     */     }
/*     */     
/* 104 */     this.textField.insertText(event.codepointAsString());
/* 105 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 110 */     String value = this.textField.value();
/* 111 */     if (value.isEmpty() && !isFocused()) {
/* 112 */       graphics.drawWordWrap(this.font, (FormattedText)this.placeholder, getInnerLeft(), getInnerTop(), this.width - totalInnerPadding(), PLACEHOLDER_TEXT_COLOR);
/*     */       
/*     */       return;
/*     */     } 
/* 116 */     int cursor = this.textField.cursor();
/* 117 */     boolean showCursor = (isFocused() && (Util.getMillis() - this.focusedTime) / 300L % 2L == 0L);
/* 118 */     boolean insertCursor = (cursor < value.length());
/*     */     
/* 120 */     int cursorX = 0;
/* 121 */     int cursorY = 0;
/* 122 */     int drawTop = getInnerTop();
/*     */     boolean hasDrawnCursor = false;
/* 124 */     for (MultilineTextField.StringView lineView : this.textField.iterateLines()) {
/* 125 */       Objects.requireNonNull(this.font); boolean lineWithinVisibleBounds = withinContentAreaTopBottom(drawTop, drawTop + 9);
/*     */       
/* 127 */       int innerLeft = getInnerLeft();
/* 128 */       if (showCursor && insertCursor && cursor >= lineView.beginIndex() && cursor <= lineView.endIndex()) {
/* 129 */         if (lineWithinVisibleBounds) {
/* 130 */           String substring = value.substring(lineView.beginIndex(), cursor);
/* 131 */           graphics.drawString(this.font, substring, innerLeft, drawTop, this.textColor, this.textShadow);
/* 132 */           cursorX = innerLeft + this.font.width(substring);
/* 133 */           if (!hasDrawnCursor) {
/* 134 */             Objects.requireNonNull(this.font); graphics.fill(cursorX, drawTop - 1, cursorX + 1, drawTop + 1 + 9, this.cursorColor);
/* 135 */             hasDrawnCursor = true;
/*     */           } 
/* 137 */           graphics.drawString(this.font, value.substring(cursor, lineView.endIndex()), cursorX, drawTop, this.textColor, this.textShadow);
/*     */         } 
/*     */       } else {
/* 140 */         if (lineWithinVisibleBounds) {
/* 141 */           String substring = value.substring(lineView.beginIndex(), lineView.endIndex());
/* 142 */           graphics.drawString(this.font, substring, innerLeft, drawTop, this.textColor, this.textShadow);
/* 143 */           cursorX = innerLeft + this.font.width(substring) - 1;
/*     */         } 
/* 145 */         cursorY = drawTop;
/*     */       } 
/*     */       
/* 148 */       Objects.requireNonNull(this.font); drawTop += 9;
/*     */     } 
/*     */     
/* 151 */     Objects.requireNonNull(this.font); if (showCursor && !insertCursor && withinContentAreaTopBottom(cursorY, cursorY + 9)) {
/* 152 */       graphics.drawString(this.font, "_", cursorX + 1, cursorY, this.cursorColor, this.textShadow);
/*     */     }
/*     */     
/* 155 */     if (this.textField.hasSelection()) {
/* 156 */       MultilineTextField.StringView selection = this.textField.getSelected();
/*     */       
/* 158 */       int drawX = getInnerLeft();
/* 159 */       drawTop = getInnerTop();
/* 160 */       for (MultilineTextField.StringView lineView : this.textField.iterateLines()) {
/* 161 */         if (selection.beginIndex() > lineView.endIndex()) {
/* 162 */           Objects.requireNonNull(this.font); drawTop += 9; continue;
/*     */         } 
/* 164 */         if (lineView.beginIndex() > selection.endIndex()) {
/*     */           break;
/*     */         }
/*     */         
/* 168 */         Objects.requireNonNull(this.font); if (withinContentAreaTopBottom(drawTop, drawTop + 9)) {
/* 169 */           int drawEnd; int drawBegin = this.font.width(value.substring(lineView.beginIndex(), Math.max(selection.beginIndex(), lineView.beginIndex())));
/*     */           
/* 171 */           if (selection.endIndex() > lineView.endIndex()) {
/* 172 */             drawEnd = this.width - innerPadding();
/*     */           } else {
/* 174 */             drawEnd = this.font.width(value.substring(lineView.beginIndex(), selection.endIndex()));
/*     */           } 
/* 176 */           Objects.requireNonNull(this.font); graphics.textHighlight(drawX + drawBegin, drawTop, drawX + drawEnd, drawTop + 9, true);
/*     */         } 
/*     */         
/* 179 */         Objects.requireNonNull(this.font); drawTop += 9;
/*     */       } 
/*     */     } 
/*     */     
/* 183 */     if (isHovered()) {
/* 184 */       graphics.requestCursor(CursorTypes.IBEAM);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderDecorations(GuiGraphics graphics) {
/* 190 */     super.renderDecorations(graphics);
/*     */     
/* 192 */     if (this.textField.hasCharacterLimit()) {
/* 193 */       int characterLimit = this.textField.characterLimit();
/* 194 */       MutableComponent mutableComponent = Component.translatable("gui.multiLineEditBox.character_limit", new Object[] { this.textField.value().length(), characterLimit });
/* 195 */       graphics.drawString(this.font, (Component)mutableComponent, getX() + this.width - this.font.width((FormattedText)mutableComponent), getY() + this.height + 4, -6250336);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInnerHeight() {
/* 201 */     Objects.requireNonNull(this.font); return 9 * this.textField.getLineCount();
/*     */   }
/*     */ 
/*     */   
/*     */   protected double scrollRate() {
/* 206 */     Objects.requireNonNull(this.font); return 9.0D / 2.0D;
/*     */   }
/*     */   
/*     */   private void scrollToCursor() {
/* 210 */     double scrollAmount = scrollAmount();
/*     */     
/* 212 */     Objects.requireNonNull(this.font); MultilineTextField.StringView firstFullyVisibleLine = this.textField.getLineView((int)(scrollAmount / 9.0D));
/*     */     
/* 214 */     if (this.textField.cursor() <= firstFullyVisibleLine.beginIndex()) {
/* 215 */       Objects.requireNonNull(this.font); scrollAmount = (this.textField.getLineAtCursor() * 9);
/*     */     } else {
/* 217 */       Objects.requireNonNull(this.font); MultilineTextField.StringView lastFullyVisibleLine = this.textField.getLineView((int)((scrollAmount + this.height) / 9.0D) - 1);
/*     */       
/* 219 */       if (this.textField.cursor() > lastFullyVisibleLine.endIndex()) {
/* 220 */         Objects.requireNonNull(this.font); Objects.requireNonNull(this.font); scrollAmount = (this.textField.getLineAtCursor() * 9 - this.height + 9 + totalInnerPadding());
/*     */       } 
/*     */     } 
/*     */     
/* 224 */     setScrollAmount(scrollAmount);
/*     */   }
/*     */   
/*     */   private void seekCursorScreen(double x, double y) {
/* 228 */     double mouseX = x - getX() - innerPadding();
/* 229 */     double mouseY = y - getY() - innerPadding() + scrollAmount();
/*     */     
/* 231 */     this.textField.seekCursorToPoint(mouseX, mouseY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFocused(boolean focused) {
/* 236 */     super.setFocused(focused);
/* 237 */     if (focused) {
/* 238 */       this.focusedTime = Util.getMillis();
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder builder() {
/* 243 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private int x;
/*     */     private int y;
/* 249 */     private Component placeholder = CommonComponents.EMPTY;
/* 250 */     private int textColor = -2039584;
/*     */     private boolean textShadow = true;
/* 252 */     private int cursorColor = -3092272;
/*     */     
/*     */     private boolean showBackground = true;
/*     */     
/*     */     private boolean showDecorations = true;
/*     */ 
/*     */     
/*     */     public Builder setX(int x) {
/* 260 */       this.x = x;
/* 261 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setY(int y) {
/* 265 */       this.y = y;
/* 266 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setPlaceholder(Component placeholder) {
/* 270 */       this.placeholder = placeholder;
/* 271 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setTextColor(int textColor) {
/* 275 */       this.textColor = textColor;
/* 276 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setTextShadow(boolean textShadow) {
/* 280 */       this.textShadow = textShadow;
/* 281 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setCursorColor(int cursorColor) {
/* 285 */       this.cursorColor = cursorColor;
/* 286 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setShowBackground(boolean showBackground) {
/* 290 */       this.showBackground = showBackground;
/* 291 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setShowDecorations(boolean showDecorations) {
/* 295 */       this.showDecorations = showDecorations;
/* 296 */       return this;
/*     */     }
/*     */     
/*     */     public MultiLineEditBox build(Font font, int width, int height, Component narration) {
/* 300 */       return new MultiLineEditBox(font, this.x, this.y, width, height, this.placeholder, narration, this.textColor, this.textShadow, this.cursorColor, this.showBackground, this.showDecorations);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/MultiLineEditBox.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */