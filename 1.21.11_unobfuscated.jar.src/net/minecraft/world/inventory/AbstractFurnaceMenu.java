/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.recipebook.ServerPlaceRecipe;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.StackedItemContents;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.AbstractCookingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.item.crafting.SingleRecipeInput;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractFurnaceMenu
/*     */   extends RecipeBookMenu
/*     */ {
/*     */   public static final int INGREDIENT_SLOT = 0;
/*     */   public static final int FUEL_SLOT = 1;
/*     */   public static final int RESULT_SLOT = 2;
/*     */   public static final int SLOT_COUNT = 3;
/*     */   public static final int DATA_COUNT = 4;
/*     */   private static final int INV_SLOT_START = 3;
/*     */   private static final int INV_SLOT_END = 30;
/*     */   private static final int USE_ROW_SLOT_START = 30;
/*     */   private static final int USE_ROW_SLOT_END = 39;
/*     */   private final Container container;
/*     */   private final ContainerData data;
/*     */   protected final Level level;
/*     */   private final RecipeType<? extends AbstractCookingRecipe> recipeType;
/*     */   private final RecipePropertySet acceptedInputs;
/*     */   private final RecipeBookType recipeBookType;
/*     */   
/*     */   protected AbstractFurnaceMenu(MenuType<?> menuType, RecipeType<? extends AbstractCookingRecipe> recipeType, ResourceKey<RecipePropertySet> allowedInputs, RecipeBookType recipeBookType, int containerId, Inventory inventory) {
/*  48 */     this(menuType, recipeType, allowedInputs, recipeBookType, containerId, inventory, (Container)new SimpleContainer(3), new SimpleContainerData(4));
/*     */   }
/*     */   
/*     */   protected AbstractFurnaceMenu(MenuType<?> menuType, RecipeType<? extends AbstractCookingRecipe> recipeType, ResourceKey<RecipePropertySet> allowedInputs, RecipeBookType recipeBookType, int containerId, Inventory inventory, Container container, ContainerData data) {
/*  52 */     super(menuType, containerId);
/*  53 */     this.recipeType = recipeType;
/*  54 */     this.recipeBookType = recipeBookType;
/*  55 */     checkContainerSize(container, 3);
/*  56 */     checkContainerDataCount(data, 4);
/*  57 */     this.container = container;
/*  58 */     this.data = data;
/*  59 */     this.level = inventory.player.level();
/*     */     
/*  61 */     this.acceptedInputs = this.level.recipeAccess().propertySet(allowedInputs);
/*     */     
/*  63 */     addSlot(new Slot(container, 0, 56, 17));
/*  64 */     addSlot(new FurnaceFuelSlot(this, container, 1, 56, 53));
/*  65 */     addSlot(new FurnaceResultSlot(inventory.player, container, 2, 116, 35));
/*     */     
/*  67 */     addStandardInventorySlots((Container)inventory, 8, 84);
/*     */     
/*  69 */     addDataSlots(data);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillCraftSlotsStackedContents(StackedItemContents stackedContents) {
/*  74 */     if (this.container instanceof StackedContentsCompatible) {
/*  75 */       ((StackedContentsCompatible)this.container).fillStackedContents(stackedContents);
/*     */     }
/*     */   }
/*     */   
/*     */   public Slot getResultSlot() {
/*  80 */     return (Slot)this.slots.get(2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player player) {
/*  85 */     return this.container.stillValid(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/*  90 */     ItemStack clicked = ItemStack.EMPTY;
/*  91 */     Slot slot = (Slot)this.slots.get(slotIndex);
/*  92 */     if (slot != null && slot.hasItem()) {
/*  93 */       ItemStack stack = slot.getItem();
/*  94 */       clicked = stack.copy();
/*     */       
/*  96 */       if (slotIndex == 2) {
/*  97 */         if (!moveItemStackTo(stack, 3, 39, true)) {
/*  98 */           return ItemStack.EMPTY;
/*     */         }
/* 100 */         slot.onQuickCraft(stack, clicked);
/* 101 */       } else if (slotIndex == 1 || slotIndex == 0) {
/* 102 */         if (!moveItemStackTo(stack, 3, 39, false)) {
/* 103 */           return ItemStack.EMPTY;
/*     */         }
/* 105 */       } else if (canSmelt(stack)) {
/* 106 */         if (!moveItemStackTo(stack, 0, 1, false)) {
/* 107 */           return ItemStack.EMPTY;
/*     */         }
/* 109 */       } else if (isFuel(stack)) {
/* 110 */         if (!moveItemStackTo(stack, 1, 2, false)) {
/* 111 */           return ItemStack.EMPTY;
/*     */         }
/* 113 */       } else if (slotIndex >= 3 && slotIndex < 30) {
/* 114 */         if (!moveItemStackTo(stack, 30, 39, false)) {
/* 115 */           return ItemStack.EMPTY;
/*     */         }
/* 117 */       } else if (slotIndex >= 30 && slotIndex < 39 && 
/* 118 */         !moveItemStackTo(stack, 3, 30, false)) {
/* 119 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/* 122 */       if (stack.isEmpty()) {
/* 123 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       } else {
/* 125 */         slot.setChanged();
/*     */       } 
/* 127 */       if (stack.getCount() == clicked.getCount()) {
/* 128 */         return ItemStack.EMPTY;
/*     */       }
/* 130 */       slot.onTake(player, stack);
/*     */     } 
/*     */     
/* 133 */     return clicked;
/*     */   }
/*     */   
/*     */   protected boolean canSmelt(ItemStack itemStack) {
/* 137 */     return this.acceptedInputs.test(itemStack);
/*     */   }
/*     */   
/*     */   protected boolean isFuel(ItemStack itemStack) {
/* 141 */     return this.level.fuelValues().isFuel(itemStack);
/*     */   }
/*     */   
/*     */   public float getBurnProgress() {
/* 145 */     int current = this.data.get(2);
/* 146 */     int total = this.data.get(3);
/* 147 */     if (total == 0 || current == 0) {
/* 148 */       return 0.0F;
/*     */     }
/* 150 */     return Mth.clamp(current / total, 0.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getLitProgress() {
/* 155 */     int litDuration = this.data.get(1);
/* 156 */     if (litDuration == 0) {
/* 157 */       litDuration = 200;
/*     */     }
/* 159 */     return Mth.clamp(this.data.get(0) / litDuration, 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public boolean isLit() {
/* 163 */     return (this.data.get(0) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public RecipeBookType getRecipeBookType() {
/* 168 */     return this.recipeBookType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecipeBookMenu.PostPlaceAction handlePlacement(boolean useMaxItems, boolean allowDroppingItemsToClear, RecipeHolder<?> recipe, final ServerLevel level, Inventory inventory) {
/* 176 */     final List<Slot> slotsToClear = List.of(getSlot(0), getSlot(2));
/* 177 */     RecipeHolder<AbstractCookingRecipe> typedRecipe = (RecipeHolder)recipe;
/* 178 */     return ServerPlaceRecipe.placeRecipe(new ServerPlaceRecipe.CraftingMenuAccess<AbstractCookingRecipe>()
/*     */         {
/*     */           public void fillCraftSlotsStackedContents(StackedItemContents stackedContents)
/*     */           {
/* 182 */             AbstractFurnaceMenu.this.fillCraftSlotsStackedContents(stackedContents);
/*     */           }
/*     */ 
/*     */           
/*     */           public void clearCraftingContent() {
/* 187 */             slotsToClear.forEach(s -> s.set(ItemStack.EMPTY));
/*     */           }
/*     */ 
/*     */           
/*     */           public boolean recipeMatches(RecipeHolder<AbstractCookingRecipe> recipe) {
/* 192 */             return ((AbstractCookingRecipe)recipe.value()).matches(new SingleRecipeInput(AbstractFurnaceMenu.this.container.getItem(0)), (Level)level);
/*     */           }
/*     */         }, 
/*     */         
/* 196 */         1, 1, List.of(getSlot(0)), slotsToClear, inventory, typedRecipe, useMaxItems, allowDroppingItemsToClear);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/AbstractFurnaceMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */