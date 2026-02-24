package net.minecraft.client.gui.render.state;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;

public interface GuiElementRenderState extends ScreenArea {
  void buildVertices(VertexConsumer paramVertexConsumer);
  
  RenderPipeline pipeline();
  
  TextureSetup textureSetup();
  
  ScreenRectangle scissorArea();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/state/GuiElementRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */