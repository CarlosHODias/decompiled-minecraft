/*    */ package net.minecraft.client.data.models.model;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class DelegatedModel implements ModelInstance {
/*    */   private final Identifier parent;
/*    */   
/*    */   public DelegatedModel(Identifier parent) {
/* 11 */     this.parent = parent;
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonElement get() {
/* 16 */     JsonObject result = new JsonObject();
/* 17 */     result.addProperty("parent", this.parent.toString());
/* 18 */     return (JsonElement)result;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/models/model/DelegatedModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */