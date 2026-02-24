/*    */ package net.minecraft.client.searchtree;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public interface IdentifierSearchTree<T>
/*    */ {
/*    */   static <T> IdentifierSearchTree<T> empty() {
/* 12 */     return new IdentifierSearchTree<T>()
/*    */       {
/*    */         public List<T> searchNamespace(String namespace) {
/* 15 */           return List.of();
/*    */         }
/*    */ 
/*    */         
/*    */         public List<T> searchPath(String path) {
/* 20 */           return List.of();
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static <T> IdentifierSearchTree<T> create(List<T> elements, Function<T, Stream<Identifier>> idGetter) {
/* 26 */     if (elements.isEmpty()) {
/* 27 */       return empty();
/*    */     }
/*    */     
/* 30 */     final SuffixArray<T> namespaceTree = new SuffixArray<>();
/* 31 */     final SuffixArray<T> pathTree = new SuffixArray<>();
/* 32 */     for (T element : elements) {
/* 33 */       ((Stream)idGetter.apply(element)).forEach(elementId -> {
/*    */             namespaceTree.add(element, elementId.getNamespace().toLowerCase(Locale.ROOT));
/*    */             pathTree.add(element, elementId.getPath().toLowerCase(Locale.ROOT));
/*    */           });
/*    */     } 
/* 38 */     namespaceTree.generate();
/* 39 */     pathTree.generate();
/*    */     
/* 41 */     return new IdentifierSearchTree<T>()
/*    */       {
/*    */         public List<T> searchNamespace(String namespace) {
/* 44 */           return namespaceTree.search(namespace);
/*    */         }
/*    */ 
/*    */         
/*    */         public List<T> searchPath(String path) {
/* 49 */           return pathTree.search(path);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   List<T> searchNamespace(String paramString);
/*    */   
/*    */   List<T> searchPath(String paramString);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/searchtree/IdentifierSearchTree.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */