/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*    */ import com.mojang.blaze3d.resource.ResourceHandle;
/*    */ import java.util.Set;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class LevelTargetBundle
/*    */   implements PostChain.TargetBundle
/*    */ {
/* 11 */   public static final Identifier MAIN_TARGET_ID = PostChain.MAIN_TARGET_ID;
/* 12 */   public static final Identifier TRANSLUCENT_TARGET_ID = Identifier.withDefaultNamespace("translucent");
/* 13 */   public static final Identifier ITEM_ENTITY_TARGET_ID = Identifier.withDefaultNamespace("item_entity");
/* 14 */   public static final Identifier PARTICLES_TARGET_ID = Identifier.withDefaultNamespace("particles");
/* 15 */   public static final Identifier WEATHER_TARGET_ID = Identifier.withDefaultNamespace("weather");
/* 16 */   public static final Identifier CLOUDS_TARGET_ID = Identifier.withDefaultNamespace("clouds");
/* 17 */   public static final Identifier ENTITY_OUTLINE_TARGET_ID = Identifier.withDefaultNamespace("entity_outline");
/*    */   
/* 19 */   public static final Set<Identifier> MAIN_TARGETS = Set.of(MAIN_TARGET_ID);
/* 20 */   public static final Set<Identifier> OUTLINE_TARGETS = Set.of(MAIN_TARGET_ID, ENTITY_OUTLINE_TARGET_ID);
/* 21 */   public static final Set<Identifier> SORTING_TARGETS = Set.of(MAIN_TARGET_ID, TRANSLUCENT_TARGET_ID, ITEM_ENTITY_TARGET_ID, PARTICLES_TARGET_ID, WEATHER_TARGET_ID, CLOUDS_TARGET_ID);
/*    */   
/* 23 */   public ResourceHandle<RenderTarget> main = ResourceHandle.invalid();
/*    */   
/*    */   public ResourceHandle<RenderTarget> translucent;
/*    */   public ResourceHandle<RenderTarget> itemEntity;
/*    */   public ResourceHandle<RenderTarget> particles;
/*    */   public ResourceHandle<RenderTarget> weather;
/*    */   public ResourceHandle<RenderTarget> clouds;
/*    */   public ResourceHandle<RenderTarget> entityOutline;
/*    */   
/*    */   public void replace(Identifier id, ResourceHandle<RenderTarget> handle) {
/* 33 */     if (id.equals(MAIN_TARGET_ID)) {
/* 34 */       this.main = handle;
/* 35 */     } else if (id.equals(TRANSLUCENT_TARGET_ID)) {
/* 36 */       this.translucent = handle;
/* 37 */     } else if (id.equals(ITEM_ENTITY_TARGET_ID)) {
/* 38 */       this.itemEntity = handle;
/* 39 */     } else if (id.equals(PARTICLES_TARGET_ID)) {
/* 40 */       this.particles = handle;
/* 41 */     } else if (id.equals(WEATHER_TARGET_ID)) {
/* 42 */       this.weather = handle;
/* 43 */     } else if (id.equals(CLOUDS_TARGET_ID)) {
/* 44 */       this.clouds = handle;
/* 45 */     } else if (id.equals(ENTITY_OUTLINE_TARGET_ID)) {
/* 46 */       this.entityOutline = handle;
/*    */     } else {
/* 48 */       throw new IllegalArgumentException("No target with id " + String.valueOf(id));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public ResourceHandle<RenderTarget> get(Identifier id) {
/* 54 */     if (id.equals(MAIN_TARGET_ID))
/* 55 */       return this.main; 
/* 56 */     if (id.equals(TRANSLUCENT_TARGET_ID))
/* 57 */       return this.translucent; 
/* 58 */     if (id.equals(ITEM_ENTITY_TARGET_ID))
/* 59 */       return this.itemEntity; 
/* 60 */     if (id.equals(PARTICLES_TARGET_ID))
/* 61 */       return this.particles; 
/* 62 */     if (id.equals(WEATHER_TARGET_ID))
/* 63 */       return this.weather; 
/* 64 */     if (id.equals(CLOUDS_TARGET_ID))
/* 65 */       return this.clouds; 
/* 66 */     if (id.equals(ENTITY_OUTLINE_TARGET_ID)) {
/* 67 */       return this.entityOutline;
/*    */     }
/* 69 */     return null;
/*    */   }
/*    */   
/*    */   public void clear() {
/* 73 */     this.main = ResourceHandle.invalid();
/* 74 */     this.translucent = null;
/* 75 */     this.itemEntity = null;
/* 76 */     this.particles = null;
/* 77 */     this.weather = null;
/* 78 */     this.clouds = null;
/* 79 */     this.entityOutline = null;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/LevelTargetBundle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */