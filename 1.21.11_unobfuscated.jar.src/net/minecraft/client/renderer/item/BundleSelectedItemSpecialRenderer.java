/*    */ package net.minecraft.client.renderer.item;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.resources.model.ResolvableModel;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.item.BundleItem;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class BundleSelectedItemSpecialRenderer implements ItemModel {
/* 12 */   private static final ItemModel INSTANCE = new BundleSelectedItemSpecialRenderer();
/*    */   
/*    */   public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, ClientLevel level, ItemOwner owner, int seed)
/*    */   {
/* 16 */     output.appendModelIdentityElement(this);
/* 17 */     ItemStack selectedItemStack = BundleItem.getSelectedItemStack(item);
/* 18 */     if (!selectedItemStack.isEmpty())
/* 19 */       resolver.appendItemLayers(output, selectedItemStack, displayContext, (Level)level, owner, seed); 
/*    */   }
/*    */   
/*    */   public static final class Unbaked
/*    */     extends Record implements ItemModel.Unbaked {
/* 24 */     public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());
/*    */     public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/BundleSelectedItemSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/BundleSelectedItemSpecialRenderer$Unbaked; } public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/BundleSelectedItemSpecialRenderer$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/BundleSelectedItemSpecialRenderer$Unbaked;
/*    */     } public MapCodec<Unbaked> type() {
/* 28 */       return MAP_CODEC;
/*    */     } public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/BundleSelectedItemSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/BundleSelectedItemSpecialRenderer$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     public ItemModel bake(ItemModel.BakingContext context) {
/* 33 */       return BundleSelectedItemSpecialRenderer.INSTANCE;
/*    */     }
/*    */     
/*    */     public void resolveDependencies(ResolvableModel.Resolver resolver) {}
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/BundleSelectedItemSpecialRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */