/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ItemParticleOption implements ParticleOptions {
/* 11 */   private static final Codec<ItemStack> ITEM_CODEC = Codec.withAlternative(ItemStack.SINGLE_ITEM_CODEC, Item.CODEC, ItemStack::new);
/*    */   
/*    */   private final ParticleType<ItemParticleOption> type;
/*    */   private final ItemStack itemStack;
/*    */   
/*    */   public static MapCodec<ItemParticleOption> codec(ParticleType<ItemParticleOption> type) {
/* 17 */     return ITEM_CODEC.xmap(stack -> new ItemParticleOption(type, stack), o -> o.itemStack).fieldOf("item");
/*    */   }
/*    */   
/*    */   public static StreamCodec<? super RegistryFriendlyByteBuf, ItemParticleOption> streamCodec(ParticleType<ItemParticleOption> type) {
/* 21 */     return ItemStack.STREAM_CODEC.map(stack -> new ItemParticleOption(type, stack), o -> o.itemStack);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemParticleOption(ParticleType<ItemParticleOption> type, ItemStack itemStack) {
/* 28 */     if (itemStack.isEmpty()) {
/* 29 */       throw new IllegalArgumentException("Empty stacks are not allowed");
/*    */     }
/* 31 */     this.type = type;
/* 32 */     this.itemStack = itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleType<ItemParticleOption> getType() {
/* 37 */     return this.type;
/*    */   }
/*    */   
/*    */   public ItemStack getItem() {
/* 41 */     return this.itemStack;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/particles/ItemParticleOption.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */