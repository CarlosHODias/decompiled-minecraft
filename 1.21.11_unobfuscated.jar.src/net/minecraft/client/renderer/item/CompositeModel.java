/*    */ package net.minecraft.client.renderer.item;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.resources.model.ResolvableModel;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class CompositeModel implements ItemModel {
/*    */   private final List<ItemModel> models;
/*    */   
/*    */   public CompositeModel(List<ItemModel> models) {
/* 17 */     this.models = models;
/*    */   }
/*    */ 
/*    */   
/*    */   public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, ClientLevel level, ItemOwner owner, int seed) {
/* 22 */     output.appendModelIdentityElement(this);
/*    */     
/* 24 */     output.ensureCapacity(this.models.size());
/* 25 */     for (ItemModel model : this.models)
/* 26 */       model.update(output, item, resolver, displayContext, level, owner, seed); 
/*    */   }
/*    */   public static final class Unbaked extends Record implements ItemModel.Unbaked { private final List<ItemModel.Unbaked> models; public static final MapCodec<Unbaked> MAP_CODEC;
/*    */     
/* 30 */     public Unbaked(List<ItemModel.Unbaked> models) { this.models = models; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/CompositeModel$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 30 */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/CompositeModel$Unbaked; } public List<ItemModel.Unbaked> models() { return this.models; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/CompositeModel$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/CompositeModel$Unbaked; }
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/CompositeModel$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/CompositeModel$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object; } static {
/* 33 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ItemModels.CODEC.listOf().fieldOf("models").forGetter(Unbaked::models)).apply((com.mojang.datafixers.kinds.Applicative)i, Unbaked::new));
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public MapCodec<Unbaked> type() {
/* 39 */       return MAP_CODEC;
/*    */     }
/*    */ 
/*    */     
/*    */     public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 44 */       for (ItemModel.Unbaked model : this.models) {
/* 45 */         model.resolveDependencies(resolver);
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public ItemModel bake(ItemModel.BakingContext context) {
/* 51 */       return new CompositeModel(this.models.stream().map(m -> m.bake(context)).toList());
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/CompositeModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */