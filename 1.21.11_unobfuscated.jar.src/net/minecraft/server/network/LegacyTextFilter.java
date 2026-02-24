/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Base64;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import net.minecraft.network.chat.FilterMask;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LegacyTextFilter
/*     */   extends ServerTextFilter
/*     */ {
/*     */   private static final String ENDPOINT = "v1/chat";
/*     */   private final URL joinEndpoint;
/*     */   private final JoinOrLeaveEncoder joinEncoder;
/*     */   private final URL leaveEndpoint;
/*     */   private final JoinOrLeaveEncoder leaveEncoder;
/*     */   private final String authKey;
/*     */   
/*     */   private LegacyTextFilter(URL chatEndpoint, ServerTextFilter.MessageEncoder chatEncoder, URL joinEndpoint, JoinOrLeaveEncoder joinEncoder, URL leaveEndpoint, JoinOrLeaveEncoder leaveEncoder, String authKey, ServerTextFilter.IgnoreStrategy chatIgnoreStrategy, ExecutorService workerPool) {
/*  35 */     super(chatEndpoint, chatEncoder, chatIgnoreStrategy, workerPool);
/*     */     
/*  37 */     this.joinEndpoint = joinEndpoint;
/*  38 */     this.joinEncoder = joinEncoder;
/*  39 */     this.leaveEndpoint = leaveEndpoint;
/*  40 */     this.leaveEncoder = leaveEncoder;
/*  41 */     this.authKey = authKey;
/*     */   }
/*     */   public static ServerTextFilter createTextFilterFromConfig(String config) {
/*     */     try {
/*     */       ServerTextFilter.MessageEncoder chatEncoder;
/*  46 */       JsonObject parsedConfig = GsonHelper.parse(config);
/*     */       
/*  48 */       URI host = new URI(GsonHelper.getAsString(parsedConfig, "apiServer"));
/*  49 */       String key = GsonHelper.getAsString(parsedConfig, "apiKey");
/*  50 */       if (key.isEmpty()) {
/*  51 */         throw new IllegalArgumentException("Missing API key");
/*     */       }
/*  53 */       int ruleId = GsonHelper.getAsInt(parsedConfig, "ruleId", 1);
/*  54 */       String serverId = GsonHelper.getAsString(parsedConfig, "serverId", "");
/*  55 */       String roomId = GsonHelper.getAsString(parsedConfig, "roomId", "Java:Chat");
/*  56 */       int hashesToDrop = GsonHelper.getAsInt(parsedConfig, "hashesToDrop", -1);
/*     */       
/*  58 */       int maxConcurrentRequests = GsonHelper.getAsInt(parsedConfig, "maxConcurrentRequests", 7);
/*     */       
/*  60 */       JsonObject endpoints = GsonHelper.getAsJsonObject(parsedConfig, "endpoints", null);
/*  61 */       String chatEndpointConfig = getEndpointFromConfig(endpoints, "chat", "v1/chat");
/*  62 */       boolean isLegacyChatEndpoint = chatEndpointConfig.equals("v1/chat");
/*  63 */       URL chatEndpoint = host.resolve("/" + chatEndpointConfig).toURL();
/*  64 */       URL joinEndpoint = getEndpoint(host, endpoints, "join", "v1/join");
/*  65 */       URL leaveEndpoint = getEndpoint(host, endpoints, "leave", "v1/leave");
/*     */       
/*     */       JoinOrLeaveEncoder commonJoinOrLeaveEncoder = user -> {
/*     */           JsonObject object = new JsonObject();
/*     */           
/*     */           object.addProperty("server", serverId);
/*     */           
/*     */           object.addProperty("room", roomId);
/*     */           object.addProperty("user_id", user.id().toString());
/*     */           object.addProperty("user_display_name", user.name());
/*     */           return object;
/*     */         };
/*  77 */       if (isLegacyChatEndpoint) {
/*  78 */         chatEncoder = ((sender, message) -> {
/*     */             JsonObject object = new JsonObject();
/*     */             object.addProperty("rule", ruleId);
/*     */             object.addProperty("server", serverId);
/*     */             object.addProperty("room", roomId);
/*     */             object.addProperty("player", sender.id().toString());
/*     */             object.addProperty("player_display_name", sender.name());
/*     */             object.addProperty("text", message);
/*     */             object.addProperty("language", "*");
/*     */             return object;
/*     */           });
/*     */       } else {
/*  90 */         String ruleIdStr = String.valueOf(ruleId);
/*  91 */         chatEncoder = ((sender, message) -> {
/*     */             JsonObject object = new JsonObject();
/*     */             
/*     */             object.addProperty("rule_id", ruleIdStr);
/*     */             object.addProperty("category", serverId);
/*     */             object.addProperty("subcategory", roomId);
/*     */             object.addProperty("user_id", sender.id().toString());
/*     */             object.addProperty("user_display_name", sender.name());
/*     */             object.addProperty("text", message);
/*     */             object.addProperty("language", "*");
/*     */             return object;
/*     */           });
/*     */       } 
/* 104 */       ServerTextFilter.IgnoreStrategy ignoreStrategy = ServerTextFilter.IgnoreStrategy.select(hashesToDrop);
/* 105 */       ExecutorService workerPool = createWorkerPool(maxConcurrentRequests);
/*     */       
/* 107 */       String encodedKey = Base64.getEncoder().encodeToString(key.getBytes(StandardCharsets.US_ASCII));
/* 108 */       return new LegacyTextFilter(chatEndpoint, chatEncoder, joinEndpoint, commonJoinOrLeaveEncoder, leaveEndpoint, commonJoinOrLeaveEncoder, encodedKey, ignoreStrategy, workerPool);
/* 109 */     } catch (Exception e) {
/* 110 */       LOGGER.warn("Failed to parse chat filter config {}", config, e);
/*     */ 
/*     */       
/* 113 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public TextFilter createContext(GameProfile gameProfile) {
/* 118 */     return new ServerTextFilter.PlayerContext(gameProfile)
/*     */       {
/*     */         public void join() {
/* 121 */           LegacyTextFilter.this.processJoinOrLeave(this.profile, LegacyTextFilter.this.joinEndpoint, LegacyTextFilter.this.joinEncoder, this.streamExecutor);
/*     */         }
/*     */ 
/*     */         
/*     */         public void leave() {
/* 126 */           LegacyTextFilter.this.processJoinOrLeave(this.profile, LegacyTextFilter.this.leaveEndpoint, LegacyTextFilter.this.leaveEncoder, this.streamExecutor);
/*     */         }
/*     */       };
/*     */   } @FunctionalInterface
/*     */   private static interface JoinOrLeaveEncoder {
/*     */     JsonObject encode(GameProfile param1GameProfile); }
/*     */   private void processJoinOrLeave(GameProfile user, URL endpoint, JoinOrLeaveEncoder encoder, Executor executor) {
/* 133 */     executor.execute(() -> {
/*     */           JsonObject object = encoder.encode(user);
/*     */           try {
/*     */             processRequest(object, endpoint);
/* 137 */           } catch (Exception e) {
/*     */             LOGGER.warn("Failed to send join/leave packet to {} for player {}", new Object[] { endpoint, user, e });
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private void processRequest(JsonObject payload, URL url) throws IOException {
/* 144 */     HttpURLConnection connection = makeRequest(payload, url);
/*     */     
/* 146 */     InputStream is = connection.getInputStream(); 
/* 147 */     try { drainStream(is);
/* 148 */       if (is != null) is.close();  }
/*     */     catch (Throwable throwable) { if (is != null)
/*     */         try { is.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 153 */      } protected void setAuthorizationProperty(HttpURLConnection connection) { connection.setRequestProperty("Authorization", "Basic " + this.authKey); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FilteredText filterText(String message, ServerTextFilter.IgnoreStrategy ignoreStrategy, JsonObject result) {
/* 158 */     boolean response = GsonHelper.getAsBoolean(result, "response", false);
/* 159 */     if (response) {
/* 160 */       return FilteredText.passThrough(message);
/*     */     }
/* 162 */     String filteredMessage = GsonHelper.getAsString(result, "hashed", null);
/* 163 */     if (filteredMessage == null) {
/* 164 */       return FilteredText.fullyFiltered(message);
/*     */     }
/*     */     
/* 167 */     JsonArray removedChars = GsonHelper.getAsJsonArray(result, "hashes");
/* 168 */     FilterMask mask = parseMask(message, removedChars, ignoreStrategy);
/* 169 */     return new FilteredText(message, mask);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/network/LegacyTextFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */