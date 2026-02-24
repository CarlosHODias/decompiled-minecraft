/*     */ package net.minecraft.world.level.border;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function9;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
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
/*     */ public class WorldBorder
/*     */   extends SavedData
/*     */ {
/*     */   public static final double MAX_SIZE = 5.9999968E7D;
/*     */   public static final double MAX_CENTER_COORDINATE = 2.9999984E7D;
/*     */   
/*     */   private class MovingBorderExtent
/*     */     implements BorderExtent
/*     */   {
/*     */     private final double from;
/*     */     private final double to;
/*     */     private final long lerpEnd;
/*     */     private final long lerpBegin;
/*     */     private final double lerpDuration;
/*     */     private long lerpProgress;
/*     */     private double size;
/*     */     private double previousSize;
/*     */     
/*     */     private MovingBorderExtent(double from, double to, long duration, long gameTime) {
/*  70 */       this.from = from;
/*  71 */       this.to = to;
/*     */       
/*  73 */       this.lerpDuration = duration;
/*  74 */       this.lerpProgress = duration;
/*  75 */       this.lerpBegin = gameTime;
/*  76 */       this.lerpEnd = this.lerpBegin + duration;
/*  77 */       double size = calculateSize();
/*  78 */       this.size = size;
/*  79 */       this.previousSize = size;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getMinX(float deltaPartialTick) {
/*  84 */       return Mth.clamp(WorldBorder.this.getCenterX() - Mth.lerp(deltaPartialTick, getPreviousSize(), getSize()) / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public double getMinZ(float deltaPartialTick) {
/*  89 */       return Mth.clamp(WorldBorder.this.getCenterZ() - Mth.lerp(deltaPartialTick, getPreviousSize(), getSize()) / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public double getMaxX(float deltaPartialTick) {
/*  94 */       return Mth.clamp(WorldBorder.this.getCenterX() + Mth.lerp(deltaPartialTick, getPreviousSize(), getSize()) / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public double getMaxZ(float deltaPartialTick) {
/*  99 */       return Mth.clamp(WorldBorder.this.getCenterZ() + Mth.lerp(deltaPartialTick, getPreviousSize(), getSize()) / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public double getSize() {
/* 104 */       return this.size;
/*     */     }
/*     */     
/*     */     public double getPreviousSize() {
/* 108 */       return this.previousSize;
/*     */     }
/*     */     
/*     */     private double calculateSize() {
/* 112 */       double progress = (this.lerpDuration - this.lerpProgress) / this.lerpDuration;
/* 113 */       return (progress < 1.0D) ? Mth.lerp(progress, this.from, this.to) : this.to;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getLerpSpeed() {
/* 118 */       return Math.abs(this.from - this.to) / (this.lerpEnd - this.lerpBegin);
/*     */     }
/*     */ 
/*     */     
/*     */     public long getLerpTime() {
/* 123 */       return this.lerpProgress;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getLerpTarget() {
/* 128 */       return this.to;
/*     */     }
/*     */ 
/*     */     
/*     */     public BorderStatus getStatus() {
/* 133 */       return (this.to < this.from) ? BorderStatus.SHRINKING : BorderStatus.GROWING;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onCenterChange() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void onAbsoluteMaxSizeChange() {}
/*     */ 
/*     */     
/*     */     public WorldBorder.BorderExtent update() {
/* 146 */       this.lerpProgress--;
/* 147 */       this.previousSize = this.size;
/* 148 */       this.size = calculateSize();
/* 149 */       if (this.lerpProgress <= 0L) {
/* 150 */         WorldBorder.this.setDirty();
/* 151 */         return new WorldBorder.StaticBorderExtent(this.to);
/*     */       } 
/*     */       
/* 154 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VoxelShape getCollisionShape() {
/* 159 */       return Shapes.join(Shapes.INFINITY, Shapes.box(
/* 160 */             Math.floor(getMinX(0.0F)), Double.NEGATIVE_INFINITY, Math.floor(getMinZ(0.0F)), 
/* 161 */             Math.ceil(getMaxX(0.0F)), Double.POSITIVE_INFINITY, Math.ceil(getMaxZ(0.0F))), BooleanOp.ONLY_FIRST);
/*     */     }
/*     */   } private static interface BorderExtent { double getMinX(float param1Float); double getMaxX(float param1Float);
/*     */     double getMinZ(float param1Float);
/*     */     double getMaxZ(float param1Float);
/*     */     double getSize();
/*     */     double getLerpSpeed();
/*     */     long getLerpTime();
/*     */     double getLerpTarget();
/*     */     BorderStatus getStatus();
/*     */     void onAbsoluteMaxSizeChange();
/*     */     void onCenterChange();
/*     */     BorderExtent update();
/*     */     VoxelShape getCollisionShape(); }
/*     */   private class StaticBorderExtent implements BorderExtent { private final double size; private double minX; private double minZ;
/*     */     public StaticBorderExtent(double size) {
/* 177 */       this.size = size;
/* 178 */       updateBox();
/*     */     }
/*     */     private double maxX; private double maxZ; private VoxelShape shape;
/*     */     
/*     */     public double getMinX(float deltaPartialTick) {
/* 183 */       return this.minX;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getMaxX(float deltaPartialTick) {
/* 188 */       return this.maxX;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getMinZ(float deltaPartialTick) {
/* 193 */       return this.minZ;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getMaxZ(float deltaPartialTick) {
/* 198 */       return this.maxZ;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getSize() {
/* 203 */       return this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public BorderStatus getStatus() {
/* 208 */       return BorderStatus.STATIONARY;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getLerpSpeed() {
/* 213 */       return 0.0D;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getLerpTime() {
/* 218 */       return 0L;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getLerpTarget() {
/* 223 */       return this.size;
/*     */     }
/*     */     
/*     */     private void updateBox() {
/* 227 */       this.minX = Mth.clamp(WorldBorder.this.getCenterX() - this.size / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/* 228 */       this.minZ = Mth.clamp(WorldBorder.this.getCenterZ() - this.size / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/* 229 */       this.maxX = Mth.clamp(WorldBorder.this.getCenterX() + this.size / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/* 230 */       this.maxZ = Mth.clamp(WorldBorder.this.getCenterZ() + this.size / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/*     */       
/* 232 */       this.shape = Shapes.join(Shapes.INFINITY, Shapes.box(
/* 233 */             Math.floor(getMinX(0.0F)), Double.NEGATIVE_INFINITY, Math.floor(getMinZ(0.0F)), 
/* 234 */             Math.ceil(getMaxX(0.0F)), Double.POSITIVE_INFINITY, Math.ceil(getMaxZ(0.0F))), BooleanOp.ONLY_FIRST);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onAbsoluteMaxSizeChange() {
/* 240 */       updateBox();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onCenterChange() {
/* 245 */       updateBox();
/*     */     }
/*     */ 
/*     */     
/*     */     public WorldBorder.BorderExtent update() {
/* 250 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VoxelShape getCollisionShape() {
/* 255 */       return this.shape;
/*     */     } }
/*     */ 
/*     */   
/* 259 */   public static final Codec<WorldBorder> CODEC = Settings.CODEC.xmap(WorldBorder::new, Settings::new);
/*     */   
/* 261 */   public static final SavedDataType<WorldBorder> TYPE = new SavedDataType("world_border", WorldBorder::new, CODEC, DataFixTypes.SAVED_DATA_WORLD_BORDER);
/*     */ 
/*     */ 
/*     */   
/*     */   private final Settings settings;
/*     */ 
/*     */   
/*     */   private boolean initialized;
/*     */ 
/*     */   
/* 271 */   private final List<BorderChangeListener> listeners = Lists.newArrayList();
/*     */   
/* 273 */   private double damagePerBlock = 0.2D;
/* 274 */   private double safeZone = 5.0D;
/* 275 */   private int warningTime = 15;
/* 276 */   private int warningBlocks = 5;
/*     */   
/*     */   private double centerX;
/*     */   
/*     */   private double centerZ;
/* 281 */   private int absoluteMaxSize = 29999984;
/*     */   
/* 283 */   private BorderExtent extent = new StaticBorderExtent(5.9999968E7D);
/*     */   
/*     */   public WorldBorder() {
/* 286 */     this(Settings.DEFAULT);
/*     */   }
/*     */   
/*     */   public WorldBorder(Settings settings) {
/* 290 */     this.settings = settings;
/*     */   }
/*     */   
/*     */   public boolean isWithinBounds(BlockPos pos) {
/* 294 */     return isWithinBounds(pos.getX(), pos.getZ());
/*     */   }
/*     */   
/*     */   public boolean isWithinBounds(Vec3 pos) {
/* 298 */     return isWithinBounds(pos.x, pos.z);
/*     */   }
/*     */   
/*     */   public boolean isWithinBounds(ChunkPos pos) {
/* 302 */     return (isWithinBounds(pos.getMinBlockX(), pos.getMinBlockZ()) && 
/* 303 */       isWithinBounds(pos.getMaxBlockX(), pos.getMaxBlockZ()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWithinBounds(AABB aabb) {
/* 308 */     return isWithinBounds(aabb.minX, aabb.minZ, aabb.maxX - 9.999999747378752E-6D, aabb.maxZ - 9.999999747378752E-6D);
/*     */   }
/*     */   
/*     */   private boolean isWithinBounds(double minX, double minZ, double maxX, double maxZ) {
/* 312 */     return (isWithinBounds(minX, minZ) && isWithinBounds(maxX, maxZ));
/*     */   }
/*     */   
/*     */   public boolean isWithinBounds(double x, double z) {
/* 316 */     return isWithinBounds(x, z, 0.0D);
/*     */   }
/*     */   
/*     */   public boolean isWithinBounds(double x, double z, double margin) {
/* 320 */     return (x >= getMinX() - margin && x < 
/* 321 */       getMaxX() + margin && z >= 
/* 322 */       getMinZ() - margin && z < 
/* 323 */       getMaxZ() + margin);
/*     */   }
/*     */   
/*     */   public BlockPos clampToBounds(BlockPos position) {
/* 327 */     return clampToBounds(position.getX(), position.getY(), position.getZ());
/*     */   }
/*     */   
/*     */   public BlockPos clampToBounds(Vec3 position) {
/* 331 */     return clampToBounds(position.x(), position.y(), position.z());
/*     */   }
/*     */   
/*     */   public BlockPos clampToBounds(double x, double y, double z) {
/* 335 */     return BlockPos.containing((Position)clampVec3ToBound(x, y, z));
/*     */   }
/*     */   
/*     */   public Vec3 clampVec3ToBound(Vec3 position) {
/* 339 */     return clampVec3ToBound(position.x, position.y, position.z);
/*     */   }
/*     */   
/*     */   public Vec3 clampVec3ToBound(double x, double y, double z) {
/* 343 */     return new Vec3(
/* 344 */         Mth.clamp(x, getMinX(), getMaxX() - 9.999999747378752E-6D), y, 
/*     */         
/* 346 */         Mth.clamp(z, getMinZ(), getMaxZ() - 9.999999747378752E-6D));
/*     */   }
/*     */   
/*     */   public double getDistanceToBorder(Entity entity) {
/* 350 */     return getDistanceToBorder(entity.getX(), entity.getZ());
/*     */   }
/*     */   
/*     */   public VoxelShape getCollisionShape() {
/* 354 */     return this.extent.getCollisionShape();
/*     */   }
/*     */   
/*     */   public double getDistanceToBorder(double x, double z) {
/* 358 */     double fromNorth = z - getMinZ();
/* 359 */     double fromSouth = getMaxZ() - z;
/* 360 */     double fromWest = x - getMinX();
/* 361 */     double fromEast = getMaxX() - x;
/* 362 */     double min = Math.min(fromWest, fromEast);
/* 363 */     min = Math.min(min, fromNorth);
/* 364 */     return Math.min(min, fromSouth);
/*     */   }
/*     */   
/*     */   public boolean isInsideCloseToBorder(Entity source, AABB boundingBox) {
/* 368 */     double bbMax = Math.max(Mth.absMax(boundingBox.getXsize(), boundingBox.getZsize()), 1.0D);
/* 369 */     return (getDistanceToBorder(source) < bbMax * 2.0D && isWithinBounds(source.getX(), source.getZ(), bbMax));
/*     */   }
/*     */   
/*     */   public BorderStatus getStatus() {
/* 373 */     return this.extent.getStatus();
/*     */   }
/*     */   
/*     */   public double getMinX() {
/* 377 */     return getMinX(0.0F);
/*     */   }
/*     */   
/*     */   public double getMinX(float deltaPartialTick) {
/* 381 */     return this.extent.getMinX(deltaPartialTick);
/*     */   }
/*     */   
/*     */   public double getMinZ() {
/* 385 */     return getMinZ(0.0F);
/*     */   }
/*     */   
/*     */   public double getMinZ(float deltaPartialTick) {
/* 389 */     return this.extent.getMinZ(deltaPartialTick);
/*     */   }
/*     */   
/*     */   public double getMaxX() {
/* 393 */     return getMaxX(0.0F);
/*     */   }
/*     */   
/*     */   public double getMaxX(float deltaPartialTick) {
/* 397 */     return this.extent.getMaxX(deltaPartialTick);
/*     */   }
/*     */   
/*     */   public double getMaxZ() {
/* 401 */     return getMaxZ(0.0F);
/*     */   }
/*     */   
/*     */   public double getMaxZ(float deltaPartialTick) {
/* 405 */     return this.extent.getMaxZ(deltaPartialTick);
/*     */   }
/*     */   
/*     */   public double getCenterX() {
/* 409 */     return this.centerX;
/*     */   }
/*     */   
/*     */   public double getCenterZ() {
/* 413 */     return this.centerZ;
/*     */   }
/*     */   
/*     */   public void setCenter(double x, double z) {
/* 417 */     this.centerX = x;
/* 418 */     this.centerZ = z;
/*     */     
/* 420 */     this.extent.onCenterChange();
/* 421 */     setDirty();
/*     */     
/* 423 */     for (BorderChangeListener listener : getListeners()) {
/* 424 */       listener.onSetCenter(this, x, z);
/*     */     }
/*     */   }
/*     */   
/*     */   public double getSize() {
/* 429 */     return this.extent.getSize();
/*     */   }
/*     */   
/*     */   public long getLerpTime() {
/* 433 */     return this.extent.getLerpTime();
/*     */   }
/*     */   
/*     */   public double getLerpTarget() {
/* 437 */     return this.extent.getLerpTarget();
/*     */   }
/*     */   
/*     */   public void setSize(double size) {
/* 441 */     this.extent = new StaticBorderExtent(size);
/* 442 */     setDirty();
/*     */     
/* 444 */     for (BorderChangeListener listener : getListeners()) {
/* 445 */       listener.onSetSize(this, size);
/*     */     }
/*     */   }
/*     */   
/*     */   public void lerpSizeBetween(double from, double to, long ticks, long gameTime) {
/* 450 */     this.extent = (from == to) ? new StaticBorderExtent(to) : new MovingBorderExtent(from, to, ticks, gameTime);
/* 451 */     setDirty();
/*     */     
/* 453 */     for (BorderChangeListener listener : getListeners()) {
/* 454 */       listener.onLerpSize(this, from, to, ticks, gameTime);
/*     */     }
/*     */   }
/*     */   
/*     */   protected List<BorderChangeListener> getListeners() {
/* 459 */     return Lists.newArrayList(this.listeners);
/*     */   }
/*     */   
/*     */   public void addListener(BorderChangeListener listener) {
/* 463 */     this.listeners.add(listener);
/*     */   }
/*     */   
/*     */   public void removeListener(BorderChangeListener listener) {
/* 467 */     this.listeners.remove(listener);
/*     */   }
/*     */   
/*     */   public void setAbsoluteMaxSize(int absoluteMaxSize) {
/* 471 */     this.absoluteMaxSize = absoluteMaxSize;
/* 472 */     this.extent.onAbsoluteMaxSizeChange();
/*     */   }
/*     */   
/*     */   public int getAbsoluteMaxSize() {
/* 476 */     return this.absoluteMaxSize;
/*     */   }
/*     */   
/*     */   public double getSafeZone() {
/* 480 */     return this.safeZone;
/*     */   }
/*     */   
/*     */   public void setSafeZone(double safeZone) {
/* 484 */     this.safeZone = safeZone;
/* 485 */     setDirty();
/*     */     
/* 487 */     for (BorderChangeListener listener : getListeners()) {
/* 488 */       listener.onSetSafeZone(this, safeZone);
/*     */     }
/*     */   }
/*     */   
/*     */   public double getDamagePerBlock() {
/* 493 */     return this.damagePerBlock;
/*     */   }
/*     */   
/*     */   public void setDamagePerBlock(double damagePerBlock) {
/* 497 */     this.damagePerBlock = damagePerBlock;
/* 498 */     setDirty();
/*     */     
/* 500 */     for (BorderChangeListener listener : getListeners()) {
/* 501 */       listener.onSetDamagePerBlock(this, damagePerBlock);
/*     */     }
/*     */   }
/*     */   
/*     */   public double getLerpSpeed() {
/* 506 */     return this.extent.getLerpSpeed();
/*     */   }
/*     */   
/*     */   public int getWarningTime() {
/* 510 */     return this.warningTime;
/*     */   }
/*     */   
/*     */   public void setWarningTime(int warningTime) {
/* 514 */     this.warningTime = warningTime;
/* 515 */     setDirty();
/*     */     
/* 517 */     for (BorderChangeListener listener : getListeners()) {
/* 518 */       listener.onSetWarningTime(this, warningTime);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getWarningBlocks() {
/* 523 */     return this.warningBlocks;
/*     */   }
/*     */   
/*     */   public void setWarningBlocks(int warningBlocks) {
/* 527 */     this.warningBlocks = warningBlocks;
/* 528 */     setDirty();
/*     */     
/* 530 */     for (BorderChangeListener listener : getListeners()) {
/* 531 */       listener.onSetWarningBlocks(this, warningBlocks);
/*     */     }
/*     */   }
/*     */   
/*     */   public void tick() {
/* 536 */     this.extent = this.extent.update();
/*     */   }
/*     */   
/*     */   public void applyInitialSettings(long gameTime) {
/* 540 */     if (!this.initialized) {
/* 541 */       setCenter(this.settings.centerX(), this.settings.centerZ());
/* 542 */       setDamagePerBlock(this.settings.damagePerBlock());
/* 543 */       setSafeZone(this.settings.safeZone());
/* 544 */       setWarningBlocks(this.settings.warningBlocks());
/* 545 */       setWarningTime(this.settings.warningTime());
/*     */       
/* 547 */       if (this.settings.lerpTime() > 0L) {
/* 548 */         lerpSizeBetween(this.settings.size(), this.settings.lerpTarget(), this.settings.lerpTime(), gameTime);
/*     */       } else {
/* 550 */         setSize(this.settings.size());
/*     */       } 
/* 552 */       this.initialized = true;
/*     */     } 
/*     */   }
/*     */   public static final class Settings extends Record { private final double centerX; private final double centerZ; private final double damagePerBlock; private final double safeZone; private final int warningBlocks; private final int warningTime; private final double size; private final long lerpTime; private final double lerpTarget;
/* 556 */     public Settings(double centerX, double centerZ, double damagePerBlock, double safeZone, int warningBlocks, int warningTime, double size, long lerpTime, double lerpTarget) { this.centerX = centerX; this.centerZ = centerZ; this.damagePerBlock = damagePerBlock; this.safeZone = safeZone; this.warningBlocks = warningBlocks; this.warningTime = warningTime; this.size = size; this.lerpTime = lerpTime; this.lerpTarget = lerpTarget; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/border/WorldBorder$Settings;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #556	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/border/WorldBorder$Settings; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/border/WorldBorder$Settings;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #556	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/border/WorldBorder$Settings; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/border/WorldBorder$Settings;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #556	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/border/WorldBorder$Settings;
/* 556 */       //   0	8	1	o	Ljava/lang/Object; } public double centerX() { return this.centerX; } public double centerZ() { return this.centerZ; } public double damagePerBlock() { return this.damagePerBlock; } public double safeZone() { return this.safeZone; } public int warningBlocks() { return this.warningBlocks; } public int warningTime() { return this.warningTime; } public double size() { return this.size; } public long lerpTime() { return this.lerpTime; } public double lerpTarget() { return this.lerpTarget; }
/* 557 */      public static final Settings DEFAULT = new Settings(0.0D, 0.0D, 0.2D, 5.0D, 5, 300, 5.9999968E7D, 0L, 0.0D); public static final Codec<Settings> CODEC; static {
/* 558 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.doubleRange(-2.9999984E7D, 2.9999984E7D).fieldOf("center_x").forGetter(Settings::centerX), (App)Codec.doubleRange(-2.9999984E7D, 2.9999984E7D).fieldOf("center_z").forGetter(Settings::centerZ), (App)Codec.DOUBLE.fieldOf("damage_per_block").forGetter(Settings::damagePerBlock), (App)Codec.DOUBLE.fieldOf("safe_zone").forGetter(Settings::safeZone), (App)Codec.INT.fieldOf("warning_blocks").forGetter(Settings::warningBlocks), (App)Codec.INT.fieldOf("warning_time").forGetter(Settings::warningTime), (App)Codec.DOUBLE.fieldOf("size").forGetter(Settings::size), (App)Codec.LONG.fieldOf("lerp_time").forGetter(Settings::lerpTime), (App)Codec.DOUBLE.fieldOf("lerp_target").forGetter(Settings::lerpTarget)).apply((Applicative)i, Settings::new));
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
/*     */     public Settings(WorldBorder worldBorder) {
/* 571 */       this(worldBorder.centerX, worldBorder.centerZ, worldBorder.damagePerBlock, worldBorder.safeZone, worldBorder.warningBlocks, worldBorder.warningTime, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 578 */           worldBorder.extent.getSize(), 
/* 579 */           worldBorder.extent.getLerpTime(), 
/* 580 */           worldBorder.extent.getLerpTarget());
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/border/WorldBorder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */