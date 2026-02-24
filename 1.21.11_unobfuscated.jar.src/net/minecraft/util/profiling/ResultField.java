/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ public final class ResultField implements Comparable<ResultField> {
/*    */   public final double percentage;
/*    */   public final double globalPercentage;
/*    */   public final long count;
/*    */   public final String name;
/*    */   
/*    */   public ResultField(String name, double percentage, double globalPercentage, long count) {
/* 10 */     this.name = name;
/* 11 */     this.percentage = percentage;
/* 12 */     this.globalPercentage = globalPercentage;
/* 13 */     this.count = count;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(ResultField resultField) {
/* 18 */     if (resultField.percentage < this.percentage) {
/* 19 */       return -1;
/*    */     }
/* 21 */     if (resultField.percentage > this.percentage) {
/* 22 */       return 1;
/*    */     }
/* 24 */     return resultField.name.compareTo(this.name);
/*    */   }
/*    */   
/*    */   public int getColor() {
/* 28 */     return (this.name.hashCode() & 0xAAAAAA) + -12303292;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/ResultField.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */