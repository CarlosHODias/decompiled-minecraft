/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ 
/*    */ public interface FloatModifier<Argument> extends AttributeModifier<Float, Argument> {
/*  9 */   public static final FloatModifier<FloatWithAlpha> ALPHA_BLEND = new FloatModifier<FloatWithAlpha>()
/*    */     {
/*    */       public Float apply(Float subject, FloatWithAlpha argument) {
/* 12 */         return Mth.lerp(argument.alpha(), subject, argument.value());
/*    */       }
/*    */ 
/*    */       
/*    */       public Codec<FloatWithAlpha> argumentCodec(EnvironmentAttribute<Float> type) {
/* 17 */         return FloatWithAlpha.CODEC;
/*    */       }
/*    */ 
/*    */       
/*    */       public LerpFunction<FloatWithAlpha> argumentKeyframeLerp(EnvironmentAttribute<Float> type) {
/* 22 */         return (alpha, from, to) -> new FloatWithAlpha(Mth.lerp(alpha, from.value(), to.value()), Mth.lerp(alpha, from.alpha(), to.alpha()));
/*    */       }
/*    */     };
/*    */ 
/*    */   
/*    */   public static final FloatModifier<Float> SUBTRACT;
/*    */   public static final FloatModifier<Float> MULTIPLY;
/* 29 */   public static final FloatModifier<Float> ADD = Float::sum; static {
/* 30 */     SUBTRACT = ((a, b) -> a - b);
/* 31 */     MULTIPLY = ((a, b) -> a * b);
/* 32 */   } public static final FloatModifier<Float> MINIMUM = Math::min;
/* 33 */   public static final FloatModifier<Float> MAXIMUM = Math::max;
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Simple
/*    */     extends FloatModifier<Float> {
/*    */     default Codec<Float> argumentCodec(EnvironmentAttribute<Float> type) {
/* 39 */       return (Codec<Float>)Codec.FLOAT;
/*    */     }
/*    */ 
/*    */     
/*    */     default LerpFunction<Float> argumentKeyframeLerp(EnvironmentAttribute<Float> type) {
/* 44 */       return LerpFunction.ofFloat();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/attribute/modifier/FloatModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */