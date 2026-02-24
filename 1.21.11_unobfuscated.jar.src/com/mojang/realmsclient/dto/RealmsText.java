/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.realmsclient.util.JsonUtils;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.resources.language.I18n;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RealmsText
/*    */ {
/*    */   private static final String TRANSLATION_KEY = "translationKey";
/*    */   private static final String ARGS = "args";
/*    */   private final String translationKey;
/*    */   private final String[] args;
/*    */   
/*    */   private RealmsText(String translationKey, String[] args) {
/* 21 */     this.translationKey = translationKey;
/* 22 */     this.args = args;
/*    */   }
/*    */   
/*    */   public Component createComponent(Component fallback) {
/* 26 */     return Objects.<Component>requireNonNullElse(createComponent(), fallback);
/*    */   }
/*    */   
/*    */   public Component createComponent() {
/* 30 */     if (!I18n.exists(this.translationKey)) {
/* 31 */       return null;
/*    */     }
/* 33 */     if (this.args == null) {
/* 34 */       return (Component)Component.translatable(this.translationKey);
/*    */     }
/* 36 */     return (Component)Component.translatable(this.translationKey, (Object[])this.args);
/*    */   }
/*    */   
/*    */   public static RealmsText parse(JsonObject jsonObject) {
/*    */     String[] args;
/* 41 */     String translationKey = JsonUtils.getRequiredString("translationKey", jsonObject);
/* 42 */     JsonElement argsJsonElement = jsonObject.get("args");
/*    */     
/* 44 */     if (argsJsonElement == null || argsJsonElement.isJsonNull()) {
/* 45 */       args = null;
/*    */     } else {
/* 47 */       JsonArray argsJsonArray = argsJsonElement.getAsJsonArray();
/* 48 */       args = new String[argsJsonArray.size()];
/* 49 */       for (int i = 0; i < argsJsonArray.size(); i++) {
/* 50 */         args[i] = argsJsonArray.get(i).getAsString();
/*    */       }
/*    */     } 
/* 53 */     return new RealmsText(translationKey, args);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 58 */     return this.translationKey;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/RealmsText.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */