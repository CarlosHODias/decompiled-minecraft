/*    */ package net.minecraft.world.timeline;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import java.util.function.LongSupplier;
/*    */ import net.minecraft.util.KeyframeTrack;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*    */ 
/*    */ public final class AttributeTrack<Value, Argument> extends Record {
/*    */   private final AttributeModifier<Value, Argument> modifier;
/*    */   private final KeyframeTrack<Argument> argumentTrack;
/*    */   
/* 14 */   public AttributeTrack(AttributeModifier<Value, Argument> modifier, KeyframeTrack<Argument> argumentTrack) { this.modifier = modifier; this.argumentTrack = argumentTrack; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/timeline/AttributeTrack;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/timeline/AttributeTrack;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/timeline/AttributeTrack<TValue;TArgument;>; } public AttributeModifier<Value, Argument> modifier() { return this.modifier; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/timeline/AttributeTrack;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/timeline/AttributeTrack;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/timeline/AttributeTrack<TValue;TArgument;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/timeline/AttributeTrack;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/timeline/AttributeTrack;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 14 */     //   0	8	0	this	Lnet/minecraft/world/timeline/AttributeTrack<TValue;TArgument;>; } public KeyframeTrack<Argument> argumentTrack() { return this.argumentTrack; }
/*    */ 
/*    */ 
/*    */   
/*    */   public static <Value> com.mojang.serialization.Codec<AttributeTrack<Value, ?>> createCodec(EnvironmentAttribute<Value> attribute) {
/* 19 */     MapCodec<AttributeModifier<Value, ?>> modifierCodec = attribute.type().modifierCodec().optionalFieldOf("modifier", AttributeModifier.override());
/* 20 */     return modifierCodec.dispatch(AttributeTrack::modifier, net.minecraft.util.Util.memoize(modifier -> createCodecWithModifier(attribute, modifier)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static <Value, Argument> MapCodec<AttributeTrack<Value, Argument>> createCodecWithModifier(EnvironmentAttribute<Value> attribute, AttributeModifier<Value, Argument> modifier) {
/* 26 */     return KeyframeTrack.mapCodec(modifier.argumentCodec(attribute)).xmap(track -> new AttributeTrack(modifier, track), AttributeTrack::argumentTrack);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AttributeTrackSampler<Value, Argument> bakeSampler(EnvironmentAttribute<Value> attribute, Optional<Integer> periodTicks, LongSupplier dayTimeGetter) {
/* 33 */     return new AttributeTrackSampler<>(periodTicks, this.modifier, this.argumentTrack, this.modifier.argumentKeyframeLerp(attribute), dayTimeGetter);
/*    */   }
/*    */   
/*    */   public static com.mojang.serialization.DataResult<AttributeTrack<?, ?>> validatePeriod(AttributeTrack<?, ?> track, int periodTicks) {
/* 37 */     return KeyframeTrack.validatePeriod(track.argumentTrack(), periodTicks).map(ignored -> track);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/timeline/AttributeTrack.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */