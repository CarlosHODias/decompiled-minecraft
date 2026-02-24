/*    */ package net.minecraft.client.color.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ 
/*    */ public final class Potion extends Record implements ItemTintSource {
/*    */   private final int defaultColor;
/*    */   public static final com.mojang.serialization.MapCodec<Potion> MAP_CODEC;
/*    */   
/* 14 */   public Potion(int defaultColor) { this.defaultColor = defaultColor; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/color/item/Potion;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/client/color/item/Potion; } public int defaultColor() { return this.defaultColor; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/color/item/Potion;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/color/item/Potion; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/color/item/Potion;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/color/item/Potion;
/*    */     //   0	8	1	o	Ljava/lang/Object; } static {
/* 17 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(Potion::defaultColor)).apply((com.mojang.datafixers.kinds.Applicative)i, Potion::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public Potion() {
/* 22 */     this(-13083194);
/*    */   }
/*    */ 
/*    */   
/*    */   public int calculate(ItemStack itemStack, net.minecraft.client.multiplayer.ClientLevel level, net.minecraft.world.entity.LivingEntity owner) {
/* 27 */     PotionContents contents = (PotionContents)itemStack.get(net.minecraft.core.component.DataComponents.POTION_CONTENTS);
/* 28 */     if (contents != null) {
/* 29 */       return net.minecraft.util.ARGB.opaque(contents.getColorOr(this.defaultColor));
/*    */     }
/*    */     
/* 32 */     return net.minecraft.util.ARGB.opaque(this.defaultColor);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<Potion> type() {
/* 37 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/color/item/Potion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */