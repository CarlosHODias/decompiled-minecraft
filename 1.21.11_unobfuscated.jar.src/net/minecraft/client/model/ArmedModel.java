package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.HumanoidArm;

public interface ArmedModel<T extends net.minecraft.client.renderer.entity.state.EntityRenderState> {
  void translateToHand(T paramT, HumanoidArm paramHumanoidArm, PoseStack paramPoseStack);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/ArmedModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */