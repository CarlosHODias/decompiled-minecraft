/*    */ package net.minecraft.server.packs.linkfs;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.FileStore;
/*    */ import java.nio.file.attribute.BasicFileAttributeView;
/*    */ import java.nio.file.attribute.FileAttributeView;
/*    */ 
/*    */ 
/*    */ class LinkFSFileStore
/*    */   extends FileStore
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   public LinkFSFileStore(String name) {
/* 15 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String name() {
/* 20 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String type() {
/* 25 */     return "index";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isReadOnly() {
/* 30 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getTotalSpace() {
/* 35 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getUsableSpace() {
/* 40 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getUnallocatedSpace() {
/* 45 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean supportsFileAttributeView(Class<? extends FileAttributeView> type) {
/* 50 */     return (type == BasicFileAttributeView.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean supportsFileAttributeView(String name) {
/* 55 */     return "basic".equals(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public <V extends java.nio.file.attribute.FileStoreAttributeView> V getFileStoreAttributeView(Class<V> type) {
/* 60 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getAttribute(String attribute) throws IOException {
/* 65 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/linkfs/LinkFSFileStore.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */