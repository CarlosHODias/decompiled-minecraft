/*     */ package net.minecraft.client.telemetry;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Encoder;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public class TelemetryPropertyMap {
/*     */   private final Map<TelemetryProperty<?>, Object> entries;
/*     */   
/*     */   private TelemetryPropertyMap(Map<TelemetryProperty<?>, Object> entries) {
/*  20 */     this.entries = entries;
/*     */   }
/*     */   
/*     */   public static Builder builder() {
/*  24 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static MapCodec<TelemetryPropertyMap> createCodec(final List<TelemetryProperty<?>> properties) {
/*  28 */     return new MapCodec<TelemetryPropertyMap>()
/*     */       {
/*     */         public <T> RecordBuilder<T> encode(TelemetryPropertyMap input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/*  31 */           RecordBuilder<T> result = prefix;
/*  32 */           for (TelemetryProperty<?> property : properties) {
/*  33 */             result = encodeProperty(input, result, property);
/*     */           }
/*  35 */           return result;
/*     */         }
/*     */         
/*     */         private <T, V> RecordBuilder<T> encodeProperty(TelemetryPropertyMap input, RecordBuilder<T> result, TelemetryProperty<V> property) {
/*  39 */           V value = input.get(property);
/*  40 */           if (value != null) {
/*  41 */             return result.add(property.id(), value, (Encoder)property.codec());
/*     */           }
/*  43 */           return result;
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<TelemetryPropertyMap> decode(DynamicOps<T> ops, MapLike<T> input) {
/*  48 */           DataResult<TelemetryPropertyMap.Builder> result = DataResult.success(new TelemetryPropertyMap.Builder());
/*  49 */           for (TelemetryProperty<?> property : properties) {
/*  50 */             result = decodeProperty(result, ops, input, property);
/*     */           }
/*  52 */           return result.map(TelemetryPropertyMap.Builder::build);
/*     */         }
/*     */         
/*     */         private <T, V> DataResult<TelemetryPropertyMap.Builder> decodeProperty(DataResult<TelemetryPropertyMap.Builder> result, DynamicOps<T> ops, MapLike<T> input, TelemetryProperty<V> property) {
/*  56 */           T value = (T)input.get(property.id());
/*  57 */           if (value != null) {
/*  58 */             DataResult<V> parse = property.codec().parse(ops, value);
/*  59 */             return result.apply2stable((b, v) -> b.put(property, v), parse);
/*     */           } 
/*  61 */           return result;
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> Stream<T> keys(DynamicOps<T> ops) {
/*  66 */           Objects.requireNonNull(ops); return properties.stream().map(TelemetryProperty::id).map(ops::createString);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T get(TelemetryProperty<T> property) {
/*  73 */     return (T)this.entries.get(property);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  78 */     return this.entries.toString();
/*     */   }
/*     */   
/*     */   public Set<TelemetryProperty<?>> propertySet() {
/*  82 */     return this.entries.keySet();
/*     */   }
/*     */   
/*     */   public static class Builder {
/*  86 */     private final Map<TelemetryProperty<?>, Object> entries = (Map<TelemetryProperty<?>, Object>)new Reference2ObjectOpenHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> Builder put(TelemetryProperty<T> property, T value) {
/*  92 */       this.entries.put(property, value);
/*  93 */       return this;
/*     */     }
/*     */     
/*     */     public <T> Builder putIfNotNull(TelemetryProperty<T> property, T value) {
/*  97 */       if (value != null) {
/*  98 */         this.entries.put(property, value);
/*     */       }
/* 100 */       return this;
/*     */     }
/*     */     
/*     */     public Builder putAll(TelemetryPropertyMap properties) {
/* 104 */       this.entries.putAll(properties.entries);
/* 105 */       return this;
/*     */     }
/*     */     
/*     */     public TelemetryPropertyMap build() {
/* 109 */       return new TelemetryPropertyMap(this.entries);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/telemetry/TelemetryPropertyMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */