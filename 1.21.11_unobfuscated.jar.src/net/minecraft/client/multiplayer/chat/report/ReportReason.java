/*    */ package net.minecraft.client.multiplayer.chat.report;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ 
/*    */ public enum ReportReason
/*    */ {
/* 10 */   I_WANT_TO_REPORT_THEM("i_want_to_report_them"),
/* 11 */   HATE_SPEECH("hate_speech"),
/* 12 */   HARASSMENT_OR_BULLYING("harassment_or_bullying"),
/* 13 */   SELF_HARM_OR_SUICIDE("self_harm_or_suicide"),
/* 14 */   IMMINENT_HARM("imminent_harm"),
/* 15 */   DEFAMATION_IMPERSONATION_FALSE_INFORMATION("defamation_impersonation_false_information"),
/* 16 */   ALCOHOL_TOBACCO_DRUGS("alcohol_tobacco_drugs"),
/* 17 */   CHILD_SEXUAL_EXPLOITATION_OR_ABUSE("child_sexual_exploitation_or_abuse"),
/* 18 */   TERRORISM_OR_VIOLENT_EXTREMISM("terrorism_or_violent_extremism"),
/* 19 */   NON_CONSENSUAL_INTIMATE_IMAGERY("non_consensual_intimate_imagery"),
/* 20 */   SEXUALLY_INAPPROPRIATE("sexually_inappropriate");
/*    */   
/*    */   private final String backendName;
/*    */   
/*    */   private final Component title;
/*    */   
/*    */   private final Component description;
/*    */   
/*    */   ReportReason(String name) {
/* 29 */     this.backendName = name.toUpperCase(Locale.ROOT);
/* 30 */     String translationKey = "gui.abuseReport.reason." + name;
/* 31 */     this.title = (Component)Component.translatable(translationKey);
/* 32 */     this.description = (Component)Component.translatable(translationKey + ".description");
/*    */   }
/*    */   
/*    */   public String backendName() {
/* 36 */     return this.backendName;
/*    */   }
/*    */   
/*    */   public Component title() {
/* 40 */     return this.title;
/*    */   }
/*    */   
/*    */   public Component description() {
/* 44 */     return this.description;
/*    */   }
/*    */   
/*    */   public static List<ReportReason> getIncompatibleCategories(ReportType reportType) {
/* 48 */     switch (reportType) { case CHAT: case SKIN: default: break; }  return 
/*    */ 
/*    */       
/* 51 */       List.of();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/chat/report/ReportReason.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */