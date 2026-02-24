package net.minecraft.client.gui;

import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.util.RandomSource;

public interface GlyphSource {
  BakedGlyph getGlyph(int paramInt);
  
  BakedGlyph getRandomGlyph(RandomSource paramRandomSource, int paramInt);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/GlyphSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */