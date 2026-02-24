/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.RecipeHolder;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ import net.minecraft.world.item.crafting.SingleRecipeInput;
/*    */ import net.minecraft.world.item.crafting.SmeltingRecipe;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class SmeltItemFunction extends LootItemConditionalFunction {
/* 19 */   private static final Logger LOGGER = com.mojang.logging.LogUtils.getLogger(); public static final com.mojang.serialization.MapCodec<SmeltItemFunction> CODEC;
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).apply((Applicative)i, SmeltItemFunction::new));
/*    */   }
/*    */   private SmeltItemFunction(List<LootItemCondition> predicates) {
/* 24 */     super(predicates);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SmeltItemFunction> getType() {
/* 29 */     return LootItemFunctions.FURNACE_SMELT;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 34 */     if (itemStack.isEmpty()) {
/* 35 */       return itemStack;
/*    */     }
/*    */     
/* 38 */     SingleRecipeInput input = new SingleRecipeInput(itemStack);
/* 39 */     Optional<RecipeHolder<SmeltingRecipe>> recipe = context.getLevel().recipeAccess().getRecipeFor(RecipeType.SMELTING, (net.minecraft.world.item.crafting.RecipeInput)input, (Level)context.getLevel());
/* 40 */     if (recipe.isPresent()) {
/* 41 */       ItemStack result = ((SmeltingRecipe)((RecipeHolder)recipe.get()).value()).assemble(input, (HolderLookup.Provider)context.getLevel().registryAccess());
/*    */       
/* 43 */       if (!result.isEmpty()) {
/* 44 */         return result.copyWithCount(itemStack.getCount());
/*    */       }
/*    */     } 
/*    */     
/* 48 */     LOGGER.warn("Couldn't smelt {} because there is no smelting recipe", itemStack);
/* 49 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> smelted() {
/* 53 */     return simpleBuilder(SmeltItemFunction::new);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/SmeltItemFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */