/*     */ package net.minecraft.client.gui.components.debugchart;
/*     */ 
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.DecimalFormatSymbols;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.util.profiling.ProfileResults;
/*     */ import net.minecraft.util.profiling.ResultField;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProfilerPieChart
/*     */ {
/*     */   public static final int RADIUS = 105;
/*     */   public static final int PIE_CHART_THICKNESS = 10;
/*     */   private static final int MARGIN = 5;
/*     */   private final Font font;
/*     */   private ProfileResults profilerPieChartResults;
/*  23 */   private String profilerTreePath = "root";
/*  24 */   private int bottomOffset = 0;
/*     */   
/*     */   public ProfilerPieChart(Font font) {
/*  27 */     this.font = font;
/*     */   }
/*     */   
/*     */   public void setPieChartResults(ProfileResults results) {
/*  31 */     this.profilerPieChartResults = results;
/*     */   }
/*     */   
/*     */   public void setBottomOffset(int bottomOffset) {
/*  35 */     this.bottomOffset = bottomOffset;
/*     */   }
/*     */   
/*     */   public void render(GuiGraphics graphics) {
/*  39 */     if (this.profilerPieChartResults == null) {
/*     */       return;
/*     */     }
/*     */     
/*  43 */     List<ResultField> list = this.profilerPieChartResults.getTimes(this.profilerTreePath);
/*  44 */     ResultField rootNode = list.removeFirst();
/*     */     
/*  46 */     int chartCenterX = graphics.guiWidth() - 105 - 10;
/*  47 */     int left = chartCenterX - 105;
/*  48 */     int right = chartCenterX + 105;
/*     */     
/*  50 */     Objects.requireNonNull(this.font); int textUnderChartHeight = list.size() * 9;
/*  51 */     int bottom = graphics.guiHeight() - this.bottomOffset - 5;
/*  52 */     int textStartY = bottom - textUnderChartHeight;
/*  53 */     int chartHalfSizeY = 62;
/*  54 */     int chartCenterY = textStartY - 62 - 5;
/*     */     
/*  56 */     graphics.fill(left - 5, chartCenterY - 62 - 5, right + 5, bottom + 5, -1873784752);
/*     */     
/*  58 */     graphics.submitProfilerChartRenderState(list, left, chartCenterY - 62 + 10, right, chartCenterY + 62);
/*     */     
/*  60 */     DecimalFormat format = new DecimalFormat("##0.00", DecimalFormatSymbols.getInstance(Locale.ROOT));
/*     */     
/*  62 */     String rootNodeName = ProfileResults.demanglePath(rootNode.name);
/*  63 */     String topText = "";
/*  64 */     if (!"unspecified".equals(rootNodeName)) {
/*  65 */       topText = topText + "[0] ";
/*     */     }
/*  67 */     if (rootNodeName.isEmpty()) {
/*  68 */       topText = topText + "ROOT ";
/*     */     } else {
/*  70 */       topText = topText + topText + " ";
/*     */     } 
/*  72 */     int col = -1;
/*  73 */     int topTextY = chartCenterY - 62;
/*  74 */     graphics.drawString(this.font, topText, left, topTextY, -1);
/*     */     
/*  76 */     topText = format.format(rootNode.globalPercentage) + "%";
/*  77 */     graphics.drawString(this.font, topText, right - this.font.width(topText), topTextY, -1);
/*     */     
/*  79 */     for (int i = 0; i < list.size(); i++) {
/*  80 */       ResultField result = list.get(i);
/*  81 */       StringBuilder string = new StringBuilder();
/*  82 */       if ("unspecified".equals(result.name)) {
/*  83 */         string.append("[?] ");
/*     */       } else {
/*  85 */         string.append("[").append(i + 1).append("] ");
/*     */       } 
/*     */       
/*  88 */       String msg = string.append(result.name).toString();
/*  89 */       Objects.requireNonNull(this.font); int textY = textStartY + i * 9;
/*  90 */       graphics.drawString(this.font, msg, left, textY, result.getColor());
/*  91 */       msg = format.format(result.percentage) + "%";
/*  92 */       graphics.drawString(this.font, msg, right - 50 - this.font.width(msg), textY, result.getColor());
/*  93 */       msg = format.format(result.globalPercentage) + "%";
/*  94 */       graphics.drawString(this.font, msg, right - this.font.width(msg), textY, result.getColor());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void profilerPieChartKeyPress(int key) {
/*  99 */     if (this.profilerPieChartResults == null) {
/*     */       return;
/*     */     }
/* 102 */     List<ResultField> list = this.profilerPieChartResults.getTimes(this.profilerTreePath);
/* 103 */     if (list.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 107 */     ResultField node = list.remove(0);
/* 108 */     if (key == 0) {
/* 109 */       if (!node.name.isEmpty()) {
/* 110 */         int pos = this.profilerTreePath.lastIndexOf('\036');
/* 111 */         if (pos >= 0) {
/* 112 */           this.profilerTreePath = this.profilerTreePath.substring(0, pos);
/*     */         }
/*     */       } 
/*     */     } else {
/* 116 */       key--;
/* 117 */       if (key < list.size() && !"unspecified".equals(((ResultField)list.get(key)).name)) {
/* 118 */         if (!this.profilerTreePath.isEmpty()) {
/* 119 */           this.profilerTreePath += "\036";
/*     */         }
/* 121 */         this.profilerTreePath += this.profilerTreePath;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debugchart/ProfilerPieChart.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */