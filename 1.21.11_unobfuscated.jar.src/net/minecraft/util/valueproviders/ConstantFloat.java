/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class ConstantFloat extends FloatProvider {
/*  8 */   public static final ConstantFloat ZERO = new ConstantFloat(0.0F);
/*    */   
/* 10 */   public static final MapCodec<ConstantFloat> CODEC = Codec.FLOAT.fieldOf("value").xmap(ConstantFloat::of, ConstantFloat::getValue);
/*    */   
/*    */   private final float value;
/*    */   
/*    */   public static ConstantFloat of(float value) {
/* 15 */     if (value == 0.0F) {
/* 16 */       return ZERO;
/*    */     }
/* 18 */     return new ConstantFloat(value);
/*    */   }
/*    */   
/*    */   private ConstantFloat(float value) {
/* 22 */     this.value = value;
/*    */   }
/*    */   
/*    */   public float getValue() {
/* 26 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public float sample(RandomSource random) {
/* 31 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getMinValue() {
/* 36 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getMaxValue() {
/* 41 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public FloatProviderType<?> getType() {
/* 46 */     return FloatProviderType.CONSTANT;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 51 */     return Float.toString(this.value);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/valueproviders/ConstantFloat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */