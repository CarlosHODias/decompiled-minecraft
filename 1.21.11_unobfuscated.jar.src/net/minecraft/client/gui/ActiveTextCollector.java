/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.gui.font.ActiveArea;
/*     */ import net.minecraft.client.gui.font.EmptyArea;
/*     */ import net.minecraft.client.gui.font.TextRenderable;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.render.state.GuiTextRenderState;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import org.joml.Matrix3x2f;
/*     */ import org.joml.Matrix3x2fc;
/*     */ import org.joml.Vector2f;
/*     */ 
/*     */ 
/*     */ public interface ActiveTextCollector
/*     */ {
/*     */   public static final double PERIOD_PER_SCROLLED_PIXEL = 0.5D;
/*     */   public static final double MIN_SCROLL_PERIOD = 3.0D;
/*     */   
/*     */   Parameters defaultParameters();
/*     */   
/*     */   void defaultParameters(Parameters paramParameters);
/*     */   
/*     */   default void accept(int x, int y, FormattedCharSequence text) {
/*  32 */     accept(TextAlignment.LEFT, x, y, defaultParameters(), text);
/*     */   }
/*     */   
/*     */   default void accept(int x, int y, Component text) {
/*  36 */     accept(TextAlignment.LEFT, x, y, defaultParameters(), text.getVisualOrderText());
/*     */   }
/*     */   
/*     */   default void accept(TextAlignment alignment, int anchorX, int y, Parameters parameters, Component text) {
/*  40 */     accept(alignment, anchorX, y, parameters, text.getVisualOrderText());
/*     */   }
/*     */   
/*     */   void accept(TextAlignment paramTextAlignment, int paramInt1, int paramInt2, Parameters paramParameters, FormattedCharSequence paramFormattedCharSequence);
/*     */   
/*     */   default void accept(TextAlignment alignment, int anchorX, int y, Component text) {
/*  46 */     accept(alignment, anchorX, y, text.getVisualOrderText());
/*     */   }
/*     */   
/*     */   default void accept(TextAlignment alignment, int anchorX, int y, FormattedCharSequence text) {
/*  50 */     accept(alignment, anchorX, y, defaultParameters(), text);
/*     */   }
/*     */ 
/*     */   
/*     */   void acceptScrolling(Component paramComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Parameters paramParameters);
/*     */   
/*     */   default void acceptScrolling(Component message, int centerX, int left, int right, int top, int bottom) {
/*  57 */     acceptScrolling(message, centerX, left, right, top, bottom, defaultParameters());
/*     */   }
/*     */   
/*     */   default void acceptScrollingWithDefaultCenter(Component message, int left, int right, int top, int bottom) {
/*  61 */     acceptScrolling(message, (left + right) / 2, left, right, top, bottom);
/*     */   }
/*     */   
/*     */   default void defaultScrollingHelper(Component message, int centerX, int left, int right, int top, int bottom, int lineWidth, int lineHeight, Parameters parameters) {
/*  65 */     int textTop = (top + bottom - lineHeight) / 2 + 1;
/*  66 */     int availableMessageWidth = right - left;
/*  67 */     if (lineWidth > availableMessageWidth) {
/*  68 */       int maxPosition = lineWidth - availableMessageWidth;
/*  69 */       double time = Util.getMillis() / 1000.0D;
/*  70 */       double period = Math.max(maxPosition * 0.5D, 3.0D);
/*  71 */       double alpha = Math.sin(1.5707963267948966D * Math.cos(6.283185307179586D * time / period)) / 2.0D + 0.5D;
/*  72 */       double pos = Mth.lerp(alpha, 0.0D, maxPosition);
/*  73 */       Parameters localParameters = parameters.withScissor(left, right, top, bottom);
/*  74 */       accept(TextAlignment.LEFT, left - (int)pos, textTop, localParameters, message.getVisualOrderText());
/*     */     } else {
/*  76 */       int textX = Mth.clamp(centerX, left + lineWidth / 2, right - lineWidth / 2);
/*  77 */       accept(TextAlignment.CENTER, textX, textTop, message);
/*     */     } 
/*     */   } public static final class Parameters extends Record {
/*     */     private final Matrix3x2fc pose; private final float opacity; private final ScreenRectangle scissor; public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/ActiveTextCollector$Parameters;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/ActiveTextCollector$Parameters;
/*     */     } public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/ActiveTextCollector$Parameters;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/ActiveTextCollector$Parameters;
/*     */     }
/*  85 */     public Parameters(Matrix3x2fc pose, float opacity, ScreenRectangle scissor) { this.pose = pose; this.opacity = opacity; this.scissor = scissor; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/ActiveTextCollector$Parameters;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/ActiveTextCollector$Parameters;
/*  85 */       //   0	8	1	o	Ljava/lang/Object; } public Matrix3x2fc pose() { return this.pose; } public float opacity() { return this.opacity; } public ScreenRectangle scissor() { return this.scissor; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Parameters(Matrix3x2fc pose) {
/*  91 */       this(pose, 1.0F, null);
/*     */     }
/*     */     
/*     */     public Parameters withPose(Matrix3x2fc pose) {
/*  95 */       return new Parameters(pose, this.opacity, this.scissor);
/*     */     }
/*     */ 
/*     */     
/*     */     public Parameters withScale(float scale) {
/* 100 */       return withPose((Matrix3x2fc)this.pose.scale(scale, scale, new Matrix3x2f()));
/*     */     }
/*     */     
/*     */     public Parameters withOpacity(float opacity) {
/* 104 */       if (this.opacity == opacity) {
/* 105 */         return this;
/*     */       }
/* 107 */       return new Parameters(this.pose, opacity, this.scissor);
/*     */     }
/*     */     
/*     */     public Parameters withScissor(ScreenRectangle scissor) {
/* 111 */       if (scissor.equals(this.scissor)) {
/* 112 */         return this;
/*     */       }
/* 114 */       return new Parameters(this.pose, this.opacity, scissor);
/*     */     }
/*     */     
/*     */     public Parameters withScissor(int left, int right, int top, int bottom) {
/* 118 */       ScreenRectangle newScissor = new ScreenRectangle(left, top, right - left, bottom - top).transformAxisAligned(this.pose);
/* 119 */       if (this.scissor != null) {
/* 120 */         newScissor = Objects.<ScreenRectangle>requireNonNullElse(this.scissor.intersection(newScissor), ScreenRectangle.empty());
/*     */       }
/* 122 */       return withScissor(newScissor);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void findElementUnderCursor(GuiTextRenderState text, float testX, float testY, final Consumer<Style> output) {
/* 129 */     ScreenRectangle bounds = text.bounds();
/* 130 */     if (bounds == null || !bounds.containsPoint((int)testX, (int)testY)) {
/*     */       return;
/*     */     }
/*     */     
/* 134 */     Vector2f vector2f = text.pose.invert(new Matrix3x2f()).transformPosition(new Vector2f(testX, testY));
/* 135 */     final float localMouseX = vector2f.x();
/* 136 */     final float localMouseY = vector2f.y();
/* 137 */     text.ensurePrepared().visit(new Font.GlyphVisitor()
/*     */         {
/*     */           public void acceptGlyph(TextRenderable.Styled glyph) {
/* 140 */             acceptActiveArea((ActiveArea)glyph);
/*     */           }
/*     */ 
/*     */           
/*     */           public void acceptEmptyArea(EmptyArea empty) {
/* 145 */             acceptActiveArea((ActiveArea)empty);
/*     */           }
/*     */           
/*     */           private void acceptActiveArea(ActiveArea glyph) {
/* 149 */             if (ActiveTextCollector.isPointInRectangle(localMouseX, localMouseY, glyph.activeLeft(), glyph.activeTop(), glyph.activeRight(), glyph.activeBottom())) {
/* 150 */               output.accept(glyph.style());
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   static boolean isPointInRectangle(float x, float y, float left, float top, float right, float bottom) {
/* 157 */     return (x >= left && x < right && y >= top && y < bottom);
/*     */   }
/*     */   
/*     */   public static class ClickableStyleFinder implements ActiveTextCollector {
/* 161 */     private static final ActiveTextCollector.Parameters INITIAL = new ActiveTextCollector.Parameters((Matrix3x2fc)new Matrix3x2f());
/*     */     
/*     */     private final Font font;
/*     */     
/*     */     private final int testX;
/*     */     private final int testY;
/* 167 */     private ActiveTextCollector.Parameters defaultParameters = INITIAL;
/*     */     private boolean includeInsertions;
/*     */     private Style result;
/*     */     private final Consumer<Style> styleScanner;
/*     */     
/*     */     public ClickableStyleFinder(Font font, int testX, int testY) {
/* 173 */       this.styleScanner = (style -> {
/*     */           if (style.getClickEvent() != null || (this.includeInsertions && style.getInsertion() != null)) {
/*     */             this.result = style;
/*     */           }
/*     */         });
/*     */ 
/*     */       
/* 180 */       this.font = font;
/* 181 */       this.testX = testX;
/* 182 */       this.testY = testY;
/*     */     }
/*     */ 
/*     */     
/*     */     public ActiveTextCollector.Parameters defaultParameters() {
/* 187 */       return this.defaultParameters;
/*     */     }
/*     */ 
/*     */     
/*     */     public void defaultParameters(ActiveTextCollector.Parameters newParameters) {
/* 192 */       this.defaultParameters = newParameters;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(TextAlignment alignment, int anchorX, int y, ActiveTextCollector.Parameters parameters, FormattedCharSequence text) {
/* 197 */       int leftX = alignment.calculateLeft(anchorX, this.font, text);
/* 198 */       GuiTextRenderState renderState = new GuiTextRenderState(this.font, text, parameters.pose(), leftX, y, ARGB.white(parameters.opacity()), 0, true, true, parameters.scissor());
/* 199 */       ActiveTextCollector.findElementUnderCursor(renderState, this.testX, this.testY, this.styleScanner);
/*     */     }
/*     */ 
/*     */     
/*     */     public void acceptScrolling(Component message, int centerX, int left, int right, int top, int bottom, ActiveTextCollector.Parameters parameters) {
/* 204 */       int lineWidth = this.font.width((FormattedText)message);
/* 205 */       Objects.requireNonNull(this.font); int lineHeight = 9;
/* 206 */       defaultScrollingHelper(message, centerX, left, right, top, bottom, lineWidth, lineHeight, parameters);
/*     */     }
/*     */     
/*     */     public ClickableStyleFinder includeInsertions(boolean flag) {
/* 210 */       this.includeInsertions = flag;
/* 211 */       return this;
/*     */     }
/*     */     
/*     */     public Style result() {
/* 215 */       return this.result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/ActiveTextCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */