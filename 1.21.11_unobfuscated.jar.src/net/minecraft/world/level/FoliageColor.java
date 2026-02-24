/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ public class FoliageColor
/*    */ {
/*    */   public static final int FOLIAGE_EVERGREEN = -10380959;
/*    */   public static final int FOLIAGE_BIRCH = -8345771;
/*    */   public static final int FOLIAGE_DEFAULT = -12012264;
/*    */   public static final int FOLIAGE_MANGROVE = -7158200;
/*  9 */   private static int[] pixels = new int[65536];
/*    */   
/*    */   public static void init(int[] pixels) {
/* 12 */     FoliageColor.pixels = pixels;
/*    */   }
/*    */   
/*    */   public static int get(double temp, double rain) {
/* 16 */     return ColorMapColorUtil.get(temp, rain, pixels, -12012264);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/FoliageColor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */