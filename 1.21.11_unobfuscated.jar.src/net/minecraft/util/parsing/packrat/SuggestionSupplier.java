/*   */ package net.minecraft.util.parsing.packrat;
/*   */ 
/*   */ import java.util.stream.Stream;
/*   */ 
/*   */ public interface SuggestionSupplier<S> {
/*   */   Stream<String> possibleValues(ParseState<S> paramParseState);
/*   */   
/*   */   static <S> SuggestionSupplier<S> empty() {
/* 9 */     return state -> Stream.empty();
/*   */   }
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/parsing/packrat/SuggestionSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */