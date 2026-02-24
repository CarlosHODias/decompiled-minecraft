/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ 
/*    */ public class BlastFurnaceMenu extends AbstractFurnaceMenu {
/*    */   public BlastFurnaceMenu(int containerId, Inventory inventory) {
/* 10 */     super(MenuType.BLAST_FURNACE, RecipeType.BLASTING, RecipePropertySet.BLAST_FURNACE_INPUT, RecipeBookType.BLAST_FURNACE, containerId, inventory);
/*    */   }
/*    */   
/*    */   public BlastFurnaceMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
/* 14 */     super(MenuType.BLAST_FURNACE, RecipeType.BLASTING, RecipePropertySet.BLAST_FURNACE_INPUT, RecipeBookType.BLAST_FURNACE, containerId, inventory, container, data);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/BlastFurnaceMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */