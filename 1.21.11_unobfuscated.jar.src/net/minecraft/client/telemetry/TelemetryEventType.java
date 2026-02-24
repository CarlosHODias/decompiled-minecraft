/*     */ package net.minecraft.client.telemetry;
/*     */ import com.mojang.authlib.minecraft.TelemetryEvent;
/*     */ import com.mojang.authlib.minecraft.TelemetrySession;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.longs.LongList;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.telemetry.events.GameLoadTimesEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ 
/*     */ public class TelemetryEventType {
/*  18 */   private static final Map<String, TelemetryEventType> REGISTRY = (Map<String, TelemetryEventType>)new Object2ObjectLinkedOpenHashMap(); public static final Codec<TelemetryEventType> CODEC;
/*     */   static {
/*  20 */     CODEC = Codec.STRING.comapFlatMap(key -> { TelemetryEventType type = REGISTRY.get(key); return (type != null) ? DataResult.success(type) : DataResult.error(()); }, TelemetryEventType::id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  28 */   private static final List<TelemetryProperty<?>> GLOBAL_PROPERTIES = List.of(TelemetryProperty.USER_ID, TelemetryProperty.CLIENT_ID, TelemetryProperty.MINECRAFT_SESSION_ID, TelemetryProperty.GAME_VERSION, TelemetryProperty.OPERATING_SYSTEM, TelemetryProperty.PLATFORM, TelemetryProperty.CLIENT_MODDED, TelemetryProperty.LAUNCHER_NAME, TelemetryProperty.EVENT_TIMESTAMP_UTC, TelemetryProperty.OPT_IN);
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
/*     */ 
/*     */   
/*  44 */   private static final List<TelemetryProperty<?>> WORLD_SESSION_PROPERTIES = Stream.<TelemetryProperty<?>>concat(GLOBAL_PROPERTIES.stream(), Stream.of((TelemetryProperty<?>[])new TelemetryProperty[] { TelemetryProperty.WORLD_SESSION_ID, TelemetryProperty.SERVER_MODDED, TelemetryProperty.SERVER_TYPE
/*     */ 
/*     */ 
/*     */         
/*  48 */         })).toList();
/*     */   
/*  50 */   public static final TelemetryEventType WORLD_LOADED = builder("world_loaded", "WorldLoaded")
/*  51 */     .defineAll(WORLD_SESSION_PROPERTIES)
/*  52 */     .<TelemetryProperty.GameMode>define(TelemetryProperty.GAME_MODE)
/*  53 */     .<String>define(TelemetryProperty.REALMS_MAP_CONTENT)
/*  54 */     .register();
/*     */   
/*  56 */   public static final TelemetryEventType PERFORMANCE_METRICS = builder("performance_metrics", "PerformanceMetrics")
/*  57 */     .defineAll(WORLD_SESSION_PROPERTIES)
/*  58 */     .<LongList>define(TelemetryProperty.FRAME_RATE_SAMPLES)
/*  59 */     .<LongList>define(TelemetryProperty.RENDER_TIME_SAMPLES)
/*  60 */     .<LongList>define(TelemetryProperty.USED_MEMORY_SAMPLES)
/*  61 */     .<Integer>define(TelemetryProperty.NUMBER_OF_SAMPLES)
/*  62 */     .<Integer>define(TelemetryProperty.RENDER_DISTANCE)
/*  63 */     .<Integer>define(TelemetryProperty.DEDICATED_MEMORY_KB)
/*  64 */     .optIn()
/*  65 */     .register();
/*     */   
/*  67 */   public static final TelemetryEventType WORLD_LOAD_TIMES = builder("world_load_times", "WorldLoadTimes")
/*  68 */     .defineAll(WORLD_SESSION_PROPERTIES)
/*  69 */     .<Integer>define(TelemetryProperty.WORLD_LOAD_TIME_MS)
/*  70 */     .<Boolean>define(TelemetryProperty.NEW_WORLD)
/*  71 */     .optIn()
/*  72 */     .register();
/*     */   
/*  74 */   public static final TelemetryEventType WORLD_UNLOADED = builder("world_unloaded", "WorldUnloaded")
/*  75 */     .defineAll(WORLD_SESSION_PROPERTIES)
/*  76 */     .<Integer>define(TelemetryProperty.SECONDS_SINCE_LOAD)
/*  77 */     .<Integer>define(TelemetryProperty.TICKS_SINCE_LOAD)
/*  78 */     .register();
/*     */   
/*  80 */   public static final TelemetryEventType ADVANCEMENT_MADE = builder("advancement_made", "AdvancementMade")
/*  81 */     .defineAll(WORLD_SESSION_PROPERTIES)
/*  82 */     .<String>define(TelemetryProperty.ADVANCEMENT_ID)
/*  83 */     .<Long>define(TelemetryProperty.ADVANCEMENT_GAME_TIME)
/*  84 */     .optIn()
/*  85 */     .register();
/*     */   
/*  87 */   public static final TelemetryEventType GAME_LOAD_TIMES = builder("game_load_times", "GameLoadTimes")
/*  88 */     .defineAll(GLOBAL_PROPERTIES)
/*  89 */     .<GameLoadTimesEvent.Measurement>define(TelemetryProperty.LOAD_TIME_TOTAL_TIME_MS)
/*  90 */     .<GameLoadTimesEvent.Measurement>define(TelemetryProperty.LOAD_TIME_PRE_WINDOW_MS)
/*  91 */     .<GameLoadTimesEvent.Measurement>define(TelemetryProperty.LOAD_TIME_BOOTSTRAP_MS)
/*  92 */     .<GameLoadTimesEvent.Measurement>define(TelemetryProperty.LOAD_TIME_LOADING_OVERLAY_MS)
/*  93 */     .optIn()
/*  94 */     .register();
/*     */   
/*     */   private final String id;
/*     */   
/*     */   private final String exportKey;
/*     */   private final List<TelemetryProperty<?>> properties;
/*     */   private final boolean isOptIn;
/*     */   private final MapCodec<TelemetryEventInstance> codec;
/*     */   
/*     */   private TelemetryEventType(String id, String exportKey, List<TelemetryProperty<?>> properties, boolean isOptIn) {
/* 104 */     this.id = id;
/* 105 */     this.exportKey = exportKey;
/* 106 */     this.properties = properties;
/* 107 */     this.isOptIn = isOptIn;
/* 108 */     this.codec = TelemetryPropertyMap.createCodec(properties).xmap(map -> new TelemetryEventInstance(this, map), TelemetryEventInstance::properties);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder builder(String id, String exportKey) {
/* 115 */     return new Builder(id, exportKey);
/*     */   }
/*     */   
/*     */   public String id() {
/* 119 */     return this.id;
/*     */   }
/*     */   
/*     */   public List<TelemetryProperty<?>> properties() {
/* 123 */     return this.properties;
/*     */   }
/*     */   
/*     */   public MapCodec<TelemetryEventInstance> codec() {
/* 127 */     return this.codec;
/*     */   }
/*     */   
/*     */   public boolean isOptIn() {
/* 131 */     return this.isOptIn;
/*     */   }
/*     */   
/*     */   public TelemetryEvent export(TelemetrySession session, TelemetryPropertyMap input) {
/* 135 */     TelemetryEvent output = session.createNewEvent(this.exportKey);
/* 136 */     for (TelemetryProperty<?> property : this.properties) {
/* 137 */       property.export(input, (com.mojang.authlib.minecraft.TelemetryPropertyContainer)output);
/*     */     }
/* 139 */     return output;
/*     */   }
/*     */   
/*     */   public <T> boolean contains(TelemetryProperty<T> property) {
/* 143 */     return this.properties.contains(property);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 148 */     return "TelemetryEventType[" + this.id + "]";
/*     */   }
/*     */   
/*     */   public MutableComponent title() {
/* 152 */     return makeTranslation("title");
/*     */   }
/*     */   
/*     */   public MutableComponent description() {
/* 156 */     return makeTranslation("description");
/*     */   }
/*     */   
/*     */   private MutableComponent makeTranslation(String suffix) {
/* 160 */     return Component.translatable("telemetry.event." + this.id + "." + suffix);
/*     */   }
/*     */   
/*     */   public static List<TelemetryEventType> values() {
/* 164 */     return List.copyOf(REGISTRY.values());
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final String id;
/*     */     private final String exportKey;
/* 170 */     private final List<TelemetryProperty<?>> properties = new ArrayList<>();
/*     */     private boolean isOptIn;
/*     */     
/*     */     private Builder(String id, String exportKey) {
/* 174 */       this.id = id;
/* 175 */       this.exportKey = exportKey;
/*     */     }
/*     */     
/*     */     public Builder defineAll(List<TelemetryProperty<?>> properties) {
/* 179 */       this.properties.addAll(properties);
/* 180 */       return this;
/*     */     }
/*     */     
/*     */     public <T> Builder define(TelemetryProperty<T> property) {
/* 184 */       this.properties.add(property);
/* 185 */       return this;
/*     */     }
/*     */     
/*     */     public Builder optIn() {
/* 189 */       this.isOptIn = true;
/* 190 */       return this;
/*     */     }
/*     */     
/*     */     public TelemetryEventType register() {
/* 194 */       TelemetryEventType type = new TelemetryEventType(this.id, this.exportKey, List.copyOf(this.properties), this.isOptIn);
/* 195 */       if (TelemetryEventType.REGISTRY.putIfAbsent(this.id, type) != null) {
/* 196 */         throw new IllegalStateException("Duplicate TelemetryEventType with key: '" + this.id + "'");
/*     */       }
/* 198 */       return type;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/telemetry/TelemetryEventType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */