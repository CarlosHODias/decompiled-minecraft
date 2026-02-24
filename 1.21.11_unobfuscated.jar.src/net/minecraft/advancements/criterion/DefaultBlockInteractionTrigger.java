/*    */ package net.minecraft.advancements.criterion;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootParams;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public class DefaultBlockInteractionTrigger extends SimpleCriterionTrigger<DefaultBlockInteractionTrigger.TriggerInstance> {
/*    */   public Codec<TriggerInstance> codec() {
/* 19 */     return TriggerInstance.CODEC;
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer player, BlockPos pos) {
/* 23 */     ServerLevel level = player.level();
/* 24 */     BlockState state = level.getBlockState(pos);
/* 25 */     LootParams params = new LootParams.Builder(level)
/* 26 */       .withParameter(LootContextParams.ORIGIN, pos.getCenter())
/* 27 */       .withParameter(LootContextParams.THIS_ENTITY, player)
/* 28 */       .withParameter(LootContextParams.BLOCK_STATE, state)
/* 29 */       .create(LootContextParamSets.BLOCK_USE);
/* 30 */     LootContext context = new LootContext.Builder(params).create(Optional.empty());
/*    */     
/* 32 */     trigger(player, t -> t.matches(context));
/*    */   }
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final Optional<ContextAwarePredicate> location; public static final Codec<TriggerInstance> CODEC;
/* 35 */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> location) { this.player = player; this.location = location; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/DefaultBlockInteractionTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #35	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 35 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/DefaultBlockInteractionTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/DefaultBlockInteractionTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #35	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/DefaultBlockInteractionTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/DefaultBlockInteractionTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #35	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/DefaultBlockInteractionTrigger$TriggerInstance;
/* 35 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<ContextAwarePredicate> location() { return this.location; }
/*    */ 
/*    */     
/*    */     static {
/* 39 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)ContextAwarePredicate.CODEC.optionalFieldOf("location").forGetter(TriggerInstance::location)).apply((Applicative)i, TriggerInstance::new));
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public boolean matches(LootContext locationContext) {
/* 45 */       return (this.location.isEmpty() || ((ContextAwarePredicate)this.location.get()).matches(locationContext));
/*    */     }
/*    */ 
/*    */     
/*    */     public void validate(CriterionValidator validator) {
/* 50 */       super.validate(validator);
/* 51 */       this.location.ifPresent(predicate -> validator.validate(predicate, LootContextParamSets.BLOCK_USE, "location"));
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/DefaultBlockInteractionTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */