/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface FunctionUserBuilder<T extends FunctionUserBuilder<T>> {
/*    */   T apply(LootItemFunction.Builder paramBuilder);
/*    */   
/*    */   default <E> T apply(Iterable<E> collection, Function<E, LootItemFunction.Builder> functionProvider) {
/* 10 */     T result = unwrap();
/* 11 */     for (E value : collection) {
/* 12 */       result = result.apply(functionProvider.apply(value));
/*    */     }
/* 14 */     return result;
/*    */   }
/*    */   
/*    */   default <E> T apply(E[] collection, Function<E, LootItemFunction.Builder> functionProvider) {
/* 18 */     return apply(Arrays.asList(collection), functionProvider);
/*    */   }
/*    */   
/*    */   T unwrap();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/FunctionUserBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */