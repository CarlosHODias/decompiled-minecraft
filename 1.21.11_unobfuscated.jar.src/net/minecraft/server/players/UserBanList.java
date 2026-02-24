/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.io.File;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.server.notifications.NotificationService;
/*    */ 
/*    */ public class UserBanList
/*    */   extends StoredUserList<NameAndId, UserBanListEntry> {
/*    */   public UserBanList(File file, NotificationService notificationService) {
/* 11 */     super(file, notificationService);
/*    */   }
/*    */ 
/*    */   
/*    */   protected StoredUserEntry<NameAndId> createEntry(JsonObject object) {
/* 16 */     return new UserBanListEntry(object);
/*    */   }
/*    */   
/*    */   public boolean isBanned(NameAndId user) {
/* 20 */     return contains(user);
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getUserList() {
/* 25 */     return (String[])getEntries().stream().map(StoredUserEntry::getUser).filter(Objects::nonNull).map(NameAndId::name).toArray(x$0 -> new String[x$0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getKeyForUser(NameAndId user) {
/* 30 */     return user.id().toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(UserBanListEntry infos) {
/* 35 */     if (super.add(infos)) {
/* 36 */       if (infos.getUser() != null) {
/* 37 */         this.notificationService.playerBanned(infos);
/*    */       }
/* 39 */       return true;
/*    */     } 
/* 41 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(NameAndId user) {
/* 46 */     if (super.remove(user)) {
/* 47 */       this.notificationService.playerUnbanned(user);
/* 48 */       return true;
/*    */     } 
/* 50 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 55 */     for (UserBanListEntry user : getEntries()) {
/* 56 */       if (user.getUser() == null) {
/*    */         continue;
/*    */       }
/* 59 */       this.notificationService.playerUnbanned(user.getUser());
/*    */     } 
/* 61 */     super.clear();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/players/UserBanList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */