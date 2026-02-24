/*    */ package net.minecraft.world.level.levelgen.synth;
/*    */ 
/*    */ import java.util.Locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoiseUtils
/*    */ {
/*    */   public static double biasTowardsExtreme(double noise, double factor) {
/* 14 */     return noise + Math.sin(Math.PI * noise) * factor / Math.PI;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void parityNoiseOctaveConfigString(StringBuilder sb, double xo, double yo, double zo, byte[] p) {
/* 19 */     sb.append(String.format(Locale.ROOT, "xo=%.3f, yo=%.3f, zo=%.3f, p0=%d, p255=%d", new Object[] { (float)xo, (float)yo, (float)zo, p[0], p[255] }));
/*    */   }
/*    */ 
/*    */   
/*    */   public static void parityNoiseOctaveConfigString(StringBuilder sb, double xo, double yo, double zo, int[] p) {
/* 24 */     sb.append(String.format(Locale.ROOT, "xo=%.3f, yo=%.3f, zo=%.3f, p0=%d, p255=%d", new Object[] { (float)xo, (float)yo, (float)zo, p[0], p[255] }));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/synth/NoiseUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */