/*     */ package net.minecraft.client.renderer.block;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.color.block.BlockColors;
/*     */ import net.minecraft.client.renderer.LevelRenderer;
/*     */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*     */ import net.minecraft.client.renderer.block.model.BlockModelPart;
/*     */ import net.minecraft.client.renderer.block.model.BlockStateModel;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.BlockAndTintGetter;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class ModelBlockRenderer {
/*  32 */   private static final Direction[] DIRECTIONS = Direction.values();
/*     */   private final BlockColors blockColors;
/*     */   private static final int CACHE_SIZE = 100;
/*     */   
/*     */   public ModelBlockRenderer(BlockColors blockColors) {
/*  37 */     this.blockColors = blockColors;
/*     */   }
/*     */   
/*     */   public void tesselateBlock(BlockAndTintGetter level, List<BlockModelPart> parts, BlockState blockState, BlockPos pos, PoseStack poseStack, VertexConsumer builder, boolean cull, int overlayCoords) {
/*  41 */     if (parts.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  45 */     boolean useAO = (Minecraft.useAmbientOcclusion() && blockState.getLightEmission() == 0 && ((BlockModelPart)parts.getFirst()).useAmbientOcclusion());
/*  46 */     poseStack.translate(blockState.getOffset(pos));
/*     */     try {
/*  48 */       if (useAO) {
/*  49 */         tesselateWithAO(level, parts, blockState, pos, poseStack, builder, cull, overlayCoords);
/*     */       } else {
/*  51 */         tesselateWithoutAO(level, parts, blockState, pos, poseStack, builder, cull, overlayCoords);
/*     */       } 
/*  53 */     } catch (Throwable t) {
/*  54 */       CrashReport report = CrashReport.forThrowable(t, "Tesselating block model");
/*  55 */       CrashReportCategory category = report.addCategory("Block model being tesselated");
/*     */       
/*  57 */       CrashReportCategory.populateBlockDetails(category, (LevelHeightAccessor)level, pos, blockState);
/*  58 */       category.setDetail("Using AO", useAO);
/*  59 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean shouldRenderFace(BlockAndTintGetter level, BlockState state, boolean cullEnabled, Direction direction, BlockPos neighborPos) {
/*  64 */     if (!cullEnabled) {
/*  65 */       return true;
/*     */     }
/*  67 */     BlockState neighborState = level.getBlockState(neighborPos);
/*  68 */     return Block.shouldRenderFace(state, neighborState, direction);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tesselateWithAO(BlockAndTintGetter level, List<BlockModelPart> parts, BlockState state, BlockPos pos, PoseStack poseStack, VertexConsumer builder, boolean cull, int overlayCoords) {
/*  73 */     AmbientOcclusionRenderStorage scratch = new AmbientOcclusionRenderStorage();
/*     */     
/*  75 */     int cacheValid = 0;
/*  76 */     int shouldRenderFaceCache = 0;
/*     */     
/*  78 */     for (BlockModelPart part : parts) {
/*  79 */       for (Direction direction : DIRECTIONS) {
/*  80 */         int cacheMask = 1 << direction.ordinal();
/*  81 */         boolean validCacheForDirection = ((cacheValid & cacheMask) == 1);
/*  82 */         boolean shouldRenderFace = ((shouldRenderFaceCache & cacheMask) == 1);
/*     */         
/*  84 */         if (!validCacheForDirection || shouldRenderFace) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  89 */           List<BakedQuad> culledQuads = part.getQuads(direction);
/*  90 */           if (!culledQuads.isEmpty()) {
/*     */ 
/*     */ 
/*     */             
/*  94 */             if (!validCacheForDirection) {
/*  95 */               shouldRenderFace = shouldRenderFace(level, state, cull, direction, (BlockPos)scratch.scratchPos.setWithOffset((Vec3i)pos, direction));
/*  96 */               cacheValid |= cacheMask;
/*  97 */               if (shouldRenderFace) {
/*  98 */                 shouldRenderFaceCache |= cacheMask;
/*     */               }
/*     */             } 
/*     */             
/* 102 */             if (shouldRenderFace)
/* 103 */               renderModelFaceAO(level, state, pos, poseStack, builder, culledQuads, scratch, overlayCoords); 
/*     */           } 
/*     */         } 
/*     */       } 
/* 107 */       List<BakedQuad> unculledQuads = part.getQuads(null);
/* 108 */       if (!unculledQuads.isEmpty()) {
/* 109 */         renderModelFaceAO(level, state, pos, poseStack, builder, unculledQuads, scratch, overlayCoords);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tesselateWithoutAO(BlockAndTintGetter level, List<BlockModelPart> parts, BlockState state, BlockPos pos, PoseStack poseStack, VertexConsumer builder, boolean cull, int overlayCoords) {
/* 116 */     CommonRenderStorage scratch = new CommonRenderStorage();
/* 117 */     int cacheValid = 0;
/* 118 */     int shouldRenderFaceCache = 0;
/*     */     
/* 120 */     for (BlockModelPart part : parts) {
/* 121 */       for (Direction direction : DIRECTIONS) {
/* 122 */         int cacheMask = 1 << direction.ordinal();
/* 123 */         boolean validCacheForDirection = ((cacheValid & cacheMask) == 1);
/* 124 */         boolean shouldRenderFace = ((shouldRenderFaceCache & cacheMask) == 1);
/*     */         
/* 126 */         if (!validCacheForDirection || shouldRenderFace) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 131 */           List<BakedQuad> culledQuads = part.getQuads(direction);
/* 132 */           if (!culledQuads.isEmpty()) {
/*     */ 
/*     */ 
/*     */             
/* 136 */             BlockPos.MutableBlockPos mutableBlockPos = scratch.scratchPos.setWithOffset((Vec3i)pos, direction);
/* 137 */             if (!validCacheForDirection) {
/* 138 */               shouldRenderFace = shouldRenderFace(level, state, cull, direction, (BlockPos)mutableBlockPos);
/* 139 */               cacheValid |= cacheMask;
/* 140 */               if (shouldRenderFace) {
/* 141 */                 shouldRenderFaceCache |= cacheMask;
/*     */               }
/*     */             } 
/*     */             
/* 145 */             if (shouldRenderFace) {
/* 146 */               int lightColor = scratch.cache.getLightColor(state, level, (BlockPos)mutableBlockPos);
/* 147 */               renderModelFaceFlat(level, state, pos, lightColor, overlayCoords, false, poseStack, builder, culledQuads, scratch);
/*     */             } 
/*     */           } 
/*     */         } 
/* 151 */       }  List<BakedQuad> unculledQuads = part.getQuads(null);
/* 152 */       if (!unculledQuads.isEmpty()) {
/* 153 */         renderModelFaceFlat(level, state, pos, -1, overlayCoords, true, poseStack, builder, unculledQuads, scratch);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderModelFaceAO(BlockAndTintGetter level, BlockState state, BlockPos pos, PoseStack poseStack, VertexConsumer builder, List<BakedQuad> quads, AmbientOcclusionRenderStorage storage, int overlayCoords) {
/* 159 */     for (BakedQuad quad : quads) {
/* 160 */       calculateShape(level, state, pos, quad, storage);
/* 161 */       storage.calculate(level, state, pos, quad.direction(), quad.shade());
/*     */       
/* 163 */       putQuadData(level, state, pos, builder, poseStack.last(), quad, storage, overlayCoords);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void putQuadData(BlockAndTintGetter level, BlockState state, BlockPos pos, VertexConsumer builder, PoseStack.Pose pose, BakedQuad quad, CommonRenderStorage renderStorage, int overlayCoords) {
/*     */     float r, g, b;
/* 172 */     int tintIndex = quad.tintIndex();
/* 173 */     if (tintIndex != -1) {
/*     */       int tintColor;
/* 175 */       if (renderStorage.tintCacheIndex == tintIndex) {
/* 176 */         tintColor = renderStorage.tintCacheValue;
/*     */       } else {
/* 178 */         tintColor = this.blockColors.getColor(state, level, pos, tintIndex);
/* 179 */         renderStorage.tintCacheIndex = tintIndex;
/* 180 */         renderStorage.tintCacheValue = tintColor;
/*     */       } 
/* 182 */       r = ARGB.redFloat(tintColor);
/* 183 */       g = ARGB.greenFloat(tintColor);
/* 184 */       b = ARGB.blueFloat(tintColor);
/*     */     } else {
/* 186 */       r = 1.0F;
/* 187 */       g = 1.0F;
/* 188 */       b = 1.0F;
/*     */     } 
/*     */     
/* 191 */     builder.putBulkData(pose, quad, renderStorage.brightness, r, g, b, 1.0F, renderStorage.lightmap, overlayCoords);
/*     */   }
/*     */   
/*     */   private static void calculateShape(BlockAndTintGetter level, BlockState state, BlockPos pos, BakedQuad quad, CommonRenderStorage storage) {
/* 195 */     float minX = 32.0F;
/* 196 */     float minY = 32.0F;
/* 197 */     float minZ = 32.0F;
/* 198 */     float maxX = -32.0F;
/* 199 */     float maxY = -32.0F;
/* 200 */     float maxZ = -32.0F;
/* 201 */     for (int i = 0; i < 4; i++) {
/* 202 */       Vector3fc position = quad.position(i);
/* 203 */       float x = position.x();
/* 204 */       float y = position.y();
/* 205 */       float z = position.z();
/* 206 */       minX = Math.min(minX, x);
/* 207 */       minY = Math.min(minY, y);
/* 208 */       minZ = Math.min(minZ, z);
/* 209 */       maxX = Math.max(maxX, x);
/* 210 */       maxY = Math.max(maxY, y);
/* 211 */       maxZ = Math.max(maxZ, z);
/*     */     } 
/*     */     
/* 214 */     if (storage instanceof AmbientOcclusionRenderStorage) { AmbientOcclusionRenderStorage aoStorage = (AmbientOcclusionRenderStorage)storage;
/* 215 */       aoStorage.faceShape[SizeInfo.WEST.index] = minX;
/* 216 */       aoStorage.faceShape[SizeInfo.EAST.index] = maxX;
/* 217 */       aoStorage.faceShape[SizeInfo.DOWN.index] = minY;
/* 218 */       aoStorage.faceShape[SizeInfo.UP.index] = maxY;
/* 219 */       aoStorage.faceShape[SizeInfo.NORTH.index] = minZ;
/* 220 */       aoStorage.faceShape[SizeInfo.SOUTH.index] = maxZ;
/* 221 */       aoStorage.faceShape[SizeInfo.FLIP_WEST.index] = 1.0F - minX;
/* 222 */       aoStorage.faceShape[SizeInfo.FLIP_EAST.index] = 1.0F - maxX;
/* 223 */       aoStorage.faceShape[SizeInfo.FLIP_DOWN.index] = 1.0F - minY;
/* 224 */       aoStorage.faceShape[SizeInfo.FLIP_UP.index] = 1.0F - maxY;
/* 225 */       aoStorage.faceShape[SizeInfo.FLIP_NORTH.index] = 1.0F - minZ;
/* 226 */       aoStorage.faceShape[SizeInfo.FLIP_SOUTH.index] = 1.0F - maxZ; }
/*     */ 
/*     */     
/* 229 */     float minEpsilon = 1.0E-4F;
/* 230 */     float maxEpsilon = 0.9999F;
/* 231 */     switch (quad.direction()) { default: throw new MatchException(null, null);
/* 232 */       case DOWN: case UP: if (minX >= 1.0E-4F || minZ >= 1.0E-4F || maxX <= 0.9999F || maxZ <= 0.9999F);
/* 233 */       case NORTH: case SOUTH: if (minX >= 1.0E-4F || minY >= 1.0E-4F || maxX <= 0.9999F || maxY <= 0.9999F);
/* 234 */       case WEST: case EAST: if (minY >= 1.0E-4F || minZ >= 1.0E-4F || maxY <= 0.9999F || maxZ <= 0.9999F); break; }  storage.facePartial = false;
/*     */ 
/*     */     
/* 237 */     switch (quad.direction()) { default: throw new MatchException(null, null);
/* 238 */       case DOWN: if (minY == maxY && (minY < 1.0E-4F || state.isCollisionShapeFullBlock((BlockGetter)level, pos)));
/* 239 */       case UP: if (minY == maxY && (maxY > 0.9999F || state.isCollisionShapeFullBlock((BlockGetter)level, pos)));
/* 240 */       case NORTH: if (minZ == maxZ && (minZ < 1.0E-4F || state.isCollisionShapeFullBlock((BlockGetter)level, pos)));
/* 241 */       case SOUTH: if (minZ == maxZ && (maxZ > 0.9999F || state.isCollisionShapeFullBlock((BlockGetter)level, pos)));
/* 242 */       case WEST: if (minX == maxX && (minX < 1.0E-4F || state.isCollisionShapeFullBlock((BlockGetter)level, pos)));
/* 243 */       case EAST: if (minX == maxX && (maxX > 0.9999F || state.isCollisionShapeFullBlock((BlockGetter)level, pos))); break; }  storage.faceCubic = false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderModelFaceFlat(BlockAndTintGetter level, BlockState state, BlockPos pos, int lightColor, int overlayCoords, boolean checkLight, PoseStack poseStack, VertexConsumer builder, List<BakedQuad> quads, CommonRenderStorage shapeState) {
/* 248 */     for (BakedQuad quad : quads) {
/* 249 */       if (checkLight) {
/* 250 */         calculateShape(level, state, pos, quad, shapeState);
/*     */         
/* 252 */         BlockPos lightPos = shapeState.faceCubic ? (BlockPos)shapeState.scratchPos.setWithOffset((Vec3i)pos, quad.direction()) : pos;
/* 253 */         lightColor = shapeState.cache.getLightColor(state, level, lightPos);
/*     */       } 
/*     */       
/* 256 */       float directionalBrightness = level.getShade(quad.direction(), quad.shade());
/* 257 */       shapeState.brightness[0] = directionalBrightness;
/* 258 */       shapeState.brightness[1] = directionalBrightness;
/* 259 */       shapeState.brightness[2] = directionalBrightness;
/* 260 */       shapeState.brightness[3] = directionalBrightness;
/*     */       
/* 262 */       shapeState.lightmap[0] = lightColor;
/* 263 */       shapeState.lightmap[1] = lightColor;
/* 264 */       shapeState.lightmap[2] = lightColor;
/* 265 */       shapeState.lightmap[3] = lightColor;
/*     */       
/* 267 */       putQuadData(level, state, pos, builder, poseStack.last(), quad, shapeState, overlayCoords);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void renderModel(PoseStack.Pose pose, VertexConsumer builder, BlockStateModel model, float r, float g, float b, int lightCoords, int overlayCoords) {
/* 272 */     for (BlockModelPart part : (Iterable<BlockModelPart>)model.collectParts(RandomSource.create(42L))) {
/* 273 */       for (Direction direction : DIRECTIONS) {
/* 274 */         renderQuadList(pose, builder, r, g, b, part.getQuads(direction), lightCoords, overlayCoords);
/*     */       }
/* 276 */       renderQuadList(pose, builder, r, g, b, part.getQuads(null), lightCoords, overlayCoords);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void renderQuadList(PoseStack.Pose pose, VertexConsumer builder, float r, float g, float b, List<BakedQuad> quads, int lightCoords, int overlayCoords) {
/* 281 */     for (BakedQuad quad : quads) {
/*     */       float red, green, blue;
/*     */ 
/*     */       
/* 285 */       if (quad.isTinted()) {
/* 286 */         red = Mth.clamp(r, 0.0F, 1.0F);
/* 287 */         green = Mth.clamp(g, 0.0F, 1.0F);
/* 288 */         blue = Mth.clamp(b, 0.0F, 1.0F);
/*     */       } else {
/* 290 */         red = 1.0F;
/* 291 */         green = 1.0F;
/* 292 */         blue = 1.0F;
/*     */       } 
/* 294 */       builder.putBulkData(pose, quad, red, green, blue, 1.0F, lightCoords, overlayCoords);
/*     */     } 
/*     */   }
/*     */   
/*     */   private enum AmbientVertexRemap {
/* 299 */     DOWN(0, 1, 2, 3),
/* 300 */     UP(2, 3, 0, 1),
/* 301 */     NORTH(3, 0, 1, 2),
/* 302 */     SOUTH(0, 1, 2, 3),
/* 303 */     WEST(3, 0, 1, 2),
/* 304 */     EAST(1, 2, 3, 0); private final int vert0;
/*     */     private final int vert1;
/*     */     private final int vert2;
/*     */     private final int vert3;
/*     */     private static final AmbientVertexRemap[] BY_FACING;
/*     */     
/*     */     static {
/* 311 */       BY_FACING = (AmbientVertexRemap[])Util.make(new AmbientVertexRemap[6], map -> {
/*     */             map[Direction.DOWN.get3DDataValue()] = DOWN;
/*     */             map[Direction.UP.get3DDataValue()] = UP;
/*     */             map[Direction.NORTH.get3DDataValue()] = NORTH;
/*     */             map[Direction.SOUTH.get3DDataValue()] = SOUTH;
/*     */             map[Direction.WEST.get3DDataValue()] = WEST;
/*     */             map[Direction.EAST.get3DDataValue()] = EAST;
/*     */           });
/*     */     }
/*     */     AmbientVertexRemap(int vert0, int vert1, int vert2, int vert3) {
/* 321 */       this.vert0 = vert0;
/* 322 */       this.vert1 = vert1;
/* 323 */       this.vert2 = vert2;
/* 324 */       this.vert3 = vert3;
/*     */     }
/*     */     
/*     */     public static AmbientVertexRemap fromFacing(Direction direction) {
/* 328 */       return BY_FACING[direction.get3DDataValue()];
/*     */     } }
/*     */   
/*     */   private static class Cache { private boolean enabled;
/*     */     private final Long2IntLinkedOpenHashMap colorCache;
/*     */     private final Long2FloatLinkedOpenHashMap brightnessCache;
/*     */     private final LevelRenderer.BrightnessGetter cachedBrightnessGetter;
/*     */     
/*     */     private Cache() {
/* 337 */       this.colorCache = (Long2IntLinkedOpenHashMap)Util.make(() -> {
/*     */             Long2IntLinkedOpenHashMap map = new Long2IntLinkedOpenHashMap(100, 0.25F)
/*     */               {
/*     */                 protected void rehash(int newN) {}
/*     */               };
/*     */             
/*     */             map.defaultReturnValue(Integer.MAX_VALUE);
/*     */             
/*     */             return map;
/*     */           });
/* 347 */       this.brightnessCache = (Long2FloatLinkedOpenHashMap)Util.make(() -> {
/*     */             Long2FloatLinkedOpenHashMap map = new Long2FloatLinkedOpenHashMap(100, 0.25F)
/*     */               {
/*     */                 protected void rehash(int newN) {}
/*     */               };
/*     */             
/*     */             map.defaultReturnValue(Float.NaN);
/*     */             
/*     */             return map;
/*     */           });
/* 357 */       this.cachedBrightnessGetter = ((level, pos) -> {
/*     */           long key = pos.asLong();
/*     */           int cached = this.colorCache.get(key);
/*     */           if (cached != Integer.MAX_VALUE) {
/*     */             return cached;
/*     */           }
/*     */           int value = LevelRenderer.BrightnessGetter.DEFAULT.packedBrightness(level, pos);
/*     */           if (this.colorCache.size() == 100) {
/*     */             this.colorCache.removeFirstInt();
/*     */           }
/*     */           this.colorCache.put(key, value);
/*     */           return value;
/*     */         });
/*     */     }
/*     */ 
/*     */     
/*     */     public void enable() {
/* 374 */       this.enabled = true;
/*     */     }
/*     */     
/*     */     public void disable() {
/* 378 */       this.enabled = false;
/* 379 */       this.colorCache.clear();
/* 380 */       this.brightnessCache.clear();
/*     */     }
/*     */     
/*     */     public int getLightColor(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/* 384 */       return LevelRenderer.getLightColor(this.enabled ? this.cachedBrightnessGetter : LevelRenderer.BrightnessGetter.DEFAULT, level, state, pos);
/*     */     }
/*     */     
/*     */     public float getShadeBrightness(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/* 388 */       long key = pos.asLong();
/* 389 */       if (this.enabled) {
/* 390 */         float cached = this.brightnessCache.get(key);
/* 391 */         if (!Float.isNaN(cached)) {
/* 392 */           return cached;
/*     */         }
/*     */       } 
/*     */       
/* 396 */       float brightness = state.getShadeBrightness((BlockGetter)level, pos);
/* 397 */       if (this.enabled) {
/* 398 */         if (this.brightnessCache.size() == 100) {
/* 399 */           this.brightnessCache.removeFirstFloat();
/*     */         }
/* 401 */         this.brightnessCache.put(key, brightness);
/*     */       } 
/* 403 */       return brightness;
/*     */     } }
/*     */ 
/*     */   
/* 407 */   private static final ThreadLocal<Cache> CACHE = ThreadLocal.withInitial(Cache::new);
/*     */   class null extends Long2IntLinkedOpenHashMap {
/*     */     null(int expected, float f) { super(expected, f); }
/* 410 */     protected void rehash(int newN) {} } public static void enableCaching() { ((Cache)CACHE.get()).enable(); } class null extends Long2FloatLinkedOpenHashMap {
/*     */     null(int expected, float f) {
/*     */       super(expected, f);
/*     */     } protected void rehash(int newN) {} } public static void clearCache() {
/* 414 */     ((Cache)CACHE.get()).disable();
/*     */   }
/*     */   
/*     */   private static class CommonRenderStorage {
/* 418 */     public final BlockPos.MutableBlockPos scratchPos = new BlockPos.MutableBlockPos();
/*     */     
/*     */     public boolean faceCubic;
/*     */     public boolean facePartial;
/* 422 */     public final float[] brightness = new float[4];
/* 423 */     public final int[] lightmap = new int[4];
/*     */ 
/*     */     
/* 426 */     public int tintCacheIndex = -1;
/*     */     
/*     */     public int tintCacheValue;
/*     */     
/* 430 */     public final ModelBlockRenderer.Cache cache = ModelBlockRenderer.CACHE.get();
/*     */   }
/*     */   
/*     */   private static class AmbientOcclusionRenderStorage
/*     */     extends CommonRenderStorage {
/* 435 */     private final float[] faceShape = new float[ModelBlockRenderer.SizeInfo.COUNT];
/*     */ 
/*     */     
/*     */     public void calculate(BlockAndTintGetter level, BlockState state, BlockPos centerPosition, Direction direction, boolean shade) {
/*     */       float shadeCorner02, shadeCorner03, shadeCorner12, shadeCorner13;
/*     */       int lightCorner02, lightCorner03, lightCorner12, lightCorner13;
/* 441 */       BlockPos basePosition = this.faceCubic ? centerPosition.relative(direction) : centerPosition;
/* 442 */       ModelBlockRenderer.AdjacencyInfo info = ModelBlockRenderer.AdjacencyInfo.fromFacing(direction);
/*     */       
/* 444 */       BlockPos.MutableBlockPos pos = this.scratchPos;
/*     */       
/* 446 */       pos.setWithOffset((Vec3i)basePosition, info.corners[0]);
/* 447 */       BlockState state0 = level.getBlockState((BlockPos)pos);
/* 448 */       int light0 = this.cache.getLightColor(state0, level, (BlockPos)pos);
/* 449 */       float shade0 = this.cache.getShadeBrightness(state0, level, (BlockPos)pos);
/*     */       
/* 451 */       pos.setWithOffset((Vec3i)basePosition, info.corners[1]);
/* 452 */       BlockState state1 = level.getBlockState((BlockPos)pos);
/* 453 */       int light1 = this.cache.getLightColor(state1, level, (BlockPos)pos);
/* 454 */       float shade1 = this.cache.getShadeBrightness(state1, level, (BlockPos)pos);
/*     */       
/* 456 */       pos.setWithOffset((Vec3i)basePosition, info.corners[2]);
/* 457 */       BlockState state2 = level.getBlockState((BlockPos)pos);
/* 458 */       int light2 = this.cache.getLightColor(state2, level, (BlockPos)pos);
/* 459 */       float shade2 = this.cache.getShadeBrightness(state2, level, (BlockPos)pos);
/*     */       
/* 461 */       pos.setWithOffset((Vec3i)basePosition, info.corners[3]);
/* 462 */       BlockState state3 = level.getBlockState((BlockPos)pos);
/* 463 */       int light3 = this.cache.getLightColor(state3, level, (BlockPos)pos);
/* 464 */       float shade3 = this.cache.getShadeBrightness(state3, level, (BlockPos)pos);
/*     */       
/* 466 */       BlockState corner0 = level.getBlockState((BlockPos)pos.setWithOffset((Vec3i)basePosition, info.corners[0]).move(direction));
/* 467 */       boolean translucent0 = (!corner0.isViewBlocking((BlockGetter)level, (BlockPos)pos) || corner0.getLightBlock() == 0);
/* 468 */       BlockState corner1 = level.getBlockState((BlockPos)pos.setWithOffset((Vec3i)basePosition, info.corners[1]).move(direction));
/* 469 */       boolean translucent1 = (!corner1.isViewBlocking((BlockGetter)level, (BlockPos)pos) || corner1.getLightBlock() == 0);
/* 470 */       BlockState corner2 = level.getBlockState((BlockPos)pos.setWithOffset((Vec3i)basePosition, info.corners[2]).move(direction));
/* 471 */       boolean translucent2 = (!corner2.isViewBlocking((BlockGetter)level, (BlockPos)pos) || corner2.getLightBlock() == 0);
/* 472 */       BlockState corner3 = level.getBlockState((BlockPos)pos.setWithOffset((Vec3i)basePosition, info.corners[3]).move(direction));
/* 473 */       boolean translucent3 = (!corner3.isViewBlocking((BlockGetter)level, (BlockPos)pos) || corner3.getLightBlock() == 0);
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
/* 484 */       if (translucent2 || translucent0) {
/* 485 */         pos.setWithOffset((Vec3i)basePosition, info.corners[0]).move(info.corners[2]);
/* 486 */         BlockState state02 = level.getBlockState((BlockPos)pos);
/* 487 */         shadeCorner02 = this.cache.getShadeBrightness(state02, level, (BlockPos)pos);
/* 488 */         lightCorner02 = this.cache.getLightColor(state02, level, (BlockPos)pos);
/*     */       } else {
/* 490 */         shadeCorner02 = shade0;
/* 491 */         lightCorner02 = light0;
/*     */       } 
/* 493 */       if (translucent3 || translucent0) {
/* 494 */         pos.setWithOffset((Vec3i)basePosition, info.corners[0]).move(info.corners[3]);
/* 495 */         BlockState state03 = level.getBlockState((BlockPos)pos);
/* 496 */         shadeCorner03 = this.cache.getShadeBrightness(state03, level, (BlockPos)pos);
/* 497 */         lightCorner03 = this.cache.getLightColor(state03, level, (BlockPos)pos);
/*     */       } else {
/* 499 */         shadeCorner03 = shade0;
/* 500 */         lightCorner03 = light0;
/*     */       } 
/* 502 */       if (translucent2 || translucent1) {
/* 503 */         pos.setWithOffset((Vec3i)basePosition, info.corners[1]).move(info.corners[2]);
/* 504 */         BlockState state12 = level.getBlockState((BlockPos)pos);
/* 505 */         shadeCorner12 = this.cache.getShadeBrightness(state12, level, (BlockPos)pos);
/* 506 */         lightCorner12 = this.cache.getLightColor(state12, level, (BlockPos)pos);
/*     */       } else {
/* 508 */         shadeCorner12 = shade0;
/* 509 */         lightCorner12 = light0;
/*     */       } 
/* 511 */       if (translucent3 || translucent1) {
/* 512 */         pos.setWithOffset((Vec3i)basePosition, info.corners[1]).move(info.corners[3]);
/* 513 */         BlockState state13 = level.getBlockState((BlockPos)pos);
/* 514 */         shadeCorner13 = this.cache.getShadeBrightness(state13, level, (BlockPos)pos);
/* 515 */         lightCorner13 = this.cache.getLightColor(state13, level, (BlockPos)pos);
/*     */       } else {
/* 517 */         shadeCorner13 = shade0;
/* 518 */         lightCorner13 = light0;
/*     */       } 
/*     */       
/* 521 */       int lightCenter = this.cache.getLightColor(state, level, centerPosition);
/* 522 */       pos.setWithOffset((Vec3i)centerPosition, direction);
/* 523 */       BlockState nextState = level.getBlockState((BlockPos)pos);
/* 524 */       if (this.faceCubic || !nextState.isSolidRender()) {
/* 525 */         lightCenter = this.cache.getLightColor(nextState, level, (BlockPos)pos);
/*     */       }
/*     */       
/* 528 */       float shadeCenter = this.faceCubic ? 
/* 529 */         this.cache.getShadeBrightness(level.getBlockState(basePosition), level, basePosition) : 
/* 530 */         this.cache.getShadeBrightness(level.getBlockState(centerPosition), level, centerPosition);
/*     */       
/* 532 */       ModelBlockRenderer.AmbientVertexRemap remap = ModelBlockRenderer.AmbientVertexRemap.fromFacing(direction);
/*     */       
/* 534 */       if (!this.facePartial || !info.doNonCubicWeight) {
/* 535 */         float lightLevel1 = (shade3 + shade0 + shadeCorner03 + shadeCenter) * 0.25F;
/* 536 */         float lightLevel2 = (shade2 + shade0 + shadeCorner02 + shadeCenter) * 0.25F;
/* 537 */         float lightLevel3 = (shade2 + shade1 + shadeCorner12 + shadeCenter) * 0.25F;
/* 538 */         float lightLevel4 = (shade3 + shade1 + shadeCorner13 + shadeCenter) * 0.25F;
/*     */         
/* 540 */         this.lightmap[remap.vert0] = blend(light3, light0, lightCorner03, lightCenter);
/* 541 */         this.lightmap[remap.vert1] = blend(light2, light0, lightCorner02, lightCenter);
/* 542 */         this.lightmap[remap.vert2] = blend(light2, light1, lightCorner12, lightCenter);
/* 543 */         this.lightmap[remap.vert3] = blend(light3, light1, lightCorner13, lightCenter);
/*     */         
/* 545 */         this.brightness[remap.vert0] = lightLevel1;
/* 546 */         this.brightness[remap.vert1] = lightLevel2;
/* 547 */         this.brightness[remap.vert2] = lightLevel3;
/* 548 */         this.brightness[remap.vert3] = lightLevel4;
/*     */       } else {
/* 550 */         float tempShade1 = (shade3 + shade0 + shadeCorner03 + shadeCenter) * 0.25F;
/* 551 */         float tempShade2 = (shade2 + shade0 + shadeCorner02 + shadeCenter) * 0.25F;
/* 552 */         float tempShade3 = (shade2 + shade1 + shadeCorner12 + shadeCenter) * 0.25F;
/* 553 */         float tempShade4 = (shade3 + shade1 + shadeCorner13 + shadeCenter) * 0.25F;
/*     */         
/* 555 */         float vert0weight01 = this.faceShape[(info.vert0Weights[0]).index] * this.faceShape[(info.vert0Weights[1]).index];
/* 556 */         float vert0weight23 = this.faceShape[(info.vert0Weights[2]).index] * this.faceShape[(info.vert0Weights[3]).index];
/* 557 */         float vert0weight45 = this.faceShape[(info.vert0Weights[4]).index] * this.faceShape[(info.vert0Weights[5]).index];
/* 558 */         float vert0weight67 = this.faceShape[(info.vert0Weights[6]).index] * this.faceShape[(info.vert0Weights[7]).index];
/*     */         
/* 560 */         float vert1weight01 = this.faceShape[(info.vert1Weights[0]).index] * this.faceShape[(info.vert1Weights[1]).index];
/* 561 */         float vert1weight23 = this.faceShape[(info.vert1Weights[2]).index] * this.faceShape[(info.vert1Weights[3]).index];
/* 562 */         float vert1weight45 = this.faceShape[(info.vert1Weights[4]).index] * this.faceShape[(info.vert1Weights[5]).index];
/* 563 */         float vert1weight67 = this.faceShape[(info.vert1Weights[6]).index] * this.faceShape[(info.vert1Weights[7]).index];
/*     */         
/* 565 */         float vert2weight01 = this.faceShape[(info.vert2Weights[0]).index] * this.faceShape[(info.vert2Weights[1]).index];
/* 566 */         float vert2weight23 = this.faceShape[(info.vert2Weights[2]).index] * this.faceShape[(info.vert2Weights[3]).index];
/* 567 */         float vert2weight45 = this.faceShape[(info.vert2Weights[4]).index] * this.faceShape[(info.vert2Weights[5]).index];
/* 568 */         float vert2weight67 = this.faceShape[(info.vert2Weights[6]).index] * this.faceShape[(info.vert2Weights[7]).index];
/*     */         
/* 570 */         float vert3weight01 = this.faceShape[(info.vert3Weights[0]).index] * this.faceShape[(info.vert3Weights[1]).index];
/* 571 */         float vert3weight23 = this.faceShape[(info.vert3Weights[2]).index] * this.faceShape[(info.vert3Weights[3]).index];
/* 572 */         float vert3weight45 = this.faceShape[(info.vert3Weights[4]).index] * this.faceShape[(info.vert3Weights[5]).index];
/* 573 */         float vert3weight67 = this.faceShape[(info.vert3Weights[6]).index] * this.faceShape[(info.vert3Weights[7]).index];
/*     */         
/* 575 */         this.brightness[remap.vert0] = Math.clamp(tempShade1 * vert0weight01 + tempShade2 * vert0weight23 + tempShade3 * vert0weight45 + tempShade4 * vert0weight67, 0.0F, 1.0F);
/* 576 */         this.brightness[remap.vert1] = Math.clamp(tempShade1 * vert1weight01 + tempShade2 * vert1weight23 + tempShade3 * vert1weight45 + tempShade4 * vert1weight67, 0.0F, 1.0F);
/* 577 */         this.brightness[remap.vert2] = Math.clamp(tempShade1 * vert2weight01 + tempShade2 * vert2weight23 + tempShade3 * vert2weight45 + tempShade4 * vert2weight67, 0.0F, 1.0F);
/* 578 */         this.brightness[remap.vert3] = Math.clamp(tempShade1 * vert3weight01 + tempShade2 * vert3weight23 + tempShade3 * vert3weight45 + tempShade4 * vert3weight67, 0.0F, 1.0F);
/*     */         
/* 580 */         int _tc1 = blend(light3, light0, lightCorner03, lightCenter);
/* 581 */         int _tc2 = blend(light2, light0, lightCorner02, lightCenter);
/* 582 */         int _tc3 = blend(light2, light1, lightCorner12, lightCenter);
/* 583 */         int _tc4 = blend(light3, light1, lightCorner13, lightCenter);
/*     */         
/* 585 */         this.lightmap[remap.vert0] = blend(_tc1, _tc2, _tc3, _tc4, vert0weight01, vert0weight23, vert0weight45, vert0weight67);
/* 586 */         this.lightmap[remap.vert1] = blend(_tc1, _tc2, _tc3, _tc4, vert1weight01, vert1weight23, vert1weight45, vert1weight67);
/* 587 */         this.lightmap[remap.vert2] = blend(_tc1, _tc2, _tc3, _tc4, vert2weight01, vert2weight23, vert2weight45, vert2weight67);
/* 588 */         this.lightmap[remap.vert3] = blend(_tc1, _tc2, _tc3, _tc4, vert3weight01, vert3weight23, vert3weight45, vert3weight67);
/*     */       } 
/*     */       
/* 591 */       float directionalBrightness = level.getShade(direction, shade);
/* 592 */       for (int i = 0; i < this.brightness.length; i++) {
/* 593 */         this.brightness[i] = this.brightness[i] * directionalBrightness;
/*     */       }
/*     */     }
/*     */     
/*     */     private static int blend(int a, int b, int c, int def) {
/* 598 */       if (a == 0) {
/* 599 */         a = def;
/*     */       }
/* 601 */       if (b == 0) {
/* 602 */         b = def;
/*     */       }
/* 604 */       if (c == 0) {
/* 605 */         c = def;
/*     */       }
/* 607 */       return a + b + c + def >> 2 & 0xFF00FF;
/*     */     }
/*     */     
/*     */     private static int blend(int a, int b, int c, int d, float fa, float fb, float fc, float fd) {
/* 611 */       int top = (int)((a >> 16 & 0xFF) * fa + (b >> 16 & 0xFF) * fb + (c >> 16 & 0xFF) * fc + (d >> 16 & 0xFF) * fd) & 0xFF;
/* 612 */       int bottom = (int)((a & 0xFF) * fa + (b & 0xFF) * fb + (c & 0xFF) * fc + (d & 0xFF) * fd) & 0xFF;
/* 613 */       return top << 16 | bottom;
/*     */     }
/*     */   }
/*     */   
/*     */   protected enum SizeInfo {
/* 618 */     DOWN(0),
/* 619 */     UP(1),
/* 620 */     NORTH(2),
/* 621 */     SOUTH(3),
/* 622 */     WEST(4),
/* 623 */     EAST(5),
/* 624 */     FLIP_DOWN(6),
/* 625 */     FLIP_UP(7),
/* 626 */     FLIP_NORTH(8),
/* 627 */     FLIP_SOUTH(9),
/* 628 */     FLIP_WEST(10),
/* 629 */     FLIP_EAST(11);
/*     */     
/* 631 */     public static final int COUNT = (values()).length;
/*     */     
/*     */     private final int index;
/*     */     
/*     */     SizeInfo(int index) {
/* 636 */       this.index = index;
/*     */     }
/*     */   }
/*     */   
/*     */   protected enum AdjacencyInfo {
/* 641 */     DOWN(new Direction[] { Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH }, 0.5F, true, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.SOUTH, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.SOUTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.NORTH, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.NORTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.NORTH, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.NORTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.SOUTH, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.SOUTH
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }),
/* 647 */     UP(new Direction[] { Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH }, 1.0F, true, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.SOUTH, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.SOUTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.NORTH, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.NORTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.NORTH, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.NORTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.SOUTH, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.SOUTH
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }),
/* 653 */     NORTH(new Direction[] { Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST }, 0.8F, true, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.FLIP_WEST }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.FLIP_EAST }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.FLIP_EAST }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.FLIP_WEST
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }),
/* 659 */     SOUTH(new Direction[] { Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP }, 0.8F, true, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.WEST }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.WEST }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.EAST }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.EAST
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }),
/* 665 */     WEST(new Direction[] { Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH }, 0.6F, true, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.SOUTH, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.SOUTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.NORTH, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.NORTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.NORTH, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.NORTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.SOUTH, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.SOUTH
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }),
/* 671 */     EAST(new Direction[] { Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH }, 0.6F, true, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.SOUTH, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.SOUTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.NORTH, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.NORTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.NORTH, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.NORTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.SOUTH, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.SOUTH });
/*     */     
/*     */     private final Direction[] corners;
/*     */     
/*     */     private final boolean doNonCubicWeight;
/*     */     
/*     */     private final ModelBlockRenderer.SizeInfo[] vert0Weights;
/*     */     
/*     */     private final ModelBlockRenderer.SizeInfo[] vert1Weights;
/*     */     private final ModelBlockRenderer.SizeInfo[] vert2Weights;
/*     */     private final ModelBlockRenderer.SizeInfo[] vert3Weights;
/*     */     private static final AdjacencyInfo[] BY_FACING;
/*     */     
/*     */     static {
/* 685 */       BY_FACING = (AdjacencyInfo[])Util.make(new AdjacencyInfo[6], map -> {
/*     */             map[Direction.DOWN.get3DDataValue()] = DOWN;
/*     */             map[Direction.UP.get3DDataValue()] = UP;
/*     */             map[Direction.NORTH.get3DDataValue()] = NORTH;
/*     */             map[Direction.SOUTH.get3DDataValue()] = SOUTH;
/*     */             map[Direction.WEST.get3DDataValue()] = WEST;
/*     */             map[Direction.EAST.get3DDataValue()] = EAST;
/*     */           });
/*     */     }
/*     */     AdjacencyInfo(Direction[] corners, float shadeWeight, boolean doNonCubicWeight, ModelBlockRenderer.SizeInfo[] vert0Weights, ModelBlockRenderer.SizeInfo[] vert1Weights, ModelBlockRenderer.SizeInfo[] vert2Weights, ModelBlockRenderer.SizeInfo[] vert3Weights) {
/* 695 */       this.corners = corners;
/* 696 */       this.doNonCubicWeight = doNonCubicWeight;
/* 697 */       this.vert0Weights = vert0Weights;
/* 698 */       this.vert1Weights = vert1Weights;
/* 699 */       this.vert2Weights = vert2Weights;
/* 700 */       this.vert3Weights = vert3Weights;
/*     */     }
/*     */     
/*     */     public static AdjacencyInfo fromFacing(Direction direction) {
/* 704 */       return BY_FACING[direction.get3DDataValue()];
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/ModelBlockRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */