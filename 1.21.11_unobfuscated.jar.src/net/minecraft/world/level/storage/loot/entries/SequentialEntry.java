/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SequentialEntry extends CompositeEntryBase {
/* 10 */   public static final MapCodec<SequentialEntry> CODEC = createCodec(SequentialEntry::new);
/*    */   
/*    */   SequentialEntry(List<LootPoolEntryContainer> children, List<LootItemCondition> conditions) {
/* 13 */     super(children, conditions);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 18 */     return LootPoolEntries.SEQUENCE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ComposableEntryContainer compose(List<? extends ComposableEntryContainer> entries) {
/* 23 */     switch (entries.size()) { case 0: case 1: case 2: default: break; }  return (context, output) -> {
/*    */         for (ComposableEntryContainer entry : (Iterable<ComposableEntryContainer>)entries) {
/*    */           if (!entry.expand(context, output)) {
/*    */             return false;
/*    */           }
/*    */         } 
/*    */         return true;
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Builder
/*    */     extends LootPoolEntryContainer.Builder<Builder>
/*    */   {
/* 39 */     private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */     
/*    */     public Builder(LootPoolEntryContainer.Builder<?>... entries) {
/* 42 */       for (LootPoolEntryContainer.Builder<?> entry : entries) {
/* 43 */         this.entries.add(entry.build());
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     protected Builder getThis() {
/* 49 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public Builder then(LootPoolEntryContainer.Builder<?> other) {
/* 54 */       this.entries.add(other.build());
/* 55 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootPoolEntryContainer build() {
/* 60 */       return new SequentialEntry((List<LootPoolEntryContainer>)this.entries.build(), getConditions());
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder sequential(LootPoolEntryContainer.Builder<?>... entries) {
/* 65 */     return new Builder(entries);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/entries/SequentialEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */