/*     */ package net.minecraft.server.packs.linkfs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.AccessDeniedException;
/*     */ import java.nio.file.AccessMode;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.DirectoryIteratorException;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.NotDirectoryException;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.ProviderMismatchException;
/*     */ import java.nio.file.ReadOnlyFileSystemException;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.BasicFileAttributeView;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.spi.FileSystemProvider;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ class LinkFSProvider
/*     */   extends FileSystemProvider
/*     */ {
/*     */   public static final String SCHEME = "x-mc-link";
/*     */   
/*     */   public String getScheme() {
/*  38 */     return "x-mc-link";
/*     */   }
/*     */ 
/*     */   
/*     */   public FileSystem newFileSystem(URI uri, Map<String, ?> env) {
/*  43 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public FileSystem getFileSystem(URI uri) {
/*  48 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Path getPath(URI uri) {
/*  53 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
/*  58 */     if (options.contains(StandardOpenOption.CREATE_NEW) || 
/*  59 */       options.contains(StandardOpenOption.CREATE) || 
/*  60 */       options.contains(StandardOpenOption.APPEND) || 
/*  61 */       options.contains(StandardOpenOption.WRITE))
/*     */     {
/*  63 */       throw new UnsupportedOperationException();
/*     */     }
/*  65 */     Path targetPath = toLinkPath(path).toAbsolutePath().getTargetPath();
/*  66 */     if (targetPath == null) {
/*  67 */       throw new NoSuchFileException(path.toString());
/*     */     }
/*  69 */     return Files.newByteChannel(targetPath, options, attrs);
/*     */   }
/*     */ 
/*     */   
/*     */   public DirectoryStream<Path> newDirectoryStream(Path dir, final DirectoryStream.Filter<? super Path> filter) throws IOException {
/*  74 */     final PathContents.DirectoryContents directoryContents = toLinkPath(dir).toAbsolutePath().getDirectoryContents();
/*  75 */     if (directoryContents == null) {
/*  76 */       throw new NotDirectoryException(dir.toString());
/*     */     }
/*     */     
/*  79 */     return new DirectoryStream<Path>(this)
/*     */       {
/*     */         public Iterator<Path> iterator() {
/*  82 */           return directoryContents.children().values()
/*  83 */             .stream()
/*  84 */             .filter(path -> {
/*     */                 try {
/*     */                   return filter.accept(path);
/*  87 */                 } catch (IOException e) {
/*     */                   
/*     */                   throw new DirectoryIteratorException(e);
/*     */                 } 
/*  91 */               }).map(path -> path)
/*  92 */             .iterator();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void close() {}
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void createDirectory(Path dir, FileAttribute<?>... attrs) {
/* 103 */     throw new ReadOnlyFileSystemException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void delete(Path path) {
/* 108 */     throw new ReadOnlyFileSystemException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void copy(Path source, Path target, CopyOption... options) {
/* 113 */     throw new ReadOnlyFileSystemException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(Path source, Path target, CopyOption... options) {
/* 118 */     throw new ReadOnlyFileSystemException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSameFile(Path path, Path path2) {
/* 123 */     return (path instanceof LinkFSPath && path2 instanceof LinkFSPath && path.equals(path2));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHidden(Path path) {
/* 128 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileStore getFileStore(Path path) {
/* 133 */     return toLinkPath(path).getFileSystem().store();
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkAccess(Path path, AccessMode... modes) throws IOException {
/* 138 */     if (modes.length == 0 && 
/* 139 */       !toLinkPath(path).exists()) {
/* 140 */       throw new NoSuchFileException(path.toString());
/*     */     }
/*     */ 
/*     */     
/* 144 */     for (AccessMode mode : modes) {
/* 145 */       switch (mode) {
/*     */         case READ:
/* 147 */           if (!toLinkPath(path).exists())
/* 148 */             throw new NoSuchFileException(path.toString());  break;
/*     */         case EXECUTE:
/*     */         case WRITE:
/* 151 */           throw new AccessDeniedException(mode.toString());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <V extends java.nio.file.attribute.FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
/* 159 */     LinkFSPath linkPath = toLinkPath(path);
/* 160 */     if (type == BasicFileAttributeView.class) {
/* 161 */       return (V)linkPath.getBasicAttributeView();
/*     */     }
/* 163 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
/* 169 */     LinkFSPath linkPath = toLinkPath(path).toAbsolutePath();
/* 170 */     if (type == BasicFileAttributes.class) {
/* 171 */       return (A)linkPath.getBasicAttributes();
/*     */     }
/* 173 */     throw new UnsupportedOperationException("Attributes of type " + type.getName() + " not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) {
/* 179 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAttribute(Path path, String attribute, Object value, LinkOption... options) {
/* 184 */     throw new ReadOnlyFileSystemException();
/*     */   }
/*     */   
/*     */   private static LinkFSPath toLinkPath(Path path) {
/* 188 */     if (path == null) {
/* 189 */       throw new NullPointerException();
/*     */     }
/* 191 */     if (path instanceof LinkFSPath) { LinkFSPath p = (LinkFSPath)path;
/* 192 */       return p; }
/*     */     
/* 194 */     throw new ProviderMismatchException();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/linkfs/LinkFSProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */