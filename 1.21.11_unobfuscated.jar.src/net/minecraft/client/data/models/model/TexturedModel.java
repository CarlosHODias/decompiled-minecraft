/*     */ package net.minecraft.client.data.models.model;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ public class TexturedModel
/*     */ {
/*  11 */   public static final Provider CUBE = createDefault(TextureMapping::cube, ModelTemplates.CUBE_ALL);
/*  12 */   public static final Provider CUBE_INNER_FACES = createDefault(TextureMapping::cube, ModelTemplates.CUBE_ALL_INNER_FACES);
/*  13 */   public static final Provider CUBE_MIRRORED = createDefault(TextureMapping::cube, ModelTemplates.CUBE_MIRRORED_ALL);
/*  14 */   public static final Provider COLUMN = createDefault(TextureMapping::column, ModelTemplates.CUBE_COLUMN);
/*  15 */   public static final Provider COLUMN_HORIZONTAL = createDefault(TextureMapping::column, ModelTemplates.CUBE_COLUMN_HORIZONTAL);
/*  16 */   public static final Provider CUBE_TOP_BOTTOM = createDefault(TextureMapping::cubeBottomTop, ModelTemplates.CUBE_BOTTOM_TOP);
/*  17 */   public static final Provider CUBE_TOP = createDefault(TextureMapping::cubeTop, ModelTemplates.CUBE_TOP);
/*     */   
/*  19 */   public static final Provider ORIENTABLE_ONLY_TOP = createDefault(TextureMapping::orientableCubeOnlyTop, ModelTemplates.CUBE_ORIENTABLE);
/*  20 */   public static final Provider ORIENTABLE = createDefault(TextureMapping::orientableCube, ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM);
/*     */   
/*  22 */   public static final Provider CARPET = createDefault(TextureMapping::wool, ModelTemplates.CARPET);
/*  23 */   public static final Provider MOSSY_CARPET_SIDE = createDefault(TextureMapping::side, ModelTemplates.MOSSY_CARPET_SIDE);
/*     */   
/*  25 */   public static final Provider FLOWERBED_1 = createDefault(TextureMapping::flowerbed, ModelTemplates.FLOWERBED_1);
/*  26 */   public static final Provider FLOWERBED_2 = createDefault(TextureMapping::flowerbed, ModelTemplates.FLOWERBED_2);
/*  27 */   public static final Provider FLOWERBED_3 = createDefault(TextureMapping::flowerbed, ModelTemplates.FLOWERBED_3);
/*  28 */   public static final Provider FLOWERBED_4 = createDefault(TextureMapping::flowerbed, ModelTemplates.FLOWERBED_4);
/*     */   
/*  30 */   public static final Provider LEAF_LITTER_1 = createDefault(TextureMapping::defaultTexture, ModelTemplates.LEAF_LITTER_1);
/*  31 */   public static final Provider LEAF_LITTER_2 = createDefault(TextureMapping::defaultTexture, ModelTemplates.LEAF_LITTER_2);
/*  32 */   public static final Provider LEAF_LITTER_3 = createDefault(TextureMapping::defaultTexture, ModelTemplates.LEAF_LITTER_3);
/*  33 */   public static final Provider LEAF_LITTER_4 = createDefault(TextureMapping::defaultTexture, ModelTemplates.LEAF_LITTER_4);
/*     */   
/*  35 */   public static final Provider GLAZED_TERRACOTTA = createDefault(TextureMapping::pattern, ModelTemplates.GLAZED_TERRACOTTA);
/*  36 */   public static final Provider CORAL_FAN = createDefault(TextureMapping::fan, ModelTemplates.CORAL_FAN);
/*  37 */   public static final Provider ANVIL = createDefault(TextureMapping::top, ModelTemplates.ANVIL);
/*  38 */   public static final Provider LEAVES = createDefault(TextureMapping::cube, ModelTemplates.LEAVES);
/*  39 */   public static final Provider LANTERN = createDefault(TextureMapping::lantern, ModelTemplates.LANTERN);
/*  40 */   public static final Provider HANGING_LANTERN = createDefault(TextureMapping::lantern, ModelTemplates.HANGING_LANTERN);
/*  41 */   public static final Provider CHAIN = createDefault(TextureMapping::defaultTexture, ModelTemplates.CHAIN);
/*  42 */   public static final Provider SEAGRASS = createDefault(TextureMapping::defaultTexture, ModelTemplates.SEAGRASS);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public static final Provider COLUMN_ALT = createDefault(TextureMapping::logColumn, ModelTemplates.CUBE_COLUMN);
/*  48 */   public static final Provider COLUMN_HORIZONTAL_ALT = createDefault(TextureMapping::logColumn, ModelTemplates.CUBE_COLUMN_HORIZONTAL);
/*     */ 
/*     */   
/*  51 */   public static final Provider TOP_BOTTOM_WITH_WALL = createDefault(TextureMapping::cubeBottomTopWithWall, ModelTemplates.CUBE_BOTTOM_TOP);
/*     */ 
/*     */   
/*  54 */   public static final Provider COLUMN_WITH_WALL = createDefault(TextureMapping::columnWithWall, ModelTemplates.CUBE_COLUMN);
/*     */   
/*     */   private final TextureMapping mapping;
/*     */   private final ModelTemplate template;
/*     */   
/*     */   private TexturedModel(TextureMapping mapping, ModelTemplate template) {
/*  60 */     this.mapping = mapping;
/*  61 */     this.template = template;
/*     */   }
/*     */   
/*     */   public ModelTemplate getTemplate() {
/*  65 */     return this.template;
/*     */   }
/*     */   
/*     */   public TextureMapping getMapping() {
/*  69 */     return this.mapping;
/*     */   }
/*     */   
/*     */   public TexturedModel updateTextures(Consumer<TextureMapping> mutator) {
/*  73 */     mutator.accept(this.mapping);
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public Identifier create(Block block, BiConsumer<Identifier, ModelInstance> modelOutput) {
/*  78 */     return this.template.create(block, this.mapping, modelOutput);
/*     */   }
/*     */   
/*     */   public Identifier createWithSuffix(Block block, String extraSuffix, BiConsumer<Identifier, ModelInstance> modelOutput) {
/*  82 */     return this.template.createWithSuffix(block, extraSuffix, this.mapping, modelOutput);
/*     */   }
/*     */   
/*     */   private static Provider createDefault(Function<Block, TextureMapping> mapping, ModelTemplate template) {
/*  86 */     return block -> new TexturedModel(mapping.apply(block), template);
/*     */   }
/*     */   
/*     */   public static TexturedModel createAllSame(Identifier id) {
/*  90 */     return new TexturedModel(TextureMapping.cube(id), ModelTemplates.CUBE_ALL);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Provider {
/*     */     TexturedModel get(Block param1Block);
/*     */     
/*     */     default Identifier create(Block block, BiConsumer<Identifier, ModelInstance> modelOutput) {
/*  98 */       return get(block).create(block, modelOutput);
/*     */     }
/*     */     
/*     */     default Identifier createWithSuffix(Block block, String suffix, BiConsumer<Identifier, ModelInstance> modelOutput) {
/* 102 */       return get(block).createWithSuffix(block, suffix, modelOutput);
/*     */     }
/*     */     
/*     */     default Provider updateTexture(Consumer<TextureMapping> mutator) {
/* 106 */       return block -> get(mutator).updateTextures(mutator);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/models/model/TexturedModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */