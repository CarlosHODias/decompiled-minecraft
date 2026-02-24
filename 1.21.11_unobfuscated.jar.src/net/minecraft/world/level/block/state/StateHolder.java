/*     */ package net.minecraft.world.level.block.state;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public abstract class StateHolder<O, S> {
/*     */   public static final String NAME_TAG = "Name";
/*     */   public static final String PROPERTIES_TAG = "Properties";
/*     */   
/*  22 */   private static final Function<Map.Entry<Property<?>, Comparable<?>>, String> PROPERTY_ENTRY_TO_STRING_FUNCTION = new Function<Map.Entry<Property<?>, Comparable<?>>, String>()
/*     */     {
/*     */       public String apply(Map.Entry<Property<?>, Comparable<?>> entry) {
/*  25 */         if (entry == null) {
/*  26 */           return "<NULL>";
/*     */         }
/*     */         
/*  29 */         Property<?> property = entry.getKey();
/*  30 */         return property.getName() + "=" + property.getName();
/*     */       }
/*     */ 
/*     */       
/*     */       private <T extends Comparable<T>> String getName(Property<T> property, Comparable<?> value) {
/*  35 */         return property.getName(value);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   protected final O owner;
/*     */   
/*     */   private final Reference2ObjectArrayMap<Property<?>, Comparable<?>> values;
/*     */   private Map<Property<?>, S[]> neighbours;
/*     */   protected final MapCodec<S> propertiesCodec;
/*     */   
/*     */   protected StateHolder(O owner, Reference2ObjectArrayMap<Property<?>, Comparable<?>> values, MapCodec<S> propertiesCodec) {
/*  47 */     this.owner = owner;
/*  48 */     this.values = values;
/*  49 */     this.propertiesCodec = propertiesCodec;
/*     */   }
/*     */   
/*     */   public <T extends Comparable<T>> S cycle(Property<T> property) {
/*  53 */     return setValue(property, findNextInCollection(property.getPossibleValues(), getValue(property)));
/*     */   }
/*     */   
/*     */   protected static <T> T findNextInCollection(List<T> values, T current) {
/*  57 */     int nextIndex = values.indexOf(current) + 1;
/*  58 */     return (nextIndex == values.size()) ? values.getFirst() : values.get(nextIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  63 */     StringBuilder builder = new StringBuilder();
/*  64 */     builder.append(this.owner);
/*     */     
/*  66 */     if (!getValues().isEmpty()) {
/*  67 */       builder.append('[');
/*  68 */       builder.append(getValues().entrySet().stream().<CharSequence>map((Function)PROPERTY_ENTRY_TO_STRING_FUNCTION).collect(Collectors.joining(",")));
/*  69 */       builder.append(']');
/*     */     } 
/*     */     
/*  72 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean equals(Object obj) {
/*  78 */     return super.equals(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  84 */     return super.hashCode();
/*     */   }
/*     */   
/*     */   public Collection<Property<?>> getProperties() {
/*  88 */     return Collections.unmodifiableCollection((Collection<? extends Property<?>>)this.values.keySet());
/*     */   }
/*     */   
/*     */   public boolean hasProperty(Property<?> property) {
/*  92 */     return this.values.containsKey(property);
/*     */   }
/*     */   
/*     */   public <T extends Comparable<T>> T getValue(Property<T> property) {
/*  96 */     Comparable<?> value = (Comparable)this.values.get(property);
/*  97 */     if (value == null) {
/*  98 */       throw new IllegalArgumentException("Cannot get property " + String.valueOf(property) + " as it does not exist in " + String.valueOf(this.owner));
/*     */     }
/*     */     
/* 101 */     return (T)property.getValueClass().cast(value);
/*     */   }
/*     */   
/*     */   public <T extends Comparable<T>> Optional<T> getOptionalValue(Property<T> property) {
/* 105 */     return Optional.ofNullable(getNullableValue(property));
/*     */   }
/*     */   
/*     */   public <T extends Comparable<T>> T getValueOrElse(Property<T> property, T defaultValue) {
/* 109 */     return (T)Objects.<Comparable>requireNonNullElse(getNullableValue(property), (Comparable)defaultValue);
/*     */   }
/*     */   
/*     */   private <T extends Comparable<T>> T getNullableValue(Property<T> property) {
/* 113 */     Comparable<?> value = (Comparable)this.values.get(property);
/* 114 */     if (value == null) {
/* 115 */       return null;
/*     */     }
/* 117 */     return (T)property.getValueClass().cast(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Comparable<T>, V extends T> S setValue(Property<T> property, V value) {
/* 127 */     Comparable<?> oldValue = (Comparable)this.values.get(property);
/* 128 */     if (oldValue == null) {
/* 129 */       throw new IllegalArgumentException("Cannot set property " + String.valueOf(property) + " as it does not exist in " + String.valueOf(this.owner));
/*     */     }
/*     */     
/* 132 */     return setValueInternal(property, value, oldValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Comparable<T>, V extends T> S trySetValue(Property<T> property, V value) {
/* 142 */     Comparable<?> oldValue = (Comparable)this.values.get(property);
/* 143 */     if (oldValue == null) {
/* 144 */       return (S)this;
/*     */     }
/*     */     
/* 147 */     return setValueInternal(property, value, oldValue);
/*     */   }
/*     */ 
/*     */   
/*     */   private <T extends Comparable<T>, V extends T> S setValueInternal(Property<T> property, V value, Comparable<?> oldValue) {
/* 152 */     if (oldValue.equals(value)) {
/* 153 */       return (S)this;
/*     */     }
/*     */     
/* 156 */     int internalIndex = property.getInternalIndex((Comparable)value);
/* 157 */     if (internalIndex < 0) {
/* 158 */       throw new IllegalArgumentException("Cannot set property " + String.valueOf(property) + " to " + String.valueOf(value) + " on " + String.valueOf(this.owner) + ", it is not an allowed value");
/*     */     }
/* 160 */     return (S)((Object[])this.neighbours.get(property))[internalIndex];
/*     */   }
/*     */ 
/*     */   
/*     */   public void populateNeighbours(Map<Map<Property<?>, Comparable<?>>, S> statesByValues) {
/* 165 */     if (this.neighbours != null) {
/* 166 */       throw new IllegalStateException();
/*     */     }
/*     */     
/* 169 */     Reference2ObjectArrayMap<Property<?>, Object[]> reference2ObjectArrayMap = new Reference2ObjectArrayMap(this.values.size());
/* 170 */     for (ObjectIterator<Map.Entry<Property<?>, Comparable<?>>> objectIterator = this.values.entrySet().iterator(); objectIterator.hasNext(); ) { Map.Entry<Property<?>, Comparable<?>> entry = objectIterator.next();
/* 171 */       Property<?> property = entry.getKey();
/*     */       
/* 173 */       reference2ObjectArrayMap.put(property, property.getPossibleValues().stream()
/* 174 */           .map(value -> statesByValues.get(makeNeighbourValues(statesByValues, property)))
/* 175 */           .toArray()); }
/*     */ 
/*     */ 
/*     */     
/* 179 */     this.neighbours = (Map)reference2ObjectArrayMap;
/*     */   }
/*     */   
/*     */   private Map<Property<?>, Comparable<?>> makeNeighbourValues(Property<?> property, Comparable<?> value) {
/* 183 */     Reference2ObjectArrayMap<Property<?>, Comparable<?>> reference2ObjectArrayMap = new Reference2ObjectArrayMap((Reference2ObjectMap)this.values);
/* 184 */     reference2ObjectArrayMap.put(property, value);
/* 185 */     return (Map<Property<?>, Comparable<?>>)reference2ObjectArrayMap;
/*     */   }
/*     */   
/*     */   public Map<Property<?>, Comparable<?>> getValues() {
/* 189 */     return (Map<Property<?>, Comparable<?>>)this.values;
/*     */   }
/*     */   
/*     */   protected static <O, S extends StateHolder<O, S>> Codec<S> codec(Codec<O> ownerCodec, Function<O, S> defaultState) {
/* 193 */     return ownerCodec.dispatch("Name", s -> s.owner, o -> {
/*     */           StateHolder stateHolder = defaultState.apply(o);
/*     */           return stateHolder.getValues().isEmpty() ? MapCodec.unit(stateHolder) : stateHolder.propertiesCodec.codec().lenientOptionalFieldOf("Properties").xmap((), Optional::of);
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/StateHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */