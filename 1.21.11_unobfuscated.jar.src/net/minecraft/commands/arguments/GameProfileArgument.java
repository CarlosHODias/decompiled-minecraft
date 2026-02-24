/*     */ package net.minecraft.commands.arguments;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelectorParser;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ 
/*     */ public class GameProfileArgument implements ArgumentType<GameProfileArgument.Result> {
/*  28 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "Player", "0123", "dd12be42-52a9-4a91-a8a1-11c01849e498", "@e" });
/*  29 */   public static final SimpleCommandExceptionType ERROR_UNKNOWN_PLAYER = new SimpleCommandExceptionType((Message)Component.translatable("argument.player.unknown"));
/*     */   
/*     */   public static Collection<NameAndId> getGameProfiles(CommandContext<CommandSourceStack> source, String name) throws CommandSyntaxException {
/*  32 */     return ((Result)source.getArgument(name, Result.class)).getNames((CommandSourceStack)source.getSource());
/*     */   }
/*     */   
/*     */   public static GameProfileArgument gameProfile() {
/*  36 */     return new GameProfileArgument();
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> Result parse(StringReader reader, S source) throws CommandSyntaxException {
/*  41 */     return parse(reader, EntitySelectorParser.allowSelectors(source));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Result parse(StringReader reader) throws CommandSyntaxException {
/*  47 */     return parse(reader, true);
/*     */   }
/*     */   
/*     */   private static Result parse(StringReader reader, boolean allowSelectors) throws CommandSyntaxException {
/*  51 */     if (reader.canRead() && reader.peek() == '@') {
/*  52 */       EntitySelectorParser parser = new EntitySelectorParser(reader, allowSelectors);
/*  53 */       EntitySelector parse = parser.parse();
/*  54 */       if (parse.includesEntities()) {
/*  55 */         throw EntityArgument.ERROR_ONLY_PLAYERS_ALLOWED.createWithContext(reader);
/*     */       }
/*  57 */       return new SelectorResult(parse);
/*     */     } 
/*     */     
/*  60 */     int start = reader.getCursor();
/*  61 */     while (reader.canRead() && reader.peek() != ' ') {
/*  62 */       reader.skip();
/*     */     }
/*  64 */     String name = reader.getString().substring(start, reader.getCursor());
/*  65 */     return c -> {
/*     */         Optional<NameAndId> result = c.getServer().services().nameToIdCache().get(name);
/*     */         Objects.requireNonNull(ERROR_UNKNOWN_PLAYER);
/*     */         return Collections.singleton(result.orElseThrow(ERROR_UNKNOWN_PLAYER::create));
/*     */       };
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Result {
/*     */     Collection<NameAndId> getNames(CommandSourceStack param1CommandSourceStack) throws CommandSyntaxException; }
/*     */   
/*     */   public static class SelectorResult implements Result {
/*     */     private final EntitySelector selector;
/*     */     
/*     */     public SelectorResult(EntitySelector selector) {
/*  80 */       this.selector = selector;
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<NameAndId> getNames(CommandSourceStack sender) throws CommandSyntaxException {
/*  85 */       List<ServerPlayer> players = this.selector.findPlayers(sender);
/*  86 */       if (players.isEmpty()) {
/*  87 */         throw EntityArgument.NO_PLAYERS_FOUND.create();
/*     */       }
/*  89 */       List<NameAndId> result = new ArrayList<>();
/*  90 */       for (ServerPlayer entity : players) {
/*  91 */         result.add(entity.nameAndId());
/*     */       }
/*  93 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> contextBuilder, SuggestionsBuilder builder) {
/*  99 */     Object object = contextBuilder.getSource(); if (object instanceof SharedSuggestionProvider) { SharedSuggestionProvider source = (SharedSuggestionProvider)object;
/* 100 */       StringReader reader = new StringReader(builder.getInput());
/* 101 */       reader.setCursor(builder.getStart());
/* 102 */       EntitySelectorParser parser = new EntitySelectorParser(reader, source.permissions().hasPermission(Permissions.COMMANDS_ENTITY_SELECTORS));
/*     */       try {
/* 104 */         parser.parse();
/* 105 */       } catch (CommandSyntaxException commandSyntaxException) {}
/*     */       
/* 107 */       return parser.fillSuggestions(builder, suggestions -> SharedSuggestionProvider.suggest(source.getOnlinePlayerNames(), suggestions)); }
/*     */     
/* 109 */     return Suggestions.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/* 115 */     return EXAMPLES;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/GameProfileArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */