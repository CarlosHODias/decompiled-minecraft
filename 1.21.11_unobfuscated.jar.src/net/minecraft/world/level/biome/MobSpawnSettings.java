/*     */ package net.minecraft.world.level.biome;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Keyable;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class MobSpawnSettings {
/*  25 */   private static final Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*     */   private static final float DEFAULT_CREATURE_SPAWN_PROBABILITY = 0.1F;
/*  27 */   public static final WeightedList<SpawnerData> EMPTY_MOB_LIST = WeightedList.of();
/*  28 */   public static final MobSpawnSettings EMPTY = new Builder().build(); public static final MapCodec<MobSpawnSettings> CODEC; private final float creatureGenerationProbability; private final Map<MobCategory, WeightedList<SpawnerData>> spawners; private final Map<EntityType<?>, MobSpawnCost> mobSpawnCosts;
/*     */   static {
/*  30 */     CODEC = RecordCodecBuilder.mapCodec(i -> {
/*     */           Objects.requireNonNull(LOGGER);
/*     */           return i.group((App)Codec.floatRange(0.0F, 0.9999999F).optionalFieldOf("creature_spawn_probability", 0.1F).forGetter(()), (App)Codec.simpleMap(MobCategory.CODEC, WeightedList.codec(SpawnerData.CODEC).promotePartial(Util.prefix("Spawn data: ", LOGGER::error)), StringRepresentable.keys((StringRepresentable[])MobCategory.values())).fieldOf("spawners").forGetter(()), (App)Codec.simpleMap(BuiltInRegistries.ENTITY_TYPE.byNameCodec(), MobSpawnCost.CODEC, (Keyable)BuiltInRegistries.ENTITY_TYPE).fieldOf("spawn_costs").forGetter(())).apply((Applicative)i, MobSpawnSettings::new);
/*     */         });
/*     */   }
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
/*     */   
/*     */   private MobSpawnSettings(float creatureGenerationProbability, Map<MobCategory, WeightedList<SpawnerData>> spawners, Map<EntityType<?>, MobSpawnCost> mobSpawnCosts) {
/*  49 */     this.creatureGenerationProbability = creatureGenerationProbability;
/*  50 */     this.spawners = (Map<MobCategory, WeightedList<SpawnerData>>)ImmutableMap.copyOf(spawners);
/*  51 */     this.mobSpawnCosts = (Map<EntityType<?>, MobSpawnCost>)ImmutableMap.copyOf(mobSpawnCosts);
/*     */   }
/*     */   
/*     */   public WeightedList<SpawnerData> getMobs(MobCategory category) {
/*  55 */     return this.spawners.getOrDefault(category, EMPTY_MOB_LIST);
/*     */   }
/*     */   
/*     */   public MobSpawnCost getMobSpawnCost(EntityType<?> type) {
/*  59 */     return this.mobSpawnCosts.get(type);
/*     */   }
/*     */   
/*     */   public float getCreatureProbability() {
/*  63 */     return this.creatureGenerationProbability;
/*     */   } public static final class SpawnerData extends Record {
/*     */     private final EntityType<?> type; private final int minCount; private final int maxCount;
/*  66 */     public EntityType<?> type() { return this.type; } public static final MapCodec<SpawnerData> CODEC; public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/MobSpawnSettings$SpawnerData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/MobSpawnSettings$SpawnerData; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/MobSpawnSettings$SpawnerData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/biome/MobSpawnSettings$SpawnerData;
/*  66 */       //   0	8	1	o	Ljava/lang/Object; } public int minCount() { return this.minCount; } public int maxCount() { return this.maxCount; }
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*  71 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("type").forGetter(()), (App)ExtraCodecs.POSITIVE_INT.fieldOf("minCount").forGetter(()), (App)ExtraCodecs.POSITIVE_INT.fieldOf("maxCount").forGetter(())).apply((Applicative)i, SpawnerData::new)).validate(spawnerData -> (spawnerData.minCount > spawnerData.maxCount) ? DataResult.error(()) : DataResult.success(spawnerData));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SpawnerData(EntityType<?> type, int minCount, int maxCount) {
/*  79 */       type = (type.getCategory() == MobCategory.MISC) ? EntityType.PIG : type;
/*     */       this.type = type;
/*     */       this.minCount = minCount;
/*     */       this.maxCount = maxCount;
/*     */     } public String toString() {
/*  84 */       return String.valueOf(EntityType.getKey(this.type)) + "*(" + String.valueOf(EntityType.getKey(this.type)) + "-" + this.minCount + ")";
/*     */     } }
/*     */   public static final class MobSpawnCost extends Record { private final double energyBudget; private final double charge; public static final Codec<MobSpawnCost> CODEC;
/*     */     
/*  88 */     public MobSpawnCost(double energyBudget, double charge) { this.energyBudget = energyBudget; this.charge = charge; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/MobSpawnSettings$MobSpawnCost;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/MobSpawnSettings$MobSpawnCost; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/MobSpawnSettings$MobSpawnCost;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/MobSpawnSettings$MobSpawnCost; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/MobSpawnSettings$MobSpawnCost;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/biome/MobSpawnSettings$MobSpawnCost;
/*  88 */       //   0	8	1	o	Ljava/lang/Object; } public double energyBudget() { return this.energyBudget; } public double charge() { return this.charge; } static {
/*  89 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.DOUBLE.fieldOf("energy_budget").forGetter(()), (App)Codec.DOUBLE.fieldOf("charge").forGetter(())).apply((Applicative)i, MobSpawnCost::new));
/*     */     } }
/*     */   public static class Builder { private final Map<MobCategory, WeightedList.Builder<MobSpawnSettings.SpawnerData>> spawners;
/*     */     private final Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> mobSpawnCosts;
/*     */     private float creatureGenerationProbability;
/*     */     
/*     */     public Builder() {
/*  96 */       this.spawners = Util.makeEnumMap(MobCategory.class, c -> WeightedList.builder());
/*  97 */       this.mobSpawnCosts = Maps.newLinkedHashMap();
/*  98 */       this.creatureGenerationProbability = 0.1F;
/*     */     }
/*     */     public Builder addSpawn(MobCategory category, int weight, MobSpawnSettings.SpawnerData spawnerData) {
/* 101 */       ((WeightedList.Builder)this.spawners.get(category)).add(spawnerData, weight);
/* 102 */       return this;
/*     */     }
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
/*     */     public Builder addMobCharge(EntityType<?> type, double charge, double energyBudget) {
/* 128 */       this.mobSpawnCosts.put(type, new MobSpawnSettings.MobSpawnCost(energyBudget, charge));
/* 129 */       return this;
/*     */     }
/*     */     
/*     */     public Builder creatureGenerationProbability(float creatureGenerationProbability) {
/* 133 */       this.creatureGenerationProbability = creatureGenerationProbability;
/* 134 */       return this;
/*     */     }
/*     */     
/*     */     public MobSpawnSettings build() {
/* 138 */       return new MobSpawnSettings(this.creatureGenerationProbability, (Map<MobCategory, WeightedList<MobSpawnSettings.SpawnerData>>)
/*     */           
/* 140 */           this.spawners.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, e -> ((WeightedList.Builder)e.getValue()).build())), 
/* 141 */           (Map<EntityType<?>, MobSpawnSettings.MobSpawnCost>)ImmutableMap.copyOf(this.mobSpawnCosts));
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/biome/MobSpawnSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */