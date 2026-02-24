/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public class LootItemKilledByPlayerCondition
/*    */   implements LootItemCondition {
/* 11 */   private static final LootItemKilledByPlayerCondition INSTANCE = new LootItemKilledByPlayerCondition();
/* 12 */   public static final MapCodec<LootItemKilledByPlayerCondition> CODEC = MapCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 19 */     return LootItemConditions.KILLED_BY_PLAYER;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 24 */     return Set.of(LootContextParams.LAST_DAMAGE_PLAYER);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext context) {
/* 29 */     return context.hasParameter(LootContextParams.LAST_DAMAGE_PLAYER);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder killedByPlayer() {
/* 33 */     return () -> INSTANCE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/predicates/LootItemKilledByPlayerCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */