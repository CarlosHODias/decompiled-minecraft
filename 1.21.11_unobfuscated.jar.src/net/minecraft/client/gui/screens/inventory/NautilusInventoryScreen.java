/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.NautilusInventoryMenu;
/*    */ 
/*    */ public class NautilusInventoryScreen extends AbstractMountInventoryScreen<NautilusInventoryMenu> {
/* 11 */   private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot");
/* 12 */   private static final Identifier NAUTILUS_INVENTORY_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/nautilus.png");
/*    */   
/*    */   public NautilusInventoryScreen(NautilusInventoryMenu menu, Inventory inventory, AbstractNautilus nautilus, int inventoryColumns) {
/* 15 */     super(menu, inventory, nautilus.getDisplayName(), inventoryColumns, (LivingEntity)nautilus);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Identifier getBackgroundTextureLocation() {
/* 20 */     return NAUTILUS_INVENTORY_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Identifier getSlotSpriteLocation() {
/* 25 */     return SLOT_SPRITE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Identifier getChestSlotsSpriteLocation() {
/* 30 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldRenderSaddleSlot() {
/* 35 */     return this.mount.canUseSlot(EquipmentSlot.SADDLE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldRenderArmorSlot() {
/* 40 */     return this.mount.canUseSlot(EquipmentSlot.BODY);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/NautilusInventoryScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */