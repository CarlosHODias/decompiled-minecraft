/*     */ package net.minecraft.world.level.block.piston;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.PistonType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class PistonMovingBlockEntity
/*     */   extends BlockEntity {
/*     */   private static final int TICKS_TO_EXTEND = 2;
/*     */   private static final double PUSH_OFFSET = 0.01D;
/*     */   public static final double TICK_MOVEMENT = 0.51D;
/*  37 */   private static final BlockState DEFAULT_BLOCK_STATE = Blocks.AIR.defaultBlockState();
/*     */   
/*     */   private static final float DEFAULT_PROGRESS = 0.0F;
/*     */   private static final boolean DEFAULT_EXTENDING = false;
/*     */   private static final boolean DEFAULT_SOURCE = false;
/*  42 */   private BlockState movedState = DEFAULT_BLOCK_STATE;
/*     */   
/*     */   private Direction direction;
/*     */   
/*     */   private boolean extending = false;
/*     */   private boolean isSourcePiston = false;
/*  48 */   private static final ThreadLocal<Direction> NOCLIP = ThreadLocal.withInitial(() -> null);
/*     */   
/*  50 */   private float progress = 0.0F;
/*  51 */   private float progressO = 0.0F;
/*     */   
/*     */   private long lastTicked;
/*     */   private int deathTicks;
/*     */   
/*     */   public PistonMovingBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  57 */     super(BlockEntityType.PISTON, worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public PistonMovingBlockEntity(BlockPos worldPosition, BlockState blockState, BlockState movedState, Direction direction, boolean extending, boolean isSourcePiston) {
/*  61 */     this(worldPosition, blockState);
/*  62 */     this.movedState = movedState;
/*  63 */     this.direction = direction;
/*  64 */     this.extending = extending;
/*  65 */     this.isSourcePiston = isSourcePiston;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
/*  70 */     return saveCustomOnly(registries);
/*     */   }
/*     */   
/*     */   public boolean isExtending() {
/*  74 */     return this.extending;
/*     */   }
/*     */   
/*     */   public Direction getDirection() {
/*  78 */     return this.direction;
/*     */   }
/*     */   
/*     */   public boolean isSourcePiston() {
/*  82 */     return this.isSourcePiston;
/*     */   }
/*     */   
/*     */   public float getProgress(float a) {
/*  86 */     if (a > 1.0F) {
/*  87 */       a = 1.0F;
/*     */     }
/*  89 */     return Mth.lerp(a, this.progressO, this.progress);
/*     */   }
/*     */   
/*     */   public float getXOff(float a) {
/*  93 */     return this.direction.getStepX() * getExtendedProgress(getProgress(a));
/*     */   }
/*     */   
/*     */   public float getYOff(float a) {
/*  97 */     return this.direction.getStepY() * getExtendedProgress(getProgress(a));
/*     */   }
/*     */   
/*     */   public float getZOff(float a) {
/* 101 */     return this.direction.getStepZ() * getExtendedProgress(getProgress(a));
/*     */   }
/*     */   
/*     */   private float getExtendedProgress(float progress) {
/* 105 */     return this.extending ? (progress - 1.0F) : (1.0F - progress);
/*     */   }
/*     */   
/*     */   private BlockState getCollisionRelatedBlockState() {
/* 109 */     if (!isExtending() && isSourcePiston() && this.movedState.getBlock() instanceof PistonBaseBlock) {
/* 110 */       return (BlockState)((BlockState)((BlockState)Blocks.PISTON_HEAD.defaultBlockState()
/* 111 */         .setValue((Property)PistonHeadBlock.SHORT, (this.progress > 0.25F)))
/* 112 */         .setValue((Property)PistonHeadBlock.TYPE, this.movedState.is(Blocks.STICKY_PISTON) ? (Comparable)PistonType.STICKY : (Comparable)PistonType.DEFAULT))
/* 113 */         .setValue((Property)PistonHeadBlock.FACING, this.movedState.getValue((Property)PistonBaseBlock.FACING));
/*     */     }
/* 115 */     return this.movedState;
/*     */   }
/*     */   
/*     */   private static void moveCollidedEntities(Level level, BlockPos pos, float newProgress, PistonMovingBlockEntity self) {
/* 119 */     Direction movement = self.getMovementDirection();
/*     */     
/* 121 */     double deltaProgress = (newProgress - self.progress);
/*     */     
/* 123 */     VoxelShape shape = self.getCollisionRelatedBlockState().getCollisionShape((BlockGetter)level, pos);
/* 124 */     if (shape.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 128 */     AABB aabb = moveByPositionAndProgress(pos, shape.bounds(), self);
/* 129 */     List<Entity> entities = level.getEntities(null, PistonMath.getMovementArea(aabb, movement, deltaProgress).minmax(aabb));
/* 130 */     if (entities.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 134 */     List<AABB> shapeAabbs = shape.toAabbs();
/* 135 */     boolean causeBounce = self.movedState.is(Blocks.SLIME_BLOCK);
/* 136 */     for (Entity entity : entities) {
/* 137 */       if (entity.getPistonPushReaction() == PushReaction.IGNORE) {
/*     */         continue;
/*     */       }
/*     */       
/* 141 */       if (causeBounce) {
/* 142 */         if (entity instanceof net.minecraft.server.level.ServerPlayer) {
/*     */           continue;
/*     */         }
/*     */         
/* 146 */         Vec3 deltaMovement = entity.getDeltaMovement();
/* 147 */         double dx = deltaMovement.x;
/* 148 */         double dy = deltaMovement.y;
/* 149 */         double dz = deltaMovement.z;
/* 150 */         switch (movement.getAxis()) {
/*     */           case X:
/* 152 */             dx = movement.getStepX();
/*     */             break;
/*     */           case Y:
/* 155 */             dy = movement.getStepY();
/*     */             break;
/*     */           case Z:
/* 158 */             dz = movement.getStepZ();
/*     */             break;
/*     */         } 
/*     */         
/* 162 */         entity.setDeltaMovement(dx, dy, dz);
/*     */       } 
/*     */       
/* 165 */       double delta = 0.0D;
/* 166 */       for (AABB shapeAabb : shapeAabbs) {
/*     */ 
/*     */         
/* 169 */         AABB movingAABB = PistonMath.getMovementArea(moveByPositionAndProgress(pos, shapeAabb, self), movement, deltaProgress);
/*     */         
/* 171 */         AABB entityAabb = entity.getBoundingBox();
/* 172 */         if (!movingAABB.intersects(entityAabb)) {
/*     */           continue;
/*     */         }
/*     */         
/* 176 */         delta = Math.max(delta, getMovement(movingAABB, movement, entityAabb));
/*     */ 
/*     */         
/* 179 */         if (delta >= deltaProgress) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 184 */       if (delta <= 0.0D) {
/*     */         continue;
/*     */       }
/*     */       
/* 188 */       delta = Math.min(delta, deltaProgress) + 0.01D;
/* 189 */       moveEntityByPiston(movement, entity, delta, movement);
/*     */       
/* 191 */       if (!self.extending && self.isSourcePiston) {
/* 192 */         fixEntityWithinPistonBase(pos, entity, movement, deltaProgress);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void moveEntityByPiston(Direction pistonDirection, Entity entity, double delta, Direction movement) {
/* 199 */     NOCLIP.set(pistonDirection);
/* 200 */     Vec3 previousPos = entity.position();
/* 201 */     entity.move(MoverType.PISTON, new Vec3(delta * movement.getStepX(), delta * movement.getStepY(), delta * movement.getStepZ()));
/*     */     
/* 203 */     entity.applyEffectsFromBlocks(previousPos, entity.position());
/*     */     
/* 205 */     entity.removeLatestMovementRecording();
/* 206 */     NOCLIP.set(null);
/*     */   }
/*     */   
/*     */   private static void moveStuckEntities(Level level, BlockPos pos, float newProgress, PistonMovingBlockEntity self) {
/* 210 */     if (!self.isStickyForEntities()) {
/*     */       return;
/*     */     }
/*     */     
/* 214 */     Direction movement = self.getMovementDirection();
/* 215 */     if (!movement.getAxis().isHorizontal()) {
/*     */       return;
/*     */     }
/*     */     
/* 219 */     double stickyTop = self.movedState.getCollisionShape((BlockGetter)level, pos).max(Direction.Axis.Y);
/* 220 */     AABB aabb = moveByPositionAndProgress(pos, new AABB(0.0D, stickyTop, 0.0D, 1.0D, 1.5000010000000001D, 1.0D), self);
/*     */     
/* 222 */     double deltaProgress = (newProgress - self.progress);
/*     */     
/* 224 */     List<Entity> entities = level.getEntities(null, aabb, entity -> matchesStickyCritera(aabb, entity, pos));
/* 225 */     for (Entity entity : entities) {
/* 226 */       moveEntityByPiston(movement, entity, deltaProgress, movement);
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean matchesStickyCritera(AABB aabb, Entity entity, BlockPos pos) {
/* 231 */     return (entity.getPistonPushReaction() == PushReaction.NORMAL && 
/* 232 */       entity.onGround() && (
/* 233 */       entity.isSupportedBy(pos) || (
/* 234 */       entity.getX() >= aabb.minX && 
/* 235 */       entity.getX() <= aabb.maxX && 
/* 236 */       entity.getZ() >= aabb.minZ && 
/* 237 */       entity.getZ() <= aabb.maxZ)));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isStickyForEntities() {
/* 242 */     return this.movedState.is(Blocks.HONEY_BLOCK);
/*     */   }
/*     */   
/*     */   public Direction getMovementDirection() {
/* 246 */     return this.extending ? this.direction : this.direction.getOpposite();
/*     */   }
/*     */   
/*     */   private static double getMovement(AABB aabbToBeOutsideOf, Direction movement, AABB aabb) {
/* 250 */     switch (movement)
/*     */     { case EAST:
/* 252 */         return aabbToBeOutsideOf.maxX - aabb.minX;
/*     */       case WEST:
/* 254 */         return aabb.maxX - aabbToBeOutsideOf.minX;
/*     */       
/*     */       default:
/* 257 */         return aabbToBeOutsideOf.maxY - aabb.minY;
/*     */       case DOWN:
/* 259 */         return aabb.maxY - aabbToBeOutsideOf.minY;
/*     */       case SOUTH:
/* 261 */         return aabbToBeOutsideOf.maxZ - aabb.minZ;
/*     */       case NORTH:
/* 263 */         break; }  return aabb.maxZ - aabbToBeOutsideOf.minZ;
/*     */   }
/*     */ 
/*     */   
/*     */   private static AABB moveByPositionAndProgress(BlockPos pos, AABB aabb, PistonMovingBlockEntity entity) {
/* 268 */     double currentPosition = entity.getExtendedProgress(entity.progress);
/* 269 */     return aabb.move(
/* 270 */         pos.getX() + currentPosition * entity.direction.getStepX(), 
/* 271 */         pos.getY() + currentPosition * entity.direction.getStepY(), 
/* 272 */         pos.getZ() + currentPosition * entity.direction.getStepZ());
/*     */   }
/*     */ 
/*     */   
/*     */   private static void fixEntityWithinPistonBase(BlockPos pos, Entity entity, Direction direction, double deltaProgress) {
/* 277 */     AABB entityAabb = entity.getBoundingBox();
/* 278 */     AABB box = Shapes.block().bounds().move(pos);
/* 279 */     if (entityAabb.intersects(box)) {
/* 280 */       Direction opposite = direction.getOpposite();
/*     */ 
/*     */       
/* 283 */       double delta = getMovement(box, opposite, entityAabb) + 0.01D;
/* 284 */       double deltaIntersected = getMovement(box, opposite, entityAabb.intersect(box)) + 0.01D;
/*     */       
/* 286 */       if (Math.abs(delta - deltaIntersected) < 0.01D) {
/* 287 */         delta = Math.min(delta, deltaProgress) + 0.01D;
/* 288 */         moveEntityByPiston(direction, entity, delta, opposite);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public BlockState getMovedState() {
/* 294 */     return this.movedState;
/*     */   }
/*     */   
/*     */   public void finalTick() {
/* 298 */     if (this.level != null && (this.progressO < 1.0F || this.level.isClientSide())) {
/* 299 */       this.progress = 1.0F;
/* 300 */       this.progressO = this.progress;
/* 301 */       this.level.removeBlockEntity(this.worldPosition);
/* 302 */       setRemoved();
/* 303 */       if (this.level.getBlockState(this.worldPosition).is(Blocks.MOVING_PISTON)) {
/*     */         BlockState newState;
/* 305 */         if (this.isSourcePiston) {
/* 306 */           newState = Blocks.AIR.defaultBlockState();
/*     */         } else {
/* 308 */           newState = Block.updateFromNeighbourShapes(this.movedState, (LevelAccessor)this.level, this.worldPosition);
/*     */         } 
/* 310 */         this.level.setBlock(this.worldPosition, newState, 3);
/* 311 */         this.level.neighborChanged(this.worldPosition, newState.getBlock(), ExperimentalRedstoneUtils.initialOrientation(this.level, getPushDirection(), null));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void preRemoveSideEffects(BlockPos pos, BlockState state) {
/* 318 */     finalTick();
/*     */   }
/*     */   
/*     */   public Direction getPushDirection() {
/* 322 */     return this.extending ? this.direction : this.direction.getOpposite();
/*     */   }
/*     */   
/*     */   public static void tick(Level level, BlockPos pos, BlockState state, PistonMovingBlockEntity entity) {
/* 326 */     entity.lastTicked = level.getGameTime();
/* 327 */     entity.progressO = entity.progress;
/*     */     
/* 329 */     if (entity.progressO >= 1.0F) {
/* 330 */       if (level.isClientSide() && entity.deathTicks < 5) {
/* 331 */         entity.deathTicks++;
/*     */         return;
/*     */       } 
/* 334 */       level.removeBlockEntity(pos);
/* 335 */       entity.setRemoved();
/* 336 */       if (level.getBlockState(pos).is(Blocks.MOVING_PISTON)) {
/* 337 */         BlockState newState = Block.updateFromNeighbourShapes(entity.movedState, (LevelAccessor)level, pos);
/* 338 */         if (newState.isAir()) {
/* 339 */           level.setBlock(pos, entity.movedState, 340);
/* 340 */           Block.updateOrDestroy(entity.movedState, newState, (LevelAccessor)level, pos, 3);
/*     */         } else {
/* 342 */           if (newState.hasProperty((Property)BlockStateProperties.WATERLOGGED) && (Boolean)newState.getValue((Property)BlockStateProperties.WATERLOGGED)) {
/* 343 */             newState = (BlockState)newState.setValue((Property)BlockStateProperties.WATERLOGGED, false);
/*     */           }
/* 345 */           level.setBlock(pos, newState, 67);
/* 346 */           level.neighborChanged(pos, newState.getBlock(), ExperimentalRedstoneUtils.initialOrientation(level, entity.getPushDirection(), null));
/*     */         } 
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 352 */     float newProgress = entity.progress + 0.5F;
/* 353 */     moveCollidedEntities(level, pos, newProgress, entity);
/* 354 */     moveStuckEntities(level, pos, newProgress, entity);
/* 355 */     entity.progress = newProgress;
/* 356 */     if (entity.progress >= 1.0F) {
/* 357 */       entity.progress = 1.0F;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 363 */     super.loadAdditional(input);
/*     */     
/* 365 */     this.movedState = input.read("blockState", BlockState.CODEC).orElse(DEFAULT_BLOCK_STATE);
/* 366 */     this.direction = input.read("facing", Direction.LEGACY_ID_CODEC).orElse(Direction.DOWN);
/* 367 */     this.progress = input.getFloatOr("progress", 0.0F);
/* 368 */     this.progressO = this.progress;
/* 369 */     this.extending = input.getBooleanOr("extending", false);
/* 370 */     this.isSourcePiston = input.getBooleanOr("source", false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 375 */     super.saveAdditional(output);
/*     */     
/* 377 */     output.store("blockState", BlockState.CODEC, this.movedState);
/* 378 */     output.store("facing", Direction.LEGACY_ID_CODEC, this.direction);
/* 379 */     output.putFloat("progress", this.progressO);
/* 380 */     output.putBoolean("extending", this.extending);
/* 381 */     output.putBoolean("source", this.isSourcePiston);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockGetter level, BlockPos pos) {
/*     */     VoxelShape pistonHeadShape;
/*     */     BlockState blockState;
/* 388 */     if (!this.extending && this.isSourcePiston && this.movedState.getBlock() instanceof PistonBaseBlock) {
/* 389 */       pistonHeadShape = ((BlockState)this.movedState.setValue((Property)PistonBaseBlock.EXTENDED, true)).getCollisionShape(level, pos);
/*     */     } else {
/* 391 */       pistonHeadShape = Shapes.empty();
/*     */     } 
/*     */     
/* 394 */     Direction noClipDirection = NOCLIP.get();
/* 395 */     if (this.progress < 1.0D && noClipDirection == getMovementDirection()) {
/* 396 */       return pistonHeadShape;
/*     */     }
/*     */ 
/*     */     
/* 400 */     if (isSourcePiston()) {
/* 401 */       blockState = (BlockState)((BlockState)Blocks.PISTON_HEAD.defaultBlockState().setValue((Property)PistonHeadBlock.FACING, (Comparable)this.direction)).setValue((Property)PistonHeadBlock.SHORT, (this.extending != ((1.0F - this.progress < 0.25F))));
/*     */     } else {
/* 403 */       blockState = this.movedState;
/*     */     } 
/* 405 */     float extendedProgress = getExtendedProgress(this.progress);
/* 406 */     double dx = (this.direction.getStepX() * extendedProgress);
/* 407 */     double dy = (this.direction.getStepY() * extendedProgress);
/* 408 */     double dz = (this.direction.getStepZ() * extendedProgress);
/* 409 */     return Shapes.or(pistonHeadShape, blockState.getCollisionShape(level, pos).move(dx, dy, dz));
/*     */   }
/*     */   
/*     */   public long getLastTicked() {
/* 413 */     return this.lastTicked;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLevel(Level level) {
/* 418 */     super.setLevel(level);
/*     */     
/* 420 */     if (level.holderLookup(Registries.BLOCK).get(this.movedState.getBlock().builtInRegistryHolder().key()).isEmpty())
/* 421 */       this.movedState = Blocks.AIR.defaultBlockState(); 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/piston/PistonMovingBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */