/*    */ package net.minecraft.advancements;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ public class AdvancementNode
/*    */ {
/*    */   private final AdvancementHolder holder;
/*    */   private final AdvancementNode parent;
/* 12 */   private final Set<AdvancementNode> children = (Set<AdvancementNode>)new ReferenceOpenHashSet();
/*    */   
/*    */   @VisibleForTesting
/*    */   public AdvancementNode(AdvancementHolder holder, AdvancementNode parent) {
/* 16 */     this.holder = holder;
/* 17 */     this.parent = parent;
/*    */   }
/*    */   
/*    */   public Advancement advancement() {
/* 21 */     return this.holder.value();
/*    */   }
/*    */   
/*    */   public AdvancementHolder holder() {
/* 25 */     return this.holder;
/*    */   }
/*    */   
/*    */   public AdvancementNode parent() {
/* 29 */     return this.parent;
/*    */   }
/*    */   
/*    */   public AdvancementNode root() {
/* 33 */     return getRoot(this);
/*    */   }
/*    */   
/*    */   public static AdvancementNode getRoot(AdvancementNode advancement) {
/* 37 */     AdvancementNode root = advancement;
/*    */     while (true) {
/* 39 */       AdvancementNode parent = root.parent();
/* 40 */       if (parent == null) {
/* 41 */         return root;
/*    */       }
/* 43 */       root = parent;
/*    */     } 
/*    */   }
/*    */   
/*    */   public Iterable<AdvancementNode> children() {
/* 48 */     return this.children;
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public void addChild(AdvancementNode child) {
/* 53 */     this.children.add(child);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 58 */     if (this == obj) {
/* 59 */       return true;
/*    */     }
/* 61 */     if (obj instanceof AdvancementNode) { AdvancementNode that = (AdvancementNode)obj; if (this.holder.equals(that.holder)); }  return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 66 */     return this.holder.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     return this.holder.id().toString();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/AdvancementNode.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */