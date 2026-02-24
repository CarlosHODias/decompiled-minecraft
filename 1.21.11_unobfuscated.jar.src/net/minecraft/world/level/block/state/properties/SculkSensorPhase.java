/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum SculkSensorPhase implements StringRepresentable {
/*  6 */   INACTIVE("inactive"),
/*  7 */   ACTIVE("active"),
/*  8 */   COOLDOWN("cooldown");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   SculkSensorPhase(String name) {
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


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/SculkSensorPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */