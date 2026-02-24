/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.component.DataComponentMap;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.world.item.CrossbowItem;
/*    */ import net.minecraft.world.item.enchantment.effects.DamageImmunity;
/*    */ import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
/*    */ import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
/*    */ import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
/*    */ import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*    */ 
/*    */ public interface EnchantmentEffectComponents
/*    */ {
/* 23 */   public static final Codec<DataComponentType<?>> COMPONENT_CODEC = Codec.lazyInitialized(() -> BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE.byNameCodec());
/* 24 */   public static final Codec<DataComponentMap> CODEC = DataComponentMap.makeCodec(COMPONENT_CODEC); public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> DAMAGE_PROTECTION; public static final DataComponentType<List<ConditionalEffect<DamageImmunity>>> DAMAGE_IMMUNITY; public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> DAMAGE; public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> SMASH_DAMAGE_PER_FALLEN_BLOCK; public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> KNOCKBACK; public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> ARMOR_EFFECTIVENESS; public static final DataComponentType<List<TargetedConditionalEffect<EnchantmentEntityEffect>>> POST_ATTACK; public static final DataComponentType<List<ConditionalEffect<EnchantmentEntityEffect>>> POST_PIERCING_ATTACK; public static final DataComponentType<List<ConditionalEffect<EnchantmentEntityEffect>>> HIT_BLOCK; public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> ITEM_DAMAGE; public static final DataComponentType<List<EnchantmentAttributeEffect>> ATTRIBUTES; public static final DataComponentType<List<TargetedConditionalEffect<EnchantmentValueEffect>>> EQUIPMENT_DROPS; public static final DataComponentType<List<ConditionalEffect<EnchantmentLocationBasedEffect>>> LOCATION_CHANGED; public static final DataComponentType<List<ConditionalEffect<EnchantmentEntityEffect>>> TICK; public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> AMMO_USE;
/*    */   static {
/* 26 */     DAMAGE_PROTECTION = register("damage_protection", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
/*    */     
/* 28 */     DAMAGE_IMMUNITY = register("damage_immunity", b -> b.persistent(ConditionalEffect.<T>codec(DamageImmunity.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
/*    */     
/* 30 */     DAMAGE = register("damage", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
/*    */     
/* 32 */     SMASH_DAMAGE_PER_FALLEN_BLOCK = register("smash_damage_per_fallen_block", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
/*    */     
/* 34 */     KNOCKBACK = register("knockback", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
/*    */     
/* 36 */     ARMOR_EFFECTIVENESS = register("armor_effectiveness", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
/*    */     
/* 38 */     POST_ATTACK = register("post_attack", b -> b.persistent(TargetedConditionalEffect.<S>codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
/*    */     
/* 40 */     POST_PIERCING_ATTACK = register("post_piercing_attack", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
/*    */     
/* 42 */     HIT_BLOCK = register("hit_block", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.HIT_BLOCK).listOf()));
/*    */     
/* 44 */     ITEM_DAMAGE = register("item_damage", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ITEM).listOf()));
/*    */     
/* 46 */     ATTRIBUTES = register("attributes", b -> b.persistent(EnchantmentAttributeEffect.CODEC.codec().listOf()));
/*    */     
/* 48 */     EQUIPMENT_DROPS = register("equipment_drops", b -> b.persistent(TargetedConditionalEffect.<S>equipmentDropsCodec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
/*    */     
/* 50 */     LOCATION_CHANGED = register("location_changed", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentLocationBasedEffect.CODEC, LootContextParamSets.ENCHANTED_LOCATION).listOf()));
/*    */     
/* 52 */     TICK = register("tick", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
/*    */     
/* 54 */     AMMO_USE = register("ammo_use", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ITEM).listOf()));
/*    */     
/* 56 */     PROJECTILE_PIERCING = register("projectile_piercing", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ITEM).listOf()));
/*    */     
/* 58 */     PROJECTILE_SPAWNED = register("projectile_spawned", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
/*    */     
/* 60 */     PROJECTILE_SPREAD = register("projectile_spread", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
/*    */     
/* 62 */     PROJECTILE_COUNT = register("projectile_count", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
/*    */     
/* 64 */     TRIDENT_RETURN_ACCELERATION = register("trident_return_acceleration", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
/*    */     
/* 66 */     FISHING_TIME_REDUCTION = register("fishing_time_reduction", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
/*    */     
/* 68 */     FISHING_LUCK_BONUS = register("fishing_luck_bonus", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
/*    */     
/* 70 */     BLOCK_EXPERIENCE = register("block_experience", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ITEM).listOf()));
/*    */     
/* 72 */     MOB_EXPERIENCE = register("mob_experience", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
/*    */     
/* 74 */     REPAIR_WITH_XP = register("repair_with_xp", b -> b.persistent(ConditionalEffect.<T>codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ITEM).listOf()));
/*    */ 
/*    */ 
/*    */     
/* 78 */     CROSSBOW_CHARGE_TIME = register("crossbow_charge_time", b -> b.persistent(EnchantmentValueEffect.CODEC));
/*    */     
/* 80 */     CROSSBOW_CHARGING_SOUNDS = register("crossbow_charging_sounds", b -> b.persistent(CrossbowItem.ChargingSounds.CODEC.listOf()));
/*    */     
/* 82 */     TRIDENT_SOUND = register("trident_sound", b -> b.persistent(SoundEvent.CODEC.listOf()));
/*    */     
/* 84 */     PREVENT_EQUIPMENT_DROP = register("prevent_equipment_drop", b -> b.persistent(Unit.CODEC));
/*    */     
/* 86 */     PREVENT_ARMOR_CHANGE = register("prevent_armor_change", b -> b.persistent(Unit.CODEC));
/*    */     
/* 88 */     TRIDENT_SPIN_ATTACK_STRENGTH = register("trident_spin_attack_strength", b -> b.persistent(EnchantmentValueEffect.CODEC));
/*    */   }
/*    */   public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> PROJECTILE_PIERCING; public static final DataComponentType<List<ConditionalEffect<EnchantmentEntityEffect>>> PROJECTILE_SPAWNED; public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> PROJECTILE_SPREAD; public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> PROJECTILE_COUNT; public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> TRIDENT_RETURN_ACCELERATION; public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> FISHING_TIME_REDUCTION; public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> FISHING_LUCK_BONUS; public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> BLOCK_EXPERIENCE; public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> MOB_EXPERIENCE; public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> REPAIR_WITH_XP; public static final DataComponentType<EnchantmentValueEffect> CROSSBOW_CHARGE_TIME; public static final DataComponentType<List<CrossbowItem.ChargingSounds>> CROSSBOW_CHARGING_SOUNDS; public static final DataComponentType<List<Holder<SoundEvent>>> TRIDENT_SOUND; public static final DataComponentType<Unit> PREVENT_EQUIPMENT_DROP; public static final DataComponentType<Unit> PREVENT_ARMOR_CHANGE; public static final DataComponentType<EnchantmentValueEffect> TRIDENT_SPIN_ATTACK_STRENGTH;
/*    */   static DataComponentType<?> bootstrap(Registry<DataComponentType<?>> registry) {
/* 92 */     return DAMAGE_PROTECTION;
/*    */   }
/*    */   
/*    */   private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builder) {
/* 96 */     return (DataComponentType<T>)Registry.register(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, id, ((DataComponentType.Builder)builder.apply(DataComponentType.builder())).build());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/EnchantmentEffectComponents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */