/*    */ package net.minecraft.world.entity.ai.control;
/*    */ 
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class JumpControl implements Control {
/*    */   private final Mob mob;
/*    */   protected boolean jump;
/*    */   
/*    */   public JumpControl(Mob mob) {
/* 10 */     this.mob = mob;
/*    */   }
/*    */   
/*    */   public void jump() {
/* 14 */     this.jump = true;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 18 */     this.mob.setJumping(this.jump);
/* 19 */     this.jump = false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/control/JumpControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */