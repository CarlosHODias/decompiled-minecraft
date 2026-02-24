/*    */ package net.minecraft.client.renderer.item;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.resources.model.ModelManager;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ItemModelResolver
/*    */ {
/*    */   private final Function<Identifier, ItemModel> modelGetter;
/*    */   private final Function<Identifier, ClientItem.Properties> clientProperties;
/*    */   
/*    */   public ItemModelResolver(ModelManager modelManager) {
/* 22 */     Objects.requireNonNull(modelManager); this.modelGetter = modelManager::getItemModel;
/* 23 */     Objects.requireNonNull(modelManager); this.clientProperties = modelManager::getItemProperties;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateForLiving(ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, LivingEntity entity) {
/* 28 */     updateForTopItem(output, item, displayContext, entity.level(), (ItemOwner)entity, entity.getId() + displayContext.ordinal());
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateForNonLiving(ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, Entity entity) {
/* 33 */     updateForTopItem(output, item, displayContext, entity.level(), null, entity.getId());
/*    */   }
/*    */   
/*    */   public void updateForTopItem(ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, Level level, ItemOwner owner, int seed) {
/* 37 */     output.clear();
/*    */     
/* 39 */     if (!item.isEmpty()) {
/* 40 */       output.displayContext = displayContext;
/* 41 */       appendItemLayers(output, item, displayContext, level, owner, seed);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void appendItemLayers(ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, Level level, ItemOwner owner, int seed) {
/* 46 */     Identifier modelId = (Identifier)item.get(DataComponents.ITEM_MODEL);
/* 47 */     if (modelId == null) {
/*    */       return;
/*    */     }
/* 50 */     output.setOversizedInGui(((ClientItem.Properties)this.clientProperties.apply(modelId)).oversizedInGui());
/* 51 */     ClientLevel clientLevel = (ClientLevel)level; ((ItemModel)this.modelGetter.apply(modelId)).update(output, item, this, displayContext, (level instanceof ClientLevel) ? clientLevel : null, owner, seed);
/*    */   }
/*    */   
/*    */   public boolean shouldPlaySwapAnimation(ItemStack stack) {
/* 55 */     Identifier modelId = (Identifier)stack.get(DataComponents.ITEM_MODEL);
/* 56 */     if (modelId == null) {
/* 57 */       return true;
/*    */     }
/* 59 */     return ((ClientItem.Properties)this.clientProperties.apply(modelId)).handAnimationOnSwap();
/*    */   }
/*    */   
/*    */   public float swapAnimationScale(ItemStack stack) {
/* 63 */     Identifier modelId = (Identifier)stack.get(DataComponents.ITEM_MODEL);
/* 64 */     if (modelId == null) {
/* 65 */       return 1.0F;
/*    */     }
/* 67 */     return ((ClientItem.Properties)this.clientProperties.apply(modelId)).swapAnimationScale();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/ItemModelResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */