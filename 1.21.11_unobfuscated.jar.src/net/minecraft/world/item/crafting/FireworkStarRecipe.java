/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.world.item.DyeItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.FireworkExplosion;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class FireworkStarRecipe extends CustomRecipe {
/*  17 */   private static final Map<Item, FireworkExplosion.Shape> SHAPE_BY_ITEM = Map.of(Items.FIRE_CHARGE, FireworkExplosion.Shape.LARGE_BALL, Items.FEATHER, FireworkExplosion.Shape.BURST, Items.GOLD_NUGGET, FireworkExplosion.Shape.STAR, Items.SKELETON_SKULL, FireworkExplosion.Shape.CREEPER, Items.WITHER_SKELETON_SKULL, FireworkExplosion.Shape.CREEPER, Items.CREEPER_HEAD, FireworkExplosion.Shape.CREEPER, Items.PLAYER_HEAD, FireworkExplosion.Shape.CREEPER, Items.DRAGON_HEAD, FireworkExplosion.Shape.CREEPER, Items.ZOMBIE_HEAD, FireworkExplosion.Shape.CREEPER, Items.PIGLIN_HEAD, FireworkExplosion.Shape.CREEPER);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  30 */   private static final Ingredient TRAIL_INGREDIENT = Ingredient.of((ItemLike)Items.DIAMOND);
/*  31 */   private static final Ingredient TWINKLE_INGREDIENT = Ingredient.of((ItemLike)Items.GLOWSTONE_DUST);
/*     */   
/*  33 */   private static final Ingredient GUNPOWDER_INGREDIENT = Ingredient.of((ItemLike)Items.GUNPOWDER);
/*     */   
/*     */   public FireworkStarRecipe(CraftingBookCategory category) {
/*  36 */     super(category);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(CraftingInput input, Level level) {
/*  42 */     if (input.ingredientCount() < 2) {
/*  43 */       return false;
/*     */     }
/*     */     
/*     */     boolean gunPowder = false;
/*     */     
/*     */     boolean colors = false;
/*     */     boolean shape = false;
/*     */     boolean trail = false;
/*     */     boolean twinkle = false;
/*  52 */     for (int slot = 0; slot < input.size(); slot++) {
/*  53 */       ItemStack itemStack = input.getItem(slot);
/*  54 */       if (!itemStack.isEmpty())
/*     */       {
/*     */ 
/*     */         
/*  58 */         if (SHAPE_BY_ITEM.containsKey(itemStack.getItem())) {
/*  59 */           if (shape) {
/*  60 */             return false;
/*     */           }
/*  62 */           shape = true;
/*  63 */         } else if (TWINKLE_INGREDIENT.test(itemStack)) {
/*  64 */           if (twinkle) {
/*  65 */             return false;
/*     */           }
/*  67 */           twinkle = true;
/*  68 */         } else if (TRAIL_INGREDIENT.test(itemStack)) {
/*  69 */           if (trail) {
/*  70 */             return false;
/*     */           }
/*  72 */           trail = true;
/*  73 */         } else if (GUNPOWDER_INGREDIENT.test(itemStack)) {
/*  74 */           if (gunPowder) {
/*  75 */             return false;
/*     */           }
/*  77 */           gunPowder = true;
/*  78 */         } else if (itemStack.getItem() instanceof DyeItem) {
/*  79 */           colors = true;
/*     */         } else {
/*  81 */           return false;
/*     */         } 
/*     */       }
/*     */     } 
/*  85 */     return (gunPowder && colors);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
/*  90 */     FireworkExplosion.Shape shape = FireworkExplosion.Shape.SMALL_BALL;
/*     */     boolean hasTwinkle = false;
/*     */     boolean hasTrail = false;
/*  93 */     IntArrayList intArrayList = new IntArrayList();
/*     */     
/*  95 */     for (int slot = 0; slot < input.size(); slot++) {
/*  96 */       ItemStack itemStack = input.getItem(slot);
/*  97 */       if (!itemStack.isEmpty()) {
/*     */ 
/*     */ 
/*     */         
/* 101 */         FireworkExplosion.Shape maybeShape = SHAPE_BY_ITEM.get(itemStack.getItem());
/* 102 */         if (maybeShape != null)
/* 103 */         { shape = maybeShape; }
/* 104 */         else if (TWINKLE_INGREDIENT.test(itemStack))
/* 105 */         { hasTwinkle = true; }
/* 106 */         else if (TRAIL_INGREDIENT.test(itemStack))
/* 107 */         { hasTrail = true; }
/* 108 */         else { Item item = itemStack.getItem(); if (item instanceof DyeItem) { DyeItem dye = (DyeItem)item;
/* 109 */             intArrayList.add(dye.getDyeColor().getFireworkColor()); }
/*     */            }
/*     */       
/*     */       } 
/* 113 */     }  ItemStack star = new ItemStack((ItemLike)Items.FIREWORK_STAR);
/* 114 */     star.set(DataComponents.FIREWORK_EXPLOSION, new FireworkExplosion(shape, (IntList)intArrayList, IntList.of(), hasTrail, hasTwinkle));
/* 115 */     return star;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecipeSerializer<FireworkStarRecipe> getSerializer() {
/* 120 */     return RecipeSerializer.FIREWORK_STAR;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/FireworkStarRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */