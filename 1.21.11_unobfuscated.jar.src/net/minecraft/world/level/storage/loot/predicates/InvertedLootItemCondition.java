/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public final class InvertedLootItemCondition extends Record implements LootItemCondition {
/*    */   private final LootItemCondition term;
/*    */   public static final com.mojang.serialization.MapCodec<InvertedLootItemCondition> CODEC;
/*    */   
/* 11 */   public InvertedLootItemCondition(LootItemCondition term) { this.term = term; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/InvertedLootItemCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/InvertedLootItemCondition; } public LootItemCondition term() { return this.term; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/InvertedLootItemCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/InvertedLootItemCondition; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/InvertedLootItemCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/InvertedLootItemCondition;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } static { CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LootItemCondition.DIRECT_CODEC.fieldOf("term").forGetter(InvertedLootItemCondition::term)).apply((com.mojang.datafixers.kinds.Applicative)i, InvertedLootItemCondition::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 18 */     return LootItemConditions.INVERTED;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(net.minecraft.world.level.storage.loot.LootContext context) {
/* 23 */     return !this.term.test(context);
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/* 28 */     return this.term.getReferencedContextParams();
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext output) {
/* 33 */     super.validate(output);
/* 34 */     this.term.validate(output);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder invert(LootItemCondition.Builder term) {
/* 38 */     InvertedLootItemCondition result = new InvertedLootItemCondition(term.build());
/* 39 */     return () -> result;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/predicates/InvertedLootItemCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */