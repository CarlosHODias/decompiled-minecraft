/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.PackResources;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ResourceManager
/*    */   extends ResourceProvider
/*    */ {
/*    */   Set<String> getNamespaces();
/*    */   
/*    */   List<Resource> getResourceStack(Identifier paramIdentifier);
/*    */   
/*    */   Map<Identifier, Resource> listResources(String paramString, Predicate<Identifier> paramPredicate);
/*    */   
/*    */   Map<Identifier, List<Resource>> listResourceStacks(String paramString, Predicate<Identifier> paramPredicate);
/*    */   
/*    */   Stream<PackResources> listPacks();
/*    */   
/*    */   public enum Empty
/*    */     implements ResourceManager
/*    */   {
/* 39 */     INSTANCE;
/*    */ 
/*    */     
/*    */     public Set<String> getNamespaces() {
/* 43 */       return Set.of();
/*    */     }
/*    */ 
/*    */     
/*    */     public Optional<Resource> getResource(Identifier location) {
/* 48 */       return Optional.empty();
/*    */     }
/*    */ 
/*    */     
/*    */     public List<Resource> getResourceStack(Identifier location) {
/* 53 */       return List.of();
/*    */     }
/*    */ 
/*    */     
/*    */     public Map<Identifier, Resource> listResources(String directory, Predicate<Identifier> filter) {
/* 58 */       return Map.of();
/*    */     }
/*    */ 
/*    */     
/*    */     public Map<Identifier, List<Resource>> listResourceStacks(String directory, Predicate<Identifier> filter) {
/* 63 */       return Map.of();
/*    */     }
/*    */ 
/*    */     
/*    */     public Stream<PackResources> listPacks() {
/* 68 */       return Stream.of(new PackResources[0]);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/resources/ResourceManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */