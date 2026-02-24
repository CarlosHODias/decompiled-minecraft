/*     */ package net.minecraft.client.gui.screens;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.FlatLevelGeneratorPresetTags;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PresetFlatWorldScreen extends Screen {
/*  47 */   private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot");
/*  48 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int SLOT_BG_SIZE = 18;
/*     */   
/*     */   private static final int SLOT_STAT_HEIGHT = 20;
/*     */   private static final int SLOT_BG_X = 1;
/*     */   private static final int SLOT_BG_Y = 1;
/*     */   private static final int SLOT_FG_X = 2;
/*     */   private static final int SLOT_FG_Y = 2;
/*  57 */   private static final ResourceKey<Biome> DEFAULT_BIOME = Biomes.PLAINS;
/*  58 */   public static final Component UNKNOWN_PRESET = (Component)Component.translatable("flat_world_preset.unknown");
/*     */   private final CreateFlatWorldScreen parent;
/*     */   private Component shareText;
/*     */   private Component listText;
/*     */   private PresetsList list;
/*     */   private Button selectButton;
/*     */   private EditBox export;
/*     */   private FlatLevelGeneratorSettings settings;
/*     */   
/*     */   public PresetFlatWorldScreen(CreateFlatWorldScreen parent)
/*     */   {
/*  69 */     super((Component)Component.translatable("createWorld.customize.presets.title"));
/*  70 */     this.parent = parent; } private static FlatLayerInfo getLayerInfoFromString(HolderGetter<Block> blocks, String input, int firstFree) {
/*     */     int height;
/*     */     String blockId;
/*     */     Optional<Holder.Reference<Block>> block;
/*  74 */     List<String> parts = Splitter.on('*').limit(2).splitToList(input);
/*     */ 
/*     */ 
/*     */     
/*  78 */     if (parts.size() == 2) {
/*  79 */       blockId = parts.get(1);
/*     */       try {
/*  81 */         height = Math.max(Integer.parseInt(parts.get(0)), 0);
/*  82 */       } catch (NumberFormatException e) {
/*  83 */         LOGGER.error("Error while parsing flat world string", e);
/*  84 */         return null;
/*     */       } 
/*     */     } else {
/*     */       
/*  88 */       blockId = parts.get(0);
/*  89 */       height = 1;
/*     */     } 
/*     */     
/*  92 */     int firstAbove = Math.min(firstFree + height, DimensionType.Y_SIZE);
/*  93 */     int actualHeight = firstAbove - firstFree;
/*     */ 
/*     */     
/*     */     try {
/*  97 */       block = blocks.get(ResourceKey.create(Registries.BLOCK, Identifier.parse(blockId)));
/*  98 */     } catch (Exception e) {
/*  99 */       LOGGER.error("Error while parsing flat world string", e);
/* 100 */       return null;
/*     */     } 
/*     */     
/* 103 */     if (block.isEmpty()) {
/* 104 */       LOGGER.error("Error while parsing flat world string => Unknown block, {}", blockId);
/* 105 */       return null;
/*     */     } 
/*     */     
/* 108 */     return new FlatLayerInfo(actualHeight, (Block)((Holder.Reference)block.get()).value());
/*     */   }
/*     */   
/*     */   private static List<FlatLayerInfo> getLayersInfoFromString(HolderGetter<Block> blocks, String input) {
/* 112 */     List<FlatLayerInfo> result = Lists.newArrayList();
/* 113 */     String[] depths = input.split(",");
/* 114 */     int firstFree = 0;
/*     */     
/* 116 */     for (String depth : depths) {
/* 117 */       FlatLayerInfo layer = getLayerInfoFromString(blocks, depth, firstFree);
/* 118 */       if (layer == null) {
/* 119 */         return Collections.emptyList();
/*     */       }
/* 121 */       int maxHeight = DimensionType.Y_SIZE - firstFree;
/* 122 */       if (maxHeight > 0) {
/*     */ 
/*     */         
/* 125 */         result.add(layer.heightLimited(maxHeight));
/* 126 */         firstFree += layer.getHeight();
/*     */       } 
/*     */     } 
/* 129 */     return result;
/*     */   }
/*     */   public static FlatLevelGeneratorSettings fromString(HolderGetter<Block> blocks, HolderGetter<Biome> biomes, HolderGetter<StructureSet> structureSets, HolderGetter<PlacedFeature> placedFeatures, String definition, FlatLevelGeneratorSettings settings) {
/*     */     Holder holder;
/* 133 */     Iterator<String> parts = Splitter.on(';').split(definition).iterator();
/* 134 */     if (!parts.hasNext()) {
/* 135 */       return FlatLevelGeneratorSettings.getDefault(biomes, structureSets, placedFeatures);
/*     */     }
/*     */     
/* 138 */     List<FlatLayerInfo> layers = getLayersInfoFromString(blocks, parts.next());
/*     */     
/* 140 */     if (layers.isEmpty()) {
/* 141 */       return FlatLevelGeneratorSettings.getDefault(biomes, structureSets, placedFeatures);
/*     */     }
/*     */     
/* 144 */     Holder.Reference<Biome> defaultBiome = biomes.getOrThrow(DEFAULT_BIOME);
/* 145 */     Holder.Reference<Biome> reference1 = defaultBiome;
/* 146 */     if (parts.hasNext()) {
/* 147 */       String biomeName = parts.next();
/*     */ 
/*     */       
/* 150 */       Objects.requireNonNull(biomes);
/* 151 */       holder = Optional.<Identifier>ofNullable(Identifier.tryParse(biomeName)).map(id -> ResourceKey.create(Registries.BIOME, id)).flatMap(biomes::get).orElseGet(() -> {
/*     */             LOGGER.warn("Invalid biome: {}", biomeName);
/*     */             
/*     */             return defaultBiome;
/*     */           });
/*     */     } 
/* 157 */     return settings.withBiomeAndLayers(layers, settings.structureOverrides(), holder);
/*     */   }
/*     */   
/*     */   private static String save(FlatLevelGeneratorSettings settings) {
/* 161 */     StringBuilder builder = new StringBuilder();
/*     */     
/* 163 */     for (int i = 0; i < settings.getLayersInfo().size(); i++) {
/* 164 */       if (i > 0) {
/* 165 */         builder.append(",");
/*     */       }
/* 167 */       builder.append(settings.getLayersInfo().get(i));
/*     */     } 
/*     */     
/* 170 */     builder.append(";");
/* 171 */     builder.append(settings.getBiome().unwrapKey().map(ResourceKey::identifier).orElseThrow(() -> new IllegalStateException("Biome not registered")));
/*     */     
/* 173 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/* 178 */     this.shareText = (Component)Component.translatable("createWorld.customize.presets.share");
/* 179 */     this.listText = (Component)Component.translatable("createWorld.customize.presets.list");
/*     */     
/* 181 */     this.export = new EditBox(this.font, 50, 40, this.width - 100, 20, this.shareText);
/* 182 */     this.export.setMaxLength(1230);
/* 183 */     WorldCreationContext worldCreatingContext = this.parent.parent.getUiState().getSettings();
/* 184 */     RegistryAccess.Frozen frozen = worldCreatingContext.worldgenLoadContext();
/* 185 */     FeatureFlagSet enabledFeatures = worldCreatingContext.dataConfiguration().enabledFeatures();
/*     */     
/* 187 */     Registry registry1 = frozen.lookupOrThrow(Registries.BIOME);
/* 188 */     Registry registry2 = frozen.lookupOrThrow(Registries.STRUCTURE_SET);
/* 189 */     Registry registry3 = frozen.lookupOrThrow(Registries.PLACED_FEATURE);
/* 190 */     HolderLookup.RegistryLookup registryLookup = frozen.lookupOrThrow(Registries.BLOCK).filterFeatures(enabledFeatures);
/* 191 */     this.export.setValue(save(this.parent.settings()));
/* 192 */     this.settings = this.parent.settings();
/* 193 */     addWidget(this.export);
/*     */     
/* 195 */     this.list = addRenderableWidget(new PresetsList((RegistryAccess)frozen, enabledFeatures));
/*     */     
/* 197 */     this.selectButton = addRenderableWidget(Button.builder((Component)Component.translatable("createWorld.customize.presets.select"), button -> {
/*     */             FlatLevelGeneratorSettings generator = fromString(blocks, biomes, structureSets, placedFeatures, this.export.getValue(), this.settings);
/*     */             this.parent.setConfig(generator);
/*     */             this.minecraft.setScreen(this.parent);
/* 201 */           }).bounds(this.width / 2 - 155, this.height - 28, 150, 20).build());
/* 202 */     addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> this.minecraft.setScreen(this.parent)).bounds(this.width / 2 + 5, this.height - 28, 150, 20).build());
/*     */     
/* 204 */     updateButtonValidity((this.list.getSelected() != null));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/* 210 */     return this.list.mouseScrolled(x, y, scrollX, scrollY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resize(int width, int height) {
/* 215 */     String oldEdit = this.export.getValue();
/* 216 */     init(width, height);
/* 217 */     this.export.setValue(oldEdit);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 222 */     this.minecraft.setScreen(this.parent);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 227 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/* 229 */     graphics.drawCenteredString(this.font, this.title, this.width / 2, 8, -1);
/* 230 */     graphics.drawString(this.font, this.shareText, 51, 30, -6250336);
/* 231 */     graphics.drawString(this.font, this.listText, 51, 68, -6250336);
/*     */     
/* 233 */     this.export.render(graphics, mouseX, mouseY, a);
/*     */   }
/*     */   
/*     */   public void updateButtonValidity(boolean hasSelected) {
/* 237 */     this.selectButton.active = (hasSelected || this.export.getValue().length() > 1);
/*     */   }
/*     */   
/*     */   private class PresetsList extends ObjectSelectionList<PresetsList.Entry> {
/*     */     public PresetsList(RegistryAccess access, FeatureFlagSet enabledFeatures) {
/* 242 */       super(PresetFlatWorldScreen.this.minecraft, PresetFlatWorldScreen.this.width, PresetFlatWorldScreen.this.height - 117, 80, 24);
/* 243 */       for (Holder<FlatLevelGeneratorPreset> preset : (Iterable<Holder<FlatLevelGeneratorPreset>>)access.lookupOrThrow(Registries.FLAT_LEVEL_GENERATOR_PRESET).getTagOrEmpty(FlatLevelGeneratorPresetTags.VISIBLE)) {
/* 244 */         Set<Block> disabledBlocks = (Set<Block>)((FlatLevelGeneratorPreset)preset.value()).settings().getLayersInfo().stream().map(p -> p.getBlockState().getBlock()).filter(b -> !b.isEnabled(enabledFeatures)).collect(Collectors.toSet());
/* 245 */         if (!disabledBlocks.isEmpty()) {
/* 246 */           PresetFlatWorldScreen.LOGGER.info("Discarding flat world preset {} since it contains experimental blocks {}", preset.unwrapKey().map(e -> e.identifier().toString()).orElse("<unknown>"), disabledBlocks); continue;
/*     */         } 
/* 248 */         addEntry((AbstractSelectionList.Entry)new Entry(preset));
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setSelected(Entry selected) {
/* 255 */       super.setSelected((AbstractSelectionList.Entry)selected);
/* 256 */       PresetFlatWorldScreen.this.updateButtonValidity((selected != null));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean keyPressed(KeyEvent event) {
/* 261 */       if (super.keyPressed(event)) {
/* 262 */         return true;
/*     */       }
/* 264 */       if (event.isSelection() && 
/* 265 */         getSelected() != null) {
/* 266 */         ((Entry)getSelected()).select();
/*     */       }
/*     */       
/* 269 */       return false;
/*     */     }
/*     */     
/*     */     public class Entry extends ObjectSelectionList.Entry<Entry> {
/* 273 */       private static final Identifier STATS_ICON_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/stats_icons.png");
/*     */       
/*     */       private final FlatLevelGeneratorPreset preset;
/*     */       private final Component name;
/*     */       
/*     */       public Entry(Holder<FlatLevelGeneratorPreset> preset) {
/* 279 */         this.preset = (FlatLevelGeneratorPreset)preset.value();
/* 280 */         this
/*     */           
/* 282 */           .name = preset.unwrapKey().map(key -> Component.translatable(key.identifier().toLanguageKey("flat_world_preset"))).orElse(PresetFlatWorldScreen.UNKNOWN_PRESET);
/*     */       }
/*     */ 
/*     */       
/*     */       public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 287 */         blitSlot(graphics, getContentX(), getContentY(), (Item)this.preset.displayItem().value());
/* 288 */         graphics.drawString(PresetFlatWorldScreen.this.font, this.name, getContentX() + 18 + 5, getContentY() + 6, -1);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 293 */         select();
/* 294 */         return super.mouseClicked(event, doubleClick);
/*     */       }
/*     */       
/*     */       private void select() {
/* 298 */         PresetFlatWorldScreen.PresetsList.this.setSelected(this);
/* 299 */         PresetFlatWorldScreen.this.settings = this.preset.settings();
/* 300 */         PresetFlatWorldScreen.this.export.setValue(PresetFlatWorldScreen.save(PresetFlatWorldScreen.this.settings));
/* 301 */         PresetFlatWorldScreen.this.export.moveCursorToStart(false);
/*     */       }
/*     */       
/*     */       private void blitSlot(GuiGraphics graphics, int x, int y, Item item) {
/* 305 */         blitSlotBg(graphics, x + 1, y + 1);
/*     */         
/* 307 */         graphics.renderFakeItem(new ItemStack((net.minecraft.world.level.ItemLike)item), x + 2, y + 2);
/*     */       }
/*     */       
/*     */       private void blitSlotBg(GuiGraphics graphics, int x, int y) {
/* 311 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PresetFlatWorldScreen.SLOT_SPRITE, x, y, 18, 18);
/*     */       }
/*     */       
/*     */       public Component getNarration()
/*     */       {
/* 316 */         return (Component)Component.translatable("narrator.select", new Object[] { this.name }); } } } public class Entry extends ObjectSelectionList.Entry<PresetsList.Entry> { public Component getNarration() { return (Component)Component.translatable("narrator.select", new Object[] { this.name }); }
/*     */ 
/*     */     
/*     */     private static final Identifier STATS_ICON_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/stats_icons.png");
/*     */     private final FlatLevelGeneratorPreset preset;
/*     */     private final Component name;
/*     */     
/*     */     public Entry(Holder<FlatLevelGeneratorPreset> preset) {
/*     */       this.preset = (FlatLevelGeneratorPreset)preset.value();
/*     */       this.name = preset.unwrapKey().map(key -> Component.translatable(key.identifier().toLanguageKey("flat_world_preset"))).orElse(PresetFlatWorldScreen.UNKNOWN_PRESET);
/*     */     }
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*     */       blitSlot(graphics, getContentX(), getContentY(), (Item)this.preset.displayItem().value());
/*     */       graphics.drawString(PresetFlatWorldScreen.this.font, this.name, getContentX() + 18 + 5, getContentY() + 6, -1);
/*     */     }
/*     */     
/*     */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/*     */       select();
/*     */       return super.mouseClicked(event, doubleClick);
/*     */     }
/*     */     
/*     */     private void select() {
/*     */       PresetFlatWorldScreen.PresetsList.this.setSelected(this);
/*     */       PresetFlatWorldScreen.this.settings = this.preset.settings();
/*     */       PresetFlatWorldScreen.this.export.setValue(PresetFlatWorldScreen.save(PresetFlatWorldScreen.this.settings));
/*     */       PresetFlatWorldScreen.this.export.moveCursorToStart(false);
/*     */     }
/*     */     
/*     */     private void blitSlot(GuiGraphics graphics, int x, int y, Item item) {
/*     */       blitSlotBg(graphics, x + 1, y + 1);
/*     */       graphics.renderFakeItem(new ItemStack((net.minecraft.world.level.ItemLike)item), x + 2, y + 2);
/*     */     }
/*     */     
/*     */     private void blitSlotBg(GuiGraphics graphics, int x, int y) {
/*     */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PresetFlatWorldScreen.SLOT_SPRITE, x, y, 18, 18);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/PresetFlatWorldScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */