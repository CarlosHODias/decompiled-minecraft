/*    */ package net.minecraft.network.chat.contents;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.Style;
/*    */ 
/*    */ public interface PlainTextContents extends net.minecraft.network.chat.ComponentContents {
/*    */   public static final MapCodec<PlainTextContents> MAP_CODEC;
/*    */   
/*    */   static {
/* 13 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.STRING.fieldOf("text").forGetter(PlainTextContents::text)).apply((com.mojang.datafixers.kinds.Applicative)i, PlainTextContents::create));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public static final PlainTextContents EMPTY = new PlainTextContents()
/*    */     {
/*    */       public String toString() {
/* 25 */         return "empty";
/*    */       }
/*    */ 
/*    */       
/*    */       public String text() {
/* 30 */         return "";
/*    */       }
/*    */     };
/*    */   public static final class LiteralContents extends Record implements PlainTextContents { private final String text;
/* 34 */     public LiteralContents(String text) { this.text = text; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/PlainTextContents$LiteralContents;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #34	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 34 */       //   0	7	0	this	Lnet/minecraft/network/chat/contents/PlainTextContents$LiteralContents; } public String text() { return this.text; } public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/PlainTextContents$LiteralContents;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #34	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/contents/PlainTextContents$LiteralContents;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     } public <T> java.util.Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 37 */       return output.accept(this.text);
/*    */     }
/*    */ 
/*    */     
/*    */     public <T> java.util.Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style currentStyle) {
/* 42 */       return output.accept(currentStyle, this.text);
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 47 */       return "literal{" + this.text + "}";
/*    */     } }
/*    */ 
/*    */   
/*    */   static PlainTextContents create(String text) {
/* 52 */     return text.isEmpty() ? EMPTY : new LiteralContents(text);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default MapCodec<PlainTextContents> codec() {
/* 59 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   String text();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/contents/PlainTextContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */