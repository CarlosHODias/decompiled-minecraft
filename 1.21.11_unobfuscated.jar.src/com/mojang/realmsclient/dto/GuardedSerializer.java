/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.ExclusionStrategy;
/*    */ import com.google.gson.FieldAttributes;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.JsonElement;
/*    */ 
/*    */ public class GuardedSerializer
/*    */ {
/* 11 */   ExclusionStrategy strategy = new ExclusionStrategy(this)
/*    */     {
/*    */       public boolean shouldSkipClass(Class<?> clazz) {
/* 14 */         return false;
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean shouldSkipField(FieldAttributes field) {
/* 19 */         return (field.getAnnotation(Exclude.class) != null);
/*    */       }
/*    */     };
/*    */   
/* 23 */   private final Gson gson = new GsonBuilder()
/* 24 */     .addSerializationExclusionStrategy(this.strategy)
/* 25 */     .addDeserializationExclusionStrategy(this.strategy)
/* 26 */     .create();
/*    */   
/*    */   public String toJson(ReflectionBasedSerialization object) {
/* 29 */     return this.gson.toJson(object);
/*    */   }
/*    */   
/*    */   public String toJson(JsonElement jsonElement) {
/* 33 */     return this.gson.toJson(jsonElement);
/*    */   }
/*    */   
/*    */   public <T extends ReflectionBasedSerialization> T fromJson(String contents, Class<T> cls) {
/* 37 */     return (T)this.gson.fromJson(contents, cls);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/GuardedSerializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */