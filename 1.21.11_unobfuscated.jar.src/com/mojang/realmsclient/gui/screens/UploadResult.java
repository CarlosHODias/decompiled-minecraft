/*    */ package com.mojang.realmsclient.gui.screens;
/*    */ 
/*    */ public final class UploadResult extends Record { private final int statusCode; private final String errorMessage; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/gui/screens/UploadResult;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/gui/screens/UploadResult; } public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/gui/screens/UploadResult;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/gui/screens/UploadResult;
/*  5 */   } public UploadResult(int statusCode, String errorMessage) { this.statusCode = statusCode; this.errorMessage = errorMessage; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/gui/screens/UploadResult;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/gui/screens/UploadResult;
/*  5 */     //   0	8	1	o	Ljava/lang/Object; } public int statusCode() { return this.statusCode; } public String errorMessage() { return this.errorMessage; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSimplifiedErrorMessage() {
/* 11 */     if (this.statusCode < 200 || this.statusCode >= 300) {
/* 12 */       if (this.statusCode == 400 && this.errorMessage != null) {
/* 13 */         return this.errorMessage;
/*    */       }
/* 15 */       return String.valueOf(this.statusCode);
/*    */     } 
/* 17 */     return null;
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 21 */     private int statusCode = -1;
/*    */     private String errorMessage;
/*    */     
/*    */     public Builder withStatusCode(int statusCode) {
/* 25 */       this.statusCode = statusCode;
/* 26 */       return this;
/*    */     }
/*    */     
/*    */     public Builder withErrorMessage(String errorMessage) {
/* 30 */       this.errorMessage = errorMessage;
/* 31 */       return this;
/*    */     }
/*    */     
/*    */     public UploadResult build() {
/* 35 */       return new UploadResult(this.statusCode, this.errorMessage);
/*    */     }
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/UploadResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */