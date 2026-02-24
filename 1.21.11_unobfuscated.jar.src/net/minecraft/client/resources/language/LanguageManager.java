/*    */ package net.minecraft.client.resources.language;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.SortedMap;
/*    */ import java.util.TreeMap;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.client.resources.metadata.language.LanguageMetadataSection;
/*    */ import net.minecraft.locale.Language;
/*    */ import net.minecraft.server.packs.PackResources;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class LanguageManager
/*    */   implements ResourceManagerReloadListener
/*    */ {
/* 24 */   private static final Logger LOGGER = LogUtils.getLogger();
/* 25 */   private static final LanguageInfo DEFAULT_LANGUAGE = new LanguageInfo("US", "English", false);
/*    */   
/* 27 */   private Map<String, LanguageInfo> languages = (Map<String, LanguageInfo>)ImmutableMap.of("en_us", DEFAULT_LANGUAGE);
/*    */   private String currentCode;
/*    */   private final Consumer<ClientLanguage> reloadCallback;
/*    */   
/*    */   public LanguageManager(String languageCode, Consumer<ClientLanguage> reloadCallback) {
/* 32 */     this.currentCode = languageCode;
/* 33 */     this.reloadCallback = reloadCallback;
/*    */   }
/*    */   
/*    */   private static Map<String, LanguageInfo> extractLanguages(Stream<PackResources> resourcePacks) {
/* 37 */     Map<String, LanguageInfo> result = Maps.newHashMap();
/*    */     
/* 39 */     resourcePacks.forEach(resourcePack -> {
/*    */           try {
/*    */             LanguageMetadataSection languageMetadataSection = (LanguageMetadataSection)resourcePack.getMetadataSection(LanguageMetadataSection.TYPE);
/*    */             if (languageMetadataSection != null) {
/*    */               Objects.requireNonNull(result);
/*    */               languageMetadataSection.languages().forEach(result::putIfAbsent);
/*    */             } 
/* 46 */           } catch (RuntimeException|java.io.IOException e) {
/*    */             LOGGER.warn("Unable to parse language metadata section of resourcepack: {}", resourcePack.packId(), e);
/*    */           } 
/*    */         });
/*    */     
/* 51 */     return (Map<String, LanguageInfo>)ImmutableMap.copyOf(result);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onResourceManagerReload(ResourceManager resourceManager) {
/* 56 */     this.languages = extractLanguages(resourceManager.listPacks());
/* 57 */     List<String> languageStack = new ArrayList<>(2);
/* 58 */     boolean defaultRightToLeft = DEFAULT_LANGUAGE.bidirectional();
/* 59 */     languageStack.add("en_us");
/*    */     
/* 61 */     if (!this.currentCode.equals("en_us")) {
/* 62 */       LanguageInfo currentLanguage = this.languages.get(this.currentCode);
/* 63 */       if (currentLanguage != null) {
/* 64 */         languageStack.add(this.currentCode);
/* 65 */         defaultRightToLeft = currentLanguage.bidirectional();
/*    */       } 
/*    */     } 
/*    */     
/* 69 */     ClientLanguage locale = ClientLanguage.loadFrom(resourceManager, languageStack, defaultRightToLeft);
/*    */     
/* 71 */     I18n.setLanguage(locale);
/*    */     
/* 73 */     Language.inject(locale);
/*    */     
/* 75 */     this.reloadCallback.accept(locale);
/*    */   }
/*    */   
/*    */   public void setSelected(String code) {
/* 79 */     this.currentCode = code;
/*    */   }
/*    */   
/*    */   public String getSelected() {
/* 83 */     return this.currentCode;
/*    */   }
/*    */   
/*    */   public SortedMap<String, LanguageInfo> getLanguages() {
/* 87 */     return new TreeMap<>(this.languages);
/*    */   }
/*    */   
/*    */   public LanguageInfo getLanguage(String code) {
/* 91 */     return this.languages.get(code);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/language/LanguageManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */