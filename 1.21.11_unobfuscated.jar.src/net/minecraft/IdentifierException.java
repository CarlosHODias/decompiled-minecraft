/*    */ package net.minecraft;
/*    */ 
/*    */ import org.apache.commons.lang3.StringEscapeUtils;
/*    */ 
/*    */ public class IdentifierException extends RuntimeException {
/*    */   public IdentifierException(String message) {
/*  7 */     super(StringEscapeUtils.escapeJava(message));
/*    */   }
/*    */   
/*    */   public IdentifierException(String message, Throwable cause) {
/* 11 */     super(StringEscapeUtils.escapeJava(message), cause);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/IdentifierException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */