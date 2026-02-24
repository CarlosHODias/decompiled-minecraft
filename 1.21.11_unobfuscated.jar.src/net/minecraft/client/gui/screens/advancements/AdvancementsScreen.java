/*     */ package net.minecraft.client.gui.screens.advancements;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementNode;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.multiplayer.ClientAdvancements;
/*     */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class AdvancementsScreen extends Screen implements ClientAdvancements.Listener {
/*  28 */   private static final Identifier WINDOW_LOCATION = Identifier.withDefaultNamespace("textures/gui/advancements/window.png");
/*     */   
/*     */   public static final int WINDOW_WIDTH = 252;
/*     */   
/*     */   public static final int WINDOW_HEIGHT = 140;
/*     */   
/*     */   private static final int WINDOW_INSIDE_X = 9;
/*     */   
/*     */   private static final int WINDOW_INSIDE_Y = 18;
/*     */   
/*     */   public static final int WINDOW_INSIDE_WIDTH = 234;
/*     */   
/*     */   public static final int WINDOW_INSIDE_HEIGHT = 113;
/*     */   
/*     */   private static final int WINDOW_TITLE_X = 8;
/*     */   private static final int WINDOW_TITLE_Y = 6;
/*     */   private static final int BACKGROUND_TEXTURE_WIDTH = 256;
/*     */   private static final int BACKGROUND_TEXTURE_HEIGHT = 256;
/*     */   public static final int BACKGROUND_TILE_WIDTH = 16;
/*     */   public static final int BACKGROUND_TILE_HEIGHT = 16;
/*     */   public static final int BACKGROUND_TILE_COUNT_X = 14;
/*     */   public static final int BACKGROUND_TILE_COUNT_Y = 7;
/*     */   private static final double SCROLL_SPEED = 16.0D;
/*  51 */   private static final Component VERY_SAD_LABEL = (Component)Component.translatable("advancements.sad_label");
/*  52 */   private static final Component NO_ADVANCEMENTS_LABEL = (Component)Component.translatable("advancements.empty");
/*  53 */   private static final Component TITLE = (Component)Component.translatable("gui.advancements");
/*     */   
/*  55 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*     */   
/*     */   private final Screen lastScreen;
/*     */   
/*     */   private final ClientAdvancements advancements;
/*  60 */   private final Map<AdvancementHolder, AdvancementTab> tabs = Maps.newLinkedHashMap();
/*     */   private AdvancementTab selectedTab;
/*     */   private boolean isScrolling;
/*     */   
/*     */   public AdvancementsScreen(ClientAdvancements advancements) {
/*  65 */     this(advancements, null);
/*     */   }
/*     */   
/*     */   public AdvancementsScreen(ClientAdvancements advancements, Screen lastScreen) {
/*  69 */     super(TITLE);
/*  70 */     this.advancements = advancements;
/*  71 */     this.lastScreen = lastScreen;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  76 */     this.layout.addTitleHeader(TITLE, this.font);
/*  77 */     this.tabs.clear();
/*  78 */     this.selectedTab = null;
/*  79 */     this.advancements.setListener(this);
/*  80 */     if (this.selectedTab == null && !this.tabs.isEmpty()) {
/*  81 */       AdvancementTab firstTab = this.tabs.values().iterator().next();
/*  82 */       this.advancements.setSelectedTab(firstTab.getRootNode().holder(), true);
/*     */     } else {
/*     */       
/*  85 */       this.advancements.setSelectedTab((this.selectedTab == null) ? null : this.selectedTab.getRootNode().holder(), true);
/*     */     } 
/*  87 */     this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onClose()).width(200).build());
/*     */     
/*  89 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*  90 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/*  95 */     this.layout.arrangeElements();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 100 */     this.minecraft.setScreen(this.lastScreen);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed() {
/* 105 */     this.advancements.setListener(null);
/* 106 */     ClientPacketListener connection = this.minecraft.getConnection();
/* 107 */     if (connection != null) {
/* 108 */       connection.send((Packet)ServerboundSeenAdvancementsPacket.closedScreen());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 114 */     if (event.button() == 0) {
/* 115 */       int xo = (this.width - 252) / 2;
/* 116 */       int yo = (this.height - 140) / 2;
/*     */       
/* 118 */       for (AdvancementTab tab : this.tabs.values()) {
/* 119 */         if (tab.isMouseOver(xo, yo, event.x(), event.y())) {
/* 120 */           this.advancements.setSelectedTab(tab.getRootNode().holder(), true);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 125 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 130 */     if (this.minecraft.options.keyAdvancements.matches(event)) {
/* 131 */       this.minecraft.setScreen(null);
/* 132 */       this.minecraft.mouseHandler.grabMouse();
/* 133 */       return true;
/*     */     } 
/* 135 */     return super.keyPressed(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 140 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/* 142 */     int xo = (this.width - 252) / 2;
/* 143 */     int yo = (this.height - 140) / 2;
/* 144 */     graphics.nextStratum();
/* 145 */     renderInside(graphics, xo, yo);
/* 146 */     graphics.nextStratum();
/* 147 */     renderWindow(graphics, xo, yo, mouseX, mouseY);
/* 148 */     if (this.isScrolling && this.selectedTab != null) {
/* 149 */       if (this.selectedTab.canScrollHorizontally() && this.selectedTab.canScrollVertically()) {
/* 150 */         graphics.requestCursor(CursorTypes.RESIZE_ALL);
/* 151 */       } else if (this.selectedTab.canScrollHorizontally()) {
/* 152 */         graphics.requestCursor(CursorTypes.RESIZE_EW);
/* 153 */       } else if (this.selectedTab.canScrollVertically()) {
/* 154 */         graphics.requestCursor(CursorTypes.RESIZE_NS);
/*     */       } 
/*     */     }
/* 157 */     renderTooltips(graphics, mouseX, mouseY, xo, yo);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/* 162 */     if (event.button() != 0) {
/* 163 */       this.isScrolling = false;
/* 164 */       return false;
/*     */     } 
/*     */     
/* 167 */     if (!this.isScrolling) {
/* 168 */       this.isScrolling = true;
/* 169 */     } else if (this.selectedTab != null) {
/* 170 */       this.selectedTab.scroll(dx, dy);
/*     */     } 
/* 172 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseReleased(MouseButtonEvent event) {
/* 177 */     this.isScrolling = false;
/* 178 */     return super.mouseReleased(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/* 183 */     if (this.selectedTab != null) {
/* 184 */       this.selectedTab.scroll(scrollX * 16.0D, scrollY * 16.0D);
/* 185 */       return true;
/*     */     } 
/* 187 */     return false;
/*     */   }
/*     */   
/*     */   private void renderInside(GuiGraphics graphics, int xo, int yo) {
/* 191 */     AdvancementTab tab = this.selectedTab;
/* 192 */     if (tab == null) {
/* 193 */       graphics.fill(xo + 9, yo + 18, xo + 9 + 234, yo + 18 + 113, -16777216);
/* 194 */       int midX = xo + 9 + 117;
/* 195 */       Objects.requireNonNull(this.font); graphics.drawCenteredString(this.font, NO_ADVANCEMENTS_LABEL, midX, yo + 18 + 56 - 9 / 2, -1);
/* 196 */       Objects.requireNonNull(this.font); graphics.drawCenteredString(this.font, VERY_SAD_LABEL, midX, yo + 18 + 113 - 9, -1);
/*     */       
/*     */       return;
/*     */     } 
/* 200 */     tab.drawContents(graphics, xo + 9, yo + 18);
/*     */   }
/*     */   
/*     */   public void renderWindow(GuiGraphics graphics, int xo, int yo, int mouseX, int mouseY) {
/* 204 */     graphics.blit(RenderPipelines.GUI_TEXTURED, WINDOW_LOCATION, xo, yo, 0.0F, 0.0F, 252, 140, 256, 256);
/* 205 */     if (this.tabs.size() > 1) {
/* 206 */       for (AdvancementTab tab : this.tabs.values()) {
/* 207 */         tab.drawTab(graphics, xo, yo, mouseX, mouseY, (tab == this.selectedTab));
/*     */       }
/*     */       
/* 210 */       for (AdvancementTab tab : this.tabs.values()) {
/* 211 */         tab.drawIcon(graphics, xo, yo);
/*     */       }
/*     */     } 
/* 214 */     graphics.drawString(this.font, (this.selectedTab != null) ? this.selectedTab.getTitle() : TITLE, xo + 8, yo + 6, -12566464, false);
/*     */   }
/*     */   
/*     */   private void renderTooltips(GuiGraphics graphics, int mouseX, int mouseY, int xo, int yo) {
/* 218 */     if (this.selectedTab != null) {
/* 219 */       graphics.pose().pushMatrix();
/* 220 */       graphics.pose().translate((xo + 9), (yo + 18));
/*     */       
/* 222 */       graphics.nextStratum();
/* 223 */       this.selectedTab.drawTooltips(graphics, mouseX - xo - 9, mouseY - yo - 18, xo, yo);
/*     */       
/* 225 */       graphics.pose().popMatrix();
/*     */     } 
/*     */     
/* 228 */     if (this.tabs.size() > 1) {
/* 229 */       for (AdvancementTab tab : this.tabs.values()) {
/* 230 */         if (tab.isMouseOver(xo, yo, mouseX, mouseY)) {
/* 231 */           graphics.setTooltipForNextFrame(this.font, tab.getTitle(), mouseX, mouseY);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAddAdvancementRoot(AdvancementNode root) {
/* 239 */     AdvancementTab tab = AdvancementTab.create(this.minecraft, this, this.tabs.size(), root);
/* 240 */     if (tab == null) {
/*     */       return;
/*     */     }
/* 243 */     this.tabs.put(root.holder(), tab);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onRemoveAdvancementRoot(AdvancementNode root) {}
/*     */ 
/*     */   
/*     */   public void onAddAdvancementTask(AdvancementNode task) {
/* 252 */     AdvancementTab tab = getTab(task);
/* 253 */     if (tab != null) {
/* 254 */       tab.addAdvancement(task);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onRemoveAdvancementTask(AdvancementNode task) {}
/*     */ 
/*     */   
/*     */   public void onUpdateAdvancementProgress(AdvancementNode advancement, AdvancementProgress progress) {
/* 264 */     AdvancementWidget widget = getAdvancementWidget(advancement);
/* 265 */     if (widget != null) {
/* 266 */       widget.setProgress(progress);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSelectedTabChanged(AdvancementHolder selectedTab) {
/* 272 */     this.selectedTab = this.tabs.get(selectedTab);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAdvancementsCleared() {
/* 277 */     this.tabs.clear();
/* 278 */     this.selectedTab = null;
/*     */   }
/*     */   
/*     */   public AdvancementWidget getAdvancementWidget(AdvancementNode node) {
/* 282 */     AdvancementTab tab = getTab(node);
/* 283 */     return (tab == null) ? null : tab.getWidget(node.holder());
/*     */   }
/*     */ 
/*     */   
/*     */   private AdvancementTab getTab(AdvancementNode node) {
/* 288 */     AdvancementNode root = node.root();
/* 289 */     return this.tabs.get(root.holder());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/advancements/AdvancementsScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */