/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface PermissionSet
/*    */ {
/*    */   public static final PermissionSet NO_PERMISSIONS = permission -> false;
/*    */   public static final PermissionSet ALL_PERMISSIONS = permission -> true;
/*    */   
/*    */   default PermissionSet union(PermissionSet other) {
/* 11 */     if (other instanceof PermissionSetUnion) {
/* 12 */       return other.union(this);
/*    */     }
/* 14 */     return new PermissionSetUnion(this, other);
/*    */   }
/*    */   
/*    */   boolean hasPermission(Permission paramPermission);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/permissions/PermissionSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */