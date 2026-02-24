/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ 
/*    */ public class RemoteRpcErrorException
/*    */   extends RuntimeException {
/*    */   private final JsonElement id;
/*    */   private final JsonObject error;
/*    */   
/*    */   public RemoteRpcErrorException(JsonElement id, JsonObject error) {
/* 12 */     this.id = id;
/* 13 */     this.error = error;
/*    */   }
/*    */   
/*    */   private JsonObject getError() {
/* 17 */     return this.error;
/*    */   }
/*    */   
/*    */   private JsonElement getId() {
/* 21 */     return this.id;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/methods/RemoteRpcErrorException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */