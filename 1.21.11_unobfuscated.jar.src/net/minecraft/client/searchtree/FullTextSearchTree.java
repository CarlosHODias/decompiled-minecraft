/*    */ package net.minecraft.client.searchtree;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class FullTextSearchTree<T>
/*    */   extends IdSearchTree<T> {
/*    */   private final SearchTree<T> plainTextSearchTree;
/*    */   
/*    */   public FullTextSearchTree(Function<T, Stream<String>> nameGetter, Function<T, Stream<Identifier>> idGetter, List<T> contents) {
/* 15 */     super(idGetter, contents);
/* 16 */     this.plainTextSearchTree = SearchTree.plainText(contents, nameGetter);
/*    */   }
/*    */ 
/*    */   
/*    */   protected List<T> searchPlainText(String text) {
/* 21 */     return this.plainTextSearchTree.search(text);
/*    */   }
/*    */ 
/*    */   
/*    */   protected List<T> searchIdentifier(String namespace, String path) {
/* 26 */     List<T> namespaces = this.identifierSearchTree.searchNamespace(namespace);
/* 27 */     List<T> paths = this.identifierSearchTree.searchPath(path);
/* 28 */     List<T> names = this.plainTextSearchTree.search(path);
/*    */     
/* 30 */     MergingUniqueIterator<T> mergingUniqueIterator = new MergingUniqueIterator<>(paths.iterator(), names.iterator(), this.additionOrder);
/* 31 */     return (List<T>)ImmutableList.copyOf((Iterator)new IntersectionIterator<>(namespaces.iterator(), (Iterator<T>)mergingUniqueIterator, this.additionOrder));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/searchtree/FullTextSearchTree.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */