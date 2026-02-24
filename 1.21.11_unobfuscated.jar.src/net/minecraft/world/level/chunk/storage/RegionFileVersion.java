/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.zip.DeflaterOutputStream;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import java.util.zip.InflaterInputStream;
/*     */ import net.jpountz.lz4.LZ4BlockInputStream;
/*     */ import net.jpountz.lz4.LZ4BlockOutputStream;
/*     */ import net.minecraft.util.FastBufferedInputStream;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class RegionFileVersion
/*     */ {
/*  24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  25 */   private static final Int2ObjectMap<RegionFileVersion> VERSIONS = (Int2ObjectMap<RegionFileVersion>)new Int2ObjectOpenHashMap();
/*  26 */   private static final Object2ObjectMap<String, RegionFileVersion> VERSIONS_BY_NAME = (Object2ObjectMap<String, RegionFileVersion>)new Object2ObjectOpenHashMap();
/*     */   static {
/*  28 */     VERSION_GZIP = register(new RegionFileVersion(1, null, in -> new FastBufferedInputStream(new GZIPInputStream(in)), out -> new BufferedOutputStream(new GZIPOutputStream(out))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  34 */     VERSION_DEFLATE = register(new RegionFileVersion(2, "deflate", in -> new FastBufferedInputStream(new InflaterInputStream(in)), out -> new BufferedOutputStream(new DeflaterOutputStream(out))));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final RegionFileVersion VERSION_GZIP;
/*     */   public static final RegionFileVersion VERSION_DEFLATE;
/*  40 */   public static final RegionFileVersion VERSION_NONE = register(new RegionFileVersion(3, "none", FastBufferedInputStream::new, BufferedOutputStream::new));
/*     */   
/*     */   public static final RegionFileVersion VERSION_LZ4;
/*     */ 
/*     */   
/*     */   static {
/*  46 */     VERSION_LZ4 = register(new RegionFileVersion(4, "lz4", in -> new FastBufferedInputStream((InputStream)new LZ4BlockInputStream(in)), out -> new BufferedOutputStream((OutputStream)new LZ4BlockOutputStream(out))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   public static final RegionFileVersion VERSION_CUSTOM = register(new RegionFileVersion(127, null, in -> {
/*     */           throw new UnsupportedOperationException();
/*     */         }, out -> {
/*     */           throw new UnsupportedOperationException();
/*     */         }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public static final RegionFileVersion DEFAULT = VERSION_DEFLATE;
/*  63 */   private static volatile RegionFileVersion selected = DEFAULT;
/*     */   
/*     */   private final int id;
/*     */   private final String optionName;
/*     */   private final StreamWrapper<InputStream> inputWrapper;
/*     */   private final StreamWrapper<OutputStream> outputWrapper;
/*     */   
/*     */   private RegionFileVersion(int id, String optionName, StreamWrapper<InputStream> inputWrapper, StreamWrapper<OutputStream> outputWrapper) {
/*  71 */     this.id = id;
/*  72 */     this.optionName = optionName;
/*  73 */     this.inputWrapper = inputWrapper;
/*  74 */     this.outputWrapper = outputWrapper;
/*     */   }
/*     */   
/*     */   private static RegionFileVersion register(RegionFileVersion version) {
/*  78 */     VERSIONS.put(version.id, version);
/*  79 */     if (version.optionName != null) {
/*  80 */       VERSIONS_BY_NAME.put(version.optionName, version);
/*     */     }
/*  82 */     return version;
/*     */   }
/*     */   
/*     */   public static RegionFileVersion fromId(int id) {
/*  86 */     return (RegionFileVersion)VERSIONS.get(id);
/*     */   }
/*     */   
/*     */   public static void configure(String optionName) {
/*  90 */     RegionFileVersion version = (RegionFileVersion)VERSIONS_BY_NAME.get(optionName);
/*  91 */     if (version != null) {
/*  92 */       selected = version;
/*     */     } else {
/*  94 */       LOGGER.error("Invalid `region-file-compression` value `{}` in server.properties. Please use one of: {}", optionName, String.join(", ", (Iterable<? extends CharSequence>)VERSIONS_BY_NAME.keySet()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static RegionFileVersion getSelected() {
/*  99 */     return selected;
/*     */   }
/*     */   
/*     */   public static boolean isValidVersion(int version) {
/* 103 */     return VERSIONS.containsKey(version);
/*     */   }
/*     */   
/*     */   public int getId() {
/* 107 */     return this.id;
/*     */   }
/*     */   
/*     */   public OutputStream wrap(OutputStream is) throws IOException {
/* 111 */     return this.outputWrapper.wrap(is);
/*     */   }
/*     */   
/*     */   public InputStream wrap(InputStream is) throws IOException {
/* 115 */     return this.inputWrapper.wrap(is);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface StreamWrapper<O> {
/*     */     O wrap(O param1O) throws IOException;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/storage/RegionFileVersion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */