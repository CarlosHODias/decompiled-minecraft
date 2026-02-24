/*    */ package net.minecraft.world.item.slot;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class LimitSlotSource extends TransformedSlotSource {
/*    */   static {
/*  8 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)ExtraCodecs.POSITIVE_INT.fieldOf("limit").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, LimitSlotSource::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<LimitSlotSource> MAP_CODEC;
/*    */   private final int limit;
/*    */   
/*    */   private LimitSlotSource(SlotSource slotSource, int limit) {
/* 15 */     super(slotSource);
/* 16 */     this.limit = limit;
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<LimitSlotSource> codec() {
/* 21 */     return MAP_CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SlotCollection transform(SlotCollection slots) {
/* 26 */     return slots.limit(this.limit);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/slot/LimitSlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */