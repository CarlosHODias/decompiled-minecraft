/*     */ package net.minecraft.network.protocol.game;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.RemoteChatSession;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.network.codec.StreamEncoder;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.player.PlayerModelPart;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ public class ClientboundPlayerInfoUpdatePacket implements Packet<ClientGamePacketListener> {
/*  28 */   public static final net.minecraft.network.codec.StreamCodec<RegistryFriendlyByteBuf, ClientboundPlayerInfoUpdatePacket> STREAM_CODEC = Packet.codec(ClientboundPlayerInfoUpdatePacket::write, ClientboundPlayerInfoUpdatePacket::new);
/*     */   
/*     */   private final EnumSet<Action> actions;
/*     */   private final List<Entry> entries;
/*     */   
/*     */   public ClientboundPlayerInfoUpdatePacket(EnumSet<Action> actions, Collection<ServerPlayer> players) {
/*  34 */     this.actions = actions;
/*  35 */     this.entries = players.stream().map(Entry::new).toList();
/*     */   }
/*     */   
/*     */   public ClientboundPlayerInfoUpdatePacket(Action action, ServerPlayer player) {
/*  39 */     this.actions = EnumSet.of(action);
/*  40 */     this.entries = List.of(new Entry(player));
/*     */   }
/*     */   
/*     */   public static ClientboundPlayerInfoUpdatePacket createPlayerInitializing(Collection<ServerPlayer> players) {
/*  44 */     EnumSet<Action> actions = EnumSet.of(Action.ADD_PLAYER, new Action[] { Action.INITIALIZE_CHAT, Action.UPDATE_GAME_MODE, Action.UPDATE_LISTED, Action.UPDATE_LATENCY, Action.UPDATE_DISPLAY_NAME, Action.UPDATE_HAT, Action.UPDATE_LIST_ORDER });
/*  45 */     return new ClientboundPlayerInfoUpdatePacket(actions, players);
/*     */   }
/*     */   
/*     */   private ClientboundPlayerInfoUpdatePacket(RegistryFriendlyByteBuf input) {
/*  49 */     this.actions = input.readEnumSet(Action.class);
/*  50 */     this.entries = input.readList(buf -> {
/*     */           EntryBuilder builder = new EntryBuilder(buf.readUUID());
/*     */           for (Action action : this.actions) {
/*     */             action.reader.read(builder, (RegistryFriendlyByteBuf)buf);
/*     */           }
/*     */           return builder.build();
/*     */         });
/*     */   }
/*     */   
/*     */   private void write(RegistryFriendlyByteBuf output) {
/*  60 */     output.writeEnumSet(this.actions, Action.class);
/*  61 */     output.writeCollection(this.entries, (buf, entry) -> {
/*     */           buf.writeUUID(entry.profileId());
/*     */           for (Action action : this.actions) {
/*     */             action.writer.write((RegistryFriendlyByteBuf)buf, entry);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public PacketType<ClientboundPlayerInfoUpdatePacket> type() {
/*  71 */     return GamePacketTypes.CLIENTBOUND_PLAYER_INFO_UPDATE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(ClientGamePacketListener listener) {
/*  76 */     listener.handlePlayerInfoUpdate(this);
/*     */   }
/*     */   
/*     */   public EnumSet<Action> actions() {
/*  80 */     return this.actions;
/*     */   }
/*     */   
/*     */   public List<Entry> entries() {
/*  84 */     return this.entries;
/*     */   }
/*     */   
/*     */   public List<Entry> newEntries() {
/*  88 */     return this.actions.contains(Action.ADD_PLAYER) ? this.entries : List.<Entry>of();
/*     */   }
/*     */   public enum Action { ADD_PLAYER, INITIALIZE_CHAT, UPDATE_GAME_MODE, UPDATE_LISTED, UPDATE_LATENCY, UPDATE_DISPLAY_NAME, UPDATE_LIST_ORDER, UPDATE_HAT; private final Reader reader; private final Writer writer;
/*     */     static {
/*  92 */       ADD_PLAYER = new Action("ADD_PLAYER", 0, (entry, input) -> {
/*     */             String name = (String)ByteBufCodecs.PLAYER_NAME.decode(input);
/*     */             
/*     */             PropertyMap properties = (PropertyMap)ByteBufCodecs.GAME_PROFILE_PROPERTIES.decode(input);
/*     */             
/*     */             entry.profile = new GameProfile(entry.profileId, name, properties);
/*     */           }, (output, entry) -> {
/*     */             GameProfile profile = Objects.<GameProfile>requireNonNull(entry.profile());
/*     */             
/*     */             ByteBufCodecs.PLAYER_NAME.encode(output, profile.name());
/*     */             ByteBufCodecs.GAME_PROFILE_PROPERTIES.encode(output, profile.properties());
/*     */           });
/* 104 */       INITIALIZE_CHAT = new Action("INITIALIZE_CHAT", 1, (entry, input) -> entry.chatSession = (RemoteChatSession.Data)input.readNullable(RemoteChatSession.Data::read), (output, entry) -> output.writeNullable(entry.chatSession, RemoteChatSession.Data::write));
/*     */ 
/*     */ 
/*     */       
/* 108 */       UPDATE_GAME_MODE = new Action("UPDATE_GAME_MODE", 2, (entry, input) -> entry.gameMode = GameType.byId(input.readVarInt()), (output, entry) -> output.writeVarInt(entry.gameMode().getId()));
/*     */ 
/*     */ 
/*     */       
/* 112 */       UPDATE_LISTED = new Action("UPDATE_LISTED", 3, (entry, input) -> entry.listed = input.readBoolean(), (output, entry) -> output.writeBoolean(entry.listed()));
/*     */ 
/*     */ 
/*     */       
/* 116 */       UPDATE_LATENCY = new Action("UPDATE_LATENCY", 4, (entry, input) -> entry.latency = input.readVarInt(), (output, entry) -> output.writeVarInt(entry.latency()));
/*     */ 
/*     */ 
/*     */       
/* 120 */       UPDATE_DISPLAY_NAME = new Action("UPDATE_DISPLAY_NAME", 5, (entry, input) -> entry.displayName = (Component)FriendlyByteBuf.readNullable((ByteBuf)input, (StreamDecoder)ComponentSerialization.TRUSTED_STREAM_CODEC), (output, entry) -> FriendlyByteBuf.writeNullable((ByteBuf)output, entry.displayName(), (StreamEncoder)ComponentSerialization.TRUSTED_STREAM_CODEC));
/*     */ 
/*     */ 
/*     */       
/* 124 */       UPDATE_LIST_ORDER = new Action("UPDATE_LIST_ORDER", 6, (entry, input) -> entry.listOrder = input.readVarInt(), (output, entry) -> output.writeVarInt(entry.listOrder));
/*     */ 
/*     */ 
/*     */       
/* 128 */       UPDATE_HAT = new Action("UPDATE_HAT", 7, (entry, input) -> entry.showHat = input.readBoolean(), (output, entry) -> output.writeBoolean(entry.showHat));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Action(Reader reader, Writer writer) {
/* 138 */       this.reader = reader;
/* 139 */       this.writer = writer;
/*     */     }
/*     */     
/*     */     public static interface Reader
/*     */     {
/*     */       void read(ClientboundPlayerInfoUpdatePacket.EntryBuilder param2EntryBuilder, RegistryFriendlyByteBuf param2RegistryFriendlyByteBuf);
/*     */     }
/*     */     
/*     */     public static interface Writer {
/*     */       void write(RegistryFriendlyByteBuf param2RegistryFriendlyByteBuf, ClientboundPlayerInfoUpdatePacket.Entry param2Entry);
/*     */     } }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 153 */     return MoreObjects.toStringHelper(this)
/* 154 */       .add("actions", this.actions)
/* 155 */       .add("entries", this.entries)
/* 156 */       .toString();
/*     */   }
/*     */   public static final class Entry extends Record { private final UUID profileId; private final GameProfile profile; private final boolean listed; private final int latency; private final GameType gameMode; private final Component displayName; private final boolean showHat; private final int listOrder; private final RemoteChatSession.Data chatSession;
/* 159 */     public RemoteChatSession.Data chatSession() { return this.chatSession; } public int listOrder() { return this.listOrder; } public boolean showHat() { return this.showHat; } public Component displayName() { return this.displayName; } public GameType gameMode() { return this.gameMode; } public int latency() { return this.latency; } public boolean listed() { return this.listed; } public GameProfile profile() { return this.profile; } public UUID profileId() { return this.profileId; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoUpdatePacket$Entry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #159	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoUpdatePacket$Entry;
/* 159 */       //   0	8	1	o	Ljava/lang/Object; } public Entry(UUID profileId, GameProfile profile, boolean listed, int latency, GameType gameMode, Component displayName, boolean showHat, int listOrder, RemoteChatSession.Data chatSession) { this.profileId = profileId; this.profile = profile; this.listed = listed; this.latency = latency; this.gameMode = gameMode; this.displayName = displayName; this.showHat = showHat; this.listOrder = listOrder; this.chatSession = chatSession; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoUpdatePacket$Entry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #159	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoUpdatePacket$Entry; }
/*     */     public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoUpdatePacket$Entry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #159	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 161 */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoUpdatePacket$Entry; } private Entry(ServerPlayer player) { this(
/* 162 */           player.getUUID(), 
/* 163 */           player.getGameProfile(), true, 
/*     */           
/* 165 */           player.connection.latency(), 
/* 166 */           player.gameMode(), 
/* 167 */           player.getTabListDisplayName(), 
/* 168 */           player.isModelPartShown(PlayerModelPart.HAT), 
/* 169 */           player.getTabListOrder(), 
/* 170 */           (RemoteChatSession.Data)net.minecraft.Optionull.map(player.getChatSession(), RemoteChatSession::asData)); }
/*     */      }
/*     */ 
/*     */   
/*     */   private static class EntryBuilder
/*     */   {
/*     */     private final UUID profileId;
/*     */     private GameProfile profile;
/*     */     private boolean listed;
/*     */     private int latency;
/* 180 */     private GameType gameMode = GameType.DEFAULT_MODE;
/*     */     private Component displayName;
/*     */     private boolean showHat;
/*     */     private int listOrder;
/*     */     private RemoteChatSession.Data chatSession;
/*     */     
/*     */     private EntryBuilder(UUID profileId) {
/* 187 */       this.profileId = profileId;
/*     */     }
/*     */     
/*     */     private ClientboundPlayerInfoUpdatePacket.Entry build() {
/* 191 */       return new ClientboundPlayerInfoUpdatePacket.Entry(this.profileId, this.profile, this.listed, this.latency, this.gameMode, this.displayName, this.showHat, this.listOrder, this.chatSession);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Writer {
/*     */     void write(RegistryFriendlyByteBuf param1RegistryFriendlyByteBuf, ClientboundPlayerInfoUpdatePacket.Entry param1Entry);
/*     */   }
/*     */   
/*     */   public static interface Reader {
/*     */     void read(ClientboundPlayerInfoUpdatePacket.EntryBuilder param1EntryBuilder, RegistryFriendlyByteBuf param1RegistryFriendlyByteBuf);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundPlayerInfoUpdatePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */