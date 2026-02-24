/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum SideChainPart implements StringRepresentable {
/*  6 */   UNCONNECTED("unconnected"),
/*  7 */   RIGHT("right"),
/*  8 */   CENTER("center"),
/*  9 */   LEFT("left");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   SideChainPart(String name) {
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
/* 28 */     return (this != UNCONNECTED);
/*    */   }
/*    */   
/*    */   public boolean isConnectionTowards(SideChainPart endPart) {
/* 32 */     return (this == CENTER || this == endPart);
/*    */   }
/*    */   
/*    */   public boolean isChainEnd() {
/* 36 */     return (this != CENTER);
/*    */   }
/*    */   
/*    */   public SideChainPart whenConnectedToTheRight() {
/* 40 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 3: case 1: case 2: break; }  return 
/*    */       
/* 42 */       CENTER;
/*    */   }
/*    */ 
/*    */   
/*    */   public SideChainPart whenConnectedToTheLeft() {
/* 47 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: case 3: break; }  return 
/*    */       
/* 49 */       CENTER;
/*    */   }
/*    */ 
/*    */   
/*    */   public SideChainPart whenDisconnectedFromTheRight() {
/* 54 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 3: case 1: case 2: break; }  return 
/*    */       
/* 56 */       RIGHT;
/*    */   }
/*    */ 
/*    */   
/*    */   public SideChainPart whenDisconnectedFromTheLeft() {
/* 61 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: case 3: break; }  return 
/*    */       
/* 63 */       LEFT;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/SideChainPart.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */