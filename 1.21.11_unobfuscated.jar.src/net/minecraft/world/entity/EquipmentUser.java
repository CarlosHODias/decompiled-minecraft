/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.equipment.Equippable;
/*    */ import net.minecraft.world.level.storage.loot.LootParams;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ 
/*    */ public interface EquipmentUser
/*    */ {
/*    */   void setItemSlot(EquipmentSlot paramEquipmentSlot, ItemStack paramItemStack);
/*    */   
/*    */   ItemStack getItemBySlot(EquipmentSlot paramEquipmentSlot);
/*    */   
/*    */   void setDropChance(EquipmentSlot paramEquipmentSlot, float paramFloat);
/*    */   
/*    */   default void equip(EquipmentTable equipment, LootParams lootParams) {
/* 23 */     equip(equipment.lootTable(), lootParams, equipment.slotDropChances());
/*    */   }
/*    */   
/*    */   default void equip(ResourceKey<LootTable> lootTable, LootParams lootParams, Map<EquipmentSlot, Float> dropChances) {
/* 27 */     equip(lootTable, lootParams, 0L, dropChances);
/*    */   }
/*    */   
/*    */   default void equip(ResourceKey<LootTable> lootTable, LootParams lootParams, long optionalLootTableSeed, Map<EquipmentSlot, Float> dropChances) {
/* 31 */     LootTable table = lootParams.getLevel().getServer().reloadableRegistries().getLootTable(lootTable);
/* 32 */     if (table == LootTable.EMPTY) {
/*    */       return;
/*    */     }
/*    */     
/* 36 */     ObjectArrayList objectArrayList = table.getRandomItems(lootParams, optionalLootTableSeed);
/*    */     
/* 38 */     List<EquipmentSlot> insertedIntoSlots = new ArrayList<>();
/* 39 */     for (ItemStack toEquip : (Iterable<ItemStack>)objectArrayList) {
/* 40 */       EquipmentSlot slot = resolveSlot(toEquip, insertedIntoSlots);
/*    */       
/* 42 */       if (slot != null) {
/* 43 */         ItemStack equipped = slot.limit(toEquip);
/* 44 */         setItemSlot(slot, equipped);
/* 45 */         Float dropChance = dropChances.get(slot);
/* 46 */         if (dropChance != null) {
/* 47 */           setDropChance(slot, dropChance);
/*    */         }
/* 49 */         insertedIntoSlots.add(slot);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   default EquipmentSlot resolveSlot(ItemStack toEquip, List<EquipmentSlot> alreadyInsertedIntoSlots) {
/* 55 */     if (toEquip.isEmpty()) {
/* 56 */       return null;
/*    */     }
/*    */     
/* 59 */     Equippable equippable = (Equippable)toEquip.get(DataComponents.EQUIPPABLE);
/*    */     
/* 61 */     if (equippable != null) {
/* 62 */       EquipmentSlot slot = equippable.slot();
/* 63 */       if (!alreadyInsertedIntoSlots.contains(slot)) {
/* 64 */         return slot;
/*    */       }
/* 66 */     } else if (!alreadyInsertedIntoSlots.contains(EquipmentSlot.MAINHAND)) {
/* 67 */       return EquipmentSlot.MAINHAND;
/*    */     } 
/*    */     
/* 70 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/EquipmentUser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */