/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ 
/*    */ public final class Ignite extends Record implements EnchantmentEntityEffect {
/*    */   private final LevelBasedValue duration;
/*    */   public static final com.mojang.serialization.MapCodec<Ignite> CODEC;
/*    */   
/* 11 */   public Ignite(LevelBasedValue duration) { this.duration = duration; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/Ignite;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/Ignite; } public LevelBasedValue duration() { return this.duration; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/Ignite;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/Ignite; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/Ignite;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/Ignite;
/*    */     //   0	8	1	o	Ljava/lang/Object; } static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LevelBasedValue.CODEC.fieldOf("duration").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, Ignite::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void apply(net.minecraft.server.level.ServerLevel serverLevel, int enchantmentLevel, net.minecraft.world.item.enchantment.EnchantedItemInUse item, net.minecraft.world.entity.Entity entity, net.minecraft.world.phys.Vec3 position) {
/* 20 */     entity.igniteForSeconds(this.duration.calculate(enchantmentLevel));
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<Ignite> codec() {
/* 25 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/effects/Ignite.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */