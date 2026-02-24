/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum WallSide implements StringRepresentable {
/*  6 */   NONE("none"),
/*  7 */   LOW("low"),
/*  8 */   TALL("tall");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/*    */   WallSide(String name) {
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
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/WallSide.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */