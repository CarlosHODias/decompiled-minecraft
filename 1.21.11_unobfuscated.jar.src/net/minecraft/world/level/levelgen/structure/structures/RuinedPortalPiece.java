/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LeavesBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.VineBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlackstoneReplaceProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.LavaSubmergedBlockProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.ProtectedBlockProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ public class RuinedPortalPiece extends TemplateStructurePiece {
/*     */   private static final float PROBABILITY_OF_GOLD_GONE = 0.3F;
/*     */   private static final float PROBABILITY_OF_MAGMA_INSTEAD_OF_NETHERRACK = 0.07F;
/*     */   private static final float PROBABILITY_OF_MAGMA_INSTEAD_OF_LAVA = 0.2F;
/*     */   private final VerticalPlacement verticalPlacement;
/*     */   private final Properties properties;
/*     */   
/*     */   public static class Properties {
/*     */     static {
/*  60 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.fieldOf("cold").forGetter(()), (App)Codec.FLOAT.fieldOf("mossiness").forGetter(()), (App)Codec.BOOL.fieldOf("air_pocket").forGetter(()), (App)Codec.BOOL.fieldOf("overgrown").forGetter(()), (App)Codec.BOOL.fieldOf("vines").forGetter(()), (App)Codec.BOOL.fieldOf("replace_with_blackstone").forGetter(())).apply((Applicative)i, Properties::new));
/*     */     }
/*     */ 
/*     */     
/*     */     public static final Codec<Properties> CODEC;
/*     */     
/*     */     public boolean cold;
/*     */     
/*     */     public float mossiness;
/*     */     
/*     */     public boolean airPocket;
/*     */     
/*     */     public boolean overgrown;
/*     */     
/*     */     public boolean vines;
/*     */     public boolean replaceWithBlackstone;
/*     */     
/*     */     public Properties() {}
/*     */     
/*     */     public Properties(boolean cold, float mossiness, boolean airPocket, boolean overgrown, boolean vines, boolean replaceWithBlackstone) {
/*  80 */       this.cold = cold;
/*  81 */       this.mossiness = mossiness;
/*  82 */       this.airPocket = airPocket;
/*  83 */       this.overgrown = overgrown;
/*  84 */       this.vines = vines;
/*  85 */       this.replaceWithBlackstone = replaceWithBlackstone;
/*     */     }
/*     */   }
/*     */   
/*     */   public RuinedPortalPiece(StructureTemplateManager structureTemplateManager, BlockPos templatePosition, VerticalPlacement verticalPlacement, Properties properties, Identifier templateLocation, StructureTemplate template, Rotation rotation, Mirror mirror, BlockPos pivot) {
/*  90 */     super(StructurePieceType.RUINED_PORTAL, 0, structureTemplateManager, templateLocation, templateLocation.toString(), makeSettings(mirror, rotation, verticalPlacement, pivot, properties), templatePosition);
/*     */     
/*  92 */     this.verticalPlacement = verticalPlacement;
/*  93 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   public RuinedPortalPiece(StructureTemplateManager structureTemplateManager, CompoundTag tag) {
/*  97 */     super(StructurePieceType.RUINED_PORTAL, tag, structureTemplateManager, location -> makeSettings(structureTemplateManager, tag, location));
/*     */     
/*  99 */     this.verticalPlacement = tag.read("VerticalPlacement", VerticalPlacement.CODEC).orElseThrow();
/* 100 */     this.properties = tag.read("Properties", Properties.CODEC).orElseThrow();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 105 */     super.addAdditionalSaveData(context, tag);
/* 106 */     tag.store("Rotation", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/* 107 */     tag.store("Mirror", Mirror.LEGACY_CODEC, this.placeSettings.getMirror());
/* 108 */     tag.store("VerticalPlacement", VerticalPlacement.CODEC, this.verticalPlacement);
/* 109 */     tag.store("Properties", Properties.CODEC, this.properties);
/*     */   }
/*     */   
/*     */   private static StructurePlaceSettings makeSettings(StructureTemplateManager structureTemplateManager, CompoundTag tag, Identifier location) {
/* 113 */     StructureTemplate template = structureTemplateManager.getOrCreate(location);
/*     */     
/* 115 */     BlockPos pivot = new BlockPos(template.getSize().getX() / 2, 0, template.getSize().getZ() / 2);
/* 116 */     return makeSettings(
/* 117 */         tag.read("Mirror", Mirror.LEGACY_CODEC).orElseThrow(), 
/* 118 */         tag.read("Rotation", Rotation.LEGACY_CODEC).orElseThrow(), 
/* 119 */         tag.read("VerticalPlacement", VerticalPlacement.CODEC).orElseThrow(), pivot, (Properties)
/*     */         
/* 121 */         Properties.CODEC.parse(new com.mojang.serialization.Dynamic((com.mojang.serialization.DynamicOps)net.minecraft.nbt.NbtOps.INSTANCE, tag.get("Properties"))).getPartialOrThrow());
/*     */   }
/*     */ 
/*     */   
/*     */   private static StructurePlaceSettings makeSettings(Mirror mirror, Rotation rotation, VerticalPlacement verticalPlacement, BlockPos pivot, Properties properties) {
/* 126 */     BlockIgnoreProcessor ignoreProcessor = properties.airPocket ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR;
/*     */     
/* 128 */     List<ProcessorRule> rules = com.google.common.collect.Lists.newArrayList();
/* 129 */     rules.add(getBlockReplaceRule(Blocks.GOLD_BLOCK, 0.3F, Blocks.AIR));
/* 130 */     rules.add(getLavaProcessorRule(verticalPlacement, properties));
/* 131 */     if (!properties.cold) {
/* 132 */       rules.add(getBlockReplaceRule(Blocks.NETHERRACK, 0.07F, Blocks.MAGMA_BLOCK));
/*     */     }
/*     */     
/* 135 */     StructurePlaceSettings settings = new StructurePlaceSettings()
/* 136 */       .setRotation(rotation)
/* 137 */       .setMirror(mirror)
/* 138 */       .setRotationPivot(pivot)
/* 139 */       .addProcessor((StructureProcessor)ignoreProcessor)
/* 140 */       .addProcessor((StructureProcessor)new RuleProcessor(rules))
/* 141 */       .addProcessor((StructureProcessor)new net.minecraft.world.level.levelgen.structure.templatesystem.BlockAgeProcessor(properties.mossiness))
/* 142 */       .addProcessor((StructureProcessor)new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE))
/* 143 */       .addProcessor((StructureProcessor)new LavaSubmergedBlockProcessor());
/*     */     
/* 145 */     if (properties.replaceWithBlackstone) {
/* 146 */       settings.addProcessor((StructureProcessor)BlackstoneReplaceProcessor.INSTANCE);
/*     */     }
/* 148 */     return settings;
/*     */   }
/*     */   
/*     */   private static ProcessorRule getLavaProcessorRule(VerticalPlacement verticalPlacement, Properties properties) {
/* 152 */     if (verticalPlacement == VerticalPlacement.ON_OCEAN_FLOOR)
/* 153 */       return getBlockReplaceRule(Blocks.LAVA, Blocks.MAGMA_BLOCK); 
/* 154 */     if (properties.cold) {
/* 155 */       return getBlockReplaceRule(Blocks.LAVA, Blocks.NETHERRACK);
/*     */     }
/* 157 */     return getBlockReplaceRule(Blocks.LAVA, 0.2F, Blocks.MAGMA_BLOCK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 164 */     BoundingBox boundingBox = this.template.getBoundingBox(this.placeSettings, this.templatePosition);
/* 165 */     if (!chunkBB.isInside((Vec3i)boundingBox.getCenter())) {
/*     */       return;
/*     */     }
/*     */     
/* 169 */     chunkBB.encapsulate(boundingBox);
/*     */     
/* 171 */     super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
/*     */     
/* 173 */     spreadNetherrack(random, (LevelAccessor)level);
/* 174 */     addNetherrackDripColumnsBelowPortal(random, (LevelAccessor)level);
/*     */     
/* 176 */     if (this.properties.vines || this.properties.overgrown) {
/* 177 */       BlockPos.betweenClosedStream(getBoundingBox()).forEach(pos -> {
/*     */             if (this.properties.vines) {
/*     */               maybeAddVines(random, (LevelAccessor)random, random);
/*     */             }
/*     */             if (this.properties.overgrown) {
/*     */               maybeAddLeavesAbove(random, (LevelAccessor)random, random);
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleDataMarker(String markerId, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBB) {}
/*     */ 
/*     */   
/*     */   private void maybeAddVines(RandomSource random, LevelAccessor level, BlockPos pos) {
/* 193 */     BlockState state = level.getBlockState(pos);
/* 194 */     if (state.isAir() || state.is(Blocks.VINE)) {
/*     */       return;
/*     */     }
/*     */     
/* 198 */     Direction direction = getRandomHorizontalDirection(random);
/* 199 */     BlockPos neighbourPos = pos.relative(direction);
/* 200 */     BlockState neighourState = level.getBlockState(neighbourPos);
/* 201 */     if (!neighourState.isAir()) {
/*     */       return;
/*     */     }
/* 204 */     if (!Block.isFaceFull(state.getCollisionShape((BlockGetter)level, pos), direction)) {
/*     */       return;
/*     */     }
/* 207 */     BooleanProperty vineDir = VineBlock.getPropertyForFace(direction.getOpposite());
/* 208 */     level.setBlock(neighbourPos, (BlockState)Blocks.VINE.defaultBlockState().setValue((Property)vineDir, true), 3);
/*     */   }
/*     */   
/*     */   private void maybeAddLeavesAbove(RandomSource random, LevelAccessor level, BlockPos pos) {
/* 212 */     if (random.nextFloat() < 0.5F && level.getBlockState(pos).is(Blocks.NETHERRACK) && level.getBlockState(pos.above()).isAir()) {
/* 213 */       level.setBlock(pos.above(), (BlockState)Blocks.JUNGLE_LEAVES.defaultBlockState().setValue((Property)LeavesBlock.PERSISTENT, true), 3);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addNetherrackDripColumnsBelowPortal(RandomSource random, LevelAccessor level) {
/* 218 */     for (int x = this.boundingBox.minX() + 1; x < this.boundingBox.maxX(); x++) {
/* 219 */       for (int z = this.boundingBox.minZ() + 1; z < this.boundingBox.maxZ(); z++) {
/* 220 */         BlockPos pos = new BlockPos(x, this.boundingBox.minY(), z);
/* 221 */         if (level.getBlockState(pos).is(Blocks.NETHERRACK)) {
/* 222 */           addNetherrackDripColumn(random, level, pos.below());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addNetherrackDripColumn(RandomSource random, LevelAccessor level, BlockPos pos) {
/* 229 */     BlockPos.MutableBlockPos currentPos = pos.mutable();
/* 230 */     placeNetherrackOrMagma(random, level, (BlockPos)currentPos);
/* 231 */     int remainingCap = 8;
/* 232 */     while (remainingCap > 0 && random.nextFloat() < 0.5F) {
/* 233 */       currentPos.move(Direction.DOWN);
/* 234 */       remainingCap--;
/* 235 */       placeNetherrackOrMagma(random, level, (BlockPos)currentPos);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spreadNetherrack(RandomSource random, LevelAccessor level) {
/* 240 */     boolean followGroundSurface = (this.verticalPlacement == VerticalPlacement.ON_LAND_SURFACE || this.verticalPlacement == VerticalPlacement.ON_OCEAN_FLOOR);
/*     */     
/* 242 */     BlockPos center = this.boundingBox.getCenter();
/* 243 */     int centerX = center.getX();
/* 244 */     int centerZ = center.getZ();
/*     */     float[] netherrackProbabilityByDistance = { 
/* 246 */         1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.9F, 0.9F, 0.8F, 0.7F, 0.6F, 0.4F, 0.2F };
/* 247 */     int maxDistance = netherrackProbabilityByDistance.length;
/* 248 */     int averageWidth = (this.boundingBox.getXSpan() + this.boundingBox.getZSpan()) / 2;
/* 249 */     int distanceAdjustment = random.nextInt(Math.max(1, 8 - averageWidth / 2));
/* 250 */     int maxYDiff = 3;
/* 251 */     BlockPos.MutableBlockPos pos = BlockPos.ZERO.mutable();
/* 252 */     for (int x = centerX - maxDistance; x <= centerX + maxDistance; x++) {
/* 253 */       for (int z = centerZ - maxDistance; z <= centerZ + maxDistance; z++) {
/* 254 */         int distance = Math.abs(x - centerX) + Math.abs(z - centerZ);
/* 255 */         int adjustedDistance = Math.max(0, distance + distanceAdjustment);
/* 256 */         if (adjustedDistance < maxDistance) {
/*     */ 
/*     */           
/* 259 */           float probabilityOfNetherrack = netherrackProbabilityByDistance[adjustedDistance];
/* 260 */           if (random.nextDouble() < probabilityOfNetherrack) {
/* 261 */             int surfaceY = getSurfaceY(level, x, z, this.verticalPlacement);
/* 262 */             int y = followGroundSurface ? surfaceY : Math.min(this.boundingBox.minY(), surfaceY);
/* 263 */             pos.set(x, y, z);
/* 264 */             if (Math.abs(y - this.boundingBox.minY()) <= 3 && canBlockBeReplacedByNetherrackOrMagma(level, (BlockPos)pos)) {
/* 265 */               placeNetherrackOrMagma(random, level, (BlockPos)pos);
/* 266 */               if (this.properties.overgrown) {
/* 267 */                 maybeAddLeavesAbove(random, level, (BlockPos)pos);
/*     */               }
/* 269 */               addNetherrackDripColumn(random, level, pos.below());
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private boolean canBlockBeReplacedByNetherrackOrMagma(LevelAccessor level, BlockPos pos) {
/* 277 */     BlockState state = level.getBlockState(pos);
/* 278 */     return (!state.is(Blocks.AIR) && 
/* 279 */       !state.is(Blocks.OBSIDIAN) && 
/* 280 */       !state.is(BlockTags.FEATURES_CANNOT_REPLACE) && (this.verticalPlacement == VerticalPlacement.IN_NETHER || 
/* 281 */       !state.is(Blocks.LAVA)));
/*     */   }
/*     */   
/*     */   private void placeNetherrackOrMagma(RandomSource random, LevelAccessor level, BlockPos pos) {
/* 285 */     if (!this.properties.cold && random.nextFloat() < 0.07F) {
/* 286 */       level.setBlock(pos, Blocks.MAGMA_BLOCK.defaultBlockState(), 3);
/*     */     } else {
/* 288 */       level.setBlock(pos, Blocks.NETHERRACK.defaultBlockState(), 3);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int getSurfaceY(LevelAccessor level, int x, int z, VerticalPlacement verticalPlacement) {
/* 293 */     return level.getHeight(getHeightMapType(verticalPlacement), x, z) - 1;
/*     */   }
/*     */   
/*     */   public static Heightmap.Types getHeightMapType(VerticalPlacement verticalPlacement) {
/* 297 */     return (verticalPlacement == VerticalPlacement.ON_OCEAN_FLOOR) ? Heightmap.Types.OCEAN_FLOOR_WG : Heightmap.Types.WORLD_SURFACE_WG;
/*     */   }
/*     */   
/*     */   private static ProcessorRule getBlockReplaceRule(Block source, float probability, Block target) {
/* 301 */     return new ProcessorRule((RuleTest)new RandomBlockMatchTest(source, probability), (RuleTest)AlwaysTrueTest.INSTANCE, target.defaultBlockState());
/*     */   }
/*     */   
/*     */   private static ProcessorRule getBlockReplaceRule(Block source, Block target) {
/* 305 */     return new ProcessorRule((RuleTest)new net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest(source), (RuleTest)AlwaysTrueTest.INSTANCE, target.defaultBlockState());
/*     */   }
/*     */   
/*     */   public enum VerticalPlacement implements StringRepresentable {
/* 309 */     ON_LAND_SURFACE("on_land_surface"),
/* 310 */     PARTLY_BURIED("partly_buried"),
/* 311 */     ON_OCEAN_FLOOR("on_ocean_floor"),
/* 312 */     IN_MOUNTAIN("in_mountain"),
/* 313 */     UNDERGROUND("underground"),
/* 314 */     IN_NETHER("in_nether");
/*     */ 
/*     */     
/* 317 */     public static final Codec<VerticalPlacement> CODEC = (Codec<VerticalPlacement>)StringRepresentable.fromEnum(VerticalPlacement::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     VerticalPlacement(String name) {
/* 322 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 326 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 331 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/structures/RuinedPortalPiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */