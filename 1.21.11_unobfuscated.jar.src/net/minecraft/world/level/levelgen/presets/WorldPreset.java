/*    */ package net.minecraft.world.level.levelgen.presets;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.dimension.LevelStem;
/*    */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*    */ 
/*    */ public class WorldPreset {
/*    */   static {
/* 21 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.unboundedMap(ResourceKey.codec(Registries.LEVEL_STEM), LevelStem.CODEC).fieldOf("dimensions").forGetter(())).apply((Applicative)i, WorldPreset::new)).validate(WorldPreset::requireOverworld);
/*    */   }
/* 23 */   public static final Codec<Holder<WorldPreset>> CODEC = (Codec<Holder<WorldPreset>>)RegistryFileCodec.create(Registries.WORLD_PRESET, DIRECT_CODEC);
/*    */   public static final Codec<WorldPreset> DIRECT_CODEC;
/*    */   private final Map<ResourceKey<LevelStem>, LevelStem> dimensions;
/*    */   
/*    */   public WorldPreset(Map<ResourceKey<LevelStem>, LevelStem> dimensions) {
/* 28 */     this.dimensions = dimensions;
/*    */   }
/*    */   
/*    */   private ImmutableMap<ResourceKey<LevelStem>, LevelStem> dimensionsInOrder() {
/* 32 */     ImmutableMap.Builder<ResourceKey<LevelStem>, LevelStem> builder = ImmutableMap.builder();
/* 33 */     WorldDimensions.keysInOrder(this.dimensions.keySet().stream()).forEach(key -> {
/*    */           LevelStem levelStem = this.dimensions.get(builder);
/*    */           if (levelStem != null) {
/*    */             builder.put(builder, levelStem);
/*    */           }
/*    */         });
/* 39 */     return builder.build();
/*    */   }
/*    */   
/*    */   public WorldDimensions createWorldDimensions() {
/* 43 */     return new WorldDimensions((Map)dimensionsInOrder());
/*    */   }
/*    */   
/*    */   public Optional<LevelStem> overworld() {
/* 47 */     return Optional.ofNullable(this.dimensions.get(LevelStem.OVERWORLD));
/*    */   }
/*    */ 
/*    */   
/*    */   private static DataResult<WorldPreset> requireOverworld(WorldPreset preset) {
/* 52 */     if (preset.overworld().isEmpty()) {
/* 53 */       return DataResult.error(() -> "Missing overworld dimension");
/*    */     }
/* 55 */     return DataResult.success(preset, Lifecycle.stable());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/presets/WorldPreset.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */