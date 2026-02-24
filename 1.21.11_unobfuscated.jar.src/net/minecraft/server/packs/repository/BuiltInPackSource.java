/*     */ package net.minecraft.server.packs.repository;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.PackLocationInfo;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import net.minecraft.server.packs.VanillaPackResources;
/*     */ import net.minecraft.world.level.validation.DirectoryValidator;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class BuiltInPackSource
/*     */   implements RepositorySource {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final String VANILLA_ID = "vanilla";
/*     */   
/*     */   public static final String TESTS_ID = "tests";
/*  30 */   public static final KnownPack CORE_PACK_INFO = KnownPack.vanilla("core");
/*     */   
/*     */   private final PackType packType;
/*     */   private final VanillaPackResources vanillaPack;
/*     */   private final Identifier packDir;
/*     */   private final DirectoryValidator validator;
/*     */   
/*     */   public BuiltInPackSource(PackType packType, VanillaPackResources vanillaPack, Identifier packDir, DirectoryValidator validator) {
/*  38 */     this.packType = packType;
/*  39 */     this.vanillaPack = vanillaPack;
/*  40 */     this.packDir = packDir;
/*  41 */     this.validator = validator;
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadPacks(Consumer<Pack> result) {
/*  46 */     Pack vanilla = createVanillaPack((PackResources)this.vanillaPack);
/*  47 */     if (vanilla != null) {
/*  48 */       result.accept(vanilla);
/*     */     }
/*  50 */     listBundledPacks(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VanillaPackResources getVanillaPack() {
/*  58 */     return this.vanillaPack;
/*     */   }
/*     */   
/*     */   private void listBundledPacks(Consumer<Pack> packConsumer) {
/*  62 */     Map<String, Function<String, Pack>> discoveredPacks = new HashMap<>();
/*     */     
/*  64 */     Objects.requireNonNull(discoveredPacks); populatePackList(discoveredPacks::put);
/*     */     
/*  66 */     discoveredPacks.forEach((id, packSupplier) -> {
/*     */           Pack pack = packSupplier.apply(id);
/*     */           if (pack != null) {
/*     */             packConsumer.accept(pack);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   protected void populatePackList(BiConsumer<String, Function<String, Pack>> discoveredPacks) {
/*  75 */     this.vanillaPack.listRawPaths(this.packType, this.packDir, path -> discoverPacksInPath(discoveredPacks, discoveredPacks));
/*     */   }
/*     */   
/*     */   protected void discoverPacksInPath(Path targetDir, BiConsumer<String, Function<String, Pack>> discoveredPacks) {
/*  79 */     if (targetDir != null && Files.isDirectory(targetDir, new java.nio.file.LinkOption[0])) {
/*     */       try {
/*  81 */         FolderRepositorySource.discoverPacks(targetDir, this.validator, (path, resources) -> discoveredPacks.accept(pathToId(discoveredPacks), ()));
/*     */       
/*     */       }
/*  84 */       catch (IOException e) {
/*  85 */         LOGGER.warn("Failed to discover packs in {}", targetDir, e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static String pathToId(Path path) {
/*  91 */     return StringUtils.removeEnd(path.getFileName().toString(), ".zip");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Pack.ResourcesSupplier fixedResources(final PackResources instance) {
/*  97 */     return new Pack.ResourcesSupplier()
/*     */       {
/*     */         public PackResources openPrimary(PackLocationInfo location) {
/* 100 */           return instance;
/*     */         }
/*     */ 
/*     */         
/*     */         public PackResources openFull(PackLocationInfo location, Pack.Metadata metadata) {
/* 105 */           return instance;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   protected abstract Pack createVanillaPack(PackResources paramPackResources);
/*     */   
/*     */   protected abstract Component getPackTitle(String paramString);
/*     */   
/*     */   protected abstract Pack createBuiltinPack(String paramString, Pack.ResourcesSupplier paramResourcesSupplier, Component paramComponent);
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/repository/BuiltInPackSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */