/*    */ package net.minecraft.commands.arguments.item;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.functions.CommandFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class FunctionArgument implements ArgumentType<FunctionArgument.Result> {
/*    */   private static final DynamicCommandExceptionType ERROR_UNKNOWN_TAG;
/* 20 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo:bar", "#foo" }); private static final DynamicCommandExceptionType ERROR_UNKNOWN_FUNCTION; static {
/* 21 */     ERROR_UNKNOWN_TAG = new DynamicCommandExceptionType(tag -> Component.translatableEscape("arguments.function.tag.unknown", new Object[] { tag }));
/* 22 */     ERROR_UNKNOWN_FUNCTION = new DynamicCommandExceptionType(value -> Component.translatableEscape("arguments.function.unknown", new Object[] { value }));
/*    */   }
/*    */   public static FunctionArgument functions() {
/* 25 */     return new FunctionArgument();
/*    */   }
/*    */ 
/*    */   
/*    */   public Result parse(StringReader reader) throws CommandSyntaxException {
/* 30 */     if (reader.canRead() && reader.peek() == '#') {
/* 31 */       reader.skip();
/* 32 */       final Identifier id = Identifier.read(reader);
/* 33 */       return new Result(this)
/*    */         {
/*    */           public Collection<CommandFunction<CommandSourceStack>> create(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
/* 36 */             return FunctionArgument.getFunctionTag(c, id);
/*    */           }
/*    */ 
/*    */           
/*    */           public Pair<Identifier, Either<CommandFunction<CommandSourceStack>, Collection<CommandFunction<CommandSourceStack>>>> unwrap(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
/* 41 */             return Pair.of(id, Either.right(FunctionArgument.getFunctionTag(context, id)));
/*    */           }
/*    */ 
/*    */           
/*    */           public Pair<Identifier, Collection<CommandFunction<CommandSourceStack>>> unwrapToCollection(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
/* 46 */             return Pair.of(id, FunctionArgument.getFunctionTag(context, id));
/*    */           }
/*    */         };
/*    */     } 
/*    */     
/* 51 */     final Identifier id = Identifier.read(reader);
/* 52 */     return new Result(this)
/*    */       {
/*    */         public Collection<CommandFunction<CommandSourceStack>> create(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
/* 55 */           return Collections.singleton(FunctionArgument.getFunction(c, id));
/*    */         }
/*    */ 
/*    */         
/*    */         public Pair<Identifier, Either<CommandFunction<CommandSourceStack>, Collection<CommandFunction<CommandSourceStack>>>> unwrap(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
/* 60 */           return Pair.of(id, Either.left(FunctionArgument.getFunction(context, id)));
/*    */         }
/*    */ 
/*    */         
/*    */         public Pair<Identifier, Collection<CommandFunction<CommandSourceStack>>> unwrapToCollection(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
/* 65 */           return Pair.of(id, Collections.singleton(FunctionArgument.getFunction(context, id)));
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   private static CommandFunction<CommandSourceStack> getFunction(CommandContext<CommandSourceStack> c, Identifier id) throws CommandSyntaxException {
/* 71 */     return (CommandFunction<CommandSourceStack>)((CommandSourceStack)c.getSource()).getServer().getFunctions().get(id)
/* 72 */       .orElseThrow(() -> ERROR_UNKNOWN_FUNCTION.create(id.toString()));
/*    */   }
/*    */   
/*    */   private static Collection<CommandFunction<CommandSourceStack>> getFunctionTag(CommandContext<CommandSourceStack> c, Identifier id) throws CommandSyntaxException {
/* 76 */     Collection<CommandFunction<CommandSourceStack>> tag = ((CommandSourceStack)c.getSource()).getServer().getFunctions().getTag(id);
/* 77 */     if (tag == null) {
/* 78 */       throw ERROR_UNKNOWN_TAG.create(id.toString());
/*    */     }
/* 80 */     return tag;
/*    */   }
/*    */   
/*    */   public static Collection<CommandFunction<CommandSourceStack>> getFunctions(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/* 84 */     return ((Result)context.getArgument(name, Result.class)).create(context);
/*    */   }
/*    */   
/*    */   public static Pair<Identifier, Either<CommandFunction<CommandSourceStack>, Collection<CommandFunction<CommandSourceStack>>>> getFunctionOrTag(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/* 88 */     return ((Result)context.getArgument(name, Result.class)).unwrap(context);
/*    */   }
/*    */   
/*    */   public static Pair<Identifier, Collection<CommandFunction<CommandSourceStack>>> getFunctionCollection(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/* 92 */     return ((Result)context.getArgument(name, Result.class)).unwrapToCollection(context);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 97 */     return EXAMPLES;
/*    */   }
/*    */   
/*    */   public static interface Result {
/*    */     Collection<CommandFunction<CommandSourceStack>> create(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException;
/*    */     
/*    */     Pair<Identifier, Either<CommandFunction<CommandSourceStack>, Collection<CommandFunction<CommandSourceStack>>>> unwrap(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException;
/*    */     
/*    */     Pair<Identifier, Collection<CommandFunction<CommandSourceStack>>> unwrapToCollection(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/item/FunctionArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */