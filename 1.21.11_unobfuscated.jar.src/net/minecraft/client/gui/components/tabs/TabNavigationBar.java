/*     */ package net.minecraft.client.gui.components.tabs;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.gui.ComponentPath;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Renderable;
/*     */ import net.minecraft.client.gui.components.TabButton;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
/*     */ import net.minecraft.client.gui.components.events.ContainerEventHandler;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class TabNavigationBar extends AbstractContainerEventHandler implements NarratableEntry, Renderable {
/*     */   private static final int NO_TAB = -1;
/*     */   private static final int MAX_WIDTH = 400;
/*     */   private static final int HEIGHT = 24;
/*     */   private static final int MARGIN = 14;
/*  37 */   private static final Component USAGE_NARRATION = (Component)Component.translatable("narration.tab_navigation.usage");
/*     */   
/*  39 */   private final LinearLayout layout = LinearLayout.horizontal();
/*     */   
/*     */   private int width;
/*     */   
/*     */   private final TabManager tabManager;
/*     */   private final ImmutableList<Tab> tabs;
/*     */   private final ImmutableList<TabButton> tabButtons;
/*     */   
/*     */   private TabNavigationBar(int width, TabManager tabManager, Iterable<Tab> tabs) {
/*  48 */     this.width = width;
/*  49 */     this.tabManager = tabManager;
/*  50 */     this.tabs = ImmutableList.copyOf(tabs);
/*     */     
/*  52 */     this.layout.defaultCellSetting().alignHorizontallyCenter();
/*     */     
/*  54 */     ImmutableList.Builder<TabButton> tabButtonsBuilder = ImmutableList.builder();
/*  55 */     for (Tab tab : tabs) {
/*  56 */       tabButtonsBuilder.add(this.layout.addChild((LayoutElement)new TabButton(tabManager, tab, 0, 24)));
/*     */     }
/*  58 */     this.tabButtons = tabButtonsBuilder.build();
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final int width;
/*     */     private final TabManager tabManager;
/*  64 */     private final List<Tab> tabs = new ArrayList<>();
/*     */     
/*     */     private Builder(TabManager tabManager, int width) {
/*  67 */       this.tabManager = tabManager;
/*  68 */       this.width = width;
/*     */     }
/*     */     
/*     */     public Builder addTabs(Tab... tabs) {
/*  72 */       Collections.addAll(this.tabs, tabs);
/*  73 */       return this;
/*     */     }
/*     */     
/*     */     public TabNavigationBar build() {
/*  77 */       return new TabNavigationBar(this.width, this.tabManager, this.tabs);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder builder(TabManager tabManager, int width) {
/*  82 */     return new Builder(tabManager, width);
/*     */   }
/*     */   
/*     */   public void setWidth(int width) {
/*  86 */     this.width = width;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMouseOver(double mouseX, double mouseY) {
/*  91 */     return (mouseX >= this.layout.getX() && mouseY >= this.layout.getY() && mouseX < (this.layout.getX() + this.layout.getWidth()) && mouseY < (this.layout.getY() + this.layout.getHeight()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFocused(boolean focused) {
/*  96 */     super.setFocused(focused);
/*  97 */     if (getFocused() != null) {
/*  98 */       setFocused(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFocused(GuiEventListener focused) {
/* 104 */     super.setFocused(focused);
/* 105 */     if (focused instanceof TabButton) { TabButton button = (TabButton)focused; if (button.isActive()) {
/* 106 */         this.tabManager.setCurrentTab(button.tab(), true);
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   public ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
/* 112 */     if (!isFocused()) {
/* 113 */       TabButton button = currentTabButton();
/* 114 */       if (button != null) {
/* 115 */         return ComponentPath.path((ContainerEventHandler)this, ComponentPath.leaf((GuiEventListener)button));
/*     */       }
/*     */     } 
/* 118 */     if (navigationEvent instanceof FocusNavigationEvent.TabNavigation) {
/* 119 */       return null;
/*     */     }
/* 121 */     return super.nextFocusPath(navigationEvent);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 126 */     return (List)this.tabButtons;
/*     */   }
/*     */   
/*     */   public List<Tab> getTabs() {
/* 130 */     return (List<Tab>)this.tabs;
/*     */   }
/*     */ 
/*     */   
/*     */   public NarratableEntry.NarrationPriority narrationPriority() {
/* 135 */     return this.tabButtons.stream()
/* 136 */       .map(AbstractWidget::narrationPriority)
/* 137 */       .max(Comparator.naturalOrder())
/* 138 */       .orElse(NarratableEntry.NarrationPriority.NONE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateNarration(NarrationElementOutput output) {
/* 143 */     Optional<TabButton> selected = this.tabButtons.stream().filter(AbstractWidget::isHovered).findFirst()
/* 144 */       .or(() -> Optional.ofNullable(currentTabButton()));
/* 145 */     selected.ifPresent(button -> {
/*     */           narrateListElementPosition(output.nest(), output);
/*     */           output.updateNarration(output);
/*     */         });
/* 149 */     if (isFocused()) {
/* 150 */       output.add(NarratedElementType.USAGE, USAGE_NARRATION);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void narrateListElementPosition(NarrationElementOutput output, TabButton widget) {
/* 155 */     if (this.tabs.size() > 1) {
/* 156 */       int index = this.tabButtons.indexOf(widget);
/* 157 */       if (index != -1) {
/* 158 */         output.add(NarratedElementType.POSITION, (Component)Component.translatable("narrator.position.tab", new Object[] { index + 1, this.tabs.size() }));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 165 */     graphics.blit(RenderPipelines.GUI_TEXTURED, Screen.HEADER_SEPARATOR, 0, this.layout.getY() + this.layout.getHeight() - 2, 0.0F, 0.0F, ((TabButton)this.tabButtons.get(0)).getX(), 2, 32, 2);
/* 166 */     int afterLastTab = ((TabButton)this.tabButtons.get(this.tabButtons.size() - 1)).getRight();
/* 167 */     graphics.blit(RenderPipelines.GUI_TEXTURED, Screen.HEADER_SEPARATOR, afterLastTab, this.layout.getY() + this.layout.getHeight() - 2, 0.0F, 0.0F, this.width, 2, 32, 2);
/* 168 */     for (UnmodifiableIterator<TabButton> unmodifiableIterator = this.tabButtons.iterator(); unmodifiableIterator.hasNext(); ) { TabButton value = unmodifiableIterator.next();
/* 169 */       value.render(graphics, mouseX, mouseY, a); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public ScreenRectangle getRectangle() {
/* 175 */     return this.layout.getRectangle();
/*     */   }
/*     */ 
/*     */   
/*     */   public void arrangeElements() {
/* 180 */     int tabsWidth = Math.min(400, this.width) - 28;
/* 181 */     int tabWidth = Mth.roundToward(tabsWidth / this.tabs.size(), 2);
/* 182 */     for (UnmodifiableIterator<TabButton> unmodifiableIterator = this.tabButtons.iterator(); unmodifiableIterator.hasNext(); ) { TabButton button = unmodifiableIterator.next();
/* 183 */       button.setWidth(tabWidth); }
/*     */     
/* 185 */     this.layout.arrangeElements();
/* 186 */     this.layout.setX(Mth.roundToward((this.width - tabsWidth) / 2, 2));
/* 187 */     this.layout.setY(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void selectTab(int index, boolean playSound) {
/* 192 */     if (isFocused()) {
/* 193 */       setFocused((GuiEventListener)this.tabButtons.get(index));
/* 194 */     } else if (((TabButton)this.tabButtons.get(index)).isActive()) {
/* 195 */       this.tabManager.setCurrentTab((Tab)this.tabs.get(index), playSound);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setTabActiveState(int index, boolean active) {
/* 200 */     if (index >= 0 && index < this.tabButtons.size()) {
/* 201 */       ((TabButton)this.tabButtons.get(index)).active = active;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setTabTooltip(int index, Tooltip hint) {
/* 206 */     if (index >= 0 && index < this.tabButtons.size()) {
/* 207 */       ((TabButton)this.tabButtons.get(index)).setTooltip(hint);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 213 */     if (event.hasControlDownWithQuirk()) {
/* 214 */       int tabIndex = getNextTabIndex(event);
/* 215 */       if (tabIndex != -1) {
/* 216 */         selectTab(Mth.clamp(tabIndex, 0, this.tabs.size() - 1), true);
/* 217 */         return true;
/*     */       } 
/*     */     } 
/* 220 */     return false;
/*     */   }
/*     */   
/*     */   private int getNextTabIndex(KeyEvent event) {
/* 224 */     return getNextTabIndex(currentTabIndex(), event);
/*     */   }
/*     */   
/*     */   private int getNextTabIndex(int currentTab, KeyEvent event) {
/* 228 */     int digit = event.getDigit();
/* 229 */     if (digit != -1)
/*     */     {
/* 231 */       return Math.floorMod(digit - 1, 10); } 
/* 232 */     if (event.isCycleFocus() && 
/* 233 */       currentTab != -1) {
/* 234 */       int nextTabIndex = event.hasShiftDown() ? (currentTab - 1) : (currentTab + 1);
/* 235 */       int index = Math.floorMod(nextTabIndex, this.tabs.size());
/* 236 */       if (((TabButton)this.tabButtons.get(index)).active) {
/* 237 */         return index;
/*     */       }
/* 239 */       return getNextTabIndex(index, event);
/*     */     } 
/*     */ 
/*     */     
/* 243 */     return -1;
/*     */   }
/*     */   
/*     */   private int currentTabIndex() {
/* 247 */     Tab currentTab = this.tabManager.getCurrentTab();
/* 248 */     int index = this.tabs.indexOf(currentTab);
/* 249 */     return (index != -1) ? index : -1;
/*     */   }
/*     */   
/*     */   private TabButton currentTabButton() {
/* 253 */     int index = currentTabIndex();
/* 254 */     return (index != -1) ? (TabButton)this.tabButtons.get(index) : null;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/tabs/TabNavigationBar.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */