/*    */ package net.minecraft.client.data.models;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.client.resources.WaypointStyle;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.waypoints.WaypointStyleAsset;
/*    */ import net.minecraft.world.waypoints.WaypointStyleAssets;
/*    */ 
/*    */ public class WaypointStyleProvider implements DataProvider {
/*    */   private final PackOutput.PathProvider pathProvider;
/*    */   
/*    */   public WaypointStyleProvider(PackOutput output) {
/* 22 */     this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "waypoint_style");
/*    */   }
/*    */   
/*    */   private static void bootstrap(BiConsumer<ResourceKey<WaypointStyleAsset>, WaypointStyle> consumer) {
/* 26 */     consumer.accept(WaypointStyleAssets.DEFAULT, new WaypointStyle(128, 332, 
/*    */ 
/*    */           
/* 29 */           List.of(
/* 30 */             Identifier.withDefaultNamespace("default_0"), 
/* 31 */             Identifier.withDefaultNamespace("default_1"), 
/* 32 */             Identifier.withDefaultNamespace("default_2"), 
/* 33 */             Identifier.withDefaultNamespace("default_3"))));
/*    */ 
/*    */     
/* 36 */     consumer.accept(WaypointStyleAssets.BOWTIE, new WaypointStyle(64, 332, 
/*    */ 
/*    */           
/* 39 */           List.of(
/* 40 */             Identifier.withDefaultNamespace("bowtie"), 
/* 41 */             Identifier.withDefaultNamespace("default_0"), 
/* 42 */             Identifier.withDefaultNamespace("default_1"), 
/* 43 */             Identifier.withDefaultNamespace("default_2"), 
/* 44 */             Identifier.withDefaultNamespace("default_3"))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput cache) {
/* 51 */     Map<ResourceKey<WaypointStyleAsset>, WaypointStyle> waypointStyles = new HashMap<>();
/* 52 */     bootstrap((id, asset) -> {
/*    */           if (waypointStyles.putIfAbsent(id, asset) != null) {
/*    */             throw new IllegalStateException("Tried to register waypoint style twice for id: " + String.valueOf(id));
/*    */           }
/*    */         });
/* 57 */     Objects.requireNonNull(this.pathProvider); return DataProvider.saveAll(cache, WaypointStyle.CODEC, this.pathProvider::json, waypointStyles);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 62 */     return "Waypoint Style Definitions";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/models/WaypointStyleProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */