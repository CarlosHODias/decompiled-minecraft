/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.RecipeAccess;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipeInput;
/*     */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.item.crafting.SmithingRecipe;
/*     */ import net.minecraft.world.item.crafting.SmithingRecipeInput;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class SmithingMenu extends ItemCombinerMenu {
/*     */   public static final int TEMPLATE_SLOT = 0;
/*     */   public static final int BASE_SLOT = 1;
/*     */   public static final int ADDITIONAL_SLOT = 2;
/*     */   public static final int RESULT_SLOT = 3;
/*     */   public static final int TEMPLATE_SLOT_X_PLACEMENT = 8;
/*     */   public static final int BASE_SLOT_X_PLACEMENT = 26;
/*     */   public static final int ADDITIONAL_SLOT_X_PLACEMENT = 44;
/*     */   private static final int RESULT_SLOT_X_PLACEMENT = 98;
/*     */   public static final int SLOT_Y_PLACEMENT = 48;
/*     */   private final Level level;
/*     */   private final RecipePropertySet baseItemTest;
/*     */   private final RecipePropertySet templateItemTest;
/*     */   private final RecipePropertySet additionItemTest;
/*  38 */   private final DataSlot hasRecipeError = DataSlot.standalone();
/*     */   
/*     */   public SmithingMenu(int containerId, Inventory inventory) {
/*  41 */     this(containerId, inventory, ContainerLevelAccess.NULL);
/*     */   }
/*     */   
/*     */   public SmithingMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
/*  45 */     this(containerId, inventory, access, inventory.player.level());
/*     */   }
/*     */   
/*     */   private SmithingMenu(int containerId, Inventory inventory, ContainerLevelAccess access, Level level) {
/*  49 */     super(MenuType.SMITHING, containerId, inventory, access, createInputSlotDefinitions(level.recipeAccess()));
/*  50 */     this.level = level;
/*     */     
/*  52 */     this.baseItemTest = level.recipeAccess().propertySet(RecipePropertySet.SMITHING_BASE);
/*  53 */     this.templateItemTest = level.recipeAccess().propertySet(RecipePropertySet.SMITHING_TEMPLATE);
/*  54 */     this.additionItemTest = level.recipeAccess().propertySet(RecipePropertySet.SMITHING_ADDITION);
/*     */     
/*  56 */     addDataSlot(this.hasRecipeError).set(0);
/*     */   }
/*     */   
/*     */   private static ItemCombinerMenuSlotDefinition createInputSlotDefinitions(RecipeAccess recipes) {
/*  60 */     RecipePropertySet baseItemTest = recipes.propertySet(RecipePropertySet.SMITHING_BASE);
/*  61 */     RecipePropertySet templateItemTest = recipes.propertySet(RecipePropertySet.SMITHING_TEMPLATE);
/*  62 */     RecipePropertySet additionItemTest = recipes.propertySet(RecipePropertySet.SMITHING_ADDITION);
/*     */ 
/*     */     
/*  65 */     Objects.requireNonNull(templateItemTest);
/*  66 */     Objects.requireNonNull(baseItemTest);
/*  67 */     Objects.requireNonNull(additionItemTest); return ItemCombinerMenuSlotDefinition.create().withSlot(0, 8, 48, templateItemTest::test).withSlot(1, 26, 48, baseItemTest::test).withSlot(2, 44, 48, additionItemTest::test)
/*  68 */       .withResultSlot(3, 98, 48)
/*  69 */       .build();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isValidBlock(BlockState state) {
/*  74 */     return state.is(Blocks.SMITHING_TABLE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onTake(Player player, ItemStack carried) {
/*  79 */     carried.onCraftedBy(player, carried.getCount());
/*  80 */     this.resultSlots.awardUsedRecipes(player, getRelevantItems());
/*     */ 
/*     */     
/*  83 */     shrinkStackInSlot(0);
/*  84 */     shrinkStackInSlot(1);
/*  85 */     shrinkStackInSlot(2);
/*     */     
/*  87 */     this.access.execute((level, pos) -> level.levelEvent(1044, pos, 0));
/*     */   }
/*     */   
/*     */   private List<ItemStack> getRelevantItems() {
/*  91 */     return List.of(
/*  92 */         this.inputSlots.getItem(0), 
/*  93 */         this.inputSlots.getItem(1), 
/*  94 */         this.inputSlots.getItem(2));
/*     */   }
/*     */ 
/*     */   
/*     */   private SmithingRecipeInput createRecipeInput() {
/*  99 */     return new SmithingRecipeInput(
/* 100 */         this.inputSlots.getItem(0), 
/* 101 */         this.inputSlots.getItem(1), 
/* 102 */         this.inputSlots.getItem(2));
/*     */   }
/*     */ 
/*     */   
/*     */   private void shrinkStackInSlot(int slot) {
/* 107 */     ItemStack stack = this.inputSlots.getItem(slot);
/* 108 */     if (!stack.isEmpty()) {
/* 109 */       stack.shrink(1);
/* 110 */       this.inputSlots.setItem(slot, stack);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container container) {
/* 116 */     super.slotsChanged(container);
/*     */     
/* 118 */     if (this.level instanceof ServerLevel) {
/* 119 */       boolean hasRecipeError = (getSlot(0).hasItem() && 
/* 120 */         getSlot(1).hasItem() && 
/* 121 */         getSlot(2).hasItem() && 
/* 122 */         !getSlot(getResultSlot()).hasItem());
/* 123 */       this.hasRecipeError.set(hasRecipeError ? 1 : 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void createResult() {
/*     */     Optional<RecipeHolder<SmithingRecipe>> foundRecipe;
/* 129 */     SmithingRecipeInput input = createRecipeInput();
/*     */ 
/*     */     
/* 132 */     Level level = this.level; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 133 */       foundRecipe = serverLevel.recipeAccess().getRecipeFor(RecipeType.SMITHING, (RecipeInput)input, (Level)serverLevel); }
/*     */     
/*     */     else
/*     */     
/* 137 */     { foundRecipe = Optional.empty(); }
/*     */ 
/*     */     
/* 140 */     foundRecipe.ifPresentOrElse(recipe -> {
/*     */           ItemStack result = ((SmithingRecipe)input.value()).assemble((RecipeInput)input, (HolderLookup.Provider)this.level.registryAccess());
/*     */           this.resultSlots.setRecipeUsed(input);
/*     */           this.resultSlots.setItem(0, result);
/*     */         }, () -> {
/*     */           this.resultSlots.setRecipeUsed(null);
/*     */           this.resultSlots.setItem(0, ItemStack.EMPTY);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canTakeItemForPickAll(ItemStack carried, Slot target) {
/* 155 */     return (target.container != this.resultSlots && super.canTakeItemForPickAll(carried, target));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canMoveIntoInputSlots(ItemStack stack) {
/* 160 */     if (this.templateItemTest.test(stack) && !getSlot(0).hasItem()) {
/* 161 */       return true;
/*     */     }
/* 163 */     if (this.baseItemTest.test(stack) && !getSlot(1).hasItem()) {
/* 164 */       return true;
/*     */     }
/* 166 */     if (this.additionItemTest.test(stack) && !getSlot(2).hasItem()) {
/* 167 */       return true;
/*     */     }
/* 169 */     return false;
/*     */   }
/*     */   
/*     */   public boolean hasRecipeError() {
/* 173 */     return (this.hasRecipeError.get() > 0);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/SmithingMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */