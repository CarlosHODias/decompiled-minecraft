/*    */ package net.minecraft.world.entity.ai.behavior.declarative;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.K1;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MemoryAccessor<F extends K1, Value>
/*    */ {
/*    */   private final Brain<?> brain;
/*    */   private final MemoryModuleType<Value> memoryType;
/*    */   private final App<F, Value> value;
/*    */   
/*    */   public MemoryAccessor(Brain<?> brain, MemoryModuleType<Value> memoryType, App<F, Value> value) {
/* 20 */     this.brain = brain;
/* 21 */     this.memoryType = memoryType;
/* 22 */     this.value = value;
/*    */   }
/*    */   
/*    */   public App<F, Value> value() {
/* 26 */     return this.value;
/*    */   }
/*    */   
/*    */   public void set(Value value) {
/* 30 */     this.brain.setMemory(this.memoryType, Optional.of(value));
/*    */   }
/*    */   
/*    */   public void setOrErase(Optional<Value> value) {
/* 34 */     this.brain.setMemory(this.memoryType, value);
/*    */   }
/*    */   
/*    */   public void setWithExpiry(Value value, long timeToLive) {
/* 38 */     this.brain.setMemoryWithExpiry(this.memoryType, value, timeToLive);
/*    */   }
/*    */   
/*    */   public void erase() {
/* 42 */     this.brain.eraseMemory(this.memoryType);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/declarative/MemoryAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */