/*      */ package net.minecraft.client.data.models;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.function.BiConsumer;
/*      */ import net.minecraft.client.color.item.Dye;
/*      */ import net.minecraft.client.color.item.Firework;
/*      */ import net.minecraft.client.color.item.ItemTintSource;
/*      */ import net.minecraft.client.color.item.MapColor;
/*      */ import net.minecraft.client.color.item.Potion;
/*      */ import net.minecraft.client.data.models.model.ItemModelUtils;
/*      */ import net.minecraft.client.data.models.model.ModelInstance;
/*      */ import net.minecraft.client.data.models.model.ModelLocationUtils;
/*      */ import net.minecraft.client.data.models.model.ModelTemplate;
/*      */ import net.minecraft.client.data.models.model.ModelTemplates;
/*      */ import net.minecraft.client.data.models.model.TextureMapping;
/*      */ import net.minecraft.client.renderer.item.BundleSelectedItemSpecialRenderer;
/*      */ import net.minecraft.client.renderer.item.ClientItem;
/*      */ import net.minecraft.client.renderer.item.ItemModel;
/*      */ import net.minecraft.client.renderer.item.RangeSelectItemModel;
/*      */ import net.minecraft.client.renderer.item.SelectItemModel;
/*      */ import net.minecraft.client.renderer.item.properties.conditional.Broken;
/*      */ import net.minecraft.client.renderer.item.properties.conditional.BundleHasSelectedItem;
/*      */ import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
/*      */ import net.minecraft.client.renderer.item.properties.conditional.FishingRodCast;
/*      */ import net.minecraft.client.renderer.item.properties.numeric.CompassAngle;
/*      */ import net.minecraft.client.renderer.item.properties.numeric.CompassAngleState;
/*      */ import net.minecraft.client.renderer.item.properties.numeric.CrossbowPull;
/*      */ import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
/*      */ import net.minecraft.client.renderer.item.properties.numeric.Time;
/*      */ import net.minecraft.client.renderer.item.properties.numeric.UseCycle;
/*      */ import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
/*      */ import net.minecraft.client.renderer.item.properties.select.Charge;
/*      */ import net.minecraft.client.renderer.item.properties.select.DisplayContext;
/*      */ import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
/*      */ import net.minecraft.client.renderer.item.properties.select.TrimMaterialProperty;
/*      */ import net.minecraft.client.renderer.special.ShieldSpecialRenderer;
/*      */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*      */ import net.minecraft.client.renderer.special.TridentSpecialRenderer;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.item.CrossbowItem;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemDisplayContext;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.equipment.EquipmentAsset;
/*      */ import net.minecraft.world.item.equipment.EquipmentAssets;
/*      */ import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
/*      */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*      */ import net.minecraft.world.item.equipment.trim.TrimMaterials;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ItemModelGenerators
/*      */ {
/*   71 */   private static final ItemTintSource BLANK_LAYER = ItemModelUtils.constantTint(-1);
/*      */   
/*   73 */   public static final Identifier TRIM_PREFIX_HELMET = prefixForSlotTrim("helmet");
/*   74 */   public static final Identifier TRIM_PREFIX_CHESTPLATE = prefixForSlotTrim("chestplate");
/*   75 */   public static final Identifier TRIM_PREFIX_LEGGINGS = prefixForSlotTrim("leggings");
/*   76 */   public static final Identifier TRIM_PREFIX_BOOTS = prefixForSlotTrim("boots");
/*      */   
/*      */   public static Identifier prefixForSlotTrim(String slotName) {
/*   79 */     return Identifier.withDefaultNamespace("trims/items/" + slotName + "_trim");
/*      */   }
/*      */   public static final class TrimMaterialData extends Record { private final MaterialAssetGroup assets; private final ResourceKey<TrimMaterial> materialKey;
/*   82 */     public TrimMaterialData(MaterialAssetGroup assets, ResourceKey<TrimMaterial> materialKey) { this.assets = assets; this.materialKey = materialKey; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/data/models/ItemModelGenerators$TrimMaterialData;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #82	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*   82 */       //   0	7	0	this	Lnet/minecraft/client/data/models/ItemModelGenerators$TrimMaterialData; } public MaterialAssetGroup assets() { return this.assets; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/data/models/ItemModelGenerators$TrimMaterialData;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #82	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/client/data/models/ItemModelGenerators$TrimMaterialData; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/data/models/ItemModelGenerators$TrimMaterialData;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #82	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/client/data/models/ItemModelGenerators$TrimMaterialData;
/*   82 */       //   0	8	1	o	Ljava/lang/Object; } public ResourceKey<TrimMaterial> materialKey() { return this.materialKey; }
/*      */      }
/*      */   
/*   85 */   public static final List<TrimMaterialData> TRIM_MATERIAL_MODELS = List.of(new TrimMaterialData[] { new TrimMaterialData(MaterialAssetGroup.QUARTZ, TrimMaterials.QUARTZ), new TrimMaterialData(MaterialAssetGroup.IRON, TrimMaterials.IRON), new TrimMaterialData(MaterialAssetGroup.NETHERITE, TrimMaterials.NETHERITE), new TrimMaterialData(MaterialAssetGroup.REDSTONE, TrimMaterials.REDSTONE), new TrimMaterialData(MaterialAssetGroup.COPPER, TrimMaterials.COPPER), new TrimMaterialData(MaterialAssetGroup.GOLD, TrimMaterials.GOLD), new TrimMaterialData(MaterialAssetGroup.EMERALD, TrimMaterials.EMERALD), new TrimMaterialData(MaterialAssetGroup.DIAMOND, TrimMaterials.DIAMOND), new TrimMaterialData(MaterialAssetGroup.LAPIS, TrimMaterials.LAPIS), new TrimMaterialData(MaterialAssetGroup.AMETHYST, TrimMaterials.AMETHYST), new TrimMaterialData(MaterialAssetGroup.RESIN, TrimMaterials.RESIN) });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final ItemModelOutput itemModelOutput;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final BiConsumer<Identifier, ModelInstance> modelOutput;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ItemModelGenerators(ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
/*  103 */     this.itemModelOutput = itemModelOutput;
/*  104 */     this.modelOutput = modelOutput;
/*      */   }
/*      */   
/*      */   private void declareCustomModelItem(Item item) {
/*  108 */     this.itemModelOutput.accept(item, ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item)));
/*      */   }
/*      */   
/*      */   private Identifier createFlatItemModel(Item item, ModelTemplate template) {
/*  112 */     return template.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), this.modelOutput);
/*      */   }
/*      */   
/*      */   private void generateFlatItem(Item item, ModelTemplate template) {
/*  116 */     this.itemModelOutput.accept(item, ItemModelUtils.plainModel(createFlatItemModel(item, template)));
/*      */   }
/*      */   
/*      */   private Identifier createFlatItemModel(Item item, String suffix, ModelTemplate template) {
/*  120 */     return template.create(ModelLocationUtils.getModelLocation(item, suffix), TextureMapping.layer0(TextureMapping.getItemTexture(item, suffix)), this.modelOutput);
/*      */   }
/*      */   
/*      */   private Identifier createFlatItemModel(Item item, Item textureDonor, ModelTemplate template) {
/*  124 */     return template.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(textureDonor), this.modelOutput);
/*      */   }
/*      */   
/*      */   private void generateFlatItem(Item item, Item textureDonor, ModelTemplate template) {
/*  128 */     this.itemModelOutput.accept(item, ItemModelUtils.plainModel(createFlatItemModel(item, textureDonor, template)));
/*      */   }
/*      */   
/*      */   private void generateItemWithTintedOverlay(Item item, ItemTintSource overlayTint) {
/*  132 */     generateItemWithTintedOverlay(item, "_overlay", overlayTint);
/*      */   }
/*      */   
/*      */   private void generateItemWithTintedOverlay(Item item, String overlaySuffix, ItemTintSource overlayTint) {
/*  136 */     Identifier model = generateLayeredItem(item, 
/*      */         
/*  138 */         TextureMapping.getItemTexture(item), 
/*  139 */         TextureMapping.getItemTexture(item, overlaySuffix));
/*      */ 
/*      */     
/*  142 */     this.itemModelOutput.accept(item, ItemModelUtils.tintedModel(model, new ItemTintSource[] { BLANK_LAYER, overlayTint }));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateItemWithTintedBaseLayer(Item item, int defaultColor) {
/*  148 */     Identifier tintedLayer = TextureMapping.getItemTexture(item);
/*  149 */     Identifier untintedLayer = TextureMapping.getItemTexture(item, "_overlay");
/*      */     
/*  151 */     Identifier model = ModelLocationUtils.getModelLocation(item);
/*  152 */     ModelTemplates.TWO_LAYERED_ITEM.create(model, TextureMapping.layered(tintedLayer, untintedLayer), this.modelOutput);
/*      */     
/*  154 */     this.itemModelOutput.accept(item, ItemModelUtils.tintedModel(model, new ItemTintSource[] { (ItemTintSource)new Dye(defaultColor) }));
/*      */   }
/*      */   
/*      */   private List<RangeSelectItemModel.Entry> createCompassModels(Item compass) {
/*  158 */     List<RangeSelectItemModel.Entry> overrides = new ArrayList<>();
/*  159 */     ItemModel.Unbaked base = ItemModelUtils.plainModel(createFlatItemModel(compass, "_16", ModelTemplates.FLAT_ITEM));
/*  160 */     overrides.add(ItemModelUtils.override(base, 0.0F));
/*      */     
/*  162 */     for (int i = 1; i < 32; i++) {
/*  163 */       int textureIndex = Mth.positiveModulo(i - 16, 32);
/*  164 */       ItemModel.Unbaked overrideModel = ItemModelUtils.plainModel(createFlatItemModel(compass, String.format(Locale.ROOT, "_%02d", new Object[] { textureIndex }), ModelTemplates.FLAT_ITEM));
/*  165 */       overrides.add(ItemModelUtils.override(overrideModel, i - 0.5F));
/*      */     } 
/*      */     
/*  168 */     overrides.add(ItemModelUtils.override(base, 31.5F));
/*  169 */     return overrides;
/*      */   }
/*      */   
/*      */   private void generateStandardCompassItem(Item compass) {
/*  173 */     List<RangeSelectItemModel.Entry> overrides = createCompassModels(compass);
/*      */     
/*  175 */     this.itemModelOutput.accept(compass, 
/*  176 */         ItemModelUtils.conditional(
/*  177 */           ItemModelUtils.hasComponent(DataComponents.LODESTONE_TRACKER), 
/*  178 */           ItemModelUtils.rangeSelect((RangeSelectItemModelProperty)new CompassAngle(true, CompassAngleState.CompassTarget.LODESTONE), 32.0F, overrides), 
/*  179 */           ItemModelUtils.rangeSelect((RangeSelectItemModelProperty)new CompassAngle(true, CompassAngleState.CompassTarget.SPAWN), 32.0F, overrides)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateRecoveryCompassItem(Item compass) {
/*  185 */     this.itemModelOutput.accept(compass, 
/*  186 */         ItemModelUtils.rangeSelect((RangeSelectItemModelProperty)new CompassAngle(true, CompassAngleState.CompassTarget.RECOVERY), 32.0F, 
/*      */ 
/*      */           
/*  189 */           createCompassModels(compass)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateClockItem(Item clock) {
/*  195 */     List<RangeSelectItemModel.Entry> overrides = new ArrayList<>();
/*  196 */     ItemModel.Unbaked base = ItemModelUtils.plainModel(createFlatItemModel(clock, "_00", ModelTemplates.FLAT_ITEM));
/*  197 */     overrides.add(ItemModelUtils.override(base, 0.0F));
/*      */     
/*  199 */     for (int i = 1; i < 64; i++) {
/*  200 */       ItemModel.Unbaked overrideModel = ItemModelUtils.plainModel(createFlatItemModel(clock, String.format(Locale.ROOT, "_%02d", new Object[] { i }), ModelTemplates.FLAT_ITEM));
/*  201 */       overrides.add(ItemModelUtils.override(overrideModel, i - 0.5F));
/*      */     } 
/*      */     
/*  204 */     overrides.add(ItemModelUtils.override(base, 63.5F));
/*  205 */     this.itemModelOutput.accept(clock, 
/*  206 */         ItemModelUtils.inOverworld(
/*  207 */           ItemModelUtils.rangeSelect((RangeSelectItemModelProperty)new Time(true, Time.TimeSource.DAYTIME), 64.0F, overrides), 
/*  208 */           ItemModelUtils.rangeSelect((RangeSelectItemModelProperty)new Time(true, Time.TimeSource.RANDOM), 64.0F, overrides)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Identifier generateLayeredItem(Item target, Identifier layer0, Identifier layer1) {
/*  214 */     return ModelTemplates.TWO_LAYERED_ITEM.create(target, TextureMapping.layered(layer0, layer1), this.modelOutput);
/*      */   }
/*      */   
/*      */   private Identifier generateLayeredItem(Identifier target, Identifier layer0, Identifier layer1) {
/*  218 */     return ModelTemplates.TWO_LAYERED_ITEM.create(target, TextureMapping.layered(layer0, layer1), this.modelOutput);
/*      */   }
/*      */   
/*      */   private void generateLayeredItem(Identifier target, Identifier layer0, Identifier layer1, Identifier layer2) {
/*  222 */     ModelTemplates.THREE_LAYERED_ITEM.create(target, TextureMapping.layered(layer0, layer1, layer2), this.modelOutput);
/*      */   }
/*      */   private void generateTrimmableItem(Item armor, ResourceKey<EquipmentAsset> equipmentAssetId, Identifier slotTrimPrefix, boolean hasDyedLayer) {
/*      */     ItemModel.Unbaked untrimmedModel;
/*  226 */     Identifier modelLocation = ModelLocationUtils.getModelLocation(armor);
/*  227 */     Identifier itemTexture = TextureMapping.getItemTexture(armor);
/*  228 */     Identifier overlayTexture = TextureMapping.getItemTexture(armor, "_overlay");
/*      */     
/*  230 */     List<SelectItemModel.SwitchCase<ResourceKey<TrimMaterial>>> cases = new ArrayList<>(TRIM_MATERIAL_MODELS.size());
/*      */     
/*  232 */     for (TrimMaterialData material : TRIM_MATERIAL_MODELS) {
/*      */       ItemModel.Unbaked trimModel;
/*  234 */       Identifier trimModelLocation = modelLocation.withSuffix("_" + material.assets().base().suffix() + "_trim");
/*  235 */       Identifier trimOverlayTexture = slotTrimPrefix.withSuffix("_" + material.assets().assetId(equipmentAssetId).suffix());
/*      */ 
/*      */       
/*  238 */       if (hasDyedLayer) {
/*  239 */         generateLayeredItem(trimModelLocation, itemTexture, overlayTexture, trimOverlayTexture);
/*  240 */         trimModel = ItemModelUtils.tintedModel(trimModelLocation, new ItemTintSource[] { (ItemTintSource)new Dye(-6265536) });
/*      */       } else {
/*  242 */         generateLayeredItem(trimModelLocation, itemTexture, trimOverlayTexture);
/*  243 */         trimModel = ItemModelUtils.plainModel(trimModelLocation);
/*      */       } 
/*      */       
/*  246 */       cases.add(ItemModelUtils.when(material.materialKey, trimModel));
/*      */     } 
/*      */ 
/*      */     
/*  250 */     if (hasDyedLayer) {
/*  251 */       ModelTemplates.TWO_LAYERED_ITEM.create(modelLocation, TextureMapping.layered(itemTexture, overlayTexture), this.modelOutput);
/*  252 */       untrimmedModel = ItemModelUtils.tintedModel(modelLocation, new ItemTintSource[] { (ItemTintSource)new Dye(-6265536) });
/*      */     } else {
/*  254 */       ModelTemplates.FLAT_ITEM.create(modelLocation, TextureMapping.layer0(itemTexture), this.modelOutput);
/*  255 */       untrimmedModel = ItemModelUtils.plainModel(modelLocation);
/*      */     } 
/*      */     
/*  258 */     this.itemModelOutput.accept(armor, 
/*  259 */         ItemModelUtils.select((SelectItemModelProperty)new TrimMaterialProperty(), untrimmedModel, cases));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateBundleModels(Item bundle) {
/*  268 */     ItemModel.Unbaked closedModel = ItemModelUtils.plainModel(createFlatItemModel(bundle, ModelTemplates.FLAT_ITEM));
/*      */     
/*  270 */     Identifier openBackCover = generateBundleCoverModel(bundle, ModelTemplates.BUNDLE_OPEN_BACK_INVENTORY, "_open_back");
/*  271 */     Identifier openFrontCover = generateBundleCoverModel(bundle, ModelTemplates.BUNDLE_OPEN_FRONT_INVENTORY, "_open_front");
/*      */     
/*  273 */     ItemModel.Unbaked openModel = ItemModelUtils.composite(new ItemModel.Unbaked[] {
/*  274 */           ItemModelUtils.plainModel(openBackCover), (ItemModel.Unbaked)new BundleSelectedItemSpecialRenderer.Unbaked(), 
/*      */           
/*  276 */           ItemModelUtils.plainModel(openFrontCover)
/*      */         });
/*      */     
/*  279 */     ItemModel.Unbaked inGuiModel = ItemModelUtils.conditional((ConditionalItemModelProperty)new BundleHasSelectedItem(), openModel, closedModel);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  285 */     this.itemModelOutput.accept(bundle, ItemModelUtils.select((SelectItemModelProperty)new DisplayContext(), closedModel, new SelectItemModel.SwitchCase[] {
/*      */ 
/*      */             
/*  288 */             ItemModelUtils.when(ItemDisplayContext.GUI, inGuiModel)
/*      */           }));
/*      */   }
/*      */   
/*      */   private Identifier generateBundleCoverModel(Item item, ModelTemplate template, String suffix) {
/*  293 */     Identifier texture = TextureMapping.getItemTexture(item, suffix);
/*  294 */     return template.create(item, TextureMapping.layer0(texture), this.modelOutput);
/*      */   }
/*      */   
/*      */   private void generateBow(Item item) {
/*  298 */     ItemModel.Unbaked bowModel = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item));
/*  299 */     ItemModel.Unbaked pulling0 = ItemModelUtils.plainModel(createFlatItemModel(item, "_pulling_0", ModelTemplates.BOW));
/*  300 */     ItemModel.Unbaked pulling1 = ItemModelUtils.plainModel(createFlatItemModel(item, "_pulling_1", ModelTemplates.BOW));
/*  301 */     ItemModel.Unbaked pulling2 = ItemModelUtils.plainModel(createFlatItemModel(item, "_pulling_2", ModelTemplates.BOW));
/*      */     
/*  303 */     this.itemModelOutput.accept(item, 
/*  304 */         ItemModelUtils.conditional(
/*  305 */           ItemModelUtils.isUsingItem(), 
/*  306 */           ItemModelUtils.rangeSelect((RangeSelectItemModelProperty)new UseDuration(false), 0.05F, pulling0, new RangeSelectItemModel.Entry[] {
/*      */ 
/*      */ 
/*      */               
/*  310 */               ItemModelUtils.override(pulling1, 0.65F), 
/*  311 */               ItemModelUtils.override(pulling2, 0.9F)
/*      */             }), bowModel));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateCrossbow(Item item) {
/*  319 */     ItemModel.Unbaked crossbowModel = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item));
/*  320 */     ItemModel.Unbaked pulling0 = ItemModelUtils.plainModel(createFlatItemModel(item, "_pulling_0", ModelTemplates.CROSSBOW));
/*  321 */     ItemModel.Unbaked pulling1 = ItemModelUtils.plainModel(createFlatItemModel(item, "_pulling_1", ModelTemplates.CROSSBOW));
/*  322 */     ItemModel.Unbaked pulling2 = ItemModelUtils.plainModel(createFlatItemModel(item, "_pulling_2", ModelTemplates.CROSSBOW));
/*  323 */     ItemModel.Unbaked loadedArrow = ItemModelUtils.plainModel(createFlatItemModel(item, "_arrow", ModelTemplates.CROSSBOW));
/*  324 */     ItemModel.Unbaked loadedFirework = ItemModelUtils.plainModel(createFlatItemModel(item, "_firework", ModelTemplates.CROSSBOW));
/*      */     
/*  326 */     this.itemModelOutput.accept(item, 
/*  327 */         ItemModelUtils.select((SelectItemModelProperty)new Charge(), 
/*      */           
/*  329 */           ItemModelUtils.conditional(
/*  330 */             ItemModelUtils.isUsingItem(), 
/*  331 */             ItemModelUtils.rangeSelect((RangeSelectItemModelProperty)new CrossbowPull(), pulling0, new RangeSelectItemModel.Entry[] {
/*      */ 
/*      */                 
/*  334 */                 ItemModelUtils.override(pulling1, 0.58F), 
/*  335 */                 ItemModelUtils.override(pulling2, 1.0F)
/*      */ 
/*      */               
/*      */               }), crossbowModel), new SelectItemModel.SwitchCase[] {
/*  339 */             ItemModelUtils.when(CrossbowItem.ChargeType.ARROW, loadedArrow), 
/*  340 */             ItemModelUtils.when(CrossbowItem.ChargeType.ROCKET, loadedFirework)
/*      */           }));
/*      */   }
/*      */ 
/*      */   
/*      */   private void generateBooleanDispatch(Item item, ConditionalItemModelProperty property, ItemModel.Unbaked modelOnTrue, ItemModel.Unbaked modelOnFalse) {
/*  346 */     this.itemModelOutput.accept(item, 
/*  347 */         ItemModelUtils.conditional(property, modelOnTrue, modelOnFalse));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateElytra(Item item) {
/*  356 */     ItemModel.Unbaked normalElytra = ItemModelUtils.plainModel(createFlatItemModel(item, ModelTemplates.FLAT_ITEM));
/*  357 */     ItemModel.Unbaked brokenElytra = ItemModelUtils.plainModel(createFlatItemModel(item, "_broken", ModelTemplates.FLAT_ITEM));
/*      */     
/*  359 */     generateBooleanDispatch(item, (ConditionalItemModelProperty)new Broken(), brokenElytra, normalElytra);
/*      */   }
/*      */   
/*      */   private void generateBrush(Item item) {
/*  363 */     ItemModel.Unbaked base = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item));
/*  364 */     ItemModel.Unbaked brushing0 = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_brushing_0"));
/*  365 */     ItemModel.Unbaked brushing1 = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_brushing_1"));
/*  366 */     ItemModel.Unbaked brushing2 = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_brushing_2"));
/*  367 */     this.itemModelOutput.accept(item, ItemModelUtils.rangeSelect((RangeSelectItemModelProperty)new UseCycle(10.0F), 0.1F, base, new RangeSelectItemModel.Entry[] {
/*      */ 
/*      */ 
/*      */             
/*  371 */             ItemModelUtils.override(brushing0, 0.25F), 
/*  372 */             ItemModelUtils.override(brushing1, 0.5F), 
/*  373 */             ItemModelUtils.override(brushing2, 0.75F)
/*      */           }));
/*      */   }
/*      */   
/*      */   private void generateFishingRod(Item item) {
/*  378 */     ItemModel.Unbaked normal = ItemModelUtils.plainModel(createFlatItemModel(item, ModelTemplates.FLAT_HANDHELD_ROD_ITEM));
/*  379 */     ItemModel.Unbaked cast = ItemModelUtils.plainModel(createFlatItemModel(item, "_cast", ModelTemplates.FLAT_HANDHELD_ROD_ITEM));
/*      */     
/*  381 */     generateBooleanDispatch(item, (ConditionalItemModelProperty)new FishingRodCast(), cast, normal);
/*      */   }
/*      */   
/*      */   private void generateGoatHorn(Item item) {
/*  385 */     ItemModel.Unbaked normal = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item));
/*  386 */     ItemModel.Unbaked tooting = ItemModelUtils.plainModel(ModelLocationUtils.decorateItemModelLocation("tooting_goat_horn"));
/*      */     
/*  388 */     generateBooleanDispatch(item, ItemModelUtils.isUsingItem(), tooting, normal);
/*      */   }
/*      */   
/*      */   private void generateShield(Item item) {
/*  392 */     ItemModel.Unbaked normal = ItemModelUtils.specialModel(
/*  393 */         ModelLocationUtils.getModelLocation(item), (SpecialModelRenderer.Unbaked)new ShieldSpecialRenderer.Unbaked());
/*      */ 
/*      */ 
/*      */     
/*  397 */     ItemModel.Unbaked blocking = ItemModelUtils.specialModel(
/*  398 */         ModelLocationUtils.getModelLocation(item, "_blocking"), (SpecialModelRenderer.Unbaked)new ShieldSpecialRenderer.Unbaked());
/*      */ 
/*      */ 
/*      */     
/*  402 */     generateBooleanDispatch(item, ItemModelUtils.isUsingItem(), blocking, normal);
/*      */   }
/*      */   
/*      */   private static ItemModel.Unbaked createFlatModelDispatch(ItemModel.Unbaked flatModel, ItemModel.Unbaked inHandModel) {
/*  406 */     return ItemModelUtils.select((SelectItemModelProperty)new DisplayContext(), inHandModel, new SelectItemModel.SwitchCase[] {
/*      */ 
/*      */           
/*  409 */           ItemModelUtils.when(List.of(ItemDisplayContext.GUI, ItemDisplayContext.GROUND, ItemDisplayContext.FIXED, ItemDisplayContext.ON_SHELF), flatModel)
/*      */         });
/*      */   }
/*      */   
/*      */   private void generateSpyglass(Item item) {
/*  414 */     ItemModel.Unbaked flatModel = ItemModelUtils.plainModel(createFlatItemModel(item, ModelTemplates.FLAT_ITEM));
/*      */     
/*  416 */     ItemModel.Unbaked inHandModel = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_in_hand"));
/*  417 */     this.itemModelOutput.accept(item, createFlatModelDispatch(flatModel, inHandModel));
/*      */   }
/*      */   
/*      */   private void generateTrident(Item item) {
/*  421 */     ItemModel.Unbaked flatModel = ItemModelUtils.plainModel(createFlatItemModel(item, ModelTemplates.FLAT_ITEM));
/*      */     
/*  423 */     ItemModel.Unbaked inHandNormalModel = ItemModelUtils.specialModel(
/*  424 */         ModelLocationUtils.getModelLocation(item, "_in_hand"), (SpecialModelRenderer.Unbaked)new TridentSpecialRenderer.Unbaked());
/*      */ 
/*      */     
/*  427 */     ItemModel.Unbaked inHandThrowingModel = ItemModelUtils.specialModel(
/*  428 */         ModelLocationUtils.getModelLocation(item, "_throwing"), (SpecialModelRenderer.Unbaked)new TridentSpecialRenderer.Unbaked());
/*      */ 
/*      */ 
/*      */     
/*  432 */     ItemModel.Unbaked inHandModel = ItemModelUtils.conditional(ItemModelUtils.isUsingItem(), inHandThrowingModel, inHandNormalModel);
/*      */     
/*  434 */     this.itemModelOutput.accept(item, createFlatModelDispatch(flatModel, inHandModel));
/*      */   }
/*      */   
/*      */   private void generateSpear(Item item) {
/*  438 */     ItemModel.Unbaked flatModel = ItemModelUtils.plainModel(createFlatItemModel(item, ModelTemplates.FLAT_ITEM));
/*  439 */     ItemModel.Unbaked inHandModel = ItemModelUtils.plainModel(
/*  440 */         ModelTemplates.SPEAR_IN_HAND.create(item, TextureMapping.layer0(TextureMapping.getItemTexture(item, "_in_hand")), this.modelOutput));
/*      */     
/*  442 */     this.itemModelOutput.accept(item, createFlatModelDispatch(flatModel, inHandModel), new ClientItem.Properties(true, false, 1.95F));
/*      */   }
/*      */   
/*      */   private void addPotionTint(Item item, Identifier model) {
/*  446 */     this.itemModelOutput.accept(item, ItemModelUtils.tintedModel(model, new ItemTintSource[] { (ItemTintSource)new Potion() }));
/*      */   }
/*      */   
/*      */   private void generatePotion(Item item) {
/*  450 */     Identifier model = generateLayeredItem(item, 
/*      */         
/*  452 */         ModelLocationUtils.decorateItemModelLocation("potion_overlay"), 
/*  453 */         ModelLocationUtils.getModelLocation(item));
/*      */     
/*  455 */     addPotionTint(item, model);
/*      */   }
/*      */   
/*      */   private void generateTippedArrow(Item item) {
/*  459 */     Identifier model = generateLayeredItem(item, 
/*      */         
/*  461 */         ModelLocationUtils.getModelLocation(item, "_head"), 
/*  462 */         ModelLocationUtils.getModelLocation(item, "_base"));
/*      */     
/*  464 */     addPotionTint(item, model);
/*      */   }
/*      */   
/*      */   private void generateDyedItem(Item item, int defaultColor) {
/*  468 */     Identifier model = createFlatItemModel(item, ModelTemplates.FLAT_ITEM);
/*  469 */     this.itemModelOutput.accept(item, ItemModelUtils.tintedModel(model, new ItemTintSource[] { (ItemTintSource)new Dye(defaultColor) }));
/*      */   }
/*      */   
/*      */   private void generateTwoLayerDyedItem(Item item) {
/*  473 */     Identifier baseLayer = TextureMapping.getItemTexture(item);
/*  474 */     Identifier tintedLayer = TextureMapping.getItemTexture(item, "_overlay");
/*      */     
/*  476 */     Identifier plainModel = ModelTemplates.FLAT_ITEM.create(item, TextureMapping.layer0(baseLayer), this.modelOutput);
/*      */     
/*  478 */     Identifier dyedModel = ModelLocationUtils.getModelLocation(item, "_dyed");
/*  479 */     ModelTemplates.TWO_LAYERED_ITEM.create(dyedModel, TextureMapping.layered(baseLayer, tintedLayer), this.modelOutput);
/*      */     
/*  481 */     this.itemModelOutput.accept(item, 
/*  482 */         ItemModelUtils.conditional(
/*  483 */           ItemModelUtils.hasComponent(DataComponents.DYED_COLOR), 
/*  484 */           ItemModelUtils.tintedModel(dyedModel, new ItemTintSource[] { BLANK_LAYER, (ItemTintSource)new Dye(0)
/*  485 */             }), ItemModelUtils.plainModel(plainModel)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void run() {
/*  491 */     generateFlatItem(Items.ACACIA_BOAT, ModelTemplates.FLAT_ITEM);
/*  492 */     generateFlatItem(Items.CHERRY_BOAT, ModelTemplates.FLAT_ITEM);
/*  493 */     generateFlatItem(Items.ACACIA_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
/*  494 */     generateFlatItem(Items.CHERRY_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
/*  495 */     generateFlatItem(Items.AMETHYST_SHARD, ModelTemplates.FLAT_ITEM);
/*  496 */     generateFlatItem(Items.APPLE, ModelTemplates.FLAT_ITEM);
/*  497 */     generateFlatItem(Items.ARMADILLO_SCUTE, ModelTemplates.FLAT_ITEM);
/*  498 */     generateFlatItem(Items.ARMOR_STAND, ModelTemplates.FLAT_ITEM);
/*  499 */     generateFlatItem(Items.ARROW, ModelTemplates.FLAT_ITEM);
/*  500 */     generateFlatItem(Items.BAKED_POTATO, ModelTemplates.FLAT_ITEM);
/*  501 */     generateFlatItem(Items.BAMBOO, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  502 */     generateFlatItem(Items.BEEF, ModelTemplates.FLAT_ITEM);
/*  503 */     generateFlatItem(Items.BEETROOT, ModelTemplates.FLAT_ITEM);
/*  504 */     generateFlatItem(Items.BEETROOT_SOUP, ModelTemplates.FLAT_ITEM);
/*  505 */     generateFlatItem(Items.BIRCH_BOAT, ModelTemplates.FLAT_ITEM);
/*  506 */     generateFlatItem(Items.BIRCH_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
/*  507 */     generateFlatItem(Items.BLACK_DYE, ModelTemplates.FLAT_ITEM);
/*  508 */     generateFlatItem(Items.BLAZE_POWDER, ModelTemplates.FLAT_ITEM);
/*  509 */     generateFlatItem(Items.BLAZE_ROD, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  510 */     generateFlatItem(Items.BLUE_DYE, ModelTemplates.FLAT_ITEM);
/*  511 */     generateFlatItem(Items.BONE_MEAL, ModelTemplates.FLAT_ITEM);
/*  512 */     generateFlatItem(Items.BORDURE_INDENTED_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  513 */     generateFlatItem(Items.BOOK, ModelTemplates.FLAT_ITEM);
/*  514 */     generateFlatItem(Items.BOWL, ModelTemplates.FLAT_ITEM);
/*  515 */     generateFlatItem(Items.BREAD, ModelTemplates.FLAT_ITEM);
/*  516 */     generateFlatItem(Items.BRICK, ModelTemplates.FLAT_ITEM);
/*  517 */     generateFlatItem(Items.BREEZE_ROD, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  518 */     generateFlatItem(Items.BROWN_DYE, ModelTemplates.FLAT_ITEM);
/*  519 */     generateFlatItem(Items.BUCKET, ModelTemplates.FLAT_ITEM);
/*  520 */     generateFlatItem(Items.CARROT_ON_A_STICK, ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
/*  521 */     generateFlatItem(Items.WARPED_FUNGUS_ON_A_STICK, ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
/*  522 */     generateFlatItem(Items.CHARCOAL, ModelTemplates.FLAT_ITEM);
/*  523 */     generateFlatItem(Items.CHEST_MINECART, ModelTemplates.FLAT_ITEM);
/*  524 */     generateFlatItem(Items.CHICKEN, ModelTemplates.FLAT_ITEM);
/*  525 */     generateFlatItem(Items.CHORUS_FRUIT, ModelTemplates.FLAT_ITEM);
/*  526 */     generateFlatItem(Items.CLAY_BALL, ModelTemplates.FLAT_ITEM);
/*      */     
/*  528 */     generateClockItem(Items.CLOCK);
/*      */     
/*  530 */     generateFlatItem(Items.COAL, ModelTemplates.FLAT_ITEM);
/*  531 */     generateFlatItem(Items.COD_BUCKET, ModelTemplates.FLAT_ITEM);
/*  532 */     generateFlatItem(Items.COMMAND_BLOCK_MINECART, ModelTemplates.FLAT_ITEM);
/*      */     
/*  534 */     generateStandardCompassItem(Items.COMPASS);
/*  535 */     generateRecoveryCompassItem(Items.RECOVERY_COMPASS);
/*      */     
/*  537 */     generateFlatItem(Items.COOKED_BEEF, ModelTemplates.FLAT_ITEM);
/*  538 */     generateFlatItem(Items.COOKED_CHICKEN, ModelTemplates.FLAT_ITEM);
/*  539 */     generateFlatItem(Items.COOKED_COD, ModelTemplates.FLAT_ITEM);
/*  540 */     generateFlatItem(Items.COOKED_MUTTON, ModelTemplates.FLAT_ITEM);
/*  541 */     generateFlatItem(Items.COOKED_PORKCHOP, ModelTemplates.FLAT_ITEM);
/*  542 */     generateFlatItem(Items.COOKED_RABBIT, ModelTemplates.FLAT_ITEM);
/*  543 */     generateFlatItem(Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
/*  544 */     generateFlatItem(Items.COOKIE, ModelTemplates.FLAT_ITEM);
/*  545 */     generateFlatItem(Items.RAW_COPPER, ModelTemplates.FLAT_ITEM);
/*  546 */     generateFlatItem(Items.COPPER_NUGGET, ModelTemplates.FLAT_ITEM);
/*  547 */     generateFlatItem(Items.COPPER_INGOT, ModelTemplates.FLAT_ITEM);
/*  548 */     generateFlatItem(Items.COPPER_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  549 */     generateFlatItem(Items.COPPER_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  550 */     generateFlatItem(Items.COPPER_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  551 */     generateFlatItem(Items.COPPER_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  552 */     generateFlatItem(Items.COPPER_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  553 */     generateFlatItem(Items.COPPER_HORSE_ARMOR, ModelTemplates.FLAT_ITEM);
/*  554 */     generateFlatItem(Items.COPPER_NAUTILUS_ARMOR, ModelTemplates.FLAT_ITEM);
/*  555 */     generateFlatItem(Items.CREEPER_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  556 */     generateFlatItem(Items.CYAN_DYE, ModelTemplates.FLAT_ITEM);
/*  557 */     generateFlatItem(Items.DARK_OAK_BOAT, ModelTemplates.FLAT_ITEM);
/*  558 */     generateFlatItem(Items.DARK_OAK_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
/*  559 */     generateFlatItem(Items.DIAMOND, ModelTemplates.FLAT_ITEM);
/*  560 */     generateFlatItem(Items.DIAMOND_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  561 */     generateFlatItem(Items.DIAMOND_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  562 */     generateFlatItem(Items.DIAMOND_HORSE_ARMOR, ModelTemplates.FLAT_ITEM);
/*  563 */     generateFlatItem(Items.DIAMOND_NAUTILUS_ARMOR, ModelTemplates.FLAT_ITEM);
/*  564 */     generateFlatItem(Items.DIAMOND_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  565 */     generateFlatItem(Items.DIAMOND_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  566 */     generateFlatItem(Items.DIAMOND_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  567 */     generateFlatItem(Items.DRAGON_BREATH, ModelTemplates.FLAT_ITEM);
/*  568 */     generateFlatItem(Items.DRIED_KELP, ModelTemplates.FLAT_ITEM);
/*  569 */     generateFlatItem(Items.EGG, ModelTemplates.FLAT_ITEM);
/*  570 */     generateFlatItem(Items.BLUE_EGG, ModelTemplates.FLAT_ITEM);
/*  571 */     generateFlatItem(Items.BROWN_EGG, ModelTemplates.FLAT_ITEM);
/*  572 */     generateFlatItem(Items.EMERALD, ModelTemplates.FLAT_ITEM);
/*  573 */     generateFlatItem(Items.ENCHANTED_BOOK, ModelTemplates.FLAT_ITEM);
/*  574 */     generateFlatItem(Items.ENDER_EYE, ModelTemplates.FLAT_ITEM);
/*  575 */     generateFlatItem(Items.ENDER_PEARL, ModelTemplates.FLAT_ITEM);
/*  576 */     generateFlatItem(Items.END_CRYSTAL, ModelTemplates.FLAT_ITEM);
/*  577 */     generateFlatItem(Items.EXPERIENCE_BOTTLE, ModelTemplates.FLAT_ITEM);
/*  578 */     generateFlatItem(Items.FERMENTED_SPIDER_EYE, ModelTemplates.FLAT_ITEM);
/*  579 */     generateFlatItem(Items.FIELD_MASONED_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  580 */     generateFlatItem(Items.FIREWORK_ROCKET, ModelTemplates.FLAT_ITEM);
/*  581 */     generateFlatItem(Items.FIRE_CHARGE, ModelTemplates.FLAT_ITEM);
/*  582 */     generateFlatItem(Items.FLINT, ModelTemplates.FLAT_ITEM);
/*  583 */     generateFlatItem(Items.FLINT_AND_STEEL, ModelTemplates.FLAT_ITEM);
/*  584 */     generateFlatItem(Items.FLOW_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  585 */     generateFlatItem(Items.FLOWER_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  586 */     generateFlatItem(Items.FURNACE_MINECART, ModelTemplates.FLAT_ITEM);
/*  587 */     generateFlatItem(Items.GHAST_TEAR, ModelTemplates.FLAT_ITEM);
/*  588 */     generateFlatItem(Items.GLASS_BOTTLE, ModelTemplates.FLAT_ITEM);
/*  589 */     generateFlatItem(Items.GLISTERING_MELON_SLICE, ModelTemplates.FLAT_ITEM);
/*  590 */     generateFlatItem(Items.GLOBE_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  591 */     generateFlatItem(Items.GLOW_BERRIES, ModelTemplates.FLAT_ITEM);
/*  592 */     generateFlatItem(Items.GLOWSTONE_DUST, ModelTemplates.FLAT_ITEM);
/*  593 */     generateFlatItem(Items.GLOW_INK_SAC, ModelTemplates.FLAT_ITEM);
/*  594 */     generateFlatItem(Items.GLOW_ITEM_FRAME, ModelTemplates.FLAT_ITEM);
/*  595 */     generateFlatItem(Items.RAW_GOLD, ModelTemplates.FLAT_ITEM);
/*  596 */     generateFlatItem(Items.GOLDEN_APPLE, ModelTemplates.FLAT_ITEM);
/*  597 */     generateFlatItem(Items.GOLDEN_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  598 */     generateFlatItem(Items.GOLDEN_CARROT, ModelTemplates.FLAT_ITEM);
/*  599 */     generateFlatItem(Items.GOLDEN_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  600 */     generateFlatItem(Items.GOLDEN_HORSE_ARMOR, ModelTemplates.FLAT_ITEM);
/*  601 */     generateFlatItem(Items.GOLDEN_NAUTILUS_ARMOR, ModelTemplates.FLAT_ITEM);
/*  602 */     generateFlatItem(Items.GOLDEN_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  603 */     generateFlatItem(Items.GOLDEN_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  604 */     generateFlatItem(Items.GOLDEN_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  605 */     generateFlatItem(Items.GOLD_INGOT, ModelTemplates.FLAT_ITEM);
/*  606 */     generateFlatItem(Items.GOLD_NUGGET, ModelTemplates.FLAT_ITEM);
/*  607 */     generateFlatItem(Items.GRAY_DYE, ModelTemplates.FLAT_ITEM);
/*  608 */     generateFlatItem(Items.GREEN_DYE, ModelTemplates.FLAT_ITEM);
/*  609 */     generateFlatItem(Items.GUNPOWDER, ModelTemplates.FLAT_ITEM);
/*  610 */     generateFlatItem(Items.GUSTER_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  611 */     generateFlatItem(Items.HEART_OF_THE_SEA, ModelTemplates.FLAT_ITEM);
/*  612 */     generateFlatItem(Items.HONEYCOMB, ModelTemplates.FLAT_ITEM);
/*  613 */     generateFlatItem(Items.HONEY_BOTTLE, ModelTemplates.FLAT_ITEM);
/*  614 */     generateFlatItem(Items.HOPPER_MINECART, ModelTemplates.FLAT_ITEM);
/*  615 */     generateFlatItem(Items.INK_SAC, ModelTemplates.FLAT_ITEM);
/*  616 */     generateFlatItem(Items.RAW_IRON, ModelTemplates.FLAT_ITEM);
/*  617 */     generateFlatItem(Items.IRON_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  618 */     generateFlatItem(Items.IRON_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  619 */     generateFlatItem(Items.IRON_HORSE_ARMOR, ModelTemplates.FLAT_ITEM);
/*  620 */     generateFlatItem(Items.IRON_NAUTILUS_ARMOR, ModelTemplates.FLAT_ITEM);
/*  621 */     generateFlatItem(Items.IRON_INGOT, ModelTemplates.FLAT_ITEM);
/*  622 */     generateFlatItem(Items.IRON_NUGGET, ModelTemplates.FLAT_ITEM);
/*  623 */     generateFlatItem(Items.IRON_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  624 */     generateFlatItem(Items.IRON_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  625 */     generateFlatItem(Items.IRON_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  626 */     generateFlatItem(Items.ITEM_FRAME, ModelTemplates.FLAT_ITEM);
/*  627 */     generateFlatItem(Items.JUNGLE_BOAT, ModelTemplates.FLAT_ITEM);
/*  628 */     generateFlatItem(Items.JUNGLE_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
/*  629 */     generateFlatItem(Items.KNOWLEDGE_BOOK, ModelTemplates.FLAT_ITEM);
/*  630 */     generateFlatItem(Items.LAPIS_LAZULI, ModelTemplates.FLAT_ITEM);
/*  631 */     generateFlatItem(Items.LAVA_BUCKET, ModelTemplates.FLAT_ITEM);
/*  632 */     generateFlatItem(Items.LEATHER, ModelTemplates.FLAT_ITEM);
/*  633 */     generateFlatItem(Items.LIGHT_BLUE_DYE, ModelTemplates.FLAT_ITEM);
/*  634 */     generateFlatItem(Items.LIGHT_GRAY_DYE, ModelTemplates.FLAT_ITEM);
/*  635 */     generateFlatItem(Items.LIME_DYE, ModelTemplates.FLAT_ITEM);
/*  636 */     generateFlatItem(Items.MAGENTA_DYE, ModelTemplates.FLAT_ITEM);
/*  637 */     generateFlatItem(Items.MAGMA_CREAM, ModelTemplates.FLAT_ITEM);
/*  638 */     generateFlatItem(Items.MANGROVE_BOAT, ModelTemplates.FLAT_ITEM);
/*  639 */     generateFlatItem(Items.MANGROVE_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
/*  640 */     generateFlatItem(Items.BAMBOO_RAFT, ModelTemplates.FLAT_ITEM);
/*  641 */     generateFlatItem(Items.BAMBOO_CHEST_RAFT, ModelTemplates.FLAT_ITEM);
/*  642 */     generateFlatItem(Items.MAP, ModelTemplates.FLAT_ITEM);
/*  643 */     generateFlatItem(Items.MELON_SLICE, ModelTemplates.FLAT_ITEM);
/*  644 */     generateFlatItem(Items.MILK_BUCKET, ModelTemplates.FLAT_ITEM);
/*  645 */     generateFlatItem(Items.MINECART, ModelTemplates.FLAT_ITEM);
/*  646 */     generateFlatItem(Items.MOJANG_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  647 */     generateFlatItem(Items.MUSHROOM_STEW, ModelTemplates.FLAT_ITEM);
/*  648 */     generateFlatItem(Items.DISC_FRAGMENT_5, ModelTemplates.FLAT_ITEM);
/*  649 */     generateFlatItem(Items.MUSIC_DISC_11, ModelTemplates.MUSIC_DISC);
/*  650 */     generateFlatItem(Items.MUSIC_DISC_13, ModelTemplates.MUSIC_DISC);
/*  651 */     generateFlatItem(Items.MUSIC_DISC_BLOCKS, ModelTemplates.MUSIC_DISC);
/*  652 */     generateFlatItem(Items.MUSIC_DISC_CAT, ModelTemplates.MUSIC_DISC);
/*  653 */     generateFlatItem(Items.MUSIC_DISC_CHIRP, ModelTemplates.MUSIC_DISC);
/*  654 */     generateFlatItem(Items.MUSIC_DISC_CREATOR, ModelTemplates.MUSIC_DISC);
/*  655 */     generateFlatItem(Items.MUSIC_DISC_CREATOR_MUSIC_BOX, ModelTemplates.MUSIC_DISC);
/*  656 */     generateFlatItem(Items.MUSIC_DISC_FAR, ModelTemplates.MUSIC_DISC);
/*  657 */     generateFlatItem(Items.MUSIC_DISC_LAVA_CHICKEN, ModelTemplates.MUSIC_DISC);
/*  658 */     generateFlatItem(Items.MUSIC_DISC_MALL, ModelTemplates.MUSIC_DISC);
/*  659 */     generateFlatItem(Items.MUSIC_DISC_MELLOHI, ModelTemplates.MUSIC_DISC);
/*  660 */     generateFlatItem(Items.MUSIC_DISC_PIGSTEP, ModelTemplates.MUSIC_DISC);
/*  661 */     generateFlatItem(Items.MUSIC_DISC_PRECIPICE, ModelTemplates.MUSIC_DISC);
/*  662 */     generateFlatItem(Items.MUSIC_DISC_STAL, ModelTemplates.MUSIC_DISC);
/*  663 */     generateFlatItem(Items.MUSIC_DISC_STRAD, ModelTemplates.MUSIC_DISC);
/*  664 */     generateFlatItem(Items.MUSIC_DISC_WAIT, ModelTemplates.MUSIC_DISC);
/*  665 */     generateFlatItem(Items.MUSIC_DISC_WARD, ModelTemplates.MUSIC_DISC);
/*  666 */     generateFlatItem(Items.MUSIC_DISC_OTHERSIDE, ModelTemplates.MUSIC_DISC);
/*  667 */     generateFlatItem(Items.MUSIC_DISC_RELIC, ModelTemplates.MUSIC_DISC);
/*  668 */     generateFlatItem(Items.MUSIC_DISC_5, ModelTemplates.MUSIC_DISC);
/*  669 */     generateFlatItem(Items.MUSIC_DISC_TEARS, ModelTemplates.MUSIC_DISC);
/*  670 */     generateFlatItem(Items.MUTTON, ModelTemplates.FLAT_ITEM);
/*  671 */     generateFlatItem(Items.NAME_TAG, ModelTemplates.FLAT_ITEM);
/*  672 */     generateFlatItem(Items.NAUTILUS_SHELL, ModelTemplates.FLAT_ITEM);
/*  673 */     generateFlatItem(Items.NETHERITE_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  674 */     generateFlatItem(Items.NETHERITE_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  675 */     generateFlatItem(Items.NETHERITE_INGOT, ModelTemplates.FLAT_ITEM);
/*  676 */     generateFlatItem(Items.NETHERITE_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  677 */     generateFlatItem(Items.NETHERITE_SCRAP, ModelTemplates.FLAT_ITEM);
/*  678 */     generateFlatItem(Items.NETHERITE_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  679 */     generateFlatItem(Items.NETHERITE_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  680 */     generateFlatItem(Items.NETHERITE_NAUTILUS_ARMOR, ModelTemplates.FLAT_ITEM);
/*  681 */     generateFlatItem(Items.NETHERITE_HORSE_ARMOR, ModelTemplates.FLAT_ITEM);
/*  682 */     generateFlatItem(Items.NETHER_BRICK, ModelTemplates.FLAT_ITEM);
/*  683 */     generateFlatItem(Items.RESIN_BRICK, ModelTemplates.FLAT_ITEM);
/*  684 */     generateFlatItem(Items.NETHER_STAR, ModelTemplates.FLAT_ITEM);
/*  685 */     generateFlatItem(Items.OAK_BOAT, ModelTemplates.FLAT_ITEM);
/*  686 */     generateFlatItem(Items.OAK_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
/*  687 */     generateFlatItem(Items.ORANGE_DYE, ModelTemplates.FLAT_ITEM);
/*  688 */     generateFlatItem(Items.PAINTING, ModelTemplates.FLAT_ITEM);
/*  689 */     generateFlatItem(Items.PALE_OAK_BOAT, ModelTemplates.FLAT_ITEM);
/*  690 */     generateFlatItem(Items.PALE_OAK_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
/*  691 */     generateFlatItem(Items.PAPER, ModelTemplates.FLAT_ITEM);
/*  692 */     generateFlatItem(Items.PHANTOM_MEMBRANE, ModelTemplates.FLAT_ITEM);
/*  693 */     generateFlatItem(Items.PIGLIN_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  694 */     generateFlatItem(Items.PINK_DYE, ModelTemplates.FLAT_ITEM);
/*  695 */     generateFlatItem(Items.POISONOUS_POTATO, ModelTemplates.FLAT_ITEM);
/*  696 */     generateFlatItem(Items.POPPED_CHORUS_FRUIT, ModelTemplates.FLAT_ITEM);
/*  697 */     generateFlatItem(Items.PORKCHOP, ModelTemplates.FLAT_ITEM);
/*  698 */     generateFlatItem(Items.POWDER_SNOW_BUCKET, ModelTemplates.FLAT_ITEM);
/*  699 */     generateFlatItem(Items.PRISMARINE_CRYSTALS, ModelTemplates.FLAT_ITEM);
/*  700 */     generateFlatItem(Items.PRISMARINE_SHARD, ModelTemplates.FLAT_ITEM);
/*  701 */     generateFlatItem(Items.PUFFERFISH, ModelTemplates.FLAT_ITEM);
/*  702 */     generateFlatItem(Items.PUFFERFISH_BUCKET, ModelTemplates.FLAT_ITEM);
/*  703 */     generateFlatItem(Items.PUMPKIN_PIE, ModelTemplates.FLAT_ITEM);
/*  704 */     generateFlatItem(Items.PURPLE_DYE, ModelTemplates.FLAT_ITEM);
/*  705 */     generateFlatItem(Items.QUARTZ, ModelTemplates.FLAT_ITEM);
/*  706 */     generateFlatItem(Items.RABBIT, ModelTemplates.FLAT_ITEM);
/*  707 */     generateFlatItem(Items.RABBIT_FOOT, ModelTemplates.FLAT_ITEM);
/*  708 */     generateFlatItem(Items.RABBIT_HIDE, ModelTemplates.FLAT_ITEM);
/*  709 */     generateFlatItem(Items.RABBIT_STEW, ModelTemplates.FLAT_ITEM);
/*  710 */     generateFlatItem(Items.RED_DYE, ModelTemplates.FLAT_ITEM);
/*  711 */     generateFlatItem(Items.ROTTEN_FLESH, ModelTemplates.FLAT_ITEM);
/*  712 */     generateFlatItem(Items.SADDLE, ModelTemplates.FLAT_ITEM);
/*  713 */     generateFlatItem(Items.SALMON, ModelTemplates.FLAT_ITEM);
/*  714 */     generateFlatItem(Items.SALMON_BUCKET, ModelTemplates.FLAT_ITEM);
/*  715 */     generateFlatItem(Items.TURTLE_SCUTE, ModelTemplates.FLAT_ITEM);
/*  716 */     generateFlatItem(Items.SHEARS, ModelTemplates.FLAT_ITEM);
/*  717 */     generateFlatItem(Items.SHULKER_SHELL, ModelTemplates.FLAT_ITEM);
/*  718 */     generateFlatItem(Items.SKULL_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  719 */     generateFlatItem(Items.SLIME_BALL, ModelTemplates.FLAT_ITEM);
/*  720 */     generateFlatItem(Items.SNOWBALL, ModelTemplates.FLAT_ITEM);
/*  721 */     generateFlatItem(Items.ECHO_SHARD, ModelTemplates.FLAT_ITEM);
/*  722 */     generateFlatItem(Items.SPECTRAL_ARROW, ModelTemplates.FLAT_ITEM);
/*  723 */     generateFlatItem(Items.SPIDER_EYE, ModelTemplates.FLAT_ITEM);
/*  724 */     generateFlatItem(Items.SPRUCE_BOAT, ModelTemplates.FLAT_ITEM);
/*  725 */     generateFlatItem(Items.SPRUCE_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
/*  726 */     generateFlatItem(Items.STICK, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  727 */     generateFlatItem(Items.STONE_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  728 */     generateFlatItem(Items.STONE_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  729 */     generateFlatItem(Items.STONE_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  730 */     generateFlatItem(Items.STONE_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  731 */     generateFlatItem(Items.STONE_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  732 */     generateFlatItem(Items.SUGAR, ModelTemplates.FLAT_ITEM);
/*  733 */     generateFlatItem(Items.SUSPICIOUS_STEW, ModelTemplates.FLAT_ITEM);
/*  734 */     generateFlatItem(Items.TNT_MINECART, ModelTemplates.FLAT_ITEM);
/*  735 */     generateFlatItem(Items.TOTEM_OF_UNDYING, ModelTemplates.FLAT_ITEM);
/*  736 */     generateFlatItem(Items.TROPICAL_FISH, ModelTemplates.FLAT_ITEM);
/*  737 */     generateFlatItem(Items.TROPICAL_FISH_BUCKET, ModelTemplates.FLAT_ITEM);
/*  738 */     generateFlatItem(Items.AXOLOTL_BUCKET, ModelTemplates.FLAT_ITEM);
/*  739 */     generateFlatItem(Items.TADPOLE_BUCKET, ModelTemplates.FLAT_ITEM);
/*  740 */     generateFlatItem(Items.WATER_BUCKET, ModelTemplates.FLAT_ITEM);
/*  741 */     generateFlatItem(Items.WHEAT, ModelTemplates.FLAT_ITEM);
/*  742 */     generateFlatItem(Items.WHITE_DYE, ModelTemplates.FLAT_ITEM);
/*  743 */     generateFlatItem(Items.WIND_CHARGE, ModelTemplates.FLAT_ITEM);
/*  744 */     generateFlatItem(Items.MACE, ModelTemplates.FLAT_HANDHELD_MACE_ITEM);
/*  745 */     generateFlatItem(Items.WOODEN_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  746 */     generateFlatItem(Items.WOODEN_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  747 */     generateFlatItem(Items.WOODEN_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  748 */     generateFlatItem(Items.WOODEN_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  749 */     generateFlatItem(Items.WOODEN_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  750 */     generateFlatItem(Items.WRITABLE_BOOK, ModelTemplates.FLAT_ITEM);
/*  751 */     generateFlatItem(Items.WRITTEN_BOOK, ModelTemplates.FLAT_ITEM);
/*  752 */     generateFlatItem(Items.YELLOW_DYE, ModelTemplates.FLAT_ITEM);
/*      */     
/*  754 */     generateFlatItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  755 */     generateFlatItem(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  756 */     generateFlatItem(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  757 */     generateFlatItem(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  758 */     generateFlatItem(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  759 */     generateFlatItem(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  760 */     generateFlatItem(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  761 */     generateFlatItem(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  762 */     generateFlatItem(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  763 */     generateFlatItem(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  764 */     generateFlatItem(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  765 */     generateFlatItem(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  766 */     generateFlatItem(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  767 */     generateFlatItem(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  768 */     generateFlatItem(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  769 */     generateFlatItem(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  770 */     generateFlatItem(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  771 */     generateFlatItem(Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  772 */     generateFlatItem(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*      */     
/*  774 */     generateFlatItem(Items.DEBUG_STICK, Items.STICK, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  775 */     generateFlatItem(Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_APPLE, ModelTemplates.FLAT_ITEM);
/*      */     
/*  777 */     generateTrimmableItem(Items.TURTLE_HELMET, EquipmentAssets.TURTLE_SCUTE, TRIM_PREFIX_HELMET, false);
/*      */     
/*  779 */     generateTrimmableItem(Items.LEATHER_HELMET, EquipmentAssets.LEATHER, TRIM_PREFIX_HELMET, true);
/*  780 */     generateTrimmableItem(Items.LEATHER_CHESTPLATE, EquipmentAssets.LEATHER, TRIM_PREFIX_CHESTPLATE, true);
/*  781 */     generateTrimmableItem(Items.LEATHER_LEGGINGS, EquipmentAssets.LEATHER, TRIM_PREFIX_LEGGINGS, true);
/*  782 */     generateTrimmableItem(Items.LEATHER_BOOTS, EquipmentAssets.LEATHER, TRIM_PREFIX_BOOTS, true);
/*      */     
/*  784 */     generateTrimmableItem(Items.COPPER_HELMET, EquipmentAssets.COPPER, TRIM_PREFIX_HELMET, false);
/*  785 */     generateTrimmableItem(Items.COPPER_CHESTPLATE, EquipmentAssets.COPPER, TRIM_PREFIX_CHESTPLATE, false);
/*  786 */     generateTrimmableItem(Items.COPPER_LEGGINGS, EquipmentAssets.COPPER, TRIM_PREFIX_LEGGINGS, false);
/*  787 */     generateTrimmableItem(Items.COPPER_BOOTS, EquipmentAssets.COPPER, TRIM_PREFIX_BOOTS, false);
/*      */     
/*  789 */     generateTrimmableItem(Items.CHAINMAIL_HELMET, EquipmentAssets.CHAINMAIL, TRIM_PREFIX_HELMET, false);
/*  790 */     generateTrimmableItem(Items.CHAINMAIL_CHESTPLATE, EquipmentAssets.CHAINMAIL, TRIM_PREFIX_CHESTPLATE, false);
/*  791 */     generateTrimmableItem(Items.CHAINMAIL_LEGGINGS, EquipmentAssets.CHAINMAIL, TRIM_PREFIX_LEGGINGS, false);
/*  792 */     generateTrimmableItem(Items.CHAINMAIL_BOOTS, EquipmentAssets.CHAINMAIL, TRIM_PREFIX_BOOTS, false);
/*      */     
/*  794 */     generateTrimmableItem(Items.IRON_HELMET, EquipmentAssets.IRON, TRIM_PREFIX_HELMET, false);
/*  795 */     generateTrimmableItem(Items.IRON_CHESTPLATE, EquipmentAssets.IRON, TRIM_PREFIX_CHESTPLATE, false);
/*  796 */     generateTrimmableItem(Items.IRON_LEGGINGS, EquipmentAssets.IRON, TRIM_PREFIX_LEGGINGS, false);
/*  797 */     generateTrimmableItem(Items.IRON_BOOTS, EquipmentAssets.IRON, TRIM_PREFIX_BOOTS, false);
/*      */     
/*  799 */     generateTrimmableItem(Items.DIAMOND_HELMET, EquipmentAssets.DIAMOND, TRIM_PREFIX_HELMET, false);
/*  800 */     generateTrimmableItem(Items.DIAMOND_CHESTPLATE, EquipmentAssets.DIAMOND, TRIM_PREFIX_CHESTPLATE, false);
/*  801 */     generateTrimmableItem(Items.DIAMOND_LEGGINGS, EquipmentAssets.DIAMOND, TRIM_PREFIX_LEGGINGS, false);
/*  802 */     generateTrimmableItem(Items.DIAMOND_BOOTS, EquipmentAssets.DIAMOND, TRIM_PREFIX_BOOTS, false);
/*      */     
/*  804 */     generateTrimmableItem(Items.GOLDEN_HELMET, EquipmentAssets.GOLD, TRIM_PREFIX_HELMET, false);
/*  805 */     generateTrimmableItem(Items.GOLDEN_CHESTPLATE, EquipmentAssets.GOLD, TRIM_PREFIX_CHESTPLATE, false);
/*  806 */     generateTrimmableItem(Items.GOLDEN_LEGGINGS, EquipmentAssets.GOLD, TRIM_PREFIX_LEGGINGS, false);
/*  807 */     generateTrimmableItem(Items.GOLDEN_BOOTS, EquipmentAssets.GOLD, TRIM_PREFIX_BOOTS, false);
/*      */     
/*  809 */     generateTrimmableItem(Items.NETHERITE_HELMET, EquipmentAssets.NETHERITE, TRIM_PREFIX_HELMET, false);
/*  810 */     generateTrimmableItem(Items.NETHERITE_CHESTPLATE, EquipmentAssets.NETHERITE, TRIM_PREFIX_CHESTPLATE, false);
/*  811 */     generateTrimmableItem(Items.NETHERITE_LEGGINGS, EquipmentAssets.NETHERITE, TRIM_PREFIX_LEGGINGS, false);
/*  812 */     generateTrimmableItem(Items.NETHERITE_BOOTS, EquipmentAssets.NETHERITE, TRIM_PREFIX_BOOTS, false);
/*      */     
/*  814 */     generateItemWithTintedBaseLayer(Items.LEATHER_HORSE_ARMOR, -6265536);
/*      */     
/*  816 */     generateFlatItem(Items.ANGLER_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  817 */     generateFlatItem(Items.ARCHER_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  818 */     generateFlatItem(Items.ARMS_UP_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  819 */     generateFlatItem(Items.BLADE_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  820 */     generateFlatItem(Items.BREWER_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  821 */     generateFlatItem(Items.BURN_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  822 */     generateFlatItem(Items.DANGER_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  823 */     generateFlatItem(Items.EXPLORER_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  824 */     generateFlatItem(Items.FLOW_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  825 */     generateFlatItem(Items.FRIEND_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  826 */     generateFlatItem(Items.GUSTER_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  827 */     generateFlatItem(Items.HEART_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  828 */     generateFlatItem(Items.HEARTBREAK_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  829 */     generateFlatItem(Items.HOWL_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  830 */     generateFlatItem(Items.MINER_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  831 */     generateFlatItem(Items.MOURNER_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  832 */     generateFlatItem(Items.PLENTY_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  833 */     generateFlatItem(Items.PRIZE_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  834 */     generateFlatItem(Items.SCRAPE_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  835 */     generateFlatItem(Items.SHEAF_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  836 */     generateFlatItem(Items.SHELTER_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  837 */     generateFlatItem(Items.SKULL_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  838 */     generateFlatItem(Items.SNORT_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  839 */     generateFlatItem(Items.TRIAL_KEY, ModelTemplates.FLAT_ITEM);
/*  840 */     generateFlatItem(Items.OMINOUS_TRIAL_KEY, ModelTemplates.FLAT_ITEM);
/*  841 */     generateFlatItem(Items.OMINOUS_BOTTLE, ModelTemplates.FLAT_ITEM);
/*      */     
/*  843 */     generateItemWithTintedOverlay(Items.FIREWORK_STAR, (ItemTintSource)new Firework());
/*  844 */     generateItemWithTintedOverlay(Items.FILLED_MAP, "_markings", (ItemTintSource)new MapColor());
/*      */     
/*  846 */     generateBundleModels(Items.BUNDLE);
/*  847 */     generateBundleModels(Items.BLACK_BUNDLE);
/*  848 */     generateBundleModels(Items.WHITE_BUNDLE);
/*  849 */     generateBundleModels(Items.GRAY_BUNDLE);
/*  850 */     generateBundleModels(Items.LIGHT_GRAY_BUNDLE);
/*  851 */     generateBundleModels(Items.LIGHT_BLUE_BUNDLE);
/*  852 */     generateBundleModels(Items.BLUE_BUNDLE);
/*  853 */     generateBundleModels(Items.CYAN_BUNDLE);
/*  854 */     generateBundleModels(Items.YELLOW_BUNDLE);
/*  855 */     generateBundleModels(Items.RED_BUNDLE);
/*  856 */     generateBundleModels(Items.PURPLE_BUNDLE);
/*  857 */     generateBundleModels(Items.MAGENTA_BUNDLE);
/*  858 */     generateBundleModels(Items.PINK_BUNDLE);
/*  859 */     generateBundleModels(Items.GREEN_BUNDLE);
/*  860 */     generateBundleModels(Items.LIME_BUNDLE);
/*  861 */     generateBundleModels(Items.BROWN_BUNDLE);
/*  862 */     generateBundleModels(Items.ORANGE_BUNDLE);
/*      */     
/*  864 */     generateSpyglass(Items.SPYGLASS);
/*  865 */     generateTrident(Items.TRIDENT);
/*  866 */     generateTwoLayerDyedItem(Items.WOLF_ARMOR);
/*      */     
/*  868 */     generateFlatItem(Items.WHITE_HARNESS, ModelTemplates.FLAT_ITEM);
/*  869 */     generateFlatItem(Items.ORANGE_HARNESS, ModelTemplates.FLAT_ITEM);
/*  870 */     generateFlatItem(Items.MAGENTA_HARNESS, ModelTemplates.FLAT_ITEM);
/*  871 */     generateFlatItem(Items.LIGHT_BLUE_HARNESS, ModelTemplates.FLAT_ITEM);
/*  872 */     generateFlatItem(Items.YELLOW_HARNESS, ModelTemplates.FLAT_ITEM);
/*  873 */     generateFlatItem(Items.LIME_HARNESS, ModelTemplates.FLAT_ITEM);
/*  874 */     generateFlatItem(Items.PINK_HARNESS, ModelTemplates.FLAT_ITEM);
/*  875 */     generateFlatItem(Items.GRAY_HARNESS, ModelTemplates.FLAT_ITEM);
/*  876 */     generateFlatItem(Items.LIGHT_GRAY_HARNESS, ModelTemplates.FLAT_ITEM);
/*  877 */     generateFlatItem(Items.CYAN_HARNESS, ModelTemplates.FLAT_ITEM);
/*  878 */     generateFlatItem(Items.PURPLE_HARNESS, ModelTemplates.FLAT_ITEM);
/*  879 */     generateFlatItem(Items.BLUE_HARNESS, ModelTemplates.FLAT_ITEM);
/*  880 */     generateFlatItem(Items.BROWN_HARNESS, ModelTemplates.FLAT_ITEM);
/*  881 */     generateFlatItem(Items.GREEN_HARNESS, ModelTemplates.FLAT_ITEM);
/*  882 */     generateFlatItem(Items.RED_HARNESS, ModelTemplates.FLAT_ITEM);
/*  883 */     generateFlatItem(Items.BLACK_HARNESS, ModelTemplates.FLAT_ITEM);
/*      */     
/*  885 */     generateBow(Items.BOW);
/*  886 */     generateCrossbow(Items.CROSSBOW);
/*  887 */     generateElytra(Items.ELYTRA);
/*  888 */     generateBrush(Items.BRUSH);
/*  889 */     generateFishingRod(Items.FISHING_ROD);
/*  890 */     generateGoatHorn(Items.GOAT_HORN);
/*  891 */     generateShield(Items.SHIELD);
/*      */     
/*  893 */     generateSpear(Items.WOODEN_SPEAR);
/*  894 */     generateSpear(Items.STONE_SPEAR);
/*  895 */     generateSpear(Items.COPPER_SPEAR);
/*  896 */     generateSpear(Items.GOLDEN_SPEAR);
/*  897 */     generateSpear(Items.IRON_SPEAR);
/*  898 */     generateSpear(Items.DIAMOND_SPEAR);
/*  899 */     generateSpear(Items.NETHERITE_SPEAR);
/*      */     
/*  901 */     generateTippedArrow(Items.TIPPED_ARROW);
/*  902 */     generatePotion(Items.POTION);
/*  903 */     generatePotion(Items.SPLASH_POTION);
/*  904 */     generatePotion(Items.LINGERING_POTION);
/*      */ 
/*      */ 
/*      */     
/*  908 */     generateFlatItem(Items.CHICKEN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  909 */     generateFlatItem(Items.COW_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  910 */     generateFlatItem(Items.PIG_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  911 */     generateFlatItem(Items.SHEEP_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*      */ 
/*      */     
/*  914 */     generateFlatItem(Items.CAMEL_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  915 */     generateFlatItem(Items.DONKEY_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  916 */     generateFlatItem(Items.HORSE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  917 */     generateFlatItem(Items.MULE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*      */ 
/*      */     
/*  920 */     generateFlatItem(Items.CAT_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  921 */     generateFlatItem(Items.PARROT_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  922 */     generateFlatItem(Items.WOLF_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*      */ 
/*      */     
/*  925 */     generateFlatItem(Items.ARMADILLO_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  926 */     generateFlatItem(Items.BAT_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  927 */     generateFlatItem(Items.BEE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  928 */     generateFlatItem(Items.FOX_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  929 */     generateFlatItem(Items.GOAT_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  930 */     generateFlatItem(Items.LLAMA_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  931 */     generateFlatItem(Items.OCELOT_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  932 */     generateFlatItem(Items.PANDA_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  933 */     generateFlatItem(Items.POLAR_BEAR_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  934 */     generateFlatItem(Items.RABBIT_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*      */ 
/*      */     
/*  937 */     generateFlatItem(Items.AXOLOTL_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  938 */     generateFlatItem(Items.COD_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  939 */     generateFlatItem(Items.DOLPHIN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  940 */     generateFlatItem(Items.FROG_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  941 */     generateFlatItem(Items.GLOW_SQUID_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  942 */     generateFlatItem(Items.NAUTILUS_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  943 */     generateFlatItem(Items.PUFFERFISH_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  944 */     generateFlatItem(Items.SALMON_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  945 */     generateFlatItem(Items.SQUID_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  946 */     generateFlatItem(Items.TADPOLE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  947 */     generateFlatItem(Items.TROPICAL_FISH_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  948 */     generateFlatItem(Items.TURTLE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*      */ 
/*      */     
/*  951 */     generateFlatItem(Items.ALLAY_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  952 */     generateFlatItem(Items.MOOSHROOM_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  953 */     generateFlatItem(Items.SNIFFER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*      */ 
/*      */     
/*  956 */     generateFlatItem(Items.COPPER_GOLEM_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  957 */     generateFlatItem(Items.IRON_GOLEM_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  958 */     generateFlatItem(Items.SNOW_GOLEM_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*      */ 
/*      */     
/*  961 */     generateFlatItem(Items.TRADER_LLAMA_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  962 */     generateFlatItem(Items.VILLAGER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  963 */     generateFlatItem(Items.WANDERING_TRADER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*      */ 
/*      */     
/*  966 */     generateFlatItem(Items.BOGGED_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  967 */     generateFlatItem(Items.CAMEL_HUSK_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  968 */     generateFlatItem(Items.DROWNED_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  969 */     generateFlatItem(Items.HUSK_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  970 */     generateFlatItem(Items.PARCHED_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  971 */     generateFlatItem(Items.SKELETON_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  972 */     generateFlatItem(Items.SKELETON_HORSE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  973 */     generateFlatItem(Items.STRAY_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  974 */     generateFlatItem(Items.WITHER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  975 */     generateFlatItem(Items.WITHER_SKELETON_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  976 */     generateFlatItem(Items.ZOMBIE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  977 */     generateFlatItem(Items.ZOMBIE_HORSE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  978 */     generateFlatItem(Items.ZOMBIE_NAUTILUS_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  979 */     generateFlatItem(Items.ZOMBIE_VILLAGER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*      */ 
/*      */ 
/*      */     
/*  983 */     generateFlatItem(Items.CAVE_SPIDER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  984 */     generateFlatItem(Items.SPIDER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*      */ 
/*      */     
/*  987 */     generateFlatItem(Items.BREEZE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  988 */     generateFlatItem(Items.CREAKING_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  989 */     generateFlatItem(Items.CREEPER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  990 */     generateFlatItem(Items.ELDER_GUARDIAN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  991 */     generateFlatItem(Items.GUARDIAN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  992 */     generateFlatItem(Items.PHANTOM_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  993 */     generateFlatItem(Items.SILVERFISH_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  994 */     generateFlatItem(Items.SLIME_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  995 */     generateFlatItem(Items.WARDEN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  996 */     generateFlatItem(Items.WITCH_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*      */ 
/*      */     
/*  999 */     generateFlatItem(Items.EVOKER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/* 1000 */     generateFlatItem(Items.PILLAGER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/* 1001 */     generateFlatItem(Items.RAVAGER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/* 1002 */     generateFlatItem(Items.VEX_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/* 1003 */     generateFlatItem(Items.VINDICATOR_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*      */ 
/*      */     
/* 1006 */     generateFlatItem(Items.BLAZE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/* 1007 */     generateFlatItem(Items.GHAST_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/* 1008 */     generateFlatItem(Items.HAPPY_GHAST_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/* 1009 */     generateFlatItem(Items.HOGLIN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/* 1010 */     generateFlatItem(Items.MAGMA_CUBE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/* 1011 */     generateFlatItem(Items.PIGLIN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/* 1012 */     generateFlatItem(Items.PIGLIN_BRUTE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/* 1013 */     generateFlatItem(Items.STRIDER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/* 1014 */     generateFlatItem(Items.ZOGLIN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/* 1015 */     generateFlatItem(Items.ZOMBIFIED_PIGLIN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*      */ 
/*      */     
/* 1018 */     generateFlatItem(Items.ENDER_DRAGON_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/* 1019 */     generateFlatItem(Items.ENDERMAN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/* 1020 */     generateFlatItem(Items.ENDERMITE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/* 1021 */     generateFlatItem(Items.SHULKER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*      */     
/* 1023 */     declareCustomModelItem(Items.AIR);
/* 1024 */     declareCustomModelItem(Items.AMETHYST_CLUSTER);
/* 1025 */     declareCustomModelItem(Items.SMALL_AMETHYST_BUD);
/* 1026 */     declareCustomModelItem(Items.MEDIUM_AMETHYST_BUD);
/* 1027 */     declareCustomModelItem(Items.LARGE_AMETHYST_BUD);
/* 1028 */     declareCustomModelItem(Items.SMALL_DRIPLEAF);
/* 1029 */     declareCustomModelItem(Items.BIG_DRIPLEAF);
/* 1030 */     declareCustomModelItem(Items.HANGING_ROOTS);
/* 1031 */     declareCustomModelItem(Items.POINTED_DRIPSTONE);
/* 1032 */     declareCustomModelItem(Items.BONE);
/* 1033 */     declareCustomModelItem(Items.COD);
/* 1034 */     declareCustomModelItem(Items.FEATHER);
/* 1035 */     declareCustomModelItem(Items.LEAD);
/*      */   }
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/models/ItemModelGenerators.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */