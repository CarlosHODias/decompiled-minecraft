/*     */ package net.minecraft.client.model.geom;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import org.joml.Matrix3f;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ 
/*     */ public final class ModelPart
/*     */ {
/*     */   public static final float DEFAULT_SCALE = 1.0F;
/*     */   public float x;
/*     */   public float y;
/*     */   public float z;
/*     */   public float xRot;
/*     */   public float yRot;
/*     */   public float zRot;
/*  34 */   public float xScale = 1.0F;
/*  35 */   public float yScale = 1.0F;
/*  36 */   public float zScale = 1.0F;
/*     */   
/*     */   public boolean visible = true;
/*     */   
/*     */   public boolean skipDraw;
/*     */   private final List<Cube> cubes;
/*     */   private final Map<String, ModelPart> children;
/*  43 */   private PartPose initialPose = PartPose.ZERO;
/*     */   
/*     */   public ModelPart(List<Cube> cubes, Map<String, ModelPart> children) {
/*  46 */     this.cubes = cubes;
/*  47 */     this.children = children;
/*     */   }
/*     */   
/*     */   public PartPose storePose() {
/*  51 */     return PartPose.offsetAndRotation(this.x, this.y, this.z, this.xRot, this.yRot, this.zRot);
/*     */   }
/*     */   
/*     */   public PartPose getInitialPose() {
/*  55 */     return this.initialPose;
/*     */   }
/*     */   
/*     */   public void setInitialPose(PartPose initialPose) {
/*  59 */     this.initialPose = initialPose;
/*     */   }
/*     */   
/*     */   public void resetPose() {
/*  63 */     loadPose(this.initialPose);
/*     */   }
/*     */   
/*     */   public void loadPose(PartPose pose) {
/*  67 */     this.x = pose.x();
/*  68 */     this.y = pose.y();
/*  69 */     this.z = pose.z();
/*  70 */     this.xRot = pose.xRot();
/*  71 */     this.yRot = pose.yRot();
/*  72 */     this.zRot = pose.zRot();
/*  73 */     this.xScale = pose.xScale();
/*  74 */     this.yScale = pose.yScale();
/*  75 */     this.zScale = pose.zScale();
/*     */   }
/*     */   
/*     */   public boolean hasChild(String name) {
/*  79 */     return this.children.containsKey(name);
/*     */   }
/*     */   
/*     */   public ModelPart getChild(String name) {
/*  83 */     ModelPart result = this.children.get(name);
/*  84 */     if (result == null) {
/*  85 */       throw new NoSuchElementException("Can't find part " + name);
/*     */     }
/*  87 */     return result;
/*     */   }
/*     */   
/*     */   public void setPos(float x, float y, float z) {
/*  91 */     this.x = x;
/*  92 */     this.y = y;
/*  93 */     this.z = z;
/*     */   }
/*     */   
/*     */   public void setRotation(float xRot, float yRot, float zRot) {
/*  97 */     this.xRot = xRot;
/*  98 */     this.yRot = yRot;
/*  99 */     this.zRot = zRot;
/*     */   }
/*     */   
/*     */   public void render(PoseStack poseStack, VertexConsumer buffer, int lightCoords, int overlayCoords) {
/* 103 */     render(poseStack, buffer, lightCoords, overlayCoords, -1);
/*     */   }
/*     */   
/*     */   public void render(PoseStack poseStack, VertexConsumer buffer, int lightCoords, int overlayCoords, int color) {
/* 107 */     if (!this.visible) {
/*     */       return;
/*     */     }
/* 110 */     if (this.cubes.isEmpty() && this.children.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 114 */     poseStack.pushPose();
/* 115 */     translateAndRotate(poseStack);
/*     */     
/* 117 */     if (!this.skipDraw) {
/* 118 */       compile(poseStack.last(), buffer, lightCoords, overlayCoords, color);
/*     */     }
/*     */     
/* 121 */     for (ModelPart child : this.children.values()) {
/* 122 */       child.render(poseStack, buffer, lightCoords, overlayCoords, color);
/*     */     }
/*     */     
/* 125 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public void rotateBy(Quaternionf rotation) {
/* 129 */     Matrix3f oldRotation = new Matrix3f().rotationZYX(this.zRot, this.yRot, this.xRot);
/* 130 */     Matrix3f newRotation = oldRotation.rotate((Quaternionfc)rotation);
/* 131 */     Vector3f newAngles = newRotation.getEulerAnglesZYX(new Vector3f());
/* 132 */     setRotation(newAngles.x, newAngles.y, newAngles.z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void getExtentsForGui(PoseStack poseStack, Consumer<Vector3fc> output) {
/* 141 */     visit(poseStack, (pose, partPath, cubeIndex, cube) -> {
/*     */           for (Polygon polygon : cube.polygons) {
/*     */             for (Vertex vertex : polygon.vertices()) {
/*     */               float x = vertex.worldX(), y = vertex.worldY(), z = vertex.worldZ();
/*     */               Vector3f pos = pose.pose().transformPosition(x, y, z, new Vector3f());
/*     */               output.accept(pos);
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(PoseStack poseStack, Visitor visitor) {
/* 155 */     visit(poseStack, visitor, "");
/*     */   }
/*     */   
/*     */   private void visit(PoseStack poseStack, Visitor visitor, String path) {
/* 159 */     if (this.cubes.isEmpty() && this.children.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 163 */     poseStack.pushPose();
/* 164 */     translateAndRotate(poseStack);
/*     */     
/* 166 */     PoseStack.Pose pose = poseStack.last();
/* 167 */     for (int i = 0; i < this.cubes.size(); i++) {
/* 168 */       visitor.visit(pose, path, i, this.cubes.get(i));
/*     */     }
/*     */     
/* 171 */     String childPath = path + "/";
/* 172 */     this.children.forEach((name, child) -> child.visit(poseStack, visitor, childPath + childPath));
/*     */ 
/*     */ 
/*     */     
/* 176 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public void translateAndRotate(PoseStack poseStack) {
/* 180 */     poseStack.translate(this.x / 16.0F, this.y / 16.0F, this.z / 16.0F);
/* 181 */     if (this.xRot != 0.0F || this.yRot != 0.0F || this.zRot != 0.0F) {
/* 182 */       poseStack.mulPose((Quaternionfc)new Quaternionf().rotationZYX(this.zRot, this.yRot, this.xRot));
/*     */     }
/* 184 */     if (this.xScale != 1.0F || this.yScale != 1.0F || this.zScale != 1.0F) {
/* 185 */       poseStack.scale(this.xScale, this.yScale, this.zScale);
/*     */     }
/*     */   }
/*     */   
/*     */   private void compile(PoseStack.Pose pose, VertexConsumer builder, int lightCoords, int overlayCoords, int color) {
/* 190 */     for (Cube cube : this.cubes) {
/* 191 */       cube.compile(pose, builder, lightCoords, overlayCoords, color);
/*     */     }
/*     */   }
/*     */   
/*     */   public Cube getRandomCube(RandomSource random) {
/* 196 */     return this.cubes.get(random.nextInt(this.cubes.size()));
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 200 */     return this.cubes.isEmpty();
/*     */   }
/*     */   
/*     */   public void offsetPos(Vector3f offset) {
/* 204 */     this.x += offset.x();
/* 205 */     this.y += offset.y();
/* 206 */     this.z += offset.z();
/*     */   }
/*     */   
/*     */   public void offsetRotation(Vector3f offset) {
/* 210 */     this.xRot += offset.x();
/* 211 */     this.yRot += offset.y();
/* 212 */     this.zRot += offset.z();
/*     */   }
/*     */   
/*     */   public void offsetScale(Vector3f offset) {
/* 216 */     this.xScale += offset.x();
/* 217 */     this.yScale += offset.y();
/* 218 */     this.zScale += offset.z();
/*     */   }
/*     */   
/*     */   public List<ModelPart> getAllParts() {
/* 222 */     List<ModelPart> allParts = new ArrayList<>();
/* 223 */     allParts.add(this);
/* 224 */     addAllChildren((name, part) -> allParts.add(part));
/* 225 */     return List.copyOf(allParts);
/*     */   }
/*     */   
/*     */   public Function<String, ModelPart> createPartLookup() {
/* 229 */     Map<String, ModelPart> parts = new HashMap<>();
/* 230 */     parts.put("root", this);
/* 231 */     Objects.requireNonNull(parts); addAllChildren(parts::putIfAbsent);
/* 232 */     Objects.requireNonNull(parts); return parts::get;
/*     */   }
/*     */   
/*     */   private void addAllChildren(BiConsumer<String, ModelPart> output) {
/* 236 */     for (Map.Entry<String, ModelPart> entry : this.children.entrySet()) {
/* 237 */       output.accept(entry.getKey(), entry.getValue());
/*     */     }
/* 239 */     for (ModelPart part : this.children.values())
/* 240 */       part.addAllChildren(output); 
/*     */   }
/*     */   @FunctionalInterface
/*     */   public static interface Visitor {
/*     */     void visit(PoseStack.Pose param1Pose, String param1String, int param1Int, ModelPart.Cube param1Cube); }
/*     */   public static class Cube { public final ModelPart.Polygon[] polygons;
/*     */     public final float minX;
/*     */     public final float minY;
/*     */     public final float minZ;
/*     */     public final float maxX;
/*     */     public final float maxY;
/*     */     public final float maxZ;
/*     */     
/*     */     public Cube(int xTexOffs, int yTexOffs, float minX, float minY, float minZ, float width, float height, float depth, float growX, float growY, float growZ, boolean mirror, float xTexSize, float yTexSize, Set<Direction> visibleFaces) {
/* 254 */       this.minX = minX;
/* 255 */       this.minY = minY;
/* 256 */       this.minZ = minZ;
/* 257 */       this.maxX = minX + width;
/* 258 */       this.maxY = minY + height;
/* 259 */       this.maxZ = minZ + depth;
/* 260 */       this.polygons = new ModelPart.Polygon[visibleFaces.size()];
/*     */       
/* 262 */       float maxX = minX + width;
/* 263 */       float maxY = minY + height;
/* 264 */       float maxZ = minZ + depth;
/*     */       
/* 266 */       minX -= growX;
/* 267 */       minY -= growY;
/* 268 */       minZ -= growZ;
/* 269 */       maxX += growX;
/* 270 */       maxY += growY;
/* 271 */       maxZ += growZ;
/*     */       
/* 273 */       if (mirror) {
/* 274 */         float tmp = maxX;
/* 275 */         maxX = minX;
/* 276 */         minX = tmp;
/*     */       } 
/*     */       
/* 279 */       ModelPart.Vertex t0 = new ModelPart.Vertex(minX, minY, minZ, 0.0F, 0.0F);
/* 280 */       ModelPart.Vertex t1 = new ModelPart.Vertex(maxX, minY, minZ, 0.0F, 8.0F);
/* 281 */       ModelPart.Vertex t2 = new ModelPart.Vertex(maxX, maxY, minZ, 8.0F, 8.0F);
/* 282 */       ModelPart.Vertex t3 = new ModelPart.Vertex(minX, maxY, minZ, 8.0F, 0.0F);
/*     */       
/* 284 */       ModelPart.Vertex l0 = new ModelPart.Vertex(minX, minY, maxZ, 0.0F, 0.0F);
/* 285 */       ModelPart.Vertex l1 = new ModelPart.Vertex(maxX, minY, maxZ, 0.0F, 8.0F);
/* 286 */       ModelPart.Vertex l2 = new ModelPart.Vertex(maxX, maxY, maxZ, 8.0F, 8.0F);
/* 287 */       ModelPart.Vertex l3 = new ModelPart.Vertex(minX, maxY, maxZ, 8.0F, 0.0F);
/*     */       
/* 289 */       float u0 = xTexOffs;
/* 290 */       float u1 = xTexOffs + depth;
/* 291 */       float u2 = xTexOffs + depth + width;
/* 292 */       float u22 = xTexOffs + depth + width + width;
/* 293 */       float u3 = xTexOffs + depth + width + depth;
/* 294 */       float u4 = xTexOffs + depth + width + depth + width;
/*     */       
/* 296 */       float v0 = yTexOffs;
/* 297 */       float v1 = yTexOffs + depth;
/* 298 */       float v2 = yTexOffs + depth + height;
/*     */       
/* 300 */       int pos = 0;
/* 301 */       if (visibleFaces.contains(Direction.DOWN)) {
/* 302 */         this.polygons[pos++] = new ModelPart.Polygon(new ModelPart.Vertex[] { l1, l0, t0, t1 }, u1, v0, u2, v1, xTexSize, yTexSize, mirror, Direction.DOWN);
/*     */       }
/* 304 */       if (visibleFaces.contains(Direction.UP)) {
/* 305 */         this.polygons[pos++] = new ModelPart.Polygon(new ModelPart.Vertex[] { t2, t3, l3, l2 }, u2, v1, u22, v0, xTexSize, yTexSize, mirror, Direction.UP);
/*     */       }
/*     */       
/* 308 */       if (visibleFaces.contains(Direction.WEST)) {
/* 309 */         this.polygons[pos++] = new ModelPart.Polygon(new ModelPart.Vertex[] { t0, l0, l3, t3 }, u0, v1, u1, v2, xTexSize, yTexSize, mirror, Direction.WEST);
/*     */       }
/* 311 */       if (visibleFaces.contains(Direction.NORTH)) {
/* 312 */         this.polygons[pos++] = new ModelPart.Polygon(new ModelPart.Vertex[] { t1, t0, t3, t2 }, u1, v1, u2, v2, xTexSize, yTexSize, mirror, Direction.NORTH);
/*     */       }
/* 314 */       if (visibleFaces.contains(Direction.EAST)) {
/* 315 */         this.polygons[pos++] = new ModelPart.Polygon(new ModelPart.Vertex[] { l1, t1, t2, l2 }, u2, v1, u3, v2, xTexSize, yTexSize, mirror, Direction.EAST);
/*     */       }
/* 317 */       if (visibleFaces.contains(Direction.SOUTH)) {
/* 318 */         this.polygons[pos] = new ModelPart.Polygon(new ModelPart.Vertex[] { l0, l1, l2, l3 }, u3, v1, u4, v2, xTexSize, yTexSize, mirror, Direction.SOUTH);
/*     */       }
/*     */     }
/*     */     
/*     */     public void compile(PoseStack.Pose pose, VertexConsumer builder, int lightCoords, int overlayCoords, int color) {
/* 323 */       Matrix4f matrix = pose.pose();
/* 324 */       Vector3f scratchVector = new Vector3f();
/* 325 */       for (ModelPart.Polygon polygon : this.polygons) {
/* 326 */         Vector3f normal = pose.transformNormal(polygon.normal, scratchVector);
/* 327 */         float nx = normal.x();
/* 328 */         float ny = normal.y();
/* 329 */         float nz = normal.z();
/*     */         
/* 331 */         for (ModelPart.Vertex vertex : polygon.vertices) {
/* 332 */           float x = vertex.worldX();
/* 333 */           float y = vertex.worldY();
/* 334 */           float z = vertex.worldZ();
/* 335 */           Vector3f pos = matrix.transformPosition(x, y, z, scratchVector);
/* 336 */           builder.addVertex(pos.x(), pos.y(), pos.z(), color, vertex.u, vertex.v, overlayCoords, lightCoords, nx, ny, nz);
/*     */         } 
/*     */       } 
/*     */     } }
/*     */   public static final class Polygon extends Record { private final ModelPart.Vertex[] vertices; private final Vector3fc normal;
/*     */     
/* 342 */     public Polygon(ModelPart.Vertex[] vertices, Vector3fc normal) { this.vertices = vertices; this.normal = normal; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/model/geom/ModelPart$Polygon;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #342	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 342 */       //   0	7	0	this	Lnet/minecraft/client/model/geom/ModelPart$Polygon; } public ModelPart.Vertex[] vertices() { return this.vertices; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/geom/ModelPart$Polygon;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #342	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/model/geom/ModelPart$Polygon; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/geom/ModelPart$Polygon;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #342	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/model/geom/ModelPart$Polygon;
/* 342 */       //   0	8	1	o	Ljava/lang/Object; } public Vector3fc normal() { return this.normal; }
/*     */      public Polygon(ModelPart.Vertex[] vertices, float u0, float v0, float u1, float v1, float xTexSize, float yTexSize, boolean mirror, Direction facing) {
/* 344 */       this(vertices, (mirror ? mirrorFacing(facing) : facing).getUnitVec3f());
/*     */       
/* 346 */       float us = 0.0F / xTexSize;
/* 347 */       float vs = 0.0F / yTexSize;
/* 348 */       vertices[0] = vertices[0].remap(u1 / xTexSize - us, v0 / yTexSize + vs);
/* 349 */       vertices[1] = vertices[1].remap(u0 / xTexSize + us, v0 / yTexSize + vs);
/* 350 */       vertices[2] = vertices[2].remap(u0 / xTexSize + us, v1 / yTexSize - vs);
/* 351 */       vertices[3] = vertices[3].remap(u1 / xTexSize - us, v1 / yTexSize - vs);
/*     */       
/* 353 */       if (mirror) {
/* 354 */         int length = vertices.length;
/* 355 */         for (int i = 0; i < length / 2; i++) {
/* 356 */           ModelPart.Vertex tmp = vertices[i];
/* 357 */           vertices[i] = vertices[length - 1 - i];
/* 358 */           vertices[length - 1 - i] = tmp;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private static Direction mirrorFacing(Direction facing) {
/* 364 */       return (facing.getAxis() == Direction.Axis.X) ? facing.getOpposite() : facing;
/*     */     } }
/*     */   public static final class Vertex extends Record { private final float x; private final float y; private final float z; private final float u; private final float v; public static final float SCALE_FACTOR = 16.0F;
/*     */     
/* 368 */     public Vertex(float x, float y, float z, float u, float v) { this.x = x; this.y = y; this.z = z; this.u = u; this.v = v; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/model/geom/ModelPart$Vertex;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #368	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/model/geom/ModelPart$Vertex; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/geom/ModelPart$Vertex;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #368	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/model/geom/ModelPart$Vertex; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/geom/ModelPart$Vertex;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #368	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/model/geom/ModelPart$Vertex;
/* 368 */       //   0	8	1	o	Ljava/lang/Object; } public float x() { return this.x; } public float y() { return this.y; } public float z() { return this.z; } public float u() { return this.u; } public float v() { return this.v; }
/*     */ 
/*     */     
/*     */     public Vertex remap(float u, float v) {
/* 372 */       return new Vertex(this.x, this.y, this.z, u, v);
/*     */     }
/*     */     
/*     */     public float worldX() {
/* 376 */       return this.x / 16.0F;
/*     */     }
/*     */     
/*     */     public float worldY() {
/* 380 */       return this.y / 16.0F;
/*     */     }
/*     */     
/*     */     public float worldZ() {
/* 384 */       return this.z / 16.0F;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/geom/ModelPart.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */