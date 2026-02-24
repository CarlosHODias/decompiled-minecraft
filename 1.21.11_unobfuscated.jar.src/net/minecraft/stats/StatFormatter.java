/*    */ package net.minecraft.stats;
/*    */ import java.text.DecimalFormat;
/*    */ import java.text.DecimalFormatSymbols;
/*    */ import java.text.NumberFormat;
/*    */ import java.util.Locale;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public interface StatFormatter {
/*  9 */   public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("########0.00", DecimalFormatSymbols.getInstance(Locale.ROOT)); public static final StatFormatter DIVIDE_BY_TEN; public static final StatFormatter DISTANCE;
/*    */   public static final StatFormatter TIME;
/* 11 */   public static final StatFormatter DEFAULT = NumberFormat.getIntegerInstance(Locale.US)::format; static { Objects.requireNonNull(NumberFormat.getIntegerInstance(Locale.US)); } static {
/* 12 */     DIVIDE_BY_TEN = (value -> DECIMAL_FORMAT.format(value * 0.1D));
/* 13 */     DISTANCE = (cm -> {
/*    */         double meters = cm / 100.0D, kilometers = meters / 1000.0D;
/*    */ 
/*    */ 
/*    */ 
/*    */         
/*    */         return (kilometers > 0.5D) ? (DECIMAL_FORMAT.format(kilometers) + " km") : ((meters > 0.5D) ? (DECIMAL_FORMAT.format(meters) + " m") : ("" + cm + " cm"));
/*    */       });
/*    */ 
/*    */ 
/*    */     
/* 24 */     TIME = (value -> {
/*    */         double seconds = value / 20.0D, minutes = seconds / 60.0D, hours = minutes / 60.0D, days = hours / 24.0D, years = days / 365.0D;
/*    */         return (years > 0.5D) ? (DECIMAL_FORMAT.format(years) + " y") : ((days > 0.5D) ? (DECIMAL_FORMAT.format(days) + " d") : ((hours > 0.5D) ? (DECIMAL_FORMAT.format(hours) + " h") : ((minutes > 0.5D) ? (DECIMAL_FORMAT.format(minutes) + " min") : ("" + seconds + " s"))));
/*    */       });
/*    */   }
/*    */   
/*    */   String format(int paramInt);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/stats/StatFormatter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */