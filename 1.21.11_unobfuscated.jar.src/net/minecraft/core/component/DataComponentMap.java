/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface DataComponentMap
/*     */   extends Iterable<TypedDataComponent<?>>, DataComponentGetter
/*     */ {
/*  32 */   public static final DataComponentMap EMPTY = new DataComponentMap()
/*     */     {
/*     */       public <T> T get(DataComponentType<? extends T> type) {
/*  35 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public Set<DataComponentType<?>> keySet() {
/*  40 */         return Set.of();
/*     */       }
/*     */ 
/*     */       
/*     */       public Iterator<TypedDataComponent<?>> iterator() {
/*  45 */         return Collections.emptyIterator();
/*     */       }
/*     */     };
/*     */   
/*     */   static Codec<DataComponentMap> makeCodec(Codec<DataComponentType<?>> componentTypeCodec) {
/*  50 */     return makeCodecFromMap(Codec.dispatchedMap(componentTypeCodec, DataComponentType::codecOrThrow));
/*     */   }
/*     */   
/*     */   static Codec<DataComponentMap> makeCodecFromMap(Codec<Map<DataComponentType<?>, Object>> mapCodec) {
/*  54 */     return mapCodec.flatComapMap(Builder::buildFromMapTrusted, components -> {
/*     */           int size = components.size();
/*     */           if (size == 0) {
/*     */             return DataResult.success(Reference2ObjectMaps.emptyMap());
/*     */           }
/*     */           Reference2ObjectArrayMap reference2ObjectArrayMap = new Reference2ObjectArrayMap(size);
/*     */           for (TypedDataComponent<?> entry : (Iterable<TypedDataComponent<?>>)components) {
/*     */             if (!entry.type().isTransient()) {
/*     */               reference2ObjectArrayMap.put(entry.type(), entry.value());
/*     */             }
/*     */           } 
/*     */           return DataResult.success(reference2ObjectArrayMap);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public static final Codec<DataComponentMap> CODEC = makeCodecFromMap(DataComponentType.VALUE_MAP_CODEC);
/*     */   
/*     */   static DataComponentMap composite(final DataComponentMap prototype, final DataComponentMap overrides) {
/*  76 */     return new DataComponentMap()
/*     */       {
/*     */         public <T> T get(DataComponentType<? extends T> type) {
/*  79 */           T value = overrides.get(type);
/*  80 */           if (value != null) {
/*  81 */             return value;
/*     */           }
/*  83 */           return prototype.get(type);
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<DataComponentType<?>> keySet() {
/*  88 */           return (Set<DataComponentType<?>>)Sets.union(prototype.keySet(), overrides.keySet());
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static Builder builder() {
/*  94 */     return new Builder();
/*     */   }
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
/*     */   
/*     */   default boolean has(DataComponentType<?> type) {
/* 113 */     return (get(type) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   default Iterator<TypedDataComponent<?>> iterator() {
/* 118 */     return Iterators.transform(keySet().iterator(), type -> (TypedDataComponent)Objects.<TypedDataComponent>requireNonNull(getTyped(type)));
/*     */   }
/*     */   
/*     */   default Stream<TypedDataComponent<?>> stream() {
/* 122 */     return StreamSupport.stream(Spliterators.spliterator(iterator(), size(), 1345), false);
/*     */   }
/*     */   
/*     */   default int size() {
/* 126 */     return keySet().size();
/*     */   }
/*     */   
/*     */   default boolean isEmpty() {
/* 130 */     return (size() == 0);
/*     */   }
/*     */   
/*     */   default DataComponentMap filter(final Predicate<DataComponentType<?>> predicate) {
/* 134 */     return new DataComponentMap()
/*     */       {
/*     */         public <T> T get(DataComponentType<? extends T> type) {
/* 137 */           return predicate.test(type) ? DataComponentMap.this.<T>get(type) : null;
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<DataComponentType<?>> keySet() {
/* 142 */           Objects.requireNonNull(predicate); return Sets.filter(DataComponentMap.this.keySet(), predicate::test);
/*     */         }
/*     */       };
/*     */   }
/*     */   Set<DataComponentType<?>> keySet();
/*     */   
/* 148 */   public static class Builder { private final Reference2ObjectMap<DataComponentType<?>, Object> map = (Reference2ObjectMap<DataComponentType<?>, Object>)new Reference2ObjectArrayMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> Builder set(DataComponentType<T> type, T value) {
/* 154 */       setUnchecked(type, value);
/* 155 */       return this;
/*     */     }
/*     */     
/*     */     <T> void setUnchecked(DataComponentType<T> type, Object value) {
/* 159 */       if (value != null) {
/* 160 */         this.map.put(type, value);
/*     */       } else {
/* 162 */         this.map.remove(type);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Builder addAll(DataComponentMap map) {
/* 167 */       for (TypedDataComponent<?> entry : (Iterable<TypedDataComponent<?>>)map) {
/* 168 */         this.map.put(entry.type(), entry.value());
/*     */       }
/* 170 */       return this;
/*     */     }
/*     */     
/*     */     public DataComponentMap build() {
/* 174 */       return buildFromMapTrusted((Map<DataComponentType<?>, Object>)this.map);
/*     */     }
/*     */     
/*     */     private static DataComponentMap buildFromMapTrusted(Map<DataComponentType<?>, Object> map) {
/* 178 */       if (map.isEmpty()) {
/* 179 */         return DataComponentMap.EMPTY;
/*     */       }
/* 181 */       if (map.size() < 8) {
/* 182 */         return new SimpleMap((Reference2ObjectMap<DataComponentType<?>, Object>)new Reference2ObjectArrayMap(map));
/*     */       }
/* 184 */       return new SimpleMap((Reference2ObjectMap<DataComponentType<?>, Object>)new Reference2ObjectOpenHashMap(map));
/*     */     }
/*     */     private static final class SimpleMap extends Record implements DataComponentMap { private final Reference2ObjectMap<DataComponentType<?>, Object> map;
/* 187 */       private SimpleMap(Reference2ObjectMap<DataComponentType<?>, Object> map) { this.map = map; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #187	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/* 187 */         //   0	7	0	this	Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap; } public Reference2ObjectMap<DataComponentType<?>, Object> map() { return this.map; }
/*     */        public final boolean equals(Object o) {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #187	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */       } public <T> T get(DataComponentType<? extends T> type) {
/* 191 */         return (T)this.map.get(type);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean has(DataComponentType<?> type) {
/* 196 */         return this.map.containsKey(type);
/*     */       }
/*     */ 
/*     */       
/*     */       public Set<DataComponentType<?>> keySet() {
/* 201 */         return (Set<DataComponentType<?>>)this.map.keySet();
/*     */       }
/*     */ 
/*     */       
/*     */       public Iterator<TypedDataComponent<?>> iterator() {
/* 206 */         return Iterators.transform((Iterator)Reference2ObjectMaps.fastIterator(this.map), TypedDataComponent::fromEntryUnchecked);
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/* 211 */         return this.map.size();
/*     */       }
/*     */       
/*     */       public String toString()
/*     */       {
/* 216 */         return this.map.toString(); } } } private static final class SimpleMap extends Record implements DataComponentMap { private final Reference2ObjectMap<DataComponentType<?>, Object> map; public String toString() { return this.map.toString(); }
/*     */ 
/*     */     
/*     */     private SimpleMap(Reference2ObjectMap<DataComponentType<?>, Object> map) {
/*     */       this.map = map;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #187	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #187	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Reference2ObjectMap<DataComponentType<?>, Object> map() {
/*     */       return this.map;
/*     */     }
/*     */     
/*     */     public <T> T get(DataComponentType<? extends T> type) {
/*     */       return (T)this.map.get(type);
/*     */     }
/*     */     
/*     */     public boolean has(DataComponentType<?> type) {
/*     */       return this.map.containsKey(type);
/*     */     }
/*     */     
/*     */     public Set<DataComponentType<?>> keySet() {
/*     */       return (Set<DataComponentType<?>>)this.map.keySet();
/*     */     }
/*     */     
/*     */     public Iterator<TypedDataComponent<?>> iterator() {
/*     */       return Iterators.transform((Iterator)Reference2ObjectMaps.fastIterator(this.map), TypedDataComponent::fromEntryUnchecked);
/*     */     }
/*     */     
/*     */     public int size() {
/*     */       return this.map.size();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/component/DataComponentMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */