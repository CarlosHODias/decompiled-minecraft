/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum DoorHingeSide implements StringRepresentable {
/*  6 */   LEFT,
/*  7 */   RIGHT;
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 12 */     return getSerializedName();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 17 */     return (this == LEFT) ? "left" : "right";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/DoorHingeSide.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */