/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.input.CharacterEvent;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.sounds.SoundManager;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EditBox
/*     */   extends AbstractWidget
/*     */ {
/*  34 */   private static final WidgetSprites SPRITES = new WidgetSprites(Identifier.withDefaultNamespace("widget/text_field"), Identifier.withDefaultNamespace("widget/text_field_highlighted"));
/*     */   
/*     */   public static final int BACKWARDS = -1;
/*     */   public static final int FORWARDS = 1;
/*     */   private static final int CURSOR_INSERT_WIDTH = 1;
/*     */   private static final String CURSOR_APPEND_CHARACTER = "_";
/*     */   public static final int DEFAULT_TEXT_COLOR = -2039584;
/*  41 */   public static final Style DEFAULT_HINT_STYLE = Style.EMPTY.withColor(ChatFormatting.DARK_GRAY);
/*  42 */   public static final Style SEARCH_HINT_STYLE = Style.EMPTY.applyFormats(new ChatFormatting[] { ChatFormatting.GRAY, ChatFormatting.ITALIC });
/*     */   
/*     */   private static final int CURSOR_BLINK_INTERVAL_MS = 300;
/*     */   
/*     */   private final Font font;
/*  47 */   private String value = "";
/*  48 */   private int maxLength = 32;
/*     */   private boolean bordered = true;
/*     */   private boolean canLoseFocus = true;
/*     */   private boolean isEditable = true;
/*     */   private boolean centered = false;
/*     */   private boolean textShadow = true;
/*     */   private boolean invertHighlightedTextColor = true;
/*     */   private int displayPos;
/*     */   private int cursorPos;
/*     */   private int highlightPos;
/*  58 */   private int textColor = -2039584;
/*  59 */   private int textColorUneditable = -9408400;
/*     */   private String suggestion;
/*     */   private Consumer<String> responder;
/*  62 */   private Predicate<String> filter = Objects::nonNull;
/*  63 */   private final List<TextFormatter> formatters = new ArrayList<>();
/*     */   
/*     */   private Component hint;
/*  66 */   private long focusedTime = Util.getMillis();
/*     */   private int textX;
/*     */   private int textY;
/*     */   
/*     */   public EditBox(Font font, int width, int height, Component narration) {
/*  71 */     this(font, 0, 0, width, height, narration);
/*     */   }
/*     */   
/*     */   public EditBox(Font font, int x, int y, int width, int height, Component narration) {
/*  75 */     this(font, x, y, width, height, null, narration);
/*     */   }
/*     */   
/*     */   public EditBox(Font font, int x, int y, int width, int height, EditBox oldBox, Component narration) {
/*  79 */     super(x, y, width, height, narration);
/*  80 */     this.font = font;
/*  81 */     if (oldBox != null) {
/*  82 */       setValue(oldBox.getValue());
/*     */     }
/*  84 */     updateTextPosition();
/*     */   }
/*     */   
/*     */   public void setResponder(Consumer<String> responder) {
/*  88 */     this.responder = responder;
/*     */   }
/*     */   
/*     */   public void addFormatter(TextFormatter formatter) {
/*  92 */     this.formatters.add(formatter);
/*     */   }
/*     */ 
/*     */   
/*     */   protected MutableComponent createNarrationMessage() {
/*  97 */     Component message = getMessage();
/*  98 */     return Component.translatable("gui.narrate.editBox", new Object[] { message, this.value });
/*     */   }
/*     */   
/*     */   public void setValue(String value) {
/* 102 */     if (!this.filter.test(value)) {
/*     */       return;
/*     */     }
/*     */     
/* 106 */     if (value.length() > this.maxLength) {
/* 107 */       this.value = value.substring(0, this.maxLength);
/*     */     } else {
/* 109 */       this.value = value;
/*     */     } 
/*     */     
/* 112 */     moveCursorToEnd(false);
/* 113 */     setHighlightPos(this.cursorPos);
/* 114 */     onValueChange(value);
/*     */   }
/*     */   
/*     */   public String getValue() {
/* 118 */     return this.value;
/*     */   }
/*     */   
/*     */   public String getHighlighted() {
/* 122 */     int start = Math.min(this.cursorPos, this.highlightPos);
/* 123 */     int end = Math.max(this.cursorPos, this.highlightPos);
/*     */     
/* 125 */     return this.value.substring(start, end);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setX(int x) {
/* 130 */     super.setX(x);
/* 131 */     updateTextPosition();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setY(int y) {
/* 136 */     super.setY(y);
/* 137 */     updateTextPosition();
/*     */   }
/*     */   
/*     */   public void setFilter(Predicate<String> filter) {
/* 141 */     this.filter = filter;
/*     */   }
/*     */   
/*     */   public void insertText(String input) {
/* 145 */     int start = Math.min(this.cursorPos, this.highlightPos);
/* 146 */     int end = Math.max(this.cursorPos, this.highlightPos);
/* 147 */     int maxInsertionLength = this.maxLength - this.value.length() - start - end;
/* 148 */     if (maxInsertionLength <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 152 */     String text = StringUtil.filterText(input);
/* 153 */     int insertionLength = text.length();
/* 154 */     if (maxInsertionLength < insertionLength) {
/* 155 */       if (Character.isHighSurrogate(text.charAt(maxInsertionLength - 1))) {
/* 156 */         maxInsertionLength--;
/*     */       }
/* 158 */       text = text.substring(0, maxInsertionLength);
/* 159 */       insertionLength = maxInsertionLength;
/*     */     } 
/*     */     
/* 162 */     String newValue = new StringBuilder(this.value).replace(start, end, text).toString();
/* 163 */     if (!this.filter.test(newValue)) {
/*     */       return;
/*     */     }
/*     */     
/* 167 */     this.value = newValue;
/* 168 */     setCursorPosition(start + insertionLength);
/* 169 */     setHighlightPos(this.cursorPos);
/*     */     
/* 171 */     onValueChange(this.value);
/*     */   }
/*     */   
/*     */   private void onValueChange(String value) {
/* 175 */     if (this.responder != null) {
/* 176 */       this.responder.accept(value);
/*     */     }
/* 178 */     updateTextPosition();
/*     */   }
/*     */   
/*     */   private void deleteText(int dir, boolean wholeWord) {
/* 182 */     if (wholeWord) {
/* 183 */       deleteWords(dir);
/*     */     } else {
/* 185 */       deleteChars(dir);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void deleteWords(int dir) {
/* 190 */     if (this.value.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 194 */     if (this.highlightPos != this.cursorPos) {
/* 195 */       insertText("");
/*     */       
/*     */       return;
/*     */     } 
/* 199 */     deleteCharsToPos(getWordPosition(dir));
/*     */   }
/*     */   
/*     */   public void deleteChars(int dir) {
/* 203 */     deleteCharsToPos(getCursorPos(dir));
/*     */   }
/*     */   
/*     */   public void deleteCharsToPos(int pos) {
/* 207 */     if (this.value.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 211 */     if (this.highlightPos != this.cursorPos) {
/* 212 */       insertText("");
/*     */       
/*     */       return;
/*     */     } 
/* 216 */     int start = Math.min(pos, this.cursorPos);
/* 217 */     int end = Math.max(pos, this.cursorPos);
/* 218 */     if (start == end) {
/*     */       return;
/*     */     }
/*     */     
/* 222 */     String newValue = new StringBuilder(this.value).delete(start, end).toString();
/*     */     
/* 224 */     if (!this.filter.test(newValue)) {
/*     */       return;
/*     */     }
/*     */     
/* 228 */     this.value = newValue;
/* 229 */     moveCursorTo(start, false);
/*     */   }
/*     */   
/*     */   public int getWordPosition(int dir) {
/* 233 */     return getWordPosition(dir, getCursorPosition());
/*     */   }
/*     */   
/*     */   private int getWordPosition(int dir, int from) {
/* 237 */     return getWordPosition(dir, from, true);
/*     */   }
/*     */   
/*     */   private int getWordPosition(int dir, int from, boolean stripSpaces) {
/* 241 */     int result = from;
/* 242 */     boolean reverse = (dir < 0);
/* 243 */     int abs = Math.abs(dir);
/*     */     
/* 245 */     for (int i = 0; i < abs; i++) {
/* 246 */       if (reverse) {
/* 247 */         while (stripSpaces && result > 0 && this.value.charAt(result - 1) == ' ') {
/* 248 */           result--;
/*     */         }
/* 250 */         while (result > 0 && this.value.charAt(result - 1) != ' ') {
/* 251 */           result--;
/*     */         }
/*     */       } else {
/* 254 */         int length = this.value.length();
/*     */         
/* 256 */         result = this.value.indexOf(' ', result);
/* 257 */         if (result == -1) {
/* 258 */           result = length;
/*     */         } else {
/* 260 */           while (stripSpaces && result < length && this.value.charAt(result) == ' ') {
/* 261 */             result++;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 267 */     return result;
/*     */   }
/*     */   
/*     */   public void moveCursor(int dir, boolean hasShiftDown) {
/* 271 */     moveCursorTo(getCursorPos(dir), hasShiftDown);
/*     */   }
/*     */   
/*     */   private int getCursorPos(int dir) {
/* 275 */     return Util.offsetByCodepoints(this.value, this.cursorPos, dir);
/*     */   }
/*     */   
/*     */   public void moveCursorTo(int dir, boolean extendSelection) {
/* 279 */     setCursorPosition(dir);
/*     */     
/* 281 */     if (!extendSelection) {
/* 282 */       setHighlightPos(this.cursorPos);
/*     */     }
/*     */     
/* 285 */     onValueChange(this.value);
/*     */   }
/*     */   
/*     */   public void setCursorPosition(int pos) {
/* 289 */     this.cursorPos = Mth.clamp(pos, 0, this.value.length());
/* 290 */     scrollTo(this.cursorPos);
/*     */   }
/*     */   
/*     */   public void moveCursorToStart(boolean hasShiftDown) {
/* 294 */     moveCursorTo(0, hasShiftDown);
/*     */   }
/*     */   
/*     */   public void moveCursorToEnd(boolean hasShiftDown) {
/* 298 */     moveCursorTo(this.value.length(), hasShiftDown);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 303 */     if (!isActive() || !isFocused()) {
/* 304 */       return false;
/*     */     }
/*     */     
/* 307 */     switch (event.key()) {
/*     */       case 263:
/* 309 */         if (event.hasControlDownWithQuirk()) {
/* 310 */           moveCursorTo(getWordPosition(-1), event.hasShiftDown());
/*     */         } else {
/* 312 */           moveCursor(-1, event.hasShiftDown());
/*     */         } 
/*     */         
/* 315 */         return true;
/*     */       case 262:
/* 317 */         if (event.hasControlDownWithQuirk()) {
/* 318 */           moveCursorTo(getWordPosition(1), event.hasShiftDown());
/*     */         } else {
/* 320 */           moveCursor(1, event.hasShiftDown());
/*     */         } 
/*     */         
/* 323 */         return true;
/*     */       case 259:
/* 325 */         if (this.isEditable) {
/* 326 */           deleteText(-1, event.hasControlDownWithQuirk());
/*     */         }
/*     */         
/* 329 */         return true;
/*     */       case 261:
/* 331 */         if (this.isEditable) {
/* 332 */           deleteText(1, event.hasControlDownWithQuirk());
/*     */         }
/*     */         
/* 335 */         return true;
/*     */       case 268:
/* 337 */         moveCursorToStart(event.hasShiftDown());
/*     */         
/* 339 */         return true;
/*     */       case 269:
/* 341 */         moveCursorToEnd(event.hasShiftDown());
/*     */         
/* 343 */         return true;
/*     */     } 
/*     */     
/* 346 */     if (event.isSelectAll()) {
/* 347 */       moveCursorToEnd(false);
/* 348 */       setHighlightPos(0);
/* 349 */       return true;
/* 350 */     }  if (event.isCopy()) {
/* 351 */       (Minecraft.getInstance()).keyboardHandler.setClipboard(getHighlighted());
/* 352 */       return true;
/* 353 */     }  if (event.isPaste()) {
/* 354 */       if (isEditable()) {
/* 355 */         insertText((Minecraft.getInstance()).keyboardHandler.getClipboard());
/*     */       }
/* 357 */       return true;
/* 358 */     }  if (event.isCut()) {
/* 359 */       (Minecraft.getInstance()).keyboardHandler.setClipboard(getHighlighted());
/* 360 */       if (isEditable()) {
/* 361 */         insertText("");
/*     */       }
/* 363 */       return true;
/*     */     } 
/*     */     
/* 366 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canConsumeInput() {
/* 370 */     return (isActive() && isFocused() && isEditable());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean charTyped(CharacterEvent event) {
/* 375 */     if (!canConsumeInput()) {
/* 376 */       return false;
/*     */     }
/* 378 */     if (event.isAllowedChatCharacter()) {
/* 379 */       if (this.isEditable) {
/* 380 */         insertText(event.codepointAsString());
/*     */       }
/*     */       
/* 383 */       return true;
/*     */     } 
/*     */     
/* 386 */     return false;
/*     */   }
/*     */   
/*     */   private int findClickedPositionInText(MouseButtonEvent event) {
/* 390 */     int positionInText = Math.min(Mth.floor(event.x()) - this.textX, getInnerWidth());
/* 391 */     String displayed = this.value.substring(this.displayPos);
/* 392 */     return this.displayPos + this.font.plainSubstrByWidth(displayed, positionInText).length();
/*     */   }
/*     */   
/*     */   private void selectWord(MouseButtonEvent event) {
/* 396 */     int clickedPosition = findClickedPositionInText(event);
/* 397 */     int wordStart = getWordPosition(-1, clickedPosition);
/* 398 */     int wordEnd = getWordPosition(1, clickedPosition);
/* 399 */     moveCursorTo(wordStart, false);
/* 400 */     moveCursorTo(wordEnd, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClick(MouseButtonEvent event, boolean doubleClick) {
/* 405 */     if (doubleClick) {
/* 406 */       selectWord(event);
/*     */     } else {
/* 408 */       moveCursorTo(findClickedPositionInText(event), event.hasShiftDown());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onDrag(MouseButtonEvent event, double dx, double dy) {
/* 414 */     moveCursorTo(findClickedPositionInText(event), true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void playDownSound(SoundManager soundManager) {}
/*     */ 
/*     */   
/*     */   public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 423 */     if (!isVisible()) {
/*     */       return;
/*     */     }
/*     */     
/* 427 */     if (isBordered()) {
/* 428 */       Identifier sprite = SPRITES.get(isActive(), isFocused());
/* 429 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getX(), getY(), getWidth(), getHeight());
/*     */     } 
/*     */     
/* 432 */     int color = this.isEditable ? this.textColor : this.textColorUneditable;
/* 433 */     int relCursorPos = this.cursorPos - this.displayPos;
/* 434 */     String displayed = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), getInnerWidth());
/* 435 */     boolean cursorOnScreen = (relCursorPos >= 0 && relCursorPos <= displayed.length());
/* 436 */     boolean showCursor = (isFocused() && (Util.getMillis() - this.focusedTime) / 300L % 2L == 0L && cursorOnScreen);
/* 437 */     int drawX = this.textX;
/*     */     
/* 439 */     int relHighlightPos = Mth.clamp(this.highlightPos - this.displayPos, 0, displayed.length());
/*     */     
/* 441 */     if (!displayed.isEmpty()) {
/* 442 */       String half = cursorOnScreen ? displayed.substring(0, relCursorPos) : displayed;
/* 443 */       FormattedCharSequence charSequence = applyFormat(half, this.displayPos);
/* 444 */       graphics.drawString(this.font, charSequence, drawX, this.textY, color, this.textShadow);
/* 445 */       drawX += this.font.width(charSequence) + 1;
/*     */     } 
/*     */     
/* 448 */     boolean insert = (this.cursorPos < this.value.length() || this.value.length() >= getMaxLength());
/* 449 */     int cursorX = drawX;
/*     */     
/* 451 */     if (!cursorOnScreen) {
/* 452 */       cursorX = (relCursorPos > 0) ? (this.textX + this.width) : this.textX;
/* 453 */     } else if (insert) {
/* 454 */       cursorX--;
/* 455 */       drawX--;
/*     */     } 
/*     */     
/* 458 */     if (!displayed.isEmpty() && cursorOnScreen && relCursorPos < displayed.length()) {
/* 459 */       graphics.drawString(this.font, applyFormat(displayed.substring(relCursorPos), this.cursorPos), drawX, this.textY, color, this.textShadow);
/*     */     }
/*     */     
/* 462 */     if (this.hint != null && displayed.isEmpty() && !isFocused()) {
/* 463 */       graphics.drawString(this.font, this.hint, drawX, this.textY, color);
/*     */     }
/*     */     
/* 466 */     if (!insert && this.suggestion != null) {
/* 467 */       graphics.drawString(this.font, this.suggestion, cursorX - 1, this.textY, -8355712, this.textShadow);
/*     */     }
/*     */     
/* 470 */     if (relHighlightPos != relCursorPos) {
/* 471 */       int highlightX = this.textX + this.font.width(displayed.substring(0, relHighlightPos));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 476 */       Objects.requireNonNull(this.font); graphics.textHighlight(Math.min(cursorX, getX() + this.width), this.textY - 1, Math.min(highlightX - 1, getX() + this.width), this.textY + 1 + 9, this.invertHighlightedTextColor);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 481 */     if (showCursor) {
/* 482 */       if (insert) {
/* 483 */         Objects.requireNonNull(this.font); graphics.fill(cursorX, this.textY - 1, cursorX + 1, this.textY + 1 + 9, color);
/*     */       } else {
/* 485 */         graphics.drawString(this.font, "_", cursorX, this.textY, color, this.textShadow);
/*     */       } 
/*     */     }
/*     */     
/* 489 */     if (isHovered()) {
/* 490 */       graphics.requestCursor(isEditable() ? CursorTypes.IBEAM : CursorTypes.NOT_ALLOWED);
/*     */     }
/*     */   }
/*     */   
/*     */   private FormattedCharSequence applyFormat(String text, int offset) {
/* 495 */     for (TextFormatter formatter : this.formatters) {
/* 496 */       FormattedCharSequence formattedCharSequence = formatter.format(text, offset);
/*     */       
/* 498 */       if (formattedCharSequence != null) {
/* 499 */         return formattedCharSequence;
/*     */       }
/*     */     } 
/* 502 */     return FormattedCharSequence.forward(text, Style.EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateTextPosition() {
/* 507 */     if (this.font == null) {
/*     */       return;
/*     */     }
/* 510 */     String displayed = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), getInnerWidth());
/* 511 */     this.textX = getX() + (isCentered() ? ((getWidth() - this.font.width(displayed)) / 2) : (this.bordered ? 4 : 0));
/* 512 */     this.textY = this.bordered ? (getY() + (this.height - 8) / 2) : getY();
/*     */   }
/*     */   
/*     */   public void setMaxLength(int maxLength) {
/* 516 */     this.maxLength = maxLength;
/*     */     
/* 518 */     if (this.value.length() > maxLength) {
/* 519 */       this.value = this.value.substring(0, maxLength);
/* 520 */       onValueChange(this.value);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getMaxLength() {
/* 525 */     return this.maxLength;
/*     */   }
/*     */   
/*     */   public int getCursorPosition() {
/* 529 */     return this.cursorPos;
/*     */   }
/*     */   
/*     */   public boolean isBordered() {
/* 533 */     return this.bordered;
/*     */   }
/*     */   
/*     */   public void setBordered(boolean bordered) {
/* 537 */     this.bordered = bordered;
/* 538 */     updateTextPosition();
/*     */   }
/*     */   
/*     */   public void setTextColor(int textColor) {
/* 542 */     this.textColor = textColor;
/*     */   }
/*     */   
/*     */   public void setTextColorUneditable(int textColorUneditable) {
/* 546 */     this.textColorUneditable = textColorUneditable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFocused(boolean focused) {
/* 551 */     if (!this.canLoseFocus && !focused) {
/*     */       return;
/*     */     }
/* 554 */     super.setFocused(focused);
/* 555 */     if (focused) {
/* 556 */       this.focusedTime = Util.getMillis();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isEditable() {
/* 561 */     return this.isEditable;
/*     */   }
/*     */   
/*     */   public void setEditable(boolean isEditable) {
/* 565 */     this.isEditable = isEditable;
/*     */   }
/*     */   
/*     */   private boolean isCentered() {
/* 569 */     return this.centered;
/*     */   }
/*     */   
/*     */   public void setCentered(boolean centered) {
/* 573 */     this.centered = centered;
/* 574 */     updateTextPosition();
/*     */   }
/*     */   
/*     */   public void setTextShadow(boolean textShadow) {
/* 578 */     this.textShadow = textShadow;
/*     */   }
/*     */   
/*     */   public void setInvertHighlightedTextColor(boolean invertHighlightedTextColor) {
/* 582 */     this.invertHighlightedTextColor = invertHighlightedTextColor;
/*     */   }
/*     */   
/*     */   public int getInnerWidth() {
/* 586 */     return isBordered() ? (this.width - 8) : this.width;
/*     */   }
/*     */   
/*     */   public void setHighlightPos(int pos) {
/* 590 */     this.highlightPos = Mth.clamp(pos, 0, this.value.length());
/* 591 */     scrollTo(this.highlightPos);
/*     */   }
/*     */ 
/*     */   
/*     */   private void scrollTo(int pos) {
/* 596 */     if (this.font == null) {
/*     */       return;
/*     */     }
/*     */     
/* 600 */     this.displayPos = Math.min(this.displayPos, this.value.length());
/*     */     
/* 602 */     int innerWidth = getInnerWidth();
/* 603 */     String displayed = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), innerWidth);
/* 604 */     int lastPos = displayed.length() + this.displayPos;
/*     */     
/* 606 */     if (pos == this.displayPos) {
/* 607 */       this.displayPos -= this.font.plainSubstrByWidth(this.value, innerWidth, true).length();
/*     */     }
/* 609 */     if (pos > lastPos) {
/* 610 */       this.displayPos += pos - lastPos;
/* 611 */     } else if (pos <= this.displayPos) {
/* 612 */       this.displayPos -= this.displayPos - pos;
/*     */     } 
/*     */     
/* 615 */     this.displayPos = Mth.clamp(this.displayPos, 0, this.value.length());
/*     */   }
/*     */   
/*     */   public void setCanLoseFocus(boolean canLoseFocus) {
/* 619 */     this.canLoseFocus = canLoseFocus;
/*     */   }
/*     */   
/*     */   public boolean isVisible() {
/* 623 */     return this.visible;
/*     */   }
/*     */   
/*     */   public void setVisible(boolean visible) {
/* 627 */     this.visible = visible;
/*     */   }
/*     */   
/*     */   public void setSuggestion(String suggestion) {
/* 631 */     this.suggestion = suggestion;
/*     */   }
/*     */   
/*     */   public int getScreenX(int charIndex) {
/* 635 */     if (charIndex > this.value.length()) {
/* 636 */       return getX();
/*     */     }
/* 638 */     return getX() + this.font.width(this.value.substring(0, charIndex));
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateWidgetNarration(NarrationElementOutput output) {
/* 643 */     output.add(NarratedElementType.TITLE, (Component)createNarrationMessage());
/*     */   }
/*     */   
/*     */   public void setHint(Component hint) {
/* 647 */     boolean hasNoStyle = hint.getStyle().equals(Style.EMPTY);
/* 648 */     this.hint = hasNoStyle ? (Component)hint.copy().withStyle(DEFAULT_HINT_STYLE) : hint;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface TextFormatter {
/*     */     FormattedCharSequence format(String param1String, int param1Int);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/EditBox.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */