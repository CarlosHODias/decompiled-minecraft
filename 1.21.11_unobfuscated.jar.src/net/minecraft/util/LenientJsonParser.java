/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonIOException;
/*    */ import com.google.gson.JsonParser;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import java.io.Reader;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LenientJsonParser
/*    */ {
/*    */   public static JsonElement parse(Reader reader) throws JsonIOException, JsonSyntaxException {
/* 18 */     return JsonParser.parseReader(reader);
/*    */   }
/*    */   
/*    */   public static JsonElement parse(String json) throws JsonSyntaxException {
/* 22 */     return JsonParser.parseString(json);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/LenientJsonParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */