package net.minecraft.client.gui.font.glyphs;

import com.mojang.blaze3d.font.GlyphInfo;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.network.chat.Style;

public interface BakedGlyph {
  GlyphInfo info();
  
  TextRenderable.Styled createGlyph(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2, Style paramStyle, float paramFloat3, float paramFloat4);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/glyphs/BakedGlyph.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */