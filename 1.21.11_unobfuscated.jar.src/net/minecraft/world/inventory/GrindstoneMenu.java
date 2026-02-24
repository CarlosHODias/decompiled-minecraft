/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class GrindstoneMenu
/*     */   extends AbstractContainerMenu
/*     */ {
/*     */   public static final int MAX_NAME_LENGTH = 35;
/*     */   public static final int INPUT_SLOT = 0;
/*     */   public static final int ADDITIONAL_SLOT = 1;
/*     */   public static final int RESULT_SLOT = 2;
/*  33 */   private final Container resultSlots = new ResultContainer(); private static final int INV_SLOT_START = 3; private static final int INV_SLOT_END = 30; private static final int USE_ROW_SLOT_START = 30; private static final int USE_ROW_SLOT_END = 39;
/*  34 */   private final Container repairSlots = (Container)new SimpleContainer(2)
/*     */     {
/*     */       public void setChanged() {
/*  37 */         super.setChanged();
/*  38 */         GrindstoneMenu.this.slotsChanged((Container)this);
/*     */       }
/*     */     };
/*     */   
/*     */   private final ContainerLevelAccess access;
/*     */   
/*     */   public GrindstoneMenu(int containerId, Inventory inventory) {
/*  45 */     this(containerId, inventory, ContainerLevelAccess.NULL);
/*     */   }
/*     */   
/*     */   public GrindstoneMenu(int containerId, Inventory inventory, final ContainerLevelAccess access) {
/*  49 */     super(MenuType.GRINDSTONE, containerId);
/*  50 */     this.access = access;
/*     */     
/*  52 */     addSlot(new Slot(this, this.repairSlots, 0, 49, 19)
/*     */         {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  55 */             return (itemStack.isDamageableItem() || EnchantmentHelper.hasAnyEnchantments(itemStack));
/*     */           }
/*     */         });
/*  58 */     addSlot(new Slot(this, this.repairSlots, 1, 49, 40)
/*     */         {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  61 */             return (itemStack.isDamageableItem() || EnchantmentHelper.hasAnyEnchantments(itemStack));
/*     */           }
/*     */         });
/*  64 */     addSlot(new Slot(this.resultSlots, 2, 129, 34)
/*     */         {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  67 */             return false;
/*     */           }
/*     */ 
/*     */           
/*     */           public void onTake(Player player, ItemStack carried) {
/*  72 */             access.execute((level, pos) -> {
/*     */                   if (level instanceof ServerLevel) {
/*     */                     ExperienceOrb.award((ServerLevel)level, Vec3.atCenterOf((Vec3i)pos), getExperienceAmount(level));
/*     */                   }
/*     */                   
/*     */                   level.levelEvent(1042, pos, 0);
/*     */                 });
/*  79 */             GrindstoneMenu.this.repairSlots.setItem(0, ItemStack.EMPTY);
/*  80 */             GrindstoneMenu.this.repairSlots.setItem(1, ItemStack.EMPTY);
/*     */           }
/*     */           
/*     */           private int getExperienceAmount(Level level) {
/*  84 */             int amount = 0;
/*  85 */             amount += getExperienceFromItem(GrindstoneMenu.this.repairSlots.getItem(0));
/*  86 */             amount += getExperienceFromItem(GrindstoneMenu.this.repairSlots.getItem(1));
/*     */             
/*  88 */             if (amount > 0) {
/*  89 */               int halfAmount = (int)Math.ceil(amount / 2.0D);
/*  90 */               return halfAmount + level.random.nextInt(halfAmount);
/*     */             } 
/*     */             
/*  93 */             return 0;
/*     */           }
/*     */           
/*     */           private int getExperienceFromItem(ItemStack item) {
/*  97 */             int amount = 0;
/*  98 */             ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(item);
/*  99 */             for (Object2IntMap.Entry<Holder<Enchantment>> entry : (Iterable<Object2IntMap.Entry<Holder<Enchantment>>>)enchantments.entrySet()) {
/* 100 */               Holder<Enchantment> enchant = (Holder<Enchantment>)entry.getKey();
/* 101 */               int lvl = entry.getIntValue();
/*     */               
/* 103 */               if (!enchant.is(EnchantmentTags.CURSE)) {
/* 104 */                 amount += ((Enchantment)enchant.value()).getMinCost(lvl);
/*     */               }
/*     */             } 
/*     */             
/* 108 */             return amount;
/*     */           }
/*     */         });
/*     */     
/* 112 */     addStandardInventorySlots((Container)inventory, 8, 84);
/*     */   }
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container container) {
/* 117 */     super.slotsChanged(container);
/*     */     
/* 119 */     if (container == this.repairSlots) {
/* 120 */       createResult();
/*     */     }
/*     */   }
/*     */   
/*     */   private void createResult() {
/* 125 */     this.resultSlots.setItem(0, computeResult(this.repairSlots.getItem(0), this.repairSlots.getItem(1)));
/* 126 */     broadcastChanges();
/*     */   }
/*     */   
/*     */   private ItemStack computeResult(ItemStack input, ItemStack additional) {
/* 130 */     boolean hasAnItem = (!input.isEmpty() || !additional.isEmpty());
/* 131 */     if (!hasAnItem) {
/* 132 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/* 135 */     if (input.getCount() > 1 || additional.getCount() > 1) {
/* 136 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/* 139 */     boolean hasBothItems = (!input.isEmpty() && !additional.isEmpty());
/* 140 */     if (!hasBothItems) {
/* 141 */       ItemStack item = !input.isEmpty() ? input : additional;
/* 142 */       if (!EnchantmentHelper.hasAnyEnchantments(item)) {
/* 143 */         return ItemStack.EMPTY;
/*     */       }
/* 145 */       return removeNonCursesFrom(item.copy());
/*     */     } 
/*     */     
/* 148 */     return mergeItems(input, additional);
/*     */   }
/*     */   
/*     */   private ItemStack mergeItems(ItemStack input, ItemStack additional) {
/* 152 */     if (!input.is(additional.getItem())) {
/* 153 */       return ItemStack.EMPTY;
/*     */     }
/*     */ 
/*     */     
/* 157 */     int durability = Math.max(input.getMaxDamage(), additional.getMaxDamage());
/*     */     
/* 159 */     int remaining1 = input.getMaxDamage() - input.getDamageValue();
/* 160 */     int remaining2 = additional.getMaxDamage() - additional.getDamageValue();
/* 161 */     int remaining = remaining1 + remaining2 + durability * 5 / 100;
/*     */     
/* 163 */     int count = 1;
/* 164 */     if (!input.isDamageableItem()) {
/* 165 */       if (input.getMaxStackSize() < 2 || !ItemStack.matches(input, additional)) {
/* 166 */         return ItemStack.EMPTY;
/*     */       }
/* 168 */       count = 2;
/*     */     } 
/*     */     
/* 171 */     ItemStack newItem = input.copyWithCount(count);
/* 172 */     if (newItem.isDamageableItem()) {
/* 173 */       newItem.set(DataComponents.MAX_DAMAGE, durability);
/* 174 */       newItem.setDamageValue(Math.max(durability - remaining, 0));
/*     */     } 
/* 176 */     mergeEnchantsFrom(newItem, additional);
/* 177 */     return removeNonCursesFrom(newItem);
/*     */   }
/*     */   
/*     */   private void mergeEnchantsFrom(ItemStack target, ItemStack source) {
/* 181 */     EnchantmentHelper.updateEnchantments(target, newEnchantments -> {
/*     */           ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(source);
/*     */           for (Object2IntMap.Entry<Holder<Enchantment>> entry : (Iterable<Object2IntMap.Entry<Holder<Enchantment>>>)enchantments.entrySet()) {
/*     */             Holder<Enchantment> enchant = (Holder<Enchantment>)entry.getKey();
/*     */             if (!enchant.is(EnchantmentTags.CURSE) || newEnchantments.getLevel(enchant) == 0) {
/*     */               newEnchantments.upgrade(enchant, entry.getIntValue());
/*     */             }
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private ItemStack removeNonCursesFrom(ItemStack item) {
/* 193 */     ItemEnchantments newEnchantments = EnchantmentHelper.updateEnchantments(item, enchantments -> enchantments.removeIf(()));
/*     */ 
/*     */ 
/*     */     
/* 197 */     if (item.is(Items.ENCHANTED_BOOK) && newEnchantments.isEmpty()) {
/* 198 */       item = item.transmuteCopy((ItemLike)Items.BOOK);
/*     */     }
/*     */     
/* 201 */     int repairCost = 0;
/* 202 */     for (int i = 0; i < newEnchantments.size(); i++) {
/* 203 */       repairCost = AnvilMenu.calculateIncreasedRepairCost(repairCost);
/*     */     }
/* 205 */     item.set(DataComponents.REPAIR_COST, repairCost);
/*     */     
/* 207 */     return item;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player player) {
/* 212 */     super.removed(player);
/* 213 */     this.access.execute((level, pos) -> clearContainer(player, this.repairSlots));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player player) {
/* 218 */     return stillValid(this.access, player, Blocks.GRINDSTONE);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/* 223 */     ItemStack clicked = ItemStack.EMPTY;
/* 224 */     Slot slot = (Slot)this.slots.get(slotIndex);
/* 225 */     if (slot != null && slot.hasItem()) {
/* 226 */       ItemStack item = slot.getItem();
/* 227 */       clicked = item.copy();
/*     */       
/* 229 */       ItemStack input = this.repairSlots.getItem(0);
/* 230 */       ItemStack additional = this.repairSlots.getItem(1);
/*     */       
/* 232 */       if (slotIndex == 2) {
/* 233 */         if (!moveItemStackTo(item, 3, 39, true)) {
/* 234 */           return ItemStack.EMPTY;
/*     */         }
/* 236 */         slot.onQuickCraft(item, clicked);
/* 237 */       } else if (slotIndex == 0 || slotIndex == 1) {
/* 238 */         if (!moveItemStackTo(item, 3, 39, false)) {
/* 239 */           return ItemStack.EMPTY;
/*     */         }
/* 241 */       } else if (input.isEmpty() || additional.isEmpty()) {
/* 242 */         if (!moveItemStackTo(item, 0, 2, false)) {
/* 243 */           return ItemStack.EMPTY;
/*     */         }
/* 245 */       } else if (slotIndex >= 3 && slotIndex < 30) {
/* 246 */         if (!moveItemStackTo(item, 30, 39, false)) {
/* 247 */           return ItemStack.EMPTY;
/*     */         }
/* 249 */       } else if (slotIndex >= 30 && slotIndex < 39 && 
/* 250 */         !moveItemStackTo(item, 3, 30, false)) {
/* 251 */         return ItemStack.EMPTY;
/*     */       } 
/*     */ 
/*     */       
/* 255 */       if (item.isEmpty()) {
/* 256 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       } else {
/* 258 */         slot.setChanged();
/*     */       } 
/*     */       
/* 261 */       if (item.getCount() == clicked.getCount()) {
/* 262 */         return ItemStack.EMPTY;
/*     */       }
/* 264 */       slot.onTake(player, item);
/*     */     } 
/*     */ 
/*     */     
/* 268 */     return clicked;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/GrindstoneMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */