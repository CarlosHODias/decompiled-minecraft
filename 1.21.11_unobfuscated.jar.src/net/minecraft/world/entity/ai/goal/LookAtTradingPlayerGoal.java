/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.npc.villager.AbstractVillager;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class LookAtTradingPlayerGoal extends LookAtPlayerGoal {
/*    */   public LookAtTradingPlayerGoal(AbstractVillager villager) {
/* 10 */     super((Mob)villager, (Class)Player.class, 8.0F);
/* 11 */     this.villager = villager;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 16 */     if (this.villager.isTrading()) {
/* 17 */       this.lookAt = (Entity)this.villager.getTradingPlayer();
/* 18 */       return true;
/*    */     } 
/* 20 */     return false;
/*    */   }
/*    */   
/*    */   private final AbstractVillager villager;
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/goal/LookAtTradingPlayerGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */