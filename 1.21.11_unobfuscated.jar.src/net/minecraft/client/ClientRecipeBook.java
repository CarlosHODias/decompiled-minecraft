/*     */ package net.minecraft.client;
/*     */ import com.google.common.collect.HashBasedTable;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
/*     */ import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
/*     */ import net.minecraft.stats.RecipeBook;
/*     */ import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
/*     */ import net.minecraft.world.item.crafting.RecipeBookCategory;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*     */ 
/*     */ public class ClientRecipeBook extends RecipeBook {
/*  24 */   private final Map<RecipeDisplayId, RecipeDisplayEntry> known = new HashMap<>();
/*  25 */   private final Set<RecipeDisplayId> highlight = new HashSet<>();
/*     */   
/*  27 */   private Map<ExtendedRecipeBookCategory, List<RecipeCollection>> collectionsByTab = Map.of();
/*  28 */   private List<RecipeCollection> allCollections = List.of();
/*     */   
/*     */   public void add(RecipeDisplayEntry display) {
/*  31 */     this.known.put(display.id(), display);
/*     */   }
/*     */   
/*     */   public void remove(RecipeDisplayId id) {
/*  35 */     this.known.remove(id);
/*  36 */     this.highlight.remove(id);
/*     */   }
/*     */   
/*     */   public void clear() {
/*  40 */     this.known.clear();
/*  41 */     this.highlight.clear();
/*     */   }
/*     */   
/*     */   public boolean willHighlight(RecipeDisplayId recipe) {
/*  45 */     return this.highlight.contains(recipe);
/*     */   }
/*     */   
/*     */   public void removeHighlight(RecipeDisplayId id) {
/*  49 */     this.highlight.remove(id);
/*     */   }
/*     */   
/*     */   public void addHighlight(RecipeDisplayId id) {
/*  53 */     this.highlight.add(id);
/*     */   }
/*     */   
/*     */   public void rebuildCollections() {
/*  57 */     Map<RecipeBookCategory, List<List<RecipeDisplayEntry>>> recipeListsByCategory = categorizeAndGroupRecipes(this.known.values());
/*     */     
/*  59 */     Map<ExtendedRecipeBookCategory, List<RecipeCollection>> byCategory = new HashMap<>();
/*  60 */     ImmutableList.Builder<RecipeCollection> all = ImmutableList.builder();
/*     */     
/*  62 */     recipeListsByCategory.forEach((category, categoryRecipes) -> {
/*     */           Objects.requireNonNull(all);
/*     */           byCategory.put(category, (List)categoryRecipes.stream().map(RecipeCollection::new).peek(all::add).collect(ImmutableList.toImmutableList()));
/*     */         });
/*  66 */     for (SearchRecipeBookCategory searchCategory : SearchRecipeBookCategory.values()) {
/*  67 */       byCategory.put(searchCategory, (List<RecipeCollection>)searchCategory.includedCategories().stream().flatMap(subCategory -> ((List)byCategory.getOrDefault(subCategory, List.of())).stream()).collect(ImmutableList.toImmutableList()));
/*     */     }
/*     */     
/*  70 */     this.collectionsByTab = Map.copyOf(byCategory);
/*  71 */     this.allCollections = (List<RecipeCollection>)all.build();
/*     */   }
/*     */   
/*     */   private static Map<RecipeBookCategory, List<List<RecipeDisplayEntry>>> categorizeAndGroupRecipes(Iterable<RecipeDisplayEntry> recipes) {
/*  75 */     Map<RecipeBookCategory, List<List<RecipeDisplayEntry>>> result = new HashMap<>();
/*  76 */     HashBasedTable hashBasedTable = HashBasedTable.create();
/*     */     
/*  78 */     for (RecipeDisplayEntry entry : recipes) {
/*  79 */       RecipeBookCategory category = entry.category();
/*  80 */       OptionalInt groupId = entry.group();
/*     */       
/*  82 */       if (groupId.isEmpty()) {
/*     */         
/*  84 */         ((List)result.computeIfAbsent(category, key -> new ArrayList())).add(List.of(entry)); continue;
/*     */       } 
/*  86 */       List<RecipeDisplayEntry> groupRecipes = (List<RecipeDisplayEntry>)hashBasedTable.get(category, groupId.getAsInt());
/*  87 */       if (groupRecipes == null) {
/*  88 */         groupRecipes = new ArrayList<>();
/*  89 */         hashBasedTable.put(category, groupId.getAsInt(), groupRecipes);
/*  90 */         ((List<List<RecipeDisplayEntry>>)result.computeIfAbsent(category, key -> new ArrayList())).add(groupRecipes);
/*     */       } 
/*  92 */       groupRecipes.add(entry);
/*     */     } 
/*     */     
/*  95 */     return result;
/*     */   }
/*     */   
/*     */   public List<RecipeCollection> getCollections() {
/*  99 */     return this.allCollections;
/*     */   }
/*     */   
/*     */   public List<RecipeCollection> getCollection(ExtendedRecipeBookCategory category) {
/* 103 */     return this.collectionsByTab.getOrDefault(category, Collections.emptyList());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/ClientRecipeBook.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */