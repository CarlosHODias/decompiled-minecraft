/*    */ package net.minecraft.gizmos;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class SimpleGizmoCollector
/*    */   implements GizmoCollector {
/* 11 */   private final List<GizmoInstance> gizmos = new ArrayList<>();
/* 12 */   private final List<GizmoInstance> temporaryGizmos = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public GizmoProperties add(Gizmo gizmo) {
/* 16 */     GizmoInstance instance = new GizmoInstance(gizmo);
/* 17 */     this.gizmos.add(instance);
/* 18 */     return instance;
/*    */   }
/*    */   
/*    */   public List<GizmoInstance> drainGizmos() {
/* 22 */     ArrayList<GizmoInstance> result = new ArrayList<>(this.gizmos);
/* 23 */     result.addAll(this.temporaryGizmos);
/* 24 */     long currentMillis = Util.getMillis();
/* 25 */     this.gizmos.removeIf(gizmo -> (gizmo.getExpireTimeMillis() < currentMillis));
/* 26 */     this.temporaryGizmos.clear();
/* 27 */     return result;
/*    */   }
/*    */   
/*    */   public List<GizmoInstance> getGizmos() {
/* 31 */     return this.gizmos;
/*    */   }
/*    */   
/*    */   public void addTemporaryGizmos(Collection<GizmoInstance> gizmos) {
/* 35 */     this.temporaryGizmos.addAll(gizmos);
/*    */   }
/*    */   
/*    */   public static class GizmoInstance implements GizmoProperties {
/*    */     private final Gizmo gizmo;
/*    */     private boolean isAlwaysOnTop;
/*    */     private long startTimeMillis;
/*    */     private long expireTimeMillis;
/*    */     private boolean shouldFadeOut;
/*    */     
/*    */     private GizmoInstance(Gizmo gizmo) {
/* 46 */       this.gizmo = gizmo;
/*    */     }
/*    */ 
/*    */     
/*    */     public GizmoProperties setAlwaysOnTop() {
/* 51 */       this.isAlwaysOnTop = true;
/* 52 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public GizmoProperties persistForMillis(int milliseconds) {
/* 57 */       this.startTimeMillis = Util.getMillis();
/* 58 */       this.expireTimeMillis = this.startTimeMillis + milliseconds;
/* 59 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public GizmoProperties fadeOut() {
/* 64 */       this.shouldFadeOut = true;
/* 65 */       return this;
/*    */     }
/*    */     
/*    */     public float getAlphaMultiplier(long currentMillis) {
/* 69 */       if (this.shouldFadeOut) {
/* 70 */         long duration = this.expireTimeMillis - this.startTimeMillis;
/* 71 */         long timeSinceStart = currentMillis - this.startTimeMillis;
/* 72 */         return 1.0F - Mth.clamp((float)timeSinceStart / (float)duration, 0.0F, 1.0F);
/*    */       } 
/* 74 */       return 1.0F;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isAlwaysOnTop() {
/* 79 */       return this.isAlwaysOnTop;
/*    */     }
/*    */     
/*    */     public long getExpireTimeMillis() {
/* 83 */       return this.expireTimeMillis;
/*    */     }
/*    */     
/*    */     public Gizmo gizmo() {
/* 87 */       return this.gizmo;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gizmos/SimpleGizmoCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */