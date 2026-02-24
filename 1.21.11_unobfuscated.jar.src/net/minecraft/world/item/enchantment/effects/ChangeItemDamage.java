/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ 
/*    */ public final class ChangeItemDamage extends Record implements EnchantmentEntityEffect {
/*    */   private final LevelBasedValue amount;
/*    */   public static final com.mojang.serialization.MapCodec<ChangeItemDamage> CODEC;
/*    */   
/* 14 */   public ChangeItemDamage(LevelBasedValue amount) { this.amount = amount; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/ChangeItemDamage;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ChangeItemDamage; } public LevelBasedValue amount() { return this.amount; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/ChangeItemDamage;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ChangeItemDamage; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/ChangeItemDamage;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/ChangeItemDamage;
/*    */     //   0	8	1	o	Ljava/lang/Object; } static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)LevelBasedValue.CODEC.fieldOf("amount").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, ChangeItemDamage::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void apply(net.minecraft.server.level.ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, net.minecraft.world.entity.Entity entity, net.minecraft.world.phys.Vec3 position) {
/* 23 */     ItemStack itemStack = item.itemStack();
/* 24 */     if (itemStack.has(net.minecraft.core.component.DataComponents.MAX_DAMAGE) && itemStack.has(net.minecraft.core.component.DataComponents.DAMAGE)) {
/* 25 */       LivingEntity livingEntity = item.owner(); ServerPlayer sp = (ServerPlayer)livingEntity, player = (livingEntity instanceof ServerPlayer) ? sp : null;
/* 26 */       int change = (int)this.amount.calculate(enchantmentLevel);
/* 27 */       itemStack.hurtAndBreak(change, serverLevel, player, item.onBreak());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<ChangeItemDamage> codec() {
/* 33 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/effects/ChangeItemDamage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */