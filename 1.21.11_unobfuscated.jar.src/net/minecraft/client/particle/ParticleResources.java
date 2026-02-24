/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.renderer.texture.SpriteLoader;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.model.AtlasManager;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleType;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.data.AtlasIds;
/*     */ import net.minecraft.resources.FileToIdConverter;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ParticleResources implements PreparableReloadListener {
/*  42 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  43 */   private static final FileToIdConverter PARTICLE_LISTER = FileToIdConverter.json("particles");
/*  44 */   private final Map<Identifier, MutableSpriteSet> spriteSets = Maps.newHashMap();
/*  45 */   private final Int2ObjectMap<ParticleProvider<?>> providers = (Int2ObjectMap<ParticleProvider<?>>)new Int2ObjectOpenHashMap();
/*     */ 
/*     */ 
/*     */   
/*     */   private Runnable onReload;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParticleResources() {
/*  55 */     registerProviders();
/*     */   }
/*     */   
/*     */   public void onReload(Runnable onReload) {
/*  59 */     this.onReload = onReload;
/*     */   }
/*     */   
/*     */   private void registerProviders() {
/*  63 */     register((ParticleType<ParticleOptions>)ParticleTypes.ANGRY_VILLAGER, AngryVillagerProvider::new);
/*  64 */     register(ParticleTypes.BLOCK_MARKER, (ParticleProvider<ParticleOptions>)new BlockMarker.Provider());
/*  65 */     register(ParticleTypes.BLOCK, (ParticleProvider<ParticleOptions>)new TerrainParticle.Provider());
/*  66 */     register((ParticleType<ParticleOptions>)ParticleTypes.BUBBLE, Provider::new);
/*  67 */     register((ParticleType<ParticleOptions>)ParticleTypes.BUBBLE_COLUMN_UP, Provider::new);
/*  68 */     register((ParticleType<ParticleOptions>)ParticleTypes.BUBBLE_POP, Provider::new);
/*  69 */     register((ParticleType<ParticleOptions>)ParticleTypes.CAMPFIRE_COSY_SMOKE, CosyProvider::new);
/*  70 */     register((ParticleType<ParticleOptions>)ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, SignalProvider::new);
/*  71 */     register((ParticleType<ParticleOptions>)ParticleTypes.CLOUD, Provider::new);
/*  72 */     register((ParticleType<ParticleOptions>)ParticleTypes.COMPOSTER, ComposterFillProvider::new);
/*  73 */     register((ParticleType<ParticleOptions>)ParticleTypes.COPPER_FIRE_FLAME, Provider::new);
/*  74 */     register((ParticleType<ParticleOptions>)ParticleTypes.CRIT, Provider::new);
/*  75 */     register((ParticleType<ParticleOptions>)ParticleTypes.CURRENT_DOWN, Provider::new);
/*  76 */     register((ParticleType<ParticleOptions>)ParticleTypes.DAMAGE_INDICATOR, DamageIndicatorProvider::new);
/*  77 */     register(ParticleTypes.DRAGON_BREATH, Provider::new);
/*  78 */     register((ParticleType<ParticleOptions>)ParticleTypes.DOLPHIN, DolphinSpeedProvider::new);
/*  79 */     register((ParticleType<ParticleOptions>)ParticleTypes.DRIPPING_LAVA, LavaHangProvider::new);
/*  80 */     register((ParticleType<ParticleOptions>)ParticleTypes.FALLING_LAVA, LavaFallProvider::new);
/*  81 */     register((ParticleType<ParticleOptions>)ParticleTypes.LANDING_LAVA, LavaLandProvider::new);
/*  82 */     register((ParticleType<ParticleOptions>)ParticleTypes.DRIPPING_WATER, WaterHangProvider::new);
/*  83 */     register((ParticleType<ParticleOptions>)ParticleTypes.FALLING_WATER, WaterFallProvider::new);
/*  84 */     register(ParticleTypes.DUST, Provider::new);
/*  85 */     register(ParticleTypes.DUST_COLOR_TRANSITION, Provider::new);
/*  86 */     register(ParticleTypes.EFFECT, InstantProvider::new);
/*  87 */     register((ParticleType<ParticleOptions>)ParticleTypes.ELDER_GUARDIAN, (ParticleProvider<ParticleOptions>)new ElderGuardianParticle.Provider());
/*  88 */     register((ParticleType<ParticleOptions>)ParticleTypes.ENCHANTED_HIT, MagicProvider::new);
/*  89 */     register((ParticleType<ParticleOptions>)ParticleTypes.ENCHANT, EnchantProvider::new);
/*  90 */     register((ParticleType<ParticleOptions>)ParticleTypes.END_ROD, Provider::new);
/*  91 */     register(ParticleTypes.ENTITY_EFFECT, MobEffectProvider::new);
/*  92 */     register((ParticleType<ParticleOptions>)ParticleTypes.EXPLOSION_EMITTER, (ParticleProvider<ParticleOptions>)new HugeExplosionSeedParticle.Provider());
/*  93 */     register((ParticleType<ParticleOptions>)ParticleTypes.EXPLOSION, Provider::new);
/*  94 */     register((ParticleType<ParticleOptions>)ParticleTypes.SONIC_BOOM, Provider::new);
/*  95 */     register(ParticleTypes.FALLING_DUST, Provider::new);
/*  96 */     register((ParticleType<ParticleOptions>)ParticleTypes.GUST, Provider::new);
/*  97 */     register((ParticleType<ParticleOptions>)ParticleTypes.SMALL_GUST, SmallProvider::new);
/*  98 */     register((ParticleType<ParticleOptions>)ParticleTypes.GUST_EMITTER_LARGE, (ParticleProvider<ParticleOptions>)new GustSeedParticle.Provider(3.0D, 7, 0));
/*  99 */     register((ParticleType<ParticleOptions>)ParticleTypes.GUST_EMITTER_SMALL, (ParticleProvider<ParticleOptions>)new GustSeedParticle.Provider(1.0D, 3, 2));
/* 100 */     register((ParticleType<ParticleOptions>)ParticleTypes.FIREWORK, SparkProvider::new);
/* 101 */     register((ParticleType<ParticleOptions>)ParticleTypes.FISHING, Provider::new);
/* 102 */     register((ParticleType<ParticleOptions>)ParticleTypes.FLAME, Provider::new);
/* 103 */     register((ParticleType<ParticleOptions>)ParticleTypes.INFESTED, Provider::new);
/* 104 */     register((ParticleType<ParticleOptions>)ParticleTypes.SCULK_SOUL, EmissiveProvider::new);
/* 105 */     register(ParticleTypes.SCULK_CHARGE, Provider::new);
/* 106 */     register((ParticleType<ParticleOptions>)ParticleTypes.SCULK_CHARGE_POP, Provider::new);
/* 107 */     register((ParticleType<ParticleOptions>)ParticleTypes.SOUL, Provider::new);
/* 108 */     register((ParticleType<ParticleOptions>)ParticleTypes.SOUL_FIRE_FLAME, Provider::new);
/* 109 */     register(ParticleTypes.FLASH, FlashProvider::new);
/* 110 */     register((ParticleType<ParticleOptions>)ParticleTypes.HAPPY_VILLAGER, HappyVillagerProvider::new);
/* 111 */     register((ParticleType<ParticleOptions>)ParticleTypes.HEART, Provider::new);
/* 112 */     register(ParticleTypes.INSTANT_EFFECT, InstantProvider::new);
/* 113 */     register(ParticleTypes.ITEM, (ParticleProvider<ParticleOptions>)new BreakingItemParticle.Provider());
/* 114 */     register((ParticleType<ParticleOptions>)ParticleTypes.ITEM_SLIME, (ParticleProvider<ParticleOptions>)new BreakingItemParticle.SlimeProvider());
/* 115 */     register((ParticleType<ParticleOptions>)ParticleTypes.ITEM_COBWEB, (ParticleProvider<ParticleOptions>)new BreakingItemParticle.CobwebProvider());
/* 116 */     register((ParticleType<ParticleOptions>)ParticleTypes.ITEM_SNOWBALL, (ParticleProvider<ParticleOptions>)new BreakingItemParticle.SnowballProvider());
/* 117 */     register((ParticleType<ParticleOptions>)ParticleTypes.LARGE_SMOKE, Provider::new);
/* 118 */     register((ParticleType<ParticleOptions>)ParticleTypes.LAVA, Provider::new);
/* 119 */     register((ParticleType<ParticleOptions>)ParticleTypes.MYCELIUM, Provider::new);
/* 120 */     register((ParticleType<ParticleOptions>)ParticleTypes.NAUTILUS, NautilusProvider::new);
/* 121 */     register((ParticleType<ParticleOptions>)ParticleTypes.NOTE, Provider::new);
/* 122 */     register((ParticleType<ParticleOptions>)ParticleTypes.POOF, Provider::new);
/* 123 */     register((ParticleType<ParticleOptions>)ParticleTypes.PORTAL, Provider::new);
/* 124 */     register((ParticleType<ParticleOptions>)ParticleTypes.RAIN, Provider::new);
/* 125 */     register((ParticleType<ParticleOptions>)ParticleTypes.SMOKE, Provider::new);
/* 126 */     register((ParticleType<ParticleOptions>)ParticleTypes.WHITE_SMOKE, Provider::new);
/* 127 */     register((ParticleType<ParticleOptions>)ParticleTypes.SNEEZE, SneezeProvider::new);
/* 128 */     register((ParticleType<ParticleOptions>)ParticleTypes.SNOWFLAKE, Provider::new);
/* 129 */     register((ParticleType<ParticleOptions>)ParticleTypes.SPIT, Provider::new);
/* 130 */     register((ParticleType<ParticleOptions>)ParticleTypes.SWEEP_ATTACK, Provider::new);
/* 131 */     register((ParticleType<ParticleOptions>)ParticleTypes.TOTEM_OF_UNDYING, Provider::new);
/* 132 */     register((ParticleType<ParticleOptions>)ParticleTypes.SQUID_INK, Provider::new);
/* 133 */     register((ParticleType<ParticleOptions>)ParticleTypes.UNDERWATER, UnderwaterProvider::new);
/* 134 */     register((ParticleType<ParticleOptions>)ParticleTypes.SPLASH, Provider::new);
/* 135 */     register((ParticleType<ParticleOptions>)ParticleTypes.WITCH, WitchProvider::new);
/* 136 */     register((ParticleType<ParticleOptions>)ParticleTypes.DRIPPING_HONEY, HoneyHangProvider::new);
/* 137 */     register((ParticleType<ParticleOptions>)ParticleTypes.FALLING_HONEY, HoneyFallProvider::new);
/* 138 */     register((ParticleType<ParticleOptions>)ParticleTypes.LANDING_HONEY, HoneyLandProvider::new);
/* 139 */     register((ParticleType<ParticleOptions>)ParticleTypes.FALLING_NECTAR, NectarFallProvider::new);
/* 140 */     register((ParticleType<ParticleOptions>)ParticleTypes.FALLING_SPORE_BLOSSOM, SporeBlossomFallProvider::new);
/* 141 */     register((ParticleType<ParticleOptions>)ParticleTypes.SPORE_BLOSSOM_AIR, SporeBlossomAirProvider::new);
/* 142 */     register((ParticleType<ParticleOptions>)ParticleTypes.ASH, Provider::new);
/* 143 */     register((ParticleType<ParticleOptions>)ParticleTypes.CRIMSON_SPORE, CrimsonSporeProvider::new);
/* 144 */     register((ParticleType<ParticleOptions>)ParticleTypes.WARPED_SPORE, WarpedSporeProvider::new);
/* 145 */     register((ParticleType<ParticleOptions>)ParticleTypes.DRIPPING_OBSIDIAN_TEAR, ObsidianTearHangProvider::new);
/* 146 */     register((ParticleType<ParticleOptions>)ParticleTypes.FALLING_OBSIDIAN_TEAR, ObsidianTearFallProvider::new);
/* 147 */     register((ParticleType<ParticleOptions>)ParticleTypes.LANDING_OBSIDIAN_TEAR, ObsidianTearLandProvider::new);
/* 148 */     register((ParticleType<ParticleOptions>)ParticleTypes.REVERSE_PORTAL, ReversePortalProvider::new);
/* 149 */     register((ParticleType<ParticleOptions>)ParticleTypes.WHITE_ASH, Provider::new);
/* 150 */     register((ParticleType<ParticleOptions>)ParticleTypes.SMALL_FLAME, SmallFlameProvider::new);
/*     */     
/* 152 */     register((ParticleType<ParticleOptions>)ParticleTypes.DRIPPING_DRIPSTONE_WATER, DripstoneWaterHangProvider::new);
/* 153 */     register((ParticleType<ParticleOptions>)ParticleTypes.FALLING_DRIPSTONE_WATER, DripstoneWaterFallProvider::new);
/*     */     
/* 155 */     register((ParticleType<ParticleOptions>)ParticleTypes.CHERRY_LEAVES, CherryProvider::new);
/* 156 */     register((ParticleType<ParticleOptions>)ParticleTypes.PALE_OAK_LEAVES, PaleOakProvider::new);
/* 157 */     register(ParticleTypes.TINTED_LEAVES, TintedLeavesProvider::new);
/*     */     
/* 159 */     register((ParticleType<ParticleOptions>)ParticleTypes.DRIPPING_DRIPSTONE_LAVA, DripstoneLavaHangProvider::new);
/* 160 */     register((ParticleType<ParticleOptions>)ParticleTypes.FALLING_DRIPSTONE_LAVA, DripstoneLavaFallProvider::new);
/* 161 */     register(ParticleTypes.VIBRATION, Provider::new);
/* 162 */     register(ParticleTypes.TRAIL, Provider::new);
/* 163 */     register((ParticleType<ParticleOptions>)ParticleTypes.GLOW_SQUID_INK, GlowInkProvider::new);
/* 164 */     register((ParticleType<ParticleOptions>)ParticleTypes.GLOW, GlowSquidProvider::new);
/* 165 */     register((ParticleType<ParticleOptions>)ParticleTypes.WAX_ON, WaxOnProvider::new);
/* 166 */     register((ParticleType<ParticleOptions>)ParticleTypes.WAX_OFF, WaxOffProvider::new);
/* 167 */     register((ParticleType<ParticleOptions>)ParticleTypes.ELECTRIC_SPARK, ElectricSparkProvider::new);
/* 168 */     register((ParticleType<ParticleOptions>)ParticleTypes.SCRAPE, ScrapeProvider::new);
/* 169 */     register(ParticleTypes.SHRIEK, Provider::new);
/*     */     
/* 171 */     register((ParticleType<ParticleOptions>)ParticleTypes.EGG_CRACK, EggCrackProvider::new);
/* 172 */     register((ParticleType<ParticleOptions>)ParticleTypes.DUST_PLUME, Provider::new);
/* 173 */     register((ParticleType<ParticleOptions>)ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER, Provider::new);
/* 174 */     register((ParticleType<ParticleOptions>)ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS, Provider::new);
/* 175 */     register((ParticleType<ParticleOptions>)ParticleTypes.VAULT_CONNECTION, VaultConnectionProvider::new);
/* 176 */     register(ParticleTypes.DUST_PILLAR, (ParticleProvider<ParticleOptions>)new TerrainParticle.DustPillarProvider());
/* 177 */     register((ParticleType<ParticleOptions>)ParticleTypes.RAID_OMEN, Provider::new);
/* 178 */     register((ParticleType<ParticleOptions>)ParticleTypes.TRIAL_OMEN, Provider::new);
/* 179 */     register((ParticleType<ParticleOptions>)ParticleTypes.OMINOUS_SPAWNING, OminousSpawnProvider::new);
/* 180 */     register(ParticleTypes.BLOCK_CRUMBLE, (ParticleProvider<ParticleOptions>)new TerrainParticle.CrumblingProvider());
/* 181 */     register((ParticleType<ParticleOptions>)ParticleTypes.FIREFLY, FireflyProvider::new);
/*     */   }
/*     */   
/*     */   private <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> provider) {
/* 185 */     this.providers.put(BuiltInRegistries.PARTICLE_TYPE.getId(type), provider);
/*     */   }
/*     */   
/*     */   private <T extends ParticleOptions> void register(ParticleType<T> type, SpriteParticleRegistration<T> provider) {
/* 189 */     MutableSpriteSet spriteSet = new MutableSpriteSet();
/* 190 */     this.spriteSets.put(BuiltInRegistries.PARTICLE_TYPE.getKey(type), spriteSet);
/* 191 */     this.providers.put(BuiltInRegistries.PARTICLE_TYPE.getId(type), provider.create(spriteSet));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<Void> reload(PreparableReloadListener.SharedState currentReload, Executor taskExecutor, PreparableReloadListener.PreparationBarrier preparationBarrier, Executor reloadExecutor) {
/* 198 */     ResourceManager manager = currentReload.resourceManager();
/* 199 */     CompletableFuture<List<ParticleDefinition>> spriteSetsToLoad = CompletableFuture.supplyAsync(() -> PARTICLE_LISTER.listMatchingResources(manager), taskExecutor)
/* 200 */       .thenCompose(definitionsToScan -> {
/*     */           List<CompletableFuture<ParticleDefinition>> loadTasks = new ArrayList<>(taskExecutor.size());
/*     */ 
/*     */           
/*     */           taskExecutor.forEach(());
/*     */           
/*     */           return Util.sequence(loadTasks);
/*     */         });
/*     */     
/* 209 */     CompletableFuture<SpriteLoader.Preparations> pendingSprites = ((AtlasManager.PendingStitchResults)currentReload.get(AtlasManager.PENDING_STITCH)).get(AtlasIds.PARTICLES);
/*     */ 
/*     */     
/* 212 */     Objects.requireNonNull(preparationBarrier); return CompletableFuture.allOf((CompletableFuture<?>[])new CompletableFuture[] { spriteSetsToLoad, pendingSprites }).thenCompose(preparationBarrier::wait)
/* 213 */       .thenAcceptAsync(unused -> {
/*     */           if (this.onReload != null) {
/*     */             this.onReload.run();
/*     */           }
/*     */           ProfilerFiller reloadProfiler = Profiler.get();
/*     */           reloadProfiler.push("upload");
/*     */           SpriteLoader.Preparations sprites = pendingSprites.join();
/*     */           reloadProfiler.popPush("bindSpriteSets");
/*     */           Set<Identifier> missingSprites = new HashSet<>();
/*     */           TextureAtlasSprite missingSprite = sprites.missing();
/*     */           ((List)pendingSprites.join()).forEach(());
/*     */           if (!missingSprites.isEmpty()) {
/*     */             LOGGER.warn("Missing particle sprites: {}", missingSprites.stream().sorted().map(Identifier::toString).collect(Collectors.joining(",")));
/*     */           }
/*     */           reloadProfiler.pop();
/*     */         }, reloadExecutor);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Optional<List<Identifier>> loadParticleDescription(Identifier id, Resource resource) {
/* 252 */     if (!this.spriteSets.containsKey(id)) {
/* 253 */       LOGGER.debug("Redundant texture list for particle: {}", id);
/* 254 */       return Optional.empty();
/*     */     }  
/* 256 */     try { Reader reader = resource.openAsReader(); 
/* 257 */       try { ParticleDescription description = ParticleDescription.fromJson(GsonHelper.parse(reader));
/* 258 */         Optional<List<Identifier>> optional = Optional.of(description.getTextures());
/* 259 */         if (reader != null) reader.close();  return optional; } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 260 */     { throw new IllegalStateException("Failed to load description for particle " + String.valueOf(id), e); }
/*     */   
/*     */   }
/*     */   
/*     */   public Int2ObjectMap<ParticleProvider<?>> getProviders() {
/* 265 */     return this.providers;
/*     */   }
/*     */   @FunctionalInterface
/*     */   private static interface SpriteParticleRegistration<T extends ParticleOptions> {
/*     */     ParticleProvider<T> create(SpriteSet param1SpriteSet); }
/*     */   private static class MutableSpriteSet implements SpriteSet { private List<TextureAtlasSprite> sprites;
/*     */     
/*     */     public TextureAtlasSprite get(int index, int max) {
/* 273 */       return this.sprites.get(index * (this.sprites.size() - 1) / max);
/*     */     }
/*     */ 
/*     */     
/*     */     public TextureAtlasSprite get(RandomSource random) {
/* 278 */       return this.sprites.get(random.nextInt(this.sprites.size()));
/*     */     }
/*     */ 
/*     */     
/*     */     public TextureAtlasSprite first() {
/* 283 */       return this.sprites.getFirst();
/*     */     }
/*     */     
/*     */     public void rebind(List<TextureAtlasSprite> ids) {
/* 287 */       this.sprites = (List<TextureAtlasSprite>)ImmutableList.copyOf(ids);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/ParticleResources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */