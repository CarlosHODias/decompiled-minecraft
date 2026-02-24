/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.packs.PackResources;
/*    */ import net.minecraft.server.packs.repository.KnownPack;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Resource
/*    */ {
/*    */   private final PackResources source;
/*    */   private final IoSupplier<InputStream> streamSupplier;
/*    */   private final IoSupplier<ResourceMetadata> metadataSupplier;
/*    */   private ResourceMetadata cachedMetadata;
/*    */   
/*    */   public Resource(PackResources source, IoSupplier<InputStream> streamSupplier, IoSupplier<ResourceMetadata> metadataSupplier) {
/* 22 */     this.source = source;
/* 23 */     this.streamSupplier = streamSupplier;
/* 24 */     this.metadataSupplier = metadataSupplier;
/*    */   }
/*    */   
/*    */   public Resource(PackResources source, IoSupplier<InputStream> streamSupplier) {
/* 28 */     this.source = source;
/* 29 */     this.streamSupplier = streamSupplier;
/* 30 */     this.metadataSupplier = ResourceMetadata.EMPTY_SUPPLIER;
/* 31 */     this.cachedMetadata = ResourceMetadata.EMPTY;
/*    */   }
/*    */   
/*    */   public PackResources source() {
/* 35 */     return this.source;
/*    */   }
/*    */   
/*    */   public String sourcePackId() {
/* 39 */     return this.source.packId();
/*    */   }
/*    */   
/*    */   public Optional<KnownPack> knownPackInfo() {
/* 43 */     return this.source.knownPackInfo();
/*    */   }
/*    */   
/*    */   public InputStream open() throws IOException {
/* 47 */     return this.streamSupplier.get();
/*    */   }
/*    */   
/*    */   public BufferedReader openAsReader() throws IOException {
/* 51 */     return new BufferedReader(new InputStreamReader(open(), StandardCharsets.UTF_8));
/*    */   }
/*    */   
/*    */   public ResourceMetadata metadata() throws IOException {
/* 55 */     if (this.cachedMetadata == null) {
/* 56 */       this.cachedMetadata = this.metadataSupplier.get();
/*    */     }
/* 58 */     return this.cachedMetadata;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/resources/Resource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */