/*    */ package net.minecraft.data;
/*    */ import com.google.common.hash.Hashing;
/*    */ import com.google.common.hash.HashingOutputStream;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Comparator;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public interface DataProvider {
/*    */   public static final ToIntFunction<String> FIXED_ORDER_FIELDS;
/*    */   public static final Comparator<String> KEY_COMPARATOR;
/*    */   
/*    */   static {
/* 31 */     FIXED_ORDER_FIELDS = (ToIntFunction<String>)Util.make(new Object2IntOpenHashMap(), m -> {
/*    */           m.put("type", 0);
/*    */           m.put("parent", 1);
/*    */           m.defaultReturnValue(2);
/*    */         });
/* 36 */     KEY_COMPARATOR = Comparator.<String>comparingInt(FIXED_ORDER_FIELDS).thenComparing(e -> e);
/*    */   }
/* 38 */   public static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <T> CompletableFuture<?> saveAll(CachedOutput cache, Codec<T> codec, PackOutput.PathProvider pathProvider, Map<net.minecraft.resources.Identifier, T> entries) {
/* 45 */     java.util.Objects.requireNonNull(pathProvider); return saveAll(cache, codec, pathProvider::json, entries);
/*    */   }
/*    */   
/*    */   static <T, E> CompletableFuture<?> saveAll(CachedOutput cache, Codec<E> codec, Function<T, Path> pathGetter, Map<T, E> contents) {
/* 49 */     return saveAll(cache, e -> (JsonElement)codec.encodeStart((DynamicOps)JsonOps.INSTANCE, e).getOrThrow(), pathGetter, contents);
/*    */   }
/*    */   
/*    */   static <T, E> CompletableFuture<?> saveAll(CachedOutput cache, Function<E, JsonElement> serializer, Function<T, Path> pathGetter, Map<T, E> contents) {
/* 53 */     return CompletableFuture.allOf((CompletableFuture<?>[])contents.entrySet().stream()
/* 54 */         .map(entry -> {
/*    */             Path path = (Path)pathGetter.apply(entry.getKey());
/*    */             
/*    */             JsonElement json = (JsonElement)serializer.apply(entry.getValue());
/*    */             return saveStable(cache, json, path);
/* 59 */           }).toArray(x$0 -> new CompletableFuture[x$0]));
/*    */   }
/*    */   
/*    */   static <T> CompletableFuture<?> saveStable(CachedOutput cache, HolderLookup.Provider registries, Codec<T> codec, T value, Path path) {
/* 63 */     RegistryOps<JsonElement> ops = registries.createSerializationContext((DynamicOps)JsonOps.INSTANCE);
/* 64 */     return saveStable(cache, (DynamicOps<JsonElement>)ops, codec, value, path);
/*    */   }
/*    */   
/*    */   static <T> CompletableFuture<?> saveStable(CachedOutput cache, Codec<T> codec, T value, Path path) {
/* 68 */     return saveStable(cache, (DynamicOps<JsonElement>)JsonOps.INSTANCE, codec, value, path);
/*    */   }
/*    */   
/*    */   private static <T> CompletableFuture<?> saveStable(CachedOutput cache, DynamicOps<JsonElement> ops, Codec<T> codec, T value, Path path) {
/* 72 */     JsonElement json = (JsonElement)codec.encodeStart(ops, value).getOrThrow();
/* 73 */     return saveStable(cache, json, path);
/*    */   }
/*    */   
/*    */   static CompletableFuture<?> saveStable(CachedOutput cache, JsonElement root, Path path) {
/* 77 */     return CompletableFuture.runAsync(() -> {
/*    */           try {
/*    */             ByteArrayOutputStream bytes = new ByteArrayOutputStream(); HashingOutputStream hashedBytes = new HashingOutputStream(Hashing.sha1(), bytes); JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter((OutputStream)hashedBytes, StandardCharsets.UTF_8)); 
/*    */             try { jsonWriter.setSerializeNulls(false); jsonWriter.setIndent("  "); GsonHelper.writeValue(jsonWriter, root, KEY_COMPARATOR); jsonWriter.close(); }
/* 81 */             catch (Throwable throwable) { try { jsonWriter.close(); } catch (Throwable throwable1)
/*    */               { throwable.addSuppressed(throwable1); }
/*    */               
/*    */               throw throwable; }
/*    */             
/*    */             cache.writeIfNeeded(path, bytes.toByteArray(), hashedBytes.hash());
/* 87 */           } catch (IOException e) {
/*    */             LOGGER.error("Failed to save file to {}", path, e);
/*    */           } 
/* 90 */         }, Util.backgroundExecutor().forName("saveStable"));
/*    */   }
/*    */   
/*    */   CompletableFuture<?> run(CachedOutput paramCachedOutput);
/*    */   
/*    */   String getName();
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Factory<T extends DataProvider> {
/*    */     T create(PackOutput param1PackOutput);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/DataProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */