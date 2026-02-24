/*    */ package net.minecraft.client.color.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.scores.PlayerTeam;
/*    */ 
/*    */ public final class TeamColor extends Record implements ItemTintSource {
/*    */   private final int defaultColor;
/*    */   public static final com.mojang.serialization.MapCodec<TeamColor> MAP_CODEC;
/*    */   
/* 14 */   public TeamColor(int defaultColor) { this.defaultColor = defaultColor; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/color/item/TeamColor;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/client/color/item/TeamColor; } public int defaultColor() { return this.defaultColor; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/color/item/TeamColor;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/color/item/TeamColor; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/color/item/TeamColor;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/color/item/TeamColor;
/*    */     //   0	8	1	o	Ljava/lang/Object; } static {
/* 17 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)net.minecraft.util.ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(TeamColor::defaultColor)).apply((com.mojang.datafixers.kinds.Applicative)i, TeamColor::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int calculate(net.minecraft.world.item.ItemStack itemStack, net.minecraft.client.multiplayer.ClientLevel level, LivingEntity owner) {
/* 23 */     if (owner != null) {
/* 24 */       PlayerTeam playerTeam = owner.getTeam();
/* 25 */       if (playerTeam != null) {
/* 26 */         ChatFormatting color = playerTeam.getColor();
/* 27 */         if (color.getColor() != null) {
/* 28 */           return net.minecraft.util.ARGB.opaque(color.getColor());
/*    */         }
/*    */       } 
/*    */     } 
/* 32 */     return net.minecraft.util.ARGB.opaque(this.defaultColor);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<TeamColor> type() {
/* 37 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/color/item/TeamColor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */