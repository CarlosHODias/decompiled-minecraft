/*    */ package net.minecraft.locale;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.io.InputStream;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ 
/*    */ public final class DeprecatedTranslationsInfo extends Record {
/*    */   private final List<String> removed;
/*    */   private final Map<String, String> renamed;
/*    */   
/* 18 */   public DeprecatedTranslationsInfo(List<String> removed, Map<String, String> renamed) { this.removed = removed; this.renamed = renamed; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/locale/DeprecatedTranslationsInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 18 */     //   0	7	0	this	Lnet/minecraft/locale/DeprecatedTranslationsInfo; } public List<String> removed() { return this.removed; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/locale/DeprecatedTranslationsInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/locale/DeprecatedTranslationsInfo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/locale/DeprecatedTranslationsInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/locale/DeprecatedTranslationsInfo;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public Map<String, String> renamed() { return this.renamed; }
/*    */ 
/*    */ 
/*    */   
/* 22 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*    */   
/* 24 */   public static final DeprecatedTranslationsInfo EMPTY = new DeprecatedTranslationsInfo(List.of(), Map.of()); public static final Codec<DeprecatedTranslationsInfo> CODEC;
/*    */   static {
/* 26 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.listOf().fieldOf("removed").forGetter(DeprecatedTranslationsInfo::removed), (App)Codec.unboundedMap((Codec)Codec.STRING, (Codec)Codec.STRING).fieldOf("renamed").forGetter(DeprecatedTranslationsInfo::renamed)).apply((Applicative)i, DeprecatedTranslationsInfo::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static DeprecatedTranslationsInfo loadFromJson(InputStream stream) {
/* 32 */     JsonElement entries = net.minecraft.util.StrictJsonParser.parse(new java.io.InputStreamReader(stream, StandardCharsets.UTF_8));
/* 33 */     return (DeprecatedTranslationsInfo)CODEC.parse((com.mojang.serialization.DynamicOps)com.mojang.serialization.JsonOps.INSTANCE, entries).getOrThrow(msg -> new IllegalStateException("Failed to parse deprecated language data: " + msg));
/*    */   }
/*    */   public static DeprecatedTranslationsInfo loadFromResource(String path) {
/*    */     
/* 37 */     try { InputStream stream = Language.class.getResourceAsStream(path); 
/* 38 */       try { if (stream != null)
/* 39 */         { DeprecatedTranslationsInfo deprecatedTranslationsInfo = loadFromJson(stream);
/*    */           
/* 41 */           if (stream != null) stream.close();  return deprecatedTranslationsInfo; }  if (stream != null) stream.close();  } catch (Throwable throwable) { if (stream != null) try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/* 42 */     { LOGGER.error("Failed to read {}", path, e); }
/*    */     
/* 44 */     return EMPTY;
/*    */   }
/*    */   
/*    */   public static DeprecatedTranslationsInfo loadFromDefaultResource() {
/* 48 */     return loadFromResource("/assets/minecraft/lang/deprecated.json");
/*    */   }
/*    */   
/*    */   public void applyToMap(Map<String, String> translations) {
/* 52 */     for (String key : this.removed) {
/* 53 */       translations.remove(key);
/*    */     }
/*    */     
/* 56 */     this.renamed.forEach((fromKey, toKey) -> {
/*    */           String value = (String)translations.remove(fromKey);
/*    */           if (value == null) {
/*    */             LOGGER.warn("Missing translation key for rename: {}", fromKey);
/*    */             translations.remove(toKey);
/*    */           } else {
/*    */             translations.put(toKey, value);
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/locale/DeprecatedTranslationsInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */