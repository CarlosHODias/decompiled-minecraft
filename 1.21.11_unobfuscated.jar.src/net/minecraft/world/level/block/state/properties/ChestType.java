/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum ChestType implements StringRepresentable {
/*  6 */   SINGLE("single"),
/*  7 */   LEFT("left"),
/*  8 */   RIGHT("right");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/*    */   ChestType(String name) {
/* 14 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 19 */     return this.name;
/*    */   }
/*    */   
/*    */   public ChestType getOpposite() {
/* 23 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: break; }  return 
/*    */ 
/*    */       
/* 26 */       LEFT;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/ChestType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */