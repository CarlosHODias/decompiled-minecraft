/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.internal.Streams;
/*     */ import com.google.gson.stream.JsonWriter;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.network.chat.FilterMask;
/*     */ import net.minecraft.server.dedicated.DedicatedServerProperties;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.LenientJsonParser;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.thread.ConsecutiveExecutor;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class ServerTextFilter implements AutoCloseable {
/*  39 */   protected static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  41 */   private static final AtomicInteger WORKER_COUNT = new AtomicInteger(1); private static final ThreadFactory THREAD_FACTORY; private final URL chatEndpoint; private final MessageEncoder chatEncoder; private final IgnoreStrategy chatIgnoreStrategy; private final ExecutorService workerPool; static {
/*  42 */     THREAD_FACTORY = (runnable -> {
/*     */         Thread thread = new Thread(runnable);
/*     */         thread.setName("Chat-Filter-Worker-" + WORKER_COUNT.getAndIncrement());
/*     */         return thread;
/*     */       });
/*     */   }
/*     */   protected static ExecutorService createWorkerPool(int maxConcurrentRequests) {
/*  49 */     return Executors.newFixedThreadPool(maxConcurrentRequests, THREAD_FACTORY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ServerTextFilter(URL chatEndpoint, MessageEncoder chatEncoder, IgnoreStrategy chatIgnoreStrategy, ExecutorService workerPool) {
/*  66 */     this.chatIgnoreStrategy = chatIgnoreStrategy;
/*     */     
/*  68 */     this.workerPool = workerPool;
/*  69 */     this.chatEndpoint = chatEndpoint;
/*  70 */     this.chatEncoder = chatEncoder;
/*     */   }
/*     */   
/*     */   protected static URL getEndpoint(URI host, JsonObject source, String id, String def) throws MalformedURLException {
/*  74 */     String endpointConfig = getEndpointFromConfig(source, id, def);
/*  75 */     return host.resolve("/" + endpointConfig).toURL();
/*     */   }
/*     */   
/*     */   protected static String getEndpointFromConfig(JsonObject source, String id, String def) {
/*  79 */     return (source != null) ? GsonHelper.getAsString(source, id, def) : def;
/*     */   }
/*     */   
/*     */   public static ServerTextFilter createFromConfig(DedicatedServerProperties config) {
/*  83 */     String textFilteringConfig = config.textFilteringConfig;
/*  84 */     if (StringUtil.isBlank(textFilteringConfig)) {
/*  85 */       return null;
/*     */     }
/*  87 */     switch (config.textFilteringVersion) { case 0: 
/*     */       case 1:
/*     */       
/*     */       default:
/*  91 */         LOGGER.warn("Could not create text filter - unsupported text filtering version used"); break; }
/*  92 */      return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected CompletableFuture<FilteredText> requestMessageProcessing(GameProfile sender, String message, IgnoreStrategy ignoreStrategy, Executor executor) {
/*  98 */     if (message.isEmpty()) {
/*  99 */       return CompletableFuture.completedFuture(FilteredText.EMPTY);
/*     */     }
/* 101 */     return CompletableFuture.supplyAsync(() -> {
/*     */           JsonObject object = this.chatEncoder.encode(sender, message);
/*     */           try {
/*     */             JsonObject result = processRequestResponse(object, this.chatEndpoint);
/*     */             return filterText(message, ignoreStrategy, result);
/* 106 */           } catch (Exception e) {
/*     */             LOGGER.warn("Failed to validate message '{}'", message, e);
/*     */             return FilteredText.fullyFiltered(message);
/*     */           } 
/*     */         }, executor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FilterMask parseMask(String message, JsonArray removedChars, IgnoreStrategy ignoreStrategy) {
/* 116 */     if (removedChars.isEmpty())
/* 117 */       return FilterMask.PASS_THROUGH; 
/* 118 */     if (ignoreStrategy.shouldIgnore(message, removedChars.size())) {
/* 119 */       return FilterMask.FULLY_FILTERED;
/*     */     }
/* 121 */     FilterMask mask = new FilterMask(message.length());
/* 122 */     for (int i = 0; i < removedChars.size(); i++) {
/* 123 */       mask.setFiltered(removedChars.get(i).getAsInt());
/*     */     }
/* 125 */     return mask;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 130 */     this.workerPool.shutdownNow();
/*     */   }
/*     */   
/*     */   protected void drainStream(InputStream input) throws IOException {
/* 134 */     byte[] trashcan = new byte[1024];
/* 135 */     while (input.read(trashcan) != -1);
/*     */   }
/*     */ 
/*     */   
/*     */   private JsonObject processRequestResponse(JsonObject payload, URL url) throws IOException {
/* 140 */     HttpURLConnection connection = makeRequest(payload, url);
/*     */     
/* 142 */     InputStream is = connection.getInputStream(); 
/* 143 */     try { if (connection.getResponseCode() == 204)
/* 144 */       { JsonObject jsonObject = new JsonObject();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 151 */         if (is != null) is.close();  return jsonObject; }  try { JsonObject jsonObject = LenientJsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8)).getAsJsonObject(); drainStream(is); return jsonObject; } finally { drainStream(is); }  } catch (Throwable throwable) { if (is != null)
/*     */         try { is.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 155 */      } protected HttpURLConnection makeRequest(JsonObject payload, URL url) throws IOException { HttpURLConnection connection = getURLConnection(url);
/*     */     
/* 157 */     setAuthorizationProperty(connection);
/*     */     
/* 159 */     OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8); 
/* 160 */     try { JsonWriter jsonWriter = new JsonWriter(writer); 
/* 161 */       try { Streams.write((JsonElement)payload, jsonWriter);
/* 162 */         jsonWriter.close(); } catch (Throwable throwable) { try { jsonWriter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/* 163 */        writer.close(); } catch (Throwable throwable) { try { writer.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 165 */      int responseCode = connection.getResponseCode();
/* 166 */     if (responseCode < 200 || responseCode >= 300) {
/* 167 */       throw new RequestFailedException("" + responseCode + " " + responseCode);
/*     */     }
/* 169 */     return connection; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int connectionReadTimeout() {
/* 175 */     return 2000;
/*     */   }
/*     */   
/*     */   protected HttpURLConnection getURLConnection(URL url) throws IOException {
/* 179 */     HttpURLConnection connection = (HttpURLConnection)url.openConnection();
/* 180 */     connection.setConnectTimeout(15000);
/* 181 */     connection.setReadTimeout(connectionReadTimeout());
/* 182 */     connection.setUseCaches(false);
/* 183 */     connection.setDoOutput(true);
/* 184 */     connection.setDoInput(true);
/*     */     
/* 186 */     connection.setRequestMethod("POST");
/* 187 */     connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
/* 188 */     connection.setRequestProperty("Accept", "application/json");
/* 189 */     connection.setRequestProperty("User-Agent", "Minecraft server" + SharedConstants.getCurrentVersion().name());
/*     */     
/* 191 */     return connection;
/*     */   }
/*     */   
/*     */   public TextFilter createContext(GameProfile gameProfile) {
/* 195 */     return new PlayerContext(gameProfile);
/*     */   }
/*     */   protected abstract FilteredText filterText(String paramString, IgnoreStrategy paramIgnoreStrategy, JsonObject paramJsonObject);
/*     */   protected abstract void setAuthorizationProperty(HttpURLConnection paramHttpURLConnection);
/*     */   protected static class RequestFailedException extends RuntimeException { protected RequestFailedException(String message) {
/* 200 */       super(message);
/*     */     } }
/*     */ 
/*     */   
/*     */   protected class PlayerContext implements TextFilter {
/*     */     protected final GameProfile profile;
/*     */     protected final Executor streamExecutor;
/*     */     
/*     */     protected PlayerContext(GameProfile profile) {
/* 209 */       this.profile = profile;
/* 210 */       ConsecutiveExecutor streamProcessor = new ConsecutiveExecutor(ServerTextFilter.this.workerPool, "chat stream for " + profile.name());
/* 211 */       Objects.requireNonNull(streamProcessor); this.streamExecutor = streamProcessor::schedule;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public CompletableFuture<List<FilteredText>> processMessageBundle(List<String> messages) {
/* 217 */       List<CompletableFuture<FilteredText>> requests = (List<CompletableFuture<FilteredText>>)messages.stream()
/* 218 */         .map(message -> ServerTextFilter.this.requestMessageProcessing(this.profile, message, ServerTextFilter.this.chatIgnoreStrategy, this.streamExecutor))
/* 219 */         .collect(ImmutableList.toImmutableList());
/*     */       
/* 221 */       return Util.sequenceFailFast(requests)
/*     */         
/* 223 */         .exceptionally(e -> ImmutableList.of());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public CompletableFuture<FilteredText> processStreamMessage(String message) {
/* 229 */       return ServerTextFilter.this.requestMessageProcessing(this.profile, message, ServerTextFilter.this.chatIgnoreStrategy, this.streamExecutor);
/*     */     } }
/*     */   @FunctionalInterface
/*     */   public static interface IgnoreStrategy { public static final IgnoreStrategy NEVER_IGNORE = (message, removedCharCount) -> false;
/*     */     public static final IgnoreStrategy IGNORE_FULLY_FILTERED;
/*     */     
/*     */     static {
/* 236 */       IGNORE_FULLY_FILTERED = ((message, removedCharCount) -> (message.length() == removedCharCount));
/*     */     }
/*     */     static IgnoreStrategy ignoreOverThreshold(int threshold) {
/* 239 */       return (message, removedCharCount) -> (removedCharCount >= threshold);
/*     */     }
/*     */     
/*     */     static IgnoreStrategy select(int hashesToDrop) {
/* 243 */       switch (hashesToDrop) { case -1: case 0: default: break; }  return 
/*     */ 
/*     */         
/* 246 */         ignoreOverThreshold(hashesToDrop);
/*     */     }
/*     */     
/*     */     boolean shouldIgnore(String param1String, int param1Int); }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   protected static interface MessageEncoder {
/*     */     JsonObject encode(GameProfile param1GameProfile, String param1String);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/network/ServerTextFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */