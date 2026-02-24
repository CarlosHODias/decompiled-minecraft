/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.criterion.StatePropertiesPredicate;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class LootItemBlockStatePropertyCondition extends Record implements LootItemCondition {
/*    */   private final Holder<Block> block;
/*    */   private final Optional<StatePropertiesPredicate> properties;
/*    */   public static final com.mojang.serialization.MapCodec<LootItemBlockStatePropertyCondition> CODEC;
/*    */   
/* 18 */   public LootItemBlockStatePropertyCondition(Holder<Block> block, Optional<StatePropertiesPredicate> properties) { this.block = block; this.properties = properties; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemBlockStatePropertyCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 18 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemBlockStatePropertyCondition; } public Holder<Block> block() { return this.block; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemBlockStatePropertyCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemBlockStatePropertyCondition; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemBlockStatePropertyCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemBlockStatePropertyCondition;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<StatePropertiesPredicate> properties() { return this.properties; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 25 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)net.minecraft.core.registries.BuiltInRegistries.BLOCK.holderByNameCodec().fieldOf("block").forGetter(LootItemBlockStatePropertyCondition::block), (App)StatePropertiesPredicate.CODEC.optionalFieldOf("properties").forGetter(LootItemBlockStatePropertyCondition::properties)).apply((com.mojang.datafixers.kinds.Applicative)i, LootItemBlockStatePropertyCondition::new)).validate(LootItemBlockStatePropertyCondition::validate);
/*    */   }
/*    */   private static DataResult<LootItemBlockStatePropertyCondition> validate(LootItemBlockStatePropertyCondition condition) {
/* 28 */     return condition.properties()
/* 29 */       .flatMap(properties -> properties.checkState(((Block)condition.block().value()).getStateDefinition()))
/* 30 */       .map(name -> DataResult.error(()))
/* 31 */       .orElse(DataResult.success(condition));
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 36 */     return LootItemConditions.BLOCK_STATE_PROPERTY;
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/* 41 */     return java.util.Set.of(net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_STATE);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext context) {
/* 46 */     BlockState state = (BlockState)context.getOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_STATE);
/* 47 */     return (state != null && state.is(this.block) && (this.properties.isEmpty() || ((StatePropertiesPredicate)this.properties.get()).matches(state)));
/*    */   }
/*    */   
/*    */   public static class Builder implements LootItemCondition.Builder {
/*    */     private final Holder<Block> block;
/* 52 */     private Optional<StatePropertiesPredicate> properties = Optional.empty();
/*    */     
/*    */     public Builder(Block block) {
/* 55 */       this.block = (Holder<Block>)block.builtInRegistryHolder();
/*    */     }
/*    */     
/*    */     public Builder setProperties(StatePropertiesPredicate.Builder properties) {
/* 59 */       this.properties = properties.build();
/* 60 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemCondition build() {
/* 65 */       return new LootItemBlockStatePropertyCondition(this.block, this.properties);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder hasBlockStateProperties(Block block) {
/* 70 */     return new Builder(block);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/predicates/LootItemBlockStatePropertyCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */