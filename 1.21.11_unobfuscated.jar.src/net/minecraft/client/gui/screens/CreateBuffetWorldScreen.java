/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import com.ibm.icu.text.Collator;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ 
/*     */ public class CreateBuffetWorldScreen extends Screen {
/*  32 */   private static final Component SEARCH_HINT = (Component)Component.translatable("createWorld.customize.buffet.search").withStyle(EditBox.SEARCH_HINT_STYLE);
/*     */   
/*     */   private static final int SPACING = 3;
/*     */   
/*     */   private static final int SEARCH_BOX_HEIGHT = 15;
/*     */   
/*     */   private final HeaderAndFooterLayout layout;
/*     */   private final Screen parent;
/*     */   private final Consumer<Holder<Biome>> applySettings;
/*     */   private final Registry<Biome> biomes;
/*     */   private BiomeList list;
/*     */   private Holder<Biome> biome;
/*     */   private Button doneButton;
/*     */   
/*     */   public CreateBuffetWorldScreen(Screen parent, WorldCreationContext settings, Consumer<Holder<Biome>> applySettings) {
/*  47 */     super((Component)Component.translatable("createWorld.customize.buffet.title"));
/*  48 */     this.parent = parent;
/*  49 */     this.applySettings = applySettings;
/*  50 */     Objects.requireNonNull(this.font); this.layout = new HeaderAndFooterLayout(this, 13 + 9 + 3 + 15, 33);
/*     */     
/*  52 */     this.biomes = settings.worldgenLoadContext().lookupOrThrow(Registries.BIOME);
/*     */ 
/*     */     
/*  55 */     Holder<Biome> defaultBiome = this.biomes.get(Biomes.PLAINS).or(() -> this.biomes.listElements().findAny()).orElseThrow();
/*  56 */     this.biome = settings.selectedDimensions().overworld().getBiomeSource().possibleBiomes().stream().findFirst().orElse(defaultBiome);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/*  61 */     this.minecraft.setScreen(this.parent);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  66 */     LinearLayout header = (LinearLayout)this.layout.addToHeader((LayoutElement)LinearLayout.vertical().spacing(3));
/*  67 */     header.defaultCellSetting().alignHorizontallyCenter();
/*  68 */     header.addChild((LayoutElement)new StringWidget(getTitle(), this.font));
/*  69 */     EditBox search = (EditBox)header.addChild((LayoutElement)new EditBox(this.font, 200, 15, (Component)Component.empty()));
/*  70 */     BiomeList biomeList = new BiomeList();
/*  71 */     search.setHint(SEARCH_HINT);
/*  72 */     Objects.requireNonNull(biomeList); search.setResponder(biomeList::filterEntries);
/*     */     
/*  74 */     this.list = (BiomeList)this.layout.addToContents((LayoutElement)biomeList);
/*     */     
/*  76 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/*  77 */     this.doneButton = (Button)footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> {
/*     */             this.applySettings.accept(this.biome);
/*     */             onClose();
/*  80 */           }).build());
/*  81 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_CANCEL, button -> onClose()).build());
/*     */     
/*  83 */     this.list.setSelected(this.list.children().stream().filter(e -> Objects.equals(e.biome, this.biome)).findFirst().orElse(null));
/*     */     
/*  85 */     this.layout.visitWidgets(this::addRenderableWidget);
/*  86 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/*  91 */     this.layout.arrangeElements();
/*  92 */     this.list.updateSize(this.width, this.layout);
/*     */   }
/*     */   
/*     */   private void updateButtonValidity() {
/*  96 */     this.doneButton.active = (this.list.getSelected() != null);
/*     */   }
/*     */   
/*     */   private class BiomeList extends ObjectSelectionList<BiomeList.Entry> {
/*     */     private BiomeList() {
/* 101 */       super(CreateBuffetWorldScreen.this.minecraft, CreateBuffetWorldScreen.this.width, CreateBuffetWorldScreen.this.layout.getContentHeight(), CreateBuffetWorldScreen.this.layout.getHeaderHeight(), 15);
/* 102 */       filterEntries("");
/*     */     }
/*     */     
/*     */     private void filterEntries(String filter) {
/* 106 */       Collator localeCollator = Collator.getInstance(Locale.getDefault());
/* 107 */       String lowercaseFilter = filter.toLowerCase(Locale.ROOT);
/* 108 */       List<Entry> list = CreateBuffetWorldScreen.this.biomes.listElements()
/* 109 */         .map(x$0 -> new Entry(x$0))
/* 110 */         .sorted(Comparator.comparing(e -> e.name.getString(), (Comparator<?>)localeCollator))
/* 111 */         .filter(entry -> (filter.isEmpty() || entry.name.getString().toLowerCase(Locale.ROOT).contains(lowercaseFilter)))
/* 112 */         .toList();
/*     */       
/* 114 */       replaceEntries(list);
/* 115 */       refreshScrollAmount();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSelected(Entry selected) {
/* 120 */       super.setSelected((AbstractSelectionList.Entry)selected);
/*     */       
/* 122 */       if (selected != null) {
/* 123 */         CreateBuffetWorldScreen.this.biome = (Holder<Biome>)selected.biome;
/*     */       }
/* 125 */       CreateBuffetWorldScreen.this.updateButtonValidity();
/*     */     }
/*     */     
/*     */     private class Entry extends ObjectSelectionList.Entry<Entry> {
/*     */       private final Holder.Reference<Biome> biome;
/*     */       private final Component name;
/*     */       
/*     */       public Entry(Holder.Reference<Biome> biome) {
/* 133 */         this.biome = biome;
/* 134 */         Identifier id = biome.key().identifier();
/*     */         
/* 136 */         String translationKey = id.toLanguageKey("biome");
/* 137 */         if (Language.getInstance().has(translationKey)) {
/* 138 */           this.name = (Component)Component.translatable(translationKey);
/*     */         } else {
/* 140 */           this.name = (Component)Component.literal(id.toString());
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public Component getNarration() {
/* 146 */         return (Component)Component.translatable("narrator.select", new Object[] { this.name });
/*     */       }
/*     */ 
/*     */       
/*     */       public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 151 */         graphics.drawString(CreateBuffetWorldScreen.this.font, this.name, getContentX() + 5, getContentY() + 2, -1);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick)
/*     */       {
/* 158 */         CreateBuffetWorldScreen.BiomeList.this.setSelected(this);
/* 159 */         return super.mouseClicked(event, doubleClick); } } } private class Entry extends ObjectSelectionList.Entry<BiomeList.Entry> { public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) { CreateBuffetWorldScreen.BiomeList.this.setSelected(this); return super.mouseClicked(event, doubleClick); }
/*     */ 
/*     */     
/*     */     private final Holder.Reference<Biome> biome;
/*     */     private final Component name;
/*     */     
/*     */     public Entry(Holder.Reference<Biome> biome) {
/*     */       this.biome = biome;
/*     */       Identifier id = biome.key().identifier();
/*     */       String translationKey = id.toLanguageKey("biome");
/*     */       if (Language.getInstance().has(translationKey)) {
/*     */         this.name = (Component)Component.translatable(translationKey);
/*     */       } else {
/*     */         this.name = (Component)Component.literal(id.toString());
/*     */       } 
/*     */     }
/*     */     
/*     */     public Component getNarration() {
/*     */       return (Component)Component.translatable("narrator.select", new Object[] { this.name });
/*     */     }
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*     */       graphics.drawString(CreateBuffetWorldScreen.this.font, this.name, getContentX() + 5, getContentY() + 2, -1);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/CreateBuffetWorldScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */