/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public abstract class TransformedSlotSource
/*    */   implements SlotSource {
/*    */   protected TransformedSlotSource(SlotSource slotSource) {
/* 14 */     this.slotSource = slotSource;
/*    */   }
/*    */ 
/*    */   
/*    */   protected final SlotSource slotSource;
/*    */   
/*    */   protected static <T extends TransformedSlotSource> Products.P1<RecordCodecBuilder.Mu<T>, SlotSource> commonFields(RecordCodecBuilder.Instance<T> i) {
/* 21 */     return i.group((App)
/* 22 */         SlotSources.CODEC.fieldOf("slot_source").forGetter(t -> t.slotSource));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final SlotCollection provide(LootContext context) {
/* 30 */     return transform(this.slotSource.provide(context));
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 35 */     super.validate(context);
/* 36 */     this.slotSource.validate(context.forChild((ProblemReporter.PathElement)new ProblemReporter.FieldPathElement("slot_source")));
/*    */   }
/*    */   
/*    */   public abstract MapCodec<? extends TransformedSlotSource> codec();
/*    */   
/*    */   protected abstract SlotCollection transform(SlotCollection paramSlotCollection);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/slot/TransformedSlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */