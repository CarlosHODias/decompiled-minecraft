/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.storage.loot.IntRange;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ 
/*    */ public final class ValueCheckCondition extends Record implements LootItemCondition {
/*    */   private final NumberProvider provider;
/*    */   private final IntRange range;
/*    */   public static final com.mojang.serialization.MapCodec<ValueCheckCondition> CODEC;
/*    */   
/* 14 */   public ValueCheckCondition(NumberProvider provider, IntRange range) { this.provider = provider; this.range = range; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/ValueCheckCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/ValueCheckCondition; } public NumberProvider provider() { return this.provider; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/ValueCheckCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/ValueCheckCondition; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/ValueCheckCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/ValueCheckCondition;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public IntRange range() { return this.range; }
/*    */ 
/*    */   
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)net.minecraft.world.level.storage.loot.providers.number.NumberProviders.CODEC.fieldOf("value").forGetter(ValueCheckCondition::provider), (App)IntRange.CODEC.fieldOf("range").forGetter(ValueCheckCondition::range)).apply((com.mojang.datafixers.kinds.Applicative)i, ValueCheckCondition::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 25 */     return LootItemConditions.VALUE_CHECK;
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/* 30 */     return (java.util.Set<net.minecraft.util.context.ContextKey<?>>)com.google.common.collect.Sets.union(this.provider.getReferencedContextParams(), this.range.getReferencedContextParams());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext context) {
/* 35 */     return this.range.test(context, this.provider.getInt(context));
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder hasValue(NumberProvider target, IntRange range) {
/* 39 */     return () -> new ValueCheckCondition(target, range);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/predicates/ValueCheckCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */