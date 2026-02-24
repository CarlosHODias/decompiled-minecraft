/*    */ package net.minecraft.data;
/*    */ 
/*    */ import com.google.common.base.Stopwatch;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.HashSet;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import net.minecraft.WorldVersion;
/*    */ import net.minecraft.server.Bootstrap;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DataGenerator {
/* 18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final Path rootOutputFolder;
/*    */   
/*    */   private final PackOutput vanillaPackOutput;
/*    */   
/* 24 */   private final Set<String> allProviderIds = new HashSet<>();
/* 25 */   private final Map<String, DataProvider> providersToRun = new LinkedHashMap<>();
/*    */   private final WorldVersion version;
/*    */   private final boolean alwaysGenerate;
/*    */   
/*    */   static {
/* 30 */     Bootstrap.bootStrap();
/*    */   }
/*    */   
/*    */   public DataGenerator(Path output, WorldVersion version, boolean alwaysGenerate) {
/* 34 */     this.rootOutputFolder = output;
/* 35 */     this.vanillaPackOutput = new PackOutput(this.rootOutputFolder);
/* 36 */     this.version = version;
/* 37 */     this.alwaysGenerate = alwaysGenerate;
/*    */   }
/*    */   
/*    */   public void run() throws IOException {
/* 41 */     HashCache cache = new HashCache(this.rootOutputFolder, this.allProviderIds, this.version);
/*    */     
/* 43 */     Stopwatch totalTime = Stopwatch.createStarted();
/* 44 */     Stopwatch stopwatch = Stopwatch.createUnstarted();
/* 45 */     this.providersToRun.forEach((providerId, provider) -> {
/*    */           if (!this.alwaysGenerate && !cache.shouldRunInThisVersion(cache)) {
/*    */             LOGGER.debug("Generator {} already run for version {}", cache, this.version.name()); return;
/*    */           } 
/*    */           LOGGER.info("Starting provider: {}", cache);
/*    */           cache.start();
/*    */           Objects.requireNonNull(provider);
/*    */           cache.applyUpdate(cache.generateUpdate(cache, provider::run).join());
/*    */           cache.stop();
/*    */           LOGGER.info("{} finished after {} ms", cache, cache.elapsed(TimeUnit.MILLISECONDS));
/*    */           cache.reset();
/*    */         });
/* 57 */     LOGGER.info("All providers took: {} ms", totalTime.elapsed(TimeUnit.MILLISECONDS));
/*    */     
/* 59 */     cache.purgeStaleAndWrite();
/*    */   }
/*    */   
/*    */   public PackGenerator getVanillaPack(boolean toRun) {
/* 63 */     return new PackGenerator(toRun, "vanilla", this.vanillaPackOutput);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public PackGenerator getBuiltinDatapack(boolean toRun, String packId) {
/* 69 */     Path packOutputDir = this.vanillaPackOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve("minecraft").resolve("datapacks").resolve(packId);
/* 70 */     return new PackGenerator(toRun, packId, new PackOutput(packOutputDir));
/*    */   }
/*    */   
/*    */   public class PackGenerator {
/*    */     private final boolean toRun;
/*    */     private final String providerPrefix;
/*    */     private final PackOutput output;
/*    */     
/*    */     private PackGenerator(boolean toRun, String providerPrefix, PackOutput output) {
/* 79 */       this.toRun = toRun;
/* 80 */       this.providerPrefix = providerPrefix;
/* 81 */       this.output = output;
/*    */     }
/*    */     
/*    */     public <T extends DataProvider> T addProvider(DataProvider.Factory<T> factory) {
/* 85 */       T provider = factory.create(this.output);
/* 86 */       String providerId = this.providerPrefix + "/" + this.providerPrefix;
/* 87 */       if (!DataGenerator.this.allProviderIds.add(providerId)) {
/* 88 */         throw new IllegalStateException("Duplicate provider: " + providerId);
/*    */       }
/* 90 */       if (this.toRun) {
/* 91 */         DataGenerator.this.providersToRun.put(providerId, (DataProvider)provider);
/*    */       }
/* 93 */       return provider;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/DataGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */