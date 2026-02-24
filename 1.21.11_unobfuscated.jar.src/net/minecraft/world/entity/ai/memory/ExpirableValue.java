/*    */ package net.minecraft.world.entity.ai.memory;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.VisibleForDebug;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExpirableValue<T>
/*    */ {
/*    */   private final T value;
/*    */   private long timeToLive;
/*    */   
/*    */   public ExpirableValue(T value, long timeToLive) {
/* 18 */     this.value = value;
/* 19 */     this.timeToLive = timeToLive;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 23 */     if (canExpire()) {
/* 24 */       this.timeToLive--;
/*    */     }
/*    */   }
/*    */   
/*    */   public static <T> ExpirableValue<T> of(T value) {
/* 29 */     return new ExpirableValue<>(value, Long.MAX_VALUE);
/*    */   }
/*    */   
/*    */   public static <T> ExpirableValue<T> of(T value, long ticksUntilExpiry) {
/* 33 */     return new ExpirableValue<>(value, ticksUntilExpiry);
/*    */   }
/*    */   
/*    */   public long getTimeToLive() {
/* 37 */     return this.timeToLive;
/*    */   }
/*    */   
/*    */   public T getValue() {
/* 41 */     return this.value;
/*    */   }
/*    */   
/*    */   public boolean hasExpired() {
/* 45 */     return (this.timeToLive <= 0L);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 50 */     return String.valueOf(this.value) + String.valueOf(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   @VisibleForDebug
/*    */   public boolean canExpire() {
/* 56 */     return (this.timeToLive != Long.MAX_VALUE);
/*    */   }
/*    */   
/*    */   public static <T> Codec<ExpirableValue<T>> codec(Codec<T> valueCodec) {
/* 60 */     return RecordCodecBuilder.create(i -> i.group((App)valueCodec.fieldOf("value").forGetter(()), (App)Codec.LONG.lenientOptionalFieldOf("ttl").forGetter(())).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/memory/ExpirableValue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */