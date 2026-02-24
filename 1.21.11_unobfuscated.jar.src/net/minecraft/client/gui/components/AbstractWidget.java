/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import java.time.Duration;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.ComponentPath;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.input.MouseButtonInfo;
/*     */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*     */ import net.minecraft.client.resources.sounds.SoundInstance;
/*     */ import net.minecraft.client.sounds.SoundManager;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ 
/*     */ public abstract class AbstractWidget
/*     */   implements LayoutElement, Renderable, GuiEventListener, NarratableEntry
/*     */ {
/*     */   protected int width;
/*     */   protected int height;
/*     */   private int x;
/*     */   private int y;
/*     */   protected Component message;
/*     */   protected boolean isHovered;
/*     */   public boolean active = true;
/*     */   public boolean visible = true;
/*  40 */   protected float alpha = 1.0F;
/*     */   
/*     */   private int tabOrderGroup;
/*     */   
/*     */   private boolean focused;
/*  45 */   private final WidgetTooltipHolder tooltip = new WidgetTooltipHolder();
/*     */   
/*     */   public AbstractWidget(int x, int y, int width, int height, Component message) {
/*  48 */     this.x = x;
/*  49 */     this.y = y;
/*  50 */     this.width = width;
/*  51 */     this.height = height;
/*  52 */     this.message = message;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/*  57 */     return this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  62 */     if (!this.visible) {
/*     */       return;
/*     */     }
/*     */     
/*  66 */     this.isHovered = (graphics.containsPointInScissor(mouseX, mouseY) && areCoordinatesInRectangle(mouseX, mouseY));
/*  67 */     renderWidget(graphics, mouseX, mouseY, a);
/*     */     
/*  69 */     this.tooltip.refreshTooltipForNextRenderPass(graphics, mouseX, mouseY, isHovered(), isFocused(), getRectangle());
/*     */   }
/*     */   
/*     */   protected void handleCursor(GuiGraphics graphics) {
/*  73 */     if (isHovered()) {
/*  74 */       graphics.requestCursor(isActive() ? CursorTypes.POINTING_HAND : CursorTypes.NOT_ALLOWED);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setTooltip(Tooltip tooltip) {
/*  79 */     this.tooltip.set(tooltip);
/*     */   }
/*     */   
/*     */   public void setTooltipDelay(Duration delay) {
/*  83 */     this.tooltip.setDelay(delay);
/*     */   }
/*     */   
/*     */   protected MutableComponent createNarrationMessage() {
/*  87 */     return wrapDefaultNarrationMessage(getMessage());
/*     */   }
/*     */   
/*     */   public static MutableComponent wrapDefaultNarrationMessage(Component message) {
/*  91 */     return Component.translatable("gui.narrate.button", new Object[] { message });
/*     */   }
/*     */   
/*     */   protected abstract void renderWidget(GuiGraphics paramGuiGraphics, int paramInt1, int paramInt2, float paramFloat);
/*     */   
/*     */   protected void renderScrollingStringOverContents(ActiveTextCollector output, Component message, int margin) {
/*  97 */     int left = getX() + margin;
/*  98 */     int right = getX() + getWidth() - margin;
/*  99 */     int top = getY();
/* 100 */     int bottom = getY() + getHeight();
/* 101 */     output.acceptScrollingWithDefaultCenter(message, left, right, top, bottom);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClick(MouseButtonEvent event, boolean doubleClick) {}
/*     */ 
/*     */   
/*     */   public void onRelease(MouseButtonEvent event) {}
/*     */ 
/*     */   
/*     */   protected void onDrag(MouseButtonEvent event, double dx, double dy) {}
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 115 */     if (!isActive()) {
/* 116 */       return false;
/*     */     }
/* 118 */     if (isValidClickButton(event.buttonInfo())) {
/* 119 */       boolean isMouseOver = isMouseOver(event.x(), event.y());
/* 120 */       if (isMouseOver) {
/* 121 */         playDownSound(Minecraft.getInstance().getSoundManager());
/* 122 */         onClick(event, doubleClick);
/* 123 */         return true;
/*     */       } 
/*     */     } 
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseReleased(MouseButtonEvent event) {
/* 131 */     if (isValidClickButton(event.buttonInfo())) {
/* 132 */       onRelease(event);
/* 133 */       return true;
/*     */     } 
/* 135 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean isValidClickButton(MouseButtonInfo buttonInfo) {
/* 139 */     return (buttonInfo.button() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/* 144 */     if (isValidClickButton(event.buttonInfo())) {
/* 145 */       onDrag(event, dx, dy);
/* 146 */       return true;
/*     */     } 
/* 148 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
/* 153 */     if (!isActive()) {
/* 154 */       return null;
/*     */     }
/*     */     
/* 157 */     if (!isFocused()) {
/* 158 */       return ComponentPath.leaf(this);
/*     */     }
/* 160 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMouseOver(double mouseX, double mouseY) {
/* 165 */     return (isActive() && areCoordinatesInRectangle(mouseX, mouseY));
/*     */   }
/*     */   
/*     */   public void playDownSound(SoundManager soundManager) {
/* 169 */     playButtonClickSound(soundManager);
/*     */   }
/*     */   
/*     */   public static void playButtonClickSound(SoundManager soundManager) {
/* 173 */     soundManager.play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 178 */     return this.width;
/*     */   }
/*     */   
/*     */   public void setWidth(int width) {
/* 182 */     this.width = width;
/*     */   }
/*     */   
/*     */   public void setHeight(int height) {
/* 186 */     this.height = height;
/*     */   }
/*     */   
/*     */   public void setAlpha(float alpha) {
/* 190 */     this.alpha = alpha;
/*     */   }
/*     */   
/*     */   public float getAlpha() {
/* 194 */     return this.alpha;
/*     */   }
/*     */   
/*     */   public void setMessage(Component message) {
/* 198 */     this.message = message;
/*     */   }
/*     */   
/*     */   public Component getMessage() {
/* 202 */     return this.message;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFocused() {
/* 207 */     return this.focused;
/*     */   }
/*     */   
/*     */   public boolean isHovered() {
/* 211 */     return this.isHovered;
/*     */   }
/*     */   
/*     */   public boolean isHoveredOrFocused() {
/* 215 */     return (isHovered() || isFocused());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 220 */     return (this.visible && this.active);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFocused(boolean focused) {
/* 228 */     this.focused = focused;
/*     */   }
/*     */ 
/*     */   
/*     */   public NarratableEntry.NarrationPriority narrationPriority() {
/* 233 */     if (isFocused()) {
/* 234 */       return NarratableEntry.NarrationPriority.FOCUSED;
/*     */     }
/* 236 */     if (this.isHovered) {
/* 237 */       return NarratableEntry.NarrationPriority.HOVERED;
/*     */     }
/* 239 */     return NarratableEntry.NarrationPriority.NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void updateNarration(NarrationElementOutput output) {
/* 244 */     updateWidgetNarration(output);
/* 245 */     this.tooltip.updateNarration(output);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void updateWidgetNarration(NarrationElementOutput paramNarrationElementOutput);
/*     */   
/*     */   protected void defaultButtonNarrationText(NarrationElementOutput output) {
/* 252 */     output.add(NarratedElementType.TITLE, (Component)createNarrationMessage());
/* 253 */     if (this.active) {
/* 254 */       if (isFocused()) {
/* 255 */         output.add(NarratedElementType.USAGE, (Component)Component.translatable("narration.button.usage.focused"));
/*     */       } else {
/* 257 */         output.add(NarratedElementType.USAGE, (Component)Component.translatable("narration.button.usage.hovered"));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getX() {
/* 264 */     return this.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setX(int x) {
/* 269 */     this.x = x;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getY() {
/* 274 */     return this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setY(int y) {
/* 279 */     this.y = y;
/*     */   }
/*     */   
/*     */   public int getRight() {
/* 283 */     return getX() + getWidth();
/*     */   }
/*     */   
/*     */   public int getBottom() {
/* 287 */     return getY() + getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {
/* 292 */     widgetVisitor.accept(this);
/*     */   }
/*     */   
/*     */   public void setSize(int width, int height) {
/* 296 */     this.width = width;
/* 297 */     this.height = height;
/*     */   }
/*     */ 
/*     */   
/*     */   public ScreenRectangle getRectangle() {
/* 302 */     return super.getRectangle();
/*     */   }
/*     */   
/*     */   private boolean areCoordinatesInRectangle(double x, double y) {
/* 306 */     return (x >= getX() && y >= getY() && x < getRight() && y < getBottom());
/*     */   }
/*     */   
/*     */   public void setRectangle(int width, int height, int x, int y) {
/* 310 */     setSize(width, height);
/* 311 */     setPosition(x, y);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTabOrderGroup() {
/* 316 */     return this.tabOrderGroup;
/*     */   }
/*     */   
/*     */   public void setTabOrderGroup(int tabOrderGroup) {
/* 320 */     this.tabOrderGroup = tabOrderGroup;
/*     */   }
/*     */   
/*     */   public static abstract class WithInactiveMessage extends AbstractWidget {
/*     */     private Component inactiveMessage;
/*     */     
/*     */     public static Component defaultInactiveMessage(Component activeMessage) {
/* 327 */       return ComponentUtils.mergeStyles(activeMessage, Style.EMPTY.withColor(-6250336));
/*     */     }
/*     */     
/*     */     public WithInactiveMessage(int x, int y, int width, int height, Component message) {
/* 331 */       super(x, y, width, height, message);
/* 332 */       this.inactiveMessage = defaultInactiveMessage(message);
/*     */     }
/*     */ 
/*     */     
/*     */     public Component getMessage() {
/* 337 */       return this.active ? super.getMessage() : this.inactiveMessage;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setMessage(Component message) {
/* 342 */       super.setMessage(message);
/* 343 */       this.inactiveMessage = defaultInactiveMessage(message);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/AbstractWidget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */