/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.Layout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.navigation.ScreenDirection;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ 
/*     */ public class ScrollableLayout
/*     */   implements Layout
/*     */ {
/*     */   private static final int SCROLLBAR_SPACING = 4;
/*     */   private static final int SCROLLBAR_RESERVE = 10;
/*     */   private final Layout content;
/*     */   private final Container container;
/*     */   private int minWidth;
/*     */   private int maxHeight;
/*     */   
/*     */   public ScrollableLayout(Minecraft minecraft, Layout content, int maxHeight) {
/*  30 */     this.content = content;
/*  31 */     this.container = new Container(minecraft, 0, maxHeight);
/*     */   }
/*     */   
/*     */   public void setMinWidth(int minWidth) {
/*  35 */     this.minWidth = minWidth;
/*  36 */     this.container.setWidth(Math.max(this.content.getWidth(), minWidth));
/*     */   }
/*     */   
/*     */   public void setMaxHeight(int maxHeight) {
/*  40 */     this.maxHeight = maxHeight;
/*  41 */     this.container.setHeight(Math.min(this.content.getHeight(), maxHeight));
/*  42 */     this.container.refreshScrollAmount();
/*     */   }
/*     */ 
/*     */   
/*     */   public void arrangeElements() {
/*  47 */     this.content.arrangeElements();
/*  48 */     int contentWidth = this.content.getWidth();
/*     */ 
/*     */     
/*  51 */     this.container.setWidth(Math.max(contentWidth + 20, this.minWidth));
/*  52 */     this.container.setHeight(Math.min(this.content.getHeight(), this.maxHeight));
/*  53 */     this.container.refreshScrollAmount();
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitChildren(Consumer<LayoutElement> layoutElementVisitor) {
/*  58 */     layoutElementVisitor.accept(this.container);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setX(int x) {
/*  63 */     this.container.setX(x);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setY(int y) {
/*  68 */     this.container.setY(y);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getX() {
/*  73 */     return this.container.getX();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getY() {
/*  78 */     return this.container.getY();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/*  83 */     return this.container.getWidth();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/*  88 */     return this.container.getHeight();
/*     */   }
/*     */   
/*     */   private class Container extends AbstractContainerWidget {
/*     */     private final Minecraft minecraft;
/*  93 */     private final List<AbstractWidget> children = new ArrayList<>();
/*     */     
/*     */     public Container(Minecraft minecraft, int width, int height) {
/*  96 */       super(0, 0, width, height, CommonComponents.EMPTY);
/*  97 */       this.minecraft = minecraft;
/*  98 */       Objects.requireNonNull(this.children); ScrollableLayout.this.content.visitWidgets(this.children::add);
/*     */     }
/*     */ 
/*     */     
/*     */     protected int contentHeight() {
/* 103 */       return ScrollableLayout.this.content.getHeight();
/*     */     }
/*     */ 
/*     */     
/*     */     protected double scrollRate() {
/* 108 */       return 10.0D;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 113 */       graphics.enableScissor(getX(), getY(), getX() + this.width, getY() + this.height);
/*     */       
/* 115 */       for (AbstractWidget child : this.children) {
/* 116 */         child.render(graphics, mouseX, mouseY, a);
/*     */       }
/*     */       
/* 119 */       graphics.disableScissor();
/*     */       
/* 121 */       renderScrollbar(graphics, mouseX, mouseY);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void updateWidgetNarration(NarrationElementOutput output) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public ScreenRectangle getBorderForArrowNavigation(ScreenDirection opposite) {
/* 131 */       return new ScreenRectangle(getX(), getY(), this.width, contentHeight());
/*     */     }
/*     */ 
/*     */     
/*     */     public void setFocused(GuiEventListener focused) {
/* 136 */       super.setFocused(focused);
/* 137 */       if (focused == null || !this.minecraft.getLastInputType().isKeyboard()) {
/*     */         return;
/*     */       }
/*     */       
/* 141 */       ScreenRectangle area = getRectangle();
/* 142 */       ScreenRectangle focusedRect = focused.getRectangle();
/* 143 */       int topDelta = focusedRect.top() - area.top();
/* 144 */       int bottomDelta = focusedRect.bottom() - area.bottom();
/*     */       
/* 146 */       if (topDelta < 0) {
/* 147 */         setScrollAmount(scrollAmount() + topDelta - 14.0D);
/* 148 */       } else if (bottomDelta > 0) {
/* 149 */         setScrollAmount(scrollAmount() + bottomDelta + 14.0D);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void setX(int x) {
/* 155 */       super.setX(x);
/* 156 */       ScrollableLayout.this.content.setX(x + 10);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setY(int y) {
/* 161 */       super.setY(y);
/* 162 */       ScrollableLayout.this.content.setY(y - (int)scrollAmount());
/*     */     }
/*     */ 
/*     */     
/*     */     public void setScrollAmount(double scrollAmount) {
/* 167 */       super.setScrollAmount(scrollAmount);
/* 168 */       ScrollableLayout.this.content.setY(getRectangle().top() - (int)scrollAmount());
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 173 */       return (List)this.children;
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<? extends NarratableEntry> getNarratables() {
/* 178 */       return (Collection)this.children;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/ScrollableLayout.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */