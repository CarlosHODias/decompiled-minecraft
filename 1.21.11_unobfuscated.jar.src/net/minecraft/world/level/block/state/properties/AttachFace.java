/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum AttachFace implements StringRepresentable {
/*  6 */   FLOOR("floor"),
/*  7 */   WALL("wall"),
/*  8 */   CEILING("ceiling");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/*    */   AttachFace(String name) {
/* 14 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 19 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/AttachFace.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */