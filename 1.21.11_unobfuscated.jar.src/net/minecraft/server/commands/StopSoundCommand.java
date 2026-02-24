/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.IdentifierArgument;
/*    */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*    */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ 
/*    */ public class StopSoundCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 28 */     RequiredArgumentBuilder<CommandSourceStack, EntitySelector> target = (RequiredArgumentBuilder<CommandSourceStack, EntitySelector>)((RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 29 */       .executes(c -> stopSound((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), null, null)))
/* 30 */       .then(
/* 31 */         Commands.literal("*")
/* 32 */         .then(
/* 33 */           Commands.argument("sound", (ArgumentType)IdentifierArgument.id())
/* 34 */           .suggests(SuggestionProviders.cast(SuggestionProviders.AVAILABLE_SOUNDS))
/* 35 */           .executes(c -> stopSound((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), null, IdentifierArgument.getId(c, "sound")))));
/*    */ 
/*    */ 
/*    */     
/* 39 */     for (SoundSource source : SoundSource.values()) {
/* 40 */       target.then((
/* 41 */           (LiteralArgumentBuilder)Commands.literal(source.getName())
/* 42 */           .executes(c -> stopSound((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), source, null)))
/* 43 */           .then(
/* 44 */             Commands.argument("sound", (ArgumentType)IdentifierArgument.id())
/* 45 */             .suggests(SuggestionProviders.cast(SuggestionProviders.AVAILABLE_SOUNDS))
/* 46 */             .executes(c -> stopSound((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), source, IdentifierArgument.getId(c, "sound")))));
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 51 */     dispatcher.register(
/* 52 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("stopsound")
/* 53 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 54 */         .then((ArgumentBuilder)target));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int stopSound(CommandSourceStack source, Collection<ServerPlayer> targets, SoundSource soundSource, Identifier sound) {
/* 61 */     ClientboundStopSoundPacket packet = new ClientboundStopSoundPacket(sound, soundSource);
/* 62 */     for (ServerPlayer player : targets) {
/* 63 */       player.connection.send((Packet)packet);
/*    */     }
/*    */     
/* 66 */     if (soundSource != null) {
/* 67 */       if (sound != null) {
/* 68 */         source.sendSuccess(() -> Component.translatable("commands.stopsound.success.source.sound", new Object[] { Component.translationArg(sound), soundSource.getName() }), true);
/*    */       } else {
/* 70 */         source.sendSuccess(() -> Component.translatable("commands.stopsound.success.source.any", new Object[] { soundSource.getName() }), true);
/*    */       }
/*    */     
/* 73 */     } else if (sound != null) {
/* 74 */       source.sendSuccess(() -> Component.translatable("commands.stopsound.success.sourceless.sound", new Object[] { Component.translationArg(sound) }), true);
/*    */     } else {
/* 76 */       source.sendSuccess(() -> Component.translatable("commands.stopsound.success.sourceless.any"), true);
/*    */     } 
/*    */ 
/*    */     
/* 80 */     return targets.size();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/StopSoundCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */