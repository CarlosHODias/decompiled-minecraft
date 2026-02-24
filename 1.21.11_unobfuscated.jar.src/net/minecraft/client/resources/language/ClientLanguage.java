/*    */ package net.minecraft.client.resources.language;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.locale.DeprecatedTranslationsInfo;
/*    */ import net.minecraft.locale.Language;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.Resource;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class ClientLanguage extends Language {
/* 21 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final Map<String, String> storage;
/*    */   private final boolean defaultRightToLeft;
/*    */   
/*    */   private ClientLanguage(Map<String, String> storage, boolean defaultRightToLeft) {
/* 27 */     this.storage = storage;
/* 28 */     this.defaultRightToLeft = defaultRightToLeft;
/*    */   }
/*    */   
/*    */   public static ClientLanguage loadFrom(ResourceManager resourceManager, List<String> languageStack, boolean defaultRightToLeft) {
/* 32 */     Map<String, String> translations = new HashMap<>();
/*    */     
/* 34 */     for (String languageCode : languageStack) {
/* 35 */       String path = String.format(Locale.ROOT, "lang/%s.json", new Object[] { languageCode });
/*    */       
/* 37 */       for (String namespace : (Iterable<String>)resourceManager.getNamespaces()) {
/*    */         try {
/* 39 */           Identifier location = Identifier.fromNamespaceAndPath(namespace, path);
/* 40 */           appendFrom(languageCode, resourceManager.getResourceStack(location), translations);
/* 41 */         } catch (Exception e) {
/* 42 */           LOGGER.warn("Skipped language file: {}:{} ({})", new Object[] { namespace, path, e.toString() });
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 47 */     DeprecatedTranslationsInfo.loadFromDefaultResource().applyToMap(translations);
/*    */     
/* 49 */     return new ClientLanguage(Map.copyOf(translations), defaultRightToLeft);
/*    */   }
/*    */   
/*    */   private static void appendFrom(String languageCode, List<Resource> resources, Map<String, String> translations) {
/* 53 */     for (Resource resource : resources) { 
/* 54 */       try { InputStream inputStream = resource.open(); 
/* 55 */         try { Objects.requireNonNull(translations); Language.loadFromJson(inputStream, translations::put);
/* 56 */           if (inputStream != null) inputStream.close();  } catch (Throwable throwable) { if (inputStream != null) try { inputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 57 */       { LOGGER.warn("Failed to load translations for {} from pack {}", new Object[] { languageCode, resource.sourcePackId(), e }); }
/*    */        }
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   public String getOrDefault(String key, String defaultValue) {
/* 64 */     return this.storage.getOrDefault(key, defaultValue);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean has(String key) {
/* 69 */     return this.storage.containsKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDefaultRightToLeft() {
/* 74 */     return this.defaultRightToLeft;
/*    */   }
/*    */ 
/*    */   
/*    */   public FormattedCharSequence getVisualOrder(FormattedText logicalOrderText) {
/* 79 */     return FormattedBidiReorder.reorder(logicalOrderText, this.defaultRightToLeft);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/language/ClientLanguage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */