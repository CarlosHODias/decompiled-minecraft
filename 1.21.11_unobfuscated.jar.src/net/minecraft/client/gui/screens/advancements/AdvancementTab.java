/*     */ package net.minecraft.client.gui.screens.advancements;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementNode;
/*     */ import net.minecraft.advancements.DisplayInfo;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.core.ClientAsset;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ 
/*     */ public class AdvancementTab
/*     */ {
/*     */   private final Minecraft minecraft;
/*     */   private final AdvancementsScreen screen;
/*     */   private final AdvancementTabType type;
/*     */   private final int index;
/*     */   private final AdvancementNode rootNode;
/*     */   private final DisplayInfo display;
/*     */   private final ItemStack icon;
/*     */   private final Component title;
/*     */   private final AdvancementWidget root;
/*  32 */   private final Map<AdvancementHolder, AdvancementWidget> widgets = Maps.newLinkedHashMap();
/*     */   private double scrollX;
/*     */   private double scrollY;
/*  35 */   private int minX = Integer.MAX_VALUE;
/*  36 */   private int minY = Integer.MAX_VALUE;
/*  37 */   private int maxX = Integer.MIN_VALUE;
/*  38 */   private int maxY = Integer.MIN_VALUE;
/*     */   private float fade;
/*     */   private boolean centered;
/*     */   
/*     */   public AdvancementTab(Minecraft minecraft, AdvancementsScreen screen, AdvancementTabType type, int index, AdvancementNode rootNode, DisplayInfo display) {
/*  43 */     this.minecraft = minecraft;
/*  44 */     this.screen = screen;
/*  45 */     this.type = type;
/*  46 */     this.index = index;
/*  47 */     this.rootNode = rootNode;
/*  48 */     this.display = display;
/*  49 */     this.icon = display.getIcon();
/*  50 */     this.title = display.getTitle();
/*  51 */     this.root = new AdvancementWidget(this, minecraft, rootNode, display);
/*  52 */     addWidget(this.root, rootNode.holder());
/*     */   }
/*     */   
/*     */   public AdvancementTabType getType() {
/*  56 */     return this.type;
/*     */   }
/*     */   
/*     */   public int getIndex() {
/*  60 */     return this.index;
/*     */   }
/*     */   
/*     */   public AdvancementNode getRootNode() {
/*  64 */     return this.rootNode;
/*     */   }
/*     */   
/*     */   public Component getTitle() {
/*  68 */     return this.title;
/*     */   }
/*     */   
/*     */   public DisplayInfo getDisplay() {
/*  72 */     return this.display;
/*     */   }
/*     */   
/*     */   public void drawTab(GuiGraphics graphics, int xo, int yo, int mouseX, int mouseY, boolean selected) {
/*  76 */     int tabX = xo + this.type.getX(this.index);
/*  77 */     int tabY = yo + this.type.getY(this.index);
/*  78 */     this.type.draw(graphics, tabX, tabY, selected, this.index);
/*  79 */     if (!selected && mouseX > tabX && mouseY > tabY && mouseX < tabX + this.type.getWidth() && mouseY < tabY + this.type.getHeight()) {
/*  80 */       graphics.requestCursor(CursorTypes.POINTING_HAND);
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawIcon(GuiGraphics graphics, int xo, int yo) {
/*  85 */     this.type.drawIcon(graphics, xo, yo, this.index, this.icon);
/*     */   }
/*     */   
/*     */   public void drawContents(GuiGraphics graphics, int windowLeft, int windowTop) {
/*  89 */     if (!this.centered) {
/*  90 */       this.scrollX = (117 - (this.maxX + this.minX) / 2);
/*  91 */       this.scrollY = (56 - (this.maxY + this.minY) / 2);
/*  92 */       this.centered = true;
/*     */     } 
/*     */     
/*  95 */     graphics.enableScissor(windowLeft, windowTop, windowLeft + 234, windowTop + 113);
/*  96 */     graphics.pose().pushMatrix();
/*  97 */     graphics.pose().translate(windowLeft, windowTop);
/*     */     
/*  99 */     Identifier background = this.display.getBackground().map(ClientAsset.ResourceTexture::texturePath).orElse(TextureManager.INTENTIONAL_MISSING_TEXTURE);
/* 100 */     int intScrollX = Mth.floor(this.scrollX);
/* 101 */     int intScrollY = Mth.floor(this.scrollY);
/* 102 */     int left = intScrollX % 16;
/* 103 */     int top = intScrollY % 16;
/*     */     
/* 105 */     for (int x = -1; x <= 15; x++) {
/* 106 */       for (int y = -1; y <= 8; y++) {
/* 107 */         graphics.blit(RenderPipelines.GUI_TEXTURED, background, left + 16 * x, top + 16 * y, 0.0F, 0.0F, 16, 16, 16, 16);
/*     */       }
/*     */     } 
/*     */     
/* 111 */     this.root.drawConnectivity(graphics, intScrollX, intScrollY, true);
/* 112 */     this.root.drawConnectivity(graphics, intScrollX, intScrollY, false);
/* 113 */     this.root.draw(graphics, intScrollX, intScrollY);
/*     */     
/* 115 */     graphics.pose().popMatrix();
/* 116 */     graphics.disableScissor();
/*     */   }
/*     */   
/*     */   public void drawTooltips(GuiGraphics graphics, int mouseX, int mouseY, int xo, int yo) {
/* 120 */     graphics.fill(0, 0, 234, 113, Mth.floor(this.fade * 255.0F) << 24);
/*     */     
/*     */     boolean hovering = false;
/* 123 */     int intScrollX = Mth.floor(this.scrollX);
/* 124 */     int intScrollY = Mth.floor(this.scrollY);
/* 125 */     if (mouseX > 0 && mouseX < 234 && mouseY > 0 && mouseY < 113) {
/* 126 */       for (AdvancementWidget widget : this.widgets.values()) {
/* 127 */         if (widget.isMouseOver(intScrollX, intScrollY, mouseX, mouseY)) {
/* 128 */           hovering = true;
/* 129 */           widget.drawHover(graphics, intScrollX, intScrollY, this.fade, xo, yo);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 135 */     if (hovering) {
/* 136 */       this.fade = Mth.clamp(this.fade + 0.02F, 0.0F, 0.3F);
/*     */     } else {
/* 138 */       this.fade = Mth.clamp(this.fade - 0.04F, 0.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isMouseOver(int xo, int yo, double mx, double my) {
/* 143 */     return this.type.isMouseOver(xo, yo, this.index, mx, my);
/*     */   }
/*     */   
/*     */   public static AdvancementTab create(Minecraft minecraft, AdvancementsScreen screen, int index, AdvancementNode root) {
/* 147 */     Optional<DisplayInfo> display = root.advancement().display();
/* 148 */     if (display.isEmpty()) {
/* 149 */       return null;
/*     */     }
/* 151 */     for (AdvancementTabType type : AdvancementTabType.values()) {
/* 152 */       if (index >= type.getMax()) {
/* 153 */         index -= type.getMax();
/*     */       } else {
/*     */         
/* 156 */         return new AdvancementTab(minecraft, screen, type, index, root, display.get());
/*     */       } 
/* 158 */     }  return null;
/*     */   }
/*     */   
/*     */   public void scroll(double x, double y) {
/* 162 */     if (canScrollHorizontally()) {
/* 163 */       this.scrollX = Mth.clamp(this.scrollX + x, -(this.maxX - 234), 0.0D);
/*     */     }
/* 165 */     if (canScrollVertically()) {
/* 166 */       this.scrollY = Mth.clamp(this.scrollY + y, -(this.maxY - 113), 0.0D);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean canScrollHorizontally() {
/* 171 */     return (this.maxX - this.minX > 234);
/*     */   }
/*     */   
/*     */   public boolean canScrollVertically() {
/* 175 */     return (this.maxY - this.minY > 113);
/*     */   }
/*     */   
/*     */   public void addAdvancement(AdvancementNode node) {
/* 179 */     Optional<DisplayInfo> display = node.advancement().display();
/* 180 */     if (display.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 184 */     AdvancementWidget widget = new AdvancementWidget(this, this.minecraft, node, display.get());
/* 185 */     addWidget(widget, node.holder());
/*     */   }
/*     */   
/*     */   private void addWidget(AdvancementWidget widget, AdvancementHolder advancement) {
/* 189 */     this.widgets.put(advancement, widget);
/* 190 */     int x0 = widget.getX();
/* 191 */     int x1 = x0 + 28;
/* 192 */     int y0 = widget.getY();
/* 193 */     int y1 = y0 + 27;
/* 194 */     this.minX = Math.min(this.minX, x0);
/* 195 */     this.maxX = Math.max(this.maxX, x1);
/* 196 */     this.minY = Math.min(this.minY, y0);
/* 197 */     this.maxY = Math.max(this.maxY, y1);
/*     */     
/* 199 */     for (AdvancementWidget other : this.widgets.values()) {
/* 200 */       other.attachToParent();
/*     */     }
/*     */   }
/*     */   
/*     */   public AdvancementWidget getWidget(AdvancementHolder advancement) {
/* 205 */     return this.widgets.get(advancement);
/*     */   }
/*     */   
/*     */   public AdvancementsScreen getScreen() {
/* 209 */     return this.screen;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/advancements/AdvancementTab.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */