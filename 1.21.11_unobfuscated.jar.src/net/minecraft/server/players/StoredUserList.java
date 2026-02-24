/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.io.Files;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.server.notifications.NotificationService;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public abstract class StoredUserList<K, V extends StoredUserEntry<K>>
/*     */ {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  30 */   private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
/*     */   
/*     */   private final File file;
/*  33 */   private final Map<String, V> map = Maps.newHashMap();
/*     */   protected final NotificationService notificationService;
/*     */   
/*     */   public StoredUserList(File file, NotificationService notificationService) {
/*  37 */     this.file = file;
/*  38 */     this.notificationService = notificationService;
/*     */   }
/*     */   
/*     */   public File getFile() {
/*  42 */     return this.file;
/*     */   }
/*     */   
/*     */   public boolean add(V infos) {
/*  46 */     String keyForUser = getKeyForUser(infos.getUser());
/*  47 */     StoredUserEntry storedUserEntry = (StoredUserEntry)this.map.get(keyForUser);
/*  48 */     if (infos.equals(storedUserEntry)) {
/*  49 */       return false;
/*     */     }
/*  51 */     this.map.put(keyForUser, infos);
/*     */     try {
/*  53 */       save();
/*  54 */     } catch (IOException e) {
/*  55 */       LOGGER.warn("Could not save the list after adding a user.", e);
/*     */     } 
/*  57 */     return true;
/*     */   }
/*     */   
/*     */   public V get(K user) {
/*  61 */     removeExpired();
/*  62 */     return this.map.get(getKeyForUser(user));
/*     */   }
/*     */   
/*     */   public boolean remove(K user) {
/*  66 */     StoredUserEntry storedUserEntry = (StoredUserEntry)this.map.remove(getKeyForUser(user));
/*  67 */     if (storedUserEntry == null) {
/*  68 */       return false;
/*     */     }
/*     */     try {
/*  71 */       save();
/*  72 */     } catch (IOException e) {
/*  73 */       LOGGER.warn("Could not save the list after removing a user.", e);
/*     */     } 
/*  75 */     return true;
/*     */   }
/*     */   
/*     */   public boolean remove(StoredUserEntry<K> infos) {
/*  79 */     return remove(Objects.requireNonNull(infos.getUser()));
/*     */   }
/*     */   
/*     */   public void clear() {
/*  83 */     this.map.clear();
/*     */     try {
/*  85 */       save();
/*  86 */     } catch (IOException e) {
/*  87 */       LOGGER.warn("Could not save the list after removing a user.", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String[] getUserList() {
/*  92 */     return (String[])this.map.keySet().toArray((Object[])new String[0]);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  96 */     return this.map.isEmpty();
/*     */   }
/*     */   
/*     */   protected String getKeyForUser(K user) {
/* 100 */     return user.toString();
/*     */   }
/*     */   
/*     */   protected boolean contains(K user) {
/* 104 */     return this.map.containsKey(getKeyForUser(user));
/*     */   }
/*     */   
/*     */   private void removeExpired() {
/* 108 */     List<K> toRemove = Lists.newArrayList();
/* 109 */     for (StoredUserEntry<K> storedUserEntry : this.map.values()) {
/* 110 */       if (storedUserEntry.hasExpired()) {
/* 111 */         toRemove.add(storedUserEntry.getUser());
/*     */       }
/*     */     } 
/* 114 */     for (K user : toRemove) {
/* 115 */       this.map.remove(getKeyForUser(user));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> getEntries() {
/* 122 */     return this.map.values();
/*     */   }
/*     */   
/*     */   public void save() throws IOException {
/* 126 */     JsonArray result = new JsonArray();
/* 127 */     Objects.requireNonNull(result); this.map.values().stream().map(entry -> { Objects.requireNonNull(entry); return Util.make(new JsonObject(), entry::serialize); }).forEach(result::add);
/* 128 */     BufferedWriter writer = Files.newWriter(this.file, StandardCharsets.UTF_8); 
/* 129 */     try { GSON.toJson((JsonElement)result, GSON.newJsonWriter(writer));
/* 130 */       if (writer != null) writer.close();  }
/*     */     catch (Throwable throwable) { if (writer != null)
/*     */         try { writer.close(); }
/*     */         catch (Throwable throwable1)
/*     */         { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 136 */      } public void load() throws IOException { if (!this.file.exists()) {
/*     */       return;
/*     */     }
/* 139 */     BufferedReader reader = Files.newReader(this.file, StandardCharsets.UTF_8); try {
/* 140 */       this.map.clear();
/* 141 */       JsonArray contents = (JsonArray)GSON.fromJson(reader, JsonArray.class);
/* 142 */       if (contents == null)
/*     */       
/*     */       { 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 152 */         if (reader != null) reader.close();  return; }  for (JsonElement element : (Iterable<JsonElement>)contents) { JsonObject object = GsonHelper.convertToJsonObject(element, "entry"); StoredUserEntry<K> entry = createEntry(object); if (entry.getUser() != null) this.map.put(getKeyForUser(entry.getUser()), (V)entry);  }  if (reader != null) reader.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (reader != null)
/*     */         try {
/*     */           reader.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     }  }
/*     */ 
/*     */   
/*     */   protected abstract StoredUserEntry<K> createEntry(JsonObject paramJsonObject);
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/players/StoredUserList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */