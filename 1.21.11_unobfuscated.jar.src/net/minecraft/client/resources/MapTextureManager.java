/*    */ package net.minecraft.client.resources;
/*    */ import com.mojang.blaze3d.platform.NativeImage;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*    */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*    */ import net.minecraft.client.renderer.texture.TextureManager;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.material.MapColor;
/*    */ import net.minecraft.world.level.saveddata.maps.MapId;
/*    */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*    */ 
/*    */ public class MapTextureManager implements AutoCloseable {
/* 15 */   private final Int2ObjectMap<MapInstance> maps = (Int2ObjectMap<MapInstance>)new Int2ObjectOpenHashMap();
/*    */   private final TextureManager textureManager;
/*    */   
/*    */   public MapTextureManager(TextureManager textureManager) {
/* 19 */     this.textureManager = textureManager;
/*    */   }
/*    */   
/*    */   public void update(MapId id, MapItemSavedData data) {
/* 23 */     getOrCreateMapInstance(id, data).forceUpload();
/*    */   }
/*    */   
/*    */   public Identifier prepareMapTexture(MapId id, MapItemSavedData data) {
/* 27 */     MapInstance mapInstance = getOrCreateMapInstance(id, data);
/* 28 */     mapInstance.updateTextureIfNeeded();
/* 29 */     return mapInstance.location;
/*    */   }
/*    */   
/*    */   public void resetData() {
/* 33 */     for (ObjectIterator<MapInstance> objectIterator = this.maps.values().iterator(); objectIterator.hasNext(); ) { MapInstance mapInstance = objectIterator.next();
/* 34 */       mapInstance.close(); }
/*    */ 
/*    */     
/* 37 */     this.maps.clear();
/*    */   }
/*    */   
/*    */   private MapInstance getOrCreateMapInstance(MapId id, MapItemSavedData data) {
/* 41 */     return (MapInstance)this.maps.compute(id.id(), (k, instance) -> {
/*    */           if (instance == null) {
/*    */             return new MapInstance(this, data, data);
/*    */           }
/*    */           instance.replaceMapData(data);
/*    */           return instance;
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {
/* 54 */     resetData();
/*    */   }
/*    */   
/*    */   private class MapInstance implements AutoCloseable {
/*    */     private MapItemSavedData data;
/*    */     private final DynamicTexture texture;
/*    */     private boolean requiresUpload = true;
/*    */     private final Identifier location;
/*    */     
/*    */     private MapInstance(MapTextureManager this$0, int id, MapItemSavedData data) {
/* 64 */       this.data = data;
/* 65 */       this.texture = new DynamicTexture(() -> "Map " + id, 128, 128, true);
/*    */       
/* 67 */       this.location = Identifier.withDefaultNamespace("map/" + id);
/* 68 */       this$0.textureManager.register(this.location, (AbstractTexture)this.texture);
/*    */     }
/*    */     
/*    */     private void replaceMapData(MapItemSavedData data) {
/* 72 */       boolean dataChanged = (this.data != data);
/* 73 */       this.data = data;
/* 74 */       this.requiresUpload |= dataChanged;
/*    */     }
/*    */     
/*    */     public void forceUpload() {
/* 78 */       this.requiresUpload = true;
/*    */     }
/*    */     
/*    */     private void updateTextureIfNeeded() {
/* 82 */       if (this.requiresUpload) {
/* 83 */         NativeImage pixels = this.texture.getPixels();
/* 84 */         if (pixels != null) {
/* 85 */           for (int y = 0; y < 128; y++) {
/* 86 */             for (int x = 0; x < 128; x++) {
/* 87 */               int i = x + y * 128;
/* 88 */               pixels.setPixel(x, y, MapColor.getColorFromPackedId(this.data.colors[i]));
/*    */             } 
/*    */           } 
/*    */         }
/* 92 */         this.texture.upload();
/* 93 */         this.requiresUpload = false;
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void close() {
/* 99 */       this.texture.close();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/MapTextureManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */