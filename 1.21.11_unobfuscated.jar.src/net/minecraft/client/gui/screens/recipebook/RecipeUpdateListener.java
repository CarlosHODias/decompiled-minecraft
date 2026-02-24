package net.minecraft.client.gui.screens.recipebook;

import net.minecraft.world.item.crafting.display.RecipeDisplay;

public interface RecipeUpdateListener {
  void recipesUpdated();
  
  void fillGhostRecipe(RecipeDisplay paramRecipeDisplay);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/recipebook/RecipeUpdateListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */