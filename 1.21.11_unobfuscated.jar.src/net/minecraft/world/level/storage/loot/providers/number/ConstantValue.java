/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class ConstantValue extends Record implements NumberProvider {
/*    */   private final float value;
/*    */   
/*  8 */   public ConstantValue(float value) { this.value = value; } public static final com.mojang.serialization.MapCodec<ConstantValue> CODEC; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/number/ConstantValue;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/ConstantValue; } public float value() { return this.value; } static {
/*  9 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.FLOAT.fieldOf("value").forGetter(ConstantValue::value)).apply((com.mojang.datafixers.kinds.Applicative)i, ConstantValue::new));
/*    */   }
/*    */ 
/*    */   
/* 13 */   public static final com.mojang.serialization.Codec<ConstantValue> INLINE_CODEC = com.mojang.serialization.Codec.FLOAT.xmap(ConstantValue::new, ConstantValue::value);
/*    */ 
/*    */   
/*    */   public LootNumberProviderType getType() {
/* 17 */     return NumberProviders.CONSTANT;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getFloat(net.minecraft.world.level.storage.loot.LootContext random) {
/* 22 */     return this.value;
/*    */   }
/*    */   
/*    */   public static ConstantValue exactly(float value) {
/* 26 */     return new ConstantValue(value);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 32 */     if (this == o) {
/* 33 */       return true;
/*    */     }
/* 35 */     if (o == null || getClass() != o.getClass()) {
/* 36 */       return false;
/*    */     }
/*    */     
/* 39 */     return (Float.compare(((ConstantValue)o).value, this.value) == 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 44 */     return (this.value != 0.0F) ? Float.floatToIntBits(this.value) : 0;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/providers/number/ConstantValue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */