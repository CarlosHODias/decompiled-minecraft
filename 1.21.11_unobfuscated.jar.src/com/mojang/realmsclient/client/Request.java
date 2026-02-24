/*     */ package com.mojang.realmsclient.client;
/*     */ 
/*     */ import com.mojang.realmsclient.exception.RealmsHttpException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Request<T extends Request<T>>
/*     */ {
/*     */   protected HttpURLConnection connection;
/*     */   private boolean connected;
/*     */   protected String url;
/*     */   private static final int DEFAULT_READ_TIMEOUT = 60000;
/*     */   private static final int DEFAULT_CONNECT_TIMEOUT = 5000;
/*     */   private static final String IS_SNAPSHOT_KEY = "Is-Prerelease";
/*     */   private static final String COOKIE_KEY = "Cookie";
/*     */   
/*     */   public Request(String url, int connectTimeout, int readTimeout) {
/*     */     try {
/*  30 */       this.url = url;
/*  31 */       Proxy proxy = RealmsClientConfig.getProxy();
/*     */       
/*  33 */       if (proxy != null) {
/*  34 */         this.connection = (HttpURLConnection)new URL(url).openConnection(proxy);
/*     */       } else {
/*  36 */         this.connection = (HttpURLConnection)new URL(url).openConnection();
/*     */       } 
/*     */       
/*  39 */       this.connection.setConnectTimeout(connectTimeout);
/*  40 */       this.connection.setReadTimeout(readTimeout);
/*  41 */     } catch (MalformedURLException e) {
/*  42 */       throw new RealmsHttpException(e.getMessage(), e);
/*  43 */     } catch (IOException e) {
/*  44 */       throw new RealmsHttpException(e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void cookie(String key, String value) {
/*  49 */     cookie(this.connection, key, value);
/*     */   }
/*     */   
/*     */   public static void cookie(HttpURLConnection connection, String key, String value) {
/*  53 */     String cookie = connection.getRequestProperty("Cookie");
/*  54 */     if (cookie == null) {
/*  55 */       connection.setRequestProperty("Cookie", key + "=" + key);
/*     */     } else {
/*  57 */       connection.setRequestProperty("Cookie", cookie + ";" + cookie + "=" + key);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addSnapshotHeader(boolean isSnapshot) {
/*  62 */     this.connection.addRequestProperty("Is-Prerelease", String.valueOf(isSnapshot));
/*     */   }
/*     */   
/*     */   public int getRetryAfterHeader() {
/*  66 */     return getRetryAfterHeader(this.connection);
/*     */   }
/*     */   
/*     */   public static int getRetryAfterHeader(HttpURLConnection connection) {
/*  70 */     String pauseTime = connection.getHeaderField("Retry-After");
/*     */     try {
/*  72 */       return Integer.valueOf(pauseTime);
/*  73 */     } catch (Exception ignored) {
/*  74 */       return 5;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int responseCode() {
/*     */     try {
/*  80 */       connect();
/*  81 */       return this.connection.getResponseCode();
/*  82 */     } catch (Exception e) {
/*  83 */       throw new RealmsHttpException(e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */   public String text() {
/*     */     try {
/*     */       String result;
/*  89 */       connect();
/*     */ 
/*     */       
/*  92 */       if (responseCode() >= 400) {
/*  93 */         result = read(this.connection.getErrorStream());
/*     */       } else {
/*  95 */         result = read(this.connection.getInputStream());
/*     */       } 
/*     */       
/*  98 */       dispose();
/*  99 */       return result;
/* 100 */     } catch (IOException e) {
/* 101 */       throw new RealmsHttpException(e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String read(InputStream in) throws IOException {
/* 106 */     if (in == null) {
/* 107 */       return "";
/*     */     }
/* 109 */     InputStreamReader streamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
/* 110 */     StringBuilder sb = new StringBuilder();
/* 111 */     for (int x = streamReader.read(); x != -1; x = streamReader.read()) {
/* 112 */       sb.append((char)x);
/*     */     }
/*     */     
/* 115 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private void dispose() {
/* 119 */     byte[] bytes = new byte[1024];
/*     */     try {
/* 121 */       InputStream in = this.connection.getInputStream();
/* 122 */       while (in.read(bytes) > 0);
/*     */ 
/*     */       
/* 125 */       in.close();
/* 126 */     } catch (Exception ignore) {
/*     */       try {
/* 128 */         InputStream errorStream = this.connection.getErrorStream();
/* 129 */         if (errorStream == null) {
/*     */           return;
/*     */         }
/*     */         
/* 133 */         while (errorStream.read(bytes) > 0);
/*     */ 
/*     */         
/* 136 */         errorStream.close();
/* 137 */       } catch (IOException iOException) {}
/*     */     } finally {
/*     */       
/* 140 */       if (this.connection != null) {
/* 141 */         this.connection.disconnect();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected T connect() {
/* 148 */     if (this.connected) {
/* 149 */       return (T)this;
/*     */     }
/* 151 */     T t = doConnect();
/* 152 */     this.connected = true;
/* 153 */     return t;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract T doConnect();
/*     */   
/*     */   public static Request<?> get(String url) {
/* 160 */     return new Get(url, 5000, 60000);
/*     */   }
/*     */   
/*     */   public static Request<?> get(String url, int connectTimeoutMillis, int readTimeoutMillis) {
/* 164 */     return new Get(url, connectTimeoutMillis, readTimeoutMillis);
/*     */   }
/*     */   
/*     */   public static Request<?> post(String uri, String content) {
/* 168 */     return new Post(uri, content, 5000, 60000);
/*     */   }
/*     */   
/*     */   public static Request<?> post(String uri, String content, int connectTimeoutMillis, int readTimeoutMillis) {
/* 172 */     return new Post(uri, content, connectTimeoutMillis, readTimeoutMillis);
/*     */   }
/*     */   
/*     */   public static Request<?> delete(String url) {
/* 176 */     return new Delete(url, 5000, 60000);
/*     */   }
/*     */   
/*     */   public static Request<?> put(String url, String content) {
/* 180 */     return new Put(url, content, 5000, 60000);
/*     */   }
/*     */   
/*     */   public static Request<?> put(String url, String content, int connectTimeoutMillis, int readTimeoutMillis) {
/* 184 */     return new Put(url, content, connectTimeoutMillis, readTimeoutMillis);
/*     */   }
/*     */   
/*     */   public String getHeader(String header) {
/* 188 */     return getHeader(this.connection, header);
/*     */   }
/*     */   
/*     */   public static String getHeader(HttpURLConnection connection, String header) {
/*     */     try {
/* 193 */       return connection.getHeaderField(header);
/* 194 */     } catch (Exception ignored) {
/* 195 */       return "";
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class Delete extends Request<Delete> {
/*     */     public Delete(String uri, int connectTimeout, int readTimeout) {
/* 201 */       super(uri, connectTimeout, readTimeout);
/*     */     }
/*     */ 
/*     */     
/*     */     public Delete doConnect() {
/*     */       try {
/* 207 */         this.connection.setDoOutput(true);
/* 208 */         this.connection.setRequestMethod("DELETE");
/* 209 */         this.connection.connect();
/* 210 */         return this;
/* 211 */       } catch (Exception e) {
/* 212 */         throw new RealmsHttpException(e.getMessage(), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Get extends Request<Get> {
/*     */     public Get(String uri, int connectTimeout, int readTimeout) {
/* 219 */       super(uri, connectTimeout, readTimeout);
/*     */     }
/*     */ 
/*     */     
/*     */     public Get doConnect() {
/*     */       try {
/* 225 */         this.connection.setDoInput(true);
/* 226 */         this.connection.setDoOutput(true);
/* 227 */         this.connection.setUseCaches(false);
/* 228 */         this.connection.setRequestMethod("GET");
/* 229 */         return this;
/* 230 */       } catch (Exception e) {
/* 231 */         throw new RealmsHttpException(e.getMessage(), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Put extends Request<Put> {
/*     */     private final String content;
/*     */     
/*     */     public Put(String uri, String content, int connectTimeout, int readTimeout) {
/* 240 */       super(uri, connectTimeout, readTimeout);
/* 241 */       this.content = content;
/*     */     }
/*     */ 
/*     */     
/*     */     public Put doConnect() {
/*     */       try {
/* 247 */         if (this.content != null) {
/* 248 */           this.connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
/*     */         }
/*     */         
/* 251 */         this.connection.setDoOutput(true);
/* 252 */         this.connection.setDoInput(true);
/* 253 */         this.connection.setRequestMethod("PUT");
/* 254 */         OutputStream out = this.connection.getOutputStream();
/* 255 */         OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
/* 256 */         writer.write(this.content);
/* 257 */         writer.close();
/* 258 */         out.flush();
/* 259 */         return this;
/* 260 */       } catch (Exception e) {
/* 261 */         throw new RealmsHttpException(e.getMessage(), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Post extends Request<Post> {
/*     */     private final String content;
/*     */     
/*     */     public Post(String uri, String content, int connectTimeout, int readTimeout) {
/* 270 */       super(uri, connectTimeout, readTimeout);
/* 271 */       this.content = content;
/*     */     }
/*     */ 
/*     */     
/*     */     public Post doConnect() {
/*     */       try {
/* 277 */         if (this.content != null) {
/* 278 */           this.connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
/*     */         }
/*     */         
/* 281 */         this.connection.setDoInput(true);
/* 282 */         this.connection.setDoOutput(true);
/* 283 */         this.connection.setUseCaches(false);
/* 284 */         this.connection.setRequestMethod("POST");
/* 285 */         OutputStream out = this.connection.getOutputStream();
/* 286 */         OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
/* 287 */         writer.write(this.content);
/* 288 */         writer.close();
/* 289 */         out.flush();
/* 290 */         return this;
/* 291 */       } catch (Exception e) {
/* 292 */         throw new RealmsHttpException(e.getMessage(), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/client/Request.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */