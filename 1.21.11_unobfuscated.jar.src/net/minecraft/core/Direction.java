/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public enum Direction
/*     */   implements StringRepresentable
/*     */ {
/*  34 */   DOWN(0, 1, -1, "down", AxisDirection.NEGATIVE, Axis.Y, new Vec3i(0, -1, 0)),
/*  35 */   UP(1, 0, -1, "up", AxisDirection.POSITIVE, Axis.Y, new Vec3i(0, 1, 0)),
/*  36 */   NORTH(2, 3, 2, "north", AxisDirection.NEGATIVE, Axis.Z, new Vec3i(0, 0, -1)),
/*  37 */   SOUTH(3, 2, 0, "south", AxisDirection.POSITIVE, Axis.Z, new Vec3i(0, 0, 1)),
/*  38 */   WEST(4, 5, 1, "west", AxisDirection.NEGATIVE, Axis.X, new Vec3i(-1, 0, 0)),
/*  39 */   EAST(5, 4, 3, "east", AxisDirection.POSITIVE, Axis.X, new Vec3i(1, 0, 0));
/*     */ 
/*     */   
/*  42 */   public static final StringRepresentable.EnumCodec<Direction> CODEC = StringRepresentable.fromEnum(Direction::values);
/*  43 */   public static final Codec<Direction> VERTICAL_CODEC = CODEC.validate(Direction::verifyVertical);
/*     */   
/*  45 */   public static final IntFunction<Direction> BY_ID = ByIdMap.continuous(Direction::get3DDataValue, (Object[])values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/*     */   
/*  47 */   public static final StreamCodec<ByteBuf, Direction> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Direction::get3DDataValue);
/*     */   
/*     */   static {
/*  50 */     LEGACY_ID_CODEC = Codec.BYTE.xmap(Direction::from3DDataValue, d -> (byte)d.get3DDataValue());
/*     */     
/*  52 */     LEGACY_ID_CODEC_2D = Codec.BYTE.xmap(Direction::from2DDataValue, d -> (byte)d.get2DDataValue());
/*     */   }
/*  54 */   private static final ImmutableList<Axis> YXZ_AXIS_ORDER = ImmutableList.of(Axis.Y, Axis.X, Axis.Z);
/*  55 */   private static final ImmutableList<Axis> YZX_AXIS_ORDER = ImmutableList.of(Axis.Y, Axis.Z, Axis.X);
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
/*  67 */   private static final Direction[] VALUES = values(); @Deprecated
/*     */   public static final Codec<Direction> LEGACY_ID_CODEC; @Deprecated
/*  69 */   public static final Codec<Direction> LEGACY_ID_CODEC_2D; private final int data3d; private final int oppositeIndex; private final int data2d; private final String name; private final Axis axis; private final AxisDirection axisDirection; private final Vec3i normal; private final Vec3 normalVec3; private final Vector3fc normalVec3f; private static final Direction[] BY_3D_DATA; private static final Direction[] BY_2D_DATA; static { BY_3D_DATA = (Direction[])Arrays.<Direction>stream(VALUES).sorted(Comparator.comparingInt(d -> d.data3d)).toArray(x$0 -> new Direction[x$0]);
/*  70 */     BY_2D_DATA = (Direction[])Arrays.<Direction>stream(VALUES).filter(d -> d.getAxis().isHorizontal()).sorted(Comparator.comparingInt(d -> d.data2d)).toArray(x$0 -> new Direction[x$0]); }
/*     */   
/*     */   Direction(int data3d, int oppositeIndex, int data2d, String name, AxisDirection axisDirection, Axis axis, Vec3i normal) {
/*  73 */     this.data3d = data3d;
/*  74 */     this.data2d = data2d;
/*  75 */     this.oppositeIndex = oppositeIndex;
/*  76 */     this.name = name;
/*  77 */     this.axis = axis;
/*  78 */     this.axisDirection = axisDirection;
/*  79 */     this.normal = normal;
/*  80 */     this.normalVec3 = Vec3.atLowerCornerOf(normal);
/*  81 */     this.normalVec3f = (Vector3fc)new Vector3f(normal.getX(), normal.getY(), normal.getZ());
/*     */   }
/*     */   
/*     */   public static Direction[] orderedByNearest(Entity entity) {
/*  85 */     float pitch = entity.getViewXRot(1.0F) * 0.017453292F;
/*  86 */     float yaw = -entity.getViewYRot(1.0F) * 0.017453292F;
/*     */     
/*  88 */     float pitchSin = Mth.sin(pitch);
/*  89 */     float pitchCos = Mth.cos(pitch);
/*  90 */     float yawSin = Mth.sin(yaw);
/*  91 */     float yawCos = Mth.cos(yaw);
/*     */     
/*  93 */     boolean xPos = (yawSin > 0.0F);
/*  94 */     boolean yPos = (pitchSin < 0.0F);
/*  95 */     boolean zPos = (yawCos > 0.0F);
/*     */     
/*  97 */     float xYaw = xPos ? yawSin : -yawSin;
/*  98 */     float yMag = yPos ? -pitchSin : pitchSin;
/*  99 */     float zYaw = zPos ? yawCos : -yawCos;
/*     */     
/* 101 */     float xMag = xYaw * pitchCos;
/* 102 */     float zMag = zYaw * pitchCos;
/*     */     
/* 104 */     Direction axisX = xPos ? EAST : WEST;
/* 105 */     Direction axisY = yPos ? UP : DOWN;
/* 106 */     Direction axisZ = zPos ? SOUTH : NORTH;
/*     */     
/* 108 */     if (xYaw > zYaw) {
/* 109 */       if (yMag > xMag)
/* 110 */         return makeDirectionArray(axisY, axisX, axisZ); 
/* 111 */       if (zMag > yMag) {
/* 112 */         return makeDirectionArray(axisX, axisZ, axisY);
/*     */       }
/* 114 */       return makeDirectionArray(axisX, axisY, axisZ);
/*     */     } 
/*     */     
/* 117 */     if (yMag > zMag)
/* 118 */       return makeDirectionArray(axisY, axisZ, axisX); 
/* 119 */     if (xMag > yMag) {
/* 120 */       return makeDirectionArray(axisZ, axisX, axisY);
/*     */     }
/* 122 */     return makeDirectionArray(axisZ, axisY, axisX);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Direction[] makeDirectionArray(Direction axis1, Direction axis2, Direction axis3) {
/* 128 */     return new Direction[] { axis1, axis2, axis3, axis3.getOpposite(), axis2.getOpposite(), axis1.getOpposite() };
/*     */   }
/*     */   
/*     */   public static Direction rotate(Matrix4fc matrix, Direction facing) {
/* 132 */     Vector3f vec = matrix.transformDirection(facing.normalVec3f, new Vector3f());
/* 133 */     return getApproximateNearest(vec.x(), vec.y(), vec.z());
/*     */   }
/*     */   
/*     */   public static Collection<Direction> allShuffled(RandomSource random) {
/* 137 */     return Util.shuffledCopy((Object[])values(), random);
/*     */   }
/*     */   
/*     */   public static Stream<Direction> stream() {
/* 141 */     return Stream.of(VALUES);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getYRot(Direction direction) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual ordinal : ()I
/*     */     //   4: tableswitch default -> 55, 2 -> 36, 3 -> 41, 4 -> 45, 5 -> 50
/*     */     //   36: ldc 180.0
/*     */     //   38: goto -> 72
/*     */     //   41: fconst_0
/*     */     //   42: goto -> 72
/*     */     //   45: ldc 90.0
/*     */     //   47: goto -> 72
/*     */     //   50: ldc -90.0
/*     */     //   52: goto -> 72
/*     */     //   55: new java/lang/IllegalStateException
/*     */     //   58: dup
/*     */     //   59: aload_0
/*     */     //   60: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   63: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   68: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   71: athrow
/*     */     //   72: freturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #145	-> 0
/*     */     //   #146	-> 36
/*     */     //   #147	-> 41
/*     */     //   #148	-> 45
/*     */     //   #149	-> 50
/*     */     //   #150	-> 55
/*     */     //   #145	-> 72
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	73	0	direction	Lnet/minecraft/core/Direction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternionf getRotation() {
/* 155 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: case 3: case 4: case 5: break; }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 161 */       new Quaternionf().rotationXYZ(1.5707964F, 0.0F, -1.5707964F);
/*     */   }
/*     */ 
/*     */   
/*     */   public int get3DDataValue() {
/* 166 */     return this.data3d;
/*     */   }
/*     */   
/*     */   public int get2DDataValue() {
/* 170 */     return this.data2d;
/*     */   }
/*     */   
/*     */   public AxisDirection getAxisDirection() {
/* 174 */     return this.axisDirection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Direction getFacingAxis(Entity entity, Axis axis) {
/* 181 */     switch (axis.ordinal()) { default: throw new MatchException(null, null);
/* 182 */       case 0: if (EAST.isFacingAngle(entity.getViewYRot(1.0F)));
/* 183 */       case 2: if (SOUTH.isFacingAngle(entity.getViewYRot(1.0F)));
/* 184 */       case 1: if (entity.getViewXRot(1.0F) < 0.0F); break; }  return DOWN;
/*     */   }
/*     */ 
/*     */   
/*     */   public Direction getOpposite() {
/* 189 */     return from3DDataValue(this.oppositeIndex);
/*     */   }
/*     */   
/*     */   public Direction getClockWise(Axis axis) {
/* 193 */     switch (axis.ordinal()) { default: throw new MatchException(null, null);
/* 194 */       case 0: if (this == WEST || this == EAST);
/* 195 */       case 1: if (this == UP || this == DOWN);
/* 196 */       case 2: if (this == NORTH || this == SOUTH); break; }  return getClockWiseZ();
/*     */   }
/*     */ 
/*     */   
/*     */   public Direction getCounterClockWise(Axis axis) {
/* 201 */     switch (axis.ordinal()) { default: throw new MatchException(null, null);
/* 202 */       case 0: if (this == WEST || this == EAST);
/* 203 */       case 1: if (this == UP || this == DOWN);
/* 204 */       case 2: if (this == NORTH || this == SOUTH); break; }  return getCounterClockWiseZ();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Direction getClockWise() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual ordinal : ()I
/*     */     //   4: tableswitch default -> 60, 2 -> 36, 3 -> 48, 4 -> 54, 5 -> 42
/*     */     //   36: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */     //   39: goto -> 77
/*     */     //   42: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */     //   45: goto -> 77
/*     */     //   48: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */     //   51: goto -> 77
/*     */     //   54: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */     //   57: goto -> 77
/*     */     //   60: new java/lang/IllegalStateException
/*     */     //   63: dup
/*     */     //   64: aload_0
/*     */     //   65: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   68: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   73: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   76: athrow
/*     */     //   77: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #209	-> 0
/*     */     //   #210	-> 36
/*     */     //   #211	-> 42
/*     */     //   #212	-> 48
/*     */     //   #213	-> 54
/*     */     //   #214	-> 60
/*     */     //   #209	-> 77
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	78	0	this	Lnet/minecraft/core/Direction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Direction getClockWiseX() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual ordinal : ()I
/*     */     //   4: tableswitch default -> 60, 0 -> 48, 1 -> 36, 2 -> 42, 3 -> 54
/*     */     //   36: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */     //   39: goto -> 77
/*     */     //   42: getstatic net/minecraft/core/Direction.DOWN : Lnet/minecraft/core/Direction;
/*     */     //   45: goto -> 77
/*     */     //   48: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */     //   51: goto -> 77
/*     */     //   54: getstatic net/minecraft/core/Direction.UP : Lnet/minecraft/core/Direction;
/*     */     //   57: goto -> 77
/*     */     //   60: new java/lang/IllegalStateException
/*     */     //   63: dup
/*     */     //   64: aload_0
/*     */     //   65: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   68: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   73: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   76: athrow
/*     */     //   77: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #219	-> 0
/*     */     //   #220	-> 36
/*     */     //   #221	-> 42
/*     */     //   #222	-> 48
/*     */     //   #223	-> 54
/*     */     //   #224	-> 60
/*     */     //   #219	-> 77
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	78	0	this	Lnet/minecraft/core/Direction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Direction getCounterClockWiseX() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual ordinal : ()I
/*     */     //   4: tableswitch default -> 60, 0 -> 48, 1 -> 36, 2 -> 54, 3 -> 42
/*     */     //   36: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */     //   39: goto -> 77
/*     */     //   42: getstatic net/minecraft/core/Direction.DOWN : Lnet/minecraft/core/Direction;
/*     */     //   45: goto -> 77
/*     */     //   48: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */     //   51: goto -> 77
/*     */     //   54: getstatic net/minecraft/core/Direction.UP : Lnet/minecraft/core/Direction;
/*     */     //   57: goto -> 77
/*     */     //   60: new java/lang/IllegalStateException
/*     */     //   63: dup
/*     */     //   64: aload_0
/*     */     //   65: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   68: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   73: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   76: athrow
/*     */     //   77: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #229	-> 0
/*     */     //   #230	-> 36
/*     */     //   #231	-> 42
/*     */     //   #232	-> 48
/*     */     //   #233	-> 54
/*     */     //   #234	-> 60
/*     */     //   #229	-> 77
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	78	0	this	Lnet/minecraft/core/Direction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Direction getClockWiseZ() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual ordinal : ()I
/*     */     //   4: tableswitch default -> 68, 0 -> 56, 1 -> 44, 2 -> 68, 3 -> 68, 4 -> 62, 5 -> 50
/*     */     //   44: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */     //   47: goto -> 85
/*     */     //   50: getstatic net/minecraft/core/Direction.DOWN : Lnet/minecraft/core/Direction;
/*     */     //   53: goto -> 85
/*     */     //   56: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */     //   59: goto -> 85
/*     */     //   62: getstatic net/minecraft/core/Direction.UP : Lnet/minecraft/core/Direction;
/*     */     //   65: goto -> 85
/*     */     //   68: new java/lang/IllegalStateException
/*     */     //   71: dup
/*     */     //   72: aload_0
/*     */     //   73: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   76: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   81: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   84: athrow
/*     */     //   85: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #239	-> 0
/*     */     //   #240	-> 44
/*     */     //   #241	-> 50
/*     */     //   #242	-> 56
/*     */     //   #243	-> 62
/*     */     //   #244	-> 68
/*     */     //   #239	-> 85
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	86	0	this	Lnet/minecraft/core/Direction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Direction getCounterClockWiseZ() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual ordinal : ()I
/*     */     //   4: tableswitch default -> 68, 0 -> 56, 1 -> 44, 2 -> 68, 3 -> 68, 4 -> 50, 5 -> 62
/*     */     //   44: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */     //   47: goto -> 85
/*     */     //   50: getstatic net/minecraft/core/Direction.DOWN : Lnet/minecraft/core/Direction;
/*     */     //   53: goto -> 85
/*     */     //   56: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */     //   59: goto -> 85
/*     */     //   62: getstatic net/minecraft/core/Direction.UP : Lnet/minecraft/core/Direction;
/*     */     //   65: goto -> 85
/*     */     //   68: new java/lang/IllegalStateException
/*     */     //   71: dup
/*     */     //   72: aload_0
/*     */     //   73: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   76: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   81: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   84: athrow
/*     */     //   85: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #249	-> 0
/*     */     //   #250	-> 44
/*     */     //   #251	-> 50
/*     */     //   #252	-> 56
/*     */     //   #253	-> 62
/*     */     //   #254	-> 68
/*     */     //   #249	-> 85
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	86	0	this	Lnet/minecraft/core/Direction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Direction getCounterClockWise() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual ordinal : ()I
/*     */     //   4: tableswitch default -> 60, 2 -> 36, 3 -> 48, 4 -> 54, 5 -> 42
/*     */     //   36: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */     //   39: goto -> 77
/*     */     //   42: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */     //   45: goto -> 77
/*     */     //   48: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */     //   51: goto -> 77
/*     */     //   54: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */     //   57: goto -> 77
/*     */     //   60: new java/lang/IllegalStateException
/*     */     //   63: dup
/*     */     //   64: aload_0
/*     */     //   65: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   68: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   73: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   76: athrow
/*     */     //   77: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #259	-> 0
/*     */     //   #260	-> 36
/*     */     //   #261	-> 42
/*     */     //   #262	-> 48
/*     */     //   #263	-> 54
/*     */     //   #264	-> 60
/*     */     //   #259	-> 77
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	78	0	this	Lnet/minecraft/core/Direction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStepX() {
/* 269 */     return this.normal.getX();
/*     */   }
/*     */   
/*     */   public int getStepY() {
/* 273 */     return this.normal.getY();
/*     */   }
/*     */   
/*     */   public int getStepZ() {
/* 277 */     return this.normal.getZ();
/*     */   }
/*     */   
/*     */   public Vector3f step() {
/* 281 */     return new Vector3f(this.normalVec3f);
/*     */   }
/*     */   
/*     */   public String getName() {
/* 285 */     return this.name;
/*     */   }
/*     */   
/*     */   public Axis getAxis() {
/* 289 */     return this.axis;
/*     */   }
/*     */   
/*     */   public static Direction byName(String name) {
/* 293 */     return (Direction)CODEC.byName(name);
/*     */   }
/*     */   
/*     */   public static Direction from3DDataValue(int data) {
/* 297 */     return BY_3D_DATA[Mth.abs(data % BY_3D_DATA.length)];
/*     */   }
/*     */   
/*     */   public static Direction from2DDataValue(int data) {
/* 301 */     return BY_2D_DATA[Mth.abs(data % BY_2D_DATA.length)];
/*     */   }
/*     */   
/*     */   public static Direction fromYRot(double yRot) {
/* 305 */     return from2DDataValue(Mth.floor(yRot / 90.0D + 0.5D) & 0x3);
/*     */   }
/*     */   
/*     */   public static Direction fromAxisAndDirection(Axis axis, AxisDirection direction) {
/* 309 */     switch (axis.ordinal()) { default: throw new MatchException(null, null);
/* 310 */       case 0: if (direction == AxisDirection.POSITIVE);
/* 311 */       case 1: if (direction == AxisDirection.POSITIVE);
/* 312 */       case 2: if (direction == AxisDirection.POSITIVE); break; }  return NORTH;
/*     */   }
/*     */ 
/*     */   
/*     */   public float toYRot() {
/* 317 */     return ((this.data2d & 0x3) * 90);
/*     */   }
/*     */   
/*     */   public static Direction getRandom(RandomSource random) {
/* 321 */     return (Direction)Util.getRandom((Object[])VALUES, random);
/*     */   }
/*     */   
/*     */   public static Direction getApproximateNearest(double dx, double dy, double dz) {
/* 325 */     return getApproximateNearest((float)dx, (float)dy, (float)dz);
/*     */   }
/*     */   
/*     */   public static Direction getApproximateNearest(float dx, float dy, float dz) {
/* 329 */     Direction result = NORTH;
/* 330 */     float highestDot = Float.MIN_VALUE;
/* 331 */     for (Direction direction : VALUES) {
/* 332 */       float dot = dx * direction.normal.getX() + dy * direction.normal.getY() + dz * direction.normal.getZ();
/*     */       
/* 334 */       if (dot > highestDot) {
/* 335 */         highestDot = dot;
/* 336 */         result = direction;
/*     */       } 
/*     */     } 
/* 339 */     return result;
/*     */   }
/*     */   
/*     */   public static Direction getApproximateNearest(Vec3 vec) {
/* 343 */     return getApproximateNearest(vec.x, vec.y, vec.z);
/*     */   }
/*     */   
/*     */   @Contract("_,_,_,!null->!null;_,_,_,_->_")
/*     */   public static Direction getNearest(int x, int y, int z, Direction orElse) {
/* 348 */     int absX = Math.abs(x);
/* 349 */     int absY = Math.abs(y);
/* 350 */     int absZ = Math.abs(z);
/* 351 */     if (absX > absZ && absX > absY)
/* 352 */       return (x < 0) ? WEST : EAST; 
/* 353 */     if (absZ > absX && absZ > absY)
/* 354 */       return (z < 0) ? NORTH : SOUTH; 
/* 355 */     if (absY > absX && absY > absZ) {
/* 356 */       return (y < 0) ? DOWN : UP;
/*     */     }
/* 358 */     return orElse;
/*     */   }
/*     */   
/*     */   @Contract("_,!null->!null;_,_->_")
/*     */   public static Direction getNearest(Vec3i vec, Direction orElse) {
/* 363 */     return getNearest(vec.getX(), vec.getY(), vec.getZ(), orElse);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 368 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSerializedName() {
/* 373 */     return this.name;
/*     */   }
/*     */   
/*     */   private static DataResult<Direction> verifyVertical(Direction v) {
/* 377 */     return v.getAxis().isVertical() ? DataResult.success(v) : DataResult.error(() -> "Expected a vertical direction");
/*     */   }
/*     */   
/*     */   public static Direction get(AxisDirection axisDirection, Axis axis) {
/* 381 */     for (Direction direction : VALUES) {
/* 382 */       if (direction.getAxisDirection() == axisDirection && direction.getAxis() == axis) {
/* 383 */         return direction;
/*     */       }
/*     */     } 
/* 386 */     throw new IllegalArgumentException("No such direction: " + String.valueOf(axisDirection) + " " + String.valueOf(axis));
/*     */   }
/*     */   
/*     */   public static ImmutableList<Axis> axisStepOrder(Vec3 movement) {
/* 390 */     if (Math.abs(movement.x) < Math.abs(movement.z)) {
/* 391 */       return YZX_AXIS_ORDER;
/*     */     }
/* 393 */     return YXZ_AXIS_ORDER;
/*     */   }
/*     */   
/*     */   public enum Axis implements Predicate<Direction>, StringRepresentable {
/* 397 */     X("x")
/*     */     {
/*     */       public int choose(int x, int y, int z) {
/* 400 */         return x;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean choose(boolean x, boolean y, boolean z) {
/* 405 */         return x;
/*     */       }
/*     */ 
/*     */       
/*     */       public double choose(double x, double y, double z) {
/* 410 */         return x;
/*     */       }
/*     */ 
/*     */       
/*     */       public Direction getPositive() {
/* 415 */         return Direction.EAST;
/*     */       }
/*     */ 
/*     */       
/*     */       public Direction getNegative() {
/* 420 */         return Direction.WEST;
/*     */       }
/*     */     },
/* 423 */     Y("y")
/*     */     {
/*     */       public int choose(int x, int y, int z) {
/* 426 */         return y;
/*     */       }
/*     */ 
/*     */       
/*     */       public double choose(double x, double y, double z) {
/* 431 */         return y;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean choose(boolean x, boolean y, boolean z) {
/* 436 */         return y;
/*     */       }
/*     */ 
/*     */       
/*     */       public Direction getPositive() {
/* 441 */         return Direction.UP;
/*     */       }
/*     */ 
/*     */       
/*     */       public Direction getNegative() {
/* 446 */         return Direction.DOWN;
/*     */       }
/*     */     },
/* 449 */     Z("z")
/*     */     {
/*     */       public int choose(int x, int y, int z) {
/* 452 */         return z;
/*     */       }
/*     */ 
/*     */       
/*     */       public double choose(double x, double y, double z) {
/* 457 */         return z;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean choose(boolean x, boolean y, boolean z) {
/* 462 */         return z;
/*     */       }
/*     */ 
/*     */       
/*     */       public Direction getPositive() {
/* 467 */         return Direction.SOUTH;
/*     */       }
/*     */ 
/*     */       
/*     */       public Direction getNegative() {
/* 472 */         return Direction.NORTH;
/*     */       }
/*     */     };
/*     */ 
/*     */     
/* 477 */     public static final Axis[] VALUES = values();
/*     */     
/* 479 */     public static final StringRepresentable.EnumCodec<Axis> CODEC = StringRepresentable.fromEnum(Axis::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     Axis(String name) {
/* 484 */       this.name = name;
/*     */     }
/*     */     
/*     */     public static Axis byName(String name) {
/* 488 */       return (Axis)CODEC.byName(name);
/*     */     }
/*     */     
/*     */     public String getName() {
/* 492 */       return this.name;
/*     */     }
/*     */     
/*     */     public boolean isVertical() {
/* 496 */       return (this == Y);
/*     */     }
/*     */     
/*     */     public boolean isHorizontal() {
/* 500 */       return (this == X || this == Z);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Direction[] getDirections() {
/* 508 */       return new Direction[] { getPositive(), getNegative() };
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 513 */       return this.name;
/*     */     }
/*     */     
/*     */     public static Axis getRandom(RandomSource random) {
/* 517 */       return (Axis)Util.getRandom((Object[])VALUES, random);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(Direction input) {
/* 522 */       return (input != null && input.getAxis() == this);
/*     */     }
/*     */     
/*     */     public Direction.Plane getPlane() {
/* 526 */       switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 2: case 1: break; }  return 
/*     */         
/* 528 */         Direction.Plane.VERTICAL;
/*     */     }
/*     */     public abstract Direction getPositive();
/*     */     public abstract Direction getNegative();
/*     */     public abstract int choose(int param1Int1, int param1Int2, int param1Int3);
/*     */     public abstract double choose(double param1Double1, double param1Double2, double param1Double3);
/* 534 */     public String getSerializedName() { return this.name; }
/*     */     public abstract boolean choose(boolean param1Boolean1, boolean param1Boolean2, boolean param1Boolean3); }
/*     */   enum null {
/*     */     public int choose(int x, int y, int z) { return x; }
/*     */     public boolean choose(boolean x, boolean y, boolean z) { return x; }
/*     */     public double choose(double x, double y, double z) { return x; }
/*     */     public Direction getPositive() { return Direction.EAST; } public Direction getNegative() { return Direction.WEST; }
/*     */   } enum null {
/*     */     public int choose(int x, int y, int z) { return y; } public double choose(double x, double y, double z) { return y; } public boolean choose(boolean x, boolean y, boolean z) { return y; } public Direction getPositive() { return Direction.UP; } public Direction getNegative() { return Direction.DOWN; }
/*     */   } enum null {
/*     */     public int choose(int x, int y, int z) { return z; } public double choose(double x, double y, double z) { return z; } public boolean choose(boolean x, boolean y, boolean z) { return z; } public Direction getPositive() { return Direction.SOUTH; } public Direction getNegative() { return Direction.NORTH; }
/* 545 */   } public enum AxisDirection { POSITIVE(1, "Towards positive"),
/* 546 */     NEGATIVE(-1, "Towards negative");
/*     */     
/*     */     private final int step;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     AxisDirection(int step, String name) {
/* 553 */       this.step = step;
/* 554 */       this.name = name;
/*     */     }
/*     */     
/*     */     public int getStep() {
/* 558 */       return this.step;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 562 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 567 */       return this.name;
/*     */     }
/*     */     
/*     */     public AxisDirection opposite() {
/* 571 */       return (this == POSITIVE) ? NEGATIVE : POSITIVE;
/*     */     } }
/*     */ 
/*     */   
/*     */   public Vec3i getUnitVec3i() {
/* 576 */     return this.normal;
/*     */   }
/*     */   
/*     */   public Vec3 getUnitVec3() {
/* 580 */     return this.normalVec3;
/*     */   }
/*     */   
/*     */   public Vector3fc getUnitVec3f() {
/* 584 */     return this.normalVec3f;
/*     */   }
/*     */   
/*     */   public boolean isFacingAngle(float yAngle) {
/* 588 */     float radians = yAngle * 0.017453292F;
/* 589 */     float dx = -Mth.sin(radians);
/* 590 */     float dz = Mth.cos(radians);
/* 591 */     return (this.normal.getX() * dx + this.normal.getZ() * dz > 0.0F);
/*     */   }
/*     */   
/*     */   public enum Plane implements Predicate<Direction>, Iterable<Direction> {
/* 595 */     HORIZONTAL(new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST }, new Direction.Axis[] { Direction.Axis.X, Direction.Axis.Z }),
/* 596 */     VERTICAL(new Direction[] { Direction.UP, Direction.DOWN }, new Direction.Axis[] { Direction.Axis.Y });
/*     */     
/*     */     private final Direction[] faces;
/*     */     
/*     */     private final Direction.Axis[] axis;
/*     */     
/*     */     Plane(Direction[] faces, Direction.Axis[] axis) {
/* 603 */       this.faces = faces;
/* 604 */       this.axis = axis;
/*     */     }
/*     */     
/*     */     public Direction getRandomDirection(RandomSource random) {
/* 608 */       return (Direction)Util.getRandom((Object[])this.faces, random);
/*     */     }
/*     */     
/*     */     public Direction.Axis getRandomAxis(RandomSource random) {
/* 612 */       return (Direction.Axis)Util.getRandom((Object[])this.axis, random);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(Direction input) {
/* 617 */       return (input != null && input.getAxis().getPlane() == this);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Direction> iterator() {
/* 622 */       return (Iterator<Direction>)Iterators.forArray((Object[])this.faces);
/*     */     }
/*     */     
/*     */     public Stream<Direction> stream() {
/* 626 */       return Arrays.stream(this.faces);
/*     */     }
/*     */     
/*     */     public List<Direction> shuffledCopy(RandomSource random) {
/* 630 */       return Util.shuffledCopy((Object[])this.faces, random);
/*     */     }
/*     */     
/*     */     public int length() {
/* 634 */       return this.faces.length;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/Direction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */