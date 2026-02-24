/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.ItemContainerContents;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.ChiseledBookShelfBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ChiseledBookShelfBlockEntity extends BlockEntity implements ListBackedContainer {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int MAX_BOOKS_IN_STORAGE = 6;
/*     */   private static final int DEFAULT_LAST_INTERACTED_SLOT = -1;
/*  33 */   private final NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);
/*     */   
/*  35 */   private int lastInteractedSlot = -1;
/*     */   
/*     */   public ChiseledBookShelfBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  38 */     super(BlockEntityType.CHISELED_BOOKSHELF, worldPosition, blockState);
/*     */   }
/*     */   
/*     */   private void updateState(int interactedSlot) {
/*  42 */     if (interactedSlot < 0 || interactedSlot >= 6) {
/*  43 */       LOGGER.error("Expected slot 0-5, got {}", interactedSlot);
/*     */       
/*     */       return;
/*     */     } 
/*  47 */     this.lastInteractedSlot = interactedSlot;
/*  48 */     BlockState updatedState = getBlockState();
/*  49 */     for (int slot = 0; slot < ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.size(); slot++) {
/*  50 */       boolean slotIsOccupied = !getItem(slot).isEmpty();
/*  51 */       BooleanProperty slotProperty = ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(slot);
/*     */       
/*  53 */       updatedState = (BlockState)updatedState.setValue((Property)slotProperty, slotIsOccupied);
/*     */     } 
/*     */     
/*  56 */     ((Level)Objects.<Level>requireNonNull(this.level)).setBlock(this.worldPosition, updatedState, 3);
/*     */     
/*  58 */     this.level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, this.worldPosition, GameEvent.Context.of(updatedState));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  63 */     super.loadAdditional(input);
/*     */     
/*  65 */     this.items.clear();
/*  66 */     ContainerHelper.loadAllItems(input, this.items);
/*  67 */     this.lastInteractedSlot = input.getIntOr("last_interacted_slot", -1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  72 */     super.saveAdditional(output);
/*     */     
/*  74 */     ContainerHelper.saveAllItems(output, this.items, true);
/*  75 */     output.putInt("last_interacted_slot", this.lastInteractedSlot);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxStackSize() {
/*  80 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean acceptsItemType(ItemStack itemStack) {
/*  85 */     return itemStack.is(ItemTags.BOOKSHELF_BOOKS);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int slot, int count) {
/*  90 */     ItemStack retrievedItem = Objects.<ItemStack>requireNonNullElse((ItemStack)getItems().get(slot), ItemStack.EMPTY);
/*  91 */     getItems().set(slot, ItemStack.EMPTY);
/*     */     
/*  93 */     if (!retrievedItem.isEmpty()) {
/*  94 */       updateState(slot);
/*     */     }
/*     */     
/*  97 */     return retrievedItem;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {
/* 102 */     if (acceptsItemType(itemStack)) {
/* 103 */       getItems().set(slot, itemStack);
/* 104 */       updateState(slot);
/* 105 */     } else if (itemStack.isEmpty()) {
/* 106 */       removeItem(slot, getMaxStackSize());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canTakeItem(Container into, int slot, ItemStack itemStack) {
/* 112 */     return into.hasAnyMatching(toItem -> toItem.isEmpty() ? true : (
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 117 */         (ItemStack.isSameItemSameComponents(itemStack, toItem) && toItem.getCount() + itemStack.getCount() <= into.getMaxStackSize(toItem))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NonNullList<ItemStack> getItems() {
/* 123 */     return this.items;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player player) {
/* 128 */     return Container.stillValidBlockEntity(this, player);
/*     */   }
/*     */   
/*     */   public int getLastInteractedSlot() {
/* 132 */     return this.lastInteractedSlot;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 137 */     super.applyImplicitComponents(components);
/* 138 */     ((ItemContainerContents)components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)).copyInto(this.items);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {
/* 143 */     super.collectImplicitComponents(components);
/* 144 */     components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems((List)this.items));
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput output) {
/* 149 */     output.discard("Items");
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/ChiseledBookShelfBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */