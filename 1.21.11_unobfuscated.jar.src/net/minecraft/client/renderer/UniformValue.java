/*     */ package net.minecraft.client.renderer;
/*     */ import com.mojang.blaze3d.buffers.Std140Builder;
/*     */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*     */ import com.mojang.serialization.Codec;
/*     */ 
/*     */ public interface UniformValue {
/*     */   public static final Codec<UniformValue> CODEC;
/*     */   
/*     */   void writeTo(Std140Builder paramStd140Builder);
/*     */   
/*     */   void addSize(Std140SizeCalculator paramStd140SizeCalculator);
/*     */   
/*     */   Type type();
/*     */   
/*     */   static {
/*  16 */     CODEC = Type.CODEC.dispatch(UniformValue::type, t -> t.valueCodec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Type
/*     */     implements net.minecraft.util.StringRepresentable
/*     */   {
/*  25 */     INT("int", UniformValue.IntUniform.CODEC),
/*  26 */     IVEC3("ivec3", UniformValue.IVec3Uniform.CODEC),
/*  27 */     FLOAT("float", UniformValue.FloatUniform.CODEC),
/*  28 */     VEC2("vec2", UniformValue.Vec2Uniform.CODEC),
/*  29 */     VEC3("vec3", UniformValue.Vec3Uniform.CODEC),
/*  30 */     VEC4("vec4", UniformValue.Vec4Uniform.CODEC),
/*  31 */     MATRIX4X4("matrix4x4", UniformValue.Matrix4x4Uniform.CODEC);
/*     */ 
/*     */     
/*  34 */     public static final net.minecraft.util.StringRepresentable.EnumCodec<Type> CODEC = net.minecraft.util.StringRepresentable.fromEnum(Type::values);
/*     */     
/*     */     private final String name;
/*     */     private final com.mojang.serialization.MapCodec<? extends UniformValue> valueCodec;
/*     */     
/*     */     Type(String name, Codec<? extends UniformValue> valueCodec) {
/*  40 */       this.name = name;
/*  41 */       this.valueCodec = valueCodec.fieldOf("value");
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/*  46 */       return this.name;
/*     */     } }
/*     */   public static final class IntUniform extends Record implements UniformValue { private final int value;
/*     */     
/*  50 */     public IntUniform(int value) { this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/UniformValue$IntUniform;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #50	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  50 */       //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$IntUniform; } public int value() { return this.value; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/UniformValue$IntUniform;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #50	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$IntUniform; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/UniformValue$IntUniform;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #50	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/UniformValue$IntUniform;
/*  51 */       //   0	8	1	o	Ljava/lang/Object; } public static final Codec<IntUniform> CODEC = Codec.INT.xmap(IntUniform::new, IntUniform::value);
/*     */ 
/*     */     
/*     */     public void writeTo(Std140Builder builder) {
/*  55 */       builder.putInt(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addSize(Std140SizeCalculator calculator) {
/*  60 */       calculator.putInt();
/*     */     }
/*     */ 
/*     */     
/*     */     public UniformValue.Type type() {
/*  65 */       return UniformValue.Type.INT;
/*     */     } }
/*     */   public static final class IVec3Uniform extends Record implements UniformValue { private final org.joml.Vector3ic value;
/*     */     
/*  69 */     public IVec3Uniform(org.joml.Vector3ic value) { this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/UniformValue$IVec3Uniform;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #69	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$IVec3Uniform; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/UniformValue$IVec3Uniform;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #69	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$IVec3Uniform; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/UniformValue$IVec3Uniform;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #69	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/UniformValue$IVec3Uniform;
/*  69 */       //   0	8	1	o	Ljava/lang/Object; } public org.joml.Vector3ic value() { return this.value; }
/*  70 */      public static final Codec<IVec3Uniform> CODEC = net.minecraft.util.ExtraCodecs.VECTOR3I.xmap(IVec3Uniform::new, IVec3Uniform::value);
/*     */ 
/*     */     
/*     */     public void writeTo(Std140Builder builder) {
/*  74 */       builder.putIVec3(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addSize(Std140SizeCalculator calculator) {
/*  79 */       calculator.putIVec3();
/*     */     }
/*     */ 
/*     */     
/*     */     public UniformValue.Type type() {
/*  84 */       return UniformValue.Type.IVEC3;
/*     */     } }
/*     */   public static final class FloatUniform extends Record implements UniformValue { private final float value;
/*     */     
/*  88 */     public FloatUniform(float value) { this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/UniformValue$FloatUniform;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$FloatUniform; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/UniformValue$FloatUniform;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$FloatUniform; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/UniformValue$FloatUniform;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/UniformValue$FloatUniform;
/*  88 */       //   0	8	1	o	Ljava/lang/Object; } public float value() { return this.value; }
/*  89 */      public static final Codec<FloatUniform> CODEC = Codec.FLOAT.xmap(FloatUniform::new, FloatUniform::value);
/*     */ 
/*     */     
/*     */     public void writeTo(Std140Builder builder) {
/*  93 */       builder.putFloat(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addSize(Std140SizeCalculator calculator) {
/*  98 */       calculator.putFloat();
/*     */     }
/*     */ 
/*     */     
/*     */     public UniformValue.Type type() {
/* 103 */       return UniformValue.Type.FLOAT;
/*     */     } }
/*     */   public static final class Vec2Uniform extends Record implements UniformValue { private final org.joml.Vector2fc value;
/*     */     
/* 107 */     public Vec2Uniform(org.joml.Vector2fc value) { this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/UniformValue$Vec2Uniform;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #107	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$Vec2Uniform; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/UniformValue$Vec2Uniform;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #107	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$Vec2Uniform; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/UniformValue$Vec2Uniform;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #107	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/UniformValue$Vec2Uniform;
/* 107 */       //   0	8	1	o	Ljava/lang/Object; } public org.joml.Vector2fc value() { return this.value; }
/* 108 */      public static final Codec<Vec2Uniform> CODEC = net.minecraft.util.ExtraCodecs.VECTOR2F.xmap(Vec2Uniform::new, Vec2Uniform::value);
/*     */ 
/*     */     
/*     */     public void writeTo(Std140Builder builder) {
/* 112 */       builder.putVec2(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addSize(Std140SizeCalculator calculator) {
/* 117 */       calculator.putVec2();
/*     */     }
/*     */ 
/*     */     
/*     */     public UniformValue.Type type() {
/* 122 */       return UniformValue.Type.VEC2;
/*     */     } }
/*     */   public static final class Vec3Uniform extends Record implements UniformValue { private final org.joml.Vector3fc value;
/*     */     
/* 126 */     public Vec3Uniform(org.joml.Vector3fc value) { this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/UniformValue$Vec3Uniform;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #126	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$Vec3Uniform; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/UniformValue$Vec3Uniform;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #126	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$Vec3Uniform; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/UniformValue$Vec3Uniform;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #126	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/UniformValue$Vec3Uniform;
/* 126 */       //   0	8	1	o	Ljava/lang/Object; } public org.joml.Vector3fc value() { return this.value; }
/* 127 */      public static final Codec<Vec3Uniform> CODEC = net.minecraft.util.ExtraCodecs.VECTOR3F.xmap(Vec3Uniform::new, Vec3Uniform::value);
/*     */ 
/*     */     
/*     */     public void writeTo(Std140Builder builder) {
/* 131 */       builder.putVec3(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addSize(Std140SizeCalculator calculator) {
/* 136 */       calculator.putVec3();
/*     */     }
/*     */ 
/*     */     
/*     */     public UniformValue.Type type() {
/* 141 */       return UniformValue.Type.VEC3;
/*     */     } }
/*     */   public static final class Vec4Uniform extends Record implements UniformValue { private final org.joml.Vector4fc value;
/*     */     
/* 145 */     public Vec4Uniform(org.joml.Vector4fc value) { this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/UniformValue$Vec4Uniform;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #145	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$Vec4Uniform; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/UniformValue$Vec4Uniform;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #145	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$Vec4Uniform; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/UniformValue$Vec4Uniform;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #145	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/UniformValue$Vec4Uniform;
/* 145 */       //   0	8	1	o	Ljava/lang/Object; } public org.joml.Vector4fc value() { return this.value; }
/* 146 */      public static final Codec<Vec4Uniform> CODEC = net.minecraft.util.ExtraCodecs.VECTOR4F.xmap(Vec4Uniform::new, Vec4Uniform::value);
/*     */ 
/*     */     
/*     */     public void writeTo(Std140Builder builder) {
/* 150 */       builder.putVec4(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addSize(Std140SizeCalculator calculator) {
/* 155 */       calculator.putVec4();
/*     */     }
/*     */ 
/*     */     
/*     */     public UniformValue.Type type() {
/* 160 */       return UniformValue.Type.VEC4;
/*     */     } }
/*     */   public static final class Matrix4x4Uniform extends Record implements UniformValue { private final org.joml.Matrix4fc value;
/*     */     
/* 164 */     public Matrix4x4Uniform(org.joml.Matrix4fc value) { this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/UniformValue$Matrix4x4Uniform;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #164	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$Matrix4x4Uniform; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/UniformValue$Matrix4x4Uniform;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #164	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$Matrix4x4Uniform; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/UniformValue$Matrix4x4Uniform;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #164	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/UniformValue$Matrix4x4Uniform;
/* 164 */       //   0	8	1	o	Ljava/lang/Object; } public org.joml.Matrix4fc value() { return this.value; }
/* 165 */      public static final Codec<Matrix4x4Uniform> CODEC = net.minecraft.util.ExtraCodecs.MATRIX4F.xmap(Matrix4x4Uniform::new, Matrix4x4Uniform::value);
/*     */ 
/*     */     
/*     */     public void writeTo(Std140Builder builder) {
/* 169 */       builder.putMat4f(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addSize(Std140SizeCalculator calculator) {
/* 174 */       calculator.putMat4f();
/*     */     }
/*     */ 
/*     */     
/*     */     public UniformValue.Type type() {
/* 179 */       return UniformValue.Type.MATRIX4X4;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/UniformValue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */