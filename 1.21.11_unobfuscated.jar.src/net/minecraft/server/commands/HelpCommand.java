/*    */ package net.minecraft.server.commands;
/*    */ import com.google.common.collect.Iterables;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.ParseResults;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.context.ParsedCommandNode;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ import java.util.Map;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class HelpCommand {
/* 20 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.help.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 23 */     dispatcher.register(
/* 24 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("help")
/* 25 */         .executes(s -> {
/*    */             Map<CommandNode<CommandSourceStack>, String> usage = dispatcher.getSmartUsage((CommandNode)dispatcher.getRoot(), s.getSource());
/*    */             
/*    */             for (String line : usage.values()) {
/*    */               ((CommandSourceStack)s.getSource()).sendSuccess((), false);
/*    */             }
/*    */             return usage.size();
/* 32 */           })).then(
/* 33 */           Commands.argument("command", (ArgumentType)StringArgumentType.greedyString())
/* 34 */           .executes(s -> {
/*    */               ParseResults<CommandSourceStack> command = dispatcher.parse(StringArgumentType.getString(s, "command"), s.getSource());
/*    */               if (command.getContext().getNodes().isEmpty())
/*    */                 throw ERROR_FAILED.create(); 
/*    */               Map<CommandNode<CommandSourceStack>, String> usage = dispatcher.getSmartUsage(((ParsedCommandNode)Iterables.getLast(command.getContext().getNodes())).getNode(), s.getSource());
/*    */               for (String line : usage.values())
/*    */                 ((CommandSourceStack)s.getSource()).sendSuccess((), false); 
/*    */               return usage.size();
/*    */             })));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/HelpCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */