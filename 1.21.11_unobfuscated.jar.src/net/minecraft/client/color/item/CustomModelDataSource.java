/*    */ package net.minecraft.client.color.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.CustomModelData;
/*    */ 
/*    */ public final class CustomModelDataSource extends Record implements ItemTintSource {
/*    */   private final int index;
/*    */   private final int defaultColor;
/*    */   public static final com.mojang.serialization.MapCodec<CustomModelDataSource> MAP_CODEC;
/*    */   
/* 14 */   public CustomModelDataSource(int index, int defaultColor) { this.index = index; this.defaultColor = defaultColor; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/color/item/CustomModelDataSource;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/client/color/item/CustomModelDataSource; } public int index() { return this.index; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/color/item/CustomModelDataSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/color/item/CustomModelDataSource; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/color/item/CustomModelDataSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/color/item/CustomModelDataSource;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public int defaultColor() { return this.defaultColor; }
/*    */ 
/*    */   
/*    */   static {
/* 18 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("index", 0).forGetter(CustomModelDataSource::index), (App)ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(CustomModelDataSource::defaultColor)).apply((com.mojang.datafixers.kinds.Applicative)i, CustomModelDataSource::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int calculate(ItemStack itemStack, net.minecraft.client.multiplayer.ClientLevel level, net.minecraft.world.entity.LivingEntity owner) {
/* 25 */     CustomModelData customModelData = (CustomModelData)itemStack.get(net.minecraft.core.component.DataComponents.CUSTOM_MODEL_DATA);
/* 26 */     if (customModelData != null) {
/* 27 */       Integer value = customModelData.getColor(this.index);
/* 28 */       if (value != null) {
/* 29 */         return net.minecraft.util.ARGB.opaque(value);
/*    */       }
/*    */     } 
/* 32 */     return net.minecraft.util.ARGB.opaque(this.defaultColor);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<CustomModelDataSource> type() {
/* 37 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/color/item/CustomModelDataSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */