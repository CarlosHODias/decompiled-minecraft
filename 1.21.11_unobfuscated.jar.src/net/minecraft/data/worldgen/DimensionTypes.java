/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.biome.OverworldBiomes;
/*     */ import net.minecraft.sounds.Musics;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.TimelineTags;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.attribute.AmbientSounds;
/*     */ import net.minecraft.world.attribute.BackgroundMusic;
/*     */ import net.minecraft.world.attribute.BedRule;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeMap;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.timeline.Timeline;
/*     */ import net.minecraft.world.timeline.Timelines;
/*     */ 
/*     */ public class DimensionTypes
/*     */ {
/*     */   public static void bootstrap(BootstrapContext<DimensionType> context) {
/*  28 */     HolderGetter<Timeline> timelines = context.lookup(Registries.TIMELINE);
/*     */     
/*  30 */     EnvironmentAttributeMap overworldAttributes = EnvironmentAttributeMap.builder()
/*  31 */       .set(EnvironmentAttributes.FOG_COLOR, -4138753)
/*  32 */       .set(EnvironmentAttributes.SKY_COLOR, OverworldBiomes.calculateSkyColor(0.8F))
/*  33 */       .set(EnvironmentAttributes.CLOUD_COLOR, ARGB.white(0.8F))
/*  34 */       .set(EnvironmentAttributes.CLOUD_HEIGHT, 192.33F)
/*  35 */       .set(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.OVERWORLD)
/*  36 */       .set(EnvironmentAttributes.BED_RULE, BedRule.CAN_SLEEP_WHEN_DARK)
/*  37 */       .set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, false)
/*  38 */       .set(EnvironmentAttributes.NETHER_PORTAL_SPAWNS_PIGLINS, true)
/*  39 */       .set(EnvironmentAttributes.AMBIENT_SOUNDS, AmbientSounds.LEGACY_CAVE_SETTINGS)
/*  40 */       .build();
/*  41 */     context.register(BuiltinDimensionTypes.OVERWORLD, new DimensionType(false, true, false, 1.0D, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, 0.0F, new DimensionType.MonsterSettings(
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
/*  52 */             (IntProvider)UniformInt.of(0, 7), 0), DimensionType.Skybox.OVERWORLD, DimensionType.CardinalLightType.DEFAULT, overworldAttributes, (HolderSet)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  58 */           timelines.getOrThrow(TimelineTags.IN_OVERWORLD)));
/*     */ 
/*     */     
/*  61 */     context.register(BuiltinDimensionTypes.NETHER, new DimensionType(true, false, true, 8.0D, 0, 256, 128, BlockTags.INFINIBURN_NETHER, 0.1F, new DimensionType.MonsterSettings(
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
/*  72 */             (IntProvider)ConstantInt.of(7), 15), DimensionType.Skybox.NONE, DimensionType.CardinalLightType.NETHER, 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  77 */           EnvironmentAttributeMap.builder()
/*  78 */           .set(EnvironmentAttributes.FOG_START_DISTANCE, 10.0F)
/*  79 */           .set(EnvironmentAttributes.FOG_END_DISTANCE, 96.0F)
/*  80 */           .set(EnvironmentAttributes.SKY_LIGHT_COLOR, Timelines.NIGHT_SKY_LIGHT_COLOR)
/*  81 */           .set(EnvironmentAttributes.SKY_LIGHT_LEVEL, 4.0F)
/*  82 */           .set(EnvironmentAttributes.SKY_LIGHT_FACTOR, 0.0F)
/*  83 */           .set(EnvironmentAttributes.DEFAULT_DRIPSTONE_PARTICLE, ParticleTypes.DRIPPING_DRIPSTONE_LAVA)
/*  84 */           .set(EnvironmentAttributes.BED_RULE, BedRule.EXPLODES)
/*  85 */           .set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, true)
/*  86 */           .set(EnvironmentAttributes.WATER_EVAPORATES, true)
/*  87 */           .set(EnvironmentAttributes.FAST_LAVA, true)
/*  88 */           .set(EnvironmentAttributes.PIGLINS_ZOMBIFY, false)
/*  89 */           .set(EnvironmentAttributes.CAN_START_RAID, false)
/*  90 */           .set(EnvironmentAttributes.SNOW_GOLEM_MELTS, true)
/*  91 */           .build(), (HolderSet)
/*  92 */           timelines.getOrThrow(TimelineTags.IN_NETHER)));
/*     */ 
/*     */     
/*  95 */     context.register(BuiltinDimensionTypes.END, new DimensionType(true, true, false, 1.0D, 0, 256, 256, BlockTags.INFINIBURN_END, 0.25F, new DimensionType.MonsterSettings(
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
/* 106 */             (IntProvider)ConstantInt.of(15), 0), DimensionType.Skybox.END, DimensionType.CardinalLightType.DEFAULT, 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 111 */           EnvironmentAttributeMap.builder()
/* 112 */           .set(EnvironmentAttributes.FOG_COLOR, -15199464)
/* 113 */           .set(EnvironmentAttributes.SKY_LIGHT_COLOR, -1736449)
/* 114 */           .set(EnvironmentAttributes.SKY_COLOR, -16777216)
/* 115 */           .set(EnvironmentAttributes.SKY_LIGHT_FACTOR, 0.0F)
/* 116 */           .set(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(Musics.END))
/* 117 */           .set(EnvironmentAttributes.AMBIENT_SOUNDS, AmbientSounds.LEGACY_CAVE_SETTINGS)
/* 118 */           .set(EnvironmentAttributes.BED_RULE, BedRule.EXPLODES)
/* 119 */           .set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, false)
/* 120 */           .build(), (HolderSet)
/* 121 */           timelines.getOrThrow(TimelineTags.IN_END)));
/*     */ 
/*     */ 
/*     */     
/* 125 */     context.register(BuiltinDimensionTypes.OVERWORLD_CAVES, new DimensionType(false, true, true, 1.0D, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, 0.0F, new DimensionType.MonsterSettings(
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
/* 136 */             (IntProvider)UniformInt.of(0, 7), 0), DimensionType.Skybox.OVERWORLD, DimensionType.CardinalLightType.DEFAULT, overworldAttributes, (HolderSet)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 142 */           timelines.getOrThrow(TimelineTags.IN_OVERWORLD)));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/worldgen/DimensionTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */