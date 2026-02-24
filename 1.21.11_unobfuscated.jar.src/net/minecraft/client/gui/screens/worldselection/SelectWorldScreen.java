/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*     */ import net.minecraft.world.level.storage.LevelSummary;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SelectWorldScreen extends Screen {
/*  31 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  33 */   public static final WorldOptions TEST_OPTIONS = new WorldOptions("test1".hashCode(), true, false);
/*     */   
/*     */   protected final Screen lastScreen;
/*     */   
/*     */   private final HeaderAndFooterLayout layout;
/*     */   
/*     */   private Button deleteButton;
/*     */   
/*     */   private Button selectButton;
/*     */   
/*     */   private Button renameButton;
/*     */   
/*     */   private Button copyButton;
/*     */   protected EditBox searchBox;
/*     */   private WorldSelectionList list;
/*     */   
/*     */   public SelectWorldScreen(Screen lastScreen) {
/*  50 */     super((Component)Component.translatable("selectWorld.title")); Objects.requireNonNull((Minecraft.getInstance()).font); this.layout = new HeaderAndFooterLayout(this, 8 + 9 + 8 + 20 + 4, 60);
/*  51 */     this.lastScreen = lastScreen;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  56 */     LinearLayout header = (LinearLayout)this.layout.addToHeader((LayoutElement)LinearLayout.vertical().spacing(4));
/*  57 */     header.defaultCellSetting().alignHorizontallyCenter();
/*  58 */     header.addChild((LayoutElement)new StringWidget(this.title, this.font));
/*     */     
/*  60 */     LinearLayout subHeader = (LinearLayout)header.addChild((LayoutElement)LinearLayout.horizontal().spacing(4));
/*  61 */     if (net.minecraft.SharedConstants.DEBUG_WORLD_RECREATE) {
/*  62 */       subHeader.addChild((LayoutElement)createDebugWorldRecreateButton());
/*     */     }
/*  64 */     this.searchBox = (EditBox)subHeader.addChild((LayoutElement)new EditBox(this.font, this.width / 2 - 100, 22, 200, 20, this.searchBox, (Component)Component.translatable("selectWorld.search")));
/*  65 */     this.searchBox.setResponder(value -> {
/*     */           if (this.list != null) {
/*     */             this.list.updateFilter(value);
/*     */           }
/*     */         });
/*  70 */     this.searchBox.setHint((Component)Component.translatable("gui.selectWorld.search").setStyle(EditBox.SEARCH_HINT_STYLE));
/*     */     
/*  72 */     Consumer<WorldSelectionList.WorldListEntry> joinWorld = WorldSelectionList.WorldListEntry::joinWorld;
/*  73 */     this.list = (WorldSelectionList)this.layout.addToContents((LayoutElement)new WorldSelectionList.Builder(this.minecraft, this).width(this.width).height(this.layout.getContentHeight()).filter(this.searchBox.getValue()).oldList(this.list)
/*  74 */         .onEntrySelect(this::updateButtonStatus).onEntryInteract(joinWorld)
/*  75 */         .build());
/*     */     
/*  77 */     createFooterButtons(joinWorld, this.list);
/*     */     
/*  79 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*  80 */     repositionElements();
/*  81 */     updateButtonStatus(null);
/*     */   }
/*     */   
/*     */   private void createFooterButtons(Consumer<WorldSelectionList.WorldListEntry> joinWorld, WorldSelectionList list) {
/*  85 */     GridLayout footer = (GridLayout)this.layout.addToFooter((LayoutElement)new GridLayout().columnSpacing(8).rowSpacing(4));
/*  86 */     footer.defaultCellSetting().alignHorizontallyCenter();
/*  87 */     GridLayout.RowHelper rowHelper = footer.createRowHelper(4);
/*  88 */     this.selectButton = (Button)rowHelper.addChild((LayoutElement)Button.builder(LevelSummary.PLAY_WORLD, button -> list.getSelectedOpt().ifPresent(joinWorld)).build(), 2);
/*  89 */     rowHelper.addChild((LayoutElement)Button.builder((Component)Component.translatable("selectWorld.create"), button -> { Objects.requireNonNull(list); CreateWorldScreen.openFresh(this.minecraft, list::returnToScreen); }).build(), 2);
/*  90 */     this.renameButton = (Button)rowHelper.addChild((LayoutElement)Button.builder((Component)Component.translatable("selectWorld.edit"), button -> list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::editWorld)).width(71).build());
/*  91 */     this.deleteButton = (Button)rowHelper.addChild((LayoutElement)Button.builder((Component)Component.translatable("selectWorld.delete"), button -> list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::deleteWorld)).width(71).build());
/*  92 */     this.copyButton = (Button)rowHelper.addChild((LayoutElement)Button.builder((Component)Component.translatable("selectWorld.recreate"), button -> list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::recreateWorld)).width(71).build());
/*  93 */     rowHelper.addChild((LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> this.minecraft.setScreen(this.lastScreen)).width(71).build());
/*     */   }
/*     */   
/*     */   private Button createDebugWorldRecreateButton() {
/*  97 */     return Button.builder((Component)Component.literal("DEBUG recreate"), button -> {
/*     */           try {
/*     */             String levelName = "DEBUG world";
/*     */             if (this.list != null && !this.list.children().isEmpty()) {
/*     */               WorldSelectionList.Entry entry = this.list.children().getFirst();
/*     */               if (entry instanceof WorldSelectionList.WorldListEntry) {
/*     */                 WorldSelectionList.WorldListEntry worldEntry = (WorldSelectionList.WorldListEntry)entry;
/*     */                 if (worldEntry.getLevelName().equals("DEBUG world"))
/*     */                   worldEntry.doDeleteWorld(); 
/*     */               } 
/*     */             } 
/*     */             LevelSettings levelSettings = new LevelSettings("DEBUG world", GameType.SPECTATOR, false, Difficulty.NORMAL, true, new GameRules(WorldDataConfiguration.DEFAULT.enabledFeatures()), WorldDataConfiguration.DEFAULT);
/*     */             String resultFolder = FileUtil.findAvailableName(this.minecraft.getLevelSource().getBaseDir(), "DEBUG world", "");
/*     */             this.minecraft.createWorldOpenFlows().createFreshLevel(resultFolder, levelSettings, TEST_OPTIONS, WorldPresets::createNormalWorldDimensions, this);
/* 111 */           } catch (IOException e) {
/*     */             LOGGER.error("Failed to recreate the debug world", e);
/*     */           } 
/* 114 */         }).width(72).build();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/* 119 */     if (this.list != null) {
/* 120 */       this.list.updateSize(this.width, this.layout);
/*     */     }
/* 122 */     this.layout.arrangeElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setInitialFocus() {
/* 127 */     if (this.searchBox != null) {
/* 128 */       setInitialFocus((GuiEventListener)this.searchBox);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 134 */     this.minecraft.setScreen(this.lastScreen);
/*     */   }
/*     */   
/*     */   public void updateButtonStatus(LevelSummary summary) {
/* 138 */     if (this.selectButton == null || this.renameButton == null || this.copyButton == null || this.deleteButton == null) {
/*     */       return;
/*     */     }
/* 141 */     if (summary == null) {
/* 142 */       this.selectButton.setMessage(LevelSummary.PLAY_WORLD);
/* 143 */       this.selectButton.active = false;
/* 144 */       this.renameButton.active = false;
/* 145 */       this.copyButton.active = false;
/* 146 */       this.deleteButton.active = false;
/*     */     } else {
/* 148 */       this.selectButton.setMessage(summary.primaryActionMessage());
/* 149 */       this.selectButton.active = summary.primaryActionActive();
/* 150 */       this.renameButton.active = summary.canEdit();
/* 151 */       this.copyButton.active = summary.canRecreate();
/* 152 */       this.deleteButton.active = summary.canDelete();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removed() {
/* 159 */     if (this.list != null)
/* 160 */       this.list.children().forEach(WorldSelectionList.Entry::close); 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/worldselection/SelectWorldScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */