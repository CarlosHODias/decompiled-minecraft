/*    */ package net.minecraft.world.level.saveddata.maps;
/*    */ public final class MapFrame extends Record { private final net.minecraft.core.BlockPos pos;
/*    */   private final int rotation;
/*    */   private final int entityId;
/*    */   public static final com.mojang.serialization.Codec<MapFrame> CODEC;
/*    */   
/*  7 */   public MapFrame(net.minecraft.core.BlockPos pos, int rotation, int entityId) { this.pos = pos; this.rotation = rotation; this.entityId = entityId; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/saveddata/maps/MapFrame;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapFrame; } public net.minecraft.core.BlockPos pos() { return this.pos; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/saveddata/maps/MapFrame;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapFrame; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/saveddata/maps/MapFrame;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/saveddata/maps/MapFrame;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public int rotation() { return this.rotation; } public int entityId() { return this.entityId; } static {
/*  8 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.core.BlockPos.CODEC.fieldOf("pos").forGetter(MapFrame::pos), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.INT.fieldOf("rotation").forGetter(MapFrame::rotation), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.INT.fieldOf("entity_id").forGetter(MapFrame::entityId)).apply((com.mojang.datafixers.kinds.Applicative)i, MapFrame::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getId() {
/* 15 */     return frameId(this.pos);
/*    */   }
/*    */   
/*    */   public static String frameId(net.minecraft.core.BlockPos pos) {
/* 19 */     return "frame-" + pos.getX() + "," + pos.getY() + "," + pos.getZ();
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/saveddata/maps/MapFrame.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */