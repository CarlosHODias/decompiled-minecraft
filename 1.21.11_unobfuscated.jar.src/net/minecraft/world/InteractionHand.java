/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ 
/*    */ public enum InteractionHand {
/*  6 */   MAIN_HAND,
/*  7 */   OFF_HAND;
/*    */ 
/*    */   
/*    */   public EquipmentSlot asEquipmentSlot() {
/* 11 */     return (this == MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/InteractionHand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */