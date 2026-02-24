/*    */ package net.minecraft.client.renderer.item;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.client.multiplayer.CacheSlot;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
/*    */ import net.minecraft.client.renderer.item.properties.conditional.ItemModelPropertyTest;
/*    */ import net.minecraft.client.resources.model.ResolvableModel;
/*    */ import net.minecraft.util.RegistryContextSwapper;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ConditionalItemModel implements ItemModel {
/*    */   private final ItemModelPropertyTest property;
/*    */   private final ItemModel onTrue;
/*    */   private final ItemModel onFalse;
/*    */   
/*    */   public ConditionalItemModel(ItemModelPropertyTest property, ItemModel onTrue, ItemModel onFalse) {
/* 23 */     this.property = property;
/* 24 */     this.onTrue = onTrue;
/* 25 */     this.onFalse = onFalse;
/*    */   }
/*    */ 
/*    */   
/*    */   public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, ClientLevel level, ItemOwner owner, int seed) {
/* 30 */     output.appendModelIdentityElement(this);
/* 31 */     (this.property.get(item, level, (owner == null) ? null : owner.asLivingEntity(), seed, displayContext) ? this.onTrue : this.onFalse).update(output, item, resolver, displayContext, level, owner, seed);
/*    */   }
/*    */   public static final class Unbaked extends Record implements ItemModel.Unbaked { private final ConditionalItemModelProperty property; private final ItemModel.Unbaked onTrue; private final ItemModel.Unbaked onFalse; public static final com.mojang.serialization.MapCodec<Unbaked> MAP_CODEC;
/* 34 */     public Unbaked(ConditionalItemModelProperty property, ItemModel.Unbaked onTrue, ItemModel.Unbaked onFalse) { this.property = property; this.onTrue = onTrue; this.onFalse = onFalse; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/ConditionalItemModel$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #34	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 34 */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/ConditionalItemModel$Unbaked; } public ConditionalItemModelProperty property() { return this.property; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/ConditionalItemModel$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #34	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/ConditionalItemModel$Unbaked; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/ConditionalItemModel$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #34	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/ConditionalItemModel$Unbaked;
/* 34 */       //   0	8	1	o	Ljava/lang/Object; } public ItemModel.Unbaked onTrue() { return this.onTrue; } public ItemModel.Unbaked onFalse() { return this.onFalse; }
/*    */ 
/*    */ 
/*    */     
/*    */     static {
/* 39 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties.MAP_CODEC.forGetter(Unbaked::property), (App)ItemModels.CODEC.fieldOf("on_true").forGetter(Unbaked::onTrue), (App)ItemModels.CODEC.fieldOf("on_false").forGetter(Unbaked::onFalse)).apply((Applicative)i, Unbaked::new));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public com.mojang.serialization.MapCodec<Unbaked> type() {
/* 47 */       return MAP_CODEC;
/*    */     }
/*    */ 
/*    */     
/*    */     public ItemModel bake(ItemModel.BakingContext context) {
/* 52 */       return new ConditionalItemModel(
/* 53 */           adaptProperty(this.property, context.contextSwapper()), 
/* 54 */           this.onTrue.bake(context), 
/* 55 */           this.onFalse.bake(context));
/*    */     }
/*    */ 
/*    */     
/*    */     private ItemModelPropertyTest adaptProperty(ConditionalItemModelProperty originalProperty, RegistryContextSwapper contextSwapper) {
/* 60 */       if (contextSwapper == null) {
/* 61 */         return (ItemModelPropertyTest)originalProperty;
/*    */       }
/* 63 */       CacheSlot<ClientLevel, ItemModelPropertyTest> remappedModelCache = new CacheSlot(context -> swapContext(originalProperty, contextSwapper, context));
/* 64 */       return (itemStack, level, owner, seed, displayContext) -> {
/*    */           ItemModelPropertyTest property = (level == null) ? (ItemModelPropertyTest)originalProperty : (ItemModelPropertyTest)remappedModelCache.compute((CacheSlot.Cleaner)level);
/*    */           return property.get(itemStack, level, owner, seed, displayContext);
/*    */         };
/*    */     }
/*    */ 
/*    */     
/*    */     private static <T extends ConditionalItemModelProperty> T swapContext(T originalProperty, RegistryContextSwapper contextSwapper, ClientLevel context) {
/* 72 */       return (T)contextSwapper.swapTo(originalProperty.type().codec(), originalProperty, (net.minecraft.core.HolderLookup.Provider)context.registryAccess()).result()
/*    */         
/* 74 */         .orElse((ConditionalItemModelProperty)originalProperty);
/*    */     }
/*    */ 
/*    */     
/*    */     public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 79 */       this.onTrue.resolveDependencies(resolver);
/* 80 */       this.onFalse.resolveDependencies(resolver);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/ConditionalItemModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */