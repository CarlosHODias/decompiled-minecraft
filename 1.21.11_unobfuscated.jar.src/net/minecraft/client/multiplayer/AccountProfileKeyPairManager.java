/*     */ package net.minecraft.client.multiplayer;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.authlib.minecraft.InsecurePublicKeyException;
/*     */ import com.mojang.authlib.minecraft.UserApiService;
/*     */ import com.mojang.authlib.yggdrasil.response.KeyPairResponse;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.security.PublicKey;
/*     */ import java.time.DateTimeException;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.util.Crypt;
/*     */ import net.minecraft.util.CryptException;
/*     */ import net.minecraft.util.StrictJsonParser;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.player.ProfileKeyPair;
/*     */ import net.minecraft.world.entity.player.ProfilePublicKey;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class AccountProfileKeyPairManager implements ProfileKeyPairManager {
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  36 */   private static final Duration MINIMUM_PROFILE_KEY_REFRESH_INTERVAL = Duration.ofHours(1L);
/*     */   
/*  38 */   private static final Path PROFILE_KEY_PAIR_DIR = Path.of("profilekeys", new String[0]);
/*     */   
/*     */   private final UserApiService userApiService;
/*     */   private final Path profileKeyPairPath;
/*  42 */   private CompletableFuture<Optional<ProfileKeyPair>> keyPair = CompletableFuture.completedFuture(Optional.empty());
/*     */   
/*  44 */   private Instant nextProfileKeyRefreshTime = Instant.EPOCH;
/*     */   
/*     */   public AccountProfileKeyPairManager(UserApiService userApiService, UUID profileId, Path gameDirectory) {
/*  47 */     this.userApiService = userApiService;
/*  48 */     this.profileKeyPairPath = gameDirectory.resolve(PROFILE_KEY_PAIR_DIR).resolve(String.valueOf(profileId) + ".json");
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Optional<ProfileKeyPair>> prepareKeyPair() {
/*  53 */     this.nextProfileKeyRefreshTime = Instant.now().plus(MINIMUM_PROFILE_KEY_REFRESH_INTERVAL);
/*  54 */     this.keyPair = this.keyPair.thenCompose(this::readOrFetchProfileKeyPair);
/*  55 */     return this.keyPair;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRefreshKeyPair() {
/*  60 */     if (this.keyPair.isDone() && Instant.now().isAfter(this.nextProfileKeyRefreshTime)) {
/*  61 */       return (Boolean)((Optional)this.keyPair.join()).map(ProfileKeyPair::dueRefresh).orElse(true);
/*     */     }
/*  63 */     return false;
/*     */   }
/*     */   
/*     */   private CompletableFuture<Optional<ProfileKeyPair>> readOrFetchProfileKeyPair(Optional<ProfileKeyPair> cachedKeyPair) {
/*  67 */     return CompletableFuture.supplyAsync(() -> {
/*     */           if (cachedKeyPair.isPresent() && !((ProfileKeyPair)cachedKeyPair.get()).dueRefresh()) {
/*     */             if (!SharedConstants.IS_RUNNING_IN_IDE) {
/*     */               writeProfileKeyPair(null);
/*     */             }
/*     */             
/*     */             return cachedKeyPair;
/*     */           } 
/*     */           
/*     */           try {
/*     */             ProfileKeyPair fetchedKeyPair = fetchProfileKeyPair(this.userApiService);
/*     */             writeProfileKeyPair(fetchedKeyPair);
/*     */             return Optional.ofNullable(fetchedKeyPair);
/*  80 */           } catch (IOException|CryptException|com.mojang.authlib.exceptions.MinecraftClientException e) {
/*     */             LOGGER.error("Failed to retrieve profile key pair", e);
/*     */             
/*     */             writeProfileKeyPair(null);
/*     */             return cachedKeyPair;
/*     */           } 
/*  86 */         }, (Executor)Util.nonCriticalIoPool());
/*     */   }
/*     */   
/*     */   private Optional<ProfileKeyPair> readProfileKeyPair() {
/*  90 */     if (Files.notExists(this.profileKeyPairPath, new java.nio.file.LinkOption[0])) {
/*  91 */       return Optional.empty();
/*     */     }
/*     */     
/*  94 */     try { BufferedReader bufferedReader = Files.newBufferedReader(this.profileKeyPairPath); 
/*  95 */       try { Optional<ProfileKeyPair> optional = ProfileKeyPair.CODEC.parse((DynamicOps)JsonOps.INSTANCE, StrictJsonParser.parse(bufferedReader)).result();
/*  96 */         if (bufferedReader != null) bufferedReader.close();  return optional; } catch (Throwable throwable) { if (bufferedReader != null) try { bufferedReader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/*  97 */     { LOGGER.error("Failed to read profile key pair file {}", this.profileKeyPairPath, e);
/*  98 */       return Optional.empty(); }
/*     */   
/*     */   }
/*     */   
/*     */   private void writeProfileKeyPair(ProfileKeyPair profileKeyPair) {
/*     */     try {
/* 104 */       Files.deleteIfExists(this.profileKeyPairPath);
/* 105 */     } catch (IOException e) {
/* 106 */       LOGGER.error("Failed to delete profile key pair file {}", this.profileKeyPairPath, e);
/*     */     } 
/*     */     
/* 109 */     if (profileKeyPair == null) {
/*     */       return;
/*     */     }
/*     */     
/* 113 */     if (!SharedConstants.IS_RUNNING_IN_IDE) {
/*     */       return;
/*     */     }
/*     */     
/* 117 */     ProfileKeyPair.CODEC.encodeStart((DynamicOps)JsonOps.INSTANCE, profileKeyPair).ifSuccess(jsonStr -> {
/*     */           try {
/*     */             Files.createDirectories(this.profileKeyPairPath.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
/*     */             Files.writeString(this.profileKeyPairPath, jsonStr.toString(), new java.nio.file.OpenOption[0]);
/* 121 */           } catch (Exception e) {
/*     */             LOGGER.error("Failed to write profile key pair file {}", this.profileKeyPairPath, e);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private ProfileKeyPair fetchProfileKeyPair(UserApiService userApiService) throws CryptException, IOException {
/* 128 */     KeyPairResponse keyPair = userApiService.getKeyPair();
/* 129 */     if (keyPair != null) {
/* 130 */       ProfilePublicKey.Data publicKeyData = parsePublicKey(keyPair);
/* 131 */       return new ProfileKeyPair(
/* 132 */           Crypt.stringToPemRsaPrivateKey(keyPair.keyPair().privateKey()), new ProfilePublicKey(publicKeyData), 
/*     */           
/* 134 */           Instant.parse(keyPair.refreshedAfter()));
/*     */     } 
/*     */     
/* 137 */     return null;
/*     */   }
/*     */   
/*     */   private static ProfilePublicKey.Data parsePublicKey(KeyPairResponse response) throws CryptException {
/* 141 */     KeyPairResponse.KeyPair keyPair = response.keyPair();
/* 142 */     if (keyPair == null || Strings.isNullOrEmpty(keyPair.publicKey()) || response.publicKeySignature() == null || (response.publicKeySignature().array()).length == 0) {
/* 143 */       throw new CryptException(new InsecurePublicKeyException.MissingException("Missing public key"));
/*     */     }
/*     */     
/*     */     try {
/* 147 */       Instant expiresAt = Instant.parse(response.expiresAt());
/* 148 */       PublicKey key = Crypt.stringToRsaPublicKey(keyPair.publicKey());
/* 149 */       ByteBuffer signature = response.publicKeySignature();
/*     */       
/* 151 */       return new ProfilePublicKey.Data(expiresAt, key, signature.array());
/* 152 */     } catch (DateTimeException|IllegalArgumentException e) {
/* 153 */       throw new CryptException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/AccountProfileKeyPairManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */