/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ 
/*    */ public interface LevelData {
/*    */   public static final class RespawnData extends Record {
/*    */     private final GlobalPos globalPos;
/*    */     private final float yaw;
/*    */     private final float pitch;
/*    */     
/* 21 */     public RespawnData(GlobalPos globalPos, float yaw, float pitch) { this.globalPos = globalPos; this.yaw = yaw; this.pitch = pitch; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/LevelData$RespawnData;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 21 */       //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelData$RespawnData; } public GlobalPos globalPos() { return this.globalPos; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/LevelData$RespawnData;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelData$RespawnData; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/LevelData$RespawnData;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/storage/LevelData$RespawnData;
/* 21 */       //   0	8	1	o	Ljava/lang/Object; } public float yaw() { return this.yaw; } public float pitch() { return this.pitch; }
/* 22 */      public static final RespawnData DEFAULT = new RespawnData(GlobalPos.of(Level.OVERWORLD, BlockPos.ZERO), 0.0F, 0.0F); public static final com.mojang.serialization.MapCodec<RespawnData> MAP_CODEC; static {
/* 23 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)GlobalPos.MAP_CODEC.forGetter(RespawnData::globalPos), (App)Codec.floatRange(-180.0F, 180.0F).fieldOf("yaw").forGetter(RespawnData::yaw), (App)Codec.floatRange(-90.0F, 90.0F).fieldOf("pitch").forGetter(RespawnData::pitch)).apply((com.mojang.datafixers.kinds.Applicative)i, RespawnData::new));
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 28 */     public static final Codec<RespawnData> CODEC = MAP_CODEC.codec();
/* 29 */     public static final net.minecraft.network.codec.StreamCodec<io.netty.buffer.ByteBuf, RespawnData> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(GlobalPos.STREAM_CODEC, RespawnData::globalPos, net.minecraft.network.codec.ByteBufCodecs.FLOAT, RespawnData::yaw, net.minecraft.network.codec.ByteBufCodecs.FLOAT, RespawnData::pitch, RespawnData::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public static RespawnData of(ResourceKey<Level> dimension, BlockPos pos, float yaw, float pitch) {
/* 37 */       return new RespawnData(GlobalPos.of(dimension, pos.immutable()), Mth.wrapDegrees(yaw), Mth.clamp(pitch, -90.0F, 90.0F));
/*    */     }
/*    */     
/*    */     public ResourceKey<Level> dimension() {
/* 41 */       return this.globalPos.dimension();
/*    */     }
/*    */     
/*    */     public BlockPos pos() {
/* 45 */       return this.globalPos.pos();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default void fillCrashReportCategory(CrashReportCategory category, LevelHeightAccessor levelHeightAccessor) {
/* 68 */     category.setDetail("Level spawn location", () -> CrashReportCategory.formatLocation(levelHeightAccessor, getRespawnData().pos()));
/* 69 */     category.setDetail("Level time", () -> String.format(java.util.Locale.ROOT, "%d game time, %d day time", new Object[] { getGameTime(), getDayTime() }));
/*    */   }
/*    */   
/*    */   RespawnData getRespawnData();
/*    */   
/*    */   long getGameTime();
/*    */   
/*    */   long getDayTime();
/*    */   
/*    */   boolean isThundering();
/*    */   
/*    */   boolean isRaining();
/*    */   
/*    */   void setRaining(boolean paramBoolean);
/*    */   
/*    */   boolean isHardcore();
/*    */   
/*    */   net.minecraft.world.Difficulty getDifficulty();
/*    */   
/*    */   boolean isDifficultyLocked();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/LevelData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */