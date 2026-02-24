/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public final class SavedTick<T> extends Record {
/*    */   private final T type;
/*    */   private final BlockPos pos;
/*    */   private final int delay;
/*    */   private final TickPriority priority;
/*    */   
/* 14 */   public SavedTick(T type, BlockPos pos, int delay, TickPriority priority) { this.type = type; this.pos = pos; this.delay = delay; this.priority = priority; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/ticks/SavedTick;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/ticks/SavedTick;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/ticks/SavedTick<TT;>; } public T type() { return this.type; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/ticks/SavedTick;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/ticks/SavedTick;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/ticks/SavedTick<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/ticks/SavedTick;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/ticks/SavedTick;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 14 */     //   0	8	0	this	Lnet/minecraft/world/ticks/SavedTick<TT;>; } public BlockPos pos() { return this.pos; } public int delay() { return this.delay; } public TickPriority priority() { return this.priority; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> Codec<SavedTick<T>> codec(Codec<T> typeCodec) {
/* 21 */     com.mojang.serialization.MapCodec<BlockPos> posCodec = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("x").forGetter(net.minecraft.core.Vec3i::getX), (App)Codec.INT.fieldOf("y").forGetter(net.minecraft.core.Vec3i::getY), (App)Codec.INT.fieldOf("z").forGetter(net.minecraft.core.Vec3i::getZ)).apply((com.mojang.datafixers.kinds.Applicative)i, BlockPos::new));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 26 */     return RecordCodecBuilder.create(i -> i.group((App)typeCodec.fieldOf("i").forGetter(SavedTick::type), (App)posCodec.forGetter(SavedTick::pos), (App)Codec.INT.fieldOf("t").forGetter(SavedTick::delay), (App)TickPriority.CODEC.fieldOf("p").forGetter(SavedTick::priority)).apply((com.mojang.datafixers.kinds.Applicative)i, SavedTick::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public static final it.unimi.dsi.fastutil.Hash.Strategy<SavedTick<?>> UNIQUE_TICK_HASH = new it.unimi.dsi.fastutil.Hash.Strategy<SavedTick<?>>()
/*    */     {
/*    */       public int hashCode(SavedTick<?> o) {
/* 37 */         return 31 * o.pos().hashCode() + o.type().hashCode();
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean equals(SavedTick<?> a, SavedTick<?> b) {
/* 42 */         if (a == b) {
/* 43 */           return true;
/*    */         }
/* 45 */         if (a == null || b == null) {
/* 46 */           return false;
/*    */         }
/* 48 */         return (a.type() == b.type() && a.pos().equals(b.pos()));
/*    */       }
/*    */     };
/*    */   
/*    */   public static <T> java.util.List<SavedTick<T>> filterTickListForChunk(java.util.List<SavedTick<T>> savedTicks, net.minecraft.world.level.ChunkPos chunkPos) {
/* 53 */     long posKey = chunkPos.toLong();
/* 54 */     return savedTicks.stream()
/* 55 */       .filter(tick -> (net.minecraft.world.level.ChunkPos.asLong(tick.pos()) == posKey))
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 60 */       .toList();
/*    */   }
/*    */   
/*    */   public ScheduledTick<T> unpack(long currentTick, long currentSubTick) {
/* 64 */     return new ScheduledTick<>(this.type, this.pos, currentTick + this.delay, this.priority, currentSubTick);
/*    */   }
/*    */   
/*    */   public static <T> SavedTick<T> probe(T type, BlockPos pos) {
/* 68 */     return new SavedTick<>(type, pos, 0, TickPriority.NORMAL);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/ticks/SavedTick.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */