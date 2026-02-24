/*    */ package net.minecraft.util;
/*    */ public final class Brightness extends Record {
/*    */   private final int block;
/*    */   private final int sky;
/*    */   
/*  6 */   public Brightness(int block, int sky) { this.block = block; this.sky = sky; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/Brightness;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  6 */     //   0	7	0	this	Lnet/minecraft/util/Brightness; } public int block() { return this.block; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/Brightness;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/Brightness; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/Brightness;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/Brightness;
/*  6 */     //   0	8	1	o	Ljava/lang/Object; } public int sky() { return this.sky; }
/*  7 */    public static final com.mojang.serialization.Codec<Integer> LIGHT_VALUE_CODEC = ExtraCodecs.intRange(0, 15); public static final com.mojang.serialization.Codec<Brightness> CODEC;
/*    */   static {
/*  9 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)LIGHT_VALUE_CODEC.fieldOf("block").forGetter(Brightness::block), (com.mojang.datafixers.kinds.App)LIGHT_VALUE_CODEC.fieldOf("sky").forGetter(Brightness::sky)).apply((com.mojang.datafixers.kinds.Applicative)i, Brightness::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final Brightness FULL_BRIGHT = new Brightness(15, 15);
/*    */   
/*    */   public static int pack(int block, int sky) {
/* 17 */     return block << 4 | sky << 20;
/*    */   }
/*    */   
/*    */   public int pack() {
/* 21 */     return pack(this.block, this.sky);
/*    */   }
/*    */   
/*    */   public static int block(int packed) {
/* 25 */     return packed >> 4 & 0xFFFF;
/*    */   }
/*    */   
/*    */   public static int sky(int packed) {
/* 29 */     return packed >> 20 & 0xFFFF;
/*    */   }
/*    */   
/*    */   public static Brightness unpack(int packed) {
/* 33 */     return new Brightness(
/* 34 */         block(packed), 
/* 35 */         sky(packed));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/Brightness.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */