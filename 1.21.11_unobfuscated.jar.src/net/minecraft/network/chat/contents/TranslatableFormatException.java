/*    */ package net.minecraft.network.chat.contents;
/*    */ 
/*    */ import java.util.Locale;
/*    */ 
/*    */ public class TranslatableFormatException extends IllegalArgumentException {
/*    */   public TranslatableFormatException(TranslatableContents component, String message) {
/*  7 */     super(String.format(Locale.ROOT, "Error parsing: %s: %s", new Object[] { component, message }));
/*    */   }
/*    */   
/*    */   public TranslatableFormatException(TranslatableContents component, int index) {
/* 11 */     super(String.format(Locale.ROOT, "Invalid index %d requested for %s", new Object[] { index, component }));
/*    */   }
/*    */   
/*    */   public TranslatableFormatException(TranslatableContents component, Throwable t) {
/* 15 */     super(String.format(Locale.ROOT, "Error while parsing: %s", new Object[] { component }), t);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/contents/TranslatableFormatException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */