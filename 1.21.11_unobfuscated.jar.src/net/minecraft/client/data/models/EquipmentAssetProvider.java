/*     */ package net.minecraft.client.data.models;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.equipment.EquipmentAsset;
/*     */ import net.minecraft.world.item.equipment.EquipmentAssets;
/*     */ 
/*     */ public class EquipmentAssetProvider implements DataProvider {
/*     */   private final PackOutput.PathProvider pathProvider;
/*     */   
/*     */   public EquipmentAssetProvider(PackOutput output) {
/*  23 */     this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
/*     */   }
/*     */   
/*     */   private static void bootstrap(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> consumer) {
/*  27 */     consumer.accept(EquipmentAssets.LEATHER, EquipmentClientInfo.builder()
/*  28 */         .addHumanoidLayers(Identifier.withDefaultNamespace("leather"), true)
/*  29 */         .addHumanoidLayers(Identifier.withDefaultNamespace("leather_overlay"), false)
/*  30 */         .addLayers(EquipmentClientInfo.LayerType.HORSE_BODY, new EquipmentClientInfo.Layer[] {
/*  31 */             EquipmentClientInfo.Layer.leatherDyeable(Identifier.withDefaultNamespace("leather"), true), 
/*  32 */             EquipmentClientInfo.Layer.leatherDyeable(Identifier.withDefaultNamespace("leather_overlay"), false)
/*  33 */           }).build());
/*     */     
/*  35 */     consumer.accept(EquipmentAssets.CHAINMAIL, onlyHumanoid("chainmail"));
/*  36 */     consumer.accept(EquipmentAssets.COPPER, humanoidAndMountArmor("copper"));
/*  37 */     consumer.accept(EquipmentAssets.IRON, humanoidAndMountArmor("iron"));
/*  38 */     consumer.accept(EquipmentAssets.GOLD, humanoidAndMountArmor("gold"));
/*  39 */     consumer.accept(EquipmentAssets.DIAMOND, humanoidAndMountArmor("diamond"));
/*  40 */     consumer.accept(EquipmentAssets.TURTLE_SCUTE, EquipmentClientInfo.builder()
/*  41 */         .addMainHumanoidLayer(Identifier.withDefaultNamespace("turtle_scute"), false)
/*  42 */         .build());
/*     */     
/*  44 */     consumer.accept(EquipmentAssets.NETHERITE, humanoidAndMountArmor("netherite"));
/*  45 */     consumer.accept(EquipmentAssets.ARMADILLO_SCUTE, EquipmentClientInfo.builder()
/*  46 */         .addLayers(EquipmentClientInfo.LayerType.WOLF_BODY, new EquipmentClientInfo.Layer[] { EquipmentClientInfo.Layer.onlyIfDyed(Identifier.withDefaultNamespace("armadillo_scute"), false)
/*  47 */           }).addLayers(EquipmentClientInfo.LayerType.WOLF_BODY, new EquipmentClientInfo.Layer[] { EquipmentClientInfo.Layer.onlyIfDyed(Identifier.withDefaultNamespace("armadillo_scute_overlay"), true)
/*  48 */           }).build());
/*     */     
/*  50 */     consumer.accept(EquipmentAssets.ELYTRA, EquipmentClientInfo.builder()
/*  51 */         .addLayers(EquipmentClientInfo.LayerType.WINGS, new EquipmentClientInfo.Layer[] { new EquipmentClientInfo.Layer(Identifier.withDefaultNamespace("elytra"), Optional.empty(), true)
/*  52 */           }).build());
/*     */ 
/*     */     
/*  55 */     EquipmentClientInfo.Layer saddleLayer = new EquipmentClientInfo.Layer(Identifier.withDefaultNamespace("saddle"));
/*  56 */     consumer.accept(EquipmentAssets.SADDLE, EquipmentClientInfo.builder()
/*  57 */         .addLayers(EquipmentClientInfo.LayerType.PIG_SADDLE, new EquipmentClientInfo.Layer[] { saddleLayer
/*  58 */           }).addLayers(EquipmentClientInfo.LayerType.STRIDER_SADDLE, new EquipmentClientInfo.Layer[] { saddleLayer
/*  59 */           }).addLayers(EquipmentClientInfo.LayerType.CAMEL_SADDLE, new EquipmentClientInfo.Layer[] { saddleLayer
/*  60 */           }).addLayers(EquipmentClientInfo.LayerType.CAMEL_HUSK_SADDLE, new EquipmentClientInfo.Layer[] { saddleLayer
/*  61 */           }).addLayers(EquipmentClientInfo.LayerType.HORSE_SADDLE, new EquipmentClientInfo.Layer[] { saddleLayer
/*  62 */           }).addLayers(EquipmentClientInfo.LayerType.DONKEY_SADDLE, new EquipmentClientInfo.Layer[] { saddleLayer
/*  63 */           }).addLayers(EquipmentClientInfo.LayerType.MULE_SADDLE, new EquipmentClientInfo.Layer[] { saddleLayer
/*  64 */           }).addLayers(EquipmentClientInfo.LayerType.SKELETON_HORSE_SADDLE, new EquipmentClientInfo.Layer[] { saddleLayer
/*  65 */           }).addLayers(EquipmentClientInfo.LayerType.ZOMBIE_HORSE_SADDLE, new EquipmentClientInfo.Layer[] { saddleLayer
/*  66 */           }).addLayers(EquipmentClientInfo.LayerType.NAUTILUS_SADDLE, new EquipmentClientInfo.Layer[] { saddleLayer
/*  67 */           }).build());
/*     */ 
/*     */     
/*  70 */     for (Map.Entry<DyeColor, ResourceKey<EquipmentAsset>> entry : (Iterable<Map.Entry<DyeColor, ResourceKey<EquipmentAsset>>>)EquipmentAssets.HARNESSES.entrySet()) {
/*  71 */       DyeColor color = entry.getKey();
/*  72 */       ResourceKey<EquipmentAsset> id = entry.getValue();
/*  73 */       consumer.accept(id, EquipmentClientInfo.builder()
/*  74 */           .addLayers(EquipmentClientInfo.LayerType.HAPPY_GHAST_BODY, new EquipmentClientInfo.Layer[] { EquipmentClientInfo.Layer.onlyIfDyed(Identifier.withDefaultNamespace(color.getSerializedName() + "_harness"), false)
/*  75 */             }).build());
/*     */     } 
/*     */ 
/*     */     
/*  79 */     for (Map.Entry<DyeColor, ResourceKey<EquipmentAsset>> entry : (Iterable<Map.Entry<DyeColor, ResourceKey<EquipmentAsset>>>)EquipmentAssets.CARPETS.entrySet()) {
/*  80 */       DyeColor color = entry.getKey();
/*  81 */       ResourceKey<EquipmentAsset> id = entry.getValue();
/*  82 */       consumer.accept(id, EquipmentClientInfo.builder()
/*  83 */           .addLayers(EquipmentClientInfo.LayerType.LLAMA_BODY, new EquipmentClientInfo.Layer[] { new EquipmentClientInfo.Layer(Identifier.withDefaultNamespace(color.getSerializedName()))
/*  84 */             }).build());
/*     */     } 
/*     */     
/*  87 */     consumer.accept(EquipmentAssets.TRADER_LLAMA, EquipmentClientInfo.builder()
/*  88 */         .addLayers(EquipmentClientInfo.LayerType.LLAMA_BODY, new EquipmentClientInfo.Layer[] { new EquipmentClientInfo.Layer(Identifier.withDefaultNamespace("trader_llama"))
/*  89 */           }).build());
/*     */   }
/*     */ 
/*     */   
/*     */   private static EquipmentClientInfo onlyHumanoid(String name) {
/*  94 */     return EquipmentClientInfo.builder()
/*  95 */       .addHumanoidLayers(Identifier.withDefaultNamespace(name))
/*  96 */       .build();
/*     */   }
/*     */   
/*     */   private static EquipmentClientInfo humanoidAndMountArmor(String name) {
/* 100 */     return EquipmentClientInfo.builder()
/* 101 */       .addHumanoidLayers(Identifier.withDefaultNamespace(name))
/* 102 */       .addLayers(EquipmentClientInfo.LayerType.HORSE_BODY, new EquipmentClientInfo.Layer[] { EquipmentClientInfo.Layer.leatherDyeable(Identifier.withDefaultNamespace(name), false)
/* 103 */         }).addLayers(EquipmentClientInfo.LayerType.NAUTILUS_BODY, new EquipmentClientInfo.Layer[] { EquipmentClientInfo.Layer.leatherDyeable(Identifier.withDefaultNamespace(name), false)
/* 104 */         }).build();
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<?> run(CachedOutput cache) {
/* 109 */     Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> equipmentAssets = new HashMap<>();
/* 110 */     bootstrap((id, asset) -> {
/*     */           if (equipmentAssets.putIfAbsent(id, asset) != null) {
/*     */             throw new IllegalStateException("Tried to register equipment asset twice for id: " + String.valueOf(id));
/*     */           }
/*     */         });
/* 115 */     Objects.requireNonNull(this.pathProvider); return DataProvider.saveAll(cache, EquipmentClientInfo.CODEC, this.pathProvider::json, equipmentAssets);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 120 */     return "Equipment Asset Definitions";
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/models/EquipmentAssetProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */