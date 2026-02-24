/*     */ package net.minecraft.gizmos;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Gizmos {
/*  11 */   private static final ThreadLocal<GizmoCollector> collector = new ThreadLocal<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TemporaryCollection withCollector(GizmoCollector collector) {
/*  17 */     TemporaryCollection result = new TemporaryCollection();
/*  18 */     Gizmos.collector.set(collector);
/*  19 */     return result;
/*     */   }
/*     */   
/*     */   public static GizmoProperties addGizmo(Gizmo gizmo) {
/*  23 */     GizmoCollector collector = Gizmos.collector.get();
/*  24 */     if (collector == null) {
/*  25 */       throw new IllegalStateException("Gizmos cannot be created here! No GizmoCollector has been registered.");
/*     */     }
/*  27 */     return collector.add(gizmo);
/*     */   }
/*     */   
/*     */   public static GizmoProperties cuboid(AABB aabb, GizmoStyle style) {
/*  31 */     return cuboid(aabb, style, false);
/*     */   }
/*     */   
/*     */   public static GizmoProperties cuboid(AABB aabb, GizmoStyle style, boolean coloredCorner) {
/*  35 */     return addGizmo(new CuboidGizmo(aabb, style, coloredCorner));
/*     */   }
/*     */   
/*     */   public static GizmoProperties cuboid(BlockPos blockPos, GizmoStyle style) {
/*  39 */     return cuboid(new AABB(blockPos), style);
/*     */   }
/*     */   
/*     */   public static GizmoProperties cuboid(BlockPos blockPos, float padding, GizmoStyle style) {
/*  43 */     return cuboid(new AABB(blockPos).inflate(padding), style);
/*     */   }
/*     */   
/*     */   public static GizmoProperties circle(Vec3 pos, float radius, GizmoStyle style) {
/*  47 */     return addGizmo(new CircleGizmo(pos, radius, style));
/*     */   }
/*     */   
/*     */   public static GizmoProperties line(Vec3 start, Vec3 end, int argb) {
/*  51 */     return addGizmo(new LineGizmo(start, end, argb, 3.0F));
/*     */   }
/*     */   
/*     */   public static GizmoProperties line(Vec3 start, Vec3 end, int argb, float width) {
/*  55 */     return addGizmo(new LineGizmo(start, end, argb, width));
/*     */   }
/*     */   
/*     */   public static GizmoProperties arrow(Vec3 start, Vec3 end, int argb) {
/*  59 */     return addGizmo(new ArrowGizmo(start, end, argb, 2.5F));
/*     */   }
/*     */   
/*     */   public static GizmoProperties arrow(Vec3 start, Vec3 end, int argb, float width) {
/*  63 */     return addGizmo(new ArrowGizmo(start, end, argb, width));
/*     */   }
/*     */   
/*     */   public static GizmoProperties rect(Vec3 cuboidCornerA, Vec3 cuboidCornerB, Direction face, GizmoStyle style) {
/*  67 */     return addGizmo(RectGizmo.fromCuboidFace(cuboidCornerA, cuboidCornerB, face, style));
/*     */   }
/*     */   
/*     */   public static GizmoProperties rect(Vec3 cornerA, Vec3 cornerB, Vec3 cornerC, Vec3 cornerD, GizmoStyle style) {
/*  71 */     return addGizmo(new RectGizmo(cornerA, cornerB, cornerC, cornerD, style));
/*     */   }
/*     */   
/*     */   public static GizmoProperties point(Vec3 position, int argb, float size) {
/*  75 */     return addGizmo(new PointGizmo(position, argb, size));
/*     */   }
/*     */   
/*     */   public static GizmoProperties billboardTextOverBlock(String text, BlockPos pos, int row, int color, float scale) {
/*  79 */     double firstRowStartPosition = 1.3D;
/*  80 */     double rowHeight = 0.2D;
/*     */     
/*  82 */     GizmoProperties properties = billboardText(text, Vec3.atLowerCornerWithOffset((Vec3i)pos, 0.5D, 1.3D + row * 0.2D, 0.5D), TextGizmo.Style.forColorAndCentered(color).withScale(scale));
/*  83 */     properties.setAlwaysOnTop();
/*  84 */     return properties;
/*     */   }
/*     */   
/*     */   public static GizmoProperties billboardTextOverMob(Entity entity, int row, String text, int color, float scale) {
/*  88 */     double firstRowStartPosition = 2.4D;
/*  89 */     double rowHeight = 0.25D;
/*     */ 
/*     */ 
/*     */     
/*  93 */     double x = entity.getBlockX() + 0.5D;
/*  94 */     double y = entity.getY() + 2.4D + row * 0.25D;
/*  95 */     double z = entity.getBlockZ() + 0.5D;
/*     */     
/*  97 */     float textAdjustLeft = 0.5F;
/*  98 */     GizmoProperties properties = billboardText(text, new Vec3(x, y, z), TextGizmo.Style.forColor(color).withScale(scale).withLeftAlignment(0.5F));
/*  99 */     properties.setAlwaysOnTop();
/* 100 */     return properties;
/*     */   }
/*     */   
/*     */   public static GizmoProperties billboardText(String name, Vec3 pos, TextGizmo.Style style) {
/* 104 */     return addGizmo(new TextGizmo(pos, name, style));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class TemporaryCollection
/*     */     implements AutoCloseable
/*     */   {
/* 112 */     private final GizmoCollector old = Gizmos.collector.get();
/*     */     
/*     */     private boolean closed;
/*     */     
/*     */     public void close() {
/* 117 */       if (!this.closed) {
/* 118 */         this.closed = true;
/* 119 */         Gizmos.collector.set(this.old);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gizmos/Gizmos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */