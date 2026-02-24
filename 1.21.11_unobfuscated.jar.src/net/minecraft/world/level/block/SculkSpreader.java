/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SculkSpreader
/*     */ {
/*     */   public static final int MAX_GROWTH_RATE_RADIUS = 24;
/*     */   public static final int MAX_CHARGE = 1000;
/*     */   public static final float MAX_DECAY_FACTOR = 0.5F;
/*     */   private static final int MAX_CURSORS = 32;
/*     */   public static final int SHRIEKER_PLACEMENT_RATE = 11;
/*     */   public static final int MAX_CURSOR_DISTANCE = 1024;
/*     */   private final boolean isWorldGeneration;
/*     */   private final TagKey<Block> replaceableBlocks;
/*     */   private final int growthSpawnCost;
/*     */   private final int noGrowthRadius;
/*     */   private final int chargeDecayRate;
/*     */   private final int additionalDecayRate;
/*  61 */   private List<ChargeCursor> cursors = new ArrayList<>();
/*     */   
/*     */   public SculkSpreader(boolean isWorldGeneration, TagKey<Block> replaceableBlocks, int growthSpawnCost, int noGrowthRadius, int chargeDecayRate, int additionalDecayRate) {
/*  64 */     this.isWorldGeneration = isWorldGeneration;
/*  65 */     this.replaceableBlocks = replaceableBlocks;
/*  66 */     this.growthSpawnCost = growthSpawnCost;
/*  67 */     this.noGrowthRadius = noGrowthRadius;
/*  68 */     this.chargeDecayRate = chargeDecayRate;
/*  69 */     this.additionalDecayRate = additionalDecayRate;
/*     */   }
/*     */   
/*     */   public static SculkSpreader createLevelSpreader() {
/*  73 */     return new SculkSpreader(false, BlockTags.SCULK_REPLACEABLE, 10, 4, 10, 5);
/*     */   }
/*     */   
/*     */   public static SculkSpreader createWorldGenSpreader() {
/*  77 */     return new SculkSpreader(true, BlockTags.SCULK_REPLACEABLE_WORLD_GEN, 50, 1, 5, 10);
/*     */   }
/*     */   
/*     */   public TagKey<Block> replaceableBlocks() {
/*  81 */     return this.replaceableBlocks;
/*     */   }
/*     */   
/*     */   public int growthSpawnCost() {
/*  85 */     return this.growthSpawnCost;
/*     */   }
/*     */   
/*     */   public int noGrowthRadius() {
/*  89 */     return this.noGrowthRadius;
/*     */   }
/*     */   
/*     */   public int chargeDecayRate() {
/*  93 */     return this.chargeDecayRate;
/*     */   }
/*     */   
/*     */   public int additionalDecayRate() {
/*  97 */     return this.additionalDecayRate;
/*     */   }
/*     */   
/*     */   public boolean isWorldGeneration() {
/* 101 */     return this.isWorldGeneration;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public List<ChargeCursor> getCursors() {
/* 106 */     return this.cursors;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 110 */     this.cursors.clear();
/*     */   }
/*     */   
/*     */   public void load(ValueInput input) {
/* 114 */     this.cursors.clear();
/* 115 */     ((List)input.read("cursors", ChargeCursor.CODEC.sizeLimitedListOf(32)).orElse(List.of()))
/* 116 */       .forEach(this::addCursor);
/*     */   }
/*     */   
/*     */   public void save(ValueOutput output) {
/* 120 */     output.store("cursors", ChargeCursor.CODEC.listOf(), this.cursors);
/*     */     
/* 122 */     if (SharedConstants.DEBUG_SCULK_CATALYST) {
/* 123 */       int charge = (Integer)getCursors().stream().map(ChargeCursor::getCharge).reduce(0, Integer::sum);
/* 124 */       int charges = (Integer)getCursors().stream().map(c -> 1).reduce(0, Integer::sum);
/* 125 */       int max = (Integer)getCursors().stream().map(ChargeCursor::getCharge).reduce(0, Math::max);
/* 126 */       output.putInt("stats.total", charge);
/* 127 */       output.putInt("stats.count", charges);
/* 128 */       output.putInt("stats.max", max);
/* 129 */       output.putInt("stats.avg", charge / (charges + 1));
/*     */     } 
/*     */   }
/*     */   public static class ChargeCursor { private static final ObjectArrayList<Vec3i> NON_CORNER_NEIGHBOURS; public static final int MAX_CURSOR_DECAY_DELAY = 1;
/*     */     static {
/* 134 */       NON_CORNER_NEIGHBOURS = (ObjectArrayList<Vec3i>)Util.make(new ObjectArrayList(18), list -> {
/*     */             Objects.requireNonNull(list);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             BlockPos.betweenClosedStream(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1)).filter(()).map(BlockPos::immutable).forEach(list::add);
/*     */           });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 151 */       DIRECTION_SET = Direction.CODEC.listOf().xmap(l -> Sets.newEnumSet(l, Direction.class), Lists::newArrayList);
/*     */       
/* 153 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockPos.CODEC.fieldOf("pos").forGetter(ChargeCursor::getPos), (App)Codec.intRange(0, 1000).fieldOf("charge").orElse(0).forGetter(ChargeCursor::getCharge), (App)Codec.intRange(0, 1).fieldOf("decay_delay").orElse(1).forGetter(ChargeCursor::getDecayDelay), (App)Codec.intRange(0, Integer.MAX_VALUE).fieldOf("update_delay").orElse(0).forGetter(()), (App)DIRECTION_SET.lenientOptionalFieldOf("facings").forGetter(())).apply((Applicative)i, ChargeCursor::new));
/*     */     }
/*     */     private BlockPos pos; private int charge; private int updateDelay;
/*     */     private int decayDelay;
/*     */     private Set<Direction> facings;
/*     */     private static final Codec<Set<Direction>> DIRECTION_SET;
/*     */     public static final Codec<ChargeCursor> CODEC;
/*     */     
/*     */     private ChargeCursor(BlockPos pos, int charge, int decayDelay, int updateDelay, Optional<Set<Direction>> facings) {
/* 162 */       this.pos = pos;
/* 163 */       this.charge = charge;
/* 164 */       this.decayDelay = decayDelay;
/* 165 */       this.updateDelay = updateDelay;
/* 166 */       this.facings = facings.orElse(null);
/*     */     }
/*     */     
/*     */     public ChargeCursor(BlockPos pos, int charge) {
/* 170 */       this(pos, charge, 1, 0, Optional.empty());
/*     */     }
/*     */     
/*     */     public BlockPos getPos() {
/* 174 */       return this.pos;
/*     */     }
/*     */     
/*     */     private boolean isPosUnreasonable(BlockPos originPos) {
/* 178 */       return (this.pos.distChessboard((Vec3i)originPos) > 1024);
/*     */     }
/*     */     
/*     */     public int getCharge() {
/* 182 */       return this.charge;
/*     */     }
/*     */     
/*     */     public int getDecayDelay() {
/* 186 */       return this.decayDelay;
/*     */     }
/*     */     
/*     */     public Set<Direction> getFacingData() {
/* 190 */       return this.facings;
/*     */     }
/*     */     
/*     */     private boolean shouldUpdate(LevelAccessor level, BlockPos pos, boolean isWorldGen) {
/* 194 */       if (this.charge <= 0) {
/* 195 */         return false;
/*     */       }
/* 197 */       if (isWorldGen) {
/* 198 */         return true;
/*     */       }
/* 200 */       if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 201 */         return serverLevel.shouldTickBlocksAt(pos); }
/*     */       
/* 203 */       return false;
/*     */     }
/*     */     
/*     */     public void update(LevelAccessor level, BlockPos originPos, RandomSource random, SculkSpreader spreader, boolean spreadVeins) {
/* 207 */       if (!shouldUpdate(level, originPos, spreader.isWorldGeneration)) {
/*     */         return;
/*     */       }
/*     */       
/* 211 */       if (this.updateDelay > 0) {
/* 212 */         this.updateDelay--;
/*     */         
/*     */         return;
/*     */       } 
/* 216 */       BlockState currentState = level.getBlockState(this.pos);
/* 217 */       SculkBehaviour sculkBehaviour = getBlockBehaviour(currentState);
/*     */ 
/*     */       
/* 220 */       if (spreadVeins && sculkBehaviour.attemptSpreadVein(level, this.pos, currentState, this.facings, spreader.isWorldGeneration())) {
/* 221 */         if (sculkBehaviour.canChangeBlockStateOnSpread()) {
/* 222 */           currentState = level.getBlockState(this.pos);
/* 223 */           sculkBehaviour = getBlockBehaviour(currentState);
/*     */         } 
/* 225 */         level.playSound(null, this.pos, SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */       } 
/*     */ 
/*     */       
/* 229 */       this.charge = sculkBehaviour.attemptUseCharge(this, level, originPos, random, spreader, spreadVeins);
/*     */       
/* 231 */       if (this.charge <= 0) {
/* 232 */         sculkBehaviour.onDischarged(level, currentState, this.pos, random);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 237 */       BlockPos transferPos = getValidMovementPos(level, this.pos, random);
/* 238 */       if (transferPos != null) {
/* 239 */         sculkBehaviour.onDischarged(level, currentState, this.pos, random);
/* 240 */         this.pos = transferPos.immutable();
/* 241 */         if (spreader.isWorldGeneration() && !this.pos.closerThan(new Vec3i(originPos.getX(), this.pos.getY(), originPos.getZ()), 15.0D)) {
/* 242 */           this.charge = 0;
/*     */           return;
/*     */         } 
/* 245 */         currentState = level.getBlockState(transferPos);
/*     */       } 
/*     */       
/* 248 */       if (currentState.getBlock() instanceof SculkBehaviour) {
/* 249 */         this.facings = MultifaceBlock.availableFaces(currentState);
/*     */       }
/* 251 */       this.decayDelay = sculkBehaviour.updateDecayDelay(this.decayDelay);
/* 252 */       this.updateDelay = sculkBehaviour.getSculkSpreadDelay();
/*     */     }
/*     */     
/*     */     private void mergeWith(ChargeCursor other) {
/* 256 */       this.charge += other.charge;
/* 257 */       other.charge = 0;
/* 258 */       this.updateDelay = Math.min(this.updateDelay, other.updateDelay);
/*     */     }
/*     */     
/*     */     private static SculkBehaviour getBlockBehaviour(BlockState state) {
/* 262 */       Block block = state.getBlock(); SculkBehaviour behaviour = (SculkBehaviour)block; return (block instanceof SculkBehaviour) ? behaviour : SculkBehaviour.DEFAULT;
/*     */     }
/*     */     
/*     */     private static List<Vec3i> getRandomizedNonCornerNeighbourOffsets(RandomSource random) {
/* 266 */       return Util.shuffledCopy(NON_CORNER_NEIGHBOURS, random);
/*     */     }
/*     */     
/*     */     private static BlockPos getValidMovementPos(LevelAccessor level, BlockPos pos, RandomSource random) {
/* 270 */       BlockPos.MutableBlockPos sculkPosition = pos.mutable();
/* 271 */       BlockPos.MutableBlockPos neighbour = pos.mutable();
/*     */       
/* 273 */       for (Vec3i offset : getRandomizedNonCornerNeighbourOffsets(random)) {
/* 274 */         neighbour.setWithOffset((Vec3i)pos, offset);
/* 275 */         BlockState transferee = level.getBlockState((BlockPos)neighbour);
/*     */         
/* 277 */         if (transferee.getBlock() instanceof SculkBehaviour && isMovementUnobstructed(level, pos, (BlockPos)neighbour)) {
/* 278 */           sculkPosition.set((Vec3i)neighbour);
/*     */           
/* 280 */           if (SculkVeinBlock.hasSubstrateAccess(level, transferee, (BlockPos)neighbour)) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/* 285 */       return sculkPosition.equals(pos) ? null : (BlockPos)sculkPosition;
/*     */     }
/*     */     
/*     */     private static boolean isMovementUnobstructed(LevelAccessor level, BlockPos from, BlockPos to) {
/* 289 */       if (from.distManhattan((Vec3i)to) == 1) {
/* 290 */         return true;
/*     */       }
/*     */ 
/*     */       
/* 294 */       BlockPos delta = to.subtract((Vec3i)from);
/* 295 */       Direction directionX = Direction.fromAxisAndDirection(Direction.Axis.X, (delta.getX() < 0) ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
/* 296 */       Direction directionY = Direction.fromAxisAndDirection(Direction.Axis.Y, (delta.getY() < 0) ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
/* 297 */       Direction directionZ = Direction.fromAxisAndDirection(Direction.Axis.Z, (delta.getZ() < 0) ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
/*     */       
/* 299 */       if (delta.getX() == 0)
/* 300 */         return (isUnobstructed(level, from, directionY) || isUnobstructed(level, from, directionZ)); 
/* 301 */       if (delta.getY() == 0) {
/* 302 */         return (isUnobstructed(level, from, directionX) || isUnobstructed(level, from, directionZ));
/*     */       }
/* 304 */       return (isUnobstructed(level, from, directionX) || isUnobstructed(level, from, directionY));
/*     */     }
/*     */ 
/*     */     
/*     */     private static boolean isUnobstructed(LevelAccessor level, BlockPos from, Direction direction) {
/* 309 */       BlockPos testPos = from.relative(direction);
/* 310 */       return !level.getBlockState(testPos).isFaceSturdy((BlockGetter)level, testPos, direction.getOpposite());
/*     */     } }
/*     */ 
/*     */   
/*     */   public void addCursors(BlockPos startPos, int charge) {
/* 315 */     while (charge > 0) {
/* 316 */       int currentCharge = Math.min(charge, 1000);
/* 317 */       addCursor(new ChargeCursor(startPos, currentCharge));
/* 318 */       charge -= currentCharge;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addCursor(ChargeCursor cursor) {
/* 323 */     if (this.cursors.size() >= 32) {
/*     */       return;
/*     */     }
/* 326 */     this.cursors.add(cursor);
/*     */   }
/*     */   
/*     */   public void updateCursors(LevelAccessor level, BlockPos originPos, RandomSource random, boolean spreadVeins) {
/* 330 */     if (this.cursors.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 334 */     List<ChargeCursor> processedCursors = new ArrayList<>();
/* 335 */     Map<BlockPos, ChargeCursor> mergeableCursors = new HashMap<>();
/* 336 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/*     */     
/* 338 */     for (ChargeCursor cursor : this.cursors) {
/* 339 */       if (cursor.isPosUnreasonable(originPos)) {
/*     */         continue;
/*     */       }
/* 342 */       cursor.update(level, originPos, random, this, spreadVeins);
/*     */       
/* 344 */       if (cursor.charge <= 0) {
/* 345 */         level.levelEvent(3006, cursor.getPos(), 0);
/*     */         
/*     */         continue;
/*     */       } 
/* 349 */       BlockPos pos = cursor.getPos();
/* 350 */       object2IntOpenHashMap.computeInt(pos, (k, count) -> ((count == null) ? 0 : count) + cursor.charge);
/*     */       
/* 352 */       ChargeCursor existing = mergeableCursors.get(pos);
/* 353 */       if (existing == null) {
/* 354 */         mergeableCursors.put(pos, cursor);
/* 355 */         processedCursors.add(cursor);
/*     */         
/*     */         continue;
/*     */       } 
/* 359 */       if (!isWorldGeneration() && cursor.charge + existing.charge <= 1000) {
/* 360 */         existing.mergeWith(cursor);
/*     */         
/*     */         continue;
/*     */       } 
/* 364 */       processedCursors.add(cursor);
/*     */       
/* 366 */       if (cursor.charge < existing.charge) {
/* 367 */         mergeableCursors.put(pos, cursor);
/*     */       }
/*     */     } 
/*     */     
/* 371 */     for (ObjectIterator<Object2IntMap.Entry<BlockPos>> objectIterator = object2IntOpenHashMap.object2IntEntrySet().iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry<BlockPos> entry = objectIterator.next();
/* 372 */       BlockPos pos = (BlockPos)entry.getKey();
/* 373 */       int charge = entry.getIntValue();
/*     */       
/* 375 */       ChargeCursor cursor = mergeableCursors.get(pos);
/* 376 */       Collection<Direction> faces = (cursor == null) ? null : cursor.getFacingData();
/*     */       
/* 378 */       if (charge > 0 && faces != null) {
/* 379 */         int numParticles = (int)(Math.log1p(charge) / 2.299999952316284D) + 1;
/* 380 */         int data = (numParticles << 6) + MultifaceBlock.pack(faces);
/* 381 */         level.levelEvent(3006, pos, data);
/*     */       }  }
/*     */     
/* 384 */     this.cursors = processedCursors;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SculkSpreader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */