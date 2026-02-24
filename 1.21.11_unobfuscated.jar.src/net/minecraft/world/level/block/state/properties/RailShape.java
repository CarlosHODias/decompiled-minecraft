/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum RailShape implements StringRepresentable {
/*  6 */   NORTH_SOUTH("north_south"),
/*  7 */   EAST_WEST("east_west"),
/*  8 */   ASCENDING_EAST("ascending_east"),
/*  9 */   ASCENDING_WEST("ascending_west"),
/* 10 */   ASCENDING_NORTH("ascending_north"),
/* 11 */   ASCENDING_SOUTH("ascending_south"),
/* 12 */   SOUTH_EAST("south_east"),
/* 13 */   SOUTH_WEST("south_west"),
/* 14 */   NORTH_WEST("north_west"),
/* 15 */   NORTH_EAST("north_east");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/*    */   RailShape(String name) {
/* 21 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 25 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 30 */     return this.name;
/*    */   }
/*    */   
/*    */   public boolean isSlope() {
/* 34 */     return (this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH || this == ASCENDING_WEST);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 39 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/RailShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */