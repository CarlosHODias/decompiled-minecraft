/*     */ package net.minecraft.commands.arguments.selector.options;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.advancements.CriterionProgress;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelectorParser;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.PlayerAdvancements;
/*     */ import net.minecraft.server.ServerAdvancementManager;
/*     */ import net.minecraft.server.ServerScoreboard;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class EntitySelectorOptions {
/*  56 */   private static final Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*  57 */   private static final Map<String, Option> OPTIONS = Maps.newHashMap(); public static final DynamicCommandExceptionType ERROR_UNKNOWN_OPTION; public static final DynamicCommandExceptionType ERROR_INAPPLICABLE_OPTION;
/*     */   static {
/*  59 */     ERROR_UNKNOWN_OPTION = new DynamicCommandExceptionType(name -> Component.translatableEscape("argument.entity.options.unknown", new Object[] { name }));
/*  60 */     ERROR_INAPPLICABLE_OPTION = new DynamicCommandExceptionType(name -> Component.translatableEscape("argument.entity.options.inapplicable", new Object[] { name }));
/*  61 */   } public static final SimpleCommandExceptionType ERROR_RANGE_NEGATIVE = new SimpleCommandExceptionType((Message)Component.translatable("argument.entity.options.distance.negative"));
/*  62 */   public static final SimpleCommandExceptionType ERROR_LEVEL_NEGATIVE = new SimpleCommandExceptionType((Message)Component.translatable("argument.entity.options.level.negative"));
/*  63 */   public static final SimpleCommandExceptionType ERROR_LIMIT_TOO_SMALL = new SimpleCommandExceptionType((Message)Component.translatable("argument.entity.options.limit.toosmall")); public static final DynamicCommandExceptionType ERROR_SORT_UNKNOWN; public static final DynamicCommandExceptionType ERROR_GAME_MODE_INVALID; public static final DynamicCommandExceptionType ERROR_ENTITY_TYPE_INVALID; static {
/*  64 */     ERROR_SORT_UNKNOWN = new DynamicCommandExceptionType(name -> Component.translatableEscape("argument.entity.options.sort.irreversible", new Object[] { name }));
/*  65 */     ERROR_GAME_MODE_INVALID = new DynamicCommandExceptionType(name -> Component.translatableEscape("argument.entity.options.mode.invalid", new Object[] { name }));
/*  66 */     ERROR_ENTITY_TYPE_INVALID = new DynamicCommandExceptionType(type -> Component.translatableEscape("argument.entity.options.type.invalid", new Object[] { type }));
/*     */   }
/*     */   private static void register(String name, Modifier modifier, Predicate<EntitySelectorParser> predicate, Component description) {
/*  69 */     OPTIONS.put(name, new Option(modifier, predicate, description));
/*     */   }
/*     */   
/*     */   public static void bootStrap() {
/*  73 */     if (!OPTIONS.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  77 */     register("name", parser -> {
/*     */           int start = parser.getReader().getCursor();
/*     */           boolean not = parser.shouldInvertValue();
/*     */           String name = parser.getReader().readString();
/*     */           if (parser.hasNameNotEquals() && !not) {
/*     */             parser.getReader().setCursor(start);
/*     */             throw ERROR_INAPPLICABLE_OPTION.createWithContext(parser.getReader(), "name");
/*     */           } 
/*     */           if (not) {
/*     */             parser.setHasNameNotEquals(true);
/*     */           } else {
/*     */             parser.setHasNameEquals(true);
/*     */           } 
/*     */           parser.addPredicate(());
/*  91 */         }, s -> !s.hasNameEquals(), (Component)Component.translatable("argument.entity.options.name.description"));
/*     */     
/*  93 */     register("distance", parser -> {
/*     */           int start = parser.getReader().getCursor();
/*     */           MinMaxBounds.Doubles value = MinMaxBounds.Doubles.fromReader(parser.getReader());
/*     */           if ((value.min().isPresent() && (Double)value.min().get() < 0.0D) || (value.max().isPresent() && (Double)value.max().get() < 0.0D)) {
/*     */             parser.getReader().setCursor(start);
/*     */             throw ERROR_RANGE_NEGATIVE.createWithContext(parser.getReader());
/*     */           } 
/*     */           parser.setDistance(value);
/*     */           parser.setWorldLimited();
/* 102 */         }, s -> (s.getDistance() == null), (Component)Component.translatable("argument.entity.options.distance.description"));
/*     */     
/* 104 */     register("level", parser -> {
/*     */           int start = parser.getReader().getCursor();
/*     */           MinMaxBounds.Ints value = MinMaxBounds.Ints.fromReader(parser.getReader());
/*     */           if ((value.min().isPresent() && (Integer)value.min().get() < 0) || (value.max().isPresent() && (Integer)value.max().get() < 0)) {
/*     */             parser.getReader().setCursor(start);
/*     */             throw ERROR_LEVEL_NEGATIVE.createWithContext(parser.getReader());
/*     */           } 
/*     */           parser.setLevel(value);
/*     */           parser.setIncludesEntities(false);
/* 113 */         }, s -> (s.getLevel() == null), (Component)Component.translatable("argument.entity.options.level.description"));
/*     */     
/* 115 */     register("x", parser -> {
/*     */           parser.setWorldLimited();
/*     */           parser.setX(parser.getReader().readDouble());
/* 118 */         }, s -> (s.getX() == null), (Component)Component.translatable("argument.entity.options.x.description"));
/*     */     
/* 120 */     register("y", parser -> {
/*     */           parser.setWorldLimited();
/*     */           parser.setY(parser.getReader().readDouble());
/* 123 */         }, s -> (s.getY() == null), (Component)Component.translatable("argument.entity.options.y.description"));
/*     */     
/* 125 */     register("z", parser -> {
/*     */           parser.setWorldLimited();
/*     */           parser.setZ(parser.getReader().readDouble());
/* 128 */         }, s -> (s.getZ() == null), (Component)Component.translatable("argument.entity.options.z.description"));
/*     */     
/* 130 */     register("dx", parser -> {
/*     */           parser.setWorldLimited();
/*     */           parser.setDeltaX(parser.getReader().readDouble());
/* 133 */         }, s -> (s.getDeltaX() == null), (Component)Component.translatable("argument.entity.options.dx.description"));
/*     */     
/* 135 */     register("dy", parser -> {
/*     */           parser.setWorldLimited();
/*     */           parser.setDeltaY(parser.getReader().readDouble());
/* 138 */         }, s -> (s.getDeltaY() == null), (Component)Component.translatable("argument.entity.options.dy.description"));
/*     */     
/* 140 */     register("dz", parser -> {
/*     */           parser.setWorldLimited();
/*     */           parser.setDeltaZ(parser.getReader().readDouble());
/* 143 */         }, s -> (s.getDeltaZ() == null), (Component)Component.translatable("argument.entity.options.dz.description"));
/*     */     
/* 145 */     register("x_rotation", parser -> parser.setRotX(MinMaxBounds.FloatDegrees.fromReader(parser.getReader())), s -> (s.getRotX() == null), 
/*     */         
/* 147 */         (Component)Component.translatable("argument.entity.options.x_rotation.description"));
/*     */     
/* 149 */     register("y_rotation", parser -> parser.setRotY(MinMaxBounds.FloatDegrees.fromReader(parser.getReader())), s -> (s.getRotY() == null), 
/*     */         
/* 151 */         (Component)Component.translatable("argument.entity.options.y_rotation.description"));
/*     */     
/* 153 */     register("limit", parser -> {
/*     */           int start = parser.getReader().getCursor(), count = parser.getReader().readInt();
/*     */           
/*     */           if (count < 1) {
/*     */             parser.getReader().setCursor(start);
/*     */             throw ERROR_LIMIT_TOO_SMALL.createWithContext(parser.getReader());
/*     */           } 
/*     */           parser.setMaxResults(count);
/*     */           parser.setLimited(true);
/* 162 */         }, s -> (!s.isCurrentEntity() && !s.isLimited()), (Component)Component.translatable("argument.entity.options.limit.description"));
/*     */     
/* 164 */     register("sort", parser -> {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           // Byte code:
/*     */           //   0: aload_0
/*     */           //   1: invokevirtual getReader : ()Lcom/mojang/brigadier/StringReader;
/*     */           //   4: invokevirtual getCursor : ()I
/*     */           //   7: istore_1
/*     */           //   8: aload_0
/*     */           //   9: invokevirtual getReader : ()Lcom/mojang/brigadier/StringReader;
/*     */           //   12: invokevirtual readUnquotedString : ()Ljava/lang/String;
/*     */           //   15: astore_2
/*     */           //   16: aload_0
/*     */           //   17: <illegal opcode> apply : ()Ljava/util/function/BiFunction;
/*     */           //   22: invokevirtual setSuggestions : (Ljava/util/function/BiFunction;)V
/*     */           //   25: aload_0
/*     */           //   26: aload_2
/*     */           //   27: astore_3
/*     */           //   28: iconst_m1
/*     */           //   29: istore #4
/*     */           //   31: aload_3
/*     */           //   32: invokevirtual hashCode : ()I
/*     */           //   35: lookupswitch default -> 137, -938285885 -> 108, 1510793967 -> 92, 1780188658 -> 124, 1825779806 -> 76
/*     */           //   76: aload_3
/*     */           //   77: ldc_w 'nearest'
/*     */           //   80: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */           //   83: ifeq -> 137
/*     */           //   86: iconst_0
/*     */           //   87: istore #4
/*     */           //   89: goto -> 137
/*     */           //   92: aload_3
/*     */           //   93: ldc_w 'furthest'
/*     */           //   96: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */           //   99: ifeq -> 137
/*     */           //   102: iconst_1
/*     */           //   103: istore #4
/*     */           //   105: goto -> 137
/*     */           //   108: aload_3
/*     */           //   109: ldc_w 'random'
/*     */           //   112: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */           //   115: ifeq -> 137
/*     */           //   118: iconst_2
/*     */           //   119: istore #4
/*     */           //   121: goto -> 137
/*     */           //   124: aload_3
/*     */           //   125: ldc_w 'arbitrary'
/*     */           //   128: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */           //   131: ifeq -> 137
/*     */           //   134: iconst_3
/*     */           //   135: istore #4
/*     */           //   137: iload #4
/*     */           //   139: tableswitch default -> 192, 0 -> 168, 1 -> 174, 2 -> 180, 3 -> 186
/*     */           //   168: getstatic net/minecraft/commands/arguments/selector/EntitySelectorParser.ORDER_NEAREST : Ljava/util/function/BiConsumer;
/*     */           //   171: goto -> 212
/*     */           //   174: getstatic net/minecraft/commands/arguments/selector/EntitySelectorParser.ORDER_FURTHEST : Ljava/util/function/BiConsumer;
/*     */           //   177: goto -> 212
/*     */           //   180: getstatic net/minecraft/commands/arguments/selector/EntitySelectorParser.ORDER_RANDOM : Ljava/util/function/BiConsumer;
/*     */           //   183: goto -> 212
/*     */           //   186: getstatic net/minecraft/commands/arguments/selector/EntitySelector.ORDER_ARBITRARY : Ljava/util/function/BiConsumer;
/*     */           //   189: goto -> 212
/*     */           //   192: aload_0
/*     */           //   193: invokevirtual getReader : ()Lcom/mojang/brigadier/StringReader;
/*     */           //   196: iload_1
/*     */           //   197: invokevirtual setCursor : (I)V
/*     */           //   200: getstatic net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.ERROR_SORT_UNKNOWN : Lcom/mojang/brigadier/exceptions/DynamicCommandExceptionType;
/*     */           //   203: aload_0
/*     */           //   204: invokevirtual getReader : ()Lcom/mojang/brigadier/StringReader;
/*     */           //   207: aload_2
/*     */           //   208: invokevirtual createWithContext : (Lcom/mojang/brigadier/ImmutableStringReader;Ljava/lang/Object;)Lcom/mojang/brigadier/exceptions/CommandSyntaxException;
/*     */           //   211: athrow
/*     */           //   212: invokevirtual setOrder : (Ljava/util/function/BiConsumer;)V
/*     */           //   215: aload_0
/*     */           //   216: iconst_1
/*     */           //   217: invokevirtual setSorted : (Z)V
/*     */           //   220: return
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #165	-> 0
/*     */           //   #166	-> 8
/*     */           //   #167	-> 16
/*     */           //   #168	-> 25
/*     */           //   #169	-> 168
/*     */           //   #170	-> 174
/*     */           //   #171	-> 180
/*     */           //   #172	-> 186
/*     */           //   #174	-> 192
/*     */           //   #175	-> 200
/*     */           //   #168	-> 212
/*     */           //   #178	-> 215
/*     */           //   #179	-> 220
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   0	221	0	parser	Lnet/minecraft/commands/arguments/selector/EntitySelectorParser;
/*     */           //   8	213	1	start	I
/*     */           //   16	205	2	name	Ljava/lang/String;
/* 179 */         }, s -> (!s.isCurrentEntity() && !s.isSorted()), (Component)Component.translatable("argument.entity.options.sort.description"));
/*     */     
/* 181 */     register("gamemode", parser -> {
/*     */           parser.setSuggestions(());
/*     */ 
/*     */ 
/*     */           
/*     */           int start = parser.getReader().getCursor();
/*     */ 
/*     */ 
/*     */           
/*     */           boolean inverted = parser.shouldInvertValue();
/*     */ 
/*     */ 
/*     */           
/*     */           if (parser.hasGamemodeNotEquals() && !inverted) {
/*     */             parser.getReader().setCursor(start);
/*     */ 
/*     */ 
/*     */             
/*     */             throw ERROR_INAPPLICABLE_OPTION.createWithContext(parser.getReader(), "gamemode");
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/*     */           String name = parser.getReader().readUnquotedString();
/*     */ 
/*     */ 
/*     */           
/*     */           GameType expected = GameType.byName(name, null);
/*     */ 
/*     */ 
/*     */           
/*     */           if (expected == null) {
/*     */             parser.getReader().setCursor(start);
/*     */ 
/*     */ 
/*     */             
/*     */             throw ERROR_GAME_MODE_INVALID.createWithContext(parser.getReader(), name);
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/*     */           parser.setIncludesEntities(false);
/*     */ 
/*     */ 
/*     */           
/*     */           parser.addPredicate(());
/*     */ 
/*     */ 
/*     */           
/*     */           if (inverted) {
/*     */             parser.setHasGamemodeNotEquals(true);
/*     */           } else {
/*     */             parser.setHasGamemodeEquals(true);
/*     */           } 
/* 235 */         }, s -> !s.hasGamemodeEquals(), (Component)Component.translatable("argument.entity.options.gamemode.description"));
/*     */     
/* 237 */     register("team", parser -> {
/*     */           boolean inverted = parser.shouldInvertValue();
/*     */ 
/*     */           
/*     */           String expected = parser.getReader().readUnquotedString();
/*     */ 
/*     */           
/*     */           parser.addPredicate(());
/*     */           
/*     */           if (inverted) {
/*     */             parser.setHasTeamNotEquals(true);
/*     */           } else {
/*     */             parser.setHasTeamEquals(true);
/*     */           } 
/* 251 */         }, s -> !s.hasTeamEquals(), (Component)Component.translatable("argument.entity.options.team.description"));
/*     */     
/* 253 */     register("type", parser -> {
/*     */           parser.setSuggestions(());
/*     */ 
/*     */           
/*     */           int start = parser.getReader().getCursor();
/*     */ 
/*     */           
/*     */           boolean inverted = parser.shouldInvertValue();
/*     */ 
/*     */           
/*     */           if (parser.isTypeLimitedInversely() && !inverted) {
/*     */             parser.getReader().setCursor(start);
/*     */             
/*     */             throw ERROR_INAPPLICABLE_OPTION.createWithContext(parser.getReader(), "type");
/*     */           } 
/*     */           
/*     */           if (inverted) {
/*     */             parser.setTypeLimitedInversely();
/*     */           }
/*     */           
/*     */           if (parser.isTag()) {
/*     */             TagKey<EntityType<?>> id = TagKey.create(Registries.ENTITY_TYPE, Identifier.read(parser.getReader()));
/*     */             
/*     */             parser.addPredicate(());
/*     */           } else {
/*     */             Identifier id = Identifier.read(parser.getReader());
/*     */             
/*     */             EntityType<?> type = (EntityType)BuiltInRegistries.ENTITY_TYPE.getOptional(id).orElseThrow(());
/*     */             
/*     */             if (Objects.equals(EntityType.PLAYER, type) && !inverted) {
/*     */               parser.setIncludesEntities(false);
/*     */             }
/*     */             
/*     */             parser.addPredicate(());
/*     */             
/*     */             if (!inverted) {
/*     */               parser.limitToType(type);
/*     */             }
/*     */           } 
/* 292 */         }, s -> !s.isTypeLimited(), (Component)Component.translatable("argument.entity.options.type.description"));
/*     */     
/* 294 */     register("tag", parser -> {
/*     */           boolean inverted = parser.shouldInvertValue();
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           String tag = parser.getReader().readUnquotedString();
/*     */ 
/*     */ 
/*     */           
/*     */           parser.addPredicate(());
/* 305 */         }, s -> true, (Component)Component.translatable("argument.entity.options.tag.description"));
/*     */     
/* 307 */     register("nbt", parser -> {
/*     */           boolean inverted = parser.shouldInvertValue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           CompoundTag tag = TagParser.parseCompoundAsArgument(parser.getReader());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           parser.addPredicate(());
/* 324 */         }, s -> true, (Component)Component.translatable("argument.entity.options.nbt.description"));
/*     */     
/* 326 */     register("scores", parser -> {
/*     */           StringReader reader = parser.getReader();
/*     */ 
/*     */           
/*     */           Map<String, MinMaxBounds.Ints> expected = Maps.newHashMap();
/*     */ 
/*     */           
/*     */           reader.expect('{');
/*     */ 
/*     */           
/*     */           reader.skipWhitespace();
/*     */ 
/*     */           
/*     */           while (reader.canRead() && reader.peek() != '}') {
/*     */             reader.skipWhitespace();
/*     */ 
/*     */             
/*     */             String name = reader.readUnquotedString();
/*     */             
/*     */             reader.skipWhitespace();
/*     */             
/*     */             reader.expect('=');
/*     */             
/*     */             reader.skipWhitespace();
/*     */             
/*     */             MinMaxBounds.Ints value = MinMaxBounds.Ints.fromReader(reader);
/*     */             
/*     */             expected.put(name, value);
/*     */             
/*     */             reader.skipWhitespace();
/*     */             
/*     */             if (reader.canRead() && reader.peek() == ',') {
/*     */               reader.skip();
/*     */             }
/*     */           } 
/*     */           
/*     */           reader.expect('}');
/*     */           
/*     */           if (!expected.isEmpty()) {
/*     */             parser.addPredicate(());
/*     */           }
/*     */           
/*     */           parser.setHasScores(true);
/* 369 */         }, s -> !s.hasScores(), (Component)Component.translatable("argument.entity.options.scores.description"));
/*     */     
/* 371 */     register("advancements", parser -> {
/*     */           StringReader reader = parser.getReader();
/*     */           
/*     */           Map<Identifier, Predicate<AdvancementProgress>> expected = Maps.newHashMap();
/*     */           
/*     */           reader.expect('{');
/*     */           
/*     */           reader.skipWhitespace();
/*     */           
/*     */           while (reader.canRead() && reader.peek() != '}') {
/*     */             reader.skipWhitespace();
/*     */             
/*     */             Identifier name = Identifier.read(reader);
/*     */             
/*     */             reader.skipWhitespace();
/*     */             
/*     */             reader.expect('=');
/*     */             
/*     */             reader.skipWhitespace();
/*     */             
/*     */             if (reader.canRead() && reader.peek() == '{') {
/*     */               Map<String, Predicate<CriterionProgress>> progress = Maps.newHashMap();
/*     */               
/*     */               reader.skipWhitespace();
/*     */               
/*     */               reader.expect('{');
/*     */               
/*     */               reader.skipWhitespace();
/*     */               
/*     */               while (reader.canRead() && reader.peek() != '}') {
/*     */                 reader.skipWhitespace();
/*     */                 
/*     */                 String criterion = reader.readUnquotedString();
/*     */                 
/*     */                 reader.skipWhitespace();
/*     */                 
/*     */                 reader.expect('=');
/*     */                 
/*     */                 reader.skipWhitespace();
/*     */                 
/*     */                 boolean value = reader.readBoolean();
/*     */                 
/*     */                 progress.put(criterion, ());
/*     */                 
/*     */                 reader.skipWhitespace();
/*     */                 
/*     */                 if (reader.canRead() && reader.peek() == ',') {
/*     */                   reader.skip();
/*     */                 }
/*     */               } 
/*     */               
/*     */               reader.skipWhitespace();
/*     */               
/*     */               reader.expect('}');
/*     */               
/*     */               reader.skipWhitespace();
/*     */               
/*     */               expected.put(name, ());
/*     */             } else {
/*     */               boolean value = reader.readBoolean();
/*     */               
/*     */               expected.put(name, ());
/*     */             } 
/*     */             
/*     */             reader.skipWhitespace();
/*     */             
/*     */             if (reader.canRead() && reader.peek() == ',') {
/*     */               reader.skip();
/*     */             }
/*     */           } 
/*     */           
/*     */           reader.expect('}');
/*     */           if (!expected.isEmpty()) {
/*     */             parser.addPredicate(());
/*     */             parser.setIncludesEntities(false);
/*     */           } 
/*     */           parser.setHasAdvancements(true);
/* 448 */         }, s -> !s.hasAdvancements(), (Component)Component.translatable("argument.entity.options.advancements.description"));
/*     */     
/* 450 */     register("predicate", parser -> {
/*     */           boolean inverted = parser.shouldInvertValue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           ResourceKey<LootItemCondition> id = ResourceKey.create(Registries.PREDICATE, Identifier.read(parser.getReader()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           parser.addPredicate(());
/* 470 */         }, s -> true, (Component)Component.translatable("argument.entity.options.predicate.description"));
/*     */   }
/*     */   
/*     */   public static Modifier get(EntitySelectorParser parser, String key, int start) throws CommandSyntaxException {
/* 474 */     Option option = OPTIONS.get(key);
/* 475 */     if (option != null) {
/* 476 */       if (option.canUse.test(parser)) {
/* 477 */         return option.modifier;
/*     */       }
/* 479 */       throw ERROR_INAPPLICABLE_OPTION.createWithContext(parser.getReader(), key);
/*     */     } 
/*     */     
/* 482 */     parser.getReader().setCursor(start);
/* 483 */     throw ERROR_UNKNOWN_OPTION.createWithContext(parser.getReader(), key);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void suggestNames(EntitySelectorParser parser, SuggestionsBuilder builder) {
/* 488 */     String lowerPrefix = builder.getRemaining().toLowerCase(Locale.ROOT);
/* 489 */     for (Map.Entry<String, Option> entry : OPTIONS.entrySet()) {
/* 490 */       if (((Option)entry.getValue()).canUse.test(parser) && ((String)entry.getKey()).toLowerCase(Locale.ROOT).startsWith(lowerPrefix))
/* 491 */         builder.suggest((String)entry.getKey() + "=", (Message)((Option)entry.getValue()).description); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class Option
/*     */     extends Record {
/*     */     private final EntitySelectorOptions.Modifier modifier;
/*     */     private final Predicate<EntitySelectorParser> canUse;
/*     */     
/*     */     private Option(EntitySelectorOptions.Modifier modifier, Predicate<EntitySelectorParser> canUse, Component description) {
/* 501 */       this.modifier = modifier; this.canUse = canUse; this.description = description; } private final Component description; public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Option;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #501	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Option; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Option;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #501	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Option; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Option;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #501	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Option;
/* 501 */       //   0	8	1	o	Ljava/lang/Object; } public EntitySelectorOptions.Modifier modifier() { return this.modifier; } public Predicate<EntitySelectorParser> canUse() { return this.canUse; } public Component description() { return this.description; }
/*     */   
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Modifier {
/*     */     void handle(EntitySelectorParser param1EntitySelectorParser) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */