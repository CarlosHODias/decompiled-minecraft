/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.trading.Merchant;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
/*     */ 
/*     */ public class MerchantContainer implements Container {
/*     */   private final Merchant merchant;
/*  15 */   private final NonNullList<ItemStack> itemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
/*     */   private MerchantOffer activeOffer;
/*     */   private int selectionHint;
/*     */   private int futureXp;
/*     */   
/*     */   public MerchantContainer(Merchant villager) {
/*  21 */     this.merchant = villager;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/*  26 */     return this.itemStacks.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  31 */     for (ItemStack itemStack : this.itemStacks) {
/*  32 */       if (!itemStack.isEmpty()) {
/*  33 */         return false;
/*     */       }
/*     */     } 
/*  36 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int slot) {
/*  41 */     return (ItemStack)this.itemStacks.get(slot);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int slot, int count) {
/*  46 */     ItemStack itemStack = (ItemStack)this.itemStacks.get(slot);
/*  47 */     if (slot == 2 && !itemStack.isEmpty()) {
/*  48 */       return ContainerHelper.removeItem((List)this.itemStacks, slot, itemStack.getCount());
/*     */     }
/*     */     
/*  51 */     ItemStack result = ContainerHelper.removeItem((List)this.itemStacks, slot, count);
/*  52 */     if (!result.isEmpty() && isPaymentSlot(slot)) {
/*  53 */       updateSellItem();
/*     */     }
/*  55 */     return result;
/*     */   }
/*     */   
/*     */   private boolean isPaymentSlot(int slot) {
/*  59 */     return (slot == 0 || slot == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int slot) {
/*  64 */     return ContainerHelper.takeItem((List)this.itemStacks, slot);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {
/*  69 */     this.itemStacks.set(slot, itemStack);
/*  70 */     itemStack.limitSize(getMaxStackSize(itemStack));
/*  71 */     if (isPaymentSlot(slot)) {
/*  72 */       updateSellItem();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player player) {
/*  78 */     return (this.merchant.getTradingPlayer() == player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChanged() {
/*  83 */     updateSellItem();
/*     */   }
/*     */   public void updateSellItem() {
/*     */     ItemStack buyA, buyB;
/*  87 */     this.activeOffer = null;
/*     */ 
/*     */ 
/*     */     
/*  91 */     if (((ItemStack)this.itemStacks.get(0)).isEmpty()) {
/*  92 */       buyA = (ItemStack)this.itemStacks.get(1);
/*  93 */       buyB = ItemStack.EMPTY;
/*     */     } else {
/*  95 */       buyA = (ItemStack)this.itemStacks.get(0);
/*  96 */       buyB = (ItemStack)this.itemStacks.get(1);
/*     */     } 
/*     */     
/*  99 */     if (buyA.isEmpty()) {
/* 100 */       setItem(2, ItemStack.EMPTY);
/* 101 */       this.futureXp = 0;
/*     */       
/*     */       return;
/*     */     } 
/* 105 */     MerchantOffers offers = this.merchant.getOffers();
/* 106 */     if (!offers.isEmpty()) {
/* 107 */       MerchantOffer offer = offers.getRecipeFor(buyA, buyB, this.selectionHint);
/* 108 */       if (offer == null || offer.isOutOfStock()) {
/*     */         
/* 110 */         this.activeOffer = offer;
/* 111 */         offer = offers.getRecipeFor(buyB, buyA, this.selectionHint);
/*     */       } 
/*     */       
/* 114 */       if (offer != null && !offer.isOutOfStock()) {
/* 115 */         this.activeOffer = offer;
/* 116 */         setItem(2, offer.assemble());
/* 117 */         this.futureXp = offer.getXp();
/*     */       } else {
/* 119 */         setItem(2, ItemStack.EMPTY);
/* 120 */         this.futureXp = 0;
/*     */       } 
/*     */     } 
/* 123 */     this.merchant.notifyTradeUpdated(getItem(2));
/*     */   }
/*     */   
/*     */   public MerchantOffer getActiveOffer() {
/* 127 */     return this.activeOffer;
/*     */   }
/*     */   
/*     */   public void setSelectionHint(int selectionHint) {
/* 131 */     this.selectionHint = selectionHint;
/* 132 */     updateSellItem();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 137 */     this.itemStacks.clear();
/*     */   }
/*     */   
/*     */   public int getFutureXp() {
/* 141 */     return this.futureXp;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/MerchantContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */