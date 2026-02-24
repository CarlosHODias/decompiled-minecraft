/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.IdentifierArgument;
/*     */ import net.minecraft.commands.arguments.RangeArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.RandomSequence;
/*     */ import net.minecraft.world.RandomSequences;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RandomCommand
/*     */ {
/*  38 */   private static final SimpleCommandExceptionType ERROR_RANGE_TOO_LARGE = new SimpleCommandExceptionType((Message)Component.translatable("commands.random.error.range_too_large"));
/*  39 */   private static final SimpleCommandExceptionType ERROR_RANGE_TOO_SMALL = new SimpleCommandExceptionType((Message)Component.translatable("commands.random.error.range_too_small"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  42 */     dispatcher.register(
/*  43 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("random")
/*  44 */         .then(
/*  45 */           (ArgumentBuilder)drawRandomValueTree("value", false)))
/*     */         
/*  47 */         .then(
/*  48 */           (ArgumentBuilder)drawRandomValueTree("roll", true)))
/*     */         
/*  50 */         .then((
/*  51 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("reset")
/*  52 */           .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  53 */           .then((
/*  54 */             (LiteralArgumentBuilder)Commands.literal("*")
/*  55 */             .executes(c -> resetAllSequences((CommandSourceStack)c.getSource())))
/*  56 */             .then((
/*  57 */               (RequiredArgumentBuilder)Commands.argument("seed", (ArgumentType)IntegerArgumentType.integer())
/*  58 */               .executes(c -> resetAllSequencesAndSetNewDefaults((CommandSourceStack)c.getSource(), IntegerArgumentType.getInteger(c, "seed"), true, true)))
/*     */ 
/*     */               
/*  61 */               .then((
/*  62 */                 (RequiredArgumentBuilder)Commands.argument("includeWorldSeed", (ArgumentType)BoolArgumentType.bool())
/*  63 */                 .executes(c -> resetAllSequencesAndSetNewDefaults((CommandSourceStack)c.getSource(), IntegerArgumentType.getInteger(c, "seed"), BoolArgumentType.getBool(c, "includeWorldSeed"), true)))
/*     */ 
/*     */                 
/*  66 */                 .then(
/*  67 */                   Commands.argument("includeSequenceId", (ArgumentType)BoolArgumentType.bool())
/*  68 */                   .executes(c -> resetAllSequencesAndSetNewDefaults((CommandSourceStack)c.getSource(), IntegerArgumentType.getInteger(c, "seed"), BoolArgumentType.getBool(c, "includeWorldSeed"), BoolArgumentType.getBool(c, "includeSequenceId"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  75 */           .then((
/*  76 */             (RequiredArgumentBuilder)Commands.argument("sequence", (ArgumentType)IdentifierArgument.id())
/*  77 */             .suggests(RandomCommand::suggestRandomSequence)
/*  78 */             .executes(c -> resetSequence((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "sequence"))))
/*  79 */             .then((
/*  80 */               (RequiredArgumentBuilder)Commands.argument("seed", (ArgumentType)IntegerArgumentType.integer())
/*  81 */               .executes(c -> resetSequence((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "sequence"), IntegerArgumentType.getInteger(c, "seed"), true, true)))
/*     */ 
/*     */               
/*  84 */               .then((
/*  85 */                 (RequiredArgumentBuilder)Commands.argument("includeWorldSeed", (ArgumentType)BoolArgumentType.bool())
/*  86 */                 .executes(c -> resetSequence((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "sequence"), IntegerArgumentType.getInteger(c, "seed"), BoolArgumentType.getBool(c, "includeWorldSeed"), true)))
/*     */ 
/*     */                 
/*  89 */                 .then(
/*  90 */                   Commands.argument("includeSequenceId", (ArgumentType)BoolArgumentType.bool())
/*  91 */                   .executes(c -> resetSequence((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "sequence"), IntegerArgumentType.getInteger(c, "seed"), BoolArgumentType.getBool(c, "includeWorldSeed"), BoolArgumentType.getBool(c, "includeSequenceId")))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static LiteralArgumentBuilder<CommandSourceStack> drawRandomValueTree(String name, boolean announce) {
/* 103 */     return (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal(name)
/* 104 */       .then((
/* 105 */         (RequiredArgumentBuilder)Commands.argument("range", (ArgumentType)RangeArgument.intRange())
/* 106 */         .executes(c -> randomSample((CommandSourceStack)c.getSource(), RangeArgument.Ints.getRange(c, "range"), null, announce)))
/* 107 */         .then((
/* 108 */           (RequiredArgumentBuilder)Commands.argument("sequence", (ArgumentType)IdentifierArgument.id())
/* 109 */           .suggests(RandomCommand::suggestRandomSequence)
/* 110 */           .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 111 */           .executes(c -> randomSample((CommandSourceStack)c.getSource(), RangeArgument.Ints.getRange(c, "range"), IdentifierArgument.getId(c, "sequence"), announce))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static CompletableFuture<Suggestions> suggestRandomSequence(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
/* 117 */     List<String> result = Lists.newArrayList();
/* 118 */     ((CommandSourceStack)context.getSource()).getLevel().getRandomSequences().forAllSequences((key, sequence) -> result.add(key.toString()));
/* 119 */     return SharedSuggestionProvider.suggest(result, builder);
/*     */   }
/*     */   
/*     */   private static int randomSample(CommandSourceStack source, MinMaxBounds.Ints range, Identifier sequence, boolean announce) throws CommandSyntaxException {
/*     */     RandomSource random;
/* 124 */     if (sequence != null) {
/* 125 */       random = source.getLevel().getRandomSequence(sequence);
/*     */     } else {
/* 127 */       random = source.getLevel().getRandom();
/*     */     } 
/*     */     
/* 130 */     int min = (Integer)range.min().orElse(Integer.MIN_VALUE);
/* 131 */     int max = (Integer)range.max().orElse(Integer.MAX_VALUE);
/* 132 */     long span = max - min;
/* 133 */     if (span == 0L) {
/* 134 */       throw ERROR_RANGE_TOO_SMALL.create();
/*     */     }
/* 136 */     if (span >= 2147483647L) {
/* 137 */       throw ERROR_RANGE_TOO_LARGE.create();
/*     */     }
/* 139 */     int value = Mth.randomBetweenInclusive(random, min, max);
/* 140 */     if (announce) {
/* 141 */       source.getServer().getPlayerList().broadcastSystemMessage((Component)Component.translatable("commands.random.roll", new Object[] { source.getDisplayName(), value, min, max }), false);
/*     */     } else {
/* 143 */       source.sendSuccess(() -> Component.translatable("commands.random.sample.success", new Object[] { value }), false);
/*     */     } 
/*     */     
/* 146 */     return value;
/*     */   }
/*     */   
/*     */   private static int resetSequence(CommandSourceStack source, Identifier sequence) throws CommandSyntaxException {
/* 150 */     ServerLevel level = source.getLevel();
/* 151 */     level.getRandomSequences().reset(sequence, level.getSeed());
/* 152 */     source.sendSuccess(() -> Component.translatable("commands.random.reset.success", new Object[] { Component.translationArg(sequence) }), false);
/* 153 */     return 1;
/*     */   }
/*     */   
/*     */   private static int resetSequence(CommandSourceStack source, Identifier sequence, int salt, boolean includeWorldSeed, boolean includeSequenceId) throws CommandSyntaxException {
/* 157 */     ServerLevel level = source.getLevel();
/* 158 */     level.getRandomSequences().reset(sequence, level.getSeed(), salt, includeWorldSeed, includeSequenceId);
/* 159 */     source.sendSuccess(() -> Component.translatable("commands.random.reset.success", new Object[] { Component.translationArg(sequence) }), false);
/* 160 */     return 1;
/*     */   }
/*     */   
/*     */   private static int resetAllSequences(CommandSourceStack source) {
/* 164 */     int count = source.getLevel().getRandomSequences().clear();
/* 165 */     source.sendSuccess(() -> Component.translatable("commands.random.reset.all.success", new Object[] { count }), false);
/* 166 */     return count;
/*     */   }
/*     */   
/*     */   private static int resetAllSequencesAndSetNewDefaults(CommandSourceStack source, int salt, boolean includeWorldSeed, boolean includeSequenceId) {
/* 170 */     RandomSequences randomSequences = source.getLevel().getRandomSequences();
/* 171 */     randomSequences.setSeedDefaults(salt, includeWorldSeed, includeSequenceId);
/* 172 */     int count = randomSequences.clear();
/* 173 */     source.sendSuccess(() -> Component.translatable("commands.random.reset.all.success", new Object[] { count }), false);
/* 174 */     return count;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/RandomCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */