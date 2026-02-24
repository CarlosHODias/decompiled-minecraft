/*    */ package com.mojang.realmsclient.client.worldupload;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.nio.file.Path;
/*    */ import java.util.function.BooleanSupplier;
/*    */ import java.util.zip.GZIPOutputStream;
/*    */ import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
/*    */ import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
/*    */ 
/*    */ public class RealmsUploadWorldPacker
/*    */ {
/*    */   private static final long SIZE_LIMIT = 5368709120L;
/*    */   private static final String WORLD_FOLDER_NAME = "world";
/*    */   private final BooleanSupplier isCanceled;
/*    */   private final Path directoryToPack;
/*    */   
/*    */   public static File pack(Path directoryToPack, BooleanSupplier isCanceled) throws IOException {
/* 23 */     return new RealmsUploadWorldPacker(directoryToPack, isCanceled).tarGzipArchive();
/*    */   }
/*    */   
/*    */   private RealmsUploadWorldPacker(Path directoryToPack, BooleanSupplier isCanceled) {
/* 27 */     this.isCanceled = isCanceled;
/* 28 */     this.directoryToPack = directoryToPack;
/*    */   }
/*    */   
/*    */   private File tarGzipArchive() throws IOException {
/* 32 */     TarArchiveOutputStream tar = null;
/*    */     try {
/* 34 */       File file = File.createTempFile("realms-upload-file", ".tar.gz");
/* 35 */       tar = new TarArchiveOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
/* 36 */       tar.setLongFileMode(3);
/* 37 */       addFileToTarGz(tar, this.directoryToPack, "world", true);
/* 38 */       if (this.isCanceled.getAsBoolean()) {
/* 39 */         throw new RealmsUploadCanceledException();
/*    */       }
/* 41 */       tar.finish();
/* 42 */       verifyBelowSizeLimit(file.length());
/* 43 */       return file;
/*    */     } finally {
/* 45 */       if (tar != null) {
/* 46 */         tar.close();
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private void addFileToTarGz(TarArchiveOutputStream out, Path path, String base, boolean root) throws IOException {
/* 52 */     if (this.isCanceled.getAsBoolean()) {
/* 53 */       throw new RealmsUploadCanceledException();
/*    */     }
/* 55 */     verifyBelowSizeLimit(out.getBytesWritten());
/*    */     
/* 57 */     File file = path.toFile();
/* 58 */     String entryName = root ? base : (base + base);
/* 59 */     TarArchiveEntry entry = new TarArchiveEntry(file, entryName);
/* 60 */     out.putArchiveEntry(entry);
/*    */     
/* 62 */     if (file.isFile()) {
/* 63 */       InputStream is = new FileInputStream(file); 
/* 64 */       try { is.transferTo((OutputStream)out);
/* 65 */         is.close(); } catch (Throwable throwable) { try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/* 66 */        out.closeArchiveEntry();
/*    */     } else {
/* 68 */       out.closeArchiveEntry();
/* 69 */       File[] children = file.listFiles();
/*    */       
/* 71 */       if (children != null) {
/* 72 */         for (File child : children) {
/* 73 */           addFileToTarGz(out, child.toPath(), entryName + "/", false);
/*    */         }
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private void verifyBelowSizeLimit(long sizeInByte) {
/* 80 */     if (sizeInByte > 5368709120L)
/* 81 */       throw new RealmsUploadTooLargeException(5368709120L); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/client/worldupload/RealmsUploadWorldPacker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */