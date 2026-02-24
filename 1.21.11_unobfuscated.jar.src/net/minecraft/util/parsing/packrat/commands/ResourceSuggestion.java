/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.parsing.packrat.ParseState;
/*    */ import net.minecraft.util.parsing.packrat.SuggestionSupplier;
/*    */ 
/*    */ public interface ResourceSuggestion
/*    */   extends SuggestionSupplier<StringReader>
/*    */ {
/*    */   Stream<Identifier> possibleResources();
/*    */   
/*    */   default Stream<String> possibleValues(ParseState<StringReader> state) {
/* 15 */     return possibleResources().map(Identifier::toString);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/parsing/packrat/commands/ResourceSuggestion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */