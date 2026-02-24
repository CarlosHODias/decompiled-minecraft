/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.world.item.slot.SlotCollection;
/*    */ 
/*    */ 
/*    */ public interface SlotProvider
/*    */ {
/*    */   SlotAccess getSlot(int paramInt);
/*    */   
/*    */   default SlotCollection getSlotsFromRange(IntList slots) {
/* 14 */     List<SlotAccess> slotList = slots.intStream()
/* 15 */       .<SlotAccess>mapToObj(this::getSlot)
/* 16 */       .filter(Objects::nonNull)
/* 17 */       .toList();
/* 18 */     return SlotCollection.of(slotList);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/SlotProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */