/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootContextUser;
/*    */ 
/*    */ 
/*    */ public interface LootItemFunction
/*    */   extends LootContextUser, BiFunction<ItemStack, LootContext, ItemStack>
/*    */ {
/*    */   LootItemFunctionType<? extends LootItemFunction> getType();
/*    */   
/*    */   static Consumer<ItemStack> decorate(BiFunction<ItemStack, LootContext, ItemStack> function, Consumer<ItemStack> output, LootContext context) {
/* 16 */     return drop -> output.accept(function.apply(drop, context));
/*    */   }
/*    */   
/*    */   public static interface Builder {
/*    */     LootItemFunction build();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/LootItemFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */