/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public final class EnchantedItemInUse extends Record {
/*    */   private final net.minecraft.world.item.ItemStack itemStack;
/*    */   private final net.minecraft.world.entity.EquipmentSlot inSlot;
/*    */   private final LivingEntity owner;
/*    */   private final java.util.function.Consumer<net.minecraft.world.item.Item> onBreak;
/*    */   
/* 11 */   public EnchantedItemInUse(net.minecraft.world.item.ItemStack itemStack, net.minecraft.world.entity.EquipmentSlot inSlot, LivingEntity owner, java.util.function.Consumer<net.minecraft.world.item.Item> onBreak) { this.itemStack = itemStack; this.inSlot = inSlot; this.owner = owner; this.onBreak = onBreak; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/EnchantedItemInUse;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/EnchantedItemInUse; } public net.minecraft.world.item.ItemStack itemStack() { return this.itemStack; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/EnchantedItemInUse;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/EnchantedItemInUse; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/EnchantedItemInUse;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/EnchantedItemInUse;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.world.entity.EquipmentSlot inSlot() { return this.inSlot; } public LivingEntity owner() { return this.owner; } public java.util.function.Consumer<net.minecraft.world.item.Item> onBreak() { return this.onBreak; }
/*    */    public EnchantedItemInUse(net.minecraft.world.item.ItemStack itemStack, net.minecraft.world.entity.EquipmentSlot inSlot, LivingEntity owner) {
/* 13 */     this(itemStack, inSlot, owner, item -> owner.onEquippedItemBroken(item, inSlot));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/EnchantedItemInUse.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */