/*    */ package net.minecraft.world.item.slot;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulator;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulators;
/*    */ 
/*    */ public class ContentsSlotSource extends TransformedSlotSource {
/*    */   static {
/*  9 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)ContainerComponentManipulators.CODEC.fieldOf("component").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, ContentsSlotSource::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<ContentsSlotSource> MAP_CODEC;
/*    */   private final ContainerComponentManipulator<?> component;
/*    */   
/*    */   private ContentsSlotSource(SlotSource slotSource, ContainerComponentManipulator<?> component) {
/* 16 */     super(slotSource);
/* 17 */     this.component = component;
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<ContentsSlotSource> codec() {
/* 22 */     return MAP_CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SlotCollection transform(SlotCollection slots) {
/* 27 */     java.util.Objects.requireNonNull(this.component); return slots.flatMap(this.component::getSlots);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/slot/ContentsSlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */