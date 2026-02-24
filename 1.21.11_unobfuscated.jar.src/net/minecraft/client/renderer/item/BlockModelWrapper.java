/*     */ package net.minecraft.client.renderer.item;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.color.item.ItemTintSource;
/*     */ import net.minecraft.client.color.item.ItemTintSources;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*     */ import net.minecraft.client.renderer.block.model.TextureSlots;
/*     */ import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.resources.model.ModelBaker;
/*     */ import net.minecraft.client.resources.model.ResolvableModel;
/*     */ import net.minecraft.client.resources.model.ResolvedModel;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.entity.ItemOwner;
/*     */ import net.minecraft.world.item.BlockItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class BlockModelWrapper implements ItemModel {
/*     */   private static final Function<ItemStack, RenderType> ITEM_RENDER_TYPE_GETTER = stack -> Sheets.translucentItemSheet();
/*     */   private static final Function<ItemStack, RenderType> BLOCK_RENDER_TYPE_GETTER;
/*     */   private final List<ItemTintSource> tints;
/*     */   private final List<BakedQuad> quads;
/*     */   private final java.util.function.Supplier<Vector3fc[]> extents;
/*     */   private final ModelRenderProperties properties;
/*     */   private final boolean animated;
/*     */   private final Function<ItemStack, RenderType> renderType;
/*     */   
/*     */   static {
/*  40 */     BLOCK_RENDER_TYPE_GETTER = (stack -> {
/*     */         Item patt0$temp = stack.getItem();
/*     */         if (patt0$temp instanceof BlockItem) {
/*     */           BlockItem blockItem = (BlockItem)patt0$temp;
/*     */           ChunkSectionLayer blockLayer = net.minecraft.client.renderer.ItemBlockRenderTypes.getChunkRenderType(blockItem.getBlock().defaultBlockState());
/*     */           if (blockLayer != ChunkSectionLayer.TRANSLUCENT) {
/*     */             return Sheets.cutoutBlockSheet();
/*     */           }
/*     */         } 
/*     */         return Sheets.translucentBlockItemSheet();
/*     */       });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BlockModelWrapper(List<ItemTintSource> tints, List<BakedQuad> quads, ModelRenderProperties properties, Function<ItemStack, RenderType> renderType) {
/*  59 */     this.tints = tints;
/*  60 */     this.quads = quads;
/*  61 */     this.properties = properties;
/*  62 */     this.renderType = renderType;
/*  63 */     this.extents = (java.util.function.Supplier<Vector3fc[]>)com.google.common.base.Suppliers.memoize(() -> computeExtents(this.quads));
/*     */     boolean animated = false;
/*  65 */     for (BakedQuad quad : quads) {
/*  66 */       if (quad.sprite().contents().isAnimated()) {
/*  67 */         animated = true;
/*     */         break;
/*     */       } 
/*     */     } 
/*  71 */     this.animated = animated;
/*     */   }
/*     */   
/*     */   public static Vector3fc[] computeExtents(List<BakedQuad> quads) {
/*  75 */     Set<Vector3fc> result = new java.util.HashSet<>();
/*  76 */     for (BakedQuad quad : quads) {
/*  77 */       for (int vertex = 0; vertex < 4; vertex++) {
/*  78 */         result.add(quad.position(vertex));
/*     */       }
/*     */     } 
/*  81 */     return (Vector3fc[])result.toArray(x$0 -> new Vector3fc[x$0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, ClientLevel level, ItemOwner owner, int seed) {
/*  86 */     output.appendModelIdentityElement(this);
/*  87 */     ItemStackRenderState.LayerRenderState layer = output.newLayer();
/*  88 */     if (item.hasFoil()) {
/*  89 */       ItemStackRenderState.FoilType foilType = hasSpecialAnimatedTexture(item) ? ItemStackRenderState.FoilType.SPECIAL : ItemStackRenderState.FoilType.STANDARD;
/*  90 */       layer.setFoilType(foilType);
/*  91 */       output.setAnimated();
/*  92 */       output.appendModelIdentityElement(foilType);
/*     */     } 
/*     */     
/*  95 */     int activeTints = this.tints.size();
/*  96 */     int[] tintLayers = layer.prepareTintLayers(activeTints);
/*     */     
/*  98 */     for (int i = 0; i < activeTints; i++) {
/*  99 */       int tint = ((ItemTintSource)this.tints.get(i)).calculate(item, level, (owner == null) ? null : owner.asLivingEntity());
/* 100 */       tintLayers[i] = tint;
/* 101 */       output.appendModelIdentityElement(tint);
/*     */     } 
/*     */     
/* 104 */     layer.setExtents(this.extents);
/*     */     
/* 106 */     layer.setRenderType(this.renderType.apply(item));
/* 107 */     this.properties.applyToLayer(layer, displayContext);
/* 108 */     layer.prepareQuadList().addAll(this.quads);
/* 109 */     if (this.animated) {
/* 110 */       output.setAnimated();
/*     */     }
/*     */   }
/*     */   
/*     */   private static Function<ItemStack, RenderType> detectRenderType(List<BakedQuad> quads) {
/* 115 */     Iterator<BakedQuad> quadIterator = quads.iterator();
/*     */     
/* 117 */     if (!quadIterator.hasNext()) {
/* 118 */       return ITEM_RENDER_TYPE_GETTER;
/*     */     }
/*     */ 
/*     */     
/* 122 */     Identifier expectedAtlas = ((BakedQuad)quadIterator.next()).sprite().atlasLocation();
/* 123 */     while (quadIterator.hasNext()) {
/* 124 */       BakedQuad quad = quadIterator.next();
/* 125 */       Identifier quadAtlas = quad.sprite().atlasLocation();
/* 126 */       if (!quadAtlas.equals(expectedAtlas)) {
/* 127 */         throw new IllegalStateException("Multiple atlases used in model, expected " + String.valueOf(expectedAtlas) + ", but also got " + String.valueOf(quadAtlas));
/*     */       }
/*     */     } 
/*     */     
/* 131 */     if (expectedAtlas.equals(TextureAtlas.LOCATION_ITEMS)) {
/* 132 */       return ITEM_RENDER_TYPE_GETTER;
/*     */     }
/*     */     
/* 135 */     if (expectedAtlas.equals(TextureAtlas.LOCATION_BLOCKS)) {
/* 136 */       return BLOCK_RENDER_TYPE_GETTER;
/*     */     }
/*     */     
/* 139 */     throw new IllegalArgumentException("Atlas " + String.valueOf(expectedAtlas) + " can't be usef for item models");
/*     */   }
/*     */   
/*     */   private static boolean hasSpecialAnimatedTexture(ItemStack itemStack) {
/* 143 */     return (itemStack.is(net.minecraft.tags.ItemTags.COMPASSES) || itemStack.is(net.minecraft.world.item.Items.CLOCK));
/*     */   }
/*     */   public static final class Unbaked extends Record implements ItemModel.Unbaked { private final Identifier model; private final List<ItemTintSource> tints; public static final com.mojang.serialization.MapCodec<Unbaked> MAP_CODEC;
/* 146 */     public Unbaked(Identifier model, List<ItemTintSource> tints) { this.model = model; this.tints = tints; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/BlockModelWrapper$Unbaked;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #146	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 146 */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/BlockModelWrapper$Unbaked; } public Identifier model() { return this.model; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/BlockModelWrapper$Unbaked;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #146	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/BlockModelWrapper$Unbaked; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/BlockModelWrapper$Unbaked;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #146	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/BlockModelWrapper$Unbaked;
/* 146 */       //   0	8	1	o	Ljava/lang/Object; } public List<ItemTintSource> tints() { return this.tints; }
/*     */ 
/*     */     
/*     */     static {
/* 150 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("model").forGetter(Unbaked::model), (App)ItemTintSources.CODEC.listOf().optionalFieldOf("tints", List.of()).forGetter(Unbaked::tints)).apply((Applicative)i, Unbaked::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 157 */       resolver.markDependency(this.model);
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemModel bake(ItemModel.BakingContext context) {
/* 162 */       ModelBaker baker = context.blockModelBaker();
/*     */       
/* 164 */       ResolvedModel resolvedModel = baker.getModel(this.model);
/* 165 */       TextureSlots textureSlots = resolvedModel.getTopTextureSlots();
/* 166 */       List<BakedQuad> quads = resolvedModel.bakeTopGeometry(textureSlots, baker, (net.minecraft.client.resources.model.ModelState)net.minecraft.client.resources.model.BlockModelRotation.IDENTITY).getAll();
/* 167 */       ModelRenderProperties properties = ModelRenderProperties.fromResolvedModel(baker, resolvedModel, textureSlots);
/*     */       
/* 169 */       Function<ItemStack, RenderType> renderTypeGetter = BlockModelWrapper.detectRenderType(quads);
/* 170 */       return new BlockModelWrapper(this.tints, quads, properties, renderTypeGetter);
/*     */     }
/*     */ 
/*     */     
/*     */     public com.mojang.serialization.MapCodec<Unbaked> type() {
/* 175 */       return MAP_CODEC;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/BlockModelWrapper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */