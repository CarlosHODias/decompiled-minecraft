/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.JukeboxPlayable;
/*    */ import net.minecraft.world.item.JukeboxSong;
/*    */ 
/*    */ public final class JukeboxPlayablePredicate extends Record implements net.minecraft.advancements.criterion.SingleComponentItemPredicate<JukeboxPlayable> {
/*    */   private final Optional<HolderSet<JukeboxSong>> song;
/*    */   public static final com.mojang.serialization.Codec<JukeboxPlayablePredicate> CODEC;
/*    */   
/* 18 */   public JukeboxPlayablePredicate(Optional<HolderSet<JukeboxSong>> song) { this.song = song; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/JukeboxPlayablePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 18 */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/JukeboxPlayablePredicate; } public Optional<HolderSet<JukeboxSong>> song() { return this.song; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/JukeboxPlayablePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/JukeboxPlayablePredicate; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/JukeboxPlayablePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/JukeboxPlayablePredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   } static {
/* 22 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)net.minecraft.core.RegistryCodecs.homogeneousList(Registries.JUKEBOX_SONG).optionalFieldOf("song").forGetter(JukeboxPlayablePredicate::song)).apply((Applicative)i, JukeboxPlayablePredicate::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.core.component.DataComponentType<JukeboxPlayable> componentType() {
/* 28 */     return net.minecraft.core.component.DataComponents.JUKEBOX_PLAYABLE;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(JukeboxPlayable value) {
/* 33 */     if (this.song.isPresent()) {
/*    */       boolean songIsPresent = false;
/* 35 */       for (net.minecraft.core.Holder<JukeboxSong> maybeSong : this.song.get()) {
/* 36 */         Optional<ResourceKey<JukeboxSong>> songId = maybeSong.unwrapKey();
/* 37 */         if (songId.isEmpty()) {
/*    */           continue;
/*    */         }
/*    */         
/* 41 */         if (songId.equals(value.song().key())) {
/* 42 */           songIsPresent = true;
/*    */           
/*    */           break;
/*    */         } 
/*    */       } 
/* 47 */       return songIsPresent;
/*    */     } 
/*    */     
/* 50 */     return true;
/*    */   }
/*    */   
/*    */   public static JukeboxPlayablePredicate any() {
/* 54 */     return new JukeboxPlayablePredicate(Optional.empty());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/component/predicates/JukeboxPlayablePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */