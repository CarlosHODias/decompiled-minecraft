/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.tags.ItemTags;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.world.item.DyeItem;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.TooltipFlag;
/*    */ 
/*    */ public final class DyedItemColor extends Record implements TooltipProvider {
/*    */   private final int rgb;
/*    */   
/* 23 */   public DyedItemColor(int rgb) { this.rgb = rgb; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/DyedItemColor;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 23 */     //   0	7	0	this	Lnet/minecraft/world/item/component/DyedItemColor; } public int rgb() { return this.rgb; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/DyedItemColor;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/DyedItemColor; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/DyedItemColor;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/DyedItemColor;
/* 24 */     //   0	8	1	o	Ljava/lang/Object; } public static final Codec<DyedItemColor> CODEC = net.minecraft.util.ExtraCodecs.RGB_COLOR_CODEC.xmap(DyedItemColor::new, DyedItemColor::rgb);
/*    */   
/* 26 */   public static final StreamCodec<ByteBuf, DyedItemColor> STREAM_CODEC = StreamCodec.composite(net.minecraft.network.codec.ByteBufCodecs.INT, DyedItemColor::rgb, DyedItemColor::new);
/*    */ 
/*    */   
/*    */   public static final int LEATHER_COLOR = -6265536;
/*    */ 
/*    */ 
/*    */   
/*    */   public static int getOrDefault(ItemStack itemStack, int defaultColor) {
/* 34 */     DyedItemColor color = (DyedItemColor)itemStack.get(DataComponents.DYED_COLOR);
/* 35 */     return (color != null) ? ARGB.opaque(color.rgb()) : defaultColor;
/*    */   }
/*    */   
/*    */   public static ItemStack applyDyes(ItemStack itemStack, List<DyeItem> dyes) {
/* 39 */     if (!itemStack.is(ItemTags.DYEABLE)) {
/* 40 */       return ItemStack.EMPTY;
/*    */     }
/*    */     
/* 43 */     ItemStack result = itemStack.copyWithCount(1);
/*    */     
/* 45 */     int redTotal = 0;
/* 46 */     int greenTotal = 0;
/* 47 */     int blueTotal = 0;
/* 48 */     int intensityTotal = 0;
/* 49 */     int colorCount = 0;
/*    */     
/* 51 */     DyedItemColor currentDye = (DyedItemColor)result.get(DataComponents.DYED_COLOR);
/* 52 */     if (currentDye != null) {
/* 53 */       int i = ARGB.red(currentDye.rgb());
/* 54 */       int j = ARGB.green(currentDye.rgb());
/* 55 */       int k = ARGB.blue(currentDye.rgb());
/* 56 */       intensityTotal += Math.max(i, Math.max(j, k));
/* 57 */       redTotal += i;
/* 58 */       greenTotal += j;
/* 59 */       blueTotal += k;
/* 60 */       colorCount++;
/*    */     } 
/*    */     
/* 63 */     for (DyeItem dye : dyes) {
/* 64 */       int color = dye.getDyeColor().getTextureDiffuseColor();
/* 65 */       int i = ARGB.red(color);
/* 66 */       int j = ARGB.green(color);
/* 67 */       int k = ARGB.blue(color);
/*    */       
/* 69 */       intensityTotal += Math.max(i, Math.max(j, k));
/*    */       
/* 71 */       redTotal += i;
/* 72 */       greenTotal += j;
/* 73 */       blueTotal += k;
/* 74 */       colorCount++;
/*    */     } 
/*    */     
/* 77 */     int red = redTotal / colorCount;
/* 78 */     int green = greenTotal / colorCount;
/* 79 */     int blue = blueTotal / colorCount;
/*    */     
/* 81 */     float averageIntensity = intensityTotal / colorCount;
/* 82 */     float resultIntensity = Math.max(red, Math.max(green, blue));
/*    */     
/* 84 */     red = (int)(red * averageIntensity / resultIntensity);
/* 85 */     green = (int)(green * averageIntensity / resultIntensity);
/* 86 */     blue = (int)(blue * averageIntensity / resultIntensity);
/*    */     
/* 88 */     int rgb = ARGB.color(0, red, green, blue);
/* 89 */     result.set(DataComponents.DYED_COLOR, new DyedItemColor(rgb));
/*    */     
/* 91 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 96 */     if (flag.isAdvanced()) {
/* 97 */       consumer.accept(Component.translatable("item.color", new Object[] { String.format(java.util.Locale.ROOT, "#%06X", new Object[] { this.rgb }) }).withStyle(ChatFormatting.GRAY));
/*    */     } else {
/* 99 */       consumer.accept(Component.translatable("item.dyed").withStyle(new ChatFormatting[] { ChatFormatting.GRAY, ChatFormatting.ITALIC }));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/DyedItemColor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */