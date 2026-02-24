/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class Zone
/*    */   implements AutoCloseable
/*    */ {
/*  8 */   public static final Zone INACTIVE = new Zone(null);
/*    */   
/*    */   private final ProfilerFiller profiler;
/*    */   
/*    */   Zone(ProfilerFiller profiler) {
/* 13 */     this.profiler = profiler;
/*    */   }
/*    */   
/*    */   public Zone addText(String text) {
/* 17 */     if (this.profiler != null) {
/* 18 */       this.profiler.addZoneText(text);
/*    */     }
/* 20 */     return this;
/*    */   }
/*    */   
/*    */   public Zone addText(Supplier<String> text) {
/* 24 */     if (this.profiler != null) {
/* 25 */       this.profiler.addZoneText(text.get());
/*    */     }
/* 27 */     return this;
/*    */   }
/*    */   
/*    */   public Zone addValue(long value) {
/* 31 */     if (this.profiler != null) {
/* 32 */       this.profiler.addZoneValue(value);
/*    */     }
/* 34 */     return this;
/*    */   }
/*    */   
/*    */   public Zone setColor(int color) {
/* 38 */     if (this.profiler != null) {
/* 39 */       this.profiler.setZoneColor(color);
/*    */     }
/* 41 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 46 */     if (this.profiler != null)
/* 47 */       this.profiler.pop(); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/Zone.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */