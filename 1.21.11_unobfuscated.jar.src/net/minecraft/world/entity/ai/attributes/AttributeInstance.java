/*     */ package net.minecraft.world.entity.ai.attributes;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class AttributeInstance {
/*     */   private final Holder<Attribute> attribute;
/*  23 */   private final Map<AttributeModifier.Operation, Map<Identifier, AttributeModifier>> modifiersByOperation = Maps.newEnumMap(AttributeModifier.Operation.class);
/*  24 */   private final Map<Identifier, AttributeModifier> modifierById = (Map<Identifier, AttributeModifier>)new Object2ObjectArrayMap();
/*  25 */   private final Map<Identifier, AttributeModifier> permanentModifiers = (Map<Identifier, AttributeModifier>)new Object2ObjectArrayMap();
/*     */   private double baseValue;
/*     */   private boolean dirty = true;
/*     */   private double cachedValue;
/*     */   private final Consumer<AttributeInstance> onDirty;
/*     */   
/*     */   public AttributeInstance(Holder<Attribute> attribute, Consumer<AttributeInstance> onDirty) {
/*  32 */     this.attribute = attribute;
/*  33 */     this.onDirty = onDirty;
/*  34 */     this.baseValue = ((Attribute)attribute.value()).getDefaultValue();
/*     */   }
/*     */   
/*     */   public Holder<Attribute> getAttribute() {
/*  38 */     return this.attribute;
/*     */   }
/*     */   
/*     */   public double getBaseValue() {
/*  42 */     return this.baseValue;
/*     */   }
/*     */   
/*     */   public void setBaseValue(double baseValue) {
/*  46 */     if (baseValue == this.baseValue) {
/*     */       return;
/*     */     }
/*  49 */     this.baseValue = baseValue;
/*  50 */     setDirty();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   Map<Identifier, AttributeModifier> getModifiers(AttributeModifier.Operation operation) {
/*  55 */     return this.modifiersByOperation.computeIfAbsent(operation, key -> new Object2ObjectOpenHashMap());
/*     */   }
/*     */   
/*     */   public Set<AttributeModifier> getModifiers() {
/*  59 */     return (Set<AttributeModifier>)ImmutableSet.copyOf(this.modifierById.values());
/*     */   }
/*     */   
/*     */   public Set<AttributeModifier> getPermanentModifiers() {
/*  63 */     return (Set<AttributeModifier>)ImmutableSet.copyOf(this.permanentModifiers.values());
/*     */   }
/*     */   
/*     */   public AttributeModifier getModifier(Identifier id) {
/*  67 */     return this.modifierById.get(id);
/*     */   }
/*     */   
/*     */   public boolean hasModifier(Identifier modifier) {
/*  71 */     return (this.modifierById.get(modifier) != null);
/*     */   }
/*     */   
/*     */   private void addModifier(AttributeModifier modifier) {
/*  75 */     AttributeModifier previous = this.modifierById.putIfAbsent(modifier.id(), modifier);
/*  76 */     if (previous != null) {
/*  77 */       throw new IllegalArgumentException("Modifier is already applied on this attribute!");
/*     */     }
/*  79 */     getModifiers(modifier.operation()).put(modifier.id(), modifier);
/*  80 */     setDirty();
/*     */   }
/*     */   
/*     */   public void addOrUpdateTransientModifier(AttributeModifier modifier) {
/*  84 */     AttributeModifier oldModifier = this.modifierById.put(modifier.id(), modifier);
/*  85 */     if (modifier == oldModifier) {
/*     */       return;
/*     */     }
/*  88 */     getModifiers(modifier.operation()).put(modifier.id(), modifier);
/*  89 */     setDirty();
/*     */   }
/*     */   
/*     */   public void addTransientModifier(AttributeModifier modifier) {
/*  93 */     addModifier(modifier);
/*     */   }
/*     */   
/*     */   public void addOrReplacePermanentModifier(AttributeModifier modifier) {
/*  97 */     removeModifier(modifier.id());
/*  98 */     addModifier(modifier);
/*  99 */     this.permanentModifiers.put(modifier.id(), modifier);
/*     */   }
/*     */   
/*     */   public void addPermanentModifier(AttributeModifier modifier) {
/* 103 */     addModifier(modifier);
/* 104 */     this.permanentModifiers.put(modifier.id(), modifier);
/*     */   }
/*     */   
/*     */   public void addPermanentModifiers(Collection<AttributeModifier> modifiers) {
/* 108 */     for (AttributeModifier modifier : modifiers) {
/* 109 */       addPermanentModifier(modifier);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void setDirty() {
/* 114 */     this.dirty = true;
/* 115 */     this.onDirty.accept(this);
/*     */   }
/*     */   
/*     */   public void removeModifier(AttributeModifier modifier) {
/* 119 */     removeModifier(modifier.id());
/*     */   }
/*     */   
/*     */   public boolean removeModifier(Identifier id) {
/* 123 */     AttributeModifier modifier = this.modifierById.remove(id);
/* 124 */     if (modifier == null) {
/* 125 */       return false;
/*     */     }
/* 127 */     getModifiers(modifier.operation()).remove(id);
/* 128 */     this.permanentModifiers.remove(id);
/* 129 */     setDirty();
/* 130 */     return true;
/*     */   }
/*     */   
/*     */   public void removeModifiers() {
/* 134 */     for (AttributeModifier modifier : getModifiers()) {
/* 135 */       removeModifier(modifier);
/*     */     }
/*     */   }
/*     */   
/*     */   public double getValue() {
/* 140 */     if (this.dirty) {
/* 141 */       this.cachedValue = calculateValue();
/* 142 */       this.dirty = false;
/*     */     } 
/*     */     
/* 145 */     return this.cachedValue;
/*     */   }
/*     */   
/*     */   private double calculateValue() {
/* 149 */     double base = getBaseValue();
/*     */     
/* 151 */     for (AttributeModifier modifier : getModifiersOrEmpty(AttributeModifier.Operation.ADD_VALUE)) {
/* 152 */       base += modifier.amount();
/*     */     }
/*     */     
/* 155 */     double result = base;
/*     */     
/* 157 */     for (AttributeModifier modifier : getModifiersOrEmpty(AttributeModifier.Operation.ADD_MULTIPLIED_BASE)) {
/* 158 */       result += base * modifier.amount();
/*     */     }
/*     */     
/* 161 */     for (AttributeModifier modifier : getModifiersOrEmpty(AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)) {
/* 162 */       result *= 1.0D + modifier.amount();
/*     */     }
/*     */     
/* 165 */     return ((Attribute)this.attribute.value()).sanitizeValue(result);
/*     */   }
/*     */   
/*     */   private Collection<AttributeModifier> getModifiersOrEmpty(AttributeModifier.Operation operation) {
/* 169 */     return ((Map)this.modifiersByOperation.getOrDefault(operation, Map.of())).values();
/*     */   }
/*     */   
/*     */   public void replaceFrom(AttributeInstance other) {
/* 173 */     this.baseValue = other.baseValue;
/*     */     
/* 175 */     this.modifierById.clear();
/* 176 */     this.modifierById.putAll(other.modifierById);
/*     */     
/* 178 */     this.permanentModifiers.clear();
/* 179 */     this.permanentModifiers.putAll(other.permanentModifiers);
/*     */     
/* 181 */     this.modifiersByOperation.clear();
/* 182 */     other.modifiersByOperation.forEach((operation, attributeModifiers) -> getModifiers(operation).putAll(attributeModifiers));
/*     */ 
/*     */     
/* 185 */     setDirty();
/*     */   }
/*     */   
/*     */   public Packed pack() {
/* 189 */     return new Packed(this.attribute, this.baseValue, 
/*     */ 
/*     */         
/* 192 */         List.copyOf(this.permanentModifiers.values()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void apply(Packed packed) {
/* 197 */     this.baseValue = packed.baseValue;
/*     */     
/* 199 */     for (AttributeModifier modifier : packed.modifiers) {
/* 200 */       this.modifierById.put(modifier.id(), modifier);
/* 201 */       getModifiers(modifier.operation()).put(modifier.id(), modifier);
/* 202 */       this.permanentModifiers.put(modifier.id(), modifier);
/*     */     } 
/* 204 */     setDirty();
/*     */   }
/*     */   public static final class Packed extends Record { private final Holder<Attribute> attribute; private final double baseValue; private final List<AttributeModifier> modifiers; public static final Codec<Packed> CODEC;
/* 207 */     public Packed(Holder<Attribute> attribute, double baseValue, List<AttributeModifier> modifiers) { this.attribute = attribute; this.baseValue = baseValue; this.modifiers = modifiers; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #207	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 207 */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed; } public Holder<Attribute> attribute() { return this.attribute; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #207	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #207	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;
/* 207 */       //   0	8	1	o	Ljava/lang/Object; } public double baseValue() { return this.baseValue; } public List<AttributeModifier> modifiers() { return this.modifiers; }
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 212 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)BuiltInRegistries.ATTRIBUTE.holderByNameCodec().fieldOf("id").forGetter(Packed::attribute), (App)Codec.DOUBLE.fieldOf("base").orElse(0.0D).forGetter(Packed::baseValue), (App)AttributeModifier.CODEC.listOf().optionalFieldOf("modifiers", List.of()).forGetter(Packed::modifiers)).apply((Applicative)i, Packed::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 218 */     public static final Codec<List<Packed>> LIST_CODEC = CODEC.listOf(); }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/attributes/AttributeInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */