/*    */ package net.minecraft.sounds;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class SoundEvent extends Record {
/*    */   private final Identifier location;
/*    */   private final Optional<Float> fixedRange;
/*    */   public static final Codec<SoundEvent> DIRECT_CODEC;
/*    */   
/* 16 */   public SoundEvent(Identifier location, Optional<Float> fixedRange) { this.location = location; this.fixedRange = fixedRange; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/sounds/SoundEvent;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/sounds/SoundEvent; } public Identifier location() { return this.location; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/sounds/SoundEvent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/sounds/SoundEvent; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/sounds/SoundEvent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/sounds/SoundEvent;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Float> fixedRange() { return this.fixedRange; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 24 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("sound_id").forGetter(SoundEvent::location), (App)Codec.FLOAT.lenientOptionalFieldOf("range").forGetter(SoundEvent::fixedRange)).apply((com.mojang.datafixers.kinds.Applicative)i, SoundEvent::create));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static final Codec<Holder<SoundEvent>> CODEC = (Codec<Holder<SoundEvent>>)net.minecraft.resources.RegistryFileCodec.create(net.minecraft.core.registries.Registries.SOUND_EVENT, DIRECT_CODEC);
/*    */   
/* 31 */   public static final StreamCodec<io.netty.buffer.ByteBuf, SoundEvent> DIRECT_STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, SoundEvent::location, 
/*    */       
/* 33 */       net.minecraft.network.codec.ByteBufCodecs.FLOAT.apply(net.minecraft.network.codec.ByteBufCodecs::optional), SoundEvent::fixedRange, SoundEvent::create);
/*    */ 
/*    */ 
/*    */   
/* 37 */   public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, Holder<SoundEvent>> STREAM_CODEC = net.minecraft.network.codec.ByteBufCodecs.holder(net.minecraft.core.registries.Registries.SOUND_EVENT, DIRECT_STREAM_CODEC);
/*    */   
/*    */   private static SoundEvent create(Identifier location, Optional<Float> range) {
/* 40 */     return range.<SoundEvent>map(r -> createFixedRangeEvent(location, r)).orElseGet(() -> createVariableRangeEvent(location));
/*    */   }
/*    */   
/*    */   public static SoundEvent createVariableRangeEvent(Identifier location) {
/* 44 */     return new SoundEvent(location, Optional.empty());
/*    */   }
/*    */   
/*    */   public static SoundEvent createFixedRangeEvent(Identifier location, float range) {
/* 48 */     return new SoundEvent(location, Optional.of(range));
/*    */   }
/*    */   
/*    */   public float getRange(float volume) {
/* 52 */     return (Float)this.fixedRange.orElse((volume > 1.0F) ? (16.0F * volume) : 16.0F);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/sounds/SoundEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */