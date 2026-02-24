/*     */ package net.minecraft.commands.arguments;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelectorParser;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.scores.ScoreHolder;
/*     */ 
/*     */ public class ScoreHolderArgument implements ArgumentType<ScoreHolderArgument.Result> {
/*     */   static {
/*  35 */     SUGGEST_SCORE_HOLDERS = ((context, builder) -> {
/*     */         StringReader reader = new StringReader(builder.getInput());
/*     */         reader.setCursor(builder.getStart());
/*     */         EntitySelectorParser parser = new EntitySelectorParser(reader, ((CommandSourceStack)context.getSource()).permissions().hasPermission(Permissions.COMMANDS_ENTITY_SELECTORS));
/*     */         try {
/*     */           parser.parse();
/*  41 */         } catch (CommandSyntaxException commandSyntaxException) {}
/*     */         return parser.fillSuggestions(builder, ());
/*     */       });
/*     */   }
/*     */   public static final SuggestionProvider<CommandSourceStack> SUGGEST_SCORE_HOLDERS;
/*  46 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "Player", "0123", "*", "@e" });
/*  47 */   private static final SimpleCommandExceptionType ERROR_NO_RESULTS = new SimpleCommandExceptionType((Message)Component.translatable("argument.scoreHolder.empty"));
/*     */   
/*     */   private final boolean multiple;
/*     */   
/*     */   public ScoreHolderArgument(boolean multiple) {
/*  52 */     this.multiple = multiple;
/*     */   }
/*     */   
/*     */   public static ScoreHolder getName(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  56 */     return getNames(context, name).iterator().next();
/*     */   }
/*     */   
/*     */   public static Collection<ScoreHolder> getNames(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  60 */     return getNames(context, name, java.util.Collections::emptyList);
/*     */   }
/*     */   
/*     */   public static Collection<ScoreHolder> getNamesWithDefaultWildcard(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  64 */     java.util.Objects.requireNonNull(((CommandSourceStack)context.getSource()).getServer().getScoreboard()); return getNames(context, name, ((CommandSourceStack)context.getSource()).getServer().getScoreboard()::getTrackedPlayers);
/*     */   }
/*     */   
/*     */   public static Collection<ScoreHolder> getNames(CommandContext<CommandSourceStack> context, String name, Supplier<Collection<ScoreHolder>> wildcard) throws CommandSyntaxException {
/*  68 */     Collection<ScoreHolder> result = ((Result)context.getArgument(name, Result.class)).getNames((CommandSourceStack)context.getSource(), wildcard);
/*  69 */     if (result.isEmpty()) {
/*  70 */       throw EntityArgument.NO_ENTITIES_FOUND.create();
/*     */     }
/*  72 */     return result;
/*     */   }
/*     */   
/*     */   public static ScoreHolderArgument scoreHolder() {
/*  76 */     return new ScoreHolderArgument(false);
/*     */   }
/*     */   
/*     */   public static ScoreHolderArgument scoreHolders() {
/*  80 */     return new ScoreHolderArgument(true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Result parse(StringReader reader) throws CommandSyntaxException {
/*  86 */     return parse(reader, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> Result parse(StringReader reader, S source) throws CommandSyntaxException {
/*  91 */     return parse(reader, EntitySelectorParser.allowSelectors(source));
/*     */   }
/*     */   
/*     */   private Result parse(StringReader reader, boolean allowSelectors) throws CommandSyntaxException {
/*  95 */     if (reader.canRead() && reader.peek() == '@') {
/*  96 */       EntitySelectorParser parser = new EntitySelectorParser(reader, allowSelectors);
/*  97 */       EntitySelector selector = parser.parse();
/*  98 */       if (!this.multiple && selector.getMaxResults() > 1) {
/*  99 */         throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.createWithContext(reader);
/*     */       }
/* 101 */       return new SelectorResult(selector);
/*     */     } 
/* 103 */     int start = reader.getCursor();
/* 104 */     while (reader.canRead() && reader.peek() != ' ') {
/* 105 */       reader.skip();
/*     */     }
/* 107 */     String text = reader.getString().substring(start, reader.getCursor());
/* 108 */     if (text.equals("*")) {
/* 109 */       return (sender, wildcard) -> {
/*     */           Collection<ScoreHolder> results = wildcard.get();
/*     */           
/*     */           if (results.isEmpty()) {
/*     */             throw ERROR_NO_RESULTS.create();
/*     */           }
/*     */           return results;
/*     */         };
/*     */     }
/* 118 */     List<ScoreHolder> nameOnlyHolder = List.of(ScoreHolder.forNameOnly(text));
/*     */ 
/*     */     
/* 121 */     if (text.startsWith("#")) {
/* 122 */       return (sender, wildcard) -> nameOnlyHolder;
/*     */     }
/*     */     
/*     */     try {
/* 126 */       UUID uuid = UUID.fromString(text);
/*     */       
/* 128 */       return (sender, wildcard) -> {
/*     */           Entity entity;
/*     */           
/*     */           MinecraftServer server = sender.getServer();
/*     */           
/*     */           ScoreHolder firstResult = null;
/*     */           
/*     */           List<ScoreHolder> moreResults = null;
/*     */           
/*     */           for (ServerLevel level : (Iterable<ServerLevel>)server.getAllLevels()) {
/*     */             Entity entity1 = level.getEntity(uuid);
/*     */             
/*     */             if (entity1 != null) {
/*     */               if (firstResult == null) {
/*     */                 entity = entity1;
/*     */                 
/*     */                 continue;
/*     */               } 
/*     */               
/*     */               if (moreResults == null) {
/*     */                 moreResults = new ArrayList<>();
/*     */                 
/*     */                 moreResults.add(entity);
/*     */               } 
/*     */               moreResults.add(entity1);
/*     */             } 
/*     */           } 
/*     */           return (moreResults != null) ? moreResults : ((entity != null) ? List.of(entity) : nameOnlyHolder);
/*     */         };
/* 157 */     } catch (IllegalArgumentException illegalArgumentException) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 162 */       return (sender, wildcard) -> {
/*     */           MinecraftServer server = sender.getServer();
/*     */           ServerPlayer player = server.getPlayerList().getPlayerByName(text);
/*     */           return (player != null) ? List.of(player) : nameOnlyHolder;
/*     */         };
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/* 177 */     return EXAMPLES;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Result {
/*     */     Collection<ScoreHolder> getNames(CommandSourceStack param1CommandSourceStack, Supplier<Collection<ScoreHolder>> param1Supplier) throws CommandSyntaxException;
/*     */   }
/*     */   
/*     */   public static class SelectorResult implements Result {
/*     */     private final EntitySelector selector;
/*     */     
/*     */     public SelectorResult(EntitySelector selector) {
/* 189 */       this.selector = selector;
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<ScoreHolder> getNames(CommandSourceStack sender, Supplier<Collection<ScoreHolder>> wildcard) throws CommandSyntaxException {
/* 194 */       List<? extends Entity> entities = this.selector.findEntities(sender);
/* 195 */       if (entities.isEmpty()) {
/* 196 */         throw EntityArgument.NO_ENTITIES_FOUND.create();
/*     */       }
/* 198 */       return (Collection)List.copyOf(entities);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Info implements ArgumentTypeInfo<ScoreHolderArgument, Info.Template> {
/*     */     private static final byte FLAG_MULTIPLE = 1;
/*     */     
/*     */     public final class Template implements ArgumentTypeInfo.Template<ScoreHolderArgument> {
/*     */       private final boolean multiple;
/*     */       
/*     */       private Template(boolean multiple) {
/* 209 */         this.multiple = multiple;
/*     */       }
/*     */ 
/*     */       
/*     */       public ScoreHolderArgument instantiate(CommandBuildContext context) {
/* 214 */         return new ScoreHolderArgument(this.multiple);
/*     */       }
/*     */ 
/*     */       
/*     */       public ArgumentTypeInfo<ScoreHolderArgument, ?> type() {
/* 219 */         return ScoreHolderArgument.Info.this;
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 225 */       int flags = 0;
/* 226 */       if (template.multiple) {
/* 227 */         flags |= 0x1;
/*     */       }
/* 229 */       out.writeByte(flags);
/*     */     }
/*     */ 
/*     */     
/*     */     public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 234 */       byte flags = in.readByte();
/* 235 */       boolean multiple = ((flags & 0x1) != 0);
/* 236 */       return new Template(multiple);
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeToJson(Template template, JsonObject out) {
/* 241 */       out.addProperty("amount", template.multiple ? "multiple" : "single");
/*     */     }
/*     */ 
/*     */     
/*     */     public Template unpack(ScoreHolderArgument argument) {
/* 246 */       return new Template(argument.multiple);
/*     */     }
/*     */   }
/*     */   
/*     */   public final class Template implements ArgumentTypeInfo.Template<ScoreHolderArgument> {
/*     */     private final boolean multiple;
/*     */     
/*     */     private Template(boolean multiple) {
/*     */       this.multiple = multiple;
/*     */     }
/*     */     
/*     */     public ScoreHolderArgument instantiate(CommandBuildContext context) {
/*     */       return new ScoreHolderArgument(this.multiple);
/*     */     }
/*     */     
/*     */     public ArgumentTypeInfo<ScoreHolderArgument, ?> type() {
/*     */       return ScoreHolderArgument.Info.this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/ScoreHolderArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */