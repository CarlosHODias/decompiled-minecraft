/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ import com.google.common.collect.Lists;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.Tuple;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.ChestBlock;
/*      */ import net.minecraft.world.level.block.Mirror;
/*      */ import net.minecraft.world.level.block.Rotation;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ 
/*      */ public class WoodlandMansionPieces {
/*      */   public static class WoodlandMansionPiece extends TemplateStructurePiece {
/*      */     public WoodlandMansionPiece(StructureTemplateManager structureTemplateManager, String templateName, BlockPos position, Rotation rotation) {
/*   40 */       this(structureTemplateManager, templateName, position, rotation, Mirror.NONE);
/*      */     }
/*      */     
/*      */     public WoodlandMansionPiece(StructureTemplateManager structureTemplateManager, String templateName, BlockPos position, Rotation rotation, Mirror mirror) {
/*   44 */       super(StructurePieceType.WOODLAND_MANSION_PIECE, 0, structureTemplateManager, makeLocation(templateName), templateName, makeSettings(mirror, rotation), position);
/*      */     }
/*      */     
/*      */     public WoodlandMansionPiece(StructureTemplateManager structureTemplateManager, CompoundTag tag) {
/*   48 */       super(StructurePieceType.WOODLAND_MANSION_PIECE, tag, structureTemplateManager, location -> makeSettings(tag.read("Mi", Mirror.LEGACY_CODEC).orElseThrow(), tag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow()));
/*      */     }
/*      */ 
/*      */     
/*      */     protected Identifier makeTemplateLocation() {
/*   53 */       return makeLocation(this.templateName);
/*      */     }
/*      */     
/*      */     private static Identifier makeLocation(String templateName) {
/*   57 */       return Identifier.withDefaultNamespace("woodland_mansion/" + templateName);
/*      */     }
/*      */     
/*      */     private static StructurePlaceSettings makeSettings(Mirror mirror, Rotation rotation) {
/*   61 */       return new StructurePlaceSettings().setIgnoreEntities(true).setRotation(rotation).setMirror(mirror).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_BLOCK);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*   66 */       super.addAdditionalSaveData(context, tag);
/*      */       
/*   68 */       tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*   69 */       tag.store("Mi", Mirror.LEGACY_CODEC, this.placeSettings.getMirror());
/*      */     }
/*      */ 
/*      */     
/*      */     protected void handleDataMarker(String markerId, BlockPos position, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBB) {
/*   74 */       if (markerId.startsWith("Chest")) {
/*   75 */         Rotation rot = this.placeSettings.getRotation();
/*   76 */         BlockState chestState = Blocks.CHEST.defaultBlockState();
/*   77 */         if ("ChestWest".equals(markerId)) {
/*   78 */           chestState = (BlockState)chestState.setValue((Property)ChestBlock.FACING, (Comparable)rot.rotate(Direction.WEST));
/*   79 */         } else if ("ChestEast".equals(markerId)) {
/*   80 */           chestState = (BlockState)chestState.setValue((Property)ChestBlock.FACING, (Comparable)rot.rotate(Direction.EAST));
/*   81 */         } else if ("ChestSouth".equals(markerId)) {
/*   82 */           chestState = (BlockState)chestState.setValue((Property)ChestBlock.FACING, (Comparable)rot.rotate(Direction.SOUTH));
/*   83 */         } else if ("ChestNorth".equals(markerId)) {
/*   84 */           chestState = (BlockState)chestState.setValue((Property)ChestBlock.FACING, (Comparable)rot.rotate(Direction.NORTH));
/*      */         } 
/*   86 */         createChest(level, chunkBB, random, position, BuiltInLootTables.WOODLAND_MANSION, chestState);
/*      */       } else {
/*   88 */         int numberOfAllays, i; List<Mob> mobs = new ArrayList<>();
/*   89 */         switch (markerId) {
/*      */           case "Mage":
/*   91 */             mobs.add((Mob)EntityType.EVOKER.create((Level)level.getLevel(), EntitySpawnReason.STRUCTURE));
/*      */             break;
/*      */           case "Warrior":
/*   94 */             mobs.add((Mob)EntityType.VINDICATOR.create((Level)level.getLevel(), EntitySpawnReason.STRUCTURE));
/*      */             break;
/*      */           case "Group of Allays":
/*   97 */             numberOfAllays = level.getRandom().nextInt(3) + 1;
/*   98 */             for (i = 0; i < numberOfAllays; i++) {
/*   99 */               mobs.add((Mob)EntityType.ALLAY.create((Level)level.getLevel(), EntitySpawnReason.STRUCTURE));
/*      */             }
/*      */             break;
/*      */           
/*      */           default:
/*      */             return;
/*      */         } 
/*  106 */         for (Mob mob : mobs) {
/*  107 */           if (mob == null) {
/*      */             continue;
/*      */           }
/*  110 */           mob.setPersistenceRequired();
/*  111 */           mob.snapTo(position, 0.0F, 0.0F);
/*  112 */           mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.STRUCTURE, null);
/*  113 */           level.addFreshEntityWithPassengers((Entity)mob);
/*  114 */           level.setBlock(position, Blocks.AIR.defaultBlockState(), 2);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static void generateMansion(StructureTemplateManager structureTemplateManager, BlockPos origin, Rotation rotation, List<WoodlandMansionPiece> pieces, RandomSource random) {
/*  121 */     MansionGrid grid = new MansionGrid(random);
/*  122 */     MansionPiecePlacer placer = new MansionPiecePlacer(structureTemplateManager, random);
/*  123 */     placer.createMansion(origin, rotation, pieces, grid);
/*      */   }
/*      */   
/*      */   private static class PlacementData
/*      */   {
/*      */     public Rotation rotation;
/*      */     public BlockPos position;
/*      */     public String wallType;
/*      */   }
/*      */   
/*      */   private static class MansionPiecePlacer {
/*      */     private final StructureTemplateManager structureTemplateManager;
/*      */     private final RandomSource random;
/*      */     private int startX;
/*      */     private int startY;
/*      */     
/*      */     public MansionPiecePlacer(StructureTemplateManager structureTemplateManager, RandomSource random) {
/*  140 */       this.structureTemplateManager = structureTemplateManager;
/*  141 */       this.random = random;
/*      */     }
/*      */     
/*      */     public void createMansion(BlockPos origin, Rotation rotation, List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.MansionGrid mansion) {
/*  145 */       WoodlandMansionPieces.PlacementData data = new WoodlandMansionPieces.PlacementData();
/*  146 */       data.position = origin;
/*  147 */       data.rotation = rotation;
/*  148 */       data.wallType = "wall_flat";
/*      */       
/*  150 */       WoodlandMansionPieces.PlacementData secondData = new WoodlandMansionPieces.PlacementData();
/*      */ 
/*      */       
/*  153 */       entrance(pieces, data);
/*  154 */       secondData.position = data.position.above(8);
/*  155 */       secondData.rotation = data.rotation;
/*  156 */       secondData.wallType = "wall_window";
/*      */       
/*  158 */       if (!pieces.isEmpty());
/*      */ 
/*      */ 
/*      */       
/*  162 */       WoodlandMansionPieces.SimpleGrid baseGrid = mansion.baseGrid;
/*  163 */       WoodlandMansionPieces.SimpleGrid thirdGrid = mansion.thirdFloorGrid;
/*      */       
/*  165 */       this.startX = mansion.entranceX + 1;
/*  166 */       this.startY = mansion.entranceY + 1;
/*  167 */       int endX = mansion.entranceX + 1;
/*  168 */       int endY = mansion.entranceY;
/*      */       
/*  170 */       traverseOuterWalls(pieces, data, baseGrid, Direction.SOUTH, this.startX, this.startY, endX, endY);
/*  171 */       traverseOuterWalls(pieces, secondData, baseGrid, Direction.SOUTH, this.startX, this.startY, endX, endY);
/*      */ 
/*      */       
/*  174 */       WoodlandMansionPieces.PlacementData thirdData = new WoodlandMansionPieces.PlacementData();
/*  175 */       thirdData.position = data.position.above(19);
/*  176 */       thirdData.rotation = data.rotation;
/*  177 */       thirdData.wallType = "wall_window";
/*      */       
/*      */       boolean done = false;
/*  180 */       for (int y = 0; y < thirdGrid.height && !done; y++) {
/*  181 */         for (int x = thirdGrid.width - 1; x >= 0 && !done; x--) {
/*  182 */           if (WoodlandMansionPieces.MansionGrid.isHouse(thirdGrid, x, y)) {
/*  183 */             thirdData.position = thirdData.position.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
/*  184 */             thirdData.position = thirdData.position.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/*  185 */             traverseWallPiece(pieces, thirdData);
/*  186 */             traverseOuterWalls(pieces, thirdData, thirdGrid, Direction.SOUTH, x, y, x, y);
/*  187 */             done = true;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  193 */       createRoof(pieces, origin.above(16), rotation, baseGrid, thirdGrid);
/*  194 */       createRoof(pieces, origin.above(27), rotation, thirdGrid, null);
/*      */       
/*  196 */       if (!pieces.isEmpty());
/*      */ 
/*      */ 
/*      */       
/*  200 */       WoodlandMansionPieces.FloorRoomCollection[] roomCollections = new WoodlandMansionPieces.FloorRoomCollection[3];
/*  201 */       roomCollections[0] = new WoodlandMansionPieces.FirstFloorRoomCollection();
/*  202 */       roomCollections[1] = new WoodlandMansionPieces.SecondFloorRoomCollection();
/*  203 */       roomCollections[2] = new WoodlandMansionPieces.ThirdFloorRoomCollection();
/*      */       
/*  205 */       for (int floorNum = 0; floorNum < 3; floorNum++) {
/*  206 */         BlockPos floorOrigin = origin.above(8 * floorNum + ((floorNum == 2) ? 3 : 0));
/*  207 */         WoodlandMansionPieces.SimpleGrid rooms = mansion.floorRooms[floorNum];
/*  208 */         WoodlandMansionPieces.SimpleGrid grid = (floorNum == 2) ? thirdGrid : baseGrid;
/*      */ 
/*      */         
/*  211 */         String southPiece = (floorNum == 0) ? "carpet_south_1" : "carpet_south_2";
/*  212 */         String westPiece = (floorNum == 0) ? "carpet_west_1" : "carpet_west_2";
/*  213 */         for (int i = 0; i < grid.height; i++) {
/*  214 */           for (int x = 0; x < grid.width; x++) {
/*  215 */             if (grid.get(x, i) == 1) {
/*  216 */               BlockPos pos = floorOrigin.relative(rotation.rotate(Direction.SOUTH), 8 + (i - this.startY) * 8);
/*  217 */               pos = pos.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/*  218 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "corridor_floor", pos, rotation));
/*      */               
/*  220 */               if (grid.get(x, i - 1) == 1 || (rooms.get(x, i - 1) & 0x800000) == 8388608) {
/*  221 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "carpet_north", pos.relative(rotation.rotate(Direction.EAST), 1).above(), rotation));
/*      */               }
/*  223 */               if (grid.get(x + 1, i) == 1 || (rooms.get(x + 1, i) & 0x800000) == 8388608) {
/*  224 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "carpet_east", pos.relative(rotation.rotate(Direction.SOUTH), 1).relative(rotation.rotate(Direction.EAST), 5).above(), rotation));
/*      */               }
/*  226 */               if (grid.get(x, i + 1) == 1 || (rooms.get(x, i + 1) & 0x800000) == 8388608) {
/*  227 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, southPiece, pos.relative(rotation.rotate(Direction.SOUTH), 5).relative(rotation.rotate(Direction.WEST), 1), rotation));
/*      */               }
/*  229 */               if (grid.get(x - 1, i) == 1 || (rooms.get(x - 1, i) & 0x800000) == 8388608) {
/*  230 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, westPiece, pos.relative(rotation.rotate(Direction.WEST), 1).relative(rotation.rotate(Direction.NORTH), 1), rotation));
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/*  236 */         String wallPiece = (floorNum == 0) ? "indoors_wall_1" : "indoors_wall_2";
/*  237 */         String doorPiece = (floorNum == 0) ? "indoors_door_1" : "indoors_door_2";
/*  238 */         List<Direction> doorDirs = Lists.newArrayList();
/*  239 */         for (int j = 0; j < grid.height; j++) {
/*  240 */           for (int x = 0; x < grid.width; x++) {
/*  241 */             boolean thirdFloorStartRoom = (floorNum == 2 && grid.get(x, j) == 3);
/*  242 */             if (grid.get(x, j) == 2 || thirdFloorStartRoom) {
/*  243 */               int roomData = rooms.get(x, j);
/*  244 */               int roomType = roomData & 0xF0000;
/*  245 */               int roomId = roomData & 0xFFFF;
/*      */ 
/*      */               
/*  248 */               thirdFloorStartRoom = (thirdFloorStartRoom && (roomData & 0x800000) == 8388608);
/*      */               
/*  250 */               doorDirs.clear();
/*  251 */               if ((roomData & 0x200000) == 2097152) {
/*  252 */                 for (Direction direction : (Iterable<Direction>)Direction.Plane.HORIZONTAL) {
/*  253 */                   if (grid.get(x + direction.getStepX(), j + direction.getStepZ()) == 1) {
/*  254 */                     doorDirs.add(direction);
/*      */                   }
/*      */                 } 
/*      */               }
/*  258 */               Direction doorDir = null;
/*  259 */               if (!doorDirs.isEmpty()) {
/*  260 */                 doorDir = doorDirs.get(this.random.nextInt(doorDirs.size()));
/*  261 */               } else if ((roomData & 0x100000) == 1048576) {
/*      */                 
/*  263 */                 doorDir = Direction.UP;
/*      */               } 
/*      */               
/*  266 */               BlockPos roomPos = floorOrigin.relative(rotation.rotate(Direction.SOUTH), 8 + (j - this.startY) * 8);
/*  267 */               roomPos = roomPos.relative(rotation.rotate(Direction.EAST), -1 + (x - this.startX) * 8);
/*      */               
/*  269 */               if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, j) && !mansion.isRoomId(grid, x - 1, j, floorNum, roomId)) {
/*  270 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (doorDir == Direction.WEST) ? doorPiece : wallPiece, roomPos, rotation));
/*      */               }
/*  272 */               if (grid.get(x + 1, j) == 1 && !thirdFloorStartRoom) {
/*  273 */                 BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 8);
/*  274 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (doorDir == Direction.EAST) ? doorPiece : wallPiece, pos, rotation));
/*      */               } 
/*  276 */               if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x, j + 1) && !mansion.isRoomId(grid, x, j + 1, floorNum, roomId)) {
/*  277 */                 BlockPos pos = roomPos.relative(rotation.rotate(Direction.SOUTH), 7);
/*  278 */                 pos = pos.relative(rotation.rotate(Direction.EAST), 7);
/*  279 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (doorDir == Direction.SOUTH) ? doorPiece : wallPiece, pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*      */               } 
/*  281 */               if (grid.get(x, j - 1) == 1 && !thirdFloorStartRoom) {
/*  282 */                 BlockPos pos = roomPos.relative(rotation.rotate(Direction.NORTH), 1);
/*  283 */                 pos = pos.relative(rotation.rotate(Direction.EAST), 7);
/*  284 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (doorDir == Direction.NORTH) ? doorPiece : wallPiece, pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*      */               } 
/*      */               
/*  287 */               if (roomType == 65536) {
/*  288 */                 addRoom1x1(pieces, roomPos, rotation, doorDir, roomCollections[floorNum]);
/*  289 */               } else if (roomType == 131072 && doorDir != null) {
/*      */                 
/*  291 */                 Direction roomDir = mansion.get1x2RoomDirection(grid, x, j, floorNum, roomId);
/*  292 */                 boolean isStairsRoom = ((roomData & 0x400000) == 4194304);
/*  293 */                 addRoom1x2(pieces, roomPos, rotation, roomDir, doorDir, roomCollections[floorNum], isStairsRoom);
/*  294 */               } else if (roomType == 262144 && doorDir != null && doorDir != Direction.UP) {
/*      */                 
/*  296 */                 Direction roomDir = doorDir.getClockWise();
/*  297 */                 if (!mansion.isRoomId(grid, x + roomDir.getStepX(), j + roomDir.getStepZ(), floorNum, roomId)) {
/*  298 */                   roomDir = roomDir.getOpposite();
/*      */                 }
/*  300 */                 addRoom2x2(pieces, roomPos, rotation, roomDir, doorDir, roomCollections[floorNum]);
/*  301 */               } else if (roomType == 262144 && doorDir == Direction.UP) {
/*  302 */                 addRoom2x2Secret(pieces, roomPos, rotation, roomCollections[floorNum]);
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     private void traverseOuterWalls(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data, WoodlandMansionPieces.SimpleGrid grid, Direction gridDirection, int startX, int startY, int endX, int endY) {
/*  311 */       int gridX = startX;
/*  312 */       int gridY = startY;
/*  313 */       Direction startDirection = gridDirection;
/*      */       
/*      */       do {
/*  316 */         if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, gridX + gridDirection.getStepX(), gridY + gridDirection.getStepZ())) {
/*      */           
/*  318 */           traverseTurn(pieces, data);
/*  319 */           gridDirection = gridDirection.getClockWise();
/*  320 */           if (gridX != endX || gridY != endY || startDirection != gridDirection) {
/*  321 */             traverseWallPiece(pieces, data);
/*      */           }
/*  323 */         } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, gridX + gridDirection.getStepX(), gridY + gridDirection.getStepZ()) && WoodlandMansionPieces.MansionGrid.isHouse(grid, gridX + gridDirection.getStepX() + gridDirection.getCounterClockWise().getStepX(), gridY + gridDirection.getStepZ() + gridDirection.getCounterClockWise().getStepZ())) {
/*      */           
/*  325 */           traverseInnerTurn(pieces, data);
/*  326 */           gridX += gridDirection.getStepX();
/*  327 */           gridY += gridDirection.getStepZ();
/*  328 */           gridDirection = gridDirection.getCounterClockWise();
/*      */         } else {
/*  330 */           gridX += gridDirection.getStepX();
/*  331 */           gridY += gridDirection.getStepZ();
/*  332 */           if (gridX != endX || gridY != endY || startDirection != gridDirection) {
/*  333 */             traverseWallPiece(pieces, data);
/*      */           }
/*      */         } 
/*  336 */       } while (gridX != endX || gridY != endY || startDirection != gridDirection);
/*      */     }
/*      */ 
/*      */     
/*      */     private void createRoof(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roofOrigin, Rotation rotation, WoodlandMansionPieces.SimpleGrid grid, WoodlandMansionPieces.SimpleGrid aboveGrid) {
/*  341 */       for (int y = 0; y < grid.height; y++) {
/*  342 */         for (int x = 0; x < grid.width; x++) {
/*  343 */           BlockPos position = roofOrigin;
/*  344 */           position = position.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
/*  345 */           position = position.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/*      */ 
/*      */           
/*  348 */           boolean isAbove = (aboveGrid != null && WoodlandMansionPieces.MansionGrid.isHouse(aboveGrid, x, y));
/*      */           
/*  350 */           if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y) && !isAbove) {
/*  351 */             pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof", position.above(3), rotation));
/*      */             
/*  353 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, y)) {
/*  354 */               BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 6);
/*  355 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", p2, rotation));
/*      */             } 
/*  357 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y)) {
/*  358 */               BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 0);
/*  359 */               p2 = p2.relative(rotation.rotate(Direction.SOUTH), 7);
/*  360 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", p2, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*      */             } 
/*  362 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y - 1)) {
/*  363 */               BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 1);
/*  364 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*      */             } 
/*  366 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1)) {
/*  367 */               BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 6);
/*  368 */               p2 = p2.relative(rotation.rotate(Direction.SOUTH), 6);
/*  369 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", p2, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  375 */       if (aboveGrid != null) {
/*  376 */         for (int j = 0; j < grid.height; j++) {
/*  377 */           for (int x = 0; x < grid.width; x++) {
/*  378 */             BlockPos position = roofOrigin;
/*  379 */             position = position.relative(rotation.rotate(Direction.SOUTH), 8 + (j - this.startY) * 8);
/*  380 */             position = position.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/*      */ 
/*      */             
/*  383 */             boolean isAbove = WoodlandMansionPieces.MansionGrid.isHouse(aboveGrid, x, j);
/*      */             
/*  385 */             if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x, j) && isAbove) {
/*      */               
/*  387 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, j)) {
/*  388 */                 BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 7);
/*  389 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", p2, rotation));
/*      */               } 
/*  391 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, j)) {
/*  392 */                 BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 1);
/*  393 */                 p2 = p2.relative(rotation.rotate(Direction.SOUTH), 6);
/*  394 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", p2, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*      */               } 
/*  396 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, j - 1)) {
/*  397 */                 BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 0);
/*  398 */                 p2 = p2.relative(rotation.rotate(Direction.NORTH), 1);
/*  399 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*      */               } 
/*  401 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, j + 1)) {
/*  402 */                 BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 6);
/*  403 */                 p2 = p2.relative(rotation.rotate(Direction.SOUTH), 7);
/*  404 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", p2, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*      */               } 
/*      */               
/*  407 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, j)) {
/*  408 */                 if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, j - 1)) {
/*  409 */                   BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 7);
/*  410 */                   p2 = p2.relative(rotation.rotate(Direction.NORTH), 2);
/*  411 */                   pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", p2, rotation));
/*      */                 } 
/*  413 */                 if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, j + 1)) {
/*  414 */                   BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 8);
/*  415 */                   p2 = p2.relative(rotation.rotate(Direction.SOUTH), 7);
/*  416 */                   pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", p2, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*      */                 } 
/*      */               } 
/*  419 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, j)) {
/*  420 */                 if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, j - 1)) {
/*  421 */                   BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 2);
/*  422 */                   p2 = p2.relative(rotation.rotate(Direction.NORTH), 1);
/*  423 */                   pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*      */                 } 
/*  425 */                 if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, j + 1)) {
/*  426 */                   BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 1);
/*  427 */                   p2 = p2.relative(rotation.rotate(Direction.SOUTH), 8);
/*  428 */                   pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", p2, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*      */       
/*  436 */       for (int i = 0; i < grid.height; i++) {
/*  437 */         for (int x = 0; x < grid.width; x++) {
/*  438 */           BlockPos position = roofOrigin;
/*  439 */           position = position.relative(rotation.rotate(Direction.SOUTH), 8 + (i - this.startY) * 8);
/*  440 */           position = position.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/*      */ 
/*      */           
/*  443 */           boolean isAbove = (aboveGrid != null && WoodlandMansionPieces.MansionGrid.isHouse(aboveGrid, x, i));
/*      */           
/*  445 */           if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x, i) && !isAbove) {
/*  446 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, i)) {
/*  447 */               BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 6);
/*  448 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, i + 1)) {
/*  449 */                 BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 6);
/*  450 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", p3, rotation));
/*  451 */               } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, i + 1)) {
/*  452 */                 BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 5);
/*  453 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", p3, rotation));
/*      */               } 
/*  455 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, i - 1)) {
/*  456 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*  457 */               } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, i - 1)) {
/*  458 */                 BlockPos p3 = position.relative(rotation.rotate(Direction.EAST), 9);
/*  459 */                 p3 = p3.relative(rotation.rotate(Direction.NORTH), 2);
/*  460 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", p3, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*      */               } 
/*      */             } 
/*  463 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, i)) {
/*  464 */               BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 0);
/*  465 */               p2 = p2.relative(rotation.rotate(Direction.SOUTH), 0);
/*  466 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, i + 1)) {
/*  467 */                 BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 6);
/*  468 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", p3, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*  469 */               } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, i + 1)) {
/*  470 */                 BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 8);
/*  471 */                 p3 = p3.relative(rotation.rotate(Direction.WEST), 3);
/*  472 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", p3, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*      */               } 
/*  474 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, i - 1)) {
/*  475 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", p2, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*  476 */               } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, i - 1)) {
/*  477 */                 BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 1);
/*  478 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", p3, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     private void entrance(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data) {
/*  487 */       Direction west = data.rotation.rotate(Direction.WEST);
/*  488 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "entrance", data.position.relative(west, 9), data.rotation));
/*  489 */       data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), 16);
/*      */     }
/*      */     
/*      */     private void traverseWallPiece(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data) {
/*  493 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, data.wallType, data.position.relative(data.rotation.rotate(Direction.EAST), 7), data.rotation));
/*  494 */       data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), 8);
/*      */     }
/*      */     
/*      */     private void traverseTurn(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data) {
/*  498 */       data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), -1);
/*  499 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "wall_corner", data.position, data.rotation));
/*  500 */       data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), -7);
/*  501 */       data.position = data.position.relative(data.rotation.rotate(Direction.WEST), -6);
/*  502 */       data.rotation = data.rotation.getRotated(Rotation.CLOCKWISE_90);
/*      */     }
/*      */     
/*      */     private void traverseInnerTurn(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data) {
/*  506 */       data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), 6);
/*  507 */       data.position = data.position.relative(data.rotation.rotate(Direction.EAST), 8);
/*  508 */       data.rotation = data.rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/*      */     }
/*      */     
/*      */     private void addRoom1x1(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roomPos, Rotation rotation, Direction doorDir, WoodlandMansionPieces.FloorRoomCollection rooms) {
/*  512 */       Rotation pieceRot = Rotation.NONE;
/*  513 */       String roomType = rooms.get1x1(this.random);
/*  514 */       if (doorDir != Direction.EAST) {
/*  515 */         if (doorDir == Direction.NORTH) {
/*  516 */           pieceRot = pieceRot.getRotated(Rotation.COUNTERCLOCKWISE_90);
/*  517 */         } else if (doorDir == Direction.WEST) {
/*  518 */           pieceRot = pieceRot.getRotated(Rotation.CLOCKWISE_180);
/*  519 */         } else if (doorDir == Direction.SOUTH) {
/*  520 */           pieceRot = pieceRot.getRotated(Rotation.CLOCKWISE_90);
/*      */         } else {
/*      */           
/*  523 */           roomType = rooms.get1x1Secret(this.random);
/*      */         } 
/*      */       }
/*  526 */       BlockPos orientation = StructureTemplate.getZeroPositionWithTransform(new BlockPos(1, 0, 0), Mirror.NONE, pieceRot, 7, 7);
/*  527 */       pieceRot = pieceRot.getRotated(rotation);
/*  528 */       orientation = orientation.rotate(rotation);
/*  529 */       BlockPos pos = roomPos.offset(orientation.getX(), 0, orientation.getZ());
/*  530 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, roomType, pos, pieceRot));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void addRoom1x2(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roomPos, Rotation rotation, Direction roomDir, Direction doorDir, WoodlandMansionPieces.FloorRoomCollection rooms, boolean isStairsRoom) {
/*  537 */       if (doorDir == Direction.EAST && roomDir == Direction.SOUTH) {
/*      */ 
/*      */         
/*  540 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/*  541 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation));
/*  542 */       } else if (doorDir == Direction.EAST && roomDir == Direction.NORTH) {
/*      */ 
/*      */         
/*  545 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/*  546 */         pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/*  547 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation, Mirror.LEFT_RIGHT));
/*  548 */       } else if (doorDir == Direction.WEST && roomDir == Direction.NORTH) {
/*      */ 
/*      */         
/*  551 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/*  552 */         pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/*  553 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*  554 */       } else if (doorDir == Direction.WEST && roomDir == Direction.SOUTH) {
/*      */ 
/*      */         
/*  557 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/*  558 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation, Mirror.FRONT_BACK));
/*  559 */       } else if (doorDir == Direction.SOUTH && roomDir == Direction.EAST) {
/*      */ 
/*      */         
/*  562 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/*  563 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_90), Mirror.LEFT_RIGHT));
/*  564 */       } else if (doorDir == Direction.SOUTH && roomDir == Direction.WEST) {
/*      */ 
/*      */         
/*  567 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/*  568 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*  569 */       } else if (doorDir == Direction.NORTH && roomDir == Direction.WEST) {
/*      */ 
/*      */         
/*  572 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/*  573 */         pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/*  574 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_90), Mirror.FRONT_BACK));
/*  575 */       } else if (doorDir == Direction.NORTH && roomDir == Direction.EAST) {
/*      */ 
/*      */         
/*  578 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/*  579 */         pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/*  580 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*  581 */       } else if (doorDir == Direction.SOUTH && roomDir == Direction.NORTH) {
/*      */ 
/*      */ 
/*      */         
/*  585 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/*  586 */         pos = pos.relative(rotation.rotate(Direction.NORTH), 8);
/*  587 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation));
/*  588 */       } else if (doorDir == Direction.NORTH && roomDir == Direction.SOUTH) {
/*      */ 
/*      */ 
/*      */         
/*  592 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/*  593 */         pos = pos.relative(rotation.rotate(Direction.SOUTH), 14);
/*  594 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*  595 */       } else if (doorDir == Direction.WEST && roomDir == Direction.EAST) {
/*      */         
/*  597 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 15);
/*  598 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*  599 */       } else if (doorDir == Direction.EAST && roomDir == Direction.WEST) {
/*      */         
/*  601 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.WEST), 7);
/*  602 */         pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/*  603 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*  604 */       } else if (doorDir == Direction.UP && roomDir == Direction.EAST) {
/*      */         
/*  606 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 15);
/*  607 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2Secret(this.random), pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*  608 */       } else if (doorDir == Direction.UP && roomDir == Direction.SOUTH) {
/*      */ 
/*      */ 
/*      */         
/*  612 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/*  613 */         pos = pos.relative(rotation.rotate(Direction.NORTH), 0);
/*  614 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2Secret(this.random), pos, rotation));
/*      */       } 
/*      */     }
/*      */     
/*      */     private void addRoom2x2(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roomPos, Rotation rotation, Direction roomDir, Direction doorDir, WoodlandMansionPieces.FloorRoomCollection rooms) {
/*  619 */       int east = 0;
/*  620 */       int south = 0;
/*  621 */       Rotation rot = rotation;
/*  622 */       Mirror mirror = Mirror.NONE;
/*      */ 
/*      */ 
/*      */       
/*  626 */       if (doorDir == Direction.EAST && roomDir == Direction.SOUTH) {
/*      */ 
/*      */         
/*  629 */         east = -7;
/*  630 */       } else if (doorDir == Direction.EAST && roomDir == Direction.NORTH) {
/*      */ 
/*      */         
/*  633 */         east = -7;
/*  634 */         south = 6;
/*  635 */         mirror = Mirror.LEFT_RIGHT;
/*  636 */       } else if (doorDir == Direction.NORTH && roomDir == Direction.EAST) {
/*      */ 
/*      */ 
/*      */         
/*  640 */         east = 1;
/*  641 */         south = 14;
/*  642 */         rot = rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/*  643 */       } else if (doorDir == Direction.NORTH && roomDir == Direction.WEST) {
/*      */ 
/*      */ 
/*      */         
/*  647 */         east = 7;
/*  648 */         south = 14;
/*  649 */         rot = rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/*  650 */         mirror = Mirror.LEFT_RIGHT;
/*  651 */       } else if (doorDir == Direction.SOUTH && roomDir == Direction.WEST) {
/*      */ 
/*      */ 
/*      */         
/*  655 */         east = 7;
/*  656 */         south = -8;
/*  657 */         rot = rotation.getRotated(Rotation.CLOCKWISE_90);
/*  658 */       } else if (doorDir == Direction.SOUTH && roomDir == Direction.EAST) {
/*      */ 
/*      */ 
/*      */         
/*  662 */         east = 1;
/*  663 */         south = -8;
/*  664 */         rot = rotation.getRotated(Rotation.CLOCKWISE_90);
/*  665 */         mirror = Mirror.LEFT_RIGHT;
/*  666 */       } else if (doorDir == Direction.WEST && roomDir == Direction.NORTH) {
/*      */ 
/*      */         
/*  669 */         east = 15;
/*  670 */         south = 6;
/*  671 */         rot = rotation.getRotated(Rotation.CLOCKWISE_180);
/*  672 */       } else if (doorDir == Direction.WEST && roomDir == Direction.SOUTH) {
/*      */ 
/*      */         
/*  675 */         east = 15;
/*  676 */         mirror = Mirror.FRONT_BACK;
/*      */       } 
/*      */       
/*  679 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), east);
/*  680 */       pos = pos.relative(rotation.rotate(Direction.SOUTH), south);
/*  681 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get2x2(this.random), pos, rot, mirror));
/*      */     }
/*      */     
/*      */     private void addRoom2x2Secret(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roomPos, Rotation rotation, WoodlandMansionPieces.FloorRoomCollection rooms) {
/*  685 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/*  686 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get2x2Secret(this.random), pos, rotation, Mirror.NONE));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class MansionGrid
/*      */   {
/*      */     private static final int DEFAULT_SIZE = 11;
/*      */     
/*      */     private static final int CLEAR = 0;
/*      */     
/*      */     private static final int CORRIDOR = 1;
/*      */     
/*      */     private static final int ROOM = 2;
/*      */     private static final int START_ROOM = 3;
/*      */     private static final int TEST_ROOM = 4;
/*      */     private static final int BLOCKED = 5;
/*      */     private static final int ROOM_1x1 = 65536;
/*      */     private static final int ROOM_1x2 = 131072;
/*      */     private static final int ROOM_2x2 = 262144;
/*      */     private static final int ROOM_ORIGIN_FLAG = 1048576;
/*      */     private static final int ROOM_DOOR_FLAG = 2097152;
/*      */     private static final int ROOM_STAIRS_FLAG = 4194304;
/*      */     private static final int ROOM_CORRIDOR_FLAG = 8388608;
/*      */     private static final int ROOM_TYPE_MASK = 983040;
/*      */     private static final int ROOM_ID_MASK = 65535;
/*      */     private final RandomSource random;
/*      */     private final WoodlandMansionPieces.SimpleGrid baseGrid;
/*      */     private final WoodlandMansionPieces.SimpleGrid thirdFloorGrid;
/*      */     private final WoodlandMansionPieces.SimpleGrid[] floorRooms;
/*      */     private final int entranceX;
/*      */     private final int entranceY;
/*      */     
/*      */     public MansionGrid(RandomSource random) {
/*  720 */       this.random = random;
/*      */       
/*  722 */       int houseSize = 11;
/*  723 */       this.entranceX = 7;
/*  724 */       this.entranceY = 4;
/*      */       
/*  726 */       this.baseGrid = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/*  727 */       this.baseGrid.set(this.entranceX, this.entranceY, this.entranceX + 1, this.entranceY + 1, 3);
/*  728 */       this.baseGrid.set(this.entranceX - 1, this.entranceY, this.entranceX - 1, this.entranceY + 1, 2);
/*  729 */       this.baseGrid.set(this.entranceX + 2, this.entranceY - 2, this.entranceX + 3, this.entranceY + 3, 5);
/*  730 */       this.baseGrid.set(this.entranceX + 1, this.entranceY - 2, this.entranceX + 1, this.entranceY - 1, 1);
/*  731 */       this.baseGrid.set(this.entranceX + 1, this.entranceY + 2, this.entranceX + 1, this.entranceY + 3, 1);
/*  732 */       this.baseGrid.set(this.entranceX - 1, this.entranceY - 1, 1);
/*  733 */       this.baseGrid.set(this.entranceX - 1, this.entranceY + 2, 1);
/*      */       
/*  735 */       this.baseGrid.set(0, 0, 11, 1, 5);
/*  736 */       this.baseGrid.set(0, 9, 11, 11, 5);
/*      */       
/*  738 */       recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY - 2, Direction.WEST, 6);
/*  739 */       recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY + 3, Direction.WEST, 6);
/*  740 */       recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY - 1, Direction.WEST, 3);
/*  741 */       recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY + 2, Direction.WEST, 3);
/*  742 */       while (cleanEdges(this.baseGrid));
/*      */ 
/*      */       
/*  745 */       this.floorRooms = new WoodlandMansionPieces.SimpleGrid[3];
/*  746 */       this.floorRooms[0] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/*  747 */       this.floorRooms[1] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/*  748 */       this.floorRooms[2] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/*  749 */       identifyRooms(this.baseGrid, this.floorRooms[0]);
/*  750 */       identifyRooms(this.baseGrid, this.floorRooms[1]);
/*      */ 
/*      */       
/*  753 */       this.floorRooms[0].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
/*  754 */       this.floorRooms[1].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
/*      */       
/*  756 */       this.thirdFloorGrid = new WoodlandMansionPieces.SimpleGrid(this.baseGrid.width, this.baseGrid.height, 5);
/*  757 */       setupThirdFloor();
/*  758 */       identifyRooms(this.thirdFloorGrid, this.floorRooms[2]);
/*      */     }
/*      */     
/*      */     public static boolean isHouse(WoodlandMansionPieces.SimpleGrid grid, int x, int y) {
/*  762 */       int value = grid.get(x, y);
/*  763 */       return (value == 1 || value == 2 || value == 3 || value == 4);
/*      */     }
/*      */     
/*      */     public boolean isRoomId(WoodlandMansionPieces.SimpleGrid grid, int x, int y, int floor, int roomId) {
/*  767 */       return ((this.floorRooms[floor].get(x, y) & 0xFFFF) == roomId);
/*      */     }
/*      */     
/*      */     public Direction get1x2RoomDirection(WoodlandMansionPieces.SimpleGrid grid, int x, int y, int floorNum, int roomId) {
/*  771 */       for (Direction direction : (Iterable<Direction>)Direction.Plane.HORIZONTAL) {
/*  772 */         if (isRoomId(grid, x + direction.getStepX(), y + direction.getStepZ(), floorNum, roomId)) {
/*  773 */           return direction;
/*      */         }
/*      */       } 
/*  776 */       return null;
/*      */     }
/*      */     
/*      */     private void recursiveCorridor(WoodlandMansionPieces.SimpleGrid grid, int x, int y, Direction heading, int depth) {
/*  780 */       if (depth <= 0) {
/*      */         return;
/*      */       }
/*      */       
/*  784 */       grid.set(x, y, 1);
/*  785 */       grid.setif(x + heading.getStepX(), y + heading.getStepZ(), 0, 1);
/*      */       
/*  787 */       for (int attempts = 0; attempts < 8; attempts++) {
/*  788 */         Direction nextDir = Direction.from2DDataValue(this.random.nextInt(4));
/*  789 */         if (nextDir != heading.getOpposite())
/*      */         {
/*      */           
/*  792 */           if (nextDir != Direction.EAST || !this.random.nextBoolean()) {
/*      */ 
/*      */ 
/*      */             
/*  796 */             int nx = x + heading.getStepX();
/*  797 */             int ny = y + heading.getStepZ();
/*  798 */             if (grid.get(nx + nextDir.getStepX(), ny + nextDir.getStepZ()) == 0 && grid.get(nx + nextDir.getStepX() * 2, ny + nextDir.getStepZ() * 2) == 0) {
/*  799 */               recursiveCorridor(grid, x + heading.getStepX() + nextDir.getStepX(), y + heading.getStepZ() + nextDir.getStepZ(), nextDir, depth - 1); break;
/*      */             } 
/*      */           }  } 
/*      */       } 
/*  803 */       Direction cw = heading.getClockWise();
/*  804 */       Direction ccw = heading.getCounterClockWise();
/*  805 */       grid.setif(x + cw.getStepX(), y + cw.getStepZ(), 0, 2);
/*  806 */       grid.setif(x + ccw.getStepX(), y + ccw.getStepZ(), 0, 2);
/*      */       
/*  808 */       grid.setif(x + heading.getStepX() + cw.getStepX(), y + heading.getStepZ() + cw.getStepZ(), 0, 2);
/*  809 */       grid.setif(x + heading.getStepX() + ccw.getStepX(), y + heading.getStepZ() + ccw.getStepZ(), 0, 2);
/*  810 */       grid.setif(x + heading.getStepX() * 2, y + heading.getStepZ() * 2, 0, 2);
/*  811 */       grid.setif(x + cw.getStepX() * 2, y + cw.getStepZ() * 2, 0, 2);
/*  812 */       grid.setif(x + ccw.getStepX() * 2, y + ccw.getStepZ() * 2, 0, 2);
/*      */     }
/*      */     
/*      */     private boolean cleanEdges(WoodlandMansionPieces.SimpleGrid grid) {
/*      */       boolean touched = false;
/*  817 */       for (int y = 0; y < grid.height; y++) {
/*  818 */         for (int x = 0; x < grid.width; x++) {
/*  819 */           if (grid.get(x, y) == 0) {
/*  820 */             int directNeighbors = 0;
/*  821 */             directNeighbors += isHouse(grid, x + 1, y) ? 1 : 0;
/*  822 */             directNeighbors += isHouse(grid, x - 1, y) ? 1 : 0;
/*  823 */             directNeighbors += isHouse(grid, x, y + 1) ? 1 : 0;
/*  824 */             directNeighbors += isHouse(grid, x, y - 1) ? 1 : 0;
/*      */             
/*  826 */             if (directNeighbors >= 3) {
/*      */               
/*  828 */               grid.set(x, y, 2);
/*  829 */               touched = true;
/*  830 */             } else if (directNeighbors == 2) {
/*      */               
/*  832 */               int diagonalNeighbors = 0;
/*  833 */               diagonalNeighbors += isHouse(grid, x + 1, y + 1) ? 1 : 0;
/*  834 */               diagonalNeighbors += isHouse(grid, x - 1, y + 1) ? 1 : 0;
/*  835 */               diagonalNeighbors += isHouse(grid, x + 1, y - 1) ? 1 : 0;
/*  836 */               diagonalNeighbors += isHouse(grid, x - 1, y - 1) ? 1 : 0;
/*  837 */               if (diagonalNeighbors <= 1) {
/*  838 */                 grid.set(x, y, 2);
/*  839 */                 touched = true;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*  845 */       return touched;
/*      */     }
/*      */ 
/*      */     
/*      */     private void setupThirdFloor() {
/*  850 */       List<Tuple<Integer, Integer>> potentialRooms = Lists.newArrayList();
/*  851 */       WoodlandMansionPieces.SimpleGrid floor = this.floorRooms[1];
/*  852 */       for (int y = 0; y < this.thirdFloorGrid.height; y++) {
/*  853 */         for (int x = 0; x < this.thirdFloorGrid.width; x++) {
/*  854 */           int j = floor.get(x, y);
/*  855 */           int roomType = j & 0xF0000;
/*  856 */           if (roomType == 131072 && (j & 0x200000) == 2097152) {
/*  857 */             potentialRooms.add(new Tuple(x, y));
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  862 */       if (potentialRooms.isEmpty()) {
/*      */         
/*  864 */         this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
/*      */         
/*      */         return;
/*      */       } 
/*  868 */       Tuple<Integer, Integer> roomPos = potentialRooms.get(this.random.nextInt(potentialRooms.size()));
/*  869 */       int roomData = floor.get((Integer)roomPos.getA(), (Integer)roomPos.getB());
/*  870 */       floor.set((Integer)roomPos.getA(), (Integer)roomPos.getB(), roomData | 0x400000);
/*  871 */       Direction roomDir = get1x2RoomDirection(this.baseGrid, (Integer)roomPos.getA(), (Integer)roomPos.getB(), 1, roomData & 0xFFFF);
/*  872 */       int roomEndX = (Integer)roomPos.getA() + roomDir.getStepX();
/*  873 */       int roomEndY = (Integer)roomPos.getB() + roomDir.getStepZ();
/*      */       
/*  875 */       for (int i = 0; i < this.thirdFloorGrid.height; i++) {
/*  876 */         for (int x = 0; x < this.thirdFloorGrid.width; x++) {
/*  877 */           if (!isHouse(this.baseGrid, x, i)) {
/*  878 */             this.thirdFloorGrid.set(x, i, 5);
/*  879 */           } else if (x == (Integer)roomPos.getA() && i == (Integer)roomPos.getB()) {
/*  880 */             this.thirdFloorGrid.set(x, i, 3);
/*  881 */           } else if (x == roomEndX && i == roomEndY) {
/*  882 */             this.thirdFloorGrid.set(x, i, 3);
/*  883 */             this.floorRooms[2].set(x, i, 8388608);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  888 */       List<Direction> potentialCorridors = Lists.newArrayList();
/*  889 */       for (Direction direction : (Iterable<Direction>)Direction.Plane.HORIZONTAL) {
/*  890 */         if (this.thirdFloorGrid.get(roomEndX + direction.getStepX(), roomEndY + direction.getStepZ()) == 0) {
/*  891 */           potentialCorridors.add(direction);
/*      */         }
/*      */       } 
/*      */       
/*  895 */       if (potentialCorridors.isEmpty()) {
/*      */         
/*  897 */         this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
/*  898 */         floor.set((Integer)roomPos.getA(), (Integer)roomPos.getB(), roomData);
/*      */         return;
/*      */       } 
/*  901 */       Direction corridorDir = potentialCorridors.get(this.random.nextInt(potentialCorridors.size()));
/*  902 */       recursiveCorridor(this.thirdFloorGrid, roomEndX + corridorDir.getStepX(), roomEndY + corridorDir.getStepZ(), corridorDir, 4);
/*  903 */       while (cleanEdges(this.thirdFloorGrid));
/*      */     }
/*      */ 
/*      */     
/*      */     private void identifyRooms(WoodlandMansionPieces.SimpleGrid fromGrid, WoodlandMansionPieces.SimpleGrid roomGrid) {
/*  908 */       ObjectArrayList<Tuple<Integer, Integer>> roomPos = new ObjectArrayList();
/*  909 */       for (int y = 0; y < fromGrid.height; y++) {
/*  910 */         for (int x = 0; x < fromGrid.width; x++) {
/*  911 */           if (fromGrid.get(x, y) == 2) {
/*  912 */             roomPos.add(new Tuple(x, y));
/*      */           }
/*      */         } 
/*      */       } 
/*  916 */       Util.shuffle((List)roomPos, this.random);
/*      */       
/*  918 */       int roomId = 10;
/*  919 */       for (ObjectListIterator<Tuple<Integer, Integer>> objectListIterator = roomPos.iterator(); objectListIterator.hasNext(); ) { Tuple<Integer, Integer> pos = objectListIterator.next();
/*  920 */         int x = (Integer)pos.getA();
/*  921 */         int i = (Integer)pos.getB();
/*      */         
/*  923 */         if (roomGrid.get(x, i) == 0) {
/*  924 */           int x0 = x;
/*  925 */           int x1 = x;
/*  926 */           int y0 = i;
/*  927 */           int y1 = i;
/*  928 */           int type = 65536;
/*  929 */           if (roomGrid.get(x + 1, i) == 0 && roomGrid.get(x, i + 1) == 0 && roomGrid.get(x + 1, i + 1) == 0 && 
/*  930 */             fromGrid.get(x + 1, i) == 2 && fromGrid.get(x, i + 1) == 2 && fromGrid.get(x + 1, i + 1) == 2) {
/*      */             
/*  932 */             x1++;
/*  933 */             y1++;
/*  934 */             type = 262144;
/*  935 */           } else if (roomGrid.get(x - 1, i) == 0 && roomGrid.get(x, i + 1) == 0 && roomGrid.get(x - 1, i + 1) == 0 && 
/*  936 */             fromGrid.get(x - 1, i) == 2 && fromGrid.get(x, i + 1) == 2 && fromGrid.get(x - 1, i + 1) == 2) {
/*      */             
/*  938 */             x0--;
/*  939 */             y1++;
/*  940 */             type = 262144;
/*  941 */           } else if (roomGrid.get(x - 1, i) == 0 && roomGrid.get(x, i - 1) == 0 && roomGrid.get(x - 1, i - 1) == 0 && 
/*  942 */             fromGrid.get(x - 1, i) == 2 && fromGrid.get(x, i - 1) == 2 && fromGrid.get(x - 1, i - 1) == 2) {
/*      */             
/*  944 */             x0--;
/*  945 */             y0--;
/*  946 */             type = 262144;
/*  947 */           } else if (roomGrid.get(x + 1, i) == 0 && fromGrid.get(x + 1, i) == 2) {
/*  948 */             x1++;
/*  949 */             type = 131072;
/*  950 */           } else if (roomGrid.get(x, i + 1) == 0 && fromGrid.get(x, i + 1) == 2) {
/*  951 */             y1++;
/*  952 */             type = 131072;
/*  953 */           } else if (roomGrid.get(x - 1, i) == 0 && fromGrid.get(x - 1, i) == 2) {
/*  954 */             x0--;
/*  955 */             type = 131072;
/*  956 */           } else if (roomGrid.get(x, i - 1) == 0 && fromGrid.get(x, i - 1) == 2) {
/*  957 */             y0--;
/*  958 */             type = 131072;
/*      */           } 
/*      */ 
/*      */           
/*  962 */           int doorX = this.random.nextBoolean() ? x0 : x1;
/*  963 */           int doorY = this.random.nextBoolean() ? y0 : y1;
/*  964 */           int doorFlag = 2097152;
/*  965 */           if (!fromGrid.edgesTo(doorX, doorY, 1)) {
/*  966 */             doorX = (doorX == x0) ? x1 : x0;
/*  967 */             doorY = (doorY == y0) ? y1 : y0;
/*  968 */             if (!fromGrid.edgesTo(doorX, doorY, 1)) {
/*  969 */               doorY = (doorY == y0) ? y1 : y0;
/*  970 */               if (!fromGrid.edgesTo(doorX, doorY, 1)) {
/*  971 */                 doorX = (doorX == x0) ? x1 : x0;
/*  972 */                 doorY = (doorY == y0) ? y1 : y0;
/*  973 */                 if (!fromGrid.edgesTo(doorX, doorY, 1)) {
/*      */                   
/*  975 */                   doorFlag = 0;
/*  976 */                   doorX = x0;
/*  977 */                   doorY = y0;
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*  982 */           for (int ry = y0; ry <= y1; ry++) {
/*  983 */             for (int rx = x0; rx <= x1; rx++) {
/*  984 */               if (rx == doorX && ry == doorY) {
/*  985 */                 roomGrid.set(rx, ry, 0x100000 | doorFlag | type | roomId);
/*      */               } else {
/*  987 */                 roomGrid.set(rx, ry, type | roomId);
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/*  992 */           roomId++;
/*      */         }  }
/*      */     
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SimpleGrid {
/*      */     private final int[][] grid;
/*      */     private final int width;
/*      */     private final int height;
/*      */     private final int valueIfOutside;
/*      */     
/*      */     public SimpleGrid(int width, int height, int valueIfOutside) {
/* 1005 */       this.width = width;
/* 1006 */       this.height = height;
/* 1007 */       this.valueIfOutside = valueIfOutside;
/* 1008 */       this.grid = new int[width][height];
/*      */     }
/*      */     
/*      */     public void set(int x, int y, int value) {
/* 1012 */       if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
/* 1013 */         this.grid[x][y] = value;
/*      */       }
/*      */     }
/*      */     
/*      */     public void set(int x0, int y0, int x1, int y1, int value) {
/* 1018 */       for (int y = y0; y <= y1; y++) {
/* 1019 */         for (int x = x0; x <= x1; x++) {
/* 1020 */           set(x, y, value);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public int get(int x, int y) {
/* 1026 */       if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
/* 1027 */         return this.grid[x][y];
/*      */       }
/* 1029 */       return this.valueIfOutside;
/*      */     }
/*      */     
/*      */     public void setif(int x, int y, int ifValue, int value) {
/* 1033 */       if (get(x, y) == ifValue) {
/* 1034 */         set(x, y, value);
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean edgesTo(int x, int y, int ifValue) {
/* 1039 */       return (get(x - 1, y) == ifValue || get(x + 1, y) == ifValue || get(x, y + 1) == ifValue || get(x, y - 1) == ifValue);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static abstract class FloorRoomCollection
/*      */   {
/*      */     public abstract String get1x1(RandomSource param1RandomSource);
/*      */ 
/*      */ 
/*      */     
/*      */     public abstract String get1x1Secret(RandomSource param1RandomSource);
/*      */ 
/*      */ 
/*      */     
/*      */     public abstract String get1x2SideEntrance(RandomSource param1RandomSource, boolean param1Boolean);
/*      */ 
/*      */ 
/*      */     
/*      */     public abstract String get1x2FrontEntrance(RandomSource param1RandomSource, boolean param1Boolean);
/*      */ 
/*      */ 
/*      */     
/*      */     public abstract String get1x2Secret(RandomSource param1RandomSource);
/*      */ 
/*      */ 
/*      */     
/*      */     public abstract String get2x2(RandomSource param1RandomSource);
/*      */ 
/*      */     
/*      */     public abstract String get2x2Secret(RandomSource param1RandomSource);
/*      */   }
/*      */ 
/*      */   
/*      */   private static class FirstFloorRoomCollection
/*      */     extends FloorRoomCollection
/*      */   {
/*      */     public String get1x1(RandomSource random) {
/* 1078 */       return "1x1_a" + random.nextInt(5) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x1Secret(RandomSource random) {
/* 1083 */       return "1x1_as" + random.nextInt(4) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x2SideEntrance(RandomSource random, boolean isStairsRoom) {
/* 1088 */       return "1x2_a" + random.nextInt(9) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x2FrontEntrance(RandomSource random, boolean isStairsRoom) {
/* 1093 */       return "1x2_b" + random.nextInt(5) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x2Secret(RandomSource random) {
/* 1098 */       return "1x2_s" + random.nextInt(2) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get2x2(RandomSource random) {
/* 1103 */       return "2x2_a" + random.nextInt(4) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get2x2Secret(RandomSource random) {
/* 1108 */       return "2x2_s1";
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SecondFloorRoomCollection
/*      */     extends FloorRoomCollection {
/*      */     public String get1x1(RandomSource random) {
/* 1115 */       return "1x1_b" + random.nextInt(5) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x1Secret(RandomSource random) {
/* 1120 */       return "1x1_as" + random.nextInt(4) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x2SideEntrance(RandomSource random, boolean isStairsRoom) {
/* 1125 */       if (isStairsRoom) {
/* 1126 */         return "1x2_c_stairs";
/*      */       }
/* 1128 */       return "1x2_c" + random.nextInt(4) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x2FrontEntrance(RandomSource random, boolean isStairsRoom) {
/* 1133 */       if (isStairsRoom) {
/* 1134 */         return "1x2_d_stairs";
/*      */       }
/* 1136 */       return "1x2_d" + random.nextInt(5) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x2Secret(RandomSource random) {
/* 1141 */       return "1x2_se" + random.nextInt(1) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get2x2(RandomSource random) {
/* 1146 */       return "2x2_b" + random.nextInt(5) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get2x2Secret(RandomSource random) {
/* 1151 */       return "2x2_s1";
/*      */     }
/*      */   }
/*      */   
/*      */   private static class ThirdFloorRoomCollection extends SecondFloorRoomCollection {}
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/structures/WoodlandMansionPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */