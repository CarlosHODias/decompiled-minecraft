/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ 
/*    */ public final class SetValue extends Record implements EnchantmentValueEffect {
/*    */   private final net.minecraft.world.item.enchantment.LevelBasedValue value;
/*    */   public static final com.mojang.serialization.MapCodec<SetValue> CODEC;
/*    */   
/*  8 */   public SetValue(net.minecraft.world.item.enchantment.LevelBasedValue value) { this.value = value; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/SetValue;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SetValue; } public net.minecraft.world.item.enchantment.LevelBasedValue value() { return this.value; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/SetValue;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SetValue; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/SetValue;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/SetValue;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } static { CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.world.item.enchantment.LevelBasedValue.CODEC.fieldOf("value").forGetter(SetValue::value)).apply((com.mojang.datafixers.kinds.Applicative)i, SetValue::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float process(int enchantmentLevel, net.minecraft.util.RandomSource random, float inputValue) {
/* 15 */     return this.value.calculate(enchantmentLevel);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<SetValue> codec() {
/* 20 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/effects/SetValue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */