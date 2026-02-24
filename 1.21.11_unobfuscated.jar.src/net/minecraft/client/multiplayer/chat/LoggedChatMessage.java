/*     */ package net.minecraft.client.multiplayer.chat;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.time.Instant;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.PlayerChatMessage;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public interface LoggedChatMessage extends LoggedChatEvent {
/*     */   static Player player(GameProfile profile, PlayerChatMessage message, ChatTrustLevel trustLevel) {
/*  22 */     return new Player(profile, message, trustLevel);
/*     */   }
/*     */   
/*     */   static System system(Component message, Instant timeStamp) {
/*  26 */     return new System(message, timeStamp);
/*     */   }
/*     */   
/*     */   Component toContentComponent();
/*     */   
/*     */   default Component toNarrationComponent() {
/*  32 */     return toContentComponent();
/*     */   }
/*     */   boolean canReport(UUID paramUUID);
/*     */   public static final class Player extends Record implements LoggedChatMessage { private final GameProfile profile; private final PlayerChatMessage message; private final ChatTrustLevel trustLevel; public static final MapCodec<Player> CODEC;
/*     */     
/*  37 */     public Player(GameProfile profile, PlayerChatMessage message, ChatTrustLevel trustLevel) { this.profile = profile; this.message = message; this.trustLevel = trustLevel; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$Player;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #37	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  37 */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$Player; } public GameProfile profile() { return this.profile; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$Player;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #37	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$Player; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$Player;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #37	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$Player;
/*  37 */       //   0	8	1	o	Ljava/lang/Object; } public PlayerChatMessage message() { return this.message; } public ChatTrustLevel trustLevel() { return this.trustLevel; } static {
/*  38 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.AUTHLIB_GAME_PROFILE.fieldOf("profile").forGetter(Player::profile), (App)PlayerChatMessage.MAP_CODEC.forGetter(Player::message), (App)ChatTrustLevel.CODEC.optionalFieldOf("trust_level", ChatTrustLevel.SECURE).forGetter(Player::trustLevel)).apply((Applicative)i, Player::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  44 */     private static final DateTimeFormatter TIME_FORMATTER = Util.localizedDateFormatter(java.time.format.FormatStyle.SHORT);
/*     */ 
/*     */     
/*     */     public Component toContentComponent() {
/*  48 */       if (!this.message.filterMask().isEmpty()) {
/*  49 */         Component filtered = this.message.filterMask().applyWithFormatting(this.message.signedContent());
/*  50 */         return (filtered != null) ? filtered : (Component)Component.empty();
/*     */       } 
/*  52 */       return this.message.decoratedContent();
/*     */     }
/*     */ 
/*     */     
/*     */     public Component toNarrationComponent() {
/*  57 */       Component content = toContentComponent();
/*  58 */       Component time = getTimeComponent();
/*  59 */       return (Component)Component.translatable("gui.chatSelection.message.narrate", new Object[] { this.profile.name(), content, time });
/*     */     }
/*     */     
/*     */     public Component toHeadingComponent() {
/*  63 */       Component time = getTimeComponent();
/*  64 */       return (Component)Component.translatable("gui.chatSelection.heading", new Object[] { this.profile.name(), time });
/*     */     }
/*     */     
/*     */     private Component getTimeComponent() {
/*  68 */       ZonedDateTime dateTime = ZonedDateTime.ofInstant(this.message.timeStamp(), java.time.ZoneId.systemDefault());
/*  69 */       return (Component)Component.literal(dateTime.format(TIME_FORMATTER)).withStyle(new ChatFormatting[] { ChatFormatting.ITALIC, ChatFormatting.GRAY });
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canReport(UUID reportedPlayerId) {
/*  74 */       return this.message.hasSignatureFrom(reportedPlayerId);
/*     */     }
/*     */     
/*     */     public UUID profileId() {
/*  78 */       return this.profile.id();
/*     */     }
/*     */ 
/*     */     
/*     */     public LoggedChatEvent.Type type() {
/*  83 */       return LoggedChatEvent.Type.PLAYER;
/*     */     } }
/*     */   public static final class System extends Record implements LoggedChatMessage { private final Component message; private final Instant timeStamp; public static final MapCodec<System> CODEC;
/*     */     
/*  87 */     public System(Component message, Instant timeStamp) { this.message = message; this.timeStamp = timeStamp; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$System;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$System; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$System;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$System; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$System;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$System;
/*  87 */       //   0	8	1	o	Ljava/lang/Object; } public Component message() { return this.message; } public Instant timeStamp() { return this.timeStamp; } static {
/*  88 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ComponentSerialization.CODEC.fieldOf("message").forGetter(System::message), (App)ExtraCodecs.INSTANT_ISO8601.fieldOf("time_stamp").forGetter(System::timeStamp)).apply((Applicative)i, System::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Component toContentComponent() {
/*  95 */       return this.message;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canReport(UUID reportedPlayerId) {
/* 100 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public LoggedChatEvent.Type type() {
/* 105 */       return LoggedChatEvent.Type.SYSTEM;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/chat/LoggedChatMessage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */