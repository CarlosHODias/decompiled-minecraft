/*    */ package net.minecraft.world.level.block.entity;
/*    */ import java.util.List;
/*    */ 
/*    */ public interface BeaconBeamOwner {
/*    */   List<Section> getBeamSections();
/*    */   
/*    */   public static class Section {
/*    */     private final int color;
/*    */     
/*    */     public Section(int color) {
/* 11 */       this.color = color;
/* 12 */       this.height = 1;
/*    */     }
/*    */     private int height;
/*    */     public void increaseHeight() {
/* 16 */       this.height++;
/*    */     }
/*    */     
/*    */     public int getColor() {
/* 20 */       return this.color;
/*    */     }
/*    */     
/*    */     public int getHeight() {
/* 24 */       return this.height;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/BeaconBeamOwner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */