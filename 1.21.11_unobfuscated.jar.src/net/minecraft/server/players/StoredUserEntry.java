/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ 
/*    */ public abstract class StoredUserEntry<T>
/*    */ {
/*    */   private final T user;
/*    */   
/*    */   public StoredUserEntry(T user) {
/* 10 */     this.user = user;
/*    */   }
/*    */   
/*    */   public T getUser() {
/* 14 */     return this.user;
/*    */   }
/*    */   
/*    */   boolean hasExpired() {
/* 18 */     return false;
/*    */   }
/*    */   
/*    */   protected abstract void serialize(JsonObject paramJsonObject);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/players/StoredUserEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */