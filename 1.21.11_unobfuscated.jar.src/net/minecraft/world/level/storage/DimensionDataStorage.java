/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtAccounter;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.util.FastBufferedInputStream;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DimensionDataStorage implements AutoCloseable {
/*  41 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  43 */   private final Map<SavedDataType<?>, Optional<SavedData>> cache = new HashMap<>();
/*     */   
/*     */   private final DataFixer fixerUpper;
/*     */   private final HolderLookup.Provider registries;
/*     */   private final Path dataFolder;
/*  48 */   private CompletableFuture<?> pendingWriteFuture = CompletableFuture.completedFuture(null);
/*     */   
/*     */   public DimensionDataStorage(Path dataFolder, DataFixer fixerUpper, HolderLookup.Provider registries) {
/*  51 */     this.fixerUpper = fixerUpper;
/*  52 */     this.dataFolder = dataFolder;
/*  53 */     this.registries = registries;
/*     */   }
/*     */   
/*     */   private Path getDataFile(String id) {
/*  57 */     return this.dataFolder.resolve(id + ".dat");
/*     */   }
/*     */   
/*     */   public <T extends SavedData> T computeIfAbsent(SavedDataType<T> type) {
/*  61 */     T data = get(type);
/*  62 */     if (data != null) {
/*  63 */       return data;
/*     */     }
/*  65 */     SavedData savedData = type.constructor().get();
/*  66 */     set(type, (T)savedData);
/*  67 */     return (T)savedData;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends SavedData> T get(SavedDataType<T> type) {
/*  72 */     Optional<SavedData> data = this.cache.get(type);
/*  73 */     if (data == null) {
/*  74 */       data = Optional.ofNullable(readSavedData(type));
/*  75 */       this.cache.put(type, data);
/*     */     } 
/*  77 */     return (T)data.orElse(null);
/*     */   }
/*     */   
/*     */   private <T extends SavedData> T readSavedData(SavedDataType<T> type) {
/*     */     try {
/*  82 */       Path file = getDataFile(type.id());
/*  83 */       if (Files.exists(file, new java.nio.file.LinkOption[0])) {
/*  84 */         CompoundTag tag = readTagFromDisk(type.id(), type.dataFixType(), SharedConstants.getCurrentVersion().dataVersion().version());
/*  85 */         RegistryOps<Tag> ops = this.registries.createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/*  86 */         return (T)type.codec().parse((DynamicOps)ops, tag.get("data"))
/*  87 */           .resultOrPartial(error -> LOGGER.error("Failed to parse saved data for '{}': {}", type, error))
/*  88 */           .orElse(null);
/*     */       } 
/*  90 */     } catch (Exception e) {
/*  91 */       LOGGER.error("Error loading saved data: {}", type, e);
/*     */     } 
/*  93 */     return null;
/*     */   }
/*     */   
/*     */   public <T extends SavedData> void set(SavedDataType<T> type, T data) {
/*  97 */     this.cache.put(type, Optional.of((SavedData)data));
/*  98 */     data.setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag readTagFromDisk(String id, DataFixTypes type, int newVersion) throws IOException {
/* 103 */     InputStream in = Files.newInputStream(getDataFile(id), new java.nio.file.OpenOption[0]); 
/* 104 */     try { PushbackInputStream inputStream = new PushbackInputStream((InputStream)new FastBufferedInputStream(in), 2);
/*     */       
/*     */       try { CompoundTag tag;
/* 107 */         if (isGzip(inputStream)) {
/* 108 */           tag = NbtIo.readCompressed(inputStream, NbtAccounter.unlimitedHeap());
/*     */         } else {
/* 110 */           DataInputStream dis = new DataInputStream(inputStream); 
/* 111 */           try { tag = NbtIo.read(dis);
/* 112 */             dis.close(); } catch (Throwable throwable) { try { dis.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */              throw throwable; }
/*     */         
/* 115 */         }  int version = NbtUtils.getDataVersion(tag, 1343);
/* 116 */         CompoundTag compoundTag1 = type.update(this.fixerUpper, tag, version, newVersion);
/* 117 */         inputStream.close(); if (in != null) in.close();  return compoundTag1; } catch (Throwable tag) { try { inputStream.close(); } catch (Throwable throwable) { tag.addSuppressed(throwable); }  throw tag; }  } catch (Throwable throwable) { if (in != null)
/*     */         try { in.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 121 */      } private boolean isGzip(PushbackInputStream inputStream) throws IOException { byte[] header = new byte[2];
/*     */     boolean gzip = false;
/* 123 */     int read = inputStream.read(header, 0, 2);
/* 124 */     if (read == 2) {
/* 125 */       int fullHeader = (header[1] & 0xFF) << 8 | header[0] & 0xFF;
/* 126 */       if (fullHeader == 35615) {
/* 127 */         gzip = true;
/*     */       }
/*     */     } 
/* 130 */     if (read != 0) {
/* 131 */       inputStream.unread(header, 0, read);
/*     */     }
/* 133 */     return gzip; }
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<?> scheduleSave() {
/* 138 */     Map<SavedDataType<?>, CompoundTag> tagsToSave = collectDirtyTagsToSave();
/* 139 */     if (tagsToSave.isEmpty()) {
/* 140 */       return CompletableFuture.completedFuture(null);
/*     */     }
/*     */     
/* 143 */     int threads = Util.maxAllowedExecutorThreads();
/* 144 */     int taskCount = tagsToSave.size();
/*     */ 
/*     */     
/* 147 */     if (taskCount > threads) {
/*     */       
/* 149 */       this.pendingWriteFuture = this.pendingWriteFuture.thenCompose(ignored -> {
/*     */             List<CompletableFuture<?>> tasks = new ArrayList<>(threads);
/*     */ 
/*     */             
/*     */             int bucketSize = Mth.positiveCeilDiv(threads, threads);
/*     */             
/*     */             for (List<Map.Entry<SavedDataType<?>, CompoundTag>> entries : (Iterable<List<Map.Entry<SavedDataType<?>, CompoundTag>>>)Iterables.partition(taskCount.entrySet(), bucketSize)) {
/*     */               tasks.add(CompletableFuture.runAsync((), (Executor)Util.ioPool()));
/*     */             }
/*     */             
/*     */             return CompletableFuture.allOf((CompletableFuture<?>[])tasks.toArray(()));
/*     */           });
/*     */     } else {
/* 162 */       this.pendingWriteFuture = this.pendingWriteFuture.thenCompose(ignored -> CompletableFuture.allOf((CompletableFuture<?>[])tagsToSave.entrySet().stream().map(()).toArray(())));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 169 */     return this.pendingWriteFuture;
/*     */   }
/*     */   
/*     */   private Map<SavedDataType<?>, CompoundTag> collectDirtyTagsToSave() {
/* 173 */     Object2ObjectArrayMap object2ObjectArrayMap = new Object2ObjectArrayMap();
/* 174 */     RegistryOps<Tag> ops = this.registries.createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/* 175 */     this.cache.forEach((type, optional) -> optional.filter(SavedData::isDirty).ifPresent(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 181 */     return (Map<SavedDataType<?>, CompoundTag>)object2ObjectArrayMap;
/*     */   }
/*     */ 
/*     */   
/*     */   private <T extends SavedData> CompoundTag encodeUnchecked(SavedDataType<T> type, SavedData data, RegistryOps<Tag> ops) {
/* 186 */     Codec<T> codec = type.codec();
/* 187 */     CompoundTag tag = new CompoundTag();
/* 188 */     tag.put("data", (Tag)codec.encodeStart((DynamicOps)ops, data).getOrThrow());
/* 189 */     NbtUtils.addCurrentDataVersion(tag);
/* 190 */     return tag;
/*     */   }
/*     */   
/*     */   private void tryWrite(SavedDataType<?> type, CompoundTag tag) {
/* 194 */     Path path = getDataFile(type.id());
/*     */     try {
/* 196 */       NbtIo.writeCompressed(tag, path);
/* 197 */     } catch (IOException e) {
/* 198 */       LOGGER.error("Could not save data to {}", path.getFileName(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void saveAndJoin() {
/* 203 */     scheduleSave().join();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 209 */     saveAndJoin();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/DimensionDataStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */