/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
/*    */ import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.BlastFurnaceMenu;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.crafting.RecipeBookCategories;
/*    */ 
/*    */ public class BlastFurnaceScreen
/*    */   extends AbstractFurnaceScreen<BlastFurnaceMenu> {
/* 15 */   private static final Identifier LIT_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/blast_furnace/lit_progress");
/* 16 */   private static final Identifier BURN_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/blast_furnace/burn_progress");
/* 17 */   private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/gui/container/blast_furnace.png");
/*    */   
/* 19 */   private static final Component FILTER_NAME = (Component)Component.translatable("gui.recipebook.toggleRecipes.blastable");
/*    */   
/* 21 */   private static final List<RecipeBookComponent.TabInfo> TABS = List.of(new RecipeBookComponent.TabInfo(SearchRecipeBookCategory.BLAST_FURNACE), new RecipeBookComponent.TabInfo(Items.REDSTONE_ORE, RecipeBookCategories.BLAST_FURNACE_BLOCKS), new RecipeBookComponent.TabInfo(Items.IRON_SHOVEL, Items.GOLDEN_LEGGINGS, RecipeBookCategories.BLAST_FURNACE_MISC));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlastFurnaceScreen(BlastFurnaceMenu menu, Inventory inventory, Component title) {
/* 28 */     super(menu, inventory, title, FILTER_NAME, TEXTURE, LIT_PROGRESS_SPRITE, BURN_PROGRESS_SPRITE, TABS);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/BlastFurnaceScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */