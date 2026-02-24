/*    */ package net.minecraft.world.level.pathfinder;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public class Target extends Node {
/*  6 */   private float bestHeuristic = Float.MAX_VALUE;
/*    */   private Node bestNode;
/*    */   private boolean reached;
/*    */   
/*    */   public Target(Node node) {
/* 11 */     super(node.x, node.y, node.z);
/*    */   }
/*    */   
/*    */   public Target(int x, int y, int z) {
/* 15 */     super(x, y, z);
/*    */   }
/*    */   
/*    */   public void updateBest(float heuristic, Node node) {
/* 19 */     if (heuristic < this.bestHeuristic) {
/* 20 */       this.bestHeuristic = heuristic;
/* 21 */       this.bestNode = node;
/*    */     } 
/*    */   }
/*    */   
/*    */   public Node getBestNode() {
/* 26 */     return this.bestNode;
/*    */   }
/*    */   
/*    */   public void setReached() {
/* 30 */     this.reached = true;
/*    */   }
/*    */   
/*    */   public boolean isReached() {
/* 34 */     return this.reached;
/*    */   }
/*    */   
/*    */   public static Target createFromStream(FriendlyByteBuf buffer) {
/* 38 */     Target node = new Target(buffer.readInt(), buffer.readInt(), buffer.readInt());
/* 39 */     readContents(buffer, node);
/* 40 */     return node;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/pathfinder/Target.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */