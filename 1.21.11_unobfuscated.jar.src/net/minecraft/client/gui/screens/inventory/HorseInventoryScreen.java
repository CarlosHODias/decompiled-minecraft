/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.tags.EntityTypeTags;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.HorseInventoryMenu;
/*    */ 
/*    */ public class HorseInventoryScreen
/*    */   extends AbstractMountInventoryScreen<HorseInventoryMenu> {
/* 13 */   private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot");
/* 14 */   private static final Identifier CHEST_SLOTS_SPRITE = Identifier.withDefaultNamespace("container/horse/chest_slots");
/* 15 */   private static final Identifier HORSE_INVENTORY_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/horse.png");
/*    */   
/*    */   public HorseInventoryScreen(HorseInventoryMenu menu, Inventory inventory, AbstractHorse horse, int inventoryColumns) {
/* 18 */     super(menu, inventory, horse.getDisplayName(), inventoryColumns, (LivingEntity)horse);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Identifier getBackgroundTextureLocation() {
/* 23 */     return HORSE_INVENTORY_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Identifier getSlotSpriteLocation() {
/* 28 */     return SLOT_SPRITE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Identifier getChestSlotsSpriteLocation() {
/* 33 */     return CHEST_SLOTS_SPRITE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldRenderSaddleSlot() {
/* 38 */     return (this.mount.canUseSlot(EquipmentSlot.SADDLE) && this.mount.getType().is(EntityTypeTags.CAN_EQUIP_SADDLE));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldRenderArmorSlot() {
/* 43 */     return (this.mount.canUseSlot(EquipmentSlot.BODY) && (this.mount.getType().is(EntityTypeTags.CAN_WEAR_HORSE_ARMOR) || this.mount instanceof net.minecraft.world.entity.animal.equine.Llama));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/HorseInventoryScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */