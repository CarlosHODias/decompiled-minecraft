/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ 
/*    */ public class FurnaceMenu extends AbstractFurnaceMenu {
/*    */   public FurnaceMenu(int containerId, Inventory inventory) {
/* 10 */     super(MenuType.FURNACE, RecipeType.SMELTING, RecipePropertySet.FURNACE_INPUT, RecipeBookType.FURNACE, containerId, inventory);
/*    */   }
/*    */   
/*    */   public FurnaceMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
/* 14 */     super(MenuType.FURNACE, RecipeType.SMELTING, RecipePropertySet.FURNACE_INPUT, RecipeBookType.FURNACE, containerId, inventory, container, data);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/FurnaceMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */