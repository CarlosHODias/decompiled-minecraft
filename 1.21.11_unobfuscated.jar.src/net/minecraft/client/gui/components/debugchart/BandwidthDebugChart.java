/*    */ package net.minecraft.client.gui.components.debugchart;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.debugchart.SampleStorage;
/*    */ 
/*    */ 
/*    */ public class BandwidthDebugChart
/*    */   extends AbstractDebugChart
/*    */ {
/*    */   private static final int MIN_COLOR = -16711681;
/*    */   private static final int MID_COLOR = -6250241;
/*    */   private static final int MAX_COLOR = -65536;
/*    */   private static final int KILOBYTE = 1024;
/*    */   private static final int MEGABYTE = 1048576;
/*    */   private static final int CHART_TOP_VALUE = 1048576;
/*    */   
/*    */   public BandwidthDebugChart(Font font, SampleStorage sampleStorage) {
/* 21 */     super(font, sampleStorage);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderAdditionalLinesAndLabels(GuiGraphics graphics, int left, int width, int bottom) {
/* 26 */     drawLabeledLineAtValue(graphics, left, width, bottom, 64);
/* 27 */     drawLabeledLineAtValue(graphics, left, width, bottom, 1024);
/* 28 */     drawLabeledLineAtValue(graphics, left, width, bottom, 16384);
/* 29 */     drawStringWithShade(graphics, toDisplayStringInternal(1048576.0D), left + 1, bottom - getSampleHeightInternal(1048576.0D) + 1);
/*    */   }
/*    */   
/*    */   private void drawLabeledLineAtValue(GuiGraphics graphics, int left, int width, int bottom, int bytesPerSecond) {
/* 33 */     drawLineWithLabel(graphics, left, width, bottom - getSampleHeightInternal(bytesPerSecond), toDisplayStringInternal(bytesPerSecond));
/*    */   }
/*    */   
/*    */   private void drawLineWithLabel(GuiGraphics graphics, int x, int width, int y, String label) {
/* 37 */     drawStringWithShade(graphics, label, x + 1, y + 1);
/* 38 */     graphics.hLine(x, x + width - 1, y, -1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String toDisplayString(double bytesPerTick) {
/* 43 */     return toDisplayStringInternal(toBytesPerSecond(bytesPerTick));
/*    */   }
/*    */   
/*    */   private static String toDisplayStringInternal(double bytesPerSecond) {
/* 47 */     if (bytesPerSecond >= 1048576.0D) {
/* 48 */       return String.format(Locale.ROOT, "%.1f MiB/s", new Object[] { bytesPerSecond / 1048576.0D });
/*    */     }
/* 50 */     if (bytesPerSecond >= 1024.0D) {
/* 51 */       return String.format(Locale.ROOT, "%.1f KiB/s", new Object[] { bytesPerSecond / 1024.0D });
/*    */     }
/* 53 */     return String.format(Locale.ROOT, "%d B/s", new Object[] { Mth.floor(bytesPerSecond) });
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getSampleHeight(double bytesPerTick) {
/* 58 */     return getSampleHeightInternal(toBytesPerSecond(bytesPerTick));
/*    */   }
/*    */   
/*    */   private static int getSampleHeightInternal(double bytesPerSecond) {
/* 62 */     return (int)Math.round(Math.log(bytesPerSecond + 1.0D) * 60.0D / Math.log(1048576.0D));
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getSampleColor(long bytesPerTick) {
/* 67 */     return getSampleColor(toBytesPerSecond(bytesPerTick), 0.0D, -16711681, 8192.0D, -6250241, 1.048576E7D, -65536);
/*    */   }
/*    */   
/*    */   private static double toBytesPerSecond(double bytesPerTick) {
/* 71 */     return bytesPerTick * 20.0D;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debugchart/BandwidthDebugChart.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */