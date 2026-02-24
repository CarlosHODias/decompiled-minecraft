/*     */ package net.minecraft.server.packs.linkfs;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.ProviderMismatchException;
/*     */ import java.nio.file.ReadOnlyFileSystemException;
/*     */ import java.nio.file.WatchEvent;
/*     */ import java.nio.file.WatchKey;
/*     */ import java.nio.file.WatchService;
/*     */ import java.nio.file.attribute.BasicFileAttributeView;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ 
/*     */ class LinkFSPath
/*     */   implements Path {
/*  27 */   private static final BasicFileAttributes DIRECTORY_ATTRIBUTES = new DummyFileAttributes()
/*     */     {
/*     */       public boolean isRegularFile() {
/*  30 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isDirectory() {
/*  35 */         return true;
/*     */       }
/*     */     };
/*     */   
/*  39 */   private static final BasicFileAttributes FILE_ATTRIBUTES = new DummyFileAttributes()
/*     */     {
/*     */       public boolean isRegularFile() {
/*  42 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isDirectory() {
/*  47 */         return false;
/*     */       }
/*     */     };
/*     */   
/*  51 */   private static final Comparator<LinkFSPath> PATH_COMPARATOR = Comparator.comparing(LinkFSPath::pathToString);
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final LinkFileSystem fileSystem;
/*     */   private final LinkFSPath parent;
/*     */   private List<String> pathToRoot;
/*     */   private String pathString;
/*     */   private final PathContents pathContents;
/*     */   
/*     */   public LinkFSPath(LinkFileSystem fileSystem, String name, LinkFSPath parent, PathContents pathContents) {
/*  62 */     this.fileSystem = fileSystem;
/*  63 */     this.name = name;
/*  64 */     this.parent = parent;
/*  65 */     this.pathContents = pathContents;
/*     */   }
/*     */   
/*     */   private LinkFSPath createRelativePath(LinkFSPath parent, String name) {
/*  69 */     return new LinkFSPath(this.fileSystem, name, parent, PathContents.RELATIVE);
/*     */   }
/*     */ 
/*     */   
/*     */   public LinkFileSystem getFileSystem() {
/*  74 */     return this.fileSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbsolute() {
/*  79 */     return (this.pathContents != PathContents.RELATIVE);
/*     */   }
/*     */ 
/*     */   
/*     */   public File toFile() {
/*  84 */     PathContents pathContents = this.pathContents; if (pathContents instanceof PathContents.FileContents) { PathContents.FileContents file = (PathContents.FileContents)pathContents;
/*  85 */       return file.contents().toFile(); }
/*     */     
/*  87 */     throw new UnsupportedOperationException("Path " + pathToString() + " does not represent file");
/*     */   }
/*     */ 
/*     */   
/*     */   public LinkFSPath getRoot() {
/*  92 */     if (isAbsolute()) {
/*  93 */       return this.fileSystem.rootPath();
/*     */     }
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LinkFSPath getFileName() {
/* 100 */     return createRelativePath(null, this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   public LinkFSPath getParent() {
/* 105 */     return this.parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNameCount() {
/* 110 */     return pathToRoot().size();
/*     */   }
/*     */ 
/*     */   
/*     */   private List<String> pathToRoot() {
/* 115 */     if (this.name.isEmpty()) {
/* 116 */       return List.of();
/*     */     }
/*     */     
/* 119 */     if (this.pathToRoot == null) {
/* 120 */       ImmutableList.Builder<String> result = ImmutableList.builder();
/* 121 */       if (this.parent != null) {
/* 122 */         result.addAll(this.parent.pathToRoot());
/*     */       }
/* 124 */       result.add(this.name);
/* 125 */       this.pathToRoot = (List<String>)result.build();
/*     */     } 
/* 127 */     return this.pathToRoot;
/*     */   }
/*     */ 
/*     */   
/*     */   public LinkFSPath getName(int index) {
/* 132 */     List<String> names = pathToRoot();
/* 133 */     if (index < 0 || index >= names.size()) {
/* 134 */       throw new IllegalArgumentException("Invalid index: " + index);
/*     */     }
/* 136 */     return createRelativePath(null, names.get(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public LinkFSPath subpath(int beginIndex, int endIndex) {
/* 141 */     List<String> names = pathToRoot();
/*     */     
/* 143 */     if (beginIndex < 0 || endIndex > names.size() || beginIndex >= endIndex) {
/* 144 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 147 */     LinkFSPath current = null;
/* 148 */     for (int i = beginIndex; i < endIndex; i++) {
/* 149 */       current = createRelativePath(current, names.get(i));
/*     */     }
/* 151 */     return current;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean startsWith(Path other) {
/* 156 */     if (other.isAbsolute() != isAbsolute()) {
/* 157 */       return false;
/*     */     }
/* 159 */     if (other instanceof LinkFSPath) { LinkFSPath otherLink = (LinkFSPath)other;
/* 160 */       if (otherLink.fileSystem != this.fileSystem) {
/* 161 */         return false;
/*     */       }
/* 163 */       List<String> thisNames = pathToRoot();
/* 164 */       List<String> otherNames = otherLink.pathToRoot();
/*     */       
/* 166 */       int otherSize = otherNames.size();
/* 167 */       if (otherSize > thisNames.size()) {
/* 168 */         return false;
/*     */       }
/* 170 */       for (int i = 0; i < otherSize; i++) {
/* 171 */         if (!((String)otherNames.get(i)).equals(thisNames.get(i))) {
/* 172 */           return false;
/*     */         }
/*     */       } 
/* 175 */       return true; }
/*     */     
/* 177 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean endsWith(Path other) {
/* 182 */     if (other.isAbsolute() && !isAbsolute()) {
/* 183 */       return false;
/*     */     }
/* 185 */     if (other instanceof LinkFSPath) { LinkFSPath otherLink = (LinkFSPath)other;
/* 186 */       if (otherLink.fileSystem != this.fileSystem) {
/* 187 */         return false;
/*     */       }
/* 189 */       List<String> thisNames = pathToRoot();
/* 190 */       List<String> otherNames = otherLink.pathToRoot();
/*     */       
/* 192 */       int otherSize = otherNames.size();
/* 193 */       int delta = thisNames.size() - otherSize;
/* 194 */       if (delta < 0) {
/* 195 */         return false;
/*     */       }
/*     */       
/* 198 */       for (int i = otherSize - 1; i >= 0; i--) {
/* 199 */         if (!((String)otherNames.get(i)).equals(thisNames.get(delta + i))) {
/* 200 */           return false;
/*     */         }
/*     */       } 
/* 203 */       return true; }
/*     */     
/* 205 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkFSPath normalize() {
/* 211 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LinkFSPath resolve(Path other) {
/* 216 */     LinkFSPath otherLink = toLinkPath(other);
/* 217 */     if (other.isAbsolute()) {
/* 218 */       return otherLink;
/*     */     }
/* 220 */     return resolve(otherLink.pathToRoot());
/*     */   }
/*     */   
/*     */   private LinkFSPath resolve(List<String> names) {
/* 224 */     LinkFSPath current = this;
/* 225 */     for (String name : names) {
/* 226 */       current = current.resolveName(name);
/*     */     }
/*     */     
/* 229 */     return current;
/*     */   }
/*     */   
/*     */   LinkFSPath resolveName(String name) {
/* 233 */     if (isRelativeOrMissing(this.pathContents))
/* 234 */       return new LinkFSPath(this.fileSystem, name, this, this.pathContents); 
/* 235 */     PathContents pathContents = this.pathContents; if (pathContents instanceof PathContents.DirectoryContents) { PathContents.DirectoryContents directory = (PathContents.DirectoryContents)pathContents;
/* 236 */       LinkFSPath child = directory.children().get(name);
/* 237 */       return (child != null) ? child : new LinkFSPath(this.fileSystem, name, this, PathContents.MISSING); }
/* 238 */      if (this.pathContents instanceof PathContents.FileContents) {
/* 239 */       return new LinkFSPath(this.fileSystem, name, this, PathContents.MISSING);
/*     */     }
/*     */     
/* 242 */     throw new AssertionError("All content types should be already handled");
/*     */   }
/*     */   
/*     */   private static boolean isRelativeOrMissing(PathContents contents) {
/* 246 */     return (contents == PathContents.MISSING || contents == PathContents.RELATIVE);
/*     */   }
/*     */ 
/*     */   
/*     */   public LinkFSPath relativize(Path other) {
/* 251 */     LinkFSPath otherLink = toLinkPath(other);
/* 252 */     if (isAbsolute() != otherLink.isAbsolute()) {
/* 253 */       throw new IllegalArgumentException("absolute mismatch");
/*     */     }
/* 255 */     List<String> thisNames = pathToRoot();
/* 256 */     List<String> otherNames = otherLink.pathToRoot();
/*     */ 
/*     */     
/* 259 */     if (thisNames.size() >= otherNames.size()) {
/* 260 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 263 */     for (int i = 0; i < thisNames.size(); i++) {
/* 264 */       if (!((String)thisNames.get(i)).equals(otherNames.get(i))) {
/* 265 */         throw new IllegalArgumentException();
/*     */       }
/*     */     } 
/*     */     
/* 269 */     return otherLink.subpath(thisNames.size(), otherNames.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public URI toUri() {
/*     */     try {
/* 275 */       return new URI("x-mc-link", this.fileSystem.store().name(), pathToString(), null);
/* 276 */     } catch (URISyntaxException e) {
/* 277 */       throw new AssertionError("Failed to create URI", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public LinkFSPath toAbsolutePath() {
/* 283 */     if (isAbsolute()) {
/* 284 */       return this;
/*     */     }
/*     */     
/* 287 */     return this.fileSystem.rootPath().resolve(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public LinkFSPath toRealPath(LinkOption... options) {
/* 292 */     return toAbsolutePath();
/*     */   }
/*     */ 
/*     */   
/*     */   public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) {
/* 297 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Path other) {
/* 302 */     LinkFSPath otherPath = toLinkPath(other);
/* 303 */     return PATH_COMPARATOR.compare(this, otherPath);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 308 */     if (other == this) {
/* 309 */       return true;
/*     */     }
/* 311 */     if (other instanceof LinkFSPath) { LinkFSPath that = (LinkFSPath)other;
/* 312 */       if (this.fileSystem != that.fileSystem) {
/* 313 */         return false;
/*     */       }
/* 315 */       boolean hasRealContents = hasRealContents();
/* 316 */       if (hasRealContents != that.hasRealContents()) {
/* 317 */         return false;
/*     */       }
/* 319 */       if (hasRealContents)
/*     */       {
/* 321 */         return (this.pathContents == that.pathContents);
/*     */       }
/* 323 */       return (Objects.equals(this.parent, that.parent) && Objects.equals(this.name, that.name)); }
/*     */     
/* 325 */     return false;
/*     */   }
/*     */   
/*     */   private boolean hasRealContents() {
/* 329 */     return !isRelativeOrMissing(this.pathContents);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 334 */     return hasRealContents() ? this.pathContents.hashCode() : this.name.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 339 */     return pathToString();
/*     */   }
/*     */   
/*     */   private String pathToString() {
/* 343 */     if (this.pathString == null) {
/* 344 */       StringBuilder builder = new StringBuilder();
/* 345 */       if (isAbsolute()) {
/* 346 */         builder.append("/");
/*     */       }
/* 348 */       Joiner.on("/").appendTo(builder, pathToRoot());
/* 349 */       this.pathString = builder.toString();
/*     */     } 
/* 351 */     return this.pathString;
/*     */   }
/*     */   
/*     */   private LinkFSPath toLinkPath(Path path) {
/* 355 */     if (path == null) {
/* 356 */       throw new NullPointerException();
/*     */     }
/* 358 */     if (path instanceof LinkFSPath) { LinkFSPath p = (LinkFSPath)path; if (p.fileSystem == this.fileSystem)
/* 359 */         return p;  }
/*     */     
/* 361 */     throw new ProviderMismatchException();
/*     */   }
/*     */   
/*     */   public boolean exists() {
/* 365 */     return hasRealContents();
/*     */   }
/*     */   
/*     */   public Path getTargetPath() {
/* 369 */     PathContents pathContents = this.pathContents; PathContents.FileContents file = (PathContents.FileContents)pathContents; return (pathContents instanceof PathContents.FileContents) ? file.contents() : null;
/*     */   }
/*     */   
/*     */   public PathContents.DirectoryContents getDirectoryContents() {
/* 373 */     PathContents pathContents = this.pathContents; PathContents.DirectoryContents dir = (PathContents.DirectoryContents)pathContents; return (pathContents instanceof PathContents.DirectoryContents) ? dir : null;
/*     */   }
/*     */   
/*     */   public BasicFileAttributeView getBasicAttributeView() {
/* 377 */     return new BasicFileAttributeView()
/*     */       {
/*     */         public String name() {
/* 380 */           return "basic";
/*     */         }
/*     */ 
/*     */         
/*     */         public BasicFileAttributes readAttributes() throws IOException {
/* 385 */           return LinkFSPath.this.getBasicAttributes();
/*     */         }
/*     */ 
/*     */         
/*     */         public void setTimes(FileTime lastModifiedTime, FileTime lastAccessTime, FileTime createTime) {
/* 390 */           throw new ReadOnlyFileSystemException();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public BasicFileAttributes getBasicAttributes() throws IOException {
/* 396 */     if (this.pathContents instanceof PathContents.DirectoryContents) {
/* 397 */       return DIRECTORY_ATTRIBUTES;
/*     */     }
/* 399 */     if (this.pathContents instanceof PathContents.FileContents) {
/* 400 */       return FILE_ATTRIBUTES;
/*     */     }
/* 402 */     throw new NoSuchFileException(pathToString());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/linkfs/LinkFSPath.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */