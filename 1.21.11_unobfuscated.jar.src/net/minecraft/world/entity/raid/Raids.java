/*     */ package net.minecraft.world.entity.raid;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.List;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.PoiTypeTags;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Raids extends net.minecraft.world.level.saveddata.SavedData {
/*     */   private static final String RAID_FILE_ID = "raids";
/*     */   public static final Codec<Raids> CODEC;
/*     */   
/*     */   static {
/*  35 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)RaidWithId.CODEC.listOf().optionalFieldOf("raids", List.of()).forGetter(()), (App)Codec.INT.fieldOf("next_id").forGetter(()), (App)Codec.INT.fieldOf("tick").forGetter(())).apply((Applicative)i, Raids::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   public static final SavedDataType<Raids> TYPE = new SavedDataType("raids", Raids::new, CODEC, DataFixTypes.SAVED_DATA_RAIDS);
/*  42 */   public static final SavedDataType<Raids> TYPE_END = new SavedDataType("raids_end", Raids::new, CODEC, DataFixTypes.SAVED_DATA_RAIDS);
/*     */ 
/*     */   
/*  45 */   private final Int2ObjectMap<Raid> raidMap = (Int2ObjectMap<Raid>)new Int2ObjectOpenHashMap();
/*  46 */   private int nextId = 1;
/*     */   private int tick;
/*     */   
/*     */   public static SavedDataType<Raids> getType(Holder<DimensionType> type) {
/*  50 */     if (type.is(BuiltinDimensionTypes.END)) {
/*  51 */       return TYPE_END;
/*     */     }
/*  53 */     return TYPE;
/*     */   }
/*     */   
/*     */   public Raids() {
/*  57 */     setDirty();
/*     */   }
/*     */   
/*     */   private Raids(List<RaidWithId> raids, int nextId, int tick) {
/*  61 */     for (RaidWithId raid : raids) {
/*  62 */       this.raidMap.put(raid.id, raid.raid);
/*     */     }
/*  64 */     this.nextId = nextId;
/*  65 */     this.tick = tick;
/*     */   }
/*     */   
/*     */   public Raid get(int raidId) {
/*  69 */     return (Raid)this.raidMap.get(raidId);
/*     */   }
/*     */   
/*     */   public OptionalInt getId(Raid raid) {
/*  73 */     for (ObjectIterator<Int2ObjectMap.Entry<Raid>> objectIterator = this.raidMap.int2ObjectEntrySet().iterator(); objectIterator.hasNext(); ) { Int2ObjectMap.Entry<Raid> entry = objectIterator.next();
/*  74 */       if (entry.getValue() == raid) {
/*  75 */         return OptionalInt.of(entry.getIntKey());
/*     */       } }
/*     */     
/*  78 */     return OptionalInt.empty();
/*     */   }
/*     */   
/*     */   public void tick(ServerLevel level) {
/*  82 */     this.tick++;
/*  83 */     ObjectIterator<Raid> objectIterator = this.raidMap.values().iterator();
/*     */     
/*  85 */     while (objectIterator.hasNext()) {
/*  86 */       Raid raid = objectIterator.next();
/*  87 */       if (!((Boolean)level.getGameRules().get(GameRules.RAIDS))) {
/*  88 */         raid.stop();
/*     */       }
/*  90 */       if (raid.isStopped()) {
/*  91 */         objectIterator.remove();
/*  92 */         setDirty();
/*     */         
/*     */         continue;
/*     */       } 
/*  96 */       raid.tick(level);
/*     */     } 
/*     */ 
/*     */     
/* 100 */     if (this.tick % 200 == 0) {
/* 101 */       setDirty();
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean canJoinRaid(Raider raider) {
/* 106 */     return (raider.isAlive() && raider.canJoinRaid() && raider.getNoActionTime() <= 2400);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Raid createOrExtendRaid(ServerPlayer player, BlockPos raidPosition) {
/*     */     BlockPos raidCenterPos;
/* 114 */     if (player.isSpectator()) {
/* 115 */       return null;
/*     */     }
/*     */     
/* 118 */     ServerLevel level = player.level();
/* 119 */     if (!((Boolean)level.getGameRules().get(GameRules.RAIDS))) {
/* 120 */       return null;
/*     */     }
/*     */     
/* 123 */     if (!((Boolean)level.environmentAttributes().getValue(net.minecraft.world.attribute.EnvironmentAttributes.CAN_START_RAID, raidPosition))) {
/* 124 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 128 */     List<PoiRecord> posses = level.getPoiManager().getInRange(e -> e.is(PoiTypeTags.VILLAGE), raidPosition, 64, net.minecraft.world.entity.ai.village.poi.PoiManager.Occupancy.IS_OCCUPIED).toList();
/* 129 */     int count = 0;
/* 130 */     Vec3 posTotals = Vec3.ZERO;
/* 131 */     for (PoiRecord p : posses) {
/* 132 */       BlockPos pos = p.getPos();
/* 133 */       posTotals = posTotals.add(pos.getX(), pos.getY(), pos.getZ());
/* 134 */       count++;
/*     */     } 
/*     */ 
/*     */     
/* 138 */     if (count > 0) {
/*     */       
/* 140 */       posTotals = posTotals.scale(1.0D / count);
/* 141 */       raidCenterPos = BlockPos.containing((net.minecraft.core.Position)posTotals);
/*     */     } else {
/*     */       
/* 144 */       raidCenterPos = raidPosition;
/*     */     } 
/*     */     
/* 147 */     Raid raid = getOrCreateRaid(level, raidCenterPos);
/*     */     
/* 149 */     if (!raid.isStarted() && !this.raidMap.containsValue(raid)) {
/* 150 */       this.raidMap.put(getUniqueId(), raid);
/*     */     }
/*     */     
/* 153 */     if (!raid.isStarted() || raid.getRaidOmenLevel() < raid.getMaxRaidOmenLevel()) {
/* 154 */       raid.absorbRaidOmen(player);
/*     */     }
/*     */     
/* 157 */     setDirty();
/*     */     
/* 159 */     return raid;
/*     */   }
/*     */   
/*     */   private Raid getOrCreateRaid(ServerLevel level, BlockPos pos) {
/* 163 */     Raid raid = level.getRaidAt(pos);
/* 164 */     return (raid != null) ? raid : new Raid(pos, level.getDifficulty());
/*     */   }
/*     */   
/*     */   public static Raids load(CompoundTag tag) {
/* 168 */     return CODEC.parse((com.mojang.serialization.DynamicOps)net.minecraft.nbt.NbtOps.INSTANCE, tag).resultOrPartial().orElseGet(Raids::new);
/*     */   }
/*     */   
/*     */   private int getUniqueId() {
/* 172 */     return this.nextId++;
/*     */   }
/*     */   
/*     */   public Raid getNearbyRaid(BlockPos pos, int maxDistSqr) {
/* 176 */     Raid closest = null;
/* 177 */     double closestDistanceSqr = maxDistSqr;
/* 178 */     for (ObjectIterator<Raid> objectIterator = this.raidMap.values().iterator(); objectIterator.hasNext(); ) { Raid raid = objectIterator.next();
/* 179 */       double distance = raid.getCenter().distSqr((net.minecraft.core.Vec3i)pos);
/* 180 */       if (!raid.isActive()) {
/*     */         continue;
/*     */       }
/* 183 */       if (distance < closestDistanceSqr) {
/* 184 */         closest = raid;
/* 185 */         closestDistanceSqr = distance;
/*     */       }  }
/*     */     
/* 188 */     return closest;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/*     */   public List<BlockPos> getRaidCentersInChunk(ChunkPos chunkPos) {
/* 195 */     java.util.Objects.requireNonNull(chunkPos); return this.raidMap.values().stream().map(Raid::getCenter).filter(chunkPos::contains)
/* 196 */       .toList();
/*     */   }
/*     */   private static final class RaidWithId extends Record { private final int id; private final Raid raid; public static final Codec<RaidWithId> CODEC;
/* 199 */     private RaidWithId(int id, Raid raid) { this.id = id; this.raid = raid; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/raid/Raids$RaidWithId;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #199	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 199 */       //   0	7	0	this	Lnet/minecraft/world/entity/raid/Raids$RaidWithId; } public int id() { return this.id; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/raid/Raids$RaidWithId;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #199	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/raid/Raids$RaidWithId; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/raid/Raids$RaidWithId;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #199	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/raid/Raids$RaidWithId;
/* 199 */       //   0	8	1	o	Ljava/lang/Object; } public Raid raid() { return this.raid; } static {
/* 200 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("id").forGetter(RaidWithId::id), (App)Raid.MAP_CODEC.forGetter(RaidWithId::raid)).apply((Applicative)i, RaidWithId::new));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public static RaidWithId from(Int2ObjectMap.Entry<Raid> entry) {
/* 206 */       return new RaidWithId(entry.getIntKey(), (Raid)entry.getValue());
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/raid/Raids.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */