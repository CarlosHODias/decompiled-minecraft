/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.io.File;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.server.notifications.NotificationService;
/*    */ 
/*    */ public class UserWhiteList
/*    */   extends StoredUserList<NameAndId, UserWhiteListEntry>
/*    */ {
/*    */   public UserWhiteList(File file, NotificationService notificationService) {
/* 12 */     super(file, notificationService);
/*    */   }
/*    */ 
/*    */   
/*    */   protected StoredUserEntry<NameAndId> createEntry(JsonObject object) {
/* 17 */     return new UserWhiteListEntry(object);
/*    */   }
/*    */   
/*    */   public boolean isWhiteListed(NameAndId user) {
/* 21 */     return contains(user);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(UserWhiteListEntry infos) {
/* 26 */     if (super.add(infos)) {
/* 27 */       if (infos.getUser() != null) {
/* 28 */         this.notificationService.playerAddedToAllowlist(infos.getUser());
/*    */       }
/* 30 */       return true;
/*    */     } 
/* 32 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(NameAndId user) {
/* 37 */     if (super.remove(user)) {
/* 38 */       this.notificationService.playerRemovedFromAllowlist(user);
/* 39 */       return true;
/*    */     } 
/* 41 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 46 */     for (UserWhiteListEntry user : getEntries()) {
/* 47 */       if (user.getUser() == null) {
/*    */         continue;
/*    */       }
/* 50 */       this.notificationService.playerRemovedFromAllowlist(user.getUser());
/*    */     } 
/* 52 */     super.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getUserList() {
/* 57 */     return (String[])getEntries().stream().map(StoredUserEntry::getUser).filter(Objects::nonNull).map(NameAndId::name).toArray(x$0 -> new String[x$0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getKeyForUser(NameAndId user) {
/* 62 */     return user.id().toString();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/players/UserWhiteList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */