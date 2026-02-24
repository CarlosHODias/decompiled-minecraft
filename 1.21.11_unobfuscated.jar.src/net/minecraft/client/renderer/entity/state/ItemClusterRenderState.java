/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ItemClusterRenderState extends EntityRenderState {
/* 11 */   public final ItemStackRenderState item = new ItemStackRenderState();
/*    */   public int count;
/*    */   public int seed;
/*    */   
/*    */   public void extractItemGroupRenderState(Entity entity, ItemStack stack, ItemModelResolver itemModelResolver) {
/* 16 */     itemModelResolver.updateForNonLiving(this.item, stack, ItemDisplayContext.GROUND, entity);
/* 17 */     this.count = getRenderedAmount(stack.getCount());
/* 18 */     this.seed = getSeedForItemStack(stack);
/*    */   }
/*    */   
/*    */   public static int getSeedForItemStack(ItemStack itemStack) {
/* 22 */     return itemStack.isEmpty() ? 187 : (Item.getId(itemStack.getItem()) + itemStack.getDamageValue());
/*    */   }
/*    */   
/*    */   public static int getRenderedAmount(int stackCount) {
/* 26 */     if (stackCount <= 1)
/* 27 */       return 1; 
/* 28 */     if (stackCount <= 16)
/* 29 */       return 2; 
/* 30 */     if (stackCount <= 32)
/* 31 */       return 3; 
/* 32 */     if (stackCount <= 48) {
/* 33 */       return 4;
/*    */     }
/*    */     
/* 36 */     return 5;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/ItemClusterRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */