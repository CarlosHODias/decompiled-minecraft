/*     */ package net.minecraft.client.renderer.special;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.renderer.blockentity.ChestRenderer;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*     */ import net.minecraft.world.level.block.SkullBlock;
/*     */ import net.minecraft.world.level.block.WeatheringCopper;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ 
/*     */ public class SpecialModelRenderers {
/*     */   public static final Codec<SpecialModelRenderer.Unbaked> CODEC;
/*  21 */   private static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends SpecialModelRenderer.Unbaked>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
/*     */   static {
/*  23 */     CODEC = ID_MAPPER.codec(Identifier.CODEC).dispatch(SpecialModelRenderer.Unbaked::type, c -> c);
/*     */   }
/*     */   public static void bootstrap() {
/*  26 */     ID_MAPPER.put(Identifier.withDefaultNamespace("bed"), BedSpecialRenderer.Unbaked.MAP_CODEC);
/*  27 */     ID_MAPPER.put(Identifier.withDefaultNamespace("banner"), BannerSpecialRenderer.Unbaked.MAP_CODEC);
/*  28 */     ID_MAPPER.put(Identifier.withDefaultNamespace("conduit"), ConduitSpecialRenderer.Unbaked.MAP_CODEC);
/*  29 */     ID_MAPPER.put(Identifier.withDefaultNamespace("chest"), ChestSpecialRenderer.Unbaked.MAP_CODEC);
/*  30 */     ID_MAPPER.put(Identifier.withDefaultNamespace("copper_golem_statue"), CopperGolemStatueSpecialRenderer.Unbaked.MAP_CODEC);
/*  31 */     ID_MAPPER.put(Identifier.withDefaultNamespace("head"), SkullSpecialRenderer.Unbaked.MAP_CODEC);
/*  32 */     ID_MAPPER.put(Identifier.withDefaultNamespace("player_head"), PlayerHeadSpecialRenderer.Unbaked.MAP_CODEC);
/*  33 */     ID_MAPPER.put(Identifier.withDefaultNamespace("shulker_box"), ShulkerBoxSpecialRenderer.Unbaked.MAP_CODEC);
/*  34 */     ID_MAPPER.put(Identifier.withDefaultNamespace("shield"), ShieldSpecialRenderer.Unbaked.MAP_CODEC);
/*  35 */     ID_MAPPER.put(Identifier.withDefaultNamespace("trident"), TridentSpecialRenderer.Unbaked.MAP_CODEC);
/*  36 */     ID_MAPPER.put(Identifier.withDefaultNamespace("decorated_pot"), DecoratedPotSpecialRenderer.Unbaked.MAP_CODEC);
/*  37 */     ID_MAPPER.put(Identifier.withDefaultNamespace("standing_sign"), StandingSignSpecialRenderer.Unbaked.MAP_CODEC);
/*  38 */     ID_MAPPER.put(Identifier.withDefaultNamespace("hanging_sign"), HangingSignSpecialRenderer.Unbaked.MAP_CODEC);
/*     */   }
/*     */   
/*  41 */   private static final Map<Block, SpecialModelRenderer.Unbaked> STATIC_BLOCK_MAPPING = (Map<Block, SpecialModelRenderer.Unbaked>)ImmutableMap.builder()
/*  42 */     .put(Blocks.SKELETON_SKULL, new SkullSpecialRenderer.Unbaked((SkullBlock.Type)SkullBlock.Types.SKELETON))
/*  43 */     .put(Blocks.ZOMBIE_HEAD, new SkullSpecialRenderer.Unbaked((SkullBlock.Type)SkullBlock.Types.ZOMBIE))
/*  44 */     .put(Blocks.CREEPER_HEAD, new SkullSpecialRenderer.Unbaked((SkullBlock.Type)SkullBlock.Types.CREEPER))
/*  45 */     .put(Blocks.DRAGON_HEAD, new SkullSpecialRenderer.Unbaked((SkullBlock.Type)SkullBlock.Types.DRAGON))
/*  46 */     .put(Blocks.PIGLIN_HEAD, new SkullSpecialRenderer.Unbaked((SkullBlock.Type)SkullBlock.Types.PIGLIN))
/*  47 */     .put(Blocks.PLAYER_HEAD, new PlayerHeadSpecialRenderer.Unbaked())
/*  48 */     .put(Blocks.WITHER_SKELETON_SKULL, new SkullSpecialRenderer.Unbaked((SkullBlock.Type)SkullBlock.Types.WITHER_SKELETON))
/*     */     
/*  50 */     .put(Blocks.SKELETON_WALL_SKULL, new SkullSpecialRenderer.Unbaked((SkullBlock.Type)SkullBlock.Types.SKELETON))
/*  51 */     .put(Blocks.ZOMBIE_WALL_HEAD, new SkullSpecialRenderer.Unbaked((SkullBlock.Type)SkullBlock.Types.ZOMBIE))
/*  52 */     .put(Blocks.CREEPER_WALL_HEAD, new SkullSpecialRenderer.Unbaked((SkullBlock.Type)SkullBlock.Types.CREEPER))
/*  53 */     .put(Blocks.DRAGON_WALL_HEAD, new SkullSpecialRenderer.Unbaked((SkullBlock.Type)SkullBlock.Types.DRAGON))
/*  54 */     .put(Blocks.PIGLIN_WALL_HEAD, new SkullSpecialRenderer.Unbaked((SkullBlock.Type)SkullBlock.Types.PIGLIN))
/*  55 */     .put(Blocks.PLAYER_WALL_HEAD, new PlayerHeadSpecialRenderer.Unbaked())
/*  56 */     .put(Blocks.WITHER_SKELETON_WALL_SKULL, new SkullSpecialRenderer.Unbaked((SkullBlock.Type)SkullBlock.Types.WITHER_SKELETON))
/*     */     
/*  58 */     .put(Blocks.WHITE_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.WHITE))
/*  59 */     .put(Blocks.ORANGE_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.ORANGE))
/*  60 */     .put(Blocks.MAGENTA_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.MAGENTA))
/*  61 */     .put(Blocks.LIGHT_BLUE_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.LIGHT_BLUE))
/*  62 */     .put(Blocks.YELLOW_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.YELLOW))
/*  63 */     .put(Blocks.LIME_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.LIME))
/*  64 */     .put(Blocks.PINK_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.PINK))
/*  65 */     .put(Blocks.GRAY_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.GRAY))
/*  66 */     .put(Blocks.LIGHT_GRAY_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.LIGHT_GRAY))
/*  67 */     .put(Blocks.CYAN_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.CYAN))
/*  68 */     .put(Blocks.PURPLE_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.PURPLE))
/*  69 */     .put(Blocks.BLUE_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.BLUE))
/*  70 */     .put(Blocks.BROWN_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.BROWN))
/*  71 */     .put(Blocks.GREEN_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.GREEN))
/*  72 */     .put(Blocks.RED_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.RED))
/*  73 */     .put(Blocks.BLACK_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.BLACK))
/*     */ 
/*     */     
/*  76 */     .put(Blocks.WHITE_WALL_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.WHITE))
/*  77 */     .put(Blocks.ORANGE_WALL_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.ORANGE))
/*  78 */     .put(Blocks.MAGENTA_WALL_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.MAGENTA))
/*  79 */     .put(Blocks.LIGHT_BLUE_WALL_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.LIGHT_BLUE))
/*  80 */     .put(Blocks.YELLOW_WALL_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.YELLOW))
/*  81 */     .put(Blocks.LIME_WALL_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.LIME))
/*  82 */     .put(Blocks.PINK_WALL_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.PINK))
/*  83 */     .put(Blocks.GRAY_WALL_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.GRAY))
/*  84 */     .put(Blocks.LIGHT_GRAY_WALL_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.LIGHT_GRAY))
/*  85 */     .put(Blocks.CYAN_WALL_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.CYAN))
/*  86 */     .put(Blocks.PURPLE_WALL_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.PURPLE))
/*  87 */     .put(Blocks.BLUE_WALL_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.BLUE))
/*  88 */     .put(Blocks.BROWN_WALL_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.BROWN))
/*  89 */     .put(Blocks.GREEN_WALL_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.GREEN))
/*  90 */     .put(Blocks.RED_WALL_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.RED))
/*  91 */     .put(Blocks.BLACK_WALL_BANNER, new BannerSpecialRenderer.Unbaked(DyeColor.BLACK))
/*     */     
/*  93 */     .put(Blocks.WHITE_BED, new BedSpecialRenderer.Unbaked(DyeColor.WHITE))
/*  94 */     .put(Blocks.ORANGE_BED, new BedSpecialRenderer.Unbaked(DyeColor.ORANGE))
/*  95 */     .put(Blocks.MAGENTA_BED, new BedSpecialRenderer.Unbaked(DyeColor.MAGENTA))
/*  96 */     .put(Blocks.LIGHT_BLUE_BED, new BedSpecialRenderer.Unbaked(DyeColor.LIGHT_BLUE))
/*  97 */     .put(Blocks.YELLOW_BED, new BedSpecialRenderer.Unbaked(DyeColor.YELLOW))
/*  98 */     .put(Blocks.LIME_BED, new BedSpecialRenderer.Unbaked(DyeColor.LIME))
/*  99 */     .put(Blocks.PINK_BED, new BedSpecialRenderer.Unbaked(DyeColor.PINK))
/* 100 */     .put(Blocks.GRAY_BED, new BedSpecialRenderer.Unbaked(DyeColor.GRAY))
/* 101 */     .put(Blocks.LIGHT_GRAY_BED, new BedSpecialRenderer.Unbaked(DyeColor.LIGHT_GRAY))
/* 102 */     .put(Blocks.CYAN_BED, new BedSpecialRenderer.Unbaked(DyeColor.CYAN))
/* 103 */     .put(Blocks.PURPLE_BED, new BedSpecialRenderer.Unbaked(DyeColor.PURPLE))
/* 104 */     .put(Blocks.BLUE_BED, new BedSpecialRenderer.Unbaked(DyeColor.BLUE))
/* 105 */     .put(Blocks.BROWN_BED, new BedSpecialRenderer.Unbaked(DyeColor.BROWN))
/* 106 */     .put(Blocks.GREEN_BED, new BedSpecialRenderer.Unbaked(DyeColor.GREEN))
/* 107 */     .put(Blocks.RED_BED, new BedSpecialRenderer.Unbaked(DyeColor.RED))
/* 108 */     .put(Blocks.BLACK_BED, new BedSpecialRenderer.Unbaked(DyeColor.BLACK))
/*     */     
/* 110 */     .put(Blocks.SHULKER_BOX, new ShulkerBoxSpecialRenderer.Unbaked())
/* 111 */     .put(Blocks.WHITE_SHULKER_BOX, new ShulkerBoxSpecialRenderer.Unbaked(DyeColor.WHITE))
/* 112 */     .put(Blocks.ORANGE_SHULKER_BOX, new ShulkerBoxSpecialRenderer.Unbaked(DyeColor.ORANGE))
/* 113 */     .put(Blocks.MAGENTA_SHULKER_BOX, new ShulkerBoxSpecialRenderer.Unbaked(DyeColor.MAGENTA))
/* 114 */     .put(Blocks.LIGHT_BLUE_SHULKER_BOX, new ShulkerBoxSpecialRenderer.Unbaked(DyeColor.LIGHT_BLUE))
/* 115 */     .put(Blocks.YELLOW_SHULKER_BOX, new ShulkerBoxSpecialRenderer.Unbaked(DyeColor.YELLOW))
/* 116 */     .put(Blocks.LIME_SHULKER_BOX, new ShulkerBoxSpecialRenderer.Unbaked(DyeColor.LIME))
/* 117 */     .put(Blocks.PINK_SHULKER_BOX, new ShulkerBoxSpecialRenderer.Unbaked(DyeColor.PINK))
/* 118 */     .put(Blocks.GRAY_SHULKER_BOX, new ShulkerBoxSpecialRenderer.Unbaked(DyeColor.GRAY))
/* 119 */     .put(Blocks.LIGHT_GRAY_SHULKER_BOX, new ShulkerBoxSpecialRenderer.Unbaked(DyeColor.LIGHT_GRAY))
/* 120 */     .put(Blocks.CYAN_SHULKER_BOX, new ShulkerBoxSpecialRenderer.Unbaked(DyeColor.CYAN))
/* 121 */     .put(Blocks.PURPLE_SHULKER_BOX, new ShulkerBoxSpecialRenderer.Unbaked(DyeColor.PURPLE))
/* 122 */     .put(Blocks.BLUE_SHULKER_BOX, new ShulkerBoxSpecialRenderer.Unbaked(DyeColor.BLUE))
/* 123 */     .put(Blocks.BROWN_SHULKER_BOX, new ShulkerBoxSpecialRenderer.Unbaked(DyeColor.BROWN))
/* 124 */     .put(Blocks.GREEN_SHULKER_BOX, new ShulkerBoxSpecialRenderer.Unbaked(DyeColor.GREEN))
/* 125 */     .put(Blocks.RED_SHULKER_BOX, new ShulkerBoxSpecialRenderer.Unbaked(DyeColor.RED))
/* 126 */     .put(Blocks.BLACK_SHULKER_BOX, new ShulkerBoxSpecialRenderer.Unbaked(DyeColor.BLACK))
/*     */     
/* 128 */     .put(Blocks.OAK_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.OAK))
/* 129 */     .put(Blocks.SPRUCE_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.SPRUCE))
/* 130 */     .put(Blocks.BIRCH_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.BIRCH))
/* 131 */     .put(Blocks.ACACIA_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.ACACIA))
/* 132 */     .put(Blocks.CHERRY_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.CHERRY))
/* 133 */     .put(Blocks.JUNGLE_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.JUNGLE))
/* 134 */     .put(Blocks.DARK_OAK_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.DARK_OAK))
/* 135 */     .put(Blocks.PALE_OAK_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.PALE_OAK))
/* 136 */     .put(Blocks.MANGROVE_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.MANGROVE))
/* 137 */     .put(Blocks.BAMBOO_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.BAMBOO))
/* 138 */     .put(Blocks.CRIMSON_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.CRIMSON))
/* 139 */     .put(Blocks.WARPED_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.WARPED))
/*     */     
/* 141 */     .put(Blocks.OAK_WALL_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.OAK))
/* 142 */     .put(Blocks.SPRUCE_WALL_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.SPRUCE))
/* 143 */     .put(Blocks.BIRCH_WALL_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.BIRCH))
/* 144 */     .put(Blocks.ACACIA_WALL_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.ACACIA))
/* 145 */     .put(Blocks.CHERRY_WALL_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.CHERRY))
/* 146 */     .put(Blocks.JUNGLE_WALL_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.JUNGLE))
/* 147 */     .put(Blocks.DARK_OAK_WALL_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.DARK_OAK))
/* 148 */     .put(Blocks.PALE_OAK_WALL_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.PALE_OAK))
/* 149 */     .put(Blocks.MANGROVE_WALL_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.MANGROVE))
/* 150 */     .put(Blocks.BAMBOO_WALL_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.BAMBOO))
/* 151 */     .put(Blocks.CRIMSON_WALL_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.CRIMSON))
/* 152 */     .put(Blocks.WARPED_WALL_SIGN, new StandingSignSpecialRenderer.Unbaked(WoodType.WARPED))
/*     */     
/* 154 */     .put(Blocks.OAK_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.OAK))
/* 155 */     .put(Blocks.SPRUCE_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.SPRUCE))
/* 156 */     .put(Blocks.BIRCH_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.BIRCH))
/* 157 */     .put(Blocks.ACACIA_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.ACACIA))
/* 158 */     .put(Blocks.CHERRY_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.CHERRY))
/* 159 */     .put(Blocks.JUNGLE_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.JUNGLE))
/* 160 */     .put(Blocks.DARK_OAK_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.DARK_OAK))
/* 161 */     .put(Blocks.PALE_OAK_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.PALE_OAK))
/* 162 */     .put(Blocks.MANGROVE_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.MANGROVE))
/* 163 */     .put(Blocks.BAMBOO_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.BAMBOO))
/* 164 */     .put(Blocks.CRIMSON_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.CRIMSON))
/* 165 */     .put(Blocks.WARPED_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.WARPED))
/*     */     
/* 167 */     .put(Blocks.OAK_WALL_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.OAK))
/* 168 */     .put(Blocks.SPRUCE_WALL_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.SPRUCE))
/* 169 */     .put(Blocks.BIRCH_WALL_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.BIRCH))
/* 170 */     .put(Blocks.ACACIA_WALL_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.ACACIA))
/* 171 */     .put(Blocks.CHERRY_WALL_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.CHERRY))
/* 172 */     .put(Blocks.JUNGLE_WALL_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.JUNGLE))
/* 173 */     .put(Blocks.DARK_OAK_WALL_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.DARK_OAK))
/* 174 */     .put(Blocks.PALE_OAK_WALL_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.PALE_OAK))
/* 175 */     .put(Blocks.MANGROVE_WALL_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.MANGROVE))
/* 176 */     .put(Blocks.BAMBOO_WALL_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.BAMBOO))
/* 177 */     .put(Blocks.CRIMSON_WALL_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.CRIMSON))
/* 178 */     .put(Blocks.WARPED_WALL_HANGING_SIGN, new HangingSignSpecialRenderer.Unbaked(WoodType.WARPED))
/*     */     
/* 180 */     .put(Blocks.CONDUIT, new ConduitSpecialRenderer.Unbaked())
/*     */     
/* 182 */     .put(Blocks.CHEST, new ChestSpecialRenderer.Unbaked(ChestSpecialRenderer.NORMAL_CHEST_TEXTURE))
/* 183 */     .put(Blocks.TRAPPED_CHEST, new ChestSpecialRenderer.Unbaked(ChestSpecialRenderer.TRAPPED_CHEST_TEXTURE))
/* 184 */     .put(Blocks.ENDER_CHEST, new ChestSpecialRenderer.Unbaked(ChestSpecialRenderer.ENDER_CHEST_TEXTURE))
/* 185 */     .put(Blocks.COPPER_CHEST, new ChestSpecialRenderer.Unbaked(ChestSpecialRenderer.COPPER_CHEST_TEXTURE))
/* 186 */     .put(Blocks.EXPOSED_COPPER_CHEST, new ChestSpecialRenderer.Unbaked(ChestSpecialRenderer.EXPOSED_COPPER_CHEST_TEXTURE))
/* 187 */     .put(Blocks.WEATHERED_COPPER_CHEST, new ChestSpecialRenderer.Unbaked(ChestSpecialRenderer.WEATHERED_COPPER_CHEST_TEXTURE))
/* 188 */     .put(Blocks.OXIDIZED_COPPER_CHEST, new ChestSpecialRenderer.Unbaked(ChestSpecialRenderer.OXIDIZED_COPPER_CHEST_TEXTURE))
/* 189 */     .put(Blocks.WAXED_COPPER_CHEST, new ChestSpecialRenderer.Unbaked(ChestSpecialRenderer.COPPER_CHEST_TEXTURE))
/* 190 */     .put(Blocks.WAXED_EXPOSED_COPPER_CHEST, new ChestSpecialRenderer.Unbaked(ChestSpecialRenderer.EXPOSED_COPPER_CHEST_TEXTURE))
/* 191 */     .put(Blocks.WAXED_WEATHERED_COPPER_CHEST, new ChestSpecialRenderer.Unbaked(ChestSpecialRenderer.WEATHERED_COPPER_CHEST_TEXTURE))
/* 192 */     .put(Blocks.WAXED_OXIDIZED_COPPER_CHEST, new ChestSpecialRenderer.Unbaked(ChestSpecialRenderer.OXIDIZED_COPPER_CHEST_TEXTURE))
/*     */     
/* 194 */     .put(Blocks.COPPER_GOLEM_STATUE, new CopperGolemStatueSpecialRenderer.Unbaked(WeatheringCopper.WeatherState.UNAFFECTED, CopperGolemStatueBlock.Pose.STANDING))
/* 195 */     .put(Blocks.EXPOSED_COPPER_GOLEM_STATUE, new CopperGolemStatueSpecialRenderer.Unbaked(WeatheringCopper.WeatherState.EXPOSED, CopperGolemStatueBlock.Pose.STANDING))
/* 196 */     .put(Blocks.WEATHERED_COPPER_GOLEM_STATUE, new CopperGolemStatueSpecialRenderer.Unbaked(WeatheringCopper.WeatherState.WEATHERED, CopperGolemStatueBlock.Pose.STANDING))
/* 197 */     .put(Blocks.OXIDIZED_COPPER_GOLEM_STATUE, new CopperGolemStatueSpecialRenderer.Unbaked(WeatheringCopper.WeatherState.OXIDIZED, CopperGolemStatueBlock.Pose.STANDING))
/* 198 */     .put(Blocks.WAXED_COPPER_GOLEM_STATUE, new CopperGolemStatueSpecialRenderer.Unbaked(WeatheringCopper.WeatherState.UNAFFECTED, CopperGolemStatueBlock.Pose.STANDING))
/* 199 */     .put(Blocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE, new CopperGolemStatueSpecialRenderer.Unbaked(WeatheringCopper.WeatherState.EXPOSED, CopperGolemStatueBlock.Pose.STANDING))
/* 200 */     .put(Blocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE, new CopperGolemStatueSpecialRenderer.Unbaked(WeatheringCopper.WeatherState.WEATHERED, CopperGolemStatueBlock.Pose.STANDING))
/* 201 */     .put(Blocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE, new CopperGolemStatueSpecialRenderer.Unbaked(WeatheringCopper.WeatherState.OXIDIZED, CopperGolemStatueBlock.Pose.STANDING))
/*     */     
/* 203 */     .put(Blocks.DECORATED_POT, new DecoratedPotSpecialRenderer.Unbaked())
/* 204 */     .build();
/*     */   
/* 206 */   private static final ChestSpecialRenderer.Unbaked GIFT_CHEST = new ChestSpecialRenderer.Unbaked(ChestSpecialRenderer.GIFT_CHEST_TEXTURE);
/*     */   
/*     */   public static Map<Block, SpecialModelRenderer<?>> createBlockRenderers(SpecialModelRenderer.BakingContext context) {
/* 209 */     Map<Block, SpecialModelRenderer.Unbaked> unbaked = new HashMap<>(STATIC_BLOCK_MAPPING);
/*     */     
/* 211 */     if (ChestRenderer.xmasTextures()) {
/* 212 */       unbaked.put(Blocks.CHEST, GIFT_CHEST);
/* 213 */       unbaked.put(Blocks.TRAPPED_CHEST, GIFT_CHEST);
/*     */     } 
/*     */     
/* 216 */     ImmutableMap.Builder<Block, SpecialModelRenderer<?>> result = ImmutableMap.builder();
/* 217 */     unbaked.forEach((block, model) -> {
/*     */           SpecialModelRenderer<?> baked = model.bake(context);
/*     */           if (baked != null) {
/*     */             result.put(block, baked);
/*     */           }
/*     */         });
/* 223 */     return (Map<Block, SpecialModelRenderer<?>>)result.build();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/special/SpecialModelRenderers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */