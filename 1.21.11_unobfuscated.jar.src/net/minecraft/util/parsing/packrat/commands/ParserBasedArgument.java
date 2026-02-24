/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ public abstract class ParserBasedArgument<T>
/*    */   implements ArgumentType<T> {
/*    */   private final CommandArgumentParser<T> parser;
/*    */   
/*    */   public ParserBasedArgument(CommandArgumentParser<T> parser) {
/* 16 */     this.parser = parser;
/*    */   }
/*    */ 
/*    */   
/*    */   public T parse(StringReader reader) throws CommandSyntaxException {
/* 21 */     return this.parser.parseForCommands(reader);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 26 */     return this.parser.parseForSuggestions(builder);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/parsing/packrat/commands/ParserBasedArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */