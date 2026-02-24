/*    */ package net.minecraft.network.protocol.status;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public final class ServerStatus extends Record {
/*    */   private final net.minecraft.network.chat.Component description;
/*    */   private final Optional<Players> players;
/*    */   private final Optional<Version> version;
/*    */   private final Optional<Favicon> favicon;
/*    */   private final boolean enforcesSecureChat;
/*    */   public static final Codec<ServerStatus> CODEC;
/*    */   
/* 18 */   public ServerStatus(net.minecraft.network.chat.Component description, Optional<Players> players, Optional<Version> version, Optional<Favicon> favicon, boolean enforcesSecureChat) { this.description = description; this.players = players; this.version = version; this.favicon = favicon; this.enforcesSecureChat = enforcesSecureChat; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/status/ServerStatus;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 18 */     //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus; } public net.minecraft.network.chat.Component description() { return this.description; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/status/ServerStatus;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/status/ServerStatus;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/status/ServerStatus;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Players> players() { return this.players; } public Optional<Version> version() { return this.version; } public Optional<Favicon> favicon() { return this.favicon; } public boolean enforcesSecureChat() { return this.enforcesSecureChat; } static {
/* 19 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)net.minecraft.network.chat.ComponentSerialization.CODEC.lenientOptionalFieldOf("description", net.minecraft.network.chat.CommonComponents.EMPTY).forGetter(ServerStatus::description), (App)Players.CODEC.lenientOptionalFieldOf("players").forGetter(ServerStatus::players), (App)Version.CODEC.lenientOptionalFieldOf("version").forGetter(ServerStatus::version), (App)Favicon.CODEC.lenientOptionalFieldOf("favicon").forGetter(ServerStatus::favicon), (App)Codec.BOOL.lenientOptionalFieldOf("enforcesSecureChat", false).forGetter(ServerStatus::enforcesSecureChat)).apply((Applicative)i, ServerStatus::new));
/*    */   }
/*    */   
/*    */   public static final class Players extends Record {
/*    */     private final int max;
/*    */     private final int online;
/*    */     private final java.util.List<net.minecraft.server.players.NameAndId> sample;
/*    */     public static final Codec<Players> CODEC;
/*    */     
/* 28 */     public Players(int max, int online, java.util.List<net.minecraft.server.players.NameAndId> sample) { this.max = max; this.online = online; this.sample = sample; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/status/ServerStatus$Players;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Players; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/status/ServerStatus$Players;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Players; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/status/ServerStatus$Players;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Players;
/* 28 */       //   0	8	1	o	Ljava/lang/Object; } public int max() { return this.max; } public int online() { return this.online; } public java.util.List<net.minecraft.server.players.NameAndId> sample() { return this.sample; } static {
/* 29 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("max").forGetter(Players::max), (App)Codec.INT.fieldOf("online").forGetter(Players::online), (App)net.minecraft.server.players.NameAndId.CODEC.listOf().lenientOptionalFieldOf("sample", java.util.List.of()).forGetter(Players::sample)).apply((Applicative)i, Players::new));
/*    */     } }
/*    */   
/*    */   public static final class Version extends Record { private final String name;
/*    */     private final int protocol;
/*    */     public static final Codec<Version> CODEC;
/*    */     
/* 36 */     public Version(String name, int protocol) { this.name = name; this.protocol = protocol; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/status/ServerStatus$Version;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Version; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/status/ServerStatus$Version;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Version; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/status/ServerStatus$Version;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Version;
/* 36 */       //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public int protocol() { return this.protocol; } static {
/* 37 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("name").forGetter(Version::name), (App)Codec.INT.fieldOf("protocol").forGetter(Version::protocol)).apply((Applicative)i, Version::new));
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public static Version current() {
/* 43 */       net.minecraft.WorldVersion version = net.minecraft.SharedConstants.getCurrentVersion();
/* 44 */       return new Version(version.name(), version.protocolVersion());
/*    */     } }
/*    */   public static final class Favicon extends Record { private final byte[] iconBytes; private static final String PREFIX = "data:image/png;base64,"; public static final Codec<Favicon> CODEC;
/*    */     
/* 48 */     public Favicon(byte[] iconBytes) { this.iconBytes = iconBytes; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/status/ServerStatus$Favicon;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Favicon; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/status/ServerStatus$Favicon;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Favicon; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/status/ServerStatus$Favicon;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Favicon;
/* 48 */       //   0	8	1	o	Ljava/lang/Object; } public byte[] iconBytes() { return this.iconBytes; }
/*    */     
/*    */     static {
/* 51 */       CODEC = Codec.STRING.comapFlatMap(string -> {
/*    */             if (!string.startsWith("data:image/png;base64,")) {
/*    */               return DataResult.error(());
/*    */             }
/*    */             
/*    */             try {
/*    */               String base64 = string.substring("data:image/png;base64,".length()).replaceAll("\n", "");
/*    */               
/*    */               byte[] iconBytes = java.util.Base64.getDecoder().decode(base64.getBytes(java.nio.charset.StandardCharsets.UTF_8));
/*    */               return DataResult.success(new Favicon(iconBytes));
/* 61 */             } catch (IllegalArgumentException e) {
/*    */               return DataResult.error(());
/*    */             } 
/*    */           }, favicon -> "data:image/png;base64," + new String(java.util.Base64.getEncoder().encode(favicon.iconBytes), java.nio.charset.StandardCharsets.UTF_8));
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/status/ServerStatus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */