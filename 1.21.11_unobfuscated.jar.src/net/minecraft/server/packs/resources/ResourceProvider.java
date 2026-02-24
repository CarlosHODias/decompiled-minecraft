/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface ResourceProvider
/*    */ {
/*    */   public static final ResourceProvider EMPTY = location -> Optional.empty();
/*    */   
/*    */   default Resource getResourceOrThrow(Identifier location) throws FileNotFoundException {
/* 23 */     return getResource(location).orElseThrow(() -> new FileNotFoundException(location.toString()));
/*    */   }
/*    */   
/*    */   default InputStream open(Identifier location) throws IOException {
/* 27 */     return getResourceOrThrow(location).open();
/*    */   }
/*    */   
/*    */   default BufferedReader openAsReader(Identifier location) throws IOException {
/* 31 */     return getResourceOrThrow(location).openAsReader();
/*    */   }
/*    */   
/*    */   static ResourceProvider fromMap(Map<Identifier, Resource> map) {
/* 35 */     return location -> Optional.ofNullable((Resource)map.get(location));
/*    */   }
/*    */   
/*    */   Optional<Resource> getResource(Identifier paramIdentifier);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/resources/ResourceProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */