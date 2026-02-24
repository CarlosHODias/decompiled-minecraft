package com.mojang.blaze3d.font;

import net.minecraft.client.gui.font.glyphs.BakedGlyph;

public interface UnbakedGlyph {
  GlyphInfo info();
  
  BakedGlyph bake(Stitcher paramStitcher);
  
  public static interface Stitcher {
    BakedGlyph stitch(GlyphInfo param1GlyphInfo, GlyphBitmap param1GlyphBitmap);
    
    BakedGlyph getMissing();
  }
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/font/UnbakedGlyph.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */