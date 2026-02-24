/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.chat.numbers.NumberFormat;
/*    */ import net.minecraft.network.chat.numbers.NumberFormatTypes;
/*    */ 
/*    */ public class Score implements ReadOnlyScoreInfo {
/*    */   private int value;
/*    */   private boolean locked = true;
/*    */   private Component display;
/*    */   private NumberFormat numberFormat;
/*    */   
/*    */   public Score() {}
/*    */   
/*    */   public Score(Packed packed) {
/* 24 */     this.value = packed.value;
/* 25 */     this.locked = packed.locked;
/* 26 */     this.display = packed.display.orElse(null);
/* 27 */     this.numberFormat = packed.numberFormat.orElse(null);
/*    */   }
/*    */   
/*    */   public Packed pack() {
/* 31 */     return new Packed(this.value, this.locked, 
/*    */ 
/*    */         
/* 34 */         Optional.ofNullable(this.display), 
/* 35 */         Optional.ofNullable(this.numberFormat));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int value() {
/* 41 */     return this.value;
/*    */   }
/*    */   
/*    */   public void value(int score) {
/* 45 */     this.value = score;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isLocked() {
/* 50 */     return this.locked;
/*    */   }
/*    */   
/*    */   public void setLocked(boolean locked) {
/* 54 */     this.locked = locked;
/*    */   }
/*    */   
/*    */   public Component display() {
/* 58 */     return this.display;
/*    */   }
/*    */   
/*    */   public void display(Component display) {
/* 62 */     this.display = display;
/*    */   }
/*    */ 
/*    */   
/*    */   public NumberFormat numberFormat() {
/* 67 */     return this.numberFormat;
/*    */   }
/*    */   
/*    */   public void numberFormat(NumberFormat numberFormat) {
/* 71 */     this.numberFormat = numberFormat;
/*    */   }
/*    */   public static final class Packed extends Record { private final int value; private final boolean locked; private final Optional<Component> display; private final Optional<NumberFormat> numberFormat; public static final MapCodec<Packed> MAP_CODEC;
/* 74 */     public Packed(int value, boolean locked, Optional<Component> display, Optional<NumberFormat> numberFormat) { this.value = value; this.locked = locked; this.display = display; this.numberFormat = numberFormat; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/scores/Score$Packed;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #74	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 74 */       //   0	7	0	this	Lnet/minecraft/world/scores/Score$Packed; } public int value() { return this.value; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/scores/Score$Packed;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #74	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/scores/Score$Packed; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/scores/Score$Packed;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #74	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/scores/Score$Packed;
/* 74 */       //   0	8	1	o	Ljava/lang/Object; } public boolean locked() { return this.locked; } public Optional<Component> display() { return this.display; } public Optional<NumberFormat> numberFormat() { return this.numberFormat; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     static {
/* 80 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.optionalFieldOf("Score", 0).forGetter(Packed::value), (App)Codec.BOOL.optionalFieldOf("Locked", false).forGetter(Packed::locked), (App)ComponentSerialization.CODEC.optionalFieldOf("display").forGetter(Packed::display), (App)NumberFormatTypes.CODEC.optionalFieldOf("format").forGetter(Packed::numberFormat)).apply((Applicative)i, Packed::new));
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/scores/Score.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */