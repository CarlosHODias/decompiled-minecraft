/*     */ package net.minecraft.client.renderer.block;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import net.minecraft.client.renderer.BiomeColors;
/*     */ import net.minecraft.client.renderer.LevelRenderer;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.model.MaterialSet;
/*     */ import net.minecraft.client.resources.model.ModelBakery;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.BlockAndTintGetter;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ public class LiquidBlockRenderer
/*     */ {
/*     */   private static final float MAX_FLUID_HEIGHT = 0.8888889F;
/*     */   private final TextureAtlasSprite lavaStill;
/*     */   private final TextureAtlasSprite lavaFlowing;
/*     */   private final TextureAtlasSprite waterStill;
/*     */   private final TextureAtlasSprite waterFlowing;
/*     */   private final TextureAtlasSprite waterOverlay;
/*     */   
/*     */   public LiquidBlockRenderer(MaterialSet materials) {
/*  34 */     this.lavaStill = materials.get(ModelBakery.LAVA_STILL);
/*  35 */     this.lavaFlowing = materials.get(ModelBakery.LAVA_FLOW);
/*  36 */     this.waterStill = materials.get(ModelBakery.WATER_STILL);
/*  37 */     this.waterFlowing = materials.get(ModelBakery.WATER_FLOW);
/*  38 */     this.waterOverlay = materials.get(ModelBakery.WATER_OVERLAY);
/*     */   }
/*     */   
/*     */   private static boolean isNeighborSameFluid(FluidState fluidState, FluidState neighborFluidState) {
/*  42 */     return neighborFluidState.getType().isSame(fluidState.getType());
/*     */   }
/*     */   
/*     */   private static boolean isFaceOccludedByState(Direction direction, float height, BlockState state) {
/*  46 */     VoxelShape occluder = state.getFaceOcclusionShape(direction.getOpposite());
/*  47 */     if (occluder == Shapes.empty())
/*  48 */       return false; 
/*  49 */     if (occluder == Shapes.block()) {
/*  50 */       boolean fullBlock = (height == 1.0F);
/*  51 */       return (direction != Direction.UP || fullBlock);
/*     */     } 
/*  53 */     VoxelShape shape = Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, height, 1.0D);
/*  54 */     return Shapes.blockOccludes(shape, occluder, direction);
/*     */   }
/*     */   
/*     */   private static boolean isFaceOccludedByNeighbor(Direction direction, float height, BlockState neighborState) {
/*  58 */     return isFaceOccludedByState(direction, height, neighborState);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isFaceOccludedBySelf(BlockState state, Direction direction) {
/*  64 */     return isFaceOccludedByState(direction.getOpposite(), 1.0F, state);
/*     */   }
/*     */   
/*     */   public static boolean shouldRenderFace(FluidState fluidState, BlockState blockState, Direction direction, FluidState neighborFluidState) {
/*  68 */     return (!isFaceOccludedBySelf(blockState, direction) && !isNeighborSameFluid(fluidState, neighborFluidState));
/*     */   }
/*     */   public void tesselate(BlockAndTintGetter level, BlockPos pos, VertexConsumer builder, BlockState blockState, FluidState fluidState) {
/*     */     float heightNorthEast, heightNorthWest, heightSouthEast, heightSouthWest;
/*  72 */     boolean isLava = fluidState.is(FluidTags.LAVA);
/*  73 */     TextureAtlasSprite stillSprite = isLava ? this.lavaStill : this.waterStill;
/*  74 */     TextureAtlasSprite flowingSprite = isLava ? this.lavaFlowing : this.waterFlowing;
/*     */     
/*  76 */     int col = isLava ? 16777215 : BiomeColors.getAverageWaterColor(level, pos);
/*  77 */     float r = (col >> 16 & 0xFF) / 255.0F;
/*  78 */     float g = (col >> 8 & 0xFF) / 255.0F;
/*  79 */     float b = (col & 0xFF) / 255.0F;
/*     */     
/*  81 */     BlockState blockStateDown = level.getBlockState(pos.relative(Direction.DOWN));
/*  82 */     FluidState fluidStateDown = blockStateDown.getFluidState();
/*  83 */     BlockState blockStateUp = level.getBlockState(pos.relative(Direction.UP));
/*  84 */     FluidState fluidStateUp = blockStateUp.getFluidState();
/*  85 */     BlockState blockStateNorth = level.getBlockState(pos.relative(Direction.NORTH));
/*  86 */     FluidState fluidStateNorth = blockStateNorth.getFluidState();
/*  87 */     BlockState blockStateSouth = level.getBlockState(pos.relative(Direction.SOUTH));
/*  88 */     FluidState fluidStateSouth = blockStateSouth.getFluidState();
/*  89 */     BlockState blockStateWest = level.getBlockState(pos.relative(Direction.WEST));
/*  90 */     FluidState fluidStateWest = blockStateWest.getFluidState();
/*  91 */     BlockState blockStateEast = level.getBlockState(pos.relative(Direction.EAST));
/*  92 */     FluidState fluidStateEast = blockStateEast.getFluidState();
/*     */     
/*  94 */     boolean renderUp = !isNeighborSameFluid(fluidState, fluidStateUp);
/*  95 */     boolean renderDown = (shouldRenderFace(fluidState, blockState, Direction.DOWN, fluidStateDown) && !isFaceOccludedByNeighbor(Direction.DOWN, 0.8888889F, blockStateDown));
/*     */     
/*  97 */     boolean renderNorth = shouldRenderFace(fluidState, blockState, Direction.NORTH, fluidStateNorth);
/*  98 */     boolean renderSouth = shouldRenderFace(fluidState, blockState, Direction.SOUTH, fluidStateSouth);
/*  99 */     boolean renderWest = shouldRenderFace(fluidState, blockState, Direction.WEST, fluidStateWest);
/* 100 */     boolean renderEast = shouldRenderFace(fluidState, blockState, Direction.EAST, fluidStateEast);
/*     */     
/* 102 */     if (!renderUp && !renderDown && !renderEast && !renderWest && !renderNorth && !renderSouth) {
/*     */       return;
/*     */     }
/*     */     
/* 106 */     float c10 = level.getShade(Direction.DOWN, true);
/* 107 */     float c11 = level.getShade(Direction.UP, true);
/* 108 */     float c2 = level.getShade(Direction.NORTH, true);
/* 109 */     float c3 = level.getShade(Direction.WEST, true);
/*     */     
/* 111 */     Fluid type = fluidState.getType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     float heightSelf = getHeight(level, type, pos, blockState, fluidState);
/* 119 */     if (heightSelf >= 1.0F) {
/* 120 */       heightNorthEast = 1.0F;
/* 121 */       heightNorthWest = 1.0F;
/* 122 */       heightSouthEast = 1.0F;
/* 123 */       heightSouthWest = 1.0F;
/*     */     } else {
/* 125 */       float heightNorth = getHeight(level, type, pos.north(), blockStateNorth, fluidStateNorth);
/* 126 */       float heightSouth = getHeight(level, type, pos.south(), blockStateSouth, fluidStateSouth);
/* 127 */       float heightEast = getHeight(level, type, pos.east(), blockStateEast, fluidStateEast);
/* 128 */       float heightWest = getHeight(level, type, pos.west(), blockStateWest, fluidStateWest);
/*     */       
/* 130 */       heightNorthEast = calculateAverageHeight(level, type, heightSelf, heightNorth, heightEast, pos.relative(Direction.NORTH).relative(Direction.EAST));
/* 131 */       heightNorthWest = calculateAverageHeight(level, type, heightSelf, heightNorth, heightWest, pos.relative(Direction.NORTH).relative(Direction.WEST));
/* 132 */       heightSouthEast = calculateAverageHeight(level, type, heightSelf, heightSouth, heightEast, pos.relative(Direction.SOUTH).relative(Direction.EAST));
/* 133 */       heightSouthWest = calculateAverageHeight(level, type, heightSelf, heightSouth, heightWest, pos.relative(Direction.SOUTH).relative(Direction.WEST));
/*     */     } 
/*     */     
/* 136 */     float x = (pos.getX() & 0xF);
/* 137 */     float y = (pos.getY() & 0xF);
/* 138 */     float z = (pos.getZ() & 0xF);
/*     */     
/* 140 */     float offs = 0.001F;
/* 141 */     float bottomOffs = renderDown ? 0.001F : 0.0F;
/*     */     
/* 143 */     if (renderUp && !isFaceOccludedByNeighbor(Direction.UP, Math.min(Math.min(heightNorthWest, heightSouthWest), Math.min(heightSouthEast, heightNorthEast)), blockStateUp)) {
/*     */       float u00, u01, u10, u11, v00, v01, v10, v11;
/* 145 */       heightNorthWest -= 0.001F;
/* 146 */       heightSouthWest -= 0.001F;
/* 147 */       heightSouthEast -= 0.001F;
/* 148 */       heightNorthEast -= 0.001F;
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
/* 159 */       Vec3 flow = fluidState.getFlow((BlockGetter)level, pos);
/* 160 */       if (flow.x == 0.0D && flow.z == 0.0D) {
/* 161 */         u00 = stillSprite.getU(0.0F);
/* 162 */         v00 = stillSprite.getV(0.0F);
/* 163 */         u01 = u00;
/* 164 */         v01 = stillSprite.getV(1.0F);
/* 165 */         u10 = stillSprite.getU(1.0F);
/* 166 */         v10 = v01;
/* 167 */         u11 = u10;
/* 168 */         v11 = v00;
/*     */       } else {
/* 170 */         float angle = (float)Mth.atan2(flow.z, flow.x) - 1.5707964F;
/* 171 */         float s = Mth.sin(angle) * 0.25F;
/* 172 */         float c = Mth.cos(angle) * 0.25F;
/* 173 */         float cc = 0.5F;
/* 174 */         u00 = flowingSprite.getU(0.5F + -c - s);
/* 175 */         v00 = flowingSprite.getV(0.5F + -c + s);
/* 176 */         u01 = flowingSprite.getU(0.5F + -c + s);
/* 177 */         v01 = flowingSprite.getV(0.5F + c + s);
/* 178 */         u10 = flowingSprite.getU(0.5F + c + s);
/* 179 */         v10 = flowingSprite.getV(0.5F + c - s);
/* 180 */         u11 = flowingSprite.getU(0.5F + c - s);
/* 181 */         v11 = flowingSprite.getV(0.5F + -c - s);
/*     */       } 
/*     */       
/* 184 */       int topColor = getLightColor(level, pos);
/* 185 */       float topRed = c11 * r;
/* 186 */       float topGreen = c11 * g;
/* 187 */       float topBlue = c11 * b;
/*     */       
/* 189 */       vertex(builder, x + 0.0F, y + heightNorthWest, z + 0.0F, topRed, topGreen, topBlue, u00, v00, topColor);
/* 190 */       vertex(builder, x + 0.0F, y + heightSouthWest, z + 1.0F, topRed, topGreen, topBlue, u01, v01, topColor);
/* 191 */       vertex(builder, x + 1.0F, y + heightSouthEast, z + 1.0F, topRed, topGreen, topBlue, u10, v10, topColor);
/* 192 */       vertex(builder, x + 1.0F, y + heightNorthEast, z + 0.0F, topRed, topGreen, topBlue, u11, v11, topColor);
/*     */       
/* 194 */       if (fluidState.shouldRenderBackwardUpFace((BlockGetter)level, pos.above())) {
/*     */         
/* 196 */         vertex(builder, x + 0.0F, y + heightNorthWest, z + 0.0F, topRed, topGreen, topBlue, u00, v00, topColor);
/* 197 */         vertex(builder, x + 1.0F, y + heightNorthEast, z + 0.0F, topRed, topGreen, topBlue, u11, v11, topColor);
/* 198 */         vertex(builder, x + 1.0F, y + heightSouthEast, z + 1.0F, topRed, topGreen, topBlue, u10, v10, topColor);
/* 199 */         vertex(builder, x + 0.0F, y + heightSouthWest, z + 1.0F, topRed, topGreen, topBlue, u01, v01, topColor);
/*     */       } 
/*     */     } 
/*     */     
/* 203 */     if (renderDown) {
/* 204 */       float u0 = stillSprite.getU0();
/* 205 */       float u1 = stillSprite.getU1();
/* 206 */       float v0 = stillSprite.getV0();
/* 207 */       float v1 = stillSprite.getV1();
/*     */       
/* 209 */       int belowColor = getLightColor(level, pos.below());
/* 210 */       float belowRed = c10 * r;
/* 211 */       float belowGreen = c10 * g;
/* 212 */       float belowBlue = c10 * b;
/*     */       
/* 214 */       vertex(builder, x, y + bottomOffs, z + 1.0F, belowRed, belowGreen, belowBlue, u0, v1, belowColor);
/* 215 */       vertex(builder, x, y + bottomOffs, z, belowRed, belowGreen, belowBlue, u0, v0, belowColor);
/* 216 */       vertex(builder, x + 1.0F, y + bottomOffs, z, belowRed, belowGreen, belowBlue, u1, v0, belowColor);
/* 217 */       vertex(builder, x + 1.0F, y + bottomOffs, z + 1.0F, belowRed, belowGreen, belowBlue, u1, v1, belowColor);
/*     */     } 
/*     */     
/* 220 */     int sideColor = getLightColor(level, pos);
/*     */     
/* 222 */     for (Direction faceDir : (Iterable<Direction>)Direction.Plane.HORIZONTAL) {
/*     */       float hh0, hh1, x0, z0, x1, z1;
/*     */ 
/*     */ 
/*     */       
/*     */       boolean renderCondition;
/*     */ 
/*     */ 
/*     */       
/* 231 */       switch (faceDir) {
/*     */         case NORTH:
/* 233 */           hh0 = heightNorthWest;
/* 234 */           hh1 = heightNorthEast;
/* 235 */           x0 = x;
/* 236 */           x1 = x + 1.0F;
/* 237 */           z0 = z + 0.001F;
/* 238 */           z1 = z + 0.001F;
/* 239 */           renderCondition = renderNorth;
/*     */           break;
/*     */         case SOUTH:
/* 242 */           hh0 = heightSouthEast;
/* 243 */           hh1 = heightSouthWest;
/* 244 */           x0 = x + 1.0F;
/* 245 */           x1 = x;
/* 246 */           z0 = z + 1.0F - 0.001F;
/* 247 */           z1 = z + 1.0F - 0.001F;
/* 248 */           renderCondition = renderSouth;
/*     */           break;
/*     */         case WEST:
/* 251 */           hh0 = heightSouthWest;
/* 252 */           hh1 = heightNorthWest;
/* 253 */           x0 = x + 0.001F;
/* 254 */           x1 = x + 0.001F;
/* 255 */           z0 = z + 1.0F;
/* 256 */           z1 = z;
/* 257 */           renderCondition = renderWest;
/*     */           break;
/*     */         default:
/* 260 */           hh0 = heightNorthEast;
/* 261 */           hh1 = heightSouthEast;
/* 262 */           x0 = x + 1.0F - 0.001F;
/* 263 */           x1 = x + 1.0F - 0.001F;
/* 264 */           z0 = z;
/* 265 */           z1 = z + 1.0F;
/* 266 */           renderCondition = renderEast;
/*     */           break;
/*     */       } 
/*     */       
/* 270 */       if (renderCondition && !isFaceOccludedByNeighbor(faceDir, Math.max(hh0, hh1), level.getBlockState(pos.relative(faceDir)))) {
/*     */         
/* 272 */         BlockPos tPos = pos.relative(faceDir);
/*     */         
/* 274 */         TextureAtlasSprite sprite = flowingSprite;
/*     */         
/* 276 */         if (!isLava) {
/* 277 */           Block relativeBlock = level.getBlockState(tPos).getBlock();
/* 278 */           if (relativeBlock instanceof net.minecraft.world.level.block.HalfTransparentBlock || relativeBlock instanceof net.minecraft.world.level.block.LeavesBlock) {
/* 279 */             sprite = this.waterOverlay;
/*     */           }
/*     */         } 
/*     */         
/* 283 */         float u0 = sprite.getU(0.0F);
/* 284 */         float u1 = sprite.getU(0.5F);
/*     */         
/* 286 */         float v01 = sprite.getV((1.0F - hh0) * 0.5F);
/* 287 */         float v02 = sprite.getV((1.0F - hh1) * 0.5F);
/* 288 */         float v1 = sprite.getV(0.5F);
/*     */         
/* 290 */         float br = (faceDir.getAxis() == Direction.Axis.Z) ? c2 : c3;
/*     */         
/* 292 */         float red = c11 * br * r;
/* 293 */         float green = c11 * br * g;
/* 294 */         float blue = c11 * br * b;
/*     */         
/* 296 */         vertex(builder, x0, y + hh0, z0, red, green, blue, u0, v01, sideColor);
/* 297 */         vertex(builder, x1, y + hh1, z1, red, green, blue, u1, v02, sideColor);
/* 298 */         vertex(builder, x1, y + bottomOffs, z1, red, green, blue, u1, v1, sideColor);
/* 299 */         vertex(builder, x0, y + bottomOffs, z0, red, green, blue, u0, v1, sideColor);
/*     */         
/* 301 */         if (sprite != this.waterOverlay) {
/* 302 */           vertex(builder, x0, y + bottomOffs, z0, red, green, blue, u0, v1, sideColor);
/* 303 */           vertex(builder, x1, y + bottomOffs, z1, red, green, blue, u1, v1, sideColor);
/* 304 */           vertex(builder, x1, y + hh1, z1, red, green, blue, u1, v02, sideColor);
/* 305 */           vertex(builder, x0, y + hh0, z0, red, green, blue, u0, v01, sideColor);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private float calculateAverageHeight(BlockAndTintGetter level, Fluid type, float heightSelf, float height2, float height1, BlockPos cornerPos) {
/* 312 */     if (height1 >= 1.0F || height2 >= 1.0F) {
/* 313 */       return 1.0F;
/*     */     }
/* 315 */     float[] weightedHeight = new float[2];
/* 316 */     if (height1 > 0.0F || height2 > 0.0F) {
/* 317 */       float heightCorner = getHeight(level, type, cornerPos);
/* 318 */       if (heightCorner >= 1.0F) {
/* 319 */         return 1.0F;
/*     */       }
/* 321 */       addWeightedHeight(weightedHeight, heightCorner);
/*     */     } 
/* 323 */     addWeightedHeight(weightedHeight, heightSelf);
/* 324 */     addWeightedHeight(weightedHeight, height1);
/* 325 */     addWeightedHeight(weightedHeight, height2);
/* 326 */     return weightedHeight[0] / weightedHeight[1];
/*     */   }
/*     */   
/*     */   private void addWeightedHeight(float[] weightedHeight, float height) {
/* 330 */     if (height >= 0.8F) {
/* 331 */       weightedHeight[0] = weightedHeight[0] + height * 10.0F;
/* 332 */       weightedHeight[1] = weightedHeight[1] + 10.0F;
/* 333 */     } else if (height >= 0.0F) {
/* 334 */       weightedHeight[0] = weightedHeight[0] + height;
/* 335 */       weightedHeight[1] = weightedHeight[1] + 1.0F;
/*     */     } 
/*     */   }
/*     */   
/*     */   private float getHeight(BlockAndTintGetter level, Fluid fluidType, BlockPos pos) {
/* 340 */     BlockState state = level.getBlockState(pos);
/* 341 */     return getHeight(level, fluidType, pos, state, state.getFluidState());
/*     */   }
/*     */   
/*     */   private float getHeight(BlockAndTintGetter level, Fluid fluidType, BlockPos pos, BlockState state, FluidState fluidState) {
/* 345 */     if (fluidType.isSame(fluidState.getType())) {
/* 346 */       BlockState aboveState = level.getBlockState(pos.above());
/* 347 */       if (fluidType.isSame(aboveState.getFluidState().getType())) {
/* 348 */         return 1.0F;
/*     */       }
/* 350 */       return fluidState.getOwnHeight();
/* 351 */     }  if (!state.isSolid()) {
/* 352 */       return 0.0F;
/*     */     }
/* 354 */     return -1.0F;
/*     */   }
/*     */   
/*     */   private void vertex(VertexConsumer builder, float x, float y, float z, float red, float green, float blue, float u, float v, int light) {
/* 358 */     builder.addVertex(x, y, z).setColor(red, green, blue, 1.0F).setUv(u, v).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
/*     */   }
/*     */   
/*     */   private int getLightColor(BlockAndTintGetter level, BlockPos pos) {
/* 362 */     int a = LevelRenderer.getLightColor(level, pos);
/* 363 */     int b = LevelRenderer.getLightColor(level, pos.above());
/*     */     
/* 365 */     int aa = a & 0xFF;
/* 366 */     int ba = b & 0xFF;
/* 367 */     int ab = a >> 16 & 0xFF;
/* 368 */     int bb = b >> 16 & 0xFF;
/*     */     
/* 370 */     return ((aa > ba) ? aa : ba) | ((ab > bb) ? ab : bb) << 16;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/LiquidBlockRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */