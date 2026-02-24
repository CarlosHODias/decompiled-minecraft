/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.saveddata.maps.MapDecorationType;
/*    */ 
/*    */ public final class Entry extends Record {
/*    */   private final net.minecraft.core.Holder<MapDecorationType> type;
/*    */   private final double x;
/*    */   private final double z;
/*    */   private final float rotation;
/*    */   public static final Codec<Entry> CODEC;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/MapDecorations$Entry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/MapDecorations$Entry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/MapDecorations$Entry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/MapDecorations$Entry;
/*    */   }
/*    */   
/* 23 */   public Entry(net.minecraft.core.Holder<MapDecorationType> type, double x, double z, float rotation) { this.type = type; this.x = x; this.z = z; this.rotation = rotation; } public net.minecraft.core.Holder<MapDecorationType> type() { return this.type; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/MapDecorations$Entry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/MapDecorations$Entry;
/* 23 */     //   0	8	1	o	Ljava/lang/Object; } public double x() { return this.x; } public double z() { return this.z; } public float rotation() { return this.rotation; } static {
/* 24 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)MapDecorationType.CODEC.fieldOf("type").forGetter(Entry::type), (App)Codec.DOUBLE.fieldOf("x").forGetter(Entry::x), (App)Codec.DOUBLE.fieldOf("z").forGetter(Entry::z), (App)Codec.FLOAT.fieldOf("rotation").forGetter(Entry::rotation)).apply((com.mojang.datafixers.kinds.Applicative)i, Entry::new));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/MapDecorations$Entry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */