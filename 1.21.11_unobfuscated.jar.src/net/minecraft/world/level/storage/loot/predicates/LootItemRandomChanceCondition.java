/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ 
/*    */ public final class LootItemRandomChanceCondition extends Record implements LootItemCondition {
/*    */   private final NumberProvider chance;
/*    */   public static final com.mojang.serialization.MapCodec<LootItemRandomChanceCondition> CODEC;
/*    */   
/* 10 */   public LootItemRandomChanceCondition(NumberProvider chance) { this.chance = chance; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition; } public NumberProvider chance() { return this.chance; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition;
/*    */     //   0	8	1	o	Ljava/lang/Object; } static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.world.level.storage.loot.providers.number.NumberProviders.CODEC.fieldOf("chance").forGetter(LootItemRandomChanceCondition::chance)).apply((com.mojang.datafixers.kinds.Applicative)i, LootItemRandomChanceCondition::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 19 */     return LootItemConditions.RANDOM_CHANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(net.minecraft.world.level.storage.loot.LootContext context) {
/* 24 */     float probability = this.chance.getFloat(context);
/* 25 */     return (context.getRandom().nextFloat() < probability);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder randomChance(float probability) {
/* 29 */     return () -> new LootItemRandomChanceCondition((NumberProvider)net.minecraft.world.level.storage.loot.providers.number.ConstantValue.exactly(probability));
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder randomChance(NumberProvider probability) {
/* 33 */     return () -> new LootItemRandomChanceCondition(probability);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */