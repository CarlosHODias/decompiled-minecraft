/*    */ package net.minecraft.client.renderer.block.model;
/*    */ public final class ItemTransforms extends Record { private final ItemTransform thirdPersonLeftHand;
/*    */   private final ItemTransform thirdPersonRightHand;
/*    */   private final ItemTransform firstPersonLeftHand;
/*    */   private final ItemTransform firstPersonRightHand;
/*    */   private final ItemTransform head;
/*    */   private final ItemTransform gui;
/*    */   private final ItemTransform ground;
/*    */   private final ItemTransform fixed;
/*    */   private final ItemTransform fixedFromBottom;
/*    */   
/* 12 */   public ItemTransforms(ItemTransform thirdPersonLeftHand, ItemTransform thirdPersonRightHand, ItemTransform firstPersonLeftHand, ItemTransform firstPersonRightHand, ItemTransform head, ItemTransform gui, ItemTransform ground, ItemTransform fixed, ItemTransform fixedFromBottom) { this.thirdPersonLeftHand = thirdPersonLeftHand; this.thirdPersonRightHand = thirdPersonRightHand; this.firstPersonLeftHand = firstPersonLeftHand; this.firstPersonRightHand = firstPersonRightHand; this.head = head; this.gui = gui; this.ground = ground; this.fixed = fixed; this.fixedFromBottom = fixedFromBottom; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/ItemTransforms;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/ItemTransforms; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/ItemTransforms;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/ItemTransforms; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/ItemTransforms;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/ItemTransforms;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public ItemTransform thirdPersonLeftHand() { return this.thirdPersonLeftHand; } public ItemTransform thirdPersonRightHand() { return this.thirdPersonRightHand; } public ItemTransform firstPersonLeftHand() { return this.firstPersonLeftHand; } public ItemTransform firstPersonRightHand() { return this.firstPersonRightHand; } public ItemTransform head() { return this.head; } public ItemTransform gui() { return this.gui; } public ItemTransform ground() { return this.ground; } public ItemTransform fixed() { return this.fixed; } public ItemTransform fixedFromBottom() { return this.fixedFromBottom; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static final ItemTransforms NO_TRANSFORMS = new ItemTransforms(ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM);
/*    */   
/*    */   public ItemTransform getTransform(net.minecraft.world.item.ItemDisplayContext type) {
/* 27 */     switch (type) { case THIRD_PERSON_LEFT_HAND: case THIRD_PERSON_RIGHT_HAND: case FIRST_PERSON_LEFT_HAND: case FIRST_PERSON_RIGHT_HAND: case HEAD: case GUI: case GROUND: case FIXED: case ON_SHELF: default: break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 37 */       ItemTransform.NO_TRANSFORM;
/*    */   }
/*    */   
/*    */   protected static class Deserializer
/*    */     implements com.google.gson.JsonDeserializer<ItemTransforms>
/*    */   {
/*    */     public ItemTransforms deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT, com.google.gson.JsonDeserializationContext context) throws com.google.gson.JsonParseException {
/* 44 */       com.google.gson.JsonObject object = json.getAsJsonObject();
/*    */       
/* 46 */       ItemTransform thirdPersonRightHand = getTransform(context, object, net.minecraft.world.item.ItemDisplayContext.THIRD_PERSON_RIGHT_HAND);
/* 47 */       ItemTransform thirdPersonLeftHand = getTransform(context, object, net.minecraft.world.item.ItemDisplayContext.THIRD_PERSON_LEFT_HAND);
/* 48 */       if (thirdPersonLeftHand == ItemTransform.NO_TRANSFORM) {
/* 49 */         thirdPersonLeftHand = thirdPersonRightHand;
/*    */       }
/* 51 */       ItemTransform firstPersonRightHand = getTransform(context, object, net.minecraft.world.item.ItemDisplayContext.FIRST_PERSON_RIGHT_HAND);
/* 52 */       ItemTransform firstPersonLeftHand = getTransform(context, object, net.minecraft.world.item.ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
/* 53 */       if (firstPersonLeftHand == ItemTransform.NO_TRANSFORM) {
/* 54 */         firstPersonLeftHand = firstPersonRightHand;
/*    */       }
/* 56 */       ItemTransform head = getTransform(context, object, net.minecraft.world.item.ItemDisplayContext.HEAD);
/* 57 */       ItemTransform gui = getTransform(context, object, net.minecraft.world.item.ItemDisplayContext.GUI);
/* 58 */       ItemTransform ground = getTransform(context, object, net.minecraft.world.item.ItemDisplayContext.GROUND);
/* 59 */       ItemTransform fixed = getTransform(context, object, net.minecraft.world.item.ItemDisplayContext.FIXED);
/* 60 */       ItemTransform fixedFromBottom = getTransform(context, object, net.minecraft.world.item.ItemDisplayContext.ON_SHELF);
/*    */       
/* 62 */       return new ItemTransforms(thirdPersonLeftHand, thirdPersonRightHand, firstPersonLeftHand, firstPersonRightHand, head, gui, ground, fixed, fixedFromBottom);
/*    */     }
/*    */     
/*    */     private ItemTransform getTransform(com.google.gson.JsonDeserializationContext context, com.google.gson.JsonObject object, net.minecraft.world.item.ItemDisplayContext transform) {
/* 66 */       String name = transform.getSerializedName();
/* 67 */       if (object.has(name)) {
/* 68 */         return (ItemTransform)context.deserialize(object.get(name), ItemTransform.class);
/*    */       }
/* 70 */       return ItemTransform.NO_TRANSFORM;
/*    */     }
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/ItemTransforms.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */