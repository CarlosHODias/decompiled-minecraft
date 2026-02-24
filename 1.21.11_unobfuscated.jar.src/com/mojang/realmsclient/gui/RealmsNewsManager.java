/*    */ package com.mojang.realmsclient.gui;
/*    */ 
/*    */ import com.mojang.realmsclient.dto.RealmsNews;
/*    */ import com.mojang.realmsclient.util.RealmsPersistence;
/*    */ 
/*    */ public class RealmsNewsManager
/*    */ {
/*    */   private final RealmsPersistence newsLocalStorage;
/*    */   private boolean hasUnreadNews;
/*    */   private String newsLink;
/*    */   
/*    */   public RealmsNewsManager(RealmsPersistence newsLocalStorage) {
/* 13 */     this.newsLocalStorage = newsLocalStorage;
/* 14 */     RealmsPersistence.RealmsPersistenceData news = newsLocalStorage.read();
/* 15 */     this.hasUnreadNews = news.hasUnreadNews;
/* 16 */     this.newsLink = news.newsLink;
/*    */   }
/*    */   
/*    */   public boolean hasUnreadNews() {
/* 20 */     return this.hasUnreadNews;
/*    */   }
/*    */   
/*    */   public String newsLink() {
/* 24 */     return this.newsLink;
/*    */   }
/*    */   
/*    */   public void updateUnreadNews(RealmsNews newsResponse) {
/* 28 */     RealmsPersistence.RealmsPersistenceData news = updateNewsStorage(newsResponse);
/* 29 */     this.hasUnreadNews = news.hasUnreadNews;
/* 30 */     this.newsLink = news.newsLink;
/*    */   }
/*    */   
/*    */   private RealmsPersistence.RealmsPersistenceData updateNewsStorage(RealmsNews newsResponse) {
/* 34 */     RealmsPersistence.RealmsPersistenceData previousNews = this.newsLocalStorage.read();
/* 35 */     if (newsResponse.newsLink() == null || newsResponse.newsLink().equals(previousNews.newsLink)) {
/* 36 */       return previousNews;
/*    */     }
/*    */     
/* 39 */     RealmsPersistence.RealmsPersistenceData realmsNews = new RealmsPersistence.RealmsPersistenceData();
/* 40 */     realmsNews.newsLink = newsResponse.newsLink();
/* 41 */     realmsNews.hasUnreadNews = true;
/* 42 */     this.newsLocalStorage.save(realmsNews);
/* 43 */     return realmsNews;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/RealmsNewsManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */