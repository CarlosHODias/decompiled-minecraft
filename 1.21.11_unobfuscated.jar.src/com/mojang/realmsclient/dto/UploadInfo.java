/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.net.URI;
/*    */ 
/*    */ public final class UploadInfo extends Record {
/*    */   private final boolean worldClosed;
/*    */   private final String token;
/*    */   private final URI uploadEndpoint;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/UploadInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/UploadInfo;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/UploadInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/UploadInfo;
/*    */   }
/*    */   
/* 19 */   public UploadInfo(boolean worldClosed, String token, URI uploadEndpoint) { this.worldClosed = worldClosed; this.token = token; this.uploadEndpoint = uploadEndpoint; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/UploadInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/UploadInfo;
/* 19 */     //   0	8	1	o	Ljava/lang/Object; } public boolean worldClosed() { return this.worldClosed; } public String token() { return this.token; } public URI uploadEndpoint() { return this.uploadEndpoint; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*    */   
/*    */   private static final String DEFAULT_SCHEMA = "http://";
/*    */   
/*    */   private static final int DEFAULT_PORT = 8080;
/* 29 */   private static final java.util.regex.Pattern URI_SCHEMA_PATTERN = java.util.regex.Pattern.compile("^[a-zA-Z][-a-zA-Z0-9+.]+:");
/*    */   
/*    */   public static UploadInfo parse(String json) {
/*    */     try {
/* 33 */       JsonObject jsonObject = net.minecraft.util.LenientJsonParser.parse(json).getAsJsonObject();
/* 34 */       String endpointStr = com.mojang.realmsclient.util.JsonUtils.getStringOr("uploadEndpoint", jsonObject, null);
/* 35 */       if (endpointStr != null) {
/* 36 */         int endpointPort = com.mojang.realmsclient.util.JsonUtils.getIntOr("port", jsonObject, -1);
/* 37 */         URI uploadEndpoint = assembleUri(endpointStr, endpointPort);
/* 38 */         if (uploadEndpoint != null) {
/* 39 */           boolean worldClosed = com.mojang.realmsclient.util.JsonUtils.getBooleanOr("worldClosed", jsonObject, false);
/* 40 */           String token = com.mojang.realmsclient.util.JsonUtils.getStringOr("token", jsonObject, null);
/* 41 */           return new UploadInfo(worldClosed, token, uploadEndpoint);
/*    */         } 
/*    */       } 
/* 44 */     } catch (Exception e) {
/* 45 */       LOGGER.error("Could not parse UploadInfo", e);
/*    */     } 
/*    */     
/* 48 */     return null;
/*    */   }
/*    */   
/*    */   @com.google.common.annotations.VisibleForTesting
/*    */   public static URI assembleUri(String endpoint, int portOverride) {
/* 53 */     java.util.regex.Matcher matcher = URI_SCHEMA_PATTERN.matcher(endpoint);
/*    */     
/* 55 */     String endpointWithSchema = ensureEndpointSchema(endpoint, matcher);
/*    */     try {
/* 57 */       URI result = new URI(endpointWithSchema);
/* 58 */       int selectedPort = selectPortOrDefault(portOverride, result.getPort());
/* 59 */       if (selectedPort != result.getPort()) {
/* 60 */         return new URI(result.getScheme(), result.getUserInfo(), result.getHost(), selectedPort, result.getPath(), result.getQuery(), result.getFragment());
/*    */       }
/* 62 */       return result;
/* 63 */     } catch (java.net.URISyntaxException e) {
/* 64 */       LOGGER.warn("Failed to parse URI {}", endpointWithSchema, e);
/*    */ 
/*    */       
/* 67 */       return null;
/*    */     } 
/*    */   }
/*    */   private static int selectPortOrDefault(int portOverride, int parsedPort) {
/* 71 */     if (portOverride != -1) {
/* 72 */       return portOverride;
/*    */     }
/* 74 */     if (parsedPort != -1) {
/* 75 */       return parsedPort;
/*    */     }
/* 77 */     return 8080;
/*    */   }
/*    */   
/*    */   private static String ensureEndpointSchema(String endpoint, java.util.regex.Matcher matcher) {
/* 81 */     if (matcher.find()) {
/* 82 */       return endpoint;
/*    */     }
/* 84 */     return "http://" + endpoint;
/*    */   }
/*    */ 
/*    */   
/*    */   public static String createRequest(String uploadToken) {
/* 89 */     JsonObject request = new JsonObject();
/* 90 */     if (uploadToken != null) {
/* 91 */       request.addProperty("token", uploadToken);
/*    */     }
/* 93 */     return request.toString();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/UploadInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */