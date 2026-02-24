/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public class SequenceFunction implements LootItemFunction {
/*    */   public static final com.mojang.serialization.MapCodec<SequenceFunction> CODEC;
/*    */   
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LootItemFunctions.TYPED_CODEC.listOf().fieldOf("functions").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, SequenceFunction::new));
/*    */ 
/*    */ 
/*    */     
/* 19 */     INLINE_CODEC = LootItemFunctions.TYPED_CODEC.listOf().xmap(SequenceFunction::new, f -> f.functions);
/*    */   }
/*    */   public static final com.mojang.serialization.Codec<SequenceFunction> INLINE_CODEC; private final List<LootItemFunction> functions;
/*    */   private final java.util.function.BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
/*    */   
/*    */   private SequenceFunction(List<LootItemFunction> functions) {
/* 25 */     this.functions = functions;
/* 26 */     this.compositeFunction = LootItemFunctions.compose((List)functions);
/*    */   }
/*    */   
/*    */   public static SequenceFunction of(List<LootItemFunction> functions) {
/* 30 */     return new SequenceFunction(List.copyOf(functions));
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack apply(ItemStack stack, LootContext context) {
/* 35 */     return this.compositeFunction.apply(stack, context);
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext output) {
/* 40 */     super.validate(output);
/*    */     
/* 42 */     for (int i = 0; i < this.functions.size(); i++) {
/* 43 */       ((LootItemFunction)this.functions.get(i)).validate(output.forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedFieldPathElement("functions", i)));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SequenceFunction> getType() {
/* 49 */     return LootItemFunctions.SEQUENCE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/SequenceFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */