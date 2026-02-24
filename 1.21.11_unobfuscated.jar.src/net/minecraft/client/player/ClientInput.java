/*    */ package net.minecraft.client.player;
/*    */ 
/*    */ import net.minecraft.world.entity.player.Input;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ 
/*    */ public class ClientInput
/*    */ {
/*  8 */   public Input keyPresses = Input.EMPTY;
/*  9 */   protected Vec2 moveVector = Vec2.ZERO;
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public Vec2 getMoveVector() {
/* 18 */     return this.moveVector;
/*    */   }
/*    */   
/*    */   public boolean hasForwardImpulse() {
/* 22 */     return (this.moveVector.y > 1.0E-5F);
/*    */   }
/*    */   
/*    */   public void makeJump() {
/* 26 */     this.keyPresses = new Input(this.keyPresses.forward(), this.keyPresses.backward(), this.keyPresses.left(), this.keyPresses.right(), true, this.keyPresses.shift(), this.keyPresses.sprint());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/player/ClientInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */