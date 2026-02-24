/*    */ package net.minecraft.client.resources.language;
/*    */ 
/*    */ import java.util.IllegalFormatException;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.locale.Language;
/*    */ 
/*    */ public class I18n
/*    */ {
/*  9 */   private static volatile Language language = Language.getInstance();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void setLanguage(Language locale) {
/* 16 */     language = locale;
/*    */   }
/*    */   
/*    */   public static String get(String id, Object... args) {
/* 20 */     String value = language.getOrDefault(id);
/*    */     try {
/* 22 */       return String.format(Locale.ROOT, value, args);
/* 23 */     } catch (IllegalFormatException ignored) {
/* 24 */       return "Format error: " + value;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static boolean exists(String id) {
/* 29 */     return language.has(id);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/language/I18n.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */