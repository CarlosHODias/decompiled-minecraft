/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum StairsShape implements StringRepresentable {
/*  6 */   STRAIGHT("straight"),
/*    */   
/*  8 */   INNER_LEFT("inner_left"),
/*  9 */   INNER_RIGHT("inner_right"),
/* 10 */   OUTER_LEFT("outer_left"),
/* 11 */   OUTER_RIGHT("outer_right");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/*    */   StairsShape(String name) {
/* 17 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 22 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 27 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/StairsShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */