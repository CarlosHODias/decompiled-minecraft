/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class AirItem extends Item {
/*    */   public AirItem(Block block, Item.Properties properties) {
/*  8 */     super(properties);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Component getName(ItemStack itemStack) {
/* 14 */     return getName();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/AirItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */