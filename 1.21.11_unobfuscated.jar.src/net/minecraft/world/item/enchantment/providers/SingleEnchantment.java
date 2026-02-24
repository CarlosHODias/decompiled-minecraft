/*    */ package net.minecraft.world.item.enchantment.providers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ 
/*    */ public final class SingleEnchantment extends Record implements EnchantmentProvider {
/*    */   private final Holder<Enchantment> enchantment;
/*    */   private final IntProvider level;
/*    */   public static final com.mojang.serialization.MapCodec<SingleEnchantment> CODEC;
/*    */   
/* 14 */   public SingleEnchantment(Holder<Enchantment> enchantment, IntProvider level) { this.enchantment = enchantment; this.level = level; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/providers/SingleEnchantment;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/providers/SingleEnchantment; } public Holder<Enchantment> enchantment() { return this.enchantment; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/providers/SingleEnchantment;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/providers/SingleEnchantment; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/providers/SingleEnchantment;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/providers/SingleEnchantment;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public IntProvider level() { return this.level; }
/*    */ 
/*    */   
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Enchantment.CODEC.fieldOf("enchantment").forGetter(SingleEnchantment::enchantment), (App)IntProvider.CODEC.fieldOf("level").forGetter(SingleEnchantment::level)).apply((com.mojang.datafixers.kinds.Applicative)i, SingleEnchantment::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void enchant(net.minecraft.world.item.ItemStack item, net.minecraft.world.item.enchantment.ItemEnchantments.Mutable itemEnchantments, net.minecraft.util.RandomSource random, net.minecraft.world.DifficultyInstance difficulty) {
/* 25 */     itemEnchantments.upgrade(this.enchantment, net.minecraft.util.Mth.clamp(this.level.sample(random), ((Enchantment)this.enchantment.value()).getMinLevel(), ((Enchantment)this.enchantment.value()).getMaxLevel()));
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<SingleEnchantment> codec() {
/* 30 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/providers/SingleEnchantment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */