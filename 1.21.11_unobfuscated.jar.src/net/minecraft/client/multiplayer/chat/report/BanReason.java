/*    */ package net.minecraft.client.multiplayer.chat.report;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ 
/*    */ public enum BanReason
/*    */ {
/*  8 */   GENERIC_VIOLATION("generic_violation"),
/*  9 */   FALSE_REPORTING("false_reporting"),
/* 10 */   HATE_SPEECH("hate_speech"),
/* 11 */   HATE_TERRORISM_NOTORIOUS_FIGURE("hate_terrorism_notorious_figure"),
/* 12 */   HARASSMENT_OR_BULLYING("harassment_or_bullying"),
/* 13 */   DEFAMATION_IMPERSONATION_FALSE_INFORMATION("defamation_impersonation_false_information"),
/* 14 */   DRUGS("drugs"),
/* 15 */   FRAUD("fraud"),
/* 16 */   SPAM_OR_ADVERTISING("spam_or_advertising"),
/* 17 */   NUDITY_OR_PORNOGRAPHY("nudity_or_pornography"),
/* 18 */   SEXUALLY_INAPPROPRIATE("sexually_inappropriate"),
/* 19 */   EXTREME_VIOLENCE_OR_GORE("extreme_violence_or_gore"),
/* 20 */   IMMINENT_HARM_TO_PERSON_OR_PROPERTY("imminent_harm_to_person_or_property");
/*    */   
/*    */   private final Component title;
/*    */ 
/*    */   
/*    */   BanReason(String name) {
/* 26 */     this.title = (Component)Component.translatable("gui.banned.reason." + name);
/*    */   }
/*    */   
/*    */   public Component title() {
/* 30 */     return this.title;
/*    */   }
/*    */   
/*    */   public static BanReason byId(int id) {
/* 34 */     switch (id) { case 17: case 19: case 23: case 31: case 2: case 5: case 16: case 25: case 21: case 27: case 28: case 29: case 30: case 32: case 33: case 35: case 36: case 34: case 53: default: break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 50 */       null;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/chat/report/BanReason.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */