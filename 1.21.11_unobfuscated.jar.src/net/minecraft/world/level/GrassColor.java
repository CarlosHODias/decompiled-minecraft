/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ public class GrassColor {
/*  4 */   private static int[] pixels = new int[65536];
/*    */   
/*    */   public static void init(int[] pixels) {
/*  7 */     GrassColor.pixels = pixels;
/*    */   }
/*    */   
/*    */   public static int get(double temp, double rain) {
/* 11 */     return ColorMapColorUtil.get(temp, rain, pixels, -65281);
/*    */   }
/*    */   
/*    */   public static int getDefaultColor() {
/* 15 */     return get(0.5D, 1.0D);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/GrassColor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */