/*    */ package net.minecraft.client.color.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.world.item.component.FireworkExplosion;
/*    */ 
/*    */ public final class Firework extends Record implements ItemTintSource {
/*    */   private final int defaultColor;
/*    */   public static final com.mojang.serialization.MapCodec<Firework> MAP_CODEC;
/*    */   
/* 15 */   public Firework(int defaultColor) { this.defaultColor = defaultColor; } public int defaultColor() { return this.defaultColor; }
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/color/item/Firework;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/color/item/Firework; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/color/item/Firework;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/color/item/Firework; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/color/item/Firework;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/color/item/Firework;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   } public Firework() {
/* 20 */     this(-7697782);
/*    */   }
/*    */   static {
/* 23 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)net.minecraft.util.ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(Firework::defaultColor)).apply((Applicative)i, Firework::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int calculate(net.minecraft.world.item.ItemStack itemStack, net.minecraft.client.multiplayer.ClientLevel level, net.minecraft.world.entity.LivingEntity owner) {
/* 29 */     FireworkExplosion explosion = (FireworkExplosion)itemStack.get(net.minecraft.core.component.DataComponents.FIREWORK_EXPLOSION);
/* 30 */     IntList explosionColors = (explosion != null) ? explosion.colors() : IntList.of();
/* 31 */     int colorCount = explosionColors.size();
/* 32 */     if (colorCount == 0)
/* 33 */       return this.defaultColor; 
/* 34 */     if (colorCount == 1) {
/* 35 */       return ARGB.opaque(explosionColors.getInt(0));
/*    */     }
/*    */     
/* 38 */     int totalRed = 0;
/* 39 */     int totalGreen = 0;
/* 40 */     int totalBlue = 0;
/* 41 */     for (int i = 0; i < colorCount; i++) {
/* 42 */       int color = explosionColors.getInt(i);
/* 43 */       totalRed += ARGB.red(color);
/* 44 */       totalGreen += ARGB.green(color);
/* 45 */       totalBlue += ARGB.blue(color);
/*    */     } 
/*    */     
/* 48 */     return ARGB.color(totalRed / colorCount, totalGreen / colorCount, totalBlue / colorCount);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<Firework> type() {
/* 53 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/color/item/Firework.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */