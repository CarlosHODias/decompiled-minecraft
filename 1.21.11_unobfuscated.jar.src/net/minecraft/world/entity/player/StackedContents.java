/*     */ package net.minecraft.world.entity.player;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterable;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ 
/*     */ public class StackedContents<T>
/*     */ {
/*  17 */   public final Reference2IntOpenHashMap<T> amounts = new Reference2IntOpenHashMap();
/*     */   
/*     */   private boolean hasAtLeast(T item, int count) {
/*  20 */     return (this.amounts.getInt(item) >= count);
/*     */   }
/*     */   
/*     */   private void take(T item, int amount) {
/*  24 */     int previous = this.amounts.addTo(item, -amount);
/*  25 */     if (previous < amount) {
/*  26 */       throw new IllegalStateException("Took " + amount + " items, but only had " + previous);
/*     */     }
/*     */   }
/*     */   
/*     */   private void put(T item, int count) {
/*  31 */     this.amounts.addTo(item, count);
/*     */   }
/*     */   
/*     */   public boolean tryPick(List<? extends IngredientInfo<T>> ingredients, int amount, Output<T> output) {
/*  35 */     return new RecipePicker(ingredients).tryPick(amount, output);
/*     */   }
/*     */   
/*     */   public int tryPickAll(List<? extends IngredientInfo<T>> ingredients, int maxSize, Output<T> output) {
/*  39 */     return new RecipePicker(ingredients).tryPickAll(maxSize, output);
/*     */   }
/*     */   
/*     */   public void clear() {
/*  43 */     this.amounts.clear();
/*     */   }
/*     */   
/*     */   public void account(T item, int count) {
/*  47 */     put(item, count);
/*     */   }
/*     */ 
/*     */   
/*     */   private List<T> getUniqueAvailableIngredientItems(Iterable<? extends IngredientInfo<T>> ingredients) {
/*  52 */     List<T> result = new ArrayList<>();
/*  53 */     for (ObjectIterator<Reference2IntMap.Entry<T>> objectIterator = Reference2IntMaps.fastIterable((Reference2IntMap)this.amounts).iterator(); objectIterator.hasNext(); ) { Reference2IntMap.Entry<T> availableItem = objectIterator.next();
/*  54 */       if (availableItem.getIntValue() > 0 && anyIngredientMatches(ingredients, (T)availableItem.getKey())) {
/*  55 */         result.add((T)availableItem.getKey());
/*     */       } }
/*     */     
/*  58 */     return result;
/*     */   }
/*     */   
/*     */   private static <T> boolean anyIngredientMatches(Iterable<? extends IngredientInfo<T>> ingredients, T item) {
/*  62 */     for (IngredientInfo<T> ingredient : ingredients) {
/*  63 */       if (ingredient.acceptsItem(item)) {
/*  64 */         return true;
/*     */       }
/*     */     } 
/*  67 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   public int getResultUpperBound(List<? extends IngredientInfo<T>> ingredients) {
/*  75 */     int min = Integer.MAX_VALUE;
/*     */     
/*  77 */     ObjectIterable<Reference2IntMap.Entry<T>> availableItems = Reference2IntMaps.fastIterable((Reference2IntMap)this.amounts);
/*  78 */     label20: for (IngredientInfo<T> ingredient : ingredients) {
/*  79 */       int max = 0;
/*     */       
/*  81 */       for (ObjectIterator<Reference2IntMap.Entry<T>> objectIterator = availableItems.iterator(); objectIterator.hasNext(); ) { Reference2IntMap.Entry<T> entry = objectIterator.next();
/*  82 */         int itemCount = entry.getIntValue();
/*  83 */         if (itemCount <= max) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/*  88 */         if (ingredient.acceptsItem((T)entry.getKey())) {
/*  89 */           max = itemCount;
/*     */         }
/*     */         
/*  92 */         if (max >= min) {
/*     */           continue label20;
/*     */         } }
/*     */ 
/*     */ 
/*     */       
/*  98 */       min = max;
/*  99 */       if (min == 0) {
/*     */         break;
/*     */       }
/*     */     } 
/* 103 */     return min;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class RecipePicker
/*     */   {
/*     */     private final List<? extends StackedContents.IngredientInfo<T>> ingredients;
/*     */ 
/*     */ 
/*     */     
/*     */     private final int ingredientCount;
/*     */ 
/*     */     
/*     */     private final List<T> items;
/*     */ 
/*     */     
/*     */     private final int itemCount;
/*     */ 
/*     */     
/*     */     private final BitSet data;
/*     */ 
/*     */     
/* 127 */     private final IntList path = (IntList)new IntArrayList();
/*     */     
/*     */     public RecipePicker(List<? extends StackedContents.IngredientInfo<T>> ingredients) {
/* 130 */       this.ingredients = ingredients;
/*     */       
/* 132 */       this.ingredientCount = ingredients.size();
/* 133 */       this.items = StackedContents.this.getUniqueAvailableIngredientItems(ingredients);
/* 134 */       this.itemCount = this.items.size();
/*     */       
/* 136 */       this.data = new BitSet(visitedIngredientCount() + visitedItemCount() + satisfiedCount() + connectionCount() + residualCount());
/* 137 */       setInitialConnections();
/*     */     }
/*     */     
/*     */     private void setInitialConnections() {
/* 141 */       for (int ingredient = 0; ingredient < this.ingredientCount; ingredient++) {
/* 142 */         StackedContents.IngredientInfo<T> ingredientInfo = this.ingredients.get(ingredient);
/* 143 */         for (int item = 0; item < this.itemCount; item++) {
/* 144 */           if (ingredientInfo.acceptsItem(this.items.get(item))) {
/* 145 */             setConnection(item, ingredient);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean tryPick(int capacity, StackedContents.Output<T> output) {
/* 159 */       if (capacity <= 0) {
/* 160 */         return true;
/*     */       }
/*     */       
/* 163 */       int satisfiedIngredientCount = 0;
/*     */       while (true) {
/* 165 */         IntList path = tryAssigningNewItem(capacity);
/* 166 */         if (path == null) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 173 */         int assignedItem = path.getInt(0);
/* 174 */         StackedContents.this.take(this.items.get(assignedItem), capacity);
/*     */ 
/*     */         
/* 177 */         int satisfiedIngredient = path.size() - 1;
/* 178 */         setSatisfied(path.getInt(satisfiedIngredient));
/* 179 */         satisfiedIngredientCount++;
/*     */ 
/*     */ 
/*     */         
/* 183 */         for (int i = 0; i < path.size() - 1; i++) {
/* 184 */           if (isPathIndexItem(i)) {
/* 185 */             int item = path.getInt(i);
/* 186 */             int j = path.getInt(i + 1);
/* 187 */             assign(item, j);
/*     */           } else {
/* 189 */             int item = path.getInt(i + 1);
/* 190 */             int j = path.getInt(i);
/* 191 */             unassign(item, j);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 196 */       boolean isValidAssignment = (satisfiedIngredientCount == this.ingredientCount);
/*     */       
/* 198 */       boolean hasOutput = (isValidAssignment && output != null);
/*     */ 
/*     */       
/* 201 */       clearAllVisited();
/* 202 */       clearSatisfied();
/*     */ 
/*     */       
/* 205 */       for (int ingredient = 0; ingredient < this.ingredientCount; ingredient++) {
/* 206 */         for (int item = 0; item < this.itemCount; item++) {
/* 207 */           if (isAssigned(item, ingredient)) {
/* 208 */             unassign(item, ingredient);
/* 209 */             StackedContents.this.put(this.items.get(item), capacity);
/*     */             
/* 211 */             if (hasOutput) {
/* 212 */               output.accept(this.items.get(item));
/*     */             }
/*     */ 
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 221 */       assert this.data.get(residualOffset(), residualOffset() + residualCount()).isEmpty();
/*     */       
/* 223 */       return isValidAssignment;
/*     */     }
/*     */     
/*     */     private static boolean isPathIndexItem(int index) {
/* 227 */       return ((index & 0x1) == 0);
/*     */     }
/*     */     
/*     */     private IntList tryAssigningNewItem(int capacity) {
/* 231 */       clearAllVisited();
/*     */       
/* 233 */       for (int item = 0; item < this.itemCount; item++) {
/* 234 */         if (StackedContents.this.hasAtLeast(this.items.get(item), capacity)) {
/* 235 */           IntList path = findNewItemAssignmentPath(item);
/* 236 */           if (path != null) {
/* 237 */             return path;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 242 */       return null;
/*     */     }
/*     */     
/*     */     private IntList findNewItemAssignmentPath(int startingItem) {
/* 246 */       this.path.clear();
/* 247 */       visitItem(startingItem);
/* 248 */       this.path.add(startingItem);
/*     */ 
/*     */       
/* 251 */       while (!this.path.isEmpty()) {
/* 252 */         int pathLength = this.path.size();
/* 253 */         if (isPathIndexItem(pathLength - 1)) {
/* 254 */           int itemToAssign = this.path.getInt(pathLength - 1);
/*     */ 
/*     */           
/* 257 */           for (int ingredient = 0; ingredient < this.ingredientCount; ingredient++) {
/* 258 */             if (!hasVisitedIngredient(ingredient) && hasConnection(itemToAssign, ingredient) && !isAssigned(itemToAssign, ingredient)) {
/* 259 */               visitIngredient(ingredient);
/* 260 */               this.path.add(ingredient);
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } else {
/* 265 */           int lastAssignedIngredient = this.path.getInt(pathLength - 1);
/*     */           
/* 267 */           if (!isSatisfied(lastAssignedIngredient)) {
/* 268 */             return this.path;
/*     */           }
/*     */ 
/*     */           
/* 272 */           for (int item = 0; item < this.itemCount; item++) {
/* 273 */             if (!hasVisitedItem(item) && isAssigned(item, lastAssignedIngredient)) {
/*     */               
/* 275 */               assert hasConnection(item, lastAssignedIngredient);
/* 276 */               visitItem(item);
/* 277 */               this.path.add(item);
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 284 */         int newLength = this.path.size();
/* 285 */         if (newLength == pathLength) {
/* 286 */           this.path.removeInt(newLength - 1);
/*     */         }
/*     */       } 
/*     */       
/* 290 */       return null;
/*     */     }
/*     */     
/*     */     private int visitedIngredientOffset() {
/* 294 */       return 0;
/*     */     }
/*     */     
/*     */     private int visitedIngredientCount() {
/* 298 */       return this.ingredientCount;
/*     */     }
/*     */     
/*     */     private int visitedItemOffset() {
/* 302 */       return visitedIngredientOffset() + visitedIngredientCount();
/*     */     }
/*     */     
/*     */     private int visitedItemCount() {
/* 306 */       return this.itemCount;
/*     */     }
/*     */     
/*     */     private int satisfiedOffset() {
/* 310 */       return visitedItemOffset() + visitedItemCount();
/*     */     }
/*     */     
/*     */     private int satisfiedCount() {
/* 314 */       return this.ingredientCount;
/*     */     }
/*     */     
/*     */     private int connectionOffset() {
/* 318 */       return satisfiedOffset() + satisfiedCount();
/*     */     }
/*     */     
/*     */     private int connectionCount() {
/* 322 */       return this.ingredientCount * this.itemCount;
/*     */     }
/*     */     
/*     */     private int residualOffset() {
/* 326 */       return connectionOffset() + connectionCount();
/*     */     }
/*     */     
/*     */     private int residualCount() {
/* 330 */       return this.ingredientCount * this.itemCount;
/*     */     }
/*     */     
/*     */     private boolean isSatisfied(int ingredient) {
/* 334 */       return this.data.get(getSatisfiedIndex(ingredient));
/*     */     }
/*     */     
/*     */     private void setSatisfied(int ingredient) {
/* 338 */       this.data.set(getSatisfiedIndex(ingredient));
/*     */     }
/*     */     
/*     */     private int getSatisfiedIndex(int ingredient) {
/* 342 */       assert ingredient >= 0 && ingredient < this.ingredientCount;
/* 343 */       return satisfiedOffset() + ingredient;
/*     */     }
/*     */     
/*     */     private void clearSatisfied() {
/* 347 */       clearRange(satisfiedOffset(), satisfiedCount());
/*     */     }
/*     */     
/*     */     private void setConnection(int item, int ingredient) {
/* 351 */       this.data.set(getConnectionIndex(item, ingredient));
/*     */     }
/*     */     
/*     */     private boolean hasConnection(int item, int ingredient) {
/* 355 */       return this.data.get(getConnectionIndex(item, ingredient));
/*     */     }
/*     */     
/*     */     private int getConnectionIndex(int item, int ingredient) {
/* 359 */       assert item >= 0 && item < this.itemCount;
/* 360 */       assert ingredient >= 0 && ingredient < this.ingredientCount;
/* 361 */       return connectionOffset() + item * this.ingredientCount + ingredient;
/*     */     }
/*     */     
/*     */     private boolean isAssigned(int item, int ingredient) {
/* 365 */       return this.data.get(getResidualIndex(item, ingredient));
/*     */     }
/*     */     
/*     */     private void assign(int item, int ingredient) {
/* 369 */       int residualIndex = getResidualIndex(item, ingredient);
/* 370 */       assert !this.data.get(residualIndex);
/* 371 */       this.data.set(residualIndex);
/*     */     }
/*     */     
/*     */     private void unassign(int item, int ingredient) {
/* 375 */       int residualIndex = getResidualIndex(item, ingredient);
/* 376 */       assert this.data.get(residualIndex);
/* 377 */       this.data.clear(residualIndex);
/*     */     }
/*     */     
/*     */     private int getResidualIndex(int item, int ingredient) {
/* 381 */       assert item >= 0 && item < this.itemCount;
/* 382 */       assert ingredient >= 0 && ingredient < this.ingredientCount;
/* 383 */       return residualOffset() + item * this.ingredientCount + ingredient;
/*     */     }
/*     */     
/*     */     private void visitIngredient(int item) {
/* 387 */       this.data.set(getVisitedIngredientIndex(item));
/*     */     }
/*     */     
/*     */     private boolean hasVisitedIngredient(int ingredient) {
/* 391 */       return this.data.get(getVisitedIngredientIndex(ingredient));
/*     */     }
/*     */     
/*     */     private int getVisitedIngredientIndex(int ingredient) {
/* 395 */       assert ingredient >= 0 && ingredient < this.ingredientCount;
/* 396 */       return visitedIngredientOffset() + ingredient;
/*     */     }
/*     */     
/*     */     private void visitItem(int item) {
/* 400 */       this.data.set(getVisitiedItemIndex(item));
/*     */     }
/*     */     
/*     */     private boolean hasVisitedItem(int item) {
/* 404 */       return this.data.get(getVisitiedItemIndex(item));
/*     */     }
/*     */     
/*     */     private int getVisitiedItemIndex(int item) {
/* 408 */       assert item >= 0 && item < this.itemCount;
/* 409 */       return visitedItemOffset() + item;
/*     */     }
/*     */     
/*     */     private void clearAllVisited() {
/* 413 */       clearRange(visitedIngredientOffset(), visitedIngredientCount());
/* 414 */       clearRange(visitedItemOffset(), visitedItemCount());
/*     */     }
/*     */     
/*     */     private void clearRange(int offset, int count) {
/* 418 */       this.data.clear(offset, offset + count);
/*     */     }
/*     */     public int tryPickAll(int maxSize, StackedContents.Output<T> output) {
/*     */       int mid;
/* 422 */       int min = 0;
/* 423 */       int max = Math.min(maxSize, StackedContents.this.getResultUpperBound(this.ingredients)) + 1;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 428 */         mid = (min + max) / 2;
/*     */         
/* 430 */         if (tryPick(mid, null)) {
/* 431 */           if (max - min <= 1) {
/*     */             break;
/*     */           }
/* 434 */           min = mid; continue;
/*     */         } 
/* 436 */         max = mid;
/*     */       } 
/*     */ 
/*     */       
/* 440 */       if (mid > 0) {
/* 441 */         tryPick(mid, output);
/*     */       }
/*     */       
/* 444 */       return mid;
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Output<T> {
/*     */     void accept(T param1T);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface IngredientInfo<T> {
/*     */     boolean acceptsItem(T param1T);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/player/StackedContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */