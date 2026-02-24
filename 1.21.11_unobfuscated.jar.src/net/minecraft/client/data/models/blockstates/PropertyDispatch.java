/*     */ package net.minecraft.client.data.models.blockstates;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.data.models.MultiVariant;
/*     */ import net.minecraft.client.renderer.block.model.VariantMutator;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public abstract class PropertyDispatch<V> {
/*  18 */   private final Map<PropertyValueList, V> values = new HashMap<>();
/*     */   
/*     */   protected void putValue(PropertyValueList key, V variant) {
/*  21 */     V previous = this.values.put(key, variant);
/*  22 */     if (previous != null) {
/*  23 */       throw new IllegalStateException("Value " + String.valueOf(key) + " is already defined");
/*     */     }
/*     */   }
/*     */   
/*     */   Map<PropertyValueList, V> getEntries() {
/*  28 */     verifyComplete();
/*  29 */     return Map.copyOf(this.values);
/*     */   }
/*     */   
/*     */   private void verifyComplete() {
/*  33 */     List<Property<?>> properties = getDefinedProperties();
/*  34 */     Stream<PropertyValueList> valuesToCover = Stream.of(PropertyValueList.EMPTY);
/*  35 */     for (Property<?> property : properties) {
/*  36 */       valuesToCover = valuesToCover.flatMap(current -> { Objects.requireNonNull(current); return property.getAllValues().map(current::extend);
/*     */           });
/*  38 */     }  List<PropertyValueList> undefinedCombinations = valuesToCover.filter(f -> !this.values.containsKey(f)).toList();
/*  39 */     if (!undefinedCombinations.isEmpty()) {
/*  40 */       throw new IllegalStateException("Missing definition for properties: " + String.valueOf(undefinedCombinations));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T1 extends Comparable<T1>> C1<MultiVariant, T1> initial(Property<T1> property1) {
/*  47 */     return new C1<>(property1);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> C2<MultiVariant, T1, T2> initial(Property<T1> property1, Property<T2> property2) {
/*  51 */     return new C2<>(property1, property2);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> C3<MultiVariant, T1, T2, T3> initial(Property<T1> property1, Property<T2> property2, Property<T3> property3) {
/*  55 */     return new C3<>(property1, property2, property3);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>> C4<MultiVariant, T1, T2, T3, T4> initial(Property<T1> property1, Property<T2> property2, Property<T3> property3, Property<T4> property4) {
/*  59 */     return new C4<>(property1, property2, property3, property4);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>> C5<MultiVariant, T1, T2, T3, T4, T5> initial(Property<T1> property1, Property<T2> property2, Property<T3> property3, Property<T4> property4, Property<T5> property5) {
/*  63 */     return new C5<>(property1, property2, property3, property4, property5);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>> C1<VariantMutator, T1> modify(Property<T1> property1) {
/*  67 */     return new C1<>(property1);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> C2<VariantMutator, T1, T2> modify(Property<T1> property1, Property<T2> property2) {
/*  71 */     return new C2<>(property1, property2);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> C3<VariantMutator, T1, T2, T3> modify(Property<T1> property1, Property<T2> property2, Property<T3> property3) {
/*  75 */     return new C3<>(property1, property2, property3);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>> C4<VariantMutator, T1, T2, T3, T4> modify(Property<T1> property1, Property<T2> property2, Property<T3> property3, Property<T4> property4) {
/*  79 */     return new C4<>(property1, property2, property3, property4);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>> C5<VariantMutator, T1, T2, T3, T4, T5> modify(Property<T1> property1, Property<T2> property2, Property<T3> property3, Property<T4> property4, Property<T5> property5) {
/*  83 */     return new C5<>(property1, property2, property3, property4, property5);
/*     */   }
/*     */   abstract List<Property<?>> getDefinedProperties();
/*     */   
/*     */   public static class C1<V, T1 extends Comparable<T1>> extends PropertyDispatch<V> { private final Property<T1> property1;
/*     */     
/*     */     private C1(Property<T1> property1) {
/*  90 */       this.property1 = property1;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Property<?>> getDefinedProperties() {
/*  95 */       return List.of(this.property1);
/*     */     }
/*     */     
/*     */     public C1<V, T1> select(T1 value1, V variants) {
/*  99 */       PropertyValueList key = PropertyValueList.of((Property.Value<?>[])new Property.Value[] {
/* 100 */             this.property1.value((Comparable)value1)
/*     */           });
/* 102 */       putValue(key, variants);
/* 103 */       return this;
/*     */     }
/*     */     
/*     */     public PropertyDispatch<V> generate(Function<T1, V> generator) {
/* 107 */       this.property1.getPossibleValues().forEach(value1 -> select((T1)generator, generator.apply(generator)));
/*     */ 
/*     */       
/* 110 */       return this;
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class C2<V, T1 extends Comparable<T1>, T2 extends Comparable<T2>> extends PropertyDispatch<V> {
/*     */     private final Property<T1> property1;
/*     */     private final Property<T2> property2;
/*     */     
/*     */     private C2(Property<T1> property1, Property<T2> property2) {
/* 119 */       this.property1 = property1;
/* 120 */       this.property2 = property2;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Property<?>> getDefinedProperties() {
/* 125 */       return List.of(this.property1, this.property2);
/*     */     }
/*     */     
/*     */     public C2<V, T1, T2> select(T1 value1, T2 value2, V variants) {
/* 129 */       PropertyValueList key = PropertyValueList.of((Property.Value<?>[])new Property.Value[] {
/* 130 */             this.property1.value((Comparable)value1), 
/* 131 */             this.property2.value((Comparable)value2)
/*     */           });
/* 133 */       putValue(key, variants);
/* 134 */       return this;
/*     */     }
/*     */     
/*     */     public PropertyDispatch<V> generate(BiFunction<T1, T2, V> generator) {
/* 138 */       this.property1.getPossibleValues().forEach(value1 -> this.property2.getPossibleValues().forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 143 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class C3<V, T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> extends PropertyDispatch<V> {
/*     */     private final Property<T1> property1;
/*     */     private final Property<T2> property2;
/*     */     private final Property<T3> property3;
/*     */     
/*     */     private C3(Property<T1> property1, Property<T2> property2, Property<T3> property3) {
/* 153 */       this.property1 = property1;
/* 154 */       this.property2 = property2;
/* 155 */       this.property3 = property3;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Property<?>> getDefinedProperties() {
/* 160 */       return List.of(this.property1, this.property2, this.property3);
/*     */     }
/*     */     
/*     */     public C3<V, T1, T2, T3> select(T1 value1, T2 value2, T3 value3, V variants) {
/* 164 */       PropertyValueList key = PropertyValueList.of((Property.Value<?>[])new Property.Value[] {
/* 165 */             this.property1.value((Comparable)value1), 
/* 166 */             this.property2.value((Comparable)value2), 
/* 167 */             this.property3.value((Comparable)value3)
/*     */           });
/* 169 */       putValue(key, variants);
/* 170 */       return this;
/*     */     }
/*     */     
/*     */     public PropertyDispatch<V> generate(Function3<T1, T2, T3, V> generator) {
/* 174 */       this.property1.getPossibleValues().forEach(value1 -> this.property2.getPossibleValues().forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 181 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class C4<V, T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>> extends PropertyDispatch<V> {
/*     */     private final Property<T1> property1;
/*     */     private final Property<T2> property2;
/*     */     private final Property<T3> property3;
/*     */     private final Property<T4> property4;
/*     */     
/*     */     private C4(Property<T1> property1, Property<T2> property2, Property<T3> property3, Property<T4> property4) {
/* 192 */       this.property1 = property1;
/* 193 */       this.property2 = property2;
/* 194 */       this.property3 = property3;
/* 195 */       this.property4 = property4;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Property<?>> getDefinedProperties() {
/* 200 */       return List.of(this.property1, this.property2, this.property3, this.property4);
/*     */     }
/*     */     
/*     */     public C4<V, T1, T2, T3, T4> select(T1 value1, T2 value2, T3 value3, T4 value4, V variants) {
/* 204 */       PropertyValueList key = PropertyValueList.of((Property.Value<?>[])new Property.Value[] {
/* 205 */             this.property1.value((Comparable)value1), 
/* 206 */             this.property2.value((Comparable)value2), 
/* 207 */             this.property3.value((Comparable)value3), 
/* 208 */             this.property4.value((Comparable)value4)
/*     */           });
/* 210 */       putValue(key, variants);
/* 211 */       return this;
/*     */     }
/*     */     
/*     */     public PropertyDispatch<V> generate(Function4<T1, T2, T3, T4, V> generator) {
/* 215 */       this.property1.getPossibleValues().forEach(value1 -> this.property2.getPossibleValues().forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 224 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class C5<V, T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>> extends PropertyDispatch<V> {
/*     */     private final Property<T1> property1;
/*     */     private final Property<T2> property2;
/*     */     private final Property<T3> property3;
/*     */     private final Property<T4> property4;
/*     */     private final Property<T5> property5;
/*     */     
/*     */     private C5(Property<T1> property1, Property<T2> property2, Property<T3> property3, Property<T4> property4, Property<T5> property5) {
/* 236 */       this.property1 = property1;
/* 237 */       this.property2 = property2;
/* 238 */       this.property3 = property3;
/* 239 */       this.property4 = property4;
/* 240 */       this.property5 = property5;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Property<?>> getDefinedProperties() {
/* 245 */       return List.of(this.property1, this.property2, this.property3, this.property4, this.property5);
/*     */     }
/*     */     
/*     */     public C5<V, T1, T2, T3, T4, T5> select(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, V variants) {
/* 249 */       PropertyValueList key = PropertyValueList.of((Property.Value<?>[])new Property.Value[] {
/* 250 */             this.property1.value((Comparable)value1), 
/* 251 */             this.property2.value((Comparable)value2), 
/* 252 */             this.property3.value((Comparable)value3), 
/* 253 */             this.property4.value((Comparable)value4), 
/* 254 */             this.property5.value((Comparable)value5)
/*     */           });
/* 256 */       putValue(key, variants);
/* 257 */       return this;
/*     */     }
/*     */     
/*     */     public PropertyDispatch<V> generate(Function5<T1, T2, T3, T4, T5, V> generator) {
/* 261 */       this.property1.getPossibleValues().forEach(value1 -> this.property2.getPossibleValues().forEach(()));
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
/* 272 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/models/blockstates/PropertyDispatch.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */