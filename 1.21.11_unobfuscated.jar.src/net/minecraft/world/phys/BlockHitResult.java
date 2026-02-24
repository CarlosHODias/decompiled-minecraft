/*    */ package net.minecraft.world.phys;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public class BlockHitResult extends HitResult {
/*    */   private final Direction direction;
/*    */   private final BlockPos blockPos;
/*    */   private final boolean miss;
/*    */   private final boolean inside;
/*    */   private final boolean worldBorderHit;
/*    */   
/*    */   public static BlockHitResult miss(Vec3 location, Direction direction, BlockPos pos) {
/* 14 */     return new BlockHitResult(true, location, direction, pos, false, false);
/*    */   }
/*    */   
/*    */   public BlockHitResult(Vec3 location, Direction direction, BlockPos pos, boolean inside) {
/* 18 */     this(false, location, direction, pos, inside, false);
/*    */   }
/*    */   
/*    */   public BlockHitResult(Vec3 location, Direction direction, BlockPos pos, boolean inside, boolean worldBorderHit) {
/* 22 */     this(false, location, direction, pos, inside, worldBorderHit);
/*    */   }
/*    */   
/*    */   private BlockHitResult(boolean miss, Vec3 location, Direction direction, BlockPos blockPos, boolean inside, boolean worldBorderHit) {
/* 26 */     super(location);
/* 27 */     this.miss = miss;
/* 28 */     this.direction = direction;
/* 29 */     this.blockPos = blockPos;
/* 30 */     this.inside = inside;
/* 31 */     this.worldBorderHit = worldBorderHit;
/*    */   }
/*    */   
/*    */   public BlockHitResult withDirection(Direction direction) {
/* 35 */     return new BlockHitResult(this.miss, this.location, direction, this.blockPos, this.inside, this.worldBorderHit);
/*    */   }
/*    */   
/*    */   public BlockHitResult withPosition(BlockPos blockPos) {
/* 39 */     return new BlockHitResult(this.miss, this.location, this.direction, blockPos, this.inside, this.worldBorderHit);
/*    */   }
/*    */   
/*    */   public BlockHitResult hitBorder() {
/* 43 */     return new BlockHitResult(this.miss, this.location, this.direction, this.blockPos, this.inside, true);
/*    */   }
/*    */   
/*    */   public BlockPos getBlockPos() {
/* 47 */     return this.blockPos;
/*    */   }
/*    */   
/*    */   public Direction getDirection() {
/* 51 */     return this.direction;
/*    */   }
/*    */ 
/*    */   
/*    */   public HitResult.Type getType() {
/* 56 */     return this.miss ? HitResult.Type.MISS : HitResult.Type.BLOCK;
/*    */   }
/*    */   
/*    */   public boolean isInside() {
/* 60 */     return this.inside;
/*    */   }
/*    */   
/*    */   public boolean isWorldBorderHit() {
/* 64 */     return this.worldBorderHit;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/phys/BlockHitResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */