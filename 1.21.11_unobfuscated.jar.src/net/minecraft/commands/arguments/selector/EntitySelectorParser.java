/*     */ package net.minecraft.commands.arguments.selector;
/*     */ 
/*     */ import com.google.common.primitives.Doubles;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.PermissionSetSupplier;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ToFloatFunction;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntitySelectorParser
/*     */ {
/*     */   public static final char SYNTAX_SELECTOR_START = '@';
/*     */   private static final char SYNTAX_OPTIONS_START = '[';
/*     */   private static final char SYNTAX_OPTIONS_END = ']';
/*     */   public static final char SYNTAX_OPTIONS_KEY_VALUE_SEPARATOR = '=';
/*     */   private static final char SYNTAX_OPTIONS_SEPARATOR = ',';
/*     */   public static final char SYNTAX_NOT = '!';
/*     */   public static final char SYNTAX_TAG = '#';
/*     */   private static final char SELECTOR_NEAREST_PLAYER = 'p';
/*     */   private static final char SELECTOR_ALL_PLAYERS = 'a';
/*     */   private static final char SELECTOR_RANDOM_PLAYERS = 'r';
/*     */   private static final char SELECTOR_CURRENT_ENTITY = 's';
/*     */   private static final char SELECTOR_ALL_ENTITIES = 'e';
/*     */   private static final char SELECTOR_NEAREST_ENTITY = 'n';
/*  52 */   public static final SimpleCommandExceptionType ERROR_INVALID_NAME_OR_UUID = new SimpleCommandExceptionType((Message)Component.translatable("argument.entity.invalid")); public static final DynamicCommandExceptionType ERROR_UNKNOWN_SELECTOR_TYPE; static {
/*  53 */     ERROR_UNKNOWN_SELECTOR_TYPE = new DynamicCommandExceptionType(type -> Component.translatableEscape("argument.entity.selector.unknown", new Object[] { type }));
/*  54 */   } public static final SimpleCommandExceptionType ERROR_SELECTORS_NOT_ALLOWED = new SimpleCommandExceptionType((Message)Component.translatable("argument.entity.selector.not_allowed"));
/*  55 */   public static final SimpleCommandExceptionType ERROR_MISSING_SELECTOR_TYPE = new SimpleCommandExceptionType((Message)Component.translatable("argument.entity.selector.missing")); public static final DynamicCommandExceptionType ERROR_EXPECTED_OPTION_VALUE; public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_NEAREST; public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_FURTHEST;
/*  56 */   public static final SimpleCommandExceptionType ERROR_EXPECTED_END_OF_OPTIONS = new SimpleCommandExceptionType((Message)Component.translatable("argument.entity.options.unterminated")); public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_RANDOM; public static final BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> SUGGEST_NOTHING; static {
/*  57 */     ERROR_EXPECTED_OPTION_VALUE = new DynamicCommandExceptionType(name -> Component.translatableEscape("argument.entity.options.valueless", new Object[] { name }));
/*     */     
/*  59 */     ORDER_NEAREST = ((p, c) -> c.sort(()));
/*  60 */     ORDER_FURTHEST = ((p, c) -> c.sort(()));
/*  61 */     ORDER_RANDOM = ((p, c) -> Collections.shuffle(c));
/*     */     
/*  63 */     SUGGEST_NOTHING = ((b, s) -> b.buildFuture());
/*     */   }
/*     */   private final StringReader reader;
/*     */   private final boolean allowSelectors;
/*     */   private int maxResults;
/*     */   private boolean includesEntities;
/*     */   private boolean worldLimited;
/*     */   private MinMaxBounds.Doubles distance;
/*     */   private MinMaxBounds.Ints level;
/*     */   private Double x;
/*     */   private Double y;
/*     */   private Double z;
/*     */   private Double deltaX;
/*     */   private Double deltaY;
/*     */   private Double deltaZ;
/*     */   private MinMaxBounds.FloatDegrees rotX;
/*     */   private MinMaxBounds.FloatDegrees rotY;
/*  80 */   private final List<Predicate<Entity>> predicates = new ArrayList<>();
/*  81 */   private BiConsumer<Vec3, List<? extends Entity>> order = EntitySelector.ORDER_ARBITRARY;
/*     */   private boolean currentEntity;
/*     */   private String playerName;
/*     */   private int startPosition;
/*     */   private UUID entityUUID;
/*  86 */   private BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> suggestions = SUGGEST_NOTHING;
/*     */   private boolean hasNameEquals;
/*     */   private boolean hasNameNotEquals;
/*     */   private boolean isLimited;
/*     */   private boolean isSorted;
/*     */   private boolean hasGamemodeEquals;
/*     */   private boolean hasGamemodeNotEquals;
/*     */   private boolean hasTeamEquals;
/*     */   private boolean hasTeamNotEquals;
/*     */   private EntityType<?> type;
/*     */   private boolean typeInverse;
/*     */   private boolean hasScores;
/*     */   private boolean hasAdvancements;
/*     */   private boolean usesSelectors;
/*     */   
/*     */   public EntitySelectorParser(StringReader reader, boolean allowSelectors) {
/* 102 */     this.reader = reader;
/* 103 */     this.allowSelectors = allowSelectors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <S> boolean allowSelectors(S source) {
/* 111 */     if (source instanceof PermissionSetSupplier) { PermissionSetSupplier sender = (PermissionSetSupplier)source; if (sender.permissions().hasPermission(Permissions.COMMANDS_ENTITY_SELECTORS)); }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static boolean allowSelectors(PermissionSetSupplier source) {
/* 119 */     return source.permissions().hasPermission(Permissions.COMMANDS_ENTITY_SELECTORS);
/*     */   }
/*     */   public EntitySelector getSelector() {
/*     */     AABB aabb;
/*     */     Function<Vec3, Vec3> position;
/* 124 */     if (this.deltaX != null || this.deltaY != null || this.deltaZ != null) {
/* 125 */       aabb = createAabb((this.deltaX == null) ? 0.0D : this.deltaX, (this.deltaY == null) ? 0.0D : this.deltaY, (this.deltaZ == null) ? 0.0D : this.deltaZ);
/* 126 */     } else if (this.distance != null && this.distance.max().isPresent()) {
/* 127 */       double maxRange = (Double)this.distance.max().get();
/* 128 */       aabb = new AABB(-maxRange, -maxRange, -maxRange, maxRange + 1.0D, maxRange + 1.0D, maxRange + 1.0D);
/*     */     } else {
/* 130 */       aabb = null;
/*     */     } 
/*     */     
/* 133 */     if (this.x == null && this.y == null && this.z == null) {
/* 134 */       position = (o -> o);
/*     */     } else {
/* 136 */       position = (o -> new Vec3((this.x == null) ? o.x : this.x, (this.y == null) ? o.y : this.y, (this.z == null) ? o.z : this.z));
/*     */     } 
/* 138 */     return new EntitySelector(this.maxResults, this.includesEntities, this.worldLimited, List.copyOf(this.predicates), this.distance, position, aabb, this.order, this.currentEntity, this.playerName, this.entityUUID, this.type, this.usesSelectors);
/*     */   }
/*     */   
/*     */   private AABB createAabb(double x, double y, double z) {
/* 142 */     boolean xNeg = (x < 0.0D);
/* 143 */     boolean yNeg = (y < 0.0D);
/* 144 */     boolean zNeg = (z < 0.0D);
/* 145 */     double xMin = xNeg ? x : 0.0D;
/* 146 */     double yMin = yNeg ? y : 0.0D;
/* 147 */     double zMin = zNeg ? z : 0.0D;
/* 148 */     double xMax = (xNeg ? 0.0D : x) + 1.0D;
/* 149 */     double yMax = (yNeg ? 0.0D : y) + 1.0D;
/* 150 */     double zMax = (zNeg ? 0.0D : z) + 1.0D;
/* 151 */     return new AABB(xMin, yMin, zMin, xMax, yMax, zMax);
/*     */   }
/*     */   
/*     */   private void finalizePredicates() {
/* 155 */     if (this.rotX != null) {
/* 156 */       this.predicates.add(createRotationPredicate(this.rotX, Entity::getXRot));
/*     */     }
/* 158 */     if (this.rotY != null) {
/* 159 */       this.predicates.add(createRotationPredicate(this.rotY, Entity::getYRot));
/*     */     }
/* 161 */     if (this.level != null)
/* 162 */       this.predicates.add(e -> {
/*     */             if (e instanceof ServerPlayer) {
/*     */               ServerPlayer serverPlayer = (ServerPlayer)e;
/*     */               if (this.level.matches(serverPlayer.experienceLevel));
/*     */             } 
/*     */             return false;
/* 168 */           });  } private Predicate<Entity> createRotationPredicate(MinMaxBounds.FloatDegrees range, ToFloatFunction<Entity> function) { float min = Mth.wrapDegrees((Float)range.min().orElse(0.0F));
/* 169 */     float max = Mth.wrapDegrees((Float)range.max().orElse(359.0F));
/* 170 */     return e -> {
/*     */         float rotation = Mth.wrapDegrees(function.applyAsFloat(e));
/*     */         
/* 173 */         return (min > max) ? ((rotation >= min || rotation <= max)) : (
/*     */           
/* 175 */           (rotation >= min && rotation <= max));
/*     */       }; }
/*     */   
/*     */   protected void parseSelector() throws CommandSyntaxException {
/*     */     boolean selectOnlyAlive;
/* 180 */     this.usesSelectors = true;
/* 181 */     this.suggestions = this::suggestSelector;
/* 182 */     if (!this.reader.canRead()) {
/* 183 */       throw ERROR_MISSING_SELECTOR_TYPE.createWithContext(this.reader);
/*     */     }
/* 185 */     int start = this.reader.getCursor();
/* 186 */     char type = this.reader.read();
/*     */ 
/*     */     
/* 189 */     switch (type) {
/*     */       case 'p':
/* 191 */         this.maxResults = 1;
/* 192 */         this.includesEntities = false;
/* 193 */         this.order = ORDER_NEAREST;
/* 194 */         limitToType(EntityType.PLAYER);
/* 195 */         selectOnlyAlive = false;
/*     */         break;
/*     */       case 'a':
/* 198 */         this.maxResults = Integer.MAX_VALUE;
/* 199 */         this.includesEntities = false;
/* 200 */         this.order = EntitySelector.ORDER_ARBITRARY;
/* 201 */         limitToType(EntityType.PLAYER);
/* 202 */         selectOnlyAlive = false;
/*     */         break;
/*     */       case 'r':
/* 205 */         this.maxResults = 1;
/* 206 */         this.includesEntities = false;
/* 207 */         this.order = ORDER_RANDOM;
/* 208 */         limitToType(EntityType.PLAYER);
/* 209 */         selectOnlyAlive = false;
/*     */         break;
/*     */       case 's':
/* 212 */         this.maxResults = 1;
/* 213 */         this.includesEntities = true;
/* 214 */         this.currentEntity = true;
/*     */         
/* 216 */         selectOnlyAlive = false;
/*     */         break;
/*     */       case 'e':
/* 219 */         this.maxResults = Integer.MAX_VALUE;
/* 220 */         this.includesEntities = true;
/* 221 */         this.order = EntitySelector.ORDER_ARBITRARY;
/* 222 */         selectOnlyAlive = true;
/*     */         break;
/*     */       case 'n':
/* 225 */         this.maxResults = 1;
/* 226 */         this.includesEntities = true;
/* 227 */         this.order = ORDER_NEAREST;
/* 228 */         selectOnlyAlive = true;
/*     */         break;
/*     */       default:
/* 231 */         this.reader.setCursor(start);
/* 232 */         throw ERROR_UNKNOWN_SELECTOR_TYPE.createWithContext(this.reader, "@" + String.valueOf(type));
/*     */     } 
/*     */ 
/*     */     
/* 236 */     if (selectOnlyAlive) {
/* 237 */       this.predicates.add(Entity::isAlive);
/*     */     }
/* 239 */     this.suggestions = this::suggestOpenOptions;
/* 240 */     if (this.reader.canRead() && this.reader.peek() == '[') {
/* 241 */       this.reader.skip();
/* 242 */       this.suggestions = this::suggestOptionsKeyOrClose;
/* 243 */       parseOptions();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void parseNameOrUUID() throws CommandSyntaxException {
/* 248 */     if (this.reader.canRead()) {
/* 249 */       this.suggestions = this::suggestName;
/*     */     }
/* 251 */     int start = this.reader.getCursor();
/* 252 */     String name = this.reader.readString();
/*     */     
/*     */     try {
/* 255 */       this.entityUUID = UUID.fromString(name);
/* 256 */       this.includesEntities = true;
/* 257 */     } catch (IllegalArgumentException ex) {
/* 258 */       if (name.isEmpty() || name.length() > 16) {
/* 259 */         this.reader.setCursor(start);
/* 260 */         throw ERROR_INVALID_NAME_OR_UUID.createWithContext(this.reader);
/*     */       } 
/* 262 */       this.includesEntities = false;
/* 263 */       this.playerName = name;
/*     */     } 
/*     */     
/* 266 */     this.maxResults = 1;
/*     */   }
/*     */   
/*     */   protected void parseOptions() throws CommandSyntaxException {
/* 270 */     this.suggestions = this::suggestOptionsKey;
/* 271 */     this.reader.skipWhitespace();
/* 272 */     while (this.reader.canRead() && this.reader.peek() != ']') {
/* 273 */       this.reader.skipWhitespace();
/* 274 */       int start = this.reader.getCursor();
/* 275 */       String key = this.reader.readString();
/* 276 */       EntitySelectorOptions.Modifier modifier = EntitySelectorOptions.get(this, key, start);
/* 277 */       this.reader.skipWhitespace();
/* 278 */       if (!this.reader.canRead() || this.reader.peek() != '=') {
/* 279 */         this.reader.setCursor(start);
/* 280 */         throw ERROR_EXPECTED_OPTION_VALUE.createWithContext(this.reader, key);
/*     */       } 
/* 282 */       this.reader.skip();
/* 283 */       this.reader.skipWhitespace();
/*     */       
/* 285 */       this.suggestions = SUGGEST_NOTHING;
/* 286 */       modifier.handle(this);
/* 287 */       this.reader.skipWhitespace();
/*     */       
/* 289 */       this.suggestions = this::suggestOptionsNextOrClose;
/* 290 */       if (this.reader.canRead()) {
/* 291 */         if (this.reader.peek() == ',') {
/* 292 */           this.reader.skip();
/* 293 */           this.suggestions = this::suggestOptionsKey; continue;
/* 294 */         }  if (this.reader.peek() == ']') {
/*     */           break;
/*     */         }
/* 297 */         throw ERROR_EXPECTED_END_OF_OPTIONS.createWithContext(this.reader);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 302 */     if (this.reader.canRead()) {
/* 303 */       this.reader.skip();
/* 304 */       this.suggestions = SUGGEST_NOTHING;
/*     */     } else {
/* 306 */       throw ERROR_EXPECTED_END_OF_OPTIONS.createWithContext(this.reader);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean shouldInvertValue() {
/* 311 */     this.reader.skipWhitespace();
/* 312 */     if (this.reader.canRead() && this.reader.peek() == '!') {
/* 313 */       this.reader.skip();
/* 314 */       this.reader.skipWhitespace();
/* 315 */       return true;
/*     */     } 
/* 317 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isTag() {
/* 321 */     this.reader.skipWhitespace();
/* 322 */     if (this.reader.canRead() && this.reader.peek() == '#') {
/* 323 */       this.reader.skip();
/* 324 */       this.reader.skipWhitespace();
/* 325 */       return true;
/*     */     } 
/* 327 */     return false;
/*     */   }
/*     */   
/*     */   public StringReader getReader() {
/* 331 */     return this.reader;
/*     */   }
/*     */   
/*     */   public void addPredicate(Predicate<Entity> predicate) {
/* 335 */     this.predicates.add(predicate);
/*     */   }
/*     */   
/*     */   public void setWorldLimited() {
/* 339 */     this.worldLimited = true;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.Doubles getDistance() {
/* 343 */     return this.distance;
/*     */   }
/*     */   
/*     */   public void setDistance(MinMaxBounds.Doubles distance) {
/* 347 */     this.distance = distance;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.Ints getLevel() {
/* 351 */     return this.level;
/*     */   }
/*     */   
/*     */   public void setLevel(MinMaxBounds.Ints level) {
/* 355 */     this.level = level;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.FloatDegrees getRotX() {
/* 359 */     return this.rotX;
/*     */   }
/*     */   
/*     */   public void setRotX(MinMaxBounds.FloatDegrees rotX) {
/* 363 */     this.rotX = rotX;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.FloatDegrees getRotY() {
/* 367 */     return this.rotY;
/*     */   }
/*     */   
/*     */   public void setRotY(MinMaxBounds.FloatDegrees rotY) {
/* 371 */     this.rotY = rotY;
/*     */   }
/*     */   
/*     */   public Double getX() {
/* 375 */     return this.x;
/*     */   }
/*     */   
/*     */   public Double getY() {
/* 379 */     return this.y;
/*     */   }
/*     */   
/*     */   public Double getZ() {
/* 383 */     return this.z;
/*     */   }
/*     */   
/*     */   public void setX(double x) {
/* 387 */     this.x = x;
/*     */   }
/*     */   
/*     */   public void setY(double y) {
/* 391 */     this.y = y;
/*     */   }
/*     */   
/*     */   public void setZ(double z) {
/* 395 */     this.z = z;
/*     */   }
/*     */   
/*     */   public void setDeltaX(double deltaX) {
/* 399 */     this.deltaX = deltaX;
/*     */   }
/*     */   
/*     */   public void setDeltaY(double deltaY) {
/* 403 */     this.deltaY = deltaY;
/*     */   }
/*     */   
/*     */   public void setDeltaZ(double deltaZ) {
/* 407 */     this.deltaZ = deltaZ;
/*     */   }
/*     */   
/*     */   public Double getDeltaX() {
/* 411 */     return this.deltaX;
/*     */   }
/*     */   
/*     */   public Double getDeltaY() {
/* 415 */     return this.deltaY;
/*     */   }
/*     */   
/*     */   public Double getDeltaZ() {
/* 419 */     return this.deltaZ;
/*     */   }
/*     */   
/*     */   public void setMaxResults(int maxResults) {
/* 423 */     this.maxResults = maxResults;
/*     */   }
/*     */   
/*     */   public void setIncludesEntities(boolean includesEntities) {
/* 427 */     this.includesEntities = includesEntities;
/*     */   }
/*     */   
/*     */   public BiConsumer<Vec3, List<? extends Entity>> getOrder() {
/* 431 */     return this.order;
/*     */   }
/*     */   
/*     */   public void setOrder(BiConsumer<Vec3, List<? extends Entity>> order) {
/* 435 */     this.order = order;
/*     */   }
/*     */   
/*     */   public EntitySelector parse() throws CommandSyntaxException {
/* 439 */     this.startPosition = this.reader.getCursor();
/* 440 */     this.suggestions = this::suggestNameOrSelector;
/* 441 */     if (this.reader.canRead() && this.reader.peek() == '@') {
/* 442 */       if (!this.allowSelectors) {
/* 443 */         throw ERROR_SELECTORS_NOT_ALLOWED.createWithContext(this.reader);
/*     */       }
/* 445 */       this.reader.skip();
/* 446 */       parseSelector();
/*     */     } else {
/* 448 */       parseNameOrUUID();
/*     */     } 
/* 450 */     finalizePredicates();
/* 451 */     return getSelector();
/*     */   }
/*     */   
/*     */   private static void fillSelectorSuggestions(SuggestionsBuilder builder) {
/* 455 */     builder.suggest("@p", (Message)Component.translatable("argument.entity.selector.nearestPlayer"));
/* 456 */     builder.suggest("@a", (Message)Component.translatable("argument.entity.selector.allPlayers"));
/* 457 */     builder.suggest("@r", (Message)Component.translatable("argument.entity.selector.randomPlayer"));
/* 458 */     builder.suggest("@s", (Message)Component.translatable("argument.entity.selector.self"));
/* 459 */     builder.suggest("@e", (Message)Component.translatable("argument.entity.selector.allEntities"));
/* 460 */     builder.suggest("@n", (Message)Component.translatable("argument.entity.selector.nearestEntity"));
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestNameOrSelector(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 464 */     names.accept(builder);
/* 465 */     if (this.allowSelectors) {
/* 466 */       fillSelectorSuggestions(builder);
/*     */     }
/* 468 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestName(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 472 */     SuggestionsBuilder sub = builder.createOffset(this.startPosition);
/* 473 */     names.accept(sub);
/* 474 */     return builder.add(sub).buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestSelector(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 478 */     SuggestionsBuilder sub = builder.createOffset(builder.getStart() - 1);
/* 479 */     fillSelectorSuggestions(sub);
/* 480 */     builder.add(sub);
/* 481 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOpenOptions(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 485 */     builder.suggest(String.valueOf('['));
/* 486 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOptionsKeyOrClose(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 490 */     builder.suggest(String.valueOf(']'));
/* 491 */     EntitySelectorOptions.suggestNames(this, builder);
/* 492 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOptionsKey(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 496 */     EntitySelectorOptions.suggestNames(this, builder);
/* 497 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOptionsNextOrClose(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 501 */     builder.suggest(String.valueOf(','));
/* 502 */     builder.suggest(String.valueOf(']'));
/* 503 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestEquals(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 507 */     builder.suggest(String.valueOf('='));
/* 508 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   public boolean isCurrentEntity() {
/* 512 */     return this.currentEntity;
/*     */   }
/*     */   
/*     */   public void setSuggestions(BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> suggestions) {
/* 516 */     this.suggestions = suggestions;
/*     */   }
/*     */   
/*     */   public CompletableFuture<Suggestions> fillSuggestions(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 520 */     return this.suggestions.apply(builder.createOffset(this.reader.getCursor()), names);
/*     */   }
/*     */   
/*     */   public boolean hasNameEquals() {
/* 524 */     return this.hasNameEquals;
/*     */   }
/*     */   
/*     */   public void setHasNameEquals(boolean hasNameEquals) {
/* 528 */     this.hasNameEquals = hasNameEquals;
/*     */   }
/*     */   
/*     */   public boolean hasNameNotEquals() {
/* 532 */     return this.hasNameNotEquals;
/*     */   }
/*     */   
/*     */   public void setHasNameNotEquals(boolean hasNameNotEquals) {
/* 536 */     this.hasNameNotEquals = hasNameNotEquals;
/*     */   }
/*     */   
/*     */   public boolean isLimited() {
/* 540 */     return this.isLimited;
/*     */   }
/*     */   
/*     */   public void setLimited(boolean limited) {
/* 544 */     this.isLimited = limited;
/*     */   }
/*     */   
/*     */   public boolean isSorted() {
/* 548 */     return this.isSorted;
/*     */   }
/*     */   
/*     */   public void setSorted(boolean sorted) {
/* 552 */     this.isSorted = sorted;
/*     */   }
/*     */   
/*     */   public boolean hasGamemodeEquals() {
/* 556 */     return this.hasGamemodeEquals;
/*     */   }
/*     */   
/*     */   public void setHasGamemodeEquals(boolean hasGamemodeEquals) {
/* 560 */     this.hasGamemodeEquals = hasGamemodeEquals;
/*     */   }
/*     */   
/*     */   public boolean hasGamemodeNotEquals() {
/* 564 */     return this.hasGamemodeNotEquals;
/*     */   }
/*     */   
/*     */   public void setHasGamemodeNotEquals(boolean hasGamemodeNotEquals) {
/* 568 */     this.hasGamemodeNotEquals = hasGamemodeNotEquals;
/*     */   }
/*     */   
/*     */   public boolean hasTeamEquals() {
/* 572 */     return this.hasTeamEquals;
/*     */   }
/*     */   
/*     */   public void setHasTeamEquals(boolean hasTeamEquals) {
/* 576 */     this.hasTeamEquals = hasTeamEquals;
/*     */   }
/*     */   
/*     */   public boolean hasTeamNotEquals() {
/* 580 */     return this.hasTeamNotEquals;
/*     */   }
/*     */   
/*     */   public void setHasTeamNotEquals(boolean hasTeamNotEquals) {
/* 584 */     this.hasTeamNotEquals = hasTeamNotEquals;
/*     */   }
/*     */   
/*     */   public void limitToType(EntityType<?> type) {
/* 588 */     this.type = type;
/*     */   }
/*     */   
/*     */   public void setTypeLimitedInversely() {
/* 592 */     this.typeInverse = true;
/*     */   }
/*     */   
/*     */   public boolean isTypeLimited() {
/* 596 */     return (this.type != null);
/*     */   }
/*     */   
/*     */   public boolean isTypeLimitedInversely() {
/* 600 */     return this.typeInverse;
/*     */   }
/*     */   
/*     */   public boolean hasScores() {
/* 604 */     return this.hasScores;
/*     */   }
/*     */   
/*     */   public void setHasScores(boolean hasScores) {
/* 608 */     this.hasScores = hasScores;
/*     */   }
/*     */   
/*     */   public boolean hasAdvancements() {
/* 612 */     return this.hasAdvancements;
/*     */   }
/*     */   
/*     */   public void setHasAdvancements(boolean hasAdvancements) {
/* 616 */     this.hasAdvancements = hasAdvancements;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/selector/EntitySelectorParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */