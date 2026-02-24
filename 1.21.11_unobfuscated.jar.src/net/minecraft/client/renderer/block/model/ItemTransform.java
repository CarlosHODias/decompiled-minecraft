/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.lang.reflect.Type;
/*    */ import net.minecraft.util.Mth;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public final class ItemTransform extends Record {
/*    */   private final Vector3fc rotation;
/*    */   private final Vector3fc translation;
/*    */   private final Vector3fc scale;
/*    */   
/* 19 */   public ItemTransform(Vector3fc rotation, Vector3fc translation, Vector3fc scale) { this.rotation = rotation; this.translation = translation; this.scale = scale; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/ItemTransform;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 19 */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/ItemTransform; } public Vector3fc rotation() { return this.rotation; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/ItemTransform;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/ItemTransform; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/ItemTransform;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/ItemTransform;
/* 19 */     //   0	8	1	o	Ljava/lang/Object; } public Vector3fc translation() { return this.translation; } public Vector3fc scale() { return this.scale; }
/* 20 */    public static final ItemTransform NO_TRANSFORM = new ItemTransform((Vector3fc)new Vector3f(), (Vector3fc)new Vector3f(), (Vector3fc)new Vector3f(1.0F, 1.0F, 1.0F));
/*    */   public void apply(boolean applyLeftHandFix, PoseStack.Pose pose) {
/*    */     float translationX, rotY, rotZ;
/* 23 */     if (this == NO_TRANSFORM) {
/* 24 */       pose.translate(-0.5F, -0.5F, -0.5F);
/*    */ 
/*    */ 
/*    */       
/*    */       return;
/*    */     } 
/*    */ 
/*    */     
/* 32 */     if (applyLeftHandFix) {
/*    */ 
/*    */ 
/*    */       
/* 36 */       translationX = -this.translation.x();
/* 37 */       rotY = -this.rotation.y();
/* 38 */       rotZ = -this.rotation.z();
/*    */     } else {
/* 40 */       translationX = this.translation.x();
/* 41 */       rotY = this.rotation.y();
/* 42 */       rotZ = this.rotation.z();
/*    */     } 
/*    */     
/* 45 */     pose.translate(translationX, this.translation.y(), this.translation.z());
/* 46 */     pose.rotate((org.joml.Quaternionfc)new org.joml.Quaternionf().rotationXYZ(this.rotation.x() * 0.017453292F, rotY * 0.017453292F, rotZ * 0.017453292F));
/* 47 */     pose.scale(this.scale.x(), this.scale.y(), this.scale.z());
/*    */     
/* 49 */     pose.translate(-0.5F, -0.5F, -0.5F);
/*    */   }
/*    */   
/*    */   protected static class Deserializer implements com.google.gson.JsonDeserializer<ItemTransform> {
/* 53 */     private static final Vector3f DEFAULT_ROTATION = new Vector3f(0.0F, 0.0F, 0.0F);
/* 54 */     private static final Vector3f DEFAULT_TRANSLATION = new Vector3f(0.0F, 0.0F, 0.0F);
/* 55 */     private static final Vector3f DEFAULT_SCALE = new Vector3f(1.0F, 1.0F, 1.0F);
/*    */     
/*    */     public static final float MAX_TRANSLATION = 5.0F;
/*    */     public static final float MAX_SCALE = 4.0F;
/*    */     
/*    */     public ItemTransform deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 61 */       JsonObject object = json.getAsJsonObject();
/*    */       
/* 63 */       Vector3f rotation = getVector3f(object, "rotation", DEFAULT_ROTATION);
/*    */       
/* 65 */       Vector3f translation = getVector3f(object, "translation", DEFAULT_TRANSLATION);
/* 66 */       translation.mul(0.0625F);
/* 67 */       translation.set(
/* 68 */           Mth.clamp(translation.x, -5.0F, 5.0F), 
/* 69 */           Mth.clamp(translation.y, -5.0F, 5.0F), 
/* 70 */           Mth.clamp(translation.z, -5.0F, 5.0F));
/*    */ 
/*    */       
/* 73 */       Vector3f scale = getVector3f(object, "scale", DEFAULT_SCALE);
/* 74 */       scale.set(
/* 75 */           Mth.clamp(scale.x, -4.0F, 4.0F), 
/* 76 */           Mth.clamp(scale.y, -4.0F, 4.0F), 
/* 77 */           Mth.clamp(scale.z, -4.0F, 4.0F));
/*    */ 
/*    */       
/* 80 */       return new ItemTransform((Vector3fc)rotation, (Vector3fc)translation, (Vector3fc)scale);
/*    */     }
/*    */     
/*    */     private Vector3f getVector3f(JsonObject object, String key, Vector3f def) {
/* 84 */       if (!object.has(key)) {
/* 85 */         return def;
/*    */       }
/* 87 */       JsonArray vecArray = net.minecraft.util.GsonHelper.getAsJsonArray(object, key);
/* 88 */       if (vecArray.size() != 3) {
/* 89 */         throw new JsonParseException("Expected 3 " + key + " values, found: " + vecArray.size());
/*    */       }
/*    */       
/* 92 */       float[] elements = new float[3];
/* 93 */       for (int i = 0; i < elements.length; i++) {
/* 94 */         elements[i] = net.minecraft.util.GsonHelper.convertToFloat(vecArray.get(i), key + "[" + key + "]");
/*    */       }
/* 96 */       return new Vector3f(elements[0], elements[1], elements[2]);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/ItemTransform.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */