/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public interface DisplayContentsFactory<T> {
/*    */   public static interface ForRemainders<T>
/*    */     extends DisplayContentsFactory<T> {
/*    */     T addRemainder(T param1T, List<T> param1List);
/*    */   }
/*    */   
/*    */   public static interface ForStacks<T> extends DisplayContentsFactory<T> {
/*    */     default T forStack(Holder<Item> item) {
/* 17 */       return forStack(new ItemStack(item));
/*    */     }
/*    */     
/*    */     default T forStack(Item item) {
/* 21 */       return forStack(new ItemStack((ItemLike)item));
/*    */     }
/*    */     
/*    */     T forStack(ItemStack param1ItemStack);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/display/DisplayContentsFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */