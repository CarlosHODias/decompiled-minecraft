/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectFunction;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.renderer.block.model.ItemTransforms;
/*     */ import net.minecraft.client.renderer.block.model.TextureSlots;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelDiscovery
/*     */ {
/*  28 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  30 */   private final Object2ObjectMap<Identifier, ModelWrapper> modelWrappers = (Object2ObjectMap<Identifier, ModelWrapper>)new Object2ObjectOpenHashMap();
/*     */   
/*     */   private final ModelWrapper missingModel;
/*     */   
/*     */   private final Object2ObjectFunction<Identifier, ModelWrapper> uncachedResolver;
/*     */   
/*     */   private final ResolvableModel.Resolver resolver;
/*     */   
/*  38 */   private final Queue<ModelWrapper> parentDiscoveryQueue = new ArrayDeque<>();
/*     */   
/*     */   public ModelDiscovery(Map<Identifier, UnbakedModel> unbakedModels, UnbakedModel missingUnbakedModel) {
/*  41 */     this.missingModel = new ModelWrapper(MissingBlockModel.LOCATION, missingUnbakedModel, true);
/*  42 */     this.modelWrappers.put(MissingBlockModel.LOCATION, this.missingModel);
/*     */     
/*  44 */     this.uncachedResolver = (rawId -> {
/*     */         Identifier id = (Identifier)unbakedModels;
/*     */         
/*     */         UnbakedModel rawModel = (UnbakedModel)unbakedModels.get(id);
/*     */         
/*     */         if (rawModel == null) {
/*     */           LOGGER.warn("Missing block model: {}", id);
/*     */           return this.missingModel;
/*     */         } 
/*     */         return createAndQueueWrapper(id, rawModel);
/*     */       });
/*  55 */     this.resolver = this::getOrCreateModel;
/*     */   }
/*     */   
/*     */   private static boolean isRoot(UnbakedModel model) {
/*  59 */     return (model.parent() == null);
/*     */   }
/*     */   
/*     */   private ModelWrapper getOrCreateModel(Identifier id) {
/*  63 */     return (ModelWrapper)this.modelWrappers.computeIfAbsent(id, this.uncachedResolver);
/*     */   }
/*     */   
/*     */   private ModelWrapper createAndQueueWrapper(Identifier id, UnbakedModel rawModel) {
/*  67 */     boolean isRoot = isRoot(rawModel);
/*  68 */     ModelWrapper result = new ModelWrapper(id, rawModel, isRoot);
/*  69 */     if (!isRoot) {
/*  70 */       this.parentDiscoveryQueue.add(result);
/*     */     }
/*  72 */     return result;
/*     */   }
/*     */   
/*     */   public void addRoot(ResolvableModel model) {
/*  76 */     model.resolveDependencies(this.resolver);
/*     */   }
/*     */   
/*     */   public void addSpecialModel(Identifier id, UnbakedModel model) {
/*  80 */     if (!isRoot(model)) {
/*     */       
/*  82 */       LOGGER.warn("Trying to add non-root special model {}, ignoring", id);
/*     */       return;
/*     */     } 
/*  85 */     ModelWrapper previous = (ModelWrapper)this.modelWrappers.put(id, createAndQueueWrapper(id, model));
/*  86 */     if (previous != null) {
/*  87 */       LOGGER.warn("Duplicate special model {}", id);
/*     */     }
/*     */   }
/*     */   
/*     */   public ResolvedModel missingModel() {
/*  92 */     return this.missingModel;
/*     */   }
/*     */   
/*     */   public Map<Identifier, ResolvedModel> resolve() {
/*  96 */     List<ModelWrapper> toValidate = new ArrayList<>();
/*     */     
/*  98 */     discoverDependencies(toValidate);
/*     */ 
/*     */     
/* 101 */     propagateValidity(toValidate);
/*     */     
/* 103 */     ImmutableMap.Builder<Identifier, ResolvedModel> result = ImmutableMap.builder();
/* 104 */     this.modelWrappers.forEach((location, model) -> {
/*     */           if (model.valid) {
/*     */             result.put(location, model);
/*     */           } else {
/*     */             LOGGER.warn("Model {} ignored due to cyclic dependency", location);
/*     */           } 
/*     */         });
/* 111 */     return (Map<Identifier, ResolvedModel>)result.build();
/*     */   }
/*     */   
/*     */   private void discoverDependencies(List<ModelWrapper> toValidate) {
/*     */     ModelWrapper current;
/* 116 */     while ((current = this.parentDiscoveryQueue.poll()) != null) {
/*     */       
/* 118 */       Identifier parentLocation = Objects.<Identifier>requireNonNull(current.wrapped.parent());
/* 119 */       ModelWrapper parent = getOrCreateModel(parentLocation);
/* 120 */       current.parent = parent;
/* 121 */       if (parent.valid) {
/*     */         
/* 123 */         current.valid = true; continue;
/*     */       } 
/* 125 */       toValidate.add(current);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void propagateValidity(List<ModelWrapper> toValidate) {
/*     */     boolean progressed = true;
/* 132 */     while (progressed) {
/* 133 */       progressed = false;
/* 134 */       Iterator<ModelWrapper> iterator = toValidate.iterator();
/* 135 */       while (iterator.hasNext()) {
/* 136 */         ModelWrapper model = iterator.next();
/*     */         
/* 138 */         if (((ModelWrapper)Objects.requireNonNull((T)model.parent)).valid) {
/* 139 */           model.valid = true;
/* 140 */           iterator.remove();
/* 141 */           progressed = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private static final class Slot<T> extends Record { private final int index;
/* 147 */     private Slot(int index) { this.index = index; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ModelDiscovery$Slot;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #147	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelDiscovery$Slot;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 147 */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelDiscovery$Slot<TT;>; } public int index() { return this.index; } public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ModelDiscovery$Slot;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #147	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelDiscovery$Slot;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelDiscovery$Slot<TT;>;
/*     */     } public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ModelDiscovery$Slot;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #147	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/ModelDiscovery$Slot;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/ModelDiscovery$Slot<TT;>;
/* 151 */     } } private static class ModelWrapper implements ResolvedModel { private static final ModelDiscovery.Slot<Boolean> KEY_AMBIENT_OCCLUSION = slot(0);
/* 152 */     private static final ModelDiscovery.Slot<UnbakedModel.GuiLight> KEY_GUI_LIGHT = slot(1);
/* 153 */     private static final ModelDiscovery.Slot<UnbakedGeometry> KEY_GEOMETRY = slot(2);
/* 154 */     private static final ModelDiscovery.Slot<ItemTransforms> KEY_TRANSFORMS = slot(3);
/* 155 */     private static final ModelDiscovery.Slot<TextureSlots> KEY_TEXTURE_SLOTS = slot(4);
/*     */ 
/*     */     
/* 158 */     private static final ModelDiscovery.Slot<TextureAtlasSprite> KEY_PARTICLE_SPRITE = slot(5);
/* 159 */     private static final ModelDiscovery.Slot<QuadCollection> KEY_DEFAULT_GEOMETRY = slot(6);
/*     */     
/*     */     private static final int SLOT_COUNT = 7;
/*     */     
/*     */     private final Identifier id;
/*     */     private boolean valid;
/*     */     private ModelWrapper parent;
/*     */     private final UnbakedModel wrapped;
/*     */     
/*     */     private static <T> ModelDiscovery.Slot<T> slot(int index) {
/* 169 */       Objects.checkIndex(index, 7);
/* 170 */       return new ModelDiscovery.Slot<>(index);
/*     */     }
/*     */     
/* 173 */     private final AtomicReferenceArray<Object> fixedSlots = new AtomicReferenceArray(7);
/*     */     
/* 175 */     private final Map<ModelState, QuadCollection> modelBakeCache = new ConcurrentHashMap<>();
/*     */     
/*     */     private ModelWrapper(Identifier id, UnbakedModel wrapped, boolean valid) {
/* 178 */       this.id = id;
/* 179 */       this.wrapped = wrapped;
/* 180 */       this.valid = valid;
/*     */     }
/*     */ 
/*     */     
/*     */     public UnbakedModel wrapped() {
/* 185 */       return this.wrapped;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResolvedModel parent() {
/* 190 */       return this.parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public String debugName() {
/* 195 */       return this.id.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     private <T> T getSlot(ModelDiscovery.Slot<T> key) {
/* 200 */       return (T)this.fixedSlots.get(key.index);
/*     */     }
/*     */ 
/*     */     
/*     */     private <T> T updateSlot(ModelDiscovery.Slot<T> key, T value) {
/* 205 */       T currentValue = (T)this.fixedSlots.compareAndExchange(key.index, null, value);
/* 206 */       if (currentValue == null)
/*     */       {
/* 208 */         return value;
/*     */       }
/*     */       
/* 211 */       return currentValue;
/*     */     }
/*     */     
/*     */     private <T> T getSimpleProperty(ModelDiscovery.Slot<T> key, Function<ResolvedModel, T> getter) {
/* 215 */       T result = getSlot(key);
/* 216 */       if (result != null) {
/* 217 */         return result;
/*     */       }
/* 219 */       return updateSlot(key, getter.apply(this));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean getTopAmbientOcclusion() {
/* 224 */       return (Boolean)getSimpleProperty(KEY_AMBIENT_OCCLUSION, ResolvedModel::findTopAmbientOcclusion);
/*     */     }
/*     */ 
/*     */     
/*     */     public UnbakedModel.GuiLight getTopGuiLight() {
/* 229 */       return getSimpleProperty(KEY_GUI_LIGHT, ResolvedModel::findTopGuiLight);
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemTransforms getTopTransforms() {
/* 234 */       return getSimpleProperty(KEY_TRANSFORMS, ResolvedModel::findTopTransforms);
/*     */     }
/*     */ 
/*     */     
/*     */     public UnbakedGeometry getTopGeometry() {
/* 239 */       return getSimpleProperty(KEY_GEOMETRY, ResolvedModel::findTopGeometry);
/*     */     }
/*     */ 
/*     */     
/*     */     public TextureSlots getTopTextureSlots() {
/* 244 */       return getSimpleProperty(KEY_TEXTURE_SLOTS, ResolvedModel::findTopTextureSlots);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public TextureAtlasSprite resolveParticleSprite(TextureSlots textureSlots, ModelBaker baker) {
/* 250 */       TextureAtlasSprite result = getSlot(KEY_PARTICLE_SPRITE);
/* 251 */       if (result != null) {
/* 252 */         return result;
/*     */       }
/* 254 */       return updateSlot(KEY_PARTICLE_SPRITE, ResolvedModel.resolveParticleSprite(textureSlots, baker, this));
/*     */     }
/*     */ 
/*     */     
/*     */     private QuadCollection bakeDefaultState(TextureSlots textureSlots, ModelBaker baker, ModelState state) {
/* 259 */       QuadCollection result = getSlot(KEY_DEFAULT_GEOMETRY);
/* 260 */       if (result != null) {
/* 261 */         return result;
/*     */       }
/* 263 */       return updateSlot(KEY_DEFAULT_GEOMETRY, getTopGeometry().bake(textureSlots, baker, state, this));
/*     */     }
/*     */ 
/*     */     
/*     */     public QuadCollection bakeTopGeometry(TextureSlots textureSlots, ModelBaker baker, ModelState state) {
/* 268 */       if (state == BlockModelRotation.IDENTITY) {
/* 269 */         return bakeDefaultState(textureSlots, baker, state);
/*     */       }
/*     */ 
/*     */       
/* 273 */       return this.modelBakeCache.computeIfAbsent(state, s -> {
/*     */             UnbakedGeometry topGeometry = getTopGeometry();
/*     */             return topGeometry.bake(textureSlots, textureSlots, baker, this);
/*     */           });
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/ModelDiscovery.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */