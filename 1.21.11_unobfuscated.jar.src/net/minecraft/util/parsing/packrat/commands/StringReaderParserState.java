/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import net.minecraft.util.parsing.packrat.CachedParseState;
/*    */ import net.minecraft.util.parsing.packrat.ErrorCollector;
/*    */ 
/*    */ public class StringReaderParserState extends CachedParseState<StringReader> {
/*    */   private final StringReader input;
/*    */   
/*    */   public StringReaderParserState(ErrorCollector<StringReader> errorCollector, StringReader input) {
/* 11 */     super(errorCollector);
/* 12 */     this.input = input;
/*    */   }
/*    */ 
/*    */   
/*    */   public StringReader input() {
/* 17 */     return this.input;
/*    */   }
/*    */ 
/*    */   
/*    */   public int mark() {
/* 22 */     return this.input.getCursor();
/*    */   }
/*    */ 
/*    */   
/*    */   public void restore(int mark) {
/* 27 */     this.input.setCursor(mark);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/parsing/packrat/commands/StringReaderParserState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */