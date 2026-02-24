/*    */ package net.minecraft.server.jsonrpc;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonNull;
/*    */ import com.google.gson.JsonObject;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum JsonRPCErrors
/*    */ {
/* 12 */   PARSE_ERROR(-32700, "Parse error"),
/* 13 */   INVALID_REQUEST(-32600, "Invalid Request"),
/* 14 */   METHOD_NOT_FOUND(-32601, "Method not found"),
/* 15 */   INVALID_PARAMS(-32602, "Invalid params"),
/* 16 */   INTERNAL_ERROR(-32603, "Internal error");
/*    */   
/*    */   private final int errorCode;
/*    */   
/*    */   private final String message;
/*    */   
/*    */   JsonRPCErrors(int errorCode, String message) {
/* 23 */     this.errorCode = errorCode;
/* 24 */     this.message = message;
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonObject createWithUnknownId(String data) {
/* 29 */     return JsonRPCUtils.createError((JsonElement)JsonNull.INSTANCE, this.message, this.errorCode, data);
/*    */   }
/*    */   
/*    */   public JsonObject createWithoutData(JsonElement id) {
/* 33 */     return JsonRPCUtils.createError(id, this.message, this.errorCode, null);
/*    */   }
/*    */   
/*    */   public JsonObject create(JsonElement id, String data) {
/* 37 */     return JsonRPCUtils.createError(id, this.message, this.errorCode, data);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/JsonRPCErrors.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */