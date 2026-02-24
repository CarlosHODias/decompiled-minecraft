/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
/*    */ import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.SmokerMenu;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.crafting.RecipeBookCategories;
/*    */ 
/*    */ public class SmokerScreen
/*    */   extends AbstractFurnaceScreen<SmokerMenu> {
/* 15 */   private static final Identifier LIT_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/smoker/lit_progress");
/* 16 */   private static final Identifier BURN_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/smoker/burn_progress");
/* 17 */   private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/gui/container/smoker.png");
/*    */   
/* 19 */   private static final Component FILTER_NAME = (Component)Component.translatable("gui.recipebook.toggleRecipes.smokable");
/*    */   
/* 21 */   private static final List<RecipeBookComponent.TabInfo> TABS = List.of(new RecipeBookComponent.TabInfo(SearchRecipeBookCategory.SMOKER), new RecipeBookComponent.TabInfo(Items.PORKCHOP, RecipeBookCategories.SMOKER_FOOD));
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SmokerScreen(SmokerMenu menu, Inventory inventory, Component title) {
/* 27 */     super(menu, inventory, title, FILTER_NAME, TEXTURE, LIT_PROGRESS_SPRITE, BURN_PROGRESS_SPRITE, TABS);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/SmokerScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */