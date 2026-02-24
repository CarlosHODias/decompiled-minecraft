/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public final class AmbientSounds extends Record {
/*    */   private final Optional<net.minecraft.core.Holder<net.minecraft.sounds.SoundEvent>> loop;
/*    */   private final Optional<AmbientMoodSettings> mood;
/*    */   private final List<AmbientAdditionsSettings> additions;
/*    */   
/* 12 */   public AmbientSounds(Optional<net.minecraft.core.Holder<net.minecraft.sounds.SoundEvent>> loop, Optional<AmbientMoodSettings> mood, List<AmbientAdditionsSettings> additions) { this.loop = loop; this.mood = mood; this.additions = additions; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/AmbientSounds;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientSounds; } public Optional<net.minecraft.core.Holder<net.minecraft.sounds.SoundEvent>> loop() { return this.loop; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/AmbientSounds;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientSounds; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/AmbientSounds;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/AmbientSounds;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<AmbientMoodSettings> mood() { return this.mood; } public List<AmbientAdditionsSettings> additions() { return this.additions; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static final AmbientSounds EMPTY = new AmbientSounds(Optional.empty(), Optional.empty(), List.of());
/* 18 */   public static final AmbientSounds LEGACY_CAVE_SETTINGS = new AmbientSounds(Optional.empty(), Optional.of(AmbientMoodSettings.LEGACY_CAVE_SETTINGS), List.of()); public static final com.mojang.serialization.Codec<AmbientSounds> CODEC;
/*    */   static {
/* 20 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((App)net.minecraft.sounds.SoundEvent.CODEC.optionalFieldOf("loop").forGetter(AmbientSounds::loop), (App)AmbientMoodSettings.CODEC.optionalFieldOf("mood").forGetter(AmbientSounds::mood), (App)net.minecraft.util.ExtraCodecs.compactListCodec(AmbientAdditionsSettings.CODEC).optionalFieldOf("additions", List.of()).forGetter(AmbientSounds::additions)).apply((com.mojang.datafixers.kinds.Applicative)i, AmbientSounds::new));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/attribute/AmbientSounds.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */