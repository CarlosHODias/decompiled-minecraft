/*     */ package net.minecraft.world.level.block.entity;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.features.EndFeatures;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TheEndGatewayBlockEntity extends TheEndPortalBlockEntity {
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int SPAWN_TIME = 200;
/*     */   
/*     */   private static final int COOLDOWN_TIME = 40;
/*     */   private static final int ATTENTION_INTERVAL = 2400;
/*     */   private static final int EVENT_COOLDOWN = 1;
/*     */   private static final int GATEWAY_HEIGHT_ABOVE_SURFACE = 10;
/*     */   private static final long DEFAULT_AGE = 0L;
/*     */   private static final boolean DEFAULT_EXACT_TELEPORT = false;
/*  44 */   private long age = 0L;
/*     */   private int teleportCooldown;
/*     */   private BlockPos exitPortal;
/*     */   private boolean exactTeleport = false;
/*     */   
/*     */   public TheEndGatewayBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  50 */     super(BlockEntityType.END_GATEWAY, worldPosition, blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  55 */     super.saveAdditional(output);
/*  56 */     output.putLong("Age", this.age);
/*  57 */     output.storeNullable("exit_portal", BlockPos.CODEC, this.exitPortal);
/*  58 */     if (this.exactTeleport) {
/*  59 */       output.putBoolean("ExactTeleport", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  65 */     super.loadAdditional(input);
/*  66 */     this.age = input.getLongOr("Age", 0L);
/*  67 */     this
/*     */       
/*  69 */       .exitPortal = input.read("exit_portal", BlockPos.CODEC).filter(Level::isInSpawnableBounds).orElse(null);
/*  70 */     this.exactTeleport = input.getBooleanOr("ExactTeleport", false);
/*     */   }
/*     */   
/*     */   public static void beamAnimationTick(Level level, BlockPos pos, BlockState state, TheEndGatewayBlockEntity entity) {
/*  74 */     entity.age++;
/*     */     
/*  76 */     if (entity.isCoolingDown()) {
/*  77 */       entity.teleportCooldown--;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void portalTick(Level level, BlockPos pos, BlockState state, TheEndGatewayBlockEntity entity) {
/*  82 */     boolean spawning = entity.isSpawning();
/*  83 */     boolean coolingDown = entity.isCoolingDown();
/*  84 */     entity.age++;
/*     */     
/*  86 */     if (coolingDown) {
/*  87 */       entity.teleportCooldown--;
/*     */     }
/*  89 */     else if (entity.age % 2400L == 0L) {
/*  90 */       triggerCooldown(level, pos, state, entity);
/*     */     } 
/*     */ 
/*     */     
/*  94 */     if (spawning != entity.isSpawning() || coolingDown != entity.isCoolingDown()) {
/*  95 */       setChanged(level, pos, state);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isSpawning() {
/* 100 */     return (this.age < 200L);
/*     */   }
/*     */   
/*     */   public boolean isCoolingDown() {
/* 104 */     return (this.teleportCooldown > 0);
/*     */   }
/*     */   
/*     */   public float getSpawnPercent(float a) {
/* 108 */     return Mth.clamp(((float)this.age + a) / 200.0F, 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public float getCooldownPercent(float a) {
/* 112 */     return 1.0F - Mth.clamp((this.teleportCooldown - a) / 40.0F, 0.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 117 */     return ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
/* 122 */     return saveCustomOnly(registries);
/*     */   }
/*     */   
/*     */   public static void triggerCooldown(Level level, BlockPos pos, BlockState blockState, TheEndGatewayBlockEntity entity) {
/* 126 */     if (!level.isClientSide()) {
/* 127 */       entity.teleportCooldown = 40;
/* 128 */       level.blockEvent(pos, blockState.getBlock(), 1, 0);
/* 129 */       setChanged(level, pos, blockState);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean triggerEvent(int b0, int b1) {
/* 135 */     if (b0 == 1) {
/* 136 */       this.teleportCooldown = 40;
/* 137 */       return true;
/*     */     } 
/*     */     
/* 140 */     return super.triggerEvent(b0, b1);
/*     */   }
/*     */   
/*     */   public Vec3 getPortalPosition(ServerLevel currentLevel, BlockPos portalEntryPos) {
/* 144 */     if (this.exitPortal == null && currentLevel.dimension() == Level.END) {
/* 145 */       BlockPos exitPortalPos = findOrCreateValidTeleportPos(currentLevel, portalEntryPos);
/* 146 */       exitPortalPos = exitPortalPos.above(10);
/* 147 */       LOGGER.debug("Creating portal at {}", exitPortalPos);
/* 148 */       spawnGatewayPortal(currentLevel, exitPortalPos, EndGatewayConfiguration.knownExit(portalEntryPos, false));
/* 149 */       setExitPosition(exitPortalPos, this.exactTeleport);
/*     */     } 
/*     */     
/* 152 */     if (this.exitPortal != null) {
/* 153 */       BlockPos pos = this.exactTeleport ? this.exitPortal : findExitPosition((Level)currentLevel, this.exitPortal);
/* 154 */       return pos.getBottomCenter();
/*     */     } 
/* 156 */     return null;
/*     */   }
/*     */   
/*     */   private static BlockPos findExitPosition(Level level, BlockPos exitPortal) {
/* 160 */     BlockPos pos = findTallestBlock((BlockGetter)level, exitPortal.offset(0, 2, 0), 5, false);
/* 161 */     LOGGER.debug("Best exit position for portal at {} is {}", exitPortal, pos);
/* 162 */     return pos.above();
/*     */   }
/*     */   
/*     */   private static BlockPos findOrCreateValidTeleportPos(ServerLevel level, BlockPos endGatewayPos) {
/* 166 */     Vec3 exitPortalXZPosTentative = findExitPortalXZPosTentative(level, endGatewayPos);
/*     */     
/* 168 */     LevelChunk exitPortalChunk = getChunk((Level)level, exitPortalXZPosTentative);
/*     */     
/* 170 */     BlockPos exitPortalPos = findValidSpawnInChunk(exitPortalChunk);
/*     */     
/* 172 */     if (exitPortalPos == null) {
/* 173 */       BlockPos newExitPortalPos = BlockPos.containing(exitPortalXZPosTentative.x + 0.5D, 75.0D, exitPortalXZPosTentative.z + 0.5D);
/* 174 */       LOGGER.debug("Failed to find a suitable block to teleport to, spawning an island on {}", newExitPortalPos);
/* 175 */       level.registryAccess().lookup(Registries.CONFIGURED_FEATURE)
/* 176 */         .flatMap(registry -> registry.get(EndFeatures.END_ISLAND))
/* 177 */         .ifPresent(endIsland -> ((ConfiguredFeature)endIsland.value()).place((WorldGenLevel)level, level.getChunkSource().getGenerator(), RandomSource.create(newExitPortalPos.asLong()), newExitPortalPos));
/* 178 */       exitPortalPos = newExitPortalPos;
/*     */     } else {
/* 180 */       LOGGER.debug("Found suitable block to teleport to: {}", exitPortalPos);
/*     */     } 
/*     */     
/* 183 */     return findTallestBlock((BlockGetter)level, exitPortalPos, 16, true);
/*     */   }
/*     */   
/*     */   private static Vec3 findExitPortalXZPosTentative(ServerLevel level, BlockPos endGatewayPos) {
/* 187 */     Vec3 teleportXZDirectionVector = new Vec3(endGatewayPos.getX(), 0.0D, endGatewayPos.getZ()).normalize();
/* 188 */     int teleportDistance = 1024;
/* 189 */     Vec3 exitPortalXZPosTentative = teleportXZDirectionVector.scale(1024.0D);
/*     */     
/* 191 */     int chunkLimit = 16;
/* 192 */     while (!isChunkEmpty(level, exitPortalXZPosTentative) && chunkLimit-- > 0) {
/* 193 */       LOGGER.debug("Skipping backwards past nonempty chunk at {}", exitPortalXZPosTentative);
/* 194 */       exitPortalXZPosTentative = exitPortalXZPosTentative.add(teleportXZDirectionVector.scale(-16.0D));
/*     */     } 
/*     */     
/* 197 */     chunkLimit = 16;
/* 198 */     while (isChunkEmpty(level, exitPortalXZPosTentative) && chunkLimit-- > 0) {
/* 199 */       LOGGER.debug("Skipping forward past empty chunk at {}", exitPortalXZPosTentative);
/* 200 */       exitPortalXZPosTentative = exitPortalXZPosTentative.add(teleportXZDirectionVector.scale(16.0D));
/*     */     } 
/* 202 */     LOGGER.debug("Found chunk at {}", exitPortalXZPosTentative);
/* 203 */     return exitPortalXZPosTentative;
/*     */   }
/*     */   
/*     */   private static boolean isChunkEmpty(ServerLevel level, Vec3 xzPos) {
/* 207 */     return (getChunk((Level)level, xzPos).getHighestFilledSectionIndex() == -1);
/*     */   }
/*     */   
/*     */   private static BlockPos findTallestBlock(BlockGetter level, BlockPos around, int dist, boolean allowBedrock) {
/* 211 */     BlockPos tallest = null;
/*     */     
/* 213 */     for (int xd = -dist; xd <= dist; xd++) {
/* 214 */       for (int zd = -dist; zd <= dist; zd++) {
/* 215 */         if (xd != 0 || zd != 0 || allowBedrock) {
/*     */ 
/*     */ 
/*     */           
/* 219 */           int y = level.getMaxY(); while (true) { if (y > ((tallest == null) ? level.getMinY() : tallest.getY())) {
/* 220 */               BlockPos pos = new BlockPos(around.getX() + xd, y, around.getZ() + zd);
/* 221 */               BlockState state = level.getBlockState(pos);
/* 222 */               if (state.isCollisionShapeFullBlock(level, pos) && (allowBedrock || !state.is(Blocks.BEDROCK))) {
/* 223 */                 tallest = pos; break;
/*     */               }  y--; continue;
/*     */             }  break; }
/*     */         
/*     */         } 
/*     */       } 
/*     */     } 
/* 230 */     return (tallest == null) ? around : tallest;
/*     */   }
/*     */   
/*     */   private static LevelChunk getChunk(Level level, Vec3 pos) {
/* 234 */     return level.getChunk(Mth.floor(pos.x / 16.0D), Mth.floor(pos.z / 16.0D));
/*     */   }
/*     */   
/*     */   private static BlockPos findValidSpawnInChunk(LevelChunk chunk) {
/* 238 */     ChunkPos chunkPos = chunk.getPos();
/* 239 */     BlockPos start = new BlockPos(chunkPos.getMinBlockX(), 30, chunkPos.getMinBlockZ());
/* 240 */     int maxY = chunk.getHighestSectionPosition() + 16 - 1;
/* 241 */     BlockPos end = new BlockPos(chunkPos.getMaxBlockX(), maxY, chunkPos.getMaxBlockZ());
/* 242 */     BlockPos closest = null;
/* 243 */     double closestDist = 0.0D;
/*     */ 
/*     */     
/* 246 */     for (BlockPos pos : (Iterable<BlockPos>)BlockPos.betweenClosed(start, end)) {
/* 247 */       BlockState state = chunk.getBlockState(pos);
/*     */       
/* 249 */       BlockPos above = pos.above();
/* 250 */       BlockPos above2 = pos.above(2);
/* 251 */       if (state.is(Blocks.END_STONE) && !chunk.getBlockState(above).isCollisionShapeFullBlock((BlockGetter)chunk, above) && !chunk.getBlockState(above2).isCollisionShapeFullBlock((BlockGetter)chunk, above2)) {
/* 252 */         double dist = pos.distToCenterSqr(0.0D, 0.0D, 0.0D);
/* 253 */         if (closest == null || dist < closestDist) {
/* 254 */           closest = pos;
/* 255 */           closestDist = dist;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 260 */     return closest;
/*     */   }
/*     */   
/*     */   private static void spawnGatewayPortal(ServerLevel level, BlockPos portalPos, EndGatewayConfiguration config) {
/* 264 */     Feature.END_GATEWAY.place((FeatureConfiguration)config, (WorldGenLevel)level, level.getChunkSource().getGenerator(), RandomSource.create(), portalPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRenderFace(Direction direction) {
/* 269 */     return Block.shouldRenderFace(getBlockState(), this.level.getBlockState(getBlockPos().relative(direction)), direction);
/*     */   }
/*     */   
/*     */   public int getParticleAmount() {
/* 273 */     int count = 0;
/* 274 */     for (Direction direction : Direction.values()) {
/* 275 */       count += shouldRenderFace(direction) ? 1 : 0;
/*     */     }
/* 277 */     return count;
/*     */   }
/*     */   
/*     */   public void setExitPosition(BlockPos exactPosition, boolean exact) {
/* 281 */     this.exactTeleport = exact;
/* 282 */     this.exitPortal = exactPosition;
/* 283 */     setChanged();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/TheEndGatewayBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */