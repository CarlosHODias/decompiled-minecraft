/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ public enum Relative
/*     */ {
/*  12 */   X(0),
/*  13 */   Y(1),
/*  14 */   Z(2),
/*  15 */   Y_ROT(3),
/*  16 */   X_ROT(4),
/*  17 */   DELTA_X(5),
/*  18 */   DELTA_Y(6),
/*  19 */   DELTA_Z(7),
/*  20 */   ROTATE_DELTA(8);
/*     */   
/*  22 */   public static final Set<Relative> ALL = Set.of(values());
/*  23 */   public static final Set<Relative> ROTATION = Set.of(X_ROT, Y_ROT);
/*  24 */   public static final Set<Relative> DELTA = Set.of(DELTA_X, DELTA_Y, DELTA_Z, ROTATE_DELTA);
/*     */   
/*     */   @SafeVarargs
/*     */   public static Set<Relative> union(Set<Relative>... sets) {
/*  28 */     HashSet<Relative> set = new HashSet<>();
/*  29 */     for (Set<Relative> s : sets) {
/*  30 */       set.addAll(s);
/*     */     }
/*  32 */     return set;
/*     */   }
/*     */   
/*     */   public static Set<Relative> rotation(boolean relativeYRot, boolean relativeXRot) {
/*  36 */     Set<Relative> relatives = EnumSet.noneOf(Relative.class);
/*  37 */     if (relativeYRot) {
/*  38 */       relatives.add(Y_ROT);
/*     */     }
/*  40 */     if (relativeXRot) {
/*  41 */       relatives.add(X_ROT);
/*     */     }
/*  43 */     return relatives;
/*     */   }
/*     */   
/*     */   public static Set<Relative> position(boolean relativeX, boolean relativeY, boolean relativeZ) {
/*  47 */     Set<Relative> relatives = EnumSet.noneOf(Relative.class);
/*  48 */     if (relativeX) {
/*  49 */       relatives.add(X);
/*     */     }
/*  51 */     if (relativeY) {
/*  52 */       relatives.add(Y);
/*     */     }
/*  54 */     if (relativeZ) {
/*  55 */       relatives.add(Z);
/*     */     }
/*  57 */     return relatives;
/*     */   }
/*     */   
/*     */   public static Set<Relative> direction(boolean relativeX, boolean relativeY, boolean relativeZ) {
/*  61 */     Set<Relative> relatives = EnumSet.noneOf(Relative.class);
/*  62 */     if (relativeX) {
/*  63 */       relatives.add(DELTA_X);
/*     */     }
/*  65 */     if (relativeY) {
/*  66 */       relatives.add(DELTA_Y);
/*     */     }
/*  68 */     if (relativeZ) {
/*  69 */       relatives.add(DELTA_Z);
/*     */     }
/*  71 */     return relatives;
/*     */   }
/*     */   
/*  74 */   public static final StreamCodec<ByteBuf, Set<Relative>> SET_STREAM_CODEC = ByteBufCodecs.INT.map(Relative::unpack, Relative::pack);
/*     */   
/*     */   private final int bit;
/*     */   
/*     */   Relative(int bit) {
/*  79 */     this.bit = bit;
/*     */   }
/*     */   
/*     */   private int getMask() {
/*  83 */     return 1 << this.bit;
/*     */   }
/*     */   
/*     */   private boolean isSet(int value) {
/*  87 */     return ((value & getMask()) == getMask());
/*     */   }
/*     */   
/*     */   public static Set<Relative> unpack(int value) {
/*  91 */     Set<Relative> result = EnumSet.noneOf(Relative.class);
/*     */     
/*  93 */     for (Relative argument : values()) {
/*  94 */       if (argument.isSet(value)) {
/*  95 */         result.add(argument);
/*     */       }
/*     */     } 
/*     */     
/*  99 */     return result;
/*     */   }
/*     */   
/*     */   public static int pack(Set<Relative> set) {
/* 103 */     int result = 0;
/*     */     
/* 105 */     for (Relative argument : set) {
/* 106 */       result |= argument.getMask();
/*     */     }
/*     */     
/* 109 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/Relative.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */