/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.InclusiveRange;
/*    */ import net.minecraft.world.entity.EquipmentTable;
/*    */ 
/*    */ public final class SpawnData extends Record {
/*    */   private final CompoundTag entityToSpawn;
/*    */   private final Optional<CustomSpawnRules> customSpawnRules;
/*    */   private final Optional<EquipmentTable> equipment;
/*    */   public static final String ENTITY_TAG = "entity";
/*    */   
/* 18 */   public CompoundTag entityToSpawn() { return this.entityToSpawn; } public static final com.mojang.serialization.Codec<SpawnData> CODEC; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/SpawnData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/SpawnData; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/SpawnData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/SpawnData; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/SpawnData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/SpawnData;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<CustomSpawnRules> customSpawnRules() { return this.customSpawnRules; } public Optional<EquipmentTable> equipment() { return this.equipment; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 25 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)CompoundTag.CODEC.fieldOf("entity").forGetter(()), (App)CustomSpawnRules.CODEC.optionalFieldOf("custom_spawn_rules").forGetter(()), (App)EquipmentTable.CODEC.optionalFieldOf("equipment").forGetter(())).apply((Applicative)i, SpawnData::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static final com.mojang.serialization.Codec<net.minecraft.util.random.WeightedList<SpawnData>> LIST_CODEC = net.minecraft.util.random.WeightedList.codec(CODEC);
/*    */   
/*    */   public SpawnData() {
/* 34 */     this(new CompoundTag(), Optional.empty(), Optional.empty());
/*    */   }
/*    */   
/*    */   public SpawnData(CompoundTag entityToSpawn, Optional<CustomSpawnRules> customSpawnRules, Optional<EquipmentTable> equipment) {
/* 38 */     Optional<Identifier> id = entityToSpawn.read("id", Identifier.CODEC);
/*    */     
/* 40 */     if (id.isPresent()) {
/* 41 */       entityToSpawn.store("id", Identifier.CODEC, id.get());
/*    */     } else {
/* 43 */       entityToSpawn.remove("id");
/*    */     } 
/*    */     this.entityToSpawn = entityToSpawn;
/*    */     this.customSpawnRules = customSpawnRules;
/*    */     this.equipment = equipment; } public CompoundTag getEntityToSpawn() {
/* 48 */     return this.entityToSpawn;
/*    */   }
/*    */   
/*    */   public Optional<CustomSpawnRules> getCustomSpawnRules() {
/* 52 */     return this.customSpawnRules;
/*    */   }
/*    */   
/*    */   public Optional<EquipmentTable> getEquipment() {
/* 56 */     return this.equipment;
/*    */   }
/*    */   public static final class CustomSpawnRules extends Record { private final InclusiveRange<Integer> blockLightLimit; private final InclusiveRange<Integer> skyLightLimit;
/* 59 */     public CustomSpawnRules(InclusiveRange<Integer> blockLightLimit, InclusiveRange<Integer> skyLightLimit) { this.blockLightLimit = blockLightLimit; this.skyLightLimit = skyLightLimit; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/SpawnData$CustomSpawnRules;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #59	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/SpawnData$CustomSpawnRules; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/SpawnData$CustomSpawnRules;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #59	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/SpawnData$CustomSpawnRules; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/SpawnData$CustomSpawnRules;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #59	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/SpawnData$CustomSpawnRules;
/* 59 */       //   0	8	1	o	Ljava/lang/Object; } public InclusiveRange<Integer> blockLightLimit() { return this.blockLightLimit; } public InclusiveRange<Integer> skyLightLimit() { return this.skyLightLimit; }
/*    */ 
/*    */ 
/*    */     
/* 63 */     private static final InclusiveRange<Integer> LIGHT_RANGE = new InclusiveRange(0, 15); public static final com.mojang.serialization.Codec<CustomSpawnRules> CODEC;
/*    */     
/*    */     private static com.mojang.serialization.DataResult<InclusiveRange<Integer>> checkLightBoundaries(InclusiveRange<Integer> range) {
/* 66 */       if (!LIGHT_RANGE.contains(range)) {
/* 67 */         return com.mojang.serialization.DataResult.error(() -> "Light values must be withing range " + String.valueOf(LIGHT_RANGE));
/*    */       }
/* 69 */       return com.mojang.serialization.DataResult.success(range);
/*    */     }
/*    */     
/*    */     private static com.mojang.serialization.MapCodec<InclusiveRange<Integer>> lightLimit(String name) {
/* 73 */       return InclusiveRange.INT.lenientOptionalFieldOf(name, LIGHT_RANGE).validate(CustomSpawnRules::checkLightBoundaries);
/*    */     }
/*    */     static {
/* 76 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)lightLimit("block_light_limit").forGetter(()), (App)lightLimit("sky_light_limit").forGetter(())).apply((Applicative)i, CustomSpawnRules::new));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public boolean isValidPosition(net.minecraft.core.BlockPos blockSpawnPos, net.minecraft.server.level.ServerLevel level) {
/* 83 */       return (this.blockLightLimit.isValueInRange(level.getBrightness(LightLayer.BLOCK, blockSpawnPos)) && 
/* 84 */         this.skyLightLimit.isValueInRange(level.getBrightness(LightLayer.SKY, blockSpawnPos)));
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/SpawnData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */