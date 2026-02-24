/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ 
/*    */ public final class Instrument extends Record {
/*    */   private final Holder<SoundEvent> soundEvent;
/*    */   private final float useDuration;
/*    */   private final float range;
/*    */   private final Component description;
/*    */   public static final com.mojang.serialization.Codec<Instrument> DIRECT_CODEC;
/*    */   
/* 16 */   public Instrument(Holder<SoundEvent> soundEvent, float useDuration, float range, Component description) { this.soundEvent = soundEvent; this.useDuration = useDuration; this.range = range; this.description = description; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/Instrument;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/world/item/Instrument; } public Holder<SoundEvent> soundEvent() { return this.soundEvent; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/Instrument;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/Instrument; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/Instrument;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/Instrument;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public float useDuration() { return this.useDuration; } public float range() { return this.range; } public Component description() { return this.description; } static {
/* 17 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.fieldOf("sound_event").forGetter(Instrument::soundEvent), (App)net.minecraft.util.ExtraCodecs.POSITIVE_FLOAT.fieldOf("use_duration").forGetter(Instrument::useDuration), (App)net.minecraft.util.ExtraCodecs.POSITIVE_FLOAT.fieldOf("range").forGetter(Instrument::range), (App)net.minecraft.network.chat.ComponentSerialization.CODEC.fieldOf("description").forGetter(Instrument::description)).apply((com.mojang.datafixers.kinds.Applicative)i, Instrument::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, Instrument> DIRECT_STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(SoundEvent.STREAM_CODEC, Instrument::soundEvent, net.minecraft.network.codec.ByteBufCodecs.FLOAT, Instrument::useDuration, net.minecraft.network.codec.ByteBufCodecs.FLOAT, Instrument::range, net.minecraft.network.chat.ComponentSerialization.STREAM_CODEC, Instrument::description, Instrument::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static final com.mojang.serialization.Codec<Holder<Instrument>> CODEC = (com.mojang.serialization.Codec<Holder<Instrument>>)net.minecraft.resources.RegistryFileCodec.create(net.minecraft.core.registries.Registries.INSTRUMENT, DIRECT_CODEC);
/* 32 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, Holder<Instrument>> STREAM_CODEC = net.minecraft.network.codec.ByteBufCodecs.holder(net.minecraft.core.registries.Registries.INSTRUMENT, DIRECT_STREAM_CODEC);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/Instrument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */