/*    */ package net.minecraft.core.component;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class TypedDataComponent<T> extends Record {
/*    */   private final DataComponentType<T> type;
/*    */   private final T value;
/*    */   
/* 11 */   public TypedDataComponent(DataComponentType<T> type, T value) { this.type = type; this.value = value; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/TypedDataComponent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/TypedDataComponent;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 11 */     //   0	7	0	this	Lnet/minecraft/core/component/TypedDataComponent<TT;>; } public DataComponentType<T> type() { return this.type; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/TypedDataComponent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/TypedDataComponent;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 11 */     //   0	8	0	this	Lnet/minecraft/core/component/TypedDataComponent<TT;>; } public T value() { return this.value; }
/* 12 */    public static final StreamCodec<RegistryFriendlyByteBuf, TypedDataComponent<?>> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, TypedDataComponent<?>>()
/*    */     {
/*    */       public TypedDataComponent<?> decode(RegistryFriendlyByteBuf input) {
/* 15 */         DataComponentType<?> type = (DataComponentType)DataComponentType.STREAM_CODEC.decode(input);
/* 16 */         return decodeTyped(input, type);
/*    */       }
/*    */       
/*    */       private static <T> TypedDataComponent<T> decodeTyped(RegistryFriendlyByteBuf input, DataComponentType<T> type) {
/* 20 */         return new TypedDataComponent<>(type, (T)type.streamCodec().decode(input));
/*    */       }
/*    */ 
/*    */       
/*    */       public void encode(RegistryFriendlyByteBuf output, TypedDataComponent<?> value) {
/* 25 */         encodeCap(output, value);
/*    */       }
/*    */       
/*    */       private static <T> void encodeCap(RegistryFriendlyByteBuf output, TypedDataComponent<T> component) {
/* 29 */         DataComponentType.STREAM_CODEC.encode(output, component.type());
/* 30 */         component.type().streamCodec().encode(output, component.value());
/*    */       }
/*    */     };
/*    */   
/*    */   static TypedDataComponent<?> fromEntryUnchecked(java.util.Map.Entry<DataComponentType<?>, Object> entry) {
/* 35 */     return createUnchecked(entry.getKey(), entry.getValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T> TypedDataComponent<T> createUnchecked(DataComponentType<T> type, Object value) {
/* 40 */     return new TypedDataComponent<>(type, (T)value);
/*    */   }
/*    */   
/*    */   public void applyTo(PatchedDataComponentMap components) {
/* 44 */     components.set(this.type, this.value);
/*    */   }
/*    */   
/*    */   public <D> com.mojang.serialization.DataResult<D> encodeValue(com.mojang.serialization.DynamicOps<D> ops) {
/* 48 */     Codec<T> codec = this.type.codec();
/* 49 */     if (codec == null) {
/* 50 */       return com.mojang.serialization.DataResult.error(() -> "Component of type " + String.valueOf(this.type) + " is not encodable");
/*    */     }
/* 52 */     return codec.encodeStart(ops, this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return String.valueOf(this.type) + "=>" + String.valueOf(this.type);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/component/TypedDataComponent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */