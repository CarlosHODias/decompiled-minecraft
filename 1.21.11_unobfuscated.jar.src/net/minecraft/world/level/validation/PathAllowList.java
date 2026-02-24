/*     */ package net.minecraft.world.level.validation;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.PathMatcher;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.stream.Stream;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PathAllowList implements PathMatcher {
/*  16 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private static final String COMMENT_PREFIX = "#";
/*     */   private final List<ConfigEntry> entries;
/*     */   
/*     */   @FunctionalInterface
/*  21 */   public static interface EntryType { public static final EntryType FILESYSTEM = FileSystem::getPathMatcher; public static final EntryType PREFIX = (fileSystem, pattern) -> ();
/*     */     
/*     */     PathMatcher compile(FileSystem param1FileSystem, String param1String); }
/*     */   
/*     */   public static final class ConfigEntry extends Record { private final PathAllowList.EntryType type;
/*     */     private final String pattern;
/*     */     
/*  28 */     public ConfigEntry(PathAllowList.EntryType type, String pattern) { this.type = type; this.pattern = pattern; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  28 */       //   0	7	0	this	Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry; } public PathAllowList.EntryType type() { return this.type; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry;
/*  28 */       //   0	8	1	o	Ljava/lang/Object; } public String pattern() { return this.pattern; }
/*     */      public PathMatcher compile(FileSystem fileSystem) {
/*  30 */       return type().compile(fileSystem, this.pattern);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static Optional<ConfigEntry> parse(String definition) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: invokevirtual isBlank : ()Z
/*     */       //   4: ifne -> 16
/*     */       //   7: aload_0
/*     */       //   8: ldc '#'
/*     */       //   10: invokevirtual startsWith : (Ljava/lang/String;)Z
/*     */       //   13: ifeq -> 20
/*     */       //   16: invokestatic empty : ()Ljava/util/Optional;
/*     */       //   19: areturn
/*     */       //   20: aload_0
/*     */       //   21: ldc '['
/*     */       //   23: invokevirtual startsWith : (Ljava/lang/String;)Z
/*     */       //   26: ifne -> 44
/*     */       //   29: new net/minecraft/world/level/validation/PathAllowList$ConfigEntry
/*     */       //   32: dup
/*     */       //   33: getstatic net/minecraft/world/level/validation/PathAllowList$EntryType.PREFIX : Lnet/minecraft/world/level/validation/PathAllowList$EntryType;
/*     */       //   36: aload_0
/*     */       //   37: invokespecial <init> : (Lnet/minecraft/world/level/validation/PathAllowList$EntryType;Ljava/lang/String;)V
/*     */       //   40: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */       //   43: areturn
/*     */       //   44: aload_0
/*     */       //   45: bipush #93
/*     */       //   47: iconst_1
/*     */       //   48: invokevirtual indexOf : (II)I
/*     */       //   51: istore_1
/*     */       //   52: iload_1
/*     */       //   53: iconst_m1
/*     */       //   54: if_icmpne -> 71
/*     */       //   57: new java/lang/IllegalArgumentException
/*     */       //   60: dup
/*     */       //   61: aload_0
/*     */       //   62: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */       //   67: invokespecial <init> : (Ljava/lang/String;)V
/*     */       //   70: athrow
/*     */       //   71: aload_0
/*     */       //   72: iconst_1
/*     */       //   73: iload_1
/*     */       //   74: invokevirtual substring : (II)Ljava/lang/String;
/*     */       //   77: astore_2
/*     */       //   78: aload_0
/*     */       //   79: iload_1
/*     */       //   80: iconst_1
/*     */       //   81: iadd
/*     */       //   82: invokevirtual substring : (I)Ljava/lang/String;
/*     */       //   85: astore_3
/*     */       //   86: aload_2
/*     */       //   87: astore #4
/*     */       //   89: iconst_m1
/*     */       //   90: istore #5
/*     */       //   92: aload #4
/*     */       //   94: invokevirtual hashCode : ()I
/*     */       //   97: lookupswitch default -> 177, -980110702 -> 164, 3175800 -> 132, 108392519 -> 148
/*     */       //   132: aload #4
/*     */       //   134: ldc 'glob'
/*     */       //   136: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */       //   139: ifeq -> 177
/*     */       //   142: iconst_0
/*     */       //   143: istore #5
/*     */       //   145: goto -> 177
/*     */       //   148: aload #4
/*     */       //   150: ldc 'regex'
/*     */       //   152: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */       //   155: ifeq -> 177
/*     */       //   158: iconst_1
/*     */       //   159: istore #5
/*     */       //   161: goto -> 177
/*     */       //   164: aload #4
/*     */       //   166: ldc 'prefix'
/*     */       //   168: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */       //   171: ifeq -> 177
/*     */       //   174: iconst_2
/*     */       //   175: istore #5
/*     */       //   177: iload #5
/*     */       //   179: tableswitch default -> 244, 0 -> 204, 1 -> 204, 2 -> 227
/*     */       //   204: new net/minecraft/world/level/validation/PathAllowList$ConfigEntry
/*     */       //   207: dup
/*     */       //   208: getstatic net/minecraft/world/level/validation/PathAllowList$EntryType.FILESYSTEM : Lnet/minecraft/world/level/validation/PathAllowList$EntryType;
/*     */       //   211: aload_2
/*     */       //   212: aload_3
/*     */       //   213: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */       //   218: invokespecial <init> : (Lnet/minecraft/world/level/validation/PathAllowList$EntryType;Ljava/lang/String;)V
/*     */       //   221: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */       //   224: goto -> 258
/*     */       //   227: new net/minecraft/world/level/validation/PathAllowList$ConfigEntry
/*     */       //   230: dup
/*     */       //   231: getstatic net/minecraft/world/level/validation/PathAllowList$EntryType.PREFIX : Lnet/minecraft/world/level/validation/PathAllowList$EntryType;
/*     */       //   234: aload_3
/*     */       //   235: invokespecial <init> : (Lnet/minecraft/world/level/validation/PathAllowList$EntryType;Ljava/lang/String;)V
/*     */       //   238: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */       //   241: goto -> 258
/*     */       //   244: new java/lang/IllegalArgumentException
/*     */       //   247: dup
/*     */       //   248: aload_0
/*     */       //   249: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */       //   254: invokespecial <init> : (Ljava/lang/String;)V
/*     */       //   257: athrow
/*     */       //   258: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #34	-> 0
/*     */       //   #35	-> 16
/*     */       //   #37	-> 20
/*     */       //   #38	-> 29
/*     */       //   #41	-> 44
/*     */       //   #42	-> 52
/*     */       //   #43	-> 57
/*     */       //   #46	-> 71
/*     */       //   #47	-> 78
/*     */       //   #48	-> 86
/*     */       //   #49	-> 204
/*     */       //   #50	-> 227
/*     */       //   #51	-> 244
/*     */       //   #48	-> 258
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	259	0	definition	Ljava/lang/String;
/*     */       //   52	207	1	split	I
/*     */       //   78	181	2	type	Ljava/lang/String;
/*     */       //   86	173	3	contents	Ljava/lang/String;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static ConfigEntry glob(String pattern) {
/*  56 */       return new ConfigEntry(PathAllowList.EntryType.FILESYSTEM, "glob:" + pattern);
/*     */     }
/*     */     
/*     */     static ConfigEntry regex(String pattern) {
/*  60 */       return new ConfigEntry(PathAllowList.EntryType.FILESYSTEM, "regex:" + pattern);
/*     */     }
/*     */     
/*     */     static ConfigEntry prefix(String pattern) {
/*  64 */       return new ConfigEntry(PathAllowList.EntryType.PREFIX, pattern);
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*  69 */   private final Map<String, PathMatcher> compiledPaths = new ConcurrentHashMap<>();
/*     */   
/*     */   public PathAllowList(List<ConfigEntry> entries) {
/*  72 */     this.entries = entries;
/*     */   }
/*     */   
/*     */   public PathMatcher getForFileSystem(FileSystem fileSystem) {
/*  76 */     return this.compiledPaths.computeIfAbsent(fileSystem.provider().getScheme(), scheme -> {
/*     */           List<PathMatcher> compiledMatchers;
/*     */ 
/*     */           
/*     */           try {
/*     */             compiledMatchers = this.entries.stream().map(()).toList();
/*  82 */           } catch (Exception e) {
/*     */             LOGGER.error("Failed to compile file pattern list", e);
/*     */             return ();
/*     */           } 
/*     */           switch (compiledMatchers.size()) {
/*     */             case 0:
/*     */             
/*     */             case 1:
/*     */             
/*     */             default:
/*     */               break;
/*     */           } 
/*     */           return ();
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(Path path) {
/* 104 */     return getForFileSystem(path.getFileSystem()).matches(path);
/*     */   }
/*     */   
/*     */   public static PathAllowList readPlain(BufferedReader reader) {
/* 108 */     return new PathAllowList(reader.lines().<ConfigEntry>flatMap(line -> ConfigEntry.parse(line).stream()).toList());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/validation/PathAllowList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */