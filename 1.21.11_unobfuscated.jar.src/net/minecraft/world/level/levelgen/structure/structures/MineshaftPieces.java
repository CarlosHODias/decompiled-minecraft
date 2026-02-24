/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.vehicle.minecart.MinecartChest;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.FenceBlock;
/*     */ import net.minecraft.world.level.block.RailBlock;
/*     */ import net.minecraft.world.level.block.WallTorchBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public class MineshaftPieces
/*     */ {
/*     */   private static final int DEFAULT_SHAFT_WIDTH = 3;
/*     */   private static final int DEFAULT_SHAFT_HEIGHT = 3;
/*     */   private static final int DEFAULT_SHAFT_LENGTH = 5;
/*     */   private static final int MAX_PILLAR_HEIGHT = 20;
/*     */   private static final int MAX_CHAIN_HEIGHT = 50;
/*     */   private static final int MAX_DEPTH = 8;
/*     */   public static final int MAGIC_START_Y = 50;
/*     */   
/*     */   private static abstract class MineShaftPiece
/*     */     extends StructurePiece {
/*     */     protected MineshaftStructure.Type type;
/*     */     
/*     */     public MineShaftPiece(StructurePieceType pieceType, int genDepth, MineshaftStructure.Type type, BoundingBox boundingBox) {
/*  57 */       super(pieceType, genDepth, boundingBox);
/*  58 */       this.type = type;
/*     */     }
/*     */     
/*     */     public MineShaftPiece(StructurePieceType type, CompoundTag tag) {
/*  62 */       super(type, tag);
/*  63 */       this.type = MineshaftStructure.Type.byId(tag.getIntOr("MST", 0));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean canBeReplaced(LevelReader level, int x, int y, int z, BoundingBox chunkBB) {
/*  69 */       BlockState state = getBlock((BlockGetter)level, x, y, z, chunkBB);
/*  70 */       return (!state.is(this.type.getPlanksState().getBlock()) && 
/*  71 */         !state.is(this.type.getWoodState().getBlock()) && 
/*  72 */         !state.is(this.type.getFenceState().getBlock()) && 
/*  73 */         !state.is(Blocks.IRON_CHAIN));
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  78 */       tag.putInt("MST", this.type.ordinal());
/*     */     }
/*     */     
/*     */     protected boolean isSupportingBox(BlockGetter level, BoundingBox chunkBB, int x0, int x1, int y1, int z0) {
/*  82 */       for (int x = x0; x <= x1; x++) {
/*  83 */         if (getBlock(level, x, y1 + 1, z0, chunkBB).isAir()) {
/*  84 */           return false;
/*     */         }
/*     */       } 
/*  87 */       return true;
/*     */     }
/*     */     
/*     */     protected boolean isInInvalidLocation(LevelAccessor level, BoundingBox chunkBB) {
/*  91 */       int x0 = Math.max(this.boundingBox.minX() - 1, chunkBB.minX());
/*  92 */       int y0 = Math.max(this.boundingBox.minY() - 1, chunkBB.minY());
/*  93 */       int z0 = Math.max(this.boundingBox.minZ() - 1, chunkBB.minZ());
/*  94 */       int x1 = Math.min(this.boundingBox.maxX() + 1, chunkBB.maxX());
/*  95 */       int y1 = Math.min(this.boundingBox.maxY() + 1, chunkBB.maxY());
/*  96 */       int z1 = Math.min(this.boundingBox.maxZ() + 1, chunkBB.maxZ());
/*     */       
/*  98 */       BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos((x0 + x1) / 2, (y0 + y1) / 2, (z0 + z1) / 2);
/*     */       
/* 100 */       if (level.getBiome((BlockPos)blockPos).is(BiomeTags.MINESHAFT_BLOCKING)) {
/* 101 */         return true;
/*     */       }
/*     */ 
/*     */       
/* 105 */       for (int x = x0; x <= x1; x++) {
/* 106 */         for (int j = z0; j <= z1; j++) {
/* 107 */           if (level.getBlockState((BlockPos)blockPos.set(x, y0, j)).liquid()) {
/* 108 */             return true;
/*     */           }
/* 110 */           if (level.getBlockState((BlockPos)blockPos.set(x, y1, j)).liquid()) {
/* 111 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 116 */       for (int i = x0; i <= x1; i++) {
/* 117 */         for (int y = y0; y <= y1; y++) {
/* 118 */           if (level.getBlockState((BlockPos)blockPos.set(i, y, z0)).liquid()) {
/* 119 */             return true;
/*     */           }
/* 121 */           if (level.getBlockState((BlockPos)blockPos.set(i, y, z1)).liquid()) {
/* 122 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 127 */       for (int z = z0; z <= z1; z++) {
/* 128 */         for (int y = y0; y <= y1; y++) {
/* 129 */           if (level.getBlockState((BlockPos)blockPos.set(x0, y, z)).liquid()) {
/* 130 */             return true;
/*     */           }
/* 132 */           if (level.getBlockState((BlockPos)blockPos.set(x1, y, z)).liquid()) {
/* 133 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/* 137 */       return false;
/*     */     }
/*     */     
/*     */     protected void setPlanksBlock(WorldGenLevel level, BoundingBox chunkBB, BlockState planksBlock, int x, int y, int z) {
/* 141 */       if (!isInterior((LevelReader)level, x, y, z, chunkBB)) {
/*     */         return;
/*     */       }
/* 144 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(x, y, z);
/* 145 */       BlockState existingState = level.getBlockState((BlockPos)mutableBlockPos);
/* 146 */       if (!existingState.isFaceSturdy((BlockGetter)level, (BlockPos)mutableBlockPos, Direction.UP))
/*     */       {
/* 148 */         level.setBlock((BlockPos)mutableBlockPos, planksBlock, 2);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static MineShaftPiece createRandomShaftPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth, MineshaftStructure.Type type) {
/* 154 */     int randomSelection = random.nextInt(100);
/* 155 */     if (randomSelection >= 80) {
/* 156 */       BoundingBox crossingBox = MineShaftCrossing.findCrossing(structurePieceAccessor, random, footX, footY, footZ, direction);
/* 157 */       if (crossingBox != null) {
/* 158 */         return new MineShaftCrossing(genDepth, crossingBox, direction, type);
/*     */       }
/* 160 */     } else if (randomSelection >= 70) {
/* 161 */       BoundingBox stairsBox = MineShaftStairs.findStairs(structurePieceAccessor, random, footX, footY, footZ, direction);
/* 162 */       if (stairsBox != null) {
/* 163 */         return new MineShaftStairs(genDepth, stairsBox, direction, type);
/*     */       }
/*     */     } else {
/* 166 */       BoundingBox corridorBox = MineShaftCorridor.findCorridorSize(structurePieceAccessor, random, footX, footY, footZ, direction);
/* 167 */       if (corridorBox != null) {
/* 168 */         return new MineShaftCorridor(genDepth, random, corridorBox, direction, type);
/*     */       }
/*     */     } 
/*     */     
/* 172 */     return null;
/*     */   }
/*     */   
/*     */   private static MineShaftPiece generateAndAddPiece(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int depth) {
/* 176 */     if (depth > 8) {
/* 177 */       return null;
/*     */     }
/* 179 */     if (Math.abs(footX - startPiece.getBoundingBox().minX()) > 80 || Math.abs(footZ - startPiece.getBoundingBox().minZ()) > 80) {
/* 180 */       return null;
/*     */     }
/*     */     
/* 183 */     MineshaftStructure.Type type = ((MineShaftPiece)startPiece).type;
/* 184 */     MineShaftPiece newPiece = createRandomShaftPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth + 1, type);
/* 185 */     if (newPiece != null) {
/* 186 */       structurePieceAccessor.addPiece(newPiece);
/* 187 */       newPiece.addChildren(startPiece, structurePieceAccessor, random);
/*     */     } 
/* 189 */     return newPiece;
/*     */   }
/*     */   
/*     */   public static class MineShaftRoom extends MineShaftPiece {
/* 193 */     private final List<BoundingBox> childEntranceBoxes = Lists.newLinkedList();
/*     */     
/*     */     public MineShaftRoom(int genDepth, RandomSource random, int west, int north, MineshaftStructure.Type type) {
/* 196 */       super(StructurePieceType.MINE_SHAFT_ROOM, genDepth, type, new BoundingBox(west, 50, north, west + 7 + random.nextInt(6), 54 + random.nextInt(6), north + 7 + random.nextInt(6)));
/* 197 */       this.type = type;
/*     */     }
/*     */     
/*     */     public MineShaftRoom(CompoundTag tag) {
/* 201 */       super(StructurePieceType.MINE_SHAFT_ROOM, tag);
/* 202 */       this.childEntranceBoxes.addAll(tag.read("Entrances", BoundingBox.CODEC.listOf()).orElse(List.of()));
/*     */     }
/*     */ 
/*     */     
/*     */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 207 */       int depth = getGenDepth();
/*     */ 
/*     */ 
/*     */       
/* 211 */       int heightSpace = this.boundingBox.getYSpan() - 3 - 1;
/* 212 */       if (heightSpace <= 0) {
/* 213 */         heightSpace = 1;
/*     */       }
/*     */ 
/*     */       
/* 217 */       int pos = 0;
/* 218 */       while (pos < this.boundingBox.getXSpan()) {
/* 219 */         pos += random.nextInt(this.boundingBox.getXSpan());
/* 220 */         if (pos + 3 > this.boundingBox.getXSpan()) {
/*     */           break;
/*     */         }
/* 223 */         MineshaftPieces.MineShaftPiece child = MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + pos, this.boundingBox.minY() + random.nextInt(heightSpace) + 1, this.boundingBox.minZ() - 1, Direction.NORTH, depth);
/* 224 */         if (child != null) {
/* 225 */           BoundingBox childBox = child.getBoundingBox();
/* 226 */           this.childEntranceBoxes.add(new BoundingBox(childBox.minX(), childBox.minY(), this.boundingBox.minZ(), childBox.maxX(), childBox.maxY(), this.boundingBox.minZ() + 1));
/*     */         } 
/* 228 */         pos += 4;
/*     */       } 
/*     */       
/* 231 */       pos = 0;
/* 232 */       while (pos < this.boundingBox.getXSpan()) {
/* 233 */         pos += random.nextInt(this.boundingBox.getXSpan());
/* 234 */         if (pos + 3 > this.boundingBox.getXSpan()) {
/*     */           break;
/*     */         }
/* 237 */         MineshaftPieces.MineShaftPiece child = MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + pos, this.boundingBox.minY() + random.nextInt(heightSpace) + 1, this.boundingBox.maxZ() + 1, Direction.SOUTH, depth);
/* 238 */         if (child != null) {
/* 239 */           BoundingBox childBox = child.getBoundingBox();
/* 240 */           this.childEntranceBoxes.add(new BoundingBox(childBox.minX(), childBox.minY(), this.boundingBox.maxZ() - 1, childBox.maxX(), childBox.maxY(), this.boundingBox.maxZ()));
/*     */         } 
/* 242 */         pos += 4;
/*     */       } 
/*     */       
/* 245 */       pos = 0;
/* 246 */       while (pos < this.boundingBox.getZSpan()) {
/* 247 */         pos += random.nextInt(this.boundingBox.getZSpan());
/* 248 */         if (pos + 3 > this.boundingBox.getZSpan()) {
/*     */           break;
/*     */         }
/* 251 */         MineshaftPieces.MineShaftPiece child = MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + random.nextInt(heightSpace) + 1, this.boundingBox.minZ() + pos, Direction.WEST, depth);
/* 252 */         if (child != null) {
/* 253 */           BoundingBox childBox = child.getBoundingBox();
/* 254 */           this.childEntranceBoxes.add(new BoundingBox(this.boundingBox.minX(), childBox.minY(), childBox.minZ(), this.boundingBox.minX() + 1, childBox.maxY(), childBox.maxZ()));
/*     */         } 
/* 256 */         pos += 4;
/*     */       } 
/*     */       
/* 259 */       pos = 0;
/* 260 */       while (pos < this.boundingBox.getZSpan()) {
/* 261 */         pos += random.nextInt(this.boundingBox.getZSpan());
/* 262 */         if (pos + 3 > this.boundingBox.getZSpan()) {
/*     */           break;
/*     */         }
/* 265 */         StructurePiece child = MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + random.nextInt(heightSpace) + 1, this.boundingBox.minZ() + pos, Direction.EAST, depth);
/* 266 */         if (child != null) {
/* 267 */           BoundingBox childBox = child.getBoundingBox();
/* 268 */           this.childEntranceBoxes.add(new BoundingBox(this.boundingBox.maxX() - 1, childBox.minY(), childBox.minZ(), this.boundingBox.maxX(), childBox.maxY(), childBox.maxZ()));
/*     */         } 
/* 270 */         pos += 4;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 276 */       if (isInInvalidLocation((LevelAccessor)level, chunkBB)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 281 */       generateBox(level, chunkBB, this.boundingBox.minX(), this.boundingBox.minY() + 1, this.boundingBox.minZ(), this.boundingBox.maxX(), Math.min(this.boundingBox.minY() + 3, this.boundingBox.maxY()), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/* 282 */       for (BoundingBox entranceBox : this.childEntranceBoxes) {
/* 283 */         generateBox(level, chunkBB, entranceBox.minX(), entranceBox.maxY() - 2, entranceBox.minZ(), entranceBox.maxX(), entranceBox.maxY(), entranceBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/*     */       }
/* 285 */       generateUpperHalfSphere(level, chunkBB, this.boundingBox.minX(), this.boundingBox.minY() + 4, this.boundingBox.minZ(), this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, false);
/*     */     }
/*     */ 
/*     */     
/*     */     public void move(int dx, int dy, int dz) {
/* 290 */       super.move(dx, dy, dz);
/* 291 */       for (BoundingBox bb : this.childEntranceBoxes) {
/* 292 */         bb.move(dx, dy, dz);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 298 */       super.addAdditionalSaveData(context, tag);
/* 299 */       tag.store("Entrances", BoundingBox.CODEC.listOf(), this.childEntranceBoxes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MineShaftCorridor extends MineShaftPiece {
/*     */     private final boolean hasRails;
/*     */     private final boolean spiderCorridor;
/*     */     private boolean hasPlacedSpider;
/*     */     private final int numSections;
/*     */     
/*     */     public MineShaftCorridor(CompoundTag tag) {
/* 310 */       super(StructurePieceType.MINE_SHAFT_CORRIDOR, tag);
/*     */       
/* 312 */       this.hasRails = tag.getBooleanOr("hr", false);
/* 313 */       this.spiderCorridor = tag.getBooleanOr("sc", false);
/* 314 */       this.hasPlacedSpider = tag.getBooleanOr("hps", false);
/* 315 */       this.numSections = tag.getIntOr("Num", 0);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 320 */       super.addAdditionalSaveData(context, tag);
/* 321 */       tag.putBoolean("hr", this.hasRails);
/* 322 */       tag.putBoolean("sc", this.spiderCorridor);
/* 323 */       tag.putBoolean("hps", this.hasPlacedSpider);
/* 324 */       tag.putInt("Num", this.numSections);
/*     */     }
/*     */     
/*     */     public MineShaftCorridor(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction, MineshaftStructure.Type type) {
/* 328 */       super(StructurePieceType.MINE_SHAFT_CORRIDOR, genDepth, type, boundingBox);
/* 329 */       setOrientation(direction);
/* 330 */       this.hasRails = (random.nextInt(3) == 0);
/* 331 */       this.spiderCorridor = (!this.hasRails && random.nextInt(23) == 0);
/*     */       
/* 333 */       if (getOrientation().getAxis() == Direction.Axis.Z) {
/* 334 */         this.numSections = boundingBox.getZSpan() / 5;
/*     */       } else {
/* 336 */         this.numSections = boundingBox.getXSpan() / 5;
/*     */       } 
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
/*     */     public static BoundingBox findCorridorSize(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction) {
/*     */       // Byte code:
/*     */       //   0: aload_1
/*     */       //   1: iconst_3
/*     */       //   2: invokeinterface nextInt : (I)I
/*     */       //   7: iconst_2
/*     */       //   8: iadd
/*     */       //   9: istore #6
/*     */       //   11: iload #6
/*     */       //   13: ifle -> 176
/*     */       //   16: iload #6
/*     */       //   18: iconst_5
/*     */       //   19: imul
/*     */       //   20: istore #8
/*     */       //   22: getstatic net/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$1.$SwitchMap$net$minecraft$core$Direction : [I
/*     */       //   25: aload #5
/*     */       //   27: invokevirtual ordinal : ()I
/*     */       //   30: iaload
/*     */       //   31: tableswitch default -> 60, 1 -> 60, 2 -> 82, 3 -> 103, 4 -> 125
/*     */       //   60: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   63: dup
/*     */       //   64: iconst_0
/*     */       //   65: iconst_0
/*     */       //   66: iload #8
/*     */       //   68: iconst_1
/*     */       //   69: isub
/*     */       //   70: ineg
/*     */       //   71: iconst_2
/*     */       //   72: iconst_2
/*     */       //   73: iconst_0
/*     */       //   74: invokespecial <init> : (IIIIII)V
/*     */       //   77: astore #7
/*     */       //   79: goto -> 143
/*     */       //   82: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   85: dup
/*     */       //   86: iconst_0
/*     */       //   87: iconst_0
/*     */       //   88: iconst_0
/*     */       //   89: iconst_2
/*     */       //   90: iconst_2
/*     */       //   91: iload #8
/*     */       //   93: iconst_1
/*     */       //   94: isub
/*     */       //   95: invokespecial <init> : (IIIIII)V
/*     */       //   98: astore #7
/*     */       //   100: goto -> 143
/*     */       //   103: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   106: dup
/*     */       //   107: iload #8
/*     */       //   109: iconst_1
/*     */       //   110: isub
/*     */       //   111: ineg
/*     */       //   112: iconst_0
/*     */       //   113: iconst_0
/*     */       //   114: iconst_0
/*     */       //   115: iconst_2
/*     */       //   116: iconst_2
/*     */       //   117: invokespecial <init> : (IIIIII)V
/*     */       //   120: astore #7
/*     */       //   122: goto -> 143
/*     */       //   125: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   128: dup
/*     */       //   129: iconst_0
/*     */       //   130: iconst_0
/*     */       //   131: iconst_0
/*     */       //   132: iload #8
/*     */       //   134: iconst_1
/*     */       //   135: isub
/*     */       //   136: iconst_2
/*     */       //   137: iconst_2
/*     */       //   138: invokespecial <init> : (IIIIII)V
/*     */       //   141: astore #7
/*     */       //   143: aload #7
/*     */       //   145: iload_2
/*     */       //   146: iload_3
/*     */       //   147: iload #4
/*     */       //   149: invokevirtual move : (III)Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   152: pop
/*     */       //   153: aload_0
/*     */       //   154: aload #7
/*     */       //   156: invokeinterface findCollisionPiece : (Lnet/minecraft/world/level/levelgen/structure/BoundingBox;)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */       //   161: ifnull -> 170
/*     */       //   164: iinc #6, -1
/*     */       //   167: goto -> 173
/*     */       //   170: aload #7
/*     */       //   172: areturn
/*     */       //   173: goto -> 11
/*     */       //   176: aconst_null
/*     */       //   177: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #341	-> 0
/*     */       //   #342	-> 11
/*     */       //   #344	-> 16
/*     */       //   #346	-> 22
/*     */       //   #349	-> 60
/*     */       //   #350	-> 79
/*     */       //   #352	-> 82
/*     */       //   #353	-> 100
/*     */       //   #355	-> 103
/*     */       //   #356	-> 122
/*     */       //   #358	-> 125
/*     */       //   #362	-> 143
/*     */       //   #364	-> 153
/*     */       //   #365	-> 164
/*     */       //   #367	-> 170
/*     */       //   #369	-> 173
/*     */       //   #372	-> 176
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   79	3	7	box	Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   100	3	7	box	Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   122	3	7	box	Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   143	30	7	box	Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   22	151	8	blockLength	I
/*     */       //   0	178	0	structurePieceAccessor	Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;
/*     */       //   0	178	1	random	Lnet/minecraft/util/RandomSource;
/*     */       //   0	178	2	footX	I
/*     */       //   0	178	3	footY	I
/*     */       //   0	178	4	footZ	I
/*     */       //   0	178	5	direction	Lnet/minecraft/core/Direction;
/*     */       //   11	167	6	corridorLength	I
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
/*     */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: invokevirtual getGenDepth : ()I
/*     */       //   4: istore #4
/*     */       //   6: aload_3
/*     */       //   7: iconst_4
/*     */       //   8: invokeinterface nextInt : (I)I
/*     */       //   13: istore #5
/*     */       //   15: aload_0
/*     */       //   16: invokevirtual getOrientation : ()Lnet/minecraft/core/Direction;
/*     */       //   19: astore #6
/*     */       //   21: aload #6
/*     */       //   23: ifnull -> 689
/*     */       //   26: getstatic net/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$1.$SwitchMap$net$minecraft$core$Direction : [I
/*     */       //   29: aload #6
/*     */       //   31: invokevirtual ordinal : ()I
/*     */       //   34: iaload
/*     */       //   35: tableswitch default -> 64, 1 -> 64, 2 -> 219, 3 -> 378, 4 -> 533
/*     */       //   64: iload #5
/*     */       //   66: iconst_1
/*     */       //   67: if_icmpgt -> 117
/*     */       //   70: aload_1
/*     */       //   71: aload_2
/*     */       //   72: aload_3
/*     */       //   73: aload_0
/*     */       //   74: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   77: invokevirtual minX : ()I
/*     */       //   80: aload_0
/*     */       //   81: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   84: invokevirtual minY : ()I
/*     */       //   87: iconst_1
/*     */       //   88: isub
/*     */       //   89: aload_3
/*     */       //   90: iconst_3
/*     */       //   91: invokeinterface nextInt : (I)I
/*     */       //   96: iadd
/*     */       //   97: aload_0
/*     */       //   98: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   101: invokevirtual minZ : ()I
/*     */       //   104: iconst_1
/*     */       //   105: isub
/*     */       //   106: aload #6
/*     */       //   108: iload #4
/*     */       //   110: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   113: pop
/*     */       //   114: goto -> 689
/*     */       //   117: iload #5
/*     */       //   119: iconst_2
/*     */       //   120: if_icmpne -> 171
/*     */       //   123: aload_1
/*     */       //   124: aload_2
/*     */       //   125: aload_3
/*     */       //   126: aload_0
/*     */       //   127: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   130: invokevirtual minX : ()I
/*     */       //   133: iconst_1
/*     */       //   134: isub
/*     */       //   135: aload_0
/*     */       //   136: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   139: invokevirtual minY : ()I
/*     */       //   142: iconst_1
/*     */       //   143: isub
/*     */       //   144: aload_3
/*     */       //   145: iconst_3
/*     */       //   146: invokeinterface nextInt : (I)I
/*     */       //   151: iadd
/*     */       //   152: aload_0
/*     */       //   153: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   156: invokevirtual minZ : ()I
/*     */       //   159: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */       //   162: iload #4
/*     */       //   164: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   167: pop
/*     */       //   168: goto -> 689
/*     */       //   171: aload_1
/*     */       //   172: aload_2
/*     */       //   173: aload_3
/*     */       //   174: aload_0
/*     */       //   175: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   178: invokevirtual maxX : ()I
/*     */       //   181: iconst_1
/*     */       //   182: iadd
/*     */       //   183: aload_0
/*     */       //   184: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   187: invokevirtual minY : ()I
/*     */       //   190: iconst_1
/*     */       //   191: isub
/*     */       //   192: aload_3
/*     */       //   193: iconst_3
/*     */       //   194: invokeinterface nextInt : (I)I
/*     */       //   199: iadd
/*     */       //   200: aload_0
/*     */       //   201: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   204: invokevirtual minZ : ()I
/*     */       //   207: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */       //   210: iload #4
/*     */       //   212: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   215: pop
/*     */       //   216: goto -> 689
/*     */       //   219: iload #5
/*     */       //   221: iconst_1
/*     */       //   222: if_icmpgt -> 272
/*     */       //   225: aload_1
/*     */       //   226: aload_2
/*     */       //   227: aload_3
/*     */       //   228: aload_0
/*     */       //   229: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   232: invokevirtual minX : ()I
/*     */       //   235: aload_0
/*     */       //   236: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   239: invokevirtual minY : ()I
/*     */       //   242: iconst_1
/*     */       //   243: isub
/*     */       //   244: aload_3
/*     */       //   245: iconst_3
/*     */       //   246: invokeinterface nextInt : (I)I
/*     */       //   251: iadd
/*     */       //   252: aload_0
/*     */       //   253: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   256: invokevirtual maxZ : ()I
/*     */       //   259: iconst_1
/*     */       //   260: iadd
/*     */       //   261: aload #6
/*     */       //   263: iload #4
/*     */       //   265: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   268: pop
/*     */       //   269: goto -> 689
/*     */       //   272: iload #5
/*     */       //   274: iconst_2
/*     */       //   275: if_icmpne -> 328
/*     */       //   278: aload_1
/*     */       //   279: aload_2
/*     */       //   280: aload_3
/*     */       //   281: aload_0
/*     */       //   282: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   285: invokevirtual minX : ()I
/*     */       //   288: iconst_1
/*     */       //   289: isub
/*     */       //   290: aload_0
/*     */       //   291: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   294: invokevirtual minY : ()I
/*     */       //   297: iconst_1
/*     */       //   298: isub
/*     */       //   299: aload_3
/*     */       //   300: iconst_3
/*     */       //   301: invokeinterface nextInt : (I)I
/*     */       //   306: iadd
/*     */       //   307: aload_0
/*     */       //   308: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   311: invokevirtual maxZ : ()I
/*     */       //   314: iconst_3
/*     */       //   315: isub
/*     */       //   316: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */       //   319: iload #4
/*     */       //   321: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   324: pop
/*     */       //   325: goto -> 689
/*     */       //   328: aload_1
/*     */       //   329: aload_2
/*     */       //   330: aload_3
/*     */       //   331: aload_0
/*     */       //   332: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   335: invokevirtual maxX : ()I
/*     */       //   338: iconst_1
/*     */       //   339: iadd
/*     */       //   340: aload_0
/*     */       //   341: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   344: invokevirtual minY : ()I
/*     */       //   347: iconst_1
/*     */       //   348: isub
/*     */       //   349: aload_3
/*     */       //   350: iconst_3
/*     */       //   351: invokeinterface nextInt : (I)I
/*     */       //   356: iadd
/*     */       //   357: aload_0
/*     */       //   358: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   361: invokevirtual maxZ : ()I
/*     */       //   364: iconst_3
/*     */       //   365: isub
/*     */       //   366: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */       //   369: iload #4
/*     */       //   371: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   374: pop
/*     */       //   375: goto -> 689
/*     */       //   378: iload #5
/*     */       //   380: iconst_1
/*     */       //   381: if_icmpgt -> 431
/*     */       //   384: aload_1
/*     */       //   385: aload_2
/*     */       //   386: aload_3
/*     */       //   387: aload_0
/*     */       //   388: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   391: invokevirtual minX : ()I
/*     */       //   394: iconst_1
/*     */       //   395: isub
/*     */       //   396: aload_0
/*     */       //   397: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   400: invokevirtual minY : ()I
/*     */       //   403: iconst_1
/*     */       //   404: isub
/*     */       //   405: aload_3
/*     */       //   406: iconst_3
/*     */       //   407: invokeinterface nextInt : (I)I
/*     */       //   412: iadd
/*     */       //   413: aload_0
/*     */       //   414: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   417: invokevirtual minZ : ()I
/*     */       //   420: aload #6
/*     */       //   422: iload #4
/*     */       //   424: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   427: pop
/*     */       //   428: goto -> 689
/*     */       //   431: iload #5
/*     */       //   433: iconst_2
/*     */       //   434: if_icmpne -> 485
/*     */       //   437: aload_1
/*     */       //   438: aload_2
/*     */       //   439: aload_3
/*     */       //   440: aload_0
/*     */       //   441: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   444: invokevirtual minX : ()I
/*     */       //   447: aload_0
/*     */       //   448: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   451: invokevirtual minY : ()I
/*     */       //   454: iconst_1
/*     */       //   455: isub
/*     */       //   456: aload_3
/*     */       //   457: iconst_3
/*     */       //   458: invokeinterface nextInt : (I)I
/*     */       //   463: iadd
/*     */       //   464: aload_0
/*     */       //   465: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   468: invokevirtual minZ : ()I
/*     */       //   471: iconst_1
/*     */       //   472: isub
/*     */       //   473: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */       //   476: iload #4
/*     */       //   478: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   481: pop
/*     */       //   482: goto -> 689
/*     */       //   485: aload_1
/*     */       //   486: aload_2
/*     */       //   487: aload_3
/*     */       //   488: aload_0
/*     */       //   489: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   492: invokevirtual minX : ()I
/*     */       //   495: aload_0
/*     */       //   496: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   499: invokevirtual minY : ()I
/*     */       //   502: iconst_1
/*     */       //   503: isub
/*     */       //   504: aload_3
/*     */       //   505: iconst_3
/*     */       //   506: invokeinterface nextInt : (I)I
/*     */       //   511: iadd
/*     */       //   512: aload_0
/*     */       //   513: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   516: invokevirtual maxZ : ()I
/*     */       //   519: iconst_1
/*     */       //   520: iadd
/*     */       //   521: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */       //   524: iload #4
/*     */       //   526: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   529: pop
/*     */       //   530: goto -> 689
/*     */       //   533: iload #5
/*     */       //   535: iconst_1
/*     */       //   536: if_icmpgt -> 586
/*     */       //   539: aload_1
/*     */       //   540: aload_2
/*     */       //   541: aload_3
/*     */       //   542: aload_0
/*     */       //   543: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   546: invokevirtual maxX : ()I
/*     */       //   549: iconst_1
/*     */       //   550: iadd
/*     */       //   551: aload_0
/*     */       //   552: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   555: invokevirtual minY : ()I
/*     */       //   558: iconst_1
/*     */       //   559: isub
/*     */       //   560: aload_3
/*     */       //   561: iconst_3
/*     */       //   562: invokeinterface nextInt : (I)I
/*     */       //   567: iadd
/*     */       //   568: aload_0
/*     */       //   569: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   572: invokevirtual minZ : ()I
/*     */       //   575: aload #6
/*     */       //   577: iload #4
/*     */       //   579: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   582: pop
/*     */       //   583: goto -> 689
/*     */       //   586: iload #5
/*     */       //   588: iconst_2
/*     */       //   589: if_icmpne -> 642
/*     */       //   592: aload_1
/*     */       //   593: aload_2
/*     */       //   594: aload_3
/*     */       //   595: aload_0
/*     */       //   596: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   599: invokevirtual maxX : ()I
/*     */       //   602: iconst_3
/*     */       //   603: isub
/*     */       //   604: aload_0
/*     */       //   605: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   608: invokevirtual minY : ()I
/*     */       //   611: iconst_1
/*     */       //   612: isub
/*     */       //   613: aload_3
/*     */       //   614: iconst_3
/*     */       //   615: invokeinterface nextInt : (I)I
/*     */       //   620: iadd
/*     */       //   621: aload_0
/*     */       //   622: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   625: invokevirtual minZ : ()I
/*     */       //   628: iconst_1
/*     */       //   629: isub
/*     */       //   630: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */       //   633: iload #4
/*     */       //   635: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   638: pop
/*     */       //   639: goto -> 689
/*     */       //   642: aload_1
/*     */       //   643: aload_2
/*     */       //   644: aload_3
/*     */       //   645: aload_0
/*     */       //   646: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   649: invokevirtual maxX : ()I
/*     */       //   652: iconst_3
/*     */       //   653: isub
/*     */       //   654: aload_0
/*     */       //   655: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   658: invokevirtual minY : ()I
/*     */       //   661: iconst_1
/*     */       //   662: isub
/*     */       //   663: aload_3
/*     */       //   664: iconst_3
/*     */       //   665: invokeinterface nextInt : (I)I
/*     */       //   670: iadd
/*     */       //   671: aload_0
/*     */       //   672: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   675: invokevirtual maxZ : ()I
/*     */       //   678: iconst_1
/*     */       //   679: iadd
/*     */       //   680: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */       //   683: iload #4
/*     */       //   685: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   688: pop
/*     */       //   689: iload #4
/*     */       //   691: bipush #8
/*     */       //   693: if_icmpge -> 951
/*     */       //   696: aload #6
/*     */       //   698: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */       //   701: if_acmpeq -> 712
/*     */       //   704: aload #6
/*     */       //   706: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */       //   709: if_acmpne -> 833
/*     */       //   712: aload_0
/*     */       //   713: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   716: invokevirtual minZ : ()I
/*     */       //   719: iconst_3
/*     */       //   720: iadd
/*     */       //   721: istore #7
/*     */       //   723: iload #7
/*     */       //   725: iconst_3
/*     */       //   726: iadd
/*     */       //   727: aload_0
/*     */       //   728: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   731: invokevirtual maxZ : ()I
/*     */       //   734: if_icmpgt -> 830
/*     */       //   737: aload_3
/*     */       //   738: iconst_5
/*     */       //   739: invokeinterface nextInt : (I)I
/*     */       //   744: istore #8
/*     */       //   746: iload #8
/*     */       //   748: ifne -> 786
/*     */       //   751: aload_1
/*     */       //   752: aload_2
/*     */       //   753: aload_3
/*     */       //   754: aload_0
/*     */       //   755: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   758: invokevirtual minX : ()I
/*     */       //   761: iconst_1
/*     */       //   762: isub
/*     */       //   763: aload_0
/*     */       //   764: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   767: invokevirtual minY : ()I
/*     */       //   770: iload #7
/*     */       //   772: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */       //   775: iload #4
/*     */       //   777: iconst_1
/*     */       //   778: iadd
/*     */       //   779: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   782: pop
/*     */       //   783: goto -> 824
/*     */       //   786: iload #8
/*     */       //   788: iconst_1
/*     */       //   789: if_icmpne -> 824
/*     */       //   792: aload_1
/*     */       //   793: aload_2
/*     */       //   794: aload_3
/*     */       //   795: aload_0
/*     */       //   796: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   799: invokevirtual maxX : ()I
/*     */       //   802: iconst_1
/*     */       //   803: iadd
/*     */       //   804: aload_0
/*     */       //   805: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   808: invokevirtual minY : ()I
/*     */       //   811: iload #7
/*     */       //   813: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */       //   816: iload #4
/*     */       //   818: iconst_1
/*     */       //   819: iadd
/*     */       //   820: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   823: pop
/*     */       //   824: iinc #7, 5
/*     */       //   827: goto -> 723
/*     */       //   830: goto -> 951
/*     */       //   833: aload_0
/*     */       //   834: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   837: invokevirtual minX : ()I
/*     */       //   840: iconst_3
/*     */       //   841: iadd
/*     */       //   842: istore #7
/*     */       //   844: iload #7
/*     */       //   846: iconst_3
/*     */       //   847: iadd
/*     */       //   848: aload_0
/*     */       //   849: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   852: invokevirtual maxX : ()I
/*     */       //   855: if_icmpgt -> 951
/*     */       //   858: aload_3
/*     */       //   859: iconst_5
/*     */       //   860: invokeinterface nextInt : (I)I
/*     */       //   865: istore #8
/*     */       //   867: iload #8
/*     */       //   869: ifne -> 907
/*     */       //   872: aload_1
/*     */       //   873: aload_2
/*     */       //   874: aload_3
/*     */       //   875: iload #7
/*     */       //   877: aload_0
/*     */       //   878: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   881: invokevirtual minY : ()I
/*     */       //   884: aload_0
/*     */       //   885: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   888: invokevirtual minZ : ()I
/*     */       //   891: iconst_1
/*     */       //   892: isub
/*     */       //   893: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */       //   896: iload #4
/*     */       //   898: iconst_1
/*     */       //   899: iadd
/*     */       //   900: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   903: pop
/*     */       //   904: goto -> 945
/*     */       //   907: iload #8
/*     */       //   909: iconst_1
/*     */       //   910: if_icmpne -> 945
/*     */       //   913: aload_1
/*     */       //   914: aload_2
/*     */       //   915: aload_3
/*     */       //   916: iload #7
/*     */       //   918: aload_0
/*     */       //   919: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   922: invokevirtual minY : ()I
/*     */       //   925: aload_0
/*     */       //   926: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   929: invokevirtual maxZ : ()I
/*     */       //   932: iconst_1
/*     */       //   933: iadd
/*     */       //   934: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */       //   937: iload #4
/*     */       //   939: iconst_1
/*     */       //   940: iadd
/*     */       //   941: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   944: pop
/*     */       //   945: iinc #7, 5
/*     */       //   948: goto -> 844
/*     */       //   951: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #377	-> 0
/*     */       //   #378	-> 6
/*     */       //   #379	-> 15
/*     */       //   #380	-> 21
/*     */       //   #381	-> 26
/*     */       //   #384	-> 64
/*     */       //   #385	-> 70
/*     */       //   #386	-> 117
/*     */       //   #387	-> 123
/*     */       //   #389	-> 171
/*     */       //   #391	-> 216
/*     */       //   #393	-> 219
/*     */       //   #394	-> 225
/*     */       //   #395	-> 272
/*     */       //   #396	-> 278
/*     */       //   #398	-> 328
/*     */       //   #400	-> 375
/*     */       //   #402	-> 378
/*     */       //   #403	-> 384
/*     */       //   #404	-> 431
/*     */       //   #405	-> 437
/*     */       //   #407	-> 485
/*     */       //   #409	-> 530
/*     */       //   #411	-> 533
/*     */       //   #412	-> 539
/*     */       //   #413	-> 586
/*     */       //   #414	-> 592
/*     */       //   #416	-> 642
/*     */       //   #423	-> 689
/*     */       //   #424	-> 696
/*     */       //   #425	-> 712
/*     */       //   #426	-> 737
/*     */       //   #427	-> 746
/*     */       //   #428	-> 751
/*     */       //   #429	-> 786
/*     */       //   #430	-> 792
/*     */       //   #425	-> 824
/*     */       //   #434	-> 833
/*     */       //   #435	-> 858
/*     */       //   #436	-> 867
/*     */       //   #437	-> 872
/*     */       //   #438	-> 907
/*     */       //   #439	-> 913
/*     */       //   #434	-> 945
/*     */       //   #444	-> 951
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   746	78	8	selection	I
/*     */       //   723	107	7	z	I
/*     */       //   867	78	8	selection	I
/*     */       //   844	107	7	x	I
/*     */       //   0	952	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftCorridor;
/*     */       //   0	952	1	startPiece	Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */       //   0	952	2	structurePieceAccessor	Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;
/*     */       //   0	952	3	random	Lnet/minecraft/util/RandomSource;
/*     */       //   6	946	4	depth	I
/*     */       //   15	937	5	endSelection	I
/*     */       //   21	931	6	orientation	Lnet/minecraft/core/Direction;
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
/*     */     protected boolean createChest(WorldGenLevel level, BoundingBox chunkBB, RandomSource random, int x, int y, int z, ResourceKey<LootTable> lootTable) {
/* 448 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(x, y, z);
/* 449 */       if (chunkBB.isInside((Vec3i)mutableBlockPos) && 
/* 450 */         level.getBlockState((BlockPos)mutableBlockPos).isAir() && !level.getBlockState(mutableBlockPos.below()).isAir()) {
/* 451 */         BlockState state = (BlockState)Blocks.RAIL.defaultBlockState().setValue((Property)RailBlock.SHAPE, random.nextBoolean() ? (Comparable)RailShape.NORTH_SOUTH : (Comparable)RailShape.EAST_WEST);
/* 452 */         placeBlock(level, state, x, y, z, chunkBB);
/* 453 */         MinecartChest chest = (MinecartChest)EntityType.CHEST_MINECART.create((Level)level.getLevel(), EntitySpawnReason.CHUNK_GENERATION);
/* 454 */         if (chest != null) {
/* 455 */           chest.setInitialPos(mutableBlockPos.getX() + 0.5D, mutableBlockPos.getY() + 0.5D, mutableBlockPos.getZ() + 0.5D);
/* 456 */           chest.setLootTable(lootTable, random.nextLong());
/* 457 */           level.addFreshEntity((Entity)chest);
/*     */         } 
/* 459 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 463 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 468 */       if (isInInvalidLocation((LevelAccessor)level, chunkBB)) {
/*     */         return;
/*     */       }
/*     */       
/* 472 */       int x0 = 0;
/* 473 */       int x1 = 2;
/* 474 */       int y0 = 0;
/* 475 */       int y1 = 2;
/* 476 */       int length = this.numSections * 5 - 1;
/*     */       
/* 478 */       BlockState planks = this.type.getPlanksState();
/*     */ 
/*     */       
/* 481 */       generateBox(level, chunkBB, 0, 0, 0, 2, 1, length, CAVE_AIR, CAVE_AIR, false);
/* 482 */       generateMaybeBox(level, chunkBB, random, 0.8F, 0, 2, 0, 2, 2, length, CAVE_AIR, CAVE_AIR, false, false);
/*     */       
/* 484 */       if (this.spiderCorridor) {
/* 485 */         generateMaybeBox(level, chunkBB, random, 0.6F, 0, 0, 0, 2, 1, length, Blocks.COBWEB.defaultBlockState(), CAVE_AIR, false, true);
/*     */       }
/*     */ 
/*     */       
/* 489 */       for (int section = 0; section < this.numSections; section++) {
/* 490 */         int z = 2 + section * 5;
/*     */         
/* 492 */         placeSupport(level, chunkBB, 0, 0, z, 2, 2, random);
/*     */         
/* 494 */         maybePlaceCobWeb(level, chunkBB, random, 0.1F, 0, 2, z - 1);
/* 495 */         maybePlaceCobWeb(level, chunkBB, random, 0.1F, 2, 2, z - 1);
/* 496 */         maybePlaceCobWeb(level, chunkBB, random, 0.1F, 0, 2, z + 1);
/* 497 */         maybePlaceCobWeb(level, chunkBB, random, 0.1F, 2, 2, z + 1);
/* 498 */         maybePlaceCobWeb(level, chunkBB, random, 0.05F, 0, 2, z - 2);
/* 499 */         maybePlaceCobWeb(level, chunkBB, random, 0.05F, 2, 2, z - 2);
/* 500 */         maybePlaceCobWeb(level, chunkBB, random, 0.05F, 0, 2, z + 2);
/* 501 */         maybePlaceCobWeb(level, chunkBB, random, 0.05F, 2, 2, z + 2);
/*     */         
/* 503 */         if (random.nextInt(100) == 0) {
/* 504 */           createChest(level, chunkBB, random, 2, 0, z - 1, BuiltInLootTables.ABANDONED_MINESHAFT);
/*     */         }
/* 506 */         if (random.nextInt(100) == 0) {
/* 507 */           createChest(level, chunkBB, random, 0, 0, z + 1, BuiltInLootTables.ABANDONED_MINESHAFT);
/*     */         }
/* 509 */         if (this.spiderCorridor && !this.hasPlacedSpider) {
/* 510 */           int newX = 1;
/* 511 */           int newZ = z - 1 + random.nextInt(3);
/* 512 */           BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(1, 0, newZ);
/*     */           
/* 514 */           if (chunkBB.isInside((Vec3i)mutableBlockPos) && isInterior((LevelReader)level, 1, 0, newZ, chunkBB)) {
/* 515 */             this.hasPlacedSpider = true;
/* 516 */             level.setBlock((BlockPos)mutableBlockPos, Blocks.SPAWNER.defaultBlockState(), 2);
/*     */             
/* 518 */             BlockEntity blockEntity = level.getBlockEntity((BlockPos)mutableBlockPos);
/* 519 */             if (blockEntity instanceof SpawnerBlockEntity) { SpawnerBlockEntity spawner = (SpawnerBlockEntity)blockEntity;
/* 520 */               spawner.setEntityId(EntityType.CAVE_SPIDER, random); }
/*     */           
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 527 */       for (int x = 0; x <= 2; x++) {
/* 528 */         for (int z = 0; z <= length; z++) {
/* 529 */           setPlanksBlock(level, chunkBB, planks, x, -1, z);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 534 */       int supportPillarIndent = 2;
/* 535 */       placeDoubleLowerOrUpperSupport(level, chunkBB, 0, -1, 2);
/* 536 */       if (this.numSections > 1) {
/* 537 */         int lastSupportPillar = length - 2;
/* 538 */         placeDoubleLowerOrUpperSupport(level, chunkBB, 0, -1, lastSupportPillar);
/*     */       } 
/*     */       
/* 541 */       if (this.hasRails) {
/* 542 */         BlockState state = (BlockState)Blocks.RAIL.defaultBlockState().setValue((Property)RailBlock.SHAPE, (Comparable)RailShape.NORTH_SOUTH);
/* 543 */         for (int z = 0; z <= length; z++) {
/* 544 */           BlockState floor = getBlock((BlockGetter)level, 1, -1, z, chunkBB);
/* 545 */           if (!floor.isAir() && floor.isSolidRender()) {
/* 546 */             float probability = isInterior((LevelReader)level, 1, 0, z, chunkBB) ? 0.7F : 0.9F;
/* 547 */             maybeGenerateBlock(level, chunkBB, random, probability, 1, 0, z, state);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void placeDoubleLowerOrUpperSupport(WorldGenLevel level, BoundingBox chunkBB, int x, int y, int z) {
/* 554 */       BlockState woodBlock = this.type.getWoodState();
/* 555 */       BlockState plankBlock = this.type.getPlanksState();
/* 556 */       if (getBlock((BlockGetter)level, x, y, z, chunkBB).is(plankBlock.getBlock())) {
/* 557 */         fillPillarDownOrChainUp(level, woodBlock, x, y, z, chunkBB);
/*     */       }
/* 559 */       if (getBlock((BlockGetter)level, x + 2, y, z, chunkBB).is(plankBlock.getBlock())) {
/* 560 */         fillPillarDownOrChainUp(level, woodBlock, x + 2, y, z, chunkBB);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected void fillColumnDown(WorldGenLevel level, BlockState columnState, int x, int startY, int z, BoundingBox chunkBB) {
/* 566 */       BlockPos.MutableBlockPos pos = getWorldPos(x, startY, z);
/* 567 */       if (!chunkBB.isInside((Vec3i)pos)) {
/*     */         return;
/*     */       }
/*     */       
/* 571 */       int worldY = pos.getY();
/*     */ 
/*     */       
/* 574 */       while (isReplaceableByStructures(level.getBlockState((BlockPos)pos)) && pos.getY() > level.getMinY() + 1) {
/* 575 */         pos.move(Direction.DOWN);
/*     */       }
/* 577 */       if (!canPlaceColumnOnTopOf((LevelReader)level, (BlockPos)pos, level.getBlockState((BlockPos)pos))) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 582 */       while (pos.getY() < worldY) {
/* 583 */         pos.move(Direction.UP);
/* 584 */         level.setBlock((BlockPos)pos, columnState, 2);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void fillPillarDownOrChainUp(WorldGenLevel level, BlockState pillarState, int x, int y, int z, BoundingBox chunkBB) {
/* 590 */       BlockPos.MutableBlockPos pos = getWorldPos(x, y, z);
/* 591 */       if (!chunkBB.isInside((Vec3i)pos)) {
/*     */         return;
/*     */       }
/*     */       
/* 595 */       int worldY = pos.getY();
/*     */ 
/*     */       
/* 598 */       int distanceFromWorldY = 1;
/*     */       
/*     */       boolean checkBelow = true;
/*     */       boolean checkAbove = true;
/* 602 */       while (checkBelow || checkAbove) {
/* 603 */         if (checkBelow) {
/* 604 */           pos.setY(worldY - distanceFromWorldY);
/* 605 */           BlockState belowState = level.getBlockState((BlockPos)pos);
/* 606 */           boolean emptyBelow = (isReplaceableByStructures(belowState) && !belowState.is(Blocks.LAVA));
/* 607 */           if (!emptyBelow && canPlaceColumnOnTopOf((LevelReader)level, (BlockPos)pos, belowState)) {
/* 608 */             fillColumnBetween(level, pillarState, pos, worldY - distanceFromWorldY + 1, worldY);
/*     */             return;
/*     */           } 
/* 611 */           checkBelow = (distanceFromWorldY <= 20 && emptyBelow && pos.getY() > level.getMinY() + 1);
/*     */         } 
/*     */         
/* 614 */         if (checkAbove) {
/* 615 */           pos.setY(worldY + distanceFromWorldY);
/* 616 */           BlockState aboveState = level.getBlockState((BlockPos)pos);
/* 617 */           boolean emptyAbove = isReplaceableByStructures(aboveState);
/* 618 */           if (!emptyAbove && canHangChainBelow((LevelReader)level, (BlockPos)pos, aboveState)) {
/*     */             
/* 620 */             level.setBlock((BlockPos)pos.setY(worldY + 1), this.type.getFenceState(), 2);
/* 621 */             fillColumnBetween(level, Blocks.IRON_CHAIN.defaultBlockState(), pos, worldY + 2, worldY + distanceFromWorldY);
/*     */             return;
/*     */           } 
/* 624 */           checkAbove = (distanceFromWorldY <= 50 && emptyAbove && pos.getY() < level.getMaxY());
/*     */         } 
/*     */         
/* 627 */         distanceFromWorldY++;
/*     */       } 
/*     */     }
/*     */     
/*     */     private static void fillColumnBetween(WorldGenLevel level, BlockState pillarState, BlockPos.MutableBlockPos pos, int bottomInclusive, int topExclusive) {
/* 632 */       for (int pillarY = bottomInclusive; pillarY < topExclusive; pillarY++) {
/* 633 */         level.setBlock((BlockPos)pos.setY(pillarY), pillarState, 2);
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean canPlaceColumnOnTopOf(LevelReader level, BlockPos posBelow, BlockState stateBelow) {
/* 638 */       return stateBelow.isFaceSturdy((BlockGetter)level, posBelow, Direction.UP);
/*     */     }
/*     */     
/*     */     private boolean canHangChainBelow(LevelReader level, BlockPos posAbove, BlockState stateAbove) {
/* 642 */       return (Block.canSupportCenter(level, posAbove, Direction.DOWN) && !(stateAbove.getBlock() instanceof net.minecraft.world.level.block.FallingBlock));
/*     */     }
/*     */ 
/*     */     
/*     */     private void placeSupport(WorldGenLevel level, BoundingBox chunkBB, int x0, int y0, int z, int y1, int x1, RandomSource random) {
/* 647 */       if (!isSupportingBox((BlockGetter)level, chunkBB, x0, x1, y1, z)) {
/*     */         return;
/*     */       }
/*     */       
/* 651 */       BlockState planksBlock = this.type.getPlanksState();
/* 652 */       BlockState fenceBlock = this.type.getFenceState();
/*     */       
/* 654 */       generateBox(level, chunkBB, x0, y0, z, x0, y1 - 1, z, (BlockState)fenceBlock.setValue((Property)FenceBlock.WEST, true), CAVE_AIR, false);
/* 655 */       generateBox(level, chunkBB, x1, y0, z, x1, y1 - 1, z, (BlockState)fenceBlock.setValue((Property)FenceBlock.EAST, true), CAVE_AIR, false);
/* 656 */       if (random.nextInt(4) == 0) {
/* 657 */         generateBox(level, chunkBB, x0, y1, z, x0, y1, z, planksBlock, CAVE_AIR, false);
/* 658 */         generateBox(level, chunkBB, x1, y1, z, x1, y1, z, planksBlock, CAVE_AIR, false);
/*     */       } else {
/* 660 */         generateBox(level, chunkBB, x0, y1, z, x1, y1, z, planksBlock, CAVE_AIR, false);
/* 661 */         maybeGenerateBlock(level, chunkBB, random, 0.05F, x0 + 1, y1, z - 1, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.SOUTH));
/* 662 */         maybeGenerateBlock(level, chunkBB, random, 0.05F, x0 + 1, y1, z + 1, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.NORTH));
/*     */       } 
/*     */     }
/*     */     
/*     */     private void maybePlaceCobWeb(WorldGenLevel level, BoundingBox chunkBB, RandomSource random, float probability, int x, int y, int z) {
/* 667 */       if (isInterior((LevelReader)level, x, y, z, chunkBB) && random.nextFloat() < probability && hasSturdyNeighbours(level, chunkBB, x, y, z, 2)) {
/* 668 */         placeBlock(level, Blocks.COBWEB.defaultBlockState(), x, y, z, chunkBB);
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean hasSturdyNeighbours(WorldGenLevel level, BoundingBox chunkBB, int x, int y, int z, int count) {
/* 673 */       BlockPos.MutableBlockPos worldPos = getWorldPos(x, y, z);
/* 674 */       int sturdyNeighbours = 0;
/* 675 */       for (Direction direction : Direction.values()) {
/* 676 */         worldPos.move(direction);
/*     */         
/* 678 */         sturdyNeighbours++;
/* 679 */         if (chunkBB.isInside((Vec3i)worldPos) && level.getBlockState((BlockPos)worldPos).isFaceSturdy((BlockGetter)level, (BlockPos)worldPos, direction.getOpposite()) && sturdyNeighbours >= count) {
/* 680 */           return true;
/*     */         }
/*     */         
/* 683 */         worldPos.move(direction.getOpposite());
/*     */       } 
/* 685 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MineShaftCrossing extends MineShaftPiece {
/*     */     private final Direction direction;
/*     */     private final boolean isTwoFloored;
/*     */     
/*     */     public MineShaftCrossing(CompoundTag tag) {
/* 694 */       super(StructurePieceType.MINE_SHAFT_CROSSING, tag);
/* 695 */       this.isTwoFloored = tag.getBooleanOr("tf", false);
/* 696 */       this.direction = tag.read("D", Direction.LEGACY_ID_CODEC_2D).orElse(Direction.SOUTH);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 701 */       super.addAdditionalSaveData(context, tag);
/* 702 */       tag.putBoolean("tf", this.isTwoFloored);
/* 703 */       tag.store("D", Direction.LEGACY_ID_CODEC_2D, this.direction);
/*     */     }
/*     */     
/*     */     public MineShaftCrossing(int genDepth, BoundingBox boundingBox, Direction direction, MineshaftStructure.Type type) {
/* 707 */       super(StructurePieceType.MINE_SHAFT_CROSSING, genDepth, type, boundingBox);
/*     */       
/* 709 */       this.direction = direction;
/* 710 */       this.isTwoFloored = (boundingBox.getYSpan() > 3);
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
/*     */ 
/*     */     
/*     */     public static BoundingBox findCrossing(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction) {
/*     */       // Byte code:
/*     */       //   0: aload_1
/*     */       //   1: iconst_4
/*     */       //   2: invokeinterface nextInt : (I)I
/*     */       //   7: ifne -> 17
/*     */       //   10: bipush #6
/*     */       //   12: istore #6
/*     */       //   14: goto -> 20
/*     */       //   17: iconst_2
/*     */       //   18: istore #6
/*     */       //   20: getstatic net/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$1.$SwitchMap$net$minecraft$core$Direction : [I
/*     */       //   23: aload #5
/*     */       //   25: invokevirtual ordinal : ()I
/*     */       //   28: iaload
/*     */       //   29: tableswitch default -> 60, 1 -> 60, 2 -> 80, 3 -> 99, 4 -> 119
/*     */       //   60: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   63: dup
/*     */       //   64: iconst_m1
/*     */       //   65: iconst_0
/*     */       //   66: bipush #-4
/*     */       //   68: iconst_3
/*     */       //   69: iload #6
/*     */       //   71: iconst_0
/*     */       //   72: invokespecial <init> : (IIIIII)V
/*     */       //   75: astore #7
/*     */       //   77: goto -> 135
/*     */       //   80: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   83: dup
/*     */       //   84: iconst_m1
/*     */       //   85: iconst_0
/*     */       //   86: iconst_0
/*     */       //   87: iconst_3
/*     */       //   88: iload #6
/*     */       //   90: iconst_4
/*     */       //   91: invokespecial <init> : (IIIIII)V
/*     */       //   94: astore #7
/*     */       //   96: goto -> 135
/*     */       //   99: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   102: dup
/*     */       //   103: bipush #-4
/*     */       //   105: iconst_0
/*     */       //   106: iconst_m1
/*     */       //   107: iconst_0
/*     */       //   108: iload #6
/*     */       //   110: iconst_3
/*     */       //   111: invokespecial <init> : (IIIIII)V
/*     */       //   114: astore #7
/*     */       //   116: goto -> 135
/*     */       //   119: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   122: dup
/*     */       //   123: iconst_0
/*     */       //   124: iconst_0
/*     */       //   125: iconst_m1
/*     */       //   126: iconst_4
/*     */       //   127: iload #6
/*     */       //   129: iconst_3
/*     */       //   130: invokespecial <init> : (IIIIII)V
/*     */       //   133: astore #7
/*     */       //   135: aload #7
/*     */       //   137: iload_2
/*     */       //   138: iload_3
/*     */       //   139: iload #4
/*     */       //   141: invokevirtual move : (III)Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   144: pop
/*     */       //   145: aload_0
/*     */       //   146: aload #7
/*     */       //   148: invokeinterface findCollisionPiece : (Lnet/minecraft/world/level/levelgen/structure/BoundingBox;)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */       //   153: ifnull -> 158
/*     */       //   156: aconst_null
/*     */       //   157: areturn
/*     */       //   158: aload #7
/*     */       //   160: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #715	-> 0
/*     */       //   #716	-> 10
/*     */       //   #718	-> 17
/*     */       //   #722	-> 20
/*     */       //   #725	-> 60
/*     */       //   #726	-> 77
/*     */       //   #728	-> 80
/*     */       //   #729	-> 96
/*     */       //   #731	-> 99
/*     */       //   #732	-> 116
/*     */       //   #734	-> 119
/*     */       //   #738	-> 135
/*     */       //   #740	-> 145
/*     */       //   #741	-> 156
/*     */       //   #744	-> 158
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   14	3	6	y1	I
/*     */       //   77	3	7	box	Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   96	3	7	box	Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   116	3	7	box	Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   0	161	0	structurePieceAccessor	Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;
/*     */       //   0	161	1	random	Lnet/minecraft/util/RandomSource;
/*     */       //   0	161	2	footX	I
/*     */       //   0	161	3	footY	I
/*     */       //   0	161	4	footZ	I
/*     */       //   0	161	5	direction	Lnet/minecraft/core/Direction;
/*     */       //   20	141	6	y1	I
/*     */       //   135	26	7	box	Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
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
/*     */ 
/*     */     
/*     */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: invokevirtual getGenDepth : ()I
/*     */       //   4: istore #4
/*     */       //   6: getstatic net/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$1.$SwitchMap$net$minecraft$core$Direction : [I
/*     */       //   9: aload_0
/*     */       //   10: getfield direction : Lnet/minecraft/core/Direction;
/*     */       //   13: invokevirtual ordinal : ()I
/*     */       //   16: iaload
/*     */       //   17: tableswitch default -> 48, 1 -> 48, 2 -> 162, 3 -> 276, 4 -> 390
/*     */       //   48: aload_1
/*     */       //   49: aload_2
/*     */       //   50: aload_3
/*     */       //   51: aload_0
/*     */       //   52: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   55: invokevirtual minX : ()I
/*     */       //   58: iconst_1
/*     */       //   59: iadd
/*     */       //   60: aload_0
/*     */       //   61: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   64: invokevirtual minY : ()I
/*     */       //   67: aload_0
/*     */       //   68: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   71: invokevirtual minZ : ()I
/*     */       //   74: iconst_1
/*     */       //   75: isub
/*     */       //   76: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */       //   79: iload #4
/*     */       //   81: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   84: pop
/*     */       //   85: aload_1
/*     */       //   86: aload_2
/*     */       //   87: aload_3
/*     */       //   88: aload_0
/*     */       //   89: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   92: invokevirtual minX : ()I
/*     */       //   95: iconst_1
/*     */       //   96: isub
/*     */       //   97: aload_0
/*     */       //   98: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   101: invokevirtual minY : ()I
/*     */       //   104: aload_0
/*     */       //   105: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   108: invokevirtual minZ : ()I
/*     */       //   111: iconst_1
/*     */       //   112: iadd
/*     */       //   113: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */       //   116: iload #4
/*     */       //   118: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   121: pop
/*     */       //   122: aload_1
/*     */       //   123: aload_2
/*     */       //   124: aload_3
/*     */       //   125: aload_0
/*     */       //   126: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   129: invokevirtual maxX : ()I
/*     */       //   132: iconst_1
/*     */       //   133: iadd
/*     */       //   134: aload_0
/*     */       //   135: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   138: invokevirtual minY : ()I
/*     */       //   141: aload_0
/*     */       //   142: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   145: invokevirtual minZ : ()I
/*     */       //   148: iconst_1
/*     */       //   149: iadd
/*     */       //   150: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */       //   153: iload #4
/*     */       //   155: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   158: pop
/*     */       //   159: goto -> 501
/*     */       //   162: aload_1
/*     */       //   163: aload_2
/*     */       //   164: aload_3
/*     */       //   165: aload_0
/*     */       //   166: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   169: invokevirtual minX : ()I
/*     */       //   172: iconst_1
/*     */       //   173: iadd
/*     */       //   174: aload_0
/*     */       //   175: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   178: invokevirtual minY : ()I
/*     */       //   181: aload_0
/*     */       //   182: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   185: invokevirtual maxZ : ()I
/*     */       //   188: iconst_1
/*     */       //   189: iadd
/*     */       //   190: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */       //   193: iload #4
/*     */       //   195: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   198: pop
/*     */       //   199: aload_1
/*     */       //   200: aload_2
/*     */       //   201: aload_3
/*     */       //   202: aload_0
/*     */       //   203: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   206: invokevirtual minX : ()I
/*     */       //   209: iconst_1
/*     */       //   210: isub
/*     */       //   211: aload_0
/*     */       //   212: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   215: invokevirtual minY : ()I
/*     */       //   218: aload_0
/*     */       //   219: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   222: invokevirtual minZ : ()I
/*     */       //   225: iconst_1
/*     */       //   226: iadd
/*     */       //   227: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */       //   230: iload #4
/*     */       //   232: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   235: pop
/*     */       //   236: aload_1
/*     */       //   237: aload_2
/*     */       //   238: aload_3
/*     */       //   239: aload_0
/*     */       //   240: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   243: invokevirtual maxX : ()I
/*     */       //   246: iconst_1
/*     */       //   247: iadd
/*     */       //   248: aload_0
/*     */       //   249: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   252: invokevirtual minY : ()I
/*     */       //   255: aload_0
/*     */       //   256: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   259: invokevirtual minZ : ()I
/*     */       //   262: iconst_1
/*     */       //   263: iadd
/*     */       //   264: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */       //   267: iload #4
/*     */       //   269: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   272: pop
/*     */       //   273: goto -> 501
/*     */       //   276: aload_1
/*     */       //   277: aload_2
/*     */       //   278: aload_3
/*     */       //   279: aload_0
/*     */       //   280: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   283: invokevirtual minX : ()I
/*     */       //   286: iconst_1
/*     */       //   287: iadd
/*     */       //   288: aload_0
/*     */       //   289: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   292: invokevirtual minY : ()I
/*     */       //   295: aload_0
/*     */       //   296: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   299: invokevirtual minZ : ()I
/*     */       //   302: iconst_1
/*     */       //   303: isub
/*     */       //   304: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */       //   307: iload #4
/*     */       //   309: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   312: pop
/*     */       //   313: aload_1
/*     */       //   314: aload_2
/*     */       //   315: aload_3
/*     */       //   316: aload_0
/*     */       //   317: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   320: invokevirtual minX : ()I
/*     */       //   323: iconst_1
/*     */       //   324: iadd
/*     */       //   325: aload_0
/*     */       //   326: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   329: invokevirtual minY : ()I
/*     */       //   332: aload_0
/*     */       //   333: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   336: invokevirtual maxZ : ()I
/*     */       //   339: iconst_1
/*     */       //   340: iadd
/*     */       //   341: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */       //   344: iload #4
/*     */       //   346: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   349: pop
/*     */       //   350: aload_1
/*     */       //   351: aload_2
/*     */       //   352: aload_3
/*     */       //   353: aload_0
/*     */       //   354: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   357: invokevirtual minX : ()I
/*     */       //   360: iconst_1
/*     */       //   361: isub
/*     */       //   362: aload_0
/*     */       //   363: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   366: invokevirtual minY : ()I
/*     */       //   369: aload_0
/*     */       //   370: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   373: invokevirtual minZ : ()I
/*     */       //   376: iconst_1
/*     */       //   377: iadd
/*     */       //   378: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */       //   381: iload #4
/*     */       //   383: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   386: pop
/*     */       //   387: goto -> 501
/*     */       //   390: aload_1
/*     */       //   391: aload_2
/*     */       //   392: aload_3
/*     */       //   393: aload_0
/*     */       //   394: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   397: invokevirtual minX : ()I
/*     */       //   400: iconst_1
/*     */       //   401: iadd
/*     */       //   402: aload_0
/*     */       //   403: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   406: invokevirtual minY : ()I
/*     */       //   409: aload_0
/*     */       //   410: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   413: invokevirtual minZ : ()I
/*     */       //   416: iconst_1
/*     */       //   417: isub
/*     */       //   418: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */       //   421: iload #4
/*     */       //   423: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   426: pop
/*     */       //   427: aload_1
/*     */       //   428: aload_2
/*     */       //   429: aload_3
/*     */       //   430: aload_0
/*     */       //   431: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   434: invokevirtual minX : ()I
/*     */       //   437: iconst_1
/*     */       //   438: iadd
/*     */       //   439: aload_0
/*     */       //   440: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   443: invokevirtual minY : ()I
/*     */       //   446: aload_0
/*     */       //   447: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   450: invokevirtual maxZ : ()I
/*     */       //   453: iconst_1
/*     */       //   454: iadd
/*     */       //   455: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */       //   458: iload #4
/*     */       //   460: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   463: pop
/*     */       //   464: aload_1
/*     */       //   465: aload_2
/*     */       //   466: aload_3
/*     */       //   467: aload_0
/*     */       //   468: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   471: invokevirtual maxX : ()I
/*     */       //   474: iconst_1
/*     */       //   475: iadd
/*     */       //   476: aload_0
/*     */       //   477: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   480: invokevirtual minY : ()I
/*     */       //   483: aload_0
/*     */       //   484: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   487: invokevirtual minZ : ()I
/*     */       //   490: iconst_1
/*     */       //   491: iadd
/*     */       //   492: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */       //   495: iload #4
/*     */       //   497: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   500: pop
/*     */       //   501: aload_0
/*     */       //   502: getfield isTwoFloored : Z
/*     */       //   505: ifeq -> 708
/*     */       //   508: aload_3
/*     */       //   509: invokeinterface nextBoolean : ()Z
/*     */       //   514: ifeq -> 558
/*     */       //   517: aload_1
/*     */       //   518: aload_2
/*     */       //   519: aload_3
/*     */       //   520: aload_0
/*     */       //   521: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   524: invokevirtual minX : ()I
/*     */       //   527: iconst_1
/*     */       //   528: iadd
/*     */       //   529: aload_0
/*     */       //   530: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   533: invokevirtual minY : ()I
/*     */       //   536: iconst_3
/*     */       //   537: iadd
/*     */       //   538: iconst_1
/*     */       //   539: iadd
/*     */       //   540: aload_0
/*     */       //   541: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   544: invokevirtual minZ : ()I
/*     */       //   547: iconst_1
/*     */       //   548: isub
/*     */       //   549: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */       //   552: iload #4
/*     */       //   554: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   557: pop
/*     */       //   558: aload_3
/*     */       //   559: invokeinterface nextBoolean : ()Z
/*     */       //   564: ifeq -> 608
/*     */       //   567: aload_1
/*     */       //   568: aload_2
/*     */       //   569: aload_3
/*     */       //   570: aload_0
/*     */       //   571: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   574: invokevirtual minX : ()I
/*     */       //   577: iconst_1
/*     */       //   578: isub
/*     */       //   579: aload_0
/*     */       //   580: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   583: invokevirtual minY : ()I
/*     */       //   586: iconst_3
/*     */       //   587: iadd
/*     */       //   588: iconst_1
/*     */       //   589: iadd
/*     */       //   590: aload_0
/*     */       //   591: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   594: invokevirtual minZ : ()I
/*     */       //   597: iconst_1
/*     */       //   598: iadd
/*     */       //   599: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */       //   602: iload #4
/*     */       //   604: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   607: pop
/*     */       //   608: aload_3
/*     */       //   609: invokeinterface nextBoolean : ()Z
/*     */       //   614: ifeq -> 658
/*     */       //   617: aload_1
/*     */       //   618: aload_2
/*     */       //   619: aload_3
/*     */       //   620: aload_0
/*     */       //   621: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   624: invokevirtual maxX : ()I
/*     */       //   627: iconst_1
/*     */       //   628: iadd
/*     */       //   629: aload_0
/*     */       //   630: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   633: invokevirtual minY : ()I
/*     */       //   636: iconst_3
/*     */       //   637: iadd
/*     */       //   638: iconst_1
/*     */       //   639: iadd
/*     */       //   640: aload_0
/*     */       //   641: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   644: invokevirtual minZ : ()I
/*     */       //   647: iconst_1
/*     */       //   648: iadd
/*     */       //   649: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */       //   652: iload #4
/*     */       //   654: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   657: pop
/*     */       //   658: aload_3
/*     */       //   659: invokeinterface nextBoolean : ()Z
/*     */       //   664: ifeq -> 708
/*     */       //   667: aload_1
/*     */       //   668: aload_2
/*     */       //   669: aload_3
/*     */       //   670: aload_0
/*     */       //   671: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   674: invokevirtual minX : ()I
/*     */       //   677: iconst_1
/*     */       //   678: iadd
/*     */       //   679: aload_0
/*     */       //   680: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   683: invokevirtual minY : ()I
/*     */       //   686: iconst_3
/*     */       //   687: iadd
/*     */       //   688: iconst_1
/*     */       //   689: iadd
/*     */       //   690: aload_0
/*     */       //   691: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   694: invokevirtual maxZ : ()I
/*     */       //   697: iconst_1
/*     */       //   698: iadd
/*     */       //   699: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */       //   702: iload #4
/*     */       //   704: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   707: pop
/*     */       //   708: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #749	-> 0
/*     */       //   #752	-> 6
/*     */       //   #755	-> 48
/*     */       //   #756	-> 85
/*     */       //   #757	-> 122
/*     */       //   #758	-> 159
/*     */       //   #760	-> 162
/*     */       //   #761	-> 199
/*     */       //   #762	-> 236
/*     */       //   #763	-> 273
/*     */       //   #765	-> 276
/*     */       //   #766	-> 313
/*     */       //   #767	-> 350
/*     */       //   #768	-> 387
/*     */       //   #770	-> 390
/*     */       //   #771	-> 427
/*     */       //   #772	-> 464
/*     */       //   #776	-> 501
/*     */       //   #777	-> 508
/*     */       //   #778	-> 517
/*     */       //   #780	-> 558
/*     */       //   #781	-> 567
/*     */       //   #783	-> 608
/*     */       //   #784	-> 617
/*     */       //   #786	-> 658
/*     */       //   #787	-> 667
/*     */       //   #790	-> 708
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	709	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftCrossing;
/*     */       //   0	709	1	startPiece	Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */       //   0	709	2	structurePieceAccessor	Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;
/*     */       //   0	709	3	random	Lnet/minecraft/util/RandomSource;
/*     */       //   6	703	4	depth	I
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
/*     */ 
/*     */     
/*     */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 794 */       if (isInInvalidLocation((LevelAccessor)level, chunkBB)) {
/*     */         return;
/*     */       }
/*     */       
/* 798 */       BlockState planks = this.type.getPlanksState();
/*     */ 
/*     */       
/* 801 */       if (this.isTwoFloored) {
/* 802 */         generateBox(level, chunkBB, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), this.boundingBox.maxX() - 1, this.boundingBox.minY() + 3 - 1, this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/* 803 */         generateBox(level, chunkBB, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxX(), this.boundingBox.minY() + 3 - 1, this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
/* 804 */         generateBox(level, chunkBB, this.boundingBox.minX() + 1, this.boundingBox.maxY() - 2, this.boundingBox.minZ(), this.boundingBox.maxX() - 1, this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/* 805 */         generateBox(level, chunkBB, this.boundingBox.minX(), this.boundingBox.maxY() - 2, this.boundingBox.minZ() + 1, this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
/* 806 */         generateBox(level, chunkBB, this.boundingBox.minX() + 1, this.boundingBox.minY() + 3, this.boundingBox.minZ() + 1, this.boundingBox.maxX() - 1, this.boundingBox.minY() + 3, this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
/*     */       } else {
/* 808 */         generateBox(level, chunkBB, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), this.boundingBox.maxX() - 1, this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/* 809 */         generateBox(level, chunkBB, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
/*     */       } 
/*     */ 
/*     */       
/* 813 */       placeSupportPillar(level, chunkBB, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxY());
/* 814 */       placeSupportPillar(level, chunkBB, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() - 1, this.boundingBox.maxY());
/* 815 */       placeSupportPillar(level, chunkBB, this.boundingBox.maxX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxY());
/* 816 */       placeSupportPillar(level, chunkBB, this.boundingBox.maxX() - 1, this.boundingBox.minY(), this.boundingBox.maxZ() - 1, this.boundingBox.maxY());
/*     */ 
/*     */ 
/*     */       
/* 820 */       int y = this.boundingBox.minY() - 1;
/* 821 */       for (int x = this.boundingBox.minX(); x <= this.boundingBox.maxX(); x++) {
/* 822 */         for (int z = this.boundingBox.minZ(); z <= this.boundingBox.maxZ(); z++) {
/* 823 */           setPlanksBlock(level, chunkBB, planks, x, y, z);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     private void placeSupportPillar(WorldGenLevel level, BoundingBox chunkBB, int x, int y0, int z, int y1) {
/* 829 */       if (!getBlock((BlockGetter)level, x, y1 + 1, z, chunkBB).isAir())
/* 830 */         generateBox(level, chunkBB, x, y0, z, x, y1, z, this.type.getPlanksState(), CAVE_AIR, false); 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MineShaftStairs
/*     */     extends MineShaftPiece {
/*     */     public MineShaftStairs(int genDepth, BoundingBox boundingBox, Direction direction, MineshaftStructure.Type type) {
/* 837 */       super(StructurePieceType.MINE_SHAFT_STAIRS, genDepth, type, boundingBox);
/* 838 */       setOrientation(direction);
/*     */     }
/*     */     
/*     */     public MineShaftStairs(CompoundTag tag) {
/* 842 */       super(StructurePieceType.MINE_SHAFT_STAIRS, tag);
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
/*     */     public static BoundingBox findStairs(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction) {
/*     */       // Byte code:
/*     */       //   0: getstatic net/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$1.$SwitchMap$net$minecraft$core$Direction : [I
/*     */       //   3: aload #5
/*     */       //   5: invokevirtual ordinal : ()I
/*     */       //   8: iaload
/*     */       //   9: tableswitch default -> 40, 1 -> 40, 2 -> 60, 3 -> 80, 4 -> 100
/*     */       //   40: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   43: dup
/*     */       //   44: iconst_0
/*     */       //   45: bipush #-5
/*     */       //   47: bipush #-8
/*     */       //   49: iconst_2
/*     */       //   50: iconst_2
/*     */       //   51: iconst_0
/*     */       //   52: invokespecial <init> : (IIIIII)V
/*     */       //   55: astore #6
/*     */       //   57: goto -> 117
/*     */       //   60: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   63: dup
/*     */       //   64: iconst_0
/*     */       //   65: bipush #-5
/*     */       //   67: iconst_0
/*     */       //   68: iconst_2
/*     */       //   69: iconst_2
/*     */       //   70: bipush #8
/*     */       //   72: invokespecial <init> : (IIIIII)V
/*     */       //   75: astore #6
/*     */       //   77: goto -> 117
/*     */       //   80: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   83: dup
/*     */       //   84: bipush #-8
/*     */       //   86: bipush #-5
/*     */       //   88: iconst_0
/*     */       //   89: iconst_0
/*     */       //   90: iconst_2
/*     */       //   91: iconst_2
/*     */       //   92: invokespecial <init> : (IIIIII)V
/*     */       //   95: astore #6
/*     */       //   97: goto -> 117
/*     */       //   100: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   103: dup
/*     */       //   104: iconst_0
/*     */       //   105: bipush #-5
/*     */       //   107: iconst_0
/*     */       //   108: bipush #8
/*     */       //   110: iconst_2
/*     */       //   111: iconst_2
/*     */       //   112: invokespecial <init> : (IIIIII)V
/*     */       //   115: astore #6
/*     */       //   117: aload #6
/*     */       //   119: iload_2
/*     */       //   120: iload_3
/*     */       //   121: iload #4
/*     */       //   123: invokevirtual move : (III)Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   126: pop
/*     */       //   127: aload_0
/*     */       //   128: aload #6
/*     */       //   130: invokeinterface findCollisionPiece : (Lnet/minecraft/world/level/levelgen/structure/BoundingBox;)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */       //   135: ifnull -> 140
/*     */       //   138: aconst_null
/*     */       //   139: areturn
/*     */       //   140: aload #6
/*     */       //   142: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #848	-> 0
/*     */       //   #851	-> 40
/*     */       //   #852	-> 57
/*     */       //   #854	-> 60
/*     */       //   #855	-> 77
/*     */       //   #857	-> 80
/*     */       //   #858	-> 97
/*     */       //   #860	-> 100
/*     */       //   #864	-> 117
/*     */       //   #866	-> 127
/*     */       //   #867	-> 138
/*     */       //   #870	-> 140
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   57	3	6	box	Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   77	3	6	box	Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   97	3	6	box	Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   0	143	0	structurePieceAccessor	Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;
/*     */       //   0	143	1	random	Lnet/minecraft/util/RandomSource;
/*     */       //   0	143	2	footX	I
/*     */       //   0	143	3	footY	I
/*     */       //   0	143	4	footZ	I
/*     */       //   0	143	5	direction	Lnet/minecraft/core/Direction;
/*     */       //   117	26	6	box	Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
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
/*     */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: invokevirtual getGenDepth : ()I
/*     */       //   4: istore #4
/*     */       //   6: aload_0
/*     */       //   7: invokevirtual getOrientation : ()Lnet/minecraft/core/Direction;
/*     */       //   10: astore #5
/*     */       //   12: aload #5
/*     */       //   14: ifnull -> 205
/*     */       //   17: getstatic net/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$1.$SwitchMap$net$minecraft$core$Direction : [I
/*     */       //   20: aload #5
/*     */       //   22: invokevirtual ordinal : ()I
/*     */       //   25: iaload
/*     */       //   26: tableswitch default -> 56, 1 -> 56, 2 -> 94, 3 -> 132, 4 -> 170
/*     */       //   56: aload_1
/*     */       //   57: aload_2
/*     */       //   58: aload_3
/*     */       //   59: aload_0
/*     */       //   60: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   63: invokevirtual minX : ()I
/*     */       //   66: aload_0
/*     */       //   67: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   70: invokevirtual minY : ()I
/*     */       //   73: aload_0
/*     */       //   74: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   77: invokevirtual minZ : ()I
/*     */       //   80: iconst_1
/*     */       //   81: isub
/*     */       //   82: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */       //   85: iload #4
/*     */       //   87: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   90: pop
/*     */       //   91: goto -> 205
/*     */       //   94: aload_1
/*     */       //   95: aload_2
/*     */       //   96: aload_3
/*     */       //   97: aload_0
/*     */       //   98: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   101: invokevirtual minX : ()I
/*     */       //   104: aload_0
/*     */       //   105: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   108: invokevirtual minY : ()I
/*     */       //   111: aload_0
/*     */       //   112: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   115: invokevirtual maxZ : ()I
/*     */       //   118: iconst_1
/*     */       //   119: iadd
/*     */       //   120: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */       //   123: iload #4
/*     */       //   125: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   128: pop
/*     */       //   129: goto -> 205
/*     */       //   132: aload_1
/*     */       //   133: aload_2
/*     */       //   134: aload_3
/*     */       //   135: aload_0
/*     */       //   136: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   139: invokevirtual minX : ()I
/*     */       //   142: iconst_1
/*     */       //   143: isub
/*     */       //   144: aload_0
/*     */       //   145: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   148: invokevirtual minY : ()I
/*     */       //   151: aload_0
/*     */       //   152: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   155: invokevirtual minZ : ()I
/*     */       //   158: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */       //   161: iload #4
/*     */       //   163: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   166: pop
/*     */       //   167: goto -> 205
/*     */       //   170: aload_1
/*     */       //   171: aload_2
/*     */       //   172: aload_3
/*     */       //   173: aload_0
/*     */       //   174: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   177: invokevirtual maxX : ()I
/*     */       //   180: iconst_1
/*     */       //   181: iadd
/*     */       //   182: aload_0
/*     */       //   183: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   186: invokevirtual minY : ()I
/*     */       //   189: aload_0
/*     */       //   190: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   193: invokevirtual minZ : ()I
/*     */       //   196: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */       //   199: iload #4
/*     */       //   201: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   204: pop
/*     */       //   205: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #875	-> 0
/*     */       //   #878	-> 6
/*     */       //   #879	-> 12
/*     */       //   #880	-> 17
/*     */       //   #883	-> 56
/*     */       //   #884	-> 91
/*     */       //   #886	-> 94
/*     */       //   #887	-> 129
/*     */       //   #889	-> 132
/*     */       //   #890	-> 167
/*     */       //   #892	-> 170
/*     */       //   #896	-> 205
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	206	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftStairs;
/*     */       //   0	206	1	startPiece	Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */       //   0	206	2	structurePieceAccessor	Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;
/*     */       //   0	206	3	random	Lnet/minecraft/util/RandomSource;
/*     */       //   6	200	4	depth	I
/*     */       //   12	194	5	orientation	Lnet/minecraft/core/Direction;
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
/*     */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 900 */       if (isInInvalidLocation((LevelAccessor)level, chunkBB)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 905 */       generateBox(level, chunkBB, 0, 5, 0, 2, 7, 1, CAVE_AIR, CAVE_AIR, false);
/*     */       
/* 907 */       generateBox(level, chunkBB, 0, 0, 7, 2, 2, 8, CAVE_AIR, CAVE_AIR, false);
/*     */       
/* 909 */       for (int i = 0; i < 5; i++)
/* 910 */         generateBox(level, chunkBB, 0, 5 - i - ((i < 4) ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, CAVE_AIR, CAVE_AIR, false); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/structures/MineshaftPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */