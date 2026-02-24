/*     */ package net.minecraft.client.renderer.entity.layers;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.io.IOException;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.VillagerLikeModel;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.VillagerDataHolderRenderState;
/*     */ import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
/*     */ import net.minecraft.client.resources.metadata.animation.VillagerMetadataSection;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerData;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerType;
/*     */ 
/*     */ public class VillagerProfessionLayer<S extends LivingEntityRenderState & VillagerDataHolderRenderState, M extends net.minecraft.client.model.EntityModel<S> & VillagerLikeModel> extends RenderLayer<S, M> {
/*     */   static {
/*  31 */     LEVEL_LOCATIONS = (Int2ObjectMap<Identifier>)Util.make(new Int2ObjectOpenHashMap(), map -> {
/*     */           map.put(1, Identifier.withDefaultNamespace("stone"));
/*     */           map.put(2, Identifier.withDefaultNamespace("iron"));
/*     */           map.put(3, Identifier.withDefaultNamespace("gold"));
/*     */           map.put(4, Identifier.withDefaultNamespace("emerald"));
/*     */           map.put(5, Identifier.withDefaultNamespace("diamond"));
/*     */         });
/*     */   }
/*  39 */   private static final Int2ObjectMap<Identifier> LEVEL_LOCATIONS; private final Object2ObjectMap<ResourceKey<VillagerType>, VillagerMetadataSection.Hat> typeHatCache = (Object2ObjectMap<ResourceKey<VillagerType>, VillagerMetadataSection.Hat>)new Object2ObjectOpenHashMap();
/*  40 */   private final Object2ObjectMap<ResourceKey<VillagerProfession>, VillagerMetadataSection.Hat> professionHatCache = (Object2ObjectMap<ResourceKey<VillagerProfession>, VillagerMetadataSection.Hat>)new Object2ObjectOpenHashMap();
/*     */   
/*     */   private final ResourceManager resourceManager;
/*     */   private final String path;
/*     */   private final M noHatModel;
/*     */   private final M noHatBabyModel;
/*     */   
/*     */   public VillagerProfessionLayer(RenderLayerParent<S, M> renderer, ResourceManager resourceManager, String path, M noHatModel, M noHatBabyModel) {
/*  48 */     super(renderer);
/*  49 */     this.resourceManager = resourceManager;
/*  50 */     this.path = path;
/*  51 */     this.noHatModel = noHatModel;
/*  52 */     this.noHatBabyModel = noHatBabyModel;
/*     */   }
/*     */ 
/*     */   
/*     */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
/*  57 */     if (((LivingEntityRenderState)state).isInvisible) {
/*     */       return;
/*     */     }
/*     */     
/*  61 */     VillagerData villagerData = ((VillagerDataHolderRenderState)state).getVillagerData();
/*  62 */     if (villagerData == null) {
/*     */       return;
/*     */     }
/*  65 */     Holder<VillagerType> type = villagerData.type();
/*  66 */     Holder<VillagerProfession> profession = villagerData.profession();
/*     */     
/*  68 */     VillagerMetadataSection.Hat typeHat = getHatData(this.typeHatCache, "type", type);
/*  69 */     VillagerMetadataSection.Hat professionHat = getHatData(this.professionHatCache, "profession", profession);
/*     */     
/*  71 */     M model = getParentModel();
/*     */     
/*  73 */     Identifier typeTexture = getIdentifier("type", type);
/*     */     
/*  75 */     boolean typeHatVisible = (professionHat == VillagerMetadataSection.Hat.NONE || (professionHat == VillagerMetadataSection.Hat.PARTIAL && typeHat != VillagerMetadataSection.Hat.FULL));
/*     */ 
/*     */     
/*  78 */     M noHatModel = ((LivingEntityRenderState)state).isBaby ? this.noHatBabyModel : this.noHatModel;
/*  79 */     renderColoredCutoutModel(typeHatVisible ? (Model<? super S>)model : (Model<? super S>)noHatModel, typeTexture, poseStack, submitNodeCollector, lightCoords, state, -1, 1);
/*     */     
/*  81 */     if (!profession.is(VillagerProfession.NONE) && !((LivingEntityRenderState)state).isBaby) {
/*  82 */       Identifier professionTexture = getIdentifier("profession", profession);
/*  83 */       renderColoredCutoutModel((Model<? super S>)model, professionTexture, poseStack, submitNodeCollector, lightCoords, state, -1, 2);
/*  84 */       if (!profession.is(VillagerProfession.NITWIT)) {
/*  85 */         Identifier professionLevelTexture = getIdentifier("profession_level", (Identifier)LEVEL_LOCATIONS.get(Mth.clamp(villagerData.level(), 1, LEVEL_LOCATIONS.size())));
/*  86 */         renderColoredCutoutModel((Model<? super S>)model, professionLevelTexture, poseStack, submitNodeCollector, lightCoords, state, -1, 3);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Identifier getIdentifier(String type, Identifier key) {
/*  92 */     return key.withPath(keyPath -> "textures/entity/" + this.path + "/" + type + "/" + type + ".png");
/*     */   }
/*     */   
/*     */   private Identifier getIdentifier(String type, Holder<?> holder) {
/*  96 */     return holder.unwrapKey().map(k -> getIdentifier(type, type.identifier())).orElse(MissingTextureAtlasSprite.getLocation());
/*     */   }
/*     */   
/*     */   public <K> VillagerMetadataSection.Hat getHatData(Object2ObjectMap<ResourceKey<K>, VillagerMetadataSection.Hat> cache, String name, Holder<K> holder) {
/* 100 */     ResourceKey<K> key = holder.unwrapKey().orElse(null);
/* 101 */     if (key == null) {
/* 102 */       return VillagerMetadataSection.Hat.NONE;
/*     */     }
/*     */     
/* 105 */     return (VillagerMetadataSection.Hat)cache.computeIfAbsent(key, k -> (VillagerMetadataSection.Hat)this.resourceManager.getResource(getIdentifier(name, name.identifier())).flatMap(()).orElse(VillagerMetadataSection.Hat.NONE));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/VillagerProfessionLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */