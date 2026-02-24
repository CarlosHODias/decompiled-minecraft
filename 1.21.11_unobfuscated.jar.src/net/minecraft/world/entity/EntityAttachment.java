/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public enum EntityAttachment
/*    */ {
/*  8 */   PASSENGER(Fallback.AT_HEIGHT),
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   VEHICLE(Fallback.AT_FEET),
/* 15 */   NAME_TAG(Fallback.AT_HEIGHT),
/* 16 */   WARDEN_CHEST(Fallback.AT_CENTER);
/*    */   
/*    */   private final Fallback fallback;
/*    */ 
/*    */   
/*    */   EntityAttachment(Fallback fallback) {
/* 22 */     this.fallback = fallback;
/*    */   }
/*    */   
/*    */   public List<Vec3> createFallbackPoints(float width, float height) {
/* 26 */     return this.fallback.create(width, height);
/*    */   }
/*    */   
/*    */   public static interface Fallback {
/* 30 */     public static final List<Vec3> ZERO = List.of(Vec3.ZERO);
/*    */     static {
/* 32 */       AT_HEIGHT = ((width, height) -> List.of(new Vec3(0.0D, height, 0.0D)));
/* 33 */       AT_CENTER = ((width, height) -> List.of(new Vec3(0.0D, height / 2.0D, 0.0D)));
/*    */     }
/*    */     
/*    */     public static final Fallback AT_FEET = (width, height) -> ZERO;
/*    */     public static final Fallback AT_HEIGHT;
/*    */     public static final Fallback AT_CENTER;
/*    */     
/*    */     List<Vec3> create(float param1Float1, float param1Float2);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/EntityAttachment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */