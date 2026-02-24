/*    */ package net.minecraft.client.color.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.MapItemColor;
/*    */ 
/*    */ public final class MapColor extends Record implements ItemTintSource {
/*    */   private final int defaultColor;
/*    */   public static final com.mojang.serialization.MapCodec<MapColor> MAP_CODEC;
/*    */   
/* 14 */   public MapColor(int defaultColor) { this.defaultColor = defaultColor; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/color/item/MapColor;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/client/color/item/MapColor; } public int defaultColor() { return this.defaultColor; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/color/item/MapColor;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/color/item/MapColor; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/color/item/MapColor;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/color/item/MapColor;
/*    */     //   0	8	1	o	Ljava/lang/Object; } static {
/* 17 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(MapColor::defaultColor)).apply((com.mojang.datafixers.kinds.Applicative)i, MapColor::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public MapColor() {
/* 22 */     this(MapItemColor.DEFAULT.rgb());
/*    */   }
/*    */ 
/*    */   
/*    */   public int calculate(ItemStack itemStack, net.minecraft.client.multiplayer.ClientLevel level, net.minecraft.world.entity.LivingEntity owner) {
/* 27 */     MapItemColor component = (MapItemColor)itemStack.get(net.minecraft.core.component.DataComponents.MAP_COLOR);
/* 28 */     if (component != null) {
/* 29 */       return net.minecraft.util.ARGB.opaque(component.rgb());
/*    */     }
/*    */     
/* 32 */     return net.minecraft.util.ARGB.opaque(this.defaultColor);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<MapColor> type() {
/* 37 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/color/item/MapColor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */