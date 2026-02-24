/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.StateHolder;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public final class StatePropertiesPredicate extends Record {
/*     */   private final List<PropertyMatcher> properties;
/*     */   private static final Codec<List<PropertyMatcher>> PROPERTIES_CODEC;
/*     */   
/*  21 */   public StatePropertiesPredicate(List<PropertyMatcher> properties) { this.properties = properties; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/StatePropertiesPredicate;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #21	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  21 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/StatePropertiesPredicate; } public List<PropertyMatcher> properties() { return this.properties; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/StatePropertiesPredicate;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #21	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/StatePropertiesPredicate; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/StatePropertiesPredicate;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #21	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/StatePropertiesPredicate;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*  22 */   static { PROPERTIES_CODEC = Codec.unboundedMap((Codec)Codec.STRING, ValueMatcher.CODEC).xmap(map -> map.entrySet().stream().map(()).toList(), properties -> (Map)properties.stream().collect(java.util.stream.Collectors.toMap(PropertyMatcher::name, PropertyMatcher::valueMatcher))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  27 */   public static final Codec<StatePropertiesPredicate> CODEC = PROPERTIES_CODEC.xmap(StatePropertiesPredicate::new, StatePropertiesPredicate::properties);
/*  28 */   public static final StreamCodec<ByteBuf, StatePropertiesPredicate> STREAM_CODEC = PropertyMatcher.STREAM_CODEC.apply(ByteBufCodecs.list())
/*  29 */     .map(StatePropertiesPredicate::new, StatePropertiesPredicate::properties);
/*     */   private static final class PropertyMatcher extends Record { private final String name; private final StatePropertiesPredicate.ValueMatcher valueMatcher;
/*  31 */     private PropertyMatcher(String name, StatePropertiesPredicate.ValueMatcher valueMatcher) { this.name = name; this.valueMatcher = valueMatcher; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$PropertyMatcher;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  31 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$PropertyMatcher; } public String name() { return this.name; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$PropertyMatcher;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$PropertyMatcher; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$PropertyMatcher;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$PropertyMatcher;
/*  31 */       //   0	8	1	o	Ljava/lang/Object; } public StatePropertiesPredicate.ValueMatcher valueMatcher() { return this.valueMatcher; }
/*  32 */      public static final StreamCodec<ByteBuf, PropertyMatcher> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, PropertyMatcher::name, StatePropertiesPredicate.ValueMatcher.STREAM_CODEC, PropertyMatcher::valueMatcher, PropertyMatcher::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <S extends StateHolder<?, S>> boolean match(StateDefinition<?, S> definition, S state) {
/*  39 */       Property<?> property = definition.getProperty(this.name);
/*  40 */       return (property != null && this.valueMatcher.match((StateHolder<?, ?>)state, property));
/*     */     }
/*     */     
/*     */     public Optional<String> checkState(StateDefinition<?, ?> states) {
/*  44 */       Property<?> property = states.getProperty(this.name);
/*  45 */       return (property != null) ? Optional.<String>empty() : Optional.<String>of(this.name);
/*     */     } }
/*     */   private static interface ValueMatcher { public static final Codec<ValueMatcher> CODEC; public static final StreamCodec<ByteBuf, ValueMatcher> STREAM_CODEC;
/*     */     
/*     */     static {
/*  50 */       CODEC = Codec.either(StatePropertiesPredicate.ExactMatcher.CODEC, StatePropertiesPredicate.RangedMatcher.CODEC).xmap(Either::unwrap, matcher -> {
/*     */             if (matcher instanceof StatePropertiesPredicate.ExactMatcher) {
/*     */               StatePropertiesPredicate.ExactMatcher exact = (StatePropertiesPredicate.ExactMatcher)matcher;
/*     */               return Either.left(exact);
/*     */             } 
/*     */             if (matcher instanceof StatePropertiesPredicate.RangedMatcher) {
/*     */               StatePropertiesPredicate.RangedMatcher ranged = (StatePropertiesPredicate.RangedMatcher)matcher;
/*     */               return Either.right(ranged);
/*     */             } 
/*     */             throw new UnsupportedOperationException();
/*     */           });
/*  61 */       STREAM_CODEC = ByteBufCodecs.either(StatePropertiesPredicate.ExactMatcher.STREAM_CODEC, StatePropertiesPredicate.RangedMatcher.STREAM_CODEC).map(Either::unwrap, matcher -> {
/*     */             if (matcher instanceof StatePropertiesPredicate.ExactMatcher) {
/*     */               StatePropertiesPredicate.ExactMatcher exact = (StatePropertiesPredicate.ExactMatcher)matcher;
/*     */               return Either.left(exact);
/*     */             } 
/*     */             if (matcher instanceof StatePropertiesPredicate.RangedMatcher) {
/*     */               StatePropertiesPredicate.RangedMatcher ranged = (StatePropertiesPredicate.RangedMatcher)matcher;
/*     */               return Either.right(ranged);
/*     */             } 
/*     */             throw new UnsupportedOperationException();
/*     */           });
/*     */     }
/*     */     <T extends Comparable<T>> boolean match(StateHolder<?, ?> param1StateHolder, Property<T> param1Property); }
/*     */   private static final class ExactMatcher extends Record implements ValueMatcher { private final String value;
/*     */     
/*  76 */     private ExactMatcher(String value) { this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$ExactMatcher;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$ExactMatcher; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$ExactMatcher;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$ExactMatcher; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$ExactMatcher;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$ExactMatcher;
/*  76 */       //   0	8	1	o	Ljava/lang/Object; } public String value() { return this.value; }
/*  77 */      public static final Codec<ExactMatcher> CODEC = Codec.STRING.xmap(ExactMatcher::new, ExactMatcher::value);
/*  78 */     public static final StreamCodec<ByteBuf, ExactMatcher> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(ExactMatcher::new, ExactMatcher::value);
/*     */ 
/*     */     
/*     */     public <T extends Comparable<T>> boolean match(StateHolder<?, ?> state, Property<T> property) {
/*  82 */       Comparable<Comparable> comparable = state.getValue(property);
/*  83 */       Optional<T> typedExpected = property.getValue(this.value);
/*  84 */       return (typedExpected.isPresent() && comparable.compareTo((Comparable)typedExpected.get()) == 0);
/*     */     } }
/*     */   private static final class RangedMatcher extends Record implements ValueMatcher { private final Optional<String> minValue; private final Optional<String> maxValue; public static final Codec<RangedMatcher> CODEC;
/*     */     
/*  88 */     private RangedMatcher(Optional<String> minValue, Optional<String> maxValue) { this.minValue = minValue; this.maxValue = maxValue; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$RangedMatcher;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$RangedMatcher; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$RangedMatcher;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$RangedMatcher; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$RangedMatcher;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$RangedMatcher;
/*  88 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<String> minValue() { return this.minValue; } public Optional<String> maxValue() { return this.maxValue; } static {
/*  89 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.optionalFieldOf("min").forGetter(RangedMatcher::minValue), (App)Codec.STRING.optionalFieldOf("max").forGetter(RangedMatcher::maxValue)).apply((com.mojang.datafixers.kinds.Applicative)i, RangedMatcher::new));
/*     */     }
/*     */ 
/*     */     
/*  93 */     public static final StreamCodec<ByteBuf, RangedMatcher> STREAM_CODEC = StreamCodec.composite(
/*  94 */         ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), RangedMatcher::minValue, 
/*  95 */         ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), RangedMatcher::maxValue, RangedMatcher::new);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends Comparable<T>> boolean match(StateHolder<?, ?> state, Property<T> property) {
/* 101 */       Comparable<Comparable> comparable = state.getValue(property);
/*     */       
/* 103 */       if (this.minValue.isPresent()) {
/* 104 */         Optional<T> typedMinValue = property.getValue(this.minValue.get());
/* 105 */         if (typedMinValue.isEmpty() || comparable.compareTo((Comparable)typedMinValue.get()) < 0) {
/* 106 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 110 */       if (this.maxValue.isPresent()) {
/* 111 */         Optional<T> typedMaxValue = property.getValue(this.maxValue.get());
/* 112 */         if (typedMaxValue.isEmpty() || comparable.compareTo((Comparable)typedMaxValue.get()) > 0) {
/* 113 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 117 */       return true;
/*     */     } }
/*     */ 
/*     */   
/*     */   public <S extends StateHolder<?, S>> boolean matches(StateDefinition<?, S> definition, S state) {
/* 122 */     for (PropertyMatcher matcher : this.properties) {
/* 123 */       if (!matcher.match(definition, state)) {
/* 124 */         return false;
/*     */       }
/*     */     } 
/* 127 */     return true;
/*     */   }
/*     */   
/*     */   public boolean matches(net.minecraft.world.level.block.state.BlockState state) {
/* 131 */     return matches(state.getBlock().getStateDefinition(), state);
/*     */   }
/*     */   
/*     */   public boolean matches(net.minecraft.world.level.material.FluidState state) {
/* 135 */     return matches(state.getType().getStateDefinition(), state);
/*     */   }
/*     */   
/*     */   public Optional<String> checkState(StateDefinition<?, ?> states) {
/* 139 */     for (PropertyMatcher property : this.properties) {
/* 140 */       Optional<String> unknownProperty = property.checkState(states);
/* 141 */       if (unknownProperty.isPresent()) {
/* 142 */         return unknownProperty;
/*     */       }
/*     */     } 
/* 145 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 149 */     private final com.google.common.collect.ImmutableList.Builder<StatePropertiesPredicate.PropertyMatcher> matchers = com.google.common.collect.ImmutableList.builder();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Builder properties() {
/* 155 */       return new Builder();
/*     */     }
/*     */     
/*     */     public Builder hasProperty(Property<?> property, String value) {
/* 159 */       this.matchers.add(new StatePropertiesPredicate.PropertyMatcher(property.getName(), new StatePropertiesPredicate.ExactMatcher(value)));
/* 160 */       return this;
/*     */     }
/*     */     
/*     */     public Builder hasProperty(Property<Integer> property, int value) {
/* 164 */       return hasProperty(property, Integer.toString(value));
/*     */     }
/*     */     
/*     */     public Builder hasProperty(Property<Boolean> property, boolean value) {
/* 168 */       return hasProperty(property, Boolean.toString(value));
/*     */     }
/*     */     
/*     */     public <T extends Comparable<T> & net.minecraft.util.StringRepresentable> Builder hasProperty(Property<T> property, T value) {
/* 172 */       return hasProperty(property, ((net.minecraft.util.StringRepresentable)value).getSerializedName());
/*     */     }
/*     */     
/*     */     public Optional<StatePropertiesPredicate> build() {
/* 176 */       return Optional.of(new StatePropertiesPredicate((List<StatePropertiesPredicate.PropertyMatcher>)this.matchers.build()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/StatePropertiesPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */