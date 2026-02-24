/*    */ package net.minecraft.world.level.saveddata.maps;
/*    */ 
/*    */ 
/*    */ public final class MapDecorationType extends Record {
/*    */   private final net.minecraft.resources.Identifier assetId;
/*    */   private final boolean showOnItemFrame;
/*    */   private final int mapColor;
/*    */   private final boolean explorationMapElement;
/*    */   private final boolean trackCount;
/*    */   public static final int NO_MAP_COLOR = -1;
/*    */   
/* 12 */   public MapDecorationType(net.minecraft.resources.Identifier assetId, boolean showOnItemFrame, int mapColor, boolean explorationMapElement, boolean trackCount) { this.assetId = assetId; this.showOnItemFrame = showOnItemFrame; this.mapColor = mapColor; this.explorationMapElement = explorationMapElement; this.trackCount = trackCount; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/saveddata/maps/MapDecorationType;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapDecorationType; } public net.minecraft.resources.Identifier assetId() { return this.assetId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/saveddata/maps/MapDecorationType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapDecorationType; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/saveddata/maps/MapDecorationType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/saveddata/maps/MapDecorationType;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public boolean showOnItemFrame() { return this.showOnItemFrame; } public int mapColor() { return this.mapColor; } public boolean explorationMapElement() { return this.explorationMapElement; } public boolean trackCount() { return this.trackCount; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public static final com.mojang.serialization.Codec<net.minecraft.core.Holder<MapDecorationType>> CODEC = net.minecraft.core.registries.BuiltInRegistries.MAP_DECORATION_TYPE.holderByNameCodec();
/* 22 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, net.minecraft.core.Holder<MapDecorationType>> STREAM_CODEC = net.minecraft.network.codec.ByteBufCodecs.holderRegistry(net.minecraft.core.registries.Registries.MAP_DECORATION_TYPE);
/*    */   
/*    */   public boolean hasMapColor() {
/* 25 */     return (this.mapColor != -1);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/saveddata/maps/MapDecorationType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */