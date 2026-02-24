/*     */ package net.minecraft.world.level.gamerules;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Objects;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.flag.FeatureElement;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GameRule<T>
/*     */   implements FeatureElement
/*     */ {
/*     */   private final GameRuleCategory category;
/*     */   private final GameRuleType gameRuleType;
/*     */   private final ArgumentType<T> argument;
/*     */   private final GameRules.VisitorCaller<T> visitorCaller;
/*     */   private final Codec<T> valueCodec;
/*     */   private final ToIntFunction<T> commandResultFunction;
/*     */   private final T defaultValue;
/*     */   private final FeatureFlagSet requiredFeatures;
/*     */   
/*     */   public GameRule(GameRuleCategory category, GameRuleType gameRuleType, ArgumentType<T> argument, GameRules.VisitorCaller<T> visitorCaller, Codec<T> valueCodec, ToIntFunction<T> commandResultFunction, T defaultValue, FeatureFlagSet requiredFeatures) {
/*  37 */     this.category = category;
/*  38 */     this.gameRuleType = gameRuleType;
/*  39 */     this.argument = argument;
/*  40 */     this.visitorCaller = visitorCaller;
/*  41 */     this.valueCodec = valueCodec;
/*  42 */     this.commandResultFunction = commandResultFunction;
/*  43 */     this.defaultValue = defaultValue;
/*  44 */     this.requiredFeatures = requiredFeatures;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  49 */     return id();
/*     */   }
/*     */   
/*     */   public String id() {
/*  53 */     return getIdentifier().toShortString();
/*     */   }
/*     */   
/*     */   public Identifier getIdentifier() {
/*  57 */     return Objects.<Identifier>requireNonNull(BuiltInRegistries.GAME_RULE.getKey(this));
/*     */   }
/*     */   
/*     */   public String getDescriptionId() {
/*  61 */     return Util.makeDescriptionId("gamerule", getIdentifier());
/*     */   }
/*     */   
/*     */   public String serialize(T value) {
/*  65 */     return value.toString();
/*     */   }
/*     */   
/*     */   public DataResult<T> deserialize(String value) {
/*     */     try {
/*  70 */       StringReader reader = new StringReader(value);
/*  71 */       T result = (T)this.argument.parse(reader);
/*  72 */       if (reader.canRead()) {
/*  73 */         return DataResult.error(() -> "Failed to deserialize; trailing characters", result);
/*     */       }
/*  75 */       return DataResult.success(result);
/*  76 */     } catch (CommandSyntaxException ignored) {
/*  77 */       return DataResult.error(() -> "Failed to deserialize");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<T> valueClass() {
/*  83 */     return (Class)this.defaultValue.getClass();
/*     */   }
/*     */   
/*     */   public void callVisitor(GameRuleTypeVisitor visitor) {
/*  87 */     this.visitorCaller.call(visitor, this);
/*     */   }
/*     */   
/*     */   public int getCommandResult(T value) {
/*  91 */     return this.commandResultFunction.applyAsInt(value);
/*     */   }
/*     */   
/*     */   public GameRuleCategory category() {
/*  95 */     return this.category;
/*     */   }
/*     */   
/*     */   public GameRuleType gameRuleType() {
/*  99 */     return this.gameRuleType;
/*     */   }
/*     */   
/*     */   public ArgumentType<T> argument() {
/* 103 */     return this.argument;
/*     */   }
/*     */   
/*     */   public Codec<T> valueCodec() {
/* 107 */     return this.valueCodec;
/*     */   }
/*     */   
/*     */   public T defaultValue() {
/* 111 */     return this.defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public FeatureFlagSet requiredFeatures() {
/* 116 */     return this.requiredFeatures;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/gamerules/GameRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */