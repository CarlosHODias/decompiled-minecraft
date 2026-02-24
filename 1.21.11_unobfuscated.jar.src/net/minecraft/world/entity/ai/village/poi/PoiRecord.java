/*     */ package net.minecraft.world.entity.ai.village.poi;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.RegistryFixedCodec;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ 
/*     */ public class PoiRecord {
/*     */   private final BlockPos pos;
/*     */   private final Holder<PoiType> poiType;
/*     */   private int freeTickets;
/*     */   private final Runnable setDirty;
/*     */   
/*     */   private PoiRecord(BlockPos pos, Holder<PoiType> poiType, int freeTickets, Runnable setDirty) {
/*  20 */     this.pos = pos.immutable();
/*  21 */     this.poiType = poiType;
/*  22 */     this.freeTickets = freeTickets;
/*  23 */     this.setDirty = setDirty;
/*     */   }
/*     */   
/*     */   public PoiRecord(BlockPos pos, Holder<PoiType> poiType, Runnable setDirty) {
/*  27 */     this(pos, poiType, ((PoiType)poiType.value()).maxTickets(), setDirty);
/*     */   }
/*     */   
/*     */   public Packed pack() {
/*  31 */     return new Packed(this.pos, this.poiType, this.freeTickets);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   @VisibleForDebug
/*     */   public int getFreeTickets() {
/*  37 */     return this.freeTickets;
/*     */   }
/*     */   
/*     */   protected boolean acquireTicket() {
/*  41 */     if (this.freeTickets <= 0) {
/*  42 */       return false;
/*     */     }
/*     */     
/*  45 */     this.freeTickets--;
/*  46 */     this.setDirty.run();
/*  47 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean releaseTicket() {
/*  51 */     if (this.freeTickets >= ((PoiType)this.poiType.value()).maxTickets()) {
/*  52 */       return false;
/*     */     }
/*     */     
/*  55 */     this.freeTickets++;
/*  56 */     this.setDirty.run();
/*  57 */     return true;
/*     */   }
/*     */   
/*     */   public boolean hasSpace() {
/*  61 */     return (this.freeTickets > 0);
/*     */   }
/*     */   
/*     */   public boolean isOccupied() {
/*  65 */     return (this.freeTickets != ((PoiType)this.poiType.value()).maxTickets());
/*     */   }
/*     */   
/*     */   public BlockPos getPos() {
/*  69 */     return this.pos;
/*     */   }
/*     */   
/*     */   public Holder<PoiType> getPoiType() {
/*  73 */     return this.poiType;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  78 */     if (this == o) {
/*  79 */       return true;
/*     */     }
/*  81 */     if (o == null || getClass() != o.getClass()) {
/*  82 */       return false;
/*     */     }
/*     */     
/*  85 */     return java.util.Objects.equals(this.pos, ((PoiRecord)o).pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  90 */     return this.pos.hashCode();
/*     */   }
/*     */   public static final class Packed extends Record { private final BlockPos pos; private final Holder<PoiType> poiType; private final int freeTickets; public static final Codec<Packed> CODEC;
/*  93 */     public Packed(BlockPos pos, Holder<PoiType> poiType, int freeTickets) { this.pos = pos; this.poiType = poiType; this.freeTickets = freeTickets; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/village/poi/PoiRecord$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #93	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  93 */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiRecord$Packed; } public BlockPos pos() { return this.pos; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/village/poi/PoiRecord$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #93	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiRecord$Packed; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/village/poi/PoiRecord$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #93	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiRecord$Packed;
/*  93 */       //   0	8	1	o	Ljava/lang/Object; } public Holder<PoiType> poiType() { return this.poiType; } public int freeTickets() { return this.freeTickets; } static {
/*  94 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockPos.CODEC.fieldOf("pos").forGetter(Packed::pos), (App)RegistryFixedCodec.create(Registries.POINT_OF_INTEREST_TYPE).fieldOf("type").forGetter(Packed::poiType), (App)Codec.INT.fieldOf("free_tickets").orElse(0).forGetter(Packed::freeTickets)).apply((Applicative)i, Packed::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public PoiRecord unpack(Runnable setDirty) {
/* 101 */       return new PoiRecord(this.pos, this.poiType, this.freeTickets, setDirty);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/village/poi/PoiRecord.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */