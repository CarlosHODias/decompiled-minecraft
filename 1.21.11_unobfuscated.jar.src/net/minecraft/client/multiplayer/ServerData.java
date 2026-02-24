/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.status.ServerStatus;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.PngInfo;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class ServerData
/*     */ {
/*  21 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int MAX_ICON_SIZE = 1024;
/*     */   public String name;
/*     */   public String ip;
/*     */   public Component status;
/*     */   public Component motd;
/*     */   public ServerStatus.Players players;
/*     */   public long ping;
/*  30 */   public int protocol = SharedConstants.getCurrentVersion().protocolVersion();
/*  31 */   public Component version = (Component)Component.literal(SharedConstants.getCurrentVersion().name());
/*  32 */   public List<Component> playerList = Collections.emptyList();
/*  33 */   private ServerPackStatus packStatus = ServerPackStatus.PROMPT;
/*     */   
/*     */   private byte[] iconBytes;
/*     */   private Type type;
/*     */   private int acceptedCodeOfConduct;
/*  38 */   private State state = State.INITIAL;
/*     */   
/*     */   public ServerData(String name, String ip, Type type) {
/*  41 */     this.name = name;
/*  42 */     this.ip = ip;
/*  43 */     this.type = type;
/*     */   }
/*     */   
/*     */   public CompoundTag write() {
/*  47 */     CompoundTag tag = new CompoundTag();
/*  48 */     tag.putString("name", this.name);
/*  49 */     tag.putString("ip", this.ip);
/*     */     
/*  51 */     tag.storeNullable("icon", ExtraCodecs.BASE64_STRING, this.iconBytes);
/*     */     
/*  53 */     tag.store(ServerPackStatus.FIELD_CODEC, this.packStatus);
/*  54 */     if (this.acceptedCodeOfConduct != 0) {
/*  55 */       tag.putInt("acceptedCodeOfConduct", this.acceptedCodeOfConduct);
/*     */     }
/*     */     
/*  58 */     return tag;
/*     */   }
/*     */   
/*     */   public ServerPackStatus getResourcePackStatus() {
/*  62 */     return this.packStatus;
/*     */   }
/*     */   
/*     */   public void setResourcePackStatus(ServerPackStatus packStatus) {
/*  66 */     this.packStatus = packStatus;
/*     */   }
/*     */   
/*     */   public static ServerData read(CompoundTag tag) {
/*  70 */     ServerData server = new ServerData(tag.getStringOr("name", ""), tag.getStringOr("ip", ""), Type.OTHER);
/*  71 */     server.setIconBytes(tag.read("icon", ExtraCodecs.BASE64_STRING).orElse(null));
/*  72 */     server.setResourcePackStatus(tag.read(ServerPackStatus.FIELD_CODEC).orElse(ServerPackStatus.PROMPT));
/*  73 */     server.acceptedCodeOfConduct = tag.getIntOr("acceptedCodeOfConduct", 0);
/*  74 */     return server;
/*     */   }
/*     */   
/*     */   public byte[] getIconBytes() {
/*  78 */     return this.iconBytes;
/*     */   }
/*     */   
/*     */   public void setIconBytes(byte[] iconBytes) {
/*  82 */     this.iconBytes = iconBytes;
/*     */   }
/*     */   
/*     */   public boolean isLan() {
/*  86 */     return (this.type == Type.LAN);
/*     */   }
/*     */   
/*     */   public boolean isRealm() {
/*  90 */     return (this.type == Type.REALM);
/*     */   }
/*     */   
/*     */   public Type type() {
/*  94 */     return this.type;
/*     */   }
/*     */   
/*     */   public boolean hasAcceptedCodeOfConduct(String codeOfConduct) {
/*  98 */     return (this.acceptedCodeOfConduct == codeOfConduct.hashCode());
/*     */   }
/*     */   
/*     */   public void acceptCodeOfConduct(String codeOfConduct) {
/* 102 */     this.acceptedCodeOfConduct = codeOfConduct.hashCode();
/*     */   }
/*     */   
/*     */   public void clearCodeOfConduct() {
/* 106 */     this.acceptedCodeOfConduct = 0;
/*     */   }
/*     */   
/*     */   public void copyNameIconFrom(ServerData other) {
/* 110 */     this.ip = other.ip;
/* 111 */     this.name = other.name;
/* 112 */     this.iconBytes = other.iconBytes;
/*     */   }
/*     */   
/*     */   public void copyFrom(ServerData other) {
/* 116 */     copyNameIconFrom(other);
/* 117 */     setResourcePackStatus(other.getResourcePackStatus());
/* 118 */     this.type = other.type;
/*     */   }
/*     */   
/*     */   public State state() {
/* 122 */     return this.state;
/*     */   }
/*     */   
/*     */   public void setState(State state) {
/* 126 */     this.state = state;
/*     */   }
/*     */   
/*     */   public enum ServerPackStatus {
/* 130 */     ENABLED("enabled"),
/* 131 */     DISABLED("disabled"),
/* 132 */     PROMPT("prompt");
/*     */     public static final MapCodec<ServerPackStatus> FIELD_CODEC; private final Component name;
/*     */     
/* 135 */     static { FIELD_CODEC = Codec.BOOL.optionalFieldOf("acceptTextures").xmap(acceptTextures -> (ServerPackStatus)acceptTextures.map(()).orElse(PROMPT), status -> {
/*     */             switch (status.ordinal()) {
/*     */               default:
/*     */                 throw new MatchException(null, null);
/*     */               case 0:
/*     */               
/*     */               case 1:
/*     */               
/*     */               case 2:
/*     */                 break;
/*     */             } 
/*     */             return Optional.empty();
/* 147 */           }); } ServerPackStatus(String name) { this.name = (Component)Component.translatable("manageServer.resourcePack." + name); }
/*     */ 
/*     */     
/*     */     public Component getName() {
/* 151 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Type {
/* 156 */     LAN,
/* 157 */     REALM,
/* 158 */     OTHER;
/*     */   }
/*     */   
/*     */   public enum State {
/* 162 */     INITIAL,
/* 163 */     PINGING,
/* 164 */     UNREACHABLE,
/* 165 */     INCOMPATIBLE,
/* 166 */     SUCCESSFUL;
/*     */   }
/*     */   
/*     */   public static byte[] validateIcon(byte[] bytes) {
/* 170 */     if (bytes != null) {
/*     */       try {
/* 172 */         PngInfo iconInfo = PngInfo.fromBytes(bytes);
/* 173 */         if (iconInfo.width() <= 1024 && iconInfo.height() <= 1024) {
/* 174 */           return bytes;
/*     */         }
/* 176 */       } catch (IOException e) {
/* 177 */         LOGGER.warn("Failed to decode server icon", e);
/*     */       } 
/*     */     }
/*     */     
/* 181 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/ServerData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */