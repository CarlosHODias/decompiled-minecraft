/*    */ package net.minecraft.commands.arguments;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.ParserUtils;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.inventory.SlotRange;
/*    */ import net.minecraft.world.inventory.SlotRanges;
/*    */ 
/*    */ public class SlotsArgument implements ArgumentType<SlotRange> {
/*    */   private static final DynamicCommandExceptionType ERROR_UNKNOWN_SLOT;
/* 22 */   private static final Collection<String> EXAMPLES = List.of("container.*", "container.5", "weapon"); static {
/* 23 */     ERROR_UNKNOWN_SLOT = new DynamicCommandExceptionType(id -> Component.translatableEscape("slot.unknown", new Object[] { id }));
/*    */   }
/*    */   public static SlotsArgument slots() {
/* 26 */     return new SlotsArgument();
/*    */   }
/*    */   
/*    */   public static SlotRange getSlots(CommandContext<CommandSourceStack> context, String name) {
/* 30 */     return (SlotRange)context.getArgument(name, SlotRange.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public SlotRange parse(StringReader reader) throws CommandSyntaxException {
/* 35 */     String name = ParserUtils.readWhile(reader, c -> (c != ' '));
/* 36 */     SlotRange result = SlotRanges.nameToIds(name);
/* 37 */     if (result == null) {
/* 38 */       throw ERROR_UNKNOWN_SLOT.createWithContext(reader, name);
/*    */     }
/* 40 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> contextBuilder, SuggestionsBuilder builder) {
/* 45 */     return SharedSuggestionProvider.suggest(SlotRanges.allNames(), builder);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 50 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/SlotsArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */