/*    */ package net.minecraft.client.multiplayer.chat.report;
/*    */ 
/*    */ import java.util.Locale;
/*    */ 
/*    */ public enum ReportType {
/*  6 */   CHAT("chat"),
/*  7 */   SKIN("skin"),
/*  8 */   USERNAME("username");
/*    */   
/*    */   private final String backendName;
/*    */   
/*    */   ReportType(String name) {
/* 13 */     this.backendName = name.toUpperCase(Locale.ROOT);
/*    */   }
/*    */   
/*    */   public String backendName() {
/* 17 */     return this.backendName;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/chat/report/ReportType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */