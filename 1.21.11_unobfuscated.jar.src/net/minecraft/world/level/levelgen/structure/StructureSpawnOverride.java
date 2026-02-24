/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ 
/*    */ public final class StructureSpawnOverride extends Record {
/*    */   private final BoundingBoxType boundingBox;
/*    */   private final net.minecraft.util.random.WeightedList<net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData> spawns;
/*    */   public static final com.mojang.serialization.Codec<StructureSpawnOverride> CODEC;
/*    */   
/*  9 */   public StructureSpawnOverride(BoundingBoxType boundingBox, net.minecraft.util.random.WeightedList<net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData> spawns) { this.boundingBox = boundingBox; this.spawns = spawns; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/StructureSpawnOverride;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSpawnOverride; } public BoundingBoxType boundingBox() { return this.boundingBox; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/StructureSpawnOverride;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSpawnOverride; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/StructureSpawnOverride;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSpawnOverride;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.util.random.WeightedList<net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData> spawns() { return this.spawns; }
/*    */ 
/*    */   
/*    */   static {
/* 13 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)BoundingBoxType.CODEC.fieldOf("bounding_box").forGetter(StructureSpawnOverride::boundingBox), (com.mojang.datafixers.kinds.App)net.minecraft.util.random.WeightedList.codec(net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData.CODEC).fieldOf("spawns").forGetter(StructureSpawnOverride::spawns)).apply((com.mojang.datafixers.kinds.Applicative)i, StructureSpawnOverride::new));
/*    */   }
/*    */   
/*    */   public enum BoundingBoxType
/*    */     implements net.minecraft.util.StringRepresentable
/*    */   {
/* 19 */     PIECE("piece"),
/* 20 */     STRUCTURE("full");
/*    */     
/* 22 */     public static final com.mojang.serialization.Codec<BoundingBoxType> CODEC = (com.mojang.serialization.Codec<BoundingBoxType>)net.minecraft.util.StringRepresentable.fromEnum(BoundingBoxType::values);
/*    */     
/*    */     private final String id;
/*    */     
/*    */     BoundingBoxType(String id) {
/* 27 */       this.id = id;
/*    */     }
/*    */ 
/*    */     
/*    */     public String getSerializedName() {
/* 32 */       return this.id;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/StructureSpawnOverride.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */