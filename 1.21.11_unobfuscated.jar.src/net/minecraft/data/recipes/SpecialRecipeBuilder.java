/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.crafting.CraftingBookCategory;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ 
/*    */ public class SpecialRecipeBuilder
/*    */ {
/*    */   private final Function<CraftingBookCategory, Recipe<?>> factory;
/*    */   
/*    */   public SpecialRecipeBuilder(Function<CraftingBookCategory, Recipe<?>> factory) {
/* 15 */     this.factory = factory;
/*    */   }
/*    */   
/*    */   public static SpecialRecipeBuilder special(Function<CraftingBookCategory, Recipe<?>> factory) {
/* 19 */     return new SpecialRecipeBuilder(factory);
/*    */   }
/*    */   
/*    */   public void save(RecipeOutput output, String name) {
/* 23 */     save(output, ResourceKey.create(Registries.RECIPE, Identifier.parse(name)));
/*    */   }
/*    */   
/*    */   public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
/* 27 */     output.accept(id, this.factory.apply(CraftingBookCategory.MISC), null);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/recipes/SpecialRecipeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */