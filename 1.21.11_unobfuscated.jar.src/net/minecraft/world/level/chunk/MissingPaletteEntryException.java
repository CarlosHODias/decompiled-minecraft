/*   */ package net.minecraft.world.level.chunk;
/*   */ 
/*   */ public class MissingPaletteEntryException
/*   */   extends RuntimeException {
/*   */   public MissingPaletteEntryException(int index) {
/* 6 */     super("Missing Palette entry for index " + index + ".");
/*   */   }
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/MissingPaletteEntryException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */