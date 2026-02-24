/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.ResourceOrIdArgument;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.common.ClientboundClearDialogPacket;
/*    */ import net.minecraft.server.dialog.Dialog;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public class DialogCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/* 25 */     dispatcher.register(
/* 26 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("dialog")
/* 27 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 28 */         .then(
/* 29 */           Commands.literal("show")
/* 30 */           .then(
/* 31 */             Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 32 */             .then(
/* 33 */               Commands.argument("dialog", (ArgumentType)ResourceOrIdArgument.dialog(context))
/* 34 */               .executes(c -> showDialog((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), ResourceOrIdArgument.getDialog(c, "dialog")))))))
/*    */ 
/*    */ 
/*    */         
/* 38 */         .then(
/* 39 */           Commands.literal("clear")
/* 40 */           .then(
/* 41 */             Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 42 */             .executes(c -> clearDialog((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int showDialog(CommandSourceStack sender, Collection<ServerPlayer> targets, Holder<Dialog> dialog) {
/* 49 */     for (ServerPlayer target : targets) {
/* 50 */       target.openDialog(dialog);
/*    */     }
/*    */     
/* 53 */     if (targets.size() == 1) {
/* 54 */       sender.sendSuccess(() -> Component.translatable("commands.dialog.show.single", new Object[] { ((ServerPlayer)targets.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 56 */       sender.sendSuccess(() -> Component.translatable("commands.dialog.show.multiple", new Object[] { targets.size() }), true);
/*    */     } 
/* 58 */     return targets.size();
/*    */   }
/*    */   
/*    */   private static int clearDialog(CommandSourceStack sender, Collection<ServerPlayer> targets) {
/* 62 */     for (ServerPlayer target : targets) {
/* 63 */       target.connection.send((Packet)ClientboundClearDialogPacket.INSTANCE);
/*    */     }
/*    */     
/* 66 */     if (targets.size() == 1) {
/* 67 */       sender.sendSuccess(() -> Component.translatable("commands.dialog.clear.single", new Object[] { ((ServerPlayer)targets.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 69 */       sender.sendSuccess(() -> Component.translatable("commands.dialog.clear.multiple", new Object[] { targets.size() }), true);
/*    */     } 
/* 71 */     return targets.size();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/DialogCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */