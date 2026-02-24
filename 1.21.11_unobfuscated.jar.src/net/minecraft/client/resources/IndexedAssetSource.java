/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import com.google.common.base.Splitter;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.packs.linkfs.LinkFileSystem;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class IndexedAssetSource
/*    */ {
/* 21 */   private static final Logger LOGGER = LogUtils.getLogger();
/* 22 */   public static final Splitter PATH_SPLITTER = Splitter.on('/');
/*    */   
/*    */   public static Path createIndexFs(Path assetsDirectory, String index) {
/* 25 */     Path objectsDirectory = assetsDirectory.resolve("objects");
/*    */     
/* 27 */     LinkFileSystem.Builder builder = LinkFileSystem.builder();
/*    */     
/* 29 */     Path indexFile = assetsDirectory.resolve("indexes/" + index + ".json"); 
/* 30 */     try { BufferedReader reader = Files.newBufferedReader(indexFile, StandardCharsets.UTF_8); 
/* 31 */       try { JsonObject root = GsonHelper.parse(reader);
/* 32 */         JsonObject objects = GsonHelper.getAsJsonObject(root, "objects", null);
/* 33 */         if (objects != null) {
/* 34 */           for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)objects.entrySet()) {
/* 35 */             JsonObject object = (JsonObject)entry.getValue();
/*    */             
/* 37 */             String filename = entry.getKey();
/* 38 */             List<String> path = PATH_SPLITTER.splitToList(filename);
/* 39 */             String hash = GsonHelper.getAsString(object, "hash");
/* 40 */             Path file = objectsDirectory.resolve(hash.substring(0, 2) + "/" + hash.substring(0, 2));
/*    */             
/* 42 */             builder.put(path, file);
/*    */           } 
/*    */         }
/* 45 */         if (reader != null) reader.close();  } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (JsonParseException ignored)
/* 46 */     { LOGGER.error("Unable to parse resource index file: {}", indexFile); }
/* 47 */     catch (IOException ignored)
/* 48 */     { LOGGER.error("Can't open the resource index file: {}", indexFile); }
/*    */ 
/*    */     
/* 51 */     return builder.build("index-" + index).getPath("/", new String[0]);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/IndexedAssetSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */