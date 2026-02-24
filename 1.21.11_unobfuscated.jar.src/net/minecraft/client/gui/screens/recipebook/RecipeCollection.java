/*    */ package net.minecraft.client.gui.screens.recipebook;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.entity.player.StackedItemContents;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*    */ 
/*    */ public class RecipeCollection
/*    */ {
/* 16 */   public static final RecipeCollection EMPTY = new RecipeCollection(List.of());
/*    */ 
/*    */   
/*    */   private final List<RecipeDisplayEntry> entries;
/*    */   
/* 21 */   private final Set<RecipeDisplayId> craftable = new HashSet<>();
/* 22 */   private final Set<RecipeDisplayId> selected = new HashSet<>();
/*    */   
/*    */   public RecipeCollection(List<RecipeDisplayEntry> recipes) {
/* 25 */     this.entries = recipes;
/*    */   }
/*    */   
/*    */   public void selectRecipes(StackedItemContents stackedContents, Predicate<RecipeDisplay> selector) {
/* 29 */     for (RecipeDisplayEntry entry : this.entries) {
/* 30 */       boolean isSelected = selector.test(entry.display());
/* 31 */       if (isSelected) {
/* 32 */         this.selected.add(entry.id());
/*    */       } else {
/* 34 */         this.selected.remove(entry.id());
/*    */       } 
/* 36 */       if (isSelected && entry.canCraft(stackedContents)) {
/* 37 */         this.craftable.add(entry.id()); continue;
/*    */       } 
/* 39 */       this.craftable.remove(entry.id());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCraftable(RecipeDisplayId recipe) {
/* 45 */     return this.craftable.contains(recipe);
/*    */   }
/*    */   
/*    */   public boolean hasCraftable() {
/* 49 */     return !this.craftable.isEmpty();
/*    */   }
/*    */   
/*    */   public boolean hasAnySelected() {
/* 53 */     return !this.selected.isEmpty();
/*    */   }
/*    */   
/*    */   public List<RecipeDisplayEntry> getRecipes() {
/* 57 */     return this.entries;
/*    */   }
/*    */   
/*    */   public enum CraftableStatus {
/* 61 */     ANY,
/* 62 */     CRAFTABLE,
/* 63 */     NOT_CRAFTABLE;
/*    */   }
/*    */   
/*    */   public List<RecipeDisplayEntry> getSelectedRecipes(CraftableStatus selector) {
/* 67 */     switch (selector.ordinal()) { default: throw new MatchException(null, null);
/* 68 */       case 0: Objects.requireNonNull(this.selected);
/*    */       case 1:
/* 70 */         Objects.requireNonNull(this.craftable);
/* 71 */       case 2: break; }  Predicate<RecipeDisplayId> predicate = recipe -> (this.selected.contains(recipe) && !this.craftable.contains(recipe));
/*    */ 
/*    */     
/* 74 */     List<RecipeDisplayEntry> result = new ArrayList<>();
/* 75 */     for (RecipeDisplayEntry entries : this.entries) {
/* 76 */       if (predicate.test(entries.id())) {
/* 77 */         result.add(entries);
/*    */       }
/*    */     } 
/*    */     
/* 81 */     return result;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/recipebook/RecipeCollection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */