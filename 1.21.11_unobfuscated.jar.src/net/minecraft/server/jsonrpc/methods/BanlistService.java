/*     */ package net.minecraft.server.jsonrpc.methods;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.time.Instant;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.UserBanListEntry;
/*     */ 
/*     */ public class BanlistService {
/*     */   private static final String BAN_SOURCE = "Management server";
/*     */   
/*     */   public static final class UserBanDto extends Record { private final PlayerDto player;
/*     */     private final Optional<String> reason;
/*     */     private final Optional<String> source;
/*     */     private final Optional<Instant> expires;
/*     */     public static final com.mojang.serialization.MapCodec<UserBanDto> CODEC;
/*     */     
/*  28 */     public UserBanDto(PlayerDto player, Optional<String> reason, Optional<String> source, Optional<Instant> expires) { this.player = player; this.reason = reason; this.source = source; this.expires = expires; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  28 */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto; } public PlayerDto player() { return this.player; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;
/*  28 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<String> reason() { return this.reason; } public Optional<String> source() { return this.source; } public Optional<Instant> expires() { return this.expires; } static {
/*  29 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)PlayerDto.CODEC.codec().fieldOf("player").forGetter(UserBanDto::player), (App)Codec.STRING.optionalFieldOf("reason").forGetter(UserBanDto::reason), (App)Codec.STRING.optionalFieldOf("source").forGetter(UserBanDto::source), (App)net.minecraft.util.ExtraCodecs.INSTANT_ISO8601.optionalFieldOf("expires").forGetter(UserBanDto::expires)).apply((com.mojang.datafixers.kinds.Applicative)i, UserBanDto::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static UserBanDto from(BanlistService.UserBan ban) {
/*  37 */       return new UserBanDto(PlayerDto.from(ban.player()), Optional.ofNullable(ban.reason()), Optional.of(ban.source()), ban.expires());
/*     */     }
/*     */     
/*     */     public static UserBanDto from(UserBanListEntry entry) {
/*  41 */       return from(BanlistService.UserBan.from(entry));
/*     */     }
/*     */     
/*     */     private BanlistService.UserBan toUserBan(NameAndId nameAndId) {
/*  45 */       return new BanlistService.UserBan(nameAndId, reason().orElse(null), source().orElse("Management server"), expires());
/*     */     } }
/*     */   private static final class UserBan extends Record { private final NameAndId player; private final String reason; private final String source; private final Optional<Instant> expires;
/*     */     
/*  49 */     private UserBan(NameAndId player, String reason, String source, Optional<Instant> expires) { this.player = player; this.reason = reason; this.source = source; this.expires = expires; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #49	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #49	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #49	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;
/*  49 */       //   0	8	1	o	Ljava/lang/Object; } public NameAndId player() { return this.player; } public String reason() { return this.reason; } public String source() { return this.source; } public Optional<Instant> expires() { return this.expires; }
/*     */      private static UserBan from(UserBanListEntry entry) {
/*  51 */       return new UserBan(java.util.Objects.<NameAndId>requireNonNull((NameAndId)entry.getUser()), entry.getReason(), entry.getSource(), Optional.<Date>ofNullable(entry.getExpires()).map(Date::toInstant));
/*     */     }
/*     */     
/*     */     private UserBanListEntry toBanEntry() {
/*  55 */       return new UserBanListEntry(new NameAndId(player().id(), player().name()), null, source(), expires().<Date>map(Date::from).orElse(null), reason());
/*     */     } }
/*     */ 
/*     */   
/*     */   public static List<UserBanDto> get(MinecraftApi minecraftApi) {
/*  60 */     return minecraftApi.banListService().getUserBanEntries().stream()
/*  61 */       .filter(p -> (p.getUser() != null))
/*  62 */       .map(UserBan::from)
/*  63 */       .map(UserBanDto::from)
/*  64 */       .toList();
/*     */   }
/*     */   
/*     */   public static List<UserBanDto> add(MinecraftApi minecraftApi, List<UserBanDto> bans, ClientInfo clientInfo) {
/*  68 */     List<CompletableFuture<Optional<UserBan>>> fetch = bans.stream()
/*  69 */       .map(ban -> minecraftApi.playerListService().getUser(ban.player().id(), ban.player().name()).thenApply(()))
/*     */       
/*  71 */       .toList();
/*     */     
/*  73 */     for (Optional<UserBan> ban : (Iterable<Optional<UserBan>>)net.minecraft.util.Util.sequence(fetch).join()) {
/*  74 */       if (ban.isEmpty()) {
/*     */         continue;
/*     */       }
/*  77 */       UserBan userBan = ban.get();
/*  78 */       minecraftApi.banListService().addUserBan(userBan.toBanEntry(), clientInfo);
/*  79 */       ServerPlayer player = minecraftApi.playerListService().getPlayer(((UserBan)ban.get()).player().id());
/*  80 */       if (player != null) {
/*  81 */         player.connection.disconnect((Component)Component.translatable("multiplayer.disconnect.banned"));
/*     */       }
/*     */     } 
/*     */     
/*  85 */     return get(minecraftApi);
/*     */   }
/*     */   
/*     */   public static List<UserBanDto> clear(MinecraftApi minecraftApi, ClientInfo clientInfo) {
/*  89 */     minecraftApi.banListService().clearUserBans(clientInfo);
/*  90 */     return get(minecraftApi);
/*     */   }
/*     */   
/*     */   public static List<UserBanDto> remove(MinecraftApi minecraftApi, List<PlayerDto> remove, ClientInfo clientInfo) {
/*  94 */     List<CompletableFuture<Optional<NameAndId>>> fetch = remove.stream()
/*  95 */       .map(playerDto -> minecraftApi.playerListService().getUser(playerDto.id(), playerDto.name()))
/*  96 */       .toList();
/*     */     
/*  98 */     for (Optional<NameAndId> user : (Iterable<Optional<NameAndId>>)net.minecraft.util.Util.sequence(fetch).join()) {
/*  99 */       if (user.isEmpty()) {
/*     */         continue;
/*     */       }
/* 102 */       minecraftApi.banListService().removeUserBan(user.get(), clientInfo);
/*     */     } 
/* 104 */     return get(minecraftApi);
/*     */   }
/*     */   
/*     */   public static List<UserBanDto> set(MinecraftApi minecraftApi, List<UserBanDto> bans, ClientInfo clientInfo) {
/* 108 */     List<CompletableFuture<Optional<UserBan>>> fetch = bans.stream()
/* 109 */       .map(ban -> minecraftApi.playerListService().getUser(ban.player().id(), ban.player().name()).thenApply(()))
/*     */       
/* 111 */       .toList();
/*     */     
/* 113 */     Set<UserBan> finalAllowList = (Set<UserBan>)((List)net.minecraft.util.Util.sequence(fetch).join()).stream()
/* 114 */       .flatMap(Optional::stream)
/* 115 */       .collect(java.util.stream.Collectors.toSet());
/*     */     
/* 117 */     Set<UserBan> currentAllowList = (Set<UserBan>)minecraftApi.banListService().getUserBanEntries().stream()
/* 118 */       .filter(entry -> (entry.getUser() != null))
/* 119 */       .map(UserBan::from)
/* 120 */       .collect(java.util.stream.Collectors.toSet());
/*     */     
/* 122 */     currentAllowList.stream()
/* 123 */       .filter(ban -> !finalAllowList.contains(ban))
/* 124 */       .forEach(ban -> minecraftApi.banListService().removeUserBan(ban.player(), clientInfo));
/*     */     
/* 126 */     finalAllowList.stream()
/* 127 */       .filter(ban -> !currentAllowList.contains(ban))
/* 128 */       .forEach(ban -> {
/*     */           minecraftApi.banListService().addUserBan(ban.toBanEntry(), clientInfo);
/*     */           
/*     */           ServerPlayer player = minecraftApi.playerListService().getPlayer(ban.player().id());
/*     */           if (player != null) {
/*     */             player.connection.disconnect((Component)Component.translatable("multiplayer.disconnect.banned"));
/*     */           }
/*     */         });
/* 136 */     return get(minecraftApi);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/methods/BanlistService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */