/*   */ package net.minecraft.client.renderer.entity.state;
/*   */ 
/*   */ import net.minecraft.client.model.animal.parrot.ParrotModel;
/*   */ import net.minecraft.world.entity.animal.parrot.Parrot;
/*   */ 
/*   */ public class ParrotRenderState extends LivingEntityRenderState {
/* 7 */   public Parrot.Variant variant = Parrot.Variant.RED_BLUE;
/*   */   public float flapAngle;
/* 9 */   public ParrotModel.Pose pose = ParrotModel.Pose.FLYING;
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/ParrotRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */