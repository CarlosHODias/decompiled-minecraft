/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*    */ 
/*    */ public class StartHuntingHoglin {
/*    */   public static net.minecraft.world.entity.ai.behavior.OneShot<Piglin> create() {
/* 10 */     return BehaviorBuilder.create(i -> i.group((App)i.present(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN), (App)i.absent(MemoryModuleType.ANGRY_AT), (App)i.absent(MemoryModuleType.HUNTED_RECENTLY), (App)i.registered(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS)).apply((com.mojang.datafixers.kinds.Applicative)i, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean hasHuntedRecently(AbstractPiglin otherPiglin) {
/* 33 */     return otherPiglin.getBrain().hasMemoryValue(MemoryModuleType.HUNTED_RECENTLY);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/monster/piglin/StartHuntingHoglin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */