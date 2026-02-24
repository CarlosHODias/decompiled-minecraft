/*     */ package net.minecraft.data.recipes;
/*     */ 
/*     */ import java.util.ArrayList;
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
/*     */ import net.minecraft.world.item.crafting.ShapelessRecipe;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public class ShapelessRecipeBuilder
/*     */   implements RecipeBuilder {
/*     */   private final HolderGetter<Item> items;
/*     */   private final RecipeCategory category;
/*     */   private final ItemStack result;
/*  29 */   private final List<Ingredient> ingredients = new ArrayList<>();
/*  30 */   private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
/*     */   private String group;
/*     */   
/*     */   private ShapelessRecipeBuilder(HolderGetter<Item> items, RecipeCategory category, ItemStack result) {
/*  34 */     this.items = items;
/*  35 */     this.category = category;
/*  36 */     this.result = result;
/*     */   }
/*     */   
/*     */   public static ShapelessRecipeBuilder shapeless(HolderGetter<Item> items, RecipeCategory category, ItemStack result) {
/*  40 */     return new ShapelessRecipeBuilder(items, category, result);
/*     */   }
/*     */   
/*     */   public static ShapelessRecipeBuilder shapeless(HolderGetter<Item> items, RecipeCategory category, ItemLike item) {
/*  44 */     return shapeless(items, category, item, 1);
/*     */   }
/*     */   
/*     */   public static ShapelessRecipeBuilder shapeless(HolderGetter<Item> items, RecipeCategory category, ItemLike item, int count) {
/*  48 */     return new ShapelessRecipeBuilder(items, category, item.asItem().getDefaultInstance().copyWithCount(count));
/*     */   }
/*     */   
/*     */   public ShapelessRecipeBuilder requires(TagKey<Item> tag) {
/*  52 */     return requires(Ingredient.of((HolderSet)this.items.getOrThrow(tag)));
/*     */   }
/*     */   
/*     */   public ShapelessRecipeBuilder requires(ItemLike item) {
/*  56 */     return requires(item, 1);
/*     */   }
/*     */   
/*     */   public ShapelessRecipeBuilder requires(ItemLike item, int count) {
/*  60 */     for (int i = 0; i < count; i++) {
/*  61 */       requires(Ingredient.of(item));
/*     */     }
/*  63 */     return this;
/*     */   }
/*     */   
/*     */   public ShapelessRecipeBuilder requires(Ingredient ingredient) {
/*  67 */     return requires(ingredient, 1);
/*     */   }
/*     */   
/*     */   public ShapelessRecipeBuilder requires(Ingredient ingredient, int count) {
/*  71 */     for (int i = 0; i < count; i++) {
/*  72 */       this.ingredients.add(ingredient);
/*     */     }
/*  74 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ShapelessRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
/*  79 */     this.criteria.put(name, criterion);
/*  80 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ShapelessRecipeBuilder group(String group) {
/*  85 */     this.group = group;
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getResult() {
/*  91 */     return this.result.getItem();
/*     */   }
/*     */ 
/*     */   
/*     */   public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
/*  96 */     ensureValid(id);
/*  97 */     Advancement.Builder advancement = output.advancement()
/*  98 */       .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
/*  99 */       .rewards(AdvancementRewards.Builder.recipe(id))
/* 100 */       .requirements(AdvancementRequirements.Strategy.OR);
/* 101 */     Objects.requireNonNull(advancement); this.criteria.forEach(advancement::addCriterion);
/* 102 */     ShapelessRecipe recipe = new ShapelessRecipe(
/* 103 */         Objects.<String>requireNonNullElse(this.group, ""), 
/* 104 */         RecipeBuilder.determineBookCategory(this.category), this.result, this.ingredients);
/*     */ 
/*     */ 
/*     */     
/* 108 */     output.accept(id, (Recipe<?>)recipe, advancement.build(id.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
/*     */   }
/*     */   
/*     */   private void ensureValid(ResourceKey<Recipe<?>> id) {
/* 112 */     if (this.criteria.isEmpty())
/* 113 */       throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(id.identifier())); 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/recipes/ShapelessRecipeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */