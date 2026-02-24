/*     */ package net.minecraft.client.renderer.block.model;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.math.Quadrant;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ 
/*     */ public final class BlockElementFace extends Record {
/*     */   private final Direction cullForDirection;
/*     */   private final int tintIndex;
/*     */   private final String texture;
/*     */   private final UVs uvs;
/*     */   private final Quadrant rotation;
/*     */   public static final int NO_TINT = -1;
/*     */   
/*  17 */   public BlockElementFace(Direction cullForDirection, int tintIndex, String texture, UVs uvs, Quadrant rotation) { this.cullForDirection = cullForDirection; this.tintIndex = tintIndex; this.texture = texture; this.uvs = uvs; this.rotation = rotation; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/BlockElementFace;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #17	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  17 */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockElementFace; } public Direction cullForDirection() { return this.cullForDirection; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/BlockElementFace;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #17	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockElementFace; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/BlockElementFace;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #17	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/BlockElementFace;
/*  17 */     //   0	8	1	o	Ljava/lang/Object; } public int tintIndex() { return this.tintIndex; } public String texture() { return this.texture; } public UVs uvs() { return this.uvs; } public Quadrant rotation() { return this.rotation; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getU(UVs uvs, Quadrant rotation, int vertex) {
/*  27 */     return uvs.getVertexU(rotation.rotateVertexIndex(vertex)) / 16.0F;
/*     */   }
/*     */   
/*     */   public static float getV(UVs uvs, Quadrant rotation, int index) {
/*  31 */     return uvs.getVertexV(rotation.rotateVertexIndex(index)) / 16.0F;
/*     */   }
/*     */   
/*     */   public static final class UVs extends Record {
/*     */     private final float minU;
/*     */     private final float minV;
/*     */     private final float maxU;
/*     */     private final float maxV;
/*     */     
/*  40 */     public UVs(float minU, float minV, float maxU, float maxV) { this.minU = minU; this.minV = minV; this.maxU = maxU; this.maxV = maxV; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/BlockElementFace$UVs;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #40	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockElementFace$UVs; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/BlockElementFace$UVs;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #40	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockElementFace$UVs; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/BlockElementFace$UVs;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #40	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/BlockElementFace$UVs;
/*  40 */       //   0	8	1	o	Ljava/lang/Object; } public float minU() { return this.minU; } public float minV() { return this.minV; } public float maxU() { return this.maxU; } public float maxV() { return this.maxV; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float getVertexU(int index) {
/*  48 */       return (index == 0 || index == 1) ? this.minU : this.maxU;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getVertexV(int index) {
/*  53 */       return (index == 0 || index == 3) ? this.minV : this.maxV;
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class Deserializer
/*     */     implements com.google.gson.JsonDeserializer<BlockElementFace> {
/*     */     private static final int DEFAULT_TINT_INDEX = -1;
/*     */     private static final int DEFAULT_ROTATION = 0;
/*     */     
/*     */     public BlockElementFace deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT, com.google.gson.JsonDeserializationContext context) throws com.google.gson.JsonParseException {
/*  63 */       JsonObject object = json.getAsJsonObject();
/*     */       
/*  65 */       Direction cullDirection = getCullFacing(object);
/*  66 */       int tintIndex = getTintIndex(object);
/*  67 */       String texture = getTexture(object);
/*     */       
/*  69 */       BlockElementFace.UVs uvs = getUVs(object);
/*  70 */       Quadrant rotation = getRotation(object);
/*     */       
/*  72 */       return new BlockElementFace(cullDirection, tintIndex, texture, uvs, rotation);
/*     */     }
/*     */     
/*     */     private static int getTintIndex(JsonObject object) {
/*  76 */       return GsonHelper.getAsInt(object, "tintindex", -1);
/*     */     }
/*     */     
/*     */     private static String getTexture(JsonObject object) {
/*  80 */       return GsonHelper.getAsString(object, "texture");
/*     */     }
/*     */     
/*     */     private static Direction getCullFacing(JsonObject object) {
/*  84 */       String cullFace = GsonHelper.getAsString(object, "cullface", "");
/*  85 */       return Direction.byName(cullFace);
/*     */     }
/*     */     
/*     */     private static Quadrant getRotation(JsonObject object) {
/*  89 */       int rotation = GsonHelper.getAsInt(object, "rotation", 0);
/*  90 */       return Quadrant.parseJson(rotation);
/*     */     }
/*     */     
/*     */     private static BlockElementFace.UVs getUVs(JsonObject object) {
/*  94 */       if (!object.has("uv")) {
/*  95 */         return null;
/*     */       }
/*     */       
/*  98 */       JsonArray uvArray = GsonHelper.getAsJsonArray(object, "uv");
/*  99 */       if (uvArray.size() != 4) {
/* 100 */         throw new com.google.gson.JsonParseException("Expected 4 uv values, found: " + uvArray.size());
/*     */       }
/*     */       
/* 103 */       float minU = GsonHelper.convertToFloat(uvArray.get(0), "minU");
/* 104 */       float minV = GsonHelper.convertToFloat(uvArray.get(1), "minV");
/* 105 */       float maxU = GsonHelper.convertToFloat(uvArray.get(2), "maxU");
/* 106 */       float maxV = GsonHelper.convertToFloat(uvArray.get(3), "maxV");
/* 107 */       return new BlockElementFace.UVs(minU, minV, maxU, maxV);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/BlockElementFace.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */