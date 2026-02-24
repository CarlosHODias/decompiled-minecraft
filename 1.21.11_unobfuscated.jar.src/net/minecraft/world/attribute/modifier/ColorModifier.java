/*    */ package net.minecraft.world.attribute.modifier;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ 
/*    */ public interface ColorModifier<Argument> extends AttributeModifier<Integer, Argument> {
/* 13 */   public static final ColorModifier<Integer> ALPHA_BLEND = new ColorModifier<Integer>()
/*    */     {
/*    */       public Integer apply(Integer subject, Integer argument) {
/* 16 */         return ARGB.alphaBlend(subject, argument);
/*    */       }
/*    */ 
/*    */       
/*    */       public Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> type) {
/* 21 */         return ExtraCodecs.STRING_ARGB_COLOR;
/*    */       }
/*    */ 
/*    */       
/*    */       public LerpFunction<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> type) {
/* 26 */         return LerpFunction.ofColor();
/*    */       }
/*    */     };
/*    */   
/* 30 */   public static final ColorModifier<Integer> ADD = ARGB::addRgb;
/* 31 */   public static final ColorModifier<Integer> SUBTRACT = ARGB::subtractRgb;
/* 32 */   public static final ColorModifier<Integer> MULTIPLY_RGB = ARGB::multiply;
/* 33 */   public static final ColorModifier<Integer> MULTIPLY_ARGB = ARGB::multiply;
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface RgbModifier
/*    */     extends ColorModifier<Integer> {
/*    */     default Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> type) {
/* 39 */       return ExtraCodecs.STRING_RGB_COLOR;
/*    */     }
/*    */ 
/*    */     
/*    */     default LerpFunction<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> type) {
/* 44 */       return LerpFunction.ofColor();
/*    */     }
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface ArgbModifier
/*    */     extends ColorModifier<Integer> {
/*    */     default Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> type) {
/* 52 */       return Codec.either(ExtraCodecs.STRING_ARGB_COLOR, ExtraCodecs.RGB_COLOR_CODEC).xmap(Either::unwrap, color -> (ARGB.alpha(color) == 255) ? Either.right(color) : Either.left(color));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     default LerpFunction<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> type) {
/* 60 */       return LerpFunction.ofColor();
/*    */     }
/*    */   }
/*    */   
/* 64 */   public static final ColorModifier<BlendToGray> BLEND_TO_GRAY = new ColorModifier<BlendToGray>()
/*    */     {
/*    */       public Integer apply(Integer subject, ColorModifier.BlendToGray argument) {
/* 67 */         int multipliedGreyscale = ARGB.scaleRGB(ARGB.greyscale(subject), argument.brightness);
/* 68 */         return ARGB.srgbLerp(argument.factor, subject, multipliedGreyscale);
/*    */       }
/*    */ 
/*    */       
/*    */       public Codec<ColorModifier.BlendToGray> argumentCodec(EnvironmentAttribute<Integer> type) {
/* 73 */         return ColorModifier.BlendToGray.CODEC;
/*    */       }
/*    */ 
/*    */       
/*    */       public LerpFunction<ColorModifier.BlendToGray> argumentKeyframeLerp(EnvironmentAttribute<Integer> type) {
/* 78 */         return (alpha, from, to) -> new ColorModifier.BlendToGray(Mth.lerp(alpha, from.brightness, to.brightness), Mth.lerp(alpha, from.factor, to.factor));
/*    */       }
/*    */     }; public static final class BlendToGray extends Record {
/*    */     private final float brightness; private final float factor; public static final Codec<BlendToGray> CODEC; public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/modifier/ColorModifier$BlendToGray;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #85	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/ColorModifier$BlendToGray;
/*    */     } public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/modifier/ColorModifier$BlendToGray;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #85	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/ColorModifier$BlendToGray;
/* 85 */     } public BlendToGray(float brightness, float factor) { this.brightness = brightness; this.factor = factor; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/modifier/ColorModifier$BlendToGray;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #85	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/attribute/modifier/ColorModifier$BlendToGray;
/* 85 */       //   0	8	1	o	Ljava/lang/Object; } public float brightness() { return this.brightness; } public float factor() { return this.factor; }
/*    */ 
/*    */     
/*    */     static {
/* 89 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.floatRange(0.0F, 1.0F).fieldOf("brightness").forGetter(BlendToGray::brightness), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("factor").forGetter(BlendToGray::factor)).apply((com.mojang.datafixers.kinds.Applicative)i, BlendToGray::new));
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/attribute/modifier/ColorModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */