/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.inventory.tooltip.TooltipComponent;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*     */ import org.apache.commons.lang3.math.Fraction;
/*     */ 
/*     */ public final class BundleContents implements TooltipComponent {
/*     */   public static final Codec<BundleContents> CODEC;
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, BundleContents> STREAM_CODEC;
/*  24 */   public static final BundleContents EMPTY = new BundleContents(List.of());
/*     */   static {
/*  26 */     CODEC = ItemStack.CODEC.listOf().flatXmap(BundleContents::checkAndCreate, contents -> DataResult.success(contents.items));
/*  27 */     STREAM_CODEC = ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()).map(BundleContents::new, contents -> contents.items);
/*     */   }
/*  29 */   private static final Fraction BUNDLE_IN_BUNDLE_WEIGHT = Fraction.getFraction(1, 16);
/*     */   
/*     */   private static final int NO_STACK_INDEX = -1;
/*     */   public static final int NO_SELECTED_ITEM_INDEX = -1;
/*     */   private final List<ItemStack> items;
/*     */   private final Fraction weight;
/*     */   private final int selectedItem;
/*     */   
/*     */   private BundleContents(List<ItemStack> items, Fraction weight, int selectedItem) {
/*  38 */     this.items = items;
/*  39 */     this.weight = weight;
/*  40 */     this.selectedItem = selectedItem;
/*     */   }
/*     */   
/*     */   private static DataResult<BundleContents> checkAndCreate(List<ItemStack> items) {
/*     */     try {
/*  45 */       Fraction weight = computeContentWeight(items);
/*  46 */       return DataResult.success(new BundleContents(items, weight, -1));
/*  47 */     } catch (ArithmeticException exception) {
/*  48 */       return DataResult.error(() -> "Excessive total bundle weight");
/*     */     } 
/*     */   }
/*     */   
/*     */   public BundleContents(List<ItemStack> items) {
/*  53 */     this(items, computeContentWeight(items), -1);
/*     */   }
/*     */   
/*     */   private static Fraction computeContentWeight(List<ItemStack> items) {
/*  57 */     Fraction weight = Fraction.ZERO;
/*  58 */     for (ItemStack stack : items) {
/*  59 */       weight = weight.add(getWeight(stack).multiplyBy(Fraction.getFraction(stack.getCount(), 1)));
/*     */     }
/*  61 */     return weight;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Fraction getWeight(ItemStack stack) {
/*  66 */     BundleContents bundle = (BundleContents)stack.get(DataComponents.BUNDLE_CONTENTS);
/*  67 */     if (bundle != null) {
/*  68 */       return BUNDLE_IN_BUNDLE_WEIGHT.add(bundle.weight());
/*     */     }
/*  70 */     List<BeehiveBlockEntity.Occupant> bees = ((Bees)stack.getOrDefault(DataComponents.BEES, Bees.EMPTY)).bees();
/*  71 */     if (!bees.isEmpty()) {
/*  72 */       return Fraction.ONE;
/*     */     }
/*  74 */     return Fraction.getFraction(1, stack.getMaxStackSize());
/*     */   }
/*     */   
/*     */   public static boolean canItemBeInBundle(ItemStack itemsToAdd) {
/*  78 */     return (!itemsToAdd.isEmpty() && itemsToAdd.getItem().canFitInsideContainerItems());
/*     */   }
/*     */   
/*     */   public int getNumberOfItemsToShow() {
/*  82 */     int numberOfItemStacks = size();
/*  83 */     int availableItemsToShow = (numberOfItemStacks > 12) ? 11 : 12;
/*  84 */     int itemsOnNonFullRow = numberOfItemStacks % 4;
/*  85 */     int emptySpaceOnNonFullRow = (itemsOnNonFullRow == 0) ? 0 : (4 - itemsOnNonFullRow);
/*  86 */     return Math.min(numberOfItemStacks, availableItemsToShow - emptySpaceOnNonFullRow);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getItemUnsafe(int index) {
/*  94 */     return this.items.get(index);
/*     */   }
/*     */   
/*     */   public Stream<ItemStack> itemCopyStream() {
/*  98 */     return this.items.stream().map(ItemStack::copy);
/*     */   }
/*     */   
/*     */   public Iterable<ItemStack> items() {
/* 102 */     return this.items;
/*     */   }
/*     */   
/*     */   public Iterable<ItemStack> itemsCopy() {
/* 106 */     return Lists.transform(this.items, ItemStack::copy);
/*     */   }
/*     */   
/*     */   public int size() {
/* 110 */     return this.items.size();
/*     */   }
/*     */   
/*     */   public Fraction weight() {
/* 114 */     return this.weight;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 118 */     return this.items.isEmpty();
/*     */   }
/*     */   
/*     */   public int getSelectedItem() {
/* 122 */     return this.selectedItem;
/*     */   }
/*     */   
/*     */   public boolean hasSelectedItem() {
/* 126 */     return (this.selectedItem != -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 131 */     if (this == obj) {
/* 132 */       return true;
/*     */     }
/* 134 */     if (obj instanceof BundleContents) { BundleContents contents = (BundleContents)obj;
/* 135 */       return (this.weight.equals(contents.weight) && ItemStack.listMatches(this.items, contents.items)); }
/*     */     
/* 137 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 142 */     return ItemStack.hashStackList(this.items);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 147 */     return "BundleContents" + String.valueOf(this.items);
/*     */   }
/*     */   
/*     */   public static class Mutable {
/*     */     private final List<ItemStack> items;
/*     */     private Fraction weight;
/*     */     private int selectedItem;
/*     */     
/*     */     public Mutable(BundleContents contents) {
/* 156 */       this.items = new ArrayList<>(contents.items);
/* 157 */       this.weight = contents.weight;
/* 158 */       this.selectedItem = contents.selectedItem;
/*     */     }
/*     */     
/*     */     public Mutable clearItems() {
/* 162 */       this.items.clear();
/* 163 */       this.weight = Fraction.ZERO;
/* 164 */       this.selectedItem = -1;
/* 165 */       return this;
/*     */     }
/*     */     
/*     */     private int findStackIndex(ItemStack itemsToAdd) {
/* 169 */       if (!itemsToAdd.isStackable()) {
/* 170 */         return -1;
/*     */       }
/* 172 */       for (int i = 0; i < this.items.size(); i++) {
/* 173 */         if (ItemStack.isSameItemSameComponents(this.items.get(i), itemsToAdd)) {
/* 174 */           return i;
/*     */         }
/*     */       } 
/* 177 */       return -1;
/*     */     }
/*     */     
/*     */     private int getMaxAmountToAdd(ItemStack item) {
/* 181 */       Fraction remainingWeight = Fraction.ONE.subtract(this.weight);
/* 182 */       return Math.max(remainingWeight.divideBy(BundleContents.getWeight(item)).intValue(), 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int tryInsert(ItemStack itemsToAdd) {
/* 189 */       if (!BundleContents.canItemBeInBundle(itemsToAdd)) {
/* 190 */         return 0;
/*     */       }
/*     */       
/* 193 */       int amountToAdd = Math.min(itemsToAdd.getCount(), getMaxAmountToAdd(itemsToAdd));
/* 194 */       if (amountToAdd == 0) {
/* 195 */         return 0;
/*     */       }
/*     */       
/* 198 */       this.weight = this.weight.add(BundleContents.getWeight(itemsToAdd).multiplyBy(Fraction.getFraction(amountToAdd, 1)));
/*     */       
/* 200 */       int stackIndex = findStackIndex(itemsToAdd);
/* 201 */       if (stackIndex != -1) {
/* 202 */         ItemStack removedStack = this.items.remove(stackIndex);
/* 203 */         ItemStack mergedStack = removedStack.copyWithCount(removedStack.getCount() + amountToAdd);
/* 204 */         itemsToAdd.shrink(amountToAdd);
/*     */         
/* 206 */         this.items.add(0, mergedStack);
/*     */       } else {
/* 208 */         this.items.add(0, itemsToAdd.split(amountToAdd));
/*     */       } 
/*     */       
/* 211 */       return amountToAdd;
/*     */     }
/*     */     
/*     */     public int tryTransfer(Slot slot, Player player) {
/* 215 */       ItemStack other = slot.getItem();
/* 216 */       int maxAmount = getMaxAmountToAdd(other);
/* 217 */       return BundleContents.canItemBeInBundle(other) ? tryInsert(slot.safeTake(other.getCount(), maxAmount, player)) : 0;
/*     */     }
/*     */     
/*     */     public void toggleSelectedItem(int selectedItem) {
/* 221 */       this.selectedItem = (this.selectedItem == selectedItem || indexIsOutsideAllowedBounds(selectedItem)) ? -1 : selectedItem;
/*     */     }
/*     */     
/*     */     private boolean indexIsOutsideAllowedBounds(int selectedItem) {
/* 225 */       return (selectedItem < 0 || selectedItem >= this.items.size());
/*     */     }
/*     */     
/*     */     public ItemStack removeOne() {
/* 229 */       if (this.items.isEmpty()) {
/* 230 */         return null;
/*     */       }
/* 232 */       int removeIndex = indexIsOutsideAllowedBounds(this.selectedItem) ? 0 : this.selectedItem;
/* 233 */       ItemStack stack = ((ItemStack)this.items.remove(removeIndex)).copy();
/* 234 */       this.weight = this.weight.subtract(BundleContents.getWeight(stack).multiplyBy(Fraction.getFraction(stack.getCount(), 1)));
/* 235 */       toggleSelectedItem(-1);
/* 236 */       return stack;
/*     */     }
/*     */     
/*     */     public Fraction weight() {
/* 240 */       return this.weight;
/*     */     }
/*     */     
/*     */     public BundleContents toImmutable() {
/* 244 */       return new BundleContents(List.copyOf(this.items), this.weight, this.selectedItem);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/BundleContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */