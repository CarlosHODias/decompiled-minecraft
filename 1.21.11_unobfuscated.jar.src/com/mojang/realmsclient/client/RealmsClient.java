/*     */ package com.mojang.realmsclient.client;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.RealmsMainScreen;
/*     */ import com.mojang.realmsclient.dto.BackupList;
/*     */ import com.mojang.realmsclient.dto.GuardedSerializer;
/*     */ import com.mojang.realmsclient.dto.Ops;
/*     */ import com.mojang.realmsclient.dto.OutboundPlayer;
/*     */ import com.mojang.realmsclient.dto.PendingInvite;
/*     */ import com.mojang.realmsclient.dto.PendingInvitesList;
/*     */ import com.mojang.realmsclient.dto.PingResult;
/*     */ import com.mojang.realmsclient.dto.PlayerInfo;
/*     */ import com.mojang.realmsclient.dto.PreferredRegionsDto;
/*     */ import com.mojang.realmsclient.dto.RealmsConfigurationDto;
/*     */ import com.mojang.realmsclient.dto.RealmsDescriptionDto;
/*     */ import com.mojang.realmsclient.dto.RealmsJoinInformation;
/*     */ import com.mojang.realmsclient.dto.RealmsNews;
/*     */ import com.mojang.realmsclient.dto.RealmsNotification;
/*     */ import com.mojang.realmsclient.dto.RealmsRegion;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.RealmsServerList;
/*     */ import com.mojang.realmsclient.dto.RealmsServerPlayerLists;
/*     */ import com.mojang.realmsclient.dto.RealmsSetting;
/*     */ import com.mojang.realmsclient.dto.RealmsSlotUpdateDto;
/*     */ import com.mojang.realmsclient.dto.RealmsWorldOptions;
/*     */ import com.mojang.realmsclient.dto.RealmsWorldResetDto;
/*     */ import com.mojang.realmsclient.dto.ReflectionBasedSerialization;
/*     */ import com.mojang.realmsclient.dto.RegionDataDto;
/*     */ import com.mojang.realmsclient.dto.RegionSelectionPreference;
/*     */ import com.mojang.realmsclient.dto.RegionSelectionPreferenceDto;
/*     */ import com.mojang.realmsclient.dto.Subscription;
/*     */ import com.mojang.realmsclient.dto.UploadInfo;
/*     */ import com.mojang.realmsclient.dto.WorldDownload;
/*     */ import com.mojang.realmsclient.dto.WorldTemplatePaginatedList;
/*     */ import com.mojang.realmsclient.exception.RealmsHttpException;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.exception.RetryCallException;
/*     */ import com.mojang.realmsclient.util.UploadTokenCache;
/*     */ import com.mojang.util.UndashedUuid;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.util.LenientJsonParser;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RealmsClient {
/*     */   public enum Environment {
/*  60 */     PRODUCTION("pc.realms.minecraft.net", "java.frontendlegacy.realms.minecraft-services.net", "https"),
/*  61 */     STAGE("pc-stage.realms.minecraft.net", "java.frontendlegacy.stage-c2a40e62.realms.minecraft-services.net", "https"),
/*  62 */     LOCAL("localhost:8080", "localhost:8080", "http");
/*     */     
/*     */     public final String baseUrl;
/*     */     public final String alternativeUrl;
/*     */     public final String protocol;
/*     */     
/*     */     Environment(String baseUrl, String alternativeUrl, String protocol) {
/*  69 */       this.baseUrl = baseUrl;
/*  70 */       this.alternativeUrl = alternativeUrl;
/*  71 */       this.protocol = protocol;
/*     */     }
/*     */     
/*     */     public static Optional<Environment> byName(String name) {
/*  75 */       switch (name.toLowerCase(Locale.ROOT)) { case "production": case "local": case "stage": case "staging": default: break; }  return 
/*     */ 
/*     */ 
/*     */         
/*  79 */         Optional.empty();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  84 */   public static final Environment ENVIRONMENT = Optional.<String>ofNullable(System.getenv("realms.environment"))
/*  85 */     .or(() -> Optional.ofNullable(System.getProperty("realms.environment")))
/*  86 */     .flatMap(Environment::byName)
/*  87 */     .orElse(Environment.PRODUCTION);
/*     */   
/*  89 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  91 */   private static volatile RealmsClient realmsClientInstance = null;
/*     */   
/*     */   private final CompletableFuture<Set<String>> featureFlags;
/*     */   
/*     */   private final String sessionId;
/*     */   
/*     */   private final String username;
/*     */   private final Minecraft minecraft;
/*     */   private static final String WORLDS_RESOURCE_PATH = "worlds";
/*     */   private static final String INVITES_RESOURCE_PATH = "invites";
/*     */   private static final String MCO_RESOURCE_PATH = "mco";
/*     */   private static final String SUBSCRIPTION_RESOURCE = "subscriptions";
/*     */   private static final String ACTIVITIES_RESOURCE = "activities";
/*     */   private static final String OPS_RESOURCE = "ops";
/*     */   private static final String REGIONS_RESOURCE = "regions/ping/stat";
/*     */   private static final String PREFERRED_REGION_RESOURCE = "regions/preferredRegions";
/*     */   private static final String TRIALS_RESOURCE = "trial";
/*     */   private static final String NOTIFICATIONS_RESOURCE = "notifications";
/*     */   private static final String FEATURE_FLAGS_RESOURCE = "feature/v1";
/*     */   private static final String PATH_LIST_ALL_REALMS = "/listUserWorldsOfType/any";
/*     */   private static final String PATH_CREATE_SNAPSHOT_REALM = "/$PARENT_WORLD_ID/createPrereleaseRealm";
/*     */   private static final String PATH_SNAPSHOT_ELIGIBLE_REALMS = "/listPrereleaseEligibleWorlds";
/*     */   private static final String PATH_INITIALIZE = "/$WORLD_ID/initialize";
/*     */   private static final String PATH_GET_LIVESTATS = "/liveplayerlist";
/*     */   private static final String PATH_GET_SUBSCRIPTION = "/$WORLD_ID";
/*     */   private static final String PATH_OP = "/$WORLD_ID/$PROFILE_UUID";
/*     */   private static final String PATH_PUT_INTO_MINIGAMES_MODE = "/minigames/$MINIGAME_ID/$WORLD_ID";
/*     */   private static final String PATH_AVAILABLE = "/available";
/*     */   private static final String PATH_TEMPLATES = "/templates/$WORLD_TYPE";
/*     */   private static final String PATH_WORLD_JOIN = "/v1/$ID/join/pc";
/*     */   private static final String PATH_WORLD_GET = "/$ID";
/*     */   private static final String PATH_WORLD_INVITES = "/$WORLD_ID";
/*     */   private static final String PATH_WORLD_UNINVITE = "/$WORLD_ID/invite/$UUID";
/*     */   private static final String PATH_PENDING_INVITES = "/pending";
/*     */   private static final String PATH_ACCEPT_INVITE = "/accept/$INVITATION_ID";
/*     */   private static final String PATH_REJECT_INVITE = "/reject/$INVITATION_ID";
/*     */   private static final String PATH_UNINVITE_MYSELF = "/$WORLD_ID";
/*     */   private static final String PATH_WORLD_CONFIGURE = "/$WORLD_ID/configuration";
/*     */   private static final String PATH_SLOT = "/$WORLD_ID/slot/$SLOT_ID";
/*     */   private static final String PATH_WORLD_OPEN = "/$WORLD_ID/open";
/*     */   private static final String PATH_WORLD_CLOSE = "/$WORLD_ID/close";
/*     */   private static final String PATH_WORLD_RESET = "/$WORLD_ID/reset";
/*     */   private static final String PATH_DELETE_WORLD = "/$WORLD_ID";
/*     */   private static final String PATH_WORLD_BACKUPS = "/$WORLD_ID/backups";
/*     */   private static final String PATH_WORLD_DOWNLOAD = "/$WORLD_ID/slot/$SLOT_ID/download";
/*     */   private static final String PATH_WORLD_UPLOAD = "/$WORLD_ID/backups/upload";
/*     */   private static final String PATH_CLIENT_COMPATIBLE = "/client/compatible";
/*     */   private static final String PATH_TOS_AGREED = "/tos/agreed";
/*     */   private static final String PATH_NEWS = "/v1/news";
/*     */   private static final String PATH_MARK_NOTIFICATIONS_SEEN = "/seen";
/*     */   private static final String PATH_DISMISS_NOTIFICATIONS = "/dismiss";
/* 142 */   private static final GuardedSerializer GSON = new GuardedSerializer();
/*     */   
/*     */   public static RealmsClient getOrCreate() {
/* 145 */     Minecraft minecraft = Minecraft.getInstance();
/* 146 */     return getOrCreate(minecraft);
/*     */   }
/*     */   
/*     */   public static RealmsClient getOrCreate(Minecraft minecraft) {
/* 150 */     String username = minecraft.getUser().getName();
/* 151 */     String sessionId = minecraft.getUser().getSessionId();
/* 152 */     RealmsClient realmsClient = realmsClientInstance;
/* 153 */     if (realmsClient != null) {
/* 154 */       return realmsClient;
/*     */     }
/* 156 */     synchronized (RealmsClient.class) {
/* 157 */       RealmsClient rc = realmsClientInstance;
/* 158 */       if (rc != null) {
/* 159 */         return rc;
/*     */       }
/* 161 */       rc = new RealmsClient(sessionId, username, minecraft);
/* 162 */       realmsClientInstance = rc;
/* 163 */       return rc;
/*     */     } 
/*     */   }
/*     */   
/*     */   private RealmsClient(String sessionId, String username, Minecraft minecraft) {
/* 168 */     this.sessionId = sessionId;
/* 169 */     this.username = username;
/* 170 */     this.minecraft = minecraft;
/*     */     
/* 172 */     RealmsClientConfig.setProxy(minecraft.getProxy());
/* 173 */     this.featureFlags = CompletableFuture.supplyAsync(this::fetchFeatureFlags, (Executor)Util.nonCriticalIoPool());
/*     */   }
/*     */   
/*     */   public Set<String> getFeatureFlags() {
/* 177 */     return this.featureFlags.join();
/*     */   }
/*     */   
/*     */   private Set<String> fetchFeatureFlags() {
/* 181 */     if (Minecraft.getInstance().isOfflineDeveloperMode()) {
/* 182 */       return Set.of();
/*     */     }
/* 184 */     String asciiUrl = url("feature/v1", null, false);
/*     */     try {
/* 186 */       String returnJson = execute(Request.get(asciiUrl, 5000, 10000));
/* 187 */       JsonArray object = LenientJsonParser.parse(returnJson).getAsJsonArray();
/* 188 */       Set<String> featureFlags = (Set<String>)object.asList().stream()
/* 189 */         .map(JsonElement::getAsString)
/* 190 */         .collect(Collectors.toSet());
/* 191 */       LOGGER.debug("Fetched Realms feature flags: {}", featureFlags);
/* 192 */       return featureFlags;
/* 193 */     } catch (RealmsServiceException e) {
/* 194 */       LOGGER.error("Failed to fetch Realms feature flags", (Throwable)e);
/* 195 */     } catch (Exception e) {
/* 196 */       LOGGER.error("Could not parse Realms feature flags", e);
/*     */     } 
/* 198 */     return Set.of();
/*     */   }
/*     */   
/*     */   public RealmsServerList listRealms() throws RealmsServiceException {
/* 202 */     String asciiUrl = url("worlds");
/* 203 */     if (RealmsMainScreen.isSnapshot()) {
/* 204 */       asciiUrl = asciiUrl + "/listUserWorldsOfType/any";
/*     */     }
/* 206 */     String json = execute(Request.get(asciiUrl));
/* 207 */     return RealmsServerList.parse(GSON, json);
/*     */   }
/*     */   
/*     */   public List<RealmsServer> listSnapshotEligibleRealms() throws RealmsServiceException {
/* 211 */     String asciiUrl = url("worlds/listPrereleaseEligibleWorlds");
/* 212 */     String json = execute(Request.get(asciiUrl));
/* 213 */     return RealmsServerList.parse(GSON, json).servers();
/*     */   }
/*     */   
/*     */   public RealmsServer createSnapshotRealm(Long parentId) throws RealmsServiceException {
/* 217 */     String parentIdString = String.valueOf(parentId);
/* 218 */     String url = url("worlds" + "/$PARENT_WORLD_ID/createPrereleaseRealm".replace("$PARENT_WORLD_ID", parentIdString));
/* 219 */     return RealmsServer.parse(GSON, execute(Request.post(url, parentIdString)));
/*     */   }
/*     */   
/*     */   public List<RealmsNotification> getNotifications() throws RealmsServiceException {
/* 223 */     String endpoint = url("notifications");
/* 224 */     String responseJson = execute(Request.get(endpoint));
/* 225 */     return RealmsNotification.parseList(responseJson);
/*     */   }
/*     */   
/*     */   private static JsonArray uuidListToJsonArray(List<UUID> uuids) {
/* 229 */     JsonArray array = new JsonArray();
/* 230 */     for (UUID uuid : uuids) {
/* 231 */       if (uuid != null) {
/* 232 */         array.add(uuid.toString());
/*     */       }
/*     */     } 
/* 235 */     return array;
/*     */   }
/*     */   
/*     */   public void notificationsSeen(List<UUID> notificationUuids) throws RealmsServiceException {
/* 239 */     String endpoint = url("notifications/seen");
/* 240 */     execute(Request.post(endpoint, GSON.toJson((JsonElement)uuidListToJsonArray(notificationUuids))));
/*     */   }
/*     */   
/*     */   public void notificationsDismiss(List<UUID> notificationUuids) throws RealmsServiceException {
/* 244 */     String endpoint = url("notifications/dismiss");
/* 245 */     execute(Request.post(endpoint, GSON.toJson((JsonElement)uuidListToJsonArray(notificationUuids))));
/*     */   }
/*     */   
/*     */   public RealmsServer getOwnRealm(long realmId) throws RealmsServiceException {
/* 249 */     String asciiUrl = url("worlds" + "/$ID".replace("$ID", String.valueOf(realmId)));
/* 250 */     String json = execute(Request.get(asciiUrl));
/* 251 */     return RealmsServer.parse(GSON, json);
/*     */   }
/*     */   
/*     */   public PreferredRegionsDto getPreferredRegionSelections() throws RealmsServiceException {
/* 255 */     String asciiUrl = url("regions/preferredRegions");
/* 256 */     String json = execute(Request.get(asciiUrl));
/*     */     try {
/* 258 */       PreferredRegionsDto preferredRegionsDto = (PreferredRegionsDto)GSON.fromJson(json, PreferredRegionsDto.class);
/* 259 */       if (preferredRegionsDto == null) {
/* 260 */         return PreferredRegionsDto.empty();
/*     */       }
/*     */       
/* 263 */       Set<RealmsRegion> regionsInResponse = (Set<RealmsRegion>)preferredRegionsDto.regionData().stream()
/* 264 */         .map(RegionDataDto::region)
/* 265 */         .collect(Collectors.toSet());
/*     */       
/* 267 */       for (RealmsRegion region : RealmsRegion.values()) {
/* 268 */         if (region != RealmsRegion.INVALID_REGION && !regionsInResponse.contains(region)) {
/* 269 */           LOGGER.debug("No realms region matching {} in server response", region);
/*     */         }
/*     */       } 
/*     */       
/* 273 */       return preferredRegionsDto;
/* 274 */     } catch (Exception e) {
/* 275 */       LOGGER.error("Could not parse PreferredRegionSelections", e);
/*     */       
/* 277 */       return PreferredRegionsDto.empty();
/*     */     } 
/*     */   }
/*     */   public RealmsServerPlayerLists getLiveStats() throws RealmsServiceException {
/* 281 */     String asciiUrl = url("activities/liveplayerlist");
/* 282 */     String json = execute(Request.get(asciiUrl));
/* 283 */     return RealmsServerPlayerLists.parse(json);
/*     */   }
/*     */   
/*     */   public RealmsJoinInformation join(long realmId) throws RealmsServiceException {
/* 287 */     String asciiUrl = url("worlds" + "/v1/$ID/join/pc".replace("$ID", "" + realmId));
/* 288 */     String json = execute(Request.get(asciiUrl, 5000, 30000));
/* 289 */     return RealmsJoinInformation.parse(GSON, json);
/*     */   }
/*     */   
/*     */   public void initializeRealm(long realmId, String name, String motd) throws RealmsServiceException {
/* 293 */     RealmsDescriptionDto realmsDescription = new RealmsDescriptionDto(name, motd);
/* 294 */     String asciiUrl = url("worlds" + "/$WORLD_ID/initialize".replace("$WORLD_ID", String.valueOf(realmId)));
/* 295 */     String json = GSON.toJson((ReflectionBasedSerialization)realmsDescription);
/* 296 */     execute(Request.post(asciiUrl, json, 5000, 10000));
/*     */   }
/*     */   
/*     */   public boolean hasParentalConsent() throws RealmsServiceException {
/* 300 */     String asciiUrl = url("mco/available");
/* 301 */     String json = execute(Request.get(asciiUrl));
/* 302 */     return Boolean.parseBoolean(json);
/*     */   }
/*     */   public CompatibleVersionResponse clientCompatible() throws RealmsServiceException {
/*     */     CompatibleVersionResponse result;
/* 306 */     String asciiUrl = url("mco/client/compatible");
/* 307 */     String response = execute(Request.get(asciiUrl));
/*     */ 
/*     */     
/*     */     try {
/* 311 */       result = CompatibleVersionResponse.valueOf(response);
/* 312 */     } catch (IllegalArgumentException ignored) {
/* 313 */       throw new RealmsServiceException(RealmsError.CustomError.unknownCompatibilityResponse(response));
/*     */     } 
/*     */     
/* 316 */     return result;
/*     */   }
/*     */   
/*     */   public void uninvite(long realmId, UUID profileId) throws RealmsServiceException {
/* 320 */     String asciiUrl = url("invites" + "/$WORLD_ID/invite/$UUID".replace("$WORLD_ID", String.valueOf(realmId)).replace("$UUID", UndashedUuid.toString(profileId)));
/* 321 */     execute(Request.delete(asciiUrl));
/*     */   }
/*     */   
/*     */   public void uninviteMyselfFrom(long realmId) throws RealmsServiceException {
/* 325 */     String asciiUrl = url("invites" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(realmId)));
/* 326 */     execute(Request.delete(asciiUrl));
/*     */   }
/*     */   
/*     */   public List<PlayerInfo> invite(long realmId, String profileName) throws RealmsServiceException {
/* 330 */     OutboundPlayer playerInfo = new OutboundPlayer();
/* 331 */     playerInfo.name = profileName;
/*     */     
/* 333 */     String asciiUrl = url("invites" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(realmId)));
/* 334 */     String json = execute(Request.post(asciiUrl, GSON.toJson((ReflectionBasedSerialization)playerInfo)));
/* 335 */     return (RealmsServer.parse(GSON, json)).players;
/*     */   }
/*     */   
/*     */   public BackupList backupsFor(long realmId) throws RealmsServiceException {
/* 339 */     String asciiUrl = url("worlds" + "/$WORLD_ID/backups".replace("$WORLD_ID", String.valueOf(realmId)));
/* 340 */     String json = execute(Request.get(asciiUrl));
/* 341 */     return BackupList.parse(json);
/*     */   }
/*     */   
/*     */   public void updateConfiguration(long realmId, String name, String description, RegionSelectionPreferenceDto regionSelectionPreference, int slotId, RealmsWorldOptions options, List<RealmsSetting> settings) throws RealmsServiceException {
/* 345 */     RegionSelectionPreferenceDto preferenceDto = (regionSelectionPreference != null) ? regionSelectionPreference : new RegionSelectionPreferenceDto(RegionSelectionPreference.DEFAULT_SELECTION, null);
/* 346 */     RealmsDescriptionDto realmsDescription = new RealmsDescriptionDto(name, description);
/* 347 */     RealmsSlotUpdateDto slotUpdateDto = new RealmsSlotUpdateDto(slotId, options, RealmsSetting.isHardcore(settings));
/* 348 */     RealmsConfigurationDto realmsConfiguration = new RealmsConfigurationDto(slotUpdateDto, settings, preferenceDto, realmsDescription);
/* 349 */     String asciiUrl = url("worlds" + "/$WORLD_ID/configuration".replace("$WORLD_ID", String.valueOf(realmId)));
/* 350 */     execute(Request.post(asciiUrl, GSON.toJson((ReflectionBasedSerialization)realmsConfiguration)));
/*     */   }
/*     */   
/*     */   public void updateSlot(long realmId, int slotId, RealmsWorldOptions options, List<RealmsSetting> settings) throws RealmsServiceException {
/* 354 */     String asciiUrl = url("worlds" + "/$WORLD_ID/slot/$SLOT_ID".replace("$WORLD_ID", String.valueOf(realmId)).replace("$SLOT_ID", String.valueOf(slotId)));
/* 355 */     String json = GSON.toJson((ReflectionBasedSerialization)new RealmsSlotUpdateDto(slotId, options, RealmsSetting.isHardcore(settings)));
/* 356 */     execute(Request.post(asciiUrl, json));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean switchSlot(long realmId, int slot) throws RealmsServiceException {
/* 361 */     String asciiUrl = url("worlds" + "/$WORLD_ID/slot/$SLOT_ID".replace("$WORLD_ID", String.valueOf(realmId)).replace("$SLOT_ID", String.valueOf(slot)));
/* 362 */     String json = execute(Request.put(asciiUrl, ""));
/* 363 */     return Boolean.valueOf(json);
/*     */   }
/*     */   
/*     */   public void restoreWorld(long realmId, String backupId) throws RealmsServiceException {
/* 367 */     String asciiUrl = url("worlds" + "/$WORLD_ID/backups".replace("$WORLD_ID", String.valueOf(realmId)), "backupId=" + backupId);
/* 368 */     execute(Request.put(asciiUrl, "", 40000, 600000));
/*     */   }
/*     */   
/*     */   public WorldTemplatePaginatedList fetchWorldTemplates(int page, int pageSize, RealmsServer.WorldType type) throws RealmsServiceException {
/* 372 */     String asciiUrl = url("worlds" + "/templates/$WORLD_TYPE".replace("$WORLD_TYPE", type.toString()), String.format(Locale.ROOT, "page=%d&pageSize=%d", new Object[] { page, pageSize }));
/* 373 */     String json = execute(Request.get(asciiUrl));
/* 374 */     return WorldTemplatePaginatedList.parse(json);
/*     */   }
/*     */   
/*     */   public Boolean putIntoMinigameMode(long realmId, String minigameId) throws RealmsServiceException {
/* 378 */     String path = "/minigames/$MINIGAME_ID/$WORLD_ID".replace("$MINIGAME_ID", minigameId).replace("$WORLD_ID", String.valueOf(realmId));
/* 379 */     String asciiUrl = url("worlds" + path);
/* 380 */     return Boolean.valueOf(execute(Request.put(asciiUrl, "")));
/*     */   }
/*     */   
/*     */   public Ops op(long realmId, UUID profileId) throws RealmsServiceException {
/* 384 */     String path = "/$WORLD_ID/$PROFILE_UUID".replace("$WORLD_ID", String.valueOf(realmId)).replace("$PROFILE_UUID", UndashedUuid.toString(profileId));
/* 385 */     String asciiUrl = url("ops" + path);
/* 386 */     return Ops.parse(execute(Request.post(asciiUrl, "")));
/*     */   }
/*     */   
/*     */   public Ops deop(long realmId, UUID profileId) throws RealmsServiceException {
/* 390 */     String path = "/$WORLD_ID/$PROFILE_UUID".replace("$WORLD_ID", String.valueOf(realmId)).replace("$PROFILE_UUID", UndashedUuid.toString(profileId));
/* 391 */     String asciiUrl = url("ops" + path);
/* 392 */     return Ops.parse(execute(Request.delete(asciiUrl)));
/*     */   }
/*     */   
/*     */   public Boolean open(long realmId) throws RealmsServiceException {
/* 396 */     String asciiUrl = url("worlds" + "/$WORLD_ID/open".replace("$WORLD_ID", String.valueOf(realmId)));
/* 397 */     String json = execute(Request.put(asciiUrl, ""));
/* 398 */     return Boolean.valueOf(json);
/*     */   }
/*     */   
/*     */   public Boolean close(long realmId) throws RealmsServiceException {
/* 402 */     String asciiUrl = url("worlds" + "/$WORLD_ID/close".replace("$WORLD_ID", String.valueOf(realmId)));
/* 403 */     String json = execute(Request.put(asciiUrl, ""));
/* 404 */     return Boolean.valueOf(json);
/*     */   }
/*     */   
/*     */   public Boolean resetWorldWithTemplate(long realmId, String worldTemplateId) throws RealmsServiceException {
/* 408 */     RealmsWorldResetDto worldReset = new RealmsWorldResetDto(null, Long.valueOf(worldTemplateId), -1, false, Set.of());
/* 409 */     String asciiUrl = url("worlds" + "/$WORLD_ID/reset".replace("$WORLD_ID", String.valueOf(realmId)));
/* 410 */     String json = execute(Request.post(asciiUrl, GSON.toJson((ReflectionBasedSerialization)worldReset), 30000, 80000));
/* 411 */     return Boolean.valueOf(json);
/*     */   }
/*     */   
/*     */   public Subscription subscriptionFor(long realmId) throws RealmsServiceException {
/* 415 */     String asciiUrl = url("subscriptions" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(realmId)));
/* 416 */     String json = execute(Request.get(asciiUrl));
/* 417 */     return Subscription.parse(json);
/*     */   }
/*     */ 
/*     */   
/*     */   public int pendingInvitesCount() throws RealmsServiceException {
/* 422 */     return pendingInvites().pendingInvites().size();
/*     */   }
/*     */   
/*     */   public PendingInvitesList pendingInvites() throws RealmsServiceException {
/* 426 */     String asciiUrl = url("invites/pending");
/* 427 */     String json = execute(Request.get(asciiUrl));
/* 428 */     PendingInvitesList list = PendingInvitesList.parse(json);
/* 429 */     list.pendingInvites().removeIf(this::isBlocked);
/* 430 */     return list;
/*     */   }
/*     */   
/*     */   private boolean isBlocked(PendingInvite invite) {
/* 434 */     return this.minecraft.getPlayerSocialManager().isBlocked(invite.realmOwnerUuid());
/*     */   }
/*     */   
/*     */   public void acceptInvitation(String invitationId) throws RealmsServiceException {
/* 438 */     String asciiUrl = url("invites" + "/accept/$INVITATION_ID".replace("$INVITATION_ID", invitationId));
/* 439 */     execute(Request.put(asciiUrl, ""));
/*     */   }
/*     */   
/*     */   public WorldDownload requestDownloadInfo(long realmId, int slotId) throws RealmsServiceException {
/* 443 */     String asciiUrl = url("worlds" + "/$WORLD_ID/slot/$SLOT_ID/download".replace("$WORLD_ID", String.valueOf(realmId)).replace("$SLOT_ID", String.valueOf(slotId)));
/* 444 */     String json = execute(Request.get(asciiUrl));
/* 445 */     return WorldDownload.parse(json);
/*     */   }
/*     */   
/*     */   public UploadInfo requestUploadInfo(long realmId) throws RealmsServiceException {
/* 449 */     String asciiUrl = url("worlds" + "/$WORLD_ID/backups/upload".replace("$WORLD_ID", String.valueOf(realmId)));
/* 450 */     String uploadToken = UploadTokenCache.get(realmId);
/* 451 */     UploadInfo uploadInfo = UploadInfo.parse(execute(Request.put(asciiUrl, UploadInfo.createRequest(uploadToken))));
/* 452 */     if (uploadInfo != null) {
/* 453 */       UploadTokenCache.put(realmId, uploadInfo.token());
/*     */     }
/* 455 */     return uploadInfo;
/*     */   }
/*     */   
/*     */   public void rejectInvitation(String invitationId) throws RealmsServiceException {
/* 459 */     String asciiUrl = url("invites" + "/reject/$INVITATION_ID".replace("$INVITATION_ID", invitationId));
/* 460 */     execute(Request.put(asciiUrl, ""));
/*     */   }
/*     */   
/*     */   public void agreeToTos() throws RealmsServiceException {
/* 464 */     String asciiUrl = url("mco/tos/agreed");
/* 465 */     execute(Request.post(asciiUrl, ""));
/*     */   }
/*     */   
/*     */   public RealmsNews getNews() throws RealmsServiceException {
/* 469 */     String asciiUrl = url("mco/v1/news");
/* 470 */     String returnJson = execute(Request.get(asciiUrl, 5000, 10000));
/* 471 */     return RealmsNews.parse(returnJson);
/*     */   }
/*     */   
/*     */   public void sendPingResults(PingResult pingResult) throws RealmsServiceException {
/* 475 */     String asciiUrl = url("regions/ping/stat");
/* 476 */     execute(Request.post(asciiUrl, GSON.toJson((ReflectionBasedSerialization)pingResult)));
/*     */   }
/*     */   
/*     */   public Boolean trialAvailable() throws RealmsServiceException {
/* 480 */     String asciiUrl = url("trial");
/* 481 */     String json = execute(Request.get(asciiUrl));
/* 482 */     return Boolean.valueOf(json);
/*     */   }
/*     */   
/*     */   public void deleteRealm(long realmId) throws RealmsServiceException {
/* 486 */     String asciiUrl = url("worlds" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(realmId)));
/* 487 */     execute(Request.delete(asciiUrl));
/*     */   }
/*     */   
/*     */   private String url(String path) throws RealmsServiceException {
/* 491 */     return url(path, null);
/*     */   }
/*     */   
/*     */   private String url(String path, String queryString) {
/* 495 */     return url(path, queryString, getFeatureFlags().contains("realms_in_aks"));
/*     */   }
/*     */   
/*     */   private static String url(String path, String queryString, boolean useAlternativeURL) {
/*     */     try {
/* 500 */       return new URI(ENVIRONMENT.protocol, useAlternativeURL ? ENVIRONMENT.alternativeUrl : ENVIRONMENT.baseUrl, "/" + path, queryString, null).toASCIIString();
/* 501 */     } catch (URISyntaxException e) {
/* 502 */       throw new IllegalArgumentException(path, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String execute(Request<?> request) throws RealmsServiceException {
/* 507 */     request.cookie("sid", this.sessionId);
/* 508 */     request.cookie("user", this.username);
/* 509 */     request.cookie("version", SharedConstants.getCurrentVersion().name());
/* 510 */     request.addSnapshotHeader(RealmsMainScreen.isSnapshot());
/*     */     
/*     */     try {
/* 513 */       int responseCode = request.responseCode();
/*     */       
/* 515 */       if (responseCode == 503 || responseCode == 277) {
/* 516 */         int pauseTime = request.getRetryAfterHeader();
/* 517 */         throw new RetryCallException(pauseTime, responseCode);
/*     */       } 
/*     */       
/* 520 */       String responseText = request.text();
/*     */       
/* 522 */       if (responseCode < 200 || responseCode >= 300) {
/* 523 */         if (responseCode == 401) {
/* 524 */           String authenticationHeader = request.getHeader("WWW-Authenticate");
/* 525 */           LOGGER.info("Could not authorize you against Realms server: {}", authenticationHeader);
/* 526 */           throw new RealmsServiceException(new RealmsError.AuthenticationError(authenticationHeader));
/*     */         } 
/*     */         
/* 529 */         String contentType = request.connection.getContentType();
/* 530 */         if (contentType != null && contentType.startsWith("text/html")) {
/* 531 */           throw new RealmsServiceException(RealmsError.CustomError.htmlPayload(responseCode, responseText));
/*     */         }
/* 533 */         RealmsError error = RealmsError.parse(responseCode, responseText);
/* 534 */         throw new RealmsServiceException(error);
/*     */       } 
/*     */       
/* 537 */       return responseText;
/* 538 */     } catch (RealmsHttpException e) {
/* 539 */       throw new RealmsServiceException(RealmsError.CustomError.connectivityError(e));
/*     */     } 
/*     */   }
/*     */   
/*     */   public enum CompatibleVersionResponse {
/* 544 */     COMPATIBLE,
/* 545 */     OUTDATED,
/* 546 */     OTHER;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/client/RealmsClient.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */