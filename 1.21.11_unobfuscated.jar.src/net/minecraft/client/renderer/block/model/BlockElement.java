/*     */ package net.minecraft.client.renderer.block.model;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public final class BlockElement extends Record {
/*     */   private final Vector3fc from;
/*     */   private final Vector3fc to;
/*     */   private final Map<Direction, BlockElementFace> faces;
/*     */   private final BlockElementRotation rotation;
/*     */   
/*  21 */   public int lightEmission() { return this.lightEmission; } private final boolean shade; private final int lightEmission; private static final boolean DEFAULT_RESCALE = false; private static final float MIN_EXTENT = -16.0F; private static final float MAX_EXTENT = 32.0F; public boolean shade() { return this.shade; } public BlockElementRotation rotation() { return this.rotation; } public Map<Direction, BlockElementFace> faces() { return this.faces; } public Vector3fc to() { return this.to; } public Vector3fc from() { return this.from; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/BlockElement;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #21	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/BlockElement;
/*  21 */     //   0	8	1	o	Ljava/lang/Object; } public BlockElement(Vector3fc from, Vector3fc to, Map<Direction, BlockElementFace> faces, BlockElementRotation rotation, boolean shade, int lightEmission) { this.from = from; this.to = to; this.faces = faces; this.rotation = rotation; this.shade = shade; this.lightEmission = lightEmission; }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/BlockElement;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #21	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockElement;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/BlockElement;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #21	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockElement;
/*     */   }
/*     */   
/*     */   public BlockElement(Vector3fc from, Vector3fc to, Map<Direction, BlockElementFace> faces) {
/*  34 */     this(from, to, faces, null, true, 0);
/*     */   }
/*     */   
/*     */   protected static class Deserializer
/*     */     implements com.google.gson.JsonDeserializer<BlockElement>
/*     */   {
/*     */     private static final boolean DEFAULT_SHADE = true;
/*     */     private static final int DEFAULT_LIGHT_EMISSION = 0;
/*     */     private static final String FIELD_SHADE = "shade";
/*     */     private static final String FIELD_LIGHT_EMISSION = "light_emission";
/*     */     private static final String FIELD_ROTATION = "rotation";
/*     */     private static final String FIELD_ORIGIN = "origin";
/*     */     private static final String FIELD_ANGLE = "angle";
/*     */     private static final String FIELD_X = "x";
/*     */     private static final String FIELD_Y = "y";
/*     */     private static final String FIELD_Z = "z";
/*     */     private static final String FIELD_AXIS = "axis";
/*     */     private static final String FIELD_RESCALE = "rescale";
/*     */     private static final String FIELD_FACES = "faces";
/*     */     private static final String FIELD_TO = "to";
/*     */     private static final String FIELD_FROM = "from";
/*     */     
/*     */     public BlockElement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/*  57 */       JsonObject object = json.getAsJsonObject();
/*  58 */       Vector3f from = getPosition(object, "from");
/*  59 */       Vector3f to = getPosition(object, "to");
/*  60 */       BlockElementRotation rotation = getRotation(object);
/*  61 */       Map<Direction, BlockElementFace> faces = getFaces(context, object);
/*  62 */       if (object.has("shade") && !GsonHelper.isBooleanValue(object, "shade")) {
/*  63 */         throw new JsonParseException("Expected 'shade' to be a Boolean");
/*     */       }
/*  65 */       boolean shade = GsonHelper.getAsBoolean(object, "shade", true);
/*  66 */       int lightEmission = 0;
/*  67 */       if (object.has("light_emission")) {
/*  68 */         boolean isNumber = GsonHelper.isNumberValue(object, "light_emission");
/*  69 */         if (isNumber) {
/*  70 */           lightEmission = GsonHelper.getAsInt(object, "light_emission");
/*     */         }
/*  72 */         if (!isNumber || lightEmission < 0 || lightEmission > 15) {
/*  73 */           throw new JsonParseException("Expected 'light_emission' to be an Integer between (inclusive) 0 and 15");
/*     */         }
/*     */       } 
/*     */       
/*  77 */       return new BlockElement((Vector3fc)from, (Vector3fc)to, faces, rotation, shade, lightEmission);
/*     */     }
/*     */     
/*     */     private BlockElementRotation getRotation(JsonObject object) {
/*  81 */       if (object.has("rotation")) {
/*  82 */         BlockElementRotation.RotationValue rotationValue; JsonObject rotationObject = GsonHelper.getAsJsonObject(object, "rotation");
/*  83 */         Vector3f origin = getVector3f(rotationObject, "origin");
/*  84 */         origin.mul(0.0625F);
/*     */ 
/*     */         
/*  87 */         if (rotationObject.has("axis") || rotationObject.has("angle")) {
/*  88 */           Direction.Axis axis = getAxis(rotationObject);
/*  89 */           float angle = GsonHelper.getAsFloat(rotationObject, "angle");
/*  90 */           rotationValue = new BlockElementRotation.SingleAxisRotation(axis, angle);
/*  91 */         } else if (rotationObject.has("x") || rotationObject.has("y") || rotationObject.has("z")) {
/*  92 */           float x = GsonHelper.getAsFloat(rotationObject, "x", 0.0F);
/*  93 */           float y = GsonHelper.getAsFloat(rotationObject, "y", 0.0F);
/*  94 */           float z = GsonHelper.getAsFloat(rotationObject, "z", 0.0F);
/*  95 */           rotationValue = new BlockElementRotation.EulerXYZRotation(x, y, z);
/*     */         } else {
/*  97 */           throw new JsonParseException("Missing rotation value, expected either 'axis' and 'angle' or 'x', 'y' and 'z'");
/*     */         } 
/*  99 */         boolean rescale = GsonHelper.getAsBoolean(rotationObject, "rescale", false);
/*     */         
/* 101 */         return new BlockElementRotation((Vector3fc)origin, rotationValue, rescale);
/*     */       } 
/* 103 */       return null;
/*     */     }
/*     */     
/*     */     private Direction.Axis getAxis(JsonObject object) {
/* 107 */       String axisName = GsonHelper.getAsString(object, "axis");
/* 108 */       Direction.Axis axis = Direction.Axis.byName(axisName.toLowerCase(java.util.Locale.ROOT));
/* 109 */       if (axis == null) {
/* 110 */         throw new JsonParseException("Invalid rotation axis: " + axisName);
/*     */       }
/* 112 */       return axis;
/*     */     }
/*     */     
/*     */     private Map<Direction, BlockElementFace> getFaces(JsonDeserializationContext context, JsonObject object) {
/* 116 */       Map<Direction, BlockElementFace> faces = filterNullFromFaces(context, object);
/*     */       
/* 118 */       if (faces.isEmpty()) {
/* 119 */         throw new JsonParseException("Expected between 1 and 6 unique faces, got 0");
/*     */       }
/*     */       
/* 122 */       return faces;
/*     */     }
/*     */     
/*     */     private Map<Direction, BlockElementFace> filterNullFromFaces(JsonDeserializationContext context, JsonObject object) {
/* 126 */       Map<Direction, BlockElementFace> result = com.google.common.collect.Maps.newEnumMap(Direction.class);
/* 127 */       JsonObject faceObjects = GsonHelper.getAsJsonObject(object, "faces");
/* 128 */       for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)faceObjects.entrySet()) {
/* 129 */         Direction direction = getFacing(entry.getKey());
/* 130 */         result.put(direction, (BlockElementFace)context.deserialize(entry.getValue(), BlockElementFace.class));
/*     */       } 
/* 132 */       return result;
/*     */     }
/*     */     
/*     */     private Direction getFacing(String name) {
/* 136 */       Direction direction = Direction.byName(name);
/* 137 */       if (direction == null) {
/* 138 */         throw new JsonParseException("Unknown facing: " + name);
/*     */       }
/* 140 */       return direction;
/*     */     }
/*     */     
/*     */     private static Vector3f getPosition(JsonObject object, String key) {
/* 144 */       Vector3f from = getVector3f(object, key);
/* 145 */       if (from.x() < -16.0F || from.y() < -16.0F || from.z() < -16.0F || 
/* 146 */         from.x() > 32.0F || from.y() > 32.0F || from.z() > 32.0F)
/*     */       {
/* 148 */         throw new JsonParseException("'" + key + "' specifier exceeds the allowed boundaries: " + String.valueOf(from));
/*     */       }
/* 150 */       return from;
/*     */     }
/*     */     
/*     */     private static Vector3f getVector3f(JsonObject object, String key) {
/* 154 */       JsonArray vecArray = GsonHelper.getAsJsonArray(object, key);
/* 155 */       if (vecArray.size() != 3) {
/* 156 */         throw new JsonParseException("Expected 3 " + key + " values, found: " + vecArray.size());
/*     */       }
/*     */       
/* 159 */       float[] elements = new float[3];
/* 160 */       for (int i = 0; i < elements.length; i++) {
/* 161 */         elements[i] = GsonHelper.convertToFloat(vecArray.get(i), key + "[" + key + "]");
/*     */       }
/* 163 */       return new Vector3f(elements[0], elements[1], elements[2]);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/BlockElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */