/*     */ package net.minecraft.core;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.stream.IntStream;
/*     */ import javax.annotation.concurrent.Immutable;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import org.joml.Vector3i;
/*     */ 
/*     */ @Immutable
/*     */ public class Vec3i implements Comparable<Vec3i> {
/*     */   static {
/*  18 */     CODEC = Codec.INT_STREAM.comapFlatMap(input -> Util.fixedSize(input, 3).map(()), pos -> IntStream.of(new int[] { pos.getX(), pos.getY(), pos.getZ() }));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Codec<Vec3i> CODEC;
/*  23 */   public static final StreamCodec<ByteBuf, Vec3i> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, Vec3i::getX, ByteBufCodecs.VAR_INT, Vec3i::getY, ByteBufCodecs.VAR_INT, Vec3i::getZ, Vec3i::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Codec<Vec3i> offsetCodec(int maxOffsetPerAxis) {
/*  34 */     return CODEC.validate(value -> 
/*  35 */         (Math.abs(value.getX()) < maxOffsetPerAxis && Math.abs(value.getY()) < maxOffsetPerAxis && Math.abs(value.getZ()) < maxOffsetPerAxis) ? DataResult.success(value) : DataResult.error(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   public static final Vec3i ZERO = new Vec3i(0, 0, 0);
/*     */   
/*     */   private int x;
/*     */   private int y;
/*     */   private int z;
/*     */   
/*     */   public Vec3i(int x, int y, int z) {
/*  50 */     this.x = x;
/*  51 */     this.y = y;
/*  52 */     this.z = z;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*     */     Vec3i vec3i;
/*  57 */     if (this == o) {
/*  58 */       return true;
/*     */     }
/*  60 */     if (o instanceof Vec3i) { vec3i = (Vec3i)o; }
/*  61 */     else { return false; }
/*     */     
/*  63 */     return (getX() == vec3i.getX() && getY() == vec3i.getY() && getZ() == vec3i.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  68 */     return (getY() + getZ() * 31) * 31 + getX();
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Vec3i pos) {
/*  73 */     if (getY() == pos.getY()) {
/*  74 */       if (getZ() == pos.getZ()) {
/*  75 */         return getX() - pos.getX();
/*     */       }
/*  77 */       return getZ() - pos.getZ();
/*     */     } 
/*  79 */     return getY() - pos.getY();
/*     */   }
/*     */   
/*     */   public int getX() {
/*  83 */     return this.x;
/*     */   }
/*     */   
/*     */   public int getY() {
/*  87 */     return this.y;
/*     */   }
/*     */   
/*     */   public int getZ() {
/*  91 */     return this.z;
/*     */   }
/*     */   
/*     */   protected Vec3i setX(int x) {
/*  95 */     this.x = x;
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   protected Vec3i setY(int y) {
/* 100 */     this.y = y;
/* 101 */     return this;
/*     */   }
/*     */   
/*     */   protected Vec3i setZ(int z) {
/* 105 */     this.z = z;
/* 106 */     return this;
/*     */   }
/*     */   
/*     */   public Vec3i offset(int x, int y, int z) {
/* 110 */     if (x == 0 && y == 0 && z == 0) {
/* 111 */       return this;
/*     */     }
/* 113 */     return new Vec3i(getX() + x, getY() + y, getZ() + z);
/*     */   }
/*     */   
/*     */   public Vec3i offset(Vec3i vec) {
/* 117 */     return offset(vec.getX(), vec.getY(), vec.getZ());
/*     */   }
/*     */   
/*     */   public Vec3i subtract(Vec3i vec) {
/* 121 */     return offset(-vec.getX(), -vec.getY(), -vec.getZ());
/*     */   }
/*     */   
/*     */   public Vec3i multiply(int scale) {
/* 125 */     if (scale == 1)
/* 126 */       return this; 
/* 127 */     if (scale == 0) {
/* 128 */       return ZERO;
/*     */     }
/* 130 */     return new Vec3i(getX() * scale, getY() * scale, getZ() * scale);
/*     */   }
/*     */   
/*     */   public Vec3i multiply(int xScale, int yScale, int zScale) {
/* 134 */     return new Vec3i(getX() * xScale, getY() * yScale, getZ() * zScale);
/*     */   }
/*     */   
/*     */   public Vec3i above() {
/* 138 */     return above(1);
/*     */   }
/*     */   
/*     */   public Vec3i above(int steps) {
/* 142 */     return relative(Direction.UP, steps);
/*     */   }
/*     */   
/*     */   public Vec3i below() {
/* 146 */     return below(1);
/*     */   }
/*     */   
/*     */   public Vec3i below(int steps) {
/* 150 */     return relative(Direction.DOWN, steps);
/*     */   }
/*     */   
/*     */   public Vec3i north() {
/* 154 */     return north(1);
/*     */   }
/*     */   
/*     */   public Vec3i north(int steps) {
/* 158 */     return relative(Direction.NORTH, steps);
/*     */   }
/*     */   
/*     */   public Vec3i south() {
/* 162 */     return south(1);
/*     */   }
/*     */   
/*     */   public Vec3i south(int steps) {
/* 166 */     return relative(Direction.SOUTH, steps);
/*     */   }
/*     */   
/*     */   public Vec3i west() {
/* 170 */     return west(1);
/*     */   }
/*     */   
/*     */   public Vec3i west(int steps) {
/* 174 */     return relative(Direction.WEST, steps);
/*     */   }
/*     */   
/*     */   public Vec3i east() {
/* 178 */     return east(1);
/*     */   }
/*     */   
/*     */   public Vec3i east(int steps) {
/* 182 */     return relative(Direction.EAST, steps);
/*     */   }
/*     */   
/*     */   public Vec3i relative(Direction direction) {
/* 186 */     return relative(direction, 1);
/*     */   }
/*     */   
/*     */   public Vec3i relative(Direction direction, int steps) {
/* 190 */     if (steps == 0) {
/* 191 */       return this;
/*     */     }
/* 193 */     return new Vec3i(getX() + direction.getStepX() * steps, getY() + direction.getStepY() * steps, getZ() + direction.getStepZ() * steps);
/*     */   }
/*     */   
/*     */   public Vec3i relative(Direction.Axis axis, int steps) {
/* 197 */     if (steps == 0) {
/* 198 */       return this;
/*     */     }
/* 200 */     int xStep = (axis == Direction.Axis.X) ? steps : 0;
/* 201 */     int yStep = (axis == Direction.Axis.Y) ? steps : 0;
/* 202 */     int zStep = (axis == Direction.Axis.Z) ? steps : 0;
/* 203 */     return new Vec3i(getX() + xStep, getY() + yStep, getZ() + zStep);
/*     */   }
/*     */   
/*     */   public Vec3i cross(Vec3i upVector) {
/* 207 */     return new Vec3i(getY() * upVector.getZ() - getZ() * upVector.getY(), getZ() * upVector.getX() - getX() * upVector.getZ(), getX() * upVector.getY() - getY() * upVector.getX());
/*     */   }
/*     */   
/*     */   public boolean closerThan(Vec3i pos, double distance) {
/* 211 */     return (distSqr(pos) < Mth.square(distance));
/*     */   }
/*     */   
/*     */   public boolean closerToCenterThan(Position pos, double distance) {
/* 215 */     return (distToCenterSqr(pos) < Mth.square(distance));
/*     */   }
/*     */   
/*     */   public double distSqr(Vec3i pos) {
/* 219 */     return distToLowCornerSqr(pos.getX(), pos.getY(), pos.getZ());
/*     */   }
/*     */   
/*     */   public double distToCenterSqr(Position pos) {
/* 223 */     return distToCenterSqr(pos.x(), pos.y(), pos.z());
/*     */   }
/*     */   
/*     */   public double distToCenterSqr(double x, double y, double z) {
/* 227 */     double dx = getX() + 0.5D - x;
/* 228 */     double dy = getY() + 0.5D - y;
/* 229 */     double dz = getZ() + 0.5D - z;
/* 230 */     return dx * dx + dy * dy + dz * dz;
/*     */   }
/*     */   
/*     */   public double distToLowCornerSqr(double x, double y, double z) {
/* 234 */     double dx = getX() - x;
/* 235 */     double dy = getY() - y;
/* 236 */     double dz = getZ() - z;
/* 237 */     return dx * dx + dy * dy + dz * dz;
/*     */   }
/*     */   
/*     */   public int distManhattan(Vec3i pos) {
/* 241 */     float xd = Math.abs(pos.getX() - getX());
/* 242 */     float yd = Math.abs(pos.getY() - getY());
/* 243 */     float zd = Math.abs(pos.getZ() - getZ());
/* 244 */     return (int)(xd + yd + zd);
/*     */   }
/*     */   
/*     */   public int distChessboard(Vec3i pos) {
/* 248 */     int xd = Math.abs(getX() - pos.getX());
/* 249 */     int yd = Math.abs(getY() - pos.getY());
/* 250 */     int zd = Math.abs(getZ() - pos.getZ());
/* 251 */     return Math.max(Math.max(xd, yd), zd);
/*     */   }
/*     */   
/*     */   public int get(Direction.Axis axis) {
/* 255 */     return axis.choose(this.x, this.y, this.z);
/*     */   }
/*     */   
/*     */   public Vector3i toMutable() {
/* 259 */     return new Vector3i(this.x, this.y, this.z);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 264 */     return MoreObjects.toStringHelper(this)
/* 265 */       .add("x", getX())
/* 266 */       .add("y", getY())
/* 267 */       .add("z", getZ())
/* 268 */       .toString();
/*     */   }
/*     */   
/*     */   public String toShortString() {
/* 272 */     return "" + getX() + ", " + getX() + ", " + getY();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/Vec3i.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */