/*    */ package net.minecraft.server.permissions;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
/*    */ import it.unimi.dsi.fastutil.objects.ReferenceSet;
/*    */ import java.util.Collection;
/*    */ 
/*    */ public class PermissionSetUnion implements PermissionSet {
/*  8 */   private final ReferenceSet<PermissionSet> permissions = (ReferenceSet<PermissionSet>)new ReferenceArraySet();
/*    */   
/*    */   PermissionSetUnion(PermissionSet first, PermissionSet second) {
/* 11 */     this.permissions.add(first);
/* 12 */     this.permissions.add(second);
/* 13 */     ensureNoUnionsWithinUnions();
/*    */   }
/*    */   
/*    */   private PermissionSetUnion(ReferenceSet<PermissionSet> oldPermissions, PermissionSet other) {
/* 17 */     this.permissions.addAll((Collection)oldPermissions);
/* 18 */     this.permissions.add(other);
/* 19 */     ensureNoUnionsWithinUnions();
/*    */   }
/*    */   
/*    */   private PermissionSetUnion(ReferenceSet<PermissionSet> oldPermissions, ReferenceSet<PermissionSet> other) {
/* 23 */     this.permissions.addAll((Collection)oldPermissions);
/* 24 */     this.permissions.addAll((Collection)other);
/* 25 */     ensureNoUnionsWithinUnions();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasPermission(Permission permission) {
/* 30 */     for (ObjectIterator<PermissionSet> objectIterator = this.permissions.iterator(); objectIterator.hasNext(); ) { PermissionSet set = objectIterator.next();
/* 31 */       if (set.hasPermission(permission)) {
/* 32 */         return true;
/*    */       } }
/*    */     
/* 35 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public PermissionSet union(PermissionSet other) {
/* 40 */     if (other instanceof PermissionSetUnion) { PermissionSetUnion otherUnion = (PermissionSetUnion)other;
/* 41 */       return new PermissionSetUnion(this.permissions, otherUnion.permissions); }
/*    */     
/* 43 */     return new PermissionSetUnion(this.permissions, other);
/*    */   }
/*    */   
/*    */   @com.google.common.annotations.VisibleForTesting
/*    */   public ReferenceSet<PermissionSet> getPermissions() {
/* 48 */     return (ReferenceSet<PermissionSet>)new ReferenceArraySet(this.permissions);
/*    */   }
/*    */   
/*    */   private void ensureNoUnionsWithinUnions() {
/* 52 */     for (ObjectIterator<PermissionSet> objectIterator = this.permissions.iterator(); objectIterator.hasNext(); ) { PermissionSet set = objectIterator.next();
/* 53 */       if (set instanceof PermissionSetUnion)
/* 54 */         throw new IllegalArgumentException("Cannot have PermissionSetUnion within another PermissionSetUnion");  }
/*    */   
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/permissions/PermissionSetUnion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */