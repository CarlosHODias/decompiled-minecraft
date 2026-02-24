/*    */ package net.minecraft.commands.arguments;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.brigadier.ImmutableStringReader;
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
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.stats.Stat;
/*    */ import net.minecraft.stats.StatType;
/*    */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*    */ 
/*    */ public class ObjectiveCriteriaArgument implements ArgumentType<ObjectiveCriteria> {
/* 25 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo.bar.baz", "minecraft:foo" }); static {
/* 26 */     ERROR_INVALID_VALUE = new DynamicCommandExceptionType(value -> Component.translatableEscape("argument.criteria.invalid", new Object[] { value }));
/*    */   }
/*    */   
/*    */   public static final DynamicCommandExceptionType ERROR_INVALID_VALUE;
/*    */   
/*    */   public static ObjectiveCriteriaArgument criteria() {
/* 32 */     return new ObjectiveCriteriaArgument();
/*    */   }
/*    */   
/*    */   public static ObjectiveCriteria getCriteria(CommandContext<CommandSourceStack> context, String name) {
/* 36 */     return (ObjectiveCriteria)context.getArgument(name, ObjectiveCriteria.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public ObjectiveCriteria parse(StringReader reader) throws CommandSyntaxException {
/* 41 */     int start = reader.getCursor();
/* 42 */     while (reader.canRead() && reader.peek() != ' ') {
/* 43 */       reader.skip();
/*    */     }
/* 45 */     String id = reader.getString().substring(start, reader.getCursor());
/* 46 */     return (ObjectiveCriteria)ObjectiveCriteria.byName(id).orElseThrow(() -> {
/*    */           reader.setCursor(start);
/*    */           return ERROR_INVALID_VALUE.createWithContext((ImmutableStringReader)reader, id);
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 54 */     List<String> ids = Lists.newArrayList(ObjectiveCriteria.getCustomCriteriaNames());
/* 55 */     for (StatType<?> type : (Iterable<StatType<?>>)BuiltInRegistries.STAT_TYPE) {
/* 56 */       for (Object value : type.getRegistry()) {
/* 57 */         String name = getName(type, value);
/* 58 */         ids.add(name);
/*    */       } 
/*    */     } 
/* 61 */     return SharedSuggestionProvider.suggest(ids, builder);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> String getName(StatType<T> type, Object value) {
/* 66 */     return Stat.buildName(type, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 71 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/ObjectiveCriteriaArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */