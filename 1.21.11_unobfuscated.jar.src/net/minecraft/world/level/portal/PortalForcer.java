/*     */ package net.minecraft.world.level.portal;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.BlockUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.NetherPortalBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PortalForcer
/*     */ {
/*     */   public static final int TICKET_RADIUS = 3;
/*     */   private static final int NETHER_PORTAL_RADIUS = 16;
/*     */   private static final int OVERWORLD_PORTAL_RADIUS = 128;
/*     */   private static final int FRAME_HEIGHT = 5;
/*     */   private static final int FRAME_WIDTH = 4;
/*     */   private static final int FRAME_BOX = 3;
/*     */   private static final int FRAME_HEIGHT_START = -1;
/*     */   private static final int FRAME_HEIGHT_END = 4;
/*     */   private static final int FRAME_WIDTH_START = -1;
/*     */   private static final int FRAME_WIDTH_END = 3;
/*     */   private static final int FRAME_BOX_START = -1;
/*     */   private static final int FRAME_BOX_END = 2;
/*     */   private static final int NOTHING_FOUND = -1;
/*     */   private final ServerLevel level;
/*     */   
/*     */   public PortalForcer(ServerLevel level) {
/*  45 */     this.level = level;
/*     */   }
/*     */   
/*     */   public Optional<BlockPos> findClosestPortalPosition(BlockPos approximateExitPos, boolean toNether, WorldBorder worldBorder) {
/*  49 */     PoiManager poiManager = this.level.getPoiManager();
/*  50 */     int radius = toNether ? 16 : 128;
/*  51 */     poiManager.ensureLoadedAndValid((LevelReader)this.level, approximateExitPos, radius);
/*     */ 
/*     */ 
/*     */     
/*  55 */     Objects.requireNonNull(worldBorder); return poiManager.getInSquare(type -> type.is(PoiTypes.NETHER_PORTAL), approximateExitPos, radius, PoiManager.Occupancy.ANY).map(PoiRecord::getPos).filter(worldBorder::isWithinBounds)
/*  56 */       .filter(pos -> this.level.getBlockState(pos).hasProperty((Property)BlockStateProperties.HORIZONTAL_AXIS))
/*  57 */       .min(Comparator.<BlockPos>comparingDouble(p -> p.distSqr((Vec3i)approximateExitPos)).thenComparingInt(Vec3i::getY));
/*     */   }
/*     */   
/*     */   public Optional<BlockUtil.FoundRectangle> createPortal(BlockPos origin, Direction.Axis portalAxis) {
/*  61 */     Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, portalAxis);
/*     */     
/*  63 */     double closestFullDistanceSqr = -1.0D;
/*  64 */     BlockPos closestFullPosition = null;
/*  65 */     double closestPartialDistanceSqr = -1.0D;
/*  66 */     BlockPos closestPartialPosition = null;
/*     */     
/*  68 */     WorldBorder worldBorder = this.level.getWorldBorder();
/*  69 */     int maxPlaceableY = Math.min(this.level.getMaxY(), this.level.getMinY() + this.level.getLogicalHeight() - 1);
/*     */     
/*  71 */     int edgeDistance = 1;
/*     */     
/*  73 */     BlockPos.MutableBlockPos mutable = origin.mutable();
/*  74 */     for (BlockPos.MutableBlockPos columnPos : (Iterable<BlockPos.MutableBlockPos>)BlockPos.spiralAround(origin, 16, Direction.EAST, Direction.SOUTH)) {
/*  75 */       int height = Math.min(maxPlaceableY, this.level.getHeight(Heightmap.Types.MOTION_BLOCKING, columnPos.getX(), columnPos.getZ()));
/*     */ 
/*     */       
/*  78 */       if (!worldBorder.isWithinBounds((BlockPos)columnPos) || !worldBorder.isWithinBounds((BlockPos)columnPos.move(direction, 1))) {
/*     */         continue;
/*     */       }
/*  81 */       columnPos.move(direction.getOpposite(), 1);
/*     */       
/*  83 */       for (int y = height; y >= this.level.getMinY(); y--) {
/*  84 */         columnPos.setY(y);
/*  85 */         if (canPortalReplaceBlock(columnPos)) {
/*     */ 
/*     */ 
/*     */           
/*  89 */           int firstEmptyY = y;
/*     */           
/*  91 */           while (y > this.level.getMinY() && canPortalReplaceBlock(columnPos.move(Direction.DOWN))) {
/*  92 */             y--;
/*     */           }
/*     */ 
/*     */           
/*  96 */           if (y + 4 <= maxPlaceableY) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 101 */             int deltaY = firstEmptyY - y;
/* 102 */             if (deltaY <= 0 || deltaY >= 3) {
/*     */ 
/*     */ 
/*     */               
/* 106 */               columnPos.setY(y);
/*     */               
/* 108 */               if (canHostFrame((BlockPos)columnPos, mutable, direction, 0)) {
/*     */                 
/* 110 */                 double distance = origin.distSqr((Vec3i)columnPos);
/*     */ 
/*     */                 
/* 113 */                 if (canHostFrame((BlockPos)columnPos, mutable, direction, -1) && 
/* 114 */                   canHostFrame((BlockPos)columnPos, mutable, direction, 1))
/*     */                 {
/*     */                   
/* 117 */                   if (closestFullDistanceSqr == -1.0D || closestFullDistanceSqr > distance) {
/* 118 */                     closestFullDistanceSqr = distance;
/* 119 */                     closestFullPosition = columnPos.immutable();
/*     */                   } 
/*     */                 }
/*     */ 
/*     */                 
/* 124 */                 if (closestFullDistanceSqr == -1.0D && (closestPartialDistanceSqr == -1.0D || closestPartialDistanceSqr > distance)) {
/* 125 */                   closestPartialDistanceSqr = distance;
/* 126 */                   closestPartialPosition = columnPos.immutable();
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 133 */     }  if (closestFullDistanceSqr == -1.0D && closestPartialDistanceSqr != -1.0D) {
/* 134 */       closestFullPosition = closestPartialPosition;
/* 135 */       closestFullDistanceSqr = closestPartialDistanceSqr;
/*     */     } 
/*     */     
/* 138 */     if (closestFullDistanceSqr == -1.0D) {
/*     */ 
/*     */       
/* 141 */       int minStartY = Math.max(this.level.getMinY() - -1, 70);
/* 142 */       int maxStartY = maxPlaceableY - 9;
/* 143 */       if (maxStartY < minStartY) {
/* 144 */         return Optional.empty();
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 151 */       closestFullPosition = new BlockPos(origin.getX() - direction.getStepX() * 1, Mth.clamp(origin.getY(), minStartY, maxStartY), origin.getZ() - direction.getStepZ() * 1).immutable();
/* 152 */       closestFullPosition = worldBorder.clampToBounds(closestFullPosition);
/* 153 */       Direction clockWise = direction.getClockWise();
/*     */ 
/*     */       
/* 156 */       for (int box = -1; box < 2; box++) {
/* 157 */         for (int j = 0; j < 2; j++) {
/*     */           
/* 159 */           for (int height = -1; height < 3; height++) {
/* 160 */             BlockState blockState = (height < 0) ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.AIR.defaultBlockState();
/*     */             
/* 162 */             mutable.setWithOffset((Vec3i)closestFullPosition, j * 
/*     */                 
/* 164 */                 direction.getStepX() + box * clockWise.getStepX(), height, j * 
/*     */                 
/* 166 */                 direction.getStepZ() + box * clockWise.getStepZ());
/*     */             
/* 168 */             this.level.setBlockAndUpdate((BlockPos)mutable, blockState);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 175 */     for (int width = -1; width < 3; width++) {
/* 176 */       for (int height = -1; height < 4; height++) {
/*     */         
/* 178 */         if (width == -1 || width == 2 || height == -1 || height == 3) {
/* 179 */           mutable.setWithOffset((Vec3i)closestFullPosition, width * 
/*     */               
/* 181 */               direction.getStepX(), height, width * 
/*     */               
/* 183 */               direction.getStepZ());
/*     */           
/* 185 */           this.level.setBlock((BlockPos)mutable, Blocks.OBSIDIAN.defaultBlockState(), 3);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 191 */     BlockState portalBlockState = (BlockState)Blocks.NETHER_PORTAL.defaultBlockState().setValue((Property)NetherPortalBlock.AXIS, (Comparable)portalAxis);
/*     */     
/* 193 */     for (int i = 0; i < 2; i++) {
/* 194 */       for (int height = 0; height < 3; height++) {
/* 195 */         mutable.setWithOffset((Vec3i)closestFullPosition, i * 
/*     */             
/* 197 */             direction.getStepX(), height, i * 
/*     */             
/* 199 */             direction.getStepZ());
/*     */         
/* 201 */         this.level.setBlock((BlockPos)mutable, portalBlockState, 18);
/*     */       } 
/*     */     } 
/*     */     
/* 205 */     return Optional.of(new BlockUtil.FoundRectangle(closestFullPosition.immutable(), 2, 3));
/*     */   }
/*     */   
/*     */   private boolean canPortalReplaceBlock(BlockPos.MutableBlockPos pos) {
/* 209 */     BlockState blockState = this.level.getBlockState((BlockPos)pos);
/* 210 */     return (blockState.canBeReplaced() && blockState.getFluidState().isEmpty());
/*     */   }
/*     */   
/*     */   private boolean canHostFrame(BlockPos origin, BlockPos.MutableBlockPos mutable, Direction direction, int offset) {
/* 214 */     Direction clockWise = direction.getClockWise();
/*     */     
/* 216 */     for (int width = -1; width < 3; width++) {
/* 217 */       for (int height = -1; height < 4; height++) {
/* 218 */         mutable.setWithOffset((Vec3i)origin, 
/*     */             
/* 220 */             direction.getStepX() * width + clockWise.getStepX() * offset, height, 
/*     */             
/* 222 */             direction.getStepZ() * width + clockWise.getStepZ() * offset);
/*     */ 
/*     */         
/* 225 */         if (height < 0 && !this.level.getBlockState((BlockPos)mutable).isSolid()) {
/* 226 */           return false;
/*     */         }
/* 228 */         if (height >= 0 && !canPortalReplaceBlock(mutable)) {
/* 229 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 234 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/portal/PortalForcer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */