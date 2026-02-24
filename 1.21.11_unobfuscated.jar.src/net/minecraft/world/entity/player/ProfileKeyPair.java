/*    */ package net.minecraft.world.entity.player;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ 
/*    */ public final class ProfileKeyPair extends Record {
/*    */   private final java.security.PrivateKey privateKey;
/*    */   private final ProfilePublicKey publicKey;
/*    */   private final java.time.Instant refreshedAfter;
/*    */   public static final com.mojang.serialization.Codec<ProfileKeyPair> CODEC;
/*    */   
/* 11 */   public ProfileKeyPair(java.security.PrivateKey privateKey, ProfilePublicKey publicKey, java.time.Instant refreshedAfter) { this.privateKey = privateKey; this.publicKey = publicKey; this.refreshedAfter = refreshedAfter; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/player/ProfileKeyPair;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/entity/player/ProfileKeyPair; } public java.security.PrivateKey privateKey() { return this.privateKey; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/player/ProfileKeyPair;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/player/ProfileKeyPair; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/player/ProfileKeyPair;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/player/ProfileKeyPair;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public ProfilePublicKey publicKey() { return this.publicKey; } public java.time.Instant refreshedAfter() { return this.refreshedAfter; } static {
/* 12 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((App)net.minecraft.util.Crypt.PRIVATE_KEY_CODEC.fieldOf("private_key").forGetter(ProfileKeyPair::privateKey), (App)ProfilePublicKey.TRUSTED_CODEC.fieldOf("public_key").forGetter(ProfileKeyPair::publicKey), (App)net.minecraft.util.ExtraCodecs.INSTANT_ISO8601.fieldOf("refreshed_after").forGetter(ProfileKeyPair::refreshedAfter)).apply((com.mojang.datafixers.kinds.Applicative)i, ProfileKeyPair::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean dueRefresh() {
/* 19 */     return this.refreshedAfter.isBefore(java.time.Instant.now());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/player/ProfileKeyPair.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */