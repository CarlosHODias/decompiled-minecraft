/*     */ package com.mojang.realmsclient.dto;
/*     */ 
/*     */ import com.google.common.collect.ComparisonChain;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.annotations.JsonAdapter;
/*     */ import com.google.gson.annotations.SerializedName;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.util.UUIDTypeAdapter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Util;
/*     */ import org.apache.commons.lang3.builder.EqualsBuilder;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RealmsServer
/*     */   extends ValueObject
/*     */   implements ReflectionBasedSerialization {
/*  28 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int NO_VALUE = -1;
/*  31 */   public static final Component WORLD_CLOSED_COMPONENT = (Component)Component.translatable("mco.play.button.realm.closed");
/*     */   @SerializedName("id")
/*  33 */   public long id = -1L;
/*     */   
/*     */   @SerializedName("remoteSubscriptionId")
/*     */   public String remoteSubscriptionId;
/*     */   
/*     */   @SerializedName("name")
/*     */   public String name;
/*     */   
/*     */   @SerializedName("motd")
/*  42 */   public String motd = "";
/*     */   
/*     */   @SerializedName("state")
/*  45 */   public State state = State.CLOSED;
/*     */   
/*     */   @SerializedName("owner")
/*     */   public String owner;
/*     */   @SerializedName("ownerUUID")
/*     */   @JsonAdapter(UUIDTypeAdapter.class)
/*  51 */   public UUID ownerUUID = Util.NIL_UUID;
/*     */ 
/*     */ 
/*     */   
/*     */   @SerializedName("players")
/*  56 */   public List<PlayerInfo> players = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */   
/*     */   @SerializedName("slots")
/*  61 */   private List<RealmsSlot> slotList = createEmptySlots();
/*     */   @Exclude
/*  63 */   public Map<Integer, RealmsSlot> slots = new HashMap<>();
/*     */   
/*     */   @SerializedName("expired")
/*     */   public boolean expired;
/*     */   
/*     */   @SerializedName("expiredTrial")
/*     */   public boolean expiredTrial = false;
/*     */   
/*     */   @SerializedName("daysLeft")
/*     */   public int daysLeft;
/*     */   
/*     */   @SerializedName("worldType")
/*  75 */   public WorldType worldType = WorldType.NORMAL;
/*     */   
/*     */   @SerializedName("isHardcore")
/*     */   public boolean isHardcore = false;
/*     */   
/*     */   @SerializedName("gameMode")
/*  81 */   public int gameMode = -1;
/*     */   
/*     */   @SerializedName("activeSlot")
/*  84 */   public int activeSlot = -1;
/*     */   
/*     */   @SerializedName("minigameName")
/*     */   public String minigameName;
/*     */   
/*     */   @SerializedName("minigameId")
/*  90 */   public int minigameId = -1;
/*     */   
/*     */   @SerializedName("minigameImage")
/*     */   public String minigameImage;
/*     */   
/*     */   @SerializedName("parentWorldId")
/*  96 */   public long parentRealmId = -1L;
/*     */   
/*     */   @SerializedName("parentWorldName")
/*     */   public String parentWorldName;
/*     */   
/*     */   @SerializedName("activeVersion")
/* 102 */   public String activeVersion = "";
/*     */   
/*     */   @SerializedName("compatibility")
/* 105 */   public Compatibility compatibility = Compatibility.UNVERIFIABLE;
/*     */ 
/*     */   
/*     */   @SerializedName("regionSelectionPreference")
/*     */   public RegionSelectionPreferenceDto regionSelectionPreference;
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 114 */     return this.motd;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 118 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getMinigameName() {
/* 122 */     return this.minigameName;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 126 */     this.name = name;
/*     */   }
/*     */   
/*     */   public void setDescription(String motd) {
/* 130 */     this.motd = motd;
/*     */   }
/*     */   
/*     */   public static RealmsServer parse(GuardedSerializer gson, String json) {
/*     */     try {
/* 135 */       RealmsServer server = gson.<RealmsServer>fromJson(json, RealmsServer.class);
/* 136 */       if (server == null) {
/* 137 */         LOGGER.error("Could not parse McoServer: {}", json);
/* 138 */         return new RealmsServer();
/*     */       } 
/*     */       
/* 141 */       finalize(server);
/* 142 */       return server;
/* 143 */     } catch (Exception e) {
/* 144 */       LOGGER.error("Could not parse McoServer", e);
/* 145 */       return new RealmsServer();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void finalize(RealmsServer server) {
/* 150 */     if (server.players == null) {
/* 151 */       server.players = Lists.newArrayList();
/*     */     }
/* 153 */     if (server.slotList == null) {
/* 154 */       server.slotList = createEmptySlots();
/*     */     }
/* 156 */     if (server.slots == null) {
/* 157 */       server.slots = new HashMap<>();
/*     */     }
/* 159 */     if (server.worldType == null) {
/* 160 */       server.worldType = WorldType.NORMAL;
/*     */     }
/* 162 */     if (server.activeVersion == null) {
/* 163 */       server.activeVersion = "";
/*     */     }
/* 165 */     if (server.compatibility == null) {
/* 166 */       server.compatibility = Compatibility.UNVERIFIABLE;
/*     */     }
/* 168 */     if (server.regionSelectionPreference == null) {
/* 169 */       server.regionSelectionPreference = RegionSelectionPreferenceDto.DEFAULT;
/*     */     }
/*     */     
/* 172 */     sortInvited(server);
/* 173 */     finalizeSlots(server);
/*     */   }
/*     */   
/*     */   private static void sortInvited(RealmsServer server) {
/* 177 */     server.players.sort((o1, o2) -> ComparisonChain.start().compareFalseFirst(o2.accepted, o1.accepted).compare(o1.name.toLowerCase(Locale.ROOT), o2.name.toLowerCase(Locale.ROOT)).result());
/*     */   }
/*     */   
/*     */   private static void finalizeSlots(RealmsServer server) {
/* 181 */     server.slotList.forEach(s -> server.slots.put(s.slotId, s));
/*     */     
/* 183 */     for (int i = 1; i <= 3; i++) {
/* 184 */       if (!server.slots.containsKey(i)) {
/* 185 */         server.slots.put(i, RealmsSlot.defaults(i));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static List<RealmsSlot> createEmptySlots() {
/* 191 */     List<RealmsSlot> slots = new ArrayList<>();
/* 192 */     slots.add(RealmsSlot.defaults(1));
/* 193 */     slots.add(RealmsSlot.defaults(2));
/* 194 */     slots.add(RealmsSlot.defaults(3));
/* 195 */     return slots;
/*     */   }
/*     */   
/*     */   public boolean isCompatible() {
/* 199 */     return this.compatibility.isCompatible();
/*     */   }
/*     */   
/*     */   public boolean needsUpgrade() {
/* 203 */     return this.compatibility.needsUpgrade();
/*     */   }
/*     */   
/*     */   public boolean needsDowngrade() {
/* 207 */     return this.compatibility.needsDowngrade();
/*     */   }
/*     */   
/*     */   public boolean shouldPlayButtonBeActive() {
/* 211 */     boolean active = (!this.expired && this.state == State.OPEN);
/* 212 */     return (active && (isCompatible() || needsUpgrade() || isSelfOwnedServer()));
/*     */   }
/*     */   
/*     */   private boolean isSelfOwnedServer() {
/* 216 */     return Minecraft.getInstance().isLocalPlayer(this.ownerUUID);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 221 */     return Objects.hash(new Object[] { this.id, this.name, this.motd, this.state, this.owner, this.expired });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 226 */     if (obj == null) {
/* 227 */       return false;
/*     */     }
/* 229 */     if (obj == this) {
/* 230 */       return true;
/*     */     }
/* 232 */     if (obj.getClass() != getClass()) {
/* 233 */       return false;
/*     */     }
/* 235 */     RealmsServer rhs = (RealmsServer)obj;
/*     */     
/* 237 */     return new EqualsBuilder()
/* 238 */       .append(this.id, rhs.id)
/* 239 */       .append(this.name, rhs.name)
/* 240 */       .append(this.motd, rhs.motd)
/* 241 */       .append(this.state, rhs.state)
/* 242 */       .append(this.owner, rhs.owner)
/* 243 */       .append(this.expired, rhs.expired)
/* 244 */       .append(this.worldType, this.worldType).isEquals();
/*     */   }
/*     */   
/*     */   public RealmsServer copy() {
/* 248 */     RealmsServer server = new RealmsServer();
/* 249 */     server.id = this.id;
/* 250 */     server.remoteSubscriptionId = this.remoteSubscriptionId;
/* 251 */     server.name = this.name;
/* 252 */     server.motd = this.motd;
/* 253 */     server.state = this.state;
/* 254 */     server.owner = this.owner;
/* 255 */     server.players = this.players;
/* 256 */     server.slotList = this.slotList.stream().map(RealmsSlot::copy).toList();
/* 257 */     server.slots = cloneSlots(this.slots);
/* 258 */     server.expired = this.expired;
/* 259 */     server.expiredTrial = this.expiredTrial;
/* 260 */     server.daysLeft = this.daysLeft;
/* 261 */     server.worldType = this.worldType;
/* 262 */     server.isHardcore = this.isHardcore;
/* 263 */     server.gameMode = this.gameMode;
/* 264 */     server.ownerUUID = this.ownerUUID;
/* 265 */     server.minigameName = this.minigameName;
/* 266 */     server.activeSlot = this.activeSlot;
/* 267 */     server.minigameId = this.minigameId;
/* 268 */     server.minigameImage = this.minigameImage;
/* 269 */     server.parentWorldName = this.parentWorldName;
/* 270 */     server.parentRealmId = this.parentRealmId;
/* 271 */     server.activeVersion = this.activeVersion;
/* 272 */     server.compatibility = this.compatibility;
/* 273 */     server.regionSelectionPreference = (this.regionSelectionPreference != null) ? this.regionSelectionPreference.copy() : null;
/* 274 */     return server;
/*     */   }
/*     */   
/*     */   public Map<Integer, RealmsSlot> cloneSlots(Map<Integer, RealmsSlot> slots) {
/* 278 */     Map<Integer, RealmsSlot> newSlots = Maps.newHashMap();
/*     */     
/* 280 */     for (Map.Entry<Integer, RealmsSlot> entry : slots.entrySet()) {
/* 281 */       newSlots.put(entry.getKey(), new RealmsSlot((Integer)entry.getKey(), ((RealmsSlot)entry.getValue()).options.copy(), ((RealmsSlot)entry.getValue()).settings));
/*     */     }
/*     */     
/* 284 */     return newSlots;
/*     */   }
/*     */   
/*     */   public boolean isSnapshotRealm() {
/* 288 */     return (this.parentRealmId != -1L);
/*     */   }
/*     */   
/*     */   public boolean isMinigameActive() {
/* 292 */     return (this.worldType == WorldType.MINIGAME);
/*     */   }
/*     */   
/*     */   public String getWorldName(int slotId) {
/* 296 */     if (this.name == null) {
/* 297 */       return ((RealmsSlot)this.slots.get(slotId)).options.getSlotName(slotId);
/*     */     }
/* 299 */     return this.name + " (" + this.name + ")";
/*     */   }
/*     */   
/*     */   public ServerData toServerData(String ip) {
/* 303 */     return new ServerData(Objects.<String>requireNonNullElse(this.name, "unknown server"), ip, ServerData.Type.REALM);
/*     */   }
/*     */   
/*     */   public static class McoServerComparator implements Comparator<RealmsServer> {
/*     */     private final String refOwner;
/*     */     
/*     */     public McoServerComparator(String owner) {
/* 310 */       this.refOwner = owner;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compare(RealmsServer server1, RealmsServer server2) {
/* 315 */       return ComparisonChain.start()
/* 316 */         .compareTrueFirst(server1.isSnapshotRealm(), server2.isSnapshotRealm())
/* 317 */         .compareTrueFirst((server1.state == RealmsServer.State.UNINITIALIZED), (server2.state == RealmsServer.State.UNINITIALIZED))
/* 318 */         .compareTrueFirst(server1.expiredTrial, server2.expiredTrial)
/* 319 */         .compareTrueFirst(Objects.equals(server1.owner, this.refOwner), Objects.equals(server2.owner, this.refOwner))
/* 320 */         .compareFalseFirst(server1.expired, server2.expired)
/* 321 */         .compareTrueFirst((server1.state == RealmsServer.State.OPEN), (server2.state == RealmsServer.State.OPEN))
/* 322 */         .compare(server1.id, server2.id).result();
/*     */     }
/*     */   }
/*     */   
/*     */   public enum State {
/* 327 */     CLOSED,
/* 328 */     OPEN,
/* 329 */     UNINITIALIZED;
/*     */   }
/*     */   
/*     */   public enum WorldType {
/* 333 */     NORMAL("normal"),
/* 334 */     MINIGAME("minigame"),
/* 335 */     ADVENTUREMAP("adventureMap"),
/* 336 */     EXPERIENCE("experience"),
/* 337 */     INSPIRATION("inspiration"),
/* 338 */     UNKNOWN("unknown");
/*     */     private static final String TRANSLATION_PREFIX = "mco.backup.entry.worldType.";
/*     */     private final Component displayName;
/*     */     
/*     */     WorldType(String translationKey) {
/* 343 */       this.displayName = (Component)Component.translatable("mco.backup.entry.worldType." + translationKey);
/*     */     }
/*     */     
/*     */     public Component getDisplayName() {
/* 347 */       return this.displayName;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Compatibility {
/* 352 */     UNVERIFIABLE,
/*     */     
/* 354 */     INCOMPATIBLE,
/* 355 */     RELEASE_TYPE_INCOMPATIBLE,
/* 356 */     NEEDS_DOWNGRADE,
/* 357 */     NEEDS_UPGRADE,
/* 358 */     COMPATIBLE;
/*     */     
/*     */     public boolean isCompatible() {
/* 361 */       return (this == COMPATIBLE);
/*     */     }
/*     */     
/*     */     public boolean needsUpgrade() {
/* 365 */       return (this == NEEDS_UPGRADE);
/*     */     }
/*     */     
/*     */     public boolean needsDowngrade() {
/* 369 */       return (this == NEEDS_DOWNGRADE);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/RealmsServer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */