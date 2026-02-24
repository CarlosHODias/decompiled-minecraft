/*     */ package net.minecraft.client.multiplayer;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.ClientRecipeBook;
/*     */ import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
/*     */ import net.minecraft.client.searchtree.FullTextSearchTree;
/*     */ import net.minecraft.client.searchtree.IdSearchTree;
/*     */ import net.minecraft.client.searchtree.SearchTree;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.TooltipFlag;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplayContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class SessionSearchTrees {
/*  32 */   private static final Key RECIPE_COLLECTIONS = new Key(); private static class Key {}
/*  33 */   private static final Key CREATIVE_NAMES = new Key();
/*  34 */   private static final Key CREATIVE_TAGS = new Key();
/*     */   
/*  36 */   private CompletableFuture<SearchTree<ItemStack>> creativeByNameSearch = CompletableFuture.completedFuture(SearchTree.empty());
/*  37 */   private CompletableFuture<SearchTree<ItemStack>> creativeByTagSearch = CompletableFuture.completedFuture(SearchTree.empty());
/*  38 */   private CompletableFuture<SearchTree<RecipeCollection>> recipeSearch = CompletableFuture.completedFuture(SearchTree.empty());
/*     */   
/*  40 */   private final Map<Key, Runnable> reloaders = new IdentityHashMap<>();
/*     */   
/*     */   private void register(Key location, Runnable updater) {
/*  43 */     updater.run();
/*  44 */     this.reloaders.put(location, updater);
/*     */   }
/*     */   
/*     */   public void rebuildAfterLanguageChange() {
/*  48 */     for (Runnable value : this.reloaders.values()) {
/*  49 */       value.run();
/*     */     }
/*     */   }
/*     */   
/*     */   private static Stream<String> getTooltipLines(Stream<ItemStack> items, Item.TooltipContext context, TooltipFlag flag) {
/*  54 */     return 
/*  55 */       items.flatMap(item -> item.getTooltipLines(context, null, flag).stream())
/*  56 */       .map(l -> ChatFormatting.stripFormatting(l.getString()).trim())
/*  57 */       .filter(s -> !s.isEmpty());
/*     */   }
/*     */   
/*     */   public void updateRecipes(ClientRecipeBook recipeBook, Level level) {
/*  61 */     register(RECIPE_COLLECTIONS, () -> {
/*     */           List<RecipeCollection> recipes = recipeBook.getCollections();
/*     */           RegistryAccess registryAccess = level.registryAccess();
/*     */           Registry<Item> itemRegistries = registryAccess.lookupOrThrow(Registries.ITEM);
/*     */           Item.TooltipContext tooltipContext = Item.TooltipContext.of((HolderLookup.Provider)registryAccess);
/*     */           ContextMap recipeContext = SlotDisplayContext.fromLevel(level);
/*     */           TooltipFlag.Default default_ = TooltipFlag.Default.NORMAL;
/*     */           CompletableFuture<?> previous = this.recipeSearch;
/*     */           this.recipeSearch = CompletableFuture.supplyAsync((), (Executor)Util.backgroundExecutor());
/*     */           previous.cancel(true);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SearchTree<RecipeCollection> recipes() {
/*  81 */     return this.recipeSearch.join();
/*     */   }
/*     */   
/*     */   public void updateCreativeTags(List<ItemStack> items) {
/*  85 */     register(CREATIVE_TAGS, () -> {
/*     */           CompletableFuture<?> previous = this.creativeByTagSearch;
/*     */           this.creativeByTagSearch = CompletableFuture.supplyAsync((), (Executor)Util.backgroundExecutor());
/*     */           previous.cancel(true);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SearchTree<ItemStack> creativeTagSearch() {
/*  97 */     return this.creativeByTagSearch.join();
/*     */   }
/*     */   
/*     */   public void updateCreativeTooltips(HolderLookup.Provider registries, List<ItemStack> itemStacks) {
/* 101 */     register(CREATIVE_NAMES, () -> {
/*     */           Item.TooltipContext tooltipContext = Item.TooltipContext.of(registries);
/*     */           TooltipFlag.Default default_ = TooltipFlag.Default.NORMAL.asCreative();
/*     */           CompletableFuture<?> previous = this.creativeByNameSearch;
/*     */           this.creativeByNameSearch = CompletableFuture.supplyAsync((), (Executor)Util.backgroundExecutor());
/*     */           previous.cancel(true);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SearchTree<ItemStack> creativeNameSearch() {
/* 116 */     return this.creativeByNameSearch.join();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/SessionSearchTrees.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */