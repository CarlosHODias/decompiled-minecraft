/*    */ package net.minecraft.client.renderer.item.properties.numeric;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.CrossbowItem;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class CrossbowPull
/*    */   implements RangeSelectItemModelProperty {
/* 12 */   public static final MapCodec<CrossbowPull> MAP_CODEC = MapCodec.unit(new CrossbowPull());
/*    */ 
/*    */   
/*    */   public float get(ItemStack itemStack, ClientLevel level, ItemOwner owner, int seed) {
/* 16 */     LivingEntity entity = (owner == null) ? null : owner.asLivingEntity();
/*    */     
/* 18 */     if (entity == null) {
/* 19 */       return 0.0F;
/*    */     }
/*    */     
/* 22 */     if (CrossbowItem.isCharged(itemStack)) {
/* 23 */       return 0.0F;
/*    */     }
/*    */     
/* 26 */     int chargeDuration = CrossbowItem.getChargeDuration(itemStack, entity);
/* 27 */     return UseDuration.useDuration(itemStack, entity) / chargeDuration;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<CrossbowPull> type() {
/* 32 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/numeric/CrossbowPull.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */