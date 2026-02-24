/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.criterion.ItemPredicate;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class MatchTool extends Record implements LootItemCondition {
/*    */   private final Optional<ItemPredicate> predicate;
/*    */   public static final com.mojang.serialization.MapCodec<MatchTool> CODEC;
/*    */   
/* 14 */   public MatchTool(Optional<ItemPredicate> predicate) { this.predicate = predicate; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/MatchTool;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/MatchTool; } public Optional<ItemPredicate> predicate() { return this.predicate; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/MatchTool;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/MatchTool; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/MatchTool;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/MatchTool;
/*    */     //   0	8	1	o	Ljava/lang/Object; } static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ItemPredicate.CODEC.optionalFieldOf("predicate").forGetter(MatchTool::predicate)).apply((com.mojang.datafixers.kinds.Applicative)i, MatchTool::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 23 */     return LootItemConditions.MATCH_TOOL;
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/* 28 */     return java.util.Set.of(net.minecraft.world.level.storage.loot.parameters.LootContextParams.TOOL);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext context) {
/* 33 */     ItemStack tool = (ItemStack)context.getOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.TOOL);
/* 34 */     return (tool != null && (this.predicate.isEmpty() || ((ItemPredicate)this.predicate.get()).test(tool)));
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder toolMatches(ItemPredicate.Builder predicate) {
/* 38 */     return () -> new MatchTool(Optional.of(predicate.build()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/predicates/MatchTool.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */