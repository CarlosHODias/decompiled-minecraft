/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
/*    */ import net.minecraft.resources.FileToIdConverter;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ import net.minecraft.world.waypoints.WaypointStyleAsset;
/*    */ import net.minecraft.world.waypoints.WaypointStyleAssets;
/*    */ 
/*    */ public class WaypointStyleManager
/*    */   extends SimpleJsonResourceReloadListener<WaypointStyle> {
/* 18 */   private static final FileToIdConverter ASSET_LISTER = FileToIdConverter.json("waypoint_style");
/*    */   
/* 20 */   private static final WaypointStyle MISSING = new WaypointStyle(0, 1, 
/*    */       
/* 22 */       List.of(MissingTextureAtlasSprite.getLocation()));
/*    */ 
/*    */   
/* 25 */   private Map<ResourceKey<WaypointStyleAsset>, WaypointStyle> waypointStyles = Map.of();
/*    */   
/*    */   public WaypointStyleManager() {
/* 28 */     super(WaypointStyle.CODEC, ASSET_LISTER);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void apply(Map<Identifier, WaypointStyle> preparations, ResourceManager manager, ProfilerFiller profiler) {
/* 33 */     this.waypointStyles = (Map<ResourceKey<WaypointStyleAsset>, WaypointStyle>)preparations.entrySet().stream().collect(Collectors.toUnmodifiableMap(e -> ResourceKey.create(WaypointStyleAssets.ROOT_ID, (Identifier)e.getKey()), Map.Entry::getValue));
/*    */   }
/*    */   
/*    */   public WaypointStyle get(ResourceKey<WaypointStyleAsset> id) {
/* 37 */     return this.waypointStyles.getOrDefault(id, MISSING);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/WaypointStyleManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */