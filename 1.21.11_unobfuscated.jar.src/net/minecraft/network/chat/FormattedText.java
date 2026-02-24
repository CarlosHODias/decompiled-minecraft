/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.Unit;
/*    */ 
/*    */ public interface FormattedText
/*    */ {
/* 10 */   public static final Optional<Unit> STOP_ITERATION = Optional.of(Unit.INSTANCE);
/*    */   
/* 12 */   public static final FormattedText EMPTY = new FormattedText()
/*    */     {
/*    */       public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 15 */         return Optional.empty();
/*    */       }
/*    */ 
/*    */       
/*    */       public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style parentStyle) {
/* 20 */         return Optional.empty();
/*    */       }
/*    */     };
/*    */   
/*    */   <T> Optional<T> visit(ContentConsumer<T> paramContentConsumer);
/*    */   
/*    */   <T> Optional<T> visit(StyledContentConsumer<T> paramStyledContentConsumer, Style paramStyle);
/*    */   
/*    */   static FormattedText of(final String text) {
/* 29 */     return new FormattedText()
/*    */       {
/*    */         public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 32 */           return output.accept(text);
/*    */         }
/*    */ 
/*    */         
/*    */         public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style parentStyle) {
/* 37 */           return output.accept(parentStyle, text);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static FormattedText of(final String text, final Style style) {
/* 43 */     return new FormattedText()
/*    */       {
/*    */         public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 46 */           return output.accept(text);
/*    */         }
/*    */ 
/*    */         
/*    */         public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style parentStyle) {
/* 51 */           return output.accept(style.applyTo(parentStyle), text);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static FormattedText composite(FormattedText... parts) {
/* 57 */     return composite((List<? extends FormattedText>)ImmutableList.copyOf((Object[])parts));
/*    */   }
/*    */   
/*    */   static FormattedText composite(final List<? extends FormattedText> parts) {
/* 61 */     return new FormattedText()
/*    */       {
/*    */         public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 64 */           for (FormattedText part : parts) {
/* 65 */             Optional<T> result = part.visit(output);
/* 66 */             if (result.isPresent()) {
/* 67 */               return result;
/*    */             }
/*    */           } 
/*    */           
/* 71 */           return Optional.empty();
/*    */         }
/*    */ 
/*    */         
/*    */         public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style parentStyle) {
/* 76 */           for (FormattedText part : parts) {
/* 77 */             Optional<T> result = part.visit(output, parentStyle);
/* 78 */             if (result.isPresent()) {
/* 79 */               return result;
/*    */             }
/*    */           } 
/*    */           
/* 83 */           return Optional.empty();
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   default String getString() {
/* 89 */     StringBuilder builder = new StringBuilder();
/*    */     
/* 91 */     visit(contents -> {
/*    */           builder.append(contents);
/*    */           
/*    */           return Optional.empty();
/*    */         });
/* 96 */     return builder.toString();
/*    */   }
/*    */   
/*    */   public static interface ContentConsumer<T> {
/*    */     Optional<T> accept(String param1String);
/*    */   }
/*    */   
/*    */   public static interface StyledContentConsumer<T> {
/*    */     Optional<T> accept(Style param1Style, String param1String);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/FormattedText.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */