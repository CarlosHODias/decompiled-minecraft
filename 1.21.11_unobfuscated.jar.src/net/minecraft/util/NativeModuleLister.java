/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.sun.jna.Memory;
/*     */ import com.sun.jna.Native;
/*     */ import com.sun.jna.Platform;
/*     */ import com.sun.jna.Pointer;
/*     */ import com.sun.jna.platform.win32.Kernel32;
/*     */ import com.sun.jna.platform.win32.Kernel32Util;
/*     */ import com.sun.jna.platform.win32.Tlhelp32;
/*     */ import com.sun.jna.platform.win32.Version;
/*     */ import com.sun.jna.platform.win32.Win32Exception;
/*     */ import com.sun.jna.ptr.IntByReference;
/*     */ import com.sun.jna.ptr.PointerByReference;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class NativeModuleLister
/*     */ {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int LANG_MASK = 65535;
/*     */   
/*     */   private static final int DEFAULT_LANG = 1033;
/*     */   private static final int CODEPAGE_MASK = -65536;
/*     */   private static final int DEFAULT_CODEPAGE = 78643200;
/*     */   
/*     */   public static List<NativeModuleInfo> listModules() {
/*  38 */     if (!Platform.isWindows()) {
/*  39 */       return (List<NativeModuleInfo>)ImmutableList.of();
/*     */     }
/*     */     
/*  42 */     int selfHandle = Kernel32.INSTANCE.GetCurrentProcessId();
/*     */     
/*  44 */     ImmutableList.Builder<NativeModuleInfo> result = ImmutableList.builder();
/*     */     
/*  46 */     List<Tlhelp32.MODULEENTRY32W> modules = Kernel32Util.getModules(selfHandle);
/*     */     
/*  48 */     for (Tlhelp32.MODULEENTRY32W module : modules) {
/*  49 */       String name = module.szModule();
/*  50 */       Optional<NativeModuleVersion> versionInfo = tryGetVersion(module.szExePath());
/*  51 */       result.add(new NativeModuleInfo(name, versionInfo));
/*     */     } 
/*     */     
/*  54 */     return (List<NativeModuleInfo>)result.build();
/*     */   }
/*     */   
/*     */   private static Optional<NativeModuleVersion> tryGetVersion(String path) {
/*     */     try {
/*  59 */       IntByReference dwDummy = new IntByReference();
/*     */       
/*  61 */       int versionLength = Version.INSTANCE.GetFileVersionInfoSize(path, dwDummy);
/*     */       
/*  63 */       if (versionLength == 0) {
/*  64 */         int lastError = Native.getLastError();
/*  65 */         if (lastError == 1813 || lastError == 1812) {
/*  66 */           return Optional.empty();
/*     */         }
/*  68 */         throw new Win32Exception(lastError);
/*     */       } 
/*     */       
/*  71 */       Memory memory = new Memory(versionLength);
/*     */       
/*  73 */       if (!Version.INSTANCE.GetFileVersionInfo(path, 0, versionLength, (Pointer)memory)) {
/*  74 */         throw new Win32Exception(Native.getLastError());
/*     */       }
/*     */       
/*  77 */       IntByReference size = new IntByReference();
/*  78 */       Pointer translationsBuffer = queryVersionValue((Pointer)memory, "\\VarFileInfo\\Translation", size);
/*  79 */       int[] langsAndCodepages = translationsBuffer.getIntArray(0L, size.getValue() / 4);
/*     */       
/*  81 */       OptionalInt maybeLangAndCodepage = findLangAndCodepage(langsAndCodepages);
/*  82 */       if (maybeLangAndCodepage.isEmpty()) {
/*  83 */         return Optional.empty();
/*     */       }
/*     */       
/*  86 */       int langAndCodepage = maybeLangAndCodepage.getAsInt();
/*  87 */       int lang = langAndCodepage & 0xFFFF;
/*  88 */       int codepage = (langAndCodepage & 0xFFFF0000) >> 16;
/*  89 */       String description = queryVersionString((Pointer)memory, langTableKey("FileDescription", lang, codepage), size);
/*  90 */       String companyName = queryVersionString((Pointer)memory, langTableKey("CompanyName", lang, codepage), size);
/*  91 */       String fileVersion = queryVersionString((Pointer)memory, langTableKey("FileVersion", lang, codepage), size);
/*     */       
/*  93 */       return Optional.of(new NativeModuleVersion(description, fileVersion, companyName));
/*  94 */     } catch (Exception e) {
/*  95 */       LOGGER.info("Failed to find module info for {}", path, e);
/*     */       
/*  97 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */   private static String langTableKey(String key, int lang, int codepage) {
/* 101 */     return String.format(Locale.ROOT, "\\StringFileInfo\\%04x%04x\\%s", new Object[] { lang, codepage, key });
/*     */   }
/*     */   
/*     */   private static OptionalInt findLangAndCodepage(int[] langsAndCodepages) {
/* 105 */     OptionalInt bestSoFar = OptionalInt.empty();
/* 106 */     for (int langAndCodepage : langsAndCodepages) {
/* 107 */       if ((langAndCodepage & 0xFFFF0000) == 78643200 && (
/* 108 */         langAndCodepage & 0xFFFF) == 1033) {
/* 109 */         return OptionalInt.of(langAndCodepage);
/*     */       }
/*     */       
/* 112 */       bestSoFar = OptionalInt.of(langAndCodepage);
/*     */     } 
/* 114 */     return bestSoFar;
/*     */   }
/*     */   
/*     */   private static Pointer queryVersionValue(Pointer lpData, String key, IntByReference outSize) {
/* 118 */     PointerByReference lplpBuffer = new PointerByReference();
/* 119 */     if (!Version.INSTANCE.VerQueryValue(lpData, key, lplpBuffer, outSize)) {
/* 120 */       throw new UnsupportedOperationException("Can't get version value " + key);
/*     */     }
/* 122 */     return lplpBuffer.getValue();
/*     */   }
/*     */   
/*     */   private static String queryVersionString(Pointer lpData, String key, IntByReference outSize) {
/*     */     try {
/* 127 */       Pointer ptr = queryVersionValue(lpData, key, outSize);
/*     */       
/* 129 */       byte[] result = ptr.getByteArray(0L, (outSize.getValue() - 1) * 2);
/* 130 */       return new String(result, StandardCharsets.UTF_16LE);
/* 131 */     } catch (Exception e) {
/* 132 */       return "";
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void addCrashSection(CrashReportCategory category) {
/* 137 */     category.setDetail("Modules", () -> (String)listModules().stream().sorted(Comparator.comparing(())).map(()).collect(Collectors.joining()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class NativeModuleVersion
/*     */   {
/*     */     public final String description;
/*     */     
/*     */     public final String version;
/*     */     
/*     */     public final String company;
/*     */ 
/*     */     
/*     */     public NativeModuleVersion(String description, String version, String company) {
/* 152 */       this.description = description;
/* 153 */       this.version = version;
/* 154 */       this.company = company;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 159 */       return this.description + ":" + this.description + ":" + this.version;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class NativeModuleInfo {
/*     */     public final String name;
/*     */     public final Optional<NativeModuleLister.NativeModuleVersion> version;
/*     */     
/*     */     public NativeModuleInfo(String name, Optional<NativeModuleLister.NativeModuleVersion> version) {
/* 168 */       this.name = name;
/* 169 */       this.version = version;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 174 */       return this.version.<String>map(v -> this.name + ":" + this.name).orElse(this.name);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/NativeModuleLister.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */