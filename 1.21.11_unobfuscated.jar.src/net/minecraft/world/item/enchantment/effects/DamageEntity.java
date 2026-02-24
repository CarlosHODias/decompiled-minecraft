/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.damagesource.DamageType;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ 
/*    */ public final class DamageEntity extends Record implements EnchantmentEntityEffect {
/*    */   private final LevelBasedValue minDamage;
/*    */   private final LevelBasedValue maxDamage;
/*    */   private final Holder<DamageType> damageType;
/*    */   public static final com.mojang.serialization.MapCodec<DamageEntity> CODEC;
/*    */   
/* 15 */   public DamageEntity(LevelBasedValue minDamage, LevelBasedValue maxDamage, Holder<DamageType> damageType) { this.minDamage = minDamage; this.maxDamage = maxDamage; this.damageType = damageType; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/DamageEntity;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/DamageEntity; } public LevelBasedValue minDamage() { return this.minDamage; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/DamageEntity;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/DamageEntity; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/DamageEntity;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/DamageEntity;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public LevelBasedValue maxDamage() { return this.maxDamage; } public Holder<DamageType> damageType() { return this.damageType; }
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 20 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LevelBasedValue.CODEC.fieldOf("min_damage").forGetter(DamageEntity::minDamage), (App)LevelBasedValue.CODEC.fieldOf("max_damage").forGetter(DamageEntity::maxDamage), (App)DamageType.CODEC.fieldOf("damage_type").forGetter(DamageEntity::damageType)).apply((com.mojang.datafixers.kinds.Applicative)i, DamageEntity::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void apply(net.minecraft.server.level.ServerLevel serverLevel, int enchantmentLevel, net.minecraft.world.item.enchantment.EnchantedItemInUse item, net.minecraft.world.entity.Entity entity, net.minecraft.world.phys.Vec3 position) {
/* 28 */     float damage = net.minecraft.util.Mth.randomBetween(entity.getRandom(), this.minDamage.calculate(enchantmentLevel), this.maxDamage.calculate(enchantmentLevel));
/* 29 */     entity.hurtServer(serverLevel, new net.minecraft.world.damagesource.DamageSource(this.damageType, (net.minecraft.world.entity.Entity)item.owner()), damage);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<DamageEntity> codec() {
/* 34 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/effects/DamageEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */