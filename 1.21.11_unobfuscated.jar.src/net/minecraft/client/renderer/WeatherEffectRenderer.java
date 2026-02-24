/*     */ package net.minecraft.client.renderer;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.WeatherRenderState;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ParticleStatus;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class WeatherEffectRenderer {
/*     */   private static final float RAIN_PARTICLES_PER_BLOCK = 0.225F;
/*     */   private static final int RAIN_RADIUS = 10;
/*  38 */   private static final Identifier RAIN_LOCATION = Identifier.withDefaultNamespace("textures/environment/rain.png");
/*  39 */   private static final Identifier SNOW_LOCATION = Identifier.withDefaultNamespace("textures/environment/snow.png");
/*     */   
/*     */   private static final int RAIN_TABLE_SIZE = 32;
/*     */   private static final int HALF_RAIN_TABLE_SIZE = 16;
/*     */   private int rainSoundTime;
/*  44 */   private final float[] columnSizeX = new float[1024];
/*  45 */   private final float[] columnSizeZ = new float[1024];
/*     */   
/*     */   public WeatherEffectRenderer() {
/*  48 */     for (int z = 0; z < 32; z++) {
/*  49 */       for (int x = 0; x < 32; x++) {
/*  50 */         float deltaX = (x - 16);
/*  51 */         float deltaZ = (z - 16);
/*  52 */         float distance = Mth.length(deltaX, deltaZ);
/*  53 */         this.columnSizeX[z * 32 + x] = -deltaZ / distance;
/*  54 */         this.columnSizeZ[z * 32 + x] = deltaX / distance;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void extractRenderState(Level level, int ticks, float partialTicks, Vec3 cameraPos, WeatherRenderState renderState) {
/*  60 */     renderState.intensity = level.getRainLevel(partialTicks);
/*  61 */     if (renderState.intensity <= 0.0F) {
/*     */       return;
/*     */     }
/*     */     
/*  65 */     renderState.radius = (Integer)(Minecraft.getInstance()).options.weatherRadius().get();
/*  66 */     int cameraBlockX = Mth.floor(cameraPos.x);
/*  67 */     int cameraBlockY = Mth.floor(cameraPos.y);
/*  68 */     int cameraBlockZ = Mth.floor(cameraPos.z);
/*     */     
/*  70 */     BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
/*  71 */     RandomSource random = RandomSource.create();
/*     */     
/*  73 */     for (int z = cameraBlockZ - renderState.radius; z <= cameraBlockZ + renderState.radius; z++) {
/*  74 */       for (int x = cameraBlockX - renderState.radius; x <= cameraBlockX + renderState.radius; x++) {
/*  75 */         int terrainHeight = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
/*  76 */         int y0 = Math.max(cameraBlockY - renderState.radius, terrainHeight);
/*  77 */         int y1 = Math.max(cameraBlockY + renderState.radius, terrainHeight);
/*  78 */         if (y1 - y0 != 0) {
/*     */ 
/*     */ 
/*     */           
/*  82 */           Biome.Precipitation precipitation = getPrecipitationAt(level, (BlockPos)mutablePos.set(x, cameraBlockY, z));
/*  83 */           if (precipitation != Biome.Precipitation.NONE) {
/*     */ 
/*     */ 
/*     */             
/*  87 */             int seed = x * x * 3121 + x * 45238971 ^ z * z * 418711 + z * 13761;
/*  88 */             random.setSeed(seed);
/*     */ 
/*     */             
/*  91 */             int lightSampleY = Math.max(cameraBlockY, terrainHeight);
/*  92 */             int lightCoords = LevelRenderer.getLightColor((net.minecraft.world.level.BlockAndTintGetter)level, (BlockPos)mutablePos.set(x, lightSampleY, z));
/*     */             
/*  94 */             if (precipitation == Biome.Precipitation.RAIN) {
/*  95 */               renderState.rainColumns.add(createRainColumnInstance(random, ticks, x, y0, y1, z, lightCoords, partialTicks));
/*  96 */             } else if (precipitation == Biome.Precipitation.SNOW) {
/*  97 */               renderState.snowColumns.add(createSnowColumnInstance(random, ticks, x, y0, y1, z, lightCoords, partialTicks));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } public void render(MultiBufferSource bufferSource, Vec3 cameraPos, WeatherRenderState renderState) {
/* 104 */     if (!renderState.rainColumns.isEmpty()) {
/* 105 */       RenderType renderType = RenderTypes.weather(RAIN_LOCATION, Minecraft.useShaderTransparency());
/* 106 */       renderInstances(bufferSource.getBuffer(renderType), renderState.rainColumns, cameraPos, 1.0F, renderState.radius, renderState.intensity);
/*     */     } 
/* 108 */     if (!renderState.snowColumns.isEmpty()) {
/* 109 */       RenderType renderType = RenderTypes.weather(SNOW_LOCATION, Minecraft.useShaderTransparency());
/* 110 */       renderInstances(bufferSource.getBuffer(renderType), renderState.snowColumns, cameraPos, 0.8F, renderState.radius, renderState.intensity);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private ColumnInstance createRainColumnInstance(RandomSource random, int ticks, int x, int bottomY, int topY, int z, int lightCoords, float partialTicks) {
/* 116 */     int wrappedTicks = ticks & 0x1FFFF;
/* 117 */     int tickOffset = x * x * 3121 + x * 45238971 + z * z * 418711 + z * 13761 & 0xFF;
/* 118 */     float blockPosRainSpeed = 3.0F + random.nextFloat();
/* 119 */     float textureOffset = -((wrappedTicks + tickOffset) + partialTicks) / 32.0F * blockPosRainSpeed;
/* 120 */     float wrappedTextureOffset = textureOffset % 32.0F;
/*     */     
/* 122 */     return new ColumnInstance(x, z, bottomY, topY, 0.0F, wrappedTextureOffset, lightCoords);
/*     */   }
/*     */   
/*     */   private ColumnInstance createSnowColumnInstance(RandomSource random, int ticks, int x, int bottomY, int topY, int z, int lightCoords, float partialTicks) {
/* 126 */     float time = ticks + partialTicks;
/*     */     
/* 128 */     float u = (float)(random.nextDouble() + (time * 0.01F * (float)random.nextGaussian()));
/* 129 */     float v = (float)(random.nextDouble() + (time * (float)random.nextGaussian() * 0.001F));
/* 130 */     float vOffset = -((ticks & 0x1FF) + partialTicks) / 512.0F;
/*     */     
/* 132 */     int brightenedLightCoords = LightTexture.pack((
/* 133 */         LightTexture.block(lightCoords) * 3 + 15) / 4, (
/* 134 */         LightTexture.sky(lightCoords) * 3 + 15) / 4);
/*     */ 
/*     */     
/* 137 */     return new ColumnInstance(x, z, bottomY, topY, u, vOffset + v, brightenedLightCoords);
/*     */   }
/*     */   public static final class ColumnInstance extends Record { private final int x; private final int z; private final int bottomY; private final int topY; private final float uOffset; private final float vOffset; private final int lightCoords;
/* 140 */     public ColumnInstance(int x, int z, int bottomY, int topY, float uOffset, float vOffset, int lightCoords) { this.x = x; this.z = z; this.bottomY = bottomY; this.topY = topY; this.uOffset = uOffset; this.vOffset = vOffset; this.lightCoords = lightCoords; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #140	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 140 */       //   0	7	0	this	Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance; } public int x() { return this.x; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #140	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #140	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;
/* 140 */       //   0	8	1	o	Ljava/lang/Object; } public int z() { return this.z; } public int bottomY() { return this.bottomY; } public int topY() { return this.topY; } public float uOffset() { return this.uOffset; } public float vOffset() { return this.vOffset; } public int lightCoords() { return this.lightCoords; }
/*     */      }
/*     */   
/*     */   private void renderInstances(VertexConsumer builder, List<ColumnInstance> columns, Vec3 cameraPos, float maxAlpha, int radius, float intensity) {
/* 144 */     float radiusSq = (radius * radius);
/* 145 */     for (ColumnInstance column : columns) {
/* 146 */       float relativeX = (float)(column.x + 0.5D - cameraPos.x);
/* 147 */       float relativeZ = (float)(column.z + 0.5D - cameraPos.z);
/* 148 */       float distanceSq = (float)Mth.lengthSquared(relativeX, relativeZ);
/*     */       
/* 150 */       float alpha = Mth.lerp(Math.min(distanceSq / radiusSq, 1.0F), maxAlpha, 0.5F) * intensity;
/* 151 */       int color = ARGB.white(alpha);
/*     */       
/* 153 */       int index = (column.z - Mth.floor(cameraPos.z) + 16) * 32 + column.x - Mth.floor(cameraPos.x) + 16;
/* 154 */       float halfSizeX = this.columnSizeX[index] / 2.0F;
/* 155 */       float halfSizeZ = this.columnSizeZ[index] / 2.0F;
/*     */       
/* 157 */       float x0 = relativeX - halfSizeX;
/* 158 */       float x1 = relativeX + halfSizeX;
/* 159 */       float y1 = (float)(column.topY - cameraPos.y);
/* 160 */       float y0 = (float)(column.bottomY - cameraPos.y);
/* 161 */       float z0 = relativeZ - halfSizeZ;
/* 162 */       float z1 = relativeZ + halfSizeZ;
/*     */       
/* 164 */       float u0 = column.uOffset + 0.0F;
/* 165 */       float u1 = column.uOffset + 1.0F;
/* 166 */       float v0 = column.bottomY * 0.25F + column.vOffset;
/* 167 */       float v1 = column.topY * 0.25F + column.vOffset;
/*     */       
/* 169 */       builder.addVertex(x0, y1, z0).setUv(u0, v0).setColor(color).setLight(column.lightCoords);
/* 170 */       builder.addVertex(x1, y1, z1).setUv(u1, v0).setColor(color).setLight(column.lightCoords);
/* 171 */       builder.addVertex(x1, y0, z1).setUv(u1, v1).setColor(color).setLight(column.lightCoords);
/* 172 */       builder.addVertex(x0, y0, z0).setUv(u0, v1).setColor(color).setLight(column.lightCoords);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tickRainParticles(ClientLevel level, Camera camera, int ticks, ParticleStatus particleStatus, int weatherRadius) {
/* 177 */     float rainLevel = level.getRainLevel(1.0F);
/* 178 */     if (rainLevel <= 0.0F) {
/*     */       return;
/*     */     }
/*     */     
/* 182 */     RandomSource random = RandomSource.create(ticks * 312987231L);
/* 183 */     BlockPos cameraPosition = BlockPos.containing((Position)camera.position());
/*     */     
/* 185 */     BlockPos rainParticlePosition = null;
/*     */     
/* 187 */     int weatherDiameter = 2 * weatherRadius + 1;
/* 188 */     int weatherArea = weatherDiameter * weatherDiameter;
/* 189 */     int rainParticles = (int)(0.225F * weatherArea * rainLevel * rainLevel) / ((particleStatus == ParticleStatus.DECREASED) ? 2 : 1);
/* 190 */     for (int ii = 0; ii < rainParticles; ii++) {
/* 191 */       int x = random.nextInt(weatherDiameter) - weatherRadius;
/* 192 */       int z = random.nextInt(weatherDiameter) - weatherRadius;
/*     */ 
/*     */       
/* 195 */       BlockPos heightmapPosition = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, cameraPosition.offset(x, 0, z));
/* 196 */       if (heightmapPosition.getY() > level.getMinY() && heightmapPosition.getY() <= cameraPosition.getY() + 10 && heightmapPosition.getY() >= cameraPosition.getY() - 10)
/*     */       {
/*     */ 
/*     */         
/* 200 */         if (getPrecipitationAt((Level)level, heightmapPosition) == Biome.Precipitation.RAIN) {
/* 201 */           rainParticlePosition = heightmapPosition.below();
/*     */           
/* 203 */           if (particleStatus == ParticleStatus.MINIMAL) {
/*     */             break;
/*     */           }
/*     */ 
/*     */           
/* 208 */           double blockX = random.nextDouble();
/* 209 */           double blockZ = random.nextDouble();
/*     */           
/* 211 */           BlockState block = level.getBlockState(rainParticlePosition);
/* 212 */           FluidState fluid = level.getFluidState(rainParticlePosition);
/* 213 */           VoxelShape blockShape = block.getCollisionShape((BlockGetter)level, rainParticlePosition);
/*     */           
/* 215 */           double blockTop = blockShape.max(net.minecraft.core.Direction.Axis.Y, blockX, blockZ);
/* 216 */           double fluidTop = fluid.getHeight((BlockGetter)level, rainParticlePosition);
/* 217 */           double particleY = Math.max(blockTop, fluidTop);
/*     */           
/* 219 */           SimpleParticleType simpleParticleType = (fluid.is(FluidTags.LAVA) || block.is(Blocks.MAGMA_BLOCK) || CampfireBlock.isLitCampfire(block)) ? ParticleTypes.SMOKE : ParticleTypes.RAIN;
/* 220 */           level.addParticle((ParticleOptions)simpleParticleType, rainParticlePosition.getX() + blockX, rainParticlePosition.getY() + particleY, rainParticlePosition.getZ() + blockZ, 0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */       }
/*     */     } 
/* 224 */     if (rainParticlePosition != null && random.nextInt(3) < this.rainSoundTime++) {
/* 225 */       this.rainSoundTime = 0;
/* 226 */       if (rainParticlePosition.getY() > cameraPosition.getY() + 1 && level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, cameraPosition).getY() > Mth.floor(cameraPosition.getY())) {
/*     */         
/* 228 */         level.playLocalSound(rainParticlePosition, SoundEvents.WEATHER_RAIN_ABOVE, SoundSource.WEATHER, 0.1F, 0.5F, false);
/*     */       } else {
/* 230 */         level.playLocalSound(rainParticlePosition, SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.2F, 1.0F, false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Biome.Precipitation getPrecipitationAt(Level level, BlockPos pos) {
/* 236 */     if (!level.getChunkSource().hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()))) {
/* 237 */       return Biome.Precipitation.NONE;
/*     */     }
/* 239 */     Biome biome = (Biome)level.getBiome(pos).value();
/* 240 */     return biome.getPrecipitationAt(pos, level.getSeaLevel());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/WeatherEffectRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */