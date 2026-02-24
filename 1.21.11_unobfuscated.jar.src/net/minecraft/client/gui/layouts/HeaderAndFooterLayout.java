/*     */ package net.minecraft.client.gui.layouts;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ 
/*     */ public class HeaderAndFooterLayout
/*     */   implements Layout
/*     */ {
/*     */   public static final int MAGIC_PADDING = 13;
/*     */   public static final int DEFAULT_HEADER_AND_FOOTER_HEIGHT = 33;
/*     */   private static final int CONTENT_MARGIN_TOP = 30;
/*  16 */   private final FrameLayout headerFrame = new FrameLayout();
/*  17 */   private final FrameLayout footerFrame = new FrameLayout();
/*  18 */   private final FrameLayout contentsFrame = new FrameLayout();
/*     */   
/*     */   private final Screen screen;
/*     */   private int headerHeight;
/*     */   private int footerHeight;
/*     */   
/*     */   public HeaderAndFooterLayout(Screen screen) {
/*  25 */     this(screen, 33);
/*     */   }
/*     */   
/*     */   public HeaderAndFooterLayout(Screen screen, int headerAndFooterHeight) {
/*  29 */     this(screen, headerAndFooterHeight, headerAndFooterHeight);
/*     */   }
/*     */   
/*     */   public HeaderAndFooterLayout(Screen screen, int headerHeight, int footerHeight) {
/*  33 */     this.screen = screen;
/*  34 */     this.headerHeight = headerHeight;
/*  35 */     this.footerHeight = footerHeight;
/*  36 */     this.headerFrame.defaultChildLayoutSetting().align(0.5F, 0.5F);
/*  37 */     this.footerFrame.defaultChildLayoutSetting().align(0.5F, 0.5F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setX(int x) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setY(int y) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public int getX() {
/*  52 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getY() {
/*  57 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/*  62 */     return this.screen.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/*  67 */     return this.screen.height;
/*     */   }
/*     */   
/*     */   public int getFooterHeight() {
/*  71 */     return this.footerHeight;
/*     */   }
/*     */   
/*     */   public void setFooterHeight(int footerHeight) {
/*  75 */     this.footerHeight = footerHeight;
/*     */   }
/*     */   
/*     */   public void setHeaderHeight(int headerHeight) {
/*  79 */     this.headerHeight = headerHeight;
/*     */   }
/*     */   
/*     */   public int getHeaderHeight() {
/*  83 */     return this.headerHeight;
/*     */   }
/*     */   
/*     */   public int getContentHeight() {
/*  87 */     return this.screen.height - getHeaderHeight() - getFooterHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitChildren(Consumer<LayoutElement> layoutElementVisitor) {
/*  92 */     this.headerFrame.visitChildren(layoutElementVisitor);
/*  93 */     this.contentsFrame.visitChildren(layoutElementVisitor);
/*  94 */     this.footerFrame.visitChildren(layoutElementVisitor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void arrangeElements() {
/*  99 */     int headerHeight = getHeaderHeight();
/* 100 */     int footerHeight = getFooterHeight();
/*     */     
/* 102 */     this.headerFrame.setMinWidth(this.screen.width);
/* 103 */     this.headerFrame.setMinHeight(headerHeight);
/* 104 */     this.headerFrame.setPosition(0, 0);
/* 105 */     this.headerFrame.arrangeElements();
/*     */     
/* 107 */     this.footerFrame.setMinWidth(this.screen.width);
/* 108 */     this.footerFrame.setMinHeight(footerHeight);
/* 109 */     this.footerFrame.arrangeElements();
/* 110 */     this.footerFrame.setY(this.screen.height - footerHeight);
/*     */     
/* 112 */     this.contentsFrame.setMinWidth(this.screen.width);
/* 113 */     this.contentsFrame.arrangeElements();
/*     */     
/* 115 */     int preferredContentY = headerHeight + 30;
/* 116 */     int maxContentY = this.screen.height - footerHeight - this.contentsFrame.getHeight();
/* 117 */     this.contentsFrame.setPosition(0, Math.min(preferredContentY, maxContentY));
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addToHeader(T child) {
/* 121 */     return this.headerFrame.addChild(child);
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addToHeader(T child, Consumer<LayoutSettings> layoutSettingsAdjustments) {
/* 125 */     return this.headerFrame.addChild(child, layoutSettingsAdjustments);
/*     */   }
/*     */   
/*     */   public void addTitleHeader(Component component, Font font) {
/* 129 */     this.headerFrame.addChild(new StringWidget(component, font));
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addToFooter(T child) {
/* 133 */     return this.footerFrame.addChild(child);
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addToFooter(T child, Consumer<LayoutSettings> layoutSettingsAdjustments) {
/* 137 */     return this.footerFrame.addChild(child, layoutSettingsAdjustments);
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addToContents(T child) {
/* 141 */     return this.contentsFrame.addChild(child);
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addToContents(T child, Consumer<LayoutSettings> layoutSettingsAdjustments) {
/* 145 */     return this.contentsFrame.addChild(child, layoutSettingsAdjustments);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/layouts/HeaderAndFooterLayout.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */