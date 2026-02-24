/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public abstract class CompositeSlotSource implements SlotSource {
/*    */   protected final List<SlotSource> terms;
/*    */   
/*    */   protected CompositeSlotSource(List<SlotSource> terms) {
/* 18 */     this.terms = terms;
/* 19 */     this.compositeSlotSource = SlotSources.group(terms);
/*    */   }
/*    */   private final Function<LootContext, SlotCollection> compositeSlotSource;
/*    */   protected static <T extends CompositeSlotSource> MapCodec<T> createCodec(Function<List<SlotSource>, T> factory) {
/* 23 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)SlotSources.CODEC.listOf().fieldOf("terms").forGetter(())).apply((Applicative)i, factory));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected static <T extends CompositeSlotSource> Codec<T> createInlineCodec(Function<List<SlotSource>, T> factory) {
/* 29 */     return SlotSources.CODEC.listOf().xmap(factory, t -> t.terms);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SlotCollection provide(LootContext context) {
/* 37 */     return this.compositeSlotSource.apply(context);
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 42 */     super.validate(context);
/*    */     
/* 44 */     for (int i = 0; i < this.terms.size(); i++)
/* 45 */       ((SlotSource)this.terms.get(i)).validate(context.forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedFieldPathElement("terms", i))); 
/*    */   }
/*    */   
/*    */   public abstract MapCodec<? extends CompositeSlotSource> codec();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/slot/CompositeSlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */