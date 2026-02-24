/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ 
/*    */ public final class EnchantmentAttributeEffect extends Record implements EnchantmentLocationBasedEffect {
/*    */   private final Identifier id;
/*    */   private final Holder<Attribute> attribute;
/*    */   private final LevelBasedValue amount;
/*    */   private final AttributeModifier.Operation operation;
/*    */   public static final com.mojang.serialization.MapCodec<EnchantmentAttributeEffect> CODEC;
/*    */   
/* 19 */   public EnchantmentAttributeEffect(Identifier id, Holder<Attribute> attribute, LevelBasedValue amount, AttributeModifier.Operation operation) { this.id = id; this.attribute = attribute; this.amount = amount; this.operation = operation; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/EnchantmentAttributeEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 19 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/EnchantmentAttributeEffect; } public Identifier id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/EnchantmentAttributeEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/EnchantmentAttributeEffect; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/EnchantmentAttributeEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/EnchantmentAttributeEffect;
/* 19 */     //   0	8	1	o	Ljava/lang/Object; } public Holder<Attribute> attribute() { return this.attribute; } public LevelBasedValue amount() { return this.amount; } public AttributeModifier.Operation operation() { return this.operation; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 25 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("id").forGetter(EnchantmentAttributeEffect::id), (App)Attribute.CODEC.fieldOf("attribute").forGetter(EnchantmentAttributeEffect::attribute), (App)LevelBasedValue.CODEC.fieldOf("amount").forGetter(EnchantmentAttributeEffect::amount), (App)AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(EnchantmentAttributeEffect::operation)).apply((com.mojang.datafixers.kinds.Applicative)i, EnchantmentAttributeEffect::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Identifier idForSlot(net.minecraft.util.StringRepresentable slot) {
/* 33 */     return this.id.withSuffix("/" + slot.getSerializedName());
/*    */   }
/*    */   
/*    */   public AttributeModifier getModifier(int level, net.minecraft.util.StringRepresentable slot) {
/* 37 */     return new AttributeModifier(idForSlot(slot), amount().calculate(level), operation());
/*    */   }
/*    */ 
/*    */   
/*    */   public void onChangedBlock(net.minecraft.server.level.ServerLevel serverLevel, int enchantmentLevel, net.minecraft.world.item.enchantment.EnchantedItemInUse item, net.minecraft.world.entity.Entity entity, net.minecraft.world.phys.Vec3 position, boolean becameActive) {
/* 42 */     if (becameActive && entity instanceof LivingEntity) { LivingEntity living = (LivingEntity)entity;
/* 43 */       living.getAttributes().addTransientAttributeModifiers((com.google.common.collect.Multimap)makeAttributeMap(enchantmentLevel, item.inSlot())); }
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDeactivated(net.minecraft.world.item.enchantment.EnchantedItemInUse item, net.minecraft.world.entity.Entity entity, net.minecraft.world.phys.Vec3 position, int level) {
/* 49 */     if (entity instanceof LivingEntity) { LivingEntity living = (LivingEntity)entity;
/* 50 */       living.getAttributes().removeAttributeModifiers((com.google.common.collect.Multimap)makeAttributeMap(level, item.inSlot())); }
/*    */   
/*    */   }
/*    */   
/*    */   private com.google.common.collect.HashMultimap<Holder<Attribute>, AttributeModifier> makeAttributeMap(int enchantmentLevel, net.minecraft.world.entity.EquipmentSlot slot) {
/* 55 */     com.google.common.collect.HashMultimap<Holder<Attribute>, AttributeModifier> map = com.google.common.collect.HashMultimap.create();
/* 56 */     map.put(this.attribute, getModifier(enchantmentLevel, (net.minecraft.util.StringRepresentable)slot));
/* 57 */     return map;
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<EnchantmentAttributeEffect> codec() {
/* 62 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/effects/EnchantmentAttributeEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */