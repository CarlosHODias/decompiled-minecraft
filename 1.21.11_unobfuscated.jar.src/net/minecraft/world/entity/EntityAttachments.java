/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.EnumMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class EntityAttachments
/*    */ {
/*    */   private final Map<EntityAttachment, List<Vec3>> attachments;
/*    */   
/*    */   private EntityAttachments(Map<EntityAttachment, List<Vec3>> attachments) {
/* 17 */     this.attachments = attachments;
/*    */   }
/*    */   
/*    */   public static EntityAttachments createDefault(float width, float height) {
/* 21 */     return builder().build(width, height);
/*    */   }
/*    */   
/*    */   public static Builder builder() {
/* 25 */     return new Builder();
/*    */   }
/*    */   
/*    */   public EntityAttachments scale(float x, float y, float z) {
/* 29 */     return new EntityAttachments(Util.makeEnumMap(EntityAttachment.class, attachment -> {
/*    */             List<Vec3> list = new ArrayList<>();
/*    */             for (Vec3 vec3 : this.attachments.get(z)) {
/*    */               list.add(vec3.multiply(x, x, y));
/*    */             }
/*    */             return list;
/*    */           }));
/*    */   }
/*    */   
/*    */   public Vec3 getNullable(EntityAttachment attachment, int index, float rotY) {
/* 39 */     List<Vec3> points = this.attachments.get(attachment);
/* 40 */     if (index < 0 || index >= points.size()) {
/* 41 */       return null;
/*    */     }
/* 43 */     return transformPoint(points.get(index), rotY);
/*    */   }
/*    */   
/*    */   public Vec3 get(EntityAttachment attachment, int index, float rotY) {
/* 47 */     Vec3 point = getNullable(attachment, index, rotY);
/* 48 */     if (point == null) {
/* 49 */       throw new IllegalStateException("Had no attachment point of type: " + String.valueOf(attachment) + " for index: " + index);
/*    */     }
/* 51 */     return point;
/*    */   }
/*    */   
/*    */   public Vec3 getAverage(EntityAttachment attachment) {
/* 55 */     List<Vec3> points = this.attachments.get(attachment);
/* 56 */     if (points == null || points.isEmpty()) {
/* 57 */       throw new IllegalStateException("No attachment points of type: PASSENGER");
/*    */     }
/* 59 */     Vec3 sum = Vec3.ZERO;
/* 60 */     for (Vec3 point : points) {
/* 61 */       sum = sum.add(point);
/*    */     }
/* 63 */     return sum.scale((1.0F / points.size()));
/*    */   }
/*    */   
/*    */   public Vec3 getClamped(EntityAttachment attachment, int index, float rotY) {
/* 67 */     List<Vec3> points = this.attachments.get(attachment);
/* 68 */     if (points.isEmpty()) {
/* 69 */       throw new IllegalStateException("Had no attachment points of type: " + String.valueOf(attachment));
/*    */     }
/* 71 */     Vec3 point = points.get(Mth.clamp(index, 0, points.size() - 1));
/* 72 */     return transformPoint(point, rotY);
/*    */   }
/*    */   
/*    */   private static Vec3 transformPoint(Vec3 point, float rotY) {
/* 76 */     return point.yRot(-rotY * 0.017453292F);
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 80 */     private final Map<EntityAttachment, List<Vec3>> attachments = new EnumMap<>(EntityAttachment.class);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public Builder attach(EntityAttachment attachment, float x, float y, float z) {
/* 86 */       return attach(attachment, new Vec3(x, y, z));
/*    */     }
/*    */     
/*    */     public Builder attach(EntityAttachment attachment, Vec3 point) {
/* 90 */       ((List<Vec3>)this.attachments.computeIfAbsent(attachment, a -> new ArrayList(1))).add(point);
/* 91 */       return this;
/*    */     }
/*    */     
/*    */     public EntityAttachments build(float width, float height) {
/* 95 */       Map<EntityAttachment, List<Vec3>> attachments = Util.makeEnumMap(EntityAttachment.class, attachment -> {
/*    */             List<Vec3> points = this.attachments.get(width);
/*    */             return (points == null) ? width.createFallbackPoints(width, width) : List.<Vec3>copyOf(points);
/*    */           });
/* 99 */       return new EntityAttachments(attachments);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/EntityAttachments.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */