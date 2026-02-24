/*    */ package net.minecraft.advancements;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.server.PlayerAdvancements;
/*    */ 
/*    */ public interface CriterionTrigger<T extends CriterionTriggerInstance> {
/*    */   void addPlayerListener(PlayerAdvancements paramPlayerAdvancements, Listener<T> paramListener);
/*    */   
/*    */   void removePlayerListener(PlayerAdvancements paramPlayerAdvancements, Listener<T> paramListener);
/*    */   
/*    */   void removePlayerListeners(PlayerAdvancements paramPlayerAdvancements);
/*    */   
/*    */   Codec<T> codec();
/*    */   
/*    */   default Criterion<T> createCriterion(T instance) {
/* 16 */     return new Criterion<>(this, instance);
/*    */   }
/*    */   public static final class Listener<T extends CriterionTriggerInstance> extends Record { private final T trigger; private final AdvancementHolder advancement; private final String criterion;
/* 19 */     public Listener(T trigger, AdvancementHolder advancement, String criterion) { this.trigger = trigger; this.advancement = advancement; this.criterion = criterion; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/CriterionTrigger$Listener;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #19	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/CriterionTrigger$Listener;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 19 */       //   0	7	0	this	Lnet/minecraft/advancements/CriterionTrigger$Listener<TT;>; } public T trigger() { return this.trigger; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/CriterionTrigger$Listener;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #19	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/CriterionTrigger$Listener;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/CriterionTrigger$Listener<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/CriterionTrigger$Listener;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #19	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/CriterionTrigger$Listener;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 19 */       //   0	8	0	this	Lnet/minecraft/advancements/CriterionTrigger$Listener<TT;>; } public AdvancementHolder advancement() { return this.advancement; } public String criterion() { return this.criterion; }
/*    */      public void run(PlayerAdvancements player) {
/* 21 */       player.award(this.advancement, this.criterion);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/CriterionTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */