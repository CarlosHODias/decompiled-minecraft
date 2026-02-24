/*    */ package net.minecraft.gizmos;
/*    */ 
/*    */ 
/*    */ public interface GizmoCollector
/*    */ {
/*  6 */   public static final GizmoProperties IGNORED = new GizmoProperties()
/*    */     {
/*    */       public GizmoProperties setAlwaysOnTop()
/*    */       {
/* 10 */         return this;
/*    */       }
/*    */ 
/*    */       
/*    */       public GizmoProperties persistForMillis(int milliseconds) {
/* 15 */         return this;
/*    */       }
/*    */ 
/*    */       
/*    */       public GizmoProperties fadeOut() {
/* 20 */         return this;
/*    */       }
/*    */     };
/*    */   
/*    */   public static final GizmoCollector NOOP = gizmo -> IGNORED;
/*    */   
/*    */   GizmoProperties add(Gizmo paramGizmo);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gizmos/GizmoCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */