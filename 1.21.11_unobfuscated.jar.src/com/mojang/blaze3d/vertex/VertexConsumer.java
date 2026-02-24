/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import net.minecraft.client.model.geom.builders.UVPair;
/*     */ import net.minecraft.client.renderer.LightTexture;
/*     */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*     */ import net.minecraft.util.ARGB;
/*     */ import org.joml.Matrix3x2fc;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector2f;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public interface VertexConsumer {
/*     */   VertexConsumer addVertex(float paramFloat1, float paramFloat2, float paramFloat3);
/*     */   
/*     */   VertexConsumer setColor(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */   
/*     */   VertexConsumer setColor(int paramInt);
/*     */   
/*     */   VertexConsumer setUv(float paramFloat1, float paramFloat2);
/*     */   
/*     */   VertexConsumer setUv1(int paramInt1, int paramInt2);
/*     */   
/*     */   VertexConsumer setUv2(int paramInt1, int paramInt2);
/*     */   
/*     */   VertexConsumer setNormal(float paramFloat1, float paramFloat2, float paramFloat3);
/*     */   
/*     */   VertexConsumer setLineWidth(float paramFloat);
/*     */   
/*     */   default void addVertex(float x, float y, float z, int color, float u, float v, int overlayCoords, int lightCoords, float nx, float ny, float nz) {
/*  32 */     addVertex(x, y, z);
/*  33 */     setColor(color);
/*  34 */     setUv(u, v);
/*  35 */     setOverlay(overlayCoords);
/*  36 */     setLight(lightCoords);
/*  37 */     setNormal(nx, ny, nz);
/*     */   }
/*     */   
/*     */   default VertexConsumer setColor(float r, float g, float b, float a) {
/*  41 */     return setColor((int)(r * 255.0F), (int)(g * 255.0F), (int)(b * 255.0F), (int)(a * 255.0F));
/*     */   }
/*     */   
/*     */   default VertexConsumer setLight(int packedLightCoords) {
/*  45 */     return setUv2(packedLightCoords & 0xFFFF, packedLightCoords >> 16 & 0xFFFF);
/*     */   }
/*     */   
/*     */   default VertexConsumer setOverlay(int packedOverlayCoords) {
/*  49 */     return setUv1(packedOverlayCoords & 0xFFFF, packedOverlayCoords >> 16 & 0xFFFF);
/*     */   }
/*     */   
/*     */   default void putBulkData(PoseStack.Pose pose, BakedQuad quad, float r, float g, float b, float a, int lightCoords, int overlayCoords) {
/*  53 */     putBulkData(pose, quad, new float[] { 1.0F, 1.0F, 1.0F, 1.0F }, r, g, b, a, new int[] { lightCoords, lightCoords, lightCoords, lightCoords }, overlayCoords);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default void putBulkData(PoseStack.Pose pose, BakedQuad quad, float[] brightness, float r, float g, float b, float a, int[] lightmapCoord, int overlayCoords) {
/*  60 */     Vector3fc normalVec = quad.direction().getUnitVec3f();
/*     */     
/*  62 */     Matrix4f matrix = pose.pose();
/*  63 */     Vector3f normal = pose.transformNormal(normalVec, new Vector3f());
/*     */     
/*  65 */     int lightEmission = quad.lightEmission();
/*     */     
/*  67 */     for (int vertex = 0; vertex < 4; vertex++) {
/*  68 */       Vector3fc position = quad.position(vertex);
/*  69 */       long packedUv = quad.packedUV(vertex);
/*     */       
/*  71 */       float brightnessForVertex = brightness[vertex];
/*     */       
/*  73 */       int color = ARGB.colorFromFloat(a, brightnessForVertex * r, brightnessForVertex * g, brightnessForVertex * b);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  80 */       int light = LightTexture.lightCoordsWithEmission(lightmapCoord[vertex], lightEmission);
/*     */       
/*  82 */       Vector3f pos = matrix.transformPosition(position, new Vector3f());
/*  83 */       float u = UVPair.unpackU(packedUv);
/*  84 */       float v = UVPair.unpackV(packedUv);
/*  85 */       addVertex(pos.x(), pos.y(), pos.z(), color, u, v, overlayCoords, light, normal.x(), normal.y(), normal.z());
/*     */     } 
/*     */   }
/*     */   
/*     */   default VertexConsumer addVertex(Vector3fc position) {
/*  90 */     return addVertex(position.x(), position.y(), position.z());
/*     */   }
/*     */   
/*     */   default VertexConsumer addVertex(PoseStack.Pose pose, Vector3f position) {
/*  94 */     return addVertex(pose, position.x(), position.y(), position.z());
/*     */   }
/*     */   
/*     */   default VertexConsumer addVertex(PoseStack.Pose pose, float x, float y, float z) {
/*  98 */     return addVertex((Matrix4fc)pose.pose(), x, y, z);
/*     */   }
/*     */   
/*     */   default VertexConsumer addVertex(Matrix4fc pose, float x, float y, float z) {
/* 102 */     Vector3f pos = pose.transformPosition(x, y, z, new Vector3f());
/* 103 */     return addVertex(pos.x(), pos.y(), pos.z());
/*     */   }
/*     */   
/*     */   default VertexConsumer addVertexWith2DPose(Matrix3x2fc pose, float x, float y) {
/* 107 */     Vector2f pos = pose.transformPosition(x, y, new Vector2f());
/* 108 */     return addVertex(pos.x(), pos.y(), 0.0F);
/*     */   }
/*     */   
/*     */   default VertexConsumer setNormal(PoseStack.Pose pose, float x, float y, float z) {
/* 112 */     Vector3f normal = pose.transformNormal(x, y, z, new Vector3f());
/* 113 */     return setNormal(normal.x(), normal.y(), normal.z());
/*     */   }
/*     */   
/*     */   default VertexConsumer setNormal(PoseStack.Pose pose, Vector3f normal) {
/* 117 */     return setNormal(pose, normal.x(), normal.y(), normal.z());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/vertex/VertexConsumer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */