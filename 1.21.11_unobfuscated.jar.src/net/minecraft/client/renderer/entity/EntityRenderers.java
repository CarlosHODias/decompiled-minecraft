/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.model.animal.squid.SquidModel;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.renderer.entity.player.AvatarRenderer;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.player.PlayerModelType;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityRenderers
/*     */ {
/*  20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  22 */   private static final Map<EntityType<?>, EntityRendererProvider<?>> PROVIDERS = (Map<EntityType<?>, EntityRendererProvider<?>>)new Object2ObjectOpenHashMap();
/*     */   
/*     */   static {
/*  25 */     register(EntityType.ACACIA_BOAT, context -> new BoatRenderer(context, ModelLayers.ACACIA_BOAT));
/*  26 */     register(EntityType.ACACIA_CHEST_BOAT, context -> new BoatRenderer(context, ModelLayers.ACACIA_CHEST_BOAT));
/*  27 */     register(EntityType.ALLAY, AllayRenderer::new);
/*  28 */     register(EntityType.AREA_EFFECT_CLOUD, NoopRenderer::new);
/*  29 */     register(EntityType.ARMADILLO, ArmadilloRenderer::new);
/*  30 */     register(EntityType.ARMOR_STAND, ArmorStandRenderer::new);
/*  31 */     register(EntityType.ARROW, TippableArrowRenderer::new);
/*  32 */     register(EntityType.AXOLOTL, AxolotlRenderer::new);
/*  33 */     register(EntityType.BAMBOO_CHEST_RAFT, context -> new RaftRenderer(context, ModelLayers.BAMBOO_CHEST_RAFT));
/*  34 */     register(EntityType.BAMBOO_RAFT, context -> new RaftRenderer(context, ModelLayers.BAMBOO_RAFT));
/*  35 */     register(EntityType.BAT, BatRenderer::new);
/*  36 */     register(EntityType.BEE, BeeRenderer::new);
/*  37 */     register(EntityType.BIRCH_BOAT, context -> new BoatRenderer(context, ModelLayers.BIRCH_BOAT));
/*  38 */     register(EntityType.BIRCH_CHEST_BOAT, context -> new BoatRenderer(context, ModelLayers.BIRCH_CHEST_BOAT));
/*  39 */     register(EntityType.BLAZE, BlazeRenderer::new);
/*  40 */     register(EntityType.BLOCK_DISPLAY, BlockDisplayRenderer::new);
/*  41 */     register(EntityType.BOGGED, BoggedRenderer::new);
/*  42 */     register(EntityType.BREEZE, BreezeRenderer::new);
/*  43 */     register(EntityType.BREEZE_WIND_CHARGE, WindChargeRenderer::new);
/*  44 */     register(EntityType.CAMEL, CamelRenderer::new);
/*  45 */     register(EntityType.CAMEL_HUSK, CamelHuskRenderer::new);
/*  46 */     register(EntityType.CAT, CatRenderer::new);
/*  47 */     register(EntityType.CAVE_SPIDER, CaveSpiderRenderer::new);
/*  48 */     register(EntityType.CHERRY_BOAT, context -> new BoatRenderer(context, ModelLayers.CHERRY_BOAT));
/*  49 */     register(EntityType.CHERRY_CHEST_BOAT, context -> new BoatRenderer(context, ModelLayers.CHERRY_CHEST_BOAT));
/*  50 */     register(EntityType.CHEST_MINECART, context -> new MinecartRenderer(context, ModelLayers.CHEST_MINECART));
/*  51 */     register(EntityType.CHICKEN, ChickenRenderer::new);
/*  52 */     register(EntityType.COD, CodRenderer::new);
/*  53 */     register(EntityType.COMMAND_BLOCK_MINECART, context -> new MinecartRenderer(context, ModelLayers.COMMAND_BLOCK_MINECART));
/*  54 */     register(EntityType.COPPER_GOLEM, CopperGolemRenderer::new);
/*  55 */     register(EntityType.COW, CowRenderer::new);
/*  56 */     register(EntityType.CREAKING, CreakingRenderer::new);
/*  57 */     register(EntityType.CREEPER, CreeperRenderer::new);
/*  58 */     register(EntityType.DARK_OAK_BOAT, context -> new BoatRenderer(context, ModelLayers.DARK_OAK_BOAT));
/*  59 */     register(EntityType.DARK_OAK_CHEST_BOAT, context -> new BoatRenderer(context, ModelLayers.DARK_OAK_CHEST_BOAT));
/*  60 */     register(EntityType.DOLPHIN, DolphinRenderer::new);
/*  61 */     register(EntityType.DONKEY, context -> new DonkeyRenderer(context, DonkeyRenderer.Type.DONKEY));
/*  62 */     register(EntityType.DRAGON_FIREBALL, DragonFireballRenderer::new);
/*  63 */     register(EntityType.DROWNED, DrownedRenderer::new);
/*  64 */     register(EntityType.EGG, ThrownItemRenderer::new);
/*  65 */     register(EntityType.ELDER_GUARDIAN, ElderGuardianRenderer::new);
/*  66 */     register(EntityType.ENDERMAN, EndermanRenderer::new);
/*  67 */     register(EntityType.ENDERMITE, EndermiteRenderer::new);
/*  68 */     register(EntityType.ENDER_DRAGON, EnderDragonRenderer::new);
/*  69 */     register(EntityType.ENDER_PEARL, ThrownItemRenderer::new);
/*  70 */     register(EntityType.END_CRYSTAL, EndCrystalRenderer::new);
/*  71 */     register(EntityType.EVOKER, EvokerRenderer::new);
/*  72 */     register(EntityType.EVOKER_FANGS, EvokerFangsRenderer::new);
/*  73 */     register(EntityType.EXPERIENCE_BOTTLE, ThrownItemRenderer::new);
/*  74 */     register(EntityType.EXPERIENCE_ORB, ExperienceOrbRenderer::new);
/*  75 */     register(EntityType.EYE_OF_ENDER, context -> new ThrownItemRenderer(context, 1.0F, true));
/*  76 */     register(EntityType.FALLING_BLOCK, FallingBlockRenderer::new);
/*  77 */     register(EntityType.FIREBALL, context -> new ThrownItemRenderer(context, 3.0F, true));
/*  78 */     register(EntityType.FIREWORK_ROCKET, FireworkEntityRenderer::new);
/*  79 */     register(EntityType.FISHING_BOBBER, FishingHookRenderer::new);
/*  80 */     register(EntityType.FOX, FoxRenderer::new);
/*  81 */     register(EntityType.FROG, FrogRenderer::new);
/*  82 */     register(EntityType.FURNACE_MINECART, context -> new MinecartRenderer(context, ModelLayers.FURNACE_MINECART));
/*  83 */     register(EntityType.GHAST, GhastRenderer::new);
/*  84 */     register(EntityType.HAPPY_GHAST, HappyGhastRenderer::new);
/*  85 */     register(EntityType.GIANT, context -> new GiantMobRenderer(context, 6.0F));
/*  86 */     register(EntityType.GLOW_ITEM_FRAME, ItemFrameRenderer::new);
/*  87 */     register(EntityType.GLOW_SQUID, context -> new GlowSquidRenderer(context, new SquidModel(context.bakeLayer(ModelLayers.GLOW_SQUID)), new SquidModel(context.bakeLayer(ModelLayers.GLOW_SQUID_BABY))));
/*  88 */     register(EntityType.GOAT, GoatRenderer::new);
/*  89 */     register(EntityType.GUARDIAN, GuardianRenderer::new);
/*  90 */     register(EntityType.HOGLIN, HoglinRenderer::new);
/*  91 */     register(EntityType.HOPPER_MINECART, context -> new MinecartRenderer(context, ModelLayers.HOPPER_MINECART));
/*  92 */     register(EntityType.HORSE, HorseRenderer::new);
/*  93 */     register(EntityType.HUSK, HuskRenderer::new);
/*  94 */     register(EntityType.ILLUSIONER, IllusionerRenderer::new);
/*  95 */     register(EntityType.INTERACTION, NoopRenderer::new);
/*  96 */     register(EntityType.IRON_GOLEM, IronGolemRenderer::new);
/*  97 */     register(EntityType.ITEM, ItemEntityRenderer::new);
/*  98 */     register(EntityType.ITEM_DISPLAY, ItemDisplayRenderer::new);
/*  99 */     register(EntityType.ITEM_FRAME, ItemFrameRenderer::new);
/* 100 */     register(EntityType.JUNGLE_BOAT, context -> new BoatRenderer(context, ModelLayers.JUNGLE_BOAT));
/* 101 */     register(EntityType.JUNGLE_CHEST_BOAT, context -> new BoatRenderer(context, ModelLayers.JUNGLE_CHEST_BOAT));
/* 102 */     register(EntityType.LEASH_KNOT, LeashKnotRenderer::new);
/* 103 */     register(EntityType.LIGHTNING_BOLT, LightningBoltRenderer::new);
/* 104 */     register(EntityType.LINGERING_POTION, ThrownItemRenderer::new);
/* 105 */     register(EntityType.LLAMA, context -> new LlamaRenderer(context, ModelLayers.LLAMA, ModelLayers.LLAMA_BABY));
/* 106 */     register(EntityType.LLAMA_SPIT, LlamaSpitRenderer::new);
/* 107 */     register(EntityType.MAGMA_CUBE, MagmaCubeRenderer::new);
/* 108 */     register(EntityType.MANGROVE_BOAT, context -> new BoatRenderer(context, ModelLayers.MANGROVE_BOAT));
/* 109 */     register(EntityType.MANGROVE_CHEST_BOAT, context -> new BoatRenderer(context, ModelLayers.MANGROVE_CHEST_BOAT));
/* 110 */     register(EntityType.MARKER, NoopRenderer::new);
/* 111 */     register(EntityType.MINECART, context -> new MinecartRenderer(context, ModelLayers.MINECART));
/* 112 */     register(EntityType.MOOSHROOM, MushroomCowRenderer::new);
/* 113 */     register(EntityType.MULE, context -> new DonkeyRenderer(context, DonkeyRenderer.Type.MULE));
/* 114 */     register(EntityType.NAUTILUS, NautilusRenderer::new);
/* 115 */     register(EntityType.OAK_BOAT, context -> new BoatRenderer(context, ModelLayers.OAK_BOAT));
/* 116 */     register(EntityType.OAK_CHEST_BOAT, context -> new BoatRenderer(context, ModelLayers.OAK_CHEST_BOAT));
/* 117 */     register(EntityType.OCELOT, OcelotRenderer::new);
/* 118 */     register(EntityType.OMINOUS_ITEM_SPAWNER, OminousItemSpawnerRenderer::new);
/* 119 */     register(EntityType.PAINTING, PaintingRenderer::new);
/* 120 */     register(EntityType.PALE_OAK_BOAT, context -> new BoatRenderer(context, ModelLayers.PALE_OAK_BOAT));
/* 121 */     register(EntityType.PALE_OAK_CHEST_BOAT, context -> new BoatRenderer(context, ModelLayers.PALE_OAK_CHEST_BOAT));
/* 122 */     register(EntityType.PANDA, PandaRenderer::new);
/* 123 */     register(EntityType.PARCHED, ParchedRenderer::new);
/* 124 */     register(EntityType.PARROT, ParrotRenderer::new);
/* 125 */     register(EntityType.PHANTOM, PhantomRenderer::new);
/* 126 */     register(EntityType.PIG, PigRenderer::new);
/* 127 */     register(EntityType.PIGLIN, context -> new PiglinRenderer(context, ModelLayers.PIGLIN, ModelLayers.PIGLIN_BABY, ModelLayers.PIGLIN_ARMOR, ModelLayers.PIGLIN_BABY_ARMOR));
/* 128 */     register(EntityType.PIGLIN_BRUTE, context -> new PiglinRenderer(context, ModelLayers.PIGLIN_BRUTE, ModelLayers.PIGLIN_BRUTE, ModelLayers.PIGLIN_BRUTE_ARMOR, ModelLayers.PIGLIN_BRUTE_ARMOR));
/* 129 */     register(EntityType.PILLAGER, PillagerRenderer::new);
/* 130 */     register(EntityType.POLAR_BEAR, PolarBearRenderer::new);
/* 131 */     register(EntityType.PUFFERFISH, PufferfishRenderer::new);
/* 132 */     register(EntityType.RABBIT, RabbitRenderer::new);
/* 133 */     register(EntityType.RAVAGER, RavagerRenderer::new);
/* 134 */     register(EntityType.SALMON, SalmonRenderer::new);
/* 135 */     register(EntityType.SHEEP, SheepRenderer::new);
/* 136 */     register(EntityType.SHULKER, ShulkerRenderer::new);
/* 137 */     register(EntityType.SHULKER_BULLET, ShulkerBulletRenderer::new);
/* 138 */     register(EntityType.SILVERFISH, SilverfishRenderer::new);
/* 139 */     register(EntityType.SKELETON, SkeletonRenderer::new);
/* 140 */     register(EntityType.SKELETON_HORSE, context -> new UndeadHorseRenderer(context, UndeadHorseRenderer.Type.SKELETON));
/* 141 */     register(EntityType.SLIME, SlimeRenderer::new);
/* 142 */     register(EntityType.SMALL_FIREBALL, context -> new ThrownItemRenderer(context, 0.75F, true));
/* 143 */     register(EntityType.SNIFFER, SnifferRenderer::new);
/* 144 */     register(EntityType.SNOWBALL, ThrownItemRenderer::new);
/* 145 */     register(EntityType.SNOW_GOLEM, SnowGolemRenderer::new);
/* 146 */     register(EntityType.SPAWNER_MINECART, context -> new MinecartRenderer(context, ModelLayers.SPAWNER_MINECART));
/* 147 */     register(EntityType.SPECTRAL_ARROW, SpectralArrowRenderer::new);
/* 148 */     register(EntityType.SPIDER, SpiderRenderer::new);
/* 149 */     register(EntityType.SPLASH_POTION, ThrownItemRenderer::new);
/* 150 */     register(EntityType.SPRUCE_BOAT, context -> new BoatRenderer(context, ModelLayers.SPRUCE_BOAT));
/* 151 */     register(EntityType.SPRUCE_CHEST_BOAT, context -> new BoatRenderer(context, ModelLayers.SPRUCE_CHEST_BOAT));
/* 152 */     register(EntityType.SQUID, context -> new SquidRenderer(context, new SquidModel(context.bakeLayer(ModelLayers.SQUID)), new SquidModel(context.bakeLayer(ModelLayers.SQUID_BABY))));
/* 153 */     register(EntityType.STRAY, StrayRenderer::new);
/* 154 */     register(EntityType.STRIDER, StriderRenderer::new);
/* 155 */     register(EntityType.TADPOLE, TadpoleRenderer::new);
/* 156 */     register(EntityType.TEXT_DISPLAY, TextDisplayRenderer::new);
/* 157 */     register(EntityType.TNT, TntRenderer::new);
/* 158 */     register(EntityType.TNT_MINECART, TntMinecartRenderer::new);
/* 159 */     register(EntityType.TRADER_LLAMA, context -> new LlamaRenderer(context, ModelLayers.TRADER_LLAMA, ModelLayers.TRADER_LLAMA_BABY));
/* 160 */     register(EntityType.TRIDENT, ThrownTridentRenderer::new);
/* 161 */     register(EntityType.TROPICAL_FISH, TropicalFishRenderer::new);
/* 162 */     register(EntityType.TURTLE, TurtleRenderer::new);
/* 163 */     register(EntityType.VEX, VexRenderer::new);
/* 164 */     register(EntityType.VILLAGER, VillagerRenderer::new);
/* 165 */     register(EntityType.VINDICATOR, VindicatorRenderer::new);
/* 166 */     register(EntityType.WANDERING_TRADER, WanderingTraderRenderer::new);
/* 167 */     register(EntityType.WARDEN, WardenRenderer::new);
/* 168 */     register(EntityType.WIND_CHARGE, WindChargeRenderer::new);
/* 169 */     register(EntityType.WITCH, WitchRenderer::new);
/* 170 */     register(EntityType.WITHER, WitherBossRenderer::new);
/* 171 */     register(EntityType.WITHER_SKELETON, WitherSkeletonRenderer::new);
/* 172 */     register(EntityType.WITHER_SKULL, WitherSkullRenderer::new);
/* 173 */     register(EntityType.WOLF, WolfRenderer::new);
/* 174 */     register(EntityType.ZOGLIN, ZoglinRenderer::new);
/* 175 */     register(EntityType.ZOMBIE, ZombieRenderer::new);
/* 176 */     register(EntityType.ZOMBIE_HORSE, context -> new UndeadHorseRenderer(context, UndeadHorseRenderer.Type.ZOMBIE));
/* 177 */     register(EntityType.ZOMBIE_NAUTILUS, ZombieNautilusRenderer::new);
/* 178 */     register(EntityType.ZOMBIE_VILLAGER, ZombieVillagerRenderer::new);
/* 179 */     register(EntityType.ZOMBIFIED_PIGLIN, context -> new ZombifiedPiglinRenderer(context, ModelLayers.ZOMBIFIED_PIGLIN, ModelLayers.ZOMBIFIED_PIGLIN_BABY, ModelLayers.ZOMBIFIED_PIGLIN_ARMOR, ModelLayers.ZOMBIFIED_PIGLIN_BABY_ARMOR));
/*     */   }
/*     */   
/*     */   private static <T extends net.minecraft.world.entity.Entity> void register(EntityType<? extends T> type, EntityRendererProvider<T> renderer) {
/* 183 */     PROVIDERS.put(type, renderer);
/*     */   }
/*     */   
/*     */   public static Map<EntityType<?>, EntityRenderer<?, ?>> createEntityRenderers(EntityRendererProvider.Context context) {
/* 187 */     ImmutableMap.Builder<EntityType<?>, EntityRenderer<?, ?>> result = ImmutableMap.builder();
/* 188 */     PROVIDERS.forEach((type, provider) -> {
/*     */           try {
/*     */             result.put(type, provider.create(context));
/* 191 */           } catch (Exception e) {
/*     */             throw new IllegalArgumentException("Failed to create model for " + String.valueOf(BuiltInRegistries.ENTITY_TYPE.getKey(type)), e);
/*     */           } 
/*     */         });
/* 195 */     return (Map<EntityType<?>, EntityRenderer<?, ?>>)result.build();
/*     */   }
/*     */   
/*     */   public static <T extends net.minecraft.world.entity.Avatar & net.minecraft.client.entity.ClientAvatarEntity> Map<PlayerModelType, AvatarRenderer<T>> createAvatarRenderers(EntityRendererProvider.Context context) {
/*     */     try {
/* 200 */       return Map.of(PlayerModelType.WIDE, new AvatarRenderer(context, false), PlayerModelType.SLIM, new AvatarRenderer(context, true));
/*     */ 
/*     */     
/*     */     }
/* 204 */     catch (Exception e) {
/* 205 */       throw new IllegalArgumentException("Failed to create avatar models", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean validateRegistrations() {
/*     */     boolean hasAllModels = true;
/* 211 */     for (EntityType<?> type : (Iterable<EntityType<?>>)BuiltInRegistries.ENTITY_TYPE) {
/* 212 */       if (type == EntityType.PLAYER || type == EntityType.MANNEQUIN) {
/*     */         continue;
/*     */       }
/* 215 */       if (!PROVIDERS.containsKey(type)) {
/* 216 */         LOGGER.warn("No renderer registered for {}", BuiltInRegistries.ENTITY_TYPE.getKey(type));
/* 217 */         hasAllModels = false;
/*     */       } 
/*     */     } 
/* 220 */     return !hasAllModels;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/EntityRenderers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */