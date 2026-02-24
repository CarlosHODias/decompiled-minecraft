/*    */ package net.minecraft.util.profiling.jfr;
/*    */ 
/*    */ import com.google.common.math.Quantiles;
/*    */ import it.unimi.dsi.fastutil.ints.Int2DoubleRBTreeMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2DoubleSortedMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2DoubleSortedMaps;
/*    */ import java.util.Comparator;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class Percentiles
/*    */ {
/* 13 */   public static final Quantiles.ScaleAndIndexes DEFAULT_INDEXES = Quantiles.scale(100).indexes(new int[] { 50, 75, 90, 99 });
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Map<Integer, Double> evaluate(long[] dataset) {
/* 19 */     return (dataset.length == 0) ? Map.<Integer, Double>of() : sorted(DEFAULT_INDEXES.compute(dataset));
/*    */   }
/*    */   
/*    */   public static Map<Integer, Double> evaluate(int[] dataset) {
/* 23 */     return (dataset.length == 0) ? Map.<Integer, Double>of() : sorted(DEFAULT_INDEXES.compute(dataset));
/*    */   }
/*    */   
/*    */   public static Map<Integer, Double> evaluate(double[] dataset) {
/* 27 */     return (dataset.length == 0) ? Map.<Integer, Double>of() : sorted(DEFAULT_INDEXES.compute(dataset));
/*    */   }
/*    */   
/*    */   private static Map<Integer, Double> sorted(Map<Integer, Double> percentiles) {
/* 31 */     Int2DoubleSortedMap sorted = (Int2DoubleSortedMap)Util.make(new Int2DoubleRBTreeMap(Comparator.reverseOrder()), it -> it.putAll(percentiles));
/* 32 */     return (Map<Integer, Double>)Int2DoubleSortedMaps.unmodifiable(sorted);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/jfr/Percentiles.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */