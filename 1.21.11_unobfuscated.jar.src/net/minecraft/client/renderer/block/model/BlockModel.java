/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.resources.model.UnbakedGeometry;
/*    */ import net.minecraft.client.resources.model.UnbakedModel;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ public final class BlockModel extends Record implements UnbakedModel {
/*    */   private final UnbakedGeometry geometry;
/*    */   private final UnbakedModel.GuiLight guiLight;
/*    */   private final Boolean ambientOcclusion;
/*    */   private final ItemTransforms transforms;
/*    */   private final TextureSlots.Data textureSlots;
/*    */   private final Identifier parent;
/*    */   
/* 22 */   public BlockModel(UnbakedGeometry geometry, UnbakedModel.GuiLight guiLight, Boolean ambientOcclusion, ItemTransforms transforms, TextureSlots.Data textureSlots, Identifier parent) { this.geometry = geometry; this.guiLight = guiLight; this.ambientOcclusion = ambientOcclusion; this.transforms = transforms; this.textureSlots = textureSlots; this.parent = parent; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/BlockModel;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 22 */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockModel; } public UnbakedGeometry geometry() { return this.geometry; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/BlockModel;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockModel; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/BlockModel;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/BlockModel;
/* 22 */     //   0	8	1	o	Ljava/lang/Object; } public UnbakedModel.GuiLight guiLight() { return this.guiLight; } public Boolean ambientOcclusion() { return this.ambientOcclusion; } public ItemTransforms transforms() { return this.transforms; } public TextureSlots.Data textureSlots() { return this.textureSlots; } public Identifier parent() { return this.parent; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @com.google.common.annotations.VisibleForTesting
/* 31 */   static final com.google.gson.Gson GSON = new com.google.gson.GsonBuilder()
/* 32 */     .registerTypeAdapter(BlockModel.class, new Deserializer())
/* 33 */     .registerTypeAdapter(BlockElement.class, new BlockElement.Deserializer())
/* 34 */     .registerTypeAdapter(BlockElementFace.class, new BlockElementFace.Deserializer())
/* 35 */     .registerTypeAdapter(ItemTransform.class, new ItemTransform.Deserializer())
/* 36 */     .registerTypeAdapter(ItemTransforms.class, new ItemTransforms.Deserializer())
/* 37 */     .create();
/*    */   
/*    */   public static BlockModel fromStream(java.io.Reader reader) {
/* 40 */     return (BlockModel)GsonHelper.fromJson(GSON, reader, BlockModel.class);
/*    */   }
/*    */   
/*    */   public static class Deserializer
/*    */     implements com.google.gson.JsonDeserializer<BlockModel> {
/*    */     public BlockModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 46 */       JsonObject object = json.getAsJsonObject();
/*    */       
/* 48 */       UnbakedGeometry elements = getElements(context, object);
/* 49 */       String parentName = getParentName(object);
/*    */       
/* 51 */       TextureSlots.Data textureMap = getTextureMap(object);
/* 52 */       Boolean hasAmbientOcclusion = getAmbientOcclusion(object);
/*    */       
/* 54 */       ItemTransforms transforms = null;
/* 55 */       if (object.has("display")) {
/* 56 */         JsonObject display = GsonHelper.getAsJsonObject(object, "display");
/* 57 */         transforms = (ItemTransforms)context.deserialize((JsonElement)display, ItemTransforms.class);
/*    */       } 
/*    */       
/* 60 */       UnbakedModel.GuiLight guiLight = null;
/* 61 */       if (object.has("gui_light")) {
/* 62 */         guiLight = UnbakedModel.GuiLight.getByName(GsonHelper.getAsString(object, "gui_light"));
/*    */       }
/*    */       
/* 65 */       Identifier parentLocation = parentName.isEmpty() ? null : Identifier.parse(parentName);
/* 66 */       return new BlockModel(elements, guiLight, hasAmbientOcclusion, transforms, textureMap, parentLocation);
/*    */     }
/*    */     
/*    */     private TextureSlots.Data getTextureMap(JsonObject object) {
/* 70 */       if (object.has("textures")) {
/* 71 */         JsonObject texturesObject = GsonHelper.getAsJsonObject(object, "textures");
/* 72 */         return TextureSlots.parseTextureMap(texturesObject);
/*    */       } 
/* 74 */       return TextureSlots.Data.EMPTY;
/*    */     }
/*    */     
/*    */     private String getParentName(JsonObject object) {
/* 78 */       return GsonHelper.getAsString(object, "parent", "");
/*    */     }
/*    */     
/*    */     protected Boolean getAmbientOcclusion(JsonObject object) {
/* 82 */       if (object.has("ambientocclusion")) {
/* 83 */         return GsonHelper.getAsBoolean(object, "ambientocclusion");
/*    */       }
/* 85 */       return null;
/*    */     }
/*    */     
/*    */     protected UnbakedGeometry getElements(JsonDeserializationContext context, JsonObject object) {
/* 89 */       if (object.has("elements")) {
/* 90 */         List<BlockElement> elements = new java.util.ArrayList<>();
/* 91 */         for (JsonElement element : (Iterable<JsonElement>)GsonHelper.getAsJsonArray(object, "elements")) {
/* 92 */           elements.add((BlockElement)context.deserialize(element, BlockElement.class));
/*    */         }
/* 94 */         return new SimpleUnbakedGeometry(elements);
/*    */       } 
/* 96 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/BlockModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */