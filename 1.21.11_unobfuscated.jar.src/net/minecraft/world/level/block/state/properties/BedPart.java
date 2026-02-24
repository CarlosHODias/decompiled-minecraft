/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum BedPart implements StringRepresentable {
/*  6 */   HEAD("head"),
/*  7 */   FOOT("foot");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/*    */   BedPart(String name) {
/* 13 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 18 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 23 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/BedPart.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */