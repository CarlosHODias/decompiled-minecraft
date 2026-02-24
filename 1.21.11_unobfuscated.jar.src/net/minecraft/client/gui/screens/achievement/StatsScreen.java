/*     */ package net.minecraft.client.gui.screens.achievement;
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.ImageButton;
/*     */ import net.minecraft.client.gui.components.ItemDisplayWidget;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.components.tabs.GridLayoutTab;
/*     */ import net.minecraft.client.gui.components.tabs.LoadingTab;
/*     */ import net.minecraft.client.gui.components.tabs.Tab;
/*     */ import net.minecraft.client.gui.components.tabs.TabManager;
/*     */ import net.minecraft.client.gui.components.tabs.TabNavigationBar;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.resources.language.I18n;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.stats.Stat;
/*     */ import net.minecraft.stats.StatType;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.stats.StatsCounter;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.BlockItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ public class StatsScreen extends Screen {
/*  54 */   private static final Component TITLE = (Component)Component.translatable("gui.stats");
/*     */   
/*  56 */   private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot");
/*  57 */   private static final Identifier HEADER_SPRITE = Identifier.withDefaultNamespace("statistics/header");
/*  58 */   private static final Identifier SORT_UP_SPRITE = Identifier.withDefaultNamespace("statistics/sort_up");
/*  59 */   private static final Identifier SORT_DOWN_SPRITE = Identifier.withDefaultNamespace("statistics/sort_down");
/*     */   
/*  61 */   private static final Component PENDING_TEXT = (Component)Component.translatable("multiplayer.downloadingStats");
/*  62 */   private static final Component NO_VALUE_DISPLAY = (Component)Component.translatable("stats.none");
/*  63 */   private static final Component GENERAL_BUTTON = (Component)Component.translatable("stat.generalButton");
/*  64 */   private static final Component ITEMS_BUTTON = (Component)Component.translatable("stat.itemsButton");
/*  65 */   private static final Component MOBS_BUTTON = (Component)Component.translatable("stat.mobsButton");
/*     */   
/*     */   protected final Screen lastScreen;
/*     */   
/*     */   private static final int LIST_WIDTH = 280;
/*     */   
/*  71 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*     */   
/*     */   private final TabManager tabManager;
/*     */   
/*     */   private TabNavigationBar tabNavigationBar;
/*     */   
/*     */   private final StatsCounter stats;
/*     */   
/*     */   private boolean isLoading;
/*     */   
/*     */   public StatsScreen(Screen lastScreen, StatsCounter stats) {
/*  82 */     super(TITLE); this.tabManager = new TabManager(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0), x$0 -> rec$.removeWidget(x$0));
/*     */     this.isLoading = true;
/*  84 */     this.lastScreen = lastScreen;
/*  85 */     this.stats = stats;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  90 */     Component loadingTitle = PENDING_TEXT;
/*  91 */     this
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  96 */       .tabNavigationBar = TabNavigationBar.builder(this.tabManager, this.width).addTabs(new Tab[] { (Tab)new LoadingTab(getFont(), GENERAL_BUTTON, loadingTitle), (Tab)new LoadingTab(getFont(), ITEMS_BUTTON, loadingTitle), (Tab)new LoadingTab(getFont(), MOBS_BUTTON, loadingTitle) }).build();
/*  97 */     addRenderableWidget((GuiEventListener)this.tabNavigationBar);
/*  98 */     this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onClose()).width(200).build());
/*     */     
/* 100 */     this.tabNavigationBar.setTabActiveState(0, true);
/* 101 */     this.tabNavigationBar.setTabActiveState(1, false);
/* 102 */     this.tabNavigationBar.setTabActiveState(2, false);
/* 103 */     this.layout.visitWidgets(button -> {
/*     */           button.setTabOrderGroup(1);
/*     */           addRenderableWidget((GuiEventListener)button);
/*     */         });
/* 107 */     this.tabNavigationBar.selectTab(0, false);
/* 108 */     repositionElements();
/* 109 */     this.minecraft.getConnection().send((net.minecraft.network.protocol.Packet)new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.REQUEST_STATS));
/*     */   }
/*     */   
/*     */   public void onStatsUpdated() {
/* 113 */     if (this.isLoading) {
/* 114 */       if (this.tabNavigationBar != null) {
/* 115 */         removeWidget((GuiEventListener)this.tabNavigationBar);
/*     */       }
/* 117 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 122 */         .tabNavigationBar = TabNavigationBar.builder(this.tabManager, this.width).addTabs(new Tab[] { (Tab)new StatisticsTab(GENERAL_BUTTON, (AbstractSelectionList<?>)new GeneralStatisticsList(this.minecraft)), (Tab)new StatisticsTab(ITEMS_BUTTON, (AbstractSelectionList<?>)new ItemStatisticsList(this.minecraft)), (Tab)new StatisticsTab(MOBS_BUTTON, (AbstractSelectionList<?>)new MobsStatisticsList(this.minecraft)) }).build();
/* 123 */       setFocused((GuiEventListener)this.tabNavigationBar);
/* 124 */       addRenderableWidget((GuiEventListener)this.tabNavigationBar);
/* 125 */       setTabActiveStateAndTooltip(1);
/* 126 */       setTabActiveStateAndTooltip(2);
/* 127 */       this.tabNavigationBar.selectTab(0, false);
/* 128 */       repositionElements();
/* 129 */       this.isLoading = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setTabActiveStateAndTooltip(int index) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield tabNavigationBar : Lnet/minecraft/client/gui/components/tabs/TabNavigationBar;
/*     */     //   4: ifnonnull -> 8
/*     */     //   7: return
/*     */     //   8: aload_0
/*     */     //   9: getfield tabNavigationBar : Lnet/minecraft/client/gui/components/tabs/TabNavigationBar;
/*     */     //   12: invokevirtual getTabs : ()Ljava/util/List;
/*     */     //   15: iload_1
/*     */     //   16: invokeinterface get : (I)Ljava/lang/Object;
/*     */     //   21: astore #4
/*     */     //   23: aload #4
/*     */     //   25: instanceof net/minecraft/client/gui/screens/achievement/StatsScreen$StatisticsTab
/*     */     //   28: ifeq -> 56
/*     */     //   31: aload #4
/*     */     //   33: checkcast net/minecraft/client/gui/screens/achievement/StatsScreen$StatisticsTab
/*     */     //   36: astore_3
/*     */     //   37: aload_3
/*     */     //   38: getfield list : Lnet/minecraft/client/gui/components/AbstractSelectionList;
/*     */     //   41: invokevirtual children : ()Ljava/util/List;
/*     */     //   44: invokeinterface isEmpty : ()Z
/*     */     //   49: ifne -> 56
/*     */     //   52: iconst_1
/*     */     //   53: goto -> 57
/*     */     //   56: iconst_0
/*     */     //   57: istore_2
/*     */     //   58: aload_0
/*     */     //   59: getfield tabNavigationBar : Lnet/minecraft/client/gui/components/tabs/TabNavigationBar;
/*     */     //   62: iload_1
/*     */     //   63: iload_2
/*     */     //   64: invokevirtual setTabActiveState : (IZ)V
/*     */     //   67: iload_2
/*     */     //   68: ifeq -> 83
/*     */     //   71: aload_0
/*     */     //   72: getfield tabNavigationBar : Lnet/minecraft/client/gui/components/tabs/TabNavigationBar;
/*     */     //   75: iload_1
/*     */     //   76: aconst_null
/*     */     //   77: invokevirtual setTabTooltip : (ILnet/minecraft/client/gui/components/Tooltip;)V
/*     */     //   80: goto -> 99
/*     */     //   83: aload_0
/*     */     //   84: getfield tabNavigationBar : Lnet/minecraft/client/gui/components/tabs/TabNavigationBar;
/*     */     //   87: iload_1
/*     */     //   88: ldc 'gui.stats.none_found'
/*     */     //   90: invokestatic translatable : (Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;
/*     */     //   93: invokestatic create : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/client/gui/components/Tooltip;
/*     */     //   96: invokevirtual setTabTooltip : (ILnet/minecraft/client/gui/components/Tooltip;)V
/*     */     //   99: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #134	-> 0
/*     */     //   #135	-> 7
/*     */     //   #137	-> 8
/*     */     //   #138	-> 58
/*     */     //   #139	-> 67
/*     */     //   #140	-> 71
/*     */     //   #142	-> 83
/*     */     //   #144	-> 99
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   37	19	3	statsTab	Lnet/minecraft/client/gui/screens/achievement/StatsScreen$StatisticsTab;
/*     */     //   0	100	0	this	Lnet/minecraft/client/gui/screens/achievement/StatsScreen;
/*     */     //   0	100	1	index	I
/*     */     //   58	42	2	active	Z
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/* 148 */     if (this.tabNavigationBar == null) {
/*     */       return;
/*     */     }
/* 151 */     this.tabNavigationBar.setWidth(this.width);
/* 152 */     this.tabNavigationBar.arrangeElements();
/*     */     
/* 154 */     int tabAreaTop = this.tabNavigationBar.getRectangle().bottom();
/* 155 */     ScreenRectangle tabArea = new ScreenRectangle(0, tabAreaTop, this.width, this.height - this.layout.getFooterHeight() - tabAreaTop);
/* 156 */     this.tabNavigationBar.getTabs().forEach(tab -> tab.visitChildren(()));
/* 157 */     this.tabManager.setTabArea(tabArea);
/* 158 */     this.layout.setHeaderHeight(tabAreaTop);
/* 159 */     this.layout.arrangeElements();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 164 */     if (this.tabNavigationBar != null && this.tabNavigationBar.keyPressed(event)) {
/* 165 */       return true;
/*     */     }
/* 167 */     return super.keyPressed(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int xm, int ym, float a) {
/* 172 */     super.render(graphics, xm, ym, a);
/* 173 */     graphics.blit(RenderPipelines.GUI_TEXTURED, Screen.FOOTER_SEPARATOR, 0, this.height - this.layout.getFooterHeight(), 0.0F, 0.0F, this.width, 2, 32, 2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderMenuBackground(GuiGraphics graphics) {
/* 178 */     graphics.blit(RenderPipelines.GUI_TEXTURED, CreateWorldScreen.TAB_HEADER_BACKGROUND, 0, 0, 0.0F, 0.0F, this.width, this.layout.getHeaderHeight(), 16, 16);
/* 179 */     renderMenuBackground(graphics, 0, this.layout.getHeaderHeight(), this.width, this.height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 184 */     this.minecraft.setScreen(this.lastScreen);
/*     */   }
/*     */   
/*     */   private class StatisticsTab extends GridLayoutTab {
/*     */     protected final AbstractSelectionList<?> list;
/*     */     
/*     */     public StatisticsTab(Component title, AbstractSelectionList<?> list) {
/* 191 */       super(title);
/* 192 */       this.layout.addChild((LayoutElement)list, 1, 1);
/* 193 */       this.list = list;
/*     */     }
/*     */ 
/*     */     
/*     */     public void doLayout(ScreenRectangle screenRectangle) {
/* 198 */       this.list.updateSizeAndPosition(StatsScreen.this.width, StatsScreen.this.layout.getContentHeight(), StatsScreen.this.layout.getHeaderHeight());
/* 199 */       super.doLayout(screenRectangle);
/*     */     }
/*     */   }
/*     */   
/*     */   private class GeneralStatisticsList extends ObjectSelectionList<GeneralStatisticsList.Entry> {
/*     */     public GeneralStatisticsList(Minecraft minecraft) {
/* 205 */       super(minecraft, StatsScreen.this.width, StatsScreen.this.layout.getContentHeight(), 33, 14);
/*     */       
/* 207 */       ObjectArrayList<Stat<Identifier>> stats = new ObjectArrayList(Stats.CUSTOM.iterator());
/* 208 */       stats.sort(Comparator.comparing(k -> I18n.get(StatsScreen.getTranslationKey(k), new Object[0])));
/* 209 */       for (ObjectListIterator<Stat<Identifier>> objectListIterator = stats.iterator(); objectListIterator.hasNext(); ) { Stat<Identifier> stat = objectListIterator.next();
/* 210 */         addEntry((AbstractSelectionList.Entry)new Entry(stat)); }
/*     */     
/*     */     }
/*     */ 
/*     */     
/*     */     public int getRowWidth() {
/* 216 */       return 280;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void renderListBackground(GuiGraphics graphics) {}
/*     */ 
/*     */     
/*     */     protected void renderListSeparators(GuiGraphics graphics) {}
/*     */     
/*     */     private class Entry
/*     */       extends ObjectSelectionList.Entry<Entry>
/*     */     {
/*     */       private final Stat<Identifier> stat;
/*     */       private final Component statDisplay;
/*     */       
/*     */       private Entry(Stat<Identifier> stat) {
/* 232 */         this.stat = stat;
/* 233 */         this.statDisplay = (Component)Component.translatable(StatsScreen.getTranslationKey(stat));
/*     */       }
/*     */       
/*     */       private String getValueText() {
/* 237 */         return this.stat.format(StatsScreen.this.stats.getValue(this.stat));
/*     */       }
/*     */ 
/*     */       
/*     */       public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 242 */         Objects.requireNonNull(StatsScreen.this.font); int y = getContentYMiddle() - 9 / 2;
/* 243 */         int index = StatsScreen.GeneralStatisticsList.this.children().indexOf(this);
/* 244 */         int color = (index % 2 == 0) ? -1 : -4539718;
/* 245 */         graphics.drawString(StatsScreen.this.font, this.statDisplay, getContentX() + 2, y, color);
/* 246 */         String msg = getValueText();
/* 247 */         graphics.drawString(StatsScreen.this.font, msg, getContentRight() - StatsScreen.this.font.width(msg) - 4, y, color);
/*     */       }
/*     */       
/*     */       public Component getNarration()
/*     */       {
/* 252 */         return (Component)Component.translatable("narrator.select", new Object[] { Component.empty().append(this.statDisplay).append(CommonComponents.SPACE).append(getValueText()) }); } } } private class Entry extends ObjectSelectionList.Entry<GeneralStatisticsList.Entry> { private final Stat<Identifier> stat; private final Component statDisplay; private Entry(Stat<Identifier> stat) { this.stat = stat; this.statDisplay = (Component)Component.translatable(StatsScreen.getTranslationKey(stat)); } private String getValueText() { return this.stat.format(StatsScreen.this.stats.getValue(this.stat)); } public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) { Objects.requireNonNull(StatsScreen.this.font); int y = getContentYMiddle() - 9 / 2; int index = StatsScreen.GeneralStatisticsList.this.children().indexOf(this); int color = (index % 2 == 0) ? -1 : -4539718; graphics.drawString(StatsScreen.this.font, this.statDisplay, getContentX() + 2, y, color); String msg = getValueText(); graphics.drawString(StatsScreen.this.font, msg, getContentRight() - StatsScreen.this.font.width(msg) - 4, y, color); } public Component getNarration() { return (Component)Component.translatable("narrator.select", new Object[] { Component.empty().append(this.statDisplay).append(CommonComponents.SPACE).append(getValueText()) }); }
/*     */      }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getTranslationKey(Stat<Identifier> stat) {
/* 258 */     return "stat." + ((Identifier)stat.getValue()).toString().replace(':', '.');
/*     */   }
/*     */ 
/*     */   
/*     */   private class ItemStatisticsList
/*     */     extends ContainerObjectSelectionList<ItemStatisticsList.Entry>
/*     */   {
/*     */     private static final int SLOT_BG_SIZE = 18;
/*     */     private static final int SLOT_STAT_HEIGHT = 22;
/*     */     private static final int SLOT_BG_Y = 1;
/*     */     private static final int SORT_NONE = 0;
/*     */     private static final int SORT_DOWN = -1;
/*     */     private static final int SORT_UP = 1;
/*     */     protected final List<StatType<Block>> blockColumns;
/*     */     protected final List<StatType<Item>> itemColumns;
/* 273 */     protected final Comparator<ItemRow> itemStatSorter = new ItemRowComparator();
/*     */     
/*     */     protected StatType<?> sortColumn;
/*     */     protected int sortOrder;
/*     */     
/*     */     public ItemStatisticsList(Minecraft minecraft) {
/* 279 */       super(minecraft, StatsScreen.this.width, StatsScreen.this.layout.getContentHeight(), 33, 22);
/*     */       
/* 281 */       this.blockColumns = Lists.newArrayList();
/* 282 */       this.blockColumns.add(Stats.BLOCK_MINED);
/* 283 */       this.itemColumns = Lists.newArrayList((Object[])new StatType[] { Stats.ITEM_BROKEN, Stats.ITEM_CRAFTED, Stats.ITEM_USED, Stats.ITEM_PICKED_UP, Stats.ITEM_DROPPED });
/*     */       
/* 285 */       Set<Item> items = com.google.common.collect.Sets.newIdentityHashSet();
/* 286 */       for (Item item : (Iterable<Item>)BuiltInRegistries.ITEM) {
/*     */         boolean addToList = false;
/*     */         
/* 289 */         for (StatType<Item> type : this.itemColumns) {
/* 290 */           if (type.contains(item) && StatsScreen.this.stats.getValue(type.get(item)) > 0) {
/* 291 */             addToList = true;
/*     */           }
/*     */         } 
/*     */         
/* 295 */         if (addToList) {
/* 296 */           items.add(item);
/*     */         }
/*     */       } 
/*     */       
/* 300 */       for (Block block : (Iterable<Block>)BuiltInRegistries.BLOCK) {
/*     */         boolean addToList = false;
/*     */         
/* 303 */         for (StatType<Block> type : this.blockColumns) {
/* 304 */           if (type.contains(block) && StatsScreen.this.stats.getValue(type.get(block)) > 0) {
/* 305 */             addToList = true;
/*     */           }
/*     */         } 
/*     */         
/* 309 */         if (addToList) {
/* 310 */           items.add(block.asItem());
/*     */         }
/*     */       } 
/*     */       
/* 314 */       items.remove(Items.AIR);
/* 315 */       if (!items.isEmpty()) {
/* 316 */         addEntry((AbstractSelectionList.Entry)new HeaderEntry());
/* 317 */         for (Item item : items) {
/* 318 */           addEntry((AbstractSelectionList.Entry)new ItemRow(item));
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void renderListBackground(GuiGraphics graphics) {}
/*     */ 
/*     */     
/*     */     private int getColumnX(int col) {
/* 328 */       return 75 + 40 * col;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getRowWidth() {
/* 333 */       return 280;
/*     */     }
/*     */     
/*     */     private StatType<?> getColumn(int i) {
/* 337 */       return (i < this.blockColumns.size()) ? this.blockColumns.get(i) : this.itemColumns.get(i - this.blockColumns.size());
/*     */     }
/*     */     
/*     */     private int getColumnIndex(StatType<?> column) {
/* 341 */       int i = this.blockColumns.indexOf(column);
/* 342 */       if (i >= 0) {
/* 343 */         return i;
/*     */       }
/* 345 */       int j = this.itemColumns.indexOf(column);
/* 346 */       if (j >= 0) {
/* 347 */         return j + this.blockColumns.size();
/*     */       }
/* 349 */       return -1;
/*     */     }
/*     */     
/*     */     protected void sortByColumn(StatType<?> column) {
/* 353 */       if (column != this.sortColumn) {
/* 354 */         this.sortColumn = column;
/* 355 */         this.sortOrder = -1;
/* 356 */       } else if (this.sortOrder == -1) {
/* 357 */         this.sortOrder = 1;
/*     */       } else {
/* 359 */         this.sortColumn = null;
/* 360 */         this.sortOrder = 0;
/*     */       } 
/*     */       
/* 363 */       sortItems(this.itemStatSorter);
/*     */     }
/*     */     
/*     */     protected void sortItems(Comparator<ItemRow> comparator) {
/* 367 */       List<ItemRow> itemRows = getItemRows();
/* 368 */       itemRows.sort(comparator);
/* 369 */       clearEntriesExcept((AbstractSelectionList.Entry)children().getFirst());
/* 370 */       for (ItemRow newChild : itemRows) {
/* 371 */         addEntry((AbstractSelectionList.Entry)newChild);
/*     */       }
/*     */     }
/*     */     
/*     */     private List<ItemRow> getItemRows() {
/* 376 */       List<ItemRow> itemRows = new ArrayList<>();
/* 377 */       children().forEach(entry -> {
/*     */             if (entry instanceof ItemRow) {
/*     */               ItemRow itemRow = (ItemRow)entry; itemRows.add(itemRow);
/*     */             } 
/*     */           });
/* 382 */       return itemRows;
/*     */     }
/*     */     
/*     */     private class ItemRowComparator
/*     */       implements Comparator<ItemRow> {
/*     */       public int compare(StatsScreen.ItemStatisticsList.ItemRow one, StatsScreen.ItemStatisticsList.ItemRow two) {
/*     */         int key1, key2;
/* 389 */         Item item1 = one.getItem();
/* 390 */         Item item2 = two.getItem();
/*     */ 
/*     */ 
/*     */         
/* 394 */         if (StatsScreen.ItemStatisticsList.this.sortColumn == null) {
/* 395 */           key1 = 0;
/* 396 */           key2 = 0;
/* 397 */         } else if (StatsScreen.ItemStatisticsList.this.blockColumns.contains(StatsScreen.ItemStatisticsList.this.sortColumn)) {
/* 398 */           StatType<Block> type = (StatType)StatsScreen.ItemStatisticsList.this.sortColumn;
/* 399 */           key1 = (item1 instanceof BlockItem) ? StatsScreen.this.stats.getValue(type, ((BlockItem)item1).getBlock()) : -1;
/* 400 */           key2 = (item2 instanceof BlockItem) ? StatsScreen.this.stats.getValue(type, ((BlockItem)item2).getBlock()) : -1;
/*     */         } else {
/* 402 */           StatType<Item> type = (StatType)StatsScreen.ItemStatisticsList.this.sortColumn;
/* 403 */           key1 = StatsScreen.this.stats.getValue(type, item1);
/* 404 */           key2 = StatsScreen.this.stats.getValue(type, item2);
/*     */         } 
/*     */         
/* 407 */         if (key1 == key2) {
/* 408 */           return StatsScreen.ItemStatisticsList.this.sortOrder * Integer.compare(Item.getId(item1), Item.getId(item2));
/*     */         }
/*     */         
/* 411 */         return StatsScreen.ItemStatisticsList.this.sortOrder * Integer.compare(key1, key2);
/*     */       }
/*     */     }
/*     */     
/*     */     protected void renderListSeparators(GuiGraphics graphics) {}
/*     */     
/*     */     private static abstract class Entry
/*     */       extends ContainerObjectSelectionList.Entry<Entry> {}
/*     */     
/*     */     private class ItemRow
/*     */       extends Entry
/*     */     {
/*     */       private final Item item;
/*     */       private final ItemRowWidget itemRowWidget;
/*     */       
/*     */       private ItemRow(Item item) {
/* 427 */         this.item = item;
/* 428 */         this.itemRowWidget = new ItemRowWidget(item.getDefaultInstance());
/*     */       }
/*     */       
/*     */       protected Item getItem() {
/* 432 */         return this.item;
/*     */       }
/*     */ 
/*     */       
/*     */       public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 437 */         this.itemRowWidget.setPosition(getContentX(), getContentY());
/* 438 */         this.itemRowWidget.render(graphics, mouseX, mouseY, a);
/*     */         
/* 440 */         StatsScreen.ItemStatisticsList itemStatsList = StatsScreen.ItemStatisticsList.this;
/* 441 */         int index = itemStatsList.children().indexOf(this);
/* 442 */         for (int col = 0; col < itemStatsList.blockColumns.size(); col++) {
/*     */           Stat<Block> stat;
/* 444 */           Item item = this.item; if (item instanceof BlockItem) { BlockItem blockItem = (BlockItem)item;
/* 445 */             stat = ((StatType)itemStatsList.blockColumns.get(col)).get(blockItem.getBlock()); }
/*     */           else
/* 447 */           { stat = null; }
/*     */           
/* 449 */           Objects.requireNonNull(StatsScreen.this.font); renderStat(graphics, stat, getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(col), getContentYMiddle() - 9 / 2, (index % 2 == 0));
/*     */         } 
/* 451 */         for (int i = 0; i < itemStatsList.itemColumns.size(); 
/* 452 */           renderStat(graphics, ((StatType)itemStatsList.itemColumns.get(i)).get(this.item), getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(i + itemStatsList.blockColumns.size()), 
/* 453 */             getContentYMiddle() - 9 / 2, (index % 2 == 0)), i++) Objects.requireNonNull(StatsScreen.this.font);
/*     */       
/*     */       }
/*     */       
/*     */       protected void renderStat(GuiGraphics graphics, Stat<?> stat, int x, int y, boolean shaded) {
/* 458 */         Component msg = (stat == null) ? StatsScreen.NO_VALUE_DISPLAY : (Component)Component.literal(stat.format(StatsScreen.this.stats.getValue(stat)));
/* 459 */         graphics.drawString(StatsScreen.this.font, msg, x - StatsScreen.this.font.width((net.minecraft.network.chat.FormattedText)msg), y, shaded ? -1 : -4539718);
/*     */       }
/*     */ 
/*     */       
/*     */       public List<? extends NarratableEntry> narratables() {
/* 464 */         return (List)List.of(this.itemRowWidget);
/*     */       }
/*     */ 
/*     */       
/*     */       public List<? extends GuiEventListener> children() {
/* 469 */         return (List)List.of(this.itemRowWidget);
/*     */       }
/*     */       
/*     */       private class ItemRowWidget extends ItemDisplayWidget {
/*     */         private ItemRowWidget(ItemStack itemStack) {
/* 474 */           super(StatsScreen.ItemStatisticsList.this.minecraft, 1, 1, 18, 18, itemStack.getHoverName(), itemStack, false, true);
/*     */         }
/*     */ 
/*     */         
/*     */         protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 479 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, StatsScreen.SLOT_SPRITE, StatsScreen.ItemStatisticsList.ItemRow.this.getContentX(), StatsScreen.ItemStatisticsList.ItemRow.this.getContentY(), 18, 18);
/* 480 */           super.renderWidget(graphics, mouseX, mouseY, a);
/*     */         }
/*     */ 
/*     */         
/*     */         protected void renderTooltip(GuiGraphics graphics, int x, int y) {
/* 485 */           super.renderTooltip(graphics, StatsScreen.ItemStatisticsList.ItemRow.this.getContentX() + 18, StatsScreen.ItemStatisticsList.ItemRow.this.getContentY() + 18);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private class HeaderEntry extends Entry {
/* 491 */       private static final Identifier BLOCK_MINED_SPRITE = Identifier.withDefaultNamespace("statistics/block_mined");
/* 492 */       private static final Identifier ITEM_BROKEN_SPRITE = Identifier.withDefaultNamespace("statistics/item_broken");
/* 493 */       private static final Identifier ITEM_CRAFTED_SPRITE = Identifier.withDefaultNamespace("statistics/item_crafted");
/* 494 */       private static final Identifier ITEM_USED_SPRITE = Identifier.withDefaultNamespace("statistics/item_used");
/* 495 */       private static final Identifier ITEM_PICKED_UP_SPRITE = Identifier.withDefaultNamespace("statistics/item_picked_up");
/* 496 */       private static final Identifier ITEM_DROPPED_SPRITE = Identifier.withDefaultNamespace("statistics/item_dropped");
/*     */       
/*     */       private final StatSortButton blockMined;
/*     */       
/*     */       private final StatSortButton itemBroken;
/*     */       private final StatSortButton itemCrafted;
/*     */       private final StatSortButton itemUsed;
/*     */       private final StatSortButton itemPickedUp;
/*     */       private final StatSortButton itemDropped;
/* 505 */       private final List<AbstractWidget> children = new ArrayList<>();
/*     */       
/*     */       private HeaderEntry() {
/* 508 */         this.blockMined = new StatSortButton(this, 0, BLOCK_MINED_SPRITE);
/* 509 */         this.itemBroken = new StatSortButton(this, 1, ITEM_BROKEN_SPRITE);
/* 510 */         this.itemCrafted = new StatSortButton(this, 2, ITEM_CRAFTED_SPRITE);
/* 511 */         this.itemUsed = new StatSortButton(this, 3, ITEM_USED_SPRITE);
/* 512 */         this.itemPickedUp = new StatSortButton(this, 4, ITEM_PICKED_UP_SPRITE);
/* 513 */         this.itemDropped = new StatSortButton(this, 5, ITEM_DROPPED_SPRITE);
/* 514 */         this.children.addAll((Collection)List.of(this.blockMined, this.itemBroken, this.itemCrafted, this.itemUsed, this.itemPickedUp, this.itemDropped));
/*     */       }
/*     */ 
/*     */       
/*     */       public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 519 */         this.blockMined.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(0) - 18, getContentY() + 1);
/* 520 */         this.blockMined.render(graphics, mouseX, mouseY, a);
/* 521 */         this.itemBroken.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(1) - 18, getContentY() + 1);
/* 522 */         this.itemBroken.render(graphics, mouseX, mouseY, a);
/* 523 */         this.itemCrafted.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(2) - 18, getContentY() + 1);
/* 524 */         this.itemCrafted.render(graphics, mouseX, mouseY, a);
/* 525 */         this.itemUsed.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(3) - 18, getContentY() + 1);
/* 526 */         this.itemUsed.render(graphics, mouseX, mouseY, a);
/* 527 */         this.itemPickedUp.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(4) - 18, getContentY() + 1);
/* 528 */         this.itemPickedUp.render(graphics, mouseX, mouseY, a);
/* 529 */         this.itemDropped.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(5) - 18, getContentY() + 1);
/* 530 */         this.itemDropped.render(graphics, mouseX, mouseY, a);
/*     */         
/* 532 */         if (StatsScreen.ItemStatisticsList.this.sortColumn != null) {
/* 533 */           int offset = StatsScreen.ItemStatisticsList.this.getColumnX(StatsScreen.ItemStatisticsList.this.getColumnIndex(StatsScreen.ItemStatisticsList.this.sortColumn)) - 36;
/*     */           
/* 535 */           Identifier sprite = (StatsScreen.ItemStatisticsList.this.sortOrder == 1) ? StatsScreen.SORT_UP_SPRITE : StatsScreen.SORT_DOWN_SPRITE;
/* 536 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getContentX() + offset, getContentY() + 1, 18, 18);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public List<? extends GuiEventListener> children() {
/* 542 */         return (List)this.children;
/*     */       }
/*     */ 
/*     */       
/*     */       public List<? extends NarratableEntry> narratables() {
/* 547 */         return (List)this.children;
/*     */       }
/*     */       
/*     */       private class StatSortButton extends ImageButton {
/*     */         private final Identifier sprite;
/*     */         
/*     */         private StatSortButton(StatsScreen.ItemStatisticsList.HeaderEntry this$0, int column, Identifier sprite) {
/* 554 */           super(18, 18, new net.minecraft.client.gui.components.WidgetSprites(StatsScreen.HEADER_SPRITE, StatsScreen.SLOT_SPRITE), button -> net$minecraft$client$gui$screens$achievement$StatsScreen$ItemStatisticsList$this.sortByColumn(net$minecraft$client$gui$screens$achievement$StatsScreen$ItemStatisticsList$this.getColumn(column)), 
/* 555 */               this$0.this$1.getColumn(column).getDisplayName());
/* 556 */           this.sprite = sprite;
/* 557 */           setTooltip(Tooltip.create(getMessage()));
/*     */         }
/*     */         
/*     */         public void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a)
/*     */         {
/* 562 */           Identifier background = this.sprites.get(isActive(), isHoveredOrFocused());
/* 563 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, background, getX(), getY(), this.width, this.height);
/* 564 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, getX(), getY(), this.width, this.height); } } } } private class ItemRowComparator implements Comparator<ItemStatisticsList.ItemRow> { public int compare(StatsScreen.ItemStatisticsList.ItemRow one, StatsScreen.ItemStatisticsList.ItemRow two) { int key1, key2; Item item1 = one.getItem(); Item item2 = two.getItem(); if (StatsScreen.ItemStatisticsList.this.sortColumn == null) { key1 = 0; key2 = 0; } else if (StatsScreen.ItemStatisticsList.this.blockColumns.contains(StatsScreen.ItemStatisticsList.this.sortColumn)) { StatType<Block> type = (StatType)StatsScreen.ItemStatisticsList.this.sortColumn; key1 = (item1 instanceof BlockItem) ? StatsScreen.this.stats.getValue(type, ((BlockItem)item1).getBlock()) : -1; key2 = (item2 instanceof BlockItem) ? StatsScreen.this.stats.getValue(type, ((BlockItem)item2).getBlock()) : -1; } else { StatType<Item> type = (StatType)StatsScreen.ItemStatisticsList.this.sortColumn; key1 = StatsScreen.this.stats.getValue(type, item1); key2 = StatsScreen.this.stats.getValue(type, item2); }  if (key1 == key2) return StatsScreen.ItemStatisticsList.this.sortOrder * Integer.compare(Item.getId(item1), Item.getId(item2));  return StatsScreen.ItemStatisticsList.this.sortOrder * Integer.compare(key1, key2); } } private static abstract class Entry extends ContainerObjectSelectionList.Entry<ItemStatisticsList.Entry> {} private class ItemRow extends ItemStatisticsList.Entry { private final Item item; private final ItemRowWidget itemRowWidget; private ItemRow(Item item) { this.item = item; this.itemRowWidget = new ItemRowWidget(item.getDefaultInstance()); } protected Item getItem() { return this.item; } public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) { this.itemRowWidget.setPosition(getContentX(), getContentY()); this.itemRowWidget.render(graphics, mouseX, mouseY, a); StatsScreen.ItemStatisticsList itemStatsList = StatsScreen.ItemStatisticsList.this; int index = itemStatsList.children().indexOf(this); for (int col = 0; col < itemStatsList.blockColumns.size(); col++) { Stat<Block> stat; Item item = this.item; if (item instanceof BlockItem) { BlockItem blockItem = (BlockItem)item; stat = ((StatType)itemStatsList.blockColumns.get(col)).get(blockItem.getBlock()); } else { stat = null; }  Objects.requireNonNull(StatsScreen.this.font); renderStat(graphics, stat, getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(col), getContentYMiddle() - 9 / 2, (index % 2 == 0)); }  for (int i = 0; i < itemStatsList.itemColumns.size(); renderStat(graphics, ((StatType)itemStatsList.itemColumns.get(i)).get(this.item), getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(i + itemStatsList.blockColumns.size()), getContentYMiddle() - 9 / 2, (index % 2 == 0)), i++) Objects.requireNonNull(StatsScreen.this.font);  } protected void renderStat(GuiGraphics graphics, Stat<?> stat, int x, int y, boolean shaded) { Component msg = (stat == null) ? StatsScreen.NO_VALUE_DISPLAY : (Component)Component.literal(stat.format(StatsScreen.this.stats.getValue(stat))); graphics.drawString(StatsScreen.this.font, msg, x - StatsScreen.this.font.width((net.minecraft.network.chat.FormattedText)msg), y, shaded ? -1 : -4539718); } public List<? extends NarratableEntry> narratables() { return (List)List.of(this.itemRowWidget); } public List<? extends GuiEventListener> children() { return (List)List.of(this.itemRowWidget); } private class ItemRowWidget extends ItemDisplayWidget { private ItemRowWidget(ItemStack itemStack) { super(this$0.this$1.minecraft, 1, 1, 18, 18, itemStack.getHoverName(), itemStack, false, true); } protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) { graphics.blitSprite(RenderPipelines.GUI_TEXTURED, StatsScreen.SLOT_SPRITE, StatsScreen.ItemStatisticsList.ItemRow.this.getContentX(), StatsScreen.ItemStatisticsList.ItemRow.this.getContentY(), 18, 18); super.renderWidget(graphics, mouseX, mouseY, a); } protected void renderTooltip(GuiGraphics graphics, int x, int y) { super.renderTooltip(graphics, StatsScreen.ItemStatisticsList.ItemRow.this.getContentX() + 18, StatsScreen.ItemStatisticsList.ItemRow.this.getContentY() + 18); } } } private class ItemRowWidget extends ItemDisplayWidget { private ItemRowWidget(ItemStack itemStack) { super(((StatsScreen.ItemStatisticsList.ItemRow)this$0).this$1.minecraft, 1, 1, 18, 18, itemStack.getHoverName(), itemStack, false, true); } protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) { graphics.blitSprite(RenderPipelines.GUI_TEXTURED, StatsScreen.SLOT_SPRITE, StatsScreen.ItemStatisticsList.ItemRow.this.getContentX(), StatsScreen.ItemStatisticsList.ItemRow.this.getContentY(), 18, 18); super.renderWidget(graphics, mouseX, mouseY, a); } protected void renderTooltip(GuiGraphics graphics, int x, int y) { super.renderTooltip(graphics, StatsScreen.ItemStatisticsList.ItemRow.this.getContentX() + 18, StatsScreen.ItemStatisticsList.ItemRow.this.getContentY() + 18); } } private class HeaderEntry extends ItemStatisticsList.Entry { private static final Identifier BLOCK_MINED_SPRITE = Identifier.withDefaultNamespace("statistics/block_mined"); private static final Identifier ITEM_BROKEN_SPRITE = Identifier.withDefaultNamespace("statistics/item_broken"); private static final Identifier ITEM_CRAFTED_SPRITE = Identifier.withDefaultNamespace("statistics/item_crafted"); private static final Identifier ITEM_USED_SPRITE = Identifier.withDefaultNamespace("statistics/item_used"); private static final Identifier ITEM_PICKED_UP_SPRITE = Identifier.withDefaultNamespace("statistics/item_picked_up"); private static final Identifier ITEM_DROPPED_SPRITE = Identifier.withDefaultNamespace("statistics/item_dropped"); private final StatSortButton blockMined; private final StatSortButton itemBroken; private final StatSortButton itemCrafted; private final StatSortButton itemUsed; private final StatSortButton itemPickedUp; private final StatSortButton itemDropped; private final List<AbstractWidget> children = new ArrayList<>(); private HeaderEntry() { this.blockMined = new StatSortButton(this, 0, BLOCK_MINED_SPRITE); this.itemBroken = new StatSortButton(this, 1, ITEM_BROKEN_SPRITE); this.itemCrafted = new StatSortButton(this, 2, ITEM_CRAFTED_SPRITE); this.itemUsed = new StatSortButton(this, 3, ITEM_USED_SPRITE); this.itemPickedUp = new StatSortButton(this, 4, ITEM_PICKED_UP_SPRITE); this.itemDropped = new StatSortButton(this, 5, ITEM_DROPPED_SPRITE); this.children.addAll((Collection)List.of(this.blockMined, this.itemBroken, this.itemCrafted, this.itemUsed, this.itemPickedUp, this.itemDropped)); } public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) { this.blockMined.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(0) - 18, getContentY() + 1); this.blockMined.render(graphics, mouseX, mouseY, a); this.itemBroken.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(1) - 18, getContentY() + 1); this.itemBroken.render(graphics, mouseX, mouseY, a); this.itemCrafted.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(2) - 18, getContentY() + 1); this.itemCrafted.render(graphics, mouseX, mouseY, a); this.itemUsed.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(3) - 18, getContentY() + 1); this.itemUsed.render(graphics, mouseX, mouseY, a); this.itemPickedUp.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(4) - 18, getContentY() + 1); this.itemPickedUp.render(graphics, mouseX, mouseY, a); this.itemDropped.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(5) - 18, getContentY() + 1); this.itemDropped.render(graphics, mouseX, mouseY, a); if (StatsScreen.ItemStatisticsList.this.sortColumn != null) { int offset = StatsScreen.ItemStatisticsList.this.getColumnX(StatsScreen.ItemStatisticsList.this.getColumnIndex(StatsScreen.ItemStatisticsList.this.sortColumn)) - 36; Identifier sprite = (StatsScreen.ItemStatisticsList.this.sortOrder == 1) ? StatsScreen.SORT_UP_SPRITE : StatsScreen.SORT_DOWN_SPRITE; graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getContentX() + offset, getContentY() + 1, 18, 18); }  } public List<? extends GuiEventListener> children() { return (List)this.children; } public List<? extends NarratableEntry> narratables() { return (List)this.children; } private class StatSortButton extends ImageButton { private final Identifier sprite; public void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) { Identifier background = this.sprites.get(isActive(), isHoveredOrFocused()); graphics.blitSprite(RenderPipelines.GUI_TEXTURED, background, getX(), getY(), this.width, this.height); graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, getX(), getY(), this.width, this.height); } private StatSortButton(StatsScreen.ItemStatisticsList.HeaderEntry this$0, int column, Identifier sprite) { super(18, 18, new net.minecraft.client.gui.components.WidgetSprites(StatsScreen.HEADER_SPRITE, StatsScreen.SLOT_SPRITE), button -> net$minecraft$client$gui$screens$achievement$StatsScreen$ItemStatisticsList$this.sortByColumn(net$minecraft$client$gui$screens$achievement$StatsScreen$ItemStatisticsList$this.getColumn(column)), this$0.this$1.getColumn(column).getDisplayName()); this.sprite = sprite; setTooltip(Tooltip.create(getMessage())); } } } private class StatSortButton extends ImageButton { private final Identifier sprite; private StatSortButton(StatsScreen this$0, int column, Identifier sprite) { super(18, 18, new net.minecraft.client.gui.components.WidgetSprites(StatsScreen.HEADER_SPRITE, StatsScreen.SLOT_SPRITE), button -> net$minecraft$client$gui$screens$achievement$StatsScreen$ItemStatisticsList$this.sortByColumn(net$minecraft$client$gui$screens$achievement$StatsScreen$ItemStatisticsList$this.getColumn(column)), ((StatsScreen.ItemStatisticsList.HeaderEntry)this$0).this$1.getColumn(column).getDisplayName()); this.sprite = sprite; setTooltip(Tooltip.create(getMessage())); } public void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) { Identifier background = this.sprites.get(isActive(), isHoveredOrFocused()); graphics.blitSprite(RenderPipelines.GUI_TEXTURED, background, getX(), getY(), this.width, this.height); graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, getX(), getY(), this.width, this.height); }
/*     */      }
/*     */ 
/*     */   
/*     */   private class MobsStatisticsList
/*     */     extends ObjectSelectionList<MobsStatisticsList.MobRow>
/*     */   {
/*     */     public MobsStatisticsList(Minecraft minecraft) {
/* 572 */       super(minecraft, StatsScreen.this.width, StatsScreen.this.layout.getContentHeight(), 33, 9 * 4);
/*     */       
/* 574 */       for (EntityType<?> type : (Iterable<EntityType<?>>)BuiltInRegistries.ENTITY_TYPE) {
/* 575 */         if (StatsScreen.this.stats.getValue(Stats.ENTITY_KILLED.get(type)) > 0 || StatsScreen.this.stats.getValue(Stats.ENTITY_KILLED_BY.get(type)) > 0) {
/* 576 */           addEntry((AbstractSelectionList.Entry)new MobRow(type));
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int getRowWidth() {
/* 583 */       return 280;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void renderListBackground(GuiGraphics graphics) {}
/*     */ 
/*     */     
/*     */     protected void renderListSeparators(GuiGraphics graphics) {}
/*     */ 
/*     */     
/*     */     private class MobRow
/*     */       extends ObjectSelectionList.Entry<MobRow>
/*     */     {
/*     */       private final Component mobName;
/*     */       private final Component kills;
/*     */       private final Component killedBy;
/*     */       private final boolean hasKills;
/*     */       private final boolean wasKilledBy;
/*     */       
/*     */       public MobRow(EntityType<?> type) {
/* 603 */         this.mobName = type.getDescription();
/*     */         
/* 605 */         int kills = StatsScreen.this.stats.getValue(Stats.ENTITY_KILLED.get(type));
/* 606 */         if (kills == 0) {
/* 607 */           this.kills = (Component)Component.translatable("stat_type.minecraft.killed.none", new Object[] { this.mobName });
/* 608 */           this.hasKills = false;
/*     */         } else {
/* 610 */           this.kills = (Component)Component.translatable("stat_type.minecraft.killed", new Object[] { kills, this.mobName });
/* 611 */           this.hasKills = true;
/*     */         } 
/*     */         
/* 614 */         int killedBy = StatsScreen.this.stats.getValue(Stats.ENTITY_KILLED_BY.get(type));
/* 615 */         if (killedBy == 0) {
/* 616 */           this.killedBy = (Component)Component.translatable("stat_type.minecraft.killed_by.none", new Object[] { this.mobName });
/* 617 */           this.wasKilledBy = false;
/*     */         } else {
/* 619 */           this.killedBy = (Component)Component.translatable("stat_type.minecraft.killed_by", new Object[] { this.mobName, killedBy });
/* 620 */           this.wasKilledBy = true;
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 626 */         graphics.drawString(StatsScreen.this.font, this.mobName, getContentX() + 2, getContentY() + 1, -1);
/* 627 */         Objects.requireNonNull(StatsScreen.this.font); graphics.drawString(StatsScreen.this.font, this.kills, getContentX() + 2 + 10, getContentY() + 1 + 9, this.hasKills ? -4539718 : -8355712);
/* 628 */         Objects.requireNonNull(StatsScreen.this.font); graphics.drawString(StatsScreen.this.font, this.killedBy, getContentX() + 2 + 10, getContentY() + 1 + 9 * 2, this.wasKilledBy ? -4539718 : -8355712);
/*     */       }
/*     */       
/*     */       public Component getNarration()
/*     */       {
/* 633 */         return (Component)Component.translatable("narrator.select", new Object[] { CommonComponents.joinForNarration(new Component[] { this.kills, this.killedBy }) }); } } } private class MobRow extends ObjectSelectionList.Entry<MobsStatisticsList.MobRow> { private final Component mobName; private final Component kills; private final Component killedBy; private final boolean hasKills; private final boolean wasKilledBy; public MobRow(EntityType<?> type) { this.mobName = type.getDescription(); int kills = StatsScreen.this.stats.getValue(Stats.ENTITY_KILLED.get(type)); if (kills == 0) { this.kills = (Component)Component.translatable("stat_type.minecraft.killed.none", new Object[] { this.mobName }); this.hasKills = false; } else { this.kills = (Component)Component.translatable("stat_type.minecraft.killed", new Object[] { kills, this.mobName }); this.hasKills = true; }  int killedBy = StatsScreen.this.stats.getValue(Stats.ENTITY_KILLED_BY.get(type)); if (killedBy == 0) { this.killedBy = (Component)Component.translatable("stat_type.minecraft.killed_by.none", new Object[] { this.mobName }); this.wasKilledBy = false; } else { this.killedBy = (Component)Component.translatable("stat_type.minecraft.killed_by", new Object[] { this.mobName, killedBy }); this.wasKilledBy = true; }  } public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) { graphics.drawString(StatsScreen.this.font, this.mobName, getContentX() + 2, getContentY() + 1, -1); Objects.requireNonNull(StatsScreen.this.font); graphics.drawString(StatsScreen.this.font, this.kills, getContentX() + 2 + 10, getContentY() + 1 + 9, this.hasKills ? -4539718 : -8355712); Objects.requireNonNull(StatsScreen.this.font); graphics.drawString(StatsScreen.this.font, this.killedBy, getContentX() + 2 + 10, getContentY() + 1 + 9 * 2, this.wasKilledBy ? -4539718 : -8355712); } public Component getNarration() { return (Component)Component.translatable("narrator.select", new Object[] { CommonComponents.joinForNarration(new Component[] { this.kills, this.killedBy }) }); }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/achievement/StatsScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */