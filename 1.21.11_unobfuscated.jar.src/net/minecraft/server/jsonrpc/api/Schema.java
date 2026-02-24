/*     */ package net.minecraft.server.jsonrpc.api;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.net.URI;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.server.jsonrpc.methods.DiscoveryService;
/*     */ import net.minecraft.server.jsonrpc.methods.GameRulesService;
/*     */ import net.minecraft.server.jsonrpc.methods.IpBanlistService;
/*     */ import net.minecraft.server.jsonrpc.methods.Message;
/*     */ import net.minecraft.server.jsonrpc.methods.OperatorService;
/*     */ import net.minecraft.server.jsonrpc.methods.PlayerService;
/*     */ import net.minecraft.server.jsonrpc.methods.ServerStateService;
/*     */ import net.minecraft.server.permissions.PermissionLevel;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.gamerules.GameRuleType;
/*     */ 
/*     */ public final class Schema<T> extends Record {
/*     */   private final Optional<URI> reference;
/*     */   private final List<String> type;
/*     */   private final Optional<Schema<?>> items;
/*     */   private final Map<String, Schema<?>> properties;
/*     */   private final List<String> enumValues;
/*     */   private final Codec<T> codec;
/*     */   public static final Codec<? extends Schema<?>> CODEC;
/*     */   
/*  34 */   public Schema(Optional<URI> reference, List<String> type, Optional<Schema<?>> items, Map<String, Schema<?>> properties, List<String> enumValues, Codec<T> codec) { this.reference = reference; this.type = type; this.items = items; this.properties = properties; this.enumValues = enumValues; this.codec = codec; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/api/Schema;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/Schema;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*  34 */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/Schema<TT;>; } public Optional<URI> reference() { return this.reference; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/api/Schema;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/Schema;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/Schema<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/api/Schema;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/Schema;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*  34 */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/Schema<TT;>; } public List<String> type() { return this.type; } public Optional<Schema<?>> items() { return this.items; } public Map<String, Schema<?>> properties() { return this.properties; } public List<String> enumValues() { return this.enumValues; } public Codec<T> codec() { return this.codec; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  42 */     CODEC = Codec.recursive("Schema", subCodec -> RecordCodecBuilder.create(())).validate(schema -> (schema == null) ? DataResult.error(()) : DataResult.success(schema));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Codec<Schema<T>> typedCodec() {
/*  52 */     return (Codec)CODEC;
/*     */   }
/*     */   
/*     */   public Schema<T> info() {
/*  56 */     return new Schema(this.reference, this.type, this.items.map(Schema::info), (Map<String, Schema<?>>)this.properties.entrySet().stream().collect(java.util.stream.Collectors.toMap(Map.Entry::getKey, b -> ((Schema)b.getValue()).info())), this.enumValues, this.codec);
/*     */   }
/*     */   
/*  59 */   private static final List<SchemaComponent<?>> SCHEMA_REGISTRY = new java.util.ArrayList<>();
/*  60 */   public static final Schema<Boolean> BOOL_SCHEMA = ofType("boolean", (Codec<Boolean>)Codec.BOOL);
/*  61 */   public static final Schema<Integer> INT_SCHEMA = ofType("integer", (Codec<Integer>)Codec.INT);
/*  62 */   public static final Schema<com.mojang.datafixers.util.Either<Boolean, Integer>> BOOL_OR_INT_SCHEMA = ofTypes(List.of("boolean", "integer"), Codec.either((Codec)Codec.BOOL, (Codec)Codec.INT));
/*  63 */   public static final Schema<Float> NUMBER_SCHEMA = ofType("number", (Codec<Float>)Codec.FLOAT);
/*  64 */   public static final Schema<String> STRING_SCHEMA = ofType("string", (Codec<String>)Codec.STRING);
/*  65 */   public static final Schema<java.util.UUID> UUID_SCHEMA = ofType("string", net.minecraft.core.UUIDUtil.CODEC);
/*  66 */   public static final Schema<DiscoveryService.DiscoverResponse> DISCOVERY_SCHEMA = ofType("string", DiscoveryService.DiscoverResponse.CODEC.codec());
/*     */   
/*  68 */   public static final SchemaComponent<Difficulty> DIFFICULTY_SCHEMA = registerSchema("difficulty", ofEnum(Difficulty::values, (Codec<Difficulty>)Difficulty.CODEC));
/*     */   
/*  70 */   public static final SchemaComponent<GameType> GAME_TYPE_SCHEMA = registerSchema("game_type", ofEnum(GameType::values, (Codec<GameType>)GameType.CODEC));
/*  71 */   public static final Schema<PermissionLevel> PERMISSION_LEVEL_SCHEMA = ofType("integer", PermissionLevel.INT_CODEC);
/*     */   
/*  73 */   public static final SchemaComponent<PlayerDto> PLAYER_SCHEMA = registerSchema("player", (Schema)record(PlayerDto.CODEC.codec())
/*  74 */       .withField("id", UUID_SCHEMA)
/*  75 */       .withField("name", STRING_SCHEMA));
/*     */   
/*  77 */   public static final SchemaComponent<DiscoveryService.DiscoverInfo> VERSION_SCHEMA = registerSchema("version", (Schema)record(DiscoveryService.DiscoverInfo.CODEC.codec())
/*  78 */       .withField("name", STRING_SCHEMA)
/*  79 */       .withField("protocol", INT_SCHEMA));
/*     */   
/*  81 */   public static final SchemaComponent<ServerStateService.ServerState> SERVER_STATE_SCHEMA = registerSchema("server_state", (Schema)record(ServerStateService.ServerState.CODEC)
/*  82 */       .withField("started", BOOL_SCHEMA)
/*  83 */       .withField("players", PLAYER_SCHEMA.asRef().asArray())
/*  84 */       .withField("version", VERSION_SCHEMA.asRef()));
/*     */   
/*  86 */   public static final Schema<GameRuleType> RULE_TYPE_SCHEMA = ofEnum(GameRuleType::values);
/*     */   
/*  88 */   public static final SchemaComponent<GameRulesService.GameRuleUpdate<?>> TYPED_GAME_RULE_SCHEMA = registerSchema("typed_game_rule", (Schema)record(GameRulesService.GameRuleUpdate.TYPED_CODEC)
/*  89 */       .withField("key", STRING_SCHEMA)
/*  90 */       .withField("value", BOOL_OR_INT_SCHEMA)
/*  91 */       .withField("type", RULE_TYPE_SCHEMA));
/*     */   
/*  93 */   public static final SchemaComponent<GameRulesService.GameRuleUpdate<?>> UNTYPED_GAME_RULE_SCHEMA = registerSchema("untyped_game_rule", (Schema)record(GameRulesService.GameRuleUpdate.CODEC)
/*  94 */       .withField("key", STRING_SCHEMA)
/*  95 */       .withField("value", BOOL_OR_INT_SCHEMA));
/*     */   
/*  97 */   public static final SchemaComponent<Message> MESSAGE_SCHEMA = registerSchema("message", (Schema)record(Message.CODEC)
/*  98 */       .withField("literal", STRING_SCHEMA)
/*  99 */       .withField("translatable", STRING_SCHEMA)
/* 100 */       .withField("translatableParams", STRING_SCHEMA.asArray()));
/*     */   
/* 102 */   public static final SchemaComponent<ServerStateService.SystemMessage> SYSTEM_MESSAGE_SCHEMA = registerSchema("system_message", (Schema)record(ServerStateService.SystemMessage.CODEC)
/* 103 */       .withField("message", MESSAGE_SCHEMA.asRef())
/* 104 */       .withField("overlay", BOOL_SCHEMA)
/* 105 */       .withField("receivingPlayers", PLAYER_SCHEMA.asRef().asArray()));
/*     */   
/* 107 */   public static final SchemaComponent<PlayerService.KickDto> KICK_PLAYER_SCHEMA = registerSchema("kick_player", (Schema)record(PlayerService.KickDto.CODEC.codec())
/* 108 */       .withField("message", MESSAGE_SCHEMA.asRef())
/* 109 */       .withField("player", PLAYER_SCHEMA.asRef()));
/*     */   
/* 111 */   public static final SchemaComponent<OperatorService.OperatorDto> OPERATOR_SCHEMA = registerSchema("operator", (Schema)record(OperatorService.OperatorDto.CODEC.codec())
/* 112 */       .withField("player", PLAYER_SCHEMA.asRef())
/* 113 */       .withField("bypassesPlayerLimit", BOOL_SCHEMA)
/* 114 */       .withField("permissionLevel", INT_SCHEMA));
/*     */   
/* 116 */   public static final SchemaComponent<IpBanlistService.IncomingIpBanDto> INCOMING_IP_BAN_SCHEMA = registerSchema("incoming_ip_ban", (Schema)record(IpBanlistService.IncomingIpBanDto.CODEC.codec())
/* 117 */       .withField("player", PLAYER_SCHEMA.asRef())
/* 118 */       .withField("ip", STRING_SCHEMA)
/* 119 */       .withField("reason", STRING_SCHEMA)
/* 120 */       .withField("source", STRING_SCHEMA)
/* 121 */       .withField("expires", STRING_SCHEMA));
/*     */   
/* 123 */   public static final SchemaComponent<IpBanlistService.IpBanDto> IP_BAN_SCHEMA = registerSchema("ip_ban", (Schema)record(IpBanlistService.IpBanDto.CODEC.codec())
/* 124 */       .withField("ip", STRING_SCHEMA)
/* 125 */       .withField("reason", STRING_SCHEMA)
/* 126 */       .withField("source", STRING_SCHEMA)
/* 127 */       .withField("expires", STRING_SCHEMA));
/*     */   
/* 129 */   public static final SchemaComponent<net.minecraft.server.jsonrpc.methods.BanlistService.UserBanDto> PLAYER_BAN_SCHEMA = registerSchema("user_ban", (Schema)record(net.minecraft.server.jsonrpc.methods.BanlistService.UserBanDto.CODEC.codec())
/* 130 */       .withField("player", PLAYER_SCHEMA.asRef())
/* 131 */       .withField("reason", STRING_SCHEMA)
/* 132 */       .withField("source", STRING_SCHEMA)
/* 133 */       .withField("expires", STRING_SCHEMA));
/*     */   
/*     */   private static <T> SchemaComponent<T> registerSchema(String name, Schema<T> schema) {
/* 136 */     SchemaComponent<T> entry = new SchemaComponent<>(name, ReferenceUtil.createLocalReference(name), schema);
/* 137 */     SCHEMA_REGISTRY.add(entry);
/* 138 */     return entry;
/*     */   }
/*     */   
/*     */   public static List<SchemaComponent<?>> getSchemaRegistry() {
/* 142 */     return SCHEMA_REGISTRY;
/*     */   }
/*     */   
/*     */   public static <T> Schema<T> ofRef(URI ref, Codec<T> codec) {
/* 146 */     return new Schema<>(Optional.of(ref), List.of(), Optional.empty(), Map.of(), List.of(), codec);
/*     */   }
/*     */   
/*     */   public static <T> Schema<T> ofType(String type, Codec<T> codec) {
/* 150 */     return ofTypes(List.of(type), codec);
/*     */   }
/*     */   
/*     */   public static <T> Schema<T> ofTypes(List<String> types, Codec<T> codec) {
/* 154 */     return new Schema<>(Optional.empty(), types, Optional.empty(), Map.of(), List.of(), codec);
/*     */   }
/*     */   
/*     */   public static <E extends Enum<E> & StringRepresentable> Schema<E> ofEnum(Supplier<E[]> values) {
/* 158 */     return ofEnum(values, (Codec<E>)StringRepresentable.fromEnum(values));
/*     */   }
/*     */   
/*     */   public static <E extends Enum<E> & StringRepresentable> Schema<E> ofEnum(Supplier<E[]> values, Codec<E> codec) {
/* 162 */     List<String> enumValues = java.util.stream.Stream.<Enum>of((Enum[])values.get())
/* 163 */       .map(rec$ -> ((StringRepresentable)rec$).getSerializedName())
/* 164 */       .toList();
/*     */     
/* 166 */     return ofEnum(enumValues, codec);
/*     */   }
/*     */   
/*     */   public static <T> Schema<T> ofEnum(List<String> enumValues, Codec<T> codec) {
/* 170 */     return new Schema<>(Optional.empty(), List.of("string"), Optional.empty(), Map.of(), enumValues, codec);
/*     */   }
/*     */   
/*     */   public static <T> Schema<List<T>> arrayOf(Schema<?> item, Codec<T> codec) {
/* 174 */     return new Schema<>(Optional.empty(), List.of("array"), Optional.of(item), Map.of(), List.of(), codec.listOf());
/*     */   }
/*     */   
/*     */   public static <T> Schema<T> record(Codec<T> codec) {
/* 178 */     return new Schema<>(Optional.empty(), List.of("object"), Optional.empty(), Map.of(), List.of(), codec);
/*     */   }
/*     */   
/*     */   private static <T> Schema<T> record(Map<String, Schema<?>> properties, Codec<T> codec) {
/* 182 */     return new Schema<>(Optional.empty(), List.of("object"), Optional.empty(), properties, List.of(), codec);
/*     */   }
/*     */   
/*     */   public Schema<T> withField(String name, Schema<?> field) {
/* 186 */     HashMap<String, Schema<?>> properties = new HashMap<>(this.properties);
/* 187 */     properties.put(name, field);
/* 188 */     return record(properties, this.codec);
/*     */   }
/*     */   
/*     */   public Schema<List<T>> asArray() {
/* 192 */     return arrayOf(this, this.codec);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/api/Schema.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */