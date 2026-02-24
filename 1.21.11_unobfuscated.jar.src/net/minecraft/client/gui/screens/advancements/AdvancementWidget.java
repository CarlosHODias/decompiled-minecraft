/*     */ package net.minecraft.client.gui.screens.advancements;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.advancements.AdvancementNode;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.advancements.DisplayInfo;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.StringSplitter;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class AdvancementWidget
/*     */ {
/*  26 */   private static final Identifier TITLE_BOX_SPRITE = Identifier.withDefaultNamespace("advancements/title_box");
/*     */   private static final int HEIGHT = 26;
/*     */   private static final int BOX_X = 0;
/*     */   private static final int BOX_WIDTH = 200;
/*     */   private static final int FRAME_WIDTH = 26;
/*     */   private static final int ICON_X = 8;
/*     */   private static final int ICON_Y = 5;
/*     */   private static final int ICON_WIDTH = 26;
/*     */   private static final int TITLE_PADDING_LEFT = 3;
/*     */   private static final int TITLE_PADDING_RIGHT = 5;
/*     */   private static final int TITLE_X = 32;
/*     */   private static final int TITLE_PADDING_TOP = 9;
/*     */   private static final int TITLE_PADDING_BOTTOM = 8;
/*     */   private static final int TITLE_MAX_WIDTH = 163;
/*     */   private static final int TITLE_MIN_WIDTH = 80;
/*  41 */   private static final int[] TEST_SPLIT_OFFSETS = new int[] { 0, 10, -10, 25, -25 };
/*     */   
/*     */   private final AdvancementTab tab;
/*     */   private final AdvancementNode advancementNode;
/*     */   private final DisplayInfo display;
/*     */   private final List<FormattedCharSequence> titleLines;
/*     */   private final int width;
/*     */   private final List<FormattedCharSequence> description;
/*     */   private final Minecraft minecraft;
/*     */   private AdvancementWidget parent;
/*  51 */   private final List<AdvancementWidget> children = Lists.newArrayList();
/*     */   private AdvancementProgress progress;
/*     */   private final int x;
/*     */   private final int y;
/*     */   
/*     */   public AdvancementWidget(AdvancementTab tab, Minecraft minecraft, AdvancementNode advancementNode, DisplayInfo display) {
/*  57 */     this.tab = tab;
/*  58 */     this.advancementNode = advancementNode;
/*  59 */     this.display = display;
/*  60 */     this.minecraft = minecraft;
/*  61 */     this.titleLines = minecraft.font.split((FormattedText)display.getTitle(), 163);
/*  62 */     this.x = Mth.floor(display.getX() * 28.0F);
/*  63 */     this.y = Mth.floor(display.getY() * 27.0F);
/*     */     
/*  65 */     Objects.requireNonNull(minecraft.font); int titleWidth = Math.max(this.titleLines.stream().mapToInt(minecraft.font::width).max().orElse(0), 80);
/*     */     
/*  67 */     int maxProgressWidth = getMaxProgressWidth();
/*  68 */     int longestDescLine = 29 + titleWidth + maxProgressWidth;
/*  69 */     this.description = Language.getInstance().getVisualOrder(findOptimalLines(ComponentUtils.mergeStyles(display.getDescription(), Style.EMPTY.withColor(display.getType().getChatColor())), longestDescLine));
/*  70 */     for (FormattedCharSequence line : this.description) {
/*  71 */       longestDescLine = Math.max(longestDescLine, minecraft.font.width(line));
/*     */     }
/*  73 */     this.width = longestDescLine + 3 + 5;
/*     */   }
/*     */   
/*     */   private int getMaxProgressWidth() {
/*  77 */     int maxCriteraRequired = this.advancementNode.advancement().requirements().size();
/*  78 */     if (maxCriteraRequired <= 1) {
/*  79 */       return 0;
/*     */     }
/*  81 */     int spacing = 8;
/*  82 */     MutableComponent mutableComponent = Component.translatable("advancements.progress", new Object[] { maxCriteraRequired, maxCriteraRequired });
/*  83 */     return this.minecraft.font.width((FormattedText)mutableComponent) + 8;
/*     */   }
/*     */   
/*     */   private static float getMaxWidth(StringSplitter splitter, List<FormattedText> input) {
/*  87 */     Objects.requireNonNull(splitter); return (float)input.stream().mapToDouble(splitter::stringWidth).max().orElse(0.0D);
/*     */   }
/*     */   
/*     */   private List<FormattedText> findOptimalLines(Component input, int preferredWidth) {
/*  91 */     StringSplitter splitter = this.minecraft.font.getSplitter();
/*     */     
/*  93 */     List<FormattedText> bestSplit = null;
/*  94 */     float bestDistance = Float.MAX_VALUE;
/*     */     
/*  96 */     for (int testMargin : TEST_SPLIT_OFFSETS) {
/*  97 */       List<FormattedText> testSplit = splitter.splitLines((FormattedText)input, preferredWidth - testMargin, Style.EMPTY);
/*  98 */       float distance = Math.abs(getMaxWidth(splitter, testSplit) - preferredWidth);
/*  99 */       if (distance <= 10.0F) {
/* 100 */         return testSplit;
/*     */       }
/* 102 */       if (distance < bestDistance) {
/* 103 */         bestDistance = distance;
/* 104 */         bestSplit = testSplit;
/*     */       } 
/*     */     } 
/*     */     
/* 108 */     return bestSplit;
/*     */   }
/*     */   
/*     */   private AdvancementWidget getFirstVisibleParent(AdvancementNode node) {
/*     */     do {
/* 113 */       node = node.parent();
/* 114 */     } while (node != null && node.advancement().display().isEmpty());
/* 115 */     if (node == null || node.advancement().display().isEmpty()) {
/* 116 */       return null;
/*     */     }
/* 118 */     return this.tab.getWidget(node.holder());
/*     */   }
/*     */   
/*     */   public void drawConnectivity(GuiGraphics graphics, int xo, int yo, boolean background) {
/* 122 */     if (this.parent != null) {
/* 123 */       int depX = xo + this.parent.x + 13;
/* 124 */       int splitX = xo + this.parent.x + 26 + 4;
/* 125 */       int depY = yo + this.parent.y + 13;
/* 126 */       int myX = xo + this.x + 13;
/* 127 */       int myY = yo + this.y + 13;
/* 128 */       int col = background ? -16777216 : -1;
/* 129 */       if (background) {
/* 130 */         graphics.hLine(splitX, depX, depY - 1, col);
/* 131 */         graphics.hLine(splitX + 1, depX, depY, col);
/* 132 */         graphics.hLine(splitX, depX, depY + 1, col);
/* 133 */         graphics.hLine(myX, splitX - 1, myY - 1, col);
/* 134 */         graphics.hLine(myX, splitX - 1, myY, col);
/* 135 */         graphics.hLine(myX, splitX - 1, myY + 1, col);
/* 136 */         graphics.vLine(splitX - 1, myY, depY, col);
/* 137 */         graphics.vLine(splitX + 1, myY, depY, col);
/*     */       } else {
/* 139 */         graphics.hLine(splitX, depX, depY, col);
/* 140 */         graphics.hLine(myX, splitX, myY, col);
/* 141 */         graphics.vLine(splitX, myY, depY, col);
/*     */       } 
/*     */     } 
/*     */     
/* 145 */     for (AdvancementWidget child : this.children) {
/* 146 */       child.drawConnectivity(graphics, xo, yo, background);
/*     */     }
/*     */   }
/*     */   
/*     */   public void draw(GuiGraphics graphics, int xo, int yo) {
/* 151 */     if (!this.display.isHidden() || (this.progress != null && this.progress.isDone())) {
/* 152 */       AdvancementWidgetType iconFrame; float amount = (this.progress == null) ? 0.0F : this.progress.getPercent();
/*     */ 
/*     */       
/* 155 */       if (amount >= 1.0F) {
/* 156 */         iconFrame = AdvancementWidgetType.OBTAINED;
/*     */       } else {
/* 158 */         iconFrame = AdvancementWidgetType.UNOBTAINED;
/*     */       } 
/*     */       
/* 161 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, iconFrame.frameSprite(this.display.getType()), xo + this.x + 3, yo + this.y, 26, 26);
/* 162 */       graphics.renderFakeItem(this.display.getIcon(), xo + this.x + 8, yo + this.y + 5);
/*     */     } 
/*     */     
/* 165 */     for (AdvancementWidget child : this.children) {
/* 166 */       child.draw(graphics, xo, yo);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 171 */     return this.width;
/*     */   }
/*     */   
/*     */   public void setProgress(AdvancementProgress progress) {
/* 175 */     this.progress = progress;
/*     */   }
/*     */   
/*     */   public void addChild(AdvancementWidget widget) {
/* 179 */     this.children.add(widget);
/*     */   } public void drawHover(GuiGraphics graphics, int xo, int yo, float fade, int screenxo, int screenyo) {
/*     */     AdvancementWidgetType firstHalf, secondHalf, iconFrame;
/*     */     int titleLeft;
/* 183 */     Font font = this.minecraft.font;
/* 184 */     Objects.requireNonNull(font); int titleBarHeight = 9 * this.titleLines.size() + 9 + 8;
/*     */     
/* 186 */     int titleTop = yo + this.y + (26 - titleBarHeight) / 2;
/* 187 */     int titleBarBottom = titleTop + titleBarHeight;
/* 188 */     Objects.requireNonNull(font); int descriptionTextHeight = this.description.size() * 9;
/* 189 */     int descriptionHeight = 6 + descriptionTextHeight;
/*     */     
/* 191 */     boolean leftSide = (screenxo + xo + this.x + this.width + 26 >= (this.tab.getScreen()).width);
/* 192 */     Component progressText = (this.progress == null) ? null : this.progress.getProgressText();
/* 193 */     int progressWidth = (progressText == null) ? 0 : font.width((FormattedText)progressText);
/* 194 */     boolean topSide = (titleBarBottom + descriptionHeight >= 113);
/* 195 */     float amount = (this.progress == null) ? 0.0F : this.progress.getPercent();
/*     */ 
/*     */ 
/*     */     
/* 199 */     int firstHalfWidth = Mth.floor(amount * this.width);
/*     */     
/* 201 */     if (amount >= 1.0F) {
/* 202 */       firstHalfWidth = this.width / 2;
/* 203 */       firstHalf = AdvancementWidgetType.OBTAINED;
/* 204 */       secondHalf = AdvancementWidgetType.OBTAINED;
/* 205 */       iconFrame = AdvancementWidgetType.OBTAINED;
/* 206 */     } else if (firstHalfWidth < 2) {
/* 207 */       firstHalfWidth = this.width / 2;
/* 208 */       firstHalf = AdvancementWidgetType.UNOBTAINED;
/* 209 */       secondHalf = AdvancementWidgetType.UNOBTAINED;
/* 210 */       iconFrame = AdvancementWidgetType.UNOBTAINED;
/* 211 */     } else if (firstHalfWidth > this.width - 2) {
/* 212 */       firstHalfWidth = this.width / 2;
/* 213 */       firstHalf = AdvancementWidgetType.OBTAINED;
/* 214 */       secondHalf = AdvancementWidgetType.OBTAINED;
/* 215 */       iconFrame = AdvancementWidgetType.UNOBTAINED;
/*     */     } else {
/* 217 */       firstHalf = AdvancementWidgetType.OBTAINED;
/* 218 */       secondHalf = AdvancementWidgetType.UNOBTAINED;
/* 219 */       iconFrame = AdvancementWidgetType.UNOBTAINED;
/*     */     } 
/* 221 */     int secondBarWidth = this.width - firstHalfWidth;
/*     */ 
/*     */     
/* 224 */     if (leftSide) {
/* 225 */       titleLeft = xo + this.x - this.width + 26 + 6;
/*     */     } else {
/* 227 */       titleLeft = xo + this.x;
/*     */     } 
/*     */     
/* 230 */     int backgroundHeight = titleBarHeight + descriptionHeight;
/* 231 */     if (!this.description.isEmpty()) {
/* 232 */       if (topSide) {
/* 233 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TITLE_BOX_SPRITE, titleLeft, titleBarBottom - backgroundHeight, this.width, backgroundHeight);
/*     */       } else {
/* 235 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TITLE_BOX_SPRITE, titleLeft, titleTop, this.width, backgroundHeight);
/*     */       } 
/*     */     }
/*     */     
/* 239 */     if (firstHalf != secondHalf) {
/* 240 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, firstHalf.boxSprite(), 200, titleBarHeight, 0, 0, titleLeft, titleTop, firstHalfWidth, titleBarHeight);
/* 241 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, secondHalf.boxSprite(), 200, titleBarHeight, 200 - secondBarWidth, 0, titleLeft + firstHalfWidth, titleTop, secondBarWidth, titleBarHeight);
/*     */     } else {
/* 243 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, firstHalf.boxSprite(), titleLeft, titleTop, this.width, titleBarHeight);
/*     */     } 
/*     */     
/* 246 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, iconFrame.frameSprite(this.display.getType()), xo + this.x + 3, yo + this.y, 26, 26);
/*     */     
/* 248 */     int descriptionLeft = titleLeft + 5;
/*     */     
/* 250 */     if (leftSide) {
/* 251 */       drawMultilineText(graphics, this.titleLines, descriptionLeft, titleTop + 9, -1);
/* 252 */       if (progressText != null) {
/* 253 */         graphics.drawString(font, progressText, xo + this.x - progressWidth, titleTop + 9, -1);
/*     */       }
/*     */     } else {
/* 256 */       drawMultilineText(graphics, this.titleLines, xo + this.x + 32, titleTop + 9, -1);
/* 257 */       if (progressText != null) {
/* 258 */         graphics.drawString(font, progressText, xo + this.x + this.width - progressWidth - 5, titleTop + 9, -1);
/*     */       }
/*     */     } 
/*     */     
/* 262 */     if (topSide) {
/* 263 */       drawMultilineText(graphics, this.description, descriptionLeft, titleTop - descriptionTextHeight + 1, -16711936);
/*     */     } else {
/* 265 */       drawMultilineText(graphics, this.description, descriptionLeft, titleBarBottom, -16711936);
/*     */     } 
/*     */     
/* 268 */     graphics.renderFakeItem(this.display.getIcon(), xo + this.x + 8, yo + this.y + 5);
/*     */   }
/*     */   
/*     */   private void drawMultilineText(GuiGraphics graphics, List<FormattedCharSequence> lines, int x, int y, int color) {
/* 272 */     Font font = this.minecraft.font;
/* 273 */     for (int i = 0; i < lines.size(); i++) {
/* 274 */       Objects.requireNonNull(font); graphics.drawString(font, lines.get(i), x, y + i * 9, color);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isMouseOver(int xo, int yo, int mouseX, int mouseY) {
/* 279 */     if (this.display.isHidden() && (this.progress == null || !this.progress.isDone())) {
/* 280 */       return false;
/*     */     }
/* 282 */     int x0 = xo + this.x;
/* 283 */     int x1 = x0 + 26;
/* 284 */     int y0 = yo + this.y;
/* 285 */     int y1 = y0 + 26;
/* 286 */     return (mouseX >= x0 && mouseX <= x1 && mouseY >= y0 && mouseY <= y1);
/*     */   }
/*     */   
/*     */   public void attachToParent() {
/* 290 */     if (this.parent == null && this.advancementNode.parent() != null) {
/* 291 */       this.parent = getFirstVisibleParent(this.advancementNode);
/* 292 */       if (this.parent != null) {
/* 293 */         this.parent.addChild(this);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getY() {
/* 299 */     return this.y;
/*     */   }
/*     */   
/*     */   public int getX() {
/* 303 */     return this.x;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/advancements/AdvancementWidget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */