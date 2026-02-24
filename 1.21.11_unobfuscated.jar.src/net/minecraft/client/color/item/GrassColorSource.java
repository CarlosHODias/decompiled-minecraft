/*    */ package net.minecraft.client.color.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class GrassColorSource extends Record implements ItemTintSource {
/*    */   private final float temperature;
/*    */   private final float downfall;
/*    */   public static final com.mojang.serialization.MapCodec<GrassColorSource> MAP_CODEC;
/*    */   
/* 12 */   public GrassColorSource(float temperature, float downfall) { this.temperature = temperature; this.downfall = downfall; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/color/item/GrassColorSource;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/client/color/item/GrassColorSource; } public float temperature() { return this.temperature; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/color/item/GrassColorSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/color/item/GrassColorSource; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/color/item/GrassColorSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/color/item/GrassColorSource;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public float downfall() { return this.downfall; }
/*    */ 
/*    */ 
/*    */   
/*    */   public GrassColorSource() {
/* 17 */     this(0.5F, 1.0F);
/*    */   }
/*    */   static {
/* 20 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("temperature").forGetter(GrassColorSource::temperature), (App)ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("downfall").forGetter(GrassColorSource::downfall)).apply((com.mojang.datafixers.kinds.Applicative)i, GrassColorSource::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int calculate(net.minecraft.world.item.ItemStack itemStack, net.minecraft.client.multiplayer.ClientLevel level, net.minecraft.world.entity.LivingEntity owner) {
/* 27 */     return net.minecraft.world.level.GrassColor.get(this.temperature, this.downfall);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<GrassColorSource> type() {
/* 32 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/color/item/GrassColorSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */