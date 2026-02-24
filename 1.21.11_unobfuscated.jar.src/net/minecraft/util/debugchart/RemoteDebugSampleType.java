/*    */ package net.minecraft.util.debugchart;
/*    */ 
/*    */ import net.minecraft.util.debug.DebugSubscription;
/*    */ import net.minecraft.util.debug.DebugSubscriptions;
/*    */ 
/*    */ public enum RemoteDebugSampleType {
/*  7 */   TICK_TIME(DebugSubscriptions.DEDICATED_SERVER_TICK_TIME);
/*    */   
/*    */   private final DebugSubscription<?> subscription;
/*    */ 
/*    */   
/*    */   RemoteDebugSampleType(DebugSubscription<?> subscription) {
/* 13 */     this.subscription = subscription;
/*    */   }
/*    */   
/*    */   public DebugSubscription<?> subscription() {
/* 17 */     return this.subscription;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/debugchart/RemoteDebugSampleType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */