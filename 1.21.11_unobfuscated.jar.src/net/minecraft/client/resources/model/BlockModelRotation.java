/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import com.mojang.math.OctahedralGroup;
/*    */ import com.mojang.math.Transformation;
/*    */ import java.util.EnumMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockMath;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Util;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public class BlockModelRotation implements ModelState {
/* 15 */   private static final Map<OctahedralGroup, BlockModelRotation> BY_GROUP_ORDINAL = Util.makeEnumMap(OctahedralGroup.class, BlockModelRotation::new);
/*    */   
/* 17 */   public static final BlockModelRotation IDENTITY = get(OctahedralGroup.IDENTITY);
/*    */   
/*    */   private final OctahedralGroup orientation;
/*    */   private final Transformation transformation;
/* 21 */   private final Map<Direction, Matrix4fc> faceMapping = new EnumMap<>(Direction.class);
/* 22 */   private final Map<Direction, Matrix4fc> inverseFaceMapping = new EnumMap<>(Direction.class);
/* 23 */   private final WithUvLock withUvLock = new WithUvLock(this);
/*    */   
/*    */   private BlockModelRotation(OctahedralGroup orientation) {
/* 26 */     this.orientation = orientation;
/* 27 */     if (orientation != OctahedralGroup.IDENTITY) {
/* 28 */       this.transformation = new Transformation((Matrix4fc)new Matrix4f(orientation.transformation()));
/*    */     } else {
/* 30 */       this.transformation = Transformation.identity();
/*    */     } 
/*    */     
/* 33 */     for (Direction face : Direction.values()) {
/*    */       
/* 35 */       Matrix4fc faceTransform = BlockMath.getFaceTransformation(this.transformation, face).getMatrix();
/* 36 */       this.faceMapping.put(face, faceTransform);
/* 37 */       this.inverseFaceMapping.put(face, faceTransform.invertAffine(new Matrix4f()));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Transformation transformation() {
/* 43 */     return this.transformation;
/*    */   }
/*    */   
/*    */   public static BlockModelRotation get(OctahedralGroup group) {
/* 47 */     return BY_GROUP_ORDINAL.get(group);
/*    */   }
/*    */   
/*    */   public ModelState withUvLock() {
/* 51 */     return this.withUvLock;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 56 */     return "simple[" + this.orientation.getSerializedName() + "]";
/*    */   }
/*    */   private static final class WithUvLock extends Record implements ModelState { private final BlockModelRotation parent;
/* 59 */     private WithUvLock(BlockModelRotation parent) { this.parent = parent; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/BlockModelRotation$WithUvLock;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #59	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 59 */       //   0	7	0	this	Lnet/minecraft/client/resources/model/BlockModelRotation$WithUvLock; } public BlockModelRotation parent() { return this.parent; } public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/BlockModelRotation$WithUvLock;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #59	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/resources/model/BlockModelRotation$WithUvLock;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     } public Transformation transformation() {
/* 62 */       return this.parent.transformation;
/*    */     }
/*    */ 
/*    */     
/*    */     public Matrix4fc faceTransformation(Direction face) {
/* 67 */       return this.parent.faceMapping.getOrDefault(face, NO_TRANSFORM);
/*    */     }
/*    */ 
/*    */     
/*    */     public Matrix4fc inverseFaceTransformation(Direction face) {
/* 72 */       return this.parent.inverseFaceMapping.getOrDefault(face, NO_TRANSFORM);
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 77 */       return "uvLocked[" + this.parent.orientation.getSerializedName() + "]";
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/BlockModelRotation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */