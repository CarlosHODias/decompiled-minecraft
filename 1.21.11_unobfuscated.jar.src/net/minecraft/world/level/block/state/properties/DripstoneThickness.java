/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum DripstoneThickness implements StringRepresentable {
/*  6 */   TIP_MERGE("tip_merge"),
/*  7 */   TIP("tip"),
/*  8 */   FRUSTUM("frustum"),
/*  9 */   MIDDLE("middle"),
/* 10 */   BASE("base");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   DripstoneThickness(String name) {
/* 15 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 20 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 25 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/DripstoneThickness.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */