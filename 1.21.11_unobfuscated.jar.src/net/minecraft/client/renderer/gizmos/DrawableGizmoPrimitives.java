/*     */ package net.minecraft.client.renderer.gizmos;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.renderer.MultiBufferSource;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.gizmos.GizmoPrimitives;
/*     */ import net.minecraft.gizmos.TextGizmo;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector4f;
/*     */ import org.joml.Vector4fc;
/*     */ 
/*     */ public class DrawableGizmoPrimitives
/*     */   implements GizmoPrimitives {
/*  25 */   private final Group opaque = new Group(true);
/*  26 */   private final Group translucent = new Group(false);
/*     */   private boolean isEmpty = true;
/*     */   
/*     */   private Group getGroup(int color) {
/*  30 */     if (ARGB.alpha(color) < 255) {
/*  31 */       return this.translucent;
/*     */     }
/*  33 */     return this.opaque;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPoint(Vec3 pos, int color, float size) {
/*  39 */     (getGroup(color)).points.add(new Point(pos, color, size));
/*  40 */     this.isEmpty = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addLine(Vec3 start, Vec3 end, int color, float width) {
/*  45 */     (getGroup(color)).lines.add(new Line(start, end, color, width));
/*  46 */     this.isEmpty = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addTriangleFan(Vec3[] points, int color) {
/*  51 */     (getGroup(color)).triangleFans.add(new TriangleFan(points, color));
/*  52 */     this.isEmpty = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addQuad(Vec3 a, Vec3 b, Vec3 c, Vec3 d, int color) {
/*  57 */     (getGroup(color)).quads.add(new Quad(a, b, c, d, color));
/*  58 */     this.isEmpty = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addText(Vec3 pos, String text, TextGizmo.Style style) {
/*  63 */     (getGroup(style.color())).texts.add(new Text(pos, text, style));
/*  64 */     this.isEmpty = false;
/*     */   }
/*     */   
/*     */   public void render(PoseStack poseStack, MultiBufferSource bufferSource, CameraRenderState camera, Matrix4f modelViewMatrix) {
/*  68 */     this.opaque.render(poseStack, bufferSource, camera, modelViewMatrix);
/*  69 */     this.translucent.render(poseStack, bufferSource, camera, modelViewMatrix);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  73 */     return this.isEmpty;
/*     */   }
/*     */   private static final class Line extends Record { private final Vec3 start; private final Vec3 end; private final int color; private final float width;
/*  76 */     private Line(Vec3 start, Vec3 end, int color, float width) { this.start = start; this.end = end; this.color = color; this.width = width; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Line;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  76 */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Line; } public Vec3 start() { return this.start; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Line;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Line; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Line;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Line;
/*  76 */       //   0	8	1	o	Ljava/lang/Object; } public Vec3 end() { return this.end; } public int color() { return this.color; } public float width() { return this.width; }
/*     */      } private static final class TriangleFan extends Record { private final Vec3[] points; private final int color;
/*  78 */     private TriangleFan(Vec3[] points, int color) { this.points = points; this.color = color; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$TriangleFan;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$TriangleFan; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$TriangleFan;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$TriangleFan; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$TriangleFan;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$TriangleFan;
/*  78 */       //   0	8	1	o	Ljava/lang/Object; } public Vec3[] points() { return this.points; } public int color() { return this.color; }
/*     */      } private static final class Quad extends Record { private final Vec3 a; private final Vec3 b; private final Vec3 c; private final Vec3 d; private final int color;
/*  80 */     private Quad(Vec3 a, Vec3 b, Vec3 c, Vec3 d, int color) { this.a = a; this.b = b; this.c = c; this.d = d; this.color = color; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Quad;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #80	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Quad; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Quad;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #80	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Quad; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Quad;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #80	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Quad;
/*  80 */       //   0	8	1	o	Ljava/lang/Object; } public Vec3 a() { return this.a; } public Vec3 b() { return this.b; } public Vec3 c() { return this.c; } public Vec3 d() { return this.d; } public int color() { return this.color; }
/*     */      } private static final class Text extends Record { private final Vec3 pos; private final String text; private final TextGizmo.Style style;
/*  82 */     private Text(Vec3 pos, String text, TextGizmo.Style style) { this.pos = pos; this.text = text; this.style = style; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Text;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #82	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Text; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Text;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #82	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Text; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Text;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #82	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Text;
/*  82 */       //   0	8	1	o	Ljava/lang/Object; } public Vec3 pos() { return this.pos; } public String text() { return this.text; } public TextGizmo.Style style() { return this.style; }
/*     */      } private static final class Point extends Record { private final Vec3 pos; private final int color; private final float size;
/*  84 */     private Point(Vec3 pos, int color, float size) { this.pos = pos; this.color = color; this.size = size; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Point;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #84	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Point; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Point;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #84	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Point; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Point;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #84	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Point;
/*  84 */       //   0	8	1	o	Ljava/lang/Object; } public Vec3 pos() { return this.pos; } public int color() { return this.color; } public float size() { return this.size; }
/*     */      } private static final class Group extends Record { private final boolean opaque; private final List<DrawableGizmoPrimitives.Line> lines; private final List<DrawableGizmoPrimitives.Quad> quads; private final List<DrawableGizmoPrimitives.TriangleFan> triangleFans; private final List<DrawableGizmoPrimitives.Text> texts; private final List<DrawableGizmoPrimitives.Point> points;
/*  86 */     private Group(boolean opaque, List<DrawableGizmoPrimitives.Line> lines, List<DrawableGizmoPrimitives.Quad> quads, List<DrawableGizmoPrimitives.TriangleFan> triangleFans, List<DrawableGizmoPrimitives.Text> texts, List<DrawableGizmoPrimitives.Point> points) { this.opaque = opaque; this.lines = lines; this.quads = quads; this.triangleFans = triangleFans; this.texts = texts; this.points = points; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Group;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #86	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Group; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Group;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #86	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Group; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Group;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #86	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Group;
/*  86 */       //   0	8	1	o	Ljava/lang/Object; } public boolean opaque() { return this.opaque; } public List<DrawableGizmoPrimitives.Line> lines() { return this.lines; } public List<DrawableGizmoPrimitives.Quad> quads() { return this.quads; } public List<DrawableGizmoPrimitives.TriangleFan> triangleFans() { return this.triangleFans; } public List<DrawableGizmoPrimitives.Text> texts() { return this.texts; } public List<DrawableGizmoPrimitives.Point> points() { return this.points; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Group(boolean opaque) {
/*  96 */       this(opaque, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
/*     */     }
/*     */     
/*     */     public void render(PoseStack poseStack, MultiBufferSource bufferSource, CameraRenderState camera, Matrix4f modelViewMatrix) {
/* 100 */       renderQuads(poseStack, bufferSource, camera);
/* 101 */       renderTriangleFans(poseStack, bufferSource, camera);
/* 102 */       renderLines(poseStack, bufferSource, camera, modelViewMatrix);
/* 103 */       renderTexts(poseStack, bufferSource, camera);
/* 104 */       renderPoints(poseStack, bufferSource, camera);
/*     */     }
/*     */     
/*     */     private void renderTexts(PoseStack poseStack, MultiBufferSource bufferSource, CameraRenderState camera) {
/* 108 */       Minecraft minecraft = Minecraft.getInstance();
/* 109 */       Font font = minecraft.font;
/*     */       
/* 111 */       if (!camera.initialized) {
/*     */         return;
/*     */       }
/* 114 */       double camX = camera.pos.x();
/* 115 */       double camY = camera.pos.y();
/* 116 */       double camZ = camera.pos.z();
/*     */       
/* 118 */       for (DrawableGizmoPrimitives.Text text : this.texts) {
/* 119 */         float fontX; poseStack.pushPose();
/* 120 */         poseStack.translate((float)(text.pos().x() - camX), (float)(text.pos().y() - camY), (float)(text.pos().z() - camZ));
/* 121 */         poseStack.mulPose((Quaternionfc)camera.orientation);
/* 122 */         poseStack.scale(text.style.scale() / 16.0F, -text.style.scale() / 16.0F, text.style.scale() / 16.0F);
/*     */ 
/*     */         
/* 125 */         if (text.style.adjustLeft().isEmpty()) {
/*     */           
/* 127 */           fontX = -font.width(text.text) / 2.0F;
/*     */         } else {
/* 129 */           fontX = (float)-text.style.adjustLeft().getAsDouble() / text.style.scale();
/*     */         } 
/*     */         
/* 132 */         font.drawInBatch(text.text, fontX, 0.0F, text.style.color(), false, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
/*     */         
/* 134 */         poseStack.popPose();
/*     */       } 
/*     */     }
/*     */     
/*     */     private void renderLines(PoseStack poseStack, MultiBufferSource bufferSource, CameraRenderState camera, Matrix4f modelViewMatrix) {
/* 139 */       VertexConsumer builder = bufferSource.getBuffer(this.opaque ? RenderTypes.lines() : RenderTypes.linesTranslucent());
/* 140 */       PoseStack.Pose pose = poseStack.last();
/* 141 */       Vector4f start = new Vector4f();
/* 142 */       Vector4f end = new Vector4f();
/* 143 */       Vector4f startViewSpace = new Vector4f();
/* 144 */       Vector4f endViewSpace = new Vector4f();
/* 145 */       Vector4f intersectionInWorld = new Vector4f();
/* 146 */       double camX = camera.pos.x();
/* 147 */       double camY = camera.pos.y();
/* 148 */       double camZ = camera.pos.z();
/*     */       
/* 150 */       for (DrawableGizmoPrimitives.Line line : this.lines) {
/* 151 */         start.set(line.start().x() - camX, line.start().y() - camY, line.start().z() - camZ, 1.0D);
/* 152 */         end.set(line.end().x() - camX, line.end().y() - camY, line.end().z() - camZ, 1.0D);
/* 153 */         start.mul((Matrix4fc)modelViewMatrix, startViewSpace);
/* 154 */         end.mul((Matrix4fc)modelViewMatrix, endViewSpace);
/*     */         
/* 156 */         boolean startIsBehindCamera = (startViewSpace.z > -0.05F);
/* 157 */         boolean endIsBehindCamera = (endViewSpace.z > -0.05F);
/*     */         
/* 159 */         if (startIsBehindCamera && endIsBehindCamera) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 164 */         if (startIsBehindCamera || endIsBehindCamera) {
/*     */ 
/*     */ 
/*     */           
/* 168 */           float denom = endViewSpace.z - startViewSpace.z;
/* 169 */           if (Math.abs(denom) < 1.0E-9F) {
/*     */             continue;
/*     */           }
/*     */ 
/*     */           
/* 174 */           float intersection = Mth.clamp((-0.05F - startViewSpace.z) / denom, 0.0F, 1.0F);
/* 175 */           start.lerp((Vector4fc)end, intersection, intersectionInWorld);
/* 176 */           if (startIsBehindCamera) {
/* 177 */             start.set((Vector4fc)intersectionInWorld);
/*     */           } else {
/* 179 */             end.set((Vector4fc)intersectionInWorld);
/*     */           } 
/*     */         } 
/*     */         
/* 183 */         builder.addVertex(pose, start.x, start.y, start.z).setNormal(pose, end.x - start.x, end.y - start.y, end.z - start.z).setColor(line.color()).setLineWidth(line.width());
/* 184 */         builder.addVertex(pose, end.x, end.y, end.z).setNormal(pose, end.x - start.x, end.y - start.y, end.z - start.z).setColor(line.color()).setLineWidth(line.width());
/*     */       } 
/*     */     }
/*     */     
/*     */     private void renderTriangleFans(PoseStack poseStack, MultiBufferSource bufferSource, CameraRenderState camera) {
/* 189 */       PoseStack.Pose pose = poseStack.last();
/* 190 */       double camX = camera.pos.x();
/* 191 */       double camY = camera.pos.y();
/* 192 */       double camZ = camera.pos.z();
/* 193 */       for (DrawableGizmoPrimitives.TriangleFan triangleFan : this.triangleFans) {
/* 194 */         VertexConsumer builder = bufferSource.getBuffer(RenderTypes.debugTriangleFan());
/* 195 */         for (Vec3 point : triangleFan.points()) {
/* 196 */           builder.addVertex(pose, (float)(point.x() - camX), (float)(point.y() - camY), (float)(point.z() - camZ)).setColor(triangleFan.color());
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     private void renderQuads(PoseStack poseStack, MultiBufferSource bufferSource, CameraRenderState camera) {
/* 202 */       VertexConsumer builder = bufferSource.getBuffer(RenderTypes.debugFilledBox());
/* 203 */       PoseStack.Pose pose = poseStack.last();
/* 204 */       double camX = camera.pos.x();
/* 205 */       double camY = camera.pos.y();
/* 206 */       double camZ = camera.pos.z();
/* 207 */       for (DrawableGizmoPrimitives.Quad quad : this.quads) {
/* 208 */         builder.addVertex(pose, (float)(quad.a().x() - camX), (float)(quad.a().y() - camY), (float)(quad.a().z() - camZ)).setColor(quad.color());
/* 209 */         builder.addVertex(pose, (float)(quad.b().x() - camX), (float)(quad.b().y() - camY), (float)(quad.b().z() - camZ)).setColor(quad.color());
/* 210 */         builder.addVertex(pose, (float)(quad.c().x() - camX), (float)(quad.c().y() - camY), (float)(quad.c().z() - camZ)).setColor(quad.color());
/* 211 */         builder.addVertex(pose, (float)(quad.d().x() - camX), (float)(quad.d().y() - camY), (float)(quad.d().z() - camZ)).setColor(quad.color());
/*     */       } 
/*     */     }
/*     */     
/*     */     private void renderPoints(PoseStack poseStack, MultiBufferSource bufferSource, CameraRenderState camera) {
/* 216 */       VertexConsumer builder = bufferSource.getBuffer(RenderTypes.debugPoint());
/* 217 */       PoseStack.Pose pose = poseStack.last();
/* 218 */       double camX = camera.pos.x();
/* 219 */       double camY = camera.pos.y();
/* 220 */       double camZ = camera.pos.z();
/* 221 */       for (DrawableGizmoPrimitives.Point point : this.points)
/* 222 */         builder.addVertex(pose, (float)(point.pos.x() - camX), (float)(point.pos.y() - camY), (float)(point.pos.z() - camZ)).setColor(point.color()).setLineWidth(point.size()); 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */