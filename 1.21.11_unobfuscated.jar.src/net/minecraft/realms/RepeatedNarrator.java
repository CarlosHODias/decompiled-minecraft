/*    */ package net.minecraft.realms;
/*    */ 
/*    */ import com.google.common.util.concurrent.RateLimiter;
/*    */ import java.time.Duration;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ import net.minecraft.client.GameNarrator;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ 
/*    */ public class RepeatedNarrator
/*    */ {
/*    */   private final float permitsPerSecond;
/* 13 */   private final AtomicReference<Params> params = new AtomicReference<>();
/*    */   
/*    */   public RepeatedNarrator(Duration repeatDelay) {
/* 16 */     this.permitsPerSecond = 1000.0F / (float)repeatDelay.toMillis();
/*    */   }
/*    */   
/*    */   public void narrate(GameNarrator narrator, Component narration) {
/* 20 */     Params params = this.params.updateAndGet(existing -> 
/* 21 */         (narration == null || !narration.equals(narration.narration)) ? new Params(narration, RateLimiter.create(this.permitsPerSecond)) : narration);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 26 */     if (params.rateLimiter.tryAcquire(1))
/* 27 */       narrator.saySystemNow(narration); 
/*    */   }
/*    */   
/*    */   private static class Params
/*    */   {
/*    */     private final Component narration;
/*    */     private final RateLimiter rateLimiter;
/*    */     
/*    */     Params(Component narration, RateLimiter rateLimiter) {
/* 36 */       this.narration = narration;
/* 37 */       this.rateLimiter = rateLimiter;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/realms/RepeatedNarrator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */