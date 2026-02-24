/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*     */ import java.util.Collection;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ public class ExperienceCommand
/*     */ {
/*  29 */   private static final SimpleCommandExceptionType ERROR_SET_POINTS_INVALID = new SimpleCommandExceptionType((Message)Component.translatable("commands.experience.set.points.invalid"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  32 */     LiteralCommandNode<CommandSourceStack> command = dispatcher.register(
/*  33 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("experience")
/*  34 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  35 */         .then(
/*  36 */           Commands.literal("add")
/*  37 */           .then(
/*  38 */             Commands.argument("target", (ArgumentType)EntityArgument.players())
/*  39 */             .then((
/*  40 */               (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("amount", (ArgumentType)IntegerArgumentType.integer())
/*  41 */               .executes(c -> addExperience((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "target"), IntegerArgumentType.getInteger(c, "amount"), Type.POINTS)))
/*  42 */               .then(
/*  43 */                 Commands.literal("points")
/*  44 */                 .executes(c -> addExperience((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "target"), IntegerArgumentType.getInteger(c, "amount"), Type.POINTS))))
/*     */               
/*  46 */               .then(
/*  47 */                 Commands.literal("levels")
/*  48 */                 .executes(c -> addExperience((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "target"), IntegerArgumentType.getInteger(c, "amount"), Type.LEVELS)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  53 */         .then(
/*  54 */           Commands.literal("set")
/*  55 */           .then(
/*  56 */             Commands.argument("target", (ArgumentType)EntityArgument.players())
/*  57 */             .then((
/*  58 */               (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("amount", (ArgumentType)IntegerArgumentType.integer(0))
/*  59 */               .executes(c -> setExperience((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "target"), IntegerArgumentType.getInteger(c, "amount"), Type.POINTS)))
/*  60 */               .then(
/*  61 */                 Commands.literal("points")
/*  62 */                 .executes(c -> setExperience((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "target"), IntegerArgumentType.getInteger(c, "amount"), Type.POINTS))))
/*     */               
/*  64 */               .then(
/*  65 */                 Commands.literal("levels")
/*  66 */                 .executes(c -> setExperience((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "target"), IntegerArgumentType.getInteger(c, "amount"), Type.LEVELS)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  71 */         .then(
/*  72 */           Commands.literal("query")
/*  73 */           .then((
/*  74 */             (RequiredArgumentBuilder)Commands.argument("target", (ArgumentType)EntityArgument.player())
/*  75 */             .then(
/*  76 */               Commands.literal("points")
/*  77 */               .executes(c -> queryExperience((CommandSourceStack)c.getSource(), EntityArgument.getPlayer(c, "target"), Type.POINTS))))
/*     */             
/*  79 */             .then(
/*  80 */               Commands.literal("levels")
/*  81 */               .executes(c -> queryExperience((CommandSourceStack)c.getSource(), EntityArgument.getPlayer(c, "target"), Type.LEVELS))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  87 */     dispatcher.register(
/*  88 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("xp")
/*  89 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  90 */         .redirect((CommandNode)command));
/*     */   }
/*     */ 
/*     */   
/*     */   private static int queryExperience(CommandSourceStack source, ServerPlayer target, Type type) {
/*  95 */     int result = type.query.applyAsInt(target);
/*  96 */     source.sendSuccess(() -> Component.translatable("commands.experience.query." + type.name, new Object[] { target.getDisplayName(), result }), false);
/*  97 */     return result;
/*     */   }
/*     */   
/*     */   private static int addExperience(CommandSourceStack source, Collection<? extends ServerPlayer> players, int amount, Type type) {
/* 101 */     for (ServerPlayer player : players) {
/* 102 */       type.add.accept(player, amount);
/*     */     }
/*     */     
/* 105 */     if (players.size() == 1) {
/* 106 */       source.sendSuccess(() -> Component.translatable("commands.experience.add." + type.name + ".success.single", new Object[] { amount, ((ServerPlayer)players.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 108 */       source.sendSuccess(() -> Component.translatable("commands.experience.add." + type.name + ".success.multiple", new Object[] { amount, players.size() }), true);
/*     */     } 
/*     */     
/* 111 */     return players.size();
/*     */   }
/*     */   
/*     */   private static int setExperience(CommandSourceStack source, Collection<? extends ServerPlayer> players, int amount, Type type) throws CommandSyntaxException {
/* 115 */     int success = 0;
/*     */     
/* 117 */     for (ServerPlayer player : players) {
/* 118 */       if (type.set.test(player, amount)) {
/* 119 */         success++;
/*     */       }
/*     */     } 
/*     */     
/* 123 */     if (success == 0) {
/* 124 */       throw ERROR_SET_POINTS_INVALID.create();
/*     */     }
/*     */     
/* 127 */     if (players.size() == 1) {
/* 128 */       source.sendSuccess(() -> Component.translatable("commands.experience.set." + type.name + ".success.single", new Object[] { amount, ((ServerPlayer)players.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 130 */       source.sendSuccess(() -> Component.translatable("commands.experience.set." + type.name + ".success.multiple", new Object[] { amount, players.size() }), true);
/*     */     } 
/*     */     
/* 133 */     return players.size();
/*     */   }
/*     */   private enum Type { POINTS, LEVELS;
/*     */     static {
/* 137 */       POINTS = new Type("POINTS", 0, "points", Player::giveExperiencePoints, (p, a) -> {
/*     */             if (a >= p.getXpNeededForNextLevel()) {
/*     */               return false;
/*     */             }
/*     */             p.setExperiencePoints(a);
/*     */             return true;
/*     */           }, p -> Mth.floor(p.experienceProgress * p.getXpNeededForNextLevel()));
/* 144 */       LEVELS = new Type("LEVELS", 1, "levels", ServerPlayer::giveExperienceLevels, (p, a) -> {
/*     */             p.setExperienceLevels(a);
/*     */             return true;
/*     */           }, p -> p.experienceLevel);
/*     */     }
/*     */     public final BiConsumer<ServerPlayer, Integer> add;
/*     */     public final BiPredicate<ServerPlayer, Integer> set;
/*     */     public final String name;
/*     */     private final ToIntFunction<ServerPlayer> query;
/*     */     
/*     */     Type(String name, BiConsumer<ServerPlayer, Integer> add, BiPredicate<ServerPlayer, Integer> set, ToIntFunction<ServerPlayer> query) {
/* 155 */       this.add = add;
/* 156 */       this.name = name;
/* 157 */       this.set = set;
/* 158 */       this.query = query;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/ExperienceCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */