/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import net.minecraft.util.parsing.packrat.DelayedException;
/*    */ import net.minecraft.util.parsing.packrat.ParseState;
/*    */ import net.minecraft.util.parsing.packrat.Rule;
/*    */ 
/*    */ public final class GreedyPatternParseRule
/*    */   implements Rule<StringReader, String> {
/*    */   private final Pattern pattern;
/*    */   private final DelayedException<CommandSyntaxException> error;
/*    */   
/*    */   public GreedyPatternParseRule(Pattern pattern, DelayedException<CommandSyntaxException> error) {
/* 17 */     this.pattern = pattern;
/* 18 */     this.error = error;
/*    */   }
/*    */ 
/*    */   
/*    */   public String parse(ParseState<StringReader> state) {
/* 23 */     StringReader input = (StringReader)state.input();
/* 24 */     String fullString = input.getString();
/* 25 */     Matcher matcher = this.pattern.matcher(fullString).region(input.getCursor(), fullString.length());
/* 26 */     if (!matcher.lookingAt()) {
/* 27 */       state.errorCollector().store(state.mark(), this.error);
/* 28 */       return null;
/*    */     } 
/* 30 */     input.setCursor(matcher.end());
/* 31 */     return matcher.group(0);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/parsing/packrat/commands/GreedyPatternParseRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */