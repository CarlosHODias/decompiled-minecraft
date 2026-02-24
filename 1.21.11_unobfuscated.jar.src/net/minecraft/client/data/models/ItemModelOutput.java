/*   */ package net.minecraft.client.data.models;
/*   */ 
/*   */ import net.minecraft.client.renderer.item.ClientItem;
/*   */ import net.minecraft.client.renderer.item.ItemModel;
/*   */ import net.minecraft.world.item.Item;
/*   */ 
/*   */ public interface ItemModelOutput {
/*   */   default void accept(Item item, ItemModel.Unbaked generator) {
/* 9 */     accept(item, generator, ClientItem.Properties.DEFAULT);
/*   */   }
/*   */   
/*   */   void accept(Item paramItem, ItemModel.Unbaked paramUnbaked, ClientItem.Properties paramProperties);
/*   */   
/*   */   void copy(Item paramItem1, Item paramItem2);
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/models/ItemModelOutput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */