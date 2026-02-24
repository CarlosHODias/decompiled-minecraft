/*     */ package net.minecraft.data.recipes;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementRequirements;
/*     */ import net.minecraft.advancements.AdvancementRewards;
/*     */ import net.minecraft.advancements.Criterion;
/*     */ import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.crafting.ShapedRecipe;
/*     */ import net.minecraft.world.item.crafting.ShapedRecipePattern;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public class ShapedRecipeBuilder
/*     */   implements RecipeBuilder {
/*     */   private final HolderGetter<Item> items;
/*     */   private final RecipeCategory category;
/*     */   private final Item result;
/*     */   private final int count;
/*  32 */   private final List<String> rows = Lists.newArrayList();
/*  33 */   private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
/*  34 */   private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
/*     */   private String group;
/*     */   private boolean showNotification = true;
/*     */   
/*     */   private ShapedRecipeBuilder(HolderGetter<Item> items, RecipeCategory category, ItemLike result, int count) {
/*  39 */     this.items = items;
/*  40 */     this.category = category;
/*  41 */     this.result = result.asItem();
/*  42 */     this.count = count;
/*     */   }
/*     */   
/*     */   public static ShapedRecipeBuilder shaped(HolderGetter<Item> items, RecipeCategory category, ItemLike item) {
/*  46 */     return shaped(items, category, item, 1);
/*     */   }
/*     */   
/*     */   public static ShapedRecipeBuilder shaped(HolderGetter<Item> items, RecipeCategory category, ItemLike item, int count) {
/*  50 */     return new ShapedRecipeBuilder(items, category, item, count);
/*     */   }
/*     */   
/*     */   public ShapedRecipeBuilder define(Character symbol, TagKey<Item> tag) {
/*  54 */     return define(symbol, Ingredient.of((HolderSet)this.items.getOrThrow(tag)));
/*     */   }
/*     */   
/*     */   public ShapedRecipeBuilder define(Character symbol, ItemLike item) {
/*  58 */     return define(symbol, Ingredient.of(item));
/*     */   }
/*     */   
/*     */   public ShapedRecipeBuilder define(Character symbol, Ingredient ingredient) {
/*  62 */     if (this.key.containsKey(symbol)) {
/*  63 */       throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
/*     */     }
/*  65 */     if (symbol == ' ') {
/*  66 */       throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
/*     */     }
/*  68 */     this.key.put(symbol, ingredient);
/*  69 */     return this;
/*     */   }
/*     */   
/*     */   public ShapedRecipeBuilder pattern(String row) {
/*  73 */     if (!this.rows.isEmpty() && row.length() != ((String)this.rows.get(0)).length()) {
/*  74 */       throw new IllegalArgumentException("Pattern must be the same width on every line!");
/*     */     }
/*  76 */     this.rows.add(row);
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ShapedRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
/*  82 */     this.criteria.put(name, criterion);
/*  83 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ShapedRecipeBuilder group(String group) {
/*  88 */     this.group = group;
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public ShapedRecipeBuilder showNotification(boolean showNotification) {
/*  93 */     this.showNotification = showNotification;
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getResult() {
/*  99 */     return this.result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
/* 104 */     ShapedRecipePattern pattern = ensureValid(id);
/* 105 */     Advancement.Builder advancement = output.advancement()
/* 106 */       .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
/* 107 */       .rewards(AdvancementRewards.Builder.recipe(id))
/* 108 */       .requirements(AdvancementRequirements.Strategy.OR);
/* 109 */     Objects.requireNonNull(advancement); this.criteria.forEach(advancement::addCriterion);
/* 110 */     ShapedRecipe recipe = new ShapedRecipe(
/* 111 */         Objects.<String>requireNonNullElse(this.group, ""), 
/* 112 */         RecipeBuilder.determineBookCategory(this.category), pattern, new ItemStack((ItemLike)this.result, this.count), this.showNotification);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 117 */     output.accept(id, (Recipe<?>)recipe, advancement.build(id.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
/*     */   }
/*     */   
/*     */   private ShapedRecipePattern ensureValid(ResourceKey<Recipe<?>> id) {
/* 121 */     if (this.criteria.isEmpty()) {
/* 122 */       throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(id.identifier()));
/*     */     }
/* 124 */     return ShapedRecipePattern.of(this.key, this.rows);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/recipes/ShapedRecipeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */