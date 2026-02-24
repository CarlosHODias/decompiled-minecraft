/*    */ package net.minecraft.client.renderer.item;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.HashSet;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.block.model.TextureSlots;
/*    */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*    */ import net.minecraft.client.renderer.special.SpecialModelRenderers;
/*    */ import net.minecraft.client.resources.model.ModelBaker;
/*    */ import net.minecraft.client.resources.model.ResolvableModel;
/*    */ import net.minecraft.client.resources.model.ResolvedModel;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class SpecialModelWrapper<T> implements ItemModel {
/*    */   private final SpecialModelRenderer<T> specialRenderer;
/*    */   private final ModelRenderProperties properties;
/*    */   private final Supplier<Vector3fc[]> extents;
/*    */   
/*    */   public SpecialModelWrapper(SpecialModelRenderer<T> specialRenderer, ModelRenderProperties properties) {
/* 29 */     this.specialRenderer = specialRenderer;
/* 30 */     this.properties = properties;
/*    */     
/* 32 */     this.extents = (Supplier<Vector3fc[]>)com.google.common.base.Suppliers.memoize(() -> {
/*    */           Set<Vector3fc> results = new HashSet<>();
/*    */           Objects.requireNonNull(results);
/*    */           specialRenderer.getExtents(results::add);
/*    */           return results.<Vector3fc>toArray(new Vector3fc[0]);
/*    */         });
/*    */   }
/*    */   
/*    */   public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, ClientLevel level, net.minecraft.world.entity.ItemOwner owner, int seed) {
/* 41 */     output.appendModelIdentityElement(this);
/* 42 */     ItemStackRenderState.LayerRenderState layer = output.newLayer();
/* 43 */     if (item.hasFoil()) {
/* 44 */       ItemStackRenderState.FoilType foilType = ItemStackRenderState.FoilType.STANDARD;
/* 45 */       layer.setFoilType(foilType);
/* 46 */       output.setAnimated();
/* 47 */       output.appendModelIdentityElement(foilType);
/*    */     } 
/*    */     
/* 50 */     T argument = (T)this.specialRenderer.extractArgument(item);
/* 51 */     layer.setExtents(this.extents);
/* 52 */     layer.setupSpecialModel(this.specialRenderer, argument);
/* 53 */     if (argument != null) {
/* 54 */       output.appendModelIdentityElement(argument);
/*    */     }
/* 56 */     this.properties.applyToLayer(layer, displayContext);
/*    */   }
/*    */   public static final class Unbaked extends Record implements ItemModel.Unbaked { private final Identifier base; private final SpecialModelRenderer.Unbaked specialModel; public static final MapCodec<Unbaked> MAP_CODEC;
/* 59 */     public Unbaked(Identifier base, SpecialModelRenderer.Unbaked specialModel) { this.base = base; this.specialModel = specialModel; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/SpecialModelWrapper$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #59	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 59 */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SpecialModelWrapper$Unbaked; } public Identifier base() { return this.base; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/SpecialModelWrapper$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #59	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SpecialModelWrapper$Unbaked; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/SpecialModelWrapper$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #59	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/SpecialModelWrapper$Unbaked;
/* 59 */       //   0	8	1	o	Ljava/lang/Object; } public SpecialModelRenderer.Unbaked specialModel() { return this.specialModel; }
/*    */ 
/*    */     
/*    */     static {
/* 63 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("base").forGetter(Unbaked::base), (App)SpecialModelRenderers.CODEC.fieldOf("model").forGetter(Unbaked::specialModel)).apply((Applicative)i, Unbaked::new));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 70 */       resolver.markDependency(this.base);
/*    */     }
/*    */ 
/*    */     
/*    */     public ItemModel bake(ItemModel.BakingContext context) {
/* 75 */       SpecialModelRenderer<?> bakedSpecialModel = this.specialModel.bake(context);
/* 76 */       if (bakedSpecialModel == null) {
/* 77 */         return context.missingItemModel();
/*    */       }
/* 79 */       ModelRenderProperties properties = getProperties(context);
/* 80 */       return new SpecialModelWrapper(bakedSpecialModel, properties);
/*    */     }
/*    */     
/*    */     private ModelRenderProperties getProperties(ItemModel.BakingContext context) {
/* 84 */       ModelBaker baker = context.blockModelBaker();
/*    */       
/* 86 */       ResolvedModel model = baker.getModel(this.base);
/* 87 */       TextureSlots textureSlots = model.getTopTextureSlots();
/* 88 */       return ModelRenderProperties.fromResolvedModel(baker, model, textureSlots);
/*    */     }
/*    */ 
/*    */     
/*    */     public MapCodec<Unbaked> type() {
/* 93 */       return MAP_CODEC;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/SpecialModelWrapper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */