/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class MultipliedFloats
/*    */   implements SampledFloat {
/*    */   private final SampledFloat[] values;
/*    */   
/*    */   public MultipliedFloats(SampledFloat... values) {
/* 11 */     this.values = values;
/*    */   }
/*    */ 
/*    */   
/*    */   public float sample(RandomSource random) {
/* 16 */     float result = 1.0F;
/* 17 */     for (SampledFloat value : this.values) {
/* 18 */       result *= value.sample(random);
/*    */     }
/* 20 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 25 */     return "MultipliedFloats" + Arrays.toString((Object[])this.values);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/valueproviders/MultipliedFloats.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */