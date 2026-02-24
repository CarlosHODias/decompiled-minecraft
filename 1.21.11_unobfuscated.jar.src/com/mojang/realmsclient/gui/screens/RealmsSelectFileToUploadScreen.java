/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ import com.mojang.realmsclient.util.task.RealmCreationTask;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.realms.RealmsScreen;
/*     */ import net.minecraft.world.level.storage.LevelSummary;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RealmsSelectFileToUploadScreen extends RealmsScreen {
/*  20 */   private static final Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*     */   
/*  22 */   public static final Component TITLE = (Component)Component.translatable("mco.upload.select.world.title");
/*  23 */   private static final Component UNABLE_TO_LOAD_WORLD = (Component)Component.translatable("selectWorld.unable_to_load");
/*     */   
/*     */   private final RealmCreationTask realmCreationTask;
/*     */   
/*     */   private final RealmsResetWorldScreen lastScreen;
/*     */   
/*     */   private final long realmId;
/*     */   
/*     */   private final int slotId;
/*     */   
/*     */   private final HeaderAndFooterLayout layout;
/*     */   
/*     */   protected EditBox searchBox;
/*     */   private WorldSelectionList list;
/*     */   private Button uploadButton;
/*     */   
/*     */   public RealmsSelectFileToUploadScreen(RealmCreationTask realmCreationTask, long realmId, int slotId, RealmsResetWorldScreen lastScreen) {
/*  40 */     super(TITLE); java.util.Objects.requireNonNull((Minecraft.getInstance()).font); this.layout = new HeaderAndFooterLayout((Screen)this, 8 + 9 + 8 + 20 + 4, 33);
/*  41 */     this.realmCreationTask = realmCreationTask;
/*  42 */     this.lastScreen = lastScreen;
/*  43 */     this.realmId = realmId;
/*  44 */     this.slotId = slotId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  49 */     LinearLayout header = (LinearLayout)this.layout.addToHeader((LayoutElement)LinearLayout.vertical().spacing(4));
/*  50 */     header.defaultCellSetting().alignHorizontallyCenter();
/*  51 */     header.addChild((LayoutElement)new net.minecraft.client.gui.components.StringWidget(this.title, this.font));
/*  52 */     this.searchBox = (EditBox)header.addChild((LayoutElement)new EditBox(this.font, this.width / 2 - 100, 22, 200, 20, this.searchBox, (Component)Component.translatable("selectWorld.search")));
/*  53 */     this.searchBox.setResponder(value -> {
/*     */           if (this.list != null) {
/*     */             this.list.updateFilter(value);
/*     */           }
/*     */         });
/*     */     
/*     */     try {
/*  60 */       this.list = (WorldSelectionList)this.layout.addToContents((LayoutElement)new WorldSelectionList.Builder(this.minecraft, (Screen)this)
/*  61 */           .width(this.width).height(this.layout.getContentHeight()).filter(this.searchBox.getValue()).oldList(this.list).uploadWorld()
/*  62 */           .onEntrySelect(this::updateButtonState).onEntryInteract(this::upload)
/*  63 */           .build());
/*  64 */     } catch (Exception e) {
/*  65 */       LOGGER.error("Couldn't load level list", e);
/*  66 */       this.minecraft.setScreen((Screen)new RealmsGenericErrorScreen(UNABLE_TO_LOAD_WORLD, Component.nullToEmpty(e.getMessage()), (Screen)this.lastScreen));
/*     */       
/*     */       return;
/*     */     } 
/*  70 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/*  71 */     footer.defaultCellSetting().alignHorizontallyCenter();
/*  72 */     this.uploadButton = (Button)footer.addChild((LayoutElement)Button.builder((Component)Component.translatable("mco.upload.button.name"), button -> this.list.getSelectedOpt().ifPresent(this::upload)).build());
/*     */     
/*  74 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> onClose()).build());
/*     */     
/*  76 */     updateButtonState(null);
/*  77 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*  78 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/*  83 */     if (this.list != null) {
/*  84 */       this.list.updateSize(this.width, this.layout);
/*     */     }
/*  86 */     this.layout.arrangeElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setInitialFocus() {
/*  91 */     setInitialFocus((GuiEventListener)this.searchBox);
/*     */   }
/*     */   
/*     */   private void updateButtonState(LevelSummary ignored) {
/*  95 */     if (this.list != null && this.uploadButton != null) {
/*  96 */       this.uploadButton.active = (this.list.getSelected() != null);
/*     */     }
/*     */   }
/*     */   
/*     */   private void upload(WorldSelectionList.WorldListEntry worldListEntry) {
/* 101 */     this.minecraft.setScreen((Screen)new RealmsUploadScreen(this.realmCreationTask, this.realmId, this.slotId, this.lastScreen, worldListEntry.getLevelSummary()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getNarrationMessage() {
/* 106 */     return (Component)CommonComponents.joinForNarration(new Component[] { getTitle(), createLabelNarration() });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 111 */     this.minecraft.setScreen((Screen)this.lastScreen);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/RealmsSelectFileToUploadScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */