/*     */ package net.minecraft.client.renderer.debug;
/*     */ 
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.CameraType;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.client.server.IntegratedServer;
/*     */ import net.minecraft.gizmos.GizmoStyle;
/*     */ import net.minecraft.gizmos.Gizmos;
/*     */ import net.minecraft.gizmos.TextGizmo;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.debug.DebugValueAccess;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class EntityHitboxDebugRenderer
/*     */   implements DebugRenderer.SimpleDebugRenderer
/*     */ {
/*     */   final Minecraft minecraft;
/*     */   
/*     */   public EntityHitboxDebugRenderer(Minecraft minecraft) {
/*  27 */     this.minecraft = minecraft;
/*     */   }
/*     */ 
/*     */   
/*     */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/*  32 */     if (this.minecraft.level == null) {
/*     */       return;
/*     */     }
/*  35 */     for (Entity entity : (Iterable<Entity>)this.minecraft.level.entitiesForRendering()) {
/*  36 */       if (entity.isInvisible() || !frustum.isVisible(entity.getBoundingBox())) {
/*     */         continue;
/*     */       }
/*     */       
/*  40 */       if (entity == this.minecraft.getCameraEntity() && this.minecraft.options.getCameraType() == CameraType.FIRST_PERSON) {
/*     */         continue;
/*     */       }
/*     */       
/*  44 */       showHitboxes(entity, partialTicks, false);
/*     */       
/*  46 */       if (SharedConstants.DEBUG_SHOW_LOCAL_SERVER_ENTITY_HIT_BOXES) {
/*  47 */         Entity serverEntity = getServerEntity(entity);
/*  48 */         if (serverEntity != null) {
/*  49 */           showHitboxes(entity, partialTicks, true); continue;
/*     */         } 
/*  51 */         Gizmos.billboardText("Missing Server Entity", entity.getPosition(partialTicks).add(0.0D, entity.getBoundingBox().getYsize() + 1.5D, 0.0D), TextGizmo.Style.forColorAndCentered(-65536));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Entity getServerEntity(Entity entity) {
/*  58 */     IntegratedServer server = this.minecraft.getSingleplayerServer();
/*  59 */     if (server != null) {
/*  60 */       ServerLevel level = server.getLevel(entity.level().dimension());
/*  61 */       if (level != null) {
/*  62 */         return level.getEntity(entity.getId());
/*     */       }
/*     */     } 
/*  65 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void showHitboxes(Entity entity, float partialTicks, boolean isServerEntity) {
/*  71 */     Vec3 latestPosition = entity.position();
/*  72 */     Vec3 currentPosition = entity.getPosition(partialTicks);
/*  73 */     Vec3 offset = currentPosition.subtract(latestPosition);
/*     */ 
/*     */     
/*  76 */     int mainColor = isServerEntity ? -16711936 : -1;
/*  77 */     Gizmos.cuboid(entity.getBoundingBox().move(offset), GizmoStyle.stroke(mainColor));
/*  78 */     Gizmos.point(currentPosition, mainColor, 2.0F);
/*     */ 
/*     */     
/*  81 */     Entity vehicle = entity.getVehicle();
/*  82 */     if (vehicle != null) {
/*  83 */       float width = Math.min(vehicle.getBbWidth(), entity.getBbWidth()) / 2.0F;
/*  84 */       float height = 0.0625F;
/*  85 */       Vec3 position = vehicle.getPassengerRidingPosition(entity).add(offset);
/*  86 */       Gizmos.cuboid(new AABB(position.x - width, position.y, position.z - width, position.x + width, position.y + 0.0625D, position.z + width), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  94 */           GizmoStyle.stroke(-256));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  99 */     if (entity instanceof net.minecraft.world.entity.LivingEntity) {
/* 100 */       AABB bb = entity.getBoundingBox().move(offset);
/* 101 */       float padding = 0.01F;
/* 102 */       Gizmos.cuboid(new AABB(bb.minX, bb.minY + 
/*     */             
/* 104 */             entity.getEyeHeight() - 0.009999999776482582D, bb.minZ, bb.maxX, bb.minY + 
/*     */ 
/*     */             
/* 107 */             entity.getEyeHeight() + 0.009999999776482582D, bb.maxZ), 
/*     */ 
/*     */           
/* 110 */           GizmoStyle.stroke(-65536));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 115 */     if (entity instanceof EnderDragon) { EnderDragon dragon = (EnderDragon)entity;
/* 116 */       for (EnderDragonPart subEntity : dragon.getSubEntities()) {
/* 117 */         Vec3 latestSubPosition = subEntity.position();
/* 118 */         Vec3 currentSubPosition = subEntity.getPosition(partialTicks);
/* 119 */         Vec3 subOffset = currentSubPosition.subtract(latestSubPosition);
/* 120 */         Gizmos.cuboid(
/* 121 */             subEntity.getBoundingBox().move(subOffset), 
/* 122 */             GizmoStyle.stroke(ARGB.colorFromFloat(1.0F, 0.25F, 1.0F, 0.0F)));
/*     */       }  }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 128 */     Vec3 eyePosition = currentPosition.add(0.0D, entity.getEyeHeight(), 0.0D);
/* 129 */     Vec3 viewVector = entity.getViewVector(partialTicks);
/* 130 */     Gizmos.arrow(eyePosition, eyePosition.add(viewVector.scale(2.0D)), -16776961);
/*     */ 
/*     */     
/* 133 */     if (isServerEntity) {
/* 134 */       Vec3 deltaMovement = entity.getDeltaMovement();
/* 135 */       Gizmos.arrow(currentPosition, currentPosition.add(deltaMovement), -256);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/EntityHitboxDebugRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */