/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ 
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ 
/*    */ public final class EnchantmentLevelProvider extends Record implements NumberProvider {
/*    */   private final LevelBasedValue amount;
/*    */   public static final com.mojang.serialization.MapCodec<EnchantmentLevelProvider> CODEC;
/*    */   
/*  9 */   public EnchantmentLevelProvider(LevelBasedValue amount) { this.amount = amount; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/number/EnchantmentLevelProvider;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/EnchantmentLevelProvider; } public LevelBasedValue amount() { return this.amount; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/number/EnchantmentLevelProvider;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/EnchantmentLevelProvider; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/number/EnchantmentLevelProvider;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/EnchantmentLevelProvider;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } static { CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)LevelBasedValue.CODEC.fieldOf("amount").forGetter(EnchantmentLevelProvider::amount)).apply((com.mojang.datafixers.kinds.Applicative)i, EnchantmentLevelProvider::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float getFloat(net.minecraft.world.level.storage.loot.LootContext context) {
/* 16 */     int level = (Integer)context.getParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ENCHANTMENT_LEVEL);
/* 17 */     return this.amount.calculate(level);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootNumberProviderType getType() {
/* 22 */     return NumberProviders.ENCHANTMENT_LEVEL;
/*    */   }
/*    */   
/*    */   public static EnchantmentLevelProvider forEnchantmentLevel(LevelBasedValue amount) {
/* 26 */     return new EnchantmentLevelProvider(amount);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/providers/number/EnchantmentLevelProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */