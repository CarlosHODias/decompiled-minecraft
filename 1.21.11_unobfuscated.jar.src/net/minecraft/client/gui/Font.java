/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.ibm.icu.text.ArabicShaping;
/*     */ import com.ibm.icu.text.ArabicShapingException;
/*     */ import com.ibm.icu.text.Bidi;
/*     */ import com.mojang.blaze3d.font.GlyphInfo;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.StringSplitter;
/*     */ import net.minecraft.client.gui.font.EmptyArea;
/*     */ import net.minecraft.client.gui.font.TextRenderable;
/*     */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*     */ import net.minecraft.client.gui.font.glyphs.EffectGlyph;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.renderer.MultiBufferSource;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FontDescription;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.chat.TextColor;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.FormattedCharSink;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringDecomposer;
/*     */ import org.joml.Matrix4f;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Font
/*     */ {
/*     */   private static final float EFFECT_DEPTH = 0.01F;
/*     */   private static final float OVER_EFFECT_DEPTH = 0.01F;
/*     */   private static final float UNDER_EFFECT_DEPTH = -0.01F;
/*     */   public static final float SHADOW_DEPTH = 0.03F;
/*  40 */   public final int lineHeight = 9;
/*  41 */   private final RandomSource random = RandomSource.create();
/*     */   
/*     */   private final Provider provider;
/*     */   
/*     */   private final StringSplitter splitter;
/*     */   
/*     */   public Font(Provider provider) {
/*  48 */     this.provider = provider;
/*  49 */     this.splitter = new StringSplitter((codepoint, style) -> getGlyphSource(style.getFont()).getGlyph(codepoint).info().getAdvance(style.isBold()));
/*     */   }
/*     */   
/*     */   private GlyphSource getGlyphSource(FontDescription fontLocation) {
/*  53 */     return this.provider.glyphs(fontLocation);
/*     */   }
/*     */   
/*     */   public String bidirectionalShaping(String text) {
/*     */     try {
/*  58 */       Bidi bidi = new Bidi(new ArabicShaping(8).shape(text), 127);
/*  59 */       bidi.setReorderingMode(0);
/*  60 */       return bidi.writeReordered(2);
/*  61 */     } catch (ArabicShapingException arabicShapingException) {
/*     */ 
/*     */       
/*  64 */       return text;
/*     */     } 
/*     */   }
/*     */   public void drawInBatch(String str, float x, float y, int color, boolean dropShadow, Matrix4f pose, MultiBufferSource bufferSource, DisplayMode displayMode, int backgroundColor, int packedLightCoords) {
/*  68 */     PreparedText preparedText = prepareText(str, x, y, color, dropShadow, backgroundColor);
/*  69 */     preparedText.visit(GlyphVisitor.forMultiBufferSource(bufferSource, pose, displayMode, packedLightCoords));
/*     */   }
/*     */   
/*     */   public void drawInBatch(Component str, float x, float y, int color, boolean dropShadow, Matrix4f pose, MultiBufferSource bufferSource, DisplayMode displayMode, int backgroundColor, int packedLightCoords) {
/*  73 */     PreparedText preparedText = prepareText(str.getVisualOrderText(), x, y, color, dropShadow, false, backgroundColor);
/*  74 */     preparedText.visit(GlyphVisitor.forMultiBufferSource(bufferSource, pose, displayMode, packedLightCoords));
/*     */   }
/*     */   
/*     */   public void drawInBatch(FormattedCharSequence str, float x, float y, int color, boolean dropShadow, Matrix4f pose, MultiBufferSource bufferSource, DisplayMode displayMode, int backgroundColor, int packedLightCoords) {
/*  78 */     PreparedText preparedText = prepareText(str, x, y, color, dropShadow, false, backgroundColor);
/*  79 */     preparedText.visit(GlyphVisitor.forMultiBufferSource(bufferSource, pose, displayMode, packedLightCoords));
/*     */   }
/*     */   
/*     */   public void drawInBatch8xOutline(FormattedCharSequence str, float x, float y, int color, int outlineColor, Matrix4f pose, MultiBufferSource bufferSource, int packedLightCoords) {
/*  83 */     PreparedTextBuilder outlineOutput = new PreparedTextBuilder(0.0F, 0.0F, outlineColor, false, false);
/*  84 */     for (int xo = -1; xo <= 1; xo++) {
/*  85 */       for (int yo = -1; yo <= 1; yo++) {
/*  86 */         if (xo != 0 || yo != 0) {
/*  87 */           float[] startX = { x };
/*  88 */           int finalXo = xo;
/*  89 */           int finalYo = yo;
/*  90 */           str.accept((position, style, codepoint) -> {
/*     */                 boolean bold = style.isBold();
/*     */                 
/*     */                 BakedGlyph glyph = getGlyph(codepoint, style);
/*     */                 
/*     */                 outlineOutput.x = outlineOutput[0] + startX * glyph.info().getShadowOffset();
/*     */                 
/*     */                 outlineOutput.y = finalXo + y * glyph.info().getShadowOffset();
/*     */                 outlineOutput[0] = outlineOutput[0] + glyph.info().getAdvance(bold);
/*     */                 return outlineOutput.accept(outlineColor, style.withColor(finalYo), glyph);
/*     */               });
/*     */         } 
/*     */       } 
/*     */     } 
/* 104 */     GlyphVisitor outlineGlyphVisitor = GlyphVisitor.forMultiBufferSource(bufferSource, pose, DisplayMode.NORMAL, packedLightCoords);
/* 105 */     for (TextRenderable.Styled glyphInstance : outlineOutput.glyphs) {
/* 106 */       outlineGlyphVisitor.acceptGlyph(glyphInstance);
/*     */     }
/*     */     
/* 109 */     PreparedTextBuilder primaryOutput = new PreparedTextBuilder(x, y, color, false, true);
/* 110 */     str.accept(primaryOutput);
/* 111 */     primaryOutput.visit(GlyphVisitor.forMultiBufferSource(bufferSource, pose, DisplayMode.POLYGON_OFFSET, packedLightCoords));
/*     */   }
/*     */   
/*     */   public enum DisplayMode {
/* 115 */     NORMAL,
/* 116 */     SEE_THROUGH,
/* 117 */     POLYGON_OFFSET;
/*     */   }
/*     */ 
/*     */   
/*     */   private class PreparedTextBuilder
/*     */     implements PreparedText, FormattedCharSink
/*     */   {
/*     */     private final boolean drawShadow;
/*     */     
/*     */     private final int color;
/*     */     
/*     */     private final int backgroundColor;
/*     */     
/*     */     private final boolean includeEmpty;
/*     */     
/*     */     private float x;
/*     */     
/*     */     private float y;
/* 135 */     private float left = Float.MAX_VALUE;
/* 136 */     private float top = Float.MAX_VALUE;
/* 137 */     private float right = -3.4028235E38F;
/* 138 */     private float bottom = -3.4028235E38F;
/*     */     
/* 140 */     private float backgroundLeft = Float.MAX_VALUE;
/* 141 */     private float backgroundTop = Float.MAX_VALUE;
/* 142 */     private float backgroundRight = -3.4028235E38F;
/* 143 */     private float backgroundBottom = -3.4028235E38F;
/*     */     
/* 145 */     private final List<TextRenderable.Styled> glyphs = new ArrayList<>();
/*     */     private List<TextRenderable> effects;
/*     */     private List<EmptyArea> emptyAreas;
/*     */     
/*     */     public PreparedTextBuilder(float x, float y, int color, boolean drawShadow, boolean includeEmpty) {
/* 150 */       this(x, y, color, 0, drawShadow, includeEmpty);
/*     */     }
/*     */     
/*     */     public PreparedTextBuilder(float x, float y, int color, int backgroundColor, boolean drawShadow, boolean includeEmpty) {
/* 154 */       this.x = x;
/* 155 */       this.y = y;
/* 156 */       this.drawShadow = drawShadow;
/* 157 */       this.color = color;
/* 158 */       this.backgroundColor = backgroundColor;
/* 159 */       this.includeEmpty = includeEmpty;
/* 160 */       markBackground(x, y, 0.0F);
/*     */     }
/*     */     
/*     */     private void markSize(float left, float top, float right, float bottom) {
/* 164 */       this.left = Math.min(this.left, left);
/* 165 */       this.top = Math.min(this.top, top);
/* 166 */       this.right = Math.max(this.right, right);
/* 167 */       this.bottom = Math.max(this.bottom, bottom);
/*     */     }
/*     */     
/*     */     private void markBackground(float x, float y, float advance) {
/* 171 */       if (ARGB.alpha(this.backgroundColor) == 0) {
/*     */         return;
/*     */       }
/* 174 */       this.backgroundLeft = Math.min(this.backgroundLeft, x - 1.0F);
/* 175 */       this.backgroundTop = Math.min(this.backgroundTop, y - 1.0F);
/* 176 */       this.backgroundRight = Math.max(this.backgroundRight, x + advance);
/* 177 */       this.backgroundBottom = Math.max(this.backgroundBottom, y + 9.0F);
/* 178 */       markSize(this.backgroundLeft, this.backgroundTop, this.backgroundRight, this.backgroundBottom);
/*     */     }
/*     */     
/*     */     private void addGlyph(TextRenderable.Styled instance) {
/* 182 */       this.glyphs.add(instance);
/* 183 */       markSize(instance.left(), instance.top(), instance.right(), instance.bottom());
/*     */     }
/*     */     
/*     */     private void addEffect(TextRenderable effect) {
/* 187 */       if (this.effects == null) {
/* 188 */         this.effects = new ArrayList<>();
/*     */       }
/* 190 */       this.effects.add(effect);
/* 191 */       markSize(effect.left(), effect.top(), effect.right(), effect.bottom());
/*     */     }
/*     */     
/*     */     private void addEmptyGlyph(EmptyArea empty) {
/* 195 */       if (this.emptyAreas == null) {
/* 196 */         this.emptyAreas = new ArrayList<>();
/*     */       }
/* 198 */       this.emptyAreas.add(empty);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean accept(int position, Style style, int c) {
/* 203 */       BakedGlyph glyph = Font.this.getGlyph(c, style);
/* 204 */       return accept(position, style, glyph);
/*     */     }
/*     */     
/*     */     public boolean accept(int position, Style style, BakedGlyph glyph) {
/* 208 */       GlyphInfo glyphInfo = glyph.info();
/*     */       
/* 210 */       boolean bold = style.isBold();
/*     */       
/* 212 */       TextColor styleColor = style.getColor();
/* 213 */       int textColor = getTextColor(styleColor);
/* 214 */       int shadowColor = getShadowColor(style, textColor);
/*     */       
/* 216 */       float advance = glyphInfo.getAdvance(bold);
/* 217 */       float effectX0 = (position == 0) ? (this.x - 1.0F) : this.x;
/* 218 */       float shadowOffset = glyphInfo.getShadowOffset();
/* 219 */       float boldOffset = bold ? glyphInfo.getBoldOffset() : 0.0F;
/*     */       
/* 221 */       TextRenderable.Styled instance = glyph.createGlyph(this.x, this.y, textColor, shadowColor, style, boldOffset, shadowOffset);
/* 222 */       if (instance != null) {
/* 223 */         addGlyph(instance);
/* 224 */       } else if (this.includeEmpty) {
/* 225 */         addEmptyGlyph(new EmptyArea(this.x, this.y, advance, 7.0F, 9.0F, style));
/*     */       } 
/*     */       
/* 228 */       markBackground(this.x, this.y, advance);
/*     */       
/* 230 */       if (style.isStrikethrough()) {
/* 231 */         addEffect(Font.this.provider.effect().createEffect(effectX0, this.y + 4.5F - 1.0F, this.x + advance, this.y + 4.5F, 0.01F, textColor, shadowColor, shadowOffset));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 243 */       if (style.isUnderlined()) {
/* 244 */         addEffect(Font.this.provider.effect().createEffect(effectX0, this.y + 9.0F - 1.0F, this.x + advance, this.y + 9.0F, 0.01F, textColor, shadowColor, shadowOffset));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 255 */       this.x += advance;
/* 256 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void visit(Font.GlyphVisitor visitor) {
/* 261 */       if (ARGB.alpha(this.backgroundColor) != 0) {
/* 262 */         visitor.acceptEffect(Font.this.provider.effect().createEffect(this.backgroundLeft, this.backgroundTop, this.backgroundRight, this.backgroundBottom, -0.01F, this.backgroundColor, 0, 0.0F));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 274 */       for (TextRenderable.Styled glyph : this.glyphs) {
/* 275 */         visitor.acceptGlyph(glyph);
/*     */       }
/*     */       
/* 278 */       if (this.effects != null) {
/* 279 */         for (TextRenderable effect : this.effects) {
/* 280 */           visitor.acceptEffect(effect);
/*     */         }
/*     */       }
/*     */       
/* 284 */       if (this.emptyAreas != null) {
/* 285 */         for (EmptyArea emptyArea : this.emptyAreas) {
/* 286 */           visitor.acceptEmptyArea(emptyArea);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private int getTextColor(TextColor textColor) {
/* 292 */       if (textColor != null) {
/* 293 */         int alpha = ARGB.alpha(this.color);
/* 294 */         int rgb = textColor.getValue();
/* 295 */         return ARGB.color(alpha, rgb);
/*     */       } 
/* 297 */       return this.color;
/*     */     }
/*     */     
/*     */     private int getShadowColor(Style style, int textColor) {
/* 301 */       Integer shadow = style.getShadowColor();
/* 302 */       if (shadow != null) {
/* 303 */         float textAlpha = ARGB.alphaFloat(textColor);
/* 304 */         float shadowAlpha = ARGB.alphaFloat(shadow);
/* 305 */         if (textAlpha != 1.0F) {
/* 306 */           return ARGB.color(ARGB.as8BitChannel(textAlpha * shadowAlpha), shadow);
/*     */         }
/* 308 */         return shadow;
/* 309 */       }  if (this.drawShadow) {
/* 310 */         return ARGB.scaleRGB(textColor, 0.25F);
/*     */       }
/* 312 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public ScreenRectangle bounds() {
/* 317 */       if (this.left >= this.right || this.top >= this.bottom) {
/* 318 */         return null;
/*     */       }
/* 320 */       int left = Mth.floor(this.left);
/* 321 */       int top = Mth.floor(this.top);
/* 322 */       int right = Mth.ceil(this.right);
/* 323 */       int bottom = Mth.ceil(this.bottom);
/* 324 */       return new ScreenRectangle(left, top, right - left, bottom - top);
/*     */     }
/*     */   }
/*     */   
/*     */   private BakedGlyph getGlyph(int codepoint, Style style) {
/* 329 */     GlyphSource glyphSource = getGlyphSource(style.getFont());
/* 330 */     BakedGlyph glyph = glyphSource.getGlyph(codepoint);
/* 331 */     if (style.isObfuscated() && codepoint != 32) {
/* 332 */       int targetWidth = Mth.ceil(glyph.info().getAdvance(false));
/* 333 */       glyph = glyphSource.getRandomGlyph(this.random, targetWidth);
/*     */     } 
/* 335 */     return glyph;
/*     */   }
/*     */   
/*     */   public PreparedText prepareText(String text, float x, float y, int originalColor, boolean drawShadow, int backgroundColor) {
/* 339 */     if (isBidirectional()) {
/* 340 */       text = bidirectionalShaping(text);
/*     */     }
/* 342 */     PreparedTextBuilder output = new PreparedTextBuilder(x, y, originalColor, backgroundColor, drawShadow, false);
/* 343 */     StringDecomposer.iterateFormatted(text, Style.EMPTY, output);
/* 344 */     return output;
/*     */   }
/*     */   
/*     */   public PreparedText prepareText(FormattedCharSequence text, float x, float y, int originalColor, boolean drawShadow, boolean includeEmpty, int backgroundColor) {
/* 348 */     PreparedTextBuilder builder = new PreparedTextBuilder(x, y, originalColor, backgroundColor, drawShadow, includeEmpty);
/* 349 */     text.accept(builder);
/* 350 */     return builder;
/*     */   }
/*     */   
/*     */   public int width(String str) {
/* 354 */     return Mth.ceil(this.splitter.stringWidth(str));
/*     */   }
/*     */   
/*     */   public int width(FormattedText text) {
/* 358 */     return Mth.ceil(this.splitter.stringWidth(text));
/*     */   }
/*     */   
/*     */   public int width(FormattedCharSequence text) {
/* 362 */     return Mth.ceil(this.splitter.stringWidth(text));
/*     */   }
/*     */   
/*     */   public String plainSubstrByWidth(String str, int width, boolean reverse) {
/* 366 */     return reverse ? this.splitter.plainTailByWidth(str, width, Style.EMPTY) : this.splitter.plainHeadByWidth(str, width, Style.EMPTY);
/*     */   }
/*     */   
/*     */   public String plainSubstrByWidth(String str, int width) {
/* 370 */     return this.splitter.plainHeadByWidth(str, width, Style.EMPTY);
/*     */   }
/*     */   
/*     */   public FormattedText substrByWidth(FormattedText text, int width) {
/* 374 */     return this.splitter.headByWidth(text, width, Style.EMPTY);
/*     */   }
/*     */   
/*     */   public int wordWrapHeight(FormattedText input, int textWidth) {
/* 378 */     return 9 * this.splitter.splitLines(input, textWidth, Style.EMPTY).size();
/*     */   }
/*     */   
/*     */   public List<FormattedCharSequence> split(FormattedText input, int maxWidth) {
/* 382 */     return Language.getInstance().getVisualOrder(this.splitter.splitLines(input, maxWidth, Style.EMPTY));
/*     */   }
/*     */   
/*     */   public List<FormattedText> splitIgnoringLanguage(FormattedText input, int maxWidth) {
/* 386 */     return this.splitter.splitLines(input, maxWidth, Style.EMPTY);
/*     */   }
/*     */   
/*     */   public boolean isBidirectional() {
/* 390 */     return Language.getInstance().isDefaultRightToLeft();
/*     */   }
/*     */   
/*     */   public StringSplitter getSplitter() {
/* 394 */     return this.splitter;
/*     */   }
/*     */   
/*     */   public static interface GlyphVisitor
/*     */   {
/* 399 */     static GlyphVisitor forMultiBufferSource(final MultiBufferSource bufferSource, final Matrix4f pose, final Font.DisplayMode displayMode, final int lightCoords) { return new GlyphVisitor()
/*     */         {
/*     */           public void acceptGlyph(TextRenderable.Styled glyph) {
/* 402 */             render((TextRenderable)glyph);
/*     */           }
/*     */ 
/*     */           
/*     */           public void acceptEffect(TextRenderable effect) {
/* 407 */             render(effect);
/*     */           }
/*     */           
/*     */           private void render(TextRenderable glyph) {
/* 411 */             VertexConsumer buffer = bufferSource.getBuffer(glyph.renderType(displayMode));
/* 412 */             glyph.render(pose, buffer, lightCoords, false); } }; } default void acceptGlyph(TextRenderable.Styled glyph) {} default void acceptEffect(TextRenderable effect) {} default void acceptEmptyArea(EmptyArea empty) {} } class null implements GlyphVisitor { private void render(TextRenderable glyph) { VertexConsumer buffer = bufferSource.getBuffer(glyph.renderType(displayMode)); glyph.render(pose, buffer, lightCoords, false); }
/*     */ 
/*     */     
/*     */     public void acceptGlyph(TextRenderable.Styled glyph) {
/*     */       render((TextRenderable)glyph);
/*     */     }
/*     */     
/*     */     public void acceptEffect(TextRenderable effect) {
/*     */       render(effect);
/*     */     } }
/*     */ 
/*     */   
/*     */   public static interface Provider {
/*     */     GlyphSource glyphs(FontDescription param1FontDescription);
/*     */     
/*     */     EffectGlyph effect();
/*     */   }
/*     */   
/*     */   public static interface PreparedText {
/*     */     void visit(Font.GlyphVisitor param1GlyphVisitor);
/*     */     
/*     */     ScreenRectangle bounds();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/Font.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */