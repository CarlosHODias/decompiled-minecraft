/*    */ package net.minecraft.world.entity.npc.villager;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.RegistryFixedCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.Biomes;
/*    */ 
/*    */ public final class VillagerType {
/* 21 */   public static final ResourceKey<VillagerType> DESERT = createKey("desert");
/* 22 */   public static final ResourceKey<VillagerType> JUNGLE = createKey("jungle");
/* 23 */   public static final ResourceKey<VillagerType> PLAINS = createKey("plains");
/* 24 */   public static final ResourceKey<VillagerType> SAVANNA = createKey("savanna");
/* 25 */   public static final ResourceKey<VillagerType> SNOW = createKey("snow");
/* 26 */   public static final ResourceKey<VillagerType> SWAMP = createKey("swamp");
/* 27 */   public static final ResourceKey<VillagerType> TAIGA = createKey("taiga");
/*    */   
/*    */   private static ResourceKey<VillagerType> createKey(String name) {
/* 30 */     return ResourceKey.create(Registries.VILLAGER_TYPE, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */   
/* 33 */   public static final Codec<Holder<VillagerType>> CODEC = (Codec<Holder<VillagerType>>)RegistryFixedCodec.create(Registries.VILLAGER_TYPE);
/* 34 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<VillagerType>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.VILLAGER_TYPE); private static final Map<ResourceKey<Biome>, ResourceKey<VillagerType>> BY_BIOME;
/*    */   
/*    */   private static VillagerType register(Registry<VillagerType> registry, ResourceKey<VillagerType> name) {
/* 37 */     return (VillagerType)Registry.register(registry, name, new VillagerType());
/*    */   }
/*    */   
/*    */   public static VillagerType bootstrap(Registry<VillagerType> registry) {
/* 41 */     register(registry, DESERT);
/* 42 */     register(registry, JUNGLE);
/* 43 */     register(registry, PLAINS);
/* 44 */     register(registry, SAVANNA);
/* 45 */     register(registry, SNOW);
/* 46 */     register(registry, SWAMP);
/* 47 */     return register(registry, TAIGA);
/*    */   }
/*    */   static {
/* 50 */     BY_BIOME = (Map<ResourceKey<Biome>, ResourceKey<VillagerType>>)Util.make(Maps.newHashMap(), map -> {
/*    */           map.put(Biomes.BADLANDS, DESERT);
/*    */           map.put(Biomes.DESERT, DESERT);
/*    */           map.put(Biomes.ERODED_BADLANDS, DESERT);
/*    */           map.put(Biomes.WOODED_BADLANDS, DESERT);
/*    */           map.put(Biomes.BAMBOO_JUNGLE, JUNGLE);
/*    */           map.put(Biomes.JUNGLE, JUNGLE);
/*    */           map.put(Biomes.SPARSE_JUNGLE, JUNGLE);
/*    */           map.put(Biomes.SAVANNA_PLATEAU, SAVANNA);
/*    */           map.put(Biomes.SAVANNA, SAVANNA);
/*    */           map.put(Biomes.WINDSWEPT_SAVANNA, SAVANNA);
/*    */           map.put(Biomes.DEEP_FROZEN_OCEAN, SNOW);
/*    */           map.put(Biomes.FROZEN_OCEAN, SNOW);
/*    */           map.put(Biomes.FROZEN_RIVER, SNOW);
/*    */           map.put(Biomes.ICE_SPIKES, SNOW);
/*    */           map.put(Biomes.SNOWY_BEACH, SNOW);
/*    */           map.put(Biomes.SNOWY_TAIGA, SNOW);
/*    */           map.put(Biomes.SNOWY_PLAINS, SNOW);
/*    */           map.put(Biomes.GROVE, SNOW);
/*    */           map.put(Biomes.SNOWY_SLOPES, SNOW);
/*    */           map.put(Biomes.FROZEN_PEAKS, SNOW);
/*    */           map.put(Biomes.JAGGED_PEAKS, SNOW);
/*    */           map.put(Biomes.SWAMP, SWAMP);
/*    */           map.put(Biomes.MANGROVE_SWAMP, SWAMP);
/*    */           map.put(Biomes.OLD_GROWTH_SPRUCE_TAIGA, TAIGA);
/*    */           map.put(Biomes.OLD_GROWTH_PINE_TAIGA, TAIGA);
/*    */           map.put(Biomes.WINDSWEPT_GRAVELLY_HILLS, TAIGA);
/*    */           map.put(Biomes.WINDSWEPT_HILLS, TAIGA);
/*    */           map.put(Biomes.TAIGA, TAIGA);
/*    */           map.put(Biomes.WINDSWEPT_FOREST, TAIGA);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ResourceKey<VillagerType> byBiome(Holder<Biome> biome) {
/* 90 */     Objects.requireNonNull(BY_BIOME); return biome.unwrapKey().map(BY_BIOME::get).orElse(PLAINS);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/npc/villager/VillagerType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */