/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.zip.ZipEntry;
/*    */ import java.util.zip.ZipFile;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface IoSupplier<T> {
/*    */   static IoSupplier<InputStream> create(Path path) {
/* 13 */     return () -> Files.newInputStream(path, new java.nio.file.OpenOption[0]);
/*    */   }
/*    */   
/*    */   static IoSupplier<InputStream> create(ZipFile zipFile, ZipEntry entry) {
/* 17 */     return () -> zipFile.getInputStream(entry);
/*    */   }
/*    */   
/*    */   T get() throws IOException;
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/resources/IoSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */