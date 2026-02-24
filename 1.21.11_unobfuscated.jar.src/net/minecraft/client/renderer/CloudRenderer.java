/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.buffers.Std140Builder;
/*     */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalDouble;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.client.CloudStatus;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ import org.joml.Vector4f;
/*     */ import org.joml.Vector4fc;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class CloudRenderer
/*     */   extends SimplePreparableReloadListener<Optional<CloudRenderer.TextureData>>
/*     */   implements AutoCloseable
/*     */ {
/*     */   private static final int FLAG_INSIDE_FACE = 16;
/*     */   private static final int FLAG_USE_TOP_COLOR = 32;
/*     */   private static final float CELL_SIZE_IN_BLOCKS = 12.0F;
/*     */   private static final int TICKS_PER_CELL = 400;
/*     */   private static final float BLOCKS_PER_SECOND = 0.6F;
/*  48 */   private static final int UBO_SIZE = new Std140SizeCalculator()
/*  49 */     .putVec4()
/*  50 */     .putVec3()
/*  51 */     .putVec3()
/*  52 */     .get();
/*     */   
/*  54 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  55 */   private static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/environment/clouds.png");
/*     */   
/*     */   private static final long EMPTY_CELL = 0L;
/*     */   
/*     */   private static final int COLOR_OFFSET = 4;
/*     */   
/*     */   private static final int NORTH_OFFSET = 3;
/*     */   private static final int EAST_OFFSET = 2;
/*     */   private static final int SOUTH_OFFSET = 1;
/*     */   private static final int WEST_OFFSET = 0;
/*     */   private boolean needsRebuild = true;
/*  66 */   private int prevCellX = Integer.MIN_VALUE;
/*  67 */   private int prevCellZ = Integer.MIN_VALUE;
/*  68 */   private RelativeCameraPos prevRelativeCameraPos = RelativeCameraPos.INSIDE_CLOUDS;
/*     */   
/*     */   private CloudStatus prevType;
/*     */   private TextureData texture;
/*  72 */   private int quadCount = 0;
/*  73 */   private final MappableRingBuffer ubo = new MappableRingBuffer(() -> "Cloud UBO", 130, UBO_SIZE);
/*     */   
/*     */   private MappableRingBuffer utb;
/*     */   
/*     */   protected Optional<TextureData> prepare(ResourceManager manager, ProfilerFiller profiler) {
/*     */     
/*  79 */     try { InputStream input = manager.open(TEXTURE_LOCATION); 
/*  80 */       try { NativeImage texture = NativeImage.read(input);
/*     */         
/*  82 */         try { int width = texture.getWidth();
/*  83 */           int height = texture.getHeight();
/*  84 */           long[] cells = new long[width * height];
/*  85 */           for (int y = 0; y < height; y++) {
/*  86 */             for (int x = 0; x < width; x++) {
/*  87 */               int color = texture.getPixel(x, y);
/*  88 */               if (isCellEmpty(color)) {
/*  89 */                 cells[x + y * width] = 0L;
/*     */               } else {
/*     */                 
/*  92 */                 boolean north = isCellEmpty(texture.getPixel(x, Math.floorMod(y - 1, height)));
/*  93 */                 boolean east = isCellEmpty(texture.getPixel(Math.floorMod(x + 1, height), y));
/*  94 */                 boolean south = isCellEmpty(texture.getPixel(x, Math.floorMod(y + 1, height)));
/*  95 */                 boolean west = isCellEmpty(texture.getPixel(Math.floorMod(x - 1, height), y));
/*  96 */                 cells[x + y * width] = packCellData(color, north, east, south, west);
/*     */               } 
/*     */             } 
/*  99 */           }  Optional<TextureData> optional = Optional.of(new TextureData(cells, width, height));
/* 100 */           if (texture != null) texture.close();  if (input != null) input.close();  return optional; } catch (Throwable throwable) { if (texture != null) try { texture.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable throwable) { if (input != null) try { input.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 101 */     { LOGGER.error("Failed to load cloud texture", e);
/* 102 */       return Optional.empty(); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getSizeForCloudDistance(int radiusCells) {
/* 111 */     int maxFacesPerCell = 4;
/* 112 */     int maxCells = (radiusCells + 1) * 2 * (radiusCells + 1) * 2 / 2;
/* 113 */     int maxFaces = maxCells * 4 + 54;
/* 114 */     return maxFaces * 3;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void apply(Optional<TextureData> preparations, ResourceManager manager, ProfilerFiller profiler) {
/* 119 */     this.texture = preparations.orElse(null);
/* 120 */     this.needsRebuild = true;
/*     */   }
/*     */   
/*     */   private static boolean isCellEmpty(int color) {
/* 124 */     return (ARGB.alpha(color) < 10);
/*     */   }
/*     */   
/*     */   private static long packCellData(int color, boolean north, boolean east, boolean south, boolean west) {
/* 128 */     return color << 4L | ((north ? 1 : 0) << 3) | ((east ? 1 : 0) << 2) | ((south ? 1 : 0) << 1) | ((west ? 1 : 0) << 0);
/*     */   }
/*     */   
/*     */   private static boolean isNorthEmpty(long cellData) {
/* 132 */     return ((cellData >> 3L & 0x1L) != 0L);
/*     */   }
/*     */   
/*     */   private static boolean isEastEmpty(long cellData) {
/* 136 */     return ((cellData >> 2L & 0x1L) != 0L);
/*     */   }
/*     */   
/*     */   private static boolean isSouthEmpty(long cellData) {
/* 140 */     return ((cellData >> 1L & 0x1L) != 0L);
/*     */   }
/*     */   
/*     */   private static boolean isWestEmpty(long cellData) {
/* 144 */     return ((cellData >> 0L & 0x1L) != 0L);
/*     */   }
/*     */   public void render(int color, CloudStatus type, float bottomY, Vec3 cameraPosition, long gameTime, float partialTicks) { RelativeCameraPos relativeCameraPos;
/*     */     GpuTextureView colorTexture, depthTexture;
/* 148 */     if (this.texture == null) {
/*     */       return;
/*     */     }
/* 151 */     int radiusBlocks = (Integer)(Minecraft.getInstance()).options.cloudRange().get() * 16;
/* 152 */     int radiusCells = Mth.ceil(radiusBlocks / 12.0F);
/* 153 */     int utbSize = getSizeForCloudDistance(radiusCells);
/* 154 */     if (this.utb == null || this.utb.currentBuffer().size() != utbSize) {
/* 155 */       if (this.utb != null) {
/* 156 */         this.utb.close();
/*     */       }
/* 158 */       this.utb = new MappableRingBuffer(() -> "Cloud UTB", 258, utbSize);
/*     */     } 
/*     */     
/* 161 */     float relativeBottomY = (float)(bottomY - cameraPosition.y);
/* 162 */     float relativeTopY = relativeBottomY + 4.0F;
/*     */     
/* 164 */     if (relativeTopY < 0.0F) {
/* 165 */       relativeCameraPos = RelativeCameraPos.ABOVE_CLOUDS;
/* 166 */     } else if (relativeBottomY > 0.0F) {
/* 167 */       relativeCameraPos = RelativeCameraPos.BELOW_CLOUDS;
/*     */     } else {
/* 169 */       relativeCameraPos = RelativeCameraPos.INSIDE_CLOUDS;
/*     */     } 
/*     */     
/* 172 */     float cloudOffset = (float)(gameTime % this.texture.width * 400L) + partialTicks;
/*     */ 
/*     */     
/* 175 */     double cloudX = cameraPosition.x + (cloudOffset * 0.030000001F);
/* 176 */     double cloudZ = cameraPosition.z + 3.9600000381469727D;
/*     */ 
/*     */     
/* 179 */     double textureWidthBlocks = this.texture.width * 12.0D;
/* 180 */     double textureHeightBlocks = this.texture.height * 12.0D;
/* 181 */     cloudX -= Mth.floor(cloudX / textureWidthBlocks) * textureWidthBlocks;
/* 182 */     cloudZ -= Mth.floor(cloudZ / textureHeightBlocks) * textureHeightBlocks;
/*     */     
/* 184 */     int cellX = Mth.floor(cloudX / 12.0D);
/* 185 */     int cellZ = Mth.floor(cloudZ / 12.0D);
/*     */     
/* 187 */     float xInCell = (float)(cloudX - (cellX * 12.0F));
/* 188 */     float zInCell = (float)(cloudZ - (cellZ * 12.0F));
/*     */     
/* 190 */     boolean fancyClouds = (type == CloudStatus.FANCY);
/* 191 */     RenderPipeline renderPipeline = fancyClouds ? RenderPipelines.CLOUDS : RenderPipelines.FLAT_CLOUDS;
/*     */     
/* 193 */     if (this.needsRebuild || cellX != this.prevCellX || cellZ != this.prevCellZ || relativeCameraPos != this.prevRelativeCameraPos || type != this.prevType) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 198 */       this.needsRebuild = false;
/* 199 */       this.prevCellX = cellX;
/* 200 */       this.prevCellZ = cellZ;
/* 201 */       this.prevRelativeCameraPos = relativeCameraPos;
/* 202 */       this.prevType = type;
/* 203 */       this.utb.rotate();
/* 204 */       GpuBuffer.MappedView mappedView = RenderSystem.getDevice().createCommandEncoder().mapBuffer(this.utb.currentBuffer(), false, true); 
/* 205 */       try { buildMesh(relativeCameraPos, mappedView.data(), cellX, cellZ, fancyClouds, radiusCells);
/* 206 */         this.quadCount = mappedView.data().position() / 3;
/* 207 */         if (mappedView != null) mappedView.close();  } catch (Throwable throwable) { if (mappedView != null)
/*     */           try { mappedView.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*     */     
/* 210 */     }  if (this.quadCount == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 214 */     GpuBuffer.MappedView view = RenderSystem.getDevice().createCommandEncoder().mapBuffer(this.ubo.currentBuffer(), false, true); 
/* 215 */     try { Std140Builder.intoBuffer(view.data())
/* 216 */         .putVec4((Vector4fc)ARGB.vector4fFromARGB32(color))
/* 217 */         .putVec3(-xInCell, relativeBottomY, -zInCell)
/* 218 */         .putVec3(12.0F, 4.0F, 12.0F);
/* 219 */       if (view != null) view.close();  } catch (Throwable throwable) { if (view != null)
/* 220 */         try { view.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform((Matrix4fc)RenderSystem.getModelViewMatrix(), (Vector4fc)new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), (Vector3fc)new Vector3f(), (Matrix4fc)new Matrix4f());
/*     */     
/* 222 */     RenderTarget mainRenderTarget = Minecraft.getInstance().getMainRenderTarget();
/* 223 */     RenderTarget cloudTarget = (Minecraft.getInstance()).levelRenderer.getCloudsTarget();
/*     */ 
/*     */     
/* 226 */     RenderSystem.AutoStorageIndexBuffer indices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
/* 227 */     GpuBuffer indexBuffer = indices.getBuffer(6 * this.quadCount);
/* 228 */     if (cloudTarget != null) {
/* 229 */       colorTexture = cloudTarget.getColorTextureView();
/* 230 */       depthTexture = cloudTarget.getDepthTextureView();
/*     */     } else {
/* 232 */       colorTexture = mainRenderTarget.getColorTextureView();
/* 233 */       depthTexture = mainRenderTarget.getDepthTextureView();
/*     */     } 
/*     */     
/* 236 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Clouds", colorTexture, OptionalInt.empty(), depthTexture, OptionalDouble.empty()); 
/* 237 */     try { renderPass.setPipeline(renderPipeline);
/* 238 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 239 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 240 */       renderPass.setIndexBuffer(indexBuffer, indices.type());
/* 241 */       renderPass.setUniform("CloudInfo", this.ubo.currentBuffer());
/* 242 */       renderPass.setUniform("CloudFaces", this.utb.currentBuffer());
/* 243 */       renderPass.drawIndexed(0, 0, 6 * this.quadCount, 1);
/* 244 */       if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/*     */         try { renderPass.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 248 */      } private void buildMesh(RelativeCameraPos relativePos, ByteBuffer faceBuffer, int centerCellX, int centerCellZ, boolean extrude, int radiusCells) { if (this.texture == null) {
/*     */       return;
/*     */     }
/* 251 */     long[] cells = this.texture.cells;
/* 252 */     int textureWidth = this.texture.width;
/* 253 */     int textureHeight = this.texture.height;
/*     */ 
/*     */     
/* 256 */     for (int ring = 0; ring <= 2 * radiusCells; ring++) {
/* 257 */       for (int relativeCellX = -ring; relativeCellX <= ring; relativeCellX++) {
/* 258 */         int relativeCellZ = ring - Math.abs(relativeCellX);
/* 259 */         if (relativeCellZ >= 0 && relativeCellZ <= radiusCells)
/*     */         {
/*     */           
/* 262 */           if (relativeCellX * relativeCellX + relativeCellZ * relativeCellZ <= radiusCells * radiusCells) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 267 */             if (relativeCellZ != 0) {
/* 268 */               tryBuildCell(relativePos, faceBuffer, centerCellX, centerCellZ, extrude, relativeCellX, textureWidth, -relativeCellZ, textureHeight, cells);
/*     */             }
/* 270 */             tryBuildCell(relativePos, faceBuffer, centerCellX, centerCellZ, extrude, relativeCellX, textureWidth, relativeCellZ, textureHeight, cells);
/*     */           }  } 
/*     */       } 
/*     */     }  }
/*     */   
/*     */   private void tryBuildCell(RelativeCameraPos relativePos, ByteBuffer faceBuffer, int cellX, int cellZ, boolean extrude, int relativeCellX, int textureWidth, int relativeCellZ, int textureHeight, long[] cells) {
/* 276 */     int indexX = Math.floorMod(cellX + relativeCellX, textureWidth);
/* 277 */     int indexY = Math.floorMod(cellZ + relativeCellZ, textureHeight);
/* 278 */     long cellData = cells[indexX + indexY * textureWidth];
/* 279 */     if (cellData == 0L) {
/*     */       return;
/*     */     }
/* 282 */     if (extrude) {
/* 283 */       buildExtrudedCell(relativePos, faceBuffer, relativeCellX, relativeCellZ, cellData);
/*     */     } else {
/* 285 */       buildFlatCell(faceBuffer, relativeCellX, relativeCellZ);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void buildFlatCell(ByteBuffer faceBuffer, int x, int z) {
/* 290 */     encodeFace(faceBuffer, x, z, Direction.DOWN, 32);
/*     */   }
/*     */   
/*     */   private void encodeFace(ByteBuffer faceBuffer, int x, int z, Direction direction, int flags) {
/* 294 */     int dirAndFlags = direction.get3DDataValue() | flags;
/* 295 */     dirAndFlags |= (x & 0x1) << 7;
/* 296 */     dirAndFlags |= (z & 0x1) << 6;
/* 297 */     faceBuffer.put((byte)(x >> 1)).put((byte)(z >> 1)).put((byte)dirAndFlags);
/*     */   }
/*     */ 
/*     */   
/*     */   private void buildExtrudedCell(RelativeCameraPos relativePos, ByteBuffer faceBuffer, int x, int z, long cellData) {
/* 302 */     if (relativePos != RelativeCameraPos.BELOW_CLOUDS) {
/* 303 */       encodeFace(faceBuffer, x, z, Direction.UP, 0);
/*     */     }
/*     */ 
/*     */     
/* 307 */     if (relativePos != RelativeCameraPos.ABOVE_CLOUDS) {
/* 308 */       encodeFace(faceBuffer, x, z, Direction.DOWN, 0);
/*     */     }
/*     */     
/* 311 */     if (isNorthEmpty(cellData) && z > 0) {
/* 312 */       encodeFace(faceBuffer, x, z, Direction.NORTH, 0);
/*     */     }
/*     */     
/* 315 */     if (isSouthEmpty(cellData) && z < 0) {
/* 316 */       encodeFace(faceBuffer, x, z, Direction.SOUTH, 0);
/*     */     }
/*     */     
/* 319 */     if (isWestEmpty(cellData) && x > 0) {
/* 320 */       encodeFace(faceBuffer, x, z, Direction.WEST, 0);
/*     */     }
/*     */     
/* 323 */     if (isEastEmpty(cellData) && x < 0) {
/* 324 */       encodeFace(faceBuffer, x, z, Direction.EAST, 0);
/*     */     }
/*     */ 
/*     */     
/* 328 */     boolean addInteriorFaces = (Math.abs(x) <= 1 && Math.abs(z) <= 1);
/* 329 */     if (addInteriorFaces) {
/* 330 */       for (Direction direction : Direction.values()) {
/* 331 */         encodeFace(faceBuffer, x, z, direction, 16);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void markForRebuild() {
/* 337 */     this.needsRebuild = true;
/*     */   }
/*     */   
/*     */   public void endFrame() {
/* 341 */     this.ubo.rotate();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 346 */     this.ubo.close();
/* 347 */     if (this.utb != null)
/* 348 */       this.utb.close(); 
/*     */   }
/*     */   
/*     */   private enum RelativeCameraPos
/*     */   {
/* 353 */     ABOVE_CLOUDS,
/* 354 */     INSIDE_CLOUDS,
/* 355 */     BELOW_CLOUDS;
/*     */   }
/*     */   public static final class TextureData extends Record { private final long[] cells; private final int width; private final int height;
/* 358 */     public TextureData(long[] cells, int width, int height) { this.cells = cells; this.width = width; this.height = height; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/CloudRenderer$TextureData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #358	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/CloudRenderer$TextureData; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/CloudRenderer$TextureData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #358	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/CloudRenderer$TextureData; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/CloudRenderer$TextureData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #358	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/CloudRenderer$TextureData;
/* 358 */       //   0	8	1	o	Ljava/lang/Object; } public long[] cells() { return this.cells; } public int width() { return this.width; } public int height() { return this.height; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/CloudRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */