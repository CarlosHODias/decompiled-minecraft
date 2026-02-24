/*     */ package net.minecraft.client.renderer.block.model.multipart;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.StateHolder;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public final class KeyValueCondition extends Record implements Condition {
/*     */   private final Map<String, Terms> tests;
/*     */   
/*  23 */   public KeyValueCondition(Map<String, Terms> tests) { this.tests = tests; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/multipart/KeyValueCondition;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #23	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  23 */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/multipart/KeyValueCondition; } public Map<String, Terms> tests() { return this.tests; }
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/multipart/KeyValueCondition;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #23	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/multipart/KeyValueCondition; } public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/multipart/KeyValueCondition;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #23	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/multipart/KeyValueCondition;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*  26 */   } private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*     */   
/*  28 */   public static final Codec<KeyValueCondition> CODEC = net.minecraft.util.ExtraCodecs.nonEmptyMap((Codec)Codec.unboundedMap((Codec)Codec.STRING, Terms.CODEC))
/*     */ 
/*     */     
/*  31 */     .xmap(KeyValueCondition::new, KeyValueCondition::tests);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <O, S extends StateHolder<O, S>> Predicate<S> instantiate(StateDefinition<O, S> definition) {
/*  38 */     List<Predicate<S>> predicates = new ArrayList<>(this.tests.size());
/*  39 */     this.tests.forEach((key, valueTest) -> predicates.add(instantiate(definition, key, valueTest)));
/*  40 */     return Util.allOf(predicates);
/*     */   }
/*     */   
/*     */   private static <O, S extends StateHolder<O, S>> Predicate<S> instantiate(StateDefinition<O, S> definition, String key, Terms valueTest) {
/*  44 */     Property<?> property = definition.getProperty(key);
/*  45 */     if (property == null) {
/*  46 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "Unknown property '%s' on '%s'", new Object[] { key, definition.getOwner() }));
/*     */     }
/*     */     
/*  49 */     return valueTest.instantiate(definition.getOwner(), property);
/*     */   } public static final class Terms extends Record { private final List<KeyValueCondition.Term> entries; private static final char SEPARATOR = '|'; public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/multipart/KeyValueCondition$Terms;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #52	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/multipart/KeyValueCondition$Terms; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/multipart/KeyValueCondition$Terms;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #52	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/multipart/KeyValueCondition$Terms;
/*     */       //   0	8	1	o	Ljava/lang/Object; }
/*     */     public List<KeyValueCondition.Term> entries() {
/*  52 */       return this.entries;
/*     */     }
/*  54 */     private static final Joiner JOINER = Joiner.on('|');
/*  55 */     private static final Splitter SPLITTER = Splitter.on('|'); private static final Codec<String> LEGACY_REPRESENTATION_CODEC;
/*     */     
/*     */     public Terms(List<KeyValueCondition.Term> entries) {
/*  58 */       if (entries.isEmpty()) {
/*  59 */         throw new IllegalArgumentException("Empty value for property");
/*     */       }
/*     */       this.entries = entries;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*  68 */       LEGACY_REPRESENTATION_CODEC = Codec.either((Codec)Codec.INT, (Codec)Codec.BOOL).flatComapMap(either -> (String)either.map(String::valueOf, String::valueOf), o -> DataResult.error(()));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  73 */     public static final Codec<Terms> CODEC = Codec.withAlternative((Codec)Codec.STRING, LEGACY_REPRESENTATION_CODEC)
/*  74 */       .comapFlatMap(Terms::parse, Terms::toString);
/*     */     
/*     */     public static DataResult<Terms> parse(String value) {
/*  77 */       List<KeyValueCondition.Term> terms = SPLITTER.splitToStream(value).map(KeyValueCondition.Term::parse).toList();
/*  78 */       if (terms.isEmpty()) {
/*  79 */         return DataResult.error(() -> "Empty value for property");
/*     */       }
/*  81 */       for (KeyValueCondition.Term entry : terms) {
/*  82 */         if (entry.value.isEmpty()) {
/*  83 */           return DataResult.error(() -> "Empty term in value '" + value + "'");
/*     */         }
/*     */       } 
/*  86 */       return DataResult.success(new Terms(terms));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  91 */       return JOINER.join(this.entries);
/*     */     } public <O, S extends StateHolder<O, S>, T extends Comparable<T>> Predicate<S> instantiate(O owner, Property<T> property) {
/*     */       boolean negate;
/*     */       List<T> valuesToMatch;
/*  95 */       Predicate<T> allowedValueTest = Util.anyOf(com.google.common.collect.Lists.transform(this.entries, t -> instantiate(owner, owner, property)));
/*     */       
/*  97 */       List<T> allowedValues = new ArrayList<>(property.getPossibleValues());
/*  98 */       int allValuesCount = allowedValues.size();
/*     */       
/* 100 */       allowedValues.removeIf(allowedValueTest.negate());
/* 101 */       int allowedValuesCount = allowedValues.size();
/*     */       
/* 103 */       if (allowedValuesCount == 0) {
/*     */         
/* 105 */         KeyValueCondition.LOGGER.warn("Condition {} for property {} on {} is always false", new Object[] { this, property.getName(), owner });
/* 106 */         return blockState -> false;
/*     */       } 
/*     */       
/* 109 */       int rejectedValuesCount = allValuesCount - allowedValuesCount;
/* 110 */       if (rejectedValuesCount == 0) {
/* 111 */         KeyValueCondition.LOGGER.warn("Condition {} for property {} on {} is always true", new Object[] { this, property.getName(), owner });
/* 112 */         return blockState -> true;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 118 */       if (allowedValuesCount <= rejectedValuesCount) {
/* 119 */         negate = false;
/* 120 */         valuesToMatch = allowedValues;
/*     */       } else {
/* 122 */         negate = true;
/* 123 */         List<T> rejectedValues = new ArrayList<>(property.getPossibleValues());
/* 124 */         rejectedValues.removeIf(allowedValueTest);
/* 125 */         valuesToMatch = rejectedValues;
/*     */       } 
/*     */       
/* 128 */       if (valuesToMatch.size() == 1) {
/* 129 */         Comparable comparable = (Comparable)valuesToMatch.getFirst();
/* 130 */         return state -> {
/*     */             Comparable comparable = state.getValue(property);
/*     */             return expectedValue.equals(comparable) ^ negate;
/*     */           };
/*     */       } 
/* 135 */       return state -> {
/*     */           Comparable comparable = state.getValue(property);
/*     */           return valuesToMatch.contains(comparable) ^ negate;
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     private <T extends Comparable<T>> T getValueOrThrow(Object owner, Property<T> property, String input) {
/* 143 */       Optional<T> value = property.getValue(input);
/* 144 */       if (value.isEmpty()) {
/* 145 */         throw new RuntimeException(String.format(Locale.ROOT, "Unknown value '%s' for property '%s' on '%s' in '%s'", new Object[] { input, property, owner, this }));
/*     */       }
/*     */       
/* 148 */       return value.get();
/*     */     }
/*     */     
/*     */     private <T extends Comparable<T>> Predicate<T> instantiate(Object owner, Property<T> property, KeyValueCondition.Term term) {
/* 152 */       T parsedValue = getValueOrThrow(owner, property, term.value);
/* 153 */       if (term.negated) {
/* 154 */         return value -> !value.equals(parsedValue);
/*     */       }
/* 156 */       return value -> value.equals(parsedValue);
/*     */     } } public static final class Term extends Record { private final String value; private final boolean negated; private static final String NEGATE = "!"; public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/multipart/KeyValueCondition$Term;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #160	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/multipart/KeyValueCondition$Term; } public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/multipart/KeyValueCondition$Term;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #160	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/multipart/KeyValueCondition$Term;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/* 160 */     public String value() { return this.value; } public boolean negated() { return this.negated; }
/*     */      public Term(String value, boolean negated) {
/* 162 */       if (value.isEmpty()) {
/* 163 */         throw new IllegalArgumentException("Empty term");
/*     */       }
/*     */       this.value = value;
/*     */       this.negated = negated;
/*     */     }
/*     */     
/*     */     public static Term parse(String value) {
/* 170 */       if (value.startsWith("!")) {
/* 171 */         return new Term(value.substring(1), true);
/*     */       }
/* 173 */       return new Term(value, false);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 178 */       return this.negated ? ("!" + this.value) : this.value;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/multipart/KeyValueCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */