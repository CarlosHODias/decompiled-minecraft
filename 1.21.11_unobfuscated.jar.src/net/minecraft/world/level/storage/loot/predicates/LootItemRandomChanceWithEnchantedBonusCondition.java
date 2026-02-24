/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.Enchantments;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class LootItemRandomChanceWithEnchantedBonusCondition extends Record implements LootItemCondition {
/*    */   private final float unenchantedChance;
/*    */   private final LevelBasedValue enchantedChance;
/*    */   private final Holder<Enchantment> enchantment;
/*    */   public static final com.mojang.serialization.MapCodec<LootItemRandomChanceWithEnchantedBonusCondition> CODEC;
/*    */   
/* 21 */   public LootItemRandomChanceWithEnchantedBonusCondition(float unenchantedChance, LevelBasedValue enchantedChance, Holder<Enchantment> enchantment) { this.unenchantedChance = unenchantedChance; this.enchantedChance = enchantedChance; this.enchantment = enchantment; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithEnchantedBonusCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 21 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithEnchantedBonusCondition; } public float unenchantedChance() { return this.unenchantedChance; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithEnchantedBonusCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithEnchantedBonusCondition; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithEnchantedBonusCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithEnchantedBonusCondition;
/* 21 */     //   0	8	1	o	Ljava/lang/Object; } public LevelBasedValue enchantedChance() { return this.enchantedChance; } public Holder<Enchantment> enchantment() { return this.enchantment; }
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 26 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)com.mojang.serialization.Codec.floatRange(0.0F, 1.0F).fieldOf("unenchanted_chance").forGetter(LootItemRandomChanceWithEnchantedBonusCondition::unenchantedChance), (App)LevelBasedValue.CODEC.fieldOf("enchanted_chance").forGetter(LootItemRandomChanceWithEnchantedBonusCondition::enchantedChance), (App)Enchantment.CODEC.fieldOf("enchantment").forGetter(LootItemRandomChanceWithEnchantedBonusCondition::enchantment)).apply((com.mojang.datafixers.kinds.Applicative)i, LootItemRandomChanceWithEnchantedBonusCondition::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 34 */     return LootItemConditions.RANDOM_CHANCE_WITH_ENCHANTED_BONUS;
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/* 39 */     return java.util.Set.of(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ATTACKING_ENTITY);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext context) {
/* 44 */     Entity killerEntity = (Entity)context.getOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ATTACKING_ENTITY);
/*    */     
/* 46 */     LivingEntity livingKiller = (LivingEntity)killerEntity; int enchantmentLevel = (killerEntity instanceof LivingEntity) ? net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentLevel(this.enchantment, livingKiller) : 0;
/* 47 */     float chance = (enchantmentLevel > 0) ? this.enchantedChance.calculate(enchantmentLevel) : this.unenchantedChance;
/* 48 */     return (context.getRandom().nextFloat() < chance);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder randomChanceAndLootingBoost(HolderLookup.Provider registries, float chance, float perEnchantmentLevel) {
/* 52 */     HolderLookup.RegistryLookup<Enchantment> enchantments = registries.lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT);
/* 53 */     return () -> new LootItemRandomChanceWithEnchantedBonusCondition(chance, (LevelBasedValue)new LevelBasedValue.Linear(chance + perEnchantmentLevel, perEnchantmentLevel), (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.LOOTING));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithEnchantedBonusCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */