/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.util.UndashedUuid;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Arrays;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public final class UUIDUtil {
/*     */   static {
/*  25 */     CODEC = Codec.INT_STREAM.comapFlatMap(list -> Util.fixedSize(list, 4).map(UUIDUtil::uuidFromIntArray), uuid -> Arrays.stream(uuidToIntArray(uuid)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Codec<UUID> CODEC;
/*  30 */   public static final Codec<Set<UUID>> CODEC_SET = Codec.list(CODEC).xmap(Sets::newHashSet, Lists::newArrayList);
/*  31 */   public static final Codec<Set<UUID>> CODEC_LINKED_SET = Codec.list(CODEC).xmap(Sets::newLinkedHashSet, Lists::newArrayList);
/*     */   static {
/*  33 */     STRING_CODEC = Codec.STRING.comapFlatMap(s -> {
/*     */           try {
/*     */             return DataResult.success(UUID.fromString(s), Lifecycle.stable());
/*  36 */           } catch (IllegalArgumentException e) {
/*     */             return DataResult.error(());
/*     */           } 
/*     */         }, UUID::toString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  46 */     AUTHLIB_CODEC = Codec.withAlternative(
/*  47 */         Codec.STRING.comapFlatMap(s -> {
/*     */             
/*     */             try {
/*     */               return DataResult.success(UndashedUuid.fromStringLenient(s), Lifecycle.stable());
/*  51 */             } catch (IllegalArgumentException e) {
/*     */               return DataResult.error(());
/*     */             } 
/*     */           }, UndashedUuid::toString), CODEC);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Codec<UUID> STRING_CODEC;
/*     */   
/*     */   public static final Codec<UUID> AUTHLIB_CODEC;
/*  61 */   public static final Codec<UUID> LENIENT_CODEC = Codec.withAlternative(CODEC, STRING_CODEC);
/*     */   
/*  63 */   public static final StreamCodec<ByteBuf, UUID> STREAM_CODEC = new StreamCodec<ByteBuf, UUID>()
/*     */     {
/*     */       public UUID decode(ByteBuf input) {
/*  66 */         return FriendlyByteBuf.readUUID(input);
/*     */       }
/*     */ 
/*     */       
/*     */       public void encode(ByteBuf output, UUID value) {
/*  71 */         FriendlyByteBuf.writeUUID(output, value);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int UUID_BYTES = 16;
/*     */   
/*     */   private static final String UUID_PREFIX_OFFLINE_PLAYER = "OfflinePlayer:";
/*     */ 
/*     */   
/*     */   public static UUID uuidFromIntArray(int[] intArray) {
/*  83 */     return new UUID(intArray[0] << 32L | intArray[1] & 0xFFFFFFFFL, intArray[2] << 32L | intArray[3] & 0xFFFFFFFFL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] uuidToIntArray(UUID uuid) {
/*  90 */     long mostSignificantBits = uuid.getMostSignificantBits();
/*  91 */     long leastSignificantBits = uuid.getLeastSignificantBits();
/*  92 */     return leastMostToIntArray(mostSignificantBits, leastSignificantBits);
/*     */   }
/*     */   
/*     */   private static int[] leastMostToIntArray(long mostSignificantBits, long leastSignificantBits) {
/*  96 */     return new int[] { (int)(mostSignificantBits >> 32L), (int)mostSignificantBits, (int)(leastSignificantBits >> 32L), (int)leastSignificantBits };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] uuidToByteArray(UUID uuid) {
/* 105 */     byte[] bytes = new byte[16];
/* 106 */     ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN)
/* 107 */       .putLong(uuid.getMostSignificantBits())
/* 108 */       .putLong(uuid.getLeastSignificantBits());
/*     */     
/* 110 */     return bytes;
/*     */   }
/*     */   
/*     */   public static UUID readUUID(Dynamic<?> input) {
/* 114 */     int[] intArray = input.asIntStream().toArray();
/* 115 */     if (intArray.length != 4) {
/* 116 */       throw new IllegalArgumentException("Could not read UUID. Expected int-array of length 4, got " + intArray.length + ".");
/*     */     }
/* 118 */     return uuidFromIntArray(intArray);
/*     */   }
/*     */   
/*     */   public static UUID createOfflinePlayerUUID(String playerName) {
/* 122 */     return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(StandardCharsets.UTF_8));
/*     */   }
/*     */   
/*     */   public static GameProfile createOfflineProfile(String playerName) {
/* 126 */     UUID id = createOfflinePlayerUUID(playerName);
/* 127 */     return new GameProfile(id, playerName);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/UUIDUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */