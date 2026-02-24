/*     */ package net.minecraft.client.gui.components.debugchart;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.debugchart.SampleStorage;
/*     */ 
/*     */ public abstract class AbstractDebugChart {
/*     */   protected static final int CHART_HEIGHT = 60;
/*     */   protected static final int LINE_WIDTH = 1;
/*     */   protected final Font font;
/*     */   protected final SampleStorage sampleStorage;
/*     */   
/*     */   protected AbstractDebugChart(Font font, SampleStorage sampleStorage) {
/*  17 */     this.font = font;
/*  18 */     this.sampleStorage = sampleStorage;
/*     */   }
/*     */   
/*     */   public int getWidth(int maxWidth) {
/*  22 */     return Math.min(this.sampleStorage.capacity() + 2, maxWidth);
/*     */   }
/*     */   
/*     */   public int getFullHeight() {
/*  26 */     Objects.requireNonNull(this.font); return 60 + 9;
/*     */   }
/*     */   
/*     */   public void drawChart(GuiGraphics graphics, int left, int width) {
/*  30 */     int bottom = graphics.guiHeight();
/*  31 */     graphics.fill(left, bottom - 60, left + width, bottom, -1873784752);
/*     */     
/*  33 */     long avg = 0L;
/*  34 */     long min = 2147483647L;
/*  35 */     long max = -2147483648L;
/*  36 */     int startIndex = Math.max(0, this.sampleStorage.capacity() - width - 2);
/*  37 */     int sampleCount = this.sampleStorage.size() - startIndex;
/*  38 */     for (int i = 0; i < sampleCount; i++) {
/*  39 */       int currentX = left + i + 1;
/*  40 */       int sampleIndex = startIndex + i;
/*  41 */       long valueForAggregation = getValueForAggregation(sampleIndex);
/*  42 */       min = Math.min(min, valueForAggregation);
/*  43 */       max = Math.max(max, valueForAggregation);
/*  44 */       avg += valueForAggregation;
/*  45 */       drawDimensions(graphics, bottom, currentX, sampleIndex);
/*     */     } 
/*     */     
/*  48 */     graphics.hLine(left, left + width - 1, bottom - 60, -1);
/*  49 */     graphics.hLine(left, left + width - 1, bottom - 1, -1);
/*  50 */     graphics.vLine(left, bottom - 60, bottom, -1);
/*  51 */     graphics.vLine(left + width - 1, bottom - 60, bottom, -1);
/*     */     
/*  53 */     if (sampleCount > 0) {
/*  54 */       String minText = toDisplayString(min) + " min";
/*  55 */       String avgText = toDisplayString(avg / sampleCount) + " avg";
/*  56 */       String maxText = toDisplayString(max) + " max";
/*     */       
/*  58 */       Objects.requireNonNull(this.font); graphics.drawString(this.font, minText, left + 2, bottom - 60 - 9, -2039584);
/*  59 */       Objects.requireNonNull(this.font); graphics.drawCenteredString(this.font, avgText, left + width / 2, bottom - 60 - 9, -2039584);
/*  60 */       Objects.requireNonNull(this.font); graphics.drawString(this.font, maxText, left + width - this.font.width(maxText) - 2, bottom - 60 - 9, -2039584);
/*     */     } 
/*     */     
/*  63 */     renderAdditionalLinesAndLabels(graphics, left, width, bottom);
/*     */   }
/*     */   
/*     */   protected void drawDimensions(GuiGraphics graphics, int bottom, int currentX, int sampleIndex) {
/*  67 */     drawMainDimension(graphics, bottom, currentX, sampleIndex);
/*  68 */     drawAdditionalDimensions(graphics, bottom, currentX, sampleIndex);
/*     */   }
/*     */   
/*     */   protected void drawMainDimension(GuiGraphics graphics, int bottom, int currentX, int sampleIndex) {
/*  72 */     long value = this.sampleStorage.get(sampleIndex);
/*  73 */     int sampleHeight = getSampleHeight(value);
/*  74 */     int color = getSampleColor(value);
/*  75 */     graphics.fill(currentX, bottom - sampleHeight, currentX + 1, bottom, color);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawAdditionalDimensions(GuiGraphics graphics, int bottom, int currentX, int sampleIndex) {}
/*     */   
/*     */   protected long getValueForAggregation(int sampleIndex) {
/*  82 */     return this.sampleStorage.get(sampleIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderAdditionalLinesAndLabels(GuiGraphics graphics, int left, int width, int bottom) {}
/*     */   
/*     */   protected void drawStringWithShade(GuiGraphics graphics, String str, int x, int y) {
/*  89 */     Objects.requireNonNull(this.font); graphics.fill(x, y, x + this.font.width(str) + 1, y + 9, -1873784752);
/*  90 */     graphics.drawString(this.font, str, x + 1, y + 1, -2039584, false);
/*     */   }
/*     */   
/*     */   protected abstract String toDisplayString(double paramDouble);
/*     */   
/*     */   protected abstract int getSampleHeight(double paramDouble);
/*     */   
/*     */   protected abstract int getSampleColor(long paramLong);
/*     */   
/*     */   protected int getSampleColor(double sample, double min, int minColor, double mid, int midColor, double max, int maxColor) {
/* 100 */     sample = Mth.clamp(sample, min, max);
/* 101 */     if (sample < mid) {
/* 102 */       return ARGB.srgbLerp((float)((sample - min) / (mid - min)), minColor, midColor);
/*     */     }
/* 104 */     return ARGB.srgbLerp((float)((sample - mid) / (max - mid)), midColor, maxColor);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debugchart/AbstractDebugChart.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */