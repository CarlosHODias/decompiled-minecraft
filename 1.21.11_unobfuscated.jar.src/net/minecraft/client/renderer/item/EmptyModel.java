/*    */ package net.minecraft.client.renderer.item;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.resources.model.ResolvableModel;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class EmptyModel implements ItemModel {
/* 11 */   public static final ItemModel INSTANCE = new EmptyModel();
/*    */   
/*    */   public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, ClientLevel level, ItemOwner owner, int seed)
/*    */   {
/* 15 */     output.appendModelIdentityElement(this);
/*    */   } public static final class Unbaked extends Record implements ItemModel.Unbaked { public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/EmptyModel$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/EmptyModel$Unbaked;
/*    */     }
/* 19 */     public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);
/*    */ 
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/EmptyModel$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/EmptyModel$Unbaked;
/*    */     }
/*    */     
/*    */     public ItemModel bake(ItemModel.BakingContext context) {
/* 27 */       return EmptyModel.INSTANCE;
/*    */     } public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/EmptyModel$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/EmptyModel$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     } public void resolveDependencies(ResolvableModel.Resolver resolver) {}
/*    */     public MapCodec<Unbaked> type() {
/* 32 */       return MAP_CODEC;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/EmptyModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */