/*    */ package net.minecraft.world.inventory;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ 
/*    */ public class NautilusInventoryMenu extends AbstractMountInventoryMenu {
/* 10 */   private static final Identifier SADDLE_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/saddle");
/* 11 */   private static final Identifier ARMOR_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/nautilus_armor_inventory");
/*    */   
/*    */   public NautilusInventoryMenu(int containerId, Inventory playerInventory, Container nautilusInventory, final AbstractNautilus nautilus, int inventoryColumns) {
/* 14 */     super(containerId, playerInventory, nautilusInventory, (LivingEntity)nautilus);
/*    */     
/* 16 */     Container saddleContainer = nautilus.createEquipmentSlotContainer(EquipmentSlot.SADDLE);
/* 17 */     addSlot(new ArmorSlot(this, saddleContainer, (LivingEntity)nautilus, EquipmentSlot.SADDLE, 0, 8, 18, SADDLE_SLOT_SPRITE)
/*    */         {
/*    */           public boolean isActive() {
/* 20 */             return nautilus.canUseSlot(EquipmentSlot.SADDLE);
/*    */           }
/*    */         });
/*    */     
/* 24 */     Container armorContainer = nautilus.createEquipmentSlotContainer(EquipmentSlot.BODY);
/* 25 */     addSlot(new ArmorSlot(this, armorContainer, (LivingEntity)nautilus, EquipmentSlot.BODY, 0, 8, 36, ARMOR_SLOT_SPRITE)
/*    */         {
/*    */           public boolean isActive() {
/* 28 */             return nautilus.canUseSlot(EquipmentSlot.BODY);
/*    */           }
/*    */         });
/* 31 */     addStandardInventorySlots((Container)playerInventory, 8, 84);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean hasInventoryChanged(Container container) {
/* 36 */     return ((AbstractNautilus)this.mount).hasInventoryChanged(container);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/NautilusInventoryMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */