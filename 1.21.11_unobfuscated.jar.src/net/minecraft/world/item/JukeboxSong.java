/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class JukeboxSong extends Record {
/*    */   private final Holder<SoundEvent> soundEvent;
/*    */   private final Component description;
/*    */   private final float lengthInSeconds;
/*    */   private final int comparatorOutput;
/*    */   public static final Codec<JukeboxSong> DIRECT_CODEC;
/*    */   
/* 22 */   public JukeboxSong(Holder<SoundEvent> soundEvent, Component description, float lengthInSeconds, int comparatorOutput) { this.soundEvent = soundEvent; this.description = description; this.lengthInSeconds = lengthInSeconds; this.comparatorOutput = comparatorOutput; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/JukeboxSong;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 22 */     //   0	7	0	this	Lnet/minecraft/world/item/JukeboxSong; } public Holder<SoundEvent> soundEvent() { return this.soundEvent; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/JukeboxSong;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/JukeboxSong; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/JukeboxSong;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/JukeboxSong;
/* 22 */     //   0	8	1	o	Ljava/lang/Object; } public Component description() { return this.description; } public float lengthInSeconds() { return this.lengthInSeconds; } public int comparatorOutput() { return this.comparatorOutput; } static {
/* 23 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.fieldOf("sound_event").forGetter(JukeboxSong::soundEvent), (App)ComponentSerialization.CODEC.fieldOf("description").forGetter(JukeboxSong::description), (App)ExtraCodecs.POSITIVE_FLOAT.fieldOf("length_in_seconds").forGetter(JukeboxSong::lengthInSeconds), (App)ExtraCodecs.intRange(0, 15).fieldOf("comparator_output").forGetter(JukeboxSong::comparatorOutput)).apply((com.mojang.datafixers.kinds.Applicative)i, JukeboxSong::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, JukeboxSong> DIRECT_STREAM_CODEC = StreamCodec.composite(SoundEvent.STREAM_CODEC, JukeboxSong::soundEvent, ComponentSerialization.STREAM_CODEC, JukeboxSong::description, ByteBufCodecs.FLOAT, JukeboxSong::lengthInSeconds, ByteBufCodecs.VAR_INT, JukeboxSong::comparatorOutput, JukeboxSong::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public static final Codec<Holder<JukeboxSong>> CODEC = (Codec<Holder<JukeboxSong>>)net.minecraft.resources.RegistryFixedCodec.create(net.minecraft.core.registries.Registries.JUKEBOX_SONG);
/* 38 */   public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, Holder<JukeboxSong>> STREAM_CODEC = ByteBufCodecs.holder(net.minecraft.core.registries.Registries.JUKEBOX_SONG, DIRECT_STREAM_CODEC);
/*    */   private static final int SONG_END_PADDING_TICKS = 20;
/*    */   
/*    */   public int lengthInTicks() {
/* 42 */     return net.minecraft.util.Mth.ceil(this.lengthInSeconds * 20.0F);
/*    */   }
/*    */   
/*    */   public boolean hasFinished(long ticksElapsed) {
/* 46 */     return (ticksElapsed >= (lengthInTicks() + 20));
/*    */   }
/*    */   
/*    */   public static java.util.Optional<Holder<JukeboxSong>> fromStack(net.minecraft.core.HolderLookup.Provider registries, ItemStack stack) {
/* 50 */     JukeboxPlayable jukeboxPlayable = (JukeboxPlayable)stack.get(net.minecraft.core.component.DataComponents.JUKEBOX_PLAYABLE);
/* 51 */     if (jukeboxPlayable != null) {
/* 52 */       return jukeboxPlayable.song().unwrap(registries);
/*    */     }
/* 54 */     return java.util.Optional.empty();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/JukeboxSong.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */