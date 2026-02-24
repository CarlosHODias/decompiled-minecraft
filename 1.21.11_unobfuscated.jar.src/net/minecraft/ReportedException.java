/*    */ package net.minecraft;
/*    */ 
/*    */ public class ReportedException extends RuntimeException {
/*    */   private final CrashReport report;
/*    */   
/*    */   public ReportedException(CrashReport report) {
/*  7 */     this.report = report;
/*    */   }
/*    */   
/*    */   public CrashReport getReport() {
/* 11 */     return this.report;
/*    */   }
/*    */ 
/*    */   
/*    */   public Throwable getCause() {
/* 16 */     return this.report.getException();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 21 */     return this.report.getTitle();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/ReportedException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */