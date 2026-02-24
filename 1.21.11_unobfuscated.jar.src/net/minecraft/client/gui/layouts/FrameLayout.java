/*     */ package net.minecraft.client.gui.layouts;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FrameLayout
/*     */   extends AbstractLayout
/*     */ {
/*  19 */   private final List<ChildContainer> children = new ArrayList<>();
/*     */   
/*     */   private int minWidth;
/*     */   private int minHeight;
/*  23 */   private final LayoutSettings defaultChildLayoutSettings = LayoutSettings.defaults().align(0.5F, 0.5F);
/*     */   
/*     */   public FrameLayout() {
/*  26 */     this(0, 0, 0, 0);
/*     */   }
/*     */   
/*     */   public FrameLayout(int minWidth, int minHeight) {
/*  30 */     this(0, 0, minWidth, minHeight);
/*     */   }
/*     */   
/*     */   public FrameLayout(int x, int y, int minWidth, int minHeight) {
/*  34 */     super(x, y, minWidth, minHeight);
/*  35 */     setMinDimensions(minWidth, minHeight);
/*     */   }
/*     */   
/*     */   public FrameLayout setMinDimensions(int minWidth, int minHeight) {
/*  39 */     return setMinWidth(minWidth).setMinHeight(minHeight);
/*     */   }
/*     */   
/*     */   public FrameLayout setMinHeight(int minHeight) {
/*  43 */     this.minHeight = minHeight;
/*  44 */     return this;
/*     */   }
/*     */   
/*     */   public FrameLayout setMinWidth(int minWidth) {
/*  48 */     this.minWidth = minWidth;
/*  49 */     return this;
/*     */   }
/*     */   
/*     */   public LayoutSettings newChildLayoutSettings() {
/*  53 */     return this.defaultChildLayoutSettings.copy();
/*     */   }
/*     */   
/*     */   public LayoutSettings defaultChildLayoutSetting() {
/*  57 */     return this.defaultChildLayoutSettings;
/*     */   }
/*     */ 
/*     */   
/*     */   public void arrangeElements() {
/*  62 */     super.arrangeElements();
/*     */     
/*  64 */     int resultWidth = this.minWidth;
/*  65 */     int resultHeight = this.minHeight;
/*     */     
/*  67 */     for (ChildContainer child : this.children) {
/*  68 */       resultWidth = Math.max(resultWidth, child.getWidth());
/*  69 */       resultHeight = Math.max(resultHeight, child.getHeight());
/*     */     } 
/*     */     
/*  72 */     for (ChildContainer child : this.children) {
/*  73 */       child.setX(getX(), resultWidth);
/*  74 */       child.setY(getY(), resultHeight);
/*     */     } 
/*     */     
/*  77 */     this.width = resultWidth;
/*  78 */     this.height = resultHeight;
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child) {
/*  82 */     return addChild(child, newChildLayoutSettings());
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, LayoutSettings childLayoutSettings) {
/*  86 */     this.children.add(new ChildContainer((LayoutElement)child, childLayoutSettings));
/*  87 */     return child;
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, Consumer<LayoutSettings> layoutSettingsAdjustments) {
/*  91 */     return addChild(child, (LayoutSettings)Util.make(newChildLayoutSettings(), layoutSettingsAdjustments));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitChildren(Consumer<LayoutElement> layoutElementVisitor) {
/*  96 */     this.children.forEach(wrapper -> layoutElementVisitor.accept(wrapper.child));
/*     */   }
/*     */   
/*     */   public static void centerInRectangle(LayoutElement widget, int x, int y, int width, int height) {
/* 100 */     alignInRectangle(widget, x, y, width, height, 0.5F, 0.5F);
/*     */   }
/*     */   
/*     */   public static void centerInRectangle(LayoutElement widget, ScreenRectangle rectangle) {
/* 104 */     centerInRectangle(widget, rectangle.position().x(), rectangle.position().y(), rectangle.width(), rectangle.height());
/*     */   }
/*     */   
/*     */   public static void alignInRectangle(LayoutElement widget, ScreenRectangle rectangle, float alignX, float alignY) {
/* 108 */     alignInRectangle(widget, rectangle.left(), rectangle.top(), rectangle.width(), rectangle.height(), alignX, alignY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void alignInRectangle(LayoutElement widget, int x, int y, int width, int height, float alignX, float alignY) {
/* 119 */     Objects.requireNonNull(widget); alignInDimension(x, width, widget.getWidth(), widget::setX, alignX);
/* 120 */     Objects.requireNonNull(widget); alignInDimension(y, height, widget.getHeight(), widget::setY, alignY);
/*     */   }
/*     */   
/*     */   public static void alignInDimension(int pos, int length, int widgetLength, Consumer<Integer> setWidgetPos, float align) {
/* 124 */     int offset = (int)Mth.lerp(align, 0.0F, (length - widgetLength));
/* 125 */     setWidgetPos.accept(pos + offset);
/*     */   }
/*     */   
/*     */   private static class ChildContainer extends AbstractLayout.AbstractChildWrapper {
/*     */     protected ChildContainer(LayoutElement child, LayoutSettings layoutSettings) {
/* 130 */       super(child, layoutSettings);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/layouts/FrameLayout.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */