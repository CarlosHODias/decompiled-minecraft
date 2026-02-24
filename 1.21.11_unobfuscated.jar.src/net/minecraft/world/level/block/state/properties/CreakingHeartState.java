/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum CreakingHeartState implements StringRepresentable {
/*  6 */   UPROOTED("uprooted"),
/*  7 */   DORMANT("dormant"),
/*  8 */   AWAKE("awake");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/*    */   CreakingHeartState(String name) {
/* 14 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 19 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 24 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/CreakingHeartState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */