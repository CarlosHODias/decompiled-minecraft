/*     */ package net.minecraft.gametest.framework;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.functions.CommandFunction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.ServerFunctionManager;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ import net.minecraft.world.level.gamerules.GameRuleMap;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public interface TestEnvironmentDefinition {
/*     */   public static final Codec<TestEnvironmentDefinition> DIRECT_CODEC;
/*     */   
/*     */   static MapCodec<? extends TestEnvironmentDefinition> bootstrap(Registry<MapCodec<? extends TestEnvironmentDefinition>> registry) {
/*  33 */     Registry.register(registry, "all_of", AllOf.CODEC);
/*  34 */     Registry.register(registry, "game_rules", SetGameRules.CODEC);
/*  35 */     Registry.register(registry, "time_of_day", TimeOfDay.CODEC);
/*  36 */     Registry.register(registry, "weather", Weather.CODEC);
/*  37 */     return (MapCodec<? extends TestEnvironmentDefinition>)Registry.register(registry, "function", Functions.CODEC);
/*     */   }
/*     */   static {
/*  40 */     DIRECT_CODEC = net.minecraft.core.registries.BuiltInRegistries.TEST_ENVIRONMENT_DEFINITION_TYPE.byNameCodec().dispatch(TestEnvironmentDefinition::codec, c -> c);
/*     */   }
/*  42 */   public static final Codec<Holder<TestEnvironmentDefinition>> CODEC = (Codec<Holder<TestEnvironmentDefinition>>)RegistryFileCodec.create(Registries.TEST_ENVIRONMENT, DIRECT_CODEC);
/*     */   default void teardown(ServerLevel level) {}
/*     */   
/*     */   void setup(ServerLevel paramServerLevel);
/*     */   
/*     */   MapCodec<? extends TestEnvironmentDefinition> codec();
/*     */   
/*     */   public static final class Weather extends Record implements TestEnvironmentDefinition { private final Type weather;
/*     */     public static final MapCodec<Weather> CODEC;
/*     */     
/*  52 */     public Weather(Type weather) { this.weather = weather; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #52	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #52	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #52	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;
/*  52 */       //   0	8	1	o	Ljava/lang/Object; } public Type weather() { return this.weather; }
/*     */     
/*  54 */     public enum Type implements StringRepresentable { CLEAR("clear", 100000, 0, false, false),
/*  55 */       RAIN("rain", 0, 100000, true, false),
/*  56 */       THUNDER("thunder", 0, 100000, true, true);
/*     */ 
/*     */       
/*  59 */       public static final Codec<Type> CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/*     */       
/*     */       private final String id;
/*     */       private final int clearTime;
/*     */       private final int rainTime;
/*     */       private final boolean raining;
/*     */       private final boolean thundering;
/*     */       
/*     */       Type(String id, int clearTime, int rainTime, boolean raining, boolean thundering) {
/*  68 */         this.id = id;
/*  69 */         this.clearTime = clearTime;
/*  70 */         this.rainTime = rainTime;
/*  71 */         this.raining = raining;
/*  72 */         this.thundering = thundering;
/*     */       }
/*     */       
/*     */       void apply(ServerLevel level) {
/*  76 */         level.setWeatherParameters(this.clearTime, this.rainTime, this.raining, this.thundering);
/*     */       }
/*     */ 
/*     */       
/*     */       public String getSerializedName() {
/*  81 */         return this.id;
/*     */       } }
/*     */     
/*     */     static {
/*  85 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Type.CODEC.fieldOf("weather").forGetter(Weather::weather)).apply((Applicative)i, Weather::new));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setup(ServerLevel level) {
/*  91 */       this.weather.apply(level);
/*     */     }
/*     */ 
/*     */     
/*     */     public void teardown(ServerLevel level) {
/*  96 */       level.resetWeatherCycle();
/*     */     }
/*     */     
/*     */     public MapCodec<Weather> codec()
/*     */     {
/* 101 */       return CODEC; } }
/*     */    public enum Type implements StringRepresentable {
/*     */     CLEAR("clear", 100000, 0, false, false), RAIN("rain", 0, 100000, true, false), THUNDER("thunder", 0, 100000, true, true); public static final Codec<Type> CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values); private final String id; private final int clearTime; private final int rainTime; private final boolean raining; private final boolean thundering; Type(String id, int clearTime, int rainTime, boolean raining, boolean thundering) { this.id = id; this.clearTime = clearTime; this.rainTime = rainTime; this.raining = raining; this.thundering = thundering; } void apply(ServerLevel level) { level.setWeatherParameters(this.clearTime, this.rainTime, this.raining, this.thundering); } public String getSerializedName() { return this.id; }
/*     */   } public static final class TimeOfDay extends Record implements TestEnvironmentDefinition {
/* 105 */     private final int time; public TimeOfDay(int time) { this.time = time; } public static final MapCodec<TimeOfDay> CODEC; public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$TimeOfDay;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$TimeOfDay; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$TimeOfDay;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$TimeOfDay; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$TimeOfDay;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$TimeOfDay;
/* 105 */       //   0	8	1	o	Ljava/lang/Object; } public int time() { return this.time; } static {
/* 106 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("time").forGetter(TimeOfDay::time)).apply((Applicative)i, TimeOfDay::new));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setup(ServerLevel level) {
/* 112 */       level.setDayTime(this.time);
/*     */     }
/*     */ 
/*     */     
/*     */     public MapCodec<TimeOfDay> codec() {
/* 117 */       return CODEC;
/*     */     } }
/*     */   public static final class SetGameRules extends Record implements TestEnvironmentDefinition { private final GameRuleMap gameRulesMap; public static final MapCodec<SetGameRules> CODEC;
/*     */     
/* 121 */     public SetGameRules(GameRuleMap gameRulesMap) { this.gameRulesMap = gameRulesMap; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #121	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #121	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #121	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;
/* 121 */       //   0	8	1	o	Ljava/lang/Object; } public GameRuleMap gameRulesMap() { return this.gameRulesMap; } static {
/* 122 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)GameRuleMap.CODEC.fieldOf("rules").forGetter(SetGameRules::gameRulesMap)).apply((Applicative)i, SetGameRules::new));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setup(ServerLevel level) {
/* 128 */       GameRules gameRules = level.getGameRules();
/* 129 */       MinecraftServer server = level.getServer();
/* 130 */       gameRules.setAll(this.gameRulesMap, server);
/*     */     }
/*     */ 
/*     */     
/*     */     public void teardown(ServerLevel level) {
/* 135 */       this.gameRulesMap.keySet().forEach(gameRule -> resetRule(level, level));
/*     */     }
/*     */     
/*     */     private <T> void resetRule(ServerLevel level, GameRule<T> gameRule) {
/* 139 */       level.getGameRules().set(gameRule, gameRule.defaultValue(), level.getServer());
/*     */     }
/*     */ 
/*     */     
/*     */     public MapCodec<SetGameRules> codec() {
/* 144 */       return CODEC;
/*     */     } }
/*     */   public static final class Functions extends Record implements TestEnvironmentDefinition { private final Optional<Identifier> setupFunction; private final Optional<Identifier> teardownFunction;
/*     */     
/* 148 */     public Functions(Optional<Identifier> setupFunction, Optional<Identifier> teardownFunction) { this.setupFunction = setupFunction; this.teardownFunction = teardownFunction; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #148	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #148	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #148	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;
/* 148 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<Identifier> setupFunction() { return this.setupFunction; } public Optional<Identifier> teardownFunction() { return this.teardownFunction; }
/* 149 */      private static final Logger LOGGER = com.mojang.logging.LogUtils.getLogger(); public static final MapCodec<Functions> CODEC;
/*     */     static {
/* 151 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.optionalFieldOf("setup").forGetter(Functions::setupFunction), (App)Identifier.CODEC.optionalFieldOf("teardown").forGetter(Functions::teardownFunction)).apply((Applicative)i, Functions::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setup(ServerLevel level) {
/* 158 */       this.setupFunction.ifPresent(p -> run(level, p));
/*     */     }
/*     */ 
/*     */     
/*     */     public void teardown(ServerLevel level) {
/* 163 */       this.teardownFunction.ifPresent(p -> run(level, p));
/*     */     }
/*     */     
/*     */     private static void run(ServerLevel level, Identifier functionId) {
/* 167 */       MinecraftServer server = level.getServer();
/* 168 */       ServerFunctionManager functions = server.getFunctions();
/* 169 */       Optional<CommandFunction<CommandSourceStack>> function = functions.get(functionId);
/* 170 */       if (function.isPresent()) {
/* 171 */         CommandSourceStack source = server.createCommandSourceStack()
/* 172 */           .withPermission((PermissionSet)net.minecraft.server.permissions.LevelBasedPermissionSet.GAMEMASTER)
/* 173 */           .withSuppressedOutput()
/* 174 */           .withLevel(level);
/* 175 */         functions.execute(function.get(), source);
/*     */       } else {
/* 177 */         LOGGER.error("Test Batch failed for non-existent function {}", functionId);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public MapCodec<Functions> codec() {
/* 183 */       return CODEC;
/*     */     } }
/*     */   public static final class AllOf extends Record implements TestEnvironmentDefinition { private final List<Holder<TestEnvironmentDefinition>> definitions; public static final MapCodec<AllOf> CODEC;
/*     */     
/* 187 */     public AllOf(List<Holder<TestEnvironmentDefinition>> definitions) { this.definitions = definitions; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #187	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #187	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #187	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;
/* 187 */       //   0	8	1	o	Ljava/lang/Object; } public List<Holder<TestEnvironmentDefinition>> definitions() { return this.definitions; } static {
/* 188 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TestEnvironmentDefinition.CODEC.listOf().fieldOf("definitions").forGetter(AllOf::definitions)).apply((Applicative)i, AllOf::new));
/*     */     }
/*     */ 
/*     */     
/*     */     public AllOf(TestEnvironmentDefinition... defs) {
/* 193 */       this(java.util.Arrays.<TestEnvironmentDefinition>stream(defs).map(Holder::direct).toList());
/*     */     }
/*     */ 
/*     */     
/*     */     public void setup(ServerLevel level) {
/* 198 */       this.definitions.forEach(b -> ((TestEnvironmentDefinition)b.value()).setup(level));
/*     */     }
/*     */ 
/*     */     
/*     */     public void teardown(ServerLevel level) {
/* 203 */       this.definitions.forEach(b -> ((TestEnvironmentDefinition)b.value()).teardown(level));
/*     */     }
/*     */ 
/*     */     
/*     */     public MapCodec<AllOf> codec() {
/* 208 */       return CODEC;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/TestEnvironmentDefinition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */