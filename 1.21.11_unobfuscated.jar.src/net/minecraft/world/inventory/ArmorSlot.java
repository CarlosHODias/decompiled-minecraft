/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ 
/*    */ class ArmorSlot
/*    */   extends Slot {
/*    */   private final LivingEntity owner;
/*    */   private final EquipmentSlot slot;
/*    */   private final Identifier emptyIcon;
/*    */   
/*    */   public ArmorSlot(Container inventory, LivingEntity owner, EquipmentSlot slot, int slotIndex, int x, int y, Identifier emptyIcon) {
/* 19 */     super(inventory, slotIndex, x, y);
/* 20 */     this.owner = owner;
/* 21 */     this.slot = slot;
/* 22 */     this.emptyIcon = emptyIcon;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setByPlayer(ItemStack itemStack, ItemStack previous) {
/* 27 */     this.owner.onEquipItem(this.slot, previous, itemStack);
/* 28 */     super.setByPlayer(itemStack, previous);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxStackSize() {
/* 33 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mayPlace(ItemStack itemStack) {
/* 38 */     return this.owner.isEquippableInSlot(itemStack, this.slot);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isActive() {
/* 43 */     return this.owner.canUseSlot(this.slot);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mayPickup(Player player) {
/* 48 */     ItemStack itemStack = getItem();
/* 49 */     if (!itemStack.isEmpty() && !player.isCreative() && EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) {
/* 50 */       return false;
/*    */     }
/* 52 */     return super.mayPickup(player);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getNoItemIcon() {
/* 57 */     return this.emptyIcon;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/ArmorSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */