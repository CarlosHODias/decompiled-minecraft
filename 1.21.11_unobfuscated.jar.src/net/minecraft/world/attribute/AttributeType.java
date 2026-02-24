/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ public final class AttributeType<Value> extends Record {
/*    */   private final com.mojang.serialization.Codec<Value> valueCodec;
/*    */   private final java.util.Map<net.minecraft.world.attribute.modifier.AttributeModifier.OperationId, net.minecraft.world.attribute.modifier.AttributeModifier<Value, ?>> modifierLibrary;
/*    */   private final com.mojang.serialization.Codec<net.minecraft.world.attribute.modifier.AttributeModifier<Value, ?>> modifierCodec;
/*    */   private final LerpFunction<Value> keyframeLerp;
/*    */   private final LerpFunction<Value> stateChangeLerp;
/*    */   private final LerpFunction<Value> spatialLerp;
/*    */   private final LerpFunction<Value> partialTickLerp;
/*    */   
/* 12 */   public AttributeType(com.mojang.serialization.Codec<Value> valueCodec, java.util.Map<net.minecraft.world.attribute.modifier.AttributeModifier.OperationId, net.minecraft.world.attribute.modifier.AttributeModifier<Value, ?>> modifierLibrary, com.mojang.serialization.Codec<net.minecraft.world.attribute.modifier.AttributeModifier<Value, ?>> modifierCodec, LerpFunction<Value> keyframeLerp, LerpFunction<Value> stateChangeLerp, LerpFunction<Value> spatialLerp, LerpFunction<Value> partialTickLerp) { this.valueCodec = valueCodec; this.modifierLibrary = modifierLibrary; this.modifierCodec = modifierCodec; this.keyframeLerp = keyframeLerp; this.stateChangeLerp = stateChangeLerp; this.spatialLerp = spatialLerp; this.partialTickLerp = partialTickLerp; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/AttributeType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/AttributeType;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/attribute/AttributeType<TValue;>; } public com.mojang.serialization.Codec<Value> valueCodec() { return this.valueCodec; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/AttributeType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/AttributeType;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 12 */     //   0	8	0	this	Lnet/minecraft/world/attribute/AttributeType<TValue;>; } public java.util.Map<net.minecraft.world.attribute.modifier.AttributeModifier.OperationId, net.minecraft.world.attribute.modifier.AttributeModifier<Value, ?>> modifierLibrary() { return this.modifierLibrary; } public com.mojang.serialization.Codec<net.minecraft.world.attribute.modifier.AttributeModifier<Value, ?>> modifierCodec() { return this.modifierCodec; } public LerpFunction<Value> keyframeLerp() { return this.keyframeLerp; } public LerpFunction<Value> stateChangeLerp() { return this.stateChangeLerp; } public LerpFunction<Value> spatialLerp() { return this.spatialLerp; } public LerpFunction<Value> partialTickLerp() { return this.partialTickLerp; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <Value> AttributeType<Value> ofInterpolated(com.mojang.serialization.Codec<Value> valueCodec, java.util.Map<net.minecraft.world.attribute.modifier.AttributeModifier.OperationId, net.minecraft.world.attribute.modifier.AttributeModifier<Value, ?>> modifierLibrary, LerpFunction<Value> lerp) {
/* 28 */     return ofInterpolated(valueCodec, modifierLibrary, lerp, lerp);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <Value> AttributeType<Value> ofInterpolated(com.mojang.serialization.Codec<Value> valueCodec, java.util.Map<net.minecraft.world.attribute.modifier.AttributeModifier.OperationId, net.minecraft.world.attribute.modifier.AttributeModifier<Value, ?>> modifierLibrary, LerpFunction<Value> lerp, LerpFunction<Value> partialTickLerp) {
/* 37 */     return new AttributeType<>(valueCodec, modifierLibrary, 
/*    */ 
/*    */         
/* 40 */         createModifierCodec(modifierLibrary), lerp, lerp, lerp, partialTickLerp);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <Value> AttributeType<Value> ofNotInterpolated(com.mojang.serialization.Codec<Value> valueCodec, java.util.Map<net.minecraft.world.attribute.modifier.AttributeModifier.OperationId, net.minecraft.world.attribute.modifier.AttributeModifier<Value, ?>> modifierLibrary) {
/* 52 */     return new AttributeType<>(valueCodec, modifierLibrary, 
/*    */ 
/*    */         
/* 55 */         createModifierCodec(modifierLibrary), 
/*    */         
/* 57 */         LerpFunction.ofStep(1.0F), 
/*    */         
/* 59 */         LerpFunction.ofStep(0.0F), 
/*    */         
/* 61 */         LerpFunction.ofStep(0.5F), 
/*    */         
/* 63 */         LerpFunction.ofStep(0.0F));
/*    */   }
/*    */ 
/*    */   
/*    */   public static <Value> AttributeType<Value> ofNotInterpolated(com.mojang.serialization.Codec<Value> valueCodec) {
/* 68 */     return ofNotInterpolated(valueCodec, java.util.Map.of());
/*    */   }
/*    */   
/*    */   private static <Value> com.mojang.serialization.Codec<net.minecraft.world.attribute.modifier.AttributeModifier<Value, ?>> createModifierCodec(java.util.Map<net.minecraft.world.attribute.modifier.AttributeModifier.OperationId, net.minecraft.world.attribute.modifier.AttributeModifier<Value, ?>> modifiers) {
/* 72 */     com.google.common.collect.ImmutableBiMap<net.minecraft.world.attribute.modifier.AttributeModifier.OperationId, net.minecraft.world.attribute.modifier.AttributeModifier<Value, ?>> modifierLookup = com.google.common.collect.ImmutableBiMap.builder()
/* 73 */       .put(net.minecraft.world.attribute.modifier.AttributeModifier.OperationId.OVERRIDE, net.minecraft.world.attribute.modifier.AttributeModifier.override())
/* 74 */       .putAll(modifiers)
/* 75 */       .buildOrThrow();
/*    */ 
/*    */ 
/*    */     
/* 79 */     java.util.Objects.requireNonNull(modifierLookup);
/* 80 */     java.util.Objects.requireNonNull(modifierLookup.inverse()); return net.minecraft.util.ExtraCodecs.idResolverCodec(net.minecraft.world.attribute.modifier.AttributeModifier.OperationId.CODEC, modifierLookup::get, modifierLookup.inverse()::get);
/*    */   }
/*    */ 
/*    */   
/*    */   public void checkAllowedModifier(net.minecraft.world.attribute.modifier.AttributeModifier<Value, ?> modifier) {
/* 85 */     if (modifier != net.minecraft.world.attribute.modifier.AttributeModifier.override() && !this.modifierLibrary.containsValue(modifier)) {
/* 86 */       throw new IllegalArgumentException("Modifier " + String.valueOf(modifier) + " is not valid for " + String.valueOf(this));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 92 */     return net.minecraft.util.Util.getRegisteredName(net.minecraft.core.registries.BuiltInRegistries.ATTRIBUTE_TYPE, this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/attribute/AttributeType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */