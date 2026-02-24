/*    */ package net.minecraft.server.packs.resources;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ public interface ResourceMetadata {
/* 20 */   public static final ResourceMetadata EMPTY = new ResourceMetadata()
/*    */     {
/*    */       public <T> Optional<T> getSection(MetadataSectionType<T> serializer) {
/* 23 */         return Optional.empty();
/*    */       }
/*    */     };
/*    */   
/*    */   public static final IoSupplier<ResourceMetadata> EMPTY_SUPPLIER = () -> EMPTY;
/*    */   
/* 29 */   static ResourceMetadata fromJsonStream(InputStream inputStream) throws IOException { BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)); 
/* 30 */     try { final JsonObject metadata = GsonHelper.parse(reader);
/*    */       
/* 32 */       ResourceMetadata resourceMetadata = new ResourceMetadata()
/*    */         {
/*    */           public <T> Optional<T> getSection(MetadataSectionType<T> serializer) {
/* 35 */             String name = serializer.name();
/* 36 */             if (metadata.has(name)) {
/* 37 */               T section = (T)serializer.codec().parse((DynamicOps)JsonOps.INSTANCE, metadata.get(name)).getOrThrow(com.google.gson.JsonParseException::new);
/* 38 */               return Optional.of(section);
/*    */             } 
/* 40 */             return Optional.empty();
/*    */           }
/*    */         };
/*    */       
/* 44 */       reader.close(); return resourceMetadata; }
/*    */     catch (Throwable throwable) { try {
/*    */         reader.close();
/*    */       } catch (Throwable throwable1) {
/*    */         throwable.addSuppressed(throwable1);
/*    */       }  throw throwable; }
/* 50 */      } default <T> Optional<MetadataSectionType.WithValue<T>> getTypedSection(MetadataSectionType<T> type) { Objects.requireNonNull(type); return getSection(type).map(type::withValue); }
/*    */ 
/*    */   
/*    */   default List<MetadataSectionType.WithValue<?>> getTypedSections(Collection<MetadataSectionType<?>> types) {
/* 54 */     return (List<MetadataSectionType.WithValue<?>>)types.stream().map(this::getTypedSection).flatMap(Optional::stream).collect(Collectors.toUnmodifiableList());
/*    */   }
/*    */   
/*    */   <T> Optional<T> getSection(MetadataSectionType<T> paramMetadataSectionType);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/resources/ResourceMetadata.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */