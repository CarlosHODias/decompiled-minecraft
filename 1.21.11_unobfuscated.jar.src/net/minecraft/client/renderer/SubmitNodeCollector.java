package net.minecraft.client.renderer;

import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.feature.ParticleFeatureRenderer;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureManager;

public interface SubmitNodeCollector extends OrderedSubmitNodeCollector {
  OrderedSubmitNodeCollector order(int paramInt);
  
  public static interface ParticleGroupRenderer {
    QuadParticleRenderState.PreparedBuffers prepare(ParticleFeatureRenderer.ParticleBufferCache param1ParticleBufferCache);
    
    void render(QuadParticleRenderState.PreparedBuffers param1PreparedBuffers, ParticleFeatureRenderer.ParticleBufferCache param1ParticleBufferCache, RenderPass param1RenderPass, TextureManager param1TextureManager, boolean param1Boolean);
  }
  
  public static interface CustomGeometryRenderer {
    void render(PoseStack.Pose param1Pose, VertexConsumer param1VertexConsumer);
  }
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/SubmitNodeCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */