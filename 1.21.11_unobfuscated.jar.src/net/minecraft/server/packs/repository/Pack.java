/*     */ package net.minecraft.server.packs.repository;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.packs.FeatureFlagsMetadataSection;
/*     */ import net.minecraft.server.packs.OverlayMetadataSection;
/*     */ import net.minecraft.server.packs.PackLocationInfo;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.PackSelectionConfig;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import net.minecraft.server.packs.metadata.pack.PackFormat;
/*     */ import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class Pack
/*     */ {
/*  22 */   private static final Logger LOGGER = LogUtils.getLogger(); private final PackLocationInfo location; private final ResourcesSupplier resources; private final Metadata metadata;
/*     */   private final PackSelectionConfig selectionConfig;
/*     */   
/*     */   public static interface ResourcesSupplier {
/*     */     PackResources openPrimary(PackLocationInfo param1PackLocationInfo);
/*     */     
/*     */     PackResources openFull(PackLocationInfo param1PackLocationInfo, Pack.Metadata param1Metadata); }
/*     */   
/*     */   public static final class Metadata extends Record { private final Component description;
/*     */     private final PackCompatibility compatibility;
/*     */     private final FeatureFlagSet requestedFeatures;
/*     */     private final List<String> overlays;
/*     */     
/*  35 */     public Metadata(Component description, PackCompatibility compatibility, FeatureFlagSet requestedFeatures, List<String> overlays) { this.description = description; this.compatibility = compatibility; this.requestedFeatures = requestedFeatures; this.overlays = overlays; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/repository/Pack$Metadata;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #35	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  35 */       //   0	7	0	this	Lnet/minecraft/server/packs/repository/Pack$Metadata; } public Component description() { return this.description; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/repository/Pack$Metadata;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #35	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/repository/Pack$Metadata; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/repository/Pack$Metadata;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #35	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/repository/Pack$Metadata;
/*  35 */       //   0	8	1	o	Ljava/lang/Object; } public PackCompatibility compatibility() { return this.compatibility; } public FeatureFlagSet requestedFeatures() { return this.requestedFeatures; } public List<String> overlays() { return this.overlays; }
/*     */      }
/*     */   
/*     */   public static Pack readMetaAndCreate(PackLocationInfo location, ResourcesSupplier resources, PackType packType, PackSelectionConfig selectionConfig) {
/*  39 */     PackFormat currentPackVersion = SharedConstants.getCurrentVersion().packVersion(packType);
/*  40 */     Metadata meta = readPackMetadata(location, resources, currentPackVersion, packType);
/*  41 */     return (meta != null) ? new Pack(location, resources, meta, selectionConfig) : null;
/*     */   }
/*     */   
/*     */   public Pack(PackLocationInfo location, ResourcesSupplier resources, Metadata metadata, PackSelectionConfig selectionConfig) {
/*  45 */     this.location = location;
/*  46 */     this.resources = resources;
/*  47 */     this.metadata = metadata;
/*  48 */     this.selectionConfig = selectionConfig;
/*     */   }
/*     */   public static Metadata readPackMetadata(PackLocationInfo location, ResourcesSupplier resources, PackFormat currentPackVersion, PackType type) {
/*     */     
/*  52 */     try { PackResources pack = resources.openPrimary(location); 
/*  53 */       try { PackMetadataSection meta = (PackMetadataSection)pack.getMetadataSection(PackMetadataSection.forPackType(type));
/*  54 */         if (meta == null)
/*     */         {
/*  56 */           meta = (PackMetadataSection)pack.getMetadataSection(PackMetadataSection.FALLBACK_TYPE);
/*     */         }
/*  58 */         if (meta == null)
/*  59 */         { LOGGER.warn("Missing metadata in pack {}", location.id());
/*  60 */           Metadata metadata1 = null;
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
/*     */           
/*  72 */           if (pack != null) pack.close();  return metadata1; }  FeatureFlagsMetadataSection featureFlagMeta = (FeatureFlagsMetadataSection)pack.getMetadataSection(FeatureFlagsMetadataSection.TYPE); FeatureFlagSet requiredFlags = (featureFlagMeta != null) ? featureFlagMeta.flags() : FeatureFlagSet.of(); PackCompatibility packCompatibility = PackCompatibility.forVersion(meta.supportedFormats(), currentPackVersion); OverlayMetadataSection overlays = (OverlayMetadataSection)pack.getMetadataSection(OverlayMetadataSection.forPackType(type)); List<String> overlaySet = (overlays != null) ? overlays.overlaysForVersion(currentPackVersion) : List.<String>of(); Metadata metadata = new Metadata(meta.description(), packCompatibility, requiredFlags, overlaySet); if (pack != null) pack.close();  return metadata; } catch (Throwable throwable) { if (pack != null) try { pack.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/*  73 */     { LOGGER.warn("Failed to read pack {} metadata", location.id(), e);
/*     */       
/*  75 */       return null; }
/*     */   
/*     */   }
/*     */   public PackLocationInfo location() {
/*  79 */     return this.location;
/*     */   }
/*     */   
/*     */   public Component getTitle() {
/*  83 */     return this.location.title();
/*     */   }
/*     */   
/*     */   public Component getDescription() {
/*  87 */     return this.metadata.description();
/*     */   }
/*     */   
/*     */   public Component getChatLink(boolean enabled) {
/*  91 */     return this.location.createChatLink(enabled, this.metadata.description);
/*     */   }
/*     */   
/*     */   public PackCompatibility getCompatibility() {
/*  95 */     return this.metadata.compatibility();
/*     */   }
/*     */   
/*     */   public FeatureFlagSet getRequestedFeatures() {
/*  99 */     return this.metadata.requestedFeatures();
/*     */   }
/*     */   
/*     */   public PackResources open() {
/* 103 */     return this.resources.openFull(this.location, this.metadata);
/*     */   }
/*     */   
/*     */   public String getId() {
/* 107 */     return this.location.id();
/*     */   }
/*     */   
/*     */   public PackSelectionConfig selectionConfig() {
/* 111 */     return this.selectionConfig;
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/* 115 */     return this.selectionConfig.required();
/*     */   }
/*     */   
/*     */   public boolean isFixedPosition() {
/* 119 */     return this.selectionConfig.fixedPosition();
/*     */   }
/*     */   
/*     */   public Position getDefaultPosition() {
/* 123 */     return this.selectionConfig.defaultPosition();
/*     */   }
/*     */   
/*     */   public PackSource getPackSource() {
/* 127 */     return this.location.source();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 132 */     if (this == o) {
/* 133 */       return true;
/*     */     }
/* 135 */     if (!(o instanceof Pack)) {
/* 136 */       return false;
/*     */     }
/*     */     
/* 139 */     Pack that = (Pack)o;
/*     */     
/* 141 */     return this.location.equals(that.location);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 146 */     return this.location.hashCode();
/*     */   }
/*     */   
/*     */   public enum Position {
/* 150 */     TOP,
/* 151 */     BOTTOM;
/*     */ 
/*     */     
/*     */     public <T> int insert(List<T> list, T value, Function<T, PackSelectionConfig> converter, boolean reverse) {
/* 155 */       Position self = reverse ? opposite() : this;
/* 156 */       if (self == BOTTOM) {
/* 157 */         int i = 0;
/* 158 */         while (i < list.size()) {
/* 159 */           PackSelectionConfig pack = converter.apply(list.get(i));
/* 160 */           if (pack.fixedPosition() && pack.defaultPosition() == this) {
/* 161 */             i++;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 166 */         list.add(i, value);
/* 167 */         return i;
/*     */       } 
/* 169 */       int index = list.size() - 1;
/* 170 */       while (index >= 0) {
/* 171 */         PackSelectionConfig pack = converter.apply(list.get(index));
/* 172 */         if (pack.fixedPosition() && pack.defaultPosition() == this) {
/* 173 */           index--;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 178 */       list.add(index + 1, value);
/* 179 */       return index + 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Position opposite() {
/* 184 */       return (this == TOP) ? BOTTOM : TOP;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/repository/Pack.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */