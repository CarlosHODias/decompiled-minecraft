/*     */ package net.minecraft.world.level.chunk;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrays;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Direction8;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.HorizontalDirectionalBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.ticks.SavedTick;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class UpgradeData {
/*  43 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  44 */   public static final UpgradeData EMPTY = new UpgradeData((LevelHeightAccessor)net.minecraft.world.level.EmptyBlockGetter.INSTANCE);
/*     */   private static final String TAG_INDICES = "Indices";
/*  46 */   private static final Direction8[] DIRECTIONS = Direction8.values();
/*     */ 
/*     */   
/*  49 */   private static final Codec<List<SavedTick<Block>>> BLOCK_TICKS_CODEC = SavedTick.codec(
/*  50 */       BuiltInRegistries.BLOCK.byNameCodec().orElse(Blocks.AIR))
/*  51 */     .listOf();
/*  52 */   private static final Codec<List<SavedTick<Fluid>>> FLUID_TICKS_CODEC = SavedTick.codec(
/*  53 */       BuiltInRegistries.FLUID.byNameCodec().orElse(Fluids.EMPTY))
/*  54 */     .listOf();
/*     */   
/*  56 */   private final EnumSet<Direction8> sides = EnumSet.noneOf(Direction8.class);
/*  57 */   private final List<SavedTick<Block>> neighborBlockTicks = Lists.newArrayList();
/*  58 */   private final List<SavedTick<Fluid>> neighborFluidTicks = Lists.newArrayList();
/*     */   private final int[][] index;
/*     */   
/*     */   private UpgradeData(LevelHeightAccessor levelHeightAccessor) {
/*  62 */     this.index = new int[levelHeightAccessor.getSectionsCount()][];
/*     */   }
/*     */   
/*     */   public UpgradeData(CompoundTag tag, LevelHeightAccessor levelHeightAccessor) {
/*  66 */     this(levelHeightAccessor);
/*     */     
/*  68 */     tag.getCompound("Indices").ifPresent(indicesTag -> {
/*     */           for (int i = 0; i < this.index.length; i++) {
/*     */             this.index[i] = indicesTag.getIntArray(String.valueOf(i)).orElse(null);
/*     */           }
/*     */         });
/*     */     
/*  74 */     int sideInt = tag.getIntOr("Sides", 0);
/*  75 */     for (Direction8 direction8 : Direction8.values()) {
/*  76 */       if ((sideInt & 1 << direction8.ordinal()) != 0) {
/*  77 */         this.sides.add(direction8);
/*     */       }
/*     */     } 
/*     */     
/*  81 */     Objects.requireNonNull(this.neighborBlockTicks); tag.read("neighbor_block_ticks", BLOCK_TICKS_CODEC).ifPresent(this.neighborBlockTicks::addAll);
/*  82 */     Objects.requireNonNull(this.neighborFluidTicks); tag.read("neighbor_fluid_ticks", FLUID_TICKS_CODEC).ifPresent(this.neighborFluidTicks::addAll);
/*     */   }
/*     */   
/*     */   private UpgradeData(UpgradeData source) {
/*  86 */     this.sides.addAll(source.sides);
/*  87 */     this.neighborBlockTicks.addAll(source.neighborBlockTicks);
/*  88 */     this.neighborFluidTicks.addAll(source.neighborFluidTicks);
/*  89 */     this.index = new int[source.index.length][];
/*  90 */     for (int i = 0; i < source.index.length; i++) {
/*  91 */       int[] indices = source.index[i];
/*  92 */       this.index[i] = (indices != null) ? IntArrays.copy(indices) : null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void upgrade(LevelChunk chunk) {
/*  97 */     upgradeInside(chunk);
/*  98 */     for (Direction8 direction8 : DIRECTIONS) {
/*  99 */       upgradeSides(chunk, direction8);
/*     */     }
/*     */     
/* 102 */     Level level = chunk.getLevel();
/*     */     
/* 104 */     this.neighborBlockTicks.forEach(tick -> {
/*     */           Block type = (tick.type() == Blocks.AIR) ? level.getBlockState(tick.pos()).getBlock() : (Block)tick.type();
/*     */           level.scheduleTick(tick.pos(), type, tick.delay(), tick.priority());
/*     */         });
/* 108 */     this.neighborFluidTicks.forEach(tick -> {
/*     */           Fluid type = (tick.type() == Fluids.EMPTY) ? level.getFluidState(tick.pos()).getType() : (Fluid)tick.type();
/*     */           
/*     */           level.scheduleTick(tick.pos(), type, tick.delay(), tick.priority());
/*     */         });
/* 113 */     CHUNKY_FIXERS.forEach(fixer -> fixer.processChunk((LevelAccessor)level));
/*     */   }
/*     */   
/*     */   private static void upgradeSides(LevelChunk chunk, Direction8 direction8) {
/* 117 */     Level level = chunk.getLevel();
/*     */     
/* 119 */     if (!(chunk.getUpgradeData()).sides.remove(direction8)) {
/*     */       return;
/*     */     }
/*     */     
/* 123 */     Set<Direction> directions = direction8.getDirections();
/*     */     
/* 125 */     int min = 0;
/* 126 */     int max = 15;
/*     */     
/* 128 */     boolean east = directions.contains(Direction.EAST);
/* 129 */     boolean west = directions.contains(Direction.WEST);
/* 130 */     boolean south = directions.contains(Direction.SOUTH);
/* 131 */     boolean north = directions.contains(Direction.NORTH);
/* 132 */     boolean singular = (directions.size() == 1);
/*     */     
/* 134 */     ChunkPos chunkPos = chunk.getPos();
/* 135 */     int minX = chunkPos.getMinBlockX() + ((singular && (north || south)) ? 1 : (west ? 0 : 15));
/* 136 */     int maxX = chunkPos.getMinBlockX() + ((singular && (north || south)) ? 14 : (west ? 0 : 15));
/* 137 */     int minZ = chunkPos.getMinBlockZ() + ((singular && (east || west)) ? 1 : (north ? 0 : 15));
/* 138 */     int maxZ = chunkPos.getMinBlockZ() + ((singular && (east || west)) ? 14 : (north ? 0 : 15));
/*     */     
/* 140 */     Direction[] updateDirections = Direction.values();
/* 141 */     BlockPos.MutableBlockPos neighbourPos = new BlockPos.MutableBlockPos();
/* 142 */     for (BlockPos pos : (Iterable<BlockPos>)BlockPos.betweenClosed(minX, level.getMinY(), minZ, maxX, level.getMaxY(), maxZ)) {
/* 143 */       BlockState state = level.getBlockState(pos);
/* 144 */       BlockState newState = state;
/*     */       
/* 146 */       for (Direction direction : updateDirections) {
/* 147 */         neighbourPos.setWithOffset((Vec3i)pos, direction);
/* 148 */         newState = updateState(newState, direction, (LevelAccessor)level, pos, (BlockPos)neighbourPos);
/*     */       } 
/*     */       
/* 151 */       Block.updateOrDestroy(state, newState, (LevelAccessor)level, pos, 18);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static BlockState updateState(BlockState state, Direction direction, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
/* 156 */     return ((BlockFixer)MAP.getOrDefault(state.getBlock(), BlockFixers.DEFAULT)).updateShape(state, direction, level.getBlockState(neighbourPos), level, pos, neighbourPos);
/*     */   }
/*     */ 
/*     */   
/*     */   private void upgradeInside(LevelChunk chunk) {
/* 161 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 162 */     BlockPos.MutableBlockPos neighbourPos = new BlockPos.MutableBlockPos();
/*     */     
/* 164 */     ChunkPos chunkPos = chunk.getPos();
/* 165 */     Level level = chunk.getLevel();
/* 166 */     for (int sectionIndex = 0; sectionIndex < this.index.length; sectionIndex++) {
/* 167 */       LevelChunkSection chunkSection = chunk.getSection(sectionIndex);
/* 168 */       int[] upgradeIndex = this.index[sectionIndex];
/* 169 */       this.index[sectionIndex] = null;
/*     */       
/* 171 */       if (upgradeIndex != null && upgradeIndex.length > 0) {
/*     */ 
/*     */ 
/*     */         
/* 175 */         Direction[] directions = Direction.values();
/* 176 */         PalettedContainer<BlockState> states = chunkSection.getStates();
/*     */         
/* 178 */         int sectionY = chunk.getSectionYFromSectionIndex(sectionIndex);
/* 179 */         int bottomYInSection = SectionPos.sectionToBlockCoord(sectionY);
/* 180 */         for (int coordinate : upgradeIndex) {
/* 181 */           int x = coordinate & 0xF;
/* 182 */           int y = coordinate >> 8 & 0xF;
/* 183 */           int z = coordinate >> 4 & 0xF;
/*     */           
/* 185 */           pos.set(chunkPos.getMinBlockX() + x, bottomYInSection + y, chunkPos.getMinBlockZ() + z);
/*     */           
/* 187 */           BlockState state = states.get(coordinate);
/* 188 */           BlockState newState = state;
/*     */           
/* 190 */           for (Direction direction : directions) {
/* 191 */             neighbourPos.setWithOffset((Vec3i)pos, direction);
/* 192 */             if (SectionPos.blockToSectionCoord(pos.getX()) == chunkPos.x && SectionPos.blockToSectionCoord(pos.getZ()) == chunkPos.z)
/*     */             {
/*     */               
/* 195 */               newState = updateState(newState, direction, (LevelAccessor)level, (BlockPos)pos, (BlockPos)neighbourPos); } 
/*     */           } 
/* 197 */           Block.updateOrDestroy(state, newState, (LevelAccessor)level, (BlockPos)pos, 18);
/*     */         } 
/*     */       } 
/* 200 */     }  for (int i = 0; i < this.index.length; i++) {
/* 201 */       if (this.index[i] != null) {
/* 202 */         LOGGER.warn("Discarding update data for section {} for chunk ({} {})", new Object[] { level.getSectionYFromSectionIndex(i), chunkPos.x, chunkPos.z });
/*     */       }
/* 204 */       this.index[i] = null;
/*     */     } 
/*     */   }
/*     */   
/* 208 */   private static final Map<Block, BlockFixer> MAP = new java.util.IdentityHashMap<>();
/* 209 */   private static final Set<BlockFixer> CHUNKY_FIXERS = com.google.common.collect.Sets.newHashSet();
/*     */   
/*     */   public boolean isEmpty() {
/* 212 */     for (int[] ints : this.index) {
/* 213 */       if (ints != null) {
/* 214 */         return false;
/*     */       }
/*     */     } 
/* 217 */     return this.sides.isEmpty();
/*     */   }
/*     */   
/*     */   public static interface BlockFixer {
/*     */     BlockState updateShape(BlockState param1BlockState1, Direction param1Direction, BlockState param1BlockState2, LevelAccessor param1LevelAccessor, BlockPos param1BlockPos1, BlockPos param1BlockPos2);
/*     */     
/*     */     default void processChunk(LevelAccessor level) {}
/*     */   }
/*     */   
/*     */   private enum BlockFixers
/*     */     implements BlockFixer {
/* 228 */     BLACKLIST(new Block[] { Blocks.OBSERVER, Blocks.NETHER_PORTAL, Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER, Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER, Blocks.ANVIL, Blocks.CHIPPED_ANVIL, Blocks.DAMAGED_ANVIL, Blocks.DRAGON_EGG, Blocks.GRAVEL, Blocks.SAND, Blocks.RED_SAND, Blocks.OAK_SIGN, Blocks.SPRUCE_SIGN, Blocks.BIRCH_SIGN, Blocks.ACACIA_SIGN, Blocks.CHERRY_SIGN, Blocks.JUNGLE_SIGN, Blocks.DARK_OAK_SIGN, Blocks.PALE_OAK_SIGN, Blocks.OAK_WALL_SIGN, Blocks.SPRUCE_WALL_SIGN, Blocks.BIRCH_WALL_SIGN, Blocks.ACACIA_WALL_SIGN, Blocks.JUNGLE_WALL_SIGN, Blocks.DARK_OAK_WALL_SIGN, Blocks.PALE_OAK_WALL_SIGN, Blocks.OAK_HANGING_SIGN, Blocks.SPRUCE_HANGING_SIGN, Blocks.BIRCH_HANGING_SIGN, Blocks.ACACIA_HANGING_SIGN, Blocks.JUNGLE_HANGING_SIGN, Blocks.DARK_OAK_HANGING_SIGN, Blocks.PALE_OAK_HANGING_SIGN, Blocks.OAK_WALL_HANGING_SIGN, Blocks.SPRUCE_WALL_HANGING_SIGN, Blocks.BIRCH_WALL_HANGING_SIGN, Blocks.ACACIA_WALL_HANGING_SIGN, Blocks.JUNGLE_WALL_HANGING_SIGN, Blocks.DARK_OAK_WALL_HANGING_SIGN, Blocks.PALE_OAK_WALL_HANGING_SIGN })
/*     */     {
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
/*     */       public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos)
/*     */       {
/* 288 */         return state;
/*     */       }
/*     */     },
/* 291 */     DEFAULT(new Block[0])
/*     */     {
/*     */       public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
/* 294 */         return state.updateShape((LevelReader)level, (ScheduledTickAccess)level, pos, direction, neighbourPos, level.getBlockState(neighbourPos), level.getRandom());
/*     */       }
/*     */     },
/* 297 */     CHEST(new Block[] { Blocks.CHEST, Blocks.TRAPPED_CHEST })
/*     */     {
/*     */       public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
/* 300 */         if (neighbour.is(state.getBlock()) && direction.getAxis().isHorizontal() && 
/* 301 */           state.getValue((Property)ChestBlock.TYPE) == ChestType.SINGLE && neighbour.getValue((Property)ChestBlock.TYPE) == ChestType.SINGLE) {
/* 302 */           Direction facing = (Direction)state.getValue((Property)ChestBlock.FACING);
/* 303 */           if (direction.getAxis() != facing.getAxis() && facing == neighbour.getValue((Property)ChestBlock.FACING)) {
/* 304 */             ChestType newType = (direction == facing.getClockWise()) ? ChestType.LEFT : ChestType.RIGHT;
/* 305 */             level.setBlock(neighbourPos, (BlockState)neighbour.setValue((Property)ChestBlock.TYPE, (Comparable)newType.getOpposite()), 18);
/*     */ 
/*     */             
/* 308 */             if (facing == Direction.NORTH || facing == Direction.EAST) {
/* 309 */               BlockEntity one = level.getBlockEntity(pos);
/* 310 */               BlockEntity two = level.getBlockEntity(neighbourPos);
/* 311 */               if (one instanceof ChestBlockEntity && two instanceof ChestBlockEntity) {
/* 312 */                 ChestBlockEntity.swapContents((ChestBlockEntity)one, (ChestBlockEntity)two);
/*     */               }
/*     */             } 
/*     */             
/* 316 */             return (BlockState)state.setValue((Property)ChestBlock.TYPE, (Comparable)newType);
/*     */           } 
/*     */         } 
/*     */         
/* 320 */         return state;
/*     */       }
/*     */     },
/*     */     
/* 324 */     LEAVES(true, new Block[] { Blocks.ACACIA_LEAVES, Blocks.CHERRY_LEAVES, Blocks.BIRCH_LEAVES, Blocks.PALE_OAK_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES })
/*     */     {
/*     */       private final ThreadLocal<List<ObjectSet<BlockPos>>> queue;
/*     */       
/*     */       public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
/* 329 */         BlockState newState = state.updateShape((LevelReader)level, (ScheduledTickAccess)level, pos, direction, neighbourPos, level.getBlockState(neighbourPos), level.getRandom());
/* 330 */         if (state != newState) {
/* 331 */           int distance = (Integer)newState.getValue((Property)BlockStateProperties.DISTANCE);
/* 332 */           List<ObjectSet<BlockPos>> queue = this.queue.get();
/* 333 */           if (queue.isEmpty()) {
/* 334 */             for (int i = 0; i < 7; i++) {
/* 335 */               queue.add(new ObjectOpenHashSet());
/*     */             }
/*     */           }
/* 338 */           ((ObjectSet)queue.get(distance)).add(pos.immutable());
/*     */         } 
/* 340 */         return state;
/*     */       }
/*     */ 
/*     */       
/*     */       public void processChunk(LevelAccessor level) {
/* 345 */         BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
/*     */         
/* 347 */         List<ObjectSet<BlockPos>> queue = this.queue.get();
/* 348 */         for (int neighborDistance = 2; neighborDistance < queue.size(); neighborDistance++) {
/* 349 */           int currentDistance = neighborDistance - 1;
/* 350 */           ObjectSet<BlockPos> set = queue.get(currentDistance);
/* 351 */           ObjectSet<BlockPos> newSet = queue.get(neighborDistance);
/*     */           
/* 353 */           for (ObjectIterator<BlockPos> objectIterator = set.iterator(); objectIterator.hasNext(); ) { BlockPos pos = objectIterator.next();
/* 354 */             BlockState state = level.getBlockState(pos);
/* 355 */             if ((Integer)state.getValue((Property)BlockStateProperties.DISTANCE) < currentDistance) {
/*     */               continue;
/*     */             }
/*     */             
/* 359 */             level.setBlock(pos, (BlockState)state.setValue((Property)BlockStateProperties.DISTANCE, currentDistance), 18);
/*     */             
/* 361 */             if (neighborDistance != 7) {
/* 362 */               for (Direction direction : DIRECTIONS) {
/* 363 */                 neighborPos.setWithOffset((Vec3i)pos, direction);
/* 364 */                 BlockState neighbor = level.getBlockState((BlockPos)neighborPos);
/*     */                 
/* 366 */                 if (neighbor.hasProperty((Property)BlockStateProperties.DISTANCE) && (Integer)state.getValue((Property)BlockStateProperties.DISTANCE) > neighborDistance) {
/* 367 */                   newSet.add(neighborPos.immutable());
/*     */                 }
/*     */               } 
/*     */             } }
/*     */         
/*     */         } 
/*     */         
/* 374 */         queue.clear();
/*     */       }
/*     */     },
/* 377 */     STEM_BLOCK(new Block[] { Blocks.MELON_STEM, Blocks.PUMPKIN_STEM })
/*     */     {
/*     */       public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
/* 380 */         if ((Integer)state.getValue((Property)net.minecraft.world.level.block.StemBlock.AGE) == 7) {
/* 381 */           Block fruit = state.is(Blocks.PUMPKIN_STEM) ? Blocks.PUMPKIN : Blocks.MELON;
/* 382 */           if (neighbour.is(fruit)) {
/* 383 */             return (BlockState)(state.is(Blocks.PUMPKIN_STEM) ? Blocks.ATTACHED_PUMPKIN_STEM : Blocks.ATTACHED_MELON_STEM).defaultBlockState().setValue((Property)HorizontalDirectionalBlock.FACING, (Comparable)direction);
/*     */           }
/*     */         } 
/* 386 */         return state;
/*     */       }
/*     */     };
/*     */ 
/*     */     
/* 391 */     public static final Direction[] DIRECTIONS = Direction.values();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     BlockFixers(boolean chunky, Block... blocks) {
/* 398 */       for (Block block : blocks) {
/* 399 */         UpgradeData.MAP.put(block, this);
/*     */       }
/* 401 */       if (chunky)
/* 402 */         UpgradeData.CHUNKY_FIXERS.add(this); 
/*     */     } } enum null {
/*     */     public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
/*     */       return state;
/*     */     }
/*     */   }
/* 408 */   public CompoundTag write() { CompoundTag tag = new CompoundTag();
/*     */     
/* 410 */     CompoundTag indicesTag = new CompoundTag();
/* 411 */     for (int i = 0; i < this.index.length; i++) {
/* 412 */       String key = String.valueOf(i);
/* 413 */       if (this.index[i] != null && (this.index[i]).length != 0) {
/* 414 */         indicesTag.putIntArray(key, this.index[i]);
/*     */       }
/*     */     } 
/* 417 */     if (!indicesTag.isEmpty()) {
/* 418 */       tag.put("Indices", (net.minecraft.nbt.Tag)indicesTag);
/*     */     }
/*     */     
/* 421 */     int sides = 0;
/* 422 */     for (Direction8 side : this.sides) {
/* 423 */       sides |= 1 << side.ordinal();
/*     */     }
/* 425 */     tag.putByte("Sides", (byte)sides);
/*     */     
/* 427 */     if (!this.neighborBlockTicks.isEmpty()) {
/* 428 */       tag.store("neighbor_block_ticks", BLOCK_TICKS_CODEC, this.neighborBlockTicks);
/*     */     }
/* 430 */     if (!this.neighborFluidTicks.isEmpty()) {
/* 431 */       tag.store("neighbor_fluid_ticks", FLUID_TICKS_CODEC, this.neighborFluidTicks);
/*     */     }
/*     */     
/* 434 */     return tag; } enum null {
/*     */     public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
/*     */       return state.updateShape((LevelReader)level, (ScheduledTickAccess)level, pos, direction, neighbourPos, level.getBlockState(neighbourPos), level.getRandom());
/*     */     } } public UpgradeData copy() {
/* 438 */     if (this == EMPTY) {
/* 439 */       return EMPTY;
/*     */     }
/* 441 */     return new UpgradeData(this);
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
/*     */       if (neighbour.is(state.getBlock()) && direction.getAxis().isHorizontal() && state.getValue((Property)ChestBlock.TYPE) == ChestType.SINGLE && neighbour.getValue((Property)ChestBlock.TYPE) == ChestType.SINGLE) {
/*     */         Direction facing = (Direction)state.getValue((Property)ChestBlock.FACING);
/*     */         if (direction.getAxis() != facing.getAxis() && facing == neighbour.getValue((Property)ChestBlock.FACING)) {
/*     */           ChestType newType = (direction == facing.getClockWise()) ? ChestType.LEFT : ChestType.RIGHT;
/*     */           level.setBlock(neighbourPos, (BlockState)neighbour.setValue((Property)ChestBlock.TYPE, (Comparable)newType.getOpposite()), 18);
/*     */           if (facing == Direction.NORTH || facing == Direction.EAST) {
/*     */             BlockEntity one = level.getBlockEntity(pos);
/*     */             BlockEntity two = level.getBlockEntity(neighbourPos);
/*     */             if (one instanceof ChestBlockEntity && two instanceof ChestBlockEntity)
/*     */               ChestBlockEntity.swapContents((ChestBlockEntity)one, (ChestBlockEntity)two); 
/*     */           } 
/*     */           return (BlockState)state.setValue((Property)ChestBlock.TYPE, (Comparable)newType);
/*     */         } 
/*     */       } 
/*     */       return state;
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     private final ThreadLocal<List<ObjectSet<BlockPos>>> queue;
/*     */     
/*     */     null(boolean chunky, Block... blocks) {
/*     */       this.queue = ThreadLocal.withInitial(() -> Lists.newArrayListWithCapacity(7));
/*     */     }
/*     */     
/*     */     public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
/*     */       BlockState newState = state.updateShape((LevelReader)level, (ScheduledTickAccess)level, pos, direction, neighbourPos, level.getBlockState(neighbourPos), level.getRandom());
/*     */       if (state != newState) {
/*     */         int distance = (Integer)newState.getValue((Property)BlockStateProperties.DISTANCE);
/*     */         List<ObjectSet<BlockPos>> queue = this.queue.get();
/*     */         if (queue.isEmpty())
/*     */           for (int i = 0; i < 7; i++)
/*     */             queue.add(new ObjectOpenHashSet());  
/*     */         ((ObjectSet)queue.get(distance)).add(pos.immutable());
/*     */       } 
/*     */       return state;
/*     */     }
/*     */     
/*     */     public void processChunk(LevelAccessor level) {
/*     */       BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
/*     */       List<ObjectSet<BlockPos>> queue = this.queue.get();
/*     */       for (int neighborDistance = 2; neighborDistance < queue.size(); neighborDistance++) {
/*     */         int currentDistance = neighborDistance - 1;
/*     */         ObjectSet<BlockPos> set = queue.get(currentDistance);
/*     */         ObjectSet<BlockPos> newSet = queue.get(neighborDistance);
/*     */         for (ObjectIterator<BlockPos> objectIterator = set.iterator(); objectIterator.hasNext(); ) {
/*     */           BlockPos pos = objectIterator.next();
/*     */           BlockState state = level.getBlockState(pos);
/*     */           if ((Integer)state.getValue((Property)BlockStateProperties.DISTANCE) < currentDistance)
/*     */             continue; 
/*     */           level.setBlock(pos, (BlockState)state.setValue((Property)BlockStateProperties.DISTANCE, currentDistance), 18);
/*     */           if (neighborDistance != 7)
/*     */             for (Direction direction : DIRECTIONS) {
/*     */               neighborPos.setWithOffset((Vec3i)pos, direction);
/*     */               BlockState neighbor = level.getBlockState((BlockPos)neighborPos);
/*     */               if (neighbor.hasProperty((Property)BlockStateProperties.DISTANCE) && (Integer)state.getValue((Property)BlockStateProperties.DISTANCE) > neighborDistance)
/*     */                 newSet.add(neighborPos.immutable()); 
/*     */             }  
/*     */         } 
/*     */       } 
/*     */       queue.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
/*     */       if ((Integer)state.getValue((Property)net.minecraft.world.level.block.StemBlock.AGE) == 7) {
/*     */         Block fruit = state.is(Blocks.PUMPKIN_STEM) ? Blocks.PUMPKIN : Blocks.MELON;
/*     */         if (neighbour.is(fruit))
/*     */           return (BlockState)(state.is(Blocks.PUMPKIN_STEM) ? Blocks.ATTACHED_PUMPKIN_STEM : Blocks.ATTACHED_MELON_STEM).defaultBlockState().setValue((Property)HorizontalDirectionalBlock.FACING, (Comparable)direction); 
/*     */       } 
/*     */       return state;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/UpgradeData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */