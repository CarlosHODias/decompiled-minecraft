/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class TranslucencyPointOfView {
/*    */   private int x;
/*    */   private int y;
/*    */   private int z;
/*    */   
/*    */   public static TranslucencyPointOfView of(Vec3 cameraPos, long sectionNode) {
/* 13 */     return new TranslucencyPointOfView().set(cameraPos, sectionNode);
/*    */   }
/*    */   
/*    */   public TranslucencyPointOfView set(Vec3 cameraPos, long sectionPos) {
/* 17 */     this.x = getCoordinate(cameraPos.x(), SectionPos.x(sectionPos));
/* 18 */     this.y = getCoordinate(cameraPos.y(), SectionPos.y(sectionPos));
/* 19 */     this.z = getCoordinate(cameraPos.z(), SectionPos.z(sectionPos));
/* 20 */     return this;
/*    */   }
/*    */   
/*    */   private static int getCoordinate(double cameraCoordinate, int section) {
/* 24 */     int relativeSection = SectionPos.blockToSectionCoord(cameraCoordinate) - section;
/* 25 */     return Mth.clamp(relativeSection, -1, 1);
/*    */   }
/*    */   
/*    */   public boolean isAxisAligned() {
/* 29 */     return (this.x == 0 || this.y == 0 || this.z == 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 34 */     if (other == this) {
/* 35 */       return true;
/*    */     }
/* 37 */     if (other instanceof TranslucencyPointOfView) { TranslucencyPointOfView otherPerspective = (TranslucencyPointOfView)other;
/* 38 */       return (this.x == otherPerspective.x && this.y == otherPerspective.y && this.z == otherPerspective.z); }
/*    */     
/* 40 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/chunk/TranslucencyPointOfView.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */