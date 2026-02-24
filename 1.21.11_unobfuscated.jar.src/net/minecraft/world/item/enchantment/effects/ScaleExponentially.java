/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ public final class ScaleExponentially extends Record implements EnchantmentValueEffect {
/*    */   private final net.minecraft.world.item.enchantment.LevelBasedValue base;
/*    */   private final net.minecraft.world.item.enchantment.LevelBasedValue exponent;
/*    */   public static final com.mojang.serialization.MapCodec<ScaleExponentially> CODEC;
/*    */   
/*  8 */   public ScaleExponentially(net.minecraft.world.item.enchantment.LevelBasedValue base, net.minecraft.world.item.enchantment.LevelBasedValue exponent) { this.base = base; this.exponent = exponent; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/ScaleExponentially;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ScaleExponentially; } public net.minecraft.world.item.enchantment.LevelBasedValue base() { return this.base; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/ScaleExponentially;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ScaleExponentially; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/ScaleExponentially;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/ScaleExponentially;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.world.item.enchantment.LevelBasedValue exponent() { return this.exponent; } static {
/*  9 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.world.item.enchantment.LevelBasedValue.CODEC.fieldOf("base").forGetter(ScaleExponentially::base), (com.mojang.datafixers.kinds.App)net.minecraft.world.item.enchantment.LevelBasedValue.CODEC.fieldOf("exponent").forGetter(ScaleExponentially::exponent)).apply((com.mojang.datafixers.kinds.Applicative)i, ScaleExponentially::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float process(int level, net.minecraft.util.RandomSource random, float inputValue) {
/* 16 */     return (float)(inputValue * Math.pow(this.base.calculate(level), this.exponent.calculate(level)));
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<ScaleExponentially> codec() {
/* 21 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/effects/ScaleExponentially.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */