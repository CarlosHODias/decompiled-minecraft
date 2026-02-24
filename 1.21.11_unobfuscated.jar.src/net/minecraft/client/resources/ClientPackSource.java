/*     */ package net.minecraft.client.resources;
/*     */ 
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.BuiltInMetadata;
/*     */ import net.minecraft.server.packs.PackLocationInfo;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.PackSelectionConfig;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import net.minecraft.server.packs.VanillaPackResources;
/*     */ import net.minecraft.server.packs.VanillaPackResourcesBuilder;
/*     */ import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
/*     */ import net.minecraft.server.packs.repository.BuiltInPackSource;
/*     */ import net.minecraft.server.packs.repository.KnownPack;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.server.packs.repository.PackSource;
/*     */ import net.minecraft.world.level.validation.DirectoryValidator;
/*     */ 
/*     */ 
/*     */ public class ClientPackSource
/*     */   extends BuiltInPackSource
/*     */ {
/*  31 */   private static final PackMetadataSection VERSION_METADATA_SECTION = new PackMetadataSection(
/*  32 */       (Component)Component.translatable("resourcePack.vanilla.description"), 
/*  33 */       SharedConstants.getCurrentVersion().packVersion(PackType.CLIENT_RESOURCES).minorRange());
/*     */ 
/*     */   
/*  36 */   private static final BuiltInMetadata BUILT_IN_METADATA = BuiltInMetadata.of(PackMetadataSection.CLIENT_TYPE, VERSION_METADATA_SECTION);
/*     */   
/*     */   public static final String HIGH_CONTRAST_PACK = "high_contrast";
/*     */   
/*  40 */   private static final Map<String, Component> SPECIAL_PACK_NAMES = (Map)Map.of("programmer_art", 
/*  41 */       Component.translatable("resourcePack.programmer_art.name"), "high_contrast", 
/*  42 */       Component.translatable("resourcePack.high_contrast.name"));
/*     */ 
/*     */   
/*  45 */   private static final PackLocationInfo VANILLA_PACK_INFO = new PackLocationInfo("vanilla", 
/*     */       
/*  47 */       (Component)Component.translatable("resourcePack.vanilla.name"), PackSource.BUILT_IN, 
/*     */       
/*  49 */       Optional.of(CORE_PACK_INFO));
/*     */ 
/*     */   
/*  52 */   private static final PackSelectionConfig VANILLA_SELECTION_CONFIG = new PackSelectionConfig(true, Pack.Position.BOTTOM, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   private static final PackSelectionConfig BUILT_IN_SELECTION_CONFIG = new PackSelectionConfig(false, Pack.Position.TOP, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   private static final Identifier PACKS_DIR = Identifier.withDefaultNamespace("resourcepacks");
/*     */   
/*     */   private final Path externalAssetDir;
/*     */   
/*     */   public ClientPackSource(Path externalAssetSource, DirectoryValidator validator) {
/*  69 */     super(PackType.CLIENT_RESOURCES, createVanillaPackSource(externalAssetSource), PACKS_DIR, validator);
/*  70 */     this.externalAssetDir = findExplodedAssetPacks(externalAssetSource);
/*     */   }
/*     */   
/*     */   private static PackLocationInfo createBuiltInPackLocation(String id, Component title) {
/*  74 */     return new PackLocationInfo(id, title, PackSource.BUILT_IN, 
/*     */ 
/*     */ 
/*     */         
/*  78 */         Optional.of(KnownPack.vanilla(id)));
/*     */   }
/*     */ 
/*     */   
/*     */   private Path findExplodedAssetPacks(Path externalAssetSource) {
/*  83 */     if (SharedConstants.IS_RUNNING_IN_IDE && externalAssetSource.getFileSystem() == FileSystems.getDefault()) {
/*  84 */       Path devAssetDir = externalAssetSource.getParent().resolve("resourcepacks");
/*  85 */       if (Files.isDirectory(devAssetDir, new java.nio.file.LinkOption[0])) {
/*  86 */         return devAssetDir;
/*     */       }
/*     */     } 
/*  89 */     return null;
/*     */   }
/*     */   
/*     */   private static VanillaPackResources createVanillaPackSource(Path externalAssetRoot) {
/*  93 */     VanillaPackResourcesBuilder builder = new VanillaPackResourcesBuilder()
/*  94 */       .setMetadata(BUILT_IN_METADATA)
/*  95 */       .exposeNamespace(new String[] { "minecraft", "realms" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 101 */     return 
/* 102 */       builder.applyDevelopmentConfig()
/* 103 */       .pushJarResources()
/* 104 */       .pushAssetPath(PackType.CLIENT_RESOURCES, externalAssetRoot)
/* 105 */       .build(VANILLA_PACK_INFO);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Component getPackTitle(String id) {
/* 110 */     Component title = SPECIAL_PACK_NAMES.get(id);
/* 111 */     return (title != null) ? title : (Component)Component.literal(id);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Pack createVanillaPack(PackResources resources) {
/* 116 */     return Pack.readMetaAndCreate(VANILLA_PACK_INFO, fixedResources(resources), PackType.CLIENT_RESOURCES, VANILLA_SELECTION_CONFIG);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Pack createBuiltinPack(String id, Pack.ResourcesSupplier resources, Component name) {
/* 121 */     return Pack.readMetaAndCreate(createBuiltInPackLocation(id, name), resources, PackType.CLIENT_RESOURCES, BUILT_IN_SELECTION_CONFIG);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populatePackList(BiConsumer<String, Function<String, Pack>> discoveredPacks) {
/* 126 */     super.populatePackList(discoveredPacks);
/*     */     
/* 128 */     if (this.externalAssetDir != null)
/* 129 */       discoverPacksInPath(this.externalAssetDir, discoveredPacks); 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/ClientPackSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */