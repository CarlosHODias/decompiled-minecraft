/*     */ package net.minecraft.client.renderer.block.model;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.math.MatrixUtil;
/*     */ import com.mojang.math.Quadrant;
/*     */ import com.mojang.math.Transformation;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.model.geom.builders.UVPair;
/*     */ import net.minecraft.client.renderer.FaceInfo;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.model.ModelBaker;
/*     */ import net.minecraft.client.resources.model.ModelState;
/*     */ import net.minecraft.core.Direction;
/*     */ import org.joml.GeometryUtils;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FaceBakery
/*     */ {
/*  24 */   private static final Vector3fc BLOCK_MIDDLE = (Vector3fc)new Vector3f(0.5F, 0.5F, 0.5F);
/*     */   
/*     */   @VisibleForTesting
/*     */   static BlockElementFace.UVs defaultFaceUV(Vector3fc from, Vector3fc to, Direction facing) {
/*  28 */     switch (facing) { default: throw new MatchException(null, null);case DOWN: case UP: case NORTH: case SOUTH: case WEST: case EAST: break; }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  34 */       new BlockElementFace.UVs(16.0F - to.z(), 16.0F - to.y(), 16.0F - from.z(), 16.0F - from.y());
/*     */   }
/*     */ 
/*     */   
/*     */   public static BakedQuad bakeQuad(ModelBaker.PartCache partCache, Vector3fc from, Vector3fc to, BlockElementFace face, TextureAtlasSprite icon, Direction facing, ModelState modelState, BlockElementRotation elementRotation, boolean shade, int lightEmission) {
/*  39 */     BlockElementFace.UVs uvs = face.uvs();
/*  40 */     if (uvs == null) {
/*  41 */       uvs = defaultFaceUV(from, to, facing);
/*     */     }
/*     */ 
/*     */     
/*  45 */     Matrix4fc uvTransform = modelState.inverseFaceTransformation(facing);
/*     */     
/*  47 */     Vector3fc[] vertexPositions = new Vector3fc[4];
/*  48 */     long[] vertexPackedUvs = new long[4];
/*     */     
/*  50 */     FaceInfo faceInfo = FaceInfo.fromFacing(facing);
/*  51 */     for (int i = 0; i < 4; i++) {
/*  52 */       bakeVertex(i, faceInfo, uvs, face.rotation(), uvTransform, from, to, icon, modelState.transformation(), elementRotation, vertexPositions, vertexPackedUvs, partCache);
/*     */     }
/*     */     
/*  55 */     Direction finalDirection = calculateFacing(vertexPositions);
/*     */     
/*  57 */     if (elementRotation == null && finalDirection != null)
/*     */     {
/*  59 */       recalculateWinding(vertexPositions, vertexPackedUvs, finalDirection);
/*     */     }
/*     */     
/*  62 */     return new BakedQuad(vertexPositions[0], vertexPositions[1], vertexPositions[2], vertexPositions[3], vertexPackedUvs[0], vertexPackedUvs[1], vertexPackedUvs[2], vertexPackedUvs[3], 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  71 */         face.tintIndex(), 
/*  72 */         Objects.<Direction>requireNonNullElse(finalDirection, Direction.UP), icon, shade, lightEmission);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void bakeVertex(int index, FaceInfo faceInfo, BlockElementFace.UVs uvs, Quadrant uvRotation, Matrix4fc uvTransform, Vector3fc from, Vector3fc to, TextureAtlasSprite icon, Transformation rotation, BlockElementRotation elementRotation, Vector3fc[] positionOutput, long[] uvOutput, ModelBaker.PartCache partCache) {
/*     */     float transformedV, transformedU;
/*  80 */     FaceInfo.VertexInfo vertexInfo = faceInfo.getVertexInfo(index);
/*  81 */     Vector3f vertex = vertexInfo.select(from, to).div(16.0F);
/*     */     
/*  83 */     if (elementRotation != null) {
/*  84 */       rotateVertexBy(vertex, elementRotation.origin(), elementRotation.transform());
/*     */     }
/*     */     
/*  87 */     if (rotation != Transformation.identity()) {
/*  88 */       rotateVertexBy(vertex, BLOCK_MIDDLE, rotation.getMatrix());
/*     */     }
/*     */     
/*  91 */     float rawU = BlockElementFace.getU(uvs, uvRotation, index);
/*  92 */     float rawV = BlockElementFace.getV(uvs, uvRotation, index);
/*     */ 
/*     */ 
/*     */     
/*  96 */     if (MatrixUtil.isIdentity(uvTransform)) {
/*  97 */       transformedU = rawU;
/*  98 */       transformedV = rawV;
/*     */     } else {
/* 100 */       Vector3f transformedUV = uvTransform.transformPosition(new Vector3f(cornerToCenter(rawU), cornerToCenter(rawV), 0.0F));
/* 101 */       transformedU = centerToCorner(transformedUV.x);
/* 102 */       transformedV = centerToCorner(transformedUV.y);
/*     */     } 
/* 104 */     positionOutput[index] = partCache.vector((Vector3fc)vertex);
/* 105 */     uvOutput[index] = UVPair.pack(icon.getU(transformedU), icon.getV(transformedV));
/*     */   }
/*     */   
/*     */   private static float cornerToCenter(float value) {
/* 109 */     return value - 0.5F;
/*     */   }
/*     */   
/*     */   private static float centerToCorner(float value) {
/* 113 */     return value + 0.5F;
/*     */   }
/*     */   
/*     */   private static void rotateVertexBy(Vector3f vertex, Vector3fc origin, Matrix4fc transformation) {
/* 117 */     vertex.sub(origin);
/* 118 */     transformation.transformPosition(vertex);
/* 119 */     vertex.add(origin);
/*     */   }
/*     */   
/*     */   private static Direction calculateFacing(Vector3fc[] positions) {
/* 123 */     Vector3f normal = new Vector3f();
/* 124 */     GeometryUtils.normal(positions[0], positions[1], positions[2], normal);
/* 125 */     return findClosestDirection(normal);
/*     */   }
/*     */   
/*     */   private static Direction findClosestDirection(Vector3f direction) {
/* 129 */     if (!direction.isFinite()) {
/* 130 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 134 */     Direction best = null;
/* 135 */     float closestProduct = 0.0F;
/* 136 */     for (Direction candidate : Direction.values()) {
/* 137 */       float product = direction.dot(candidate.getUnitVec3f());
/*     */       
/* 139 */       if (product >= 0.0F && product > closestProduct) {
/* 140 */         closestProduct = product;
/* 141 */         best = candidate;
/*     */       } 
/*     */     } 
/* 144 */     return best;
/*     */   }
/*     */   
/*     */   private static void recalculateWinding(Vector3fc[] positions, long[] uvs, Direction direction) {
/* 148 */     float minX = 999.0F;
/* 149 */     float minY = 999.0F;
/* 150 */     float minZ = 999.0F;
/* 151 */     float maxX = -999.0F;
/* 152 */     float maxY = -999.0F;
/* 153 */     float maxZ = -999.0F;
/* 154 */     for (int i = 0; i < 4; i++) {
/* 155 */       Vector3fc position = positions[i];
/* 156 */       float x = position.x();
/* 157 */       float y = position.y();
/* 158 */       float z = position.z();
/* 159 */       if (x < minX) {
/* 160 */         minX = x;
/*     */       }
/* 162 */       if (y < minY) {
/* 163 */         minY = y;
/*     */       }
/* 165 */       if (z < minZ) {
/* 166 */         minZ = z;
/*     */       }
/* 168 */       if (x > maxX) {
/* 169 */         maxX = x;
/*     */       }
/* 171 */       if (y > maxY) {
/* 172 */         maxY = y;
/*     */       }
/* 174 */       if (z > maxZ) {
/* 175 */         maxZ = z;
/*     */       }
/*     */     } 
/*     */     
/* 179 */     FaceInfo info = FaceInfo.fromFacing(direction);
/* 180 */     for (int vertex = 0; vertex < 4; vertex++) {
/* 181 */       FaceInfo.VertexInfo vertInfo = info.getVertexInfo(vertex);
/*     */       
/* 183 */       float newX = vertInfo.xFace().select(minX, minY, minZ, maxX, maxY, maxZ);
/* 184 */       float newY = vertInfo.yFace().select(minX, minY, minZ, maxX, maxY, maxZ);
/* 185 */       float newZ = vertInfo.zFace().select(minX, minY, minZ, maxX, maxY, maxZ);
/*     */       
/* 187 */       int vertexToSwap = findVertex(positions, vertex, newX, newY, newZ);
/* 188 */       if (vertexToSwap == -1)
/*     */       {
/* 190 */         throw new IllegalStateException("Can't find vertex to swap");
/*     */       }
/*     */       
/* 193 */       if (vertexToSwap != vertex) {
/* 194 */         swap(positions, vertexToSwap, vertex);
/* 195 */         swap(uvs, vertexToSwap, vertex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int findVertex(Vector3fc[] positions, int start, float x, float y, float z) {
/* 201 */     for (int i = start; i < 4; i++) {
/* 202 */       Vector3fc position = positions[i];
/*     */       
/* 204 */       if (x == position.x() && y == position.y() && z == position.z()) {
/* 205 */         return i;
/*     */       }
/*     */     } 
/* 208 */     return -1;
/*     */   }
/*     */   
/*     */   private static void swap(Vector3fc[] array, int indexA, int indexB) {
/* 212 */     Vector3fc tmp = array[indexA];
/* 213 */     array[indexA] = array[indexB];
/* 214 */     array[indexB] = tmp;
/*     */   }
/*     */   
/*     */   private static void swap(long[] array, int indexA, int indexB) {
/* 218 */     long tmp = array[indexA];
/* 219 */     array[indexA] = array[indexB];
/* 220 */     array[indexB] = tmp;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/FaceBakery.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */