/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.world.entity.npc.villager.VillagerData;
/*    */ 
/*    */ public class ZombieVillagerRenderState
/*    */   extends ZombieRenderState
/*    */   implements VillagerDataHolderRenderState {
/*    */   public VillagerData villagerData;
/*    */   
/*    */   public VillagerData getVillagerData() {
/* 11 */     return this.villagerData;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/ZombieVillagerRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */