/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum RedstoneSide implements StringRepresentable {
/*  6 */   UP("up"),
/*  7 */   SIDE("side"),
/*  8 */   NONE("none");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/*    */   RedstoneSide(String name) {
/* 14 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 19 */     return getSerializedName();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 24 */     return this.name;
/*    */   }
/*    */   
/*    */   public boolean isConnected() {
/* 28 */     return (this != NONE);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/RedstoneSide.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */