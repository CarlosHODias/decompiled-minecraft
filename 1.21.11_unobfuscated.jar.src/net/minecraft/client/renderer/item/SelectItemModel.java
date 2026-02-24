/*     */ package net.minecraft.client.renderer.item;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.client.multiplayer.CacheSlot;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
/*     */ import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
/*     */ import net.minecraft.client.resources.model.ResolvableModel;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RegistryContextSwapper;
/*     */ import net.minecraft.world.entity.ItemOwner;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class SelectItemModel<T> implements ItemModel {
/*     */   private final SelectItemModelProperty<T> property;
/*     */   private final ModelSelector<T> models;
/*     */   
/*     */   public SelectItemModel(SelectItemModelProperty<T> property, ModelSelector<T> models) {
/*  28 */     this.property = property;
/*  29 */     this.models = models;
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, ClientLevel level, ItemOwner owner, int seed) {
/*  34 */     output.appendModelIdentityElement(this);
/*  35 */     T value = (T)this.property.get(item, level, (owner == null) ? null : owner.asLivingEntity(), seed, displayContext);
/*  36 */     ItemModel model = this.models.get(value, level);
/*  37 */     if (model != null)
/*  38 */       model.update(output, item, resolver, displayContext, level, owner, seed); 
/*     */   }
/*     */   public static final class Unbaked extends Record implements ItemModel.Unbaked { private final SelectItemModel.UnbakedSwitch<?, ?> unbakedSwitch; private final Optional<ItemModel.Unbaked> fallback; public static final MapCodec<Unbaked> MAP_CODEC;
/*     */     
/*  42 */     public Unbaked(SelectItemModel.UnbakedSwitch<?, ?> unbakedSwitch, Optional<ItemModel.Unbaked> fallback) { this.unbakedSwitch = unbakedSwitch; this.fallback = fallback; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/SelectItemModel$Unbaked;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  42 */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$Unbaked; } public SelectItemModel.UnbakedSwitch<?, ?> unbakedSwitch() { return this.unbakedSwitch; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/SelectItemModel$Unbaked;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$Unbaked; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/SelectItemModel$Unbaked;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$Unbaked;
/*  42 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<ItemModel.Unbaked> fallback() { return this.fallback; }
/*     */ 
/*     */     
/*     */     static {
/*  46 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SelectItemModel.UnbakedSwitch.MAP_CODEC.forGetter(Unbaked::unbakedSwitch), (App)ItemModels.CODEC.optionalFieldOf("fallback").forGetter(Unbaked::fallback)).apply((Applicative)i, Unbaked::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MapCodec<Unbaked> type() {
/*  54 */       return MAP_CODEC;
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemModel bake(ItemModel.BakingContext context) {
/*  59 */       ItemModel bakedFallback = this.fallback.<ItemModel>map(m -> m.bake(context)).orElse(context.missingItemModel());
/*  60 */       return this.unbakedSwitch.bake(context, bakedFallback);
/*     */     }
/*     */ 
/*     */     
/*     */     public void resolveDependencies(ResolvableModel.Resolver resolver) {
/*  65 */       this.unbakedSwitch.resolveDependencies(resolver);
/*  66 */       this.fallback.ifPresent(m -> m.resolveDependencies(resolver));
/*     */     } }
/*     */   public static final class UnbakedSwitch<P extends SelectItemModelProperty<T>, T> extends Record { private final P property; private final List<SelectItemModel.SwitchCase<T>> cases; public static final MapCodec<UnbakedSwitch<?, ?>> MAP_CODEC;
/*     */     
/*  70 */     public UnbakedSwitch(P property, List<SelectItemModel.SwitchCase<T>> cases) { this.property = property; this.cases = cases; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch<TP;TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch<TP;TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  70 */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch<TP;TT;>; } public P property() { return this.property; } public List<SelectItemModel.SwitchCase<T>> cases() { return this.cases; }
/*     */ 
/*     */     
/*     */     static {
/*  74 */       MAP_CODEC = SelectItemModelProperties.CODEC.dispatchMap("property", unbaked -> unbaked.property().type(), SelectItemModelProperty.Type::switchCodec);
/*     */     }
/*     */     public ItemModel bake(ItemModel.BakingContext context, ItemModel fallback) {
/*  77 */       Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap();
/*  78 */       for (SelectItemModel.SwitchCase<T> c : this.cases) {
/*  79 */         ItemModel.Unbaked caseModel = c.model;
/*     */         
/*  81 */         ItemModel bakedCaseModel = caseModel.bake(context);
/*  82 */         for (T value : c.values) {
/*  83 */           object2ObjectOpenHashMap.put(value, bakedCaseModel);
/*     */         }
/*     */       } 
/*     */       
/*  87 */       object2ObjectOpenHashMap.defaultReturnValue(fallback);
/*  88 */       return new SelectItemModel<>((SelectItemModelProperty<T>)this.property, createModelGetter((Object2ObjectMap<T, ItemModel>)object2ObjectOpenHashMap, context.contextSwapper()));
/*     */     }
/*     */     
/*     */     private SelectItemModel.ModelSelector<T> createModelGetter(Object2ObjectMap<T, ItemModel> originalModels, RegistryContextSwapper registrySwapper) {
/*  92 */       if (registrySwapper == null) {
/*  93 */         return (value, context) -> (ItemModel)originalModels.get(value);
/*     */       }
/*  95 */       ItemModel defaultModel = (ItemModel)originalModels.defaultReturnValue();
/*  96 */       CacheSlot<ClientLevel, Object2ObjectMap<T, ItemModel>> remappedModelCache = new CacheSlot(clientLevel -> {
/*     */             Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap(originalModels.size());
/*     */             
/*     */             object2ObjectOpenHashMap.defaultReturnValue(originalModels);
/*     */             
/*     */             originalModels.forEach(());
/*     */             
/*     */             return object2ObjectOpenHashMap;
/*     */           });
/*     */       
/* 106 */       return (value, context) -> (context == null) ? (ItemModel)originalModels.get(value) : ((value == null) ? defaultModel : (ItemModel)((Object2ObjectMap)remappedModelCache.compute((CacheSlot.Cleaner)context)).get(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 120 */       for (SelectItemModel.SwitchCase<?> c : this.cases)
/* 121 */         c.model.resolveDependencies(resolver); 
/*     */     } }
/*     */   public static final class SwitchCase<T> extends Record { private final List<T> values;
/*     */     private final ItemModel.Unbaked model;
/*     */     
/* 126 */     public SwitchCase(List<T> values, ItemModel.Unbaked model) { this.values = values; this.model = model; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #126	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #126	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #126	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 126 */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase<TT;>; } public List<T> values() { return this.values; } public ItemModel.Unbaked model() { return this.model; }
/*     */ 
/*     */ 
/*     */     
/*     */     public static <T> Codec<SwitchCase<T>> codec(Codec<T> valueCodec) {
/* 131 */       return RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.nonEmptyList(ExtraCodecs.compactListCodec(valueCodec)).fieldOf("when").forGetter(SwitchCase::values), (App)ItemModels.CODEC.fieldOf("model").forGetter(SwitchCase::model)).apply((Applicative)i, SwitchCase::new));
/*     */     } }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ModelSelector<T> {
/*     */     ItemModel get(T param1T, ClientLevel param1ClientLevel);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/SelectItemModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */