/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class SpectateCommand {
/*    */   private static final DynamicCommandExceptionType ERROR_NOT_SPECTATOR;
/*    */   private static final DynamicCommandExceptionType ERROR_CANNOT_SPECTATE;
/* 22 */   private static final SimpleCommandExceptionType ERROR_SELF = new SimpleCommandExceptionType((Message)Component.translatable("commands.spectate.self")); static {
/* 23 */     ERROR_NOT_SPECTATOR = new DynamicCommandExceptionType(s -> Component.translatableEscape("commands.spectate.not_spectator", new Object[] { s }));
/* 24 */     ERROR_CANNOT_SPECTATE = new DynamicCommandExceptionType(s -> Component.translatableEscape("commands.spectate.cannot_spectate", new Object[] { s }));
/*    */   }
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 27 */     dispatcher.register(
/* 28 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("spectate")
/* 29 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 30 */         .executes(c -> spectate((CommandSourceStack)c.getSource(), null, ((CommandSourceStack)c.getSource()).getPlayerOrException())))
/* 31 */         .then((
/* 32 */           (RequiredArgumentBuilder)Commands.argument("target", (ArgumentType)EntityArgument.entity())
/* 33 */           .executes(c -> spectate((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), ((CommandSourceStack)c.getSource()).getPlayerOrException())))
/* 34 */           .then(
/* 35 */             Commands.argument("player", (ArgumentType)EntityArgument.player())
/* 36 */             .executes(c -> spectate((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), EntityArgument.getPlayer(c, "player"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int spectate(CommandSourceStack source, Entity target, ServerPlayer player) throws CommandSyntaxException {
/* 43 */     if (player == target)
/* 44 */       throw ERROR_SELF.create(); 
/* 45 */     if (!player.isSpectator())
/* 46 */       throw ERROR_NOT_SPECTATOR.create(player.getDisplayName()); 
/* 47 */     if (target != null && target.getType().clientTrackingRange() == 0) {
/* 48 */       throw ERROR_CANNOT_SPECTATE.create(target.getDisplayName());
/*    */     }
/*    */     
/* 51 */     player.setCamera(target);
/* 52 */     if (target != null) {
/* 53 */       source.sendSuccess(() -> Component.translatable("commands.spectate.success.started", new Object[] { target.getDisplayName() }), false);
/*    */     } else {
/* 55 */       source.sendSuccess(() -> Component.translatable("commands.spectate.success.stopped"), false);
/*    */     } 
/* 57 */     return 1;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/SpectateCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */