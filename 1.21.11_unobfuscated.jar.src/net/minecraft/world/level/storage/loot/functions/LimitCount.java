/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.IntRange;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class LimitCount extends LootItemConditionalFunction {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)IntRange.CODEC.fieldOf("limit").forGetter(())).apply((Applicative)i, LimitCount::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<LimitCount> CODEC;
/*    */   private final IntRange limiter;
/*    */   
/*    */   private LimitCount(List<LootItemCondition> predicates, IntRange limiter) {
/* 22 */     super(predicates);
/* 23 */     this.limiter = limiter;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<LimitCount> getType() {
/* 28 */     return LootItemFunctions.LIMIT_COUNT;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/* 33 */     return this.limiter.getReferencedContextParams();
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 38 */     int limit = this.limiter.clamp(context, itemStack.getCount());
/* 39 */     itemStack.setCount(limit);
/* 40 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> limitCount(IntRange limiter) {
/* 44 */     return simpleBuilder(conditions -> new LimitCount(conditions, limiter));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/LimitCount.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */