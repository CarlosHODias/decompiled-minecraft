/*     */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.IdMapper;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.data.worldgen.Pools;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.DoubleTag;
/*     */ import net.minecraft.nbt.IntTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.decoration.painting.Painting;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.JigsawBlock;
/*     */ import net.minecraft.world.level.block.LiquidBlockContainer;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
/*     */ import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class StructureTemplate {
/*  69 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final String PALETTE_TAG = "palette";
/*     */   
/*     */   public static final String PALETTE_LIST_TAG = "palettes";
/*     */   public static final String ENTITIES_TAG = "entities";
/*     */   public static final String BLOCKS_TAG = "blocks";
/*     */   public static final String BLOCK_TAG_POS = "pos";
/*     */   public static final String BLOCK_TAG_STATE = "state";
/*     */   public static final String BLOCK_TAG_NBT = "nbt";
/*     */   public static final String ENTITY_TAG_POS = "pos";
/*     */   public static final String ENTITY_TAG_BLOCKPOS = "blockPos";
/*     */   public static final String ENTITY_TAG_NBT = "nbt";
/*     */   public static final String SIZE_TAG = "size";
/*  83 */   private final List<Palette> palettes = Lists.newArrayList();
/*  84 */   private final List<StructureEntityInfo> entityInfoList = Lists.newArrayList();
/*  85 */   private Vec3i size = Vec3i.ZERO;
/*  86 */   private String author = "?";
/*     */   
/*     */   public Vec3i getSize() {
/*  89 */     return this.size;
/*     */   }
/*     */   
/*     */   public void setAuthor(String author) {
/*  93 */     this.author = author;
/*     */   }
/*     */   
/*     */   public String getAuthor() {
/*  97 */     return this.author;
/*     */   }
/*     */   
/*     */   public void fillFromWorld(Level level, BlockPos position, Vec3i size, boolean inludeEntities, List<Block> ignoreBlocks) {
/* 101 */     if (size.getX() < 1 || size.getY() < 1 || size.getZ() < 1) {
/*     */       return;
/*     */     }
/* 104 */     BlockPos corner2 = position.offset(size).offset(-1, -1, -1);
/* 105 */     List<StructureBlockInfo> fullBlockList = Lists.newArrayList();
/* 106 */     List<StructureBlockInfo> blockEntitiesList = Lists.newArrayList();
/* 107 */     List<StructureBlockInfo> otherBlocksList = Lists.newArrayList();
/*     */     
/* 109 */     BlockPos minCorner = new BlockPos(Math.min(position.getX(), corner2.getX()), Math.min(position.getY(), corner2.getY()), Math.min(position.getZ(), corner2.getZ()));
/* 110 */     BlockPos maxCorner = new BlockPos(Math.max(position.getX(), corner2.getX()), Math.max(position.getY(), corner2.getY()), Math.max(position.getZ(), corner2.getZ()));
/* 111 */     this.size = size;
/*     */     
/* 113 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(LOGGER); 
/* 114 */     try { for (BlockPos pos : (Iterable<BlockPos>)BlockPos.betweenClosed(minCorner, maxCorner)) {
/* 115 */         StructureBlockInfo info; BlockPos relativePos = pos.subtract((Vec3i)minCorner);
/* 116 */         BlockState blockState = level.getBlockState(pos);
/* 117 */         Objects.requireNonNull(blockState); if (ignoreBlocks.stream().anyMatch(blockState::is)) {
/*     */           continue;
/*     */         }
/* 120 */         BlockEntity blockEntity = level.getBlockEntity(pos);
/*     */ 
/*     */ 
/*     */         
/* 124 */         if (blockEntity != null) {
/* 125 */           TagValueOutput output = TagValueOutput.createWithContext((ProblemReporter)reporter, (HolderLookup.Provider)level.registryAccess());
/* 126 */           blockEntity.saveWithId((ValueOutput)output);
/* 127 */           info = new StructureBlockInfo(relativePos, blockState, output.buildResult());
/*     */         } else {
/* 129 */           info = new StructureBlockInfo(relativePos, blockState, null);
/*     */         } 
/*     */         
/* 132 */         addToLists(info, fullBlockList, blockEntitiesList, otherBlocksList);
/*     */       } 
/* 134 */       List<StructureBlockInfo> blockInfoList = buildInfoList(fullBlockList, blockEntitiesList, otherBlocksList);
/*     */       
/* 136 */       this.palettes.clear();
/* 137 */       this.palettes.add(new Palette(blockInfoList));
/*     */       
/* 139 */       if (inludeEntities) {
/* 140 */         fillEntityList(level, minCorner, maxCorner, (ProblemReporter)reporter);
/*     */       } else {
/* 142 */         this.entityInfoList.clear();
/*     */       } 
/* 144 */       reporter.close(); }
/*     */     catch (Throwable throwable) { try { reporter.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 148 */      } private static void addToLists(StructureBlockInfo info, List<StructureBlockInfo> fullBlockList, List<StructureBlockInfo> blockEntitiesList, List<StructureBlockInfo> otherBlocksList) { if (info.nbt != null) {
/* 149 */       blockEntitiesList.add(info);
/* 150 */     } else if (!info.state.getBlock().hasDynamicShape() && info.state.isCollisionShapeFullBlock((BlockGetter)net.minecraft.world.level.EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) {
/* 151 */       fullBlockList.add(info);
/*     */     } else {
/* 153 */       otherBlocksList.add(info);
/*     */     }  }
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<StructureBlockInfo> buildInfoList(List<StructureBlockInfo> fullBlockList, List<StructureBlockInfo> blockEntitiesList, List<StructureBlockInfo> otherBlocksList) {
/* 159 */     Comparator<StructureBlockInfo> comparator = Comparator.<StructureBlockInfo>comparingInt(o -> o.pos.getY()).thenComparingInt(o -> o.pos.getX()).thenComparingInt(o -> o.pos.getZ());
/* 160 */     fullBlockList.sort(comparator);
/* 161 */     otherBlocksList.sort(comparator);
/* 162 */     blockEntitiesList.sort(comparator);
/*     */     
/* 164 */     List<StructureBlockInfo> blockInfoList = Lists.newArrayList();
/* 165 */     blockInfoList.addAll(fullBlockList);
/* 166 */     blockInfoList.addAll(otherBlocksList);
/* 167 */     blockInfoList.addAll(blockEntitiesList);
/* 168 */     return blockInfoList;
/*     */   }
/*     */   
/*     */   private void fillEntityList(Level level, BlockPos minCorner, BlockPos maxCorner, ProblemReporter reporter) {
/* 172 */     List<Entity> entities = level.getEntitiesOfClass(Entity.class, net.minecraft.world.phys.AABB.encapsulatingFullBlocks(minCorner, maxCorner), input -> !(input instanceof net.minecraft.world.entity.player.Player));
/* 173 */     this.entityInfoList.clear();
/*     */     
/* 175 */     for (Entity entity : entities) {
/* 176 */       BlockPos blockPos; Vec3 pos = new Vec3(entity.getX() - minCorner.getX(), entity.getY() - minCorner.getY(), entity.getZ() - minCorner.getZ());
/* 177 */       TagValueOutput output = TagValueOutput.createWithContext(reporter.forChild(entity.problemPath()), (HolderLookup.Provider)entity.registryAccess());
/* 178 */       entity.save((ValueOutput)output);
/*     */       
/* 180 */       if (entity instanceof Painting) { Painting painting = (Painting)entity;
/* 181 */         blockPos = painting.getPos().subtract((Vec3i)minCorner); }
/*     */       else
/* 183 */       { blockPos = BlockPos.containing((Position)pos); }
/*     */ 
/*     */       
/* 186 */       this.entityInfoList.add(new StructureEntityInfo(pos, blockPos, output.buildResult().copy()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<StructureBlockInfo> filterBlocks(BlockPos position, StructurePlaceSettings settings, Block block) {
/* 191 */     return (List<StructureBlockInfo>)filterBlocks(position, settings, block, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<JigsawBlockInfo> getJigsaws(BlockPos position, Rotation rotation) {
/* 196 */     if (this.palettes.isEmpty()) {
/* 197 */       return new ArrayList<>();
/*     */     }
/*     */     
/* 200 */     StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(rotation);
/* 201 */     List<JigsawBlockInfo> jigsaws = settings.getRandomPalette(this.palettes, position).jigsaws();
/*     */ 
/*     */     
/* 204 */     List<JigsawBlockInfo> result = new ArrayList<>(jigsaws.size());
/* 205 */     for (JigsawBlockInfo jigsaw : jigsaws) {
/* 206 */       StructureBlockInfo blockInfo = jigsaw.info;
/* 207 */       result.add(jigsaw.withInfo(new StructureBlockInfo(calculateRelativePosition(settings, blockInfo.pos()).offset((Vec3i)position), blockInfo.state.rotate(settings.getRotation()), blockInfo.nbt)));
/*     */     } 
/* 209 */     return result;
/*     */   }
/*     */   
/*     */   public ObjectArrayList<StructureBlockInfo> filterBlocks(BlockPos position, StructurePlaceSettings settings, Block block, boolean absolute) {
/* 213 */     ObjectArrayList<StructureBlockInfo> result = new ObjectArrayList();
/* 214 */     BoundingBox boundingBox = settings.getBoundingBox();
/*     */     
/* 216 */     if (this.palettes.isEmpty()) {
/* 217 */       return result;
/*     */     }
/* 219 */     for (StructureBlockInfo blockInfo : settings.getRandomPalette(this.palettes, position).blocks(block)) {
/* 220 */       BlockPos blockPos = absolute ? calculateRelativePosition(settings, blockInfo.pos).offset((Vec3i)position) : blockInfo.pos;
/* 221 */       if (boundingBox != null && !boundingBox.isInside((Vec3i)blockPos)) {
/*     */         continue;
/*     */       }
/* 224 */       result.add(new StructureBlockInfo(blockPos, blockInfo.state.rotate(settings.getRotation()), blockInfo.nbt));
/*     */     } 
/* 226 */     return result;
/*     */   }
/*     */   
/*     */   public BlockPos calculateConnectedPosition(StructurePlaceSettings settings1, BlockPos connection1, StructurePlaceSettings settings2, BlockPos connection2) {
/* 230 */     BlockPos markerPos1 = calculateRelativePosition(settings1, connection1);
/* 231 */     BlockPos markerPos2 = calculateRelativePosition(settings2, connection2);
/* 232 */     return markerPos1.subtract((Vec3i)markerPos2);
/*     */   }
/*     */   
/*     */   public static BlockPos calculateRelativePosition(StructurePlaceSettings settings, BlockPos pos) {
/* 236 */     return transform(pos, settings.getMirror(), settings.getRotation(), settings.getRotationPivot());
/*     */   }
/*     */   
/*     */   public boolean placeInWorld(ServerLevelAccessor level, BlockPos position, BlockPos referencePos, StructurePlaceSettings settings, RandomSource random, @Block.UpdateFlags int updateMode) {
/* 240 */     if (this.palettes.isEmpty()) {
/* 241 */       return false;
/*     */     }
/* 243 */     List<StructureBlockInfo> blockInfoList = settings.getRandomPalette(this.palettes, position).blocks();
/* 244 */     if ((blockInfoList.isEmpty() && (settings.isIgnoreEntities() || this.entityInfoList.isEmpty())) || this.size.getX() < 1 || this.size.getY() < 1 || this.size.getZ() < 1) {
/* 245 */       return false;
/*     */     }
/*     */     
/* 248 */     BoundingBox boundingBox = settings.getBoundingBox();
/* 249 */     List<BlockPos> toFill = Lists.newArrayListWithCapacity(settings.shouldApplyWaterlogging() ? blockInfoList.size() : 0);
/* 250 */     List<BlockPos> lockedFluids = Lists.newArrayListWithCapacity(settings.shouldApplyWaterlogging() ? blockInfoList.size() : 0);
/* 251 */     List<Pair<BlockPos, CompoundTag>> placed = Lists.newArrayListWithCapacity(blockInfoList.size());
/*     */     
/* 253 */     int minX = Integer.MAX_VALUE;
/* 254 */     int minY = Integer.MAX_VALUE;
/* 255 */     int minZ = Integer.MAX_VALUE;
/*     */     
/* 257 */     int maxX = Integer.MIN_VALUE;
/* 258 */     int maxY = Integer.MIN_VALUE;
/* 259 */     int maxZ = Integer.MIN_VALUE;
/*     */     
/* 261 */     List<StructureBlockInfo> processedBlockInfoList = processBlockInfos(level, position, referencePos, settings, blockInfoList);
/*     */     
/* 263 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(LOGGER); 
/* 264 */     try { for (StructureBlockInfo blockInfo : processedBlockInfoList) {
/* 265 */         BlockPos blockPos = blockInfo.pos;
/*     */         
/* 267 */         if (boundingBox != null && !boundingBox.isInside((Vec3i)blockPos)) {
/*     */           continue;
/*     */         }
/*     */         
/* 271 */         FluidState previousFluidState = settings.shouldApplyWaterlogging() ? level.getFluidState(blockPos) : null;
/* 272 */         BlockState state = blockInfo.state.mirror(settings.getMirror()).rotate(settings.getRotation());
/*     */         
/* 274 */         if (blockInfo.nbt != null)
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 279 */           level.setBlock(blockPos, Blocks.BARRIER.defaultBlockState(), 820);
/*     */         }
/* 281 */         if (level.setBlock(blockPos, state, updateMode)) {
/* 282 */           minX = Math.min(minX, blockPos.getX());
/* 283 */           minY = Math.min(minY, blockPos.getY());
/* 284 */           minZ = Math.min(minZ, blockPos.getZ());
/*     */           
/* 286 */           maxX = Math.max(maxX, blockPos.getX());
/* 287 */           maxY = Math.max(maxY, blockPos.getY());
/* 288 */           maxZ = Math.max(maxZ, blockPos.getZ());
/* 289 */           placed.add(Pair.of(blockPos, blockInfo.nbt));
/*     */           
/* 291 */           if (blockInfo.nbt != null) {
/* 292 */             BlockEntity blockEntity = level.getBlockEntity(blockPos);
/* 293 */             if (blockEntity != null) {
/* 294 */               if (!net.minecraft.SharedConstants.DEBUG_STRUCTURE_EDIT_MODE && blockEntity instanceof net.minecraft.world.RandomizableContainer) {
/* 295 */                 blockInfo.nbt.putLong("LootTableSeed", random.nextLong());
/*     */               }
/* 297 */               blockEntity.loadWithComponents(TagValueInput.create(reporter.forChild(blockEntity.problemPath()), (HolderLookup.Provider)level.registryAccess(), blockInfo.nbt));
/*     */             } 
/*     */           } 
/* 300 */           if (previousFluidState != null) {
/* 301 */             if (state.getFluidState().isSource()) {
/*     */               
/* 303 */               lockedFluids.add(blockPos); continue;
/* 304 */             }  if (state.getBlock() instanceof LiquidBlockContainer) {
/*     */               
/* 306 */               ((LiquidBlockContainer)state.getBlock()).placeLiquid((LevelAccessor)level, blockPos, state, previousFluidState);
/* 307 */               if (!previousFluidState.isSource())
/*     */               {
/* 309 */                 toFill.add(blockPos);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/*     */       boolean filled = true;
/* 317 */       Direction[] directions = { Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
/*     */       
/* 319 */       while (filled && !toFill.isEmpty()) {
/* 320 */         filled = false;
/* 321 */         for (Iterator<BlockPos> iterator = toFill.iterator(); iterator.hasNext(); ) {
/* 322 */           BlockPos pos = iterator.next();
/*     */           
/* 324 */           FluidState toPlace = level.getFluidState(pos);
/* 325 */           for (int i = 0; i < directions.length && !toPlace.isSource(); i++) {
/* 326 */             BlockPos neighborPos = pos.relative(directions[i]);
/* 327 */             FluidState neighbor = level.getFluidState(neighborPos);
/* 328 */             if (neighbor.isSource() && !lockedFluids.contains(neighborPos)) {
/* 329 */               toPlace = neighbor;
/*     */             }
/*     */           } 
/*     */           
/* 333 */           if (toPlace.isSource()) {
/* 334 */             BlockState state = level.getBlockState(pos);
/* 335 */             Block block = state.getBlock();
/* 336 */             if (block instanceof LiquidBlockContainer) {
/* 337 */               ((LiquidBlockContainer)block).placeLiquid((LevelAccessor)level, pos, state, toPlace);
/* 338 */               filled = true;
/* 339 */               iterator.remove();
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 345 */       if (minX <= maxX) {
/* 346 */         if (!settings.getKnownShape()) {
/* 347 */           BitSetDiscreteVoxelShape bitSetDiscreteVoxelShape = new BitSetDiscreteVoxelShape(maxX - minX + 1, maxY - minY + 1, maxZ - minZ + 1);
/*     */           
/* 349 */           int startX = minX;
/* 350 */           int startY = minY;
/* 351 */           int startZ = minZ;
/*     */           
/* 353 */           for (Pair<BlockPos, CompoundTag> blockInfo : placed) {
/* 354 */             BlockPos blockPos = (BlockPos)blockInfo.getFirst();
/* 355 */             bitSetDiscreteVoxelShape.fill(blockPos.getX() - startX, blockPos.getY() - startY, blockPos.getZ() - startZ);
/*     */           } 
/*     */           
/* 358 */           updateShapeAtEdge((LevelAccessor)level, updateMode, (DiscreteVoxelShape)bitSetDiscreteVoxelShape, startX, startY, startZ);
/*     */         } 
/*     */         
/* 361 */         for (Pair<BlockPos, CompoundTag> blockInfo : placed) {
/* 362 */           BlockPos blockPos = (BlockPos)blockInfo.getFirst();
/* 363 */           if (!settings.getKnownShape()) {
/* 364 */             BlockState state = level.getBlockState(blockPos);
/* 365 */             BlockState newState = Block.updateFromNeighbourShapes(state, (LevelAccessor)level, blockPos);
/* 366 */             if (state != newState) {
/* 367 */               level.setBlock(blockPos, newState, updateMode & 0xFFFFFFFE | 0x10);
/*     */             }
/* 369 */             level.updateNeighborsAt(blockPos, newState.getBlock());
/*     */           } 
/*     */           
/* 372 */           if (blockInfo.getSecond() != null) {
/* 373 */             BlockEntity blockEntity = level.getBlockEntity(blockPos);
/* 374 */             if (blockEntity != null) {
/* 375 */               blockEntity.setChanged();
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 381 */       if (!settings.isIgnoreEntities()) {
/* 382 */         placeEntities(level, position, settings.getMirror(), settings.getRotation(), settings.getRotationPivot(), boundingBox, settings.shouldFinalizeEntities(), (ProblemReporter)reporter);
/*     */       }
/* 384 */       reporter.close(); } catch (Throwable throwable) { try { reporter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 386 */      return true;
/*     */   }
/*     */   
/*     */   public static void updateShapeAtEdge(LevelAccessor level, @Block.UpdateFlags int updateMode, DiscreteVoxelShape shape, BlockPos pos) {
/* 390 */     updateShapeAtEdge(level, updateMode, shape, pos.getX(), pos.getY(), pos.getZ());
/*     */   }
/*     */   
/*     */   public static void updateShapeAtEdge(LevelAccessor level, @Block.UpdateFlags int updateMode, DiscreteVoxelShape shape, int startX, int startY, int startZ) {
/* 394 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 395 */     BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
/* 396 */     shape.forAllFaces((direction, x, y, z) -> {
/*     */           pos.set(startX + x, startY + y, startZ + z);
/*     */           neighborPos.setWithOffset((Vec3i)pos, direction);
/*     */           BlockState state = level.getBlockState((BlockPos)pos), neighborState = level.getBlockState((BlockPos)neighborPos), newState = state.updateShape((LevelReader)level, (ScheduledTickAccess)level, (BlockPos)pos, direction, (BlockPos)neighborPos, neighborState, level.getRandom());
/*     */           if (state != newState) {
/*     */             level.setBlock((BlockPos)pos, newState, updateMode & 0xFFFFFFFE);
/*     */           }
/*     */           BlockState newNeighborState = neighborState.updateShape((LevelReader)level, (ScheduledTickAccess)level, (BlockPos)neighborPos, direction.getOpposite(), (BlockPos)pos, newState, level.getRandom());
/*     */           if (neighborState != newNeighborState) {
/*     */             level.setBlock((BlockPos)neighborPos, newNeighborState, updateMode & 0xFFFFFFFE);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<StructureBlockInfo> processBlockInfos(ServerLevelAccessor level, BlockPos position, BlockPos referencePos, StructurePlaceSettings settings, List<StructureBlockInfo> blockInfoList) {
/* 414 */     List<StructureBlockInfo> originalBlockInfoList = new ArrayList<>();
/* 415 */     List<StructureBlockInfo> processedBlockInfoList = new ArrayList<>();
/*     */     
/* 417 */     for (StructureBlockInfo blockInfo : blockInfoList) {
/* 418 */       BlockPos blockPos = calculateRelativePosition(settings, blockInfo.pos).offset((Vec3i)position);
/* 419 */       StructureBlockInfo processedBlockInfo = new StructureBlockInfo(blockPos, blockInfo.state, (blockInfo.nbt != null) ? blockInfo.nbt.copy() : null);
/*     */       
/* 421 */       Iterator<StructureProcessor> iterator = settings.getProcessors().iterator();
/* 422 */       while (processedBlockInfo != null && iterator.hasNext()) {
/* 423 */         processedBlockInfo = ((StructureProcessor)iterator.next()).processBlock((LevelReader)level, position, referencePos, blockInfo, processedBlockInfo, settings);
/*     */       }
/*     */       
/* 426 */       if (processedBlockInfo != null) {
/* 427 */         processedBlockInfoList.add(processedBlockInfo);
/* 428 */         originalBlockInfoList.add(blockInfo);
/*     */       } 
/*     */     } 
/*     */     
/* 432 */     for (StructureProcessor processor : settings.getProcessors()) {
/* 433 */       processedBlockInfoList = processor.finalizeProcessing(level, position, referencePos, originalBlockInfoList, processedBlockInfoList, settings);
/*     */     }
/*     */     
/* 436 */     return processedBlockInfoList;
/*     */   }
/*     */   
/*     */   private void placeEntities(ServerLevelAccessor level, BlockPos position, Mirror mirror, Rotation rotation, BlockPos pivot, BoundingBox boundingBox, boolean finalizeEntities, ProblemReporter problemReporter) {
/* 440 */     for (StructureEntityInfo entityInfo : this.entityInfoList) {
/* 441 */       BlockPos blockPos = transform(entityInfo.blockPos, mirror, rotation, pivot).offset((Vec3i)position);
/* 442 */       if (boundingBox != null && !boundingBox.isInside((Vec3i)blockPos)) {
/*     */         continue;
/*     */       }
/*     */       
/* 446 */       CompoundTag tag = entityInfo.nbt.copy();
/* 447 */       Vec3 relativePos = transform(entityInfo.pos, mirror, rotation, pivot);
/* 448 */       Vec3 pos = relativePos.add(position.getX(), position.getY(), position.getZ());
/*     */       
/* 450 */       ListTag posTag = new ListTag();
/* 451 */       posTag.add(DoubleTag.valueOf(pos.x));
/* 452 */       posTag.add(DoubleTag.valueOf(pos.y));
/* 453 */       posTag.add(DoubleTag.valueOf(pos.z));
/* 454 */       tag.put("Pos", (Tag)posTag);
/*     */       
/* 456 */       tag.remove("UUID");
/*     */       
/* 458 */       createEntityIgnoreException(problemReporter, level, tag).ifPresent(entity -> {
/*     */             float yRot = entity.rotate(rotation);
/*     */             yRot += entity.mirror(mirror) - entity.getYRot();
/*     */             entity.snapTo(pos.x, pos.y, pos.z, yRot, entity.getXRot());
/*     */             entity.setYBodyRot(yRot);
/*     */             entity.setYHeadRot(yRot);
/*     */             if (finalizeEntities && entity instanceof Mob) {
/*     */               Mob mob = (Mob)entity;
/*     */               mob.finalizeSpawn(level, level.getCurrentDifficultyAt(BlockPos.containing((Position)pos)), EntitySpawnReason.STRUCTURE, null);
/*     */             } 
/*     */             level.addFreshEntityWithPassengers(entity);
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Optional<Entity> createEntityIgnoreException(ProblemReporter reporter, ServerLevelAccessor level, CompoundTag tag) {
/*     */     try {
/* 475 */       return EntityType.create(TagValueInput.create(reporter, (HolderLookup.Provider)level.registryAccess(), tag), (Level)level.getLevel(), EntitySpawnReason.STRUCTURE);
/* 476 */     } catch (Exception ignored) {
/* 477 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Vec3i getSize(Rotation rotation) {
/* 482 */     switch (rotation) {
/*     */       case COUNTERCLOCKWISE_90:
/*     */       case CLOCKWISE_90:
/* 485 */         return new Vec3i(this.size.getZ(), this.size.getY(), this.size.getX());
/*     */     } 
/* 487 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BlockPos transform(BlockPos pos, Mirror mirror, Rotation rotation, BlockPos pivot) {
/* 492 */     int x = pos.getX();
/* 493 */     int y = pos.getY();
/* 494 */     int z = pos.getZ();
/*     */     
/*     */     boolean wasMirrored = true;
/* 497 */     switch (mirror) {
/*     */       case LEFT_RIGHT:
/* 499 */         z = -z;
/*     */         break;
/*     */       case FRONT_BACK:
/* 502 */         x = -x;
/*     */         break;
/*     */       default:
/* 505 */         wasMirrored = false;
/*     */         break;
/*     */     } 
/*     */     
/* 509 */     int pivotX = pivot.getX();
/* 510 */     int pivotZ = pivot.getZ();
/* 511 */     switch (rotation) {
/*     */       case CLOCKWISE_180:
/* 513 */         return new BlockPos(pivotX + pivotX - x, y, pivotZ + pivotZ - z);
/*     */       case COUNTERCLOCKWISE_90:
/* 515 */         return new BlockPos(pivotX - pivotZ + z, y, pivotX + pivotZ - x);
/*     */       case CLOCKWISE_90:
/* 517 */         return new BlockPos(pivotX + pivotZ - z, y, pivotZ - pivotX + x);
/*     */     } 
/* 519 */     return wasMirrored ? new BlockPos(x, y, z) : pos;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Vec3 transform(Vec3 pos, Mirror mirror, Rotation rotation, BlockPos pivot) {
/* 524 */     double x = pos.x;
/* 525 */     double y = pos.y;
/* 526 */     double z = pos.z;
/*     */     
/*     */     boolean wasMirrored = true;
/* 529 */     switch (mirror) {
/*     */       case LEFT_RIGHT:
/* 531 */         z = 1.0D - z;
/*     */         break;
/*     */       case FRONT_BACK:
/* 534 */         x = 1.0D - x;
/*     */         break;
/*     */       default:
/* 537 */         wasMirrored = false;
/*     */         break;
/*     */     } 
/*     */     
/* 541 */     int pivotX = pivot.getX();
/* 542 */     int pivotZ = pivot.getZ();
/* 543 */     switch (rotation) {
/*     */       case CLOCKWISE_180:
/* 545 */         return new Vec3((pivotX + pivotX + 1) - x, y, (pivotZ + pivotZ + 1) - z);
/*     */       case COUNTERCLOCKWISE_90:
/* 547 */         return new Vec3((pivotX - pivotZ) + z, y, (pivotX + pivotZ + 1) - x);
/*     */       case CLOCKWISE_90:
/* 549 */         return new Vec3((pivotX + pivotZ + 1) - z, y, (pivotZ - pivotX) + x);
/*     */     } 
/* 551 */     return wasMirrored ? new Vec3(x, y, z) : pos;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getZeroPositionWithTransform(BlockPos zeroPos, Mirror mirror, Rotation rotation) {
/* 556 */     return getZeroPositionWithTransform(zeroPos, mirror, rotation, getSize().getX(), getSize().getZ());
/*     */   }
/*     */   
/*     */   public static BlockPos getZeroPositionWithTransform(BlockPos zeroPos, Mirror mirror, Rotation rotation, int sizeX, int sizeZ) {
/* 560 */     sizeX--;
/* 561 */     sizeZ--;
/*     */     
/* 563 */     int mirrorDeltaX = (mirror == Mirror.FRONT_BACK) ? sizeX : 0;
/* 564 */     int mirrorDeltaZ = (mirror == Mirror.LEFT_RIGHT) ? sizeZ : 0;
/*     */     
/* 566 */     BlockPos targetPos = zeroPos;
/*     */     
/* 568 */     switch (rotation) {
/*     */       case NONE:
/* 570 */         targetPos = zeroPos.offset(mirrorDeltaX, 0, mirrorDeltaZ);
/*     */         break;
/*     */       case CLOCKWISE_90:
/* 573 */         targetPos = zeroPos.offset(sizeZ - mirrorDeltaZ, 0, mirrorDeltaX);
/*     */         break;
/*     */       case CLOCKWISE_180:
/* 576 */         targetPos = zeroPos.offset(sizeX - mirrorDeltaX, 0, sizeZ - mirrorDeltaZ);
/*     */         break;
/*     */       case COUNTERCLOCKWISE_90:
/* 579 */         targetPos = zeroPos.offset(mirrorDeltaZ, 0, sizeX - mirrorDeltaX);
/*     */         break;
/*     */     } 
/* 582 */     return targetPos;
/*     */   }
/*     */   
/*     */   public BoundingBox getBoundingBox(StructurePlaceSettings settings, BlockPos position) {
/* 586 */     return getBoundingBox(position, settings.getRotation(), settings.getRotationPivot(), settings.getMirror());
/*     */   }
/*     */   
/*     */   public BoundingBox getBoundingBox(BlockPos position, Rotation rotation, BlockPos pivot, Mirror mirror) {
/* 590 */     return getBoundingBox(position, rotation, pivot, mirror, this.size);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   protected static BoundingBox getBoundingBox(BlockPos position, Rotation rotation, BlockPos pivot, Mirror mirror, Vec3i size) {
/* 595 */     Vec3i delta = size.offset(-1, -1, -1);
/* 596 */     BlockPos corner1 = transform(BlockPos.ZERO, mirror, rotation, pivot);
/* 597 */     BlockPos corner2 = transform(BlockPos.ZERO.offset(delta), mirror, rotation, pivot);
/* 598 */     return BoundingBox.fromCorners((Vec3i)corner1, (Vec3i)corner2).move((Vec3i)position);
/*     */   }
/*     */   
/*     */   private static class SimplePalette implements Iterable<BlockState> {
/* 602 */     public static final BlockState DEFAULT_BLOCK_STATE = Blocks.AIR.defaultBlockState();
/*     */     
/* 604 */     private final IdMapper<BlockState> ids = new IdMapper(16);
/*     */     private int lastId;
/*     */     
/*     */     public int idFor(BlockState state) {
/* 608 */       int id = this.ids.getId(state);
/* 609 */       if (id == -1) {
/* 610 */         id = this.lastId++;
/* 611 */         this.ids.addMapping(state, id);
/*     */       } 
/*     */       
/* 614 */       return id;
/*     */     }
/*     */     
/*     */     public BlockState stateFor(int index) {
/* 618 */       BlockState blockState = (BlockState)this.ids.byId(index);
/* 619 */       return (blockState == null) ? DEFAULT_BLOCK_STATE : blockState;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<BlockState> iterator() {
/* 624 */       return this.ids.iterator();
/*     */     }
/*     */     
/*     */     public void addMapping(BlockState state, int id) {
/* 628 */       this.ids.addMapping(state, id);
/*     */     }
/*     */   }
/*     */   
/*     */   public CompoundTag save(CompoundTag tag) {
/* 633 */     if (this.palettes.isEmpty()) {
/* 634 */       tag.put("blocks", (Tag)new ListTag());
/* 635 */       tag.put("palette", (Tag)new ListTag());
/*     */     } else {
/* 637 */       List<SimplePalette> palettes = Lists.newArrayList();
/* 638 */       SimplePalette mainPalette = new SimplePalette();
/* 639 */       palettes.add(mainPalette);
/*     */       
/* 641 */       for (int p = 1; p < this.palettes.size(); p++) {
/* 642 */         palettes.add(new SimplePalette());
/*     */       }
/*     */       
/* 645 */       ListTag blockList = new ListTag();
/* 646 */       List<StructureBlockInfo> mainPaletteBlocks = ((Palette)this.palettes.get(0)).blocks();
/* 647 */       for (int i = 0; i < mainPaletteBlocks.size(); i++) {
/* 648 */         StructureBlockInfo blockInfo = mainPaletteBlocks.get(i);
/* 649 */         CompoundTag blockTag = new CompoundTag();
/* 650 */         blockTag.put("pos", (Tag)newIntegerList(new int[] { blockInfo.pos.getX(), blockInfo.pos.getY(), blockInfo.pos.getZ() }));
/* 651 */         int id = mainPalette.idFor(blockInfo.state);
/* 652 */         blockTag.putInt("state", id);
/* 653 */         if (blockInfo.nbt != null) {
/* 654 */           blockTag.put("nbt", (Tag)blockInfo.nbt);
/*     */         }
/* 656 */         blockList.add(blockTag);
/*     */         
/* 658 */         for (int j = 1; j < this.palettes.size(); j++) {
/* 659 */           SimplePalette palette = palettes.get(j);
/* 660 */           palette.addMapping(((StructureBlockInfo)((Palette)this.palettes.get(j)).blocks().get(i)).state, id);
/*     */         } 
/*     */       } 
/* 663 */       tag.put("blocks", (Tag)blockList);
/*     */       
/* 665 */       if (palettes.size() == 1) {
/* 666 */         ListTag paletteList = new ListTag();
/* 667 */         for (BlockState state : (Iterable<BlockState>)mainPalette) {
/* 668 */           paletteList.add(NbtUtils.writeBlockState(state));
/*     */         }
/* 670 */         tag.put("palette", (Tag)paletteList);
/*     */       } else {
/* 672 */         ListTag paletteListList = new ListTag();
/* 673 */         for (SimplePalette palette : palettes) {
/* 674 */           ListTag paletteList = new ListTag();
/* 675 */           for (BlockState state : (Iterable<BlockState>)palette) {
/* 676 */             paletteList.add(NbtUtils.writeBlockState(state));
/*     */           }
/* 678 */           paletteListList.add(paletteList);
/*     */         } 
/* 680 */         tag.put("palettes", (Tag)paletteListList);
/*     */       } 
/*     */     } 
/*     */     
/* 684 */     ListTag entityList = new ListTag();
/* 685 */     for (StructureEntityInfo entityInfo : this.entityInfoList) {
/* 686 */       CompoundTag entityTag = new CompoundTag();
/* 687 */       entityTag.put("pos", (Tag)newDoubleList(new double[] { entityInfo.pos.x, entityInfo.pos.y, entityInfo.pos.z }));
/* 688 */       entityTag.put("blockPos", (Tag)newIntegerList(new int[] { entityInfo.blockPos.getX(), entityInfo.blockPos.getY(), entityInfo.blockPos.getZ() }));
/* 689 */       if (entityInfo.nbt != null) {
/* 690 */         entityTag.put("nbt", (Tag)entityInfo.nbt);
/*     */       }
/* 692 */       entityList.add(entityTag);
/*     */     } 
/*     */     
/* 695 */     tag.put("entities", (Tag)entityList);
/* 696 */     tag.put("size", (Tag)newIntegerList(new int[] { this.size.getX(), this.size.getY(), this.size.getZ() }));
/* 697 */     return NbtUtils.addCurrentDataVersion(tag);
/*     */   }
/*     */   
/*     */   public void load(HolderGetter<Block> blockLookup, CompoundTag tag) {
/* 701 */     this.palettes.clear();
/* 702 */     this.entityInfoList.clear();
/*     */     
/* 704 */     ListTag sizeTag = tag.getListOrEmpty("size");
/* 705 */     this.size = new Vec3i(sizeTag.getIntOr(0, 0), sizeTag.getIntOr(1, 0), sizeTag.getIntOr(2, 0));
/*     */     
/* 707 */     ListTag blockList = tag.getListOrEmpty("blocks");
/*     */     
/* 709 */     Optional<ListTag> paletteListList = tag.getList("palettes");
/* 710 */     if (paletteListList.isPresent()) {
/* 711 */       for (int p = 0; p < ((ListTag)paletteListList.get()).size(); p++) {
/* 712 */         loadPalette(blockLookup, ((ListTag)paletteListList.get()).getListOrEmpty(p), blockList);
/*     */       }
/*     */     } else {
/* 715 */       loadPalette(blockLookup, tag.getListOrEmpty("palette"), blockList);
/*     */     } 
/*     */     
/* 718 */     tag.getListOrEmpty("entities").compoundStream().forEach(entityTag -> {
/*     */           ListTag posTag = entityTag.getListOrEmpty("pos");
/*     */           Vec3 pos = new Vec3(posTag.getDoubleOr(0, 0.0D), posTag.getDoubleOr(1, 0.0D), posTag.getDoubleOr(2, 0.0D));
/*     */           ListTag blockPosTag = entityTag.getListOrEmpty("blockPos");
/*     */           BlockPos blockPos = new BlockPos(blockPosTag.getIntOr(0, 0), blockPosTag.getIntOr(1, 0), blockPosTag.getIntOr(2, 0));
/*     */           entityTag.getCompound("nbt").ifPresent(());
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadPalette(HolderGetter<Block> blockLookup, ListTag paletteList, ListTag blockList) {
/* 730 */     SimplePalette palette = new SimplePalette();
/*     */     
/* 732 */     for (int i = 0; i < paletteList.size(); i++) {
/* 733 */       palette.addMapping(NbtUtils.readBlockState(blockLookup, paletteList.getCompoundOrEmpty(i)), i);
/*     */     }
/*     */     
/* 736 */     List<StructureBlockInfo> fullBlockList = Lists.newArrayList();
/* 737 */     List<StructureBlockInfo> blockEntitiesList = Lists.newArrayList();
/* 738 */     List<StructureBlockInfo> otherBlocksList = Lists.newArrayList();
/*     */     
/* 740 */     blockList.compoundStream().forEach(blockTag -> {
/*     */           ListTag posTag = blockTag.getListOrEmpty("pos");
/*     */           
/*     */           BlockPos pos = new BlockPos(posTag.getIntOr(0, 0), posTag.getIntOr(1, 0), posTag.getIntOr(2, 0));
/*     */           
/*     */           BlockState state = palette.stateFor(blockTag.getIntOr("state", 0));
/*     */           CompoundTag nbt = blockTag.getCompound("nbt").orElse(null);
/*     */           StructureBlockInfo info = new StructureBlockInfo(pos, state, nbt);
/*     */           addToLists(info, fullBlockList, blockEntitiesList, otherBlocksList);
/*     */         });
/* 750 */     List<StructureBlockInfo> blockInfoList = buildInfoList(fullBlockList, blockEntitiesList, otherBlocksList);
/*     */     
/* 752 */     this.palettes.add(new Palette(blockInfoList));
/*     */   }
/*     */   
/*     */   private ListTag newIntegerList(int... values) {
/* 756 */     ListTag res = new ListTag();
/* 757 */     for (int value : values) {
/* 758 */       res.add(IntTag.valueOf(value));
/*     */     }
/* 760 */     return res;
/*     */   }
/*     */   
/*     */   private ListTag newDoubleList(double... values) {
/* 764 */     ListTag res = new ListTag();
/* 765 */     for (double value : values) {
/* 766 */       res.add(DoubleTag.valueOf(value));
/*     */     }
/* 768 */     return res;
/*     */   }
/*     */   public static final class StructureBlockInfo extends Record { private final BlockPos pos; private final BlockState state; private final CompoundTag nbt;
/* 771 */     public StructureBlockInfo(BlockPos pos, BlockState state, CompoundTag nbt) { this.pos = pos; this.state = state; this.nbt = nbt; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #771	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 771 */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo; } public BlockPos pos() { return this.pos; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #771	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;
/* 771 */       //   0	8	1	o	Ljava/lang/Object; } public BlockState state() { return this.state; } public CompoundTag nbt() { return this.nbt; }
/*     */     
/*     */     public String toString() {
/* 774 */       return String.format(Locale.ROOT, "<StructureBlockInfo | %s | %s | %s>", new Object[] { this.pos, this.state, this.nbt });
/*     */     } }
/*     */ 
/*     */   
/*     */   public static JigsawBlockEntity.JointType getJointType(CompoundTag nbt, BlockState state) {
/* 779 */     return nbt.read("joint", (Codec)JigsawBlockEntity.JointType.CODEC).orElseGet(() -> getDefaultJointType(state));
/*     */   }
/*     */   
/*     */   public static JigsawBlockEntity.JointType getDefaultJointType(BlockState state) {
/* 783 */     return JigsawBlock.getFrontFacing(state).getAxis().isHorizontal() ? JigsawBlockEntity.JointType.ALIGNED : JigsawBlockEntity.JointType.ROLLABLE;
/*     */   }
/*     */   public static final class JigsawBlockInfo extends Record { private final StructureTemplate.StructureBlockInfo info; private final JigsawBlockEntity.JointType jointType; private final Identifier name; private final ResourceKey<StructureTemplatePool> pool; private final Identifier target; private final int placementPriority; private final int selectionPriority;
/* 786 */     public JigsawBlockInfo(StructureTemplate.StructureBlockInfo info, JigsawBlockEntity.JointType jointType, Identifier name, ResourceKey<StructureTemplatePool> pool, Identifier target, int placementPriority, int selectionPriority) { this.info = info; this.jointType = jointType; this.name = name; this.pool = pool; this.target = target; this.placementPriority = placementPriority; this.selectionPriority = selectionPriority; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$JigsawBlockInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #786	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$JigsawBlockInfo; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$JigsawBlockInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #786	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$JigsawBlockInfo;
/* 786 */       //   0	8	1	o	Ljava/lang/Object; } public StructureTemplate.StructureBlockInfo info() { return this.info; } public JigsawBlockEntity.JointType jointType() { return this.jointType; } public Identifier name() { return this.name; } public ResourceKey<StructureTemplatePool> pool() { return this.pool; } public Identifier target() { return this.target; } public int placementPriority() { return this.placementPriority; } public int selectionPriority() { return this.selectionPriority; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static JigsawBlockInfo of(StructureTemplate.StructureBlockInfo info) {
/* 796 */       CompoundTag nbt = Objects.<CompoundTag>requireNonNull(info.nbt(), () -> String.valueOf(info) + " nbt was null");
/*     */       
/* 798 */       return new JigsawBlockInfo(info, 
/*     */           
/* 800 */           StructureTemplate.getJointType(nbt, info.state()), 
/* 801 */           nbt.read("name", Identifier.CODEC).orElse(JigsawBlockEntity.EMPTY_ID), 
/* 802 */           nbt.read("pool", JigsawBlockEntity.POOL_CODEC).orElse(Pools.EMPTY), 
/* 803 */           nbt.read("target", Identifier.CODEC).orElse(JigsawBlockEntity.EMPTY_ID), 
/* 804 */           nbt.getIntOr("placement_priority", 0), 
/* 805 */           nbt.getIntOr("selection_priority", 0));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 811 */       return String.format(Locale.ROOT, "<JigsawBlockInfo | %s | %s | name: %s | pool: %s | target: %s | placement: %d | selection: %d | %s>", new Object[] { this.info.pos, this.info.state, this.name, this.pool.identifier(), this.target, this.placementPriority, this.selectionPriority, this.info.nbt });
/*     */     }
/*     */     
/*     */     public JigsawBlockInfo withInfo(StructureTemplate.StructureBlockInfo info) {
/* 815 */       return new JigsawBlockInfo(info, this.jointType, this.name, this.pool, this.target, this.placementPriority, this.selectionPriority);
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class StructureEntityInfo {
/*     */     public final Vec3 pos;
/*     */     public final BlockPos blockPos;
/*     */     public final CompoundTag nbt;
/*     */     
/*     */     public StructureEntityInfo(Vec3 pos, BlockPos blockPos, CompoundTag nbt) {
/* 825 */       this.pos = pos;
/* 826 */       this.blockPos = blockPos;
/* 827 */       this.nbt = nbt;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Palette
/*     */   {
/*     */     private final List<StructureTemplate.StructureBlockInfo> blocks;
/* 834 */     private final Map<Block, List<StructureTemplate.StructureBlockInfo>> cache = Maps.newHashMap();
/*     */     private List<StructureTemplate.JigsawBlockInfo> cachedJigsaws;
/*     */     
/*     */     private Palette(List<StructureTemplate.StructureBlockInfo> blocks) {
/* 838 */       this.blocks = blocks;
/*     */     }
/*     */     
/*     */     public List<StructureTemplate.JigsawBlockInfo> jigsaws() {
/* 842 */       if (this.cachedJigsaws == null) {
/* 843 */         this
/*     */           
/* 845 */           .cachedJigsaws = blocks(Blocks.JIGSAW).stream().map(StructureTemplate.JigsawBlockInfo::of).toList();
/*     */       }
/* 847 */       return this.cachedJigsaws;
/*     */     }
/*     */     
/*     */     public List<StructureTemplate.StructureBlockInfo> blocks() {
/* 851 */       return this.blocks;
/*     */     }
/*     */     
/*     */     public List<StructureTemplate.StructureBlockInfo> blocks(Block filter) {
/* 855 */       return this.cache.computeIfAbsent(filter, block -> (List)this.blocks.stream().filter(()).collect(Collectors.toList()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */