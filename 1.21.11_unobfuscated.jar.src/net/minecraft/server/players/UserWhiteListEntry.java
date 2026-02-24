/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ 
/*    */ public class UserWhiteListEntry extends StoredUserEntry<NameAndId> {
/*    */   public UserWhiteListEntry(NameAndId user) {
/*  7 */     super(user);
/*    */   }
/*    */   
/*    */   public UserWhiteListEntry(JsonObject object) {
/* 11 */     super(NameAndId.fromJson(object));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void serialize(JsonObject object) {
/* 16 */     if (getUser() == null) {
/*    */       return;
/*    */     }
/* 19 */     getUser().appendTo(object);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/players/UserWhiteListEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */