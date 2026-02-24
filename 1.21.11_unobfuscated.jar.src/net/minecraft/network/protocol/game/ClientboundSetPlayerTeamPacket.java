/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.Team;
/*     */ 
/*     */ public class ClientboundSetPlayerTeamPacket implements Packet<ClientGamePacketListener> {
/*  20 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetPlayerTeamPacket> STREAM_CODEC = Packet.codec(ClientboundSetPlayerTeamPacket::write, ClientboundSetPlayerTeamPacket::new);
/*     */   
/*     */   private static final int METHOD_ADD = 0;
/*     */   
/*     */   private static final int METHOD_REMOVE = 1;
/*     */   
/*     */   private static final int METHOD_CHANGE = 2;
/*     */   private static final int METHOD_JOIN = 3;
/*     */   private static final int METHOD_LEAVE = 4;
/*     */   private static final int MAX_VISIBILITY_LENGTH = 40;
/*     */   private static final int MAX_COLLISION_LENGTH = 40;
/*     */   private final int method;
/*     */   private final String name;
/*     */   private final Collection<String> players;
/*     */   private final Optional<Parameters> parameters;
/*     */   
/*     */   private ClientboundSetPlayerTeamPacket(String name, int method, Optional<Parameters> parameters, Collection<String> players) {
/*  37 */     this.name = name;
/*  38 */     this.method = method;
/*  39 */     this.parameters = parameters;
/*  40 */     this.players = (Collection<String>)ImmutableList.copyOf(players);
/*     */   }
/*     */   
/*     */   public static ClientboundSetPlayerTeamPacket createAddOrModifyPacket(PlayerTeam team, boolean createNew) {
/*  44 */     return new ClientboundSetPlayerTeamPacket(
/*  45 */         team.getName(), 
/*  46 */         createNew ? 0 : 2, 
/*  47 */         Optional.of(new Parameters(team)), 
/*  48 */         createNew ? team.getPlayers() : (Collection<String>)ImmutableList.of());
/*     */   }
/*     */ 
/*     */   
/*     */   public static ClientboundSetPlayerTeamPacket createRemovePacket(PlayerTeam team) {
/*  53 */     return new ClientboundSetPlayerTeamPacket(
/*  54 */         team.getName(), 1, 
/*     */         
/*  56 */         Optional.empty(), 
/*  57 */         (Collection<String>)ImmutableList.of());
/*     */   }
/*     */ 
/*     */   
/*     */   public static ClientboundSetPlayerTeamPacket createPlayerPacket(PlayerTeam team, String player, Action action) {
/*  62 */     return new ClientboundSetPlayerTeamPacket(
/*  63 */         team.getName(), 
/*  64 */         (action == Action.ADD) ? 3 : 4, 
/*  65 */         Optional.empty(), 
/*  66 */         (Collection<String>)ImmutableList.of(player));
/*     */   }
/*     */ 
/*     */   
/*     */   private ClientboundSetPlayerTeamPacket(RegistryFriendlyByteBuf input) {
/*  71 */     this.name = input.readUtf();
/*  72 */     this.method = input.readByte();
/*     */     
/*  74 */     if (shouldHaveParameters(this.method)) {
/*  75 */       this.parameters = Optional.of(new Parameters(input));
/*     */     } else {
/*  77 */       this.parameters = Optional.empty();
/*     */     } 
/*     */     
/*  80 */     if (shouldHavePlayerList(this.method)) {
/*  81 */       this.players = input.readList(FriendlyByteBuf::readUtf);
/*     */     } else {
/*  83 */       this.players = (Collection<String>)ImmutableList.of();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void write(RegistryFriendlyByteBuf output) {
/*  88 */     output.writeUtf(this.name);
/*  89 */     output.writeByte(this.method);
/*     */     
/*  91 */     if (shouldHaveParameters(this.method)) {
/*  92 */       ((Parameters)this.parameters.orElseThrow(() -> new IllegalStateException("Parameters not present, but method is" + this.method))).write(output);
/*     */     }
/*     */     
/*  95 */     if (shouldHavePlayerList(this.method)) {
/*  96 */       output.writeCollection(this.players, FriendlyByteBuf::writeUtf);
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean shouldHavePlayerList(int method) {
/* 101 */     return (method == 0 || method == 3 || method == 4);
/*     */   }
/*     */   
/*     */   private static boolean shouldHaveParameters(int method) {
/* 105 */     return (method == 0 || method == 2);
/*     */   }
/*     */   
/*     */   public Action getPlayerAction() {
/* 109 */     switch (this.method) { case 0: case 3: case 4: default: break; }  return 
/*     */ 
/*     */       
/* 112 */       null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Action getTeamAction() {
/* 117 */     switch (this.method) { case 0: case 1: default: break; }  return 
/*     */ 
/*     */       
/* 120 */       null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PacketType<ClientboundSetPlayerTeamPacket> type() {
/* 126 */     return GamePacketTypes.CLIENTBOUND_SET_PLAYER_TEAM;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(ClientGamePacketListener listener) {
/* 131 */     listener.handleSetPlayerTeamPacket(this);
/*     */   }
/*     */   
/*     */   public String getName() {
/* 135 */     return this.name;
/*     */   }
/*     */   
/*     */   public Collection<String> getPlayers() {
/* 139 */     return this.players;
/*     */   }
/*     */   
/*     */   public Optional<Parameters> getParameters() {
/* 143 */     return this.parameters;
/*     */   }
/*     */   
/*     */   public enum Action {
/* 147 */     ADD,
/* 148 */     REMOVE;
/*     */   }
/*     */   
/*     */   public static class Parameters
/*     */   {
/*     */     private final Component displayName;
/*     */     private final Component playerPrefix;
/*     */     private final Component playerSuffix;
/*     */     private final Team.Visibility nametagVisibility;
/*     */     private final Team.CollisionRule collisionRule;
/*     */     private final ChatFormatting color;
/*     */     private final int options;
/*     */     
/*     */     public Parameters(PlayerTeam team) {
/* 162 */       this.displayName = team.getDisplayName();
/* 163 */       this.options = team.packOptions();
/* 164 */       this.nametagVisibility = team.getNameTagVisibility();
/* 165 */       this.collisionRule = team.getCollisionRule();
/* 166 */       this.color = team.getColor();
/* 167 */       this.playerPrefix = team.getPlayerPrefix();
/* 168 */       this.playerSuffix = team.getPlayerSuffix();
/*     */     }
/*     */     
/*     */     public Parameters(RegistryFriendlyByteBuf input) {
/* 172 */       this.displayName = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(input);
/* 173 */       this.options = input.readByte();
/* 174 */       this.nametagVisibility = (Team.Visibility)Team.Visibility.STREAM_CODEC.decode(input);
/* 175 */       this.collisionRule = (Team.CollisionRule)Team.CollisionRule.STREAM_CODEC.decode(input);
/* 176 */       this.color = (ChatFormatting)input.readEnum(ChatFormatting.class);
/* 177 */       this.playerPrefix = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(input);
/* 178 */       this.playerSuffix = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(input);
/*     */     }
/*     */     
/*     */     public Component getDisplayName() {
/* 182 */       return this.displayName;
/*     */     }
/*     */     
/*     */     public int getOptions() {
/* 186 */       return this.options;
/*     */     }
/*     */     
/*     */     public ChatFormatting getColor() {
/* 190 */       return this.color;
/*     */     }
/*     */     
/*     */     public Team.Visibility getNametagVisibility() {
/* 194 */       return this.nametagVisibility;
/*     */     }
/*     */     
/*     */     public Team.CollisionRule getCollisionRule() {
/* 198 */       return this.collisionRule;
/*     */     }
/*     */     
/*     */     public Component getPlayerPrefix() {
/* 202 */       return this.playerPrefix;
/*     */     }
/*     */     
/*     */     public Component getPlayerSuffix() {
/* 206 */       return this.playerSuffix;
/*     */     }
/*     */     
/*     */     public void write(RegistryFriendlyByteBuf output) {
/* 210 */       ComponentSerialization.TRUSTED_STREAM_CODEC.encode(output, this.displayName);
/* 211 */       output.writeByte(this.options);
/* 212 */       Team.Visibility.STREAM_CODEC.encode(output, this.nametagVisibility);
/* 213 */       Team.CollisionRule.STREAM_CODEC.encode(output, this.collisionRule);
/* 214 */       output.writeEnum((Enum)this.color);
/* 215 */       ComponentSerialization.TRUSTED_STREAM_CODEC.encode(output, this.playerPrefix);
/* 216 */       ComponentSerialization.TRUSTED_STREAM_CODEC.encode(output, this.playerSuffix);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */