/*      */ package net.minecraft.client.data.models;
/*      */ 
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.math.Quadrant;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.IntStream;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.client.color.item.GrassColorSource;
/*      */ import net.minecraft.client.color.item.ItemTintSource;
/*      */ import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
/*      */ import net.minecraft.client.data.models.blockstates.ConditionBuilder;
/*      */ import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
/*      */ import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
/*      */ import net.minecraft.client.data.models.blockstates.PropertyDispatch;
/*      */ import net.minecraft.client.data.models.model.ItemModelUtils;
/*      */ import net.minecraft.client.data.models.model.ModelInstance;
/*      */ import net.minecraft.client.data.models.model.ModelLocationUtils;
/*      */ import net.minecraft.client.data.models.model.ModelTemplate;
/*      */ import net.minecraft.client.data.models.model.ModelTemplates;
/*      */ import net.minecraft.client.data.models.model.TextureMapping;
/*      */ import net.minecraft.client.data.models.model.TextureSlot;
/*      */ import net.minecraft.client.data.models.model.TexturedModel;
/*      */ import net.minecraft.client.renderer.block.model.Variant;
/*      */ import net.minecraft.client.renderer.block.model.VariantMutator;
/*      */ import net.minecraft.client.renderer.block.model.multipart.CombinedCondition;
/*      */ import net.minecraft.client.renderer.block.model.multipart.Condition;
/*      */ import net.minecraft.client.renderer.item.ItemModel;
/*      */ import net.minecraft.client.renderer.special.BannerSpecialRenderer;
/*      */ import net.minecraft.client.renderer.special.BedSpecialRenderer;
/*      */ import net.minecraft.client.renderer.special.ChestSpecialRenderer;
/*      */ import net.minecraft.client.renderer.special.ConduitSpecialRenderer;
/*      */ import net.minecraft.client.renderer.special.CopperGolemStatueSpecialRenderer;
/*      */ import net.minecraft.client.renderer.special.DecoratedPotSpecialRenderer;
/*      */ import net.minecraft.client.renderer.special.PlayerHeadSpecialRenderer;
/*      */ import net.minecraft.client.renderer.special.ShulkerBoxSpecialRenderer;
/*      */ import net.minecraft.client.renderer.special.SkullSpecialRenderer;
/*      */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.FrontAndTop;
/*      */ import net.minecraft.data.BlockFamilies;
/*      */ import net.minecraft.data.BlockFamily;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.util.random.Weighted;
/*      */ import net.minecraft.util.random.WeightedList;
/*      */ import net.minecraft.world.item.DyeColor;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.level.block.BeehiveBlock;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.ChiseledBookShelfBlock;
/*      */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*      */ import net.minecraft.world.level.block.CrafterBlock;
/*      */ import net.minecraft.world.level.block.CreakingHeartBlock;
/*      */ import net.minecraft.world.level.block.DriedGhastBlock;
/*      */ import net.minecraft.world.level.block.HangingMossBlock;
/*      */ import net.minecraft.world.level.block.LayeredCauldronBlock;
/*      */ import net.minecraft.world.level.block.LightBlock;
/*      */ import net.minecraft.world.level.block.MangrovePropaguleBlock;
/*      */ import net.minecraft.world.level.block.MossyCarpetBlock;
/*      */ import net.minecraft.world.level.block.MultifaceBlock;
/*      */ import net.minecraft.world.level.block.PitcherCropBlock;
/*      */ import net.minecraft.world.level.block.SkullBlock;
/*      */ import net.minecraft.world.level.block.SnifferEggBlock;
/*      */ import net.minecraft.world.level.block.TestBlock;
/*      */ import net.minecraft.world.level.block.VaultBlock;
/*      */ import net.minecraft.world.level.block.WeatheringCopper;
/*      */ import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
/*      */ import net.minecraft.world.level.block.entity.vault.VaultState;
/*      */ import net.minecraft.world.level.block.state.StateHolder;
/*      */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*      */ import net.minecraft.world.level.block.state.properties.BambooLeaves;
/*      */ import net.minecraft.world.level.block.state.properties.BellAttachType;
/*      */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*      */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*      */ import net.minecraft.world.level.block.state.properties.ComparatorMode;
/*      */ import net.minecraft.world.level.block.state.properties.CreakingHeartState;
/*      */ import net.minecraft.world.level.block.state.properties.DoorHingeSide;
/*      */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*      */ import net.minecraft.world.level.block.state.properties.DripstoneThickness;
/*      */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*      */ import net.minecraft.world.level.block.state.properties.Half;
/*      */ import net.minecraft.world.level.block.state.properties.PistonType;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.block.state.properties.RailShape;
/*      */ import net.minecraft.world.level.block.state.properties.RedstoneSide;
/*      */ import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
/*      */ import net.minecraft.world.level.block.state.properties.SideChainPart;
/*      */ import net.minecraft.world.level.block.state.properties.SlabType;
/*      */ import net.minecraft.world.level.block.state.properties.StairsShape;
/*      */ import net.minecraft.world.level.block.state.properties.StructureMode;
/*      */ import net.minecraft.world.level.block.state.properties.TestBlockMode;
/*      */ import net.minecraft.world.level.block.state.properties.Tilt;
/*      */ import net.minecraft.world.level.block.state.properties.WallSide;
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
/*      */ public class BlockModelGenerators
/*      */ {
/*      */   private final Consumer<BlockModelDefinitionGenerator> blockStateOutput;
/*      */   private final ItemModelOutput itemModelOutput;
/*      */   private final BiConsumer<Identifier, ModelInstance> modelOutput;
/*  128 */   private static final List<Block> NON_ORIENTABLE_TRAPDOOR = List.of(Blocks.OAK_TRAPDOOR, Blocks.DARK_OAK_TRAPDOOR, Blocks.IRON_TRAPDOOR); public static final VariantMutator NOP;
/*      */   static {
/*  130 */     NOP = (v -> v);
/*      */   }
/*  132 */   public static final VariantMutator UV_LOCK = VariantMutator.UV_LOCK.withValue(true);
/*      */   
/*  134 */   public static final VariantMutator X_ROT_90 = VariantMutator.X_ROT.withValue(Quadrant.R90);
/*  135 */   public static final VariantMutator X_ROT_180 = VariantMutator.X_ROT.withValue(Quadrant.R180);
/*  136 */   public static final VariantMutator X_ROT_270 = VariantMutator.X_ROT.withValue(Quadrant.R270);
/*      */   
/*  138 */   public static final VariantMutator Y_ROT_90 = VariantMutator.Y_ROT.withValue(Quadrant.R90);
/*  139 */   public static final VariantMutator Y_ROT_180 = VariantMutator.Y_ROT.withValue(Quadrant.R180);
/*  140 */   public static final VariantMutator Y_ROT_270 = VariantMutator.Y_ROT.withValue(Quadrant.R270); private static final Function<ConditionBuilder, ConditionBuilder> FLOWER_BED_MODEL_1_SEGMENT_CONDITION; private static final Function<ConditionBuilder, ConditionBuilder> FLOWER_BED_MODEL_2_SEGMENT_CONDITION; private static final Function<ConditionBuilder, ConditionBuilder> FLOWER_BED_MODEL_3_SEGMENT_CONDITION; private static final Function<ConditionBuilder, ConditionBuilder> FLOWER_BED_MODEL_4_SEGMENT_CONDITION; private static final Function<ConditionBuilder, ConditionBuilder> LEAF_LITTER_MODEL_1_SEGMENT_CONDITION; private static final Function<ConditionBuilder, ConditionBuilder> LEAF_LITTER_MODEL_2_SEGMENT_CONDITION; private static final Function<ConditionBuilder, ConditionBuilder> LEAF_LITTER_MODEL_3_SEGMENT_CONDITION; private static final Function<ConditionBuilder, ConditionBuilder> LEAF_LITTER_MODEL_4_SEGMENT_CONDITION;
/*      */   static {
/*  142 */     FLOWER_BED_MODEL_1_SEGMENT_CONDITION = (condition -> condition);
/*  143 */     FLOWER_BED_MODEL_2_SEGMENT_CONDITION = (condition -> condition.term((Property)BlockStateProperties.FLOWER_AMOUNT, 2, (Comparable[])new Integer[] { 3, 4 }));
/*  144 */     FLOWER_BED_MODEL_3_SEGMENT_CONDITION = (condition -> condition.term((Property)BlockStateProperties.FLOWER_AMOUNT, 3, (Comparable[])new Integer[] { 4 }));
/*  145 */     FLOWER_BED_MODEL_4_SEGMENT_CONDITION = (condition -> condition.term((Property)BlockStateProperties.FLOWER_AMOUNT, 4));
/*      */     
/*  147 */     LEAF_LITTER_MODEL_1_SEGMENT_CONDITION = (condition -> condition.term((Property)BlockStateProperties.SEGMENT_AMOUNT, 1));
/*  148 */     LEAF_LITTER_MODEL_2_SEGMENT_CONDITION = (condition -> condition.term((Property)BlockStateProperties.SEGMENT_AMOUNT, 2, (Comparable[])new Integer[] { 3 }));
/*  149 */     LEAF_LITTER_MODEL_3_SEGMENT_CONDITION = (condition -> condition.term((Property)BlockStateProperties.SEGMENT_AMOUNT, 3));
/*  150 */     LEAF_LITTER_MODEL_4_SEGMENT_CONDITION = (condition -> condition.term((Property)BlockStateProperties.SEGMENT_AMOUNT, 4));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  157 */   private static final Map<Block, BlockStateGeneratorSupplier> FULL_BLOCK_MODEL_CUSTOM_GENERATORS = Map.of(Blocks.STONE, BlockModelGenerators::createMirroredCubeGenerator, Blocks.DEEPSLATE, BlockModelGenerators::createMirroredColumnGenerator, Blocks.MUD_BRICKS, BlockModelGenerators::createNorthWestMirroredCubeGenerator);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  163 */   private static final PropertyDispatch<VariantMutator> ROTATION_FACING = (PropertyDispatch<VariantMutator>)PropertyDispatch.modify((Property)BlockStateProperties.FACING)
/*  164 */     .select((Comparable)Direction.DOWN, X_ROT_90)
/*  165 */     .select((Comparable)Direction.UP, X_ROT_270)
/*  166 */     .select((Comparable)Direction.NORTH, NOP)
/*  167 */     .select((Comparable)Direction.SOUTH, Y_ROT_180)
/*  168 */     .select((Comparable)Direction.WEST, Y_ROT_270)
/*  169 */     .select((Comparable)Direction.EAST, Y_ROT_90);
/*      */   
/*  171 */   private static final PropertyDispatch<VariantMutator> ROTATIONS_COLUMN_WITH_FACING = (PropertyDispatch<VariantMutator>)PropertyDispatch.modify((Property)BlockStateProperties.FACING)
/*  172 */     .select((Comparable)Direction.DOWN, X_ROT_180)
/*  173 */     .select((Comparable)Direction.UP, NOP)
/*  174 */     .select((Comparable)Direction.NORTH, X_ROT_90)
/*  175 */     .select((Comparable)Direction.SOUTH, X_ROT_90.then(Y_ROT_180))
/*  176 */     .select((Comparable)Direction.WEST, X_ROT_90.then(Y_ROT_270))
/*  177 */     .select((Comparable)Direction.EAST, X_ROT_90.then(Y_ROT_90));
/*      */   
/*  179 */   private static final PropertyDispatch<VariantMutator> ROTATION_TORCH = (PropertyDispatch<VariantMutator>)PropertyDispatch.modify((Property)BlockStateProperties.HORIZONTAL_FACING)
/*  180 */     .select((Comparable)Direction.EAST, NOP)
/*  181 */     .select((Comparable)Direction.SOUTH, Y_ROT_90)
/*  182 */     .select((Comparable)Direction.WEST, Y_ROT_180)
/*  183 */     .select((Comparable)Direction.NORTH, Y_ROT_270);
/*      */   
/*  185 */   private static final PropertyDispatch<VariantMutator> ROTATION_HORIZONTAL_FACING_ALT = (PropertyDispatch<VariantMutator>)PropertyDispatch.modify((Property)BlockStateProperties.HORIZONTAL_FACING)
/*  186 */     .select((Comparable)Direction.SOUTH, NOP)
/*  187 */     .select((Comparable)Direction.WEST, Y_ROT_90)
/*  188 */     .select((Comparable)Direction.NORTH, Y_ROT_180)
/*  189 */     .select((Comparable)Direction.EAST, Y_ROT_270);
/*      */   
/*  191 */   private static final PropertyDispatch<VariantMutator> ROTATION_HORIZONTAL_FACING = (PropertyDispatch<VariantMutator>)PropertyDispatch.modify((Property)BlockStateProperties.HORIZONTAL_FACING)
/*  192 */     .select((Comparable)Direction.EAST, Y_ROT_90)
/*  193 */     .select((Comparable)Direction.SOUTH, Y_ROT_180)
/*  194 */     .select((Comparable)Direction.WEST, Y_ROT_270)
/*  195 */     .select((Comparable)Direction.NORTH, NOP); private static final Map<Block, TexturedModel> TEXTURED_MODELS;
/*      */   
/*      */   private static Variant plainModel(Identifier model) {
/*  198 */     return new Variant(model);
/*      */   }
/*      */   
/*      */   private static MultiVariant variant(Variant variant) {
/*  202 */     return new MultiVariant(WeightedList.of(variant));
/*      */   }
/*      */   
/*      */   private static MultiVariant variants(Variant... variant) {
/*  206 */     return new MultiVariant(WeightedList.of(Arrays.<Variant>stream(variant).map(v -> new Weighted(v, 1)).toList()));
/*      */   }
/*      */   
/*      */   private static MultiVariant plainVariant(Identifier model) {
/*  210 */     return variant(plainModel(model));
/*      */   }
/*      */   
/*      */   private static ConditionBuilder condition() {
/*  214 */     return new ConditionBuilder();
/*      */   }
/*      */   
/*      */   @SafeVarargs
/*      */   private static <T extends Enum<T> & net.minecraft.util.StringRepresentable> ConditionBuilder condition(EnumProperty<T> property, T term, T... additionalTerms) {
/*  219 */     return condition().term((Property)property, (Comparable)term, (Comparable[])additionalTerms);
/*      */   }
/*      */   
/*      */   private static ConditionBuilder condition(BooleanProperty property, boolean term) {
/*  223 */     return condition().term((Property)property, term);
/*      */   }
/*      */   
/*      */   private static Condition or(ConditionBuilder... terms) {
/*  227 */     return (Condition)new CombinedCondition(CombinedCondition.Operation.OR, Stream.<ConditionBuilder>of(terms).map(ConditionBuilder::build).toList());
/*      */   }
/*      */   
/*      */   private static Condition and(ConditionBuilder... terms) {
/*  231 */     return (Condition)new CombinedCondition(CombinedCondition.Operation.AND, Stream.<ConditionBuilder>of(terms).map(ConditionBuilder::build).toList());
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createMirroredCubeGenerator(Block block, Variant normal, TextureMapping mapping, BiConsumer<Identifier, ModelInstance> modelOutput) {
/*  235 */     Variant mirrored = plainModel(ModelTemplates.CUBE_MIRRORED_ALL.create(block, mapping, modelOutput));
/*  236 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block, createRotatedVariants(normal, mirrored));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createNorthWestMirroredCubeGenerator(Block block, Variant normal, TextureMapping mapping, BiConsumer<Identifier, ModelInstance> modelOutput) {
/*  240 */     MultiVariant northWestMirrored = plainVariant(ModelTemplates.CUBE_NORTH_WEST_MIRRORED_ALL.create(block, mapping, modelOutput));
/*  241 */     return (BlockModelDefinitionGenerator)createSimpleBlock(block, northWestMirrored);
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createMirroredColumnGenerator(Block block, Variant normal, TextureMapping mapping, BiConsumer<Identifier, ModelInstance> modelOutput) {
/*  245 */     Variant mirrored = plainModel(ModelTemplates.CUBE_COLUMN_MIRRORED.create(block, mapping, modelOutput));
/*  246 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block, createRotatedVariants(normal, mirrored))
/*  247 */       .with(createRotatedPillar());
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/*  272 */     TEXTURED_MODELS = (Map<Block, TexturedModel>)ImmutableMap.builder().put(Blocks.SANDSTONE, TexturedModel.TOP_BOTTOM_WITH_WALL.get(Blocks.SANDSTONE)).put(Blocks.RED_SANDSTONE, TexturedModel.TOP_BOTTOM_WITH_WALL.get(Blocks.RED_SANDSTONE)).put(Blocks.SMOOTH_SANDSTONE, TexturedModel.createAllSame(TextureMapping.getBlockTexture(Blocks.SANDSTONE, "_top"))).put(Blocks.SMOOTH_RED_SANDSTONE, TexturedModel.createAllSame(TextureMapping.getBlockTexture(Blocks.RED_SANDSTONE, "_top"))).put(Blocks.CUT_SANDSTONE, TexturedModel.COLUMN.get(Blocks.SANDSTONE).updateTextures(m -> m.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.CUT_SANDSTONE)))).put(Blocks.CUT_RED_SANDSTONE, TexturedModel.COLUMN.get(Blocks.RED_SANDSTONE).updateTextures(m -> m.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.CUT_RED_SANDSTONE)))).put(Blocks.QUARTZ_BLOCK, TexturedModel.COLUMN.get(Blocks.QUARTZ_BLOCK)).put(Blocks.SMOOTH_QUARTZ, TexturedModel.createAllSame(TextureMapping.getBlockTexture(Blocks.QUARTZ_BLOCK, "_bottom"))).put(Blocks.BLACKSTONE, TexturedModel.COLUMN_WITH_WALL.get(Blocks.BLACKSTONE)).put(Blocks.DEEPSLATE, TexturedModel.COLUMN_WITH_WALL.get(Blocks.DEEPSLATE)).put(Blocks.CHISELED_QUARTZ_BLOCK, TexturedModel.COLUMN.get(Blocks.CHISELED_QUARTZ_BLOCK).updateTextures(m -> m.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.CHISELED_QUARTZ_BLOCK)))).put(Blocks.CHISELED_SANDSTONE, TexturedModel.COLUMN.get(Blocks.CHISELED_SANDSTONE).updateTextures(m -> { m.put(TextureSlot.END, TextureMapping.getBlockTexture(Blocks.SANDSTONE, "_top")); m.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.CHISELED_SANDSTONE)); })).put(Blocks.CHISELED_RED_SANDSTONE, TexturedModel.COLUMN.get(Blocks.CHISELED_RED_SANDSTONE).updateTextures(m -> { m.put(TextureSlot.END, TextureMapping.getBlockTexture(Blocks.RED_SANDSTONE, "_top")); m.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.CHISELED_RED_SANDSTONE)); })).put(Blocks.CHISELED_TUFF_BRICKS, TexturedModel.COLUMN_WITH_WALL.get(Blocks.CHISELED_TUFF_BRICKS)).put(Blocks.CHISELED_TUFF, TexturedModel.COLUMN_WITH_WALL.get(Blocks.CHISELED_TUFF)).build();
/*      */   }
/*      */   public BlockModelGenerators(Consumer<BlockModelDefinitionGenerator> blockStateOutput, ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
/*  275 */     this.blockStateOutput = blockStateOutput;
/*  276 */     this.itemModelOutput = itemModelOutput;
/*  277 */     this.modelOutput = modelOutput;
/*      */   }
/*      */   
/*      */   private void registerSimpleItemModel(Item item, Identifier model) {
/*  281 */     this.itemModelOutput.accept(item, ItemModelUtils.plainModel(model));
/*      */   }
/*      */   
/*      */   private void registerSimpleItemModel(Block block, Identifier model) {
/*  285 */     this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(model));
/*      */   }
/*      */   
/*      */   private void registerSimpleTintedItemModel(Block block, Identifier model, ItemTintSource tint) {
/*  289 */     this.itemModelOutput.accept(block.asItem(), ItemModelUtils.tintedModel(model, new ItemTintSource[] { tint }));
/*      */   }
/*      */   
/*      */   private Identifier createFlatItemModel(Item item) {
/*  293 */     return ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), this.modelOutput);
/*      */   }
/*      */   
/*      */   private Identifier createFlatItemModelWithBlockTexture(Item item, Block block) {
/*  297 */     return ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(block), this.modelOutput);
/*      */   }
/*      */   
/*      */   private Identifier createFlatItemModelWithBlockTexture(Item item, Block block, String suffix) {
/*  301 */     return ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(TextureMapping.getBlockTexture(block, suffix)), this.modelOutput);
/*      */   }
/*      */   
/*      */   private Identifier createFlatItemModelWithBlockTextureAndOverlay(Item item, Block block, String overlaySuffix) {
/*  305 */     Identifier base = TextureMapping.getBlockTexture(block);
/*  306 */     Identifier overlay = TextureMapping.getBlockTexture(block, overlaySuffix);
/*  307 */     return ModelTemplates.TWO_LAYERED_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layered(base, overlay), this.modelOutput);
/*      */   }
/*      */   
/*      */   private void registerSimpleFlatItemModel(Item item) {
/*  311 */     registerSimpleItemModel(item, createFlatItemModel(item));
/*      */   }
/*      */   
/*      */   private void registerSimpleFlatItemModel(Block block) {
/*  315 */     Item blockItem = block.asItem();
/*  316 */     if (blockItem != Items.AIR) {
/*  317 */       registerSimpleItemModel(blockItem, createFlatItemModelWithBlockTexture(blockItem, block));
/*      */     }
/*      */   }
/*      */   
/*      */   private void registerSimpleFlatItemModel(Block block, String suffix) {
/*  322 */     Item blockItem = block.asItem();
/*  323 */     if (blockItem != Items.AIR) {
/*  324 */       registerSimpleItemModel(blockItem, createFlatItemModelWithBlockTexture(blockItem, block, suffix));
/*      */     }
/*      */   }
/*      */   
/*      */   private void registerTwoLayerFlatItemModel(Block block, String overlaySuffix) {
/*  329 */     Item blockItem = block.asItem();
/*  330 */     if (blockItem != Items.AIR) {
/*  331 */       Identifier model = createFlatItemModelWithBlockTextureAndOverlay(blockItem, block, overlaySuffix);
/*  332 */       registerSimpleItemModel(blockItem, model);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static MultiVariant createRotatedVariants(Variant base) {
/*  337 */     return variants(new Variant[] { base, 
/*      */           
/*  339 */           base.with(Y_ROT_90), 
/*  340 */           base.with(Y_ROT_180), 
/*  341 */           base.with(Y_ROT_270) });
/*      */   }
/*      */ 
/*      */   
/*      */   private static MultiVariant createRotatedVariants(Variant normal, Variant mirrored) {
/*  346 */     return variants(new Variant[] { normal, mirrored, 
/*      */ 
/*      */           
/*  349 */           normal.with(Y_ROT_180), 
/*  350 */           mirrored.with(Y_ROT_180) });
/*      */   }
/*      */ 
/*      */   
/*      */   private static PropertyDispatch<MultiVariant> createBooleanModelDispatch(BooleanProperty property, MultiVariant onTrue, MultiVariant onFalse) {
/*  355 */     return (PropertyDispatch<MultiVariant>)PropertyDispatch.initial((Property)property)
/*  356 */       .select(true, onTrue)
/*  357 */       .select(false, onFalse);
/*      */   }
/*      */   
/*      */   private void createRotatedMirroredVariantBlock(Block block) {
/*  361 */     Variant normal = plainModel(TexturedModel.CUBE.create(block, this.modelOutput));
/*  362 */     Variant mirrored = plainModel(TexturedModel.CUBE_MIRRORED.create(block, this.modelOutput));
/*  363 */     this.blockStateOutput.accept(
/*  364 */         MultiVariantGenerator.dispatch(block, createRotatedVariants(normal, mirrored)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createRotatedVariantBlock(Block block) {
/*  369 */     Variant normal = plainModel(TexturedModel.CUBE.create(block, this.modelOutput));
/*  370 */     this.blockStateOutput.accept(
/*  371 */         MultiVariantGenerator.dispatch(block, createRotatedVariants(normal)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createBrushableBlock(Block block) {
/*  376 */     this.blockStateOutput.accept(
/*  377 */         MultiVariantGenerator.dispatch(block)
/*  378 */         .with(
/*  379 */           PropertyDispatch.initial((Property)BlockStateProperties.DUSTED)
/*  380 */           .generate(dustProgress -> {
/*      */               String suffix = "_" + block;
/*      */ 
/*      */               
/*      */               Identifier texture = TextureMapping.getBlockTexture(block, suffix), model = ModelTemplates.CUBE_ALL.createWithSuffix(block, suffix, new TextureMapping().put(TextureSlot.ALL, texture), this.modelOutput);
/*      */               
/*      */               return plainVariant(model);
/*      */             })));
/*      */     
/*  389 */     registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block, "_0"));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createButton(Block block, MultiVariant normal, MultiVariant pressed) {
/*  393 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  394 */       .with(
/*  395 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.POWERED)
/*  396 */         .select(false, normal)
/*  397 */         .select(true, pressed))
/*      */       
/*  399 */       .with(
/*  400 */         (PropertyDispatch)PropertyDispatch.modify((Property)BlockStateProperties.ATTACH_FACE, (Property)BlockStateProperties.HORIZONTAL_FACING)
/*  401 */         .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.EAST, Y_ROT_90)
/*  402 */         .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.WEST, Y_ROT_270)
/*  403 */         .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.SOUTH, Y_ROT_180)
/*  404 */         .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.NORTH, NOP)
/*      */         
/*  406 */         .select((Comparable)AttachFace.WALL, (Comparable)Direction.EAST, Y_ROT_90.then(X_ROT_90).then(UV_LOCK))
/*  407 */         .select((Comparable)AttachFace.WALL, (Comparable)Direction.WEST, Y_ROT_270.then(X_ROT_90).then(UV_LOCK))
/*  408 */         .select((Comparable)AttachFace.WALL, (Comparable)Direction.SOUTH, Y_ROT_180.then(X_ROT_90).then(UV_LOCK))
/*  409 */         .select((Comparable)AttachFace.WALL, (Comparable)Direction.NORTH, X_ROT_90.then(UV_LOCK))
/*      */         
/*  411 */         .select((Comparable)AttachFace.CEILING, (Comparable)Direction.EAST, Y_ROT_270.then(X_ROT_180))
/*  412 */         .select((Comparable)AttachFace.CEILING, (Comparable)Direction.WEST, Y_ROT_90.then(X_ROT_180))
/*  413 */         .select((Comparable)AttachFace.CEILING, (Comparable)Direction.SOUTH, X_ROT_180)
/*  414 */         .select((Comparable)AttachFace.CEILING, (Comparable)Direction.NORTH, Y_ROT_180.then(X_ROT_180)));
/*      */   }
/*      */ 
/*      */   
/*      */   private static BlockModelDefinitionGenerator createDoor(Block block, MultiVariant bottomLeft, MultiVariant bottomLeftOpen, MultiVariant bottomRight, MultiVariant bottomRightOpen, MultiVariant topLeft, MultiVariant topLeftOpen, MultiVariant topRight, MultiVariant topRightOpen) {
/*  419 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  420 */       .with(
/*  421 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.HORIZONTAL_FACING, (Property)BlockStateProperties.DOUBLE_BLOCK_HALF, (Property)BlockStateProperties.DOOR_HINGE, (Property)BlockStateProperties.OPEN)
/*      */         
/*  423 */         .select((Comparable)Direction.EAST, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.LEFT, false, bottomLeft)
/*  424 */         .select((Comparable)Direction.SOUTH, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.LEFT, false, bottomLeft.with(Y_ROT_90))
/*  425 */         .select((Comparable)Direction.WEST, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.LEFT, false, bottomLeft.with(Y_ROT_180))
/*  426 */         .select((Comparable)Direction.NORTH, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.LEFT, false, bottomLeft.with(Y_ROT_270))
/*      */         
/*  428 */         .select((Comparable)Direction.EAST, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.RIGHT, false, bottomRight)
/*  429 */         .select((Comparable)Direction.SOUTH, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.RIGHT, false, bottomRight.with(Y_ROT_90))
/*  430 */         .select((Comparable)Direction.WEST, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.RIGHT, false, bottomRight.with(Y_ROT_180))
/*  431 */         .select((Comparable)Direction.NORTH, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.RIGHT, false, bottomRight.with(Y_ROT_270))
/*      */         
/*  433 */         .select((Comparable)Direction.EAST, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.LEFT, true, bottomLeftOpen.with(Y_ROT_90))
/*  434 */         .select((Comparable)Direction.SOUTH, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.LEFT, true, bottomLeftOpen.with(Y_ROT_180))
/*  435 */         .select((Comparable)Direction.WEST, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.LEFT, true, bottomLeftOpen.with(Y_ROT_270))
/*  436 */         .select((Comparable)Direction.NORTH, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.LEFT, true, bottomLeftOpen)
/*      */         
/*  438 */         .select((Comparable)Direction.EAST, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.RIGHT, true, bottomRightOpen.with(Y_ROT_270))
/*  439 */         .select((Comparable)Direction.SOUTH, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.RIGHT, true, bottomRightOpen)
/*  440 */         .select((Comparable)Direction.WEST, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.RIGHT, true, bottomRightOpen.with(Y_ROT_90))
/*  441 */         .select((Comparable)Direction.NORTH, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.RIGHT, true, bottomRightOpen.with(Y_ROT_180))
/*      */ 
/*      */         
/*  444 */         .select((Comparable)Direction.EAST, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.LEFT, false, topLeft)
/*  445 */         .select((Comparable)Direction.SOUTH, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.LEFT, false, topLeft.with(Y_ROT_90))
/*  446 */         .select((Comparable)Direction.WEST, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.LEFT, false, topLeft.with(Y_ROT_180))
/*  447 */         .select((Comparable)Direction.NORTH, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.LEFT, false, topLeft.with(Y_ROT_270))
/*      */         
/*  449 */         .select((Comparable)Direction.EAST, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.RIGHT, false, topRight)
/*  450 */         .select((Comparable)Direction.SOUTH, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.RIGHT, false, topRight.with(Y_ROT_90))
/*  451 */         .select((Comparable)Direction.WEST, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.RIGHT, false, topRight.with(Y_ROT_180))
/*  452 */         .select((Comparable)Direction.NORTH, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.RIGHT, false, topRight.with(Y_ROT_270))
/*      */         
/*  454 */         .select((Comparable)Direction.EAST, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.LEFT, true, topLeftOpen.with(Y_ROT_90))
/*  455 */         .select((Comparable)Direction.SOUTH, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.LEFT, true, topLeftOpen.with(Y_ROT_180))
/*  456 */         .select((Comparable)Direction.WEST, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.LEFT, true, topLeftOpen.with(Y_ROT_270))
/*  457 */         .select((Comparable)Direction.NORTH, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.LEFT, true, topLeftOpen)
/*      */         
/*  459 */         .select((Comparable)Direction.EAST, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.RIGHT, true, topRightOpen.with(Y_ROT_270))
/*  460 */         .select((Comparable)Direction.SOUTH, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.RIGHT, true, topRightOpen)
/*  461 */         .select((Comparable)Direction.WEST, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.RIGHT, true, topRightOpen.with(Y_ROT_90))
/*  462 */         .select((Comparable)Direction.NORTH, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.RIGHT, true, topRightOpen.with(Y_ROT_180)));
/*      */   }
/*      */ 
/*      */   
/*      */   private static BlockModelDefinitionGenerator createCustomFence(Block block, MultiVariant post, MultiVariant north, MultiVariant east, MultiVariant south, MultiVariant west) {
/*  467 */     return (BlockModelDefinitionGenerator)MultiPartGenerator.multiPart(block)
/*  468 */       .with(post)
/*  469 */       .with(condition().term((Property)BlockStateProperties.NORTH, true), north)
/*  470 */       .with(condition().term((Property)BlockStateProperties.EAST, true), east)
/*  471 */       .with(condition().term((Property)BlockStateProperties.SOUTH, true), south)
/*  472 */       .with(condition().term((Property)BlockStateProperties.WEST, true), west);
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createFence(Block block, MultiVariant post, MultiVariant side) {
/*  476 */     return (BlockModelDefinitionGenerator)MultiPartGenerator.multiPart(block)
/*  477 */       .with(post)
/*  478 */       .with(condition().term((Property)BlockStateProperties.NORTH, true), side.with(UV_LOCK))
/*  479 */       .with(condition().term((Property)BlockStateProperties.EAST, true), side.with(Y_ROT_90).with(UV_LOCK))
/*  480 */       .with(condition().term((Property)BlockStateProperties.SOUTH, true), side.with(Y_ROT_180).with(UV_LOCK))
/*  481 */       .with(condition().term((Property)BlockStateProperties.WEST, true), side.with(Y_ROT_270).with(UV_LOCK));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createWall(Block block, MultiVariant post, MultiVariant lowSide, MultiVariant tallSide) {
/*  485 */     return (BlockModelDefinitionGenerator)MultiPartGenerator.multiPart(block)
/*  486 */       .with(condition().term((Property)BlockStateProperties.UP, true), post)
/*      */       
/*  488 */       .with(condition().term((Property)BlockStateProperties.NORTH_WALL, (Comparable)WallSide.LOW), lowSide.with(UV_LOCK))
/*  489 */       .with(condition().term((Property)BlockStateProperties.EAST_WALL, (Comparable)WallSide.LOW), lowSide.with(Y_ROT_90).with(UV_LOCK))
/*  490 */       .with(condition().term((Property)BlockStateProperties.SOUTH_WALL, (Comparable)WallSide.LOW), lowSide.with(Y_ROT_180).with(UV_LOCK))
/*  491 */       .with(condition().term((Property)BlockStateProperties.WEST_WALL, (Comparable)WallSide.LOW), lowSide.with(Y_ROT_270).with(UV_LOCK))
/*      */       
/*  493 */       .with(condition().term((Property)BlockStateProperties.NORTH_WALL, (Comparable)WallSide.TALL), tallSide.with(UV_LOCK))
/*  494 */       .with(condition().term((Property)BlockStateProperties.EAST_WALL, (Comparable)WallSide.TALL), tallSide.with(Y_ROT_90).with(UV_LOCK))
/*  495 */       .with(condition().term((Property)BlockStateProperties.SOUTH_WALL, (Comparable)WallSide.TALL), tallSide.with(Y_ROT_180).with(UV_LOCK))
/*  496 */       .with(condition().term((Property)BlockStateProperties.WEST_WALL, (Comparable)WallSide.TALL), tallSide.with(Y_ROT_270).with(UV_LOCK));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createFenceGate(Block block, MultiVariant open, MultiVariant closed, MultiVariant openWall, MultiVariant closedWall, boolean uvLock) {
/*  500 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  501 */       .with(
/*  502 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.IN_WALL, (Property)BlockStateProperties.OPEN)
/*  503 */         .select(false, false, closed)
/*  504 */         .select(true, false, closedWall)
/*  505 */         .select(false, true, open)
/*  506 */         .select(true, true, openWall))
/*      */       
/*  508 */       .with(uvLock ? UV_LOCK : NOP)
/*  509 */       .with(ROTATION_HORIZONTAL_FACING_ALT);
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createStairs(Block block, MultiVariant inner, MultiVariant straight, MultiVariant outer) {
/*  513 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  514 */       .with(
/*  515 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.HORIZONTAL_FACING, (Property)BlockStateProperties.HALF, (Property)BlockStateProperties.STAIRS_SHAPE)
/*  516 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.STRAIGHT, straight)
/*  517 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.STRAIGHT, straight.with(Y_ROT_180).with(UV_LOCK))
/*  518 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.STRAIGHT, straight.with(Y_ROT_90).with(UV_LOCK))
/*  519 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.STRAIGHT, straight.with(Y_ROT_270).with(UV_LOCK))
/*  520 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_RIGHT, outer)
/*  521 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_RIGHT, outer.with(Y_ROT_180).with(UV_LOCK))
/*  522 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_RIGHT, outer.with(Y_ROT_90).with(UV_LOCK))
/*  523 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_RIGHT, outer.with(Y_ROT_270).with(UV_LOCK))
/*  524 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_LEFT, outer.with(Y_ROT_270).with(UV_LOCK))
/*  525 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_LEFT, outer.with(Y_ROT_90).with(UV_LOCK))
/*  526 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_LEFT, outer)
/*  527 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_LEFT, outer.with(Y_ROT_180).with(UV_LOCK))
/*  528 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_RIGHT, inner)
/*  529 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_RIGHT, inner.with(Y_ROT_180).with(UV_LOCK))
/*  530 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_RIGHT, inner.with(Y_ROT_90).with(UV_LOCK))
/*  531 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_RIGHT, inner.with(Y_ROT_270).with(UV_LOCK))
/*  532 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_LEFT, inner.with(Y_ROT_270).with(UV_LOCK))
/*  533 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_LEFT, inner.with(Y_ROT_90).with(UV_LOCK))
/*  534 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_LEFT, inner)
/*  535 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_LEFT, inner.with(Y_ROT_180).with(UV_LOCK))
/*  536 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, (Comparable)StairsShape.STRAIGHT, straight.with(X_ROT_180).with(UV_LOCK))
/*  537 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, (Comparable)StairsShape.STRAIGHT, straight.with(X_ROT_180).with(Y_ROT_180).with(UV_LOCK))
/*  538 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, (Comparable)StairsShape.STRAIGHT, straight.with(X_ROT_180).with(Y_ROT_90).with(UV_LOCK))
/*  539 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, (Comparable)StairsShape.STRAIGHT, straight.with(X_ROT_180).with(Y_ROT_270).with(UV_LOCK))
/*  540 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_RIGHT, outer.with(X_ROT_180).with(Y_ROT_90).with(UV_LOCK))
/*  541 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_RIGHT, outer.with(X_ROT_180).with(Y_ROT_270).with(UV_LOCK))
/*  542 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_RIGHT, outer.with(X_ROT_180).with(Y_ROT_180).with(UV_LOCK))
/*  543 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_RIGHT, outer.with(X_ROT_180).with(UV_LOCK))
/*  544 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_LEFT, outer.with(X_ROT_180).with(UV_LOCK))
/*  545 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_LEFT, outer.with(X_ROT_180).with(Y_ROT_180).with(UV_LOCK))
/*  546 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_LEFT, outer.with(X_ROT_180).with(Y_ROT_90).with(UV_LOCK))
/*  547 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_LEFT, outer.with(X_ROT_180).with(Y_ROT_270).with(UV_LOCK))
/*  548 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_RIGHT, inner.with(X_ROT_180).with(Y_ROT_90).with(UV_LOCK))
/*  549 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_RIGHT, inner.with(X_ROT_180).with(Y_ROT_270).with(UV_LOCK))
/*  550 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_RIGHT, inner.with(X_ROT_180).with(Y_ROT_180).with(UV_LOCK))
/*  551 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_RIGHT, inner.with(X_ROT_180).with(UV_LOCK))
/*  552 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_LEFT, inner.with(X_ROT_180).with(UV_LOCK))
/*  553 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_LEFT, inner.with(X_ROT_180).with(Y_ROT_180).with(UV_LOCK))
/*  554 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_LEFT, inner.with(X_ROT_180).with(Y_ROT_90).with(UV_LOCK))
/*  555 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_LEFT, inner.with(X_ROT_180).with(Y_ROT_270).with(UV_LOCK)));
/*      */   }
/*      */ 
/*      */   
/*      */   private static BlockModelDefinitionGenerator createOrientableTrapdoor(Block block, MultiVariant top, MultiVariant bottom, MultiVariant open) {
/*  560 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  561 */       .with(
/*  562 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.HORIZONTAL_FACING, (Property)BlockStateProperties.HALF, (Property)BlockStateProperties.OPEN)
/*  563 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, false, bottom)
/*  564 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, false, bottom.with(Y_ROT_180))
/*  565 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, false, bottom.with(Y_ROT_90))
/*  566 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, false, bottom.with(Y_ROT_270))
/*  567 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, false, top)
/*  568 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, false, top.with(Y_ROT_180))
/*  569 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, false, top.with(Y_ROT_90))
/*  570 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, false, top.with(Y_ROT_270))
/*  571 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, true, open)
/*  572 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, true, open.with(Y_ROT_180))
/*  573 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, true, open.with(Y_ROT_90))
/*  574 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, true, open.with(Y_ROT_270))
/*  575 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, true, open.with(X_ROT_180).with(Y_ROT_180))
/*  576 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, true, open.with(X_ROT_180))
/*  577 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, true, open.with(X_ROT_180).with(Y_ROT_270))
/*  578 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, true, open.with(X_ROT_180).with(Y_ROT_90)));
/*      */   }
/*      */ 
/*      */   
/*      */   private static BlockModelDefinitionGenerator createTrapdoor(Block block, MultiVariant top, MultiVariant bottom, MultiVariant open) {
/*  583 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  584 */       .with(
/*  585 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.HORIZONTAL_FACING, (Property)BlockStateProperties.HALF, (Property)BlockStateProperties.OPEN)
/*  586 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, false, bottom)
/*  587 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, false, bottom)
/*  588 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, false, bottom)
/*  589 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, false, bottom)
/*  590 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, false, top)
/*  591 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, false, top)
/*  592 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, false, top)
/*  593 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, false, top)
/*  594 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, true, open)
/*  595 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, true, open.with(Y_ROT_180))
/*  596 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, true, open.with(Y_ROT_90))
/*  597 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, true, open.with(Y_ROT_270))
/*  598 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, true, open)
/*  599 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, true, open.with(Y_ROT_180))
/*  600 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, true, open.with(Y_ROT_90))
/*  601 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, true, open.with(Y_ROT_270)));
/*      */   }
/*      */ 
/*      */   
/*      */   private static MultiVariantGenerator createSimpleBlock(Block block, MultiVariant variant) {
/*  606 */     return MultiVariantGenerator.dispatch(block, variant);
/*      */   }
/*      */   
/*      */   private static PropertyDispatch<VariantMutator> createRotatedPillar() {
/*  610 */     return (PropertyDispatch<VariantMutator>)PropertyDispatch.modify((Property)BlockStateProperties.AXIS)
/*  611 */       .select((Comparable)Direction.Axis.Y, NOP)
/*  612 */       .select((Comparable)Direction.Axis.Z, X_ROT_90)
/*  613 */       .select((Comparable)Direction.Axis.X, X_ROT_90.then(Y_ROT_90));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createPillarBlockUVLocked(Block block, TextureMapping mapping, BiConsumer<Identifier, ModelInstance> modelOutput) {
/*  617 */     MultiVariant xAxisModel = plainVariant(ModelTemplates.CUBE_COLUMN_UV_LOCKED_X.create(block, mapping, modelOutput));
/*  618 */     MultiVariant yAxisModel = plainVariant(ModelTemplates.CUBE_COLUMN_UV_LOCKED_Y.create(block, mapping, modelOutput));
/*  619 */     MultiVariant zAxisModel = plainVariant(ModelTemplates.CUBE_COLUMN_UV_LOCKED_Z.create(block, mapping, modelOutput));
/*      */     
/*  621 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  622 */       .with(
/*  623 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.AXIS)
/*  624 */         .select((Comparable)Direction.Axis.X, xAxisModel)
/*  625 */         .select((Comparable)Direction.Axis.Y, yAxisModel)
/*  626 */         .select((Comparable)Direction.Axis.Z, zAxisModel));
/*      */   }
/*      */ 
/*      */   
/*      */   private static BlockModelDefinitionGenerator createAxisAlignedPillarBlock(Block block, MultiVariant model) {
/*  631 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block, model).with(createRotatedPillar());
/*      */   }
/*      */   
/*      */   private void createAxisAlignedPillarBlockCustomModel(Block block, MultiVariant model) {
/*  635 */     this.blockStateOutput.accept(
/*  636 */         createAxisAlignedPillarBlock(block, model));
/*      */   }
/*      */ 
/*      */   
/*      */   public void createAxisAlignedPillarBlock(Block block, TexturedModel.Provider modelProvider) {
/*  641 */     MultiVariant model = plainVariant(modelProvider.create(block, this.modelOutput));
/*  642 */     this.blockStateOutput.accept(
/*  643 */         createAxisAlignedPillarBlock(block, model));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createHorizontallyRotatedBlock(Block block, TexturedModel.Provider modelProvider) {
/*  648 */     MultiVariant model = plainVariant(modelProvider.create(block, this.modelOutput));
/*  649 */     this.blockStateOutput.accept(
/*  650 */         MultiVariantGenerator.dispatch(block, model)
/*  651 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */ 
/*      */   
/*      */   private static BlockModelDefinitionGenerator createRotatedPillarWithHorizontalVariant(Block block, MultiVariant model, MultiVariant horizontalModel) {
/*  656 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  657 */       .with(
/*  658 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.AXIS)
/*  659 */         .select((Comparable)Direction.Axis.Y, model)
/*  660 */         .select((Comparable)Direction.Axis.Z, horizontalModel.with(X_ROT_90))
/*  661 */         .select((Comparable)Direction.Axis.X, horizontalModel.with(X_ROT_90).with(Y_ROT_90)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createRotatedPillarWithHorizontalVariant(Block block, TexturedModel.Provider verticalProvider, TexturedModel.Provider horizontalProvider) {
/*  666 */     MultiVariant model = plainVariant(verticalProvider.create(block, this.modelOutput));
/*  667 */     MultiVariant horizontalModel = plainVariant(horizontalProvider.create(block, this.modelOutput));
/*  668 */     this.blockStateOutput.accept(
/*  669 */         createRotatedPillarWithHorizontalVariant(block, model, horizontalModel));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createCreakingHeart(Block block) {
/*  674 */     MultiVariant model = plainVariant(TexturedModel.COLUMN_ALT.create(block, this.modelOutput));
/*  675 */     MultiVariant horizontalModel = plainVariant(TexturedModel.COLUMN_HORIZONTAL_ALT.create(block, this.modelOutput));
/*  676 */     MultiVariant activeModel = plainVariant(createCreakingHeartModel(TexturedModel.COLUMN_ALT, block, "_awake"));
/*  677 */     MultiVariant activeHorizontalModel = plainVariant(createCreakingHeartModel(TexturedModel.COLUMN_HORIZONTAL_ALT, block, "_awake"));
/*  678 */     MultiVariant dormantModel = plainVariant(createCreakingHeartModel(TexturedModel.COLUMN_ALT, block, "_dormant"));
/*  679 */     MultiVariant dormantHorizontalModel = plainVariant(createCreakingHeartModel(TexturedModel.COLUMN_HORIZONTAL_ALT, block, "_dormant"));
/*  680 */     this.blockStateOutput.accept(
/*  681 */         MultiVariantGenerator.dispatch(block)
/*  682 */         .with(
/*  683 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.AXIS, (Property)CreakingHeartBlock.STATE)
/*  684 */           .select((Comparable)Direction.Axis.Y, (Comparable)CreakingHeartState.UPROOTED, model)
/*  685 */           .select((Comparable)Direction.Axis.Z, (Comparable)CreakingHeartState.UPROOTED, horizontalModel.with(X_ROT_90))
/*  686 */           .select((Comparable)Direction.Axis.X, (Comparable)CreakingHeartState.UPROOTED, horizontalModel.with(X_ROT_90).with(Y_ROT_90))
/*  687 */           .select((Comparable)Direction.Axis.Y, (Comparable)CreakingHeartState.DORMANT, dormantModel)
/*  688 */           .select((Comparable)Direction.Axis.Z, (Comparable)CreakingHeartState.DORMANT, dormantHorizontalModel.with(X_ROT_90))
/*  689 */           .select((Comparable)Direction.Axis.X, (Comparable)CreakingHeartState.DORMANT, dormantHorizontalModel.with(X_ROT_90).with(Y_ROT_90))
/*  690 */           .select((Comparable)Direction.Axis.Y, (Comparable)CreakingHeartState.AWAKE, activeModel)
/*  691 */           .select((Comparable)Direction.Axis.Z, (Comparable)CreakingHeartState.AWAKE, activeHorizontalModel.with(X_ROT_90))
/*  692 */           .select((Comparable)Direction.Axis.X, (Comparable)CreakingHeartState.AWAKE, activeHorizontalModel.with(X_ROT_90).with(Y_ROT_90))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Identifier createCreakingHeartModel(TexturedModel.Provider provider, Block block, String suffix) {
/*  698 */     return provider.updateTexture(t -> t.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, suffix)).put(TextureSlot.END, TextureMapping.getBlockTexture(block, "_top" + suffix)))
/*      */ 
/*      */       
/*  701 */       .createWithSuffix(block, suffix, this.modelOutput);
/*      */   }
/*      */   
/*      */   private Identifier createSuffixedVariant(Block block, String suffix, ModelTemplate template, Function<Identifier, TextureMapping> textureMapping) {
/*  705 */     return template.createWithSuffix(block, suffix, textureMapping.apply(TextureMapping.getBlockTexture(block, suffix)), this.modelOutput);
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createPressurePlate(Block block, MultiVariant off, MultiVariant on) {
/*  709 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  710 */       .with(createBooleanModelDispatch(BlockStateProperties.POWERED, on, off));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createSlab(Block block, MultiVariant bottom, MultiVariant top, MultiVariant full) {
/*  714 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  715 */       .with(
/*  716 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.SLAB_TYPE)
/*  717 */         .select((Comparable)SlabType.BOTTOM, bottom)
/*  718 */         .select((Comparable)SlabType.TOP, top)
/*  719 */         .select((Comparable)SlabType.DOUBLE, full));
/*      */   }
/*      */ 
/*      */   
/*      */   public void createTrivialCube(Block block) {
/*  724 */     createTrivialBlock(block, TexturedModel.CUBE);
/*      */   }
/*      */   
/*      */   public void createTrivialBlock(Block block, TexturedModel.Provider modelProvider) {
/*  728 */     this.blockStateOutput.accept(
/*  729 */         createSimpleBlock(block, plainVariant(modelProvider.create(block, this.modelOutput))));
/*      */   }
/*      */ 
/*      */   
/*      */   public void createTintedLeaves(Block block, TexturedModel.Provider modelProvider, int tintColor) {
/*  734 */     Identifier blockModel = modelProvider.create(block, this.modelOutput);
/*  735 */     this.blockStateOutput.accept(
/*  736 */         createSimpleBlock(block, plainVariant(blockModel)));
/*      */     
/*  738 */     registerSimpleTintedItemModel(block, blockModel, ItemModelUtils.constantTint(tintColor));
/*      */   }
/*      */   
/*      */   private void createVine() {
/*  742 */     createMultifaceBlockStates(Blocks.VINE);
/*      */     
/*  744 */     Identifier itemModel = createFlatItemModelWithBlockTexture(Items.VINE, Blocks.VINE);
/*  745 */     registerSimpleTintedItemModel(Blocks.VINE, itemModel, ItemModelUtils.constantTint(-12012264));
/*      */   }
/*      */   
/*      */   private void createItemWithGrassTint(Block block) {
/*  749 */     Identifier itemModel = createFlatItemModelWithBlockTexture(block.asItem(), block);
/*  750 */     registerSimpleTintedItemModel(block, itemModel, (ItemTintSource)new GrassColorSource());
/*      */   }
/*      */   
/*  753 */   private static final Map<BlockFamily.Variant, BiConsumer<BlockFamilyProvider, Block>> SHAPE_CONSUMERS = (Map<BlockFamily.Variant, BiConsumer<BlockFamilyProvider, Block>>)ImmutableMap.builder()
/*  754 */     .put(BlockFamily.Variant.BUTTON, BlockFamilyProvider::button)
/*  755 */     .put(BlockFamily.Variant.DOOR, BlockFamilyProvider::door)
/*  756 */     .put(BlockFamily.Variant.CHISELED, BlockFamilyProvider::fullBlockVariant)
/*  757 */     .put(BlockFamily.Variant.CRACKED, BlockFamilyProvider::fullBlockVariant)
/*  758 */     .put(BlockFamily.Variant.CUSTOM_FENCE, BlockFamilyProvider::customFence)
/*  759 */     .put(BlockFamily.Variant.FENCE, BlockFamilyProvider::fence)
/*  760 */     .put(BlockFamily.Variant.CUSTOM_FENCE_GATE, BlockFamilyProvider::customFenceGate)
/*  761 */     .put(BlockFamily.Variant.FENCE_GATE, BlockFamilyProvider::fenceGate)
/*  762 */     .put(BlockFamily.Variant.SIGN, BlockFamilyProvider::sign)
/*  763 */     .put(BlockFamily.Variant.SLAB, BlockFamilyProvider::slab)
/*  764 */     .put(BlockFamily.Variant.STAIRS, BlockFamilyProvider::stairs)
/*  765 */     .put(BlockFamily.Variant.PRESSURE_PLATE, BlockFamilyProvider::pressurePlate)
/*  766 */     .put(BlockFamily.Variant.TRAPDOOR, BlockFamilyProvider::trapdoor)
/*  767 */     .put(BlockFamily.Variant.WALL, BlockFamilyProvider::wall)
/*  768 */     .build();
/*      */   
/*      */   private class BlockFamilyProvider {
/*      */     private final TextureMapping mapping;
/*  772 */     private final Map<ModelTemplate, Identifier> models = new HashMap<>();
/*      */     
/*      */     private BlockFamily family;
/*      */     private Variant fullBlock;
/*  776 */     private final Set<Block> skipGeneratingModelsFor = new HashSet<>();
/*      */     
/*      */     public BlockFamilyProvider(TextureMapping mapping) {
/*  779 */       this.mapping = mapping;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider fullBlock(Block block, ModelTemplate template) {
/*  783 */       this.fullBlock = BlockModelGenerators.plainModel(template.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  784 */       if (BlockModelGenerators.FULL_BLOCK_MODEL_CUSTOM_GENERATORS.containsKey(block)) {
/*  785 */         BlockModelGenerators.this.blockStateOutput.accept(((BlockModelGenerators.BlockStateGeneratorSupplier)BlockModelGenerators.FULL_BLOCK_MODEL_CUSTOM_GENERATORS.get(block)).create(block, this.fullBlock, this.mapping, BlockModelGenerators.this.modelOutput));
/*      */       } else {
/*  787 */         BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.variant(this.fullBlock)));
/*      */       } 
/*  789 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider donateModelTo(Block donor, Block copyTo) {
/*  793 */       Identifier donorModelLocation = ModelLocationUtils.getModelLocation(donor);
/*  794 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(copyTo, BlockModelGenerators.plainVariant(donorModelLocation)));
/*  795 */       BlockModelGenerators.this.itemModelOutput.copy(donor.asItem(), copyTo.asItem());
/*  796 */       this.skipGeneratingModelsFor.add(copyTo);
/*  797 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider button(Block block) {
/*  801 */       MultiVariant normal = BlockModelGenerators.plainVariant(ModelTemplates.BUTTON.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  802 */       MultiVariant pressed = BlockModelGenerators.plainVariant(ModelTemplates.BUTTON_PRESSED.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  803 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createButton(block, normal, pressed));
/*      */       
/*  805 */       Identifier inventory = ModelTemplates.BUTTON_INVENTORY.create(block, this.mapping, BlockModelGenerators.this.modelOutput);
/*  806 */       BlockModelGenerators.this.registerSimpleItemModel(block, inventory);
/*  807 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider wall(Block block) {
/*  811 */       MultiVariant post = BlockModelGenerators.plainVariant(ModelTemplates.WALL_POST.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  812 */       MultiVariant low = BlockModelGenerators.plainVariant(ModelTemplates.WALL_LOW_SIDE.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  813 */       MultiVariant high = BlockModelGenerators.plainVariant(ModelTemplates.WALL_TALL_SIDE.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  814 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createWall(block, post, low, high));
/*      */       
/*  816 */       Identifier inventory = ModelTemplates.WALL_INVENTORY.create(block, this.mapping, BlockModelGenerators.this.modelOutput);
/*  817 */       BlockModelGenerators.this.registerSimpleItemModel(block, inventory);
/*  818 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider customFence(Block block) {
/*  822 */       TextureMapping mapping = TextureMapping.customParticle(block);
/*      */       
/*  824 */       MultiVariant post = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_POST.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  825 */       MultiVariant north = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_SIDE_NORTH.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  826 */       MultiVariant east = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_SIDE_EAST.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  827 */       MultiVariant south = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_SIDE_SOUTH.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  828 */       MultiVariant west = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_SIDE_WEST.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  829 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createCustomFence(block, post, north, east, south, west));
/*      */       
/*  831 */       Identifier inventory = ModelTemplates.CUSTOM_FENCE_INVENTORY.create(block, mapping, BlockModelGenerators.this.modelOutput);
/*  832 */       BlockModelGenerators.this.registerSimpleItemModel(block, inventory);
/*  833 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider fence(Block block) {
/*  837 */       MultiVariant post = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_POST.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  838 */       MultiVariant side = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_SIDE.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  839 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createFence(block, post, side));
/*      */       
/*  841 */       Identifier inventory = ModelTemplates.FENCE_INVENTORY.create(block, this.mapping, BlockModelGenerators.this.modelOutput);
/*  842 */       BlockModelGenerators.this.registerSimpleItemModel(block, inventory);
/*  843 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider customFenceGate(Block block) {
/*  847 */       TextureMapping mapping = TextureMapping.customParticle(block);
/*      */       
/*  849 */       MultiVariant open = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_GATE_OPEN.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  850 */       MultiVariant closed = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_GATE_CLOSED.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  851 */       MultiVariant openWall = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_GATE_WALL_OPEN.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  852 */       MultiVariant closedWall = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_GATE_WALL_CLOSED.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  853 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createFenceGate(block, open, closed, openWall, closedWall, false));
/*  854 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider fenceGate(Block block) {
/*  858 */       MultiVariant open = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_GATE_OPEN.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  859 */       MultiVariant closed = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_GATE_CLOSED.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  860 */       MultiVariant openWall = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_GATE_WALL_OPEN.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  861 */       MultiVariant closedWall = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_GATE_WALL_CLOSED.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  862 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createFenceGate(block, open, closed, openWall, closedWall, true));
/*  863 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider pressurePlate(Block block) {
/*  867 */       MultiVariant off = BlockModelGenerators.plainVariant(ModelTemplates.PRESSURE_PLATE_UP.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  868 */       MultiVariant on = BlockModelGenerators.plainVariant(ModelTemplates.PRESSURE_PLATE_DOWN.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  869 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createPressurePlate(block, off, on));
/*  870 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider sign(Block sign) {
/*  874 */       if (this.family == null) {
/*  875 */         throw new IllegalStateException("Family not defined");
/*      */       }
/*  877 */       Block wallSign = (Block)this.family.getVariants().get(BlockFamily.Variant.WALL_SIGN);
/*  878 */       MultiVariant model = BlockModelGenerators.plainVariant(ModelTemplates.PARTICLE_ONLY.create(sign, this.mapping, BlockModelGenerators.this.modelOutput));
/*  879 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(sign, model));
/*  880 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(wallSign, model));
/*  881 */       BlockModelGenerators.this.registerSimpleFlatItemModel(sign.asItem());
/*  882 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider slab(Block slab) {
/*  886 */       if (this.fullBlock == null) {
/*  887 */         throw new IllegalStateException("Full block not generated yet");
/*      */       }
/*  889 */       Identifier bottom = getOrCreateModel(ModelTemplates.SLAB_BOTTOM, slab);
/*  890 */       MultiVariant top = BlockModelGenerators.plainVariant(getOrCreateModel(ModelTemplates.SLAB_TOP, slab));
/*      */       
/*  892 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSlab(slab, BlockModelGenerators.plainVariant(bottom), top, BlockModelGenerators.variant(this.fullBlock)));
/*  893 */       BlockModelGenerators.this.registerSimpleItemModel(slab, bottom);
/*  894 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider stairs(Block stairs) {
/*  898 */       MultiVariant inner = BlockModelGenerators.plainVariant(getOrCreateModel(ModelTemplates.STAIRS_INNER, stairs));
/*  899 */       Identifier straight = getOrCreateModel(ModelTemplates.STAIRS_STRAIGHT, stairs);
/*  900 */       MultiVariant outer = BlockModelGenerators.plainVariant(getOrCreateModel(ModelTemplates.STAIRS_OUTER, stairs));
/*      */       
/*  902 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createStairs(stairs, inner, BlockModelGenerators.plainVariant(straight), outer));
/*  903 */       BlockModelGenerators.this.registerSimpleItemModel(stairs, straight);
/*  904 */       return this;
/*      */     }
/*      */     
/*      */     private BlockFamilyProvider fullBlockVariant(Block variant) {
/*  908 */       TexturedModel model = BlockModelGenerators.TEXTURED_MODELS.getOrDefault(variant, TexturedModel.CUBE.get(variant));
/*  909 */       MultiVariant variantModel = BlockModelGenerators.plainVariant(model.create(variant, BlockModelGenerators.this.modelOutput));
/*  910 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(variant, variantModel));
/*  911 */       return this;
/*      */     }
/*      */     
/*      */     private BlockFamilyProvider door(Block door) {
/*  915 */       BlockModelGenerators.this.createDoor(door);
/*  916 */       return this;
/*      */     }
/*      */     
/*      */     private void trapdoor(Block result) {
/*  920 */       if (BlockModelGenerators.NON_ORIENTABLE_TRAPDOOR.contains(result)) {
/*  921 */         BlockModelGenerators.this.createTrapdoor(result);
/*      */       } else {
/*  923 */         BlockModelGenerators.this.createOrientableTrapdoor(result);
/*      */       } 
/*      */     }
/*      */     
/*      */     private Identifier getOrCreateModel(ModelTemplate modelTemplate, Block block) {
/*  928 */       return this.models.computeIfAbsent(modelTemplate, template -> block.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider generateFor(BlockFamily family) {
/*  932 */       this.family = family;
/*  933 */       family.getVariants().forEach((variant, result) -> {
/*      */             if (this.skipGeneratingModelsFor.contains(result)) {
/*      */               return;
/*      */             }
/*      */             BiConsumer<BlockFamilyProvider, Block> consumer = BlockModelGenerators.SHAPE_CONSUMERS.get(variant);
/*      */             if (consumer != null) {
/*      */               consumer.accept(this, result);
/*      */             }
/*      */           });
/*  942 */       return this;
/*      */     }
/*      */   }
/*      */   
/*      */   private BlockFamilyProvider family(Block block) {
/*  947 */     TexturedModel model = TEXTURED_MODELS.getOrDefault(block, TexturedModel.CUBE.get(block));
/*  948 */     return new BlockFamilyProvider(model.getMapping()).fullBlock(block, model.getTemplate());
/*      */   }
/*      */   
/*      */   public void createHangingSign(Block particleBlock, Block hangingSign, Block wallHangingSign) {
/*  952 */     MultiVariant model = createParticleOnlyBlockModel(hangingSign, particleBlock);
/*  953 */     this.blockStateOutput.accept(createSimpleBlock(hangingSign, model));
/*  954 */     this.blockStateOutput.accept(createSimpleBlock(wallHangingSign, model));
/*  955 */     registerSimpleFlatItemModel(hangingSign.asItem());
/*      */   }
/*      */   
/*      */   private void createDoor(Block door) {
/*  959 */     TextureMapping mapping = TextureMapping.door(door);
/*  960 */     MultiVariant doorBottomLeft = plainVariant(ModelTemplates.DOOR_BOTTOM_LEFT.create(door, mapping, this.modelOutput));
/*  961 */     MultiVariant doorBottomLeftOpen = plainVariant(ModelTemplates.DOOR_BOTTOM_LEFT_OPEN.create(door, mapping, this.modelOutput));
/*  962 */     MultiVariant doorBottomRight = plainVariant(ModelTemplates.DOOR_BOTTOM_RIGHT.create(door, mapping, this.modelOutput));
/*  963 */     MultiVariant doorBottomRightOpen = plainVariant(ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN.create(door, mapping, this.modelOutput));
/*  964 */     MultiVariant doorTopLeft = plainVariant(ModelTemplates.DOOR_TOP_LEFT.create(door, mapping, this.modelOutput));
/*  965 */     MultiVariant doorTopLeftOpen = plainVariant(ModelTemplates.DOOR_TOP_LEFT_OPEN.create(door, mapping, this.modelOutput));
/*  966 */     MultiVariant doorTopRight = plainVariant(ModelTemplates.DOOR_TOP_RIGHT.create(door, mapping, this.modelOutput));
/*  967 */     MultiVariant doorTopRightOpen = plainVariant(ModelTemplates.DOOR_TOP_RIGHT_OPEN.create(door, mapping, this.modelOutput));
/*      */     
/*  969 */     registerSimpleFlatItemModel(door.asItem());
/*  970 */     this.blockStateOutput.accept(createDoor(door, doorBottomLeft, doorBottomLeftOpen, doorBottomRight, doorBottomRightOpen, doorTopLeft, doorTopLeftOpen, doorTopRight, doorTopRightOpen));
/*      */   }
/*      */   
/*      */   private void copyDoorModel(Block donor, Block acceptor) {
/*  974 */     MultiVariant doorBottomLeft = plainVariant(ModelTemplates.DOOR_BOTTOM_LEFT.getDefaultModelLocation(donor));
/*  975 */     MultiVariant doorBottomLeftOpen = plainVariant(ModelTemplates.DOOR_BOTTOM_LEFT_OPEN.getDefaultModelLocation(donor));
/*  976 */     MultiVariant doorBottomRight = plainVariant(ModelTemplates.DOOR_BOTTOM_RIGHT.getDefaultModelLocation(donor));
/*  977 */     MultiVariant doorBottomRightOpen = plainVariant(ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN.getDefaultModelLocation(donor));
/*  978 */     MultiVariant doorTopLeft = plainVariant(ModelTemplates.DOOR_TOP_LEFT.getDefaultModelLocation(donor));
/*  979 */     MultiVariant doorTopLeftOpen = plainVariant(ModelTemplates.DOOR_TOP_LEFT_OPEN.getDefaultModelLocation(donor));
/*  980 */     MultiVariant doorTopRight = plainVariant(ModelTemplates.DOOR_TOP_RIGHT.getDefaultModelLocation(donor));
/*  981 */     MultiVariant doorTopRightOpen = plainVariant(ModelTemplates.DOOR_TOP_RIGHT_OPEN.getDefaultModelLocation(donor));
/*      */     
/*  983 */     this.itemModelOutput.copy(donor.asItem(), acceptor.asItem());
/*  984 */     this.blockStateOutput.accept(createDoor(acceptor, doorBottomLeft, doorBottomLeftOpen, doorBottomRight, doorBottomRightOpen, doorTopLeft, doorTopLeftOpen, doorTopRight, doorTopRightOpen));
/*      */   }
/*      */   
/*      */   private void createOrientableTrapdoor(Block trapdoor) {
/*  988 */     TextureMapping mapping = TextureMapping.defaultTexture(trapdoor);
/*  989 */     MultiVariant top = plainVariant(ModelTemplates.ORIENTABLE_TRAPDOOR_TOP.create(trapdoor, mapping, this.modelOutput));
/*  990 */     Identifier bottom = ModelTemplates.ORIENTABLE_TRAPDOOR_BOTTOM.create(trapdoor, mapping, this.modelOutput);
/*  991 */     MultiVariant open = plainVariant(ModelTemplates.ORIENTABLE_TRAPDOOR_OPEN.create(trapdoor, mapping, this.modelOutput));
/*      */     
/*  993 */     this.blockStateOutput.accept(createOrientableTrapdoor(trapdoor, top, plainVariant(bottom), open));
/*  994 */     registerSimpleItemModel(trapdoor, bottom);
/*      */   }
/*      */   
/*      */   private void createTrapdoor(Block trapdoor) {
/*  998 */     TextureMapping mapping = TextureMapping.defaultTexture(trapdoor);
/*  999 */     MultiVariant top = plainVariant(ModelTemplates.TRAPDOOR_TOP.create(trapdoor, mapping, this.modelOutput));
/* 1000 */     Identifier bottom = ModelTemplates.TRAPDOOR_BOTTOM.create(trapdoor, mapping, this.modelOutput);
/* 1001 */     MultiVariant open = plainVariant(ModelTemplates.TRAPDOOR_OPEN.create(trapdoor, mapping, this.modelOutput));
/*      */     
/* 1003 */     this.blockStateOutput.accept(createTrapdoor(trapdoor, top, plainVariant(bottom), open));
/* 1004 */     registerSimpleItemModel(trapdoor, bottom);
/*      */   }
/*      */   
/*      */   private void copyTrapdoorModel(Block donor, Block acceptor) {
/* 1008 */     MultiVariant top = plainVariant(ModelTemplates.TRAPDOOR_TOP.getDefaultModelLocation(donor));
/* 1009 */     MultiVariant bottom = plainVariant(ModelTemplates.TRAPDOOR_BOTTOM.getDefaultModelLocation(donor));
/* 1010 */     MultiVariant open = plainVariant(ModelTemplates.TRAPDOOR_OPEN.getDefaultModelLocation(donor));
/*      */     
/* 1012 */     this.itemModelOutput.copy(donor.asItem(), acceptor.asItem());
/* 1013 */     this.blockStateOutput.accept(createTrapdoor(acceptor, top, bottom, open));
/*      */   }
/*      */   
/*      */   private void createBigDripLeafBlock() {
/* 1017 */     MultiVariant noTilt = plainVariant(ModelLocationUtils.getModelLocation(Blocks.BIG_DRIPLEAF));
/* 1018 */     MultiVariant partialTilt = plainVariant(ModelLocationUtils.getModelLocation(Blocks.BIG_DRIPLEAF, "_partial_tilt"));
/* 1019 */     MultiVariant fullTilt = plainVariant(ModelLocationUtils.getModelLocation(Blocks.BIG_DRIPLEAF, "_full_tilt"));
/*      */     
/* 1021 */     this.blockStateOutput.accept(
/* 1022 */         MultiVariantGenerator.dispatch(Blocks.BIG_DRIPLEAF)
/* 1023 */         .with(
/* 1024 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.TILT)
/* 1025 */           .select((Comparable)Tilt.NONE, noTilt)
/* 1026 */           .select((Comparable)Tilt.UNSTABLE, noTilt)
/* 1027 */           .select((Comparable)Tilt.PARTIAL, partialTilt)
/* 1028 */           .select((Comparable)Tilt.FULL, fullTilt))
/*      */         
/* 1030 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */   
/*      */   private class WoodProvider
/*      */   {
/*      */     private final TextureMapping logMapping;
/*      */     
/*      */     public WoodProvider(TextureMapping logMapping) {
/* 1038 */       this.logMapping = logMapping;
/*      */     }
/*      */     
/*      */     public WoodProvider wood(Block block) {
/* 1042 */       TextureMapping woodMapping = this.logMapping.copyAndUpdate(TextureSlot.END, this.logMapping.get(TextureSlot.SIDE));
/* 1043 */       Identifier model = ModelTemplates.CUBE_COLUMN.create(block, woodMapping, BlockModelGenerators.this.modelOutput);
/* 1044 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createAxisAlignedPillarBlock(block, BlockModelGenerators.plainVariant(model)));
/* 1045 */       BlockModelGenerators.this.registerSimpleItemModel(block, model);
/* 1046 */       return this;
/*      */     }
/*      */     
/*      */     public WoodProvider log(Block block) {
/* 1050 */       Identifier model = ModelTemplates.CUBE_COLUMN.create(block, this.logMapping, BlockModelGenerators.this.modelOutput);
/* 1051 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createAxisAlignedPillarBlock(block, BlockModelGenerators.plainVariant(model)));
/* 1052 */       BlockModelGenerators.this.registerSimpleItemModel(block, model);
/* 1053 */       return this;
/*      */     }
/*      */     
/*      */     public WoodProvider logWithHorizontal(Block block) {
/* 1057 */       Identifier model = ModelTemplates.CUBE_COLUMN.create(block, this.logMapping, BlockModelGenerators.this.modelOutput);
/* 1058 */       MultiVariant horizontalModel = BlockModelGenerators.plainVariant(ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(block, this.logMapping, BlockModelGenerators.this.modelOutput));
/* 1059 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createRotatedPillarWithHorizontalVariant(block, BlockModelGenerators.plainVariant(model), horizontalModel));
/* 1060 */       BlockModelGenerators.this.registerSimpleItemModel(block, model);
/* 1061 */       return this;
/*      */     }
/*      */     
/*      */     public WoodProvider logUVLocked(Block block) {
/* 1065 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createPillarBlockUVLocked(block, this.logMapping, BlockModelGenerators.this.modelOutput));
/* 1066 */       BlockModelGenerators.this.registerSimpleItemModel(block, ModelTemplates.CUBE_COLUMN.create(block, this.logMapping, BlockModelGenerators.this.modelOutput));
/* 1067 */       return this;
/*      */     }
/*      */   }
/*      */   
/*      */   private WoodProvider woodProvider(Block log) {
/* 1072 */     return new WoodProvider(TextureMapping.logColumn(log));
/*      */   }
/*      */   
/*      */   private void createNonTemplateModelBlock(Block block) {
/* 1076 */     createNonTemplateModelBlock(block, block);
/*      */   }
/*      */   
/*      */   private void createNonTemplateModelBlock(Block block, Block donor) {
/* 1080 */     this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelLocationUtils.getModelLocation(donor))));
/*      */   }
/*      */   
/*      */   private enum PlantType {
/* 1084 */     TINTED(ModelTemplates.TINTED_CROSS, ModelTemplates.TINTED_FLOWER_POT_CROSS, false),
/* 1085 */     NOT_TINTED(ModelTemplates.CROSS, ModelTemplates.FLOWER_POT_CROSS, false),
/* 1086 */     EMISSIVE_NOT_TINTED(ModelTemplates.CROSS_EMISSIVE, ModelTemplates.FLOWER_POT_CROSS_EMISSIVE, true);
/*      */     
/*      */     private final ModelTemplate blockTemplate;
/*      */     
/*      */     private final ModelTemplate flowerPotTemplate;
/*      */     private final boolean isEmissive;
/*      */     
/*      */     PlantType(ModelTemplate blockTemplate, ModelTemplate flowerPotTemplate, boolean isEmissive) {
/* 1094 */       this.blockTemplate = blockTemplate;
/* 1095 */       this.flowerPotTemplate = flowerPotTemplate;
/* 1096 */       this.isEmissive = isEmissive;
/*      */     }
/*      */     
/*      */     public ModelTemplate getCross() {
/* 1100 */       return this.blockTemplate;
/*      */     }
/*      */     
/*      */     public ModelTemplate getCrossPot() {
/* 1104 */       return this.flowerPotTemplate;
/*      */     }
/*      */     
/*      */     public Identifier createItemModel(BlockModelGenerators generator, Block block) {
/* 1108 */       Item blockItem = block.asItem();
/* 1109 */       if (this.isEmissive) {
/* 1110 */         return generator.createFlatItemModelWithBlockTextureAndOverlay(blockItem, block, "_emissive");
/*      */       }
/* 1112 */       return generator.createFlatItemModelWithBlockTexture(blockItem, block);
/*      */     }
/*      */ 
/*      */     
/*      */     public TextureMapping getTextureMapping(Block block) {
/* 1117 */       return this.isEmissive ? TextureMapping.crossEmissive(block) : TextureMapping.cross(block);
/*      */     }
/*      */     
/*      */     public TextureMapping getPlantTextureMapping(Block standAlone) {
/* 1121 */       return this.isEmissive ? TextureMapping.plantEmissive(standAlone) : TextureMapping.plant(standAlone);
/*      */     }
/*      */   }
/*      */   
/*      */   private void createCrossBlockWithDefaultItem(Block block, PlantType plantType) {
/* 1126 */     registerSimpleItemModel(block.asItem(), plantType.createItemModel(this, block));
/* 1127 */     createCrossBlock(block, plantType);
/*      */   }
/*      */   
/*      */   private void createCrossBlockWithDefaultItem(Block block, PlantType plantType, TextureMapping textures) {
/* 1131 */     registerSimpleFlatItemModel(block);
/* 1132 */     createCrossBlock(block, plantType, textures);
/*      */   }
/*      */   
/*      */   private void createCrossBlock(Block block, PlantType plantType) {
/* 1136 */     TextureMapping textures = plantType.getTextureMapping(block);
/* 1137 */     createCrossBlock(block, plantType, textures);
/*      */   }
/*      */   
/*      */   private void createCrossBlock(Block block, PlantType plantType, TextureMapping textures) {
/* 1141 */     MultiVariant model = plainVariant(plantType.getCross().create(block, textures, this.modelOutput));
/* 1142 */     this.blockStateOutput.accept(createSimpleBlock(block, model));
/*      */   }
/*      */   
/*      */   private void createCrossBlock(Block block, PlantType plantType, Property<Integer> property, int... stages) {
/* 1146 */     if (property.getPossibleValues().size() != stages.length) {
/* 1147 */       throw new IllegalArgumentException("missing values for property: " + String.valueOf(property));
/*      */     }
/*      */     
/* 1150 */     registerSimpleFlatItemModel(block.asItem());
/* 1151 */     this.blockStateOutput.accept(
/* 1152 */         MultiVariantGenerator.dispatch(block)
/* 1153 */         .with(PropertyDispatch.initial(property)
/* 1154 */           .generate(i -> {
/*      */               String suffix = "_stage" + stages[plantType];
/*      */               TextureMapping texture = TextureMapping.cross(TextureMapping.getBlockTexture(stages, suffix));
/*      */               return plainVariant(block.getCross().createWithSuffix(stages, suffix, texture, this.modelOutput));
/*      */             })));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createPlantWithDefaultItem(Block standAlone, Block potted, PlantType plantType) {
/* 1164 */     registerSimpleItemModel(standAlone.asItem(), plantType.createItemModel(this, standAlone));
/* 1165 */     createPlant(standAlone, potted, plantType);
/*      */   }
/*      */   
/*      */   private void createPlant(Block standAlone, Block potted, PlantType plantType) {
/* 1169 */     createCrossBlock(standAlone, plantType);
/*      */     
/* 1171 */     TextureMapping textures = plantType.getPlantTextureMapping(standAlone);
/* 1172 */     MultiVariant model = plainVariant(plantType.getCrossPot().create(potted, textures, this.modelOutput));
/* 1173 */     this.blockStateOutput.accept(createSimpleBlock(potted, model));
/*      */   }
/*      */   
/*      */   private void createCoralFans(Block fan, Block wallFan) {
/* 1177 */     TexturedModel fanTemplate = TexturedModel.CORAL_FAN.get(fan);
/*      */     
/* 1179 */     MultiVariant fanModel = plainVariant(fanTemplate.create(fan, this.modelOutput));
/* 1180 */     this.blockStateOutput.accept(createSimpleBlock(fan, fanModel));
/*      */     
/* 1182 */     MultiVariant wallFanModel = plainVariant(ModelTemplates.CORAL_WALL_FAN.create(wallFan, fanTemplate.getMapping(), this.modelOutput));
/* 1183 */     this.blockStateOutput.accept(MultiVariantGenerator.dispatch(wallFan, wallFanModel).with(ROTATION_HORIZONTAL_FACING));
/*      */     
/* 1185 */     registerSimpleFlatItemModel(fan);
/*      */   }
/*      */   
/*      */   private void createStems(Block growingStem, Block attachedStem) {
/* 1189 */     registerSimpleFlatItemModel(growingStem.asItem());
/* 1190 */     TextureMapping growingMapping = TextureMapping.stem(growingStem);
/* 1191 */     TextureMapping attachedMapping = TextureMapping.attachedStem(growingStem, attachedStem);
/*      */     
/* 1193 */     MultiVariant attachedStemModel = plainVariant(ModelTemplates.ATTACHED_STEM.create(attachedStem, attachedMapping, this.modelOutput));
/* 1194 */     this.blockStateOutput.accept(
/* 1195 */         MultiVariantGenerator.dispatch(attachedStem, attachedStemModel)
/* 1196 */         .with((PropertyDispatch)PropertyDispatch.modify((Property)BlockStateProperties.HORIZONTAL_FACING)
/* 1197 */           .select((Comparable)Direction.WEST, NOP)
/* 1198 */           .select((Comparable)Direction.SOUTH, Y_ROT_270)
/* 1199 */           .select((Comparable)Direction.NORTH, Y_ROT_90)
/* 1200 */           .select((Comparable)Direction.EAST, Y_ROT_180)));
/*      */ 
/*      */     
/* 1203 */     this.blockStateOutput.accept(
/* 1204 */         MultiVariantGenerator.dispatch(growingStem)
/* 1205 */         .with(
/* 1206 */           PropertyDispatch.initial((Property)BlockStateProperties.AGE_7)
/* 1207 */           .generate(i -> plainVariant(ModelTemplates.STEMS[growingMapping].create(growingStem, growingStem, this.modelOutput)))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createPitcherPlant() {
/* 1213 */     Block block = Blocks.PITCHER_PLANT;
/* 1214 */     registerSimpleFlatItemModel(block.asItem());
/* 1215 */     MultiVariant topModel = plainVariant(ModelLocationUtils.getModelLocation(block, "_top"));
/* 1216 */     MultiVariant bottomModel = plainVariant(ModelLocationUtils.getModelLocation(block, "_bottom"));
/* 1217 */     createDoubleBlock(block, topModel, bottomModel);
/*      */   }
/*      */   
/*      */   private void createPitcherCrop() {
/* 1221 */     Block block = Blocks.PITCHER_CROP;
/*      */     
/* 1223 */     registerSimpleFlatItemModel(block.asItem());
/*      */     
/* 1225 */     this.blockStateOutput.accept(
/* 1226 */         MultiVariantGenerator.dispatch(block)
/* 1227 */         .with(
/* 1228 */           PropertyDispatch.initial((Property)PitcherCropBlock.AGE, (Property)BlockStateProperties.DOUBLE_BLOCK_HALF)
/* 1229 */           .generate((age, shape) -> {
/*      */               switch (shape) {
/*      */                 default:
/*      */                   throw new MatchException(null, null);
/*      */                 case UPPER:
/*      */                 
/*      */                 case LOWER:
/*      */                   break;
/*      */               } 
/*      */               return plainVariant(ModelLocationUtils.getModelLocation(block, "_bottom_stage_" + age));
/* 1239 */             }))); } private void createCoral(Block plant, Block deadPlant, Block block, Block deadBlock, Block fan, Block deadFan, Block wallFan, Block deadWallFan) { createCrossBlockWithDefaultItem(plant, PlantType.NOT_TINTED);
/* 1240 */     createCrossBlockWithDefaultItem(deadPlant, PlantType.NOT_TINTED);
/*      */     
/* 1242 */     createTrivialCube(block);
/* 1243 */     createTrivialCube(deadBlock);
/*      */     
/* 1245 */     createCoralFans(fan, wallFan);
/* 1246 */     createCoralFans(deadFan, deadWallFan); }
/*      */ 
/*      */   
/*      */   private void createDoublePlant(Block block, PlantType plantType) {
/* 1250 */     MultiVariant topModel = plainVariant(createSuffixedVariant(block, "_top", plantType.getCross(), TextureMapping::cross));
/* 1251 */     MultiVariant bottomModel = plainVariant(createSuffixedVariant(block, "_bottom", plantType.getCross(), TextureMapping::cross));
/* 1252 */     createDoubleBlock(block, topModel, bottomModel);
/*      */   }
/*      */   
/*      */   private void createDoublePlantWithDefaultItem(Block block, PlantType plantType) {
/* 1256 */     registerSimpleFlatItemModel(block, "_top");
/* 1257 */     createDoublePlant(block, plantType);
/*      */   }
/*      */   
/*      */   private void createTintedDoublePlant(Block block) {
/* 1261 */     Identifier itemModel = createFlatItemModelWithBlockTexture(block.asItem(), block, "_top");
/* 1262 */     registerSimpleTintedItemModel(block, itemModel, (ItemTintSource)new GrassColorSource());
/* 1263 */     createDoublePlant(block, PlantType.TINTED);
/*      */   }
/*      */   
/*      */   private void createSunflower() {
/* 1267 */     registerSimpleFlatItemModel(Blocks.SUNFLOWER, "_front");
/* 1268 */     MultiVariant topModel = plainVariant(ModelLocationUtils.getModelLocation(Blocks.SUNFLOWER, "_top"));
/* 1269 */     MultiVariant bottomModel = plainVariant(createSuffixedVariant(Blocks.SUNFLOWER, "_bottom", PlantType.NOT_TINTED.getCross(), TextureMapping::cross));
/* 1270 */     createDoubleBlock(Blocks.SUNFLOWER, topModel, bottomModel);
/*      */   }
/*      */   
/*      */   private void createTallSeagrass() {
/* 1274 */     MultiVariant topModel = plainVariant(createSuffixedVariant(Blocks.TALL_SEAGRASS, "_top", ModelTemplates.SEAGRASS, TextureMapping::defaultTexture));
/* 1275 */     MultiVariant bottomModel = plainVariant(createSuffixedVariant(Blocks.TALL_SEAGRASS, "_bottom", ModelTemplates.SEAGRASS, TextureMapping::defaultTexture));
/* 1276 */     createDoubleBlock(Blocks.TALL_SEAGRASS, topModel, bottomModel);
/*      */   }
/*      */   
/*      */   private void createSmallDripleaf() {
/* 1280 */     MultiVariant topModel = plainVariant(ModelLocationUtils.getModelLocation(Blocks.SMALL_DRIPLEAF, "_top"));
/* 1281 */     MultiVariant bottomModel = plainVariant(ModelLocationUtils.getModelLocation(Blocks.SMALL_DRIPLEAF, "_bottom"));
/* 1282 */     this.blockStateOutput.accept(
/* 1283 */         MultiVariantGenerator.dispatch(Blocks.SMALL_DRIPLEAF)
/* 1284 */         .with(
/* 1285 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.DOUBLE_BLOCK_HALF)
/* 1286 */           .select((Comparable)DoubleBlockHalf.LOWER, bottomModel)
/* 1287 */           .select((Comparable)DoubleBlockHalf.UPPER, topModel))
/*      */         
/* 1289 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createDoubleBlock(Block block, MultiVariant topModel, MultiVariant bottomModel) {
/* 1294 */     this.blockStateOutput.accept(
/* 1295 */         MultiVariantGenerator.dispatch(block)
/* 1296 */         .with(
/* 1297 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.DOUBLE_BLOCK_HALF)
/* 1298 */           .select((Comparable)DoubleBlockHalf.LOWER, bottomModel)
/* 1299 */           .select((Comparable)DoubleBlockHalf.UPPER, topModel)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createPassiveRail(Block block) {
/* 1305 */     TextureMapping texture = TextureMapping.rail(block);
/* 1306 */     TextureMapping cornerTexture = TextureMapping.rail(TextureMapping.getBlockTexture(block, "_corner"));
/*      */     
/* 1308 */     MultiVariant flat = plainVariant(ModelTemplates.RAIL_FLAT.create(block, texture, this.modelOutput));
/* 1309 */     MultiVariant curved = plainVariant(ModelTemplates.RAIL_CURVED.create(block, cornerTexture, this.modelOutput));
/* 1310 */     MultiVariant risingNE = plainVariant(ModelTemplates.RAIL_RAISED_NE.create(block, texture, this.modelOutput));
/* 1311 */     MultiVariant risingSW = plainVariant(ModelTemplates.RAIL_RAISED_SW.create(block, texture, this.modelOutput));
/*      */     
/* 1313 */     registerSimpleFlatItemModel(block);
/* 1314 */     this.blockStateOutput.accept(
/* 1315 */         MultiVariantGenerator.dispatch(block)
/* 1316 */         .with(
/* 1317 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.RAIL_SHAPE)
/* 1318 */           .select((Comparable)RailShape.NORTH_SOUTH, flat)
/* 1319 */           .select((Comparable)RailShape.EAST_WEST, flat.with(Y_ROT_90))
/*      */           
/* 1321 */           .select((Comparable)RailShape.ASCENDING_EAST, risingNE.with(Y_ROT_90))
/* 1322 */           .select((Comparable)RailShape.ASCENDING_WEST, risingSW.with(Y_ROT_90))
/* 1323 */           .select((Comparable)RailShape.ASCENDING_NORTH, risingNE)
/* 1324 */           .select((Comparable)RailShape.ASCENDING_SOUTH, risingSW)
/*      */           
/* 1326 */           .select((Comparable)RailShape.SOUTH_EAST, curved)
/* 1327 */           .select((Comparable)RailShape.SOUTH_WEST, curved.with(Y_ROT_90))
/* 1328 */           .select((Comparable)RailShape.NORTH_WEST, curved.with(Y_ROT_180))
/* 1329 */           .select((Comparable)RailShape.NORTH_EAST, curved.with(Y_ROT_270))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createActiveRail(Block block) {
/* 1335 */     MultiVariant flat = plainVariant(createSuffixedVariant(block, "", ModelTemplates.RAIL_FLAT, TextureMapping::rail));
/* 1336 */     MultiVariant risingNE = plainVariant(createSuffixedVariant(block, "", ModelTemplates.RAIL_RAISED_NE, TextureMapping::rail));
/* 1337 */     MultiVariant risingSW = plainVariant(createSuffixedVariant(block, "", ModelTemplates.RAIL_RAISED_SW, TextureMapping::rail));
/*      */     
/* 1339 */     MultiVariant flatOn = plainVariant(createSuffixedVariant(block, "_on", ModelTemplates.RAIL_FLAT, TextureMapping::rail));
/* 1340 */     MultiVariant risingNEOn = plainVariant(createSuffixedVariant(block, "_on", ModelTemplates.RAIL_RAISED_NE, TextureMapping::rail));
/* 1341 */     MultiVariant risingSWOn = plainVariant(createSuffixedVariant(block, "_on", ModelTemplates.RAIL_RAISED_SW, TextureMapping::rail));
/*      */     
/* 1343 */     registerSimpleFlatItemModel(block);
/* 1344 */     this.blockStateOutput.accept(
/* 1345 */         MultiVariantGenerator.dispatch(block)
/* 1346 */         .with(
/* 1347 */           PropertyDispatch.initial((Property)BlockStateProperties.POWERED, (Property)BlockStateProperties.RAIL_SHAPE_STRAIGHT)
/* 1348 */           .generate((powered, railShape) -> {
/*      */               // Byte code:
/*      */               //   0: getstatic net/minecraft/client/data/models/BlockModelGenerators$1.$SwitchMap$net$minecraft$world$level$block$state$properties$RailShape : [I
/*      */               //   3: aload #7
/*      */               //   5: invokevirtual ordinal : ()I
/*      */               //   8: iaload
/*      */               //   9: tableswitch default -> 166, 1 -> 48, 2 -> 64, 3 -> 86, 4 -> 108, 5 -> 132, 6 -> 148
/*      */               //   48: aload #6
/*      */               //   50: invokevirtual booleanValue : ()Z
/*      */               //   53: ifeq -> 60
/*      */               //   56: aload_0
/*      */               //   57: goto -> 177
/*      */               //   60: aload_1
/*      */               //   61: goto -> 177
/*      */               //   64: aload #6
/*      */               //   66: invokevirtual booleanValue : ()Z
/*      */               //   69: ifeq -> 76
/*      */               //   72: aload_0
/*      */               //   73: goto -> 77
/*      */               //   76: aload_1
/*      */               //   77: getstatic net/minecraft/client/data/models/BlockModelGenerators.Y_ROT_90 : Lnet/minecraft/client/renderer/block/model/VariantMutator;
/*      */               //   80: invokevirtual with : (Lnet/minecraft/client/renderer/block/model/VariantMutator;)Lnet/minecraft/client/data/models/MultiVariant;
/*      */               //   83: goto -> 177
/*      */               //   86: aload #6
/*      */               //   88: invokevirtual booleanValue : ()Z
/*      */               //   91: ifeq -> 98
/*      */               //   94: aload_2
/*      */               //   95: goto -> 99
/*      */               //   98: aload_3
/*      */               //   99: getstatic net/minecraft/client/data/models/BlockModelGenerators.Y_ROT_90 : Lnet/minecraft/client/renderer/block/model/VariantMutator;
/*      */               //   102: invokevirtual with : (Lnet/minecraft/client/renderer/block/model/VariantMutator;)Lnet/minecraft/client/data/models/MultiVariant;
/*      */               //   105: goto -> 177
/*      */               //   108: aload #6
/*      */               //   110: invokevirtual booleanValue : ()Z
/*      */               //   113: ifeq -> 121
/*      */               //   116: aload #4
/*      */               //   118: goto -> 123
/*      */               //   121: aload #5
/*      */               //   123: getstatic net/minecraft/client/data/models/BlockModelGenerators.Y_ROT_90 : Lnet/minecraft/client/renderer/block/model/VariantMutator;
/*      */               //   126: invokevirtual with : (Lnet/minecraft/client/renderer/block/model/VariantMutator;)Lnet/minecraft/client/data/models/MultiVariant;
/*      */               //   129: goto -> 177
/*      */               //   132: aload #6
/*      */               //   134: invokevirtual booleanValue : ()Z
/*      */               //   137: ifeq -> 144
/*      */               //   140: aload_2
/*      */               //   141: goto -> 177
/*      */               //   144: aload_3
/*      */               //   145: goto -> 177
/*      */               //   148: aload #6
/*      */               //   150: invokevirtual booleanValue : ()Z
/*      */               //   153: ifeq -> 161
/*      */               //   156: aload #4
/*      */               //   158: goto -> 177
/*      */               //   161: aload #5
/*      */               //   163: goto -> 177
/*      */               //   166: new java/lang/UnsupportedOperationException
/*      */               //   169: dup
/*      */               //   170: ldc_w 'Fix you generator!'
/*      */               //   173: invokespecial <init> : (Ljava/lang/String;)V
/*      */               //   176: athrow
/*      */               //   177: areturn
/*      */               // Line number table:
/*      */               //   Java source line number -> byte code offset
/*      */               //   #1348	-> 0
/*      */               //   #1349	-> 48
/*      */               //   #1350	-> 64
/*      */               //   #1351	-> 86
/*      */               //   #1352	-> 108
/*      */               //   #1353	-> 132
/*      */               //   #1354	-> 148
/*      */               //   #1355	-> 166
/*      */               //   #1348	-> 177
/*      */               // Local variable table:
/*      */               //   start	length	slot	name	descriptor
/*      */               //   0	178	0	flatOn	Lnet/minecraft/client/data/models/MultiVariant;
/*      */               //   0	178	1	flat	Lnet/minecraft/client/data/models/MultiVariant;
/*      */               //   0	178	2	risingNEOn	Lnet/minecraft/client/data/models/MultiVariant;
/*      */               //   0	178	3	risingNE	Lnet/minecraft/client/data/models/MultiVariant;
/*      */               //   0	178	4	risingSWOn	Lnet/minecraft/client/data/models/MultiVariant;
/*      */               //   0	178	5	risingSW	Lnet/minecraft/client/data/models/MultiVariant;
/*      */               //   0	178	6	powered	Ljava/lang/Boolean;
/*      */               //   0	178	7	railShape	Lnet/minecraft/world/level/block/state/properties/RailShape;
/*      */             })));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createAirLikeBlock(Block block, Item particleItem) {
/* 1362 */     MultiVariant dummyModel = plainVariant(ModelTemplates.PARTICLE_ONLY.create(block, TextureMapping.particleFromItem(particleItem), this.modelOutput));
/* 1363 */     this.blockStateOutput.accept(createSimpleBlock(block, dummyModel));
/*      */   }
/*      */   
/*      */   private void createAirLikeBlock(Block block, Identifier particle) {
/* 1367 */     MultiVariant dummyModel = plainVariant(ModelTemplates.PARTICLE_ONLY.create(block, TextureMapping.particle(particle), this.modelOutput));
/* 1368 */     this.blockStateOutput.accept(createSimpleBlock(block, dummyModel));
/*      */   }
/*      */ 
/*      */   
/*      */   private MultiVariant createParticleOnlyBlockModel(Block block, Block particleDonor) {
/* 1373 */     return plainVariant(ModelTemplates.PARTICLE_ONLY.create(block, TextureMapping.particle(particleDonor), this.modelOutput));
/*      */   }
/*      */ 
/*      */   
/*      */   public void createParticleOnlyBlock(Block block, Block particleDonor) {
/* 1378 */     this.blockStateOutput.accept(createSimpleBlock(block, createParticleOnlyBlockModel(block, particleDonor)));
/*      */   }
/*      */   
/*      */   private void createParticleOnlyBlock(Block block) {
/* 1382 */     createParticleOnlyBlock(block, block);
/*      */   }
/*      */   
/*      */   private void createFullAndCarpetBlocks(Block block, Block carpet) {
/* 1386 */     createTrivialCube(block);
/*      */ 
/*      */     
/* 1389 */     MultiVariant model = plainVariant(TexturedModel.CARPET.get(block).create(carpet, this.modelOutput));
/* 1390 */     this.blockStateOutput.accept(createSimpleBlock(carpet, model));
/*      */   }
/*      */   
/*      */   private void createLeafLitter(Block block) {
/* 1394 */     MultiVariant model1 = plainVariant(TexturedModel.LEAF_LITTER_1.create(block, this.modelOutput));
/* 1395 */     MultiVariant model2 = plainVariant(TexturedModel.LEAF_LITTER_2.create(block, this.modelOutput));
/* 1396 */     MultiVariant model3 = plainVariant(TexturedModel.LEAF_LITTER_3.create(block, this.modelOutput));
/* 1397 */     MultiVariant model4 = plainVariant(TexturedModel.LEAF_LITTER_4.create(block, this.modelOutput));
/* 1398 */     registerSimpleFlatItemModel(block.asItem());
/* 1399 */     createSegmentedBlock(block, model1, LEAF_LITTER_MODEL_1_SEGMENT_CONDITION, model2, LEAF_LITTER_MODEL_2_SEGMENT_CONDITION, model3, LEAF_LITTER_MODEL_3_SEGMENT_CONDITION, model4, LEAF_LITTER_MODEL_4_SEGMENT_CONDITION);
/*      */   }
/*      */   
/*      */   private void createFlowerBed(Block flowerbed) {
/* 1403 */     MultiVariant model1 = plainVariant(TexturedModel.FLOWERBED_1.create(flowerbed, this.modelOutput));
/* 1404 */     MultiVariant model2 = plainVariant(TexturedModel.FLOWERBED_2.create(flowerbed, this.modelOutput));
/* 1405 */     MultiVariant model3 = plainVariant(TexturedModel.FLOWERBED_3.create(flowerbed, this.modelOutput));
/* 1406 */     MultiVariant model4 = plainVariant(TexturedModel.FLOWERBED_4.create(flowerbed, this.modelOutput));
/* 1407 */     registerSimpleFlatItemModel(flowerbed.asItem());
/* 1408 */     createSegmentedBlock(flowerbed, model1, FLOWER_BED_MODEL_1_SEGMENT_CONDITION, model2, FLOWER_BED_MODEL_2_SEGMENT_CONDITION, model3, FLOWER_BED_MODEL_3_SEGMENT_CONDITION, model4, FLOWER_BED_MODEL_4_SEGMENT_CONDITION);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createSegmentedBlock(Block segmentedProperty, MultiVariant model1, Function<ConditionBuilder, ConditionBuilder> model1SegmentCondition, MultiVariant model2, Function<ConditionBuilder, ConditionBuilder> model2SegmentCondition, MultiVariant model3, Function<ConditionBuilder, ConditionBuilder> model3SegmentCondition, MultiVariant model4, Function<ConditionBuilder, ConditionBuilder> model4SegmentCondition) {
/* 1416 */     this.blockStateOutput.accept(
/* 1417 */         MultiPartGenerator.multiPart(segmentedProperty)
/* 1418 */         .with(
/* 1419 */           model1SegmentCondition.apply(condition()
/* 1420 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.NORTH)), model1)
/*      */ 
/*      */         
/* 1423 */         .with(
/* 1424 */           model1SegmentCondition.apply(condition()
/* 1425 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.EAST)), 
/* 1426 */           model1.with(Y_ROT_90))
/*      */         
/* 1428 */         .with(
/* 1429 */           model1SegmentCondition.apply(condition()
/* 1430 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.SOUTH)), 
/* 1431 */           model1.with(Y_ROT_180))
/*      */         
/* 1433 */         .with(
/* 1434 */           model1SegmentCondition.apply(condition()
/* 1435 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.WEST)), 
/* 1436 */           model1.with(Y_ROT_270))
/*      */         
/* 1438 */         .with(
/* 1439 */           model2SegmentCondition.apply(condition()
/* 1440 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.NORTH)), model2)
/*      */ 
/*      */         
/* 1443 */         .with(
/* 1444 */           model2SegmentCondition.apply(condition()
/* 1445 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.EAST)), 
/* 1446 */           model2.with(Y_ROT_90))
/*      */         
/* 1448 */         .with(
/* 1449 */           model2SegmentCondition.apply(condition()
/* 1450 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.SOUTH)), 
/* 1451 */           model2.with(Y_ROT_180))
/*      */         
/* 1453 */         .with(
/* 1454 */           model2SegmentCondition.apply(condition()
/* 1455 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.WEST)), 
/* 1456 */           model2.with(Y_ROT_270))
/*      */         
/* 1458 */         .with(
/* 1459 */           model3SegmentCondition.apply(condition()
/* 1460 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.NORTH)), model3)
/*      */ 
/*      */         
/* 1463 */         .with(
/* 1464 */           model3SegmentCondition.apply(condition()
/* 1465 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.EAST)), 
/* 1466 */           model3.with(Y_ROT_90))
/*      */         
/* 1468 */         .with(
/* 1469 */           model3SegmentCondition.apply(condition()
/* 1470 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.SOUTH)), 
/* 1471 */           model3.with(Y_ROT_180))
/*      */         
/* 1473 */         .with(
/* 1474 */           model3SegmentCondition.apply(condition()
/* 1475 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.WEST)), 
/* 1476 */           model3.with(Y_ROT_270))
/*      */         
/* 1478 */         .with(
/* 1479 */           model4SegmentCondition.apply(condition()
/* 1480 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.NORTH)), model4)
/*      */ 
/*      */         
/* 1483 */         .with(
/* 1484 */           model4SegmentCondition.apply(condition()
/* 1485 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.EAST)), 
/* 1486 */           model4.with(Y_ROT_90))
/*      */         
/* 1488 */         .with(
/* 1489 */           model4SegmentCondition.apply(condition()
/* 1490 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.SOUTH)), 
/* 1491 */           model4.with(Y_ROT_180))
/*      */         
/* 1493 */         .with(
/* 1494 */           model4SegmentCondition.apply(condition()
/* 1495 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.WEST)), 
/* 1496 */           model4.with(Y_ROT_270)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createColoredBlockWithRandomRotations(TexturedModel.Provider modelProvider, Block... blocks) {
/* 1502 */     for (Block block : blocks) {
/* 1503 */       Variant model = plainModel(modelProvider.create(block, this.modelOutput));
/* 1504 */       this.blockStateOutput.accept(
/* 1505 */           MultiVariantGenerator.dispatch(block, createRotatedVariants(model)));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void createColoredBlockWithStateRotations(TexturedModel.Provider modelProvider, Block... blocks) {
/* 1511 */     for (Block block : blocks) {
/* 1512 */       MultiVariant model = plainVariant(modelProvider.create(block, this.modelOutput));
/* 1513 */       this.blockStateOutput.accept(
/* 1514 */           MultiVariantGenerator.dispatch(block, model)
/* 1515 */           .with(ROTATION_HORIZONTAL_FACING_ALT));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void createGlassBlocks(Block block, Block pane) {
/* 1521 */     createTrivialCube(block);
/*      */     
/* 1523 */     TextureMapping paneMapping = TextureMapping.pane(block, pane);
/* 1524 */     MultiVariant post = plainVariant(ModelTemplates.STAINED_GLASS_PANE_POST.create(pane, paneMapping, this.modelOutput));
/* 1525 */     MultiVariant side = plainVariant(ModelTemplates.STAINED_GLASS_PANE_SIDE.create(pane, paneMapping, this.modelOutput));
/* 1526 */     MultiVariant sideAlt = plainVariant(ModelTemplates.STAINED_GLASS_PANE_SIDE_ALT.create(pane, paneMapping, this.modelOutput));
/* 1527 */     MultiVariant noSide = plainVariant(ModelTemplates.STAINED_GLASS_PANE_NOSIDE.create(pane, paneMapping, this.modelOutput));
/* 1528 */     MultiVariant noSideAlt = plainVariant(ModelTemplates.STAINED_GLASS_PANE_NOSIDE_ALT.create(pane, paneMapping, this.modelOutput));
/*      */     
/* 1530 */     Item paneItem = pane.asItem();
/* 1531 */     registerSimpleItemModel(paneItem, createFlatItemModelWithBlockTexture(paneItem, block));
/*      */     
/* 1533 */     this.blockStateOutput.accept(
/* 1534 */         MultiPartGenerator.multiPart(pane)
/* 1535 */         .with(post)
/* 1536 */         .with(condition().term((Property)BlockStateProperties.NORTH, true), side)
/* 1537 */         .with(condition().term((Property)BlockStateProperties.EAST, true), side.with(Y_ROT_90))
/* 1538 */         .with(condition().term((Property)BlockStateProperties.SOUTH, true), sideAlt)
/* 1539 */         .with(condition().term((Property)BlockStateProperties.WEST, true), sideAlt.with(Y_ROT_90))
/*      */         
/* 1541 */         .with(condition().term((Property)BlockStateProperties.NORTH, false), noSide)
/* 1542 */         .with(condition().term((Property)BlockStateProperties.EAST, false), noSideAlt)
/* 1543 */         .with(condition().term((Property)BlockStateProperties.SOUTH, false), noSideAlt.with(Y_ROT_90))
/* 1544 */         .with(condition().term((Property)BlockStateProperties.WEST, false), noSide.with(Y_ROT_270)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createCommandBlock(Block block) {
/* 1549 */     TextureMapping normalTextures = TextureMapping.commandBlock(block);
/*      */     
/* 1551 */     MultiVariant normalModel = plainVariant(ModelTemplates.COMMAND_BLOCK.create(block, normalTextures, this.modelOutput));
/* 1552 */     MultiVariant conditionalModel = plainVariant(createSuffixedVariant(block, "_conditional", ModelTemplates.COMMAND_BLOCK, id -> normalTextures.copyAndUpdate(TextureSlot.SIDE, id)));
/*      */     
/* 1554 */     this.blockStateOutput.accept(
/* 1555 */         MultiVariantGenerator.dispatch(block)
/* 1556 */         .with(createBooleanModelDispatch(BlockStateProperties.CONDITIONAL, conditionalModel, normalModel))
/* 1557 */         .with(ROTATION_FACING));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createAnvil(Block block) {
/* 1562 */     MultiVariant anvilModel = plainVariant(TexturedModel.ANVIL.create(block, this.modelOutput));
/* 1563 */     this.blockStateOutput.accept(
/* 1564 */         createSimpleBlock(block, anvilModel)
/* 1565 */         .with(ROTATION_HORIZONTAL_FACING_ALT));
/*      */   }
/*      */ 
/*      */   
/*      */   private static MultiVariant createBambooModels(int age) {
/* 1570 */     String ageSuffix = "_age" + age;
/* 1571 */     return new MultiVariant(WeightedList.of((List)IntStream.range(1, 5)
/* 1572 */           .mapToObj(i -> new Weighted(plainModel(ModelLocationUtils.getModelLocation(Blocks.BAMBOO, "" + i + i)), 1))
/* 1573 */           .collect(Collectors.toList())));
/*      */   }
/*      */   
/*      */   private void createBamboo() {
/* 1577 */     this.blockStateOutput.accept(
/* 1578 */         MultiPartGenerator.multiPart(Blocks.BAMBOO)
/* 1579 */         .with(condition().term((Property)BlockStateProperties.AGE_1, 0), createBambooModels(0))
/* 1580 */         .with(condition().term((Property)BlockStateProperties.AGE_1, 1), createBambooModels(1))
/* 1581 */         .with(condition().term((Property)BlockStateProperties.BAMBOO_LEAVES, (Comparable)BambooLeaves.SMALL), plainVariant(ModelLocationUtils.getModelLocation(Blocks.BAMBOO, "_small_leaves")))
/* 1582 */         .with(condition().term((Property)BlockStateProperties.BAMBOO_LEAVES, (Comparable)BambooLeaves.LARGE), plainVariant(ModelLocationUtils.getModelLocation(Blocks.BAMBOO, "_large_leaves"))));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createBarrel() {
/* 1587 */     Identifier openTop = TextureMapping.getBlockTexture(Blocks.BARREL, "_top_open");
/*      */     
/* 1589 */     MultiVariant closedModel = plainVariant(TexturedModel.CUBE_TOP_BOTTOM.create(Blocks.BARREL, this.modelOutput));
/* 1590 */     MultiVariant openModel = plainVariant(TexturedModel.CUBE_TOP_BOTTOM.get(Blocks.BARREL).updateTextures(t -> t.put(TextureSlot.TOP, openTop)).createWithSuffix(Blocks.BARREL, "_open", this.modelOutput));
/* 1591 */     this.blockStateOutput.accept(
/* 1592 */         MultiVariantGenerator.dispatch(Blocks.BARREL)
/* 1593 */         .with(
/* 1594 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.OPEN)
/* 1595 */           .select(false, closedModel)
/* 1596 */           .select(true, openModel))
/*      */         
/* 1598 */         .with(ROTATIONS_COLUMN_WITH_FACING));
/*      */   }
/*      */ 
/*      */   
/*      */   private static <T extends Comparable<T>> PropertyDispatch<MultiVariant> createEmptyOrFullDispatch(Property<T> property, T threshold, MultiVariant fullModel, MultiVariant emptyModel) {
/* 1603 */     return PropertyDispatch.initial(property)
/* 1604 */       .generate(value -> {
/*      */           boolean isFull = (value.compareTo(threshold) >= 0);
/*      */           return isFull ? fullModel : emptyModel;
/*      */         });
/*      */   }
/*      */   
/*      */   private void createBeeNest(Block block, Function<Block, TextureMapping> mappingFunction) {
/* 1611 */     TextureMapping emptyMapping = ((TextureMapping)mappingFunction.apply(block)).copyForced(TextureSlot.SIDE, TextureSlot.PARTICLE);
/* 1612 */     TextureMapping fullMapping = emptyMapping.copyAndUpdate(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front_honey"));
/*      */     
/* 1614 */     Identifier emptyModel = ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.createWithSuffix(block, "_empty", emptyMapping, this.modelOutput);
/* 1615 */     Identifier fullModel = ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.createWithSuffix(block, "_honey", fullMapping, this.modelOutput);
/*      */     
/* 1617 */     this.itemModelOutput.accept(block.asItem(), ItemModelUtils.selectBlockItemProperty((Property)BeehiveBlock.HONEY_LEVEL, 
/*      */           
/* 1619 */           ItemModelUtils.plainModel(emptyModel), 
/* 1620 */           Map.of(5, 
/* 1621 */             ItemModelUtils.plainModel(fullModel))));
/*      */ 
/*      */ 
/*      */     
/* 1625 */     this.blockStateOutput.accept(
/* 1626 */         MultiVariantGenerator.dispatch(block)
/* 1627 */         .with(createEmptyOrFullDispatch((Property<Integer>)BeehiveBlock.HONEY_LEVEL, 5, plainVariant(fullModel), plainVariant(emptyModel)))
/* 1628 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createCropBlock(Block block, Property<Integer> property, int... stages) {
/* 1633 */     registerSimpleFlatItemModel(block.asItem());
/*      */     
/* 1635 */     if (property.getPossibleValues().size() != stages.length) {
/* 1636 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/* 1639 */     Int2ObjectOpenHashMap int2ObjectOpenHashMap = new Int2ObjectOpenHashMap();
/* 1640 */     this.blockStateOutput.accept(
/* 1641 */         MultiVariantGenerator.dispatch(block)
/* 1642 */         .with(
/* 1643 */           PropertyDispatch.initial(property)
/* 1644 */           .generate(i -> {
/*      */               int stage = stages[block];
/*      */               return plainVariant((Identifier)stages.computeIfAbsent(stage, ()));
/*      */             })));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createBell() {
/* 1653 */     MultiVariant floor = plainVariant(ModelLocationUtils.getModelLocation(Blocks.BELL, "_floor"));
/* 1654 */     MultiVariant ceiling = plainVariant(ModelLocationUtils.getModelLocation(Blocks.BELL, "_ceiling"));
/* 1655 */     MultiVariant wall = plainVariant(ModelLocationUtils.getModelLocation(Blocks.BELL, "_wall"));
/* 1656 */     MultiVariant betweenWalls = plainVariant(ModelLocationUtils.getModelLocation(Blocks.BELL, "_between_walls"));
/*      */     
/* 1658 */     registerSimpleFlatItemModel(Items.BELL);
/* 1659 */     this.blockStateOutput.accept(
/* 1660 */         MultiVariantGenerator.dispatch(Blocks.BELL)
/* 1661 */         .with(
/* 1662 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.HORIZONTAL_FACING, (Property)BlockStateProperties.BELL_ATTACHMENT)
/* 1663 */           .select((Comparable)Direction.NORTH, (Comparable)BellAttachType.FLOOR, floor)
/* 1664 */           .select((Comparable)Direction.SOUTH, (Comparable)BellAttachType.FLOOR, floor.with(Y_ROT_180))
/* 1665 */           .select((Comparable)Direction.EAST, (Comparable)BellAttachType.FLOOR, floor.with(Y_ROT_90))
/* 1666 */           .select((Comparable)Direction.WEST, (Comparable)BellAttachType.FLOOR, floor.with(Y_ROT_270))
/*      */           
/* 1668 */           .select((Comparable)Direction.NORTH, (Comparable)BellAttachType.CEILING, ceiling)
/* 1669 */           .select((Comparable)Direction.SOUTH, (Comparable)BellAttachType.CEILING, ceiling.with(Y_ROT_180))
/* 1670 */           .select((Comparable)Direction.EAST, (Comparable)BellAttachType.CEILING, ceiling.with(Y_ROT_90))
/* 1671 */           .select((Comparable)Direction.WEST, (Comparable)BellAttachType.CEILING, ceiling.with(Y_ROT_270))
/*      */           
/* 1673 */           .select((Comparable)Direction.NORTH, (Comparable)BellAttachType.SINGLE_WALL, wall.with(Y_ROT_270))
/* 1674 */           .select((Comparable)Direction.SOUTH, (Comparable)BellAttachType.SINGLE_WALL, wall.with(Y_ROT_90))
/* 1675 */           .select((Comparable)Direction.EAST, (Comparable)BellAttachType.SINGLE_WALL, wall)
/* 1676 */           .select((Comparable)Direction.WEST, (Comparable)BellAttachType.SINGLE_WALL, wall.with(Y_ROT_180))
/*      */           
/* 1678 */           .select((Comparable)Direction.SOUTH, (Comparable)BellAttachType.DOUBLE_WALL, betweenWalls.with(Y_ROT_90))
/* 1679 */           .select((Comparable)Direction.NORTH, (Comparable)BellAttachType.DOUBLE_WALL, betweenWalls.with(Y_ROT_270))
/* 1680 */           .select((Comparable)Direction.EAST, (Comparable)BellAttachType.DOUBLE_WALL, betweenWalls)
/* 1681 */           .select((Comparable)Direction.WEST, (Comparable)BellAttachType.DOUBLE_WALL, betweenWalls.with(Y_ROT_180))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createGrindstone() {
/* 1687 */     this.blockStateOutput.accept(
/* 1688 */         MultiVariantGenerator.dispatch(Blocks.GRINDSTONE, plainVariant(ModelLocationUtils.getModelLocation(Blocks.GRINDSTONE)))
/* 1689 */         .with(
/* 1690 */           (PropertyDispatch)PropertyDispatch.modify((Property)BlockStateProperties.ATTACH_FACE, (Property)BlockStateProperties.HORIZONTAL_FACING)
/* 1691 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.NORTH, NOP)
/* 1692 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.EAST, Y_ROT_90)
/* 1693 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.SOUTH, Y_ROT_180)
/* 1694 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.WEST, Y_ROT_270)
/*      */           
/* 1696 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.NORTH, X_ROT_90)
/* 1697 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.EAST, X_ROT_90.then(Y_ROT_90))
/* 1698 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.SOUTH, X_ROT_90.then(Y_ROT_180))
/* 1699 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.WEST, X_ROT_90.then(Y_ROT_270))
/*      */           
/* 1701 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.SOUTH, X_ROT_180)
/* 1702 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.WEST, X_ROT_180.then(Y_ROT_90))
/* 1703 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.NORTH, X_ROT_180.then(Y_ROT_180))
/* 1704 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.EAST, X_ROT_180.then(Y_ROT_270))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createFurnace(Block furnace, TexturedModel.Provider provider) {
/* 1710 */     MultiVariant normalModel = plainVariant(provider.create(furnace, this.modelOutput));
/*      */     
/* 1712 */     Identifier frontTexture = TextureMapping.getBlockTexture(furnace, "_front_on");
/* 1713 */     MultiVariant litModel = plainVariant(provider.get(furnace).updateTextures(t -> t.put(TextureSlot.FRONT, frontTexture)).createWithSuffix(furnace, "_on", this.modelOutput));
/*      */     
/* 1715 */     this.blockStateOutput.accept(
/* 1716 */         MultiVariantGenerator.dispatch(furnace)
/* 1717 */         .with(createBooleanModelDispatch(BlockStateProperties.LIT, litModel, normalModel))
/* 1718 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createCampfires(Block... campFires) {
/* 1723 */     MultiVariant offModel = plainVariant(ModelLocationUtils.decorateBlockModelLocation("campfire_off"));
/*      */     
/* 1725 */     for (Block campFire : campFires) {
/* 1726 */       MultiVariant litModel = plainVariant(ModelTemplates.CAMPFIRE.create(campFire, TextureMapping.campfire(campFire), this.modelOutput));
/*      */       
/* 1728 */       registerSimpleFlatItemModel(campFire.asItem());
/* 1729 */       this.blockStateOutput.accept(
/* 1730 */           MultiVariantGenerator.dispatch(campFire)
/* 1731 */           .with(createBooleanModelDispatch(BlockStateProperties.LIT, litModel, offModel))
/* 1732 */           .with(ROTATION_HORIZONTAL_FACING_ALT));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void createAzalea(Block block) {
/* 1738 */     MultiVariant model = plainVariant(ModelTemplates.AZALEA.create(block, TextureMapping.cubeTop(block), this.modelOutput));
/* 1739 */     this.blockStateOutput.accept(createSimpleBlock(block, model));
/*      */   }
/*      */   
/*      */   private void createPottedAzalea(Block block) {
/*      */     MultiVariant model;
/* 1744 */     if (block == Blocks.POTTED_FLOWERING_AZALEA) {
/* 1745 */       model = plainVariant(ModelTemplates.POTTED_FLOWERING_AZALEA.create(block, TextureMapping.pottedAzalea(block), this.modelOutput));
/*      */     } else {
/* 1747 */       model = plainVariant(ModelTemplates.POTTED_AZALEA.create(block, TextureMapping.pottedAzalea(block), this.modelOutput));
/*      */     } 
/* 1749 */     this.blockStateOutput.accept(createSimpleBlock(block, model));
/*      */   }
/*      */   
/*      */   private void createBookshelf() {
/* 1753 */     TextureMapping textures = TextureMapping.column(TextureMapping.getBlockTexture(Blocks.BOOKSHELF), TextureMapping.getBlockTexture(Blocks.OAK_PLANKS));
/* 1754 */     MultiVariant model = plainVariant(ModelTemplates.CUBE_COLUMN.create(Blocks.BOOKSHELF, textures, this.modelOutput));
/* 1755 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.BOOKSHELF, model));
/*      */   }
/*      */   
/*      */   private void createRedstoneWire() {
/* 1759 */     registerSimpleFlatItemModel(Items.REDSTONE);
/* 1760 */     this.blockStateOutput.accept(
/* 1761 */         MultiPartGenerator.multiPart(Blocks.REDSTONE_WIRE)
/* 1762 */         .with(
/* 1763 */           or(new ConditionBuilder[] {
/* 1764 */               condition()
/* 1765 */               .term((Property)BlockStateProperties.NORTH_REDSTONE, (Comparable)RedstoneSide.NONE)
/* 1766 */               .term((Property)BlockStateProperties.EAST_REDSTONE, (Comparable)RedstoneSide.NONE)
/* 1767 */               .term((Property)BlockStateProperties.SOUTH_REDSTONE, (Comparable)RedstoneSide.NONE)
/* 1768 */               .term((Property)BlockStateProperties.WEST_REDSTONE, (Comparable)RedstoneSide.NONE), 
/* 1769 */               condition()
/* 1770 */               .term((Property)BlockStateProperties.NORTH_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1771 */                 }).term((Property)BlockStateProperties.EAST_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1772 */                 }), condition()
/* 1773 */               .term((Property)BlockStateProperties.EAST_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1774 */                 }).term((Property)BlockStateProperties.SOUTH_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1775 */                 }), condition()
/* 1776 */               .term((Property)BlockStateProperties.SOUTH_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1777 */                 }).term((Property)BlockStateProperties.WEST_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1778 */                 }), condition()
/* 1779 */               .term((Property)BlockStateProperties.WEST_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1780 */                 }).term((Property)BlockStateProperties.NORTH_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/*      */                 })
/* 1782 */             }), plainVariant(ModelLocationUtils.decorateBlockModelLocation("redstone_dust_dot")))
/*      */         
/* 1784 */         .with(
/* 1785 */           condition()
/* 1786 */           .term((Property)BlockStateProperties.NORTH_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1787 */             }), plainVariant(ModelLocationUtils.decorateBlockModelLocation("redstone_dust_side0")))
/*      */         
/* 1789 */         .with(
/* 1790 */           condition()
/* 1791 */           .term((Property)BlockStateProperties.SOUTH_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1792 */             }), plainVariant(ModelLocationUtils.decorateBlockModelLocation("redstone_dust_side_alt0")))
/*      */         
/* 1794 */         .with(
/* 1795 */           condition()
/* 1796 */           .term((Property)BlockStateProperties.EAST_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1797 */             }), plainVariant(ModelLocationUtils.decorateBlockModelLocation("redstone_dust_side_alt1")).with(Y_ROT_270))
/*      */         
/* 1799 */         .with(
/* 1800 */           condition()
/* 1801 */           .term((Property)BlockStateProperties.WEST_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1802 */             }), plainVariant(ModelLocationUtils.decorateBlockModelLocation("redstone_dust_side1")).with(Y_ROT_270))
/*      */         
/* 1804 */         .with(
/* 1805 */           condition().term((Property)BlockStateProperties.NORTH_REDSTONE, (Comparable)RedstoneSide.UP), 
/* 1806 */           plainVariant(ModelLocationUtils.decorateBlockModelLocation("redstone_dust_up")))
/*      */         
/* 1808 */         .with(
/* 1809 */           condition().term((Property)BlockStateProperties.EAST_REDSTONE, (Comparable)RedstoneSide.UP), 
/* 1810 */           plainVariant(ModelLocationUtils.decorateBlockModelLocation("redstone_dust_up")).with(Y_ROT_90))
/*      */         
/* 1812 */         .with(
/* 1813 */           condition().term((Property)BlockStateProperties.SOUTH_REDSTONE, (Comparable)RedstoneSide.UP), 
/* 1814 */           plainVariant(ModelLocationUtils.decorateBlockModelLocation("redstone_dust_up")).with(Y_ROT_180))
/*      */         
/* 1816 */         .with(
/* 1817 */           condition().term((Property)BlockStateProperties.WEST_REDSTONE, (Comparable)RedstoneSide.UP), 
/* 1818 */           plainVariant(ModelLocationUtils.decorateBlockModelLocation("redstone_dust_up")).with(Y_ROT_270)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createComparator() {
/* 1824 */     registerSimpleFlatItemModel(Items.COMPARATOR);
/* 1825 */     this.blockStateOutput.accept(
/* 1826 */         MultiVariantGenerator.dispatch(Blocks.COMPARATOR)
/* 1827 */         .with(
/* 1828 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.MODE_COMPARATOR, (Property)BlockStateProperties.POWERED)
/* 1829 */           .select((Comparable)ComparatorMode.COMPARE, false, plainVariant(ModelLocationUtils.getModelLocation(Blocks.COMPARATOR)))
/* 1830 */           .select((Comparable)ComparatorMode.COMPARE, true, plainVariant(ModelLocationUtils.getModelLocation(Blocks.COMPARATOR, "_on")))
/* 1831 */           .select((Comparable)ComparatorMode.SUBTRACT, false, plainVariant(ModelLocationUtils.getModelLocation(Blocks.COMPARATOR, "_subtract")))
/* 1832 */           .select((Comparable)ComparatorMode.SUBTRACT, true, plainVariant(ModelLocationUtils.getModelLocation(Blocks.COMPARATOR, "_on_subtract"))))
/*      */         
/* 1834 */         .with(ROTATION_HORIZONTAL_FACING_ALT));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createSmoothStoneSlab() {
/* 1839 */     TextureMapping smoothStoneTextures = TextureMapping.cube(Blocks.SMOOTH_STONE);
/* 1840 */     TextureMapping smoothStoneSlabTextures = TextureMapping.column(
/* 1841 */         TextureMapping.getBlockTexture(Blocks.SMOOTH_STONE_SLAB, "_side"), 
/* 1842 */         smoothStoneTextures.get(TextureSlot.TOP));
/*      */     
/* 1844 */     MultiVariant bottom = plainVariant(ModelTemplates.SLAB_BOTTOM.create(Blocks.SMOOTH_STONE_SLAB, smoothStoneSlabTextures, this.modelOutput));
/* 1845 */     MultiVariant top = plainVariant(ModelTemplates.SLAB_TOP.create(Blocks.SMOOTH_STONE_SLAB, smoothStoneSlabTextures, this.modelOutput));
/* 1846 */     MultiVariant doubleSlab = plainVariant(ModelTemplates.CUBE_COLUMN.createWithOverride(Blocks.SMOOTH_STONE_SLAB, "_double", smoothStoneSlabTextures, this.modelOutput));
/* 1847 */     this.blockStateOutput.accept(createSlab(Blocks.SMOOTH_STONE_SLAB, bottom, top, doubleSlab));
/* 1848 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.SMOOTH_STONE, plainVariant(ModelTemplates.CUBE_ALL.create(Blocks.SMOOTH_STONE, smoothStoneTextures, this.modelOutput))));
/*      */   }
/*      */   
/*      */   private void createBrewingStand() {
/* 1852 */     registerSimpleFlatItemModel(Items.BREWING_STAND);
/* 1853 */     this.blockStateOutput.accept(
/* 1854 */         MultiPartGenerator.multiPart(Blocks.BREWING_STAND)
/* 1855 */         .with(plainVariant(TextureMapping.getBlockTexture(Blocks.BREWING_STAND)))
/* 1856 */         .with(condition().term((Property)BlockStateProperties.HAS_BOTTLE_0, true), plainVariant(TextureMapping.getBlockTexture(Blocks.BREWING_STAND, "_bottle0")))
/* 1857 */         .with(condition().term((Property)BlockStateProperties.HAS_BOTTLE_1, true), plainVariant(TextureMapping.getBlockTexture(Blocks.BREWING_STAND, "_bottle1")))
/* 1858 */         .with(condition().term((Property)BlockStateProperties.HAS_BOTTLE_2, true), plainVariant(TextureMapping.getBlockTexture(Blocks.BREWING_STAND, "_bottle2")))
/*      */         
/* 1860 */         .with(condition().term((Property)BlockStateProperties.HAS_BOTTLE_0, false), plainVariant(TextureMapping.getBlockTexture(Blocks.BREWING_STAND, "_empty0")))
/* 1861 */         .with(condition().term((Property)BlockStateProperties.HAS_BOTTLE_1, false), plainVariant(TextureMapping.getBlockTexture(Blocks.BREWING_STAND, "_empty1")))
/* 1862 */         .with(condition().term((Property)BlockStateProperties.HAS_BOTTLE_2, false), plainVariant(TextureMapping.getBlockTexture(Blocks.BREWING_STAND, "_empty2"))));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createMushroomBlock(Block block) {
/* 1867 */     MultiVariant skin = plainVariant(ModelTemplates.SINGLE_FACE.create(block, TextureMapping.defaultTexture(block), this.modelOutput));
/* 1868 */     MultiVariant skinless = plainVariant(ModelLocationUtils.decorateBlockModelLocation("mushroom_block_inside"));
/*      */     
/* 1870 */     this.blockStateOutput.accept(
/* 1871 */         MultiPartGenerator.multiPart(block)
/* 1872 */         .with(condition().term((Property)BlockStateProperties.NORTH, true), skin)
/* 1873 */         .with(condition().term((Property)BlockStateProperties.EAST, true), skin.with(Y_ROT_90).with(UV_LOCK))
/* 1874 */         .with(condition().term((Property)BlockStateProperties.SOUTH, true), skin.with(Y_ROT_180).with(UV_LOCK))
/* 1875 */         .with(condition().term((Property)BlockStateProperties.WEST, true), skin.with(Y_ROT_270).with(UV_LOCK))
/* 1876 */         .with(condition().term((Property)BlockStateProperties.UP, true), skin.with(X_ROT_270).with(UV_LOCK))
/* 1877 */         .with(condition().term((Property)BlockStateProperties.DOWN, true), skin.with(X_ROT_90).with(UV_LOCK))
/*      */         
/* 1879 */         .with(condition().term((Property)BlockStateProperties.NORTH, false), skinless)
/* 1880 */         .with(condition().term((Property)BlockStateProperties.EAST, false), skinless.with(Y_ROT_90))
/* 1881 */         .with(condition().term((Property)BlockStateProperties.SOUTH, false), skinless.with(Y_ROT_180))
/* 1882 */         .with(condition().term((Property)BlockStateProperties.WEST, false), skinless.with(Y_ROT_270))
/* 1883 */         .with(condition().term((Property)BlockStateProperties.UP, false), skinless.with(X_ROT_270))
/* 1884 */         .with(condition().term((Property)BlockStateProperties.DOWN, false), skinless.with(X_ROT_90)));
/*      */ 
/*      */     
/* 1887 */     registerSimpleItemModel(block, TexturedModel.CUBE.createWithSuffix(block, "_inventory", this.modelOutput));
/*      */   }
/*      */   
/*      */   private void createCakeBlock() {
/* 1891 */     registerSimpleFlatItemModel(Items.CAKE);
/* 1892 */     this.blockStateOutput.accept(
/* 1893 */         MultiVariantGenerator.dispatch(Blocks.CAKE)
/* 1894 */         .with(
/* 1895 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.BITES)
/* 1896 */           .select(0, plainVariant(ModelLocationUtils.getModelLocation(Blocks.CAKE)))
/* 1897 */           .select(1, plainVariant(ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice1")))
/* 1898 */           .select(2, plainVariant(ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice2")))
/* 1899 */           .select(3, plainVariant(ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice3")))
/* 1900 */           .select(4, plainVariant(ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice4")))
/* 1901 */           .select(5, plainVariant(ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice5")))
/* 1902 */           .select(6, plainVariant(ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice6")))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createCartographyTable() {
/* 1908 */     TextureMapping mapping = new TextureMapping()
/* 1909 */       .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side3"))
/* 1910 */       .put(TextureSlot.DOWN, TextureMapping.getBlockTexture(Blocks.DARK_OAK_PLANKS))
/* 1911 */       .put(TextureSlot.UP, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_top"))
/* 1912 */       .put(TextureSlot.NORTH, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side3"))
/* 1913 */       .put(TextureSlot.EAST, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side3"))
/* 1914 */       .put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side1"))
/* 1915 */       .put(TextureSlot.WEST, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side2"));
/*      */     
/* 1917 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.CARTOGRAPHY_TABLE, plainVariant(ModelTemplates.CUBE.create(Blocks.CARTOGRAPHY_TABLE, mapping, this.modelOutput))));
/*      */   }
/*      */   
/*      */   private void createSmithingTable() {
/* 1921 */     TextureMapping mapping = new TextureMapping()
/* 1922 */       .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_front"))
/* 1923 */       .put(TextureSlot.DOWN, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_bottom"))
/* 1924 */       .put(TextureSlot.UP, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_top"))
/* 1925 */       .put(TextureSlot.NORTH, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_front"))
/* 1926 */       .put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_front"))
/* 1927 */       .put(TextureSlot.EAST, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_side"))
/* 1928 */       .put(TextureSlot.WEST, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_side"));
/*      */     
/* 1930 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.SMITHING_TABLE, plainVariant(ModelTemplates.CUBE.create(Blocks.SMITHING_TABLE, mapping, this.modelOutput))));
/*      */   }
/*      */   
/*      */   private void createCraftingTableLike(Block block, Block bottomBlock, BiFunction<Block, Block, TextureMapping> mappingProvider) {
/* 1934 */     TextureMapping mapping = mappingProvider.apply(block, bottomBlock);
/* 1935 */     this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE.create(block, mapping, this.modelOutput))));
/*      */   }
/*      */   
/*      */   public void createGenericCube(Block block) {
/* 1939 */     TextureMapping mapping = new TextureMapping()
/* 1940 */       .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_particle"))
/* 1941 */       .put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_down"))
/* 1942 */       .put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_up"))
/* 1943 */       .put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_north"))
/* 1944 */       .put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_south"))
/* 1945 */       .put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_east"))
/* 1946 */       .put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_west"));
/*      */     
/* 1948 */     this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE.create(block, mapping, this.modelOutput))));
/*      */   }
/*      */   
/*      */   private void createPumpkins() {
/* 1952 */     TextureMapping pumpkinTextures = TextureMapping.column(Blocks.PUMPKIN);
/*      */     
/* 1954 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.PUMPKIN, plainVariant(ModelLocationUtils.getModelLocation(Blocks.PUMPKIN))));
/*      */     
/* 1956 */     createPumpkinVariant(Blocks.CARVED_PUMPKIN, pumpkinTextures);
/* 1957 */     createPumpkinVariant(Blocks.JACK_O_LANTERN, pumpkinTextures);
/*      */   }
/*      */   
/*      */   private void createPumpkinVariant(Block block, TextureMapping textures) {
/* 1961 */     MultiVariant model = plainVariant(ModelTemplates.CUBE_ORIENTABLE.create(block, textures.copyAndUpdate(TextureSlot.FRONT, TextureMapping.getBlockTexture(block)), this.modelOutput));
/* 1962 */     this.blockStateOutput.accept(
/* 1963 */         MultiVariantGenerator.dispatch(block, model)
/* 1964 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createCauldrons() {
/* 1969 */     registerSimpleFlatItemModel(Items.CAULDRON);
/* 1970 */     createNonTemplateModelBlock(Blocks.CAULDRON);
/*      */     
/* 1972 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.LAVA_CAULDRON, plainVariant(ModelTemplates.CAULDRON_FULL.create(Blocks.LAVA_CAULDRON, TextureMapping.cauldron(TextureMapping.getBlockTexture(Blocks.LAVA, "_still")), this.modelOutput))));
/*      */     
/* 1974 */     this.blockStateOutput.accept(
/* 1975 */         MultiVariantGenerator.dispatch(Blocks.WATER_CAULDRON)
/* 1976 */         .with(
/* 1977 */           (PropertyDispatch)PropertyDispatch.initial((Property)LayeredCauldronBlock.LEVEL)
/* 1978 */           .select(1, plainVariant(ModelTemplates.CAULDRON_LEVEL1.createWithSuffix(Blocks.WATER_CAULDRON, "_level1", TextureMapping.cauldron(TextureMapping.getBlockTexture(Blocks.WATER, "_still")), this.modelOutput)))
/* 1979 */           .select(2, plainVariant(ModelTemplates.CAULDRON_LEVEL2.createWithSuffix(Blocks.WATER_CAULDRON, "_level2", TextureMapping.cauldron(TextureMapping.getBlockTexture(Blocks.WATER, "_still")), this.modelOutput)))
/* 1980 */           .select(3, plainVariant(ModelTemplates.CAULDRON_FULL.createWithSuffix(Blocks.WATER_CAULDRON, "_full", TextureMapping.cauldron(TextureMapping.getBlockTexture(Blocks.WATER, "_still")), this.modelOutput)))));
/*      */ 
/*      */ 
/*      */     
/* 1984 */     this.blockStateOutput.accept(
/* 1985 */         MultiVariantGenerator.dispatch(Blocks.POWDER_SNOW_CAULDRON)
/* 1986 */         .with(
/* 1987 */           (PropertyDispatch)PropertyDispatch.initial((Property)LayeredCauldronBlock.LEVEL)
/* 1988 */           .select(1, plainVariant(ModelTemplates.CAULDRON_LEVEL1.createWithSuffix(Blocks.POWDER_SNOW_CAULDRON, "_level1", TextureMapping.cauldron(TextureMapping.getBlockTexture(Blocks.POWDER_SNOW)), this.modelOutput)))
/* 1989 */           .select(2, plainVariant(ModelTemplates.CAULDRON_LEVEL2.createWithSuffix(Blocks.POWDER_SNOW_CAULDRON, "_level2", TextureMapping.cauldron(TextureMapping.getBlockTexture(Blocks.POWDER_SNOW)), this.modelOutput)))
/* 1990 */           .select(3, plainVariant(ModelTemplates.CAULDRON_FULL.createWithSuffix(Blocks.POWDER_SNOW_CAULDRON, "_full", TextureMapping.cauldron(TextureMapping.getBlockTexture(Blocks.POWDER_SNOW)), this.modelOutput)))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createChorusFlower() {
/* 1996 */     TextureMapping aliveTextures = TextureMapping.defaultTexture(Blocks.CHORUS_FLOWER);
/* 1997 */     MultiVariant aliveModel = plainVariant(ModelTemplates.CHORUS_FLOWER.create(Blocks.CHORUS_FLOWER, aliveTextures, this.modelOutput));
/* 1998 */     MultiVariant deadModel = plainVariant(createSuffixedVariant(Blocks.CHORUS_FLOWER, "_dead", ModelTemplates.CHORUS_FLOWER, id -> aliveTextures.copyAndUpdate(TextureSlot.TEXTURE, id)));
/*      */     
/* 2000 */     this.blockStateOutput.accept(
/* 2001 */         MultiVariantGenerator.dispatch(Blocks.CHORUS_FLOWER)
/* 2002 */         .with(createEmptyOrFullDispatch((Property<Integer>)BlockStateProperties.AGE_5, 5, deadModel, aliveModel)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createCrafterBlock() {
/* 2007 */     MultiVariant off = plainVariant(ModelLocationUtils.getModelLocation(Blocks.CRAFTER));
/* 2008 */     MultiVariant triggeredLocation = plainVariant(ModelLocationUtils.getModelLocation(Blocks.CRAFTER, "_triggered"));
/* 2009 */     MultiVariant craftingLocation = plainVariant(ModelLocationUtils.getModelLocation(Blocks.CRAFTER, "_crafting"));
/* 2010 */     MultiVariant craftingTriggeredLocation = plainVariant(ModelLocationUtils.getModelLocation(Blocks.CRAFTER, "_crafting_triggered"));
/*      */     
/* 2012 */     this.blockStateOutput.accept(
/* 2013 */         MultiVariantGenerator.dispatch(Blocks.CRAFTER)
/* 2014 */         .with(
/* 2015 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.TRIGGERED, (Property)CrafterBlock.CRAFTING)
/* 2016 */           .select(false, false, off)
/* 2017 */           .select(true, true, craftingTriggeredLocation)
/* 2018 */           .select(true, false, triggeredLocation)
/* 2019 */           .select(false, true, craftingLocation))
/*      */         
/* 2021 */         .with(
/* 2022 */           PropertyDispatch.modify((Property)BlockStateProperties.ORIENTATION)
/* 2023 */           .generate(BlockModelGenerators::applyRotation)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createDispenserBlock(Block block) {
/* 2029 */     TextureMapping horizontalTextures = new TextureMapping()
/* 2030 */       .put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.FURNACE, "_top"))
/* 2031 */       .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.FURNACE, "_side"))
/* 2032 */       .put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front"));
/*      */     
/* 2034 */     TextureMapping verticalTextures = new TextureMapping()
/* 2035 */       .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.FURNACE, "_top"))
/* 2036 */       .put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front_vertical"));
/*      */     
/* 2038 */     MultiVariant horizontalModel = plainVariant(ModelTemplates.CUBE_ORIENTABLE.create(block, horizontalTextures, this.modelOutput));
/* 2039 */     MultiVariant verticalModel = plainVariant(ModelTemplates.CUBE_ORIENTABLE_VERTICAL.create(block, verticalTextures, this.modelOutput));
/*      */     
/* 2041 */     this.blockStateOutput.accept(
/* 2042 */         MultiVariantGenerator.dispatch(block)
/* 2043 */         .with(
/* 2044 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.FACING)
/* 2045 */           .select((Comparable)Direction.DOWN, verticalModel.with(X_ROT_180))
/* 2046 */           .select((Comparable)Direction.UP, verticalModel)
/*      */           
/* 2048 */           .select((Comparable)Direction.NORTH, horizontalModel)
/* 2049 */           .select((Comparable)Direction.EAST, horizontalModel.with(Y_ROT_90))
/* 2050 */           .select((Comparable)Direction.SOUTH, horizontalModel.with(Y_ROT_180))
/* 2051 */           .select((Comparable)Direction.WEST, horizontalModel.with(Y_ROT_270))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createEndPortalFrame() {
/* 2058 */     MultiVariant empty = plainVariant(ModelLocationUtils.getModelLocation(Blocks.END_PORTAL_FRAME));
/* 2059 */     MultiVariant filled = plainVariant(ModelLocationUtils.getModelLocation(Blocks.END_PORTAL_FRAME, "_filled"));
/*      */     
/* 2061 */     this.blockStateOutput.accept(
/* 2062 */         MultiVariantGenerator.dispatch(Blocks.END_PORTAL_FRAME)
/* 2063 */         .with(
/* 2064 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.EYE)
/* 2065 */           .select(false, empty)
/* 2066 */           .select(true, filled))
/*      */         
/* 2068 */         .with(ROTATION_HORIZONTAL_FACING_ALT));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createChorusPlant() {
/* 2075 */     MultiVariant side = plainVariant(ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_side"));
/* 2076 */     Variant noside = plainModel(ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_noside"));
/* 2077 */     Variant noside1 = plainModel(ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_noside1"));
/* 2078 */     Variant noside2 = plainModel(ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_noside2"));
/* 2079 */     Variant noside3 = plainModel(ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_noside3"));
/*      */     
/* 2081 */     Variant nosideUvLock = noside.with(UV_LOCK);
/* 2082 */     Variant noside1uvLock = noside1.with(UV_LOCK);
/* 2083 */     Variant noside2uvLock = noside2.with(UV_LOCK);
/* 2084 */     Variant noside3uvLock = noside3.with(UV_LOCK);
/*      */     
/* 2086 */     this.blockStateOutput.accept(
/* 2087 */         MultiPartGenerator.multiPart(Blocks.CHORUS_PLANT)
/* 2088 */         .with(condition().term((Property)BlockStateProperties.NORTH, true), side)
/* 2089 */         .with(condition().term((Property)BlockStateProperties.EAST, true), side.with(Y_ROT_90).with(UV_LOCK))
/* 2090 */         .with(condition().term((Property)BlockStateProperties.SOUTH, true), side.with(Y_ROT_180).with(UV_LOCK))
/* 2091 */         .with(condition().term((Property)BlockStateProperties.WEST, true), side.with(Y_ROT_270).with(UV_LOCK))
/* 2092 */         .with(condition().term((Property)BlockStateProperties.UP, true), side.with(X_ROT_270).with(UV_LOCK))
/* 2093 */         .with(condition().term((Property)BlockStateProperties.DOWN, true), side.with(X_ROT_90).with(UV_LOCK))
/*      */         
/* 2095 */         .with(condition().term((Property)BlockStateProperties.NORTH, false), new MultiVariant(
/* 2096 */             WeightedList.of(new Weighted[] {
/*      */ 
/*      */                 
/*      */                 new Weighted(noside, 2), new Weighted(noside1, 1), new Weighted(noside2, 1), new Weighted(noside3, 1)
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2104 */               }))).with(condition().term((Property)BlockStateProperties.EAST, false), new MultiVariant(
/* 2105 */             WeightedList.of(new Weighted[] {
/* 2106 */                 new Weighted(noside1uvLock.with(Y_ROT_90), 1), new Weighted(
/* 2107 */                   noside2uvLock.with(Y_ROT_90), 1), new Weighted(
/* 2108 */                   noside3uvLock.with(Y_ROT_90), 1), new Weighted(
/* 2109 */                   nosideUvLock.with(Y_ROT_90), 2)
/*      */ 
/*      */ 
/*      */               
/* 2113 */               }))).with(condition().term((Property)BlockStateProperties.SOUTH, false), new MultiVariant(
/* 2114 */             WeightedList.of(new Weighted[] {
/* 2115 */                 new Weighted(noside2uvLock.with(Y_ROT_180), 1), new Weighted(
/* 2116 */                   noside3uvLock.with(Y_ROT_180), 1), new Weighted(
/* 2117 */                   nosideUvLock.with(Y_ROT_180), 2), new Weighted(
/* 2118 */                   noside1uvLock.with(Y_ROT_180), 1)
/*      */ 
/*      */ 
/*      */               
/* 2122 */               }))).with(condition().term((Property)BlockStateProperties.WEST, false), new MultiVariant(
/* 2123 */             WeightedList.of(new Weighted[] {
/* 2124 */                 new Weighted(noside3uvLock.with(Y_ROT_270), 1), new Weighted(
/* 2125 */                   nosideUvLock.with(Y_ROT_270), 2), new Weighted(
/* 2126 */                   noside1uvLock.with(Y_ROT_270), 1), new Weighted(
/* 2127 */                   noside2uvLock.with(Y_ROT_270), 1)
/*      */ 
/*      */ 
/*      */               
/* 2131 */               }))).with(condition().term((Property)BlockStateProperties.UP, false), new MultiVariant(
/* 2132 */             WeightedList.of(new Weighted[] {
/* 2133 */                 new Weighted(nosideUvLock.with(X_ROT_270), 2), new Weighted(
/* 2134 */                   noside3uvLock.with(X_ROT_270), 1), new Weighted(
/* 2135 */                   noside1uvLock.with(X_ROT_270), 1), new Weighted(
/* 2136 */                   noside2uvLock.with(X_ROT_270), 1)
/*      */ 
/*      */ 
/*      */               
/* 2140 */               }))).with(condition().term((Property)BlockStateProperties.DOWN, false), new MultiVariant(
/* 2141 */             WeightedList.of(new Weighted[] {
/* 2142 */                 new Weighted(noside3uvLock.with(X_ROT_90), 1), new Weighted(
/* 2143 */                   noside2uvLock.with(X_ROT_90), 1), new Weighted(
/* 2144 */                   noside1uvLock.with(X_ROT_90), 1), new Weighted(
/* 2145 */                   nosideUvLock.with(X_ROT_90), 2)
/*      */               }))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createComposter() {
/* 2152 */     this.blockStateOutput.accept(
/* 2153 */         MultiPartGenerator.multiPart(Blocks.COMPOSTER)
/* 2154 */         .with(plainVariant(TextureMapping.getBlockTexture(Blocks.COMPOSTER)))
/* 2155 */         .with(condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, 1), plainVariant(TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents1")))
/* 2156 */         .with(condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, 2), plainVariant(TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents2")))
/* 2157 */         .with(condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, 3), plainVariant(TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents3")))
/* 2158 */         .with(condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, 4), plainVariant(TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents4")))
/* 2159 */         .with(condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, 5), plainVariant(TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents5")))
/* 2160 */         .with(condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, 6), plainVariant(TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents6")))
/* 2161 */         .with(condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, 7), plainVariant(TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents7")))
/* 2162 */         .with(condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, 8), plainVariant(TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents_ready"))));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createCopperBulb(Block copperBulb) {
/* 2167 */     MultiVariant baseModel = plainVariant(ModelTemplates.CUBE_ALL.create(copperBulb, TextureMapping.cube(copperBulb), this.modelOutput));
/* 2168 */     MultiVariant baseModelPowered = plainVariant(createSuffixedVariant(copperBulb, "_powered", ModelTemplates.CUBE_ALL, TextureMapping::cube));
/* 2169 */     MultiVariant litModel = plainVariant(createSuffixedVariant(copperBulb, "_lit", ModelTemplates.CUBE_ALL, TextureMapping::cube));
/* 2170 */     MultiVariant litModelPowered = plainVariant(createSuffixedVariant(copperBulb, "_lit_powered", ModelTemplates.CUBE_ALL, TextureMapping::cube));
/*      */     
/* 2172 */     this.blockStateOutput.accept(createCopperBulb(copperBulb, baseModel, litModel, baseModelPowered, litModelPowered));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createCopperBulb(Block copperBulb, MultiVariant baseModel, MultiVariant litModel, MultiVariant baseModelPowered, MultiVariant litModelPowered) {
/* 2176 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(copperBulb)
/* 2177 */       .with(
/* 2178 */         PropertyDispatch.initial((Property)BlockStateProperties.LIT, (Property)BlockStateProperties.POWERED)
/* 2179 */         .generate((emittingLight, powered) -> emittingLight ? (powered ? litModelPowered : litModel) : (powered ? baseModelPowered : baseModel)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void copyCopperBulbModel(Block donor, Block acceptor) {
/* 2189 */     MultiVariant baseModel = plainVariant(ModelLocationUtils.getModelLocation(donor));
/* 2190 */     MultiVariant baseModelPowered = plainVariant(ModelLocationUtils.getModelLocation(donor, "_powered"));
/* 2191 */     MultiVariant litModel = plainVariant(ModelLocationUtils.getModelLocation(donor, "_lit"));
/* 2192 */     MultiVariant litModelPowered = plainVariant(ModelLocationUtils.getModelLocation(donor, "_lit_powered"));
/*      */     
/* 2194 */     this.itemModelOutput.copy(donor.asItem(), acceptor.asItem());
/* 2195 */     this.blockStateOutput.accept(createCopperBulb(acceptor, baseModel, litModel, baseModelPowered, litModelPowered));
/*      */   }
/*      */   
/*      */   private void createAmethystCluster(Block clusterBlock) {
/* 2199 */     MultiVariant model = plainVariant(ModelTemplates.CROSS.create(clusterBlock, TextureMapping.cross(clusterBlock), this.modelOutput));
/* 2200 */     this.blockStateOutput.accept(
/* 2201 */         MultiVariantGenerator.dispatch(clusterBlock, model)
/* 2202 */         .with(ROTATIONS_COLUMN_WITH_FACING));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createAmethystClusters() {
/* 2207 */     createAmethystCluster(Blocks.SMALL_AMETHYST_BUD);
/* 2208 */     createAmethystCluster(Blocks.MEDIUM_AMETHYST_BUD);
/* 2209 */     createAmethystCluster(Blocks.LARGE_AMETHYST_BUD);
/* 2210 */     createAmethystCluster(Blocks.AMETHYST_CLUSTER);
/*      */   }
/*      */   
/*      */   private void createPointedDripstone() {
/* 2214 */     PropertyDispatch.C2<MultiVariant, Direction, DripstoneThickness> generator = PropertyDispatch.initial((Property)BlockStateProperties.VERTICAL_DIRECTION, (Property)BlockStateProperties.DRIPSTONE_THICKNESS);
/* 2215 */     for (DripstoneThickness dripstoneThickness : DripstoneThickness.values()) {
/* 2216 */       generator.select((Comparable)Direction.UP, (Comparable)dripstoneThickness, createPointedDripstoneVariant(Direction.UP, dripstoneThickness));
/*      */     }
/* 2218 */     for (DripstoneThickness dripstoneThickness : DripstoneThickness.values()) {
/* 2219 */       generator.select((Comparable)Direction.DOWN, (Comparable)dripstoneThickness, createPointedDripstoneVariant(Direction.DOWN, dripstoneThickness));
/*      */     }
/* 2221 */     this.blockStateOutput.accept(
/* 2222 */         MultiVariantGenerator.dispatch(Blocks.POINTED_DRIPSTONE)
/* 2223 */         .with((PropertyDispatch)generator));
/*      */   }
/*      */ 
/*      */   
/*      */   private MultiVariant createPointedDripstoneVariant(Direction direction, DripstoneThickness dripstoneThickness) {
/* 2228 */     String suffix = "_" + direction.getSerializedName() + "_" + dripstoneThickness.getSerializedName();
/* 2229 */     TextureMapping texture = TextureMapping.cross(TextureMapping.getBlockTexture(Blocks.POINTED_DRIPSTONE, suffix));
/* 2230 */     return plainVariant(ModelTemplates.POINTED_DRIPSTONE.createWithSuffix(Blocks.POINTED_DRIPSTONE, suffix, texture, this.modelOutput));
/*      */   }
/*      */   
/*      */   private void createNyliumBlock(Block block) {
/* 2234 */     TextureMapping mapping = new TextureMapping()
/* 2235 */       .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.NETHERRACK))
/* 2236 */       .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block))
/* 2237 */       .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"));
/*      */     
/* 2239 */     this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_BOTTOM_TOP.create(block, mapping, this.modelOutput))));
/*      */   }
/*      */   
/*      */   private void createDaylightDetector() {
/* 2243 */     Identifier sideTexture = TextureMapping.getBlockTexture(Blocks.DAYLIGHT_DETECTOR, "_side");
/* 2244 */     TextureMapping normalTextures = new TextureMapping().put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.DAYLIGHT_DETECTOR, "_top")).put(TextureSlot.SIDE, sideTexture);
/* 2245 */     TextureMapping invertedTextures = new TextureMapping().put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.DAYLIGHT_DETECTOR, "_inverted_top")).put(TextureSlot.SIDE, sideTexture);
/*      */     
/* 2247 */     this.blockStateOutput.accept(
/* 2248 */         MultiVariantGenerator.dispatch(Blocks.DAYLIGHT_DETECTOR)
/* 2249 */         .with(
/* 2250 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.INVERTED)
/* 2251 */           .select(false, plainVariant(ModelTemplates.DAYLIGHT_DETECTOR.create(Blocks.DAYLIGHT_DETECTOR, normalTextures, this.modelOutput)))
/* 2252 */           .select(true, plainVariant(ModelTemplates.DAYLIGHT_DETECTOR.create(ModelLocationUtils.getModelLocation(Blocks.DAYLIGHT_DETECTOR, "_inverted"), invertedTextures, this.modelOutput)))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createRotatableColumn(Block block) {
/* 2258 */     this.blockStateOutput.accept(
/* 2259 */         MultiVariantGenerator.dispatch(block, plainVariant(ModelLocationUtils.getModelLocation(block)))
/* 2260 */         .with(ROTATIONS_COLUMN_WITH_FACING));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createLightningRod(Block block, Block waxedBlock) {
/* 2265 */     MultiVariant on = plainVariant(ModelLocationUtils.getModelLocation(Blocks.LIGHTNING_ROD, "_on"));
/* 2266 */     MultiVariant off = plainVariant(ModelTemplates.LIGHTNING_ROD.create(block, TextureMapping.defaultTexture(block), this.modelOutput));
/*      */     
/* 2268 */     this.blockStateOutput.accept(
/* 2269 */         MultiVariantGenerator.dispatch(block)
/* 2270 */         .with(createBooleanModelDispatch(BlockStateProperties.POWERED, on, off))
/* 2271 */         .with(ROTATIONS_COLUMN_WITH_FACING));
/*      */     
/* 2273 */     this.blockStateOutput.accept(
/* 2274 */         MultiVariantGenerator.dispatch(waxedBlock)
/* 2275 */         .with(createBooleanModelDispatch(BlockStateProperties.POWERED, on, off))
/* 2276 */         .with(ROTATIONS_COLUMN_WITH_FACING));
/*      */     
/* 2278 */     this.itemModelOutput.copy(block.asItem(), waxedBlock.asItem());
/*      */   }
/*      */   
/*      */   private void createFarmland() {
/* 2282 */     TextureMapping dryTextures = new TextureMapping().put(TextureSlot.DIRT, TextureMapping.getBlockTexture(Blocks.DIRT)).put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.FARMLAND));
/* 2283 */     TextureMapping moistTextures = new TextureMapping().put(TextureSlot.DIRT, TextureMapping.getBlockTexture(Blocks.DIRT)).put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.FARMLAND, "_moist"));
/*      */     
/* 2285 */     MultiVariant dryModel = plainVariant(ModelTemplates.FARMLAND.create(Blocks.FARMLAND, dryTextures, this.modelOutput));
/* 2286 */     MultiVariant moistModel = plainVariant(ModelTemplates.FARMLAND.create(TextureMapping.getBlockTexture(Blocks.FARMLAND, "_moist"), moistTextures, this.modelOutput));
/*      */     
/* 2288 */     this.blockStateOutput.accept(
/* 2289 */         MultiVariantGenerator.dispatch(Blocks.FARMLAND)
/* 2290 */         .with(createEmptyOrFullDispatch((Property<Integer>)BlockStateProperties.MOISTURE, 7, moistModel, dryModel)));
/*      */   }
/*      */ 
/*      */   
/*      */   private MultiVariant createFloorFireModels(Block block) {
/* 2295 */     return variants(new Variant[] {
/* 2296 */           plainModel(ModelTemplates.FIRE_FLOOR.create(ModelLocationUtils.getModelLocation(block, "_floor0"), TextureMapping.fire0(block), this.modelOutput)), 
/* 2297 */           plainModel(ModelTemplates.FIRE_FLOOR.create(ModelLocationUtils.getModelLocation(block, "_floor1"), TextureMapping.fire1(block), this.modelOutput))
/*      */         });
/*      */   }
/*      */   
/*      */   private MultiVariant createSideFireModels(Block block) {
/* 2302 */     return variants(new Variant[] {
/* 2303 */           plainModel(ModelTemplates.FIRE_SIDE.create(ModelLocationUtils.getModelLocation(block, "_side0"), TextureMapping.fire0(block), this.modelOutput)), 
/* 2304 */           plainModel(ModelTemplates.FIRE_SIDE.create(ModelLocationUtils.getModelLocation(block, "_side1"), TextureMapping.fire1(block), this.modelOutput)), 
/*      */           
/* 2306 */           plainModel(ModelTemplates.FIRE_SIDE_ALT.create(ModelLocationUtils.getModelLocation(block, "_side_alt0"), TextureMapping.fire0(block), this.modelOutput)), 
/* 2307 */           plainModel(ModelTemplates.FIRE_SIDE_ALT.create(ModelLocationUtils.getModelLocation(block, "_side_alt1"), TextureMapping.fire1(block), this.modelOutput))
/*      */         });
/*      */   }
/*      */   
/*      */   private MultiVariant createTopFireModels(Block block) {
/* 2312 */     return variants(new Variant[] {
/* 2313 */           plainModel(ModelTemplates.FIRE_UP.create(ModelLocationUtils.getModelLocation(block, "_up0"), TextureMapping.fire0(block), this.modelOutput)), 
/* 2314 */           plainModel(ModelTemplates.FIRE_UP.create(ModelLocationUtils.getModelLocation(block, "_up1"), TextureMapping.fire1(block), this.modelOutput)), 
/* 2315 */           plainModel(ModelTemplates.FIRE_UP_ALT.create(ModelLocationUtils.getModelLocation(block, "_up_alt0"), TextureMapping.fire0(block), this.modelOutput)), 
/* 2316 */           plainModel(ModelTemplates.FIRE_UP_ALT.create(ModelLocationUtils.getModelLocation(block, "_up_alt1"), TextureMapping.fire1(block), this.modelOutput))
/*      */         });
/*      */   }
/*      */   
/*      */   private void createFire() {
/* 2321 */     ConditionBuilder noSides = condition()
/* 2322 */       .term((Property)BlockStateProperties.NORTH, false)
/* 2323 */       .term((Property)BlockStateProperties.EAST, false)
/* 2324 */       .term((Property)BlockStateProperties.SOUTH, false)
/* 2325 */       .term((Property)BlockStateProperties.WEST, false)
/* 2326 */       .term((Property)BlockStateProperties.UP, false);
/* 2327 */     MultiVariant floorFireModels = createFloorFireModels(Blocks.FIRE);
/* 2328 */     MultiVariant sideFireModels = createSideFireModels(Blocks.FIRE);
/* 2329 */     MultiVariant topFireModels = createTopFireModels(Blocks.FIRE);
/*      */     
/* 2331 */     this.blockStateOutput.accept(
/* 2332 */         MultiPartGenerator.multiPart(Blocks.FIRE)
/* 2333 */         .with(noSides, floorFireModels)
/*      */ 
/*      */ 
/*      */         
/* 2337 */         .with(
/* 2338 */           or(new ConditionBuilder[] { condition().term((Property)BlockStateProperties.NORTH, true), noSides }), sideFireModels)
/*      */ 
/*      */         
/* 2341 */         .with(
/* 2342 */           or(new ConditionBuilder[] { condition().term((Property)BlockStateProperties.EAST, true), noSides
/* 2343 */             }), sideFireModels.with(Y_ROT_90))
/*      */         
/* 2345 */         .with(
/* 2346 */           or(new ConditionBuilder[] { condition().term((Property)BlockStateProperties.SOUTH, true), noSides
/* 2347 */             }), sideFireModels.with(Y_ROT_180))
/*      */         
/* 2349 */         .with(
/* 2350 */           or(new ConditionBuilder[] { condition().term((Property)BlockStateProperties.WEST, true), noSides
/* 2351 */             }), sideFireModels.with(Y_ROT_270))
/*      */         
/* 2353 */         .with(
/* 2354 */           condition().term((Property)BlockStateProperties.UP, true), topFireModels));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createSoulFire() {
/* 2361 */     MultiVariant floorFireModels = createFloorFireModels(Blocks.SOUL_FIRE);
/* 2362 */     MultiVariant sideFireModels = createSideFireModels(Blocks.SOUL_FIRE);
/*      */     
/* 2364 */     this.blockStateOutput.accept(
/* 2365 */         MultiPartGenerator.multiPart(Blocks.SOUL_FIRE)
/* 2366 */         .with(floorFireModels)
/* 2367 */         .with(sideFireModels)
/* 2368 */         .with(sideFireModels.with(Y_ROT_90))
/* 2369 */         .with(sideFireModels.with(Y_ROT_180))
/* 2370 */         .with(sideFireModels.with(Y_ROT_270)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createLantern(Block block) {
/* 2375 */     MultiVariant ground = plainVariant(TexturedModel.LANTERN.create(block, this.modelOutput));
/* 2376 */     MultiVariant hanging = plainVariant(TexturedModel.HANGING_LANTERN.create(block, this.modelOutput));
/*      */     
/* 2378 */     registerSimpleFlatItemModel(block.asItem());
/* 2379 */     this.blockStateOutput.accept(
/* 2380 */         MultiVariantGenerator.dispatch(block)
/* 2381 */         .with(createBooleanModelDispatch(BlockStateProperties.HANGING, hanging, ground)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createCopperLantern(Block unwaxed, Block waxed) {
/* 2386 */     Identifier ground = TexturedModel.LANTERN.create(unwaxed, this.modelOutput);
/* 2387 */     Identifier hanging = TexturedModel.HANGING_LANTERN.create(unwaxed, this.modelOutput);
/*      */     
/* 2389 */     registerSimpleFlatItemModel(unwaxed.asItem());
/* 2390 */     this.itemModelOutput.copy(unwaxed.asItem(), waxed.asItem());
/* 2391 */     this.blockStateOutput.accept(
/* 2392 */         MultiVariantGenerator.dispatch(unwaxed)
/* 2393 */         .with(createBooleanModelDispatch(BlockStateProperties.HANGING, plainVariant(hanging), plainVariant(ground))));
/*      */     
/* 2395 */     this.blockStateOutput.accept(
/* 2396 */         MultiVariantGenerator.dispatch(waxed)
/* 2397 */         .with(createBooleanModelDispatch(BlockStateProperties.HANGING, plainVariant(hanging), plainVariant(ground))));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createCopperChain(Block unwaxed, Block waxed) {
/* 2402 */     MultiVariant model = plainVariant(TexturedModel.CHAIN.create(unwaxed, this.modelOutput));
/* 2403 */     createAxisAlignedPillarBlockCustomModel(unwaxed, model);
/* 2404 */     createAxisAlignedPillarBlockCustomModel(waxed, model);
/*      */   }
/*      */   
/*      */   private void createMuddyMangroveRoots() {
/* 2408 */     TextureMapping textures = TextureMapping.column(TextureMapping.getBlockTexture(Blocks.MUDDY_MANGROVE_ROOTS, "_side"), TextureMapping.getBlockTexture(Blocks.MUDDY_MANGROVE_ROOTS, "_top"));
/* 2409 */     MultiVariant model = plainVariant(ModelTemplates.CUBE_COLUMN.create(Blocks.MUDDY_MANGROVE_ROOTS, textures, this.modelOutput));
/* 2410 */     this.blockStateOutput.accept(createAxisAlignedPillarBlock(Blocks.MUDDY_MANGROVE_ROOTS, model));
/*      */   }
/*      */   
/*      */   private void createMangrovePropagule() {
/* 2414 */     registerSimpleFlatItemModel(Items.MANGROVE_PROPAGULE);
/*      */     
/* 2416 */     Block block = Blocks.MANGROVE_PROPAGULE;
/*      */     
/* 2418 */     MultiVariant plantedModel = plainVariant(ModelLocationUtils.getModelLocation(block));
/* 2419 */     this.blockStateOutput.accept(
/* 2420 */         MultiVariantGenerator.dispatch(Blocks.MANGROVE_PROPAGULE)
/* 2421 */         .with(
/* 2422 */           PropertyDispatch.initial((Property)MangrovePropaguleBlock.HANGING, (Property)MangrovePropaguleBlock.AGE)
/* 2423 */           .generate((hanging, age) -> hanging ? plainVariant(ModelLocationUtils.getModelLocation(block, "_hanging_" + age)) : plantedModel)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createFrostedIce() {
/* 2433 */     this.blockStateOutput.accept(
/* 2434 */         MultiVariantGenerator.dispatch(Blocks.FROSTED_ICE)
/* 2435 */         .with(
/* 2436 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.AGE_3)
/* 2437 */           .select(0, plainVariant(createSuffixedVariant(Blocks.FROSTED_ICE, "_0", ModelTemplates.CUBE_ALL, TextureMapping::cube)))
/* 2438 */           .select(1, plainVariant(createSuffixedVariant(Blocks.FROSTED_ICE, "_1", ModelTemplates.CUBE_ALL, TextureMapping::cube)))
/* 2439 */           .select(2, plainVariant(createSuffixedVariant(Blocks.FROSTED_ICE, "_2", ModelTemplates.CUBE_ALL, TextureMapping::cube)))
/* 2440 */           .select(3, plainVariant(createSuffixedVariant(Blocks.FROSTED_ICE, "_3", ModelTemplates.CUBE_ALL, TextureMapping::cube)))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createGrassBlocks() {
/* 2446 */     Identifier bottomTexture = TextureMapping.getBlockTexture(Blocks.DIRT);
/* 2447 */     TextureMapping snowyMapping = new TextureMapping()
/* 2448 */       .put(TextureSlot.BOTTOM, bottomTexture)
/* 2449 */       .copyForced(TextureSlot.BOTTOM, TextureSlot.PARTICLE)
/* 2450 */       .put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.GRASS_BLOCK, "_top"))
/* 2451 */       .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.GRASS_BLOCK, "_snow"));
/*      */     
/* 2453 */     MultiVariant snowyGrass = plainVariant(ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(Blocks.GRASS_BLOCK, "_snow", snowyMapping, this.modelOutput));
/*      */     
/* 2455 */     Identifier plainGrassModel = ModelLocationUtils.getModelLocation(Blocks.GRASS_BLOCK);
/* 2456 */     createGrassLikeBlock(Blocks.GRASS_BLOCK, createRotatedVariants(plainModel(plainGrassModel)), snowyGrass);
/* 2457 */     registerSimpleTintedItemModel(Blocks.GRASS_BLOCK, plainGrassModel, (ItemTintSource)new GrassColorSource());
/*      */     
/* 2459 */     MultiVariant myceliumModel = createRotatedVariants(plainModel(TexturedModel.CUBE_TOP_BOTTOM.get(Blocks.MYCELIUM).updateTextures(m -> m.put(TextureSlot.BOTTOM, bottomTexture)).create(Blocks.MYCELIUM, this.modelOutput)));
/* 2460 */     createGrassLikeBlock(Blocks.MYCELIUM, myceliumModel, snowyGrass);
/*      */     
/* 2462 */     MultiVariant podzolModel = createRotatedVariants(plainModel(TexturedModel.CUBE_TOP_BOTTOM.get(Blocks.PODZOL).updateTextures(m -> m.put(TextureSlot.BOTTOM, bottomTexture)).create(Blocks.PODZOL, this.modelOutput)));
/* 2463 */     createGrassLikeBlock(Blocks.PODZOL, podzolModel, snowyGrass);
/*      */   }
/*      */   
/*      */   private void createGrassLikeBlock(Block block, MultiVariant normal, MultiVariant snowy) {
/* 2467 */     this.blockStateOutput.accept(
/* 2468 */         MultiVariantGenerator.dispatch(block)
/* 2469 */         .with(
/* 2470 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.SNOWY)
/* 2471 */           .select(true, snowy)
/* 2472 */           .select(false, normal)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createCocoa() {
/* 2478 */     registerSimpleFlatItemModel(Items.COCOA_BEANS);
/* 2479 */     this.blockStateOutput.accept(
/* 2480 */         MultiVariantGenerator.dispatch(Blocks.COCOA)
/* 2481 */         .with(
/* 2482 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.AGE_2)
/* 2483 */           .select(0, plainVariant(ModelLocationUtils.getModelLocation(Blocks.COCOA, "_stage0")))
/* 2484 */           .select(1, plainVariant(ModelLocationUtils.getModelLocation(Blocks.COCOA, "_stage1")))
/* 2485 */           .select(2, plainVariant(ModelLocationUtils.getModelLocation(Blocks.COCOA, "_stage2"))))
/*      */         
/* 2487 */         .with(ROTATION_HORIZONTAL_FACING_ALT));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createDirtPath() {
/* 2492 */     Variant model = plainModel(ModelLocationUtils.getModelLocation(Blocks.DIRT_PATH));
/* 2493 */     this.blockStateOutput.accept(
/* 2494 */         MultiVariantGenerator.dispatch(Blocks.DIRT_PATH, createRotatedVariants(model)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createWeightedPressurePlate(Block block, Block appearance) {
/* 2499 */     TextureMapping textures = TextureMapping.defaultTexture(appearance);
/* 2500 */     MultiVariant up = plainVariant(ModelTemplates.PRESSURE_PLATE_UP.create(block, textures, this.modelOutput));
/* 2501 */     MultiVariant down = plainVariant(ModelTemplates.PRESSURE_PLATE_DOWN.create(block, textures, this.modelOutput));
/*      */     
/* 2503 */     this.blockStateOutput.accept(
/* 2504 */         MultiVariantGenerator.dispatch(block)
/* 2505 */         .with(createEmptyOrFullDispatch((Property<Integer>)BlockStateProperties.POWER, 1, down, up)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createHopper() {
/* 2510 */     MultiVariant downBlock = plainVariant(ModelLocationUtils.getModelLocation(Blocks.HOPPER));
/* 2511 */     MultiVariant sideBlock = plainVariant(ModelLocationUtils.getModelLocation(Blocks.HOPPER, "_side"));
/*      */     
/* 2513 */     registerSimpleFlatItemModel(Items.HOPPER);
/* 2514 */     this.blockStateOutput.accept(
/* 2515 */         MultiVariantGenerator.dispatch(Blocks.HOPPER)
/* 2516 */         .with(
/* 2517 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.FACING_HOPPER)
/* 2518 */           .select((Comparable)Direction.DOWN, downBlock)
/* 2519 */           .select((Comparable)Direction.NORTH, sideBlock)
/* 2520 */           .select((Comparable)Direction.EAST, sideBlock.with(Y_ROT_90))
/* 2521 */           .select((Comparable)Direction.SOUTH, sideBlock.with(Y_ROT_180))
/* 2522 */           .select((Comparable)Direction.WEST, sideBlock.with(Y_ROT_270))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void copyModel(Block donor, Block acceptor) {
/* 2529 */     MultiVariant model = plainVariant(ModelLocationUtils.getModelLocation(donor));
/* 2530 */     this.blockStateOutput.accept(MultiVariantGenerator.dispatch(acceptor, model));
/* 2531 */     this.itemModelOutput.copy(donor.asItem(), acceptor.asItem());
/*      */   }
/*      */   
/*      */   private void createBarsAndItem(Block block) {
/* 2535 */     TextureMapping textures = TextureMapping.bars(block);
/* 2536 */     createBars(block, 
/*      */         
/* 2538 */         ModelTemplates.BARS_POST_ENDS.create(block, textures, this.modelOutput), 
/* 2539 */         ModelTemplates.BARS_POST.create(block, textures, this.modelOutput), 
/* 2540 */         ModelTemplates.BARS_CAP.create(block, textures, this.modelOutput), 
/* 2541 */         ModelTemplates.BARS_CAP_ALT.create(block, textures, this.modelOutput), 
/* 2542 */         ModelTemplates.BARS_POST_SIDE.create(block, textures, this.modelOutput), 
/* 2543 */         ModelTemplates.BARS_POST_SIDE_ALT.create(block, textures, this.modelOutput));
/*      */     
/* 2545 */     registerSimpleFlatItemModel(block);
/*      */   }
/*      */   
/*      */   private void createBarsAndItem(Block unwaxed, Block waxed) {
/* 2549 */     TextureMapping textures = TextureMapping.bars(unwaxed);
/* 2550 */     Identifier postEndResource = ModelTemplates.BARS_POST_ENDS.create(unwaxed, textures, this.modelOutput);
/* 2551 */     Identifier postResource = ModelTemplates.BARS_POST.create(unwaxed, textures, this.modelOutput);
/* 2552 */     Identifier capResource = ModelTemplates.BARS_CAP.create(unwaxed, textures, this.modelOutput);
/* 2553 */     Identifier capAltResource = ModelTemplates.BARS_CAP_ALT.create(unwaxed, textures, this.modelOutput);
/* 2554 */     Identifier sideResource = ModelTemplates.BARS_POST_SIDE.create(unwaxed, textures, this.modelOutput);
/* 2555 */     Identifier sideAltResource = ModelTemplates.BARS_POST_SIDE_ALT.create(unwaxed, textures, this.modelOutput);
/*      */     
/* 2557 */     createBars(unwaxed, postEndResource, postResource, capResource, capAltResource, sideResource, sideAltResource);
/* 2558 */     createBars(waxed, postEndResource, postResource, capResource, capAltResource, sideResource, sideAltResource);
/*      */     
/* 2560 */     registerSimpleFlatItemModel(unwaxed);
/* 2561 */     this.itemModelOutput.copy(unwaxed.asItem(), waxed.asItem());
/*      */   }
/*      */   
/*      */   private void createBars(Block block, Identifier postEndResource, Identifier postResource, Identifier capResource, Identifier capAltResource, Identifier sideResource, Identifier sideAltResource) {
/* 2565 */     MultiVariant postEnds = plainVariant(postEndResource);
/* 2566 */     MultiVariant post = plainVariant(postResource);
/* 2567 */     MultiVariant cap = plainVariant(capResource);
/* 2568 */     MultiVariant capAlt = plainVariant(capAltResource);
/* 2569 */     MultiVariant side = plainVariant(sideResource);
/* 2570 */     MultiVariant sideAlt = plainVariant(sideAltResource);
/*      */     
/* 2572 */     this.blockStateOutput.accept(
/* 2573 */         MultiPartGenerator.multiPart(block)
/* 2574 */         .with(postEnds)
/* 2575 */         .with(
/* 2576 */           condition()
/* 2577 */           .term((Property)BlockStateProperties.NORTH, false)
/* 2578 */           .term((Property)BlockStateProperties.EAST, false)
/* 2579 */           .term((Property)BlockStateProperties.SOUTH, false)
/* 2580 */           .term((Property)BlockStateProperties.WEST, false), post)
/*      */ 
/*      */         
/* 2583 */         .with(
/* 2584 */           condition()
/* 2585 */           .term((Property)BlockStateProperties.NORTH, true)
/* 2586 */           .term((Property)BlockStateProperties.EAST, false)
/* 2587 */           .term((Property)BlockStateProperties.SOUTH, false)
/* 2588 */           .term((Property)BlockStateProperties.WEST, false), cap)
/*      */ 
/*      */         
/* 2591 */         .with(
/* 2592 */           condition()
/* 2593 */           .term((Property)BlockStateProperties.NORTH, false)
/* 2594 */           .term((Property)BlockStateProperties.EAST, true)
/* 2595 */           .term((Property)BlockStateProperties.SOUTH, false)
/* 2596 */           .term((Property)BlockStateProperties.WEST, false), 
/* 2597 */           cap.with(Y_ROT_90))
/*      */         
/* 2599 */         .with(
/* 2600 */           condition()
/* 2601 */           .term((Property)BlockStateProperties.NORTH, false)
/* 2602 */           .term((Property)BlockStateProperties.EAST, false)
/* 2603 */           .term((Property)BlockStateProperties.SOUTH, true)
/* 2604 */           .term((Property)BlockStateProperties.WEST, false), capAlt)
/*      */ 
/*      */         
/* 2607 */         .with(
/* 2608 */           condition()
/* 2609 */           .term((Property)BlockStateProperties.NORTH, false)
/* 2610 */           .term((Property)BlockStateProperties.EAST, false)
/* 2611 */           .term((Property)BlockStateProperties.SOUTH, false)
/* 2612 */           .term((Property)BlockStateProperties.WEST, true), 
/* 2613 */           capAlt.with(Y_ROT_90))
/*      */         
/* 2615 */         .with(condition().term((Property)BlockStateProperties.NORTH, true), side)
/* 2616 */         .with(condition().term((Property)BlockStateProperties.EAST, true), side.with(Y_ROT_90))
/* 2617 */         .with(condition().term((Property)BlockStateProperties.SOUTH, true), sideAlt)
/* 2618 */         .with(condition().term((Property)BlockStateProperties.WEST, true), sideAlt.with(Y_ROT_90)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createNonTemplateHorizontalBlock(Block block) {
/* 2623 */     this.blockStateOutput.accept(
/* 2624 */         MultiVariantGenerator.dispatch(block, plainVariant(ModelLocationUtils.getModelLocation(block)))
/* 2625 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createLever() {
/* 2630 */     MultiVariant off = plainVariant(ModelLocationUtils.getModelLocation(Blocks.LEVER));
/* 2631 */     MultiVariant on = plainVariant(ModelLocationUtils.getModelLocation(Blocks.LEVER, "_on"));
/*      */     
/* 2633 */     registerSimpleFlatItemModel(Blocks.LEVER);
/* 2634 */     this.blockStateOutput.accept(
/* 2635 */         MultiVariantGenerator.dispatch(Blocks.LEVER)
/* 2636 */         .with(
/* 2637 */           createBooleanModelDispatch(BlockStateProperties.POWERED, off, on))
/*      */         
/* 2639 */         .with(
/* 2640 */           (PropertyDispatch)PropertyDispatch.modify((Property)BlockStateProperties.ATTACH_FACE, (Property)BlockStateProperties.HORIZONTAL_FACING)
/* 2641 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.NORTH, X_ROT_180.then(Y_ROT_180))
/* 2642 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.EAST, X_ROT_180.then(Y_ROT_270))
/* 2643 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.SOUTH, X_ROT_180)
/* 2644 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.WEST, X_ROT_180.then(Y_ROT_90))
/*      */           
/* 2646 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.NORTH, NOP)
/* 2647 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.EAST, Y_ROT_90)
/* 2648 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.SOUTH, Y_ROT_180)
/* 2649 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.WEST, Y_ROT_270)
/*      */           
/* 2651 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.NORTH, X_ROT_90)
/* 2652 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.EAST, X_ROT_90.then(Y_ROT_90))
/* 2653 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.SOUTH, X_ROT_90.then(Y_ROT_180))
/* 2654 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.WEST, X_ROT_90.then(Y_ROT_270))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createLilyPad() {
/* 2660 */     Identifier itemModel = createFlatItemModelWithBlockTexture(Items.LILY_PAD, Blocks.LILY_PAD);
/* 2661 */     registerSimpleTintedItemModel(Blocks.LILY_PAD, itemModel, ItemModelUtils.constantTint(-9321636));
/* 2662 */     Variant blockModel = plainModel(ModelLocationUtils.getModelLocation(Blocks.LILY_PAD));
/* 2663 */     this.blockStateOutput.accept(
/* 2664 */         MultiVariantGenerator.dispatch(Blocks.LILY_PAD, createRotatedVariants(blockModel)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createFrogspawnBlock() {
/* 2669 */     registerSimpleFlatItemModel(Blocks.FROGSPAWN);
/* 2670 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.FROGSPAWN, plainVariant(ModelLocationUtils.getModelLocation(Blocks.FROGSPAWN))));
/*      */   }
/*      */   
/*      */   private void createNetherPortalBlock() {
/* 2674 */     this.blockStateOutput.accept(
/* 2675 */         MultiVariantGenerator.dispatch(Blocks.NETHER_PORTAL)
/* 2676 */         .with(
/* 2677 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.HORIZONTAL_AXIS)
/* 2678 */           .select((Comparable)Direction.Axis.X, plainVariant(ModelLocationUtils.getModelLocation(Blocks.NETHER_PORTAL, "_ns")))
/* 2679 */           .select((Comparable)Direction.Axis.Z, plainVariant(ModelLocationUtils.getModelLocation(Blocks.NETHER_PORTAL, "_ew")))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createNetherrack() {
/* 2685 */     Variant model = plainModel(TexturedModel.CUBE.create(Blocks.NETHERRACK, this.modelOutput));
/* 2686 */     this.blockStateOutput.accept(
/* 2687 */         MultiVariantGenerator.dispatch(Blocks.NETHERRACK, 
/* 2688 */           variants(new Variant[] {
/*      */               
/* 2690 */               model, model.with(X_ROT_90), 
/* 2691 */               model.with(X_ROT_180), 
/* 2692 */               model.with(X_ROT_270), 
/*      */               
/* 2694 */               model.with(Y_ROT_90), 
/* 2695 */               model.with(Y_ROT_90.then(X_ROT_90)), 
/* 2696 */               model.with(Y_ROT_90.then(X_ROT_180)), 
/* 2697 */               model.with(Y_ROT_90.then(X_ROT_270)), 
/*      */               
/* 2699 */               model.with(Y_ROT_180), 
/* 2700 */               model.with(Y_ROT_180.then(X_ROT_90)), 
/* 2701 */               model.with(Y_ROT_180.then(X_ROT_180)), 
/* 2702 */               model.with(Y_ROT_180.then(X_ROT_270)), 
/*      */               
/* 2704 */               model.with(Y_ROT_270), 
/* 2705 */               model.with(Y_ROT_270.then(X_ROT_90)), 
/* 2706 */               model.with(Y_ROT_270.then(X_ROT_180)), 
/* 2707 */               model.with(Y_ROT_270.then(X_ROT_270))
/*      */             })));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createObserver() {
/* 2714 */     MultiVariant off = plainVariant(ModelLocationUtils.getModelLocation(Blocks.OBSERVER));
/* 2715 */     MultiVariant on = plainVariant(ModelLocationUtils.getModelLocation(Blocks.OBSERVER, "_on"));
/*      */     
/* 2717 */     this.blockStateOutput.accept(
/* 2718 */         MultiVariantGenerator.dispatch(Blocks.OBSERVER)
/* 2719 */         .with(createBooleanModelDispatch(BlockStateProperties.POWERED, on, off))
/* 2720 */         .with(ROTATION_FACING));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createPistons() {
/* 2725 */     TextureMapping commonMapping = new TextureMapping()
/* 2726 */       .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.PISTON, "_bottom"))
/* 2727 */       .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.PISTON, "_side"));
/*      */     
/* 2729 */     Identifier topSticky = TextureMapping.getBlockTexture(Blocks.PISTON, "_top_sticky");
/* 2730 */     Identifier top = TextureMapping.getBlockTexture(Blocks.PISTON, "_top");
/*      */     
/* 2732 */     TextureMapping stickyTextures = commonMapping.copyAndUpdate(TextureSlot.PLATFORM, topSticky);
/* 2733 */     TextureMapping normalTextures = commonMapping.copyAndUpdate(TextureSlot.PLATFORM, top);
/*      */     
/* 2735 */     MultiVariant extendedPiston = plainVariant(ModelLocationUtils.getModelLocation(Blocks.PISTON, "_base"));
/*      */     
/* 2737 */     createPistonVariant(Blocks.PISTON, extendedPiston, normalTextures);
/* 2738 */     createPistonVariant(Blocks.STICKY_PISTON, extendedPiston, stickyTextures);
/*      */     
/* 2740 */     Identifier normalInventory = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(Blocks.PISTON, "_inventory", commonMapping.copyAndUpdate(TextureSlot.TOP, top), this.modelOutput);
/* 2741 */     Identifier stickyInventory = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(Blocks.STICKY_PISTON, "_inventory", commonMapping.copyAndUpdate(TextureSlot.TOP, topSticky), this.modelOutput);
/*      */     
/* 2743 */     registerSimpleItemModel(Blocks.PISTON, normalInventory);
/* 2744 */     registerSimpleItemModel(Blocks.STICKY_PISTON, stickyInventory);
/*      */   }
/*      */   
/*      */   private void createPistonVariant(Block block, MultiVariant extended, TextureMapping textures) {
/* 2748 */     MultiVariant retracted = plainVariant(ModelTemplates.PISTON.create(block, textures, this.modelOutput));
/* 2749 */     this.blockStateOutput.accept(
/* 2750 */         MultiVariantGenerator.dispatch(block)
/* 2751 */         .with(createBooleanModelDispatch(BlockStateProperties.EXTENDED, extended, retracted))
/* 2752 */         .with(ROTATION_FACING));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createPistonHeads() {
/* 2757 */     TextureMapping commonMapping = new TextureMapping()
/* 2758 */       .put(TextureSlot.UNSTICKY, TextureMapping.getBlockTexture(Blocks.PISTON, "_top"))
/* 2759 */       .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.PISTON, "_side"));
/*      */     
/* 2761 */     TextureMapping stickyTextures = commonMapping.copyAndUpdate(TextureSlot.PLATFORM, TextureMapping.getBlockTexture(Blocks.PISTON, "_top_sticky"));
/* 2762 */     TextureMapping normalTextures = commonMapping.copyAndUpdate(TextureSlot.PLATFORM, TextureMapping.getBlockTexture(Blocks.PISTON, "_top"));
/*      */     
/* 2764 */     this.blockStateOutput.accept(
/* 2765 */         MultiVariantGenerator.dispatch(Blocks.PISTON_HEAD)
/* 2766 */         .with(
/* 2767 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.SHORT, (Property)BlockStateProperties.PISTON_TYPE)
/* 2768 */           .select(false, (Comparable)PistonType.DEFAULT, plainVariant(ModelTemplates.PISTON_HEAD.createWithSuffix(Blocks.PISTON, "_head", normalTextures, this.modelOutput)))
/* 2769 */           .select(false, (Comparable)PistonType.STICKY, plainVariant(ModelTemplates.PISTON_HEAD.createWithSuffix(Blocks.PISTON, "_head_sticky", stickyTextures, this.modelOutput)))
/* 2770 */           .select(true, (Comparable)PistonType.DEFAULT, plainVariant(ModelTemplates.PISTON_HEAD_SHORT.createWithSuffix(Blocks.PISTON, "_head_short", normalTextures, this.modelOutput)))
/* 2771 */           .select(true, (Comparable)PistonType.STICKY, plainVariant(ModelTemplates.PISTON_HEAD_SHORT.createWithSuffix(Blocks.PISTON, "_head_short_sticky", stickyTextures, this.modelOutput))))
/*      */         
/* 2773 */         .with(ROTATION_FACING));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createTrialSpawner() {
/* 2780 */     Block block = Blocks.TRIAL_SPAWNER;
/* 2781 */     TextureMapping inactiveTextures = TextureMapping.trialSpawner(block, "_side_inactive", "_top_inactive");
/* 2782 */     TextureMapping activeTextures = TextureMapping.trialSpawner(block, "_side_active", "_top_active");
/* 2783 */     TextureMapping ejectingRewardTextures = TextureMapping.trialSpawner(block, "_side_active", "_top_ejecting_reward");
/* 2784 */     TextureMapping ominousInactiveTextures = TextureMapping.trialSpawner(block, "_side_inactive_ominous", "_top_inactive_ominous");
/* 2785 */     TextureMapping ominousActiveTextures = TextureMapping.trialSpawner(block, "_side_active_ominous", "_top_active_ominous");
/* 2786 */     TextureMapping ominousEjectingRewardTextures = TextureMapping.trialSpawner(block, "_side_active_ominous", "_top_ejecting_reward_ominous");
/*      */     
/* 2788 */     Identifier inactiveModel = ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.create(block, inactiveTextures, this.modelOutput);
/* 2789 */     MultiVariant inactive = plainVariant(inactiveModel);
/* 2790 */     MultiVariant active = plainVariant(ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_active", activeTextures, this.modelOutput));
/* 2791 */     MultiVariant ejectingReward = plainVariant(ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_ejecting_reward", ejectingRewardTextures, this.modelOutput));
/* 2792 */     MultiVariant ominousInactive = plainVariant(ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_inactive_ominous", ominousInactiveTextures, this.modelOutput));
/* 2793 */     MultiVariant ominousActive = plainVariant(ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_active_ominous", ominousActiveTextures, this.modelOutput));
/* 2794 */     MultiVariant ominousEjectingReward = plainVariant(ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_ejecting_reward_ominous", ominousEjectingRewardTextures, this.modelOutput));
/*      */     
/* 2796 */     registerSimpleItemModel(block, inactiveModel);
/*      */     
/* 2798 */     this.blockStateOutput.accept(
/* 2799 */         MultiVariantGenerator.dispatch(block)
/* 2800 */         .with(
/* 2801 */           PropertyDispatch.initial((Property)BlockStateProperties.TRIAL_SPAWNER_STATE, (Property)BlockStateProperties.OMINOUS)
/* 2802 */           .generate((state, ominous) -> { switch (state) { default:
/*      */                   throw new MatchException(null, null);
/*      */                 case INACTIVE:
/*      */                 case COOLDOWN:
/*      */                   if (ominous);
/*      */                 case WAITING_FOR_PLAYERS:
/*      */                 case ACTIVE:
/*      */                 case WAITING_FOR_REWARD_EJECTION:
/*      */                   if (ominous);
/*      */                 case EJECTING_REWARD:
/*      */                   if (ominous); break; }  return ejectingReward;
/* 2813 */             }))); } private void createVault() { Block block = Blocks.VAULT;
/* 2814 */     TextureMapping inactiveTextures = TextureMapping.vault(block, "_front_off", "_side_off", "_top", "_bottom");
/* 2815 */     TextureMapping activeTextures = TextureMapping.vault(block, "_front_on", "_side_on", "_top", "_bottom");
/* 2816 */     TextureMapping unlockingTextures = TextureMapping.vault(block, "_front_ejecting", "_side_on", "_top", "_bottom");
/* 2817 */     TextureMapping ejectingRewardTextures = TextureMapping.vault(block, "_front_ejecting", "_side_on", "_top_ejecting", "_bottom");
/*      */     
/* 2819 */     Identifier inactiveModel = ModelTemplates.VAULT.create(block, inactiveTextures, this.modelOutput);
/* 2820 */     MultiVariant inactive = plainVariant(inactiveModel);
/* 2821 */     MultiVariant active = plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_active", activeTextures, this.modelOutput));
/* 2822 */     MultiVariant unlocking = plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_unlocking", unlockingTextures, this.modelOutput));
/* 2823 */     MultiVariant ejectingReward = plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_ejecting_reward", ejectingRewardTextures, this.modelOutput));
/*      */     
/* 2825 */     TextureMapping inactiveTexturesOminous = TextureMapping.vault(block, "_front_off_ominous", "_side_off_ominous", "_top_ominous", "_bottom_ominous");
/* 2826 */     TextureMapping activeTexturesOminous = TextureMapping.vault(block, "_front_on_ominous", "_side_on_ominous", "_top_ominous", "_bottom_ominous");
/* 2827 */     TextureMapping unlockingTexturesOminous = TextureMapping.vault(block, "_front_ejecting_ominous", "_side_on_ominous", "_top_ominous", "_bottom_ominous");
/* 2828 */     TextureMapping ejectingRewardTexturesOminous = TextureMapping.vault(block, "_front_ejecting_ominous", "_side_on_ominous", "_top_ejecting_ominous", "_bottom_ominous");
/*      */     
/* 2830 */     MultiVariant inactiveOminous = plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_ominous", inactiveTexturesOminous, this.modelOutput));
/* 2831 */     MultiVariant activeOminous = plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_active_ominous", activeTexturesOminous, this.modelOutput));
/* 2832 */     MultiVariant unlockingOminous = plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_unlocking_ominous", unlockingTexturesOminous, this.modelOutput));
/* 2833 */     MultiVariant ejectingRewardOminous = plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_ejecting_reward_ominous", ejectingRewardTexturesOminous, this.modelOutput));
/*      */     
/* 2835 */     registerSimpleItemModel(block, inactiveModel);
/* 2836 */     this.blockStateOutput.accept(
/* 2837 */         MultiVariantGenerator.dispatch(block)
/* 2838 */         .with(
/* 2839 */           PropertyDispatch.initial(VaultBlock.STATE, (Property)VaultBlock.OMINOUS)
/* 2840 */           .generate((state, ominous) -> { switch (state) { default: throw new MatchException(null, null);
/*      */                 case INACTIVE: if (ominous);
/*      */                 case ACTIVE:
/*      */                   if (ominous);
/*      */                 case UNLOCKING:
/*      */                   if (ominous);
/*      */                 case EJECTING:
/* 2847 */                   if (ominous); break; }  return ejectingReward; })).with(ROTATION_HORIZONTAL_FACING)); }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createSculkSensor() {
/* 2852 */     Identifier inactiveModel = ModelLocationUtils.getModelLocation(Blocks.SCULK_SENSOR, "_inactive");
/* 2853 */     MultiVariant inactive = plainVariant(inactiveModel);
/* 2854 */     MultiVariant active = plainVariant(ModelLocationUtils.getModelLocation(Blocks.SCULK_SENSOR, "_active"));
/* 2855 */     registerSimpleItemModel(Blocks.SCULK_SENSOR, inactiveModel);
/* 2856 */     this.blockStateOutput.accept(
/* 2857 */         MultiVariantGenerator.dispatch(Blocks.SCULK_SENSOR)
/* 2858 */         .with(
/* 2859 */           PropertyDispatch.initial((Property)BlockStateProperties.SCULK_SENSOR_PHASE)
/* 2860 */           .generate(phase -> (phase == SculkSensorPhase.ACTIVE || phase == SculkSensorPhase.COOLDOWN) ? active : inactive)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createCalibratedSculkSensor() {
/* 2866 */     Identifier inactiveModel = ModelLocationUtils.getModelLocation(Blocks.CALIBRATED_SCULK_SENSOR, "_inactive");
/* 2867 */     MultiVariant inactive = plainVariant(inactiveModel);
/* 2868 */     MultiVariant active = plainVariant(ModelLocationUtils.getModelLocation(Blocks.CALIBRATED_SCULK_SENSOR, "_active"));
/* 2869 */     registerSimpleItemModel(Blocks.CALIBRATED_SCULK_SENSOR, inactiveModel);
/* 2870 */     this.blockStateOutput.accept(
/* 2871 */         MultiVariantGenerator.dispatch(Blocks.CALIBRATED_SCULK_SENSOR)
/* 2872 */         .with(
/* 2873 */           PropertyDispatch.initial((Property)BlockStateProperties.SCULK_SENSOR_PHASE)
/* 2874 */           .generate(phase -> (phase == SculkSensorPhase.ACTIVE || phase == SculkSensorPhase.COOLDOWN) ? active : inactive))
/*      */         
/* 2876 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createSculkShrieker() {
/* 2881 */     Identifier sculkShriekerModel = ModelTemplates.SCULK_SHRIEKER.create(Blocks.SCULK_SHRIEKER, TextureMapping.sculkShrieker(false), this.modelOutput);
/* 2882 */     MultiVariant sculkShrieker = plainVariant(sculkShriekerModel);
/* 2883 */     MultiVariant sculkShriekerCanSummon = plainVariant(ModelTemplates.SCULK_SHRIEKER.createWithSuffix(Blocks.SCULK_SHRIEKER, "_can_summon", TextureMapping.sculkShrieker(true), this.modelOutput));
/*      */     
/* 2885 */     registerSimpleItemModel(Blocks.SCULK_SHRIEKER, sculkShriekerModel);
/* 2886 */     this.blockStateOutput.accept(
/* 2887 */         MultiVariantGenerator.dispatch(Blocks.SCULK_SHRIEKER)
/* 2888 */         .with(createBooleanModelDispatch(BlockStateProperties.CAN_SUMMON, sculkShriekerCanSummon, sculkShrieker)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createScaffolding() {
/* 2893 */     Identifier stableModel = ModelLocationUtils.getModelLocation(Blocks.SCAFFOLDING, "_stable");
/* 2894 */     MultiVariant stable = plainVariant(stableModel);
/* 2895 */     MultiVariant unstable = plainVariant(ModelLocationUtils.getModelLocation(Blocks.SCAFFOLDING, "_unstable"));
/* 2896 */     registerSimpleItemModel(Blocks.SCAFFOLDING, stableModel);
/* 2897 */     this.blockStateOutput.accept(
/* 2898 */         MultiVariantGenerator.dispatch(Blocks.SCAFFOLDING)
/* 2899 */         .with(createBooleanModelDispatch(BlockStateProperties.BOTTOM, unstable, stable)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createCaveVines() {
/* 2904 */     MultiVariant offHead = plainVariant(createSuffixedVariant(Blocks.CAVE_VINES, "", ModelTemplates.CROSS, TextureMapping::cross));
/* 2905 */     MultiVariant onHead = plainVariant(createSuffixedVariant(Blocks.CAVE_VINES, "_lit", ModelTemplates.CROSS, TextureMapping::cross));
/*      */     
/* 2907 */     this.blockStateOutput.accept(
/* 2908 */         MultiVariantGenerator.dispatch(Blocks.CAVE_VINES)
/* 2909 */         .with(createBooleanModelDispatch(BlockStateProperties.BERRIES, onHead, offHead)));
/*      */ 
/*      */     
/* 2912 */     MultiVariant offBody = plainVariant(createSuffixedVariant(Blocks.CAVE_VINES_PLANT, "", ModelTemplates.CROSS, TextureMapping::cross));
/* 2913 */     MultiVariant onBody = plainVariant(createSuffixedVariant(Blocks.CAVE_VINES_PLANT, "_lit", ModelTemplates.CROSS, TextureMapping::cross));
/*      */     
/* 2915 */     this.blockStateOutput.accept(
/* 2916 */         MultiVariantGenerator.dispatch(Blocks.CAVE_VINES_PLANT)
/* 2917 */         .with(createBooleanModelDispatch(BlockStateProperties.BERRIES, onBody, offBody)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createRedstoneLamp() {
/* 2922 */     MultiVariant off = plainVariant(TexturedModel.CUBE.create(Blocks.REDSTONE_LAMP, this.modelOutput));
/* 2923 */     MultiVariant on = plainVariant(createSuffixedVariant(Blocks.REDSTONE_LAMP, "_on", ModelTemplates.CUBE_ALL, TextureMapping::cube));
/*      */     
/* 2925 */     this.blockStateOutput.accept(
/* 2926 */         MultiVariantGenerator.dispatch(Blocks.REDSTONE_LAMP)
/* 2927 */         .with(createBooleanModelDispatch(BlockStateProperties.LIT, on, off)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createNormalTorch(Block ground, Block wall) {
/* 2932 */     TextureMapping textures = TextureMapping.torch(ground);
/*      */     
/* 2934 */     this.blockStateOutput.accept(createSimpleBlock(ground, plainVariant(ModelTemplates.TORCH.create(ground, textures, this.modelOutput))));
/*      */     
/* 2936 */     this.blockStateOutput.accept(
/* 2937 */         MultiVariantGenerator.dispatch(wall, plainVariant(ModelTemplates.WALL_TORCH.create(wall, textures, this.modelOutput)))
/* 2938 */         .with(ROTATION_TORCH));
/*      */     
/* 2940 */     registerSimpleFlatItemModel(ground);
/*      */   }
/*      */   
/*      */   private void createRedstoneTorch() {
/* 2944 */     TextureMapping onTextures = TextureMapping.torch(Blocks.REDSTONE_TORCH);
/* 2945 */     TextureMapping offTextures = TextureMapping.torch(TextureMapping.getBlockTexture(Blocks.REDSTONE_TORCH, "_off"));
/*      */     
/* 2947 */     MultiVariant groundModelOn = plainVariant(ModelTemplates.REDSTONE_TORCH.create(Blocks.REDSTONE_TORCH, onTextures, this.modelOutput));
/* 2948 */     MultiVariant groundModelOff = plainVariant(ModelTemplates.TORCH_UNLIT.createWithSuffix(Blocks.REDSTONE_TORCH, "_off", offTextures, this.modelOutput));
/*      */     
/* 2950 */     this.blockStateOutput.accept(
/* 2951 */         MultiVariantGenerator.dispatch(Blocks.REDSTONE_TORCH)
/* 2952 */         .with(createBooleanModelDispatch(BlockStateProperties.LIT, groundModelOn, groundModelOff)));
/*      */ 
/*      */     
/* 2955 */     MultiVariant wallModelOn = plainVariant(ModelTemplates.REDSTONE_WALL_TORCH.create(Blocks.REDSTONE_WALL_TORCH, onTextures, this.modelOutput));
/* 2956 */     MultiVariant wallModelOff = plainVariant(ModelTemplates.WALL_TORCH_UNLIT.createWithSuffix(Blocks.REDSTONE_WALL_TORCH, "_off", offTextures, this.modelOutput));
/*      */     
/* 2958 */     this.blockStateOutput.accept(
/* 2959 */         MultiVariantGenerator.dispatch(Blocks.REDSTONE_WALL_TORCH)
/* 2960 */         .with(createBooleanModelDispatch(BlockStateProperties.LIT, wallModelOn, wallModelOff))
/* 2961 */         .with(ROTATION_TORCH));
/*      */     
/* 2963 */     registerSimpleFlatItemModel(Blocks.REDSTONE_TORCH);
/*      */   }
/*      */   
/*      */   private void createRepeater() {
/* 2967 */     registerSimpleFlatItemModel(Items.REPEATER);
/* 2968 */     this.blockStateOutput.accept(
/* 2969 */         MultiVariantGenerator.dispatch(Blocks.REPEATER)
/* 2970 */         .with(
/* 2971 */           PropertyDispatch.initial((Property)BlockStateProperties.DELAY, (Property)BlockStateProperties.LOCKED, (Property)BlockStateProperties.POWERED)
/* 2972 */           .generate((delay, locked, powered) -> {
/*      */               StringBuilder suffix = new StringBuilder();
/*      */               
/*      */               suffix.append('_').append(delay).append("tick");
/*      */               
/*      */               if (powered) {
/*      */                 suffix.append("_on");
/*      */               }
/*      */               if (locked) {
/*      */                 suffix.append("_locked");
/*      */               }
/*      */               return plainVariant(TextureMapping.getBlockTexture(Blocks.REPEATER, suffix.toString()));
/* 2984 */             })).with(ROTATION_HORIZONTAL_FACING_ALT));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createSeaPickle() {
/* 2989 */     registerSimpleFlatItemModel(Items.SEA_PICKLE);
/*      */     
/* 2991 */     this.blockStateOutput.accept(
/* 2992 */         MultiVariantGenerator.dispatch(Blocks.SEA_PICKLE)
/* 2993 */         .with(
/* 2994 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.PICKLES, (Property)BlockStateProperties.WATERLOGGED)
/* 2995 */           .select(1, false, createRotatedVariants(plainModel(ModelLocationUtils.decorateBlockModelLocation("dead_sea_pickle"))))
/* 2996 */           .select(2, false, createRotatedVariants(plainModel(ModelLocationUtils.decorateBlockModelLocation("two_dead_sea_pickles"))))
/* 2997 */           .select(3, false, createRotatedVariants(plainModel(ModelLocationUtils.decorateBlockModelLocation("three_dead_sea_pickles"))))
/* 2998 */           .select(4, false, createRotatedVariants(plainModel(ModelLocationUtils.decorateBlockModelLocation("four_dead_sea_pickles"))))
/*      */           
/* 3000 */           .select(1, true, createRotatedVariants(plainModel(ModelLocationUtils.decorateBlockModelLocation("sea_pickle"))))
/* 3001 */           .select(2, true, createRotatedVariants(plainModel(ModelLocationUtils.decorateBlockModelLocation("two_sea_pickles"))))
/* 3002 */           .select(3, true, createRotatedVariants(plainModel(ModelLocationUtils.decorateBlockModelLocation("three_sea_pickles"))))
/* 3003 */           .select(4, true, createRotatedVariants(plainModel(ModelLocationUtils.decorateBlockModelLocation("four_sea_pickles"))))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createSnowBlocks() {
/* 3010 */     TextureMapping textures = TextureMapping.cube(Blocks.SNOW);
/* 3011 */     MultiVariant snowModel = plainVariant(ModelTemplates.CUBE_ALL.create(Blocks.SNOW_BLOCK, textures, this.modelOutput));
/*      */     
/* 3013 */     this.blockStateOutput.accept(
/* 3014 */         MultiVariantGenerator.dispatch(Blocks.SNOW)
/* 3015 */         .with(
/* 3016 */           PropertyDispatch.initial((Property)BlockStateProperties.LAYERS)
/* 3017 */           .generate(level -> (level < 8) ? plainVariant(ModelLocationUtils.getModelLocation(Blocks.SNOW, "_height" + level * 2)) : snowModel)));
/*      */ 
/*      */ 
/*      */     
/* 3021 */     registerSimpleItemModel(Blocks.SNOW, ModelLocationUtils.getModelLocation(Blocks.SNOW, "_height2"));
/* 3022 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.SNOW_BLOCK, snowModel));
/*      */   }
/*      */   
/*      */   private void createStonecutter() {
/* 3026 */     this.blockStateOutput.accept(
/* 3027 */         MultiVariantGenerator.dispatch(Blocks.STONECUTTER, plainVariant(ModelLocationUtils.getModelLocation(Blocks.STONECUTTER)))
/* 3028 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createStructureBlock() {
/* 3033 */     Identifier inventory = TexturedModel.CUBE.create(Blocks.STRUCTURE_BLOCK, this.modelOutput);
/* 3034 */     registerSimpleItemModel(Blocks.STRUCTURE_BLOCK, inventory);
/*      */     
/* 3036 */     this.blockStateOutput.accept(
/* 3037 */         MultiVariantGenerator.dispatch(Blocks.STRUCTURE_BLOCK)
/* 3038 */         .with(
/* 3039 */           PropertyDispatch.initial((Property)BlockStateProperties.STRUCTUREBLOCK_MODE)
/* 3040 */           .generate(model -> plainVariant(createSuffixedVariant(Blocks.STRUCTURE_BLOCK, "_" + model.getSerializedName(), ModelTemplates.CUBE_ALL, TextureMapping::cube)))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createTestBlock() {
/* 3046 */     Map<TestBlockMode, Identifier> variantIds = new HashMap<>();
/* 3047 */     for (TestBlockMode mode : TestBlockMode.values()) {
/* 3048 */       variantIds.put(mode, createSuffixedVariant(Blocks.TEST_BLOCK, "_" + mode.getSerializedName(), ModelTemplates.CUBE_ALL, TextureMapping::cube));
/*      */     }
/*      */     
/* 3051 */     this.blockStateOutput.accept(
/* 3052 */         MultiVariantGenerator.dispatch(Blocks.TEST_BLOCK)
/* 3053 */         .with(
/* 3054 */           PropertyDispatch.initial((Property)BlockStateProperties.TEST_BLOCK_MODE)
/* 3055 */           .generate(mode -> plainVariant((Identifier)variantIds.get(mode)))));
/*      */ 
/*      */ 
/*      */     
/* 3059 */     this.itemModelOutput.accept(Items.TEST_BLOCK, ItemModelUtils.selectBlockItemProperty((Property)TestBlock.MODE, 
/*      */           
/* 3061 */           ItemModelUtils.plainModel(variantIds.get(TestBlockMode.START)), 
/* 3062 */           Map.of(TestBlockMode.FAIL, 
/* 3063 */             ItemModelUtils.plainModel(variantIds.get(TestBlockMode.FAIL)), TestBlockMode.LOG, 
/* 3064 */             ItemModelUtils.plainModel(variantIds.get(TestBlockMode.LOG)), TestBlockMode.ACCEPT, 
/* 3065 */             ItemModelUtils.plainModel(variantIds.get(TestBlockMode.ACCEPT)))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createSweetBerryBush() {
/* 3071 */     registerSimpleFlatItemModel(Items.SWEET_BERRIES);
/* 3072 */     this.blockStateOutput.accept(
/* 3073 */         MultiVariantGenerator.dispatch(Blocks.SWEET_BERRY_BUSH)
/* 3074 */         .with(
/* 3075 */           PropertyDispatch.initial((Property)BlockStateProperties.AGE_3)
/* 3076 */           .generate(age -> plainVariant(createSuffixedVariant(Blocks.SWEET_BERRY_BUSH, "_stage" + age, ModelTemplates.CROSS, TextureMapping::cross)))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createTripwire() {
/* 3084 */     registerSimpleFlatItemModel(Items.STRING);
/* 3085 */     this.blockStateOutput.accept(
/* 3086 */         MultiVariantGenerator.dispatch(Blocks.TRIPWIRE)
/* 3087 */         .with(
/* 3088 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.ATTACHED, (Property)BlockStateProperties.EAST, (Property)BlockStateProperties.NORTH, (Property)BlockStateProperties.SOUTH, (Property)BlockStateProperties.WEST)
/*      */           
/* 3090 */           .select(false, false, false, false, false, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ns")))
/*      */ 
/*      */           
/* 3093 */           .select(false, true, false, false, false, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_n")).with(Y_ROT_90))
/* 3094 */           .select(false, false, true, false, false, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_n")))
/* 3095 */           .select(false, false, false, true, false, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_n")).with(Y_ROT_180))
/* 3096 */           .select(false, false, false, false, true, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_n")).with(Y_ROT_270))
/*      */ 
/*      */           
/* 3099 */           .select(false, true, true, false, false, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ne")))
/* 3100 */           .select(false, true, false, true, false, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ne")).with(Y_ROT_90))
/* 3101 */           .select(false, false, false, true, true, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ne")).with(Y_ROT_180))
/* 3102 */           .select(false, false, true, false, true, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ne")).with(Y_ROT_270))
/* 3103 */           .select(false, false, true, true, false, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ns")))
/* 3104 */           .select(false, true, false, false, true, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ns")).with(Y_ROT_90))
/*      */ 
/*      */           
/* 3107 */           .select(false, true, true, true, false, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nse")))
/* 3108 */           .select(false, true, false, true, true, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nse")).with(Y_ROT_90))
/* 3109 */           .select(false, false, true, true, true, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nse")).with(Y_ROT_180))
/* 3110 */           .select(false, true, true, false, true, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nse")).with(Y_ROT_270))
/*      */ 
/*      */           
/* 3113 */           .select(false, true, true, true, true, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nsew")))
/*      */ 
/*      */           
/* 3116 */           .select(true, false, false, false, false, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ns")))
/*      */ 
/*      */           
/* 3119 */           .select(true, false, true, false, false, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_n")))
/* 3120 */           .select(true, false, false, true, false, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_n")).with(Y_ROT_180))
/* 3121 */           .select(true, true, false, false, false, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_n")).with(Y_ROT_90))
/* 3122 */           .select(true, false, false, false, true, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_n")).with(Y_ROT_270))
/*      */ 
/*      */           
/* 3125 */           .select(true, true, true, false, false, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ne")))
/* 3126 */           .select(true, true, false, true, false, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ne")).with(Y_ROT_90))
/* 3127 */           .select(true, false, false, true, true, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ne")).with(Y_ROT_180))
/* 3128 */           .select(true, false, true, false, true, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ne")).with(Y_ROT_270))
/* 3129 */           .select(true, false, true, true, false, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ns")))
/* 3130 */           .select(true, true, false, false, true, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ns")).with(Y_ROT_90))
/*      */ 
/*      */           
/* 3133 */           .select(true, true, true, true, false, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nse")))
/* 3134 */           .select(true, true, false, true, true, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nse")).with(Y_ROT_90))
/* 3135 */           .select(true, false, true, true, true, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nse")).with(Y_ROT_180))
/* 3136 */           .select(true, true, true, false, true, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nse")).with(Y_ROT_270))
/*      */ 
/*      */           
/* 3139 */           .select(true, true, true, true, true, plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nsew")))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createTripwireHook() {
/* 3145 */     registerSimpleFlatItemModel(Blocks.TRIPWIRE_HOOK);
/* 3146 */     this.blockStateOutput.accept(
/* 3147 */         MultiVariantGenerator.dispatch(Blocks.TRIPWIRE_HOOK)
/* 3148 */         .with(
/* 3149 */           PropertyDispatch.initial((Property)BlockStateProperties.ATTACHED, (Property)BlockStateProperties.POWERED)
/* 3150 */           .generate((attached, powered) -> plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE_HOOK, (attached ? "_attached" : "") + (attached ? "_attached" : "")))))
/*      */         
/* 3152 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Variant createTurtleEggModel(int count, String hatchProgress, TextureMapping texture) {
/*      */     // Byte code:
/*      */     //   0: iload_1
/*      */     //   1: tableswitch default -> 136, 1 -> 32, 2 -> 58, 3 -> 84, 4 -> 110
/*      */     //   32: getstatic net/minecraft/client/data/models/model/ModelTemplates.TURTLE_EGG : Lnet/minecraft/client/data/models/model/ModelTemplate;
/*      */     //   35: aload_2
/*      */     //   36: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*      */     //   41: invokestatic decorateBlockModelLocation : (Ljava/lang/String;)Lnet/minecraft/resources/Identifier;
/*      */     //   44: aload_3
/*      */     //   45: aload_0
/*      */     //   46: getfield modelOutput : Ljava/util/function/BiConsumer;
/*      */     //   49: invokevirtual create : (Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/data/models/model/TextureMapping;Ljava/util/function/BiConsumer;)Lnet/minecraft/resources/Identifier;
/*      */     //   52: invokestatic plainModel : (Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/block/model/Variant;
/*      */     //   55: goto -> 144
/*      */     //   58: getstatic net/minecraft/client/data/models/model/ModelTemplates.TWO_TURTLE_EGGS : Lnet/minecraft/client/data/models/model/ModelTemplate;
/*      */     //   61: aload_2
/*      */     //   62: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*      */     //   67: invokestatic decorateBlockModelLocation : (Ljava/lang/String;)Lnet/minecraft/resources/Identifier;
/*      */     //   70: aload_3
/*      */     //   71: aload_0
/*      */     //   72: getfield modelOutput : Ljava/util/function/BiConsumer;
/*      */     //   75: invokevirtual create : (Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/data/models/model/TextureMapping;Ljava/util/function/BiConsumer;)Lnet/minecraft/resources/Identifier;
/*      */     //   78: invokestatic plainModel : (Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/block/model/Variant;
/*      */     //   81: goto -> 144
/*      */     //   84: getstatic net/minecraft/client/data/models/model/ModelTemplates.THREE_TURTLE_EGGS : Lnet/minecraft/client/data/models/model/ModelTemplate;
/*      */     //   87: aload_2
/*      */     //   88: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*      */     //   93: invokestatic decorateBlockModelLocation : (Ljava/lang/String;)Lnet/minecraft/resources/Identifier;
/*      */     //   96: aload_3
/*      */     //   97: aload_0
/*      */     //   98: getfield modelOutput : Ljava/util/function/BiConsumer;
/*      */     //   101: invokevirtual create : (Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/data/models/model/TextureMapping;Ljava/util/function/BiConsumer;)Lnet/minecraft/resources/Identifier;
/*      */     //   104: invokestatic plainModel : (Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/block/model/Variant;
/*      */     //   107: goto -> 144
/*      */     //   110: getstatic net/minecraft/client/data/models/model/ModelTemplates.FOUR_TURTLE_EGGS : Lnet/minecraft/client/data/models/model/ModelTemplate;
/*      */     //   113: aload_2
/*      */     //   114: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*      */     //   119: invokestatic decorateBlockModelLocation : (Ljava/lang/String;)Lnet/minecraft/resources/Identifier;
/*      */     //   122: aload_3
/*      */     //   123: aload_0
/*      */     //   124: getfield modelOutput : Ljava/util/function/BiConsumer;
/*      */     //   127: invokevirtual create : (Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/data/models/model/TextureMapping;Ljava/util/function/BiConsumer;)Lnet/minecraft/resources/Identifier;
/*      */     //   130: invokestatic plainModel : (Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/block/model/Variant;
/*      */     //   133: goto -> 144
/*      */     //   136: new java/lang/UnsupportedOperationException
/*      */     //   139: dup
/*      */     //   140: invokespecial <init> : ()V
/*      */     //   143: athrow
/*      */     //   144: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #3157	-> 0
/*      */     //   #3159	-> 32
/*      */     //   #3161	-> 58
/*      */     //   #3163	-> 84
/*      */     //   #3165	-> 110
/*      */     //   #3166	-> 136
/*      */     //   #3157	-> 144
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	145	0	this	Lnet/minecraft/client/data/models/BlockModelGenerators;
/*      */     //   0	145	1	count	I
/*      */     //   0	145	2	hatchProgress	Ljava/lang/String;
/*      */     //   0	145	3	texture	Lnet/minecraft/client/data/models/model/TextureMapping;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Variant createTurtleEggModel(int eggs, int hatch) {
/*      */     // Byte code:
/*      */     //   0: iload_2
/*      */     //   1: tableswitch default -> 94, 0 -> 28, 1 -> 48, 2 -> 71
/*      */     //   28: aload_0
/*      */     //   29: iload_1
/*      */     //   30: ldc_w ''
/*      */     //   33: getstatic net/minecraft/world/level/block/Blocks.TURTLE_EGG : Lnet/minecraft/world/level/block/Block;
/*      */     //   36: invokestatic getBlockTexture : (Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/resources/Identifier;
/*      */     //   39: invokestatic cube : (Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/data/models/model/TextureMapping;
/*      */     //   42: invokevirtual createTurtleEggModel : (ILjava/lang/String;Lnet/minecraft/client/data/models/model/TextureMapping;)Lnet/minecraft/client/renderer/block/model/Variant;
/*      */     //   45: goto -> 102
/*      */     //   48: aload_0
/*      */     //   49: iload_1
/*      */     //   50: ldc_w 'slightly_cracked_'
/*      */     //   53: getstatic net/minecraft/world/level/block/Blocks.TURTLE_EGG : Lnet/minecraft/world/level/block/Block;
/*      */     //   56: ldc_w '_slightly_cracked'
/*      */     //   59: invokestatic getBlockTexture : (Lnet/minecraft/world/level/block/Block;Ljava/lang/String;)Lnet/minecraft/resources/Identifier;
/*      */     //   62: invokestatic cube : (Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/data/models/model/TextureMapping;
/*      */     //   65: invokevirtual createTurtleEggModel : (ILjava/lang/String;Lnet/minecraft/client/data/models/model/TextureMapping;)Lnet/minecraft/client/renderer/block/model/Variant;
/*      */     //   68: goto -> 102
/*      */     //   71: aload_0
/*      */     //   72: iload_1
/*      */     //   73: ldc_w 'very_cracked_'
/*      */     //   76: getstatic net/minecraft/world/level/block/Blocks.TURTLE_EGG : Lnet/minecraft/world/level/block/Block;
/*      */     //   79: ldc_w '_very_cracked'
/*      */     //   82: invokestatic getBlockTexture : (Lnet/minecraft/world/level/block/Block;Ljava/lang/String;)Lnet/minecraft/resources/Identifier;
/*      */     //   85: invokestatic cube : (Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/data/models/model/TextureMapping;
/*      */     //   88: invokevirtual createTurtleEggModel : (ILjava/lang/String;Lnet/minecraft/client/data/models/model/TextureMapping;)Lnet/minecraft/client/renderer/block/model/Variant;
/*      */     //   91: goto -> 102
/*      */     //   94: new java/lang/UnsupportedOperationException
/*      */     //   97: dup
/*      */     //   98: invokespecial <init> : ()V
/*      */     //   101: athrow
/*      */     //   102: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #3171	-> 0
/*      */     //   #3172	-> 28
/*      */     //   #3174	-> 48
/*      */     //   #3176	-> 71
/*      */     //   #3177	-> 94
/*      */     //   #3171	-> 102
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	103	0	this	Lnet/minecraft/client/data/models/BlockModelGenerators;
/*      */     //   0	103	1	eggs	I
/*      */     //   0	103	2	hatch	I
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createTurtleEgg() {
/* 3182 */     registerSimpleFlatItemModel(Items.TURTLE_EGG);
/* 3183 */     this.blockStateOutput.accept(
/* 3184 */         MultiVariantGenerator.dispatch(Blocks.TURTLE_EGG)
/* 3185 */         .with(
/* 3186 */           PropertyDispatch.initial((Property)BlockStateProperties.EGGS, (Property)BlockStateProperties.HATCH)
/* 3187 */           .generate((eggs, hatch) -> createRotatedVariants(createTurtleEggModel(eggs, hatch)))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createDriedGhastBlock() {
/* 3193 */     Identifier driedGhast = ModelLocationUtils.getModelLocation(Blocks.DRIED_GHAST, "_hydration_0");
/* 3194 */     registerSimpleItemModel(Blocks.DRIED_GHAST, driedGhast); Function<Integer, Identifier> createModel = stage -> {
/*      */         switch (stage) {
/*      */           case 1:
/*      */           
/*      */           case 2:
/*      */           
/*      */           case 3:
/*      */           
/*      */           default:
/*      */             break;
/*      */         } 
/*      */         String suffix = "_hydration_0";
/*      */         TextureMapping texture = TextureMapping.driedGhast(suffix);
/*      */         return ModelTemplates.DRIED_GHAST.createWithSuffix(Blocks.DRIED_GHAST, suffix, texture, this.modelOutput);
/*      */       };
/* 3209 */     this.blockStateOutput.accept(
/* 3210 */         MultiVariantGenerator.dispatch(Blocks.DRIED_GHAST)
/* 3211 */         .with(PropertyDispatch.initial((Property)DriedGhastBlock.HYDRATION_LEVEL)
/* 3212 */           .generate(stage -> plainVariant(createModel.apply(stage))))
/* 3213 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createSnifferEgg() {
/* 3218 */     registerSimpleFlatItemModel(Items.SNIFFER_EGG);
/*      */     
/* 3220 */     this.blockStateOutput.accept(
/* 3221 */         MultiVariantGenerator.dispatch(Blocks.SNIFFER_EGG)
/* 3222 */         .with(PropertyDispatch.initial((Property)SnifferEggBlock.HATCH)
/* 3223 */           .generate(stage -> {
/*      */               switch (stage) {
/*      */                 case 1:
/*      */                 
/*      */                 case 2:
/*      */                 
/*      */                 default:
/*      */                   break;
/*      */               } 
/*      */               String suffix = "_not_cracked";
/*      */               TextureMapping texture = TextureMapping.snifferEgg(suffix);
/*      */               return plainVariant(ModelTemplates.SNIFFER_EGG.createWithSuffix(Blocks.SNIFFER_EGG, suffix, texture, this.modelOutput));
/*      */             })));
/*      */   }
/*      */   
/*      */   private void createMultiface(Block block) {
/* 3239 */     registerSimpleFlatItemModel(block);
/* 3240 */     createMultifaceBlockStates(block);
/*      */   }
/*      */   
/*      */   private void createMultiface(Block block, Item item) {
/* 3244 */     registerSimpleFlatItemModel(item);
/* 3245 */     createMultifaceBlockStates(block);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3252 */   private static final Map<Direction, VariantMutator> MULTIFACE_GENERATOR = (Map<Direction, VariantMutator>)ImmutableMap.of(Direction.NORTH, NOP, Direction.EAST, 
/*      */       
/* 3254 */       Y_ROT_90.then(UV_LOCK), Direction.SOUTH, 
/* 3255 */       Y_ROT_180.then(UV_LOCK), Direction.WEST, 
/* 3256 */       Y_ROT_270.then(UV_LOCK), Direction.UP, 
/* 3257 */       X_ROT_270.then(UV_LOCK), Direction.DOWN, 
/* 3258 */       X_ROT_90.then(UV_LOCK));
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T extends Property<?>> Map<T, VariantMutator> selectMultifaceProperties(StateHolder<?, ?> holder, Function<Direction, T> converter) {
/* 3263 */     ImmutableMap.Builder<T, VariantMutator> result = ImmutableMap.builderWithExpectedSize(MULTIFACE_GENERATOR.size());
/* 3264 */     MULTIFACE_GENERATOR.forEach((direction, mutator) -> {
/*      */           Property property = converter.apply(direction);
/*      */           if (holder.hasProperty(property)) {
/*      */             result.put(property, mutator);
/*      */           }
/*      */         });
/* 3270 */     return (Map<T, VariantMutator>)result.build();
/*      */   }
/*      */   
/*      */   private void createMultifaceBlockStates(Block block) {
/* 3274 */     Map<Property<Boolean>, VariantMutator> directionProperties = selectMultifaceProperties((StateHolder<?, ?>)block.defaultBlockState(), MultifaceBlock::getFaceProperty);
/*      */     
/* 3276 */     ConditionBuilder noFaces = condition();
/* 3277 */     directionProperties.forEach((property, mutator) -> noFaces.term(property, false));
/*      */     
/* 3279 */     MultiVariant model = plainVariant(ModelLocationUtils.getModelLocation(block));
/*      */     
/* 3281 */     MultiPartGenerator generator = MultiPartGenerator.multiPart(block);
/* 3282 */     directionProperties.forEach((property, mutator) -> {
/*      */           generator.with(condition().term(property, true), model.with(mutator));
/*      */           
/*      */           generator.with(noFaces, model.with(mutator));
/*      */         });
/* 3287 */     this.blockStateOutput.accept(generator);
/*      */   }
/*      */   
/*      */   private void createMossyCarpet(Block block) {
/* 3291 */     Map<Property<WallSide>, VariantMutator> directionProperties = selectMultifaceProperties((StateHolder<?, ?>)block.defaultBlockState(), MossyCarpetBlock::getPropertyForFace);
/*      */     
/* 3293 */     ConditionBuilder noFaces = condition().term((Property)MossyCarpetBlock.BASE, false);
/* 3294 */     directionProperties.forEach((property, mutator) -> noFaces.term(property, (Comparable)WallSide.NONE));
/*      */     
/* 3296 */     MultiVariant modelCarpet = plainVariant(TexturedModel.CARPET.create(block, this.modelOutput));
/* 3297 */     MultiVariant modelSideTall = plainVariant(TexturedModel.MOSSY_CARPET_SIDE.get(block).updateTextures(m -> m.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side_tall"))).createWithSuffix(block, "_side_tall", this.modelOutput));
/* 3298 */     MultiVariant modelSideSmall = plainVariant(TexturedModel.MOSSY_CARPET_SIDE.get(block).updateTextures(m -> m.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side_small"))).createWithSuffix(block, "_side_small", this.modelOutput));
/*      */     
/* 3300 */     MultiPartGenerator generator = MultiPartGenerator.multiPart(block);
/* 3301 */     generator.with(condition().term((Property)MossyCarpetBlock.BASE, true), modelCarpet);
/* 3302 */     generator.with(noFaces, modelCarpet);
/* 3303 */     directionProperties.forEach((property, mutator) -> {
/*      */           generator.with(condition().term(property, (Comparable)WallSide.TALL), modelSideTall.with(mutator));
/*      */           
/*      */           generator.with(condition().term(property, (Comparable)WallSide.LOW), modelSideSmall.with(mutator));
/*      */           generator.with(noFaces, modelSideTall.with(mutator));
/*      */         });
/* 3309 */     this.blockStateOutput.accept(generator);
/*      */   }
/*      */   
/*      */   private void createHangingMoss(Block block) {
/* 3313 */     registerSimpleFlatItemModel(block);
/* 3314 */     this.blockStateOutput.accept(
/* 3315 */         MultiVariantGenerator.dispatch(block)
/* 3316 */         .with(
/* 3317 */           PropertyDispatch.initial((Property)HangingMossBlock.TIP)
/* 3318 */           .generate(isTip -> {
/*      */               String suffix = block ? "_tip" : "";
/*      */               TextureMapping texture = TextureMapping.cross(TextureMapping.getBlockTexture(block, suffix));
/*      */               return plainVariant(PlantType.NOT_TINTED.getCross().createWithSuffix(block, suffix, texture, this.modelOutput));
/*      */             })));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createSculkCatalyst() {
/* 3328 */     Identifier bottom = TextureMapping.getBlockTexture(Blocks.SCULK_CATALYST, "_bottom");
/* 3329 */     TextureMapping defaultTextureMap = new TextureMapping()
/* 3330 */       .put(TextureSlot.BOTTOM, bottom)
/* 3331 */       .put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.SCULK_CATALYST, "_top"))
/* 3332 */       .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.SCULK_CATALYST, "_side"));
/*      */     
/* 3334 */     TextureMapping bloomTextureMap = new TextureMapping()
/* 3335 */       .put(TextureSlot.BOTTOM, bottom)
/* 3336 */       .put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.SCULK_CATALYST, "_top_bloom"))
/* 3337 */       .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.SCULK_CATALYST, "_side_bloom"));
/*      */     
/* 3339 */     Identifier defaultModel = ModelTemplates.CUBE_BOTTOM_TOP.create(Blocks.SCULK_CATALYST, defaultTextureMap, this.modelOutput);
/* 3340 */     MultiVariant defaultVariant = plainVariant(defaultModel);
/* 3341 */     MultiVariant bloom = plainVariant(ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(Blocks.SCULK_CATALYST, "_bloom", bloomTextureMap, this.modelOutput));
/*      */     
/* 3343 */     this.blockStateOutput.accept(
/* 3344 */         MultiVariantGenerator.dispatch(Blocks.SCULK_CATALYST)
/* 3345 */         .with(
/* 3346 */           PropertyDispatch.initial((Property)BlockStateProperties.BLOOM)
/* 3347 */           .generate(pulse -> pulse ? bloom : defaultVariant)));
/*      */ 
/*      */     
/* 3350 */     registerSimpleItemModel(Blocks.SCULK_CATALYST, defaultModel);
/*      */   }
/*      */   
/*      */   private void createShelf(Block block, Block particle) {
/* 3354 */     TextureMapping mapping = new TextureMapping().put(TextureSlot.ALL, TextureMapping.getBlockTexture(block)).put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(particle));
/* 3355 */     MultiPartGenerator generator = MultiPartGenerator.multiPart(block);
/*      */     
/* 3357 */     addShelfPart(block, mapping, generator, ModelTemplates.SHELF_BODY, null, null);
/* 3358 */     addShelfPart(block, mapping, generator, ModelTemplates.SHELF_UNPOWERED, false, null);
/*      */     
/* 3360 */     addShelfPart(block, mapping, generator, ModelTemplates.SHELF_UNCONNECTED, true, SideChainPart.UNCONNECTED);
/* 3361 */     addShelfPart(block, mapping, generator, ModelTemplates.SHELF_LEFT, true, SideChainPart.LEFT);
/* 3362 */     addShelfPart(block, mapping, generator, ModelTemplates.SHELF_CENTER, true, SideChainPart.CENTER);
/* 3363 */     addShelfPart(block, mapping, generator, ModelTemplates.SHELF_RIGHT, true, SideChainPart.RIGHT);
/*      */     
/* 3365 */     this.blockStateOutput.accept(generator);
/* 3366 */     registerSimpleItemModel(block, ModelTemplates.SHELF_INVENTORY.create(block, mapping, this.modelOutput));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void addShelfPart(Block block, TextureMapping mapping, MultiPartGenerator generator, ModelTemplate template, Boolean isPowered, SideChainPart sideChainPart) {
/* 3372 */     MultiVariant variant = plainVariant(template.create(block, mapping, this.modelOutput));
/* 3373 */     forEachHorizontalDirection((direction, rotation) -> generator.with(shelfCondition(direction, isPowered, sideChainPart), variant.with(rotation)));
/*      */   }
/*      */ 
/*      */   
/*      */   private static void forEachHorizontalDirection(BiConsumer<Direction, VariantMutator> consumer) {
/* 3378 */     List.<Pair>of(
/* 3379 */         Pair.of(Direction.NORTH, NOP), 
/* 3380 */         Pair.of(Direction.EAST, Y_ROT_90), 
/* 3381 */         Pair.of(Direction.SOUTH, Y_ROT_180), 
/* 3382 */         Pair.of(Direction.WEST, Y_ROT_270))
/* 3383 */       .forEach(pair -> {
/*      */           Direction direction = (Direction)pair.getFirst();
/*      */           VariantMutator rotation = (VariantMutator)pair.getSecond();
/*      */           consumer.accept(direction, rotation);
/*      */         });
/*      */   }
/*      */   
/*      */   private static Condition shelfCondition(Direction direction, Boolean isPowered, SideChainPart sideChainPart) {
/* 3391 */     ConditionBuilder facing = condition(BlockStateProperties.HORIZONTAL_FACING, direction, new Direction[0]);
/* 3392 */     if (isPowered == null) {
/* 3393 */       return facing.build();
/*      */     }
/*      */     
/* 3396 */     ConditionBuilder powered = condition(BlockStateProperties.POWERED, isPowered);
/* 3397 */     return (sideChainPart != null) ? 
/* 3398 */       and(new ConditionBuilder[] { facing, powered, condition(BlockStateProperties.SIDE_CHAIN_PART, sideChainPart, new SideChainPart[0])
/* 3399 */         }) : and(new ConditionBuilder[] { facing, powered });
/*      */   }
/*      */   
/*      */   private void createChiseledBookshelf() {
/* 3403 */     Block block = Blocks.CHISELED_BOOKSHELF;
/* 3404 */     MultiVariant body = plainVariant(ModelLocationUtils.getModelLocation(block));
/* 3405 */     MultiPartGenerator multiPartGenerator = MultiPartGenerator.multiPart(block);
/*      */     
/* 3407 */     forEachHorizontalDirection((direction, rotation) -> {
/*      */           Condition facingCondition = condition().term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)body).build();
/*      */           
/*      */           multiPartGenerator.with(facingCondition, multiPartGenerator.with(rotation).with(UV_LOCK));
/*      */           addSlotStateAndRotationVariants(multiPartGenerator, facingCondition, rotation);
/*      */         });
/* 3413 */     this.blockStateOutput.accept(multiPartGenerator);
/* 3414 */     registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block, "_inventory"));
/* 3415 */     CHISELED_BOOKSHELF_SLOT_MODEL_CACHE.clear();
/*      */   }
/*      */   
/*      */   private void addSlotStateAndRotationVariants(MultiPartGenerator multiPartGenerator, Condition facingCondition, VariantMutator mutator) {
/* 3419 */     List.<Pair>of(
/* 3420 */         Pair.of(ChiseledBookShelfBlock.SLOT_0_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_TOP_LEFT), 
/* 3421 */         Pair.of(ChiseledBookShelfBlock.SLOT_1_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_TOP_MID), 
/* 3422 */         Pair.of(ChiseledBookShelfBlock.SLOT_2_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_TOP_RIGHT), 
/* 3423 */         Pair.of(ChiseledBookShelfBlock.SLOT_3_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_BOTTOM_LEFT), 
/* 3424 */         Pair.of(ChiseledBookShelfBlock.SLOT_4_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_BOTTOM_MID), 
/* 3425 */         Pair.of(ChiseledBookShelfBlock.SLOT_5_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_BOTTOM_RIGHT))
/* 3426 */       .forEach(pair -> {
/*      */           BooleanProperty stateProperty = (BooleanProperty)facingCondition.getFirst();
/*      */           ModelTemplate modelTemplate = (ModelTemplate)facingCondition.getSecond();
/*      */           addBookSlotModel(multiPartGenerator, multiPartGenerator, multiPartGenerator, stateProperty, modelTemplate, true);
/*      */           addBookSlotModel(multiPartGenerator, multiPartGenerator, multiPartGenerator, stateProperty, modelTemplate, false);
/*      */         });
/*      */   } private static final class BookSlotModelCacheKey extends Record {
/*      */     private final ModelTemplate template; private final String modelSuffix;
/* 3434 */     private BookSlotModelCacheKey(ModelTemplate template, String modelSuffix) { this.template = template; this.modelSuffix = modelSuffix; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/data/models/BlockModelGenerators$BookSlotModelCacheKey;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #3434	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/client/data/models/BlockModelGenerators$BookSlotModelCacheKey; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/data/models/BlockModelGenerators$BookSlotModelCacheKey;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #3434	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/client/data/models/BlockModelGenerators$BookSlotModelCacheKey; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/data/models/BlockModelGenerators$BookSlotModelCacheKey;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #3434	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/client/data/models/BlockModelGenerators$BookSlotModelCacheKey;
/* 3434 */       //   0	8	1	o	Ljava/lang/Object; } public ModelTemplate template() { return this.template; } public String modelSuffix() { return this.modelSuffix; }
/*      */   
/*      */   }
/* 3437 */   private static final Map<BookSlotModelCacheKey, Identifier> CHISELED_BOOKSHELF_SLOT_MODEL_CACHE = new HashMap<>();
/*      */   
/*      */   private void addBookSlotModel(MultiPartGenerator multiPartGenerator, Condition facingCondition, VariantMutator mutator, BooleanProperty stateProperty, ModelTemplate template, boolean isSlotOccupied) {
/* 3440 */     String suffix = isSlotOccupied ? "_occupied" : "_empty";
/* 3441 */     TextureMapping mapping = new TextureMapping().put(TextureSlot.TEXTURE, TextureMapping.getBlockTexture(Blocks.CHISELED_BOOKSHELF, suffix));
/* 3442 */     BookSlotModelCacheKey cacheKey = new BookSlotModelCacheKey(template, suffix);
/* 3443 */     MultiVariant model = plainVariant(CHISELED_BOOKSHELF_SLOT_MODEL_CACHE.computeIfAbsent(cacheKey, key -> template.createWithSuffix(Blocks.CHISELED_BOOKSHELF, template, suffix, this.modelOutput)));
/*      */     
/* 3445 */     multiPartGenerator.with((Condition)new CombinedCondition(CombinedCondition.Operation.AND, 
/*      */ 
/*      */           
/* 3448 */           List.of(facingCondition, 
/*      */             
/* 3450 */             condition().term((Property)stateProperty, isSlotOccupied).build())), 
/*      */ 
/*      */         
/* 3453 */         model.with(mutator));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createMagmaBlock() {
/* 3458 */     MultiVariant model = plainVariant(ModelTemplates.CUBE_ALL.create(Blocks.MAGMA_BLOCK, TextureMapping.cube(ModelLocationUtils.decorateBlockModelLocation("magma")), this.modelOutput));
/* 3459 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.MAGMA_BLOCK, model));
/*      */   }
/*      */   
/*      */   private void createShulkerBox(Block block, DyeColor color) {
/* 3463 */     createParticleOnlyBlock(block);
/*      */     
/* 3465 */     Item item = block.asItem();
/* 3466 */     Identifier baseModel = ModelTemplates.SHULKER_BOX_INVENTORY.create(item, TextureMapping.particle(block), this.modelOutput);
/* 3467 */     ItemModel.Unbaked itemModel = (color != null) ? 
/* 3468 */       ItemModelUtils.specialModel(baseModel, (SpecialModelRenderer.Unbaked)new ShulkerBoxSpecialRenderer.Unbaked(color)) : 
/* 3469 */       ItemModelUtils.specialModel(baseModel, (SpecialModelRenderer.Unbaked)new ShulkerBoxSpecialRenderer.Unbaked());
/*      */     
/* 3471 */     this.itemModelOutput.accept(item, itemModel);
/*      */   }
/*      */   
/*      */   private void createGrowingPlant(Block kelp, Block kelpPlant, PlantType type) {
/* 3475 */     createCrossBlock(kelp, type);
/* 3476 */     createCrossBlock(kelpPlant, type);
/*      */   }
/*      */   
/*      */   private void createInfestedStone() {
/* 3480 */     Identifier normalModel = ModelLocationUtils.getModelLocation(Blocks.STONE);
/* 3481 */     Variant normal = plainModel(normalModel);
/* 3482 */     Variant mirrored = plainModel(ModelLocationUtils.getModelLocation(Blocks.STONE, "_mirrored"));
/* 3483 */     this.blockStateOutput.accept(
/* 3484 */         MultiVariantGenerator.dispatch(Blocks.INFESTED_STONE, createRotatedVariants(normal, mirrored)));
/*      */     
/* 3486 */     registerSimpleItemModel(Blocks.INFESTED_STONE, normalModel);
/*      */   }
/*      */   
/*      */   private void createInfestedDeepslate() {
/* 3490 */     Identifier normalModel = ModelLocationUtils.getModelLocation(Blocks.DEEPSLATE);
/* 3491 */     Variant normal = plainModel(normalModel);
/* 3492 */     Variant mirrored = plainModel(ModelLocationUtils.getModelLocation(Blocks.DEEPSLATE, "_mirrored"));
/* 3493 */     this.blockStateOutput.accept(
/* 3494 */         MultiVariantGenerator.dispatch(Blocks.INFESTED_DEEPSLATE, createRotatedVariants(normal, mirrored)).with(createRotatedPillar()));
/*      */     
/* 3496 */     registerSimpleItemModel(Blocks.INFESTED_DEEPSLATE, normalModel);
/*      */   }
/*      */   
/*      */   private void createNetherRoots(Block roots, Block pottedRoots) {
/* 3500 */     createCrossBlockWithDefaultItem(roots, PlantType.NOT_TINTED);
/* 3501 */     TextureMapping textures = TextureMapping.plant(TextureMapping.getBlockTexture(roots, "_pot"));
/* 3502 */     MultiVariant model = plainVariant(PlantType.NOT_TINTED.getCrossPot().create(pottedRoots, textures, this.modelOutput));
/* 3503 */     this.blockStateOutput.accept(createSimpleBlock(pottedRoots, model));
/*      */   }
/*      */   
/*      */   private void createRespawnAnchor() {
/* 3507 */     Identifier bottom = TextureMapping.getBlockTexture(Blocks.RESPAWN_ANCHOR, "_bottom");
/* 3508 */     Identifier topOff = TextureMapping.getBlockTexture(Blocks.RESPAWN_ANCHOR, "_top_off");
/* 3509 */     Identifier topOn = TextureMapping.getBlockTexture(Blocks.RESPAWN_ANCHOR, "_top");
/* 3510 */     Identifier[] chargeLevelModels = new Identifier[5];
/* 3511 */     for (int i = 0; i < 5; i++) {
/* 3512 */       TextureMapping mapping = new TextureMapping()
/* 3513 */         .put(TextureSlot.BOTTOM, bottom)
/* 3514 */         .put(TextureSlot.TOP, (i == 0) ? topOff : topOn)
/* 3515 */         .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.RESPAWN_ANCHOR, "_side" + i));
/* 3516 */       chargeLevelModels[i] = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(Blocks.RESPAWN_ANCHOR, "_" + i, mapping, this.modelOutput);
/*      */     } 
/*      */     
/* 3519 */     this.blockStateOutput.accept(
/* 3520 */         MultiVariantGenerator.dispatch(Blocks.RESPAWN_ANCHOR)
/* 3521 */         .with(
/* 3522 */           PropertyDispatch.initial((Property)BlockStateProperties.RESPAWN_ANCHOR_CHARGES)
/* 3523 */           .generate(i -> plainVariant(chargeLevelModels[i]))));
/*      */ 
/*      */     
/* 3526 */     registerSimpleItemModel(Blocks.RESPAWN_ANCHOR, chargeLevelModels[0]);
/*      */   }
/*      */   
/*      */   private static VariantMutator applyRotation(FrontAndTop orientation) {
/* 3530 */     switch (orientation) { default: throw new MatchException(null, null);case DOWN_NORTH: case DOWN_SOUTH: case DOWN_WEST: case DOWN_EAST: case UP_NORTH: case UP_SOUTH: case UP_WEST: case UP_EAST: case NORTH_UP: case SOUTH_UP: case WEST_UP: case EAST_UP: break; }  return 
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
/* 3542 */       Y_ROT_90;
/*      */   }
/*      */ 
/*      */   
/*      */   private void createJigsaw() {
/* 3547 */     Identifier front = TextureMapping.getBlockTexture(Blocks.JIGSAW, "_top");
/* 3548 */     Identifier back = TextureMapping.getBlockTexture(Blocks.JIGSAW, "_bottom");
/* 3549 */     Identifier side = TextureMapping.getBlockTexture(Blocks.JIGSAW, "_side");
/* 3550 */     Identifier lock = TextureMapping.getBlockTexture(Blocks.JIGSAW, "_lock");
/* 3551 */     TextureMapping mapping = new TextureMapping()
/* 3552 */       .put(TextureSlot.DOWN, side)
/* 3553 */       .put(TextureSlot.WEST, side)
/* 3554 */       .put(TextureSlot.EAST, side)
/*      */       
/* 3556 */       .put(TextureSlot.PARTICLE, front)
/*      */       
/* 3558 */       .put(TextureSlot.NORTH, front)
/* 3559 */       .put(TextureSlot.SOUTH, back)
/*      */       
/* 3561 */       .put(TextureSlot.UP, lock);
/*      */     
/* 3563 */     this.blockStateOutput.accept(
/* 3564 */         MultiVariantGenerator.dispatch(Blocks.JIGSAW, plainVariant(ModelTemplates.CUBE_DIRECTIONAL.create(Blocks.JIGSAW, mapping, this.modelOutput)))
/* 3565 */         .with(
/* 3566 */           PropertyDispatch.modify((Property)BlockStateProperties.ORIENTATION)
/* 3567 */           .generate(BlockModelGenerators::applyRotation)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createPetrifiedOakSlab() {
/* 3573 */     Block fullBlock = Blocks.OAK_PLANKS;
/* 3574 */     MultiVariant fullBlockModel = plainVariant(ModelLocationUtils.getModelLocation(fullBlock));
/* 3575 */     TextureMapping fullBlockTextures = TextureMapping.cube(fullBlock);
/* 3576 */     Block petrifiedSlab = Blocks.PETRIFIED_OAK_SLAB;
/* 3577 */     MultiVariant petrifiedSlabBottom = plainVariant(ModelTemplates.SLAB_BOTTOM.create(petrifiedSlab, fullBlockTextures, this.modelOutput));
/* 3578 */     MultiVariant petrifiedSlabTop = plainVariant(ModelTemplates.SLAB_TOP.create(petrifiedSlab, fullBlockTextures, this.modelOutput));
/* 3579 */     this.blockStateOutput.accept(createSlab(petrifiedSlab, petrifiedSlabBottom, petrifiedSlabTop, fullBlockModel));
/*      */   }
/*      */   
/*      */   private void createHead(Block standAlone, Block wall, SkullBlock.Type skullType, Identifier itemBase) {
/* 3583 */     MultiVariant blockModel = plainVariant(ModelLocationUtils.decorateBlockModelLocation("skull"));
/* 3584 */     this.blockStateOutput.accept(createSimpleBlock(standAlone, blockModel));
/* 3585 */     this.blockStateOutput.accept(createSimpleBlock(wall, blockModel));
/* 3586 */     if (skullType == SkullBlock.Types.PLAYER) {
/* 3587 */       this.itemModelOutput.accept(standAlone.asItem(), ItemModelUtils.specialModel(itemBase, (SpecialModelRenderer.Unbaked)new PlayerHeadSpecialRenderer.Unbaked()));
/*      */     } else {
/* 3589 */       this.itemModelOutput.accept(standAlone.asItem(), ItemModelUtils.specialModel(itemBase, (SpecialModelRenderer.Unbaked)new SkullSpecialRenderer.Unbaked(skullType)));
/*      */     } 
/*      */   }
/*      */   
/*      */   private void createHeads() {
/* 3594 */     Identifier defaultHeadItemBase = ModelLocationUtils.decorateItemModelLocation("template_skull");
/* 3595 */     createHead(Blocks.CREEPER_HEAD, Blocks.CREEPER_WALL_HEAD, (SkullBlock.Type)SkullBlock.Types.CREEPER, defaultHeadItemBase);
/* 3596 */     createHead(Blocks.PLAYER_HEAD, Blocks.PLAYER_WALL_HEAD, (SkullBlock.Type)SkullBlock.Types.PLAYER, defaultHeadItemBase);
/* 3597 */     createHead(Blocks.ZOMBIE_HEAD, Blocks.ZOMBIE_WALL_HEAD, (SkullBlock.Type)SkullBlock.Types.ZOMBIE, defaultHeadItemBase);
/* 3598 */     createHead(Blocks.SKELETON_SKULL, Blocks.SKELETON_WALL_SKULL, (SkullBlock.Type)SkullBlock.Types.SKELETON, defaultHeadItemBase);
/* 3599 */     createHead(Blocks.WITHER_SKELETON_SKULL, Blocks.WITHER_SKELETON_WALL_SKULL, (SkullBlock.Type)SkullBlock.Types.WITHER_SKELETON, defaultHeadItemBase);
/* 3600 */     createHead(Blocks.PIGLIN_HEAD, Blocks.PIGLIN_WALL_HEAD, (SkullBlock.Type)SkullBlock.Types.PIGLIN, defaultHeadItemBase);
/*      */ 
/*      */     
/* 3603 */     createHead(Blocks.DRAGON_HEAD, Blocks.DRAGON_WALL_HEAD, (SkullBlock.Type)SkullBlock.Types.DRAGON, ModelLocationUtils.getModelLocation(Items.DRAGON_HEAD));
/*      */   }
/*      */   
/*      */   private void createCopperGolemStatues() {
/* 3607 */     createCopperGolemStatue(Blocks.COPPER_GOLEM_STATUE, Blocks.COPPER_BLOCK, WeatheringCopper.WeatherState.UNAFFECTED);
/* 3608 */     createCopperGolemStatue(Blocks.EXPOSED_COPPER_GOLEM_STATUE, Blocks.EXPOSED_COPPER, WeatheringCopper.WeatherState.EXPOSED);
/* 3609 */     createCopperGolemStatue(Blocks.WEATHERED_COPPER_GOLEM_STATUE, Blocks.WEATHERED_COPPER, WeatheringCopper.WeatherState.WEATHERED);
/* 3610 */     createCopperGolemStatue(Blocks.OXIDIZED_COPPER_GOLEM_STATUE, Blocks.OXIDIZED_COPPER, WeatheringCopper.WeatherState.OXIDIZED);
/*      */     
/* 3612 */     copyModel(Blocks.COPPER_GOLEM_STATUE, Blocks.WAXED_COPPER_GOLEM_STATUE);
/* 3613 */     copyModel(Blocks.EXPOSED_COPPER_GOLEM_STATUE, Blocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE);
/* 3614 */     copyModel(Blocks.WEATHERED_COPPER_GOLEM_STATUE, Blocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE);
/* 3615 */     copyModel(Blocks.OXIDIZED_COPPER_GOLEM_STATUE, Blocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE);
/*      */   }
/*      */   
/*      */   private void createCopperGolemStatue(Block block, Block particle, WeatheringCopper.WeatherState state) {
/* 3619 */     MultiVariant blockModel = plainVariant(ModelTemplates.PARTICLE_ONLY.create(block, TextureMapping.particle(TextureMapping.getBlockTexture(particle)), this.modelOutput));
/* 3620 */     Identifier itemBase = ModelLocationUtils.decorateItemModelLocation("template_copper_golem_statue");
/* 3621 */     this.blockStateOutput.accept(createSimpleBlock(block, blockModel));
/* 3622 */     this.itemModelOutput.accept(block.asItem(), ItemModelUtils.selectBlockItemProperty((Property)CopperGolemStatueBlock.POSE, 
/*      */           
/* 3624 */           ItemModelUtils.specialModel(itemBase, (SpecialModelRenderer.Unbaked)new CopperGolemStatueSpecialRenderer.Unbaked(state, CopperGolemStatueBlock.Pose.STANDING)), 
/* 3625 */           Map.of(CopperGolemStatueBlock.Pose.SITTING, 
/* 3626 */             ItemModelUtils.specialModel(itemBase, (SpecialModelRenderer.Unbaked)new CopperGolemStatueSpecialRenderer.Unbaked(state, CopperGolemStatueBlock.Pose.SITTING)), CopperGolemStatueBlock.Pose.STAR, 
/* 3627 */             ItemModelUtils.specialModel(itemBase, (SpecialModelRenderer.Unbaked)new CopperGolemStatueSpecialRenderer.Unbaked(state, CopperGolemStatueBlock.Pose.STAR)), CopperGolemStatueBlock.Pose.RUNNING, 
/* 3628 */             ItemModelUtils.specialModel(itemBase, (SpecialModelRenderer.Unbaked)new CopperGolemStatueSpecialRenderer.Unbaked(state, CopperGolemStatueBlock.Pose.RUNNING)))));
/*      */   }
/*      */   
/*      */   private void createBanner(Block standAlone, Block wall, DyeColor baseColor) {
/* 3632 */     MultiVariant blockModel = plainVariant(ModelLocationUtils.decorateBlockModelLocation("banner"));
/* 3633 */     Identifier itemModel = ModelLocationUtils.decorateItemModelLocation("template_banner");
/* 3634 */     this.blockStateOutput.accept(createSimpleBlock(standAlone, blockModel));
/* 3635 */     this.blockStateOutput.accept(createSimpleBlock(wall, blockModel));
/* 3636 */     Item item = standAlone.asItem();
/* 3637 */     this.itemModelOutput.accept(item, ItemModelUtils.specialModel(itemModel, (SpecialModelRenderer.Unbaked)new BannerSpecialRenderer.Unbaked(baseColor)));
/*      */   }
/*      */   
/*      */   private void createBanners() {
/* 3641 */     createBanner(Blocks.WHITE_BANNER, Blocks.WHITE_WALL_BANNER, DyeColor.WHITE);
/* 3642 */     createBanner(Blocks.ORANGE_BANNER, Blocks.ORANGE_WALL_BANNER, DyeColor.ORANGE);
/* 3643 */     createBanner(Blocks.MAGENTA_BANNER, Blocks.MAGENTA_WALL_BANNER, DyeColor.MAGENTA);
/* 3644 */     createBanner(Blocks.LIGHT_BLUE_BANNER, Blocks.LIGHT_BLUE_WALL_BANNER, DyeColor.LIGHT_BLUE);
/* 3645 */     createBanner(Blocks.YELLOW_BANNER, Blocks.YELLOW_WALL_BANNER, DyeColor.YELLOW);
/* 3646 */     createBanner(Blocks.LIME_BANNER, Blocks.LIME_WALL_BANNER, DyeColor.LIME);
/* 3647 */     createBanner(Blocks.PINK_BANNER, Blocks.PINK_WALL_BANNER, DyeColor.PINK);
/* 3648 */     createBanner(Blocks.GRAY_BANNER, Blocks.GRAY_WALL_BANNER, DyeColor.GRAY);
/* 3649 */     createBanner(Blocks.LIGHT_GRAY_BANNER, Blocks.LIGHT_GRAY_WALL_BANNER, DyeColor.LIGHT_GRAY);
/* 3650 */     createBanner(Blocks.CYAN_BANNER, Blocks.CYAN_WALL_BANNER, DyeColor.CYAN);
/* 3651 */     createBanner(Blocks.PURPLE_BANNER, Blocks.PURPLE_WALL_BANNER, DyeColor.PURPLE);
/* 3652 */     createBanner(Blocks.BLUE_BANNER, Blocks.BLUE_WALL_BANNER, DyeColor.BLUE);
/* 3653 */     createBanner(Blocks.BROWN_BANNER, Blocks.BROWN_WALL_BANNER, DyeColor.BROWN);
/* 3654 */     createBanner(Blocks.GREEN_BANNER, Blocks.GREEN_WALL_BANNER, DyeColor.GREEN);
/* 3655 */     createBanner(Blocks.RED_BANNER, Blocks.RED_WALL_BANNER, DyeColor.RED);
/* 3656 */     createBanner(Blocks.BLACK_BANNER, Blocks.BLACK_WALL_BANNER, DyeColor.BLACK);
/*      */   }
/*      */   
/*      */   private void createChest(Block block, Block particle, Identifier texture, boolean hasGiftVariant) {
/* 3660 */     createParticleOnlyBlock(block, particle);
/*      */     
/* 3662 */     Item chestItem = block.asItem();
/* 3663 */     Identifier itemModelBase = ModelTemplates.CHEST_INVENTORY.create(chestItem, TextureMapping.particle(particle), this.modelOutput);
/* 3664 */     ItemModel.Unbaked plainModel = ItemModelUtils.specialModel(itemModelBase, (SpecialModelRenderer.Unbaked)new ChestSpecialRenderer.Unbaked(texture));
/*      */     
/* 3666 */     if (hasGiftVariant) {
/* 3667 */       ItemModel.Unbaked giftModel = ItemModelUtils.specialModel(itemModelBase, (SpecialModelRenderer.Unbaked)new ChestSpecialRenderer.Unbaked(ChestSpecialRenderer.GIFT_CHEST_TEXTURE));
/* 3668 */       this.itemModelOutput.accept(chestItem, ItemModelUtils.isXmas(giftModel, plainModel));
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 3673 */       this.itemModelOutput.accept(chestItem, plainModel);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void createChests() {
/* 3678 */     createChest(Blocks.CHEST, Blocks.OAK_PLANKS, ChestSpecialRenderer.NORMAL_CHEST_TEXTURE, true);
/* 3679 */     createChest(Blocks.TRAPPED_CHEST, Blocks.OAK_PLANKS, ChestSpecialRenderer.TRAPPED_CHEST_TEXTURE, true);
/* 3680 */     createChest(Blocks.ENDER_CHEST, Blocks.OBSIDIAN, ChestSpecialRenderer.ENDER_CHEST_TEXTURE, false);
/*      */   }
/*      */   
/*      */   private void createCopperChests() {
/* 3684 */     createChest(Blocks.COPPER_CHEST, Blocks.COPPER_BLOCK, ChestSpecialRenderer.COPPER_CHEST_TEXTURE, false);
/* 3685 */     createChest(Blocks.EXPOSED_COPPER_CHEST, Blocks.EXPOSED_COPPER, ChestSpecialRenderer.EXPOSED_COPPER_CHEST_TEXTURE, false);
/* 3686 */     createChest(Blocks.WEATHERED_COPPER_CHEST, Blocks.WEATHERED_COPPER, ChestSpecialRenderer.WEATHERED_COPPER_CHEST_TEXTURE, false);
/* 3687 */     createChest(Blocks.OXIDIZED_COPPER_CHEST, Blocks.OXIDIZED_COPPER, ChestSpecialRenderer.OXIDIZED_COPPER_CHEST_TEXTURE, false);
/*      */     
/* 3689 */     copyModel(Blocks.COPPER_CHEST, Blocks.WAXED_COPPER_CHEST);
/* 3690 */     copyModel(Blocks.EXPOSED_COPPER_CHEST, Blocks.WAXED_EXPOSED_COPPER_CHEST);
/* 3691 */     copyModel(Blocks.WEATHERED_COPPER_CHEST, Blocks.WAXED_WEATHERED_COPPER_CHEST);
/* 3692 */     copyModel(Blocks.OXIDIZED_COPPER_CHEST, Blocks.WAXED_OXIDIZED_COPPER_CHEST);
/*      */   }
/*      */   
/*      */   private void createBed(Block bed, Block itemParticle, DyeColor dyeColor) {
/* 3696 */     MultiVariant blockModel = plainVariant(ModelLocationUtils.decorateBlockModelLocation("bed"));
/* 3697 */     this.blockStateOutput.accept(createSimpleBlock(bed, blockModel));
/* 3698 */     Item bedItem = bed.asItem();
/*      */     
/* 3700 */     Identifier baseModel = ModelTemplates.BED_INVENTORY.create(ModelLocationUtils.getModelLocation(bedItem), TextureMapping.particle(itemParticle), this.modelOutput);
/* 3701 */     this.itemModelOutput.accept(bedItem, ItemModelUtils.specialModel(baseModel, (SpecialModelRenderer.Unbaked)new BedSpecialRenderer.Unbaked(dyeColor)));
/*      */   }
/*      */   
/*      */   private void createBeds() {
/* 3705 */     createBed(Blocks.WHITE_BED, Blocks.WHITE_WOOL, DyeColor.WHITE);
/* 3706 */     createBed(Blocks.ORANGE_BED, Blocks.ORANGE_WOOL, DyeColor.ORANGE);
/* 3707 */     createBed(Blocks.MAGENTA_BED, Blocks.MAGENTA_WOOL, DyeColor.MAGENTA);
/* 3708 */     createBed(Blocks.LIGHT_BLUE_BED, Blocks.LIGHT_BLUE_WOOL, DyeColor.LIGHT_BLUE);
/* 3709 */     createBed(Blocks.YELLOW_BED, Blocks.YELLOW_WOOL, DyeColor.YELLOW);
/* 3710 */     createBed(Blocks.LIME_BED, Blocks.LIME_WOOL, DyeColor.LIME);
/* 3711 */     createBed(Blocks.PINK_BED, Blocks.PINK_WOOL, DyeColor.PINK);
/* 3712 */     createBed(Blocks.GRAY_BED, Blocks.GRAY_WOOL, DyeColor.GRAY);
/* 3713 */     createBed(Blocks.LIGHT_GRAY_BED, Blocks.LIGHT_GRAY_WOOL, DyeColor.LIGHT_GRAY);
/* 3714 */     createBed(Blocks.CYAN_BED, Blocks.CYAN_WOOL, DyeColor.CYAN);
/* 3715 */     createBed(Blocks.PURPLE_BED, Blocks.PURPLE_WOOL, DyeColor.PURPLE);
/* 3716 */     createBed(Blocks.BLUE_BED, Blocks.BLUE_WOOL, DyeColor.BLUE);
/* 3717 */     createBed(Blocks.BROWN_BED, Blocks.BROWN_WOOL, DyeColor.BROWN);
/* 3718 */     createBed(Blocks.GREEN_BED, Blocks.GREEN_WOOL, DyeColor.GREEN);
/* 3719 */     createBed(Blocks.RED_BED, Blocks.RED_WOOL, DyeColor.RED);
/* 3720 */     createBed(Blocks.BLACK_BED, Blocks.BLACK_WOOL, DyeColor.BLACK);
/*      */   }
/*      */   
/*      */   private void generateSimpleSpecialItemModel(Block block, SpecialModelRenderer.Unbaked specialModel) {
/* 3724 */     Item item = block.asItem();
/* 3725 */     Identifier harcodedModelBase = ModelLocationUtils.getModelLocation(item);
/* 3726 */     this.itemModelOutput.accept(item, ItemModelUtils.specialModel(harcodedModelBase, specialModel));
/*      */   }
/*      */   
/*      */   public void run() {
/* 3730 */     BlockFamilies.getAllFamilies().filter(BlockFamily::shouldGenerateModel).forEach(blockFamily -> family(blockFamily.getBaseBlock()).generateFor(blockFamily));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3735 */     family(Blocks.CUT_COPPER)
/* 3736 */       .generateFor(BlockFamilies.CUT_COPPER)
/* 3737 */       .donateModelTo(Blocks.CUT_COPPER, Blocks.WAXED_CUT_COPPER)
/* 3738 */       .donateModelTo(Blocks.CHISELED_COPPER, Blocks.WAXED_CHISELED_COPPER)
/* 3739 */       .generateFor(BlockFamilies.WAXED_CUT_COPPER);
/*      */     
/* 3741 */     family(Blocks.EXPOSED_CUT_COPPER)
/* 3742 */       .generateFor(BlockFamilies.EXPOSED_CUT_COPPER)
/* 3743 */       .donateModelTo(Blocks.EXPOSED_CUT_COPPER, Blocks.WAXED_EXPOSED_CUT_COPPER)
/* 3744 */       .donateModelTo(Blocks.EXPOSED_CHISELED_COPPER, Blocks.WAXED_EXPOSED_CHISELED_COPPER)
/* 3745 */       .generateFor(BlockFamilies.WAXED_EXPOSED_CUT_COPPER);
/*      */     
/* 3747 */     family(Blocks.WEATHERED_CUT_COPPER)
/* 3748 */       .generateFor(BlockFamilies.WEATHERED_CUT_COPPER)
/* 3749 */       .donateModelTo(Blocks.WEATHERED_CUT_COPPER, Blocks.WAXED_WEATHERED_CUT_COPPER)
/* 3750 */       .donateModelTo(Blocks.WEATHERED_CHISELED_COPPER, Blocks.WAXED_WEATHERED_CHISELED_COPPER)
/* 3751 */       .generateFor(BlockFamilies.WAXED_WEATHERED_CUT_COPPER);
/*      */     
/* 3753 */     family(Blocks.OXIDIZED_CUT_COPPER)
/* 3754 */       .generateFor(BlockFamilies.OXIDIZED_CUT_COPPER)
/* 3755 */       .donateModelTo(Blocks.OXIDIZED_CUT_COPPER, Blocks.WAXED_OXIDIZED_CUT_COPPER)
/* 3756 */       .donateModelTo(Blocks.OXIDIZED_CHISELED_COPPER, Blocks.WAXED_OXIDIZED_CHISELED_COPPER)
/* 3757 */       .generateFor(BlockFamilies.WAXED_OXIDIZED_CUT_COPPER);
/*      */     
/* 3759 */     createCopperBulb(Blocks.COPPER_BULB);
/* 3760 */     createCopperBulb(Blocks.EXPOSED_COPPER_BULB);
/* 3761 */     createCopperBulb(Blocks.WEATHERED_COPPER_BULB);
/* 3762 */     createCopperBulb(Blocks.OXIDIZED_COPPER_BULB);
/* 3763 */     copyCopperBulbModel(Blocks.COPPER_BULB, Blocks.WAXED_COPPER_BULB);
/* 3764 */     copyCopperBulbModel(Blocks.EXPOSED_COPPER_BULB, Blocks.WAXED_EXPOSED_COPPER_BULB);
/* 3765 */     copyCopperBulbModel(Blocks.WEATHERED_COPPER_BULB, Blocks.WAXED_WEATHERED_COPPER_BULB);
/* 3766 */     copyCopperBulbModel(Blocks.OXIDIZED_COPPER_BULB, Blocks.WAXED_OXIDIZED_COPPER_BULB);
/*      */     
/* 3768 */     createNonTemplateModelBlock(Blocks.AIR);
/* 3769 */     createNonTemplateModelBlock(Blocks.CAVE_AIR, Blocks.AIR);
/* 3770 */     createNonTemplateModelBlock(Blocks.VOID_AIR, Blocks.AIR);
/* 3771 */     createNonTemplateModelBlock(Blocks.BEACON);
/* 3772 */     createNonTemplateModelBlock(Blocks.CACTUS);
/* 3773 */     createNonTemplateModelBlock(Blocks.BUBBLE_COLUMN, Blocks.WATER);
/* 3774 */     createNonTemplateModelBlock(Blocks.DRAGON_EGG);
/* 3775 */     createNonTemplateModelBlock(Blocks.DRIED_KELP_BLOCK);
/* 3776 */     createNonTemplateModelBlock(Blocks.ENCHANTING_TABLE);
/* 3777 */     createNonTemplateModelBlock(Blocks.FLOWER_POT);
/* 3778 */     registerSimpleFlatItemModel(Items.FLOWER_POT);
/* 3779 */     createNonTemplateModelBlock(Blocks.HONEY_BLOCK);
/* 3780 */     createNonTemplateModelBlock(Blocks.WATER);
/* 3781 */     createNonTemplateModelBlock(Blocks.LAVA);
/* 3782 */     createNonTemplateModelBlock(Blocks.SLIME_BLOCK);
/* 3783 */     registerSimpleFlatItemModel(Items.IRON_CHAIN);
/*      */     
/* 3785 */     Items.COPPER_CHAIN.waxedMapping().forEach(this::createCopperChainItem);
/*      */     
/* 3787 */     createCandleAndCandleCake(Blocks.WHITE_CANDLE, Blocks.WHITE_CANDLE_CAKE);
/* 3788 */     createCandleAndCandleCake(Blocks.ORANGE_CANDLE, Blocks.ORANGE_CANDLE_CAKE);
/* 3789 */     createCandleAndCandleCake(Blocks.MAGENTA_CANDLE, Blocks.MAGENTA_CANDLE_CAKE);
/* 3790 */     createCandleAndCandleCake(Blocks.LIGHT_BLUE_CANDLE, Blocks.LIGHT_BLUE_CANDLE_CAKE);
/* 3791 */     createCandleAndCandleCake(Blocks.YELLOW_CANDLE, Blocks.YELLOW_CANDLE_CAKE);
/* 3792 */     createCandleAndCandleCake(Blocks.LIME_CANDLE, Blocks.LIME_CANDLE_CAKE);
/* 3793 */     createCandleAndCandleCake(Blocks.PINK_CANDLE, Blocks.PINK_CANDLE_CAKE);
/* 3794 */     createCandleAndCandleCake(Blocks.GRAY_CANDLE, Blocks.GRAY_CANDLE_CAKE);
/* 3795 */     createCandleAndCandleCake(Blocks.LIGHT_GRAY_CANDLE, Blocks.LIGHT_GRAY_CANDLE_CAKE);
/* 3796 */     createCandleAndCandleCake(Blocks.CYAN_CANDLE, Blocks.CYAN_CANDLE_CAKE);
/* 3797 */     createCandleAndCandleCake(Blocks.PURPLE_CANDLE, Blocks.PURPLE_CANDLE_CAKE);
/* 3798 */     createCandleAndCandleCake(Blocks.BLUE_CANDLE, Blocks.BLUE_CANDLE_CAKE);
/* 3799 */     createCandleAndCandleCake(Blocks.BROWN_CANDLE, Blocks.BROWN_CANDLE_CAKE);
/* 3800 */     createCandleAndCandleCake(Blocks.GREEN_CANDLE, Blocks.GREEN_CANDLE_CAKE);
/* 3801 */     createCandleAndCandleCake(Blocks.RED_CANDLE, Blocks.RED_CANDLE_CAKE);
/* 3802 */     createCandleAndCandleCake(Blocks.BLACK_CANDLE, Blocks.BLACK_CANDLE_CAKE);
/* 3803 */     createCandleAndCandleCake(Blocks.CANDLE, Blocks.CANDLE_CAKE);
/*      */     
/* 3805 */     createNonTemplateModelBlock(Blocks.POTTED_BAMBOO);
/* 3806 */     createNonTemplateModelBlock(Blocks.POTTED_CACTUS);
/* 3807 */     createNonTemplateModelBlock(Blocks.POWDER_SNOW);
/* 3808 */     createNonTemplateModelBlock(Blocks.SPORE_BLOSSOM);
/* 3809 */     createAzalea(Blocks.AZALEA);
/* 3810 */     createAzalea(Blocks.FLOWERING_AZALEA);
/* 3811 */     createPottedAzalea(Blocks.POTTED_AZALEA);
/* 3812 */     createPottedAzalea(Blocks.POTTED_FLOWERING_AZALEA);
/* 3813 */     createCaveVines();
/* 3814 */     createFullAndCarpetBlocks(Blocks.MOSS_BLOCK, Blocks.MOSS_CARPET);
/* 3815 */     createMossyCarpet(Blocks.PALE_MOSS_CARPET);
/* 3816 */     createHangingMoss(Blocks.PALE_HANGING_MOSS);
/* 3817 */     createTrivialCube(Blocks.PALE_MOSS_BLOCK);
/* 3818 */     createFlowerBed(Blocks.PINK_PETALS);
/* 3819 */     createFlowerBed(Blocks.WILDFLOWERS);
/* 3820 */     createLeafLitter(Blocks.LEAF_LITTER);
/* 3821 */     createCrossBlock(Blocks.FIREFLY_BUSH, PlantType.EMISSIVE_NOT_TINTED);
/* 3822 */     registerSimpleFlatItemModel(Items.FIREFLY_BUSH);
/*      */     
/* 3824 */     createAirLikeBlock(Blocks.BARRIER, Items.BARRIER);
/* 3825 */     registerSimpleFlatItemModel(Items.BARRIER);
/* 3826 */     createLightBlock();
/* 3827 */     createAirLikeBlock(Blocks.STRUCTURE_VOID, Items.STRUCTURE_VOID);
/* 3828 */     registerSimpleFlatItemModel(Items.STRUCTURE_VOID);
/* 3829 */     createAirLikeBlock(Blocks.MOVING_PISTON, TextureMapping.getBlockTexture(Blocks.PISTON, "_side"));
/*      */     
/* 3831 */     createTrivialCube(Blocks.COAL_ORE);
/* 3832 */     createTrivialCube(Blocks.DEEPSLATE_COAL_ORE);
/* 3833 */     createTrivialCube(Blocks.COAL_BLOCK);
/* 3834 */     createTrivialCube(Blocks.DIAMOND_ORE);
/* 3835 */     createTrivialCube(Blocks.DEEPSLATE_DIAMOND_ORE);
/* 3836 */     createTrivialCube(Blocks.DIAMOND_BLOCK);
/* 3837 */     createTrivialCube(Blocks.EMERALD_ORE);
/* 3838 */     createTrivialCube(Blocks.DEEPSLATE_EMERALD_ORE);
/* 3839 */     createTrivialCube(Blocks.EMERALD_BLOCK);
/* 3840 */     createTrivialCube(Blocks.GOLD_ORE);
/* 3841 */     createTrivialCube(Blocks.NETHER_GOLD_ORE);
/* 3842 */     createTrivialCube(Blocks.DEEPSLATE_GOLD_ORE);
/* 3843 */     createTrivialCube(Blocks.GOLD_BLOCK);
/* 3844 */     createTrivialCube(Blocks.IRON_ORE);
/* 3845 */     createTrivialCube(Blocks.DEEPSLATE_IRON_ORE);
/* 3846 */     createTrivialCube(Blocks.IRON_BLOCK);
/* 3847 */     createTrivialBlock(Blocks.ANCIENT_DEBRIS, TexturedModel.COLUMN);
/* 3848 */     createTrivialCube(Blocks.NETHERITE_BLOCK);
/* 3849 */     createTrivialCube(Blocks.LAPIS_ORE);
/* 3850 */     createTrivialCube(Blocks.DEEPSLATE_LAPIS_ORE);
/* 3851 */     createTrivialCube(Blocks.LAPIS_BLOCK);
/* 3852 */     createTrivialCube(Blocks.RESIN_BLOCK);
/* 3853 */     createTrivialCube(Blocks.NETHER_QUARTZ_ORE);
/* 3854 */     createTrivialCube(Blocks.REDSTONE_ORE);
/* 3855 */     createTrivialCube(Blocks.DEEPSLATE_REDSTONE_ORE);
/* 3856 */     createTrivialCube(Blocks.REDSTONE_BLOCK);
/* 3857 */     createTrivialCube(Blocks.GILDED_BLACKSTONE);
/*      */     
/* 3859 */     createTrivialCube(Blocks.BLUE_ICE);
/* 3860 */     createTrivialCube(Blocks.CLAY);
/* 3861 */     createTrivialCube(Blocks.COARSE_DIRT);
/* 3862 */     createTrivialCube(Blocks.CRYING_OBSIDIAN);
/* 3863 */     createTrivialCube(Blocks.END_STONE);
/* 3864 */     createTrivialCube(Blocks.GLOWSTONE);
/* 3865 */     createTrivialCube(Blocks.GRAVEL);
/* 3866 */     createTrivialCube(Blocks.HONEYCOMB_BLOCK);
/* 3867 */     createTrivialCube(Blocks.ICE);
/* 3868 */     createTrivialBlock(Blocks.JUKEBOX, TexturedModel.CUBE_TOP);
/* 3869 */     createTrivialBlock(Blocks.LODESTONE, TexturedModel.COLUMN);
/* 3870 */     createTrivialBlock(Blocks.MELON, TexturedModel.COLUMN);
/* 3871 */     createNonTemplateModelBlock(Blocks.MANGROVE_ROOTS);
/* 3872 */     createNonTemplateModelBlock(Blocks.POTTED_MANGROVE_PROPAGULE);
/* 3873 */     createTrivialCube(Blocks.NETHER_WART_BLOCK);
/* 3874 */     createTrivialCube(Blocks.NOTE_BLOCK);
/* 3875 */     createTrivialCube(Blocks.PACKED_ICE);
/* 3876 */     createTrivialCube(Blocks.OBSIDIAN);
/* 3877 */     createTrivialCube(Blocks.QUARTZ_BRICKS);
/* 3878 */     createTrivialCube(Blocks.SEA_LANTERN);
/* 3879 */     createTrivialCube(Blocks.SHROOMLIGHT);
/* 3880 */     createTrivialCube(Blocks.SOUL_SAND);
/* 3881 */     createTrivialCube(Blocks.SOUL_SOIL);
/* 3882 */     createTrivialBlock(Blocks.SPAWNER, TexturedModel.CUBE_INNER_FACES);
/* 3883 */     createCreakingHeart(Blocks.CREAKING_HEART);
/* 3884 */     createTrivialCube(Blocks.SPONGE);
/* 3885 */     createTrivialBlock(Blocks.SEAGRASS, TexturedModel.SEAGRASS);
/* 3886 */     registerSimpleFlatItemModel(Items.SEAGRASS);
/* 3887 */     createTrivialBlock(Blocks.TNT, TexturedModel.CUBE_TOP_BOTTOM);
/* 3888 */     createTrivialBlock(Blocks.TARGET, TexturedModel.COLUMN);
/* 3889 */     createTrivialCube(Blocks.WARPED_WART_BLOCK);
/* 3890 */     createTrivialCube(Blocks.WET_SPONGE);
/* 3891 */     createTrivialCube(Blocks.AMETHYST_BLOCK);
/* 3892 */     createTrivialCube(Blocks.BUDDING_AMETHYST);
/* 3893 */     createTrivialCube(Blocks.CALCITE);
/* 3894 */     createTrivialCube(Blocks.DRIPSTONE_BLOCK);
/* 3895 */     createTrivialCube(Blocks.RAW_IRON_BLOCK);
/* 3896 */     createTrivialCube(Blocks.RAW_COPPER_BLOCK);
/* 3897 */     createTrivialCube(Blocks.RAW_GOLD_BLOCK);
/* 3898 */     createRotatedMirroredVariantBlock(Blocks.SCULK);
/* 3899 */     createNonTemplateModelBlock(Blocks.HEAVY_CORE);
/*      */     
/* 3901 */     createPetrifiedOakSlab();
/*      */     
/* 3903 */     createTrivialCube(Blocks.COPPER_ORE);
/* 3904 */     createTrivialCube(Blocks.DEEPSLATE_COPPER_ORE);
/* 3905 */     createTrivialCube(Blocks.COPPER_BLOCK);
/* 3906 */     createTrivialCube(Blocks.EXPOSED_COPPER);
/* 3907 */     createTrivialCube(Blocks.WEATHERED_COPPER);
/* 3908 */     createTrivialCube(Blocks.OXIDIZED_COPPER);
/* 3909 */     copyModel(Blocks.COPPER_BLOCK, Blocks.WAXED_COPPER_BLOCK);
/* 3910 */     copyModel(Blocks.EXPOSED_COPPER, Blocks.WAXED_EXPOSED_COPPER);
/* 3911 */     copyModel(Blocks.WEATHERED_COPPER, Blocks.WAXED_WEATHERED_COPPER);
/* 3912 */     copyModel(Blocks.OXIDIZED_COPPER, Blocks.WAXED_OXIDIZED_COPPER);
/* 3913 */     createDoor(Blocks.COPPER_DOOR);
/* 3914 */     createDoor(Blocks.EXPOSED_COPPER_DOOR);
/* 3915 */     createDoor(Blocks.WEATHERED_COPPER_DOOR);
/* 3916 */     createDoor(Blocks.OXIDIZED_COPPER_DOOR);
/* 3917 */     copyDoorModel(Blocks.COPPER_DOOR, Blocks.WAXED_COPPER_DOOR);
/* 3918 */     copyDoorModel(Blocks.EXPOSED_COPPER_DOOR, Blocks.WAXED_EXPOSED_COPPER_DOOR);
/* 3919 */     copyDoorModel(Blocks.WEATHERED_COPPER_DOOR, Blocks.WAXED_WEATHERED_COPPER_DOOR);
/* 3920 */     copyDoorModel(Blocks.OXIDIZED_COPPER_DOOR, Blocks.WAXED_OXIDIZED_COPPER_DOOR);
/* 3921 */     createTrapdoor(Blocks.COPPER_TRAPDOOR);
/* 3922 */     createTrapdoor(Blocks.EXPOSED_COPPER_TRAPDOOR);
/* 3923 */     createTrapdoor(Blocks.WEATHERED_COPPER_TRAPDOOR);
/* 3924 */     createTrapdoor(Blocks.OXIDIZED_COPPER_TRAPDOOR);
/* 3925 */     copyTrapdoorModel(Blocks.COPPER_TRAPDOOR, Blocks.WAXED_COPPER_TRAPDOOR);
/* 3926 */     copyTrapdoorModel(Blocks.EXPOSED_COPPER_TRAPDOOR, Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR);
/* 3927 */     copyTrapdoorModel(Blocks.WEATHERED_COPPER_TRAPDOOR, Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR);
/* 3928 */     copyTrapdoorModel(Blocks.OXIDIZED_COPPER_TRAPDOOR, Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR);
/* 3929 */     createTrivialCube(Blocks.COPPER_GRATE);
/* 3930 */     createTrivialCube(Blocks.EXPOSED_COPPER_GRATE);
/* 3931 */     createTrivialCube(Blocks.WEATHERED_COPPER_GRATE);
/* 3932 */     createTrivialCube(Blocks.OXIDIZED_COPPER_GRATE);
/* 3933 */     copyModel(Blocks.COPPER_GRATE, Blocks.WAXED_COPPER_GRATE);
/* 3934 */     copyModel(Blocks.EXPOSED_COPPER_GRATE, Blocks.WAXED_EXPOSED_COPPER_GRATE);
/* 3935 */     copyModel(Blocks.WEATHERED_COPPER_GRATE, Blocks.WAXED_WEATHERED_COPPER_GRATE);
/* 3936 */     copyModel(Blocks.OXIDIZED_COPPER_GRATE, Blocks.WAXED_OXIDIZED_COPPER_GRATE);
/* 3937 */     createLightningRod(Blocks.LIGHTNING_ROD, Blocks.WAXED_LIGHTNING_ROD);
/* 3938 */     createLightningRod(Blocks.EXPOSED_LIGHTNING_ROD, Blocks.WAXED_EXPOSED_LIGHTNING_ROD);
/* 3939 */     createLightningRod(Blocks.WEATHERED_LIGHTNING_ROD, Blocks.WAXED_WEATHERED_LIGHTNING_ROD);
/* 3940 */     createLightningRod(Blocks.OXIDIZED_LIGHTNING_ROD, Blocks.WAXED_OXIDIZED_LIGHTNING_ROD);
/*      */     
/* 3942 */     createWeightedPressurePlate(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, Blocks.GOLD_BLOCK);
/* 3943 */     createWeightedPressurePlate(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, Blocks.IRON_BLOCK);
/*      */     
/* 3945 */     createShelf(Blocks.ACACIA_SHELF, Blocks.STRIPPED_ACACIA_LOG);
/* 3946 */     createShelf(Blocks.BAMBOO_SHELF, Blocks.STRIPPED_BAMBOO_BLOCK);
/* 3947 */     createShelf(Blocks.BIRCH_SHELF, Blocks.STRIPPED_BIRCH_LOG);
/* 3948 */     createShelf(Blocks.CHERRY_SHELF, Blocks.STRIPPED_CHERRY_LOG);
/* 3949 */     createShelf(Blocks.CRIMSON_SHELF, Blocks.STRIPPED_CRIMSON_STEM);
/* 3950 */     createShelf(Blocks.DARK_OAK_SHELF, Blocks.STRIPPED_DARK_OAK_LOG);
/* 3951 */     createShelf(Blocks.JUNGLE_SHELF, Blocks.STRIPPED_JUNGLE_LOG);
/* 3952 */     createShelf(Blocks.MANGROVE_SHELF, Blocks.STRIPPED_MANGROVE_LOG);
/* 3953 */     createShelf(Blocks.OAK_SHELF, Blocks.STRIPPED_OAK_LOG);
/* 3954 */     createShelf(Blocks.PALE_OAK_SHELF, Blocks.STRIPPED_PALE_OAK_LOG);
/* 3955 */     createShelf(Blocks.SPRUCE_SHELF, Blocks.STRIPPED_SPRUCE_LOG);
/* 3956 */     createShelf(Blocks.WARPED_SHELF, Blocks.STRIPPED_WARPED_STEM);
/*      */     
/* 3958 */     createAmethystClusters();
/* 3959 */     createBookshelf();
/* 3960 */     createChiseledBookshelf();
/* 3961 */     createBrewingStand();
/* 3962 */     createCakeBlock();
/* 3963 */     createCampfires(new Block[] { Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE });
/* 3964 */     createCartographyTable();
/* 3965 */     createCauldrons();
/* 3966 */     createChorusFlower();
/* 3967 */     createChorusPlant();
/* 3968 */     createComposter();
/* 3969 */     createDaylightDetector();
/* 3970 */     createEndPortalFrame();
/* 3971 */     createRotatableColumn(Blocks.END_ROD);
/* 3972 */     createFarmland();
/* 3973 */     createFire();
/* 3974 */     createSoulFire();
/* 3975 */     createFrostedIce();
/* 3976 */     createGrassBlocks();
/* 3977 */     createCocoa();
/* 3978 */     createDirtPath();
/* 3979 */     createGrindstone();
/* 3980 */     createHopper();
/*      */     
/* 3982 */     createBarsAndItem(Blocks.IRON_BARS);
/* 3983 */     Blocks.COPPER_BARS.waxedMapping().forEach(this::createBarsAndItem);
/*      */     
/* 3985 */     createLever();
/* 3986 */     createLilyPad();
/* 3987 */     createNetherPortalBlock();
/* 3988 */     createNetherrack();
/* 3989 */     createObserver();
/* 3990 */     createPistons();
/* 3991 */     createPistonHeads();
/* 3992 */     createScaffolding();
/* 3993 */     createRedstoneTorch();
/* 3994 */     createRedstoneLamp();
/* 3995 */     createRepeater();
/* 3996 */     createSeaPickle();
/* 3997 */     createSmithingTable();
/* 3998 */     createSnowBlocks();
/* 3999 */     createStonecutter();
/* 4000 */     createStructureBlock();
/* 4001 */     createSweetBerryBush();
/* 4002 */     createTestBlock();
/* 4003 */     createTrivialCube(Blocks.TEST_INSTANCE_BLOCK);
/* 4004 */     createTripwire();
/* 4005 */     createTripwireHook();
/* 4006 */     createTurtleEgg();
/* 4007 */     createSnifferEgg();
/* 4008 */     createDriedGhastBlock();
/* 4009 */     createVine();
/* 4010 */     createMultiface(Blocks.GLOW_LICHEN);
/* 4011 */     createMultiface(Blocks.SCULK_VEIN);
/* 4012 */     createMultiface(Blocks.RESIN_CLUMP, Items.RESIN_CLUMP);
/* 4013 */     createMagmaBlock();
/* 4014 */     createJigsaw();
/* 4015 */     createSculkSensor();
/* 4016 */     createCalibratedSculkSensor();
/* 4017 */     createSculkShrieker();
/* 4018 */     createFrogspawnBlock();
/* 4019 */     createMangrovePropagule();
/* 4020 */     createMuddyMangroveRoots();
/* 4021 */     createTrialSpawner();
/* 4022 */     createVault();
/*      */     
/* 4024 */     createNonTemplateHorizontalBlock(Blocks.LADDER);
/* 4025 */     registerSimpleFlatItemModel(Blocks.LADDER);
/* 4026 */     createNonTemplateHorizontalBlock(Blocks.LECTERN);
/*      */     
/* 4028 */     createBigDripLeafBlock();
/* 4029 */     createNonTemplateHorizontalBlock(Blocks.BIG_DRIPLEAF_STEM);
/*      */     
/* 4031 */     createNormalTorch(Blocks.TORCH, Blocks.WALL_TORCH);
/* 4032 */     createNormalTorch(Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH);
/* 4033 */     createNormalTorch(Blocks.COPPER_TORCH, Blocks.COPPER_WALL_TORCH);
/*      */     
/* 4035 */     createCraftingTableLike(Blocks.CRAFTING_TABLE, Blocks.OAK_PLANKS, TextureMapping::craftingTable);
/* 4036 */     createCraftingTableLike(Blocks.FLETCHING_TABLE, Blocks.BIRCH_PLANKS, TextureMapping::fletchingTable);
/*      */     
/* 4038 */     createNyliumBlock(Blocks.CRIMSON_NYLIUM);
/* 4039 */     createNyliumBlock(Blocks.WARPED_NYLIUM);
/*      */     
/* 4041 */     createDispenserBlock(Blocks.DISPENSER);
/* 4042 */     createDispenserBlock(Blocks.DROPPER);
/* 4043 */     createCrafterBlock();
/*      */     
/* 4045 */     createLantern(Blocks.LANTERN);
/* 4046 */     createLantern(Blocks.SOUL_LANTERN);
/*      */     
/* 4048 */     Blocks.COPPER_LANTERN.waxedMapping().forEach(this::createCopperLantern);
/*      */     
/* 4050 */     createAxisAlignedPillarBlockCustomModel(Blocks.IRON_CHAIN, plainVariant(TexturedModel.CHAIN.create(Blocks.IRON_CHAIN, this.modelOutput)));
/*      */     
/* 4052 */     Blocks.COPPER_CHAIN.waxedMapping().forEach(this::createCopperChain);
/*      */     
/* 4054 */     createAxisAlignedPillarBlock(Blocks.BASALT, TexturedModel.COLUMN);
/* 4055 */     createAxisAlignedPillarBlock(Blocks.POLISHED_BASALT, TexturedModel.COLUMN);
/* 4056 */     createTrivialCube(Blocks.SMOOTH_BASALT);
/* 4057 */     createAxisAlignedPillarBlock(Blocks.BONE_BLOCK, TexturedModel.COLUMN);
/* 4058 */     createRotatedVariantBlock(Blocks.DIRT);
/* 4059 */     createRotatedVariantBlock(Blocks.ROOTED_DIRT);
/* 4060 */     createRotatedVariantBlock(Blocks.SAND);
/* 4061 */     createBrushableBlock(Blocks.SUSPICIOUS_SAND);
/* 4062 */     createBrushableBlock(Blocks.SUSPICIOUS_GRAVEL);
/* 4063 */     createRotatedVariantBlock(Blocks.RED_SAND);
/* 4064 */     createRotatedMirroredVariantBlock(Blocks.BEDROCK);
/* 4065 */     createTrivialBlock(Blocks.REINFORCED_DEEPSLATE, TexturedModel.CUBE_TOP_BOTTOM);
/* 4066 */     createRotatedPillarWithHorizontalVariant(Blocks.HAY_BLOCK, TexturedModel.COLUMN, TexturedModel.COLUMN_HORIZONTAL);
/* 4067 */     createRotatedPillarWithHorizontalVariant(Blocks.PURPUR_PILLAR, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT);
/* 4068 */     createRotatedPillarWithHorizontalVariant(Blocks.QUARTZ_PILLAR, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT);
/* 4069 */     createRotatedPillarWithHorizontalVariant(Blocks.OCHRE_FROGLIGHT, TexturedModel.COLUMN, TexturedModel.COLUMN_HORIZONTAL);
/* 4070 */     createRotatedPillarWithHorizontalVariant(Blocks.VERDANT_FROGLIGHT, TexturedModel.COLUMN, TexturedModel.COLUMN_HORIZONTAL);
/* 4071 */     createRotatedPillarWithHorizontalVariant(Blocks.PEARLESCENT_FROGLIGHT, TexturedModel.COLUMN, TexturedModel.COLUMN_HORIZONTAL);
/*      */     
/* 4073 */     createHorizontallyRotatedBlock(Blocks.LOOM, TexturedModel.ORIENTABLE);
/*      */     
/* 4075 */     createPumpkins();
/* 4076 */     createBeeNest(Blocks.BEE_NEST, TextureMapping::orientableCube);
/* 4077 */     createBeeNest(Blocks.BEEHIVE, TextureMapping::orientableCubeSameEnds);
/*      */ 
/*      */     
/* 4080 */     createCropBlock(Blocks.BEETROOTS, (Property<Integer>)BlockStateProperties.AGE_3, new int[] { 0, 1, 2, 3 });
/* 4081 */     createCropBlock(Blocks.CARROTS, (Property<Integer>)BlockStateProperties.AGE_7, new int[] { 0, 0, 1, 1, 2, 2, 2, 3 });
/* 4082 */     createCropBlock(Blocks.NETHER_WART, (Property<Integer>)BlockStateProperties.AGE_3, new int[] { 0, 1, 1, 2 });
/* 4083 */     createCropBlock(Blocks.POTATOES, (Property<Integer>)BlockStateProperties.AGE_7, new int[] { 0, 0, 1, 1, 2, 2, 2, 3 });
/* 4084 */     createCropBlock(Blocks.WHEAT, (Property<Integer>)BlockStateProperties.AGE_7, new int[] { 0, 1, 2, 3, 4, 5, 6, 7 });
/* 4085 */     createCrossBlock(Blocks.TORCHFLOWER_CROP, PlantType.NOT_TINTED, (Property<Integer>)BlockStateProperties.AGE_1, new int[] { 0, 1 });
/* 4086 */     createPitcherCrop();
/* 4087 */     createPitcherPlant();
/*      */     
/* 4089 */     createBanners();
/*      */     
/* 4091 */     createBeds();
/*      */     
/* 4093 */     createHeads();
/*      */     
/* 4095 */     createChests();
/*      */     
/* 4097 */     createCopperChests();
/*      */     
/* 4099 */     createShulkerBox(Blocks.SHULKER_BOX, null);
/* 4100 */     createShulkerBox(Blocks.WHITE_SHULKER_BOX, DyeColor.WHITE);
/* 4101 */     createShulkerBox(Blocks.ORANGE_SHULKER_BOX, DyeColor.ORANGE);
/* 4102 */     createShulkerBox(Blocks.MAGENTA_SHULKER_BOX, DyeColor.MAGENTA);
/* 4103 */     createShulkerBox(Blocks.LIGHT_BLUE_SHULKER_BOX, DyeColor.LIGHT_BLUE);
/* 4104 */     createShulkerBox(Blocks.YELLOW_SHULKER_BOX, DyeColor.YELLOW);
/* 4105 */     createShulkerBox(Blocks.LIME_SHULKER_BOX, DyeColor.LIME);
/* 4106 */     createShulkerBox(Blocks.PINK_SHULKER_BOX, DyeColor.PINK);
/* 4107 */     createShulkerBox(Blocks.GRAY_SHULKER_BOX, DyeColor.GRAY);
/* 4108 */     createShulkerBox(Blocks.LIGHT_GRAY_SHULKER_BOX, DyeColor.LIGHT_GRAY);
/* 4109 */     createShulkerBox(Blocks.CYAN_SHULKER_BOX, DyeColor.CYAN);
/* 4110 */     createShulkerBox(Blocks.PURPLE_SHULKER_BOX, DyeColor.PURPLE);
/* 4111 */     createShulkerBox(Blocks.BLUE_SHULKER_BOX, DyeColor.BLUE);
/* 4112 */     createShulkerBox(Blocks.BROWN_SHULKER_BOX, DyeColor.BROWN);
/* 4113 */     createShulkerBox(Blocks.GREEN_SHULKER_BOX, DyeColor.GREEN);
/* 4114 */     createShulkerBox(Blocks.RED_SHULKER_BOX, DyeColor.RED);
/* 4115 */     createShulkerBox(Blocks.BLACK_SHULKER_BOX, DyeColor.BLACK);
/*      */     
/* 4117 */     createCopperGolemStatues();
/*      */     
/* 4119 */     createParticleOnlyBlock(Blocks.CONDUIT);
/* 4120 */     generateSimpleSpecialItemModel(Blocks.CONDUIT, (SpecialModelRenderer.Unbaked)new ConduitSpecialRenderer.Unbaked());
/*      */     
/* 4122 */     createParticleOnlyBlock(Blocks.DECORATED_POT, Blocks.TERRACOTTA);
/* 4123 */     generateSimpleSpecialItemModel(Blocks.DECORATED_POT, (SpecialModelRenderer.Unbaked)new DecoratedPotSpecialRenderer.Unbaked());
/*      */     
/* 4125 */     createParticleOnlyBlock(Blocks.END_PORTAL, Blocks.OBSIDIAN);
/* 4126 */     createParticleOnlyBlock(Blocks.END_GATEWAY, Blocks.OBSIDIAN);
/*      */     
/* 4128 */     createTrivialCube(Blocks.AZALEA_LEAVES);
/* 4129 */     createTrivialCube(Blocks.FLOWERING_AZALEA_LEAVES);
/* 4130 */     createTrivialCube(Blocks.WHITE_CONCRETE);
/* 4131 */     createTrivialCube(Blocks.ORANGE_CONCRETE);
/* 4132 */     createTrivialCube(Blocks.MAGENTA_CONCRETE);
/* 4133 */     createTrivialCube(Blocks.LIGHT_BLUE_CONCRETE);
/* 4134 */     createTrivialCube(Blocks.YELLOW_CONCRETE);
/* 4135 */     createTrivialCube(Blocks.LIME_CONCRETE);
/* 4136 */     createTrivialCube(Blocks.PINK_CONCRETE);
/* 4137 */     createTrivialCube(Blocks.GRAY_CONCRETE);
/* 4138 */     createTrivialCube(Blocks.LIGHT_GRAY_CONCRETE);
/* 4139 */     createTrivialCube(Blocks.CYAN_CONCRETE);
/* 4140 */     createTrivialCube(Blocks.PURPLE_CONCRETE);
/* 4141 */     createTrivialCube(Blocks.BLUE_CONCRETE);
/* 4142 */     createTrivialCube(Blocks.BROWN_CONCRETE);
/* 4143 */     createTrivialCube(Blocks.GREEN_CONCRETE);
/* 4144 */     createTrivialCube(Blocks.RED_CONCRETE);
/* 4145 */     createTrivialCube(Blocks.BLACK_CONCRETE);
/*      */     
/* 4147 */     createColoredBlockWithRandomRotations(TexturedModel.CUBE, new Block[] { Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER, Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER });
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
/*      */ 
/*      */ 
/*      */     
/* 4166 */     createTrivialCube(Blocks.TERRACOTTA);
/* 4167 */     createTrivialCube(Blocks.WHITE_TERRACOTTA);
/* 4168 */     createTrivialCube(Blocks.ORANGE_TERRACOTTA);
/* 4169 */     createTrivialCube(Blocks.MAGENTA_TERRACOTTA);
/* 4170 */     createTrivialCube(Blocks.LIGHT_BLUE_TERRACOTTA);
/* 4171 */     createTrivialCube(Blocks.YELLOW_TERRACOTTA);
/* 4172 */     createTrivialCube(Blocks.LIME_TERRACOTTA);
/* 4173 */     createTrivialCube(Blocks.PINK_TERRACOTTA);
/* 4174 */     createTrivialCube(Blocks.GRAY_TERRACOTTA);
/* 4175 */     createTrivialCube(Blocks.LIGHT_GRAY_TERRACOTTA);
/* 4176 */     createTrivialCube(Blocks.CYAN_TERRACOTTA);
/* 4177 */     createTrivialCube(Blocks.PURPLE_TERRACOTTA);
/* 4178 */     createTrivialCube(Blocks.BLUE_TERRACOTTA);
/* 4179 */     createTrivialCube(Blocks.BROWN_TERRACOTTA);
/* 4180 */     createTrivialCube(Blocks.GREEN_TERRACOTTA);
/* 4181 */     createTrivialCube(Blocks.RED_TERRACOTTA);
/* 4182 */     createTrivialCube(Blocks.BLACK_TERRACOTTA);
/*      */     
/* 4184 */     createTrivialCube(Blocks.TINTED_GLASS);
/* 4185 */     createGlassBlocks(Blocks.GLASS, Blocks.GLASS_PANE);
/* 4186 */     createGlassBlocks(Blocks.WHITE_STAINED_GLASS, Blocks.WHITE_STAINED_GLASS_PANE);
/* 4187 */     createGlassBlocks(Blocks.ORANGE_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS_PANE);
/* 4188 */     createGlassBlocks(Blocks.MAGENTA_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS_PANE);
/* 4189 */     createGlassBlocks(Blocks.LIGHT_BLUE_STAINED_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE);
/* 4190 */     createGlassBlocks(Blocks.YELLOW_STAINED_GLASS, Blocks.YELLOW_STAINED_GLASS_PANE);
/* 4191 */     createGlassBlocks(Blocks.LIME_STAINED_GLASS, Blocks.LIME_STAINED_GLASS_PANE);
/* 4192 */     createGlassBlocks(Blocks.PINK_STAINED_GLASS, Blocks.PINK_STAINED_GLASS_PANE);
/* 4193 */     createGlassBlocks(Blocks.GRAY_STAINED_GLASS, Blocks.GRAY_STAINED_GLASS_PANE);
/* 4194 */     createGlassBlocks(Blocks.LIGHT_GRAY_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS_PANE);
/* 4195 */     createGlassBlocks(Blocks.CYAN_STAINED_GLASS, Blocks.CYAN_STAINED_GLASS_PANE);
/* 4196 */     createGlassBlocks(Blocks.PURPLE_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS_PANE);
/* 4197 */     createGlassBlocks(Blocks.BLUE_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS_PANE);
/* 4198 */     createGlassBlocks(Blocks.BROWN_STAINED_GLASS, Blocks.BROWN_STAINED_GLASS_PANE);
/* 4199 */     createGlassBlocks(Blocks.GREEN_STAINED_GLASS, Blocks.GREEN_STAINED_GLASS_PANE);
/* 4200 */     createGlassBlocks(Blocks.RED_STAINED_GLASS, Blocks.RED_STAINED_GLASS_PANE);
/* 4201 */     createGlassBlocks(Blocks.BLACK_STAINED_GLASS, Blocks.BLACK_STAINED_GLASS_PANE);
/*      */     
/* 4203 */     createColoredBlockWithStateRotations(TexturedModel.GLAZED_TERRACOTTA, new Block[] { Blocks.WHITE_GLAZED_TERRACOTTA, Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, Blocks.YELLOW_GLAZED_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA, Blocks.PINK_GLAZED_TERRACOTTA, Blocks.GRAY_GLAZED_TERRACOTTA, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, Blocks.CYAN_GLAZED_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA, Blocks.BROWN_GLAZED_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA, Blocks.RED_GLAZED_TERRACOTTA, Blocks.BLACK_GLAZED_TERRACOTTA });
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
/*      */ 
/*      */ 
/*      */     
/* 4222 */     createFullAndCarpetBlocks(Blocks.WHITE_WOOL, Blocks.WHITE_CARPET);
/* 4223 */     createFullAndCarpetBlocks(Blocks.ORANGE_WOOL, Blocks.ORANGE_CARPET);
/* 4224 */     createFullAndCarpetBlocks(Blocks.MAGENTA_WOOL, Blocks.MAGENTA_CARPET);
/* 4225 */     createFullAndCarpetBlocks(Blocks.LIGHT_BLUE_WOOL, Blocks.LIGHT_BLUE_CARPET);
/* 4226 */     createFullAndCarpetBlocks(Blocks.YELLOW_WOOL, Blocks.YELLOW_CARPET);
/* 4227 */     createFullAndCarpetBlocks(Blocks.LIME_WOOL, Blocks.LIME_CARPET);
/* 4228 */     createFullAndCarpetBlocks(Blocks.PINK_WOOL, Blocks.PINK_CARPET);
/* 4229 */     createFullAndCarpetBlocks(Blocks.GRAY_WOOL, Blocks.GRAY_CARPET);
/* 4230 */     createFullAndCarpetBlocks(Blocks.LIGHT_GRAY_WOOL, Blocks.LIGHT_GRAY_CARPET);
/* 4231 */     createFullAndCarpetBlocks(Blocks.CYAN_WOOL, Blocks.CYAN_CARPET);
/* 4232 */     createFullAndCarpetBlocks(Blocks.PURPLE_WOOL, Blocks.PURPLE_CARPET);
/* 4233 */     createFullAndCarpetBlocks(Blocks.BLUE_WOOL, Blocks.BLUE_CARPET);
/* 4234 */     createFullAndCarpetBlocks(Blocks.BROWN_WOOL, Blocks.BROWN_CARPET);
/* 4235 */     createFullAndCarpetBlocks(Blocks.GREEN_WOOL, Blocks.GREEN_CARPET);
/* 4236 */     createFullAndCarpetBlocks(Blocks.RED_WOOL, Blocks.RED_CARPET);
/* 4237 */     createFullAndCarpetBlocks(Blocks.BLACK_WOOL, Blocks.BLACK_CARPET);
/*      */     
/* 4239 */     createTrivialCube(Blocks.MUD);
/* 4240 */     createTrivialCube(Blocks.PACKED_MUD);
/*      */     
/* 4242 */     createPlant(Blocks.FERN, Blocks.POTTED_FERN, PlantType.TINTED);
/* 4243 */     createItemWithGrassTint(Blocks.FERN);
/* 4244 */     createPlantWithDefaultItem(Blocks.DANDELION, Blocks.POTTED_DANDELION, PlantType.NOT_TINTED);
/* 4245 */     createPlantWithDefaultItem(Blocks.POPPY, Blocks.POTTED_POPPY, PlantType.NOT_TINTED);
/* 4246 */     createPlantWithDefaultItem(Blocks.OPEN_EYEBLOSSOM, Blocks.POTTED_OPEN_EYEBLOSSOM, PlantType.EMISSIVE_NOT_TINTED);
/* 4247 */     createPlantWithDefaultItem(Blocks.CLOSED_EYEBLOSSOM, Blocks.POTTED_CLOSED_EYEBLOSSOM, PlantType.NOT_TINTED);
/* 4248 */     createPlantWithDefaultItem(Blocks.BLUE_ORCHID, Blocks.POTTED_BLUE_ORCHID, PlantType.NOT_TINTED);
/* 4249 */     createPlantWithDefaultItem(Blocks.ALLIUM, Blocks.POTTED_ALLIUM, PlantType.NOT_TINTED);
/* 4250 */     createPlantWithDefaultItem(Blocks.AZURE_BLUET, Blocks.POTTED_AZURE_BLUET, PlantType.NOT_TINTED);
/* 4251 */     createPlantWithDefaultItem(Blocks.RED_TULIP, Blocks.POTTED_RED_TULIP, PlantType.NOT_TINTED);
/* 4252 */     createPlantWithDefaultItem(Blocks.ORANGE_TULIP, Blocks.POTTED_ORANGE_TULIP, PlantType.NOT_TINTED);
/* 4253 */     createPlantWithDefaultItem(Blocks.WHITE_TULIP, Blocks.POTTED_WHITE_TULIP, PlantType.NOT_TINTED);
/* 4254 */     createPlantWithDefaultItem(Blocks.PINK_TULIP, Blocks.POTTED_PINK_TULIP, PlantType.NOT_TINTED);
/* 4255 */     createPlantWithDefaultItem(Blocks.OXEYE_DAISY, Blocks.POTTED_OXEYE_DAISY, PlantType.NOT_TINTED);
/* 4256 */     createPlantWithDefaultItem(Blocks.CORNFLOWER, Blocks.POTTED_CORNFLOWER, PlantType.NOT_TINTED);
/* 4257 */     createPlantWithDefaultItem(Blocks.LILY_OF_THE_VALLEY, Blocks.POTTED_LILY_OF_THE_VALLEY, PlantType.NOT_TINTED);
/* 4258 */     createPlantWithDefaultItem(Blocks.WITHER_ROSE, Blocks.POTTED_WITHER_ROSE, PlantType.NOT_TINTED);
/* 4259 */     createPlantWithDefaultItem(Blocks.RED_MUSHROOM, Blocks.POTTED_RED_MUSHROOM, PlantType.NOT_TINTED);
/* 4260 */     createPlantWithDefaultItem(Blocks.BROWN_MUSHROOM, Blocks.POTTED_BROWN_MUSHROOM, PlantType.NOT_TINTED);
/* 4261 */     createPlantWithDefaultItem(Blocks.DEAD_BUSH, Blocks.POTTED_DEAD_BUSH, PlantType.NOT_TINTED);
/* 4262 */     createPlantWithDefaultItem(Blocks.TORCHFLOWER, Blocks.POTTED_TORCHFLOWER, PlantType.NOT_TINTED);
/*      */     
/* 4264 */     createPointedDripstone();
/*      */     
/* 4266 */     createMushroomBlock(Blocks.BROWN_MUSHROOM_BLOCK);
/* 4267 */     createMushroomBlock(Blocks.RED_MUSHROOM_BLOCK);
/* 4268 */     createMushroomBlock(Blocks.MUSHROOM_STEM);
/*      */     
/* 4270 */     createCrossBlock(Blocks.SHORT_GRASS, PlantType.TINTED);
/* 4271 */     createItemWithGrassTint(Blocks.SHORT_GRASS);
/* 4272 */     createCrossBlockWithDefaultItem(Blocks.SHORT_DRY_GRASS, PlantType.NOT_TINTED);
/* 4273 */     createCrossBlockWithDefaultItem(Blocks.TALL_DRY_GRASS, PlantType.NOT_TINTED);
/* 4274 */     createCrossBlock(Blocks.BUSH, PlantType.TINTED);
/* 4275 */     createItemWithGrassTint(Blocks.BUSH);
/* 4276 */     createCrossBlock(Blocks.SUGAR_CANE, PlantType.TINTED);
/* 4277 */     registerSimpleFlatItemModel(Items.SUGAR_CANE);
/* 4278 */     createGrowingPlant(Blocks.KELP, Blocks.KELP_PLANT, PlantType.NOT_TINTED);
/* 4279 */     registerSimpleFlatItemModel(Items.KELP);
/* 4280 */     createCrossBlock(Blocks.HANGING_ROOTS, PlantType.NOT_TINTED);
/* 4281 */     createGrowingPlant(Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT, PlantType.NOT_TINTED);
/* 4282 */     createGrowingPlant(Blocks.TWISTING_VINES, Blocks.TWISTING_VINES_PLANT, PlantType.NOT_TINTED);
/* 4283 */     registerSimpleFlatItemModel(Blocks.WEEPING_VINES, "_plant");
/* 4284 */     registerSimpleFlatItemModel(Blocks.TWISTING_VINES, "_plant");
/* 4285 */     createCrossBlockWithDefaultItem(Blocks.BAMBOO_SAPLING, PlantType.TINTED, TextureMapping.cross(TextureMapping.getBlockTexture(Blocks.BAMBOO, "_stage0")));
/* 4286 */     createBamboo();
/* 4287 */     createCrossBlockWithDefaultItem(Blocks.CACTUS_FLOWER, PlantType.NOT_TINTED);
/* 4288 */     createCrossBlockWithDefaultItem(Blocks.COBWEB, PlantType.NOT_TINTED);
/* 4289 */     createDoublePlantWithDefaultItem(Blocks.LILAC, PlantType.NOT_TINTED);
/* 4290 */     createDoublePlantWithDefaultItem(Blocks.ROSE_BUSH, PlantType.NOT_TINTED);
/* 4291 */     createDoublePlantWithDefaultItem(Blocks.PEONY, PlantType.NOT_TINTED);
/* 4292 */     createTintedDoublePlant(Blocks.TALL_GRASS);
/* 4293 */     createTintedDoublePlant(Blocks.LARGE_FERN);
/*      */     
/* 4295 */     createSunflower();
/* 4296 */     createTallSeagrass();
/* 4297 */     createSmallDripleaf();
/*      */     
/* 4299 */     createCoral(Blocks.TUBE_CORAL, Blocks.DEAD_TUBE_CORAL, Blocks.TUBE_CORAL_BLOCK, Blocks.DEAD_TUBE_CORAL_BLOCK, Blocks.TUBE_CORAL_FAN, Blocks.DEAD_TUBE_CORAL_FAN, Blocks.TUBE_CORAL_WALL_FAN, Blocks.DEAD_TUBE_CORAL_WALL_FAN);
/* 4300 */     createCoral(Blocks.BRAIN_CORAL, Blocks.DEAD_BRAIN_CORAL, Blocks.BRAIN_CORAL_BLOCK, Blocks.DEAD_BRAIN_CORAL_BLOCK, Blocks.BRAIN_CORAL_FAN, Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.BRAIN_CORAL_WALL_FAN, Blocks.DEAD_BRAIN_CORAL_WALL_FAN);
/* 4301 */     createCoral(Blocks.BUBBLE_CORAL, Blocks.DEAD_BUBBLE_CORAL, Blocks.BUBBLE_CORAL_BLOCK, Blocks.DEAD_BUBBLE_CORAL_BLOCK, Blocks.BUBBLE_CORAL_FAN, Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.BUBBLE_CORAL_WALL_FAN, Blocks.DEAD_BUBBLE_CORAL_WALL_FAN);
/* 4302 */     createCoral(Blocks.FIRE_CORAL, Blocks.DEAD_FIRE_CORAL, Blocks.FIRE_CORAL_BLOCK, Blocks.DEAD_FIRE_CORAL_BLOCK, Blocks.FIRE_CORAL_FAN, Blocks.DEAD_FIRE_CORAL_FAN, Blocks.FIRE_CORAL_WALL_FAN, Blocks.DEAD_FIRE_CORAL_WALL_FAN);
/* 4303 */     createCoral(Blocks.HORN_CORAL, Blocks.DEAD_HORN_CORAL, Blocks.HORN_CORAL_BLOCK, Blocks.DEAD_HORN_CORAL_BLOCK, Blocks.HORN_CORAL_FAN, Blocks.DEAD_HORN_CORAL_FAN, Blocks.HORN_CORAL_WALL_FAN, Blocks.DEAD_HORN_CORAL_WALL_FAN);
/*      */     
/* 4305 */     createStems(Blocks.MELON_STEM, Blocks.ATTACHED_MELON_STEM);
/* 4306 */     createStems(Blocks.PUMPKIN_STEM, Blocks.ATTACHED_PUMPKIN_STEM);
/*      */     
/* 4308 */     woodProvider(Blocks.MANGROVE_LOG).logWithHorizontal(Blocks.MANGROVE_LOG).wood(Blocks.MANGROVE_WOOD);
/* 4309 */     woodProvider(Blocks.STRIPPED_MANGROVE_LOG).logWithHorizontal(Blocks.STRIPPED_MANGROVE_LOG).wood(Blocks.STRIPPED_MANGROVE_WOOD);
/* 4310 */     createHangingSign(Blocks.STRIPPED_MANGROVE_LOG, Blocks.MANGROVE_HANGING_SIGN, Blocks.MANGROVE_WALL_HANGING_SIGN);
/* 4311 */     createTintedLeaves(Blocks.MANGROVE_LEAVES, TexturedModel.LEAVES, -7158200);
/*      */     
/* 4313 */     woodProvider(Blocks.ACACIA_LOG).logWithHorizontal(Blocks.ACACIA_LOG).wood(Blocks.ACACIA_WOOD);
/* 4314 */     woodProvider(Blocks.STRIPPED_ACACIA_LOG).logWithHorizontal(Blocks.STRIPPED_ACACIA_LOG).wood(Blocks.STRIPPED_ACACIA_WOOD);
/* 4315 */     createHangingSign(Blocks.STRIPPED_ACACIA_LOG, Blocks.ACACIA_HANGING_SIGN, Blocks.ACACIA_WALL_HANGING_SIGN);
/* 4316 */     createPlantWithDefaultItem(Blocks.ACACIA_SAPLING, Blocks.POTTED_ACACIA_SAPLING, PlantType.NOT_TINTED);
/* 4317 */     createTintedLeaves(Blocks.ACACIA_LEAVES, TexturedModel.LEAVES, -12012264);
/*      */     
/* 4319 */     woodProvider(Blocks.CHERRY_LOG).logUVLocked(Blocks.CHERRY_LOG).wood(Blocks.CHERRY_WOOD);
/* 4320 */     woodProvider(Blocks.STRIPPED_CHERRY_LOG).logUVLocked(Blocks.STRIPPED_CHERRY_LOG).wood(Blocks.STRIPPED_CHERRY_WOOD);
/* 4321 */     createHangingSign(Blocks.STRIPPED_CHERRY_LOG, Blocks.CHERRY_HANGING_SIGN, Blocks.CHERRY_WALL_HANGING_SIGN);
/* 4322 */     createPlantWithDefaultItem(Blocks.CHERRY_SAPLING, Blocks.POTTED_CHERRY_SAPLING, PlantType.NOT_TINTED);
/* 4323 */     createTrivialBlock(Blocks.CHERRY_LEAVES, TexturedModel.LEAVES);
/*      */     
/* 4325 */     woodProvider(Blocks.BIRCH_LOG).logWithHorizontal(Blocks.BIRCH_LOG).wood(Blocks.BIRCH_WOOD);
/* 4326 */     woodProvider(Blocks.STRIPPED_BIRCH_LOG).logWithHorizontal(Blocks.STRIPPED_BIRCH_LOG).wood(Blocks.STRIPPED_BIRCH_WOOD);
/* 4327 */     createHangingSign(Blocks.STRIPPED_BIRCH_LOG, Blocks.BIRCH_HANGING_SIGN, Blocks.BIRCH_WALL_HANGING_SIGN);
/* 4328 */     createPlantWithDefaultItem(Blocks.BIRCH_SAPLING, Blocks.POTTED_BIRCH_SAPLING, PlantType.NOT_TINTED);
/* 4329 */     createTintedLeaves(Blocks.BIRCH_LEAVES, TexturedModel.LEAVES, -8345771);
/*      */     
/* 4331 */     woodProvider(Blocks.OAK_LOG).logWithHorizontal(Blocks.OAK_LOG).wood(Blocks.OAK_WOOD);
/* 4332 */     woodProvider(Blocks.STRIPPED_OAK_LOG).logWithHorizontal(Blocks.STRIPPED_OAK_LOG).wood(Blocks.STRIPPED_OAK_WOOD);
/* 4333 */     createHangingSign(Blocks.STRIPPED_OAK_LOG, Blocks.OAK_HANGING_SIGN, Blocks.OAK_WALL_HANGING_SIGN);
/* 4334 */     createPlantWithDefaultItem(Blocks.OAK_SAPLING, Blocks.POTTED_OAK_SAPLING, PlantType.NOT_TINTED);
/* 4335 */     createTintedLeaves(Blocks.OAK_LEAVES, TexturedModel.LEAVES, -12012264);
/*      */     
/* 4337 */     woodProvider(Blocks.SPRUCE_LOG).logWithHorizontal(Blocks.SPRUCE_LOG).wood(Blocks.SPRUCE_WOOD);
/* 4338 */     woodProvider(Blocks.STRIPPED_SPRUCE_LOG).logWithHorizontal(Blocks.STRIPPED_SPRUCE_LOG).wood(Blocks.STRIPPED_SPRUCE_WOOD);
/* 4339 */     createHangingSign(Blocks.STRIPPED_SPRUCE_LOG, Blocks.SPRUCE_HANGING_SIGN, Blocks.SPRUCE_WALL_HANGING_SIGN);
/* 4340 */     createPlantWithDefaultItem(Blocks.SPRUCE_SAPLING, Blocks.POTTED_SPRUCE_SAPLING, PlantType.NOT_TINTED);
/* 4341 */     createTintedLeaves(Blocks.SPRUCE_LEAVES, TexturedModel.LEAVES, -10380959);
/*      */     
/* 4343 */     woodProvider(Blocks.DARK_OAK_LOG).logWithHorizontal(Blocks.DARK_OAK_LOG).wood(Blocks.DARK_OAK_WOOD);
/* 4344 */     woodProvider(Blocks.STRIPPED_DARK_OAK_LOG).logWithHorizontal(Blocks.STRIPPED_DARK_OAK_LOG).wood(Blocks.STRIPPED_DARK_OAK_WOOD);
/* 4345 */     createHangingSign(Blocks.STRIPPED_DARK_OAK_LOG, Blocks.DARK_OAK_HANGING_SIGN, Blocks.DARK_OAK_WALL_HANGING_SIGN);
/* 4346 */     createPlantWithDefaultItem(Blocks.DARK_OAK_SAPLING, Blocks.POTTED_DARK_OAK_SAPLING, PlantType.NOT_TINTED);
/* 4347 */     createTintedLeaves(Blocks.DARK_OAK_LEAVES, TexturedModel.LEAVES, -12012264);
/*      */     
/* 4349 */     woodProvider(Blocks.PALE_OAK_LOG).logWithHorizontal(Blocks.PALE_OAK_LOG).wood(Blocks.PALE_OAK_WOOD);
/* 4350 */     woodProvider(Blocks.STRIPPED_PALE_OAK_LOG).logWithHorizontal(Blocks.STRIPPED_PALE_OAK_LOG).wood(Blocks.STRIPPED_PALE_OAK_WOOD);
/* 4351 */     createHangingSign(Blocks.STRIPPED_PALE_OAK_LOG, Blocks.PALE_OAK_HANGING_SIGN, Blocks.PALE_OAK_WALL_HANGING_SIGN);
/* 4352 */     createPlantWithDefaultItem(Blocks.PALE_OAK_SAPLING, Blocks.POTTED_PALE_OAK_SAPLING, PlantType.NOT_TINTED);
/* 4353 */     createTrivialBlock(Blocks.PALE_OAK_LEAVES, TexturedModel.LEAVES);
/*      */     
/* 4355 */     woodProvider(Blocks.JUNGLE_LOG).logWithHorizontal(Blocks.JUNGLE_LOG).wood(Blocks.JUNGLE_WOOD);
/* 4356 */     woodProvider(Blocks.STRIPPED_JUNGLE_LOG).logWithHorizontal(Blocks.STRIPPED_JUNGLE_LOG).wood(Blocks.STRIPPED_JUNGLE_WOOD);
/* 4357 */     createHangingSign(Blocks.STRIPPED_JUNGLE_LOG, Blocks.JUNGLE_HANGING_SIGN, Blocks.JUNGLE_WALL_HANGING_SIGN);
/* 4358 */     createPlantWithDefaultItem(Blocks.JUNGLE_SAPLING, Blocks.POTTED_JUNGLE_SAPLING, PlantType.NOT_TINTED);
/* 4359 */     createTintedLeaves(Blocks.JUNGLE_LEAVES, TexturedModel.LEAVES, -12012264);
/*      */     
/* 4361 */     woodProvider(Blocks.CRIMSON_STEM).log(Blocks.CRIMSON_STEM).wood(Blocks.CRIMSON_HYPHAE);
/* 4362 */     woodProvider(Blocks.STRIPPED_CRIMSON_STEM).log(Blocks.STRIPPED_CRIMSON_STEM).wood(Blocks.STRIPPED_CRIMSON_HYPHAE);
/* 4363 */     createHangingSign(Blocks.STRIPPED_CRIMSON_STEM, Blocks.CRIMSON_HANGING_SIGN, Blocks.CRIMSON_WALL_HANGING_SIGN);
/* 4364 */     createPlantWithDefaultItem(Blocks.CRIMSON_FUNGUS, Blocks.POTTED_CRIMSON_FUNGUS, PlantType.NOT_TINTED);
/* 4365 */     createNetherRoots(Blocks.CRIMSON_ROOTS, Blocks.POTTED_CRIMSON_ROOTS);
/*      */     
/* 4367 */     woodProvider(Blocks.WARPED_STEM).log(Blocks.WARPED_STEM).wood(Blocks.WARPED_HYPHAE);
/* 4368 */     woodProvider(Blocks.STRIPPED_WARPED_STEM).log(Blocks.STRIPPED_WARPED_STEM).wood(Blocks.STRIPPED_WARPED_HYPHAE);
/* 4369 */     createHangingSign(Blocks.STRIPPED_WARPED_STEM, Blocks.WARPED_HANGING_SIGN, Blocks.WARPED_WALL_HANGING_SIGN);
/* 4370 */     createPlantWithDefaultItem(Blocks.WARPED_FUNGUS, Blocks.POTTED_WARPED_FUNGUS, PlantType.NOT_TINTED);
/* 4371 */     createNetherRoots(Blocks.WARPED_ROOTS, Blocks.POTTED_WARPED_ROOTS);
/*      */     
/* 4373 */     woodProvider(Blocks.BAMBOO_BLOCK).logUVLocked(Blocks.BAMBOO_BLOCK);
/* 4374 */     woodProvider(Blocks.STRIPPED_BAMBOO_BLOCK).logUVLocked(Blocks.STRIPPED_BAMBOO_BLOCK);
/* 4375 */     createHangingSign(Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_HANGING_SIGN, Blocks.BAMBOO_WALL_HANGING_SIGN);
/*      */     
/* 4377 */     createCrossBlock(Blocks.NETHER_SPROUTS, PlantType.NOT_TINTED);
/* 4378 */     registerSimpleFlatItemModel(Items.NETHER_SPROUTS);
/*      */     
/* 4380 */     createDoor(Blocks.IRON_DOOR);
/* 4381 */     createTrapdoor(Blocks.IRON_TRAPDOOR);
/*      */     
/* 4383 */     createSmoothStoneSlab();
/*      */     
/* 4385 */     createPassiveRail(Blocks.RAIL);
/* 4386 */     createActiveRail(Blocks.POWERED_RAIL);
/* 4387 */     createActiveRail(Blocks.DETECTOR_RAIL);
/* 4388 */     createActiveRail(Blocks.ACTIVATOR_RAIL);
/*      */     
/* 4390 */     createComparator();
/*      */     
/* 4392 */     createCommandBlock(Blocks.COMMAND_BLOCK);
/* 4393 */     createCommandBlock(Blocks.REPEATING_COMMAND_BLOCK);
/* 4394 */     createCommandBlock(Blocks.CHAIN_COMMAND_BLOCK);
/*      */     
/* 4396 */     createAnvil(Blocks.ANVIL);
/* 4397 */     createAnvil(Blocks.CHIPPED_ANVIL);
/* 4398 */     createAnvil(Blocks.DAMAGED_ANVIL);
/*      */     
/* 4400 */     createBarrel();
/* 4401 */     createBell();
/*      */     
/* 4403 */     createFurnace(Blocks.FURNACE, TexturedModel.ORIENTABLE_ONLY_TOP);
/* 4404 */     createFurnace(Blocks.BLAST_FURNACE, TexturedModel.ORIENTABLE_ONLY_TOP);
/* 4405 */     createFurnace(Blocks.SMOKER, TexturedModel.ORIENTABLE);
/*      */     
/* 4407 */     createRedstoneWire();
/*      */     
/* 4409 */     createRespawnAnchor();
/* 4410 */     createSculkCatalyst();
/*      */     
/* 4412 */     copyModel(Blocks.CHISELED_STONE_BRICKS, Blocks.INFESTED_CHISELED_STONE_BRICKS);
/* 4413 */     copyModel(Blocks.COBBLESTONE, Blocks.INFESTED_COBBLESTONE);
/* 4414 */     copyModel(Blocks.CRACKED_STONE_BRICKS, Blocks.INFESTED_CRACKED_STONE_BRICKS);
/* 4415 */     copyModel(Blocks.MOSSY_STONE_BRICKS, Blocks.INFESTED_MOSSY_STONE_BRICKS);
/* 4416 */     createInfestedStone();
/* 4417 */     copyModel(Blocks.STONE_BRICKS, Blocks.INFESTED_STONE_BRICKS);
/* 4418 */     createInfestedDeepslate();
/*      */   }
/*      */   
/*      */   private void createLightBlock() {
/* 4422 */     ItemModel.Unbaked base = ItemModelUtils.plainModel(createFlatItemModel(Items.LIGHT));
/*      */     
/* 4424 */     Map<Integer, ItemModel.Unbaked> overrides = new HashMap<>(16);
/*      */     
/* 4426 */     PropertyDispatch.C1<MultiVariant, Integer> light = PropertyDispatch.initial((Property)BlockStateProperties.LEVEL);
/* 4427 */     for (int i = 0; i <= 15; i++) {
/* 4428 */       String suffix = String.format(Locale.ROOT, "_%02d", new Object[] { i });
/* 4429 */       Identifier texture = TextureMapping.getItemTexture(Items.LIGHT, suffix);
/* 4430 */       light.select(i, plainVariant(ModelTemplates.PARTICLE_ONLY.createWithSuffix(Blocks.LIGHT, suffix, TextureMapping.particle(texture), this.modelOutput)));
/* 4431 */       ItemModel.Unbaked overrideItem = ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(Items.LIGHT, suffix), TextureMapping.layer0(texture), this.modelOutput));
/* 4432 */       overrides.put(i, overrideItem);
/*      */     } 
/*      */     
/* 4435 */     this.itemModelOutput.accept(Items.LIGHT, ItemModelUtils.selectBlockItemProperty((Property)LightBlock.LEVEL, base, overrides));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4441 */     this.blockStateOutput.accept(
/* 4442 */         MultiVariantGenerator.dispatch(Blocks.LIGHT)
/* 4443 */         .with((PropertyDispatch)light));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createCopperChainItem(Item unwaxed, Item waxed) {
/* 4448 */     Identifier model = createFlatItemModel(unwaxed);
/* 4449 */     registerSimpleItemModel(unwaxed, model);
/* 4450 */     registerSimpleItemModel(waxed, model);
/*      */   }
/*      */   
/*      */   private void createCandleAndCandleCake(Block candleBlock, Block candleCakeBlock) {
/* 4454 */     registerSimpleFlatItemModel(candleBlock.asItem());
/*      */     
/* 4456 */     TextureMapping candleTexture = TextureMapping.cube(TextureMapping.getBlockTexture(candleBlock));
/* 4457 */     TextureMapping candleLitTexture = TextureMapping.cube(TextureMapping.getBlockTexture(candleBlock, "_lit"));
/*      */     
/* 4459 */     MultiVariant oneCandle = plainVariant(ModelTemplates.CANDLE.createWithSuffix(candleBlock, "_one_candle", candleTexture, this.modelOutput));
/* 4460 */     MultiVariant twoCandles = plainVariant(ModelTemplates.TWO_CANDLES.createWithSuffix(candleBlock, "_two_candles", candleTexture, this.modelOutput));
/* 4461 */     MultiVariant threeCandles = plainVariant(ModelTemplates.THREE_CANDLES.createWithSuffix(candleBlock, "_three_candles", candleTexture, this.modelOutput));
/* 4462 */     MultiVariant fourCandles = plainVariant(ModelTemplates.FOUR_CANDLES.createWithSuffix(candleBlock, "_four_candles", candleTexture, this.modelOutput));
/*      */     
/* 4464 */     MultiVariant oneCandleLit = plainVariant(ModelTemplates.CANDLE.createWithSuffix(candleBlock, "_one_candle_lit", candleLitTexture, this.modelOutput));
/* 4465 */     MultiVariant twoCandlesLit = plainVariant(ModelTemplates.TWO_CANDLES.createWithSuffix(candleBlock, "_two_candles_lit", candleLitTexture, this.modelOutput));
/* 4466 */     MultiVariant threeCandlesLit = plainVariant(ModelTemplates.THREE_CANDLES.createWithSuffix(candleBlock, "_three_candles_lit", candleLitTexture, this.modelOutput));
/* 4467 */     MultiVariant fourCandlesLit = plainVariant(ModelTemplates.FOUR_CANDLES.createWithSuffix(candleBlock, "_four_candles_lit", candleLitTexture, this.modelOutput));
/*      */     
/* 4469 */     this.blockStateOutput.accept(
/* 4470 */         MultiVariantGenerator.dispatch(candleBlock)
/* 4471 */         .with(
/* 4472 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.CANDLES, (Property)BlockStateProperties.LIT)
/* 4473 */           .select(1, false, oneCandle)
/* 4474 */           .select(2, false, twoCandles)
/* 4475 */           .select(3, false, threeCandles)
/* 4476 */           .select(4, false, fourCandles)
/* 4477 */           .select(1, true, oneCandleLit)
/* 4478 */           .select(2, true, twoCandlesLit)
/* 4479 */           .select(3, true, threeCandlesLit)
/* 4480 */           .select(4, true, fourCandlesLit)));
/*      */ 
/*      */ 
/*      */     
/* 4484 */     MultiVariant candleCake = plainVariant(ModelTemplates.CANDLE_CAKE.create(candleCakeBlock, TextureMapping.candleCake(candleBlock, false), this.modelOutput));
/* 4485 */     MultiVariant litCandleCake = plainVariant(ModelTemplates.CANDLE_CAKE.createWithSuffix(candleCakeBlock, "_lit", TextureMapping.candleCake(candleBlock, true), this.modelOutput));
/* 4486 */     this.blockStateOutput.accept(
/* 4487 */         MultiVariantGenerator.dispatch(candleCakeBlock)
/* 4488 */         .with(createBooleanModelDispatch(BlockStateProperties.LIT, litCandleCake, candleCake)));
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   private static interface BlockStateGeneratorSupplier {
/*      */     BlockModelDefinitionGenerator create(Block param1Block, Variant param1Variant, TextureMapping param1TextureMapping, BiConsumer<Identifier, ModelInstance> param1BiConsumer);
/*      */   }
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/models/BlockModelGenerators.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */