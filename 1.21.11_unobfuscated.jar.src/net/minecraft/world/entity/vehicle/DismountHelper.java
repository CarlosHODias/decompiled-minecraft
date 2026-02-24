/*     */ package net.minecraft.world.entity.vehicle;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.block.TrapDoorBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class DismountHelper
/*     */ {
/*     */   public static int[][] offsetsForDirection(Direction forward) {
/*  25 */     Direction right = forward.getClockWise();
/*  26 */     Direction left = right.getOpposite();
/*  27 */     Direction back = forward.getOpposite();
/*     */     
/*  29 */     return new int[][] {
/*  30 */         { right.getStepX(), right.getStepZ()
/*  31 */         }, { left.getStepX(), left.getStepZ()
/*  32 */         }, { back.getStepX() + right.getStepX(), back.getStepZ() + right.getStepZ()
/*  33 */         }, { back.getStepX() + left.getStepX(), back.getStepZ() + left.getStepZ()
/*  34 */         }, { forward.getStepX() + right.getStepX(), forward.getStepZ() + right.getStepZ()
/*  35 */         }, { forward.getStepX() + left.getStepX(), forward.getStepZ() + left.getStepZ()
/*  36 */         }, { back.getStepX(), back.getStepZ()
/*  37 */         }, { forward.getStepX(), forward.getStepZ() }
/*     */       };
/*     */   }
/*     */   
/*     */   public static boolean isBlockFloorValid(double blockFloorHeight) {
/*  42 */     return (!Double.isInfinite(blockFloorHeight) && blockFloorHeight < 1.0D);
/*     */   }
/*     */   
/*     */   public static boolean canDismountTo(CollisionGetter level, LivingEntity passenger, AABB box) {
/*  46 */     Iterable<VoxelShape> blockCollisions = level.getBlockCollisions((Entity)passenger, box);
/*  47 */     for (VoxelShape collision : blockCollisions) {
/*  48 */       if (!collision.isEmpty()) {
/*  49 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  53 */     if (!level.getWorldBorder().isWithinBounds(box)) {
/*  54 */       return false;
/*     */     }
/*     */     
/*  57 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean canDismountTo(CollisionGetter level, Vec3 location, LivingEntity passenger, Pose dismountPose) {
/*  61 */     return canDismountTo(level, passenger, passenger.getLocalBoundsForPose(dismountPose).move(location));
/*     */   }
/*     */   
/*     */   public static VoxelShape nonClimbableShape(BlockGetter level, BlockPos pos) {
/*  65 */     BlockState blockState = level.getBlockState(pos);
/*  66 */     if (blockState.is(BlockTags.CLIMBABLE) || (blockState.getBlock() instanceof TrapDoorBlock && (Boolean)blockState.getValue((Property)TrapDoorBlock.OPEN))) {
/*  67 */       return Shapes.empty();
/*     */     }
/*  69 */     return blockState.getCollisionShape(level, pos);
/*     */   }
/*     */   
/*     */   public static double findCeilingFrom(BlockPos pos, int blocks, Function<BlockPos, VoxelShape> shapeGetter) {
/*  73 */     BlockPos.MutableBlockPos cursor = pos.mutable();
/*  74 */     int y = 0;
/*  75 */     while (y < blocks) {
/*  76 */       VoxelShape collisionShape = shapeGetter.apply(cursor);
/*  77 */       if (!collisionShape.isEmpty()) {
/*  78 */         return (pos.getY() + y) + collisionShape.min(Direction.Axis.Y);
/*     */       }
/*  80 */       y++;
/*  81 */       cursor.move(Direction.UP);
/*     */     } 
/*  83 */     return Double.POSITIVE_INFINITY;
/*     */   }
/*     */   
/*     */   public static Vec3 findSafeDismountLocation(EntityType<?> type, CollisionGetter level, BlockPos blockPos, boolean checkDangerous) {
/*  87 */     if (checkDangerous && type.isBlockDangerous(level.getBlockState(blockPos))) {
/*  88 */       return null;
/*     */     }
/*     */     
/*  91 */     double floorHeight = level.getBlockFloorHeight(nonClimbableShape((BlockGetter)level, blockPos), () -> nonClimbableShape((BlockGetter)level, blockPos.below()));
/*  92 */     if (!isBlockFloorValid(floorHeight)) {
/*  93 */       return null;
/*     */     }
/*     */     
/*  96 */     if (checkDangerous && floorHeight <= 0.0D && type.isBlockDangerous(level.getBlockState(blockPos.below()))) {
/*  97 */       return null;
/*     */     }
/*     */     
/* 100 */     Vec3 position = Vec3.upFromBottomCenterOf((Vec3i)blockPos, floorHeight);
/* 101 */     AABB aabb = type.getDimensions().makeBoundingBox(position);
/* 102 */     Iterable<VoxelShape> worldCollisions = level.getBlockCollisions(null, aabb);
/* 103 */     for (VoxelShape shape : worldCollisions) {
/* 104 */       if (!shape.isEmpty()) {
/* 105 */         return null;
/*     */       }
/*     */     } 
/*     */     
/* 109 */     if (type == EntityType.PLAYER)
/*     */     {
/* 111 */       if (level.getBlockState(blockPos).is(BlockTags.INVALID_SPAWN_INSIDE) || level.getBlockState(blockPos.above()).is(BlockTags.INVALID_SPAWN_INSIDE)) {
/* 112 */         return null;
/*     */       }
/*     */     }
/*     */     
/* 116 */     if (!level.getWorldBorder().isWithinBounds(aabb)) {
/* 117 */       return null;
/*     */     }
/*     */     
/* 120 */     return position;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/vehicle/DismountHelper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */