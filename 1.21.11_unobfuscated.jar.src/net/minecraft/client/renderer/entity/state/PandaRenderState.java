/*   */ package net.minecraft.client.renderer.entity.state;
/*   */ 
/*   */ import net.minecraft.world.entity.animal.panda.Panda;
/*   */ 
/*   */ public class PandaRenderState extends HoldingEntityRenderState {
/* 6 */   public Panda.Gene variant = Panda.Gene.NORMAL;
/*   */   public boolean isUnhappy;
/*   */   public boolean isSneezing;
/*   */   public int sneezeTime;
/*   */   public boolean isEating;
/*   */   public boolean isScared;
/*   */   public boolean isSitting;
/*   */   public float sitAmount;
/*   */   public float lieOnBackAmount;
/*   */   public float rollAmount;
/*   */   public float rollTime;
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/PandaRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */