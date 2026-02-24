/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.resources.FileToIdConverter;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ import net.minecraft.world.item.equipment.EquipmentAsset;
/*    */ import net.minecraft.world.item.equipment.EquipmentAssets;
/*    */ 
/*    */ public class EquipmentAssetManager
/*    */   extends SimpleJsonResourceReloadListener<EquipmentClientInfo> {
/* 16 */   public static final EquipmentClientInfo MISSING = new EquipmentClientInfo(Map.of());
/* 17 */   private static final FileToIdConverter ASSET_LISTER = FileToIdConverter.json("equipment");
/*    */   
/* 19 */   private Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> equipmentAssets = Map.of();
/*    */   
/*    */   public EquipmentAssetManager() {
/* 22 */     super(EquipmentClientInfo.CODEC, ASSET_LISTER);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void apply(Map<Identifier, EquipmentClientInfo> preparations, ResourceManager manager, ProfilerFiller profiler) {
/* 27 */     this.equipmentAssets = (Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo>)preparations.entrySet().stream().collect(Collectors.toUnmodifiableMap(e -> ResourceKey.create(EquipmentAssets.ROOT_ID, (Identifier)e.getKey()), Map.Entry::getValue));
/*    */   }
/*    */   
/*    */   public EquipmentClientInfo get(ResourceKey<EquipmentAsset> id) {
/* 31 */     return this.equipmentAssets.getOrDefault(id, MISSING);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/EquipmentAssetManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */