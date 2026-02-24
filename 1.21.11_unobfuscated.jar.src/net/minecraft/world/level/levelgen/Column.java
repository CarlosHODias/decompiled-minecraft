/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Column
/*     */ {
/*     */   public static Range around(int lowest, int highest) {
/*  23 */     return new Range(lowest - 1, highest + 1);
/*     */   }
/*     */   
/*     */   public static Range inside(int floor, int ceiling) {
/*  27 */     return new Range(floor, ceiling);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Column below(int ceiling) {
/*  34 */     return new Ray(ceiling, false);
/*     */   }
/*     */   
/*     */   public static Column fromHighest(int highest) {
/*  38 */     return new Ray(highest + 1, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Column above(int floor) {
/*  45 */     return new Ray(floor, true);
/*     */   }
/*     */   
/*     */   public static Column fromLowest(int lowest) {
/*  49 */     return new Ray(lowest - 1, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Column line() {
/*  56 */     return Line.INSTANCE;
/*     */   }
/*     */   
/*     */   public static Column create(OptionalInt floor, OptionalInt ceiling) {
/*  60 */     if (floor.isPresent() && ceiling.isPresent()) {
/*  61 */       return inside(floor.getAsInt(), ceiling.getAsInt());
/*     */     }
/*     */     
/*  64 */     if (floor.isPresent()) {
/*  65 */       return above(floor.getAsInt());
/*     */     }
/*     */     
/*  68 */     if (ceiling.isPresent()) {
/*  69 */       return below(ceiling.getAsInt());
/*     */     }
/*     */     
/*  72 */     return line();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract OptionalInt getCeiling();
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract OptionalInt getFloor();
/*     */ 
/*     */   
/*     */   public abstract OptionalInt getHeight();
/*     */ 
/*     */   
/*     */   public Column withFloor(OptionalInt floor) {
/*  88 */     return create(floor, getCeiling());
/*     */   }
/*     */   
/*     */   public Column withCeiling(OptionalInt ceiling) {
/*  92 */     return create(getFloor(), ceiling);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Optional<Column> scan(LevelSimulatedReader level, BlockPos pos, int searchRange, Predicate<BlockState> insideColumn, Predicate<BlockState> validEdge) {
/* 102 */     BlockPos.MutableBlockPos mutablePos = pos.mutable();
/* 103 */     if (!level.isStateAtPosition(pos, insideColumn)) {
/* 104 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/* 108 */     int nearestEmptyY = pos.getY();
/* 109 */     OptionalInt ceiling = scanDirection(level, searchRange, insideColumn, validEdge, mutablePos, nearestEmptyY, Direction.UP);
/* 110 */     OptionalInt floor = scanDirection(level, searchRange, insideColumn, validEdge, mutablePos, nearestEmptyY, Direction.DOWN);
/*     */     
/* 112 */     return Optional.of(create(floor, ceiling));
/*     */   }
/*     */   
/*     */   private static OptionalInt scanDirection(LevelSimulatedReader level, int searchRange, Predicate<BlockState> insideColumn, Predicate<BlockState> validEdge, BlockPos.MutableBlockPos mutablePos, int nearestEmptyY, Direction direction) {
/* 116 */     mutablePos.setY(nearestEmptyY);
/* 117 */     for (int i = 1; i < searchRange && 
/* 118 */       level.isStateAtPosition((BlockPos)mutablePos, insideColumn); i++) {
/* 119 */       mutablePos.move(direction);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     return level.isStateAtPosition((BlockPos)mutablePos, validEdge) ? OptionalInt.of(mutablePos.getY()) : OptionalInt.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Range
/*     */     extends Column
/*     */   {
/*     */     private final int floor;
/*     */     private final int ceiling;
/*     */     
/*     */     protected Range(int floor, int ceiling) {
/* 136 */       this.floor = floor;
/* 137 */       this.ceiling = ceiling;
/* 138 */       if (height() < 0) {
/* 139 */         throw new IllegalArgumentException("Column of negative height: " + String.valueOf(this));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public OptionalInt getCeiling() {
/* 145 */       return OptionalInt.of(this.ceiling);
/*     */     }
/*     */ 
/*     */     
/*     */     public OptionalInt getFloor() {
/* 150 */       return OptionalInt.of(this.floor);
/*     */     }
/*     */ 
/*     */     
/*     */     public OptionalInt getHeight() {
/* 155 */       return OptionalInt.of(height());
/*     */     }
/*     */     
/*     */     public int ceiling() {
/* 159 */       return this.ceiling;
/*     */     }
/*     */     
/*     */     public int floor() {
/* 163 */       return this.floor;
/*     */     }
/*     */     
/*     */     public int height() {
/* 167 */       return this.ceiling - this.floor - 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 172 */       return "C(" + this.ceiling + "-" + this.floor + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Line
/*     */     extends Column
/*     */   {
/* 180 */     private static final Line INSTANCE = new Line();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OptionalInt getCeiling() {
/* 187 */       return OptionalInt.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public OptionalInt getFloor() {
/* 192 */       return OptionalInt.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public OptionalInt getHeight() {
/* 197 */       return OptionalInt.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 202 */       return "C(-)";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Ray
/*     */     extends Column
/*     */   {
/*     */     private final int edge;
/*     */     private final boolean pointingUp;
/*     */     
/*     */     public Ray(int edge, boolean pointingUp) {
/* 214 */       this.edge = edge;
/* 215 */       this.pointingUp = pointingUp;
/*     */     }
/*     */ 
/*     */     
/*     */     public OptionalInt getCeiling() {
/* 220 */       return this.pointingUp ? OptionalInt.empty() : OptionalInt.of(this.edge);
/*     */     }
/*     */ 
/*     */     
/*     */     public OptionalInt getFloor() {
/* 225 */       return this.pointingUp ? OptionalInt.of(this.edge) : OptionalInt.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public OptionalInt getHeight() {
/* 230 */       return OptionalInt.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 235 */       return this.pointingUp ? ("C(" + this.edge + "-)") : ("C(-" + this.edge + ")");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/Column.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */