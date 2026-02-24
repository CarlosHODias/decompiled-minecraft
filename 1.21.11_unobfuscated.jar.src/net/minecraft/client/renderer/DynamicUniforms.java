/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*    */ import com.mojang.blaze3d.buffers.Std140Builder;
/*    */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*    */ import java.nio.ByteBuffer;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ import org.joml.Vector4f;
/*    */ import org.joml.Vector4fc;
/*    */ 
/*    */ public class DynamicUniforms
/*    */   implements AutoCloseable {
/* 16 */   public static final int TRANSFORM_UBO_SIZE = new Std140SizeCalculator()
/* 17 */     .putMat4f()
/* 18 */     .putVec4()
/* 19 */     .putVec3()
/* 20 */     .putMat4f()
/* 21 */     .get();
/* 22 */   public static final int CHUNK_SECTION_UBO_SIZE = new Std140SizeCalculator()
/* 23 */     .putMat4f()
/* 24 */     .putFloat()
/* 25 */     .putIVec2()
/* 26 */     .putIVec3()
/* 27 */     .get();
/*    */ 
/*    */ 
/*    */   
/*    */   private static final int INITIAL_CAPACITY = 2;
/*    */ 
/*    */   
/* 34 */   private final DynamicUniformStorage<Transform> transforms = new DynamicUniformStorage<>("Dynamic Transforms UBO", TRANSFORM_UBO_SIZE, 2);
/* 35 */   private final DynamicUniformStorage<ChunkSectionInfo> chunkSections = new DynamicUniformStorage<>("Chunk Sections UBO", CHUNK_SECTION_UBO_SIZE, 2);
/*    */ 
/*    */   
/*    */   public void reset() {
/* 39 */     this.transforms.endFrame();
/* 40 */     this.chunkSections.endFrame();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 45 */     this.transforms.close();
/* 46 */     this.chunkSections.close();
/*    */   }
/*    */   
/*    */   public GpuBufferSlice writeTransform(Matrix4fc modelView, Vector4fc colorModulator, Vector3fc modelOffset, Matrix4fc textureMatrix) {
/* 50 */     return this.transforms.writeUniform(new Transform((Matrix4fc)new Matrix4f(modelView), (Vector4fc)new Vector4f(colorModulator), (Vector3fc)new Vector3f(modelOffset), (Matrix4fc)new Matrix4f(textureMatrix)));
/*    */   }
/*    */   
/*    */   public GpuBufferSlice[] writeTransforms(Transform... transforms) {
/* 54 */     return this.transforms.writeUniforms(transforms);
/*    */   }
/*    */   
/*    */   public GpuBufferSlice[] writeChunkSections(ChunkSectionInfo... infos) {
/* 58 */     return this.chunkSections.writeUniforms(infos);
/*    */   }
/*    */   public static final class Transform extends Record implements DynamicUniformStorage.DynamicUniform { private final Matrix4fc modelView; private final Vector4fc colorModulator; private final Vector3fc modelOffset; private final Matrix4fc textureMatrix;
/* 61 */     public Transform(Matrix4fc modelView, Vector4fc colorModulator, Vector3fc modelOffset, Matrix4fc textureMatrix) { this.modelView = modelView; this.colorModulator = colorModulator; this.modelOffset = modelOffset; this.textureMatrix = textureMatrix; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/DynamicUniforms$Transform;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 61 */       //   0	7	0	this	Lnet/minecraft/client/renderer/DynamicUniforms$Transform; } public Matrix4fc modelView() { return this.modelView; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/DynamicUniforms$Transform;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/DynamicUniforms$Transform; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/DynamicUniforms$Transform;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/DynamicUniforms$Transform;
/* 61 */       //   0	8	1	o	Ljava/lang/Object; } public Vector4fc colorModulator() { return this.colorModulator; } public Vector3fc modelOffset() { return this.modelOffset; } public Matrix4fc textureMatrix() { return this.textureMatrix; }
/*    */     
/*    */     public void write(ByteBuffer buffer) {
/* 64 */       Std140Builder.intoBuffer(buffer)
/* 65 */         .putMat4f(this.modelView)
/* 66 */         .putVec4(this.colorModulator)
/* 67 */         .putVec3(this.modelOffset)
/* 68 */         .putMat4f(this.textureMatrix);
/*    */     } }
/*    */   public static final class ChunkSectionInfo extends Record implements DynamicUniformStorage.DynamicUniform { private final Matrix4fc modelView; private final int x; private final int y; private final int z; private final float visibility; private final int textureAtlasWidth; private final int textureAtlasHeight;
/*    */     
/* 72 */     public ChunkSectionInfo(Matrix4fc modelView, int x, int y, int z, float visibility, int textureAtlasWidth, int textureAtlasHeight) { this.modelView = modelView; this.x = x; this.y = y; this.z = z; this.visibility = visibility; this.textureAtlasWidth = textureAtlasWidth; this.textureAtlasHeight = textureAtlasHeight; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/DynamicUniforms$ChunkSectionInfo;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #72	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/DynamicUniforms$ChunkSectionInfo; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/DynamicUniforms$ChunkSectionInfo;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #72	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/DynamicUniforms$ChunkSectionInfo; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/DynamicUniforms$ChunkSectionInfo;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #72	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/DynamicUniforms$ChunkSectionInfo;
/* 72 */       //   0	8	1	o	Ljava/lang/Object; } public Matrix4fc modelView() { return this.modelView; } public int x() { return this.x; } public int y() { return this.y; } public int z() { return this.z; } public float visibility() { return this.visibility; } public int textureAtlasWidth() { return this.textureAtlasWidth; } public int textureAtlasHeight() { return this.textureAtlasHeight; }
/*    */     
/*    */     public void write(ByteBuffer buffer) {
/* 75 */       Std140Builder.intoBuffer(buffer)
/* 76 */         .putMat4f(this.modelView)
/* 77 */         .putFloat(this.visibility)
/* 78 */         .putIVec2(this.textureAtlasWidth, this.textureAtlasHeight)
/* 79 */         .putIVec3(this.x, this.y, this.z);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/DynamicUniforms.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */