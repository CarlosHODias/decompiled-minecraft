/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class NodeEvaluator
/*     */ {
/*     */   protected PathfindingContext currentContext;
/*     */   protected Mob mob;
/*  22 */   protected final Int2ObjectMap<Node> nodes = (Int2ObjectMap<Node>)new Int2ObjectOpenHashMap();
/*     */   
/*     */   protected int entityWidth;
/*     */   
/*     */   protected int entityHeight;
/*     */   
/*     */   protected int entityDepth;
/*     */   
/*     */   protected boolean canPassDoors = true;
/*     */   protected boolean canOpenDoors;
/*     */   protected boolean canFloat;
/*     */   protected boolean canWalkOverFences;
/*     */   
/*     */   public void prepare(PathNavigationRegion level, Mob entity) {
/*  36 */     this.currentContext = new PathfindingContext((CollisionGetter)level, entity);
/*  37 */     this.mob = entity;
/*  38 */     this.nodes.clear();
/*     */     
/*  40 */     this.entityWidth = Mth.floor(entity.getBbWidth() + 1.0F);
/*  41 */     this.entityHeight = Mth.floor(entity.getBbHeight() + 1.0F);
/*  42 */     this.entityDepth = Mth.floor(entity.getBbWidth() + 1.0F);
/*     */   }
/*     */   
/*     */   public void done() {
/*  46 */     this.currentContext = null;
/*  47 */     this.mob = null;
/*     */   }
/*     */   
/*     */   protected Node getNode(BlockPos pos) {
/*  51 */     return getNode(pos.getX(), pos.getY(), pos.getZ());
/*     */   }
/*     */   
/*     */   protected Node getNode(int x, int y, int z) {
/*  55 */     return (Node)this.nodes.computeIfAbsent(Node.createHash(x, y, z), k -> new Node(x, y, z));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Target getTargetNodeAt(double x, double y, double z) {
/*  63 */     return new Target(getNode(Mth.floor(x), Mth.floor(y), Mth.floor(z)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathType getPathType(Mob mob, BlockPos pos) {
/*  73 */     return getPathType(new PathfindingContext((CollisionGetter)mob.level(), mob), pos.getX(), pos.getY(), pos.getZ());
/*     */   }
/*     */   
/*     */   public void setCanPassDoors(boolean canPassDoors) {
/*  77 */     this.canPassDoors = canPassDoors;
/*     */   }
/*     */   
/*     */   public void setCanOpenDoors(boolean canOpenDoors) {
/*  81 */     this.canOpenDoors = canOpenDoors;
/*     */   }
/*     */   
/*     */   public void setCanFloat(boolean canFloat) {
/*  85 */     this.canFloat = canFloat;
/*     */   }
/*     */   
/*     */   public void setCanWalkOverFences(boolean canWalkOverFences) {
/*  89 */     this.canWalkOverFences = canWalkOverFences;
/*     */   }
/*     */   
/*     */   public boolean canPassDoors() {
/*  93 */     return this.canPassDoors;
/*     */   }
/*     */   
/*     */   public boolean canOpenDoors() {
/*  97 */     return this.canOpenDoors;
/*     */   }
/*     */   
/*     */   public boolean canFloat() {
/* 101 */     return this.canFloat;
/*     */   }
/*     */   
/*     */   public boolean canWalkOverFences() {
/* 105 */     return this.canWalkOverFences;
/*     */   }
/*     */   
/*     */   public static boolean isBurningBlock(BlockState blockState) {
/* 109 */     return (blockState.is(BlockTags.FIRE) || 
/* 110 */       blockState.is(Blocks.LAVA) || 
/* 111 */       blockState.is(Blocks.MAGMA_BLOCK) || 
/* 112 */       CampfireBlock.isLitCampfire(blockState) || 
/* 113 */       blockState.is(Blocks.LAVA_CAULDRON));
/*     */   }
/*     */   
/*     */   public abstract Node getStart();
/*     */   
/*     */   public abstract Target getTarget(double paramDouble1, double paramDouble2, double paramDouble3);
/*     */   
/*     */   public abstract int getNeighbors(Node[] paramArrayOfNode, Node paramNode);
/*     */   
/*     */   public abstract PathType getPathTypeOfMob(PathfindingContext paramPathfindingContext, int paramInt1, int paramInt2, int paramInt3, Mob paramMob);
/*     */   
/*     */   public abstract PathType getPathType(PathfindingContext paramPathfindingContext, int paramInt1, int paramInt2, int paramInt3);
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/pathfinder/NodeEvaluator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */