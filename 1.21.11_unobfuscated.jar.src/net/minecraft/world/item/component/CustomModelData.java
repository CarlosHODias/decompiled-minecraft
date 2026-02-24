/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ 
/*    */ public final class CustomModelData extends Record {
/*    */   private final List<Float> floats;
/*    */   private final List<Boolean> flags;
/*    */   private final List<String> strings;
/*    */   private final List<Integer> colors;
/*    */   
/* 13 */   public CustomModelData(List<Float> floats, List<Boolean> flags, List<String> strings, List<Integer> colors) { this.floats = floats; this.flags = flags; this.strings = strings; this.colors = colors; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/CustomModelData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/world/item/component/CustomModelData; } public List<Float> floats() { return this.floats; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/CustomModelData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/CustomModelData; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/CustomModelData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/CustomModelData;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public List<Boolean> flags() { return this.flags; } public List<String> strings() { return this.strings; } public List<Integer> colors() { return this.colors; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final CustomModelData EMPTY = new CustomModelData(
/* 20 */       List.of(), 
/* 21 */       List.of(), 
/* 22 */       List.of(), 
/* 23 */       List.of()); public static final com.mojang.serialization.Codec<CustomModelData> CODEC;
/*    */   
/*    */   static {
/* 26 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((App)com.mojang.serialization.Codec.FLOAT.listOf().optionalFieldOf("floats", List.of()).forGetter(CustomModelData::floats), (App)com.mojang.serialization.Codec.BOOL.listOf().optionalFieldOf("flags", List.of()).forGetter(CustomModelData::flags), (App)com.mojang.serialization.Codec.STRING.listOf().optionalFieldOf("strings", List.of()).forGetter(CustomModelData::strings), (App)net.minecraft.util.ExtraCodecs.RGB_COLOR_CODEC.listOf().optionalFieldOf("colors", List.of()).forGetter(CustomModelData::colors)).apply((com.mojang.datafixers.kinds.Applicative)i, CustomModelData::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public static final net.minecraft.network.codec.StreamCodec<io.netty.buffer.ByteBuf, CustomModelData> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(
/* 34 */       ByteBufCodecs.FLOAT.apply(ByteBufCodecs.list()), CustomModelData::floats, 
/* 35 */       ByteBufCodecs.BOOL.apply(ByteBufCodecs.list()), CustomModelData::flags, 
/* 36 */       ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), CustomModelData::strings, 
/* 37 */       ByteBufCodecs.INT.apply(ByteBufCodecs.list()), CustomModelData::colors, CustomModelData::new);
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T> T getSafe(List<T> values, int index) {
/* 42 */     if (index < 0 || index >= values.size()) {
/* 43 */       return null;
/*    */     }
/*    */     
/* 46 */     return values.get(index);
/*    */   }
/*    */   
/*    */   public Float getFloat(int index) {
/* 50 */     return getSafe(this.floats, index);
/*    */   }
/*    */   
/*    */   public Boolean getBoolean(int index) {
/* 54 */     return getSafe(this.flags, index);
/*    */   }
/*    */   
/*    */   public String getString(int index) {
/* 58 */     return getSafe(this.strings, index);
/*    */   }
/*    */   
/*    */   public Integer getColor(int index) {
/* 62 */     return getSafe(this.colors, index);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/CustomModelData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */