/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class MultilineTextField
/*     */ {
/*  19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int NO_LIMIT = 2147483647;
/*     */   
/*     */   private static final int LINE_SEEK_PIXEL_BIAS = 2;
/*     */   private final Font font;
/*  25 */   private final List<StringView> displayLines = Lists.newArrayList();
/*     */   
/*     */   private String value;
/*     */   private int cursor;
/*     */   private int selectCursor;
/*     */   private boolean selecting;
/*  31 */   private int characterLimit = Integer.MAX_VALUE;
/*  32 */   private int lineLimit = Integer.MAX_VALUE; private final int width; private Consumer<String> valueListener = s -> {
/*     */     
/*     */     }; private Runnable cursorListener = () -> {
/*     */     
/*     */     };
/*     */   
/*     */   public MultilineTextField(Font font, int width) {
/*  39 */     this.font = font;
/*  40 */     this.width = width;
/*  41 */     setValue("");
/*     */   }
/*     */   
/*     */   public int characterLimit() {
/*  45 */     return this.characterLimit;
/*     */   }
/*     */   
/*     */   public void setCharacterLimit(int characterLimit) {
/*  49 */     if (characterLimit < 0) {
/*  50 */       throw new IllegalArgumentException("Character limit cannot be negative");
/*     */     }
/*  52 */     this.characterLimit = characterLimit;
/*     */   }
/*     */   
/*     */   public void setLineLimit(int lineLimit) {
/*  56 */     if (lineLimit < 0) {
/*  57 */       throw new IllegalArgumentException("Character limit cannot be negative");
/*     */     }
/*  59 */     this.lineLimit = lineLimit;
/*     */   }
/*     */   
/*     */   public boolean hasCharacterLimit() {
/*  63 */     return (this.characterLimit != Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public boolean hasLineLimit() {
/*  67 */     return (this.lineLimit != Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public void setValueListener(Consumer<String> valueListener) {
/*  71 */     this.valueListener = valueListener;
/*     */   }
/*     */   
/*     */   public void setCursorListener(Runnable cursorListener) {
/*  75 */     this.cursorListener = cursorListener;
/*     */   }
/*     */   
/*     */   public void setValue(String value) {
/*  79 */     setValue(value, false);
/*     */   }
/*     */   
/*     */   public void setValue(String value, boolean allowOverflowLineLimit) {
/*  83 */     String newValue = truncateFullText(value);
/*  84 */     if (!allowOverflowLineLimit && overflowsLineLimit(newValue)) {
/*     */       return;
/*     */     }
/*  87 */     this.value = newValue;
/*  88 */     this.cursor = this.value.length();
/*  89 */     this.selectCursor = this.cursor;
/*  90 */     onValueChange();
/*     */   }
/*     */   
/*     */   public String value() {
/*  94 */     return this.value;
/*     */   }
/*     */   
/*     */   public void insertText(String input) {
/*  98 */     if (input.isEmpty() && !hasSelection()) {
/*     */       return;
/*     */     }
/*     */     
/* 102 */     String text = truncateInsertionText(StringUtil.filterText(input, true));
/*     */     
/* 104 */     StringView selected = getSelected();
/* 105 */     String newValue = new StringBuilder(this.value).replace(selected.beginIndex, selected.endIndex, text).toString();
/*     */     
/* 107 */     if (overflowsLineLimit(newValue)) {
/*     */       return;
/*     */     }
/*     */     
/* 111 */     this.value = newValue;
/* 112 */     this.cursor = selected.beginIndex + text.length();
/* 113 */     this.selectCursor = this.cursor;
/*     */     
/* 115 */     onValueChange();
/*     */   }
/*     */   
/*     */   public void deleteText(int dir) {
/* 119 */     if (!hasSelection()) {
/* 120 */       this.selectCursor = Mth.clamp(this.cursor + dir, 0, this.value.length());
/*     */     }
/*     */     
/* 123 */     insertText("");
/*     */   }
/*     */   
/*     */   public int cursor() {
/* 127 */     return this.cursor;
/*     */   }
/*     */   
/*     */   public void setSelecting(boolean selecting) {
/* 131 */     this.selecting = selecting;
/*     */   }
/*     */   
/*     */   public StringView getSelected() {
/* 135 */     return new StringView(Math.min(this.selectCursor, this.cursor), Math.max(this.selectCursor, this.cursor));
/*     */   }
/*     */   
/*     */   public int getLineCount() {
/* 139 */     return this.displayLines.size();
/*     */   }
/*     */   
/*     */   public int getLineAtCursor() {
/* 143 */     for (int i = 0; i < this.displayLines.size(); i++) {
/* 144 */       StringView view = this.displayLines.get(i);
/* 145 */       if (this.cursor >= view.beginIndex && this.cursor <= view.endIndex) {
/* 146 */         return i;
/*     */       }
/*     */     } 
/* 149 */     return -1;
/*     */   }
/*     */   
/*     */   public StringView getLineView(int lineIndex) {
/* 153 */     return this.displayLines.get(Mth.clamp(lineIndex, 0, this.displayLines.size() - 1));
/*     */   }
/*     */   
/*     */   public void seekCursor(Whence whence, int cursor) {
/* 157 */     switch (whence) { case ABSOLUTE:
/* 158 */         this.cursor = cursor; break;
/* 159 */       case RELATIVE: this.cursor += cursor; break;
/* 160 */       case END: this.cursor = this.value.length() + cursor; break; }
/*     */     
/* 162 */     this.cursor = Mth.clamp(this.cursor, 0, this.value.length());
/* 163 */     this.cursorListener.run();
/*     */     
/* 165 */     if (!this.selecting) {
/* 166 */       this.selectCursor = this.cursor;
/*     */     }
/*     */   }
/*     */   
/*     */   public void seekCursorLine(int lineOffset) {
/* 171 */     if (lineOffset == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 175 */     int oldCursorLeft = this.font.width(this.value.substring((getCursorLineView()).beginIndex, this.cursor)) + 2;
/*     */     
/* 177 */     StringView lineView = getCursorLineView(lineOffset);
/* 178 */     int newCursor = this.font.plainSubstrByWidth(this.value.substring(lineView.beginIndex, lineView.endIndex), oldCursorLeft).length();
/* 179 */     seekCursor(Whence.ABSOLUTE, lineView.beginIndex + newCursor);
/*     */   }
/*     */   
/*     */   public void seekCursorToPoint(double x, double y) {
/* 183 */     int left = Mth.floor(x);
/* 184 */     Objects.requireNonNull(this.font); int top = Mth.floor(y / 9.0D);
/*     */     
/* 186 */     StringView lineView = this.displayLines.get(Mth.clamp(top, 0, this.displayLines.size() - 1));
/* 187 */     int clickedColumn = this.font.plainSubstrByWidth(this.value.substring(lineView.beginIndex, lineView.endIndex), left).length();
/*     */     
/* 189 */     seekCursor(Whence.ABSOLUTE, lineView.beginIndex + clickedColumn);
/*     */   }
/*     */   
/*     */   public void selectWordAtCursor() {
/* 193 */     StringView wordView = getPreviousWord();
/* 194 */     seekCursor(Whence.ABSOLUTE, wordView.beginIndex);
/* 195 */     setSelecting(true);
/* 196 */     seekCursor(Whence.ABSOLUTE, wordView.endIndex);
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 200 */     this.selecting = event.hasShiftDown();
/*     */     
/* 202 */     if (event.isSelectAll()) {
/* 203 */       this.cursor = this.value.length();
/* 204 */       this.selectCursor = 0;
/* 205 */       return true;
/* 206 */     }  if (event.isCopy()) {
/* 207 */       (Minecraft.getInstance()).keyboardHandler.setClipboard(getSelectedText());
/* 208 */       return true;
/* 209 */     }  if (event.isPaste()) {
/* 210 */       insertText((Minecraft.getInstance()).keyboardHandler.getClipboard());
/* 211 */       return true;
/* 212 */     }  if (event.isCut()) {
/* 213 */       (Minecraft.getInstance()).keyboardHandler.setClipboard(getSelectedText());
/* 214 */       insertText("");
/* 215 */       return true;
/*     */     } 
/*     */     
/* 218 */     switch (event.key()) {
/*     */       case 263:
/* 220 */         if (event.hasControlDownWithQuirk()) {
/* 221 */           StringView wordView = getPreviousWord();
/* 222 */           seekCursor(Whence.ABSOLUTE, wordView.beginIndex);
/*     */         } else {
/* 224 */           seekCursor(Whence.RELATIVE, -1);
/*     */         } 
/* 226 */         return true;
/*     */       
/*     */       case 262:
/* 229 */         if (event.hasControlDownWithQuirk()) {
/* 230 */           StringView wordView = getNextWord();
/* 231 */           seekCursor(Whence.ABSOLUTE, wordView.beginIndex);
/*     */         } else {
/* 233 */           seekCursor(Whence.RELATIVE, 1);
/*     */         } 
/* 235 */         return true;
/*     */       
/*     */       case 265:
/* 238 */         if (!event.hasControlDownWithQuirk()) {
/* 239 */           seekCursorLine(-1);
/*     */         }
/* 241 */         return true;
/*     */       
/*     */       case 264:
/* 244 */         if (!event.hasControlDownWithQuirk()) {
/* 245 */           seekCursorLine(1);
/*     */         }
/* 247 */         return true;
/*     */       
/*     */       case 266:
/* 250 */         seekCursor(Whence.ABSOLUTE, 0);
/* 251 */         return true;
/*     */       
/*     */       case 267:
/* 254 */         seekCursor(Whence.END, 0);
/* 255 */         return true;
/*     */       
/*     */       case 268:
/* 258 */         if (event.hasControlDownWithQuirk()) {
/* 259 */           seekCursor(Whence.ABSOLUTE, 0);
/*     */         } else {
/* 261 */           seekCursor(Whence.ABSOLUTE, (getCursorLineView()).beginIndex);
/*     */         } 
/* 263 */         return true;
/*     */       
/*     */       case 269:
/* 266 */         if (event.hasControlDownWithQuirk()) {
/* 267 */           seekCursor(Whence.END, 0);
/*     */         } else {
/* 269 */           seekCursor(Whence.ABSOLUTE, (getCursorLineView()).endIndex);
/*     */         } 
/* 271 */         return true;
/*     */       
/*     */       case 259:
/* 274 */         if (event.hasControlDownWithQuirk()) {
/* 275 */           StringView wordView = getPreviousWord();
/* 276 */           deleteText(wordView.beginIndex - this.cursor);
/*     */         } else {
/* 278 */           deleteText(-1);
/*     */         } 
/* 280 */         return true;
/*     */       
/*     */       case 261:
/* 283 */         if (event.hasControlDownWithQuirk()) {
/* 284 */           StringView wordView = getNextWord();
/* 285 */           deleteText(wordView.beginIndex - this.cursor);
/*     */         } else {
/* 287 */           deleteText(1);
/*     */         } 
/* 289 */         return true;
/*     */       case 257:
/*     */       case 335:
/* 292 */         insertText("\n");
/* 293 */         return true;
/*     */     } 
/*     */     
/* 296 */     return false;
/*     */   }
/*     */   
/*     */   public Iterable<StringView> iterateLines() {
/* 300 */     return this.displayLines;
/*     */   }
/*     */   
/*     */   public boolean hasSelection() {
/* 304 */     return (this.selectCursor != this.cursor);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public String getSelectedText() {
/* 309 */     StringView selected = getSelected();
/* 310 */     return this.value.substring(selected.beginIndex, selected.endIndex);
/*     */   }
/*     */   
/*     */   private StringView getCursorLineView() {
/* 314 */     return getCursorLineView(0);
/*     */   }
/*     */   
/*     */   private StringView getCursorLineView(int lineOffset) {
/* 318 */     int lineIndex = getLineAtCursor();
/* 319 */     if (lineIndex < 0) {
/* 320 */       LOGGER.error("Cursor is not within text (cursor = {}, length = {})", this.cursor, this.value.length());
/* 321 */       return this.displayLines.getLast();
/*     */     } 
/*     */     
/* 324 */     return this.displayLines.get(Mth.clamp(lineIndex + lineOffset, 0, this.displayLines.size() - 1));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public StringView getPreviousWord() {
/* 329 */     if (this.value.isEmpty()) {
/* 330 */       return StringView.EMPTY;
/*     */     }
/*     */     
/* 333 */     int startPosition = Mth.clamp(this.cursor, 0, this.value.length() - 1);
/*     */ 
/*     */     
/* 336 */     while (startPosition > 0 && Character.isWhitespace(this.value.charAt(startPosition - 1))) {
/* 337 */       startPosition--;
/*     */     }
/*     */ 
/*     */     
/* 341 */     while (startPosition > 0 && !Character.isWhitespace(this.value.charAt(startPosition - 1))) {
/* 342 */       startPosition--;
/*     */     }
/*     */     
/* 345 */     return new StringView(startPosition, getWordEndPosition(startPosition));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public StringView getNextWord() {
/* 350 */     if (this.value.isEmpty()) {
/* 351 */       return StringView.EMPTY;
/*     */     }
/*     */     
/* 354 */     int startPosition = Mth.clamp(this.cursor, 0, this.value.length() - 1);
/*     */ 
/*     */     
/* 357 */     while (startPosition < this.value.length() && !Character.isWhitespace(this.value.charAt(startPosition))) {
/* 358 */       startPosition++;
/*     */     }
/*     */ 
/*     */     
/* 362 */     while (startPosition < this.value.length() && Character.isWhitespace(this.value.charAt(startPosition))) {
/* 363 */       startPosition++;
/*     */     }
/*     */     
/* 366 */     return new StringView(startPosition, getWordEndPosition(startPosition));
/*     */   }
/*     */   
/*     */   private int getWordEndPosition(int from) {
/* 370 */     int end = from;
/* 371 */     while (end < this.value.length() && !Character.isWhitespace(this.value.charAt(end))) {
/* 372 */       end++;
/*     */     }
/*     */     
/* 375 */     return end;
/*     */   }
/*     */   
/*     */   private void onValueChange() {
/* 379 */     reflowDisplayLines();
/* 380 */     this.valueListener.accept(this.value);
/* 381 */     this.cursorListener.run();
/*     */   }
/*     */   
/*     */   private void reflowDisplayLines() {
/* 385 */     this.displayLines.clear();
/*     */     
/* 387 */     if (this.value.isEmpty()) {
/* 388 */       this.displayLines.add(StringView.EMPTY);
/*     */       
/*     */       return;
/*     */     } 
/* 392 */     this.font.getSplitter().splitLines(this.value, this.width, Style.EMPTY, false, (style, start, end) -> this.displayLines.add(new StringView(start, end)));
/*     */ 
/*     */     
/* 395 */     if (this.value.charAt(this.value.length() - 1) == '\n') {
/* 396 */       this.displayLines.add(new StringView(this.value.length(), this.value.length()));
/*     */     }
/*     */   }
/*     */   
/*     */   private String truncateFullText(String input) {
/* 401 */     if (hasCharacterLimit()) {
/* 402 */       return StringUtil.truncateStringIfNecessary(input, this.characterLimit, false);
/*     */     }
/* 404 */     return input;
/*     */   }
/*     */   
/*     */   private String truncateInsertionText(String input) {
/* 408 */     String truncatedInput = input;
/* 409 */     if (hasCharacterLimit()) {
/* 410 */       int remainingCharacters = this.characterLimit - this.value.length();
/* 411 */       truncatedInput = StringUtil.truncateStringIfNecessary(input, remainingCharacters, false);
/*     */     } 
/*     */     
/* 414 */     return truncatedInput;
/*     */   }
/*     */   
/*     */   private boolean overflowsLineLimit(String newValue) {
/* 418 */     if (hasLineLimit()) if (this.font.getSplitter().splitLines(newValue, this.width, Style.EMPTY).size() + (StringUtil.endsWithNewLine(newValue) ? 1 : 0) > this.lineLimit);  return false;
/*     */   }
/*     */   protected static final class StringView extends Record { private final int beginIndex; private final int endIndex;
/* 421 */     protected StringView(int beginIndex, int endIndex) { this.beginIndex = beginIndex; this.endIndex = endIndex; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/MultilineTextField$StringView;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #421	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 421 */       //   0	7	0	this	Lnet/minecraft/client/gui/components/MultilineTextField$StringView; } public int beginIndex() { return this.beginIndex; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/MultilineTextField$StringView;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #421	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/MultilineTextField$StringView; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/MultilineTextField$StringView;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #421	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/components/MultilineTextField$StringView;
/* 421 */       //   0	8	1	o	Ljava/lang/Object; } public int endIndex() { return this.endIndex; }
/* 422 */      private static final StringView EMPTY = new StringView(0, 0); }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/MultilineTextField.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */