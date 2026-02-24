/*     */ package net.minecraft.client.gui.screens;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
/*     */ 
/*     */ public class CreateFlatWorldScreen extends Screen {
/*  30 */   private static final Component TITLE = (Component)Component.translatable("createWorld.customize.flat.title");
/*  31 */   private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot");
/*     */   
/*     */   private static final int SLOT_BG_SIZE = 18;
/*     */   private static final int SLOT_STAT_HEIGHT = 20;
/*     */   private static final int SLOT_BG_X = 1;
/*     */   private static final int SLOT_BG_Y = 1;
/*     */   private static final int SLOT_FG_X = 2;
/*     */   private static final int SLOT_FG_Y = 2;
/*  39 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 33, 64);
/*     */   
/*     */   protected final CreateWorldScreen parent;
/*     */   
/*     */   private final Consumer<FlatLevelGeneratorSettings> applySettings;
/*     */   
/*     */   private FlatLevelGeneratorSettings generator;
/*     */   private DetailsList list;
/*     */   private Button deleteLayerButton;
/*     */   
/*     */   public CreateFlatWorldScreen(CreateWorldScreen parent, Consumer<FlatLevelGeneratorSettings> applySettings, FlatLevelGeneratorSettings generator) {
/*  50 */     super(TITLE);
/*  51 */     this.parent = parent;
/*  52 */     this.applySettings = applySettings;
/*  53 */     this.generator = generator;
/*     */   }
/*     */   
/*     */   public FlatLevelGeneratorSettings settings() {
/*  57 */     return this.generator;
/*     */   }
/*     */   
/*     */   public void setConfig(FlatLevelGeneratorSettings generator) {
/*  61 */     this.generator = generator;
/*  62 */     if (this.list != null) {
/*  63 */       this.list.resetRows();
/*  64 */       updateButtonValidity();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  70 */     this.layout.addTitleHeader(this.title, this.font);
/*     */     
/*  72 */     this.list = (DetailsList)this.layout.addToContents((LayoutElement)new DetailsList());
/*     */     
/*  74 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.vertical().spacing(4));
/*  75 */     footer.defaultCellSetting().alignVerticallyMiddle();
/*  76 */     LinearLayout topFooterButtons = (LinearLayout)footer.addChild((LayoutElement)LinearLayout.horizontal().spacing(8));
/*  77 */     LinearLayout bottomFooterButtons = (LinearLayout)footer.addChild((LayoutElement)LinearLayout.horizontal().spacing(8));
/*     */     
/*  79 */     this.deleteLayerButton = (Button)topFooterButtons.addChild((LayoutElement)Button.builder((Component)Component.translatable("createWorld.customize.flat.removeLayer"), button -> { if (this.list != null) { AbstractSelectionList.Entry patt0$temp = this.list.getSelected(); if (patt0$temp instanceof DetailsList.LayerEntry) {
/*     */                 DetailsList.LayerEntry selectedLayerEntry = (DetailsList.LayerEntry)patt0$temp; this.list.deleteLayer(selectedLayerEntry);
/*     */               }  }
/*     */           
/*  83 */           }).build());
/*     */     
/*  85 */     topFooterButtons.addChild((LayoutElement)Button.builder((Component)Component.translatable("createWorld.customize.presets"), button -> {
/*     */             this.minecraft.setScreen(new PresetFlatWorldScreen(this));
/*     */             this.generator.updateLayers();
/*     */             updateButtonValidity();
/*  89 */           }).build());
/*     */     
/*  91 */     bottomFooterButtons.addChild((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> {
/*     */             this.applySettings.accept(this.generator);
/*     */             onClose();
/*     */             this.generator.updateLayers();
/*  95 */           }).build());
/*     */     
/*  97 */     bottomFooterButtons.addChild((LayoutElement)Button.builder(CommonComponents.GUI_CANCEL, button -> {
/*     */             onClose();
/*     */             this.generator.updateLayers();
/* 100 */           }).build());
/*     */     
/* 102 */     this.generator.updateLayers();
/* 103 */     updateButtonValidity();
/*     */     
/* 105 */     this.layout.visitWidgets(this::addRenderableWidget);
/* 106 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/* 111 */     if (this.list != null) {
/* 112 */       this.list.updateSize(this.width, this.layout);
/*     */     }
/* 114 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   private void updateButtonValidity() {
/* 118 */     if (this.deleteLayerButton != null) {
/* 119 */       this.deleteLayerButton.active = hasValidSelection();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean hasValidSelection() {
/* 124 */     return (this.list != null && this.list.getSelected() instanceof DetailsList.LayerEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 129 */     this.minecraft.setScreen((Screen)this.parent);
/*     */   }
/*     */   
/*     */   private class DetailsList extends ObjectSelectionList<DetailsList.Entry> {
/* 133 */     private static final Component LAYER_MATERIAL_TITLE = (Component)Component.translatable("createWorld.customize.flat.tile").withStyle(ChatFormatting.UNDERLINE);
/* 134 */     private static final Component HEIGHT_TITLE = (Component)Component.translatable("createWorld.customize.flat.height").withStyle(ChatFormatting.UNDERLINE);
/*     */     
/*     */     public DetailsList() {
/* 137 */       super(CreateFlatWorldScreen.this.minecraft, CreateFlatWorldScreen.this.width, CreateFlatWorldScreen.this.height - 103, 43, 24);
/*     */       
/* 139 */       populateList();
/*     */     }
/*     */     
/*     */     private void populateList() {
/* 143 */       Objects.requireNonNull(CreateFlatWorldScreen.this.font); addEntry((AbstractSelectionList.Entry)new HeaderEntry(CreateFlatWorldScreen.this.font), (int)(9.0D * 1.5D));
/* 144 */       List<FlatLayerInfo> layersInfo = CreateFlatWorldScreen.this.generator.getLayersInfo().reversed();
/* 145 */       for (int i = 0; i < layersInfo.size(); i++) {
/* 146 */         addEntry((AbstractSelectionList.Entry)new LayerEntry(layersInfo.get(i), i));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSelected(Entry selected) {
/* 152 */       super.setSelected((AbstractSelectionList.Entry)selected);
/* 153 */       CreateFlatWorldScreen.this.updateButtonValidity();
/*     */     }
/*     */     
/*     */     public void resetRows() {
/* 157 */       int index = children().indexOf(getSelected());
/* 158 */       clearEntries();
/* 159 */       populateList();
/*     */       
/* 161 */       List<Entry> children = children();
/* 162 */       if (index >= 0 && index < children.size()) {
/* 163 */         setSelected(children.get(index));
/*     */       }
/*     */     }
/*     */     
/*     */     private void deleteLayer(LayerEntry selectedLayerEntry) {
/* 168 */       List<FlatLayerInfo> layersInfo = CreateFlatWorldScreen.this.generator.getLayersInfo();
/* 169 */       int deletedLayerIndex = children().indexOf(selectedLayerEntry);
/* 170 */       removeEntry((AbstractSelectionList.Entry)selectedLayerEntry);
/* 171 */       layersInfo.remove(selectedLayerEntry.layerInfo);
/* 172 */       setSelected(layersInfo.isEmpty() ? null : children().get(Math.min(deletedLayerIndex, layersInfo.size())));
/* 173 */       CreateFlatWorldScreen.this.generator.updateLayers();
/* 174 */       resetRows();
/* 175 */       CreateFlatWorldScreen.this.updateButtonValidity();
/*     */     }
/*     */     
/*     */     private static abstract class Entry
/*     */       extends ObjectSelectionList.Entry<Entry> {}
/*     */     
/*     */     private class LayerEntry extends Entry {
/*     */       private final FlatLayerInfo layerInfo;
/*     */       private final int index;
/*     */       
/*     */       public LayerEntry(FlatLayerInfo layerInfo, int index) {
/* 186 */         this.layerInfo = layerInfo;
/* 187 */         this.index = index;
/*     */       }
/*     */       
/*     */       public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*     */         MutableComponent mutableComponent;
/* 192 */         BlockState blockState = this.layerInfo.getBlockState();
/*     */         
/* 194 */         ItemStack itemStack = getDisplayItem(blockState);
/* 195 */         blitSlot(graphics, getContentX(), getContentY(), itemStack);
/*     */         
/* 197 */         Objects.requireNonNull(CreateFlatWorldScreen.this.font); int y = getContentYMiddle() - 9 / 2;
/* 198 */         graphics.drawString(CreateFlatWorldScreen.this.font, itemStack.getHoverName(), getContentX() + 18 + 5, y, -1);
/*     */ 
/*     */         
/* 201 */         if (this.index == 0) {
/* 202 */           mutableComponent = Component.translatable("createWorld.customize.flat.layer.top", new Object[] { this.layerInfo.getHeight() });
/* 203 */         } else if (this.index == CreateFlatWorldScreen.this.generator.getLayersInfo().size() - 1) {
/* 204 */           mutableComponent = Component.translatable("createWorld.customize.flat.layer.bottom", new Object[] { this.layerInfo.getHeight() });
/*     */         } else {
/* 206 */           mutableComponent = Component.translatable("createWorld.customize.flat.layer", new Object[] { this.layerInfo.getHeight() });
/*     */         } 
/*     */         
/* 209 */         graphics.drawString(CreateFlatWorldScreen.this.font, (Component)mutableComponent, getContentRight() - CreateFlatWorldScreen.this.font.width((FormattedText)mutableComponent), y, -1);
/*     */       }
/*     */       
/*     */       private ItemStack getDisplayItem(BlockState blockState) {
/* 213 */         Item item = blockState.getBlock().asItem();
/* 214 */         if (item == Items.AIR) {
/* 215 */           if (blockState.is(Blocks.WATER)) {
/* 216 */             item = Items.WATER_BUCKET;
/* 217 */           } else if (blockState.is(Blocks.LAVA)) {
/* 218 */             item = Items.LAVA_BUCKET;
/*     */           } 
/*     */         }
/* 221 */         return new ItemStack((net.minecraft.world.level.ItemLike)item);
/*     */       }
/*     */ 
/*     */       
/*     */       public Component getNarration() {
/* 226 */         ItemStack itemStack = getDisplayItem(this.layerInfo.getBlockState());
/* 227 */         if (!itemStack.isEmpty()) {
/* 228 */           return (Component)CommonComponents.joinForNarration(new Component[] {
/* 229 */                 (Component)Component.translatable("narrator.select", new Object[] { itemStack.getHoverName() }), CreateFlatWorldScreen.DetailsList.HEIGHT_TITLE, 
/*     */                 
/* 231 */                 (Component)Component.literal(String.valueOf(this.layerInfo.getHeight()))
/*     */               });
/*     */         }
/* 234 */         return CommonComponents.EMPTY;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 240 */         CreateFlatWorldScreen.DetailsList.this.setSelected(this);
/* 241 */         return super.mouseClicked(event, doubleClick);
/*     */       }
/*     */       
/*     */       private void blitSlot(GuiGraphics graphics, int x, int y, ItemStack itemStack) {
/* 245 */         blitSlotBg(graphics, x + 1, y + 1);
/*     */         
/* 247 */         if (!itemStack.isEmpty()) {
/* 248 */           graphics.renderFakeItem(itemStack, x + 2, y + 2);
/*     */         }
/*     */       }
/*     */       
/*     */       private void blitSlotBg(GuiGraphics graphics, int x, int y) {
/* 253 */         graphics.blitSprite(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, CreateFlatWorldScreen.SLOT_SPRITE, x, y, 18, 18);
/*     */       }
/*     */     }
/*     */     
/*     */     private static class HeaderEntry extends Entry {
/*     */       private final Font font;
/*     */       
/*     */       public HeaderEntry(Font font) {
/* 261 */         this.font = font;
/*     */       }
/*     */ 
/*     */       
/*     */       public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 266 */         graphics.drawString(this.font, CreateFlatWorldScreen.DetailsList.LAYER_MATERIAL_TITLE, getContentX(), getContentY(), -1);
/* 267 */         graphics.drawString(this.font, CreateFlatWorldScreen.DetailsList.HEIGHT_TITLE, getContentRight() - this.font.width((FormattedText)CreateFlatWorldScreen.DetailsList.HEIGHT_TITLE), getContentY(), -1);
/*     */       }
/*     */       
/*     */       public Component getNarration()
/*     */       {
/* 272 */         return (Component)CommonComponents.joinForNarration(new Component[] { CreateFlatWorldScreen.DetailsList.LAYER_MATERIAL_TITLE, CreateFlatWorldScreen.DetailsList.HEIGHT_TITLE }); } } } private static abstract class Entry extends ObjectSelectionList.Entry<DetailsList.Entry> {} private class LayerEntry extends DetailsList.Entry { private final FlatLayerInfo layerInfo; private final int index; public LayerEntry(FlatLayerInfo layerInfo, int index) { this.layerInfo = layerInfo; this.index = index; } public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) { MutableComponent mutableComponent; BlockState blockState = this.layerInfo.getBlockState(); ItemStack itemStack = getDisplayItem(blockState); blitSlot(graphics, getContentX(), getContentY(), itemStack); Objects.requireNonNull(CreateFlatWorldScreen.this.font); int y = getContentYMiddle() - 9 / 2; graphics.drawString(CreateFlatWorldScreen.this.font, itemStack.getHoverName(), getContentX() + 18 + 5, y, -1); if (this.index == 0) { mutableComponent = Component.translatable("createWorld.customize.flat.layer.top", new Object[] { this.layerInfo.getHeight() }); } else if (this.index == CreateFlatWorldScreen.this.generator.getLayersInfo().size() - 1) { mutableComponent = Component.translatable("createWorld.customize.flat.layer.bottom", new Object[] { this.layerInfo.getHeight() }); } else { mutableComponent = Component.translatable("createWorld.customize.flat.layer", new Object[] { this.layerInfo.getHeight() }); }  graphics.drawString(CreateFlatWorldScreen.this.font, (Component)mutableComponent, getContentRight() - CreateFlatWorldScreen.this.font.width((FormattedText)mutableComponent), y, -1); } private ItemStack getDisplayItem(BlockState blockState) { Item item = blockState.getBlock().asItem(); if (item == Items.AIR) if (blockState.is(Blocks.WATER)) { item = Items.WATER_BUCKET; } else if (blockState.is(Blocks.LAVA)) { item = Items.LAVA_BUCKET; }   return new ItemStack((net.minecraft.world.level.ItemLike)item); } public Component getNarration() { ItemStack itemStack = getDisplayItem(this.layerInfo.getBlockState()); if (!itemStack.isEmpty()) return (Component)CommonComponents.joinForNarration(new Component[] { (Component)Component.translatable("narrator.select", new Object[] { itemStack.getHoverName() }), CreateFlatWorldScreen.DetailsList.HEIGHT_TITLE, (Component)Component.literal(String.valueOf(this.layerInfo.getHeight())) });  return CommonComponents.EMPTY; } public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) { CreateFlatWorldScreen.DetailsList.this.setSelected(this); return super.mouseClicked(event, doubleClick); } private void blitSlot(GuiGraphics graphics, int x, int y, ItemStack itemStack) { blitSlotBg(graphics, x + 1, y + 1); if (!itemStack.isEmpty()) graphics.renderFakeItem(itemStack, x + 2, y + 2);  } private void blitSlotBg(GuiGraphics graphics, int x, int y) { graphics.blitSprite(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, CreateFlatWorldScreen.SLOT_SPRITE, x, y, 18, 18); } } private static class HeaderEntry extends DetailsList.Entry { public Component getNarration() { return (Component)CommonComponents.joinForNarration(new Component[] { CreateFlatWorldScreen.DetailsList.LAYER_MATERIAL_TITLE, CreateFlatWorldScreen.DetailsList.HEIGHT_TITLE }); }
/*     */ 
/*     */     
/*     */     private final Font font;
/*     */     
/*     */     public HeaderEntry(Font font) {
/*     */       this.font = font;
/*     */     }
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*     */       graphics.drawString(this.font, CreateFlatWorldScreen.DetailsList.LAYER_MATERIAL_TITLE, getContentX(), getContentY(), -1);
/*     */       graphics.drawString(this.font, CreateFlatWorldScreen.DetailsList.HEIGHT_TITLE, getContentRight() - this.font.width((FormattedText)CreateFlatWorldScreen.DetailsList.HEIGHT_TITLE), getContentY(), -1);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/CreateFlatWorldScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */