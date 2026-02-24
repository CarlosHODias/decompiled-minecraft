/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.ImmutableMultimap;
/*    */ import com.google.common.collect.Multimap;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ public class RecipeMap
/*    */ {
/* 15 */   public static final RecipeMap EMPTY = new RecipeMap((Multimap<RecipeType<?>, RecipeHolder<?>>)ImmutableMultimap.of(), Map.of());
/*    */   
/*    */   private final Multimap<RecipeType<?>, RecipeHolder<?>> byType;
/*    */   
/*    */   private final Map<ResourceKey<Recipe<?>>, RecipeHolder<?>> byKey;
/*    */   
/*    */   private RecipeMap(Multimap<RecipeType<?>, RecipeHolder<?>> byType, Map<ResourceKey<Recipe<?>>, RecipeHolder<?>> byKey) {
/* 22 */     this.byType = byType;
/* 23 */     this.byKey = byKey;
/*    */   }
/*    */   
/*    */   public static RecipeMap create(Iterable<RecipeHolder<?>> recipes) {
/* 27 */     ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> byType = ImmutableMultimap.builder();
/* 28 */     ImmutableMap.Builder<ResourceKey<Recipe<?>>, RecipeHolder<?>> byKey = ImmutableMap.builder();
/*    */     
/* 30 */     for (RecipeHolder<?> recipe : recipes) {
/* 31 */       byType.put(recipe.value().getType(), recipe);
/* 32 */       byKey.put(recipe.id(), recipe);
/*    */     } 
/*    */     
/* 35 */     return new RecipeMap((Multimap<RecipeType<?>, RecipeHolder<?>>)byType.build(), (Map<ResourceKey<Recipe<?>>, RecipeHolder<?>>)byKey.build());
/*    */   }
/*    */ 
/*    */   
/*    */   public <I extends RecipeInput, T extends Recipe<I>> Collection<RecipeHolder<T>> byType(RecipeType<T> type) {
/* 40 */     return this.byType.get(type);
/*    */   }
/*    */   
/*    */   public Collection<RecipeHolder<?>> values() {
/* 44 */     return this.byKey.values();
/*    */   }
/*    */   
/*    */   public RecipeHolder<?> byKey(ResourceKey<Recipe<?>> recipeId) {
/* 48 */     return this.byKey.get(recipeId);
/*    */   }
/*    */   
/*    */   public <I extends RecipeInput, T extends Recipe<I>> Stream<RecipeHolder<T>> getRecipesFor(RecipeType<T> type, I container, Level level) {
/* 52 */     if (container.isEmpty()) {
/* 53 */       return Stream.empty();
/*    */     }
/* 55 */     return byType(type).stream().filter(r -> r.value().matches(container, level));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/RecipeMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */