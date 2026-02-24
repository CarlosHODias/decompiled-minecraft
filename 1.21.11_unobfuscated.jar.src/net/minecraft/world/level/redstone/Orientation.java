/*     */ package net.minecraft.world.level.redstone;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Orientation
/*     */ {
/*  34 */   public static final StreamCodec<ByteBuf, Orientation> STREAM_CODEC = ByteBufCodecs.idMapper(Orientation::fromIndex, Orientation::getIndex);
/*     */   static {
/*  36 */     ORIENTATIONS = (Orientation[])Util.make(() -> {
/*     */           Orientation[] orientations = new Orientation[48];
/*     */           generateContext(new Orientation(Direction.UP, Direction.NORTH, SideBias.LEFT), orientations);
/*     */           return orientations;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static final Orientation[] ORIENTATIONS;
/*     */   
/*     */   private final Direction up;
/*     */   
/*     */   private final Direction front;
/*     */   
/*     */   private final Direction side;
/*     */   
/*     */   private final SideBias sideBias;
/*     */   private final int index;
/*     */   private final List<Direction> neighbors;
/*     */   private final List<Direction> horizontalNeighbors;
/*     */   private final List<Direction> verticalNeighbors;
/*  57 */   private final Map<Direction, Orientation> withFront = new EnumMap<>(Direction.class);
/*  58 */   private final Map<Direction, Orientation> withUp = new EnumMap<>(Direction.class);
/*  59 */   private final Map<SideBias, Orientation> withSideBias = new EnumMap<>(SideBias.class);
/*     */   
/*     */   private Orientation(Direction up, Direction front, SideBias sideBias) {
/*  62 */     this.up = up;
/*  63 */     this.front = front;
/*  64 */     this.sideBias = sideBias;
/*  65 */     this.index = generateIndex(up, front, sideBias);
/*     */     
/*  67 */     Vec3i rightVector = front.getUnitVec3i().cross(up.getUnitVec3i());
/*  68 */     Direction side = Direction.getNearest(rightVector, null);
/*  69 */     Objects.requireNonNull(side);
/*  70 */     if (this.sideBias == SideBias.RIGHT) {
/*  71 */       this.side = side;
/*     */     } else {
/*  73 */       this.side = side.getOpposite();
/*     */     } 
/*  75 */     this.neighbors = List.of(
/*  76 */         this.front.getOpposite(), this.front, this.side, 
/*     */ 
/*     */         
/*  79 */         this.side.getOpposite(), 
/*  80 */         this.up.getOpposite(), this.up);
/*     */ 
/*     */     
/*  83 */     this.horizontalNeighbors = this.neighbors.stream().filter(d -> (d.getAxis() != this.up.getAxis())).toList();
/*  84 */     this.verticalNeighbors = this.neighbors.stream().filter(d -> (d.getAxis() == this.up.getAxis())).toList();
/*     */   }
/*     */   
/*     */   public static Orientation of(Direction up, Direction front, SideBias sideBias) {
/*  88 */     return ORIENTATIONS[generateIndex(up, front, sideBias)];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Orientation withUp(Direction up) {
/*  98 */     return this.withUp.get(up);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Orientation withFront(Direction front) {
/* 108 */     return this.withFront.get(front);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Orientation withFrontPreserveUp(Direction front) {
/* 117 */     if (front.getAxis() == this.up.getAxis()) {
/* 118 */       return this;
/*     */     }
/* 120 */     return this.withFront.get(front);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Orientation withFrontAdjustSideBias(Direction front) {
/* 128 */     Orientation withFront = withFront(front);
/* 129 */     if (this.front == withFront.side) {
/* 130 */       return withFront.withMirror();
/*     */     }
/* 132 */     return withFront;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Orientation withSideBias(SideBias sideBias) {
/* 139 */     return this.withSideBias.get(sideBias);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Orientation withMirror() {
/* 146 */     return withSideBias(this.sideBias.getOpposite());
/*     */   }
/*     */   
/*     */   public Direction getFront() {
/* 150 */     return this.front;
/*     */   }
/*     */   
/*     */   public Direction getUp() {
/* 154 */     return this.up;
/*     */   }
/*     */   
/*     */   public Direction getSide() {
/* 158 */     return this.side;
/*     */   }
/*     */   
/*     */   public SideBias getSideBias() {
/* 162 */     return this.sideBias;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Direction> getDirections() {
/* 170 */     return this.neighbors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Direction> getHorizontalDirections() {
/* 179 */     return this.horizontalNeighbors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Direction> getVerticalDirections() {
/* 188 */     return this.verticalNeighbors;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 193 */     return "[up=" + String.valueOf(this.up) + ",front=" + String.valueOf(this.front) + ",sideBias=" + String.valueOf(this.sideBias) + "]";
/*     */   }
/*     */   
/*     */   public int getIndex() {
/* 197 */     return this.index;
/*     */   }
/*     */   
/*     */   public static Orientation fromIndex(int index) {
/* 201 */     return ORIENTATIONS[index];
/*     */   }
/*     */   
/*     */   public static Orientation random(RandomSource rand) {
/* 205 */     return (Orientation)Util.getRandom((Object[])ORIENTATIONS, rand);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Orientation generateContext(Orientation self, Orientation[] lookup) {
/* 210 */     if (lookup[self.getIndex()] != null) {
/* 211 */       return lookup[self.getIndex()];
/*     */     }
/* 213 */     lookup[self.getIndex()] = self;
/*     */     
/* 215 */     for (SideBias sideBias : SideBias.values()) {
/* 216 */       self.withSideBias.put(sideBias, generateContext(new Orientation(self.up, self.front, sideBias), lookup));
/*     */     }
/*     */     
/* 219 */     for (Direction facing : Direction.values()) {
/* 220 */       Direction up = self.up;
/*     */       
/* 222 */       if (facing == self.up) {
/* 223 */         up = self.front.getOpposite();
/*     */       }
/* 225 */       if (facing == self.up.getOpposite()) {
/* 226 */         up = self.front;
/*     */       }
/* 228 */       self.withFront.put(facing, generateContext(new Orientation(up, facing, self.sideBias), lookup));
/*     */     } 
/*     */     
/* 231 */     for (Direction facing : Direction.values()) {
/* 232 */       Direction front = self.front;
/*     */       
/* 234 */       if (facing == self.front) {
/* 235 */         front = self.up.getOpposite();
/*     */       }
/* 237 */       if (facing == self.front.getOpposite()) {
/* 238 */         front = self.up;
/*     */       }
/* 240 */       self.withUp.put(facing, generateContext(new Orientation(facing, front, self.sideBias), lookup));
/*     */     } 
/* 242 */     return self;
/*     */   }
/*     */   @VisibleForTesting
/*     */   protected static int generateIndex(Direction up, Direction front, SideBias sideBias) {
/*     */     int frontAxisKey;
/* 247 */     if (up.getAxis() == front.getAxis()) {
/* 248 */       throw new IllegalStateException("Up-vector and front-vector can not be on the same axis");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 253 */     if (up.getAxis() == Direction.Axis.Y) {
/*     */       
/* 255 */       frontAxisKey = (front.getAxis() == Direction.Axis.X) ? 1 : 0;
/*     */     } else {
/*     */       
/* 258 */       frontAxisKey = (front.getAxis() == Direction.Axis.Y) ? 1 : 0;
/*     */     } 
/* 260 */     int frontKey = frontAxisKey << 1 | front.getAxisDirection().ordinal();
/* 261 */     return ((up.ordinal() << 2) + frontKey << 1) + sideBias.ordinal();
/*     */   }
/*     */   
/*     */   public enum SideBias {
/* 265 */     LEFT("left"),
/* 266 */     RIGHT("right");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     SideBias(String name) {
/* 271 */       this.name = name;
/*     */     }
/*     */     
/*     */     public SideBias getOpposite() {
/* 275 */       return (this == LEFT) ? RIGHT : LEFT;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 280 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/redstone/Orientation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */