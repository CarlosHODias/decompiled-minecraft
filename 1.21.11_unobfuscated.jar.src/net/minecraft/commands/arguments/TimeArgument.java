/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class TimeArgument
/*     */   implements ArgumentType<Integer> {
/*  26 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0d", "0s", "0t", "0" }); private static final Dynamic2CommandExceptionType ERROR_TICK_COUNT_TOO_LOW;
/*  27 */   private static final SimpleCommandExceptionType ERROR_INVALID_UNIT = new SimpleCommandExceptionType((Message)Component.translatable("argument.time.invalid_unit")); static {
/*  28 */     ERROR_TICK_COUNT_TOO_LOW = new Dynamic2CommandExceptionType((value, limit) -> Component.translatableEscape("argument.time.tick_count_too_low", new Object[] { limit, value }));
/*     */   }
/*  30 */   private static final Object2IntMap<String> UNITS = (Object2IntMap<String>)new Object2IntOpenHashMap(); private final int minimum;
/*     */   
/*     */   static {
/*  33 */     UNITS.put("d", 24000);
/*  34 */     UNITS.put("s", 20);
/*  35 */     UNITS.put("t", 1);
/*  36 */     UNITS.put("", 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private TimeArgument(int minimum) {
/*  42 */     this.minimum = minimum;
/*     */   }
/*     */   
/*     */   public static TimeArgument time() {
/*  46 */     return new TimeArgument(0);
/*     */   }
/*     */   
/*     */   public static TimeArgument time(int minimum) {
/*  50 */     return new TimeArgument(minimum);
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer parse(StringReader reader) throws CommandSyntaxException {
/*  55 */     float value = reader.readFloat();
/*  56 */     String unit = reader.readUnquotedString();
/*  57 */     int factor = UNITS.getOrDefault(unit, 0);
/*  58 */     if (factor == 0) {
/*  59 */       throw ERROR_INVALID_UNIT.createWithContext(reader);
/*     */     }
/*     */     
/*  62 */     int ticks = Math.round(value * factor);
/*  63 */     if (ticks < this.minimum) {
/*  64 */       throw ERROR_TICK_COUNT_TOO_LOW.createWithContext(reader, ticks, this.minimum);
/*     */     }
/*     */     
/*  67 */     return ticks;
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/*  72 */     StringReader reader = new StringReader(builder.getRemaining());
/*     */     try {
/*  74 */       reader.readFloat();
/*  75 */     } catch (CommandSyntaxException ignored) {
/*  76 */       return builder.buildFuture();
/*     */     } 
/*     */     
/*  79 */     return SharedSuggestionProvider.suggest((Iterable)UNITS.keySet(), builder.createOffset(builder.getStart() + reader.getCursor()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/*  84 */     return EXAMPLES;
/*     */   }
/*     */   
/*     */   public static class Info implements ArgumentTypeInfo<TimeArgument, Info.Template> {
/*     */     public final class Template implements ArgumentTypeInfo.Template<TimeArgument> {
/*     */       private final int min;
/*     */       
/*     */       private Template(int min) {
/*  92 */         this.min = min;
/*     */       }
/*     */ 
/*     */       
/*     */       public TimeArgument instantiate(CommandBuildContext context) {
/*  97 */         return TimeArgument.time(this.min);
/*     */       }
/*     */ 
/*     */       
/*     */       public ArgumentTypeInfo<TimeArgument, ?> type() {
/* 102 */         return TimeArgument.Info.this;
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 108 */       out.writeInt(template.min);
/*     */     }
/*     */ 
/*     */     
/*     */     public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 113 */       int min = in.readInt();
/* 114 */       return new Template(min);
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeToJson(Template template, JsonObject out) {
/* 119 */       out.addProperty("min", template.min);
/*     */     }
/*     */ 
/*     */     
/*     */     public Template unpack(TimeArgument argument) {
/* 124 */       return new Template(argument.minimum);
/*     */     }
/*     */   }
/*     */   
/*     */   public final class Template implements ArgumentTypeInfo.Template<TimeArgument> {
/*     */     private final int min;
/*     */     
/*     */     private Template(int min) {
/*     */       this.min = min;
/*     */     }
/*     */     
/*     */     public TimeArgument instantiate(CommandBuildContext context) {
/*     */       return TimeArgument.time(this.min);
/*     */     }
/*     */     
/*     */     public ArgumentTypeInfo<TimeArgument, ?> type() {
/*     */       return TimeArgument.Info.this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/TimeArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */