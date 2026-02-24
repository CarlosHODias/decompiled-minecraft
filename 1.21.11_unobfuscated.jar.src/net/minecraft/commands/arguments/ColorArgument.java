/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class ColorArgument implements ArgumentType<ChatFormatting> {
/* 20 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "red", "green" }); static {
/* 21 */     ERROR_INVALID_VALUE = new DynamicCommandExceptionType(value -> Component.translatableEscape("argument.color.invalid", new Object[] { value }));
/*    */   }
/*    */   
/*    */   public static final DynamicCommandExceptionType ERROR_INVALID_VALUE;
/*    */   
/*    */   public static ColorArgument color() {
/* 27 */     return new ColorArgument();
/*    */   }
/*    */   
/*    */   public static ChatFormatting getColor(CommandContext<CommandSourceStack> context, String name) {
/* 31 */     return (ChatFormatting)context.getArgument(name, ChatFormatting.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public ChatFormatting parse(StringReader reader) throws CommandSyntaxException {
/* 36 */     String id = reader.readUnquotedString();
/* 37 */     ChatFormatting result = ChatFormatting.getByName(id);
/* 38 */     if (result == null || result.isFormat()) {
/* 39 */       throw ERROR_INVALID_VALUE.createWithContext(reader, id);
/*    */     }
/* 41 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> contextBuilder, SuggestionsBuilder builder) {
/* 46 */     return SharedSuggestionProvider.suggest(ChatFormatting.getNames(true, false), builder);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 51 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/ColorArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */