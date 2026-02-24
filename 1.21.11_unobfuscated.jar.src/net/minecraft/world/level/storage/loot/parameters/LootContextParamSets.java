/*     */ package net.minecraft.world.level.storage.loot.parameters;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.HashBiMap;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.context.ContextKeySet;
/*     */ 
/*     */ public class LootContextParamSets {
/*  14 */   private static final BiMap<Identifier, ContextKeySet> REGISTRY = (BiMap<Identifier, ContextKeySet>)HashBiMap.create();
/*     */   
/*     */   public static final Codec<ContextKeySet> CODEC;
/*     */ 
/*     */   
/*     */   static {
/*  20 */     Objects.requireNonNull(REGISTRY.inverse()); CODEC = Identifier.CODEC.comapFlatMap(location -> (DataResult)Optional.<ContextKeySet>ofNullable((ContextKeySet)REGISTRY.get(location)).map(DataResult::success).orElseGet(()), REGISTRY.inverse()::get);
/*     */   }
/*     */   public static final ContextKeySet CHEST; public static final ContextKeySet COMMAND; public static final ContextKeySet SELECTOR; public static final ContextKeySet FISHING; public static final ContextKeySet ENTITY; public static final ContextKeySet EQUIPMENT;
/*  23 */   public static final ContextKeySet EMPTY = register("empty", builder -> {
/*     */       
/*  25 */       }); public static final ContextKeySet ARCHAEOLOGY; public static final ContextKeySet GIFT; public static final ContextKeySet PIGLIN_BARTER; public static final ContextKeySet VAULT; public static final ContextKeySet ADVANCEMENT_REWARD; public static final ContextKeySet ADVANCEMENT_ENTITY; static { CHEST = register("chest", builder -> builder.required(LootContextParams.ORIGIN).optional(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  30 */     COMMAND = register("command", builder -> builder.required(LootContextParams.ORIGIN).optional(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  35 */     SELECTOR = register("selector", builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  40 */     FISHING = register("fishing", builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.TOOL).optional(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  46 */     ENTITY = register("entity", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN).required(LootContextParams.DAMAGE_SOURCE).optional(LootContextParams.ATTACKING_ENTITY).optional(LootContextParams.DIRECT_ATTACKING_ENTITY).optional(LootContextParams.LAST_DAMAGE_PLAYER));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  55 */     EQUIPMENT = register("equipment", builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     ARCHAEOLOGY = register("archaeology", builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY).required(LootContextParams.TOOL));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  66 */     GIFT = register("gift", builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     PIGLIN_BARTER = register("barter", builder -> builder.required(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */ 
/*     */     
/*  75 */     VAULT = register("vault", builder -> builder.required(LootContextParams.ORIGIN).optional(LootContextParams.THIS_ENTITY).optional(LootContextParams.TOOL));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     ADVANCEMENT_REWARD = register("advancement_reward", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     ADVANCEMENT_ENTITY = register("advancement_entity", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     ADVANCEMENT_LOCATION = register("advancement_location", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN).required(LootContextParams.TOOL).required(LootContextParams.BLOCK_STATE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     BLOCK_USE = register("block_use", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN).required(LootContextParams.BLOCK_STATE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     ALL_PARAMS = register("generic", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.LAST_DAMAGE_PLAYER).required(LootContextParams.DAMAGE_SOURCE).required(LootContextParams.ATTACKING_ENTITY).required(LootContextParams.DIRECT_ATTACKING_ENTITY).required(LootContextParams.ORIGIN).required(LootContextParams.BLOCK_STATE).required(LootContextParams.BLOCK_ENTITY).required(LootContextParams.TOOL).required(LootContextParams.EXPLOSION_RADIUS));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 117 */     BLOCK = register("block", builder -> builder.required(LootContextParams.BLOCK_STATE).required(LootContextParams.ORIGIN).required(LootContextParams.TOOL).optional(LootContextParams.THIS_ENTITY).optional(LootContextParams.BLOCK_ENTITY).optional(LootContextParams.EXPLOSION_RADIUS));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     SHEARING = register("shearing", builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY).required(LootContextParams.TOOL));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     ENTITY_INTERACT = register("entity_interact", builder -> builder.required(LootContextParams.TARGET_ENTITY).optional(LootContextParams.INTERACTING_ENTITY).required(LootContextParams.TOOL));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     BLOCK_INTERACT = register("block_interact", builder -> builder.required(LootContextParams.BLOCK_STATE).optional(LootContextParams.BLOCK_ENTITY).optional(LootContextParams.INTERACTING_ENTITY).optional(LootContextParams.TOOL));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 145 */     ENCHANTED_DAMAGE = register("enchanted_damage", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ENCHANTMENT_LEVEL).required(LootContextParams.ORIGIN).required(LootContextParams.DAMAGE_SOURCE).optional(LootContextParams.DIRECT_ATTACKING_ENTITY).optional(LootContextParams.ATTACKING_ENTITY));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 154 */     ENCHANTED_ITEM = register("enchanted_item", builder -> builder.required(LootContextParams.TOOL).required(LootContextParams.ENCHANTMENT_LEVEL));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     ENCHANTED_LOCATION = register("enchanted_location", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ENCHANTMENT_LEVEL).required(LootContextParams.ORIGIN).required(LootContextParams.ENCHANTMENT_ACTIVE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 166 */     ENCHANTED_ENTITY = register("enchanted_entity", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ENCHANTMENT_LEVEL).required(LootContextParams.ORIGIN));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     HIT_BLOCK = register("hit_block", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ENCHANTMENT_LEVEL).required(LootContextParams.ORIGIN).required(LootContextParams.BLOCK_STATE)); }
/*     */   
/*     */   public static final ContextKeySet ADVANCEMENT_LOCATION; public static final ContextKeySet BLOCK_USE; public static final ContextKeySet ALL_PARAMS; public static final ContextKeySet BLOCK; public static final ContextKeySet SHEARING; public static final ContextKeySet ENTITY_INTERACT; public static final ContextKeySet BLOCK_INTERACT; public static final ContextKeySet ENCHANTED_DAMAGE; public static final ContextKeySet ENCHANTED_ITEM;
/*     */   public static final ContextKeySet ENCHANTED_LOCATION;
/*     */   public static final ContextKeySet ENCHANTED_ENTITY;
/*     */   public static final ContextKeySet HIT_BLOCK;
/*     */   
/*     */   private static ContextKeySet register(String name, Consumer<ContextKeySet.Builder> consumer) {
/* 180 */     ContextKeySet.Builder builder = new ContextKeySet.Builder();
/* 181 */     consumer.accept(builder);
/* 182 */     ContextKeySet result = builder.build();
/* 183 */     Identifier id = Identifier.withDefaultNamespace(name);
/* 184 */     ContextKeySet prev = (ContextKeySet)REGISTRY.put(id, result);
/* 185 */     if (prev != null) {
/* 186 */       throw new IllegalStateException("Loot table parameter set " + String.valueOf(id) + " is already registered");
/*     */     }
/* 188 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/parameters/LootContextParamSets.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */