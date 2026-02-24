/*    */ package net.minecraft.world.level.gamerules;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum GameRuleType implements StringRepresentable {
/*  6 */   INT("integer"),
/*  7 */   BOOL("boolean");
/*    */   private final String name;
/*    */   
/*    */   GameRuleType(String name) {
/* 11 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 16 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/gamerules/GameRuleType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */