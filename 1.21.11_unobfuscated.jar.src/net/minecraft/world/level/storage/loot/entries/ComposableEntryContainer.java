/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ interface ComposableEntryContainer
/*    */ {
/*    */   public static final ComposableEntryContainer ALWAYS_FALSE = (context, output) -> false;
/*    */   public static final ComposableEntryContainer ALWAYS_TRUE = (context, output) -> true;
/*    */   
/*    */   default ComposableEntryContainer and(ComposableEntryContainer other) {
/* 16 */     Objects.requireNonNull(other);
/* 17 */     return (context, output) -> (expand(other, output) && other.expand(other, output));
/*    */   }
/*    */   
/*    */   default ComposableEntryContainer or(ComposableEntryContainer other) {
/* 21 */     Objects.requireNonNull(other);
/* 22 */     return (context, output) -> (expand(other, output) || other.expand(other, output));
/*    */   }
/*    */   
/*    */   boolean expand(LootContext paramLootContext, Consumer<LootPoolEntry> paramConsumer);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/entries/ComposableEntryContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */