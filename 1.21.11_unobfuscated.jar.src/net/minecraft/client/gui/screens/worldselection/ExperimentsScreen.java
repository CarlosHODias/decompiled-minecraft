/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*     */ import net.minecraft.client.gui.components.ScrollableLayout;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.Layout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.resources.language.I18n;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.packs.repository.PackSource;
/*     */ 
/*     */ public class ExperimentsScreen extends Screen {
/*  29 */   private static final Component TITLE = (Component)Component.translatable("selectWorld.experiments");
/*  30 */   private static final Component INFO = (Component)Component.translatable("selectWorld.experiments.info").withStyle(ChatFormatting.RED);
/*     */   private static final int MAIN_CONTENT_WIDTH = 310;
/*     */   private static final int SCROLL_AREA_MIN_HEIGHT = 130;
/*  33 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*     */   private final Screen parent;
/*     */   private final PackRepository packRepository;
/*     */   private final Consumer<PackRepository> output;
/*  37 */   private final Object2BooleanMap<Pack> packs = (Object2BooleanMap<Pack>)new Object2BooleanLinkedOpenHashMap();
/*     */   private ScrollableLayout scrollArea;
/*     */   
/*     */   public ExperimentsScreen(Screen parent, PackRepository packRepository, Consumer<PackRepository> output) {
/*  41 */     super(TITLE);
/*  42 */     this.parent = parent;
/*  43 */     this.packRepository = packRepository;
/*  44 */     this.output = output;
/*  45 */     for (Pack pack : (Iterable<Pack>)packRepository.getAvailablePacks()) {
/*  46 */       if (pack.getPackSource() == PackSource.FEATURE) {
/*  47 */         this.packs.put(pack, packRepository.getSelectedPacks().contains(pack));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  54 */     this.layout.addTitleHeader(TITLE, this.font);
/*     */     
/*  56 */     LinearLayout content = (LinearLayout)this.layout.addToContents((LayoutElement)LinearLayout.vertical());
/*  57 */     content.addChild((LayoutElement)new MultiLineTextWidget(INFO, this.font).setMaxWidth(310), s -> s.paddingBottom(15));
/*     */     
/*  59 */     SwitchGrid.Builder switchGridBuilder = SwitchGrid.builder(299).withInfoUnderneath(2, true).withRowSpacing(4);
/*  60 */     this.packs.forEach((pack, selected) -> switchGridBuilder.addSwitch(getHumanReadableTitle(switchGridBuilder), (), ()).withInfo(switchGridBuilder.getDescription()));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     Layout switchGridLayout = switchGridBuilder.build().layout();
/*  66 */     this.scrollArea = new ScrollableLayout(this.minecraft, switchGridLayout, 130);
/*  67 */     this.scrollArea.setMinWidth(310);
/*  68 */     content.addChild((LayoutElement)this.scrollArea);
/*     */     
/*  70 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/*  71 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onDone()).build());
/*  72 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_CANCEL, button -> onClose()).build());
/*     */     
/*  74 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*  75 */     repositionElements();
/*     */   }
/*     */   
/*     */   private static Component getHumanReadableTitle(Pack pack) {
/*  79 */     String translationKey = "dataPack." + pack.getId() + ".name";
/*  80 */     return I18n.exists(translationKey) ? (Component)Component.translatable(translationKey) : pack.getTitle();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/*  86 */     this.scrollArea.setMaxHeight(130);
/*  87 */     this.layout.arrangeElements();
/*     */     
/*  89 */     int availableExtraHeight = this.height - this.layout.getFooterHeight() - this.scrollArea.getRectangle().bottom();
/*  90 */     this.scrollArea.setMaxHeight(this.scrollArea.getHeight() + availableExtraHeight);
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getNarrationMessage() {
/*  95 */     return (Component)CommonComponents.joinForNarration(new Component[] { super.getNarrationMessage(), INFO });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 100 */     this.minecraft.setScreen(this.parent);
/*     */   }
/*     */   
/*     */   private void onDone() {
/* 104 */     List<Pack> selectedPacks = new ArrayList<>(this.packRepository.getSelectedPacks());
/* 105 */     List<Pack> selectedFeatures = new ArrayList<>();
/* 106 */     this.packs.forEach((pack, selected) -> {
/*     */           selectedPacks.remove(pack);
/*     */           if (selected) {
/*     */             selectedFeatures.add(pack);
/*     */           }
/*     */         });
/* 112 */     selectedPacks.addAll(Lists.reverse(selectedFeatures));
/* 113 */     this.packRepository.setSelected(selectedPacks.stream().map(Pack::getId).toList());
/* 114 */     this.output.accept(this.packRepository);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/worldselection/ExperimentsScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */