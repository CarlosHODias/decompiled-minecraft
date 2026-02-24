/*    */ package net.minecraft.client.searchtree;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.Comparator;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.ToIntFunction;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class IdSearchTree<T> implements SearchTree<T> {
/*    */   protected final Comparator<T> additionOrder;
/*    */   protected final IdentifierSearchTree<T> identifierSearchTree;
/*    */   
/*    */   public IdSearchTree(Function<T, Stream<Identifier>> idGetter, List<T> contents) {
/* 18 */     ToIntFunction<T> indexLookup = Util.createIndexLookup(contents);
/* 19 */     this.additionOrder = Comparator.comparingInt(indexLookup);
/* 20 */     this.identifierSearchTree = IdentifierSearchTree.create(contents, idGetter);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<T> search(String text) {
/* 27 */     int colon = text.indexOf(':');
/* 28 */     if (colon == -1) {
/* 29 */       return searchPlainText(text);
/*    */     }
/* 31 */     return searchIdentifier(text.substring(0, colon).trim(), text.substring(colon + 1).trim());
/*    */   }
/*    */   
/*    */   protected List<T> searchPlainText(String text) {
/* 35 */     return this.identifierSearchTree.searchPath(text);
/*    */   }
/*    */   
/*    */   protected List<T> searchIdentifier(String namespace, String path) {
/* 39 */     List<T> namespaces = this.identifierSearchTree.searchNamespace(namespace);
/* 40 */     List<T> paths = this.identifierSearchTree.searchPath(path);
/* 41 */     return (List<T>)ImmutableList.copyOf((Iterator)new IntersectionIterator<>(namespaces.iterator(), paths.iterator(), this.additionOrder));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/searchtree/IdSearchTree.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */