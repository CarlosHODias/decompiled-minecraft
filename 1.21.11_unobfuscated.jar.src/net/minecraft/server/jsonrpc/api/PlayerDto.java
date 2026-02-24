/*    */ package net.minecraft.server.jsonrpc.api;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public final class PlayerDto extends Record {
/*    */   private final Optional<UUID> id;
/*    */   private final Optional<String> name;
/*    */   public static final com.mojang.serialization.MapCodec<PlayerDto> CODEC;
/*    */   
/* 14 */   public PlayerDto(Optional<UUID> id, Optional<String> name) { this.id = id; this.name = name; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/api/PlayerDto;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/PlayerDto; } public Optional<UUID> id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/api/PlayerDto;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/PlayerDto; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/api/PlayerDto;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/PlayerDto;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<String> name() { return this.name; }
/*    */    static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)net.minecraft.core.UUIDUtil.STRING_CODEC.optionalFieldOf("id").forGetter(PlayerDto::id), (App)com.mojang.serialization.Codec.STRING.optionalFieldOf("name").forGetter(PlayerDto::name)).apply((com.mojang.datafixers.kinds.Applicative)i, PlayerDto::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static PlayerDto from(GameProfile gameProfile) {
/* 22 */     return new PlayerDto(Optional.of(gameProfile.id()), Optional.of(gameProfile.name()));
/*    */   }
/*    */   
/*    */   public static PlayerDto from(net.minecraft.server.players.NameAndId nameAndId) {
/* 26 */     return new PlayerDto(Optional.of(nameAndId.id()), Optional.of(nameAndId.name()));
/*    */   }
/*    */   
/*    */   public static PlayerDto from(net.minecraft.server.level.ServerPlayer player) {
/* 30 */     GameProfile gameProfile = player.getGameProfile();
/* 31 */     return from(gameProfile);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/api/PlayerDto.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */