/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.FormattedCharSink;
/*     */ import net.minecraft.util.StringDecomposer;
/*     */ import org.apache.commons.lang3.mutable.MutableFloat;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringSplitter
/*     */ {
/*     */   private final WidthProvider widthProvider;
/*     */   
/*     */   public StringSplitter(WidthProvider widthProvider) {
/*  28 */     this.widthProvider = widthProvider;
/*     */   }
/*     */   
/*     */   public float stringWidth(String str) {
/*  32 */     if (str == null) {
/*  33 */       return 0.0F;
/*     */     }
/*     */     
/*  36 */     MutableFloat result = new MutableFloat();
/*  37 */     StringDecomposer.iterateFormatted(str, Style.EMPTY, (position, style, codepoint) -> {
/*     */           result.add(this.widthProvider.getWidth(codepoint, style));
/*     */           return true;
/*     */         });
/*  41 */     return result.floatValue();
/*     */   }
/*     */   
/*     */   public float stringWidth(FormattedText text) {
/*  45 */     MutableFloat result = new MutableFloat();
/*  46 */     StringDecomposer.iterateFormatted(text, Style.EMPTY, (position, style, codepoint) -> {
/*     */           result.add(this.widthProvider.getWidth(codepoint, style));
/*     */           return true;
/*     */         });
/*  50 */     return result.floatValue();
/*     */   }
/*     */   
/*     */   public float stringWidth(FormattedCharSequence text) {
/*  54 */     MutableFloat result = new MutableFloat();
/*  55 */     text.accept((position, style, codepoint) -> {
/*     */           result.add(this.widthProvider.getWidth(codepoint, style));
/*     */           return true;
/*     */         });
/*  59 */     return result.floatValue();
/*     */   }
/*     */   
/*     */   private class WidthLimitedCharSink implements FormattedCharSink {
/*     */     private float maxWidth;
/*     */     private int position;
/*     */     
/*     */     public WidthLimitedCharSink(float maxWidth) {
/*  67 */       this.maxWidth = maxWidth;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean accept(int position, Style style, int codepoint) {
/*  72 */       this.maxWidth -= StringSplitter.this.widthProvider.getWidth(codepoint, style);
/*  73 */       if (this.maxWidth >= 0.0F) {
/*  74 */         this.position = position + Character.charCount(codepoint);
/*  75 */         return true;
/*     */       } 
/*  77 */       return false;
/*     */     }
/*     */     
/*     */     public int getPosition() {
/*  81 */       return this.position;
/*     */     }
/*     */     
/*     */     public void resetPosition() {
/*  85 */       this.position = 0;
/*     */     }
/*     */   }
/*     */   
/*     */   public int plainIndexAtWidth(String str, int maxWidth, Style style) {
/*  90 */     WidthLimitedCharSink output = new WidthLimitedCharSink(maxWidth);
/*  91 */     StringDecomposer.iterate(str, style, output);
/*  92 */     return output.getPosition();
/*     */   }
/*     */   
/*     */   public String plainHeadByWidth(String str, int maxWidth, Style style) {
/*  96 */     return str.substring(0, plainIndexAtWidth(str, maxWidth, style));
/*     */   }
/*     */   
/*     */   public String plainTailByWidth(String str, int maxWidth, Style style) {
/* 100 */     MutableFloat currentWidth = new MutableFloat();
/* 101 */     MutableInt result = new MutableInt(str.length());
/* 102 */     StringDecomposer.iterateBackwards(str, style, (position, s, codepoint) -> {
/*     */           float w = currentWidth.addAndGet(this.widthProvider.getWidth(codepoint, s));
/*     */           
/*     */           if (w > currentWidth) {
/*     */             return false;
/*     */           }
/*     */           
/*     */           maxWidth.setValue(result);
/*     */           return true;
/*     */         });
/* 112 */     return str.substring(result.intValue());
/*     */   }
/*     */   
/*     */   public FormattedText headByWidth(FormattedText text, int width, Style initialStyle) {
/* 116 */     final WidthLimitedCharSink output = new WidthLimitedCharSink(width);
/*     */     
/* 118 */     return text.visit(new FormattedText.StyledContentConsumer<FormattedText>(this) { private final ComponentCollector collector; {
/* 119 */             this.collector = new ComponentCollector();
/*     */           }
/*     */           
/*     */           public Optional<FormattedText> accept(Style style, String contents) {
/* 123 */             output.resetPosition();
/* 124 */             if (!StringDecomposer.iterateFormatted(contents, style, output)) {
/* 125 */               String partial = contents.substring(0, output.getPosition());
/* 126 */               if (!partial.isEmpty()) {
/* 127 */                 this.collector.append(FormattedText.of(partial, style));
/*     */               }
/* 129 */               return Optional.of(this.collector.getResultOrEmpty());
/*     */             } 
/* 131 */             if (!contents.isEmpty()) {
/* 132 */               this.collector.append(FormattedText.of(contents, style));
/*     */             }
/* 134 */             return Optional.empty();
/*     */           }
/* 136 */         },  initialStyle).orElse(text);
/*     */   }
/*     */   
/*     */   private class LineBreakFinder
/*     */     implements FormattedCharSink {
/*     */     private final float maxWidth;
/* 142 */     private int lineBreak = -1;
/* 143 */     private Style lineBreakStyle = Style.EMPTY;
/*     */     private boolean hadNonZeroWidthChar;
/*     */     private float width;
/* 146 */     private int lastSpace = -1;
/* 147 */     private Style lastSpaceStyle = Style.EMPTY;
/*     */     private int nextChar;
/*     */     private int offset;
/*     */     
/*     */     public LineBreakFinder(float maxWidth) {
/* 152 */       this.maxWidth = Math.max(maxWidth, 1.0F);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean accept(int position, Style style, int codepoint) {
/* 157 */       int adjustedPosition = position + this.offset;
/* 158 */       switch (codepoint) {
/*     */         case 10:
/* 160 */           return finishIteration(adjustedPosition, style);
/*     */         case 32:
/* 162 */           this.lastSpace = adjustedPosition;
/* 163 */           this.lastSpaceStyle = style;
/*     */           break;
/*     */       } 
/* 166 */       float charWidth = StringSplitter.this.widthProvider.getWidth(codepoint, style);
/* 167 */       this.width += charWidth;
/* 168 */       if (this.hadNonZeroWidthChar && this.width > this.maxWidth) {
/* 169 */         if (this.lastSpace != -1) {
/* 170 */           return finishIteration(this.lastSpace, this.lastSpaceStyle);
/*     */         }
/* 172 */         return finishIteration(adjustedPosition, style);
/*     */       } 
/*     */       
/* 175 */       this.hadNonZeroWidthChar |= (charWidth != 0.0F) ? true : false;
/* 176 */       this.nextChar = adjustedPosition + Character.charCount(codepoint);
/* 177 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean finishIteration(int lineBreak, Style style) {
/* 183 */       this.lineBreak = lineBreak;
/* 184 */       this.lineBreakStyle = style;
/* 185 */       return false;
/*     */     }
/*     */     
/*     */     private boolean lineBreakFound() {
/* 189 */       return (this.lineBreak != -1);
/*     */     }
/*     */     
/*     */     public int getSplitPosition() {
/* 193 */       return lineBreakFound() ? this.lineBreak : this.nextChar;
/*     */     }
/*     */     
/*     */     public Style getSplitStyle() {
/* 197 */       return this.lineBreakStyle;
/*     */     }
/*     */     
/*     */     public void addToOffset(int delta) {
/* 201 */       this.offset += delta;
/*     */     }
/*     */   }
/*     */   
/*     */   public int findLineBreak(String input, int max, Style initialStyle) {
/* 206 */     LineBreakFinder finder = new LineBreakFinder(max);
/* 207 */     StringDecomposer.iterateFormatted(input, initialStyle, finder);
/* 208 */     return finder.getSplitPosition();
/*     */   }
/*     */   
/*     */   public static int getWordPosition(String text, int dir, int from, boolean stripSpaces) {
/* 212 */     int result = from;
/* 213 */     boolean reverse = (dir < 0);
/* 214 */     int abs = Math.abs(dir);
/*     */     
/* 216 */     for (int i = 0; i < abs; i++) {
/* 217 */       if (reverse) {
/* 218 */         while (stripSpaces && result > 0 && (text.charAt(result - 1) == ' ' || text.charAt(result - 1) == '\n')) {
/* 219 */           result--;
/*     */         }
/* 221 */         while (result > 0 && text.charAt(result - 1) != ' ' && text.charAt(result - 1) != '\n') {
/* 222 */           result--;
/*     */         }
/*     */       } else {
/* 225 */         int length = text.length();
/*     */         
/* 227 */         int index1 = text.indexOf(' ', result);
/* 228 */         int index2 = text.indexOf('\n', result);
/* 229 */         if (index1 == -1 && index2 == -1) {
/* 230 */           result = -1;
/* 231 */         } else if (index1 != -1 && index2 != -1) {
/* 232 */           result = Math.min(index1, index2);
/* 233 */         } else if (index1 != -1) {
/* 234 */           result = index1;
/*     */         } else {
/* 236 */           result = index2;
/*     */         } 
/*     */         
/* 239 */         if (result == -1) {
/* 240 */           result = length;
/*     */         } else {
/* 242 */           while (stripSpaces && result < length && (text.charAt(result) == ' ' || text.charAt(result) == '\n')) {
/* 243 */             result++;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 249 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void splitLines(String input, int maxWidth, Style initialStyle, boolean includeAll, LinePosConsumer output) {
/* 258 */     int start = 0;
/* 259 */     int size = input.length();
/* 260 */     Style workStyle = initialStyle;
/* 261 */     while (start < size) {
/* 262 */       LineBreakFinder finder = new LineBreakFinder(maxWidth);
/* 263 */       boolean endOfText = StringDecomposer.iterateFormatted(input, start, workStyle, initialStyle, finder);
/* 264 */       if (endOfText) {
/* 265 */         output.accept(workStyle, start, size);
/*     */         break;
/*     */       } 
/* 268 */       int lineBreak = finder.getSplitPosition();
/* 269 */       char firstTailChar = input.charAt(lineBreak);
/*     */       
/* 271 */       int adjustedBreak = (firstTailChar == '\n' || firstTailChar == ' ') ? (lineBreak + 1) : lineBreak;
/* 272 */       output.accept(workStyle, start, includeAll ? adjustedBreak : lineBreak);
/* 273 */       start = adjustedBreak;
/* 274 */       workStyle = finder.getSplitStyle();
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<FormattedText> splitLines(String input, int maxWidth, Style initialStyle) {
/* 279 */     List<FormattedText> result = Lists.newArrayList();
/* 280 */     splitLines(input, maxWidth, initialStyle, false, (style, start, end) -> result.add(FormattedText.of(input.substring(start, end), style)));
/* 281 */     return result;
/*     */   }
/*     */   
/*     */   private static class LineComponent implements FormattedText {
/*     */     private final String contents;
/*     */     private final Style style;
/*     */     
/*     */     public LineComponent(String contents, Style style) {
/* 289 */       this.contents = contents;
/* 290 */       this.style = style;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 295 */       return output.accept(this.contents);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style parentStyle) {
/* 300 */       return output.accept(this.style.applyTo(parentStyle), this.contents);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FlatComponents {
/*     */     private final List<StringSplitter.LineComponent> parts;
/*     */     private String flatParts;
/*     */     
/*     */     public FlatComponents(List<StringSplitter.LineComponent> parts) {
/* 309 */       this.parts = parts;
/* 310 */       this.flatParts = parts.stream().map(p -> p.contents).collect(Collectors.joining());
/*     */     }
/*     */     
/*     */     public char charAt(int position) {
/* 314 */       return this.flatParts.charAt(position);
/*     */     }
/*     */     
/*     */     public FormattedText splitAt(int skipPosition, int skipSize, Style splitStyle) {
/* 318 */       ComponentCollector result = new ComponentCollector();
/* 319 */       ListIterator<StringSplitter.LineComponent> it = this.parts.listIterator();
/* 320 */       int position = skipPosition;
/*     */       boolean inSkip = false;
/* 322 */       while (it.hasNext()) {
/* 323 */         StringSplitter.LineComponent element = it.next();
/* 324 */         String contents = element.contents;
/* 325 */         int contentsSize = contents.length();
/*     */         
/* 327 */         if (!inSkip) {
/* 328 */           if (position > contentsSize) {
/* 329 */             result.append(element);
/* 330 */             it.remove();
/* 331 */             position -= contentsSize;
/*     */           } else {
/* 333 */             String beforeSplit = contents.substring(0, position);
/* 334 */             if (!beforeSplit.isEmpty()) {
/* 335 */               result.append(FormattedText.of(beforeSplit, element.style));
/*     */             }
/* 337 */             position += skipSize;
/* 338 */             inSkip = true;
/*     */           } 
/*     */         }
/*     */         
/* 342 */         if (inSkip) {
/* 343 */           if (position > contentsSize) {
/* 344 */             it.remove();
/* 345 */             position -= contentsSize; continue;
/*     */           } 
/* 347 */           String afterSplit = contents.substring(position);
/* 348 */           if (afterSplit.isEmpty()) {
/* 349 */             it.remove(); break;
/*     */           } 
/* 351 */           it.set(new StringSplitter.LineComponent(afterSplit, splitStyle));
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       
/* 358 */       this.flatParts = this.flatParts.substring(skipPosition + skipSize);
/* 359 */       return result.getResultOrEmpty();
/*     */     }
/*     */     
/*     */     public FormattedText getRemainder() {
/* 363 */       ComponentCollector result = new ComponentCollector();
/* 364 */       Objects.requireNonNull(result); this.parts.forEach(result::append);
/* 365 */       this.parts.clear();
/* 366 */       return result.getResult();
/*     */     }
/*     */   }
/*     */   
/*     */   public List<FormattedText> splitLines(FormattedText input, int maxWidth, Style initialStyle) {
/* 371 */     List<FormattedText> result = Lists.newArrayList();
/* 372 */     splitLines(input, maxWidth, initialStyle, (text, wrapped) -> result.add(text));
/* 373 */     return result;
/*     */   }
/*     */   
/*     */   public void splitLines(FormattedText input, int maxWidth, Style initialStyle, BiConsumer<FormattedText, Boolean> output) {
/* 377 */     List<LineComponent> partList = Lists.newArrayList();
/*     */     
/* 379 */     input.visit((style, contents) -> { if (!contents.isEmpty()) partList.add(new LineComponent(contents, style));  return Optional.empty(); }, initialStyle);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 386 */     FlatComponents parts = new FlatComponents(partList);
/*     */     boolean shouldRestart = true;
/*     */     boolean forceNewLine = false;
/*     */     boolean isWrapped = false;
/* 390 */     while (shouldRestart) {
/* 391 */       shouldRestart = false;
/* 392 */       LineBreakFinder finder = new LineBreakFinder(maxWidth);
/* 393 */       for (LineComponent part : parts.parts) {
/* 394 */         boolean endOfText = StringDecomposer.iterateFormatted(part.contents, 0, part.style, initialStyle, finder);
/* 395 */         if (!endOfText) {
/*     */           
/* 397 */           int lineBreak = finder.getSplitPosition();
/* 398 */           Style lineBreakStyle = finder.getSplitStyle();
/*     */           
/* 400 */           char firstTailChar = parts.charAt(lineBreak);
/*     */           
/* 402 */           boolean isNewLine = (firstTailChar == '\n');
/* 403 */           boolean skipNextChar = (isNewLine || firstTailChar == ' ');
/* 404 */           forceNewLine = isNewLine;
/* 405 */           FormattedText result = parts.splitAt(lineBreak, skipNextChar ? 1 : 0, lineBreakStyle);
/* 406 */           output.accept(result, isWrapped);
/* 407 */           isWrapped = !isNewLine;
/* 408 */           shouldRestart = true;
/*     */           break;
/*     */         } 
/* 411 */         finder.addToOffset(part.contents.length());
/*     */       } 
/*     */     } 
/*     */     
/* 415 */     FormattedText lastLine = parts.getRemainder();
/* 416 */     if (lastLine != null) {
/* 417 */       output.accept(lastLine, isWrapped);
/* 418 */     } else if (forceNewLine) {
/* 419 */       output.accept(FormattedText.EMPTY, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface WidthProvider {
/*     */     float getWidth(int param1Int, Style param1Style);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface LinePosConsumer {
/*     */     void accept(Style param1Style, int param1Int1, int param1Int2);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/StringSplitter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */